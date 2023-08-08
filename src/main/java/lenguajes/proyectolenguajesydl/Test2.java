
package lenguajes.proyectolenguajesydl;

import javax.swing.*;
import java.awt.*;
import javax.swing.text.*;
import lenguajes.proyectolenguajesydl.analizadorlexico.AnalizadorLexico;
import lenguajes.proyectolenguajesydl.analizadorlexico.Expresion;

public class Test2 extends JFrame {

    private Expresion ex = new Expresion();
    private int findLastNonWordChar(String text, int index) {
        //el indice se decrementa antes de la evaluacion, se verifica que sea mayor o igual a 0
        while (--index >= 0) {
            char character = text.charAt(index);
            if(!(ex.isAlphaNumeric(character))){
                break;
            }
        }
        return index;
    }

    private int findFirstNonWordChar(String text, int index) {
        while (index < text.length()) {
            char character = text.charAt(index);
            if(!(ex.isAlphaNumeric(character))){
                break;
            }
            index++;
        }
        return index;
    }
        //se almacena el estilo actual, el default
        final StyleContext cont = StyleContext.getDefaultStyleContext();
        //se crean los diferentes COLORES, estos como atributos
        final AttributeSet attr = cont.addAttribute(cont.getEmptySet(), StyleConstants.Foreground, Color.RED);
        final AttributeSet attrBlack = cont.addAttribute(cont.getEmptySet(), StyleConstants.Foreground, Color.BLACK);
        
    AnalizadorLexico aLex = new AnalizadorLexico();
    public Test2() {
        //creacion 
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(400, 400);
        setLocationRelativeTo(null);
            
        //definicion de un documento para que pueda ser coloreado
        DefaultStyledDocument doc = new DefaultStyledDocument() {
            //se sobrescribe el metodo para ingresar un string, agregando algo de codigo extra
            @Override
            public void insertString(int offset, String str, AttributeSet a) throws BadLocationException {
                super.insertString(offset, str, a);
                //este codigo se ejecuta cuando se presiona un string
                System.out.println("ingresando texto");
                System.out.println("offset" + offset + "-srt" + str + "-a:" + a);
                
                //srt == caracter ingresado
                //offset == posicion de donde se ingresa el caracter, su inicio
            }

            @Override
            public void remove(int offs, int len) throws BadLocationException {
                super.remove(offs, len);
                //este codigo se ejecuta cuando se borra una entrada en el teclado
                //borrando texto
                
            }
        };
        JTextPane txt = new JTextPane(doc);
        txt.setText("(def class Hi {}");
        add(new JScrollPane(txt));
        setVisible(true);
    }

    private int encontrarUltimoDivisor(){
        return 0;
    }
    public static void main(String args[]) {
        new Test2();
    }
}
