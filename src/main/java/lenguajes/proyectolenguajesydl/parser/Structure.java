
package lenguajes.proyectolenguajesydl.parser;


/**
 *
 * @author yenni
 */
public class Structure {

    
    public static final int USE_FUNCTION = 0, ARRAY = 1, LIST = 2;
    
    public String getDelimitadorL(int type){
        switch (type) {
            case 0:
                return "pL";
            case 1:
                return "cL";
            case 2:
                return "lL";
            default:
                throw new AssertionError();
        }
    }
    public String getDelimitadorR(int type){
        switch (type) {
            case 0:
                return "pR";
            case 1:
                return "cR";
            case 2:
                return "lR";
            default:
                throw new AssertionError();
        }
    }
}
