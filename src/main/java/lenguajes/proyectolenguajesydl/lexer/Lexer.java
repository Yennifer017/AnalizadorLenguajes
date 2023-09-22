
package lenguajes.proyectolenguajesydl.lexer;
import java.util.ArrayList;
import java.util.List;
import lenguajes.proyectolenguajesydl.util.Position;

/**
 *
 * @author yenni
 */
public class Lexer {
    
    int noLinea, index, columna; //noLinea == fila, index == recorido en el texto
    List<Token> tokens;
    private String lecturaTkn;
    private boolean readAll;
    private Regex ex;
    public Lexer() {
        noLinea = 0;
        tokens = new ArrayList<>();
        ex = new Regex();
    }

    public void analyze(String texto){
        tokens.clear();
        separarTokens(texto) ;
    }
    public void analyzeAll(String texto){
        tokens.clear();
        separarTokens(texto);
        for (int i = 0; i < tokens.size(); i++) {
            Token currentTkn = tokens.get(i);
            currentTkn.setSubType(getSubTypeTkn(currentTkn.getContenido(), 
                    currentTkn.getType()));
            currentTkn.setPatron(getPatron(currentTkn.getContenido(), currentTkn.getType()));
        }
    }
    public String getErrors(){
        String analisis= "";
        for (int i = 0; i < tokens.size(); i++) {
            if(tokens.get(i).getType().equalsIgnoreCase("error")){
                analisis += tokens.get(i).toString();
                analisis += "\n";
            }
        }
        if(!analisis.equals("")){
            return analisis;
        }else{
            return "No hay Errores lexicos :)";
        }
    }
    public String getReporte(){
        String analisis= "";
        for (int i = 0; i < tokens.size(); i++) {
                analisis += tokens.get(i).toString();
                analisis += "\n";
        }
        if(!analisis.equals("")){
            return analisis;
        }else{
            return "No hay tokens que mostrar";
        }
    }
    public ArrayList<Token> getTokens(){
        return (ArrayList<Token>) tokens;
    }
    public Token getToken(int index){
        return tokens.get(index);
    }
    /********************************************
     *********** SEPARACION DE TOKENS ***********
     ********************************************/
    private void separarTokens(String texto){
        readAll = false;
        lecturaTkn = "";
        index = 0;
        columna = 0;
        noLinea = 0;
        while(index < texto.length()) {
            char currentChar = texto.charAt(index);
            //countLine(currentChar);
            if(!readAll){
                evaluateChar(texto);
            }else{ //cuando se trata de una cadena de caracteres, no se tiene que analizar el mismo
                if(currentChar != '\n'){
                    lecturaTkn += currentChar;
                }
                if(currentChar == '\n'){
                    saveToken(3, currentChar == '\n');
                    readAll = false;
                }else if(currentChar == lecturaTkn.charAt(0) && currentChar != '#'){
                    saveToken(3, currentChar == '\n');
                    readAll = false;
                }
            }
            countLine(currentChar);
            actualizarIndex();
        }
        if(!lecturaTkn.equals("")){
            saveToken(true);
        }
    }
    private void evaluateChar(String texto){
        //<editor-fold defaultstate="collapsed" desc="evalua caracter por caracter e interrumpe el flujo cuando encuentra un caracter terminal">
       
        char currentChar = texto.charAt(index);
        if (ex.isAlphaNumeric(currentChar)){
            lecturaTkn += currentChar;
            if(ex.isNumeric(currentChar) && lecturaTkn.length() == 1){
                analyzeNumberTkn(texto);
                saveToken(2, false);
            }
        //cuando se interrumpe el flujo por un caracter especial
        } else if (!ex.isIgnoredCharacter(currentChar) && lecturaTkn.length() != 0) {
            saveToken(1, true);
            columna--;
            index--;
        //cuando se inicia por un caracter especial
        } else if (!ex.isIgnoredCharacter(currentChar) && lecturaTkn.length() == 0) {
            lecturaTkn += currentChar;
            if(currentChar == '\"' || currentChar == '\'' || currentChar == '#'){ //cadenas y comentarios
                readAll = true;
            }else if(!ex.isCombinable(currentChar)){
                saveToken(4, false);
            }else if(ex.isCombinable(currentChar)){//cuando es un caracter especial que es pueda combinar
                analyzeCombinableTkn(texto);
                saveToken(4, false);
            }
        //cuando se interrumper por un caracter ignorado
        } else if (ex.isIgnoredCharacter(currentChar) && lecturaTkn.length() != 0) {
            saveToken(1, true);
        } //de lo contrario si se trata de otros caracteres ignorados no hace nada
        //</editor-fold>
    }
    private void analyzeCombinableTkn(String texto){
        while (true) {
            if ((index + 1) < texto.length()) {
                char nextChar = texto.charAt(index + 1);
                if (ex.isCombinable(nextChar)) {
                    lecturaTkn += nextChar;
                    actualizarIndex();
                } else {
                    break;
                }
            } else {
                break;
            }
        }
    }
    private void analyzeNumberTkn(String texto){
        boolean onlyOne = true;
        while (true) {            
            if ((index + 1) < texto.length()) {
                char nextChar = texto.charAt(index + 1);
                if (ex.isNumeric(nextChar) || (nextChar == '.' && onlyOne)) {
                    lecturaTkn += nextChar;
                    if(nextChar == '.'){
                        onlyOne = false;
                    }
                    actualizarIndex();
                } else {
                    break;
                }
            } else {
                break;
            }
        }
    }
    private void saveToken(int preliminarType, boolean incrDone){
        if(!incrDone){
            index++;
            columna++;
        }
        /*tokens.add(new Token(lecturaTkn, noLinea, columna, 
                getTypeTkn(preliminarType, lecturaTkn)));*/
        String typeTkn = getTypeTkn(preliminarType, lecturaTkn);
        tokens.add(new Token(lecturaTkn, new Position(columna-lecturaTkn.length(), noLinea, 
                index-lecturaTkn.length()), typeTkn));
        lecturaTkn="";
        if(!incrDone){
            index--;
            columna--;
        }
    }
    private void saveToken(boolean incrDone){
        if(!incrDone){
            index++;
            columna++;
        }
        String typeTkn = getTypeTkn(lecturaTkn);
        tokens.add(new Token(lecturaTkn, new Position(columna-lecturaTkn.length(), noLinea, 
                index-lecturaTkn.length()), typeTkn));
        lecturaTkn="";
        if(!incrDone){
            index--;
            columna--;
        }
    }
    private void countLine(char character){
        if(character == '\n'){
            columna = -1;
            noLinea++;
        }
    }
    private void actualizarIndex(){
        index++;
        columna++;
    }
    
