package lenguajes.proyectolenguajesydl.parser;

import java.util.List;
import lenguajes.proyectolenguajesydl.lexer.Token;

/**
 *
 * @author yenni
 */
public class Separator {

    /**
     * identifica el fin de un statment, como el de un if, for, while, etc
     *
     * @param tokens, la lista a analizar
     * @param init inicio
     * @return el fin del statment
     */
    public int findEndOfStmt(List<Token> tokens, int init) {
        boolean includeElif = false, includeElse = false;
        Token initTkn = tokens.get(init);
        int identation = initTkn.getInicio();
        //identifica los statments que necesitan incluir elif y/o else
        switch (initTkn.getSubType()) {
            case "if" -> { //los que aceptan elif y else
                includeElif = true;
                includeElse = true;
            }
            case "for" -> //los que solo esperan elsee
                includeElse = true;
        }
        for (int i = init + 1; i < tokens.size(); i++) {
            if (tokens.get(i).getInicio() <= identation) {
                String typeTknEnd = tokens.get(i).getSubType();
                if (!(typeTknEnd.equals("elif") && includeElif)
                        && !(typeTknEnd.equals("else") && includeElse)) {
                    return i;
                }
            }
        }
        return tokens.size();
    }

    /**
     * encuentra el fin de un bloque, este incluye varios statments
     *
     * @param tokens la lista de tokens a analizar
     * @param init inicio
     * @return el fin del bloque
     */
    public int findEndOfBlock(List<Token> tokens, int init) {
        int line = tokens.get(init).getFila();
        for (int i = init + 1; i < tokens.size(); i++) {
            if (tokens.get(i).getInicio() < line) {
                return i;
            }
        }
        return tokens.size();
    }

    public int findEndOfExpression(List<Token> tokens, int init, String typeTknEnd, int noLine) {
        //fin de expresion puede ser: otra linea, un caracter indicado, 
        int i = init;
        while (i < tokens.size() && tokens.get(i).getFila() == noLine
                && !tokens.get(i).getSubType().equals(typeTknEnd)) {
            i++;
        }
        return i;
    }

    public int findEndOfLine(List<Token> tokens, int init){
        int line = tokens.get(init).getFila();
        for (int i = init + 1; i < tokens.size(); i++) {
            if(tokens.get(i).getFila() != line){
                return i;
            }
        }
        return tokens.size();
    }
}
