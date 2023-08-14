
package lenguajes.proyectolenguajesydl.util;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import lenguajes.proyectolenguajesydl.analizadorlexico.Expresion;
import lenguajes.proyectolenguajesydl.analizadorlexico.Token;

/**
 *
 * @author yenni
 */
public class Graficador {
    Archivo archivo;
    Expresion ex;
    Nodo nodo;
    public Graficador() {
        archivo = new Archivo();
        nodo = new Nodo();
        ex = new Expresion();
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
            case "Reservada", "boolean", "Aritmetico", "Otro", "Comparativo", "Logico", "Asignacion" 
                    -> estructura += getEstrEspecifica(lexema);
            case "Identificador" -> {
                estructura += nodo.defineLabel("nodo0", "letra")
                        + nodo.defineLabel("guionB", "guionBajo")
                        + nodo.colorear("guionB", "blue")
                        + nodo.defineShape("nodo0", "doublecircle")
                        + nodo.defineShape("guionB", "doublecircle")
                        + nodo.defineShape("digito", "doublecircle");
                estructura += nodo.repeat("nodo0", true) 
                        + nodo.repeat("guionB", true)
                        + nodo.repeat("digito", true)
                        + nodo.dobleImplicacion("nodo0", "guionB")
                        + nodo.dobleImplicacion("nodo0", "digito")
                        + nodo.dobleImplicacion("guionB", "digito");
            }
            case "Cadena" -> estructura += getEstrDelimitado(String.valueOf(lexema.charAt(0)), 
                        String.valueOf(lexema.charAt(lexema.length()-1)));
            case "int" -> {
                estructura += nodo.defineLabel("nodo0", "digito")
                        + nodo.defineShape("nodo0", "doublecircle");
                estructura += nodo.repeat("nodo0", false);
            }
            case "float" -> {
                estructura += nodo.defineLabel("nodo0", "digito")
                        + nodo.defineShape("digito", "doublecircle")
                        + nodo.defineLabel("dot", ".");
                estructura += nodo.repeat("nodo0", false) + "-> dot ->" 
                        + nodo.repeat("digito", false);
            }
            case "Comentario" -> estructura += getEstrDelimitado(String.valueOf(lexema.charAt(0)), 
                        "SaltoDeLinea") + nodo.repeat("nodo0", false);
            
            default -> {
                return "";
            }
        }
        return estructura;
    }
    private String getEstrEspecifica(String lexema){
        String estructura = "";
        if (lexema.length() != 1) {
            estructura += nodo.defineLabel("nodoFinal", String.valueOf(lexema.charAt(lexema.length()-1)))+
                    nodo.defineShape("nodoFinal", "doublecircle");
            //define los nodos necesarios y les da un seudonimo, excepto al final
            for (int i = 0; i < lexema.length() -1 ; i++) {
                estructura += nodo.defineShape("nodo" + i, "circle");
                estructura += nodo.defineLabel("nodo" + i, String.valueOf(lexema.charAt(i)));
            }
            estructura += "\n";
            //enlaza los nodos
            for (int i = 0; i < lexema.length(); i++) {
                if (i < lexema.length() - 1) {
                        estructura += "nodo" + i +  "-> ";
                } else { //cuando es el ultimo
                    estructura += "nodoFinal";
                }
            }
        } else {
            estructura += nodo.defineLabel("nodo0", String.valueOf(lexema.charAt(0)))
                    + nodo.defineShape("nodo0", "doublecircle");
        }
        return estructura;
    }
    private String getEstrDelimitado(String labelN0, String labelNF){
        String estructura = nodo.defineLabel("nodo0", labelN0)
                + nodo.defineLabel("nodoFinal", labelNF)
                + nodo.defineShape("nodoFinal", "doublecircle");
        estructura += "nodo0 -> " + nodo.repeat("caracter", false) + " -> nodoFinal \n";
        estructura += "nodo0 -> nodoFinal \n";
        return estructura;
    }
    
}

