
package lenguajes.proyectolenguajesydl.analizadorlexico;

import lenguajes.proyectolenguajesydl.util.Position;

/**
 *
 * @author yenni
 */
public class Token {
    private String lexema, type;
    private Position position;
    public Token(String lexema, Position position, String type) {
        this.lexema = lexema;
        this.position = position;
        this.type = type;
    }
    @Override
    public String toString(){
        String reporte = "Lexema<" + lexema + ">    Token<" +  type + ">";
        reporte += "    Linea: " + (position.getFila()+1) + " Columna: " + (position.getColumna()+1);
        /*String reporte = "Token: " + type + "   Patron: " + "PATRON AQUI" + "   Lexema: "; 
        reporte += lexema  + "   Linea: " + position.getFila() + " Columna: " + position.getColumna();*/
        return reporte;
       
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
        return lexema.length();
    }
    public String getContenido(){
        return lexema;
    }
    public int getFila(){
        return position.getFila();
    }
    public int getColumna(){
        return position.getColumna();
    }
   
}
