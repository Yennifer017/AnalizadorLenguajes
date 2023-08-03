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
    private String contenido;
    private int fila, colStart;
    public Token(String contenido, int fila, int columnaFin) {
        this.contenido = contenido;
        this.fila = fila;
        this.colStart = columnaFin - contenido.length();
    }
    @Override
    public String toString(){
        return contenido + " - fila = " + fila + "- columna = " + colStart; 
    }
    
}
