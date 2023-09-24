
package lenguajes.proyectolenguajesydl.parser;

import lenguajes.proyectolenguajesydl.util.Position;
import lombok.*;

/**
 *
 * @author yenni
 */
@AllArgsConstructor @Getter
public class SyntaxError {
    private Position position;
    private String details;
}
