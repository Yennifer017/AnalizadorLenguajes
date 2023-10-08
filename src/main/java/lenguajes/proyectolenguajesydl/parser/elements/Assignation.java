
package lenguajes.proyectolenguajesydl.parser.elements;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 *
 * @author yenni
 */
@AllArgsConstructor @Getter
public class Assignation {
    public static final String UNKNOWN = "desconocido", NO_APPLY = "no aplica";
    private int identationLevel;
    private String name, type, valor;
    private int linea, columna;
}
