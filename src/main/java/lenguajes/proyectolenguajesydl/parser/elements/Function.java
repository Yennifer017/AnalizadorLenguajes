
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
    private Token token;
    private List<String> parameters;
    private int index;
    private List<Token> usos;
            
    public Function(Token token, List<String> parameters, int index) {
        this.token = token;
        this.parameters = parameters;
        this.index = index;
    }
    
    public String getName(){
        return token.getContenido();
    }
    public void setUsos(List<Token> usos){
        this.usos = usos;
    }
}
