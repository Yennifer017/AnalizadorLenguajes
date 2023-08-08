
package lenguajes.proyectolenguajesydl.util;

/**
 *
 * @author yenni
 */
public class Position {
    private int columna, fila, index;

    public Position(int columna, int fila, int index) {
        this.columna = columna;
        this.fila = fila;
        this.index = index;
    }

    public int getColumna() {
        return columna;
    }

    public int getFila() {
        return fila;
    }

    public int getIndex() {
        return index;
    }
    
    
}
