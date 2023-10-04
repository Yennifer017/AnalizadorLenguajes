
package lenguajes.proyectolenguajesydl.parser;

import java.util.ArrayList;
import java.util.List;
import lenguajes.proyectolenguajesydl.lexer.Lexer;
import lenguajes.proyectolenguajesydl.lexer.Token;
import lenguajes.proyectolenguajesydl.parser.elements.Function;


/**
 *
 * @author yenni
 */
public class Registrador {
    
    private List<Registro> registros;
    private Parser parser;
    private Lexer lexer;
    private Separator separator;
    public Registrador(Parser parser, Lexer lexer){
        this.registros = new ArrayList<>();
        this.parser = parser;
        this.lexer = lexer;
        this.separator = new Separator();
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
