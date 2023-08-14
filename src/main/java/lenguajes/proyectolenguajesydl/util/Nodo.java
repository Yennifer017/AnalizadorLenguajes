
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
        return nodo + "[label=\"" + label + "\"]\n";
    }
    public String defineShape(String nodo, String shape){
        return nodo + "[shape=" + shape + "]\n";
    }
    public String repeat(String nodo, boolean saltoLinea){
        String sintaxis = nodo + "->" + nodo;
        if(saltoLinea){
            sintaxis += "\n";
        }
        return sintaxis;
    }
    public String dobleImplicacion(String nodo0, String nodo1){
        String sintaxis = nodo0 + "->" + nodo1 + "\n" + nodo1 + "->" + nodo0 + "\n";
        return sintaxis;
    }
}
