package lenguajes.proyectolenguajesydl.parser;

import java.util.List;
import java.util.Stack;
import lenguajes.proyectolenguajesydl.lexer.Regex;
import lenguajes.proyectolenguajesydl.lexer.Token;
import lenguajes.proyectolenguajesydl.util.Position;

/**
 *
 * @author yenni
 */
public class ExpressionVerificator {

    private List<SyntaxError> errors;
    private Stack<String> stack = new Stack<>();
    private int noTkn;
    private List<Token> expression;
    private Regex ex;
    private Separator separator;
    private Parser parser;
    protected ExpressionVerificator(Parser parser) {
        this.errors = parser.getErrors();
        this.parser = parser;
        stack = new Stack<>();
        ex = new Regex();
        noTkn = 0;
        separator = new Separator();
    }

    public int validate(List<Token> tokens, String typeTknEnd, int currentLine, int init) {
        String[] delimtador = {typeTknEnd};
        return validate(tokens, delimtador , currentLine, init);
    }
    
    public int validate(List<Token> tokens, String[] delimitadors, int currentLine, int init) {
        int end = separator.findEndOfExpression(tokens, init, delimitadors, currentLine);
        System.out.println("fin de expresion" + end);
        if (init == end) { //cuando la expresion no exite
            Token tkn = tokens.get(init);
            errors.add(new SyntaxError(new Position(tkn.getColumna(),
                    currentLine), "Se esperaba una expresion"));
        } else {
            validate(tokens.subList(init, end));
        }
        return end - 1; //no perder la secuencia incluso si ocurre algun error
    }
    
    private void validate(List<Token> expression) {
        stack.clear();
        noTkn = 0;
        this.expression = expression;
        stack.push("E");
        validateExpression();
        if (!stack.isEmpty()) {
            //while (!stack.isEmpty()) {
                String element = stack.pop();
            try {
                addErrors(element);
            } catch (Exception e) {
                System.out.println("Ocurrio una excepcion inesperada");
            }
        }
    }

    private void addErrors(String element) {
        System.out.println(element + "elemento conflictivo");
        String message = switch (element) {
            case "E" -> "Expresion esperada";
            case "E+" -> "Codigo a la derecha no esperado, se necesita un conector u operacion";
            case "SR+" -> "Separador de cierre inesperado";
            case "pL" -> "Se esperaba un token de cierre"; 
            case "e" -> "Se esperaba un else para el operador ternario"; 
            default -> "Error inesperado";
        };
        Token tkn;
        if(noTkn>0){
            tkn = expression.get(noTkn-1);
            errors.add(new SyntaxError(new Position(tkn.getColumna() + tkn.length(), tkn.getLine()),
                        message));
        }else{
            tkn = expression.get(noTkn);
            errors.add(new SyntaxError(new Position(tkn.getColumna(), tkn.getLine()),
                        message));
        }
    }

    private void validateExpression() {
        if (noTkn < expression.size()) {
            int tknsRestantes = expression.size() - noTkn;
            if (tknsRestantes == 1) {
                validateOneTkn();
            } else if (tknsRestantes > 1) {
                validateTkns();
            }
        }
    }

    private boolean validateOneTkn() {
        String type;
        try {
            type = expression.get(noTkn).getSubType();
        } catch (Exception e) {
            return false;
        }
        switch (type) {
            case "int", "float", "Cadena", "True", "False", "Identificador" -> {
                try {
                    stack.pop();
                    return true;
                } catch (Exception e) {
                    System.out.println("una excepcion ocurrio con la pila");
                }
            }
        }
        return false;
    }

