package lenguajes.proyectolenguajesydl;

import java.awt.Color;
import lenguajes.proyectolenguajesydl.util.*;
import java.util.StringTokenizer;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;
import lenguajes.proyectolenguajesydl.analizadorlexico.AnalizadorLexico;
import lenguajes.proyectolenguajesydl.analizadorlexico.Expresion;
import lenguajes.proyectolenguajesydl.util.NumeroLinea;

/**
 *
 * @author yenni
 */
public class InterfazUsuario extends javax.swing.JFrame {
    //NumeroLinea numeracionEditor, numeracionDisplay;
    NumeroLinea numEditor, numDisAnalisis;
    
    /**
     * Creacion de los elementos necesarios para hacer funcionar el analizador
     */ 
    private Archivo archivo;
    private AnalizadorLexico lexer = new AnalizadorLexico();
    private Expresion ex;
    
    //se almacena el estilo actual, el default
    private final StyleContext cont = StyleContext.getDefaultStyleContext();
    //COLORES, estos como atributos
    private final AttributeSet attrWhite = cont.addAttribute(cont.getEmptySet(), StyleConstants.Foreground, Color.WHITE);
    final AttributeSet attrSkyBlue = cont.addAttribute(cont.getEmptySet(), StyleConstants.Foreground, new Color(0,242,255));
    final AttributeSet attrPurple = cont.addAttribute(cont.getEmptySet(), StyleConstants.Foreground, new Color(177,0,255));   
    final AttributeSet attrOrange = cont.addAttribute(cont.getEmptySet(), StyleConstants.Foreground, Color.ORANGE);
    final AttributeSet attrGray = cont.addAttribute(cont.getEmptySet(), StyleConstants.Foreground, Color.GRAY);
    final AttributeSet attrGreen = cont.addAttribute(cont.getEmptySet(), StyleConstants.Foreground, Color.GREEN);
    
