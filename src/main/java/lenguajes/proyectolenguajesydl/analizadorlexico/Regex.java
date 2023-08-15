
package lenguajes.proyectolenguajesydl.analizadorlexico;

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
    public boolean isOtro(char character){
        return switch (character) {
            case '(', ')', '[', ']', '{', '}', ',', ':', ';' ->
                true;
            default ->
                false;
        };
    }
}
