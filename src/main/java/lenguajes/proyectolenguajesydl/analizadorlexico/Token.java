/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package lenguajes.proyectolenguajesydl.analizadorlexico;

import lenguajes.proyectolenguajesydl.util.Position;

/**
 *
 * @author yenni
 */
public class Token {
    private String contenido, type;
    private Position position;
    public Token(String contenido, Position position, String tipoToken) {
        this.contenido = contenido;
        this.position = position;
        this.type = tipoToken;
    }
    @Override
    public String toString(){
        return contenido + "       fila " + (position.getFila()+1) + " columna " + (position.getColumna()+1) 
                + " type: " + type; 
    }
    public String getType(){
        return type;
    }
    public int getInicio(){
        return position.getColumna();
    }
    public int getRelativeIndex(){
        return position.getIndex();
    }
    public int length(){
        return contenido.length();
    }
    public String getContenido(){
        return contenido;
    }
   
}
