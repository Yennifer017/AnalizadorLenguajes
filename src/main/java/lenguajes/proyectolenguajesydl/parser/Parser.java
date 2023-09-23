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
    private int breaksAllowed;
    private Separator separator;

    public Parser(Lexer lexer) {
        this.lexer = lexer;
        this.errors = new ArrayList<>();
        separator = new Separator();
    }

    public Parser() {
        this.errors = new ArrayList<>();
        separator = new Separator();
    }

    public void analiceAll(Lexer lexer) {
        errors.clear();
        this.lexer = lexer;
        currentNoTkn = 0;
        lexer.deleteNoUtilTkns();
        analiceBlock(lexer.getTokens().size());
        /*while (currentNoTkn < lexer.getTokens().size()) {
            int endStmt = this.findEndOfStmt(lexer.getTokens(), currentNoTkn);
            System.out.println("fin de statment:" + endStmt);
            analiceStmt(endStmt, lexer.getTokens());
        }*/
        visualizarErrores();
    }

    /**
     * clasifica las definicines de statmets y las analiza por separado
     */
    private void analiceBlock(int fin) {
        while (currentNoTkn < fin) {
            int endStmt = separator.findEndOfStmt(lexer.getTokens(), currentNoTkn);
            System.out.println("fin de statment:" + endStmt);
            analiceStmt(endStmt, lexer.getTokens());
        }
    }

    private void analiceStmt(int fin, List<Token> tokens) {
        Token inicial = tokens.get(currentNoTkn);
        switch (inicial.getSubType()) {
            case "Identificador" -> {
                analiceAssignation(fin, tokens);
            }
            case "if" -> {
                validateIfBlock(fin, tokens);
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
                            throw new AssertionError("El automata esta siendo mal usado");
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
                            errors.add(new SintaxError(tokens.get(currentNoTkn).getPosition(),
                                    "Se esperaba una coma o una asignacion"));
                            currentNoTkn--; // nos deja en la misma posicion
                            status = 3;
                        }
                    }
                }
                case 2 -> {
                    switch (type) {
                        case "Identificador" ->
                            status = 1;
                        default -> {
                            errors.add(new SintaxError(tokens.get(currentNoTkn).getPosition(),
                                    "Se esperaba un identificador"));
                            currentNoTkn--; // nos deja en la misma posicion
                            status = 1;
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
                            errors.add(new SintaxError(tokens.get(currentNoTkn).getPosition(),
                                    "Se esperaba una coma"));
                            currentNoTkn--; // nos deja en la misma posicion
                            status = 5;
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
        if (status != 4) {
            String message = "Error inesperado";
            switch (status) {
                case 1 ->
                    message = "Se esperaba una coma o una asignacion";
                case 2 ->
                    message = "Se esperaba un identificador";
                case 3, 5 ->
                    message = "Se esperaba una expresion";
            }
            Token endTkn = tokens.get(currentNoTkn - 1);
            errors.add(new SintaxError(new Position(endTkn.getColumna() + endTkn.length(), endTkn.getFila()),
                    message));
        }
    }

    private void validateIfBlock(int fin, List<Token> tokens) {
        int status = 0;
        while (currentNoTkn < fin) {
            switch (status) {
                case 0 -> {
                    validateIfStmt(tokens);
                    status = 1;
                }
                case 1 -> {
                    String typeTkn = tokens.get(currentNoTkn).getSubType();
                    if(!typeTkn.equals("elif") && !typeTkn.equals("else")){
                        int endOfBlock = separator.findEndOfBlock(tokens, currentNoTkn);
                        analiceBlock(endOfBlock);
                    }
                    status = 2;
                }
                case 2 -> {
                    System.out.println("casificacion de elif_stm y else_block aun no disponible");
                    //clasificar y validar elif_stmt OR else_block
                    status = 3;
                }
            }
            currentNoTkn++;
        }//end of while
    }

    private void validateIfStmt(List<Token> tokens) {
        boolean read = true;
        int status = 0;
        int end = separator.findEndOfLine(tokens, currentNoTkn);

        while (currentNoTkn < end && read) {
            String type = tokens.get(currentNoTkn).getSubType();
            switch (status) {
                case 0 -> {
                    if (!type.equals("if")) {
                        throw new AssertionError("No se esta validando un if statment");
                    }
                    status = 1;
                }
                case 1 -> {
                    validateExpression(tokens, "dos_puntos",
                            tokens.get(currentNoTkn).getFila());
                    status = 2;
                }
                case 2 -> {
                    switch (type) {
                        case "dos_puntos" ->
                            status = 3;
                        default -> {
                            errors.add(new SintaxError(tokens.get(currentNoTkn).getPosition(),
                                    "Se esperaban dos puntos"));
                        }
                    }
                    read = false;
                }
            }
            currentNoTkn++;
        } //end of while
        if (currentNoTkn != end) {
            errors.add(new SintaxError(tokens.get(currentNoTkn).getPosition(),"codigo inesperado a la derecha"));
        } else if (status != 3) {
            String message = "Error inesperado";
            switch (status) {
                case 1 ->
                    message = "Se esperaba una expresion";
                case 2 ->
                    message = "Se esperaban dos puntos";
            }
            Token endTkn = tokens.get(currentNoTkn - 1);
            errors.add(new SintaxError(new Position(endTkn.getColumna() + endTkn.length(), endTkn.getFila()),
                    message));
        }
        currentNoTkn = end - 1;
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
        int end = separator.findEndOfExpression(tokens, currentNoTkn, typeTknEnd, currentLine);
        if (currentNoTkn == end) {
            Token tkn = tokens.get(currentNoTkn);
            errors.add(new SintaxError(new Position(tkn.getColumna() + tkn.length(),
                    currentLine), "Se esperaba una expresion"));
        } else if ((currentNoTkn + 1) == end) {
            boolean isValid = switch (tokens.get(currentNoTkn).getSubType()) {
                case "int", "float", "Cadena", "True", "False", "Identificador" ->
                    true;
                default ->
                    false;
            };
            if (!isValid) {
                errors.add(new SintaxError(tokens.get(currentNoTkn).getPosition(),
                        "Se esperaba una expresion"));
            }
        } else {
            System.out.println("evaluar una expresion de mas de un parametro, aun no soportado");
        }
        currentNoTkn = end - 1;
    }

}
