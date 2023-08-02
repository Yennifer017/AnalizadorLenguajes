/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package lenguajes.proyectolenguajesydl.analizadorlexico;

import java.util.ArrayList;

/**
 *
 * @author yenni
 */
public class AnalizadorLexico {
    int noLinea, index; //noLinea == fila, index == columna
    int totalLineas;
    ArrayList<String> preTokens;

    public AnalizadorLexico() {
        preTokens = new ArrayList<>();
    }

    
    public void analizar(String texto){
        String[] linea = texto.split("\n");
        totalLineas = linea.length;
        for (noLinea = 0; noLinea < totalLineas; noLinea++) {
            analizarLinea(linea[noLinea]);
        }
        showElements(preTokens);
    }
    private void showElements(ArrayList<String> list){
        for (int i = 0; i < list.size(); i++) {
            System.out.println(list.get(i));
        }
    }
    
    private String lecturaTkn;
    private void analizarLinea(String texto){
        String lectura = "";
        boolean takeAll = false;
        for (index = 0; index < texto.length(); index++) {
            char currentChar = texto.charAt(index);
            if(!takeAll){
                if (isAlphaUp(currentChar) || isAlphaDown(currentChar) 
                        || isNumeric(currentChar)) {
                    lectura += currentChar;
                }else if(!isIgnoredCharacter(currentChar) && lectura.length() != 0){
                    preTokens.add(lectura);
                    index--;
                    lectura = "";
                }else if(!isIgnoredCharacter(currentChar) && lectura.length() == 0){
                    //faltan condiciones
                    preTokens.add(String.valueOf(currentChar));
                }else if(isIgnoredCharacter(currentChar) && lectura.length() != 0){
                    preTokens.add(lectura);
                    lectura = "";
                } //de lo contrario se ignoran los espacio o tabulaciones
            }
            
        }
        if(!lectura.equals("")){
            preTokens.add(lectura);

        }
    }
    private void readToken(char currentChar){
        if (isAlphaUp(currentChar) || isAlphaDown(currentChar)
                || isNumeric(currentChar)) {
            lecturaTkn += currentChar;
        } else if (!isIgnoredCharacter(currentChar) && lecturaTkn.length() != 0) {
            preTokens.add(lecturaTkn);
            index--;
            lecturaTkn = "";
        } else if (!isIgnoredCharacter(currentChar) && lecturaTkn.length() == 0) {
            //faltan condiciones
            preTokens.add(String.valueOf(currentChar));
        } else if (isIgnoredCharacter(currentChar) && lecturaTkn.length() != 0) {
            preTokens.add(lecturaTkn);
            lecturaTkn = "";
        } //de lo contrario se ignoran los espacio o tabulaciones
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
    private boolean isIgnoredCharacter(char character){
        return (character == ' ' || character == 9);
    }
    
}
