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

    public ExpressionVerificator() {
    }

    public ExpressionVerificator(List<SyntaxError> errors) {
        this.errors = errors;
        stack = new Stack<>();
        ex = new Regex();
        noTkn = 0;
    }

    public void validate(List<Token> expression) {
        stack.clear();
        noTkn = 0;
        this.expression = expression;
        stack.push("E");
        validateExpression();
        if (!stack.isEmpty()) {
            //while (!stack.isEmpty()) {
                String element = stack.pop();
                addErrors(element);
            //}
        }
    }

    private void addErrors(String element) {
        System.out.println(element + "elemento conflictivo");
        String message = switch (element) {
            case "E" -> "Expresion esperada";
            case "O" -> "Operacion (aritmetica, comparativa, otra) esperada";
            case "E+" -> "Codigo a la derecha no esperado";
            case "SR+" -> "Separador de cierre inesperado";
            case "pL" -> "Se esperaba un token de cierre";
            case "n" -> "Se esperaba un numero (int o float) o un identificador";   
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
        int tknRest = expression.size() - noTkn;
        boolean read = true;
        while (tknRest > 1 && read) {
            String type = expression.get(noTkn).getSubType();
            switch (type) {
                case "Identificador", "int", "float", "Cadena", "True", "False" -> {
                    read = validateValues();
                }
                case "pL" -> {
                    read = validateSeparatorL();
                }
                case "pR" -> {
                    read = validateSeparatorR();
                }
                case "suma", "resta" -> {
                    read = validateNumbers();
                }
                case "exponente", "division", "modulo", "multiplicacion" ->{
                    read = validateOperations();
                }
                default -> {
                    read = false;
                    noTkn--;
                }
            }

            noTkn++;
            tknRest = expression.size() - noTkn;
        }
        //sobra un token
        validateLastTkn();
        
    }
    private void validateLastTkn(){
        if(!stack.isEmpty() && noTkn < expression.size()){
            Token tkn = expression.get(noTkn);
            switch (tkn.getSubType()) {

                case "int", "float", "Cadena", "True", "False", "Identificador":
                    if(stack.peek().equals("E")){
                        stack.pop();
                        noTkn++;
                    }
                    break;
                case "pR":
                    if(ex.isComplementario(stack.peek(), tkn.getSubType())){
                        stack.pop();
                        noTkn++;
                    }
                    
                    break;
                default:
                    
            }
        }else if(noTkn != expression.size()){
            stack.push("E+");
        }
    }
    
    
    private boolean validateValues(){
        if(expressionOK()){ //si se esperaba una expresion
            noTkn++;
            String nextT = expression.get(noTkn).getSubType();
            switch (nextT) {
                case "pR" -> { //cuando se cierran parentesis
                    return validateSeparatorR();
                }
                default -> {
                    return isOperation();
                }
            }
            
        }
        return false;
    }
    
    
    private boolean isOperation() {
        stack.push("E");
        stack.push("O");
        switch (expression.get(noTkn).getType()) {
            case "Comparativo", "Aritmetico", "Logico" -> {
                stack.pop();
                return true;
            }
            default -> noTkn--;
        }
        return false;
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
        if(noTkn == 0){ //si viene de primero
            stack.pop();
            stack.push("n");
            noTkn++;
            switch (expression.get(noTkn).getSubType()) {
                case "int", "float", "Identificador" -> {
                    stack.pop();
                    return true;
                }
                default -> {
                    noTkn--;
                    return false;
                }
            }
        }else{
            if(!stack.isEmpty()){
                if(stack.peek().equals("E")){
                    stack.pop();
                }else{
                    return false;
                }
            }
            stack.push("E");
            return true;
        }
        
    }
    
    private boolean validateOperations(){
        if(noTkn != 0){ //que no sea el inicial
            Token anterior = expression.get(noTkn-1);
            switch (anterior.getSubType()) {
                case "exponente", "division", "modulo", "multiplicacion":
                    return false;
                default:
                    stack.push("E");
                    return true;
            }
        }
        return false;
    }
}