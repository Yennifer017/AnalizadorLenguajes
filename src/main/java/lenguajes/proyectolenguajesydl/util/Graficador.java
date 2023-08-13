
package lenguajes.proyectolenguajesydl.util;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import lenguajes.proyectolenguajesydl.analizadorlexico.Token;

/**
 *
 * @author yenni
 */
public class Graficador {
    Archivo archivo;
    public Graficador() {
        archivo = new Archivo();
    }
    
    
    public void graficar(Token token){
    
    }
    public void graficar(String typeTkn, String lexema, String fileName) throws NullPointerException{
        String code = initGraphviz(fileName) + getEstructura(typeTkn, lexema) + endGraphviz();
        archivo.saveFile(code, fileName + ".dot");
        String[] cmd = {"dot.exe", "-Tpng", fileName+".dot", "-o", fileName + ".png"};
        
        Runtime rt = Runtime.getRuntime();
        try {
            rt.exec(cmd);
        } catch (IOException ex) {
            System.out.println("error al obtener la imagen");
            Logger.getLogger(Graficador.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    private String initGraphviz(String fileName){
        return "digraph " + fileName + "{\nrankdir=LR\n";
    }
    private String endGraphviz(){
        return "}";
    }
    private String getEstructura(String typeTkn, String lexema){
        String estructura = "nodo0[color=blue]\n";
        switch (typeTkn) {
            case "Reservada", "boolean":
                estructura += "nodo0[label=\"" +lexema.charAt(0) +"\"] \n";
                estructura += "nodoFinal[label=\"" +lexema.charAt(lexema.length()-1) +"\" shape=doublecircle] \n";
                estructura += "nodo0 -> ";
                for (int i = 1; i < lexema.length(); i++) {
                    if(i<lexema.length()-1){
                        estructura += lexema.charAt(i) + "-> ";
                    }else{ //cuando es el ultimo
                        estructura += "nodoFinal";
                    }
                }
                break;
            case "String":
                
                break;
            case "int":
                
                break;
            case "float":
                
                break;
            case "Comentario":
                
                break;
            default:
                throw new AssertionError();
        }
        return estructura;
    }
}