    //definicion de un documento para que pueda ser coloreado
    private int currrentLine;
    DefaultStyledDocument doc = new DefaultStyledDocument() {
        //se sobrescribe el metodo para ingresar un string, agregando algo de codigo extra
        @Override
        public void insertString(int offset, String str, AttributeSet a) throws BadLocationException {
            super.insertString(offset, str, a);
            //obtener todo el texto que se esta escribiendo
            String text = getText(0, getLength());
            //srt == caracter ingresado
            //offset == posicion de donde se ingresa el caracter, su inicio
            if(str.length() == 1){
                //if(ex.isAlphaNumeric(str.charAt(0))){
                    int posInit = lexer.findDelimitadorL(text, offset);
                    int posFinal = lexer.findDelimitadorR(text, offset);
                    if(posInit == posFinal){
                        posInit--;
                    }
                    System.out.println(posInit + "init - final" + posFinal);
                    String currentTkn = text.substring(posInit, posFinal);
                    System.out.println(currentTkn);
                    String currentType = lexer.getTypeTkn(currentTkn);
                    System.out.println(currentType);
                    switch (currentType) {
                        case "Reservada":
                            setCharacterAttributes(posInit,  currentTkn.length(), attrPurple, false);
                            break;
                        case "boolean", "int":
                            setCharacterAttributes(posInit,  currentTkn.length(), attrOrange, false);
                            break;
                        case "Logico":
                            setCharacterAttributes(posInit,  currentTkn.length(), attrSkyBlue, false);
                            break;
                        case "Identificador":
                            setCharacterAttributes(posInit,  currentTkn.length(), attrWhite, false);
                            break;    
                        case "Otro":
                            setCharacterAttributes(posInit, 1 , attrGreen, false);
                            break;   
                        default:
                            setCharacterAttributes(posInit, 1, attrSkyBlue, false);
                    }
                /*}else if(ex.isOtro(str.charAt(0))){
                    setCharacterAttributes(offset, 1, attrGreen, false);
                }else if(!ex.isIgnoredCharacter(str.charAt(0))){
                    setCharacterAttributes(offset, 1, attrSkyBlue, false);
                }else if(str.charAt(0) == '"' || str.charAt(0) == '\'' || str.charAt(0) == '#' ){
                    
                }*/
            }else{
            
            }
            
            //System.out.println("offset:" + offset + "-srt:" + str + "-a:" + a);

        }

        @Override
        public void remove(int offs, int len) throws BadLocationException {
            super.remove(offs, len);
            String text = getText(0, getLength());
            System.out.println(offs + "-offs --- len->" + len);
            //offs -posicion donde termina el cursor luego de la eliminacion
            //len cuantos caracteres se eliminarion
            
            try {
                int posInit = lexer.findDelimitadorL(text, offs);
                int posFinal = lexer.findDelimitadorR(text, offs);
                //System.out.println(posInit + "init - final" + posFinal);
                if(posInit == posFinal){
                    posInit--;
                }
                //System.out.println(posInit + "init - final" + posFinal);
                String currentTkn = text.substring(posInit, posFinal);
                //System.out.println(currentTkn);
                String currentType = lexer.getTypeTkn(currentTkn);
                //System.out.println(currentType);
                switch (currentType) {
                    case "Reservada":
                        setCharacterAttributes(posInit,  currentTkn.length(), attrPurple, false);
                        break;
                    case "boolean", "int":
                        setCharacterAttributes(posInit,  currentTkn.length(), attrOrange, false);
                        break;
                    case "Logico":
                        setCharacterAttributes(posInit,  currentTkn.length(), attrSkyBlue, false);
                        break;
                    case "Identificador":
                        setCharacterAttributes(posInit,  currentTkn.length(), attrWhite, false);
                        break;    
                    case "Otro":
                        setCharacterAttributes(posInit,  currentTkn.length(), attrGreen, false);
                        break;   
                    default:
                        setCharacterAttributes(posInit, currentTkn.length(), attrSkyBlue, false);
                } 
            } catch (IndexOutOfBoundsException e) {
                System.out.println(e);
            }
        }
    };
    
        
    /**
     * Creates new form InterfazUsuario
     */    
    public InterfazUsuario() {
        initComponents();
        this.setLocationRelativeTo(null);
        initNumeracion();
        archivo = new Archivo();
        //lexer = new AnalizadorLexico();
        ex = new Expresion();
        //inicializando estilos 
        JTextPane txt = new JTextPane(doc);
        String temp = editor.getText();
        editor.setStyledDocument(txt.getStyledDocument());
        editor.setText(temp);
    }
    private void initNumeracion(){
        numEditor = new NumeroLinea(editor);
        scrollEditor.setRowHeaderView(numEditor);
        numDisAnalisis = new NumeroLinea(displayAnalisis);
        scrollDisAnalisis.setRowHeaderView(numDisAnalisis);
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
        bSave1 = new javax.swing.JButton();
        bSave2 = new javax.swing.JButton();
        bSave3 = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        bClear = new javax.swing.JButton();
        bLexico = new javax.swing.JButton();
        scrollEditor = new javax.swing.JScrollPane();
        editor = new javax.swing.JTextPane();
        scrollDisAnalisis = new javax.swing.JScrollPane();
        displayAnalisis = new javax.swing.JTextPane();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel1.setBackground(new java.awt.Color(7, 7, 110));

        bOpenFile.setBackground(new java.awt.Color(7, 7, 110));
        bOpenFile.setFont(new java.awt.Font("Dialog", 1, 12)); // NOI18N
        bOpenFile.setForeground(new java.awt.Color(255, 255, 255));
        bOpenFile.setText("Abrir archivo");
        bOpenFile.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bOpenFileActionPerformed(evt);
            }
        });

        bSave.setBackground(new java.awt.Color(7, 7, 110));
        bSave.setFont(new java.awt.Font("Dialog", 1, 12)); // NOI18N
        bSave.setForeground(new java.awt.Color(255, 255, 255));
        bSave.setText("Guardar");

        bSave1.setBackground(new java.awt.Color(7, 7, 110));
        bSave1.setFont(new java.awt.Font("Dialog", 1, 12)); // NOI18N
        bSave1.setForeground(new java.awt.Color(255, 255, 255));
        bSave1.setText("Generar Gráfica");

        bSave2.setBackground(new java.awt.Color(7, 7, 110));
        bSave2.setFont(new java.awt.Font("Dialog", 1, 12)); // NOI18N
        bSave2.setForeground(new java.awt.Color(255, 255, 255));
        bSave2.setText("Ayuda");

        bSave3.setBackground(new java.awt.Color(7, 7, 110));
        bSave3.setFont(new java.awt.Font("Dialog", 1, 12)); // NOI18N
        bSave3.setForeground(new java.awt.Color(255, 255, 255));
        bSave3.setText("Creditos");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addComponent(bOpenFile, javax.swing.GroupLayout.PREFERRED_SIZE, 126, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(bSave, javax.swing.GroupLayout.PREFERRED_SIZE, 116, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(95, 95, 95)
                .addComponent(bSave1, javax.swing.GroupLayout.PREFERRED_SIZE, 141, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(102, 102, 102)
                .addComponent(bSave2, javax.swing.GroupLayout.PREFERRED_SIZE, 141, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(bSave3, javax.swing.GroupLayout.PREFERRED_SIZE, 141, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(bOpenFile, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 36, Short.MAX_VALUE)
                    .addComponent(bSave, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(bSave1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(bSave2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(bSave3, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(12, 12, 12))
        );

        getContentPane().add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 950, -1));

        jPanel2.setBackground(new java.awt.Color(0, 0, 0));
        jPanel2.setForeground(new java.awt.Color(255, 255, 255));
        jPanel2.setFont(new java.awt.Font("Dialog", 0, 14)); // NOI18N

        bClear.setBackground(new java.awt.Color(7, 7, 110));
        bClear.setFont(new java.awt.Font("Dialog", 1, 12)); // NOI18N
        bClear.setForeground(new java.awt.Color(255, 255, 255));
        bClear.setText("Limpiar");
        bClear.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bClearActionPerformed(evt);
            }
        });

        bLexico.setBackground(new java.awt.Color(7, 7, 110));
        bLexico.setFont(new java.awt.Font("Dialog", 1, 12)); // NOI18N
        bLexico.setForeground(new java.awt.Color(255, 255, 255));
        bLexico.setText("Analizador Léxico");
        bLexico.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bLexicoActionPerformed(evt);
            }
        });

        scrollEditor.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        editor.setBackground(new java.awt.Color(0, 0, 44));
        editor.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(51, 51, 51)));
        editor.setFont(new java.awt.Font("Dialog", 0, 14)); // NOI18N
        editor.setForeground(new java.awt.Color(0, 153, 204));
        editor.setCaretColor(new java.awt.Color(255, 255, 255));
        scrollEditor.setViewportView(editor);

        displayAnalisis.setEditable(false);
        displayAnalisis.setBackground(new java.awt.Color(0, 0, 44));
        displayAnalisis.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(51, 51, 51)));
        displayAnalisis.setFont(new java.awt.Font("Dialog", 0, 14)); // NOI18N
        displayAnalisis.setForeground(new java.awt.Color(255, 255, 255));
        displayAnalisis.setCaretColor(new java.awt.Color(255, 255, 255));
        scrollDisAnalisis.setViewportView(displayAnalisis);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(22, 22, 22)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(scrollEditor, javax.swing.GroupLayout.PREFERRED_SIZE, 898, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(bClear, javax.swing.GroupLayout.PREFERRED_SIZE, 157, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(bLexico, javax.swing.GroupLayout.PREFERRED_SIZE, 157, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addComponent(scrollDisAnalisis)))
                .addContainerGap(32, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(30, 30, 30)
                .addComponent(scrollEditor, javax.swing.GroupLayout.PREFERRED_SIZE, 316, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(32, 32, 32)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(bClear, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(bLexico, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(scrollDisAnalisis, javax.swing.GroupLayout.PREFERRED_SIZE, 171, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(41, Short.MAX_VALUE))
        );

        getContentPane().add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 60, 952, 590));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void bOpenFileActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bOpenFileActionPerformed
        editor.setText(archivo.readTextFile(archivo.getPath()));
    }//GEN-LAST:event_bOpenFileActionPerformed

    private void bLexicoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bLexicoActionPerformed
        lexer.analyzeAll(editor.getText());
        /*StringTokenizer st = new StringTokenizer(jTextArea1.getText());
        while (st.hasMoreTokens()) {
            System.out.println("-----------------------");
            System.out.println(st.nextToken());
        }*/
        displayAnalisis.setText(lexer.getAnalisis());
    }//GEN-LAST:event_bLexicoActionPerformed

    private void bClearActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bClearActionPerformed
        editor.setText("");
        displayAnalisis.setText("");
    }//GEN-LAST:event_bClearActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(InterfazUsuario.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(InterfazUsuario.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(InterfazUsuario.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(InterfazUsuario.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new InterfazUsuario().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton bClear;
    private javax.swing.JButton bLexico;
    private javax.swing.JButton bOpenFile;
    private javax.swing.JButton bSave;
    private javax.swing.JButton bSave1;
    private javax.swing.JButton bSave2;
    private javax.swing.JButton bSave3;
    private javax.swing.JTextPane displayAnalisis;
    private javax.swing.JTextPane editor;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane scrollDisAnalisis;
    private javax.swing.JScrollPane scrollEditor;
    // End of variables declaration//GEN-END:variables
}
