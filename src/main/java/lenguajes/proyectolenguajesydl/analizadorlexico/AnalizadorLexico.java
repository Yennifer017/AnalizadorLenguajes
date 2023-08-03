
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
        tokens = new ArrayList<>();
    }

    public void analizar(String texto){
        tokens.clear();
        separarTokens(texto) ;
        showElements(tokens);
        clasificarTkn();
    }
    public void displayAnalisis(){
        
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
        while(index < texto.length()) {
            char currentChar = texto.charAt(index);
            countLine(currentChar);
            if(!readAll){
                evaluateChar(texto);
            }else{ //cuando se trata de una cadena de caracteres, no se tiene que analizar el mismo
                if(currentChar != '\n'){
                    lecturaTkn += currentChar;
                }
                if(currentChar == '\n' || currentChar == '"' || currentChar == '\''){
                    saveToken();
                    readAll = false;
                }
            }
            actualizarIndex();
        }
        if(!lecturaTkn.equals("")){
            tokens.add(new Token(lecturaTkn, noLinea, columna));
        }
    }
    private void evaluateChar(String texto){
        char currentChar = texto.charAt(index);
        if (isAlphaUp(currentChar) || isAlphaDown(currentChar) || 
                isNumeric(currentChar)){
            lecturaTkn += currentChar;
            if(isNumeric(currentChar) && lecturaTkn.length() == 1){
                analyzeNumberTkn(texto);
                saveToken();
            }
        } else if (!isIgnoredCharacter(currentChar) && lecturaTkn.length() != 0) {
            saveToken();
            index--;
        //cuando se inicia por un caracter especial
        } else if (!isIgnoredCharacter(currentChar) && lecturaTkn.length() == 0) {
            lecturaTkn += currentChar;
            if(currentChar == '\"' || currentChar == '\'' || currentChar == '#'){ //cadenas y comentarios
                readAll = true;
            }else if(!isCombinable(currentChar)){
                saveToken();
            }else if(isCombinable(currentChar)){//cuando es un caracter especial que es pueda combinar
                analyzeCombinableTkn(texto);
                saveToken();
            }
        //cuando se interrumper por un caracter ignorado
        } else if (isIgnoredCharacter(currentChar) && lecturaTkn.length() != 0) {
            saveToken();
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
        while (true) {            
            if ((index + 1) < texto.length()) {
                char nextChar = texto.charAt(index + 1);
                if (isNumeric(nextChar) || nextChar == '.') {
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
    private void saveToken(){
        tokens.add(new Token(lecturaTkn, noLinea, columna));
        lecturaTkn="";
    }
    private void countLine(char character){
        if(character == '\n'){
            columna = 0;
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
    
    private boolean isAlphaUp(char character){
        return ((character >= 'a' && character <= 'z') || character == '_');
    }
    private boolean isAlphaDown(char character){
        return (character >= 'A' && character <= 'Z');
    }
    private boolean isNumeric(char character){
        return (character >= '0' && character <= '9');
    }
    private boolean isIgnoredCharacter(char character){
        return (character == ' ' || character == 9 || character == '\n');
    }
    private boolean isCombinable (char character){
        return switch (character) {
            case '!', '-', '*', '/', '+', '=', '>', '<' -> true;
            default -> false;
        };
    }
    
    /***********************************************
     *********** CLASIFICACION DE TOKENS ***********
     ***********************************************/
    private void clasificarTkn(){
        for (int i = 0; i < tokens.size(); i++) {
            //String currentTkn = Token.getLexema();
        }
    }
}
