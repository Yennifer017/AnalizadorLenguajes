
package lenguajes.proyectolenguajesydl.util;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import lenguajes.proyectolenguajesydl.analizadorlexico.Token;

/**
 *
 * @author yenni
 */
public class Graficador {
    Archivo archivo;
    Nodo nodo;
    public Graficador() {
        archivo = new Archivo();
        nodo = new Nodo();
    }
    
    public void graficar(Token token, String fileName, String extensionOutput) {
        String code = initGraphviz(fileName) + getEstructura(token.getType(), token.getContenido()) 
                + endGraphviz();
        archivo.saveFile(code, fileName + ".dot");
        String[] cmd = {"dot.exe", "-Tpng", fileName+".dot", "-o", fileName + extensionOutput};
        
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
        //String estructura = "nodo0[color=blue]\n";
        String estructura = nodo.colorear("nodo0", "blue");
        switch (typeTkn) {
            //causan problemas los aritmeticos otros y asignacion
            case "Reservada", "boolean", "Asignacion", "Aritmetico", "Otro":
                estructura += nodo.defineLabel("nodo0", String.valueOf(lexema.charAt(0)))
                        + nodo.defineLabel("nodoFinal", String.valueOf(lexema.charAt(lexema.length()-1)))
                        + nodo.defineShape("nodoFinal", "doublecircle")
                        + "nodo0 -> ";
                for (int i = 1; i < lexema.length(); i++) {
                    if(i<lexema.length()-1){
                        estructura += lexema.charAt(i) + "-> ";
                    }else{ //cuando es el ultimo
                        estructura += "nodoFinal";
                    }
                }
                break;
            case "Cadena":
                estructura += nodo.defineLabel("nodo0", String.valueOf(lexema.charAt(0))) 
                        + nodo.defineLabel("nodoFinal", String.valueOf(lexema.charAt(lexema.length()-1)))
                        + nodo.defineShape("nodoFinal", "doublecircle");
                        //+ nodo.defineLabel("texto", ".");
                estructura += "nodo0->" + nodo.repeat("texto") + "-> nodoFinal \n";
                estructura += "nodo0 -> nodoFinal";
                break;
            case "int":
                estructura += nodo.defineLabel("nodo0", "digito")
                        + nodo.defineShape("nodo0", "doublecircle");
                estructura += nodo.repeat("nodo0");
                break;
            case "float":
                estructura += nodo.defineLabel("nodo0", "digito")
                        + nodo.defineShape("digito", "doublecircle")
                        + nodo.defineLabel("dot", ".");
                estructura += nodo.repeat("nodo0") + "-> dot ->" + nodo.repeat("digito");
                break; 
                //problemas aqui
            case "Comentario":
                estructura += nodo.defineLabel("nodo0", "\\" + String.valueOf(lexema.charAt(0)))
                        + nodo.defineShape("nodo0", "doublecircle")
                        + nodo.defineLabel("nodoFinal", "\\n")
                        + nodo.defineShape("nodoFinal", "doublecircle");
                estructura += nodo.repeat("nodo0") + "->" +  nodo.repeat("texto") + "-> nodoFinal";
                break;
            default:
                //throw new AssertionError();
        }
        return estructura;
    }
}
