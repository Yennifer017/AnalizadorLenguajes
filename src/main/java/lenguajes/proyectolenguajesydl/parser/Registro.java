
package lenguajes.proyectolenguajesydl.parser;

import lenguajes.proyectolenguajesydl.lexer.Token;
import lombok.Getter;

/**
 *
 * @author yenni
 */
@Getter
public class Registro {

    private int identationLevel, indexInList;
    private String name;
            
    public Registro(int identationLevel, int indexInList) {
        this.indexInList = indexInList;
        this.identationLevel = identationLevel;
    }
    public Registro(){
    
    }
    public void setName(String name){
        this.name = name;
    }
    
}