    private void validateTkns() {
        boolean read = true;
        while (noTkn<expression.size() && read) {
            String subType = expression.get(noTkn).getSubType();
            switch (subType) {
                case "Identificador", "int", "float", "Cadena", "True", "False" -> 
                    read = validateValues();
                case "pL" -> 
                    read = validateSeparatorL();
                case "pR" -> 
                    read = validateSeparatorR();
                case "suma", "resta" -> 
                    read = validateNumbers();
                case "exponente", "division", "modulo", "multiplicacion", "and", "or",
                        "igual", "diferente", "mayor_que", "menor_que", "mayor_o_igual_que", "menor_o_igual_que"->
                    read = validateOperations();
                case "not" ->
                    read = validateNegation();
                case "cL" -> 
                    read = validateArray();
                case "lL" ->
                    read = validateDictionary();
                case "if", "else" ->
                    read = validateTernaryOp();
                default -> {
                    read = false;
                    noTkn--;
                }
            }
            noTkn++;
        } //end of while
        if(noTkn != expression.size()){
            if(stack.isEmpty()){
                stack.push("E+");
            }
        }
    }
    private boolean validateValues(){
        if (expressionOK()) { //si se esperaba una expresion
            if (expression.get(noTkn).getSubType().equals("Identificador")) {
                String[] delimitador = new String[1];
                try {
                    int type;
                    switch (expression.get(noTkn + 1).getSubType()) {
                        case "pL" -> {
                            type = Structure.USE_FUNCTION;
                            delimitador[0] = "pR";
                        }
                        case "cL" -> {
                            type = Structure.ARRAY;
                            delimitador[0] = "cR";
                        }
                        default -> {
                            return true;
                        }
                    }
                    noTkn++;
                    int end = separator.finEndOfExpressionIncludeSeparator(expression, noTkn,
                            delimitador, expression.get(noTkn - 1).getLine());
                    List<Token> sentence = expression.subList(noTkn, end);
                    parser.validateStructure(sentence,
                            type, errors);
                    noTkn = end - 1;
                } catch (IndexOutOfBoundsException e) {
                    System.out.println("excepcion controlada");
                }
                return true;
            }
            return true; 
        }else{
            noTkn--;
            return false;
        }
    }
    
    private boolean validateSeparatorL(){
        if(expressionOK()){
            stack.push(expression.get(noTkn).getSubType());
            stack.push("E");
            return true;
        }
        noTkn--;
        return false;
    }
    private boolean validateSeparatorR(){
        if(!stack.isEmpty()){
            if(ex.isComplementario(stack.peek(), expression.get(noTkn).getSubType())){
                stack.pop();
                return true;
            }
        }
        noTkn--; //nos deja en la misma posicion
        stack.push("SR+");
        return false;
    }
    
    private boolean expressionOK(){
        if(!stack.isEmpty()){
            if(stack.peek().equals("E")){
                stack.pop();
                return true;
            }else{
                stack.push("E+");
                return false;
            }
        }else{
            stack.push("E+");
            System.out.println("una excepcion ocurrio con la pila");
            return false;
        }
    }
    
    private boolean validateNumbers(){
        if (!stack.isEmpty()) {
            if (stack.peek().equals("E")) {
                stack.pop();
            }
        }
        stack.push("E");
        return true;
    }
    
    private boolean validateOperations(){
        if(noTkn != 0){ //que no sea el inicial
            Token anterior = expression.get(noTkn-1);
            switch (anterior.getSubType()) {
                case "exponente", "division", "modulo", "multiplicacion", "and", "or",
                        "igual", "diferente", "mayor_que", "menor_que", "mayor_o_igual_que", "menor_o_igual_que":
                    noTkn--; //dejar en la misma posicion
                    return false;
                default:
                    stack.push("E");
                    return true;
            }
        }
        noTkn--;
        return false;
    }
    
    private boolean validateArray() {
        if ((noTkn != 0 && expression.get(noTkn - 1).getSubType().equals("cR"))
                || expressionOK()) {
            String[] delimitador = {"cR"};
            int end = separator.finEndOfExpressionIncludeSeparator(expression, noTkn,
                    delimitador, expression.get(noTkn).getLine());
            parser.validateStructure(expression.subList(noTkn, end), Structure.ARRAY,
                    errors);
            noTkn = end - 1;
            return true;
        }
        return false;
    }
    private boolean validateDictionary(){
        if(expressionOK()){
            String[] delimitador = {"lR"};
            int end = separator.finEndOfExpressionIncludeSeparator(expression, noTkn,
                    delimitador, expression.get(noTkn).getLine());
            parser.validateDictionary(expression.subList(noTkn, end), errors);
            noTkn = end - 1;
            return true;
        }
        return false;
    }
    
    private boolean validateTernaryOp(){
        switch (expression.get(noTkn).getSubType()) {
            case "if" -> {
                if (stack.isEmpty() || !stack.peek().equals("E")) {
                    stack.push("E");
                    stack.push("e");
                    stack.push("E");
                    return true;
                }
            }
            case "else" -> {
                if (!stack.isEmpty() && stack.peek().equals("e")){
                    stack.pop();
                    return true;
                }
            }
            default -> throw new AssertionError();
        }
        noTkn--;
        return false;
    }
    private boolean validateNegation(){
        if(expressionOK()){
            stack.push("E");
            return true;
        }else{
            noTkn--;
            return false;
        }
    }
    
}