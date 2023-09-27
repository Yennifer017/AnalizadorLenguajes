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
            case "pL" -> "Se esperaba cerrar la expresion cierre";    
            default -> "Error inesperado";
        };
        Token tkn = expression.get(noTkn-1);
        errors.add(new SyntaxError(new Position(tkn.getColumna() + tkn.length(), tkn.getLine()),
                        message));
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
                default -> read = false;
            }

            noTkn++;
            tknRest = expression.size() - noTkn;
        }
        //sobra un token
        validateOneTkn();
        /*if(!stack.isEmpty() && stack.peek().equals("E")){
            validateOneTkn();
        }*/
        
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
}