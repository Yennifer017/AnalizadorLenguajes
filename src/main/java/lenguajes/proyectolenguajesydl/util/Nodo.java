
package lenguajes.proyectolenguajesydl.util;

/**
 *
 * @author yenni
 */
public class Nodo {
    public String colorear(String nodo, String color){
        return nodo + "[color=" + color + "]\n";
    }
    public String defineLabel(String nodo, String label){
        if(label.equals("\"")){
            return nodo + "[label=\"\\\"\"]\n";
        }
        return nodo + "[label=\" " + label + " \"]\n ";
    }
    public String defineShape(String nodo, String shape){
        return nodo + "[shape=  " + shape + "  ]\n";
    }
    public String repeat(String nodo){
        return nodo + "->" + nodo;
    }
}
