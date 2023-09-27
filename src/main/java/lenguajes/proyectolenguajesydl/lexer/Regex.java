
package lenguajes.proyectolenguajesydl.lexer;

/**
 *
 * @author yenni
 */
public class Regex {
    
    public boolean isAlphaUp(char character) {
        return ((character >= 'a' && character <= 'z') || character == '_');
    }

    public boolean isAlphaDown(char character) {
        return (character >= 'A' && character <= 'Z');
    }

    public boolean isNumeric(char character) {
        return (character >= '0' && character <= '9');
    }

    public boolean isIgnoredCharacter(char character) {
        return switch (character) {
            case ' ', 9, '\r', '\n' ->
                true;
            default ->
                false;
        };
    }

    public boolean isCombinable(char character) {
        return switch (character) {
            case '!', '-', '*', '/', '+', '=', '>', '<' ->
                true;
            default ->
                false;
        };
    }

    public boolean isAlphaNumeric(char character) {
        return isAlphaDown(character) || isAlphaUp(character) || isNumeric(character);
    }
    public boolean isLetter(char character){
        return isAlphaDown(character) || isAlphaUp(character);
    }
    
    //regex especificas para este proyecto
    public boolean isOtro(char character){
        return switch (character) {
            case '(', ')', '[', ']', '{', '}', ',', ':', ';' ->
                true;
            default ->
                false;
        };
    }
    
    public boolean isReservada(String palabra){
        char inicial;
        try {
            inicial = palabra.charAt(0); 
        } catch (IndexOutOfBoundsException e) {
            return false;
        }
        return switch (inicial) {
            case 'a' -> switch (palabra) {
                case "as", "assert" -> true;
                default -> false;
            };
            case 'b' -> switch (palabra) {
                case "break" -> true;
                default -> false;
            };
            case 'c' -> switch (palabra) {
                case "class", "continue" -> true;
                default -> false;
            };
            case 'd' -> switch (palabra) {
                case "def", "del" -> true;
                default -> false;
            };
            case 'e' -> switch (palabra) {
                case "elif", "else", "except" -> true;
                default -> false;
            };
            case 'f' -> switch (palabra) {
                case "finally", "for", "from" -> true;
                default -> false;
            };
            case 'g' -> switch (palabra) {
                case "global" -> true;
                default -> false;
            };
            case 'i' -> switch (palabra) {
                case "if", "import", "in", "is" -> true;
                default -> false;
            };
            case 'l' -> switch (palabra) {
                case "lambda"-> true;
                default -> false;
            };
            case 'n' -> switch (palabra) {
                case "None", "nonlocal"-> true;
                default -> false;
            };
            case 'p' -> switch (palabra) {
                case "pass" -> true;
                default -> false;
            };
            case 'r' -> switch (palabra) {
                case "rase", "return" -> true;
                default -> false;
            };
            case 't' -> switch (palabra) {
                case "try" -> true;
                default -> false;
            };
            case 'w' -> switch (palabra) {
                case "while", "with" -> true;
                default -> false;
            };
            case 'y' -> switch (palabra) {
                case "yield"-> true;
                default -> false;
            };
            default -> false;
        };
    }
    
    public boolean isBooleana(String token){
        return switch (token) {
            case "True", "False"-> true;
            default -> false;
        };
    }
    public boolean isLogico(String token){
        return switch (token) {
            case "and", "or", "not"-> true;
            default -> false;
        };
    }
    public boolean isAritmetico(String preTkn){
        return switch (preTkn) {
            case "+", "-", "**", "/", "//", "%", "*" -> true;
            default -> false;
        };
    }
    public boolean isComparativo(String preTkn){
        return switch(preTkn) {
            case "==", "!=", ">", "<", ">=", "<=" -> true;
            default -> false;
        };
    }
    
    public boolean isComplementario(String specificT1, String specificT2){
        return (specificT1.equals("pL") && specificT2.equals("pR")) ||
                (specificT1.equals("cL") && specificT2.equals("cR")) ||
                (specificT1.equals("lL") && specificT2.equals("lR"));
    }
}
