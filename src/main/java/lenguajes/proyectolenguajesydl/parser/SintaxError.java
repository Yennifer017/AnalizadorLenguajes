
package lenguajes.proyectolenguajesydl.parser;

import lenguajes.proyectolenguajesydl.util.Position;
import lombok.*;

/**
 *
 * @author yenni
 */
@AllArgsConstructor @Getter
public class SintaxError {
    private Position position;
    private String details;
}
