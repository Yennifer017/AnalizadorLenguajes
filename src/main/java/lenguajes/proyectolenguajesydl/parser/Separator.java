package lenguajes.proyectolenguajesydl.parser;

import java.util.List;
import java.util.Stack;
import lenguajes.proyectolenguajesydl.lexer.Regex;
import lenguajes.proyectolenguajesydl.lexer.Token;

/**
 *
 * @author yenni
 */
public class Separator {

    private Regex ex;
    public Separator() {
        ex = new Regex();
    }

    
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
        int identation = initTkn.getColumna();
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
            if (tokens.get(i).getColumna() == identation) {
                String typeTknEnd = tokens.get(i).getSubType();
                if (!(typeTknEnd.equals("elif") && includeElif)
                        && !(typeTknEnd.equals("else") && includeElse)) {
                    return i;
                }
            }else if(tokens.get(i).getColumna()<identation){
                return i;
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
    public int findEndOfBlock(List<Token> tokens, int init, int bigIdentation, List<SyntaxError> errors) {
        int identation = tokens.get(init).getColumna();
        
        for (int i = init + 1; i < tokens.size(); i++) {
            if (tokens.get(i).getColumna() < identation) {
                if(tokens.get(i).getColumna() <= bigIdentation){
                    return i;
                }else{
                    errors.add(new SyntaxError(tokens.get(i).getPosition(),
                            "Identation Error" ));
                    //agregar un error de identacion
                }
            }
        }
        return tokens.size();
    }

    public int findEndOfExpression(List<Token> tokens, int init, String typeTknEnd, int noLine) {
        //fin de expresion puede ser: otra linea, un caracter indicado, 
        Stack<String> stack = new Stack<>();
       
        for (int i = init; i < tokens.size(); i++) {
            Token currentTkn = tokens.get(i);
            String subT = currentTkn.getSubType();
            switch (subT) {
                case "pL", "cL", "lL" -> stack.push(subT);
                case "pR", "cR", "lR" -> { //AQUI HAY UN ERROR
                    if(!stack.isEmpty()){
                        if(ex.isComplementario(stack.peek(), subT)){
                            stack.pop();
                        }
                    }
                }
            }
            if( (currentTkn.getLine() != noLine) 
                    || (currentTkn.getSubType().equals(typeTknEnd) && stack.isEmpty()) ){
                return i;
            }    
        }
        return tokens.size();
        
    }

    public int findEndOfLine(List<Token> tokens, int init){
        int line = tokens.get(init).getLine();
        for (int i = init + 1; i < tokens.size(); i++) {
            if(tokens.get(i).getLine() != line){
                return i;
            }
        }
        return tokens.size();
    }
}
