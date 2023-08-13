
package lenguajes.proyectolenguajesydl.util;

import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import lenguajes.proyectolenguajesydl.analizadorlexico.Lexer;

/**
 *
 * @author yenni
 */
public class Reportero {
    public Object[][] getAnalisisLexico(Lexer lexer){
        //Object[filas][columnas]
        int filas = lexer.getTokens().size();
        if(filas<18){
            filas =18;
        }
        Object[][] reporte = new Object[filas][5];
        for (int j = 0; j < lexer.getTokens().size(); j++) { //por cada fila hacer...
            reporte[j][0] = lexer.getTokens().get(j).getType();
            reporte[j][1] = lexer.getTokens().get(j).getPatron();
            reporte[j][2] = lexer.getTokens().get(j).getContenido();
            reporte[j][3] = lexer.getTokens().get(j).getFila() + 1;
            reporte[j][4] = lexer.getTokens().get(j).getColumna() + 1;
        }
        return reporte;
    }
    private Object[] getDataRow(Lexer lexer, int row) throws IndexOutOfBoundsException{
        Object[] data = new Object[5];
        data[0] = lexer.getTokens().get(row).getType();
        data[1] = lexer.getTokens().get(row).getPatron();
        data[2] = lexer.getTokens().get(row).getContenido();
        data[3] = lexer.getTokens().get(row).getFila() + 1;
        data[4] = lexer.getTokens().get(row).getColumna() + 1;
        return data;
    }
    private Object[] getNullRow(int columas){
        return new Object[columas];
    }
    public void clearTable(JTable table, boolean rellenar){
        DefaultTableModel tb = (DefaultTableModel) table.getModel();
        int a = table.getRowCount()-1;
        for (int i = a; i >= 0; i--) {          
            tb.removeRow(tb.getRowCount()-1);
        }
        if(rellenar){
            for (int i = 0; i < 18; i++) {
                tb.addRow(getNullRow(5));
            }
        }
    }
    public void setReporte(JTable table, Lexer lexer){
        clearTable(table, false);
        DefaultTableModel tb = (DefaultTableModel) table.getModel();
        for (int i = 0; i < lexer.getTokens().size(); i++) {
            tb.addRow(getDataRow(lexer, i));
        }
        if(lexer.getTokens().size()<18){
            for (int i = lexer.getTokens().size(); i < 18; i++) {
                tb.addRow(getNullRow(5));
            }
        }
    }
    
}
