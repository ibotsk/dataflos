/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sk.sav.bot.dataflos.gui;

import java.awt.Color;
import java.awt.Component;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JProgressBar;
import javax.swing.JRootPane;


/**
 *
 * @author Jakub
 */
public final class LoadingDialog extends javax.swing.JDialog {
    
    private static LoadingDialog instance = null;

    private String title;
    private String note;
    
    /**
     * Creates new form LoadingFrame
     */
    
    protected LoadingDialog() {
      // Exists only to defeat instantiation.
   }
    
    public LoadingDialog(String title, String note, int min, int max, Component component){ 
        
        initComponents();
        setLoadingTitle(title);
        setNote(note);
        this.progressBar.setMinimum(min);
        this.progressBar.setMaximum(max);
        
        this.getRootPane().setBorder( BorderFactory.createLineBorder(new java.awt.Color(134, 209, 59)) );
        this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        this.setAlwaysOnTop(true);
        this.setLocationRelativeTo(component);
        this.setVisible(true);
    }
    
    public static LoadingDialog getInstance() {
        if(instance == null) {
           instance = new LoadingDialog();
        }
        return instance;
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
        progressBar = new javax.swing.JProgressBar();
        loadingTitle = new javax.swing.JLabel();
        loadingStatus = new javax.swing.JLabel();

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );

        loadingTitle.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        loadingTitle.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        loadingTitle.setText("loadingTitle");

        loadingStatus.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        loadingStatus.setText("loadingStatus");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(loadingStatus, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(progressBar, javax.swing.GroupLayout.DEFAULT_SIZE, 330, Short.MAX_VALUE)
                    .addComponent(loadingTitle, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(loadingTitle, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(loadingStatus)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(progressBar, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    javax.swing.JPanel jPanel1;
    javax.swing.JLabel loadingStatus;
    javax.swing.JLabel loadingTitle;
    javax.swing.JProgressBar progressBar;
    // End of variables declaration//GEN-END:variables

    /**
     * @return the title
     */
    public String getLoadingTitle() {
        return this.loadingTitle.getText();
    }

    /**
     * @param title the title to set
     */
    public void setLoadingTitle(String title) {
        this.loadingTitle.setText(title);
    }

    /**
     * @return the note
     */
    public String getNote() {
        return this.loadingStatus.getText();
    }

    /**
     * @param note the note to set
     */
    public void setNote(String note) {
        this.loadingStatus.setText(note);
    }
    
    /**
     * @return the note
     */
    public double getProgress() {
        return this.progressBar.getValue();
    }

    /**
     * @param note the note to set
     */
    public void setProgress(int progress) {
        this.progressBar.setValue(progress);
    }
    
    public JProgressBar getProgressBar(){
        return this.progressBar;
    }
}
