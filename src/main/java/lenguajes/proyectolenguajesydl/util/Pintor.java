
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
                //obtener todo el texto que se esta escribiendo
                String text = getText(0, getLength());
                //srt == caracter ingresado
                //offset == posicion de donde se ingresa el caracter, su inicio
                if(str.length() == 1){
                    if(ex.isAlphaNumeric(str.charAt(0))){ //si se ingresa un caracter alfanumerico
                        int posInit = lexer.findDelimitadorL(text, offset);
                        int posFinal = lexer.findDelimitadorR(text, offset);
                        //System.out.println(posInit + "init - final" + posFinal);
                        String currentTkn = text.substring(posInit, posFinal);
                        //System.out.println(currentTkn);
                        String currentType = lexer.getTypeTkn(currentTkn);
                        //System.out.println(currentType);
                        setcurrentAttr(currentType);
                        setCharacterAttributes(posInit, currentTkn.length(), currentAttr, false);
                    }else if(ex.isOtro(str.charAt(0))){ 
                        setCharacterAttributes(offset, 1, attrGreen, false);
                    }else if(str.charAt(0) == '.'){
                        /*int posInit = offset;
                        try {
                            posInit = lexer.findDelimitadorL(text, offset-1);
                        } catch (IndexOutOfBoundsException e) {
                        }
                        int posFinal = lexer.findDelimitadorR(text, offset, true);
                        String currentTkn =text.substring(posInit, posFinal);
                        String currentType = lexer.getTypeTkn(currentTkn);
                        setcurrentAttr(currentType);
                        setCharacterAttributes(posInit, currentTkn.length(), currentAttr, false);*/
                    }


                    /*if(!ex.isIgnoredCharacter(str.charAt(0))){
                        setCharacterAttributes(offset, 1, attrSkyBlue, false);
                    }else if(str.charAt(0) == '"' || str.charAt(0) == '\'' || str.charAt(0) == '#' ){

                    }*/
                }else{
                    int posInit = lexer.findDelimitadorL(text, offset, '\n');
                    int posFinal = lexer.findDelimitadorR(text, offset +str.length()-1, '\n');
                    String subText = text; 
                    try {
                        subText= text.substring(posInit, posFinal);
                        System.out.println("delimitadorL->" + posInit);
                        System.out.println("delimitadorR ->" + posFinal);
                        System.out.println("----------------------");
                    } catch (Exception e) {
                        System.out.println("delimitadorL->" + posInit);
                        System.out.println("delimitadorR ->" + posFinal);
                        System.out.println("error");
                    }
                    lexer.analyzeAll(subText);
                    for (int i = 0; i < lexer.getTokens().size(); i++) {
                        Token currentTkn = lexer.getTokens().get(i);
                        setcurrentAttr(currentTkn.getType());
                        System.out.println(currentTkn.getType());
                        System.out.println(currentAttr.toString());
                        setCharacterAttributes(posInit + currentTkn.getRelativeIndex(), currentTkn.length(),currentAttr, false);
                    }
                }

                //System.out.println("offset:" + offset + "-srt:" + str + "-a:" + a);

            }

            @Override
            public void remove(int offs, int len) throws BadLocationException {
                super.remove(offs, len);
                String text = getText(0, getLength());
                System.out.println(offs + "-offs --- len->" + len);
                //offs -posicion donde termina el cursor luego de la eliminacion
                //len cuantos caracteres se eliminarion
                try {
                    int posInit = lexer.findDelimitadorL(text, offs);
                    int posFinal = lexer.findDelimitadorR(text, offs);
                    //System.out.println(posInit + "init - final" + posFinal);
                    if(posInit == posFinal){
                        posInit--;
                    }
                    //System.out.println(posInit + "init - final" + posFinal);
                    String currentTkn = text.substring(posInit, posFinal);
                    //System.out.println(currentTkn);
                    String currentType = lexer.getTypeTkn(currentTkn);
                    //System.out.println(currentType);
                    setcurrentAttr(currentType);
                    setCharacterAttributes(posInit,  currentTkn.length(), currentAttr, false);
                } catch (IndexOutOfBoundsException e) {
                    //System.out.println(e);
                }
            }
        };
    };
    private DefaultStyledDocument getNewDoc2(AnalizadorLexico lexer, Expresion ex){
        return new DefaultStyledDocument() {
        //se sobrescribe el metodo para ingresar un string, agregando algo de codigo extra
        @Override
            public void insertString(int offset, String str, AttributeSet a) throws BadLocationException {
                super.insertString(offset, str, a);
                //obtener todo el texto que se esta escribiendo
                String text = getText(0, getLength());
                //srt == caracter ingresado
                //offset == posicion de donde se ingresa el caracter, su inicio
                int posInit = lexer.findDelimitadorL(text, offset, '\n');
                int posFinal = lexer.findDelimitadorR(text, offset + str.length() - 1, '\n');
                String subText = text.substring(posInit, posFinal);
                //se analizara toda una fila
                lexer.analyzeAll(subText);
                for (int i = 0; i < lexer.getTokens().size(); i++) {
                    Token currentTkn = lexer.getTokens().get(i);
                    setcurrentAttr(currentTkn.getType());
                    //System.out.println(currentTkn.getContenido() + " -- " + currentTkn.getType());
                    setCharacterAttributes(posInit + currentTkn.getRelativeIndex(), currentTkn.length(), currentAttr, false);
                }
            }

            @Override
            public void remove(int offs, int len) throws BadLocationException {
                super.remove(offs, len);
                String text = getText(0, getLength());
                System.out.println(offs + "-offs --- len->" + len);
                //offs -posicion donde termina el cursor luego de la eliminacion
                //len cuantos caracteres se eliminarion
                try {
                    int posInit = lexer.findDelimitadorL(text, offs);
                    int posFinal = lexer.findDelimitadorR(text, offs);
                    //System.out.println(posInit + "init - final" + posFinal);
                    if (posInit == posFinal) {
                        posInit--;
                    }
                    //System.out.println(posInit + "init - final" + posFinal);
                    String currentTkn = text.substring(posInit, posFinal);
                    //System.out.println(currentTkn);
                    String currentType = lexer.getTypeTkn(currentTkn);
                    //System.out.println(currentType);
                    setcurrentAttr(currentType);
                    setCharacterAttributes(posInit, currentTkn.length(), currentAttr, false);
                } catch (IndexOutOfBoundsException e) {
                    //System.out.println(e);
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
        doc = getNewDoc2(lexer, new Expresion());
        
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
