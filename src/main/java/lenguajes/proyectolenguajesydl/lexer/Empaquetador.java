
package lenguajes.proyectolenguajesydl.lexer;

import java.util.ArrayList;
import java.util.List;
import lenguajes.proyectolenguajesydl.parser.Parser;
import lenguajes.proyectolenguajesydl.parser.Separator;
import lenguajes.proyectolenguajesydl.parser.elements.Function;

/**
 *
 * @author yenni
 */
public class Empaquetador {

    private Parser parser;
    private Lexer lexer;
    private Separator separator;
    
    public Empaquetador(Lexer lexer, Parser parser) {
        this.lexer = lexer;
        this.parser = parser;
        this.separator = new Separator();
    }
    
    public List<Function> getFunctions(){
        if(parser.getErrors().isEmpty()){ //solo si no hay errores sintacticos
            List<Token> tokens = lexer.getTokens();
            List<Function> functions = new ArrayList<>();
            for (int i = 0; i < tokens.size(); i++) {
                if(tokens.get(i).getSubType().equals("def")){
                    i++;
                    int index = i;
                    Token token = tokens.get(i);
                    //find params
                    i+=2;
                    int endParam = separator.findEndOfExpression(tokens, i, "pR", 
                            tokens.get(i).getLine());
                    List<String> params = new ArrayList<>(); 
                    while (i<endParam) {                        
                        if(tokens.get(i).getSubType().equals("Identificador")){
                            params.add(tokens.get(i).getContenido());
                        }
                        i+=2;
                    }
                    functions.add(new Function(token, params, index));
                }
            }
            return functions;
        }else{
            throw new AssertionError();
        }
    }
    
    
    
    
}
