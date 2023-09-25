
package lenguajes.proyectolenguajesydl.lexer;

/**
 *
 * @author yenni
 */
public enum TipoTokenEnum {
    IDENTIFICADOR("Identificador");
    
    public final String name;

    private TipoTokenEnum(String name) {
        this.name = name;
    }
    @Override
    public String toString(){
        return this.name;
    }
}
