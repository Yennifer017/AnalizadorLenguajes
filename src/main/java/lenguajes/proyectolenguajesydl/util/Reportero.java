
package lenguajes.proyectolenguajesydl.util;

import java.util.List;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import lenguajes.proyectolenguajesydl.parser.elements.Function;
import lenguajes.proyectolenguajesydl.lexer.Lexer;
import lenguajes.proyectolenguajesydl.lexer.Token;
import lenguajes.proyectolenguajesydl.parser.Registrador;
import lenguajes.proyectolenguajesydl.parser.Registro;
import lenguajes.proyectolenguajesydl.parser.SyntaxError;
import lenguajes.proyectolenguajesydl.parser.SyntaxException;
import lenguajes.proyectolenguajesydl.parser.elements.Assignation;

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
    private Object[] getDataRow(List<Function> functions, List<Token> tokens, int row){
        Object[] data = new Object[5];
        data[0] = tokens.get(functions.get(row).getIndexStart()).getLine() +1;
        data[1] = tokens.get(functions.get(row).getIndexStart()).getColumna() +1;
        data[2] = tokens.get(functions.get(row).getIndexStart()+1).getContenido();
        data[3] = functions.get(row).getParameters();
        data[4] = functions.get(row).getUsos().size();
        return data;
    }
    private Object[] getDataInstructions(List<Token> tokens, List<Registro> registro, int row){
        Object[] data = new Object[4];
        data[0] = registro.get(row).getIdentationLevel();
        data[1] = tokens.get(registro.get(row).getIndexInList()).getLine() + 1;
        data[2] = tokens.get(registro.get(row).getIndexInList()).getColumna() + 1;
        data[3] = registro.get(row).getName();
        return data;
    }
    private Object[] getDataSyntaxE(List<SyntaxError> errors, int row){
        Object[] data = new Object[3];
        data[0] = errors.get(row).getPosition().getFila() + 1;
        data[1] = errors.get(row).getPosition().getColumna() + 1;
        data[2] = errors.get(row).getDetails();
        return data;
    }
    private Object[] getDataLexE(List<Token> errors, int row){
        Object[] data = new Object[3];
        data[0] = errors.get(row).getPosition().getFila() + 1;
        data[1] = errors.get(row).getPosition().getColumna() + 1;
        data[2] = errors.get(row).getContenido();
        return data;
    }
    private Object[] getDataSymbolTable(List<Token> tokens, Assignation asignacion,
            boolean includeIndentation){
        Object[] data;
        int index = 0;
        if(includeIndentation){
            data = new Object[6];
            data[index] = asignacion.getIdentationLevel();
            index++;
        }else{
            data = new Object[5];
        }
        data[index] = asignacion.getName();
        data[index+1] = asignacion.getType();
        data[index+2] = asignacion.getValor();
        data[index+3] = asignacion.getPosition().getFila()+ 1;
        data[index+4] = asignacion.getPosition().getColumna() + 1;
        
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
    
    public void setFunctionsRep(JTable table, Registrador registrador) throws SyntaxException{
        clearTable(table, false);
        List<Function> functions = registrador.getFunctions();
        DefaultTableModel tb = (DefaultTableModel) table.getModel();
        for (int i = 0; i < functions.size(); i++) {
            tb.addRow(getDataRow(functions, registrador.getListTokens(), i));
        }
    }
    public void setSyntaxErrorRep(JTable table, List<SyntaxError> errors){
        clearTable(table, false);
        DefaultTableModel tb = (DefaultTableModel) table.getModel();
        for (int i = 0; i < errors.size(); i++) {
            tb.addRow(getDataSyntaxE(errors, i));
        }
    }
    public void setLexErrorRep(JTable table, List<Token> errors){
        clearTable(table, false);
        DefaultTableModel tb = (DefaultTableModel) table.getModel();
        for (int i = 0; i < errors.size(); i++) {
            tb.addRow(getDataLexE(errors, i));
        }
    }
    public void setListInstRep(JTable table, Registrador registrador) throws SyntaxException{
        clearTable(table, false);
        List<Registro> registros = registrador.getRegistros();
        DefaultTableModel tb = (DefaultTableModel) table.getModel();
        for (int i = 0; i < registros.size(); i++) {
            tb.addRow(getDataInstructions(registrador.getListTokens(), registros, i));
        }
    }
    public void setSymbolTable(JTable table, Registrador registrador, boolean includeIndentation) 
            throws SyntaxException{
        clearTable(table, false);
        List<Assignation> asignaciones = registrador.getDataForSymbolTable();
        /*List<Registro> registros = registrador.getRegistrosForST();*/
        DefaultTableModel tb = (DefaultTableModel) table.getModel();
        for (Assignation asignacion : asignaciones) {
            tb.addRow(getDataSymbolTable(registrador.getListTokens(), asignacion, includeIndentation));
        }
    }
    
}
