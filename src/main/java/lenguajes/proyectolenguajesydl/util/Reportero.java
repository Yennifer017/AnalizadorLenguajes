
package lenguajes.proyectolenguajesydl.util;

import java.util.List;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import lenguajes.proyectolenguajesydl.parser.elements.Function;
import lenguajes.proyectolenguajesydl.lexer.Lexer;
import lenguajes.proyectolenguajesydl.lexer.Token;
import lenguajes.proyectolenguajesydl.parser.SyntaxError;
import lenguajes.proyectolenguajesydl.parser.SyntaxException;
import lenguajes.proyectolenguajesydl.parser.elements.Empaquetador;

/**
 *
 * @author yenni
 */
public class Reportero {

    private Object[] getDataRow(Lexer lexer, int row) {
        Object[] data = new Object[5];
        data[0] = lexer.getTokens().get(row).getType();
        data[1] = lexer.getTokens().get(row).getPatron();
        data[2] = lexer.getTokens().get(row).getContenido();
        data[3] = lexer.getTokens().get(row).getLine() + 1;
        data[4] = lexer.getTokens().get(row).getColumna() + 1;
        return data;
    }
    private Object[] getDataRow(List<Function> functions, int row){
        Object[] data = new Object[3];
        data[0] = functions.get(row).getName();
        data[1] = functions.get(row).getParameters();
        data[2] = functions.get(row).getUsos().size();
        return data;
    }
    private Object[] getDataSE(List<SyntaxError> errors, int row){
        Object[] data = new Object[3];
        data[0] = errors.get(row).getPosition().getFila() + 1;
        data[1] = errors.get(row).getPosition().getColumna() + 1;
        data[2] = errors.get(row).getDetails();
        return data;
    }
    private Object[] getDataLE(List<Token> errors, int row){
        Object[] data = new Object[3];
        data[0] = errors.get(row).getPosition().getFila() + 1;
        data[1] = errors.get(row).getPosition().getColumna() + 1;
        data[2] = errors.get(row).getContenido();
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
                tb.addRow(getNullRow(table.getColumnCount()));
            }
        }
    }
    public void setTokensReporte(JTable table, Lexer lexer){
        clearTable(table, false);
        DefaultTableModel tb = (DefaultTableModel) table.getModel();
        for (int i = 0; i < lexer.getTokens().size(); i++) {
            tb.addRow(getDataRow(lexer, i));
        }
        if(lexer.getTokens().size()<18){
            for (int i = lexer.getTokens().size(); i < 18; i++) {
                tb.addRow(getNullRow(table.getColumnCount()));
            }
        }
    }
    
    public void setFunctionsRep(JTable table, Empaquetador empaquetador) throws SyntaxException{
        clearTable(table, false);
        List<Function> functions = empaquetador.getFunctions();
        DefaultTableModel tb = (DefaultTableModel) table.getModel();
        for (int i = 0; i < functions.size(); i++) {
            tb.addRow(getDataRow(functions, i));
        }
    }
    public void setSyntaxErrorRep(JTable table, List<SyntaxError> errors){
        clearTable(table, false);
        DefaultTableModel tb = (DefaultTableModel) table.getModel();
        for (int i = 0; i < errors.size(); i++) {
            tb.addRow(getDataSE(errors, i));
        }
    }
    public void setLexErrorRep(JTable table, List<Token> errors){
        clearTable(table, false);
        DefaultTableModel tb = (DefaultTableModel) table.getModel();
        for (int i = 0; i < errors.size(); i++) {
            tb.addRow(getDataLE(errors, i));
        }
    }
    
}
