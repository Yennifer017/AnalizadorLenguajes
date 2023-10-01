
package lenguajes.proyectolenguajesydl.parser.elements;

import java.util.List;
import lenguajes.proyectolenguajesydl.lexer.Token;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 *
 * @author yenni
 */
@AllArgsConstructor @Getter
public class Function {
    private Token token;
    private List<String> parameters;
    private int index;
    
    public String getName(){
        return token.getContenido();
    }
}
