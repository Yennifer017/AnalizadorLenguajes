
package lenguajes.proyectolenguajesydl.parser;

import java.util.ArrayList;
import java.util.List;
import lenguajes.proyectolenguajesydl.lexer.Lexer;
import lenguajes.proyectolenguajesydl.lexer.Token;
import lenguajes.proyectolenguajesydl.parser.elements.Assignation;
import lenguajes.proyectolenguajesydl.parser.elements.Function;
import lenguajes.proyectolenguajesydl.util.Position;


/**
 *
 * @author yenni
 */
public class Registrador {
    private List<Assignation> assignations;
    private List<Registro> registros;
    private Parser parser;
    private Lexer lexer;
    private Separator separator;
    public Registrador(Parser parser, Lexer lexer){
        this.registros = new ArrayList<>();
        this.parser = parser;
        this.lexer = lexer;
        this.separator = new Separator();
        assignations = new ArrayList<>();
    }

    public List<Token> getListTokens(){
        return lexer.getTokens();
    }
    protected void addRegistro(int identationLevel, int indexInList){
        registros.add(new Registro(identationLevel, indexInList));
      
    }
   
    protected void clear(){
        registros.clear();
    }
    protected void empaquetarDatos(){
        if(parser.getErrors().isEmpty()){
            List<Token> tokens = lexer.getTokens();
            for (Registro reg : registros) {
                String typeTkn = tokens.get(reg.getIndexInList()).getSubType();
                String name = "error inesperado";
                switch (typeTkn) {
                    case "def" ->
                        name = "definicion de la funcion '" + 
                                tokens.get(reg.getIndexInList()+1).getContenido() +  "'";
                    case "if", "for", "else", "elif", "while" ->
                        name = "bloque " + typeTkn;
                    case "break", "return" ->
                        name = "sentencia" + typeTkn;
                    case "Identificador" -> {
                        this.addAssignations(reg, tokens, assignations);
                        try {
                            if (tokens.get(reg.getIndexInList() + 1).getSubType().equals("pL")) {
                                name = "Uso de la funcion " + tokens.get(reg.getIndexInList()).getContenido();
                            } else {
                                name = "Asignacion";
                            }
                        } catch (IndexOutOfBoundsException e) {
                            System.out.println("algo salio mal");
                        }
                        
                    }
                    default -> {
                        System.out.println("Algo que no debia salir mal salio mal");
                        
                    }
                }
                reg.setName(name);
            }
        }
    }
    public List<Registro> getRegistros() throws SyntaxException{
        if(parser.getErrors().isEmpty()){
            return this.registros;
        }else{
            throw new SyntaxException();
        }
    }
    
