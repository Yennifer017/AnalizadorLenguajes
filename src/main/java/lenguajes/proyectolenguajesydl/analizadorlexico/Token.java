/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package lenguajes.proyectolenguajesydl.analizadorlexico;

/**
 *
 * @author yenni
 */
public class Token {
    private String contenido, type;
    private int fila, colStart;
    public Token(String contenido, int fila, int columnaFin, String tipoToken) {
        this.contenido = contenido;
        this.fila = fila;
        this.colStart = columnaFin - contenido.length();
        this.type = tipoToken;
    }
    @Override
    public String toString(){
        return contenido + "       fila " + (fila+1) + " columna " + (colStart+1) + " type: " + type; 
    }
    public String getType(){
        return type;
    }
    public int getInicio(){
        return colStart;
    }
    
}
