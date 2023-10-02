
package lenguajes.proyectolenguajesydl.lexer;

/**
 *
 * @author yenni
 */
public enum TypeTkn {
    IDENTIFIER("Identificador", "(([a-zA-Z]|_)+)[.]*"),
    INT("int", "[0-9]+"),
    FLOAT("float", "([0-9]+)['.']([0-9]+)"),
    STRING("Cadena", ""),
    COMENTARY("comentario", "[#][.]*"),
    OTHER("otro", "");
    
    private String name;
    private String patron;
    
    private TypeTkn(String name, String patron) {
        this.name = name;
        this.patron = patron;
    }
    
    public String getName(){
        return name;
    }
    public String getPatron(){
        return patron;
    }
    
    
}