    /***********************************************
     *********** CLASIFICACION DE TOKENS ***********
     ***********************************************/
    
    private String getTypeTkn(int preliminarType, String token){
        switch (preliminarType) {  
            case 1://cuando empieza con una letra
                return sortFistLetter(token);
            case 2: //cuando empieza por un numero
                return sortFirstNumber(token);
            case 3: //cuando empiza por comillas o simbolo de comentario
                return sortString(token);
            case 4: //cuando es un caracter especial
                return sortFistSpecialC(token);
            default: throw new AssertionError();
        }
    }
    public String getTypeTkn(String preTkn) throws IndexOutOfBoundsException{
        char initial = preTkn.charAt(0);
        if (ex.isLetter(initial)) {
            return sortFistLetter(preTkn);
        }else if(ex.isNumeric(initial)){
            return sortFirstNumber(preTkn);
        }else if(ex.isOtro(initial)){
            return "Otro";
        }else if(initial == '#' || initial == '\'' || initial == '"'){
            return sortString(preTkn);
        }else{
            return sortFistSpecialC(preTkn);
        }
    }
    private String sortFistLetter(String preTkn){
        if (ex.isBooleana(preTkn)) {
            return "boolean";
        } else if (ex.isLogico(preTkn)) {
            return "Logico";
        } else if (ex.isReservada(preTkn)) {
            return "Reservada";
        } else {
            return "Identificador";
        }
    }
    private String sortFirstNumber(String preTkn){
        if (preTkn.contains(".") && preTkn.charAt(preTkn.length()-1)!='.') {
            return "float";
        } else if(!preTkn.contains(".")){
            return "int";
        } else {
            return "error";
        }
    }
    private String sortString(String preTkn){
        if (preTkn.charAt(0) == '#') {
            return "Comentario";
        } else if(preTkn.length()>1 && preTkn.charAt(0) == preTkn.charAt(preTkn.length()-1)){
            return "Cadena";
        } else {
            return "error";
        }
    }
    private String sortFistSpecialC(String preTkn){
        if (preTkn.length() == 1) { //cuando solo es un caracter
            if (preTkn.equals("=")) {
                return "Asignacion";
            } else if (ex.isOtro(preTkn.charAt(0))) {
                return "Otro";
            } 
        }
        //cuando es mas de un caracter
        if (ex.isAritmetico(preTkn)) {
            return "Aritmetico";
        } else if (ex.isComparativo(preTkn)) {
            return "Comparativo";
        } else if (ex.isAritmetico(String.valueOf(preTkn.charAt(0)))) {
            if (preTkn.length() == 2 && preTkn.charAt(1) == '=') {
                return "Asignacion";
            } else if (preTkn.length() == 3
                    && (preTkn.charAt(0) == '*' || preTkn.charAt(0) == '/')) {
                if (preTkn.charAt(0) == preTkn.charAt(1) && preTkn.charAt(2) == '=') {
                    return "Asignacion";
                }
            }
        }
        return "error";
    }
    
