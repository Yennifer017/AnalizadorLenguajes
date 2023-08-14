package lenguajes.proyectolenguajesydl;

import java.awt.event.*;
import java.io.File;
import javax.swing.*;
import lenguajes.proyectolenguajesydl.util.*;
import lenguajes.proyectolenguajesydl.analizadorlexico.*;

/**
 *
 * @author yenni
 */
public class InterfazUsuario extends javax.swing.JFrame {

    NumberLine numEditor, numDisAnalisis;
    private Archivo archivo;
    private Lexer lexer;
    private Pintor pintor; 
    private Graficador graficador;
    private final String FILE_NAME, FILE_EXTENSION;
    private int counterFile;
    private String currentPath;
    Reportero rep;
    /**
     * Creates new form InterfazUsuario
     */
    public InterfazUsuario() {
        initComponents();
        this.setLocationRelativeTo(null);
        initNumeracion();
        archivo = new Archivo();
        lexer = new Lexer();
        pintor = new Pintor(numEditor, lexer);
        rep = new Reportero();
        graficador = new Graficador();
        FILE_NAME = "graph";
        FILE_EXTENSION = ".png";
        counterFile = 0;
        initStyle();
        initTable();
        initButtons();
        currentPath = "";
    }
    private void initNumeracion(){
        numEditor = new NumberLine(editor);
        scrollEditor.setRowHeaderView(numEditor);
        numDisAnalisis = new NumberLine(displayAnalisis);
        scrollDisAnalisis.setRowHeaderView(numDisAnalisis);
        numEditor.updateColumna(displayC);
    }
    private void initStyle(){
        JTextPane txt = new JTextPane(pintor.getDefStyleDoc());
        String temp = editor.getText();
        editor.setStyledDocument(txt.getStyledDocument());
        editor.setText(temp);
    }
    private void initButtons(){
        bSave.setToolTipText("""
                             Guardar el documento abierto con los cambios correspondientes. 
                             Si no se ha abierto ningun documento, permite crear uno""");
        bClear.setToolTipText("Limpia el editor asi como los analisis hechos");
        bGraph.setToolTipText("""
                              Graficar el token seleccionado, si se selecciona mas de uno
                              se graficara el primero""");
        bSaveAs.setToolTipText("Guardar el codigo en un nuevo archivo");
    }
    private void initTable(){
        displayReporte.getTableHeader().setReorderingAllowed(false) ;
        displayReporte.addMouseListener(new MouseAdapter(){
            @Override
            public void mousePressed(MouseEvent mouseE){
                if(mouseE.getClickCount() == 1){
                    try {
                        String typeTkn = displayReporte.getValueAt(displayReporte.getSelectedRow(), 0).toString();
                        String lexema = displayReporte.getValueAt(displayReporte.getSelectedRow(), 2).toString();
                        archivo.deleteFile(FILE_NAME + counterFile + FILE_EXTENSION); //se borra el viejo
                        graficador.graficar(new Token(lexema, typeTkn), FILE_NAME + counterFile, FILE_EXTENSION);
                    } catch (NullPointerException e) {
                        System.out.println("error");
                    }
                     
                }
            }
        });
    }
    private void close(){
        archivo.deleteFile(FILE_NAME + counterFile  + FILE_EXTENSION);
        archivo.deleteFile(FILE_NAME + counterFile  + ".dot");
        System.exit(0);
    }
    private String getMssCTkn(){
        String typeTkn = displayReporte.getValueAt(displayReporte.getSelectedRow(), 0).toString();
        String lexema = displayReporte.getValueAt(displayReporte.getSelectedRow(), 2).toString();
        String patron = displayReporte.getValueAt(displayReporte.getSelectedRow(), 1).toString();
        String linea = displayReporte.getValueAt(displayReporte.getSelectedRow(), 3).toString();
        String columna = displayReporte.getValueAt(displayReporte.getSelectedRow(), 4).toString();
        return "Tipo de token: " + typeTkn + "\nPatron: " + patron + "\nLexema: " + lexema +
                    "\n\nLinea: " + linea + "\nColumna: " + columna;
    }
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        bOpenFile = new javax.swing.JButton();
        bSave = new javax.swing.JButton();
        bHelp = new javax.swing.JButton();
        bCreditos = new javax.swing.JButton();
        title = new javax.swing.JLabel();
        bSaveAs = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        bClear = new javax.swing.JButton();
        bLexico = new javax.swing.JButton();
        scrollEditor = new javax.swing.JScrollPane();
        editor = new javax.swing.JTextPane();
        scrollDisAnalisis = new javax.swing.JScrollPane();
        displayAnalisis = new javax.swing.JTextPane();
        lCol = new javax.swing.JLabel();
        displayC = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        displayReporte = new javax.swing.JTable();
        bGraph = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        setResizable(false);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel1.setBackground(new java.awt.Color(7, 7, 110));

