
package lenguajes.proyectolenguajesydl.parser.elements;

import java.util.List;
import lenguajes.proyectolenguajesydl.lexer.Token;
import lombok.Getter;

/**
 *
 * @author yenni
 */
@Getter
public class Function {
    private List<String> parameters;
    private int indexStart;
    private List<Token> usos;
            
    public Function(int indexStart, List<String> parameters) {
        this.parameters = parameters;
        this.indexStart = indexStart;
    }
    
    public void setUsos(List<Token> usos){
        this.usos = usos;
    }
}
