/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package lenguajes.proyectolenguajesydl.analizadorlexico;

/**
 *
 * @author yenni
 */
public class IdeaFail {
    int totalLines, fila, columna;
    String currentToken;
    int currentTknType; //se manejara el tipo de token segun un catalogo de tokens?
    /*
     *
     *
     */
    public void analisis(String texto){
        String[] linea = texto.split("\n");
        totalLines = linea.length;
    }
    
    private void lineAnalisis(String line){
        columna = 0;
        while (true) {            
            char character = line.charAt(columna);
            //hace algo con el caracter
            columna++;
        }
    }
    private boolean isAlphaUp(char character){
        return ((character >= 'a' && character <= 'z') || character == '_');
    }
    private boolean isAlphaDown(char character){
        return (character >= 'A' && character <= 'Z');
    }
    private boolean isNumeric(char character){
        return (character >= '0' && character <= '9');
    }
    
    private void extractIdentificador(String texto){
        boolean isToken = true;
        String srcToken = "";
        int srcTokenTyp = 2;
        while (isToken) {            
            srcToken += texto.charAt(columna);
            columna++;
            if(columna >= texto.length()){
                break;
            }
            isToken = isAlphaUp(srcToken.charAt(columna)); 
        }
    }
    
    
}