        bOpenFile.setBackground(new java.awt.Color(7, 7, 110));
        bOpenFile.setFont(new java.awt.Font("Dialog", 1, 12)); // NOI18N
        bOpenFile.setForeground(new java.awt.Color(255, 255, 255));
        bOpenFile.setText("Abrir archivo");
        bOpenFile.setFocusable(false);
        bOpenFile.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bOpenFileActionPerformed(evt);
            }
        });

        bSave.setBackground(new java.awt.Color(7, 7, 110));
        bSave.setFont(new java.awt.Font("Dialog", 1, 12)); // NOI18N
        bSave.setForeground(new java.awt.Color(255, 255, 255));
        bSave.setText("Guardar");
        bSave.setFocusable(false);
        bSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bSaveActionPerformed(evt);
            }
        });

        bHelp.setBackground(new java.awt.Color(7, 7, 110));
        bHelp.setFont(new java.awt.Font("Dialog", 1, 12)); // NOI18N
        bHelp.setForeground(new java.awt.Color(255, 255, 255));
        bHelp.setText("Ayuda");
        bHelp.setFocusable(false);
        bHelp.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bHelpActionPerformed(evt);
            }
        });

        bCreditos.setBackground(new java.awt.Color(7, 7, 110));
        bCreditos.setFont(new java.awt.Font("Dialog", 1, 12)); // NOI18N
        bCreditos.setForeground(new java.awt.Color(255, 255, 255));
        bCreditos.setText("Creditos");
        bCreditos.setFocusable(false);
        bCreditos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bCreditosActionPerformed(evt);
            }
        });

        title.setFont(new java.awt.Font("Magneto", 1, 36)); // NOI18N
        title.setForeground(new java.awt.Color(255, 255, 255));
        title.setText("Parser - pY");

        bSaveAs.setBackground(new java.awt.Color(7, 7, 110));
        bSaveAs.setFont(new java.awt.Font("Dialog", 1, 12)); // NOI18N
        bSaveAs.setForeground(new java.awt.Color(255, 255, 255));
        bSaveAs.setText("Guardar como");
        bSaveAs.setFocusable(false);
        bSaveAs.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bSaveAsActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addComponent(bOpenFile, javax.swing.GroupLayout.PREFERRED_SIZE, 126, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(bSave, javax.swing.GroupLayout.PREFERRED_SIZE, 116, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(bSaveAs, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(94, 94, 94)
                .addComponent(title, javax.swing.GroupLayout.PREFERRED_SIZE, 314, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 106, Short.MAX_VALUE)
                .addComponent(bHelp, javax.swing.GroupLayout.PREFERRED_SIZE, 141, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(bCreditos, javax.swing.GroupLayout.PREFERRED_SIZE, 141, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(59, 59, 59))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(bOpenFile, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 37, Short.MAX_VALUE)
                    .addComponent(bHelp, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(bCreditos, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(bSaveAs, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(bSave, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(12, 12, 12))
            .addComponent(title, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        getContentPane().add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1300, 60));

        jPanel2.setBackground(new java.awt.Color(0, 0, 0));
        jPanel2.setForeground(new java.awt.Color(255, 255, 255));
        jPanel2.setFont(new java.awt.Font("Dialog", 0, 14)); // NOI18N

        bClear.setBackground(new java.awt.Color(7, 7, 110));
        bClear.setFont(new java.awt.Font("Dialog", 1, 12)); // NOI18N
        bClear.setForeground(new java.awt.Color(255, 255, 255));
        bClear.setText("Limpiar");
        bClear.setFocusable(false);
        bClear.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bClearActionPerformed(evt);
            }
        });

        bLexico.setBackground(new java.awt.Color(7, 7, 110));
        bLexico.setFont(new java.awt.Font("Dialog", 1, 12)); // NOI18N
        bLexico.setForeground(new java.awt.Color(255, 255, 255));
        bLexico.setText("Analisis Lexico");
        bLexico.setFocusable(false);
        bLexico.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bLexicoActionPerformed(evt);
            }
        });

        scrollEditor.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        editor.setBackground(new java.awt.Color(0, 0, 44));
        editor.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(51, 51, 51)));
        editor.setFont(new java.awt.Font("Dialog", 0, 17)); // NOI18N
        editor.setForeground(new java.awt.Color(0, 153, 204));
        editor.setCaretColor(new java.awt.Color(255, 255, 255));
        editor.addCaretListener(new javax.swing.event.CaretListener() {
            public void caretUpdate(javax.swing.event.CaretEvent evt) {
                editorCaretUpdate(evt);
            }
        });
        editor.addInputMethodListener(new java.awt.event.InputMethodListener() {
            public void caretPositionChanged(java.awt.event.InputMethodEvent evt) {
                editorCaretPositionChanged(evt);
            }
            public void inputMethodTextChanged(java.awt.event.InputMethodEvent evt) {
            }
        });
        editor.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                editorKeyTyped(evt);
            }
        });
        scrollEditor.setViewportView(editor);

        displayAnalisis.setEditable(false);
        displayAnalisis.setBackground(new java.awt.Color(0, 0, 44));
        displayAnalisis.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(51, 51, 51)));
        displayAnalisis.setFont(new java.awt.Font("Dialog", 0, 14)); // NOI18N
        displayAnalisis.setForeground(new java.awt.Color(255, 255, 255));
        displayAnalisis.setCaretColor(new java.awt.Color(255, 255, 255));
        scrollDisAnalisis.setViewportView(displayAnalisis);

        lCol.setFont(new java.awt.Font("Dialog", 0, 14)); // NOI18N
        lCol.setForeground(new java.awt.Color(204, 204, 204));
        lCol.setText("Columna: ");

        displayC.setFont(new java.awt.Font("Dialog", 0, 14)); // NOI18N
        displayC.setForeground(new java.awt.Color(204, 204, 204));
        displayC.setText("0000");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(22, 22, 22)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(scrollEditor)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(lCol, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(displayC, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(bLexico, javax.swing.GroupLayout.DEFAULT_SIZE, 142, Short.MAX_VALUE)
                            .addComponent(bClear, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(18, 18, 18)
                        .addComponent(scrollDisAnalisis, javax.swing.GroupLayout.DEFAULT_SIZE, 596, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(30, 30, 30)
                .addComponent(scrollEditor, javax.swing.GroupLayout.PREFERRED_SIZE, 316, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(32, 32, 32)
                        .addComponent(scrollDisAnalisis, javax.swing.GroupLayout.PREFERRED_SIZE, 171, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lCol, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(displayC))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(bClear, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(bLexico, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(41, Short.MAX_VALUE))
        );

        getContentPane().add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 60, 790, 590));

        jPanel3.setBackground(new java.awt.Color(0, 0, 0));

        displayReporte.setBackground(new java.awt.Color(0, 0, 51));
        displayReporte.setFont(new java.awt.Font("Dialog", 0, 14)); // NOI18N
        displayReporte.setForeground(new java.awt.Color(198, 198, 198));
        displayReporte.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "Token", "Patron", "Lexema", "L", "C"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.Integer.class, java.lang.Integer.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        displayReporte.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_NEXT_COLUMN);
        displayReporte.setFocusable(false);
        displayReporte.setGridColor(new java.awt.Color(0, 0, 0));
        displayReporte.setSelectionBackground(new java.awt.Color(0, 128, 201));
        displayReporte.setShowGrid(true);
        jScrollPane1.setViewportView(displayReporte);
        if (displayReporte.getColumnModel().getColumnCount() > 0) {
            displayReporte.getColumnModel().getColumn(0).setResizable(false);
            displayReporte.getColumnModel().getColumn(0).setPreferredWidth(40);
            displayReporte.getColumnModel().getColumn(1).setResizable(false);
            displayReporte.getColumnModel().getColumn(2).setResizable(false);
            displayReporte.getColumnModel().getColumn(3).setResizable(false);
            displayReporte.getColumnModel().getColumn(3).setPreferredWidth(5);
            displayReporte.getColumnModel().getColumn(4).setResizable(false);
            displayReporte.getColumnModel().getColumn(4).setPreferredWidth(5);
        }

        bGraph.setBackground(new java.awt.Color(7, 7, 110));
        bGraph.setFont(new java.awt.Font("Dialog", 1, 12)); // NOI18N
        bGraph.setForeground(new java.awt.Color(255, 255, 255));
        bGraph.setText("Generar Gráfica");
        bGraph.setFocusable(false);
        bGraph.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bGraphActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(28, 28, 28)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(29, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(bGraph, javax.swing.GroupLayout.PREFERRED_SIZE, 141, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(180, 180, 180))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(32, 32, 32)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 313, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(29, 29, 29)
                .addComponent(bGraph, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(169, Short.MAX_VALUE))
        );

        getContentPane().add(jPanel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(790, 60, 510, 590));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void bOpenFileActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bOpenFileActionPerformed
        currentPath = archivo.getPath();
        editor.setText(archivo.readTextFile(currentPath));
    }//GEN-LAST:event_bOpenFileActionPerformed

    private void bLexicoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bLexicoActionPerformed
        lexer.analyzeAll(editor.getText());
        displayAnalisis.setText(lexer.getErrors());
        rep.setReporte(displayReporte, lexer);
    }//GEN-LAST:event_bLexicoActionPerformed

    private void bClearActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bClearActionPerformed
        editor.setText("");
        displayAnalisis.setText("");
        rep.clearTable(displayReporte, true);
    }//GEN-LAST:event_bClearActionPerformed

    private void bGraphActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bGraphActionPerformed
        try {
            Icon icon = new ImageIcon(FILE_NAME + counterFile + FILE_EXTENSION);
            if(!displayReporte.getValueAt(displayReporte.getSelectedRow(), 0).toString().equals("error")){
                JOptionPane.showMessageDialog(null, getMssCTkn(), "grafica del token", 
                JOptionPane.INFORMATION_MESSAGE, icon);
            }else{
                JOptionPane.showMessageDialog(null, """
                                            Se ha seleccionado un token conflictivo,
                                            pues se trata de un error lexico, por favor
                                            selecciona un token valido e intentalo otra vez""", "Error lexico seleccionado", 
                JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, """
                                            No se ha seleccionado un token valido,
                                            por favor seleciona uno valido de la tabla
                                            del reporte e intentalo otra vez""", "Se ha producido un error", 
            JOptionPane.ERROR_MESSAGE);
        }
        archivo.deleteFile(FILE_NAME + counterFile + ".dot");
        archivo.deleteFile(FILE_NAME + counterFile + FILE_EXTENSION);
        counterFile++;
        displayReporte.clearSelection();
    }//GEN-LAST:event_bGraphActionPerformed

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        close();
    }//GEN-LAST:event_formWindowClosing

    private void bCreditosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bCreditosActionPerformed
        JOptionPane.showMessageDialog(null, """
                                            Proyecto realizado a base de muchos desvelos
                                            y dolores de cabez... digo de mucho esfuerzo y dedicacion. 
                                            
                                            Realizado por Yennifer Maria de Leon Samuc 
                                            para el laboratorio de Lenguajes formales y de programacion 
                                            Segundo semestre del 2023""", "Creditos", 
            JOptionPane.INFORMATION_MESSAGE);
    }//GEN-LAST:event_bCreditosActionPerformed

    private void editorCaretPositionChanged(java.awt.event.InputMethodEvent evt) {//GEN-FIRST:event_editorCaretPositionChanged
        numEditor.updateColumna(displayC);
    }//GEN-LAST:event_editorCaretPositionChanged

    private void editorCaretUpdate(javax.swing.event.CaretEvent evt) {//GEN-FIRST:event_editorCaretUpdate
        numEditor.updateColumna(displayC);
    }//GEN-LAST:event_editorCaretUpdate

    private void editorKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_editorKeyTyped
        numEditor.updateColumna(displayC);
        displayAnalisis.setText("");
        rep.clearTable(displayReporte, true);
    }//GEN-LAST:event_editorKeyTyped

    private void bHelpActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bHelpActionPerformed
        JOptionPane.showMessageDialog(null, """
                                            Puedes consultar el manual de usuario en el 
                                            siguiente enlace:
                                            //enlace aqui""", "Ayuda", 
            JOptionPane.INFORMATION_MESSAGE);
    }//GEN-LAST:event_bHelpActionPerformed

    private void bSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bSaveActionPerformed
        File file = new File(currentPath);
        if(file.exists()){ //cuando se tiene un archivo abierto
            archivo.saveFromExistentPath(editor.getText(), currentPath, file.getName());
        }else{ //cuando se crea desde 0
            archivo.saveAs(editor.getText(), ".txt");
        }
    }//GEN-LAST:event_bSaveActionPerformed

    private void bSaveAsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bSaveAsActionPerformed
        archivo.saveAs(editor.getText(), ".txt");
    }//GEN-LAST:event_bSaveAsActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton bClear;
    private javax.swing.JButton bCreditos;
    private javax.swing.JButton bGraph;
    private javax.swing.JButton bHelp;
    private javax.swing.JButton bLexico;
    private javax.swing.JButton bOpenFile;
    private javax.swing.JButton bSave;
    private javax.swing.JButton bSaveAs;
    private javax.swing.JTextPane displayAnalisis;
    private javax.swing.JLabel displayC;
    private javax.swing.JTable displayReporte;
    private javax.swing.JTextPane editor;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lCol;
    private javax.swing.JScrollPane scrollDisAnalisis;
    private javax.swing.JScrollPane scrollEditor;
    private javax.swing.JLabel title;
    // End of variables declaration//GEN-END:variables
}
