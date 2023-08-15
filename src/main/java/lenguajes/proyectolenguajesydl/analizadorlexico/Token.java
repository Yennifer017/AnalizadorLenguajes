
package lenguajes.proyectolenguajesydl.analizadorlexico;
import lenguajes.proyectolenguajesydl.util.Position;

/**
 *
 * @author yenni
 */
public class Token {
    private String lexema, type, patron, subType;
    private Position position;
    public Token(String lexema, Position position, String type) {
        this.lexema = lexema;
        this.position = position;
        this.type = type;
    }
    public Token(String lexema, String type){
        this.lexema = lexema;
        this.type = type;
    }
    @Override
    public String toString(){
        String reporte = "Lexema<" + lexema + ">    Token<" +  subType + ">";
        reporte += "    Linea: " + (position.getFila()+1) + " Columna: " + (position.getColumna()+1);
        return reporte;
       
    }
    //geters
    public String getType(){
        return type;
    }
    public String getSubType(){
        return subType;
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
    /*
     * seters
     */
    public void setSubType(String subType){
        this.subType = subType;
    }
    public void setPatron(String patron){
        this.patron = patron;
    }
}
