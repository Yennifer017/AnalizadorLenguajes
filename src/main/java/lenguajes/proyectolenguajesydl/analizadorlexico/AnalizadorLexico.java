
package lenguajes.proyectolenguajesydl.analizadorlexico;

import java.util.ArrayList;

/**
 *
 * @author yenni
 */
public class AnalizadorLexico {
    
    int noLinea, index, columna; //noLinea == fila, index == recorido en el texto
    ArrayList<Token> tokens;
    private String lecturaTkn;
    private boolean readAll;
    public AnalizadorLexico() {
        noLinea = 0;
        tokens = new ArrayList<>();
    }

    public void analizar(String texto){
        tokens.clear();
        separarTokens(texto) ;
    }
    public String getAnalisis(){
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
            saveToken(1, true);
        }
    }
    private void evaluateChar(String texto){
        char currentChar = texto.charAt(index);
        if (isAlphaUp(currentChar) || isAlphaDown(currentChar) || 
                isNumeric(currentChar)){
            lecturaTkn += currentChar;
            if(isNumeric(currentChar) && lecturaTkn.length() == 1){
                analyzeNumberTkn(texto);
                saveToken(2, false);
            }
        //cuando se interrumpe el flujo por un caracter especial
        } else if (!isIgnoredCharacter(currentChar) && lecturaTkn.length() != 0) {
            saveToken(1, true);
            columna--;
            index--;
        //cuando se inicia por un caracter especial
        } else if (!isIgnoredCharacter(currentChar) && lecturaTkn.length() == 0) {
            lecturaTkn += currentChar;
            if(currentChar == '\"' || currentChar == '\'' || currentChar == '#'){ //cadenas y comentarios
                readAll = true;
            }else if(!isCombinable(currentChar)){
                saveToken(4, false);
            }else if(isCombinable(currentChar)){//cuando es un caracter especial que es pueda combinar
                analyzeCombinableTkn(texto);
                saveToken(4, false);
            }
        //cuando se interrumper por un caracter ignorado
        } else if (isIgnoredCharacter(currentChar) && lecturaTkn.length() != 0) {
            saveToken(1, true);
        } //de lo contrario si se trata de otros caracteres ignorados no hace nada
    }
    private void analyzeCombinableTkn(String texto){
        while (true) {
            if ((index + 1) < texto.length()) {
                char nextChar = texto.charAt(index + 1);
                if (isCombinable(nextChar)) {
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
                if (isNumeric(nextChar) || (nextChar == '.' && onlyOne)) {
                    onlyOne = false;
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
    private void saveToken(int preliminarType, boolean incrDone){
        if(!incrDone){
            columna++;
        }
        tokens.add(new Token(lecturaTkn, noLinea, columna, 
                getTypeTkn(preliminarType, lecturaTkn)));
        lecturaTkn="";
        if(!incrDone){
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
    
    /***************************************************
     *********** CONDICIONES PARA CARACTERES ***********
     ***************************************************/
    
    public static boolean isAlphaUp(char character){
        return ((character >= 'a' && character <= 'z') || character == '_');
    }
    public static boolean isAlphaDown(char character){
        return (character >= 'A' && character <= 'Z');
    }
    public static boolean isNumeric(char character){
        return (character >= '0' && character <= '9');
    }
    public static boolean isIgnoredCharacter(char character){
        return switch (character) {
            case ' ', 9, '\r', '\n'-> true;
            default -> false;
        };
    }
    public static boolean isCombinable (char character){
        return switch (character) {
            case '!', '-', '*', '/', '+', '=', '>', '<' -> true;
            default -> false;
        };
    }
    
    //clasificacion de tokens
    private boolean isReservada(String palabra){
        char inicial = palabra.charAt(0);
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
        if (preTkn.contains(".")) {
            return "foat";
        } else {
            return "int";
        }
    }
    private String sortString(String preTkn){
        if (preTkn.charAt(0) == '#') {
            return "Comentario";
        } else if(preTkn.charAt(0) == preTkn.charAt(preTkn.length()-1)){
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
}
