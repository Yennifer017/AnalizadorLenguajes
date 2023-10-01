
package lenguajes.proyectolenguajesydl.parser.elements;

import lenguajes.proyectolenguajesydl.lexer.Token;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 *
 * @author yenni
 */
@AllArgsConstructor @Getter
public class Variable {
    private Token token;
    private String alcance;
}