    public List<Assignation> getDataForSymbolTable() throws SyntaxException{
        //advertencias.clear();
        if(parser.getErrors().isEmpty()){
            /*List<Token> tokens = lexer.getTokens();
            //List<Registro> registrosST = new ArrayList<>();
            List<Assignation> assignations = new ArrayList<>();
            for (Registro registro : registros) {
                String subType = tokens.get(registro.getIndexInList()).getSubType();
                if(subType.equals("Identificador")){
                    this.addAssignations(registro, tokens, assignations);
                }
            }
            //mostrarAdvertencias();*/
            return assignations;
        }else{
            throw new SyntaxException();
        }
        
    }
    /*private void mostrarAdvertencias(){
        for (SyntaxError advertencia : advertencias) {
            System.out.println(advertencia.getPosition().getFila() + "-linea -- columna-" + advertencia.getPosition().getColumna());
            System.out.println("---- detalles " + advertencia.getDetails());
            System.out.println("\n");
        }
    }*/
    private void addAssignations(Registro registro, List<Token> tokens, List<Assignation> assignations){
        int init = registro.getIndexInList();
        int end = separator.findEndOfLine(tokens, init);
        List<Token> assignationTkns = tokens.subList(init, end);
        String subTypeNext = assignationTkns.get(1).getSubType();
        if(subTypeNext.contains("Asignacion")){
            evaluateSimpleAssignation(assignationTkns, registro, assignations);
        }else if(subTypeNext.equals("coma")){
            evaluateCombinateAssignation(assignationTkns, registro, assignations);
        }else if(subTypeNext.equals("pL")){
            Token inicial = assignationTkns.get(0);
            assignations.add(new Assignation(
                    registro.getIdentationLevel(), 
                    inicial.getContenido(), "Funcion",Assignation.NO_APPLY, 
                    inicial.getLine(), inicial.getColumna()
            ));
        }
        
    }
    private void evaluateSimpleAssignation(List<Token> assignationTkns, 
            Registro registro, List<Assignation> assignations){
        String subTypeNext = assignationTkns.get(1).getSubType();
        Token inicial = assignationTkns.get(0);
        String type = Assignation.UNKNOWN, value = Assignation.UNKNOWN;
        int endOfExpression = separator.findEndOfExpression(assignationTkns, 2,
                "coma", assignationTkns.get(0).getLine());
        if (subTypeNext.equals("Asignacion") && endOfExpression == 3) { //solo si es una asignacion comun
            String preType = assignationTkns.get(2).getSubType();
            type = !preType.equals("Identificador") ? preType : Assignation.UNKNOWN;
            value = !preType.equals("Identificador")
                    ? assignationTkns.get(2).getContenido() : Assignation.UNKNOWN;
        }

        assignations.add(new Assignation(
                registro.getIdentationLevel(),
                inicial.getContenido(), type, value,
                inicial.getLine(), inicial.getColumna()
        ));
        if (endOfExpression != assignationTkns.size()) {
            parser.getErrors().add(new SyntaxError(assignationTkns.get(endOfExpression).getPosition(),
                    "parametro de mas, no se puede asignar"));
        }
    }
    private void evaluateCombinateAssignation(List<Token> assignationTkns, 
            Registro registro, List<Assignation> assignations){
        List<Token> identifiers = new ArrayList<>();
        Token assignation = null;
        
        int index = 0;
        for (int i = 0; i < assignationTkns.size(); i++) {
            Token token = assignationTkns.get(i);
            if(token.getSubType().contains("Asignacion")){
                index = i+1;
                assignation = token;
                break;
            }else if(token.getSubType().equals("Identificador")){
                identifiers.add(token);
            }
        }
        int noIdentifier = 0;
        while (index<assignationTkns.size()) {            
            int end = separator.findEndOfExpression(assignationTkns, 
                    index, "coma", assignationTkns.get(0).getLine());
            
            String type = Assignation.UNKNOWN, value = Assignation.UNKNOWN;
            if (end == index + 1 && assignation.getSubType().equals("Asignacion")) { //solo si es una asignacion comun
                Token tokenEx = assignationTkns.get(index);
                type = tokenEx.getSubType().equals("Identificador") ? Assignation.UNKNOWN : tokenEx.getSubType();
                value = tokenEx.getSubType().equals("Identificador")
                        ? Assignation.UNKNOWN : tokenEx.getContenido();
            }
            
            if(noIdentifier < identifiers.size()){
                Token identifier = identifiers.get(noIdentifier);
                assignations.add(new Assignation(
                        registro.getIdentationLevel(),
                        identifier.getContenido(), type, value,
                        identifier.getLine(), identifier.getColumna()
                ));
                noIdentifier++;
            }else if(noIdentifier == identifiers.size()){      
                break;
            }else{
                index = end;
                break;
            }
            index = end;
            index = (index == assignationTkns.size())? index : (end+1);
        }
        if (index != assignationTkns.size()) {
            parser.getErrors().add(new SyntaxError(assignationTkns.get(index).getPosition(),
                    "parametro(s) de mas, no se puede asignar"));
        }else if(noIdentifier != identifiers.size()){
            Token anterior = assignationTkns.get(index-1);
            parser.getErrors().add(new SyntaxError(
                    new Position(anterior.getColumna() + anterior.length(),anterior.getLine()),
                    "falta un paramtero para asignarle a una variable")
            );
        }
    }
    
    public List<Function> getFunctions() throws SyntaxException{
        if(parser.getErrors().isEmpty()){ //solo si no hay errores sintacticos
            List<Function> functions = this.getFunctions(lexer.getTokens());
            this.setUsesFunc(functions, lexer.getTokens());
            return functions;
        }else{
            throw new SyntaxException();
        }
    }
    private List<Function> getFunctions(List<Token> tokens){
        List<Function> functions = new ArrayList<>();
        for (Registro registro : registros) {
            String typeTkn = tokens.get(registro.getIndexInList()).getSubType();
            if(typeTkn.equals("def")){
                int indexTknFunc = registro.getIndexInList(); //el token donde inicia la declaracion            
                //find params
                int i = indexTknFunc + 3;
                int endParam = separator.findEndOfExpression(tokens, i, "pR",
                        tokens.get(i).getLine());
                List<String> params = new ArrayList<>();
                while (i < endParam) {
                    if (tokens.get(i).getSubType().equals("Identificador")) {
                        params.add(tokens.get(i).getContenido());
                    }
                    i += 2;
                }
                functions.add(new Function(indexTknFunc, params));
            }
        }
        return functions;
    }
    
    
    private void setUsesFunc(List<Function> functions, List<Token> tokens){
        
        List<Integer> indexes = new ArrayList<>();
        for (int i = 0; i < tokens.size(); i++) {
            if(tokens.get(i).getSubType().equals("Identificador")){
                indexes.add(i);
            }
        }
        for (Function function : functions) {
            List<Token> usos = new ArrayList<>();
            //for (Integer index : indexes) {
            for (int i = 0; i < indexes.size(); i++) {
                Integer index = indexes.get(i);
                if(tokens.get(index).getContenido().equals(
                        tokens.get(function.getIndexStart()+1).getContenido())){
                    try {
                        String typeBeforeTkn = tokens.get(index -1).getSubType();
                        if(!typeBeforeTkn.equals("def")){
                            usos.add(tokens.get(index));
                            indexes.remove(i);
                            i--;
                        }
                    } catch (IndexOutOfBoundsException e) {
                        usos.add(tokens.get(index));
                        indexes.remove(i);
                        i--;
                    }
                }
            }
            function.setUsos(usos);
        }
    }
}
