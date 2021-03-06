/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.skourtis.sqladmin.view;

import java.awt.Color;
import javax.swing.JEditorPane;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;
import net.skourtis.sqladmin.model.SqlProcedure;
import net.skourtis.sqladmin.model.Trigger;

/**
 *
 * @author Stavros
 */
public class SqlProcedurePanel extends javax.swing.JPanel {

    /**
     * Creates new form TriggerPanel
     */
    public SqlProcedurePanel(SqlProcedure procedure) {
        initComponents();

        codeField.setStyledDocument(new SqlStyleDocument(
                StyleContext.getDefaultStyleContext().addAttribute(StyleContext.getDefaultStyleContext().getEmptySet(), StyleConstants.Foreground, Color.BLUE),
                StyleContext.getDefaultStyleContext().addAttribute(StyleContext.getDefaultStyleContext().getEmptySet(), StyleConstants.Foreground, Color.BLACK)));

        codeField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                calculateLineNumbers();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                calculateLineNumbers();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
            }
        });
        codeField.setText(procedure.getCode());
        calculateLineNumbers();
    }

    public void calculateLineNumbers() {

        String text = codeField.getText();
        int counter = 1;

        for (int i = 0; i < text.length(); i++) {
            if (text.charAt(i) == '\n') {
                counter++;
            }
        }

        StringBuilder lineNumbersText = new StringBuilder();
        lineNumbersText.append("<html>");
        for (int i = 1; i <= counter; i++) {
            lineNumbersText.append(i);
            lineNumbersText.append("<br>");
        }

        jLabel1.setFont(codeField.getFont());
        jLabel1.setText(lineNumbersText.toString());
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        jPanel2 = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        codeField = new javax.swing.JTextPane();

        setBackground(new java.awt.Color(255, 255, 255));

        jScrollPane1.getVerticalScrollBar().setUnitIncrement(16);

        jLabel1.setText("jLabel1");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(2, 2, 2)
                .addComponent(jLabel1)
                .addContainerGap(561, Short.MAX_VALUE))
        );

        codeField.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jScrollPane2.setViewportView(codeField);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 782, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jScrollPane2)
        );

        jScrollPane1.setViewportView(jPanel2);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1)
        );
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextPane codeField;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    // End of variables declaration//GEN-END:variables
}
