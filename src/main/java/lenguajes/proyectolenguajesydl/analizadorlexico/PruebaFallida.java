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
public class PruebaFallida {
    int fila, columna, index;
    String texto;
    boolean finDoc = false;


    public PruebaFallida(int fila, int columna, int index, String texto) {
        this.fila = fila;
        this.columna = columna;
        this.index = index;
        this.texto = texto;
    }
    
    public void analize(String texto){
        this.texto = texto;
        ArrayList<String> preTokens = new ArrayList();
        while (!finDoc) {            
            preTokens.add(getPreToken());
        }
        showElements(preTokens);
    }
    
    private void showElements(ArrayList<String> list){
        try {
                for (int i = 0; i < list.size(); i++) {
                System.out.println(list.get(i));
            }
        } catch (Exception e) {
            System.out.println("No estas usando bien el arrayList");
        }
        
    }
    
    private String getPreToken(){
        String lectura = "";
        while(index<texto.length()){
            if (!contains(ignoredChars, texto.charAt(index))) {
                lectura += texto.charAt(index);
                index++;
                
                if(index == texto.length()){
                    finDoc = true;
                    break;
                }else if(isFinal(texto.charAt(index-1)) || isFinal(texto.charAt(index))){
                    break;
                }
            }else{
                index ++;
            }
        }
        if(lectura.equals("")){
            return null;
        }
        return lectura;
    }
    
    private boolean contains(char[] compareTo, char character){
        for (int i = 0; i < compareTo.length; i++) {
            if(character == compareTo[i]){
                return true;
            }
        }
        return false;
    }
    private boolean isFinal(char character){
        if(contains(asignacion, character)){
            return true;
        }else if(contains(ignoredChars, character)){
            return true;
        }else if(contains(otros, character)){
            return true;
        }else if(contains(fin, character)){
            return true;
        }else{
            return false;
        }
    }
    
    
    private final char[] ignoredChars = {' ', '\n', 9}; 
    private final String[] aritmeticos = {"+", "-", "**", "/", "//", "%", "*"};
    private final String[] comparacion = {"==", "!=", "<", ">", ">=", "<="};
    private final String[] logitos = {"and", "or", "not"};
    private final char[] asignacion = {'='};
    private final String[] palabrasReservadas = {"and", "as", "assert", "break", "class",
        "continue", "def", "del", "elif", "else", "except", "........"};
    private final char[] otros = {'(', ')', '[',']', '{', '}', ',', ';', ':'};
    
    private final char[] fin = {'+', '-', '*','/','%','!','<','>'};
    
    

    /*public void analizar(String codigo){
        for (int i = 0; i < codigo.length(); i++) {
            
        }
    }
    private void countLine(char character){
        if(character == '\n'){
            fila++;
        }
    }*/

    public PruebaFallida() {
    }
}
