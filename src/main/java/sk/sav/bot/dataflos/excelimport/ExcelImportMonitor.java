/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sk.sav.bot.dataflos.excelimport;

import sk.sav.bot.dataflos.gui.LoadingDialog;
import sk.sav.bot.dataflos.models.ImportMonitorModel;
import sk.sav.bot.dataflos.util.HibernateQuery;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import org.apache.log4j.Logger;
import sk.sav.bot.dataflos.entity.Udaj;
import sk.sav.bot.dataflos.entity.interf.AssociableEntity;

/**
 *
 * @author Jakub
 */
public class ExcelImportMonitor extends javax.swing.JFrame implements PropertyChangeListener {

    private static ExcelImportMonitor instance = null;
    private HibernateQuery hq = null;
    
    private ExcelPrepareImport iew;
    private LoadingDialog loadingDialog;
    private boolean isStarted = false;
    
    private boolean controllRunning = false;
    private boolean importRunning = false;
    
    private static Logger log = Logger.getLogger(ExcelImportMonitor.class.getName());
    
    /**
     * Creates new form ExcelImportMonitor
     */
    private ExcelImportMonitor() {
        initComponents();
        
        TableModelListener lsn = new TableModelListener() {

            // always scroll down to the newest added row
            @Override
            public void tableChanged(final TableModelEvent e) {
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        int viewRow = jTableImportMonitor.convertRowIndexToView(e.getFirstRow());
                        jTableImportMonitor.scrollRectToVisible(jTableImportMonitor.getCellRect(viewRow, 0, true));    
                    }
                });
            }
        };
        
        // nastavi model dat pre monitorovaciu tabulku + vysku riadku a sirku stlpcov
        ImportMonitorModel im = new ImportMonitorModel();
        this.jTableImportMonitor.setModel(im);
        this.jTableImportMonitor.getModel().addTableModelListener(lsn);
        this.jTableImportMonitor.setRowHeight(28);
        this.jTableImportMonitor.getColumnModel().getColumn(0).setPreferredWidth(110);
        this.jTableImportMonitor.getColumnModel().getColumn(1).setPreferredWidth(80);
        this.jTableImportMonitor.getColumnModel().getColumn(2).setPreferredWidth(530);
        this.jTableImportMonitor.getColumnModel().getColumn(3).setPreferredWidth(90);
    }
    
    public static ExcelImportMonitor getInstance() {
        if (instance == null) {
            instance = new ExcelImportMonitor();
        }
        log.info("Getting instance of excel import monitor");
        return instance;
    }
    
    public void setHq(HibernateQuery hq){
        this.hq = hq;
    }
    
    // read import data and load db checking data
    public void prepareImport(File excelFile) throws IOException {
        if(!isStarted) {
            log.info("Preparing import");
            isStarted = true;
            loadingDialog = new LoadingDialog("Načítavanie dát na kontolu", " ", 0, 100, ExcelImportMonitor.this);
            loadingDialog.setProgress(0);
            iew = new ExcelPrepareImport(this, excelFile, hq);
            iew.addPropertyChangeListener(this);
            iew.execute();
        } else {
            log.warn("2nd instance of import not allowed.");
            JOptionPane.showMessageDialog(null, String.format("Nie je povolené spúšťať viacero importov súčasne."), "Upozornenie", JOptionPane.INFORMATION_MESSAGE);
        }
        
        setImportEnabled(false);
        this.setVisible(true);
    }
    
    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (evt.getPropertyName().equals("progress")) {
            int progress = (Integer) evt.getNewValue();
            loadingDialog.setProgress(progress);
            String note = String.format("Dokončených %d%%.\n", progress);
            loadingDialog.setNote(note);
            if (loadingDialog.getProgress() == 100) {
                loadingDialog.setVisible(false);
                // bol to progress ku kontolovaniu dat, alebo importu?
                if (controllRunning) {
                    controllRunning = false;
                } else if (importRunning) {
                    log.info("Import of data completed successfully.");
                    int res = JOptionPane.showOptionDialog(null, String.format("Importovanie dát do databázy prebeho úspešne."), "Potvrdzujúca informácia", JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, null, null);
                    if (res == JOptionPane.OK_OPTION){
                        this.dispose();
                    }
                    importRunning = false;
                }
            }
        }
    }
    
    public void setImportEnabled(boolean enabled){
        this.btnDoImport.setEnabled(enabled);
    }
    
    public JTable getImportMonitorTable() {
        return this.jTableImportMonitor;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        lblImportMonitor = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTableImportMonitor = new javax.swing.JTable();
        btnDoImport = new javax.swing.JButton();
        btnCancelImport = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Proces importovania");
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

        lblImportMonitor.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lblImportMonitor.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblImportMonitor.setText("Monitor importovania");

        jTableImportMonitor.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane1.setViewportView(jTableImportMonitor);

        btnDoImport.setText("Importovať");
        btnDoImport.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnDoImportMouseClicked(evt);
            }
        });

        btnCancelImport.setText("Zrušiť");
        btnCancelImport.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnCancelImportMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(lblImportMonitor, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 830, Short.MAX_VALUE))
                        .addContainerGap())
                    .addGroup(layout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addComponent(btnCancelImport)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnDoImport)
                        .addGap(18, 18, 18))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(4, 4, 4)
                .addComponent(lblImportMonitor)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 665, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnDoImport)
                    .addComponent(btnCancelImport))
                .addContainerGap())
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void btnCancelImportMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnCancelImportMouseClicked
        
        //zrusime beh vlakna, zatvorime loadingDialog, zavrieme monitorovacie okno a nastavime, ze ziaden import nebezi (isStarted = false)
        log.info("Import cancelled");
        iew.cancel(true);
        loadingDialog.setVisible(false);
        this.dispose();
        isStarted = false;
        
    }//GEN-LAST:event_btnCancelImportMouseClicked

    // import entities and udajs to DB, via separate thread
    private void btnDoImportMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnDoImportMouseClicked
        
        Map<String, AssociableEntity> unsavedEntities = iew.getUnsavedEntities();
        List<Udaj> importUdaje = iew.getImportUdaje();

        loadingDialog.getProgressBar().setIndeterminate(false);
        loadingDialog.setLoadingTitle("Zápis dát do databázy");
        loadingDialog.setNote("Načítavanie...");
        loadingDialog.setProgress(0);
        loadingDialog.setVisible(true);
        
        importRunning = true;
        
        log.info("Importing "+importUdaje.size()+" udajs to DB");
        ExcelDoImport edi = new ExcelDoImport(this, hq, unsavedEntities, importUdaje);
        edi.addPropertyChangeListener(this);       
        edi.execute();
    }//GEN-LAST:event_btnDoImportMouseClicked

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        //zrusime beh vlakna, zatvorime loadingDialog, zavrieme monitorovacie okno a nastavime, ze ziaden import nebezi (isStarted = false)
        iew.cancel(true);
        loadingDialog.setVisible(false);
        this.dispose();
        isStarted = false;
    }//GEN-LAST:event_formWindowClosing

    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    javax.swing.JButton btnCancelImport;
    javax.swing.JButton btnDoImport;
    javax.swing.JScrollPane jScrollPane1;
    javax.swing.JTable jTableImportMonitor;
    javax.swing.JLabel lblImportMonitor;
    // End of variables declaration//GEN-END:variables
       
}