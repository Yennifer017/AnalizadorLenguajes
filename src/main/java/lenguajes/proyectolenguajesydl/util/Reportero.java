
package lenguajes.proyectolenguajesydl.util;

import lenguajes.proyectolenguajesydl.analizadorlexico.AnalizadorLexico;

/**
 *
 * @author yenni
 */
public class Reportero {
    public Object[][] getAnalisisLexico(AnalizadorLexico lexer){
        int filas = lexer.getTokens().size();
        if(filas<18){
            filas =18;
        }
        Object[][] reporte = new Object[filas][5];
        for (int j = 0; j < lexer.getTokens().size(); j++) { //por cada fila hacer...
            reporte[j][0] = lexer.getTokens().get(j).getType();
            reporte[j][1] = "PATRON AQUI";
            reporte[j][2] = lexer.getTokens().get(j).getContenido();
            reporte[j][3] = lexer.getTokens().get(j).getFila() + 1;
            reporte[j][4] = lexer.getTokens().get(j).getColumna() + 1;
        }
        return reporte;
    }
}