    /*****************************************************
    **** SETEAR Atributos de los tokens ****
    ******************************************************/
    private String getPatron(String lexema, String typeTkn) {
        String patron;
        patron = switch (typeTkn) {
            case "Identificador" -> "(([a-zA-Z]|_)+)[.]*";
            case "Reservada", "boolean", "Aritmetico", "Asignacion", "Comparativo", "Logico"-> lexema;
            case "int" -> "[0-9]+";
            case "float" -> "([0-9]+)['.']([0-9]+)";
            case "Cadena" -> "[" + lexema.charAt(0) + "][.]*[" + lexema.charAt(0) + "]";
            case "Comentario" -> "[#][.]*";
            case "Otro" -> String.valueOf(lexema.charAt(0));
            default -> "No existe";
        };
        return patron;
    }
    private String getSubTypeTkn(String lexema, String typeTkn){
        switch (typeTkn) {
            case "Asignacion", "Identificador", "int", "float", "Cadena", "Comentario":
                return typeTkn;
            case "Reservada", "boolean", "Logico":
                return lexema;
            case "Aritmetico":
                return getSubTypeAritmetico(lexema);
            case "Comparativo":
                return getSubTypeComp(lexema);
            case "Otro":
                return getSubTypeOtro(lexema);
            /*case "Asignacion":
                if(lexema.length() != 1){
                    String arit = lexema.substring(0, lexema.length()-1);
                    return getSubTypeAritmetico(arit) + "_" + typeTkn;
                }else{  
                    return typeTkn;
                }*/
            default:
                return "error";
        }
    }
    private String getSubTypeAritmetico(String lexema){
        return switch (lexema) {
            case "+" -> "suma";
            case "-" -> "resta";
            case "**" -> "exponente";
            case "/", "//" -> "division";
            case "%" -> "modulo";
            case "*" -> "multiplicacion";
            default -> "error";
        };
    }
    private String getSubTypeComp(String lexema){
        return switch (lexema) {
            case "==" -> "igual";
            case "!=" -> "diferente";
            case ">"  -> "mayor_que";
            case "<"  -> "menor_que";
            case ">=" -> "mayor_o_igual_que";
            case "<=" -> "menor_o_igual_que";
            default -> "error";
        };
    }
    private String getSubTypeOtro(String lexema){
        return switch (lexema) {
            case "(" -> "parentesisL";
            case ")" -> "parentesisR";
            case "{" -> "llaveL";
            case "}" -> "llaveR";
            case "[" -> "corcheteL";
            case "]" -> "corcheteR";
            case "," -> "coma";
            case ":" -> "dos_puntos";
            case ";" -> "punto_y_coma";
            default -> "error";
        };
    }
}

