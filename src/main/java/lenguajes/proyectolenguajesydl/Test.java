
package lenguajes.proyectolenguajesydl;

 import javax.swing.*;
import java.awt.*;
import javax.swing.text.*;
import lenguajes.proyectolenguajesydl.analizadorlexico.AnalizadorLexico;

public class Test extends JFrame {

    private boolean isAlphaNumeric(char character){
        return AnalizadorLexico.isAlphaDown(character) || AnalizadorLexico.isAlphaUp(character)
                    || AnalizadorLexico.isNumeric(character);
    }
    private int findLastNonWordChar(String text, int index) {
        //el indice se decrementa antes de la evaluacion, se verifica que sea mayor o igual a 0
        while (--index >= 0) {
            //se evalua si el caracter en la posicion index no es un caracter alfanumerico
            /*if (String.valueOf(text.charAt(index)).matches("\\W")) {
                break;
            }*/
            char character = text.charAt(index);
            if(!(isAlphaNumeric(character))){
                break;
            }
        }
        return index;
    }

    private int findFirstNonWordChar(String text, int index) {
        while (index < text.length()) {
            /*if (String.valueOf(text.charAt(index)).matches("\\W")) {
                break;
            }*/
            char character = text.charAt(index);
            if(!(isAlphaNumeric(character))){
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
    public Test() {
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

                //obtener todo el texto que se esta escribiendo
                String text = getText(0, getLength());
                
                int before = findLastNonWordChar(text, offset);
                if (before < 0) { //evitar que se deje un indice negativo
                    before = 0;
                }
                int after = findFirstNonWordChar(text, offset + str.length()); //indice para evaluar todo el cont.
                int wordL = before;
                int wordR = before;

                while (wordR <= after) {
                    if(wordR == after || !(isAlphaNumeric(text.charAt(wordR)))){
                        if(aLex.containsReservedWord(text.substring(wordL, wordR))){
                            setCharacterAttributes(wordL, wordR - wordL, attr, false);
                        }else {
                            setCharacterAttributes(wordL, wordR - wordL, attrBlack, false);
                        }
                        wordL = wordR;
                    }
                    /*if (wordR == after || String.valueOf(text.charAt(wordR)).matches("\\W")) {
                        if (text.substring(wordL, wordR).matches("(\\W)*(private|public|protected)")) {
                            setCharacterAttributes(wordL, wordR - wordL, attr, false);
                        } else {
                            setCharacterAttributes(wordL, wordR - wordL, attrBlack, false);
                        }
                        wordL = wordR;
                    }*/
                    wordR++;
                }
            }

            @Override
            public void remove(int offs, int len) throws BadLocationException {
                super.remove(offs, len);

                String text = getText(0, getLength());
                int before = findLastNonWordChar(text, offs);
                if (before < 0) {
                    before = 0;
                }
                int after = findFirstNonWordChar(text, offs);

                if(aLex.containsReservedWord(text.substring(before, after))){
                    setCharacterAttributes(before, after - before, attr, false);
                }else {
                    setCharacterAttributes(before, after - before, attrBlack, false);
                }
                /*if (text.substring(before, after).matches("(\\W)*(private|public|protected)")) {
                    setCharacterAttributes(before, after - before, attr, false);
                } else {
                    setCharacterAttributes(before, after - before, attrBlack, false);
                }*/
            }
        };
        JTextPane txt = new JTextPane(doc);
        txt.setText("public class Hi {}");
        add(new JScrollPane(txt));
        setVisible(true);
    }

    public static void main(String args[]) {
        new Test();
    }
}
