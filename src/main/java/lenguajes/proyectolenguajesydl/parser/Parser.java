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
    private List<SyntaxError> errors;
    private int currentNoTkn;
    private int breaksAllowed, returnsAllowed;
    private Separator separator;
    private ExpressionVerificator eVerificator;
    private Structure structureV;
    private int identationLevel;
    private Registrador registrador;
    public Parser(Lexer lexer) {
        this.errors = new ArrayList<>();
        separator = new Separator();
        breaksAllowed = 0;
        returnsAllowed = 0;
        eVerificator = new ExpressionVerificator(this);
        structureV = new Structure();
        identationLevel = 0;
        this.lexer = lexer;
        registrador = new Registrador(this,lexer);
    }

    public void analiceAll() {
        registrador.clear();
        errors.clear();
        //this.lexer = lexer;
        currentNoTkn = 0;
        lexer.deleteNoUtilTkns();
        try {
            analiceSubBlock(lexer.getTokens(), lexer.getToken(currentNoTkn).getColumna());
            try {
                registrador.empaquetarDatos();
            } catch (Exception e) {
                System.out.println("algo que definitivamente no debio salir mal, salio mal");
            }
        } catch (NullPointerException e) {
            System.out.println(e);
        }
        
    }

    public String getReportErrors() {
        String analisis = "ERRORES SINTACTICOS: \n";
        for (int i = 0; i < errors.size(); i++) {
            SyntaxError error = errors.get(i);
            analisis += "Fila: " + (error.getPosition().getFila() + 1)
                    + " -- Columna: " + (error.getPosition().getColumna() + 1)
                    + " -- Detalles: " + error.getDetails();
            analisis += "\n";
        }
        if (analisis.equals("ERRORES SINTACTICOS: \n")) {
            analisis += "No hay Errores sintacticos :D";
        }
        return analisis;
    }
    public List<SyntaxError> getErrors(){
        return this.errors;
    }
    public Registrador getRegistrador(){
        return this.registrador;
    }
    /**
     * clasifica las definicines de statmets y las analiza por separado
     */
    private void analiceBlock(int fin) {
        identationLevel++;
        while (currentNoTkn < fin) {
            int endStmt = separator.findEndOfStmt(lexer.getTokens(), currentNoTkn);
            analiceStmt(endStmt, lexer.getTokens());
        }
        identationLevel--;
    }

    private void analiceStmt(int fin, List<Token> tokens) {
        Token inicial = tokens.get(currentNoTkn);
        switch (inicial.getSubType()) {
            case "Identificador" ->{
                registrador.addRegistro(identationLevel, currentNoTkn);
                validateAssignation(fin, tokens);
            }
            case "if" ->
                validateIfBlock(fin, tokens);
            case "for" ->{
                registrador.addRegistro(identationLevel, currentNoTkn);
                validateForBlock(tokens, fin);
            }
            case "while" ->
                validateWhileBlock(tokens, fin);
            case "def" ->{
                registrador.addRegistro(identationLevel, currentNoTkn);
                validateDefBlock(tokens, fin);
            }
            case "break" -> {
                if (breaksAllowed > 0) {
                    registrador.addRegistro(identationLevel, currentNoTkn);
                    validateBreakStmt(tokens, fin);
                } else {
                    errors.add(new SyntaxError(inicial.getPosition(),
                            "Sentencia 'break' usado fuera de un ciclo"));
                }
            }
            case "return" -> {
                if (returnsAllowed > 0) {
                    registrador.addRegistro(identationLevel, currentNoTkn);
                    validateReturnStmt(tokens, fin);
                } else {
                    errors.add(new SyntaxError(inicial.getPosition(),
                            "Sentencia 'return' usado fuera de una declaracion de funcion"));
                }
            }
            default ->
                errors.add(new SyntaxError(inicial.getPosition(),
                        "identificador, if, for, while o def esperado"));
        }
        currentNoTkn = fin;
    }

    /**
     * ********ANALICE DEPENDING OF THE TYPE OF THE BLOCK**********************
     */
    private void validateAssignation(int fin, List<Token> tokens) {
        //verificar que solo sea de una linea
        int noLine = tokens.get(currentNoTkn).getLine();
        fin = validateOneLine(currentNoTkn, fin, tokens);
        int status = 0;
        boolean read = true;
        while (currentNoTkn < fin && read) {
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
                    if(type.contains("Asignacion")){
                        status = 3;
                    }else{
                        switch (type) {
                            case "coma" ->
                                status = 2;
                            case "pL" -> {
                                validateStructure(tokens.subList(currentNoTkn, fin),
                                        Structure.USE_FUNCTION, errors);
                                status = 4;
                                currentNoTkn = fin;
                            }
                            default -> {
                                currentNoTkn--; // nos deja en la misma posicion
                                read = false; //interrumpir flujo

                            }
                        }
                    }
                    /*switch (type) {
                        case "coma" ->
                            status = 2;
                        case "Asignacion" ->
                            status = 3;
                        case "pL" ->{
                            validateStructure(tokens.subList(currentNoTkn, fin),
                                    Structure.USE_FUNCTION, errors);
                            status = 4;
                            currentNoTkn = fin;
                        }
                        default -> {
                            currentNoTkn--; // nos deja en la misma posicion
                            read = false; //interrumpir flujo
                           
                        }
                    }*/
                }
                case 2 -> {
                    switch (type) {
                        case "Identificador" ->
                            status = 1;
                        default -> {
                            errors.add(new SyntaxError(tokens.get(currentNoTkn).getPosition(),
                                    "Se esperaba un identificador"));
                            currentNoTkn--; // nos deja en la misma posicion
                            status = 1;
                        }
                    }
                }
                case 3 -> {
                    currentNoTkn = eVerificator.validate(tokens, "coma", 
                            noLine, currentNoTkn);
                    //validateExpression(tokens, "coma", noLine);
                    status = 4;
                }
                case 4 -> {
                    switch (type) {
                        case "coma" ->
                            status = 5;
                        default -> {
                            errors.add(new SyntaxError(tokens.get(currentNoTkn).getPosition(),
                                    "Se esperaba una coma"));
                            currentNoTkn--; // nos deja en la misma posicion
                            status = 5;
                        }
                    }
                }
                case 5 -> {
                    currentNoTkn = eVerificator.validate(tokens, "coma", 
                            noLine, currentNoTkn);
                    //validateExpression(tokens, "coma", noLine);
                    status = 4;
                }
            }
            currentNoTkn++;
        }//end of while
        if (status != 4) {
            String message = "Error inesperado";
            switch (status) {
                case 1 ->
                    message = "Se esperaba una coma, una asignacion o parentesis para el uso de una funcion";
                case 2 ->
                    message = "Se esperaba un identificador";
                case 3, 5 ->
                    message = "Se esperaba una expresion";
            }
            Token endTkn = tokens.get(currentNoTkn - 1);
            errors.add(new SyntaxError(new Position(endTkn.getColumna() + endTkn.length(), endTkn.getLine()),
                    message));
        }
    }

    private void validateIfBlock(int fin, List<Token> tokens) {
        int status = 0;
        int bigIdentation = tokens.get(currentNoTkn).getColumna();
        while (currentNoTkn < fin) {
            switch (status) {
                case 0 -> {
                    validateConditionalStmt(tokens, "if");
                    status = 1;
                }
                case 1 -> {
                    analiceSubBlock(tokens, bigIdentation);
                    status = 2;
                }
                case 2 -> {
                    String typeTkn = tokens.get(currentNoTkn).getSubType();
                    switch (typeTkn) {
                        case "elif" ->
                            validateElifBlock(tokens, fin);
                        case "else" ->
                            validateElseBlock(tokens, fin);
                    }
                    status = 3;
                }
            }
            currentNoTkn++;
        }//end of while
    }

    private void validateElifBlock(List<Token> tokens, int end) {
        int status = 0;
        int bigIdentation = tokens.get(currentNoTkn).getColumna();
        while (currentNoTkn < end) {
            switch (status) {
                case 0 -> {
                    validateConditionalStmt(tokens, "elif");
                    status = 1;
                }
                case 1 -> {
                    analiceSubBlock(tokens, bigIdentation);
                    status = 2;
                }
                case 2 -> {
                    String type = tokens.get(currentNoTkn).getSubType();
                    switch (type) {
                        case "elif" -> {
                            validateConditionalStmt(tokens, "elif");
                            status = 1;
                        }
                        case "else" -> {
                            validateElseBlock(tokens, end);
                            status = 3;
                        }
                        default ->
                            throw new AssertionError();
                    }
                }
            }
            currentNoTkn++;
        }//end of while

    }

    private void validateConditionalStmt(List<Token> tokens, String conditional) {
        boolean read = true;
        int status = 0;
        int end = separator.findEndOfLine(tokens, currentNoTkn);

        while (currentNoTkn < end && read) {
            String type = tokens.get(currentNoTkn).getSubType();
            switch (status) {
                case 0 -> {
                    if (!type.equals(conditional)) {
                        throw new AssertionError("No se esta validando un conditional statment");
                    }
                    registrador.addRegistro(identationLevel, currentNoTkn);
                    status = 1;
                }
                case 1 -> {
                    currentNoTkn = eVerificator.validate(tokens, "dos_puntos", 
                            tokens.get(currentNoTkn).getLine(), currentNoTkn);
                    //validateExpression(tokens, "dos_puntos",
                    //        tokens.get(currentNoTkn).getLine());
                    status = 2;
                }
                case 2 -> {
                    switch (type) {
                        case "dos_puntos" ->
                            status = 3;
                        default -> {
                            errors.add(new SyntaxError(tokens.get(currentNoTkn).getPosition(),
                                    "Se esperaban dos puntos"));
                        }
                    }
                    read = false;
                }
            }
            currentNoTkn++;
        } //end of while
        if (currentNoTkn != end) {
            errors.add(new SyntaxError(tokens.get(currentNoTkn).getPosition(), "codigo inesperado a la derecha"));
        } else if (status != 3) {
            String message = "Error inesperado";
            switch (status) {
                case 1 ->
                    message = "Se esperaba una expresion";
                case 2 ->
                    message = "Se esperaban dos puntos";
            }
            Token endTkn = tokens.get(currentNoTkn - 1);
            errors.add(new SyntaxError(new Position(endTkn.getColumna() + endTkn.length(), endTkn.getLine()),
                    message));
        }
        currentNoTkn = end - 1;
    }

    private void validateElseStmt(List<Token> tokens) {
        boolean read = true;
        int status = 0;
        int end = separator.findEndOfLine(tokens, currentNoTkn);
        while (currentNoTkn < end && read) {
            String type = tokens.get(currentNoTkn).getSubType();
            switch (status) {
                case 0 -> {
                    if (!type.equals("else")) {
                        throw new AssertionError("No se esta validando un else statment");
                    }
                    registrador.addRegistro(identationLevel, currentNoTkn);
                    status = 1;
                }
                case 1 -> {
                    if (!type.equals("dos_puntos")) {
                        errors.add(new SyntaxError(tokens.get(currentNoTkn).getPosition(),
                                "Se esperaban dos puntos"));
                    }
                    status = 2;
                    read = false;
                }
            }
            currentNoTkn++;
        } //end of while
        if (currentNoTkn != end) {
            errors.add(new SyntaxError(tokens.get(currentNoTkn).getPosition(), "codigo inesperado a la derecha"));
        } else if (status != 2) {
            String message = "Error inesperado";
            switch (status) {
                case 1 ->
                    message = "Dos puntos esperados";
            }
            Token endTkn = tokens.get(currentNoTkn - 1);
            errors.add(new SyntaxError(new Position(endTkn.getColumna() + endTkn.length(), endTkn.getLine()),
                    message));
        }
        currentNoTkn = end - 1;
    }

    private void validateElseBlock(List<Token> tokens, int end) {
        int bigIdentation = tokens.get(currentNoTkn).getColumna();
        int status = 0;
        while (currentNoTkn < end) {
            switch (status) {
                case 0 -> {
                    validateElseStmt(tokens);
                    status = 1;
                }
                case 1 -> {
                    analiceSubBlock(tokens, bigIdentation);
                    status = 2;
                }
            }
            currentNoTkn++;
        } //end of while
    }

    private void validateWhileBlock(List<Token> tokens, int end) {
        int bigIdentation = tokens.get(currentNoTkn).getColumna();
        breaksAllowed++;
        int status = 0;
        while (currentNoTkn < end) {
            switch (status) {
                case 0 -> {
                    validateConditionalStmt(tokens, "while");
                    status = 1;
                }
                case 1 ->
                    analiceSubBlock(tokens, bigIdentation);
            }
            currentNoTkn++;
        }//end of while
        breaksAllowed--;
    }

    private void validateForBlock(List<Token> tokens, int end) {
        breaksAllowed++;
        int status = 0;
        int bigIdentation = tokens.get(currentNoTkn).getColumna();
        while (currentNoTkn < end) {   
            switch (status) {
                case 0 -> {
                    validateForStmt(tokens);
                    status = 1;
                }
                case 1 -> {
                    analiceSubBlock(tokens, bigIdentation);
                    status = 2;
                }
                case 2 -> validateElseBlock(tokens, end);
            }
            currentNoTkn++;
        }
        breaksAllowed--;
    }

    private void validateForStmt(List<Token> tokens) {
        int status = 0;
        int end = separator.findEndOfLine(tokens, currentNoTkn);
        boolean read = true;
        while (currentNoTkn < end && read) {
            Token tkn = tokens.get(currentNoTkn);
            switch (status) {
                case 0 -> {
                    if(!tkn.getSubType().equals("for")){
                        throw new AssertionError("No se esta validando un for_stmt");
                    }
                    status = 1;
                }
                case 1 -> {
                    validateOnePos(tkn, "Identificador", "identificador");
                    status = 2;
                }
                case 2 -> {
                    validateOnePos(tkn, "in", "'in'");
                    status = 3;
                }
                case 3 -> {
                    currentNoTkn = eVerificator.validate(tokens, "dos_puntos", 
                            tokens.get(currentNoTkn).getLine(), currentNoTkn);
                    //validateExpression(tokens, "dos_puntos", tokens.get(currentNoTkn).getLine());
                    status = 4;
                }
                case 4 -> {
                    validateOnePos(tkn, "dos_puntos", "dos puntos");
                    status = 5;
                }
                case 5 -> read = false;
            }
            currentNoTkn++;
        }//end of while
        String message;
        if(status != 5){
            message = switch (status) {
                case 1 -> "Identificador esperado";
                case 2 -> "'in' esperado";
                case 3 -> "Se esperaban dos puntos";
                default -> "Error inesperado";
            };
            errors.add(new SyntaxError(tokens.get(currentNoTkn).getPosition(), message));
        }else if(currentNoTkn != end && status == 5){
            errors.add(new SyntaxError(tokens.get(currentNoTkn).getPosition(),
                    "Codigo inesperado a la derecha"));
        }
        currentNoTkn = end - 1;
    }

    private void validateDefBlock(List<Token> tokens, int end) {
        returnsAllowed++;
        int status = 0;
        while (currentNoTkn < end) {
            switch (status) {
                case 0 -> {
                    validateDefStmt(tokens);
                    status = 1;
                }
                case 1 ->
                    analiceBlock(end);
            }
            currentNoTkn++;
        }
        returnsAllowed--;
    }

    private void validateDefStmt(List<Token> tokens) {
        boolean read = true;
        int end = separator.findEndOfLine(tokens, currentNoTkn);
        int status = 0;
        while (currentNoTkn < end && read) {
            Token cTkn = tokens.get(currentNoTkn);
            switch (status) {
                case 0 -> {
                    if (!cTkn.getSubType().equals("def")) {
                        throw new AssertionError("No se esta validando un def stmt");
                    }
                    status = 1;
                }
                case 1 -> {
                    validateOnePos(cTkn, "Identificador", "identificador");
                    status = 2;
                }
                case 2 -> {
                    validateOnePos(tokens.get(currentNoTkn), "pL",
                            "parentesis de apertura");
                    status = 3;
                }
                case 3 -> {
                    switch (cTkn.getSubType()) {
                        case "pR" ->
                            status = 4;
                        case "Identificador" ->
                            status = 5;
                        default -> {
                            currentNoTkn--;
                            read = false;
                        }
                    }
                }
                case 4 -> {
                    validateOnePos(tokens.get(currentNoTkn), "dos_puntos",
                            "dos puntos");
                    status = 7;
                }
                case 5 -> {
                    switch (cTkn.getSubType()) {
                        case "coma" ->
                            status = 6;
                        case "pR" ->
                            status = 4;
                        default -> {
                            currentNoTkn--;
                            read = false;
                        }
                    }
                }

                case 6 -> {
                    validateOnePos(cTkn, "Identificador", "identificador");
                    status = 5;
                }
                case 7 ->
                    read = false;
            }
            currentNoTkn++;
        } // end of while
        if (status != 7) {
            String message = switch (status) {
                case 1 ->
                    "Identificador esperado";
                case 2 ->
                    "Parentesis de apartura esperado";
                case 3 ->
                    "Parentesis de cierre o identificador esperado";
                case 4 ->
                    "Dos puntos esperados";
                case 5 ->
                    "Coma esperada";
                case 6 ->
                    "Identificador esperado";
                default ->
                    "error inesperado";
            };
            errors.add(new SyntaxError(tokens.get(currentNoTkn).getPosition(), message));
        } else if (currentNoTkn != end && status == 7) {
            errors.add(new SyntaxError(tokens.get(currentNoTkn).getPosition(),
                    "Codigo inesperado a la derecha"));
        }
        currentNoTkn = end - 1;
    }

    private void validateOnePos(Token token, String correctType, String correctTypeName) {
        String type = token.getSubType();
        if (!type.equals(correctType)) {
            errors.add(new SyntaxError(token.getPosition(),
                    "Se esperaba " + correctTypeName));
            currentNoTkn--;
        }
    }
    private int validateOnePos(Token token, String correctType, String correctTypeName, int index){
        String type = token.getSubType();
        if (!type.equals(correctType)) {
            errors.add(new SyntaxError(token.getPosition(),
                    "Se esperaba " + correctTypeName));
            return index - 1;
        }
        return index;
    }

    private void validateBreakStmt(List<Token> tokens, int end) {
        end = validateOneLine(currentNoTkn, end, tokens);
        if (tokens.get(currentNoTkn).getSubType().equals("break")) {
            currentNoTkn++;
            if (currentNoTkn != end) {
                errors.add(new SyntaxError(tokens.get(currentNoTkn).getPosition(),
                        "codigo inesperado a la derecha"));
            }
        } else {
            throw new AssertionError("No se esta tratando de validar un break");
        }
    }

    private void validateReturnStmt(List<Token> tokens, int end) {
        int status = 0;
        while (currentNoTkn<end) { 
            switch (status) {
                case 0 -> {
                    if(!tokens.get(currentNoTkn).getSubType().equals("return")){
                        throw new AssertionError("No se esta tratando de validar un return statement");
                    }
                    status = 1;
                }
                case 1 -> {
                    currentNoTkn = eVerificator.validate(tokens, "", 
                            tokens.get(currentNoTkn).getLine(), currentNoTkn);
                    //validateExpression(tokens, "", tokens.get(currentNoTkn).getLine());
                    status = 2;
                }
            }
            currentNoTkn++;
        } //end of while
        if(status != 2 ){
            Token endTkn = tokens.get(currentNoTkn - 1);
            errors.add(new SyntaxError(new Position(endTkn.getColumna() + endTkn.length(), endTkn.getLine()),
                    "Expresion esperada"));
        }
    }

    protected void validateStructure(List<Token> sentence, int type, List<SyntaxError> errorsList) {
        boolean read = true;
        int index = 0;
        int status = 0;
        ExpressionVerificator ev = new ExpressionVerificator(this);
        String[] delimitadors = {"coma", structureV.getDelimitadorR(type)};
        while (index<sentence.size() && read) { 
            String typeTkn = sentence.get(index).getSubType();
            switch (status) {
                case 0 -> {
                    if (!typeTkn.equals(structureV.getDelimitadorL(type))) { 
                        throw new AssertionError("No se esta trando de validar una estructura");
                    }
                    status = 1;
                }
                case 1 -> {
                    if(typeTkn.equals(structureV.getDelimitadorR(type))){
                        status = 2;
                    }else{
                        index = ev.validate(sentence, delimitadors,
                                sentence.get(index).getLine(), index);
                        status = 3;
                    }
                }
                case 2 -> {
                    index--;
                    read = false;
                }
                case 3 -> {
                    if(typeTkn.equals("coma")){
                        status = 4;
                    }else if(typeTkn.equals(structureV.getDelimitadorR(type))){
                        status = 2;
                    }else{
                        errorsList.add(new SyntaxError(sentence.get(index).getPosition(),
                                "Se esperaba una coma o un cierre de sentencia"));
                        index--; // nos deja en la misma posicion
                        read = false;
                    }
                }
                case 4 -> {
                    index = ev.validate(sentence, delimitadors,
                                sentence.get(index).getLine(), index);
                    status = 3;
                }
            }
            index++;
        }//end of while
        if(status != 2){
            String message = switch (status) {
                case 1 -> "Cierre de sentencia o expresion esperada";
                case 3 -> "Coma o cierre de sentencia esperada";
                case 4 -> "Expresion esperada";
                default -> "Error inesperado";
            };
            Token endTkn = sentence.get(index - 1);
            errorsList.add(new SyntaxError(new Position(endTkn.getColumna() + endTkn.length(), endTkn.getLine()),
                    message));
        }else if(index<sentence.size() && status == 2){
            errorsList.add(new SyntaxError(sentence.get(index).getPosition(),
                    "Codigo inesperado a la derecha"));
        }
        
    }

    protected void validateDictionary(List<Token> sentence, List<SyntaxError> errorsList){
        int status = 0;
        int index = 0;
        int noLine = sentence.get(index).getLine();
        boolean read = true;
        String[] delimitadores = {"dos_puntos", "coma", "lR"};
        ExpressionVerificator ev = new ExpressionVerificator(this);
        while (index < sentence.size() && read) {   
            String typeTkn = sentence.get(index).getSubType();
            switch (status) {
                case 0 -> {
                    if (!typeTkn.equals("lL")) { 
                        throw new AssertionError("No se esta trando de validar una estructura");
                    }
                    status = 1;
                }
                case 1 -> {
                    if(typeTkn.equals("lR")){
                        status = 2;
                    }else{
                        index = ev.validate(sentence, delimitadores,
                                noLine, index);
                        status = 3;
                    }
                }
                case 2 -> {
                    index--;
                    read = false;
                }
                case 3 -> {
                    index  = validateOnePos(sentence.get(index), 
                            "dos_puntos", "dos puntos", index);
                    status = 4;
                }
                case 4 -> {
                    index = ev.validate(sentence, delimitadores, noLine, index);
                    status = 5;
                }
                case 5 -> {
                    switch (typeTkn) {
                        case "coma":
                            index = validateOnePos(sentence.get(index), "coma",
                            "coma", index);
                            status = 6;
                            break;
                        case "lR":
                            status = 2;
                            break;
                        default:
                            index--;
                            read = false;
                    }
                }
                case 6 -> {
                    index = ev.validate(sentence, delimitadores, noLine, index);
                    status = 3;
                }
            }
            index++;
        } //end of while
        if(status != 2){
            String message = switch (status) {
                case 1 -> "Expresion o cierre de sentencia esperada";
                case 3 -> "Se esperaban dos puntos";
                case 4, 6 -> "Expresion esperada";
                case 5 -> "coma o cierre de sentencia esperada";
                default -> "Error inesperado";
            };
            Token endTkn = sentence.get(index - 1);
            errorsList.add(new SyntaxError(new Position(endTkn.getColumna() + endTkn.length(), endTkn.getLine()),
                    message));
        }else if(index<sentence.size() && status == 2){
            errorsList.add(new SyntaxError(sentence.get(index).getPosition(),
                    "Codigo inesperado a la derecha"));
        }
    }
    private void analiceSubBlock(List<Token> tokens, int bigIdentation) {
        String typeTkn = tokens.get(currentNoTkn).getSubType();
        if (!typeTkn.equals("elif") && !typeTkn.equals("else")) {
            int endOfBlock = separator.findEndOfBlock(tokens, currentNoTkn, bigIdentation, errors);
            analiceBlock(endOfBlock);
        }
        currentNoTkn--;
    }

    private int validateOneLine(int init, int fin, List<Token> tokens) {
        int line = tokens.get(init).getLine();
        int i = init;
        while (i < tokens.size() && i < fin) {
            int currentLine = tokens.get(i).getLine();
            if (currentLine != line) {
                errors.add(new SyntaxError(tokens.get(i).getPosition(), "IdentationError"));
                break;
            }
            i++;
        }
        return i++;
    }

    
}
