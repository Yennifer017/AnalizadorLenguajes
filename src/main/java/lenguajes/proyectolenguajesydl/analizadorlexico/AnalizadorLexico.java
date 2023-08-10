
package lenguajes.proyectolenguajesydl.analizadorlexico;

import java.util.ArrayList;
import lenguajes.proyectolenguajesydl.util.Position;

/**
 *
 * @author yenni
 */
public class AnalizadorLexico {
    
    int noLinea, index, columna; //noLinea == fila, index == recorido en el texto
    ArrayList<Token> tokens;
    private String lecturaTkn;
    private boolean readAll;
    private Expresion ex;
    public AnalizadorLexico() {
        noLinea = 0;
        tokens = new ArrayList<>();
        ex = new Expresion();
    }

    public void analyzeAll(String texto){
        tokens.clear();
        separarTokens(texto) ;
    }
    
    
    public String getErrors(){
        String analisis= "";
        for (int i = 0; i < tokens.size(); i++) {
            if(tokens.get(i).getType().equalsIgnoreCase("error")){
                analisis += tokens.get(i).toString();
            }
        }
        if(!analisis.equals("")){
            return analisis;
        }else{
            return "No hay Errores";
        }
    }
    
    private void showElements(ArrayList<Token> list){
        for (int i = 0; i < list.size(); i++) {
            System.out.println("----- TOKEN NO. " + i );
            System.out.println(list.get(i).toString());
        }
        System.out.println("El total de lineas es igual a " + noLinea);
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
                if(currentChar == '\n' || currentChar == '"' || currentChar == '\''){
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
                index-lecturaTkn.length()), typeTkn, getPatron(lecturaTkn,  typeTkn)));
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
                index-lecturaTkn.length()), typeTkn, getPatron(lecturaTkn, typeTkn)));
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
    private boolean isReservada(String palabra){
        char inicial;
        try {
            inicial = palabra.charAt(0); 
        } catch (IndexOutOfBoundsException e) {
            return false;
        }
        return switch (inicial) {
            case 'a' -> switch (palabra) {
                case "as", "assert" -> true;
                default -> false;
            };
            case 'b' -> switch (palabra) {
                case "break" -> true;
                default -> false;
            };
            case 'c' -> switch (palabra) {
                case "class", "continue" -> true;
                default -> false;
            };
            case 'd' -> switch (palabra) {
                case "def", "del" -> true;
                default -> false;
            };
            case 'e' -> switch (palabra) {
                case "elif", "else", "except" -> true;
                default -> false;
            };
            case 'f', 'F' -> switch (palabra) {
                case "False", "finally", "for", "from" -> true;
                default -> false;
            };
            case 'g' -> switch (palabra) {
                case "global" -> true;
                default -> false;
            };
            case 'i' -> switch (palabra) {
                case "if", "import", "in", "is" -> true;
                default -> false;
            };
            case 'l' -> switch (palabra) {
                case "lamda"-> true;
                default -> false;
            };
            case 'n' -> switch (palabra) {
                case "None", "nonlocal"-> true;
                default -> false;
            };
            case 'p' -> switch (palabra) {
                case "pass" -> true;
                default -> false;
            };
            case 'r' -> switch (palabra) {
                case "rase", "return" -> true;
                default -> false;
            };
            case 't', 'T' -> switch (palabra) {
                case "True", "try" -> true;
                default -> false;
            };
            case 'w' -> switch (palabra) {
                case "while", "with" -> true;
                default -> false;
            };
            case 'y' -> switch (palabra) {
                case "yield"-> true;
                default -> false;
            };
            default -> false;
        };
    }
    private boolean isBooleana(String token){
        return switch (token) {
            case "True", "False"-> true;
            default -> false;
        };
    }
    private boolean isLogico(String token){
        return switch (token) {
            case "and", "or", "not"-> true;
            default -> false;
        };
    }
    private boolean isAritmetico(String preTkn){
        return switch (preTkn) {
            case "+", "-", "**", "/", "//", "%", "*" -> true;
            default -> false;
        };
    }
    private boolean isComparativo(String preTkn){
        return switch(preTkn) {
            case "==", "!=", ">", "<", ">=", "<=" -> true;
            default -> false;
        };
    }
    private boolean isOtro(char character){
        return switch(character) {
            case '(', ')', '{', '}', '[', ']', ',', ';', ':' -> true;
            default -> false;
        };
    }
    
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
        if (isBooleana(preTkn)) {
            return "boolean";
        } else if (isLogico(preTkn)) {
            return "Logico";
        } else if (isReservada(preTkn)) {
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
            } else if (isOtro(preTkn.charAt(0))) {
                return "Otro";
            } 
        }
        //cuando es mas de un caracter
        if (isAritmetico(preTkn)) {
            return "Aritmetico";
        } else if (isComparativo(preTkn)) {
            return "Comparativo";
        } else if (isAritmetico(String.valueOf(preTkn.charAt(0)))) {
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
    
    /********************************************
     *********** OTROS METODOS UTILES ***********
     ********************************************/
    public int findDelimitadorL(String text, int index){
        while (index >= 0) {            
            char character = text.charAt(index);
            if(!ex.isAlphaNumeric(character)){
                index++;
                return index;
            }
            index--;
        }
        if(index<0){
            index = 0;
        }
        return index;
    }
    
    public int findDelimitadorL(String text, int index, char where){
        while (index >= 0 && text.length()!=0) {            
            char character = text.charAt(index);
            if(character == where){
                index++;
                return index;
            }
            index--;
        }
        if(index<0){
            index = 0;
        }
        return index;
    }
    
    public int findDelimitadorR(String text, int index){
        int indexInitial = index;
        while (index<text.length()) {            
            char character = text.charAt(index);
            if(!ex.isAlphaNumeric(character)){
                if(index == indexInitial){
                        index++;
                    }
                    return index;
                }    
            index++;
        }
        if(index > text.length()){
            index = text.length();
        }
        return index;
    }
    
    public int findDelimitadorR(String text, int index, char where){
        int indexInitial = index;
        while (index<text.length()) {            
            char character = text.charAt(index);
            if(character == where){
                if(index == indexInitial){
                        index++;
                    }
                    return index;
                }    
            index++;
        }
        if(index > text.length()){
            index = text.length();
        }
        return index;
    }
    public ArrayList<Token> getTokens(){
        return tokens;
    }
    /*****************************************************
    **** SETEAR LOS PATRONES PARA LOS TIPOS DE TOKENS ****
    ******************************************************/
    private String getPatron(String lexema, String typeTkn) {
        String patron;
        patron = switch (typeTkn) {
            case "Identificador" -> "(([a-zA-Z]|_)+)(\\w|.)*";
            case "Reservada", "boolean", "Aritmetico", "asignacion", "Comparativo"-> lexema;
            case "int" -> "[0-9]+";
            case "float" -> "([0-9]+)[.]([0-9]+)";
            case "Cadena" -> "[" + lexema.charAt(0) + "](\\w|.)*[" + lexema.charAt(0) + "]";
            case "Comentario" -> "[#].*";
            case "Otro" -> String.valueOf(lexema.charAt(0));
            default -> "No existe";
        };
        return patron;
    }
}

