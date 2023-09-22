package lenguajes.proyectolenguajesydl.parser;

import java.util.ArrayList;
import java.util.List;
import lenguajes.proyectolenguajesydl.lexer.Lexer;
import lenguajes.proyectolenguajesydl.lexer.Token;
import lenguajes.proyectolenguajesydl.util.Position;

/**
 *
 * @author yenni
 */
public class Parser {

    private Lexer lexer;
    private List<SintaxError> errors;
    private int currentNoTkn;

    public Parser(Lexer lexer) {
        this.lexer = lexer;
        this.errors = new ArrayList<>();
    }

    public Parser() {
        this.errors = new ArrayList<>();
    }

    public void analiceAll(Lexer lexer) {
        errors.clear();
        this.lexer = lexer;
        currentNoTkn = 0;
        deleteNoUtilTkns(lexer.getTokens());
        while (currentNoTkn < lexer.getTokens().size()) {
            int endStmt = this.findEndOfStmt(lexer.getTokens(), currentNoTkn);
            System.out.println("fin de statment:" + endStmt);
            analiceStmt(endStmt, lexer.getTokens());
        }
        visualizarErrores();
    }

    /**
     * Elimina elementos que no sirven, como los comentarios
     */
    private void deleteNoUtilTkns(List<Token> tokens) {
        for (int i = 0; i < tokens.size(); i++) {
            if (tokens.get(i).getType().equals("Comentario")) {
                tokens.remove(i);
            }
        }
    }

    /**
     * identifica el fin de un statment
     */
    private int findEndOfStmt(List<Token> tokens, int init) {
        int identation = tokens.get(init).getInicio();
        for (int i = init + 1; i < tokens.size(); i++) {
            if (tokens.get(i).getInicio() == identation) {
                return i; //retorna el numero de posicion del token
            }
        }
        return tokens.size();
    }

    /**
     * encuentra el fin de un bloque,
     */
    private int findEndOfBlock(List<Token> tokens, int init) {
        /*int identation = tokens.get(init).getInicio();
        for (int i = init + 1; i < tokens.size(); i++) {
            if (tokens.get(i).getInicio() == identation) {
                return i; //retorna el numero de posicion del token
            }
        }
        return tokens.size() - 1;*/
        return 0;
    }

    private int findEndOfExpression(List<Token> tokens, int init, String typeTknEnd, int noLine) {
        //fin de expresion puede ser: otra linea, un caracter indicado, 
        int i = init;
        while (i < tokens.size() && tokens.get(i).getFila() == noLine
                && !tokens.get(i).getSubType().equals(typeTknEnd)) {
            i++;
        }
        return i;
    }

    /**
     * clasifica las definicines de statmets y las analiza por separado
     */
    private void analiceStmt(int fin, List<Token> tokens) {
        Token inicial = tokens.get(currentNoTkn);
        switch (inicial.getSubType()) {
            case "Identificador" -> {
                System.out.println("analizando un identificador....");
                analiceAssignation(fin, tokens);
            }
            case "if" -> {
                System.out.println("entro a un if");
            }
            case "for" -> {
                System.out.println("entro a un for");
            }
            case "while" -> {
                System.out.println("entro a un while");
            }
            case "def" -> {
                System.out.println("entro a un def");

            }
            default -> {
                errors.add(new SintaxError(inicial.getPosition(),
                        "identificador, if, for, while o def esperado"));

            }
        }
        currentNoTkn = fin;
    }

    private void visualizarErrores() {
        System.out.println("total de errores =" + errors.size());
        for (int i = 0; i < errors.size(); i++) {
            SintaxError e = errors.get(i);
            System.out.println(e.getDetails());
            System.out.println("fila" + e.getPosition().getFila() + "- columna" + e.getPosition().getColumna());
        }
        if (errors.isEmpty()) {
            System.out.println("no hay errores que mostrar");
        }
    }

    /**
     * ********ANALICE DEPENDING OF THE TYPE OF THE BLOCK**********************
     */
    private void analiceAssignation(int fin, List<Token> tokens) {
        //verificar que solo sea de una linea
        int noLine = tokens.get(currentNoTkn).getFila();
        fin = validateOneLine(currentNoTkn, fin, tokens);
        int status = 0;
        while (currentNoTkn < fin) {
            String type = tokens.get(currentNoTkn).getSubType();
            switch (status) {
                case 0 -> {
                    switch (type) {
                        case "Identificador" ->
                            status = 1;
                        default -> {
                            break;
                        }
                    }
                }

                case 1 -> {
                    switch (type) {
                        case "coma" ->
                            status = 2;
                        case "Asignacion" ->
                            status = 3;
                        default -> {
                            break;
                        }
                    }
                }
                case 2 -> {
                    switch (type) {
                        case "Identificador" ->
                            status = 1;
                        default -> {
                            break;
                        }
                    }
                }
                case 3 -> {
                    validateExpression(tokens, "coma", noLine);
                    status = 4;
                }
                case 4 -> {
                    switch (type) {
                        case "coma" ->
                            status = 5;
                        default -> {
                            break;
                        }
                    }
                }
                case 5 -> {
                    validateExpression(tokens, "coma", noLine);
                    status = 4;
                }
            }
            currentNoTkn++;
        }//end of while
        if (status == 1 || status == 2) {
            String details = switch (status) {
                case 1 ->
                    "Coma u operador de asignacion esperado";
                case 2 ->
                    "Identificador esperado";
                default ->
                    "Esto no tuvo que ocurrir";
            };
            errors.add(new SintaxError(tokens.get(currentNoTkn).getPosition(), details));
        }
    }

    private int validateOneLine(int init, int fin, List<Token> tokens) {
        int line = tokens.get(init).getFila();
        int i = init;
        while (i < tokens.size() && i < fin) {
            int currentLine = tokens.get(i).getFila();
            if (currentLine != line) {
                errors.add(new SintaxError(tokens.get(i).getPosition(), "IdentationError"));
                break;
            }
            i++;
        }
        return i++;
    }

    private void validateExpression(List<Token> tokens, String typeTknEnd, int currentLine) {
        int end = this.findEndOfExpression(tokens, currentNoTkn, typeTknEnd, currentLine);
        if (currentNoTkn == end) {
            Token tkn = tokens.get(currentNoTkn);
            errors.add(new SintaxError(new Position(tkn.getColumna() + tkn.length(),
                    currentLine), "Falta expresion"));
        } else if ((currentNoTkn + 1) == end) {
            boolean isValid = switch (tokens.get(currentNoTkn).getSubType()) {
                case "int", "float", "Cadena", "True", "False", "Identificador" ->
                    true;
                default ->
                    false;
            };
            if (!isValid) {
                errors.add(new SintaxError(tokens.get(currentNoTkn).getPosition(),
                        "Expresion experada"));
            }
        } else {
            System.out.println("evaluar una expresion de mas de un parametro, aun no soportado");
        }
        currentNoTkn = end - 1;
    }

}
