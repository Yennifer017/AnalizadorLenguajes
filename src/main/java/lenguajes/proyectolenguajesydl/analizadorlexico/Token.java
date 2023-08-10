
package lenguajes.proyectolenguajesydl.analizadorlexico;

import lenguajes.proyectolenguajesydl.util.Position;

/**
 *
 * @author yenni
 */
public class Token {
    private String lexema, type, patron;
    private Position position;
    public Token(String lexema, Position position, String type, String patron) {
        this.lexema = lexema;
        this.position = position;
        this.type = type;
        this.patron = patron;
    }
    @Override
    public String toString(){
        String reporte = "Lexema<" + lexema + ">    Token<" +  type + ">";
        reporte += "    Linea: " + (position.getFila()+1) + " Columna: " + (position.getColumna()+1);
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
    public String getPatron(){
        return patron;
    }
   
}
