
package lenguajes.proyectolenguajesydl.util;

import java.awt.Color;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;
import lenguajes.proyectolenguajesydl.analizadorlexico.AnalizadorLexico;
import lenguajes.proyectolenguajesydl.analizadorlexico.Expresion;
import lenguajes.proyectolenguajesydl.analizadorlexico.Token;

/**
 *
 * @author yenni
 */
public class Pintor {
    //se almacena el estilo actual, el default
    private final StyleContext cont = StyleContext.getDefaultStyleContext();
    //COLORES, estos como atributos
    private final AttributeSet attrWhite;
    final AttributeSet attrSkyBlue, attrPurple, attrOrange, attrGray, attrGreen, attrRed;
    
    AttributeSet currentAttr;
    
    //definicion de un documento para que pueda ser coloreado
    DefaultStyledDocument doc; 
    private DefaultStyledDocument getNewDoc(AnalizadorLexico lexer, Expresion ex){
        return new DefaultStyledDocument() {
        //se sobrescribe el metodo para ingresar un string, agregando algo de codigo extra
        @Override
            public void insertString(int offset, String str, AttributeSet a) throws BadLocationException {
                super.insertString(offset, str, a);
                //srt == caracter ingresado
                //offset == posicion de donde se ingresa el caracter, su inicio
                String text = getText(0, getLength());
                int posInit = lexer.findDelimitadorL(text, offset, '\n');
                int posFinal = lexer.findDelimitadorR(text, offset + str.length() - 1, '\n');
                String subText = text.substring(posInit, posFinal);
                //se analizara toda una fila
                lexer.analyzeAll(subText);
                for (int i = 0; i < lexer.getTokens().size(); i++) {
                    Token currentTkn = lexer.getTokens().get(i);
                    setcurrentAttr(currentTkn.getType());
                    setCharacterAttributes(posInit + currentTkn.getRelativeIndex(), currentTkn.length(), currentAttr, false);
                }
            }

            @Override
            public void remove(int offs, int len) throws BadLocationException {
                super.remove(offs, len);
                //offs -posicion donde termina el cursor luego de la eliminacion
                //len cuantos caracteres se eliminarion
                String text = getText(0, getLength());
                int posInit = lexer.findDelimitadorL(text, offs - 1, '\n');
                int posFinal = lexer.findDelimitadorR(text, offs, '\n');
                String subText = text.substring(posInit, posFinal);
                //se analiza toda la fila
                lexer.analyzeAll(subText);
                for (int i = 0; i < lexer.getTokens().size(); i++) {
                    Token currentTkn = lexer.getTokens().get(i);
                    setcurrentAttr(currentTkn.getType());
                    setCharacterAttributes(posInit + currentTkn.getRelativeIndex(), currentTkn.length(), currentAttr, false);
                }
            }
        };
    };
    
    public Pintor(AnalizadorLexico lexer) {
        attrWhite = cont.addAttribute(cont.getEmptySet(), StyleConstants.Foreground, Color.WHITE);
        attrSkyBlue = cont.addAttribute(cont.getEmptySet(), StyleConstants.Foreground, new Color(0,242,255));
        attrPurple = cont.addAttribute(cont.getEmptySet(), StyleConstants.Foreground, new Color(225,156,255));  
        attrOrange = cont.addAttribute(cont.getEmptySet(), StyleConstants.Foreground, Color.ORANGE);
        attrGray = cont.addAttribute(cont.getEmptySet(), StyleConstants.Foreground, Color.GRAY);
        attrGreen = cont.addAttribute(cont.getEmptySet(), StyleConstants.Foreground, Color.GREEN);
        attrRed = cont.addAttribute(cont.getEmptySet(), StyleConstants.Foreground, Color.RED);
        doc = getNewDoc(lexer, new Expresion());
    }
    private void setcurrentAttr(String typeTkn){
        currentAttr = switch (typeTkn) {
            case "Identificador" -> attrWhite;
            case "Reservada" -> attrPurple;
            case "boolean", "int", "float", "Cadena" -> attrOrange;
            case "Comentario" -> attrGray;
            case "Otro" -> attrGreen;
            case "error" -> attrRed;
            default -> attrSkyBlue;
        };
    }
    public DefaultStyledDocument getDefStyleDoc (){
        return doc;
    }
    
}
