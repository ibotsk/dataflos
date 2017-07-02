package sk.sav.bot.dataflos.main;

import sk.sav.bot.dataflos.entity.Nac;
import sk.sav.bot.dataflos.entity.UdajZberAsoc;
import sk.sav.bot.dataflos.entity.Casopisy;
import sk.sav.bot.dataflos.entity.Flora;
import sk.sav.bot.dataflos.entity.Kvadrant;
import sk.sav.bot.dataflos.entity.UdajObrazky;
import sk.sav.bot.dataflos.entity.SkupRevDet;
import sk.sav.bot.dataflos.entity.HerbarPolozky;
import sk.sav.bot.dataflos.entity.Chu;
import sk.sav.bot.dataflos.entity.Obec;
import sk.sav.bot.dataflos.entity.LokalityFtgokresAsoc;
import sk.sav.bot.dataflos.entity.Ftgokres;
import sk.sav.bot.dataflos.entity.Voucher;
import sk.sav.bot.dataflos.entity.MenaZberRev;
import sk.sav.bot.dataflos.entity.MenaTaxonov;
import sk.sav.bot.dataflos.entity.Lokality;
import sk.sav.bot.dataflos.entity.Vgc;
import sk.sav.bot.dataflos.entity.ListOfSpecies;
import sk.sav.bot.dataflos.entity.Brumit4;
import sk.sav.bot.dataflos.entity.LitZdrojRev;
import sk.sav.bot.dataflos.entity.Brumit3;
import sk.sav.bot.dataflos.entity.SkupRev;
import sk.sav.bot.dataflos.entity.LokalityKvadrantAsoc;
import sk.sav.bot.dataflos.entity.Ngc;
import sk.sav.bot.dataflos.entity.Vac;
import sk.sav.bot.dataflos.entity.LitZdroj;
import sk.sav.bot.dataflos.entity.LzdrojEditoriAsoc;
import sk.sav.bot.dataflos.entity.Udaj;
import sk.sav.bot.dataflos.entity.Exsikaty;
import sk.sav.bot.dataflos.entity.LzdrojAutoriAsoc;
import sk.sav.bot.dataflos.entity.Herbar;
import sk.sav.bot.dataflos.entity.DallaTorre;
import sk.sav.bot.dataflos.models.RevisionsTableModel;
import sk.sav.bot.dataflos.models.TableListModel;
import sk.sav.bot.dataflos.entity.interf.AssociableEntity;
import sk.sav.bot.dataflos.entity.interf.PeopleAsoc;
import sk.sav.bot.dataflos.exception.EntityException;
import sk.sav.bot.dataflos.factory.ListAddRemoveUtils;
import sk.sav.bot.dataflos.factory.UdajFactory;
import sk.sav.bot.dataflos.gui.DynamicContentFrame;
import sk.sav.bot.dataflos.gui.ExportDataForm;
import sk.sav.bot.dataflos.excelimport.ExcelImportMonitor;
import sk.sav.bot.dataflos.gui.LiteratureSearchForm;
import sk.sav.bot.dataflos.gui.LoadingDialog;
import sk.sav.bot.dataflos.gui.TextFieldMoznosti;
import sk.sav.bot.dataflos.models.ImagesTableModel;
import sk.sav.bot.dataflos.models.ImportMonitorModel;
import sk.sav.bot.dataflos.models.LiteraturesTableModel;
import sk.sav.bot.dataflos.models.PagingModel;
import sk.sav.bot.dataflos.models.RecordsInsertedModel;
import sk.sav.bot.dataflos.util.DisplayEntities;
import sk.sav.bot.dataflos.util.HandyUtils;
import sk.sav.bot.dataflos.util.HibernateQuery;
import sk.sav.bot.dataflos.util.MaxLengthTextDocument;
import sk.sav.bot.dataflos.util.StdMenoFontTypeRenderer;
import sk.sav.bot.dataflos.util.TableColumnsWidthCounter;
import sk.sav.bot.dataflos.verify.DateDayVerifier;
import sk.sav.bot.dataflos.verify.DateMonthVerifier;
import sk.sav.bot.dataflos.verify.DateYearVerifier;
import sk.sav.bot.dataflos.verify.UrlVerifier;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.regex.PatternSyntaxException;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.event.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.TableRowSorter;
import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.exception.ConstraintViolationException;
import org.hibernate.resource.transaction.spi.TransactionStatus;
import sk.sav.bot.dataflos.gui.FilterJList;
import sk.sav.bot.dataflos.gui.ImageDisplay;
import sk.sav.bot.dataflos.gui.MoznostiCaller;

/**
 *
 * @author Matus
 */
public class MainFrame extends javax.swing.JFrame implements PropertyChangeListener {

    private static final long serialVersionUID = 1L;

    private static Logger log = Logger.getLogger(MainFrame.class.getName());

    private DisplayEntities dispEnt = DisplayEntities.getInstance();
    private TableRowSorter<PagingModel> sorter;
    private HibernateQuery hq;
    private boolean isRevisionNew = true;
    private boolean isImageNew = true;
    private BufferedImage activeImage = null;
    private final Session session;
    private DynamicContentFrame dcfEntity;
    private ExportDataForm exportDataForm;
    private LoadingDialog loadingDialog;

    private String login;//logged user
    private boolean isAdmin;
    private LitZdroj selectedLzdroj = null;
    boolean ignoreEvents = false;

    // rozne typy ramcekov na zvyraznenie odporucanych poli
    Border defaultBorder = UIManager.getBorder("TextField.border");

    Border greenBorder = BorderFactory.createLineBorder(new java.awt.Color(134, 209, 59), 2);
    Border blueBorder = BorderFactory.createLineBorder(new java.awt.Color(0, 112, 192), 2);
    Border emptyBorder = BorderFactory.createEmptyBorder(3, 3, 3, 3);

    //zlozene ramceky pre simulovanie paddingu pre text (aby nebol text nalepeny hned na okraji)
    Border borderGreenWithPadding = new CompoundBorder(greenBorder, emptyBorder);
    Border borderBlueWithPadding = new CompoundBorder(blueBorder, emptyBorder);

    /**
     * Creates new form MainFrame.
     */
    public MainFrame() {
               
        
        //initialize components
        initComponents();

        //set position of login dialog and show it
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        int w = loginDialog.getSize().width;
        int h = loginDialog.getSize().height;
        int x = (dim.width - w) / 2;
        int y = (dim.height - h) / 2;
        loginDialog.setLocation(x, y);
        loginDialog.setVisible(true);

        //initialized session with PostgreSQL DB
        this.session = hq.getSession();

        //initialized listeners of components
        initListeners();

        this.sorter = new TableRowSorter<>();
        this.jTableOverview.setRowSorter(this.sorter);
        this.jTableOverview.setModel(new PagingModel(null));
        this.jTableOverview.setAutoCreateRowSorter(true);
        this.jTableOverview.addMouseListener(new TablesOverviewDoubleClick());
        this.jTableOverview.getSelectionModel().addListSelectionListener(new TablesOverviewSelectionListener());

        this.filterRecordsOverview.getRecordsOverviewTable().addMouseListener(new RecordsOverviewDoubleClick());
        this.filterRecordsOverview.getRecordsOverviewTable().getSelectionModel().addListSelectionListener(new TableRecordsOverviewSelectionListener());

        this.jTableLitSources.getSelectionModel().addListSelectionListener(new TableLitSourceSelectionListener());
        this.jTableRevisions.getSelectionModel().addListSelectionListener(new TableRevisionsSelectionListener());
        this.jTableImages.getSelectionModel().addListSelectionListener(new TableImagesSelectionListener());

        this.jScrollPaneRevizie.getVerticalScrollBar().setUnitIncrement(20);
        this.jScrollPaneLokalita.getVerticalScrollBar().setUnitIncrement(15);
        this.jScrollPaneZaradenie.getVerticalScrollBar().setUnitIncrement(20);
        this.jScrollPaneLitZdroj.getVerticalScrollBar().setUnitIncrement(20);

        // specialny cell renderer pre zoznam moznosti nastavi v pripade standardizovanych mien rozdielny druh pisma pre akceptovane, neakceptovane a doposial neschvalene mena
        this.listMoznosti.getMainList().setCellRenderer(new StdMenoFontTypeRenderer());

        this.jTabbedPaneMain.remove(this.tablesOverviewTab);
        this.jTabbedPaneMain.remove(this.recordTab);
        this.jTabbedPaneMain.remove(this.recordsOverviewTab);

        this.exportDataForm = new ExportDataForm(hq);
        this.dcfEntity = new DynamicContentFrame(hq);

        this.filterRecordsOverview.setHq(hq);

        //policka lit. zdroja, revizii a obrazkov maju byt spociatku nepristupne
        this.enabledLitFields(false, false);
        this.enabledJournalData(false);
        this.enabledBookData(false);
        this.enabledRevFields(false);
        this.enabledImageFields(false);

        this.setExtendedState(JFrame.MAXIMIZED_BOTH);
    }

    /**
     * Inititalizes document listeners for all TextFieldMoznosti component objects.
     */
    private void initListeners() {
        tfUrcStdMeno.getTextField().getDocument().addDocumentListener(new StdMenoDocumentListener(tfUrcStdMeno, tfUrcAccName, tfUrcPochybnost, tfUrcOhrozenost, tfUrcEndemizmus, tfUrcPovodnost, tfUrcSvkNazov, cbUrcOchrana));
        tfRevStdMeno.getTextField().getDocument().addDocumentListener(new StdMenoDocumentListener(tfRevStdMeno, tfRevAccName, tfRevPochybnost, tfRevOhrozenost, tfRevEndemizmus, tfRevPovodnost, tfRevSvkNazov, cbRevOchrana));
        tfUrcStdMeno.getTextField().getDocument().addDocumentListener(new TextFieldMoznostiDocListener(tfUrcStdMeno.getTextField(), this.listMoznosti));
        tfRevStdMeno.getTextField().getDocument().addDocumentListener(new TextFieldMoznostiDocListener(tfRevStdMeno.getTextField(), this.listMoznosti));
        tfLocBrumFour.getTextField().getDocument().addDocumentListener(new TextFieldMoznostiDocListener(tfLocBrumFour.getTextField(), this.listMoznosti));
        tfLocFlora.getTextField().getDocument().addDocumentListener(new TextFieldMoznostiDocListener(tfLocFlora.getTextField(), this.listMoznosti));
        tfLocNac.getTextField().getDocument().addDocumentListener(new TextFieldMoznostiDocListener(tfLocNac.getTextField(), this.listMoznosti));
        tfLocNgc.getTextField().getDocument().addDocumentListener(new TextFieldMoznostiDocListener(tfLocNgc.getTextField(), this.listMoznosti));
        tfLocVac.getTextField().getDocument().addDocumentListener(new TextFieldMoznostiDocListener(tfLocVac.getTextField(), this.listMoznosti));
        tfLocVgc.getTextField().getDocument().addDocumentListener(new TextFieldMoznostiDocListener(tfLocVgc.getTextField(), this.listMoznosti));
        tfLocProtArea.getTextField().getDocument().addDocumentListener(new TextFieldMoznostiDocListener(tfLocProtArea.getTextField(), this.listMoznosti));
        tfLocVill.getTextField().getDocument().addDocumentListener(new TextFieldMoznostiDocListener(tfLocVill.getTextField(), this.listMoznosti));
        tfZarHerbar.getTextField().getDocument().addDocumentListener(new TextFieldMoznostiDocListener(tfZarHerbar.getTextField(), this.listMoznosti));
        tfZarDallaTorre.getTextField().getDocument().addDocumentListener(new TextFieldMoznostiDocListener(tfZarDallaTorre.getTextField(), this.listMoznosti));
        tfZarExsikat.getTextField().getDocument().addDocumentListener(new TextFieldMoznostiDocListener(tfZarExsikat.getTextField(), this.listMoznosti));
        tfZarVoucher.getTextField().getDocument().addDocumentListener(new TextFieldMoznostiDocListener(tfZarVoucher.getTextField(), this.listMoznosti));
        tfLitJournalTitle.getTextField().getDocument().addDocumentListener(new TextFieldMoznostiDocListener(tfLitJournalTitle.getTextField(), this.listMoznosti));
    }

    /**
     * Load last "udaj" inserted by user. If it exists, copy its data to given tab
     * @param tabName tab into which the contents are copied (populated)
     */
    private void copyDataFromPrevious(String tabName) {

        Udaj poslednyUdaj = hq.getMyLastUdaj();

        if (poslednyUdaj == null) {
            log.warn("copyDataFromPrevious - no udajs present inserted by user");
            JOptionPane.showMessageDialog(null, String.format("Chyba. \n Doteraz nebol zaevidovaný Vami zadaný údaj."));

        } else {

            //podla zalozky najprv vycisti vsetky udaje a nasledne vypln prislusnu cast udaja
            switch (tabName) {
                case "lit":
                    clearTabFields("lit");
                    populateLitZdrojAndLitRevs(poslednyUdaj);
                    break;
                case "herb":
                    HerbarPolozky herbPolozky = poslednyUdaj.getHerbarPolozky();
                    clearTabFields("herb");
                    if (herbPolozky != null) {
                        populateHerbarPolozky(herbPolozky);
                    }
                    break;
                case "rev":
                    List<SkupRev> rev = poslednyUdaj.getRevisions();
                    clearTabFields("rev");
                    if (!rev.isEmpty()) {
                        populateRevision(replicateRevisions(rev));
                    } else {
                        jTableRevisions.setModel(new RevisionsTableModel());
                    }
                    break;
                case "loc":
                    Lokality loc = poslednyUdaj.getLokality();
                    clearTabFields("loc");
                    if (loc != null) {
                        populateLokality(loc, poslednyUdaj);
                    }
                    break;
                case "ident":
                    SkupRev urc = poslednyUdaj.getUrcenie();
                    clearTabFields("ident");
                    if (urc != null) {
                        populateIdentification(urc);
                    }
                    break;
                case "status":
                    populateStatusUdaja(poslednyUdaj);
                    break;
                case "images":
                    List<UdajObrazky> udajObrazkies = poslednyUdaj.getObrazky();
                    clearTabFields("images");
                    if (!udajObrazkies.isEmpty()) {
                        populateImages(udajObrazkies, true);
                        enabledImageFields(false);
                    } else {
                        jTableImages.setModel(new ImagesTableModel());
                    }

                    break;
            }
        }
    }

    /**
     * Sets counter of owned records to their end + 1. 
     * First record is 1, second 2, etc.
     */
    private void setRecViewCounterToNew() {
        cbListAllRec.setSelected(false);
        btnNextRec.setEnabled(false);
        long myRecordsCount = hq.getAllMyRecordsCount();
        //+1 kvoli nastaveniu pozicie na novy udaj
        long actualRecPosition = myRecordsCount + 1;
        String recPositionText = actualRecPosition + " / " + actualRecPosition;
        lblRecPosition.setText(recPositionText);
        if (actualRecPosition > 1) {
            btnPrevRec.setEnabled(true);
        } else {
            btnPrevRec.setEnabled(false);
            btnNextRec.setEnabled(false);
        }
    }

    /**
     * Sets counter of owned records to desired value.
     * @param id 
     */
    private void setRecViewCounterToId(int id) {

        long recordsCount, recordPosition;

        //ak zaznam nie je novym zaznamom
        if (id != -1) {

            if (!cbListAllRec.isSelected()) { //owned records
                recordsCount = hq.getAllMyRecordsCount();
                recordPosition = hq.getMyUdajIdPosition(id);

            } else { //all records
                recordsCount = hq.getUdajAllCount();
                recordPosition = hq.getUdajIdPosition(id);
            }
        } else {
            if (!cbListAllRec.isSelected()) {
                recordsCount = hq.getAllMyRecordsCount();
                recordPosition = recordsCount;

            } else {
                recordsCount = hq.getUdajAllCount();
                recordPosition = recordsCount;
            }
        }
        String recPositionText = recordPosition + " / " + recordsCount;
        lblRecPosition.setText(recPositionText);
        btnPrevRec.setEnabled(false);
        btnNextRec.setEnabled(false);
        if (recordPosition > 1) {
            btnPrevRec.setEnabled(true);
        }
        if (recordPosition <= recordsCount) {
            btnNextRec.setEnabled(true);
        }
    }

    /**
     * Record tab is tab where data of the record are set - either for new record 
     * or for its editing.
     * @return Record tab
     */
    public javax.swing.JPanel getRecordTab() {
        return recordTab;
    }

    public void setRecordTab(javax.swing.JPanel recordTab) {
        this.recordTab = recordTab;
    }

    public LitZdroj getSelectedLzdroj() {
        return selectedLzdroj;
    }

    public void setSelectedLzdroj(LitZdroj selectedLzdroj) {
        this.selectedLzdroj = selectedLzdroj;
    }

    public javax.swing.JTextField getTfLitBook() {
        return tfLitBook;
    }

    public void setTfLitBook(javax.swing.JTextField tfLitBook) {
        this.tfLitBook = tfLitBook;
    }

    /**
     * Hides Records overview tab.
     */
    public void hideRecordsOverviewTab() {
        this.jTabbedPaneMain.remove(recordsOverviewTab);
    }

    /**
     * Populates Record tab in the background process.
     */
    class PopulateRecordTask extends SwingWorker<Void, Integer> {

        /*
     * Main task. Executed in background thread.
         */
        int modelRow;
        String udajOwner = "";

        List<Udaj> udajs = null;

        public PopulateRecordTask(int modelRow) {
            this.modelRow = modelRow;
        }

        @Override
        public Void doInBackground() {

            int id = (int) filterRecordsOverview.getRecordsOverviewTable().getModel().getValueAt(modelRow, 0);

            Udaj udaj = (Udaj) hq.getById(Udaj.class, id);
            populateRecordTabs(udaj, false);

            getRecordTab().setName("Edit " + id); //showid
            lblUdajTyp.setText(udaj.getTyp().toString());

            setTabsAndFocus(udaj.getTyp(), true);
            //setNewRecordTabName(udaj.getTyp());

            jTabbedPaneMain.add(getRecordTab());
            jTabbedPaneMain.setSelectedComponent(getRecordTab());

            ignoreEvents = true;
            cbListAllRec.setSelected(!filterRecordsOverview.getCbMyOwnRecords().isSelected());
            ignoreEvents = false;
            setRecViewCounterToId(id);

            //zistime ci udaj je editovatelny uzivatelom (je jeho vlastnik, alebo je admin) a podla toho nastavime ci sa moze neskor pripadne updatovat
            if (udaj.getUzivatel().equals(login) || isAdmin) {
                btnRecordSave.setEnabled(true);
                btnRecordDelete.setEnabled(true);
                SaveAction sa = (SaveAction) btnRecordSave.getAction();
                sa.setUdaj(udaj);
                sa.setUdajType(udaj.getTyp());
                DeleteRecordAction dra = (DeleteRecordAction) btnRecordDelete.getAction();
                dra.setUdaj(udaj);
            } else {
                btnRecordSave.setEnabled(false);
                btnRecordDelete.setEnabled(false);
            }

            return null;
        }

        /*
         * Executed in event dispatching thread
         */
        @Override
        public void done() {

            loadingDialog.setVisible(false);

            // TODO: is needed?
            if (!udajOwner.equals(login) && !isAdmin) {
                //JOptionPane.showMessageDialog(MainFrame.this, "Nie je možné editovanie iba prehliadanie.");
            }
        }

        @Override
        protected void process(final List<Integer> pieces) {
            // Updates the progressbar state
        }
    }

    /**
     * Clears input fields on the given tab.
     * @param tabName The tab whose fields will be cleared
     */
    private void clearTabFields(String tabName) {

        // vymaz policka na prislusnej zalozke
        switch (tabName) {
            case "lit":
                clearLitFields();
                break;
            case "herb":
                clearHerbFields();
                break;
            case "rev":
                clearRevFields();
                break;
            case "loc":
                clearLocFields();
                break;
            case "ident":
                clearIdentFields();
                break;
            case "status":
                clearStatusFields();
                break;
            case "images":
                clearImagesFields();
                break;
        }
    }

    /**
     * Clear all input fields on the Record placement tab (herbarium).
     */
    private void clearHerbFields() {
        this.tfZarHerbar.setEntity(null);
        this.tfZarDallaTorre.setEntity(null);
        this.tfZarCisloPol.setText(null);
        this.tfZarBarcode.setText(null);
        this.tfZarCisloZberu.setText(null);
        this.tfZarKvantif.setText(null);
        this.tfZarExsikat.setEntity(null);
        this.tfZarVoucher.setEntity(null);
        this.taZarHerbPoznamka.setText(null);
        this.tfLitPublisher.setText(null);
        this.cbTypeUnit.setSelected(false);
    }

    /**
     * Clear all input fields on the Locality tab.
     */
    private void clearLocFields() {
        this.tfLocFlora.setEntity(null);
        this.tfLocBrumFour.setEntity(null);
        this.tfLocVac.setEntity(null);
        this.tfLocVgc.setEntity(null);
        this.tfLocNac.setEntity(null);
        this.tfLocNgc.setEntity(null);
        this.tfLocProtArea.setEntity(null);
        this.tfLocVill.setEntity(null);
        this.taLocDescr.setText(null);
        this.tfLocAltFrom.setText(null);
        this.tfLocAltTo.setText(null);
        this.chbLocAltCca.setSelected(false);
        this.tfLocLatDeg.setText(null);
        this.tfLocLatMin.setText(null);
        this.tfLocLatSec.setText(null);
        this.tfLocLngDeg.setText(null);
        this.tfLocLngMin.setText(null);
        this.tfLocLngSec.setText(null);
        this.rbLocNorth.setSelected(true);
        this.rbLocEast.setSelected(true);
        this.tfLocSoil.setText(null);
        this.tfLocSubstrate.setText(null);
        this.tfLocHost.setText(null);
        this.cbLocCoordType.setSelectedIndex(0);
        this.tfLocAltEpif.setText(null);
        this.jListCollectors.clear();
        this.jListQuadrants.clear();
        this.jListFyto.clear();
        this.tfDatumZbD.setText(null);
        this.tfDatumZbM.setText(null);
        this.tfDatumZbR.setText(null);
        this.tfDatumZbSlovom.setText(null);
        this.taLocScheda.setText(null);
        this.taLocNotes.setText(null);
    }

    /**
     * Clear all fields on the Identification tab.
     */
    private void clearIdentFields() {
        this.tfUrcNazovScheda.setText(null);
        this.tfUrcStdMeno.setEntity(null);
        this.tfUrcAccName.setText(null);
        this.tfUrcPoznamka.setText(null);
        this.jListIdentificators.clear();
        this.tfDatumUrcD.setText(null);
        this.tfDatumUrcM.setText(null);
        this.tfDatumUrcR.setText(null);
        this.tfDatumUrcSlovom.setText(null);
    }

    /**
     * Clear all fields on the Record status tab.
     */
    private void clearStatusFields() {
        this.rbIhned.setSelected(true);
        this.tfNeverDD.setText(null);
        this.tfNeverMM.setText(null);
        this.tfNeverRRRR.setText(null);
        this.jPoznamkaNepublik.setText(null);
        this.fotograficky.setSelected(false);
    }

    /**
     * Clear all fields on the Images tab.
     */
    private void clearImagesFields() {
        this.tfFindLocalImageName.setText(null);
        this.taImageDescr.setText(null);
        this.tfImageUrl.setText(null);
        this.lblImageDisplay.setIcon(null);
        this.activeImage = null;
    }

    /**
     * Handles actions after literature source is selected - populates literature fields.

     * @param lzdroj 
     */
    public void literatureSelected(LitZdroj lzdroj) {
        populateLitZdrojFields(lzdroj);
        enabledLitFields(true, true);
        setSelectedLzdroj(lzdroj);

        // TODO: selectedLzdroj vs SaveAction co z toho?
        //SaveAction sa = (SaveAction) btnRecordSave.getAction();
        //sa.setLzdroj(lzdroj);
    }

    /**
     * Handles actions after new literature has been saved.
     */
    public void literatureAddNew() {
        enabledLitFields(true, true);
        clearLitFields();
        setSelectedLzdroj(null);
    }

    /**
     * Method of PropertyChangeListener implemented by MainFrame
     * @param evt 
     */
    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if ("progress".equals(evt.getPropertyName())) {
            int progress = (Integer) evt.getNewValue();
            loadingDialog.setProgress(progress);
            String note = String.format("Dokončených %d%%.\n", progress);
            loadingDialog.setNote(note);
            if (loadingDialog.getProgress() == 100) {
                loadingDialog.setVisible(false);
            }
        }
    }

    
    /**
     * Import from file action.
     */
    private class DataImportAction extends AbstractAction {

        public DataImportAction() {
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            try {

                JFileChooser fileopen = new JFileChooser(".");
                FileNameExtensionFilter xlsfilter = new FileNameExtensionFilter("xls files (*.xls)", "xls", "xlsx");
                fileopen.setFileFilter(xlsfilter);
                fileopen.setDialogTitle("Vyberte súbor na import");
                fileopen.setApproveButtonText("Importuj súbor");

                int ret = fileopen.showOpenDialog(null);

                if (ret == JFileChooser.APPROVE_OPTION) {

                    // vytvorenie okna s aktualnymi informaciami o priebehu importu
                    ExcelImportMonitor importMonitor = ExcelImportMonitor.getInstance();
                    importMonitor.setHq(hq);
                    ((ImportMonitorModel) importMonitor.getImportMonitorTable().getModel()).clear();
                    importMonitor.prepareImport(fileopen.getSelectedFile());
                }

            } catch (IOException ex) {
                log.error("Exception while trying to read from specified import file", ex);
                JOptionPane.showMessageDialog(MainFrame.this, "Nastala chyba pri výbere súboru pre import. \n Skontrolujte jeho umiestnenie a typ.", "Chyba pri výbere súboru", JOptionPane.WARNING_MESSAGE);
            }
        }
    }

    /**
     * Handles records loading when table name is clicked in the Tables overview.
     */
    private class TableListSelectionListener implements ListSelectionListener {

        @Override
        public void valueChanged(ListSelectionEvent lse) {

            // pri vybere z ponuky tauliek sa zachytia 2 volania list listenera, preto prve z nich odignorujeme
            if (!lse.getValueIsAdjusting()) {

                btnTablesPrevPage.setEnabled(false);

                String tableName = (String) jListTables.getSelectedValue();

                //kedze niektore z tabuliek nemaju stlpec "name", treba urcit podla coho sa maju usporiadat
                List<AssociableEntity> resultList;
                switch (tableName) {
                    case "DallaTorre":
                        resultList = hq.getAllRecords(tableName, "idEvid");
                        break;
                    case "Herbar":
                        resultList = hq.getAllRecords(tableName, "skratkaHerb");
                        break;
                    case "LitZdroj":
                        resultList = hq.getAllRecords(tableName, "id");
                        break;
                    case "TaxonEndemizmus":
                        resultList = hq.getAllRecords(tableName, "skratka");
                        break;
                    case "TaxonOhrozenost":
                        resultList = hq.getAllRecords(tableName, "skratka");
                        break;
                    case "TaxonPochybnost":
                        resultList = hq.getAllRecords(tableName, "skratka");
                        break;
                    case "TaxonPovodnost":
                        resultList = hq.getAllRecords(tableName, "skratka");
                        break;
                    case "Voucher":
                        resultList = hq.getAllRecords(tableName, "id");
                        break;
                    default:
                        resultList = hq.getAllRecords(tableName, "meno");
                }

                if (!resultList.isEmpty()) {

                    PagingModel tblModel = new PagingModel(resultList);
                    jTableOverview.setModel(tblModel);

                    if (jTableOverview.getColumnCount() > 0) {
                        jTableOverview.removeColumn(jTableOverview.getColumnModel().getColumn(jTableOverview.getColumnCount() - 1));
                    }

                    // TODO: ako ovplyvni pripadne rychlost nastavovanie sirky stlpcov?
                    //TableColumnsWidthCounter.setWidthOfColumns(jTableOverview);
                    int pageCount = tblModel.getPageCount();
                    String pagesText = "1/" + pageCount;
                    jLabelPages.setText(pagesText);
                    if (pageCount > 1) {
                        btnTablesNextPage.setEnabled(true);
                    } else {
                        btnTablesNextPage.setEnabled(false);
                        btnTablesPrevPage.setEnabled(false);
                    }
                } else {
                    PagingModel tblModel = new PagingModel(resultList);
                    jTableOverview.setModel(tblModel);
                    TableColumnsWidthCounter.setWidthOfColumns(filterRecordsOverview.getRecordsOverviewTable());
                    log.info("Table with no data yet: " + tableName);
                    JOptionPane.showMessageDialog(MainFrame.this, "Daná tabuľka zatiaľ neobsahuje žiadne dáta");
                }
            }
        }
    }

    /**
     * Triggered when checkbox to show or hide all/owned records is changed in the
     * new/edit record tab. Accordingly the last record id is handled.
     */
    private class AllRecordsChangeListener extends AbstractAction implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {

            String tabName = jTabbedPaneMain.getSelectedComponent().getName();
            if (tabName.substring(0, 3).equals("Edit")) {
                setRecViewCounterToId(Integer.valueOf(tabName.substring(5, tabName.length())));
            } else {
                setRecViewCounterToId(-1);
            }

            String recPositionText = lblRecPosition.getText();
            String[] recPositions = recPositionText.split("\\ / ");
            int recPosition = Integer.parseInt(recPositions[0]);

            // TODO: zostat na rovnakom udaji? (teraz sa nastavi na posledny udaj)
            //SaveAction sa = (SaveAction) btnRecordSave.getAction();
            //Udaj saUdaj = sa.getUdaj();
            Udaj udaj;
            if (cbListAllRec.isSelected()) {
                //long recCount = hq.getUdajAllCount();
                udaj = hq.getLastUdaj();
            } else {
                //long recCount = hq.getAllMyRecordsCount();
                udaj = hq.getMyLastUdaj();
            }

            lblUdajTyp.setText(udaj.getTyp().toString());

            setTabsAndFocus(udaj.getTyp(), true);

            btnRecordSave.setEnabled(true);
            btnRecordDelete.setEnabled(true);

            populateRecordTabs(udaj, false);
        }
    }

    /**
     * Handles populating of revision fields on Record revisions (Revizie) tab when a revision
     * is selected in the table (Prehlad revizii).
     */
    private class TableRevisionsSelectionListener implements ListSelectionListener {

        @Override
        public void valueChanged(ListSelectionEvent e) {

            int row = jTableRevisions.getSelectedRow();
            Object selected = null;
            if (jTableRevisions.getModel().getRowCount() > row) {
                selected = jTableRevisions.getModel().getValueAt(row, 0);
            }
            if (selected != null && selected instanceof SkupRev) {
                SkupRev rev = (SkupRev) selected;
                populateRevisionFields(rev);

                enabledRevFields(true);

                btnDelRev.setEnabled(true);
            } else {
                btnDelRev.setEnabled(false);
            }
        }
    }

    /**
     * Handles populating of fields on Images tab (Obrazky) when an image record
     * is selected in the table (Prehlad obrazkov).
     */
    private class TableImagesSelectionListener implements ListSelectionListener {

        @Override
        public void valueChanged(ListSelectionEvent e) {
            int row = jTableImages.getSelectedRow();
            Object selected = null;
            if (jTableImages.getModel().getRowCount() > row) {
                selected = jTableImages.getModel().getValueAt(row, 0);
            }
            if (selected != null && selected instanceof UdajObrazky) {
                btnDelImage.setEnabled(true);
                UdajObrazky image = (UdajObrazky) selected;

                populateImageFields(image);
                enabledImageFields(true);
            }
        }
    }

    /**
     * Handles populating of fields on Literature source tab (Literarny zdroj) when
     * a literature source is selected in the tab (Prehlad literarnych zdrojov).
     */
    private class TableLitSourceSelectionListener implements ListSelectionListener {

        @Override
        public void valueChanged(ListSelectionEvent e) {
            int row = jTableLitSources.getSelectedRow();
            Object selected = null;
            if (jTableLitSources.getModel().getRowCount() > row) {
                selected = jTableLitSources.getModel().getValueAt(row, 0);
            }
            if (selected != null && selected instanceof LitZdrojRev) {
                LitZdrojRev lzdrojRev = (LitZdrojRev) selected;
                populateLitZdrojFields(lzdrojRev.getLitZdroj());

                enabledLitFields(true, false);

                btnDeleteLitSource.setEnabled(true);
            } else {
                btnDeleteLitSource.setEnabled(false);
            }
        }
    }

    /**
     * Enables copy and delete buttons when a record is selected in the Records overview tab
     * (Prehlad udajov). When the record is deselected, the buttons are disabled.
     */
    private class TableRecordsOverviewSelectionListener implements ListSelectionListener {

        @Override
        public void valueChanged(ListSelectionEvent e) {
            int row = filterRecordsOverview.getRecordsOverviewTable().getSelectedRow();
            if (row > -1) {
                btnOverviewCopyRec.setEnabled(true);
                btnOverviewDeleteRec.setEnabled(true);
            } else {
                btnOverviewCopyRec.setEnabled(false);
                btnOverviewDeleteRec.setEnabled(false);
            }
        }
    }

    /**
     * Enables/disables delete button when record is selected on the Tables overview 
     * tab (Prehlad tabuliek).
     */
    private class TablesOverviewSelectionListener implements ListSelectionListener {

        @Override
        public void valueChanged(ListSelectionEvent e) {
            int row = jTableOverview.getSelectedRow();
            if (row > -1 && isAdmin) {
                btnTablesDeleteRec.setEnabled(true);
            } else {
                btnTablesDeleteRec.setEnabled(false);
            }
        }
    }

    /**
     * Handles double click on the record in Tables overview tab (Prehlad tabuliek). Opens a dialog to edit
     * doubleclicked record.
     */
    private class TablesOverviewDoubleClick extends MouseAdapter {

        @Override
        public void mouseClicked(MouseEvent e) {
            if (e.getClickCount() == 2) {
                JTable target = (JTable) e.getSource();
                int row = target.getSelectedRow();
                if (row > -1 && isAdmin) {
                    int modelRow = target.convertRowIndexToModel(row);
                    if (target.getModel() instanceof PagingModel) {
                        log.info("TablesOverview entity populated - to be edited.");
                        PagingModel tblModel = (PagingModel) target.getModel();
                        dcfEntity.setIsNew(false);
                        dcfEntity.setEntity((AssociableEntity) tblModel.getValueAt(modelRow, tblModel.getColumnCount() - 1));
                        dcfEntity.showFrame(tblModel);
                    }
                }
            }
        }
    }

    /**
     * Handles double click on the record in Records overview tab (Prehlad udajov).
     * Opens doubleclicked record for editing.
     */
    private class RecordsOverviewDoubleClick extends MouseAdapter {

        @Override
        public void mouseClicked(MouseEvent e) {
            if (e.getClickCount() == 2) {
                int row = filterRecordsOverview.getRecordsOverviewTable().getSelectedRow();
                if (row > -1) {

                    log.info("Populating double-clicked udaj");

                    loadingDialog = new LoadingDialog("Načítavanie dát údaja", "Načítavam dáta zvoleného údaja...", 0, 100, MainFrame.this);
                    loadingDialog.getProgressBar().setIndeterminate(true);

                    int modelRow = filterRecordsOverview.getRecordsOverviewTable().convertRowIndexToModel(row);

                    PopulateRecordTask lra = new PopulateRecordTask(modelRow);
                    lra.execute();
                }
            }
        }
    }

    /**
     * Handles a correct display of accepted name when plant name is selected.
     * When selected name itself is an accepted name, same name is displayed. 
     * Otherwise the corresponding accepted name is displayed.
     */
    private class StdMenoDocumentListener implements DocumentListener {

        private TextFieldMoznosti fieldStdName;
        private JTextField fieldAccName;
        private JTextField fieldPochybnost;
        private JTextField fieldOhrozenost;
        private JTextField fieldEndemizmus;
        private JTextField fieldPovodnost;
        private JTextField fieldSvkNazov;
        private JCheckBox cbOchrana;

        public StdMenoDocumentListener(TextFieldMoznosti fieldStdName, JTextField fieldAccName, JTextField fieldPochybnost, JTextField fieldOhrozenost, JTextField fieldEndemizmus, JTextField fieldPovodnost, JTextField fieldSvkNazov, JCheckBox cbOchrana) {
            this.fieldStdName = fieldStdName;
            this.fieldAccName = fieldAccName;
            this.fieldPochybnost = fieldPochybnost;
            this.fieldOhrozenost = fieldOhrozenost;
            this.fieldEndemizmus = fieldEndemizmus;
            this.fieldPovodnost = fieldPovodnost;
            this.fieldSvkNazov = fieldSvkNazov;
            this.cbOchrana = cbOchrana;
        }

        @Override
        public void insertUpdate(DocumentEvent e) {
            if (fieldStdName.getEntity() != null) {
                setAcceptedNameAndAttrs();
            }
        }

        @Override
        public void removeUpdate(DocumentEvent e) {
            fieldAccName.setText(null);
            fieldPochybnost.setText(null);
            fieldOhrozenost.setText(null);
            fieldEndemizmus.setText(null);
            fieldPovodnost.setText(null);
            fieldSvkNazov.setText(null);
            cbOchrana.setSelected(false);
        }

        @Override
        public void changedUpdate(DocumentEvent e) {
            if (fieldStdName.getTextField().getText().isEmpty()) {
                fieldStdName.setEntity(null);
            }
            if (fieldStdName.getEntity() != null) {
                setAcceptedNameAndAttrs();
            }
        }

        private void setAcceptedNameAndAttrs() {
            ListOfSpecies los = (ListOfSpecies) fieldStdName.getEntity();
            if (los != null) {
                ListOfSpecies acc = los.getAcceptedName();
                fieldAccName.setText("");
                if (acc != null) {
                    fieldAccName.setText(acc.toString());
                } else {
                    fieldAccName.setText(fieldStdName.getTextField().getText());
                }
                fieldPochybnost.setText(los.getTaxonPochybnost() == null ? "" : los.getTaxonPochybnost().getSkratka());
                fieldOhrozenost.setText(los.getTaxonOhrozenost() == null ? "" : los.getTaxonOhrozenost().getSkratka());
                fieldEndemizmus.setText(los.getTaxonEndemizmus() == null ? "" : los.getTaxonEndemizmus().getSkratka());
                fieldPovodnost.setText(los.getTaxonPovodnost() == null ? "" : los.getTaxonPovodnost().getSkratka());
                fieldSvkNazov.setText(los.getTaxonSvkNazov());
                cbOchrana.setSelected(los.isTaxonOchrana());
            } else {
                fieldAccName.setText(null);
            }
        }
    }

    /**
     * Handles text changes in TextFieldMoznosti. Anything written to this field
     * is mirrored to the filter field of the FilterJList.
     */
    private class TextFieldMoznostiDocListener implements DocumentListener {

        private JTextField tf;
        private FilterJList listMoznosti;

        public TextFieldMoznostiDocListener(JTextField tf, FilterJList listMoznosti) {
            this.tf = tf;
            this.listMoznosti = listMoznosti;
        }

        @Override
        public void insertUpdate(DocumentEvent e) {
            this.listMoznosti.getFilterField().setText(this.tf.getText());
        }

        @Override
        public void removeUpdate(DocumentEvent e) {
            this.listMoznosti.getFilterField().setText(this.tf.getText());
        }

        @Override
        public void changedUpdate(DocumentEvent e) {
            this.listMoznosti.getFilterField().setText(this.tf.getText());
        }
    }

    /**
     * Actions
     */
    
    private class PreviousPageAction extends AbstractAction {

        @Override
        public void actionPerformed(ActionEvent e) {
            PagingModel tblModel = (PagingModel) jTableOverview.getModel();
            tblModel.pageDown();
            String pageText = (tblModel.getPageOffset() + 1) + "/" + tblModel.getPageCount();
            jLabelPages.setText(pageText);

            btnTablesNextPage.setEnabled(true);

            if (tblModel.getPageOffset() == 0) {
                btnTablesPrevPage.setEnabled(false);
            } else {
                btnTablesPrevPage.setEnabled(true);
            }
        }
    }

    private class NextPageAction extends AbstractAction {

        @Override
        public void actionPerformed(ActionEvent e) {
            PagingModel tblModel = (PagingModel) jTableOverview.getModel();
            tblModel.pageUp();
            String pageText = (tblModel.getPageOffset() + 1) + "/" + tblModel.getPageCount();
            jLabelPages.setText(pageText);

            btnTablesPrevPage.setEnabled(true);

            if (tblModel.getPageOffset() == tblModel.getPageCount() - 1) {
                btnTablesNextPage.setEnabled(false);
            } else {
                btnTablesNextPage.setEnabled(true);
            }
        }
    }

    private class SearchAction extends AbstractAction {

        @Override
        public void actionPerformed(ActionEvent e) {
            String expr = jTextFieldFilter.getText();
            if (expr.length() == 0) {
                sorter.setRowFilter(null);
            } else {
                try {
                    sorter.setModel((PagingModel) jTableOverview.getModel());
                    sorter.setRowFilter(RowFilter.regexFilter("(?i)" + expr));
                    PagingModel pm = (PagingModel) jTableOverview.getModel();
                    jTableOverview.setRowSorter(sorter);
                    pm.getData();

                } catch (PatternSyntaxException pse) {
                    log.error("Wrong formatted filtering expression: " + expr, pse);
                    JOptionPane.showMessageDialog(MainFrame.this, "Nekorektne zadaný výraz na filtrovanie", "Chybný výraz", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }

    private class CancelSearchAction extends AbstractAction {

        @Override
        public void actionPerformed(ActionEvent e) {
            sorter.setModel((PagingModel) jTableOverview.getModel());
            sorter.setRowFilter(null);
            jTableOverview.setRowSorter(sorter);
        }
    }

    private class NewRevisionAction extends AbstractAction {

        @Override
        public void actionPerformed(ActionEvent e) {
            clearRevFields();
            enabledRevFields(true);
            btnAddRevDown.setEnabled(false);
            isRevisionNew = true;
        }
    }

    private class CancelRevisionAction extends AbstractAction {

        @Override
        public void actionPerformed(ActionEvent e) {
            clearRevFields();
            enabledRevFields(false);
            jTableRevisions.clearSelection();
            btnAddRevDown.setEnabled(true);
        }
    }

    private class AddRevisionAction extends AbstractAction {

        @Override
        public void actionPerformed(ActionEvent e) {
            AssociableEntity stdMeno = tfRevStdMeno.getEntity();
            //yet not requred, so commented out
//            if (stdMeno != null && stdMeno instanceof ListOfSpecies) {
            ListOfSpecies meno = (ListOfSpecies) stdMeno;
            String nazovListok = tfRevNazovListok.getText();
            String poznamka = tfRevPoznamka.getText();
            String datum = HandyUtils.eToN(HandyUtils.createDate(tfDatumRevD.getText(), tfDatumRevM.getText(), tfDatumRevR.getText()));
            String datumSlov = tfDatumRevSlovom.getText();
            List<MenaZberRev> revisors = jListRevisors.getContainer();

            SkupRev rev;
            MenaTaxonov menaTax;
            RevisionsTableModel revModel = (RevisionsTableModel) jTableRevisions.getModel();
            // momentalne vyuzivana iba prva vetva
            if (isRevisionNew) {
                menaTax = new MenaTaxonov(meno, nazovListok, poznamka, null);
                rev = new SkupRev(null, menaTax, datum, true, datumSlov);
                revModel.getRevisions().add(ListAddRemoveUtils.setRevisorsAndIdentificators(rev, revisors));
            } else {
                int row = jTableRevisions.getSelectedRow();
                if (row > -1) {
                    int modelRow = jTableRevisions.convertColumnIndexToModel(row);
                    rev = revModel.getRevisions().get(modelRow);
                    menaTax = rev.getMenaTaxonov();
                    menaTax.setSpecies(meno);
                    menaTax.setMenoScheda(nazovListok);
                    menaTax.setPoznamka(poznamka);
                    rev = ListAddRemoveUtils.setRevisorsAndIdentificators(rev, revisors);
                    rev.setDatum(datum);
                    rev.setDatumSlovom(datumSlov);
                    rev.setMenaTaxonov(menaTax);
                }
            }

            revModel.fireTableDataChanged();
            clearRevFields();
            enabledRevFields(false);
            MainFrame.this.btnAddRevDown.setEnabled(true);

//            } else {
//                log.error("Nevyplnene revidovane meno.");
//                JOptionPane.showMessageDialog(MainFrame.this, "Nie je vyplnené revidované meno", "Prázdne povinné pole", JOptionPane.ERROR_MESSAGE);
//            }
        }
    }

    private class DeleteRevisionAction extends AbstractAction {

        @Override
        public void actionPerformed(ActionEvent e) {
            int row = jTableRevisions.getSelectedRow();
            RevisionsTableModel revModel = (RevisionsTableModel) jTableRevisions.getModel();
            if (row > -1) {
                int modelRow = jTableRevisions.convertColumnIndexToModel(row);

                String[] options = {"Áno", "Nie"};
                int answer = JOptionPane.showOptionDialog(MainFrame.this,
                        "Naozaj odstrániť revíziu?",
                        "Obstránenie revízie",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.WARNING_MESSAGE,
                        null,
                        options,
                        options[1]);
                if (answer == 0) {
                    revModel.getRevisions().remove(modelRow);
                    revModel.fireTableDataChanged();
                    log.info("Revision removed from revision table.");
                    jTableRevisions.clearSelection();
                    clearRevFields();
                    enabledRevFields(false);
                }
            }
        }
    }

    private class NewImageAction extends AbstractAction {

        @Override
        public void actionPerformed(ActionEvent e) {
            clearImageFields();
            enabledImageFields(true);
            btnAddImageDown.setEnabled(false);
            isImageNew = true;
        }
    }

    private class AddImageAction extends AbstractAction {

        @Override
        public void actionPerformed(ActionEvent e) {
            String popis = taImageDescr.getText();
            BufferedImage image = activeImage;

            UdajObrazky uo;
            ImagesTableModel imagesModel = (ImagesTableModel) jTableImages.getModel();
            // momentalne vyuzivana iba prva vetva
            if (isImageNew) {
                SaveAction sa = (SaveAction) btnRecordSave.getAction();
                uo = new UdajObrazky(sa.getUdaj(), popis, HandyUtils.imageToByteArray(image), null);
                imagesModel.getImages().add(uo);
            } else {
                int row = jTableImages.getSelectedRow();
                if (row > -1) {
                    int modelRow = jTableImages.convertColumnIndexToModel(row);
                    uo = imagesModel.getImages().get(modelRow);
                    uo.setPopis(popis);

                    byte[] byteArray = HandyUtils.imageToByteArray(image);
                    uo.setObrazok(byteArray);
                }
            }

            imagesModel.fireTableDataChanged();
            clearImageFields();
            enabledImageFields(false);
            btnAddImageDown.setEnabled(true);
        }
    }

    private class CancelImageAction extends AbstractAction {

        @Override
        public void actionPerformed(ActionEvent e) {
            clearImageFields();
            enabledImageFields(false);
            jTableImages.clearSelection();
            btnAddImageDown.setEnabled(true);
        }
    }

    private class DeleteImageAction extends AbstractAction {

        @Override
        public void actionPerformed(ActionEvent e) {
            int row = jTableImages.getSelectedRow();
            ImagesTableModel imagesModel = (ImagesTableModel) jTableImages.getModel();
            if (row > -1) {
                int modelRow = jTableImages.convertColumnIndexToModel(row);

                String[] options = {"Áno", "Nie"};
                int answer = JOptionPane.showOptionDialog(MainFrame.this,
                        "Naozaj odstrániť obrázok?",
                        "Obstránenie obrázku",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.WARNING_MESSAGE,
                        null,
                        options,
                        options[1]);
                if (answer == 0) {
                    imagesModel.getImages().remove(modelRow);
                    imagesModel.fireTableDataChanged();
                    log.info("Image removed from image table.");
                    jTableImages.clearSelection();
                    clearImageFields();
                    enabledImageFields(false);
                }
            }
        }
    }

    /**
     * Obsluhuje volbu pridania literarnej polozky.
     */
    private class NewLitSourceAction extends AbstractAction {

        @Override
        public void actionPerformed(ActionEvent e) {
            clearLitFields();
            if (jTableLitSources.getSelectedRow() > 0) {
                jTableLitSources.removeRowSelectionInterval(0, jTableLitSources.getRowCount() - 1);
            }
            enabledLitFields(false, false);
            jTableLitSources.clearSelection();
            LiteratureSearchForm.getInstance(MainFrame.this, hq);
        }
    }

    private class CancelLitSourceAddingAction extends AbstractAction {

        @Override
        public void actionPerformed(ActionEvent e) {
            clearLitFields();
            jTableLitSources.clearSelection();
//            listMoznosti.clearSelection();
            listMoznosti.clear();

            enabledLitFields(false, false);
            setSelectedLzdroj(null);
        }
    }

    private class SaveLitSourceToTableAction extends AbstractAction {

        @Override
        public void actionPerformed(ActionEvent e) {
            LitZdrojRev lzdrojRev = null;
            LiteraturesTableModel model = (LiteraturesTableModel) jTableLitSources.getModel();
            int row = MainFrame.this.jTableLitSources.getSelectedRow();
            int modelRow = jTableLitSources.convertRowIndexToModel(row);
            if (row > -1) {
                // momentalne nevyuzivane, neda sa modifikovat lzdroj z tabulky, iba odstranit a nanovo pridat
                lzdrojRev = model.getLiteratures().get(modelRow);
            }
            LitZdrojRev lzdrojAdd = createOrModifyLitZdrojRev(lzdrojRev);
            // ak pridavame lit. zdroj pridaj riadok inak zaktualizuj data zmeneneho lit. zdroja v tabulke
            if (lzdrojRev == null) {
                if (lzdrojAdd != null) {
                    model.getLiteratures().add(lzdrojAdd);
                }
            } else {
                // momentalne nevyuzivane
                model.getLiteratures().set(modelRow, lzdrojAdd);
            }

            model.fireTableDataChanged();

            clearLitFields();
            enabledLitFields(false, false);
        }
    }

    private class DeleteLitSourceAction extends AbstractAction {

        @Override
        public void actionPerformed(ActionEvent e) {
            int row = jTableLitSources.getSelectedRow();
            LiteraturesTableModel model = (LiteraturesTableModel) jTableLitSources.getModel();
            if (row > -1) {
                int modelRow = jTableLitSources.convertColumnIndexToModel(row);
                String[] options = {"Áno", "Nie"};
                int answer = JOptionPane.showOptionDialog(MainFrame.this,
                        "Naozaj odstrániť literárny zdroj?",
                        "Odstránenie literárneho zdroja",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.WARNING_MESSAGE,
                        null,
                        options,
                        options[1]);
                if (answer == 0) {
                    model.getLiteratures().remove(modelRow);
                    model.fireTableDataChanged();
                    log.info("Literature removed from literature table.");
                    jTableLitSources.clearSelection();
                    clearLitFields();
                    enabledLitFields(false, false);
                    btnDeleteLitSource.setEnabled(false);
                }
            }
        }
    }

    /**
     * Saves record to the database.
     */
    private class SaveAction extends AbstractAction {

        private Udaj udaj = null;
        private char udajType;
        private boolean udajVerejny = true;
        private String udajVerejnyDatum = null;
        //private LitZdroj lzdroj = null;

        public SaveAction() {
        }

        public SaveAction(Udaj udaj, char udajType, LitZdroj lzdroj) {
            this.udaj = udaj;
            this.udajType = udajType;
            //this.lzdroj = lzdroj;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                UdajFactory uf = UdajFactory.getInstance(MainFrame.this.session);
                Lokality lok = createOrModifyLokalita(this.udaj == null ? null : this.udaj.getLokality());
                SkupRev urc = createOrModifyUrcenie(this.udaj == null ? null : this.udaj.getUrcenie());
                HerbarPolozky herb = createOrModifyHerbPolozka(this.udaj == null ? null : this.udaj.getHerbarPolozky());
                List<LitZdrojRev> lits = createOrModifyLiterature();
                List<SkupRev> revs = createOrModifyRevisions();
                List<UdajObrazky> obr = createOrModifyImages();
                Set<PeopleAsoc> zbers = createOrModifyZberatelia(this.udaj);
                String datumZb = HandyUtils.eToN(HandyUtils.createDate(MainFrame.this.tfDatumZbD.getText(), MainFrame.this.tfDatumZbM.getText(), MainFrame.this.tfDatumZbR.getText()));
                String datumSl = MainFrame.this.tfDatumZbSlovom.getText();

                uf.createUdaj(this.udaj, lok, herb, lits, revs, urc, obr, zbers, udajType, datumZb, datumSl, udajVerejny, udajVerejnyDatum, login);
                clearAll();
                this.udaj = null;

                MainFrame.this.jTabbedPaneMain.setSelectedIndex(1);
                MainFrame.this.jTabbedPaneMain.remove(getRecordTab());
            } catch (EntityException ex) {
                log.error("Exception while saving udaj to DB", ex);
                JOptionPane.showMessageDialog(MainFrame.this, "Chyba pri zápise údaja do databázy. \n Skúste akciu zopakovať znova", "Chyba pri zápise", JOptionPane.ERROR_MESSAGE);
            }
        }

        public Udaj getUdaj() {
            return udaj;
        }

        public void setUdaj(Udaj udaj) {
            this.udaj = udaj;
        }

        public char getUdajType() {
            return udajType;
        }

        public void setUdajType(char udajType) {
            this.udajType = udajType;
        }

        public boolean isUdajVerejny() {
            return udajVerejny;
        }

        public void setUdajVerejny(boolean udajVerejny) {
            this.udajVerejny = udajVerejny;
        }

        public String getUdajVerejnyDatum() {
            return udajVerejnyDatum;
        }

        public void setUdajVerejnyDatum(String udajVerejnyDatum) {
            this.udajVerejnyDatum = udajVerejnyDatum;
        }
    }

    private class CancelAction extends AbstractAction {

        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() instanceof JButton) {
                // isActive
                if (MainFrame.this.session.getTransaction().getStatus().equals(TransactionStatus.ACTIVE)) {
                    MainFrame.this.session.getTransaction().rollback();
                }

                clearAll();
                JButton cancel = (JButton) e.getSource();
                Component parent = cancel.getParent();
                jTabbedPaneMain.remove(parent);

                //(ak neboli) spristupni tlacidla na pripadne buduce editovanie udaja
                btnRecordSave.setEnabled(true);
                btnRecordDelete.setEnabled(true);
            }
        }
    }

    /**
     * Spustena pri kliknuti na tlacitko Pridat v prehlade tabuliek a na
     * tlacitko plus pri kazdom TextFieldMoznosti
     */
    private class AddNewObjectAction extends AbstractAction {

        PagingModel tblModel;

        @Override
        public void actionPerformed(ActionEvent e) {
            String name = "";
            if (e.getSource() instanceof JButton) {
                JButton btn = (JButton) e.getSource();
                name = btn.getName();
            }
            AssociableEntity entity;
            tblModel = (PagingModel) MainFrame.this.jTableOverview.getModel();
            if (name != null && name.equals("jListMoznostiAdd")) { //kliknute pridat na zozname
                List<AssociableEntity> moznosti = MainFrame.this.listMoznosti.getContainer();
                if (moznosti != null && !moznosti.isEmpty()) {
                    log.info("JListMoznosti - creating new entity.");
                    entity = moznosti.get(0);
                    openDialog(entity);
//                    MainFrame.this.listMoznosti.getMoznostiListModel().addElement(entity);
                    MainFrame.this.listMoznosti.setContainer(moznosti);
                }
            } else { //kliknute pridat na prehlade tabuliek
                if (tblModel != null && tblModel.getData() != null && !tblModel.getData().isEmpty()) {
                    entity = tblModel.getData().get(0);
                    log.info("TablesOverview - creating new entity.");
                    openDialog(entity);
                }
            }
        }

        private void openDialog(AssociableEntity entity) {
            if (entity != null) {
                MainFrame.this.dcfEntity.setIsNew(true);
                MainFrame.this.dcfEntity.setEntity(entity);
                MainFrame.this.dcfEntity.showFrame(tblModel);
            }
        }
    }

    private class DeleteRowAction extends AbstractAction {

        @Override
        public void actionPerformed(ActionEvent e) {
            if (jTableOverview.getModel() instanceof PagingModel) {
                int row = jTableOverview.getSelectedRow();
                if (row > -1) {
                    String[] options = {"Áno", "Nie"};
                    int answer = JOptionPane.showOptionDialog(MainFrame.this,
                            "Naozaj odstrániť položku?",
                            "Odstránenie položky",
                            JOptionPane.YES_NO_OPTION,
                            JOptionPane.WARNING_MESSAGE,
                            null,
                            options,
                            options[1]);
                    if (answer == 0) {
                        log.info("Deleting entity from DB.");
                        int modelRow = jTableOverview.convertRowIndexToModel(row);
                        if (jTableOverview.getModel() instanceof PagingModel) {
                            PagingModel tblModel = (PagingModel) jTableOverview.getModel();
                            AssociableEntity ent = (AssociableEntity) tblModel.getValueAt(modelRow, tblModel.getColumnCount() - 1);
                            if (ent != null) {
                                try {
                                    hq.deleteEntity(ent);
                                    tblModel.getData().remove(modelRow);
                                    tblModel.fireTableDataChanged();
                                    log.info("Entity deleted successfully.");
                                } catch (ConstraintViolationException cve) {
                                    log.error("Exception while deleting entity. Probably there exists other entity bind to this entity", cve);
                                    JOptionPane.showMessageDialog(MainFrame.this, "Záznam nemôže byť odstránený z databázy. \n Odkazuje naňho niektorý iný záznam \n a mohlo by prísť ku strate informácii.", "Problém pri odstraňovaní záznamu", JOptionPane.WARNING_MESSAGE);
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private class DeleteRecordAction extends AbstractAction {

        private Udaj udaj = null;

        public DeleteRecordAction() {
        }

        public DeleteRecordAction(Udaj udaj) {
            this.udaj = udaj;
        }

        @Override
        public void actionPerformed(ActionEvent e) {

            // ak sa odstranuje udaj z tabulky prehladu udajov
            if (e.getSource() == btnOverviewDeleteRec) {
                if (filterRecordsOverview.getRecordsOverviewTable().getModel() instanceof RecordsInsertedModel) {
                    int row = filterRecordsOverview.getRecordsOverviewTable().getSelectedRow();
                    if (row > -1) {
                        int modelRow = filterRecordsOverview.getRecordsOverviewTable().convertColumnIndexToModel(row);
                        if (filterRecordsOverview.getRecordsOverviewTable().getModel() instanceof RecordsInsertedModel) {
                            String[] options = {"Áno", "Nie"};
                            int answer = JOptionPane.showOptionDialog(MainFrame.this,
                                    "Naozaj odstrániť položku?",
                                    "Odstránenie položky",
                                    JOptionPane.YES_NO_OPTION,
                                    JOptionPane.WARNING_MESSAGE,
                                    null,
                                    options,
                                    options[1]);
                            if (answer == 0) {

                                log.info("Deleting udaj from DB.");
                                loadingDialog = new LoadingDialog("Vymazávanie údaju", "Čakajte prosím, údaj sa vymazáva z databázy...", 0, 100, MainFrame.this);
                                loadingDialog.getProgressBar().setIndeterminate(true);

                                RecordsInsertedModel model = (RecordsInsertedModel) filterRecordsOverview.getRecordsOverviewTable().getModel();
                                int id = (int) model.getValueAt(modelRow, 0);
                                try {
                                    DeleteRecordTask dra = new DeleteRecordTask(id, model, modelRow);
                                    dra.execute();
                                    log.info("Udaj deleted successfully.");
                                } catch (Exception ex) {
                                    log.error("Exception while deleting udaj. Probably there exists other entity bind to this udaj", ex);
                                    JOptionPane.showMessageDialog(MainFrame.this, "Záznam nemôže byť odstránený z databázy. \n Odkazuje naňho niektorý iný záznam \n a mohlo by prísť ku strate informácii.", "Problém pri odstraňovaní záznamu", JOptionPane.WARNING_MESSAGE);
                                }
                            }
                        }
                    }
                }

                // ak sa odstranuje udaj v editovacom mode (rozpisany na zalozkach)
            } else if (e.getSource() == btnRecordDelete) {

                String[] options = {"Áno", "Nie"};
                int answer = JOptionPane.showOptionDialog(MainFrame.this,
                        "Naozaj odstrániť údaj z databázy?",
                        "Odstránenie údaja",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.WARNING_MESSAGE,
                        null,
                        options,
                        options[1]);
                if (answer == 0) {

                    log.info("Deleting udaj from DB.");
                    loadingDialog = new LoadingDialog("Vymazávanie údaju", "Čakajte prosím, údaj sa vymazáva z databázy...", 0, 100, MainFrame.this);
                    loadingDialog.getProgressBar().setIndeterminate(true);

                    int id = udaj.getId();
                    try {
                        DeleteRecordTask dra = new DeleteRecordTask(id, null, 0);
                        dra.execute();
                        log.info("Udaj deleted successfully.");
                    } catch (Exception ex) {
                        log.error("Exception while deleting udaj. Probably there exists other entity bind to this udaj", ex);
                        JOptionPane.showMessageDialog(MainFrame.this, "Záznam nemôže byť odstránený z databázy. \n Odkazuje naňho niektorý iný záznam \n a mohlo by prísť ku strate informácii.", "Problém pri odstraňovaní záznamu", JOptionPane.WARNING_MESSAGE);
                    }
                }
            }
        }

        private void setUdaj(Udaj udaj) {
            this.udaj = udaj;
        }
    }

    // trieda na filtrovanie poloziek podla zaciatocneho pismena, vyuzivana "pismenkovymi tlacitkami" z sprave tabuliek
    public class FilterByLetterAction extends AbstractAction {

        @Override
        public void actionPerformed(ActionEvent e) {

            // standardne sa filtruje podla 1. stlpca
            // ale pri LOS, casopisoch, dallaTorre a kvadrantoch sa filtruje podla 2.
            int filterColumn = 0;
            String tableName = (String) jListTables.getSelectedValue();
            if (tableName.equals("ListOfSpecies") || tableName.equals("Casopisy") || tableName.equals("DallaTorre") || tableName.equals("Kvadrant")) {
                filterColumn = 1;
            }

            JButton o = (JButton) e.getSource();
            String letter = o.getText();

            sorter.setRowFilter(null);
            sorter.setModel((PagingModel) jTableOverview.getModel());
            sorter.setRowFilter(RowFilter.regexFilter("^[" + letter + letter.toLowerCase() + "](?i)", filterColumn));
            PagingModel pm = (PagingModel) jTableOverview.getModel();
            jTableOverview.setRowSorter(sorter);

            pm.getData();
        }

    }

    /**
     * Opens tab for new herbarium record.
     */
    private class ShowNewHerbariumRecordTabAction extends AbstractAction {

        @Override
        public void actionPerformed(ActionEvent e) {
            SaveAction sa = (SaveAction) btnRecordSave.getAction();
            sa.setUdaj(null);
            sa.setUdajType('H');
            cbTypeUnit.setSelected(false);
            jPoznamkaNepub.setEnabled(false);
            jPoznamkaNepublik.setEnabled(false);
            jUdajDolozeny.setEnabled(false);
            fotograficky.setEnabled(false);
            enabledLitFields(false, false);

            lblUdajTyp.setText("H");
            setTabsAndFocus('H', true);
            setNewRecordTabName('H');

            jTabbedPaneMain.add(getRecordTab());
            jTabbedPaneMain.setSelectedComponent(getRecordTab());

            clearAll();

            //nastavenie policka vlastnych udajov a pocitadla na poziciu noveho udaja
            setRecViewCounterToNew();

            //inicializovanie sirky stlpcov tabulky literarnych zdrojov, revizii a obrazkov
            TableColumnsWidthCounter.setWidthOfColumns(jTableLitSources);
            TableColumnsWidthCounter.setWidthOfColumns(jTableRevisions);
            TableColumnsWidthCounter.setWidthOfColumns(jTableImages);
        }
    }

    /**
     * Opens tab for new literature record.
     */
    private class ShowNewLiteratureRecordTabAction extends AbstractAction {

        @Override
        public void actionPerformed(ActionEvent e) {
            SaveAction sa = (SaveAction) btnRecordSave.getAction();
            sa.setUdaj(null);
            sa.setUdajType('L');
            cbTypeUnit.setSelected(false);
            jPoznamkaNepub.setEnabled(false);
            jPoznamkaNepublik.setEnabled(false);
            jUdajDolozeny.setEnabled(false);
            fotograficky.setEnabled(false);
            enabledLitFields(false, false);

            lblUdajTyp.setText("L");
            setTabsAndFocus('L', true);
            setNewRecordTabName('L');

            jTabbedPaneMain.add(getRecordTab());
            jTabbedPaneMain.setSelectedComponent(getRecordTab());

            clearAll();

            //nastavenie policka vlastnych udajov a pocitadla na poziciu noveho udaja
            setRecViewCounterToNew();

            //inicializovanie sirky stlpcov tabulky literarnych zdrojov, revizii a obrazkov
            TableColumnsWidthCounter.setWidthOfColumns(jTableLitSources);
            TableColumnsWidthCounter.setWidthOfColumns(jTableRevisions);
            TableColumnsWidthCounter.setWidthOfColumns(jTableImages);
        }
    }

    /**
     * Opens tab for new field record.
     */
    private class ShowNewFieldRecordTabAction extends AbstractAction {

        @Override
        public void actionPerformed(ActionEvent e) {
            SaveAction sa = (SaveAction) btnRecordSave.getAction();
            sa.setUdaj(null);
            sa.setUdajType('T');
            jPoznamkaNepub.setEnabled(true);
            jPoznamkaNepublik.setEnabled(true);
            jUdajDolozeny.setEnabled(true);
            fotograficky.setEnabled(true);

            lblUdajTyp.setText("T");
            setTabsAndFocus('T', true);
            setNewRecordTabName('T');

            jTabbedPaneMain.add(getRecordTab());
            jTabbedPaneMain.setSelectedComponent(getRecordTab());

            clearAll();

            //nastavenie policka vlastnych udajov a pocitadla na poziciu noveho udaja
            setRecViewCounterToNew();

            //inicializovanie sirky stlpcov tabulky revizii a obrazkov
            TableColumnsWidthCounter.setWidthOfColumns(jTableRevisions);
            TableColumnsWidthCounter.setWidthOfColumns(jTableImages);
        }
    }

    /**
     * Opens Records overview tab (Prehlad udajov)
     */
    private class ShowRecordsOverviewTabAction extends AbstractAction {

        @Override
        public void actionPerformed(ActionEvent e) {
            MainFrame.this.recordsOverviewTab.setName("Prehľad údajov");
            MainFrame.this.jTabbedPaneMain.add(MainFrame.this.recordsOverviewTab);
            MainFrame.this.jTabbedPaneMain.setSelectedComponent(MainFrame.this.recordsOverviewTab);

            // data nacitavaj iba pri prvom otvoreni (= ked tabulka este nema nastaveny spravny tableModel)
            if (!(filterRecordsOverview.getRecordsOverviewTable().getModel() instanceof RecordsInsertedModel)) {
                filterRecordsOverview.loadFiltersAndData("overview");
            }
        }
    }

    /**
     * Displays export window.
     */
    private class ShowExportWindowAction extends AbstractAction {

        @Override
        public void actionPerformed(ActionEvent e) {
            exportDataForm.displayForm();
        }
    }

    /**
     * Opens a Tables overview tab (Prehlad tabuliek).
     */
    private class ShowTablesOverviewTabAction extends AbstractAction {

        @Override
        public void actionPerformed(ActionEvent e) {
            MainFrame.this.tablesOverviewTab.setName("Prehľad tabuliek");
            MainFrame.this.jTabbedPaneMain.add(MainFrame.this.tablesOverviewTab);
            MainFrame.this.jTabbedPaneMain.setSelectedComponent(MainFrame.this.tablesOverviewTab);
        }
    }

    /**
     * Loggs in a user. User must exist in the database as a role and in the table
     * uzivatelia.
     */
    private class DoLoginAction extends AbstractAction {

        @Override
        public void actionPerformed(ActionEvent e) {
            String loginF = tfLogin.getText();
            char[] passch = pfPassword.getPassword();
            String pass = String.valueOf(passch);
            if ((loginF != null && !loginF.isEmpty()) && (pass != null && !pass.isEmpty())) {

                try {
                    hq = HibernateQuery.getInstance(loginF, pass);

                    login = loginF;
                    isAdmin = hq.isAdmin();
                    loginDialog.setVisible(false);
                    log.info("User logged in: " + login);
                } catch (Exception ex) {
                    log.info("Wrong login/password: " + loginF + "/" + pass);
                    loadingDialog.setTitle("Nesprávne prihlasovacie údaje");
                    System.exit(0);
                }
            }
        }
    }

    /**
     * Closes login dialog.
     */
    private class CancelLoginAction extends AbstractAction {

        @Override
        public void actionPerformed(ActionEvent e) {
            System.exit(0);
        }
    }

    /**
     * Copies complete existing record and creates a new one from it.
     */
    private class CopyRecordAction extends AbstractAction {

        @Override
        public void actionPerformed(ActionEvent e) {
            int row = filterRecordsOverview.getRecordsOverviewTable().getSelectedRow();
            if (row > -1) {
                RecordsInsertedModel model = (RecordsInsertedModel) filterRecordsOverview.getRecordsOverviewTable().getModel();
                int id = (int) model.getValueAt(row, 0);
                log.info("Creating copy of the udaj " + id);
                Udaj udaj = (Udaj) hq.getById(Udaj.class, id);
                populateRecordTabs(udaj, true);
                tfZarBarcode.setText(null);

                SaveAction sa = (SaveAction) btnRecordSave.getAction();
                sa.setUdaj(null);
                sa.setUdajType(udaj.getTyp());

                lblUdajTyp.setText(udaj.getTyp().toString());

                setTabsAndFocus(udaj.getTyp(), true);
                setNewRecordTabName(udaj.getTyp());

                jTabbedPaneMain.add(getRecordTab());
                jTabbedPaneMain.setSelectedComponent(getRecordTab());
                btnRecordSave.setEnabled(true);
                btnRecordDelete.setEnabled(true);

                //nastav policko vlastnych udajov a pocitadlo na poziciu noveho udaja
                setRecViewCounterToNew();

            } else {
                log.warn("Copy action without selection of any udaj");
                JOptionPane.showMessageDialog(MainFrame.this, "Nebol zvolený žiadny údaj", "Nemožno vykonať operáciu", JOptionPane.WARNING_MESSAGE);
            }
        }
    }

    private class StatusChangeAction extends AbstractAction {

        @Override
        public void actionPerformed(ActionEvent e) {

            SaveAction sa = (SaveAction) btnRecordSave.getAction();

            if (rbIhned.isSelected()) {
                sa.setUdajVerejny(true);
            } else if (rbNeverejny.isSelected()) {
                sa.setUdajVerejny(false);
            } else {
                sa.setUdajVerejny(false);
                sa.setUdajVerejnyDatum(tfNeverRRRR.getText() + "" + tfNeverMM.getText() + "" + tfNeverDD.getText());
            }
        }
    }

    /*
     * Actions end
     */
    /**
     * Z hodnot v poliach na zalozke lokalita vytvori novy objekt lokalita,
     * alebo aktualizuje existujuci.
     *
     * @param lokalita ak null, vytvorime novy objekt, inak update
     * @return
     */
    private Lokality createOrModifyLokalita(Lokality lokalita) {
        Brumit4 bFour = (Brumit4) tfLocBrumFour.getEntity();
        Brumit3 bThree = bFour == null ? null : bFour.getBrumit3();
        Nac nac = (Nac) tfLocNac.getEntity();
        Vac vac = (Vac) tfLocVac.getEntity();
        Ngc ngc = (Ngc) tfLocNgc.getEntity();
        Vgc vgc = (Vgc) tfLocVgc.getEntity();
        Obec obec = (Obec) tfLocVill.getEntity();
        Flora fl = (Flora) tfLocFlora.getEntity();
        Chu chu = (Chu) tfLocProtArea.getEntity();
        String typss = (String) cbLocCoordType.getSelectedItem(); //typ suradnicovej sustavy
        double latDeg = tfLocLatDeg.getText().isEmpty() ? Double.NaN : Double.parseDouble(tfLocLatDeg.getText().trim());
        double latMin = tfLocLatMin.getText().isEmpty() ? Double.NaN : Double.parseDouble(tfLocLatMin.getText().trim());
        double latSec = tfLocLatSec.getText().isEmpty() ? Double.NaN : Double.parseDouble(tfLocLatSec.getText().trim());
        double lngDeg = tfLocLngDeg.getText().isEmpty() ? Double.NaN : Double.parseDouble(tfLocLngDeg.getText().trim());
        double lngMin = tfLocLngMin.getText().isEmpty() ? Double.NaN : Double.parseDouble(tfLocLngMin.getText().trim());
        double lngSec = tfLocLngSec.getText().isEmpty() ? Double.NaN : Double.parseDouble(tfLocLngSec.getText().trim());
        double lat = 0;
        double lng = 0;
        if (latDeg != Double.NaN && latMin != Double.NaN && latSec != Double.NaN) {
            lat = latDeg + latMin / 60 + latSec / 3600;
        }
        if (lngDeg != Double.NaN && lngMin != Double.NaN && lngSec != Double.NaN) {
            lng = lngDeg + lngMin / 60 + lngSec / 3600;
        }
        boolean altOdCca = chbLocAltCca.isSelected();
        Integer altOd = tfLocAltFrom.getText().isEmpty() ? null : Integer.parseInt(tfLocAltFrom.getText().trim());
        Integer altDo = tfLocAltTo.getText().isEmpty() ? null : Integer.parseInt(tfLocAltTo.getText().trim());
        String opis = HandyUtils.eToN(taLocDescr.getText().trim());
        String pozn = HandyUtils.eToN(taLocNotes.getText().trim());
        String scheda = HandyUtils.eToN(taLocScheda.getText().trim());
        String poda = HandyUtils.eToN(tfLocSoil.getText().trim());
        String subst = HandyUtils.eToN(tfLocSubstrate.getText().trim());
        String host = HandyUtils.eToN(tfLocHost.getText().trim());
        Integer vyska = tfLocAltEpif.getText().isEmpty() ? null : Integer.parseInt(tfLocAltEpif.getText().trim());

        if (bFour == null && nac == null && vac == null && ngc == null && vgc == null
                && obec == null && fl == null && chu == null && opis == null
                && pozn == null && scheda == null && poda == null && subst == null && host == null
                && Double.isNaN(lat) && Double.isNaN(lng) && altOd == null
                && altDo == null && vyska == null && jListQuadrants.getContainer().isEmpty() && jListFyto.getContainer().isEmpty()) {
            return null;
        }
        if (lokalita == null) {
            lokalita = new Lokality();
        }
        lokalita.setAltDo(altDo);
        lokalita.setAltOd(altOd);
        lokalita.setAltOdCca(altOdCca);
        lokalita.setChu(chu);
        lokalita.setFlora(fl);
        lokalita.setHostitel(host);
        lokalita.setBrumit3(bThree);
        lokalita.setBrumit4(bFour);
        lokalita.setKopiaSchedy(scheda);
        lokalita.setLatitude(Double.isNaN(lat) ? null : lat);
        lokalita.setLongitude(Double.isNaN(lng) ? null : lng);
        lokalita.setNac(nac);
        lokalita.setNgc(ngc);
        lokalita.setObec(obec);
        lokalita.setOpisLokality(opis);
        lokalita.setPoda(poda);
        lokalita.setPoznamkaLok(pozn);
        lokalita.setSubstrat(subst);
        lokalita.setTypss(typss);
        lokalita.setVac(vac);
        lokalita.setVgc(vgc);
        lokalita.setVyskaZberu(vyska);
        lokalita = ListAddRemoveUtils.setFtgokresy(lokalita, jListFyto.getContainer());
        lokalita = ListAddRemoveUtils.setQuadrants(lokalita, jListQuadrants.getContainer());

        return lokalita;
    }

    /**
     * Vytvori alebo modifikuje urcenie. Berie hodnoty z poli urcenia taxonu. Ak
     * uzivatel vyplnil polia na identifikaciu taxonu, mozeme vytvorit nove
     * urcenie, alebo modifikovat existujuce. Ak su polia na identifikaciu
     * taxonu prazdne, vraciame null, cim indikujeme, ze nevytvorime nove
     * urcenie. Ak chceme zmazat existujuce, tomu priradime null pre menataxonov
     * a udaj a vratime ho.
     *
     * @param urc ak je null, vytvorime nove urcenie, inak modifikujeme tento
     * objekt
     * @return nove urcenie, ak je parameter null. Modifikovane urcenie, ak
     * parameter nie je null. Null, ak nie su vyplanene poliab na identifikaciu.
     */
    private SkupRev createOrModifyUrcenie(SkupRev urc) {
        ListOfSpecies stdMeno = (ListOfSpecies) tfUrcStdMeno.getEntity();
        String menoScheda = HandyUtils.eToN(tfUrcNazovScheda.getText().trim());
        String poznamka = HandyUtils.eToN(tfUrcPoznamka.getText().trim());
        String datum = HandyUtils.eToN(HandyUtils.createDate(tfDatumUrcD.getText(), tfDatumUrcM.getText(), tfDatumUrcR.getText().trim()));
        String datumSlovom = HandyUtils.eToN(tfDatumUrcSlovom.getText().trim());
        List<MenaZberRev> idents = jListIdentificators.getContainer();

        if (stdMeno == null && (menoScheda == null || menoScheda.isEmpty())) {
            if (urc != null) {
                urc.setMenaTaxonov(null);
                urc.setUdaj(null);
                return urc;
            }
            log.warn("stdMeno and menoScheda are null");
            return null;
        }

        if (urc == null) {
            urc = new SkupRev();
        }

        MenaTaxonov menaTax = urc.getMenaTaxonov();
        if (menaTax == null) {
            menaTax = new MenaTaxonov();
        }
        menaTax.setSpecies(stdMeno);
        menaTax.setMenoScheda(menoScheda);
        menaTax.setPoznamka(poznamka);

        urc = ListAddRemoveUtils.setRevisorsAndIdentificators(urc, idents);
        urc.setMenaTaxonov(menaTax);
        urc.setDatum(datum);
        urc.setFRevizia(false);
        urc.setDatumSlovom(datumSlovom);

        return urc;

    }

    /**
     * Z hodnot poli v zalozke zaradenie polozky vytvori novy objekt
     * HerbarPolozky, alebo ho aktualizuje.
     *
     * @param herbPol ak null, novy objekt, inak update
     * @return
     * @throws EntityException
     */
    public HerbarPolozky createOrModifyHerbPolozka(HerbarPolozky herbPol) throws EntityException {
        if (herbPol == null) {
            herbPol = new HerbarPolozky();
        }
        Herbar herbar = (Herbar) tfZarHerbar.getEntity();
        DallaTorre dallT = (DallaTorre) tfZarDallaTorre.getEntity();
        Voucher vouch = (Voucher) tfZarVoucher.getEntity();
        Exsikaty exs = (Exsikaty) tfZarExsikat.getEntity();
        String cisloPol = HandyUtils.eToN(tfZarCisloPol.getText().trim());
        String cisloZb = HandyUtils.eToN(tfZarCisloZberu.getText().trim());
        String kvant = HandyUtils.eToN(tfZarKvantif.getText().trim());
        String pozn = HandyUtils.eToN(taZarHerbPoznamka.getText().trim());
        boolean typ = cbTypeUnit.isSelected();

        //TODO pred buildom pre smatanovu odkomentovat potrebu ciaroveho kodu
        //if (tfBarcode.getText().isEmpty()) {
        //    throw new EntityException("Číslo čiarového kódu je prázdne!");
        //} else {
        String cisloCkFull = tfZarBarcode.getText().trim();
        Integer cisloCk = null;
        if (!cisloCkFull.isEmpty()) {
            cisloCk = Integer.parseInt(cisloCkFull.replaceAll("[^0-9]", "")); //zo SAV00001241 urobi 1241 (nevhodne pre kody ako 1245-SAV-1000 => 12451000)
        }

        if (cisloCkFull.isEmpty() && herbar == null && dallT == null && vouch == null
                && exs == null && cisloPol == null && cisloZb == null && kvant == null && pozn == null) {
            return null;
        }

        herbPol.setHerbar(herbar);
        herbPol.setDallaTorre(dallT);
        herbPol.setVoucher(vouch);
        herbPol.setExsikaty(exs);
        herbPol.setCisloPol(cisloPol);
        herbPol.setCisloCk(cisloCk);
        herbPol.setCisloCkFull(cisloCkFull);
        herbPol.setCisloZberu(cisloZb);
        herbPol.setKvantifikacia(kvant);
        herbPol.setPoznamka(pozn);
        herbPol.setTyp(typ);
        //}
        return herbPol;
    }

    public Set<UdajObrazky> createUdajObrazky() {
        return null;
    }

    public Set<PeopleAsoc> createOrModifyZberatelia(Udaj udaj) {
        List<MenaZberRev> collectors = jListCollectors.getContainer();
        Set<PeopleAsoc> collsAsoc = ListAddRemoveUtils.setCollectors(udaj, collectors);
        return collsAsoc;
    }

    public List<LitZdrojRev> createOrModifyLiterature() {
        LiteraturesTableModel litModel = (LiteraturesTableModel) MainFrame.this.jTableLitSources.getModel();
        List<LitZdrojRev> lits = new ArrayList<>(litModel.getLiteratures());
        return lits;
    }

    private List<SkupRev> createOrModifyRevisions() {
        RevisionsTableModel revModel = (RevisionsTableModel) MainFrame.this.jTableRevisions.getModel();
        List<SkupRev> revs = new ArrayList<>(revModel.getRevisions());
        return revs;
    }

    private List<UdajObrazky> createOrModifyImages() {
        ImagesTableModel obrModel = (ImagesTableModel) MainFrame.this.jTableImages.getModel();
        List<UdajObrazky> obr = new ArrayList<>(obrModel.getImages());
        return obr;
    }

    public LitZdrojRev createOrModifyLitZdrojRev(LitZdrojRev lzdrojRev) {
        String book = HandyUtils.eToN(getTfLitBook().getText().trim());
        String publisher = HandyUtils.eToN(tfLitPublisher.getText().trim());
        String chapter = HandyUtils.eToN(tfLitChapter.getText().trim());

        Casopisy journal = (Casopisy) tfLitJournalTitle.getEntity();
        String year = HandyUtils.eToN(tfLitYear.getText().trim());
        String issue = HandyUtils.eToN(tfLitIssue.getText().trim());
        String pages = HandyUtils.eToN(tfLitPages.getText().trim());

        String publTitle = HandyUtils.eToN(tfLitPublTitle.getText().trim());
        String publTitleTransl = HandyUtils.eToN(tfLitPublTitleTransl.getText().trim());
        String source = HandyUtils.eToN(tfLitSource.getText().trim());
        String pyear = tfLitPublYear.getText().isEmpty() ? null : tfLitPublYear.getText().trim();

        String notes = HandyUtils.eToN(tfLitNotes.getText().trim());
        boolean photo = cbLitPhoto.isSelected();
        boolean map = cbLitMap.isSelected();
        boolean komplet = cbLitPhoto.isSelected() && cbLitMap.isSelected();

        List<MenaZberRev> authors = jListLitAuthors.getContainer();
        List<MenaZberRev> editors = jListLitEditors.getContainer();

        if (publTitle == null && publTitleTransl == null && source == null && year == null
                && journal == null && pyear == null && issue == null && pages == null
                && book == null && publisher == null && chapter == null && notes == null
                && !komplet && !photo && !map && authors.isEmpty() && editors.isEmpty()) {
            return null;
        }

        LitZdroj lzdroj = null;
        if (lzdrojRev == null) { // ak pridavame lit. zdroj k udaju
            lzdrojRev = new LitZdrojRev();
            if (getSelectedLzdroj() != null) { // pridavame uz existujuci lit. zdroj            
                lzdroj = getSelectedLzdroj();
                lzdrojRev.setLitZdroj(lzdroj);
                return lzdrojRev;
            } else { // pridavame novy lit. zdroj
                lzdroj = new LitZdroj();

                lzdroj.setCasopis(journal);
                lzdroj.setCislo(issue);
                lzdroj.setFotka(photo);
                lzdroj.setKomplet(komplet);
                lzdroj.setMapaRozsirenia(map);
                lzdroj.setNazovClanku(publTitle);
                lzdroj.setNazovClankuPreklad(publTitleTransl);
                lzdroj.setNazovKapitoly(chapter);
                lzdroj.setNazovKnihy(book);
                lzdroj.setPoznamka(notes);
                lzdroj.setPramen(source);
                lzdroj.setRocnik(year);
                lzdroj.setRok(pyear);
                lzdroj.setStrany(pages);
                lzdroj.setVydavatel(publisher);

                lzdroj.setKod("");
                lzdroj.setReferencia(null);
                lzdroj.setTyp(null);

                lzdroj = ListAddRemoveUtils.setLitAuthors(lzdroj, authors);
                lzdroj = ListAddRemoveUtils.setLitEditors(lzdroj, editors);
            }
        } else {
            // ak menime povodny lit. zdroj - momentalne nevyuzivame
        }

        lzdrojRev.setLitZdroj(lzdroj);
        return lzdrojRev;
    }

    //vymaz vsetky polozky, 
    private void clearAll() {
        clearFields();
        clearTextfieldsMoznosti(true);
//        clearSourceLists();
        clearLitFields();
        clearRevFields();

        jListIdentificators.clear();
        jListCollectors.clear();
        jListRevisors.clear();
        jListQuadrants.clear();
        jListFyto.clear();
        listMoznosti.clear();
        jTableRevisions.setModel(new RevisionsTableModel());
        jTableLitSources.setModel(new LiteraturesTableModel());
        jTableImages.setModel(new ImagesTableModel());
    }

    //zmaz vsetky textove polia a checkboxy
    private void clearFields() {
        tfUrcNazovScheda.setText(null);
        tfUrcAccName.setText(null);
        tfUrcPoznamka.setText(null);
        taLocDescr.setText(null);
        tfLocAltFrom.setText(null);
        tfLocAltTo.setText(null);
        tfLocAltEpif.setText(null);
        chbLocAltCca.setSelected(false);
        tfLocLatDeg.setText(null);
        tfLocLatMin.setText(null);
        tfLocLatSec.setText(null);
        tfLocLngDeg.setText(null);
        tfLocLngMin.setText(null);
        tfLocLngSec.setText(null);
        tfLocSoil.setText(null);
        tfLocSubstrate.setText(null);
        tfLocHost.setText(null);
        taLocScheda.setText(null);
        taLocNotes.setText(null);
        tfZarCisloPol.setText(null);
        tfZarCisloZberu.setText(null);
        tfZarBarcode.setText(null);
        tfZarKvantif.setText(null);
        taZarHerbPoznamka.setText(null);
        tfDatumRevD.setText(null);
        tfDatumRevM.setText(null);
        tfDatumRevR.setText(null);
        tfDatumRevSlovom.setText(null);
        tfDatumUrcD.setText(null);
        tfDatumUrcM.setText(null);
        tfDatumUrcR.setText(null);
        tfDatumUrcSlovom.setText(null);
        tfDatumZbD.setText(null);
        tfDatumZbM.setText(null);
        tfDatumZbR.setText(null);
        tfDatumZbSlovom.setText(null);
    }

    //zmaz vsetky polia s moznostami
    private void clearTextfieldsMoznosti(boolean full) {
        if (full) {
            tfUrcStdMeno.clear();
            tfLocBrumFour.clear();
            tfLocFlora.clear();
            tfLocVac.clear();
            tfLocVill.clear();
            tfLocNac.clear();
            tfLocVgc.clear();
            tfLocNgc.clear();
            tfRevStdMeno.clear();
            tfZarHerbar.clear();
            tfZarDallaTorre.clear();
            tfZarExsikat.clear();
            tfZarVoucher.clear();
        } else {
//            tfUrcStdMeno.clearMoznosti();
//            tfLocBrumFour.clearMoznosti();
//            tfLocFlora.clearMoznosti();
//            tfLocVac.clearMoznosti();
//            tfLocNac.clearMoznosti();
//            tfLocVgc.clearMoznosti();
//            tfLocNgc.clearMoznosti();
//            tfRevStdMeno.clearMoznosti();
//            tfZarHerbar.clearMoznosti();
//            tfZarDallaTorre.clearMoznosti();
//            tfZarExsikat.clearMoznosti();
//            tfZarVoucher.clearMoznosti();
        }
    }

    //zmaz vsetky zoznamy s moznostou pridavania a odoberania
//    private void clearSourceLists() {
//        jListIdentificators.setSourceList(null);
//        jListCollectors.setSourceList(null);
//        jListRevisors.setSourceList(null);
//        jListQuadrants.setSourceList(null);
//        jListFyto.setSourceList(null);
//    }
    //zmaz vsetky udaje na zalozke o revizii
    private void clearRevFields() {
        tfRevNazovListok.setText(null);
        tfRevAccName.setText(null);
        tfRevPoznamka.setText(null);
        tfRevStdMeno.setEntity(null);
        tfDatumRevD.setText(null);
        tfDatumRevM.setText(null);
        tfDatumRevR.setText(null);
        tfDatumRevSlovom.setText(null);
        jListRevisors.clear();
    }

    //zmaz vsetky udaje na zalozke o obrazkoch
    private void clearImageFields() {
        tfFindLocalImageName.setText(null);
        taImageDescr.setText(null);
        tfImageUrl.setText(null);
        lblImageDisplay.setIcon(null);
        activeImage = null;
    }

    //zmaz vsatky udaje na zalozke o lit. zdroji
    private void clearLitFields() {
        this.getTfLitBook().setText(null);
        this.tfLitChapter.setText(null);
        this.tfLitIssue.setText(null);
        this.tfLitJournalTitle.setEntity(null);
        this.tfLitNotes.setText(null);
        this.tfLitPages.setText(null);
        this.tfLitPublTitle.setText(null);
        this.tfLitPublTitleTransl.setText(null);
        this.tfLitPublYear.setText(null);
        this.tfLitPublisher.setText(null);
        this.tfLitRecPage.setText(null);
        this.tfLitSource.setText(null);
        this.tfLitYear.setText(null);
        this.cbLitMap.setSelected(false);
        this.cbLitPhoto.setSelected(false);
        jListLitAuthors.clear();
        jListLitEditors.clear();
        jListLitKeywords.clear();
    }

    /**
     * Zmaze udaj s danym id. Treba manualne zmazat herbarovu polozku, obrazky,
     * mena taxonov a lokalitu.
     *
     * @param id Identifikator udaja na zmazanie
     * @return true, ak sa podarilo zmazat vsetko, inak false
     */
    private boolean deleteRecord(int id) {
        try {
            AssociableEntity ent = (AssociableEntity) hq.getById(Udaj.class, id);
            if (ent instanceof Udaj) {
                log.info("Deleting udaj and its dependencies.");
                Udaj udaj = (Udaj) ent;
                // musime si uchovat herbarovu polozku, obrazky, mena taxonov a lokalitu, pretoze najprv musime zmazat udaj, az potom odstranime siroty
                HerbarPolozky hp = udaj.getHerbarPolozky();
                Lokality lok = udaj.getLokality();
                List<UdajObrazky> imgs = udaj.getObrazky();

                List<MenaTaxonov> mts = new ArrayList<>();
                for (Object object : udaj.getSkupRevs()) {
                    if (object instanceof SkupRev) {
                        SkupRev sr = (SkupRev) object;
                        mts.add(sr.getMenaTaxonov());
                    }
                }

                hq.deleteEntity(ent); //zmazanie udaja
                log.info("Udaj deleted.");
                hq.deleteEntity(hp);
                log.info("Herbar polozky deleted.");
                hq.deleteEntity(lok);
                log.info("Lokalita deleted.");
                for (UdajObrazky image : imgs) {
                    hq.deleteEntity(image);
                }
                log.info("Images deleted.");
                for (MenaTaxonov menaTaxonov : mts) {
                    hq.deleteEntity(menaTaxonov);
                }
                log.info("Mena taxonov deleted.");
            }
        } catch (Exception ex) {
            log.error("Exception while deleting udaj and its dependencies", ex);
            JOptionPane.showMessageDialog(MainFrame.this, "Nastala chyba pri odstraňovaní údaja z databázy. \n Skúste akciu zopakovať znova", "Chyba pri odstraňovaní", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return true;
    }

    private void enabledRevFields(boolean enabled) {
        tfRevNazovListok.setEnabled(enabled);
        tfRevStdMeno.setEnabled(enabled);
        tfRevPoznamka.setEnabled(enabled);
        tfDatumRevD.setEnabled(enabled);
        tfDatumRevM.setEnabled(enabled);
        tfDatumRevR.setEnabled(enabled);
        tfDatumRevSlovom.setEnabled(enabled);
        jListRevisors.setEnabled(enabled);

        jButtonAddRev.setEnabled(enabled);
        jButtonCancelRev.setEnabled(enabled);

        btnAddRevDown.setEnabled(!enabled);
        btnAddRevTop.setEnabled(!enabled);
    }

    private void enabledImageFields(boolean enabled) {
        tfFindLocalImageName.setEnabled(enabled);
        taImageDescr.setEnabled(enabled);
        btnFindLocalImage.setEnabled(enabled);

        jButtonAddImage.setEnabled(enabled);
        jButtonCancelImage.setEnabled(enabled);

        btnAddImageDown.setEnabled(!enabled);
        btnAddImageTop.setEnabled(!enabled);
    }

    private void enabledLitFields(boolean enabled, boolean newLit) {

        this.tfLitNotes.setEnabled(enabled);
        this.tfLitPublYear.setEnabled(enabled);
        this.tfLitPublTitle.setEnabled(enabled);
        this.tfLitPublTitleTransl.setEnabled(enabled);
        this.tfLitRecPage.setEnabled(enabled);
        this.tfLitSource.setEnabled(enabled);
        this.cbLitMap.setEnabled(enabled);
        this.cbLitPhoto.setEnabled(enabled);
        this.jListLitAuthors.setEnabled(enabled);
        this.jListLitEditors.setEnabled(enabled);
        this.jListLitKeywords.setEnabled(enabled);

        enabledJournalTitle(enabled);
        enabledBookTitle(enabled);

        //umozni editovat udaje v literarnom zdroji
        btnSaveLitSourceToTable.setEnabled(newLit);
        btnCancelAdding.setEnabled(newLit);

        //docasne znepristupni tlacitko na pridavanie dalsieho zdroja
        btnAddLitSourceDown.setEnabled(!newLit);
        btnAddLitSourceTop.setEnabled(!newLit);
    }

    private void enabledJournalTitle(boolean enabled) {
        this.tfLitJournalTitle.setEnabled(enabled);
        if (enabled) {
            this.tfLitJournalTitle.getTextField().setBorder(borderGreenWithPadding);
        } else {
            this.tfLitJournalTitle.getTextField().setBorder(defaultBorder);
            enabledJournalData(false);
        }

    }

    private void enabledBookTitle(boolean enabled) {
        this.getTfLitBook().setEnabled(enabled);
        if (enabled) {
            this.getTfLitBook().setBorder(borderBlueWithPadding);
        } else {
            this.getTfLitBook().setBorder(defaultBorder);
            enabledBookData(false);
        }

    }

    private void enabledJournalData(boolean enabled) {
        this.tfLitYear.setEnabled(enabled);
        this.tfLitIssue.setEnabled(enabled);
        this.tfLitPages.setEnabled(enabled);
        if (enabled) {
            this.tfLitYear.setBorder(borderGreenWithPadding);
            this.tfLitIssue.setBorder(borderGreenWithPadding);
            this.tfLitPages.setBorder(borderGreenWithPadding);
        } else {
            this.tfLitYear.setBorder(defaultBorder);
            this.tfLitIssue.setBorder(defaultBorder);
            this.tfLitPages.setBorder(defaultBorder);
        }
    }

    private void enabledBookData(boolean enabled) {
        this.tfLitPublisher.setEnabled(enabled);
        this.tfLitChapter.setEnabled(enabled);
        this.tfLitYear.setEnabled(enabled);
        if (enabled) {
            this.tfLitPublisher.setBorder(borderBlueWithPadding);
            this.tfLitChapter.setBorder(borderBlueWithPadding);
        } else {
            this.tfLitPublisher.setBorder(defaultBorder);
            this.tfLitChapter.setBorder(defaultBorder);
        }
    }

    private void populateRecordTabs(Udaj udaj, boolean isCopied) {
        if (udaj == null) {
            log.error("populateRecordTabs: udaj supplied is NULL");
            throw new NullPointerException("udaj");
        }
        // vzdy vychadzame z prazdnych policok
        clearAll();

        populateIdentAndRev(udaj, isCopied);
        populateCollectors(udaj);
        populateLitZdrojAndLitRevs(udaj);
        populateStatusUdaja(udaj);

        HerbarPolozky herbPol = udaj.getHerbarPolozky();
        if (herbPol != null) {
            populateHerbarPolozky(herbPol);
        } else {
            clearTabFields("herb");
        }

        Lokality lok = udaj.getLokality();
        if (lok != null) {
            populateLokality(lok, udaj);
        } else {
            clearTabFields("loc");
        }

        List<UdajObrazky> obr = udaj.getObrazky();
        if (!obr.isEmpty()) {
            populateImages(obr, isCopied);
        } else {
            clearTabFields("images");
        }

        // prednastavime pre uvodny nahlad
        this.listMoznosti.clear();
    }

    private void populateIdentAndRev(Udaj udaj, boolean isCopied) {
        if (udaj == null) {
            log.error("populateIdentAndRev: udaj supplied is NULL");
            throw new NullPointerException("udaj");
        }
        // nacitame udaje o urceni
        SkupRev urcenie = udaj.getUrcenie();
        if (urcenie != null) {
            populateIdentification(udaj.getUrcenie());
        }

        // nacitame udaje o reviziach
        List<SkupRev> revs = udaj.getRevisions();
        if (!revs.isEmpty()) {
            if (isCopied) {
                populateRevision(replicateRevisions(revs));
            } else {
                populateRevision(revs);
            }
        }
    }

    private void populateIdentification(SkupRev identif) {
        if (identif == null) {
            log.error("populateIdentification: skupRev supplied is NULL");
            throw new NullPointerException("skupRev");
        }
        if (identif.isFRevizia()) {
            log.error("populateIdentification: skupRev is frevizia");
            throw new IllegalArgumentException("skupRev");
        }

        MenaTaxonov menaTax = identif.getMenaTaxonov();
        if (menaTax != null) {
            tfUrcNazovScheda.setText(menaTax.getMenoScheda());
            if (menaTax.getSpecies() != null) {
                tfUrcStdMeno.setEntity(menaTax.getSpecies());
                tfUrcAccName.setText(menaTax.getSpecies().getAcceptedName() == null ? tfUrcStdMeno.getTextField().getText() : identif.getMenaTaxonov().getSpecies().getAcceptedName().getMeno());
            }
            tfUrcPoznamka.setText(menaTax.getPoznamka());
        }
        if (identif.getDatum() != null) {
            tfDatumUrcD.setText(identif.getDatum().substring(6));
            tfDatumUrcM.setText(identif.getDatum().substring(4, 6));
            tfDatumUrcR.setText(identif.getDatum().substring(0, 4));
        }
        tfDatumUrcSlovom.setText(identif.getDatumSlovom());
        Set srDets = identif.getSkupRevDets();
        List<MenaZberRev> iders = new ArrayList<>();
        for (Object object : srDets) {
            SkupRevDet srd = (SkupRevDet) object;
            iders.add(srd.getMenaZberRev());
        }

        jListIdentificators.setContainer(iders);
    }

    private void populateRevision(List<SkupRev> revisions) {
        if (revisions == null) {
            log.error("populateRevision: revisions supplied are NULL");
            throw new NullPointerException("revision");
        }
        jTableRevisions.setModel(new RevisionsTableModel(revisions));
        if (!revisions.isEmpty()) {
            // vyobraz prvu z revizii
            populateRevisionFields(revisions.get(0));
            enabledRevFields(false);
        }

    }

    private void populateLokality(Lokality lokalita, Udaj udaj) {
        if (lokalita == null) {
            log.error("populateLokality: lokalita supplied is NULL");
            throw new NullPointerException("lokalita");
        }
        tfLocBrumFour.setEntity(lokalita.getBrumit4());
        tfLocFlora.setEntity(lokalita.getFlora());
        tfLocVac.setEntity(lokalita.getVac());
        tfLocNac.setEntity(lokalita.getNac());
        tfLocVill.setEntity(lokalita.getObec());
        tfLocVgc.setEntity(lokalita.getVgc());
        tfLocNgc.setEntity(lokalita.getNgc());
        tfLocProtArea.setEntity(lokalita.getChu());
        taLocDescr.setText(lokalita.getOpisLokality());
        tfLocAltFrom.setText(lokalita.getAltOd() == null ? null : lokalita.getAltOd().toString());
        tfLocAltTo.setText(lokalita.getAltDo() == null ? null : lokalita.getAltDo().toString());
        chbLocAltCca.setSelected(lokalita.isAltOdCca());
        cbLocCoordType.setSelectedItem(lokalita.getTypss());
        tfLocSoil.setText(lokalita.getPoda());
        tfLocSubstrate.setText(lokalita.getSubstrat());
        tfLocHost.setText(lokalita.getHostitel());
        tfLocAltEpif.setText(lokalita.getVyskaZberu() == null ? null : lokalita.getVyskaZberu().toString());
        taLocScheda.setText(lokalita.getKopiaSchedy());
        taLocNotes.setText(lokalita.getPoznamkaLok());
        
        final String datumZberu = udaj.getDatumZberu();
        if (datumZberu != null && datumZberu.length() == 8) {
            tfDatumZbD.setText(datumZberu.substring(6));
            tfDatumZbM.setText(datumZberu.substring(4, 6));
            tfDatumZbR.setText(datumZberu.substring(0, 4));
        }

        if (lokalita.getLatitude() != null) {
            double lat = lokalita.getLatitude();
            splitDegrees(lat, true);
            if (Math.signum(lat) >= 0) {
                rbLocNorth.setSelected(true);
            } else {
                rbLocSouth.setSelected(true);
            }
        }
        if (lokalita.getLongitude() != null) {
            double lng = lokalita.getLongitude();
            splitDegrees(lng, false);
            if (Math.signum(lng) >= 0) {
                rbLocEast.setSelected(true);
            } else {
                rbLocWest.setSelected(true);
            }
        }

        List<Kvadrant> quads = new ArrayList<>();
        for (Object object : lokalita.getLokalityKvadrantAsocs()) {
            LokalityKvadrantAsoc lka = (LokalityKvadrantAsoc) object;
            quads.add(lka.getKvadrant());
        }
        jListQuadrants.setContainer(quads);

        List<Ftgokres> fyto = new ArrayList<>();
        for (Object object : lokalita.getLokalityFtgokresAsocs()) {
            LokalityFtgokresAsoc lfa = (LokalityFtgokresAsoc) object;
            fyto.add(lfa.getFtgokres());
        }
        jListFyto.setContainer(fyto);

        List<MenaZberRev> collectrs = new ArrayList<>();
        for (Object object : udaj.getUdajZberAsocs()) {
            UdajZberAsoc srd = (UdajZberAsoc) object;
            collectrs.add(srd.getMenaZberRev());
        }
        
        jListCollectors.setContainer(collectrs);
    }

    public void populateLitZdrojFields(LitZdroj lzdroj) {
        if (lzdroj != null) {
            this.tfLitPublTitle.setText(lzdroj.getNazovClanku() == null ? "" : lzdroj.getNazovClanku());
            this.tfLitPublTitleTransl.setText(lzdroj.getNazovClankuPreklad() == null ? "" : lzdroj.getNazovClankuPreklad());
            this.tfLitSource.setText(lzdroj.getPramen() == null ? "" : lzdroj.getNazovClanku());
            this.tfLitYear.setText(lzdroj.getRocnik() == null ? "" : lzdroj.getRocnik());
            this.tfLitPublYear.setText(lzdroj.getRok() == null ? "" : lzdroj.getRok().toString());
            this.tfLitIssue.setText(lzdroj.getCislo() == null ? "" : lzdroj.getCislo());
            this.tfLitPages.setText(lzdroj.getStrany() == null ? "" : lzdroj.getStrany());
            this.getTfLitBook().setText(lzdroj.getNazovKnihy() == null ? "" : lzdroj.getNazovKnihy());
            this.tfLitPublisher.setText(lzdroj.getVydavatel() == null ? "" : lzdroj.getVydavatel());
            this.tfLitJournalTitle.getTextField().setText(lzdroj.getCasopis() == null ? "" : lzdroj.getCasopis().getMeno());
            this.tfLitChapter.setText(lzdroj.getNazovKapitoly() == null ? "" : lzdroj.getNazovKapitoly());
            this.tfLitNotes.setText(lzdroj.getPoznamka() == null ? "" : lzdroj.getPoznamka());
            this.cbLitPhoto.setSelected(lzdroj.isFotka());
            this.cbLitMap.setSelected(lzdroj.isMapaRozsirenia());

            //naplnit autorov
            List<MenaZberRev> authors = new ArrayList<>(5);
            for (Object object : lzdroj.getLzdrojAutoriAsocs()) {
                LzdrojAutoriAsoc lza = (LzdrojAutoriAsoc) object;
                authors.add(lza.getMenoAutora());
            }
            this.jListLitAuthors.setContainer(authors);

            //naplnit editorov
            List<MenaZberRev> editors = new ArrayList<>(3);
            for (Object object : lzdroj.getLzdrojEditoriAsocs()) {
                LzdrojEditoriAsoc lze = (LzdrojEditoriAsoc) object;
                editors.add(lze.getMenoEditora());
            }
            this.jListLitEditors.setContainer(editors);

            //SaveAction sa = (SaveAction) jButtonSave.getAction();
            //sa.setLzdroj(lzdroj);
            // tfLitRecPage only for primary lit zdroj
            enabledLitFields(true, false);

        } else {
            clearLitFields();
        }
    }

    public void populateRevisionFields(SkupRev rev) {
        if (rev != null) {
            tfDatumRevD.setText(rev.getDatum() == null ? "" : rev.getDatum().substring(6));
            tfDatumRevM.setText(rev.getDatum() == null ? "" : rev.getDatum().substring(4, 6));
            tfDatumRevR.setText(rev.getDatum() == null ? "" : rev.getDatum().substring(0, 4));
            tfDatumRevSlovom.setText(rev.getDatumSlovom());

            if (rev.getMenaTaxonov() != null) {
                tfRevStdMeno.setEntity(rev.getMenaTaxonov().getSpecies());
                tfRevNazovListok.setText(rev.getMenaTaxonov().getMenoScheda());
                tfRevPoznamka.setText(rev.getMenaTaxonov().getPoznamka());
            } else {
                tfRevStdMeno.setEntity(null);
                tfRevNazovListok.setText("");
                tfRevPoznamka.setText("");
            }

            List<MenaZberRev> revisors = new ArrayList<>();
            for (Object object : rev.getSkupRevDets()) {
                if (object instanceof SkupRevDet) {
                    SkupRevDet revisor = (SkupRevDet) object;
                    revisors.add(revisor.getMenaZberRev());
                }
            }
            jListRevisors.setContainer(revisors);
        }
        enabledRevFields(true);
    }

    private void populateImageFields(UdajObrazky image) {
        if (image == null) {
            log.error("populateUdajObrazky: image supplied is NULL");
            throw new NullPointerException("image");
        }

        taImageDescr.setText(image.getPopis());
        tfImageUrl.setText(image.getUrl());
        if (image.getObrazok() != null) {
            ByteArrayInputStream bais = new ByteArrayInputStream(image.getObrazok());
            try {
                BufferedImage originalImg = ImageIO.read(bais);
                BufferedImage scaledImg = scaleImgToLbl(originalImg);
                lblImageDisplay.setIcon(new ImageIcon(scaledImg));
                activeImage = originalImg;
            } catch (IOException ex) {
                log.error("Exception while reading udaj image", ex);
                JOptionPane.showMessageDialog(MainFrame.this, "Ľutujeme, nastala chyba pri načítaní obrázku údaja.", "Chyba pri načítaní", JOptionPane.WARNING_MESSAGE);
            }
        } else if (image.getUrl() != null && !image.getUrl().equals("")) {

            URL url;
            URLConnection conn;
            InputStream is;
            try {
                url = new URL(image.getUrl());
                conn = url.openConnection();
                is = conn.getInputStream();
                BufferedImage originalImg = ImageIO.read(is);
                BufferedImage scaledImg = scaleImgToLbl(originalImg);
                lblImageDisplay.setIcon(new ImageIcon(scaledImg));
                activeImage = originalImg;
            } catch (MalformedURLException ex) {
                log.error("Wrong format of URL: " + image.getUrl(), ex);
                JOptionPane.showMessageDialog(MainFrame.this, "Ľutujeme, nastala chyba pri načítaní obrázku údaja", "Chybná URL adresa", JOptionPane.WARNING_MESSAGE);
            } catch (IOException ex) {
                log.error("Exception while reading udaj from specified URL address: " + image.getUrl(), ex);
                JOptionPane.showMessageDialog(MainFrame.this, "Ľutujeme, nastala chyba pri načítaní obrázku údaja", "Chybná pri načítaní", JOptionPane.WARNING_MESSAGE);
            }
        }
    }

    private void populateHerbarPolozky(HerbarPolozky herbPol) {
        if (herbPol == null) {
            log.error("populateHerbarPolozky: herbarPolozka supplied is NULL");
            throw new NullPointerException("herbPol");
        }
        tfZarHerbar.setEntity(herbPol.getHerbar());
        tfZarDallaTorre.setEntity(herbPol.getDallaTorre());
        tfZarExsikat.setEntity(herbPol.getExsikaty());
        tfZarVoucher.setEntity(herbPol.getVoucher());
        tfZarCisloPol.setText(herbPol.getCisloPol());
        tfZarBarcode.setText(herbPol.getCisloCkFull());
        tfZarCisloZberu.setText(herbPol.getCisloZberu());
        tfZarKvantif.setText(herbPol.getKvantifikacia());
        taZarHerbPoznamka.setText(herbPol.getPoznamka());
        cbTypeUnit.setSelected(herbPol.isTyp());
    }

    private void populateCollectors(Udaj udaj) {
        if (udaj == null) {
            log.error("populateCollectors: udaj supplied is NULL");
            throw new NullPointerException("udaj");
        }
        List<MenaZberRev> colls = new ArrayList<>(1);
        for (Object object : udaj.getUdajZberAsocs()) {
            UdajZberAsoc uza = (UdajZberAsoc) object;
            colls.add(uza.getMenaZberRev());
        }

        this.jListCollectors.setContainer(colls);

        if (udaj.getDatumZberu() != null) {
            this.tfDatumZbD.setText(udaj.getDatumZberu().substring(6));
            this.tfDatumZbM.setText(udaj.getDatumZberu().substring(4, 6));
            this.tfDatumZbR.setText(udaj.getDatumZberu().substring(0, 4));
        }
        this.tfDatumZbSlovom.setText(udaj.getDatumZberuSlovom());
    }

    // tu sa deje magia nacitania vsetkych lit zdrojov
    // primarnym literarnym zdrojom sa berie udaj.getLitZdroj(), ostatne su brane ako "revizie"
    private void populateLitZdrojAndLitRevs(Udaj udaj) {

        if (udaj.getLitZdroj() != null) {

            List<LitZdrojRev> literatures = new ArrayList<>();

            literatures.addAll(udaj.getLitZdrojRevs());
            populateLitRevs(udaj.getLitZdroj(), literatures);
            TableColumnsWidthCounter.setWidthOfColumns(jTableLitSources);

        } else {
            jTableLitSources.setModel(new LiteraturesTableModel());
        }

        // 1 udaj moze mat viac lit. zdrojov, avsak iba 1 cislo strany - prim. lit. zdroja
        this.tfLitRecPage.setText(udaj.getStranaUdaja());
        this.cbLitPhoto.setSelected(!udaj.getUdajObrazkies().isEmpty());
        this.cbLitMap.setSelected(udaj.getLitZdroj() == null ? false : udaj.getLitZdroj().isMapaRozsirenia());

        enabledLitFields(false, false);
    }

    private void populateLitRevs(LitZdroj litZdroj, List<LitZdrojRev> literatures) { //vsetky zdroje, originalny je prvy
        if (literatures == null) {
            log.error("populateLitRevs: litZrojRevs supplied are NULL");
            throw new NullPointerException("literatures");
        }
        this.jTableLitSources.setModel(new LiteraturesTableModel(replicateLitSources(litZdroj, literatures)));
        LiteraturesTableModel litModel = (LiteraturesTableModel) MainFrame.this.jTableLitSources.getModel();
        this.populateLitZdrojFields(litZdroj); // vyobraz primarny lit. zdroj
    }

    private void populateStatusUdaja(Udaj udaj) {
        if (udaj.isVerejnePristupny()) {
            rbIhned.setSelected(true);
        } else if (udaj.getVerejnePristupnyOd() == null) {
            rbNeverejny.setSelected(true);
        } else {
            rbOd.setSelected(true);
            tfNeverDD.setText(login);
            tfNeverMM.setText(login);
            tfNeverRRRR.setText(login);
        }
    }

    private void populateImages(List<UdajObrazky> udajObrazkies, boolean isCopied) {

        if (udajObrazkies.isEmpty()) {
            log.error("populateImages: udajObrazkies supplied is EMPTY");
            throw new NullPointerException("udajObrazkies");
        }

        if (isCopied) {
            udajObrazkies = replicateImages(udajObrazkies);
        }

        jTableImages.setModel(new ImagesTableModel(udajObrazkies));
        TableColumnsWidthCounter.setWidthOfColumns(jTableImages);

        // vyobraz prvy z obrazkov
        populateImageFields(udajObrazkies.get(0));
        enabledImageFields(false);

    }

    private List<SkupRev> replicateRevisions(Collection<SkupRev> original) {
        List<SkupRev> revs = new ArrayList<>(original.size()); //zoznam novych revizii
        List<MenaZberRev> srds = new ArrayList<>(2); //zoznam revidovatelov
        for (SkupRev skupRev : original) {
            SkupRev rev = skupRev.replicate(); //kopia objektu
            for (Object object : skupRev.getSkupRevDets()) {
                if (object instanceof SkupRevDet) {
                    SkupRevDet srd = (SkupRevDet) object;
                    srds.add(srd.getMenaZberRev());
                }
            }
            revs.add(ListAddRemoveUtils.setRevisorsAndIdentificators(rev, srds));
            srds.clear();
        }
        return revs;
    }

    private List<LitZdrojRev> replicateLitSources(LitZdroj litZdroj, List<LitZdrojRev> originalLzrevs) {
        List<LitZdrojRev> revs = new ArrayList<>(originalLzrevs.size());

        //povodny literalny zdroj zaradime na zaciatok zoznamu
        LitZdrojRev lzr = new LitZdrojRev();
        lzr.setLitZdroj(litZdroj);

        revs.add(lzr);

        for (LitZdrojRev litZdrojRev : originalLzrevs) {
            // nevyuzivame, nakolko sa momentelne nepodporuje funkcionalita na modifikovanie existujuceho lit. zdroja
            //LitZdrojRev rev = litZdrojRev.replicate(); //kopia objektu
            //revs.add(rev);
            revs.add(litZdrojRev);
        }
        return revs;
    }

    private List<UdajObrazky> replicateImages(Collection<UdajObrazky> original) {
        List<UdajObrazky> imgs = new ArrayList(original.size()); //zoznam novych revizii
        for (UdajObrazky image : original) {
            UdajObrazky img = image.replicate(); //kopia objektu
            imgs.add(img);
        }
        return imgs;
    }

    /**
     * Zaisti nahratie zoznamu autorov do moznosti pri kliknuti na
     * JListAddRemove.
     *
     * @param evt
     */
    private void jListPeopleFocusGained(MoznostiCaller field) {
//        clearTextfieldsMoznosti(false);
        this.tfMoznostiFocusGained(field, "MenaZberRev", "meno");

//        List<Entity> people = hq.getAllRecords("MenaZberRev", "meno");
//        listMoznosti.getFilterField().setText("");
//        listMoznosti.setContainer(people);
//        jListIdentificators.setSourceList(listMoznosti.getMainList());
//        jListCollectors.setSourceList(listMoznosti.getMainList());
//        jListRevisors.setSourceList(listMoznosti.getMainList());
//        jListLitAuthors.setSourceList(listMoznosti.getMainList());
//        jListLitEditors.setSourceList(listMoznosti.getMainList());
    }

    private void jListQuadrantsFocusGained(MoznostiCaller field) {
//        clearTextfieldsMoznosti(false);
        this.tfMoznostiFocusGained(field, "Kvadrant", "meno");
//        List<Entity> quadrants = hq.getAllRecords("Kvadrant", "meno");
//        listMoznosti.getFilterField().setText("");
//        listMoznosti.setContainer(quadrants);
//        jListQuadrants.setSourceList(listMoznosti.getMainList());
    }

    private void jListFytoFocusGained(MoznostiCaller field) {
//        clearTextfieldsMoznosti(false);
        this.tfMoznostiFocusGained(field, "Ftgokres", "meno");
//        List<Entity> ftgokresy = hq.getAllRecords("Ftgokres", "meno");
//        listMoznosti.getFilterField().setText("");
//        listMoznosti.setContainer(ftgokresy);
//        jListFyto.setSourceList(listMoznosti.getMainList());
    }

    /**
     * Zaisti nahratie spravneho zoznamu udajov do moznosti pri kliknuti na pole
     * fieldStdName.
     *
     * @param field Pole, na ktore ziskalo focus.
     * @param tablename
     * @param tablefield
     */
    private void tfMoznostiFocusGained(MoznostiCaller field, String tablename, String tablefield) {
//        clearSourceLists();
        List<AssociableEntity> ents = hq.getAllRecords(tablename, tablefield);
        //field.setMoznosti(listMoznosti.getMainList());
        listMoznosti.setCaller(field);
        listMoznosti.setContainer(ents);
    }

    /**
     * Metoda je napisana presne pre komponenty. Bud pre dlzku alebo sirku. Nie
     * je to velmi stastne.
     *
     * @param decimal
     * @param isLat
     */
    private void splitDegrees(double decimal, boolean isLat) {
        double deg = Math.floor(decimal);
        double frac = Math.abs(decimal - deg);

        double mins = Math.floor(frac * 60);
        frac = frac * 60 - mins;

        double sec = frac * 60;
        if (Math.rint(sec) == 60) {
            mins++;
            sec = 0;
        }

        if (Math.rint(mins) == 60) {
            if (deg < 0) {
                deg--;
            } else {
                deg++;
            }
            mins = 0;
        }

        DecimalFormat df = new DecimalFormat("#.####");
        DecimalFormatSymbols decimalSymbols = new DecimalFormatSymbols();
        decimalSymbols.setDecimalSeparator('.');
        df.setDecimalFormatSymbols(decimalSymbols);

        if (isLat) {
            tfLocLatDeg.setText(String.valueOf((int) deg));
            tfLocLatMin.setText(String.valueOf((int) mins));
            tfLocLatSec.setText(String.valueOf(df.format(sec)));
        } else {
            tfLocLngDeg.setText(String.valueOf((int) deg));
            tfLocLngMin.setText(String.valueOf((int) mins));
            tfLocLngSec.setText(String.valueOf(df.format(sec)));
        }
    }

    public void setTabsAndFocus(Character typ, boolean changeFocus) {
        int focusTabIndex = 0;
        switch (typ) {
            case 'L':
                // pridaj zaluzku s lit. zdrojmi
                jScrollPaneLitZdroj.setName("Literárny zdroj");
                tabbedPaneInsert.add(jScrollPaneLitZdroj, 0);
                break;
            case 'H':
                // pridaj zaluzku s lit. zdrojmi
                jScrollPaneLitZdroj.setName("Literárny zdroj");
                tabbedPaneInsert.add(jScrollPaneLitZdroj, 0);
                focusTabIndex = 1;
                break;
            case 'T':
                // bez zaluzky s lit. zdrojmi
                tabbedPaneInsert.remove(jScrollPaneLitZdroj);
                break;
        }

        if (changeFocus) {
            // nastav sa na predvolenu zalozku
            tabbedPaneInsert.setSelectedIndex(focusTabIndex);
        }
    }

    public void setNewRecordTabName(Character typ) {
        if (typ == null) {
            log.error("setNewRecordTabName: supplied typ of udaj is NULL");
            throw new NullPointerException("record typ");
        }
        switch (typ) {
            case 'L':
                getRecordTab().setName("Nový literárny údaj");
                break;
            case 'H':
                getRecordTab().setName("Nový herbárový údaj");
                break;
            case 'T':
                getRecordTab().setName("Nový terénny údaj");
                break;
            default:
                getRecordTab().setName("Nový údaj");
                log.error("unknown type of record");
                throw new IllegalArgumentException("unknown recod type");
        }
    }

    private BufferedImage scaleImgToLbl(BufferedImage img) {
        int imgWidth = img.getWidth();
        int imgHeight = img.getHeight();
        int lblWidth = lblImageDisplay.getWidth();
        int lblHeight = lblImageDisplay.getHeight();

        // pokial su rozmery obrazka mensie ako vyobrazovacieho ramceka, vratime jeho plne rozmery, v opacnom pripade ho zmensime mensim z pomerov stran (ratio)
        double ratio = Math.min((double) lblWidth / imgWidth, (double) lblHeight / imgHeight);

        if (ratio >= 1 || ratio <= 0) {
            return img;
        } else {
            return scale(img, ratio);
        }
    }

    private BufferedImage scale(BufferedImage source, double ratio) {
        int w = (int) (source.getWidth() * ratio);
        int h = (int) (source.getHeight() * ratio);
        BufferedImage bi = getCompatibleImage(w, h);
        Graphics2D g2d = bi.createGraphics();
        double xScale = (double) w / source.getWidth();
        double yScale = (double) h / source.getHeight();
        AffineTransform at = AffineTransform.getScaleInstance(xScale, yScale);
        g2d.drawRenderedImage(source, at);
        g2d.dispose();
        return bi;
    }

    private BufferedImage getCompatibleImage(int w, int h) {
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice gd = ge.getDefaultScreenDevice();
        GraphicsConfiguration gc = gd.getDefaultConfiguration();
        BufferedImage image = gc.createCompatibleImage(w, h);
        return image;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        rbLocationNS = new javax.swing.ButtonGroup();
        rbLocationWE = new javax.swing.ButtonGroup();
        loginDialog = new javax.swing.JDialog();
        jLabelDATAflos = new javax.swing.JLabel();
        jLabelLogin = new javax.swing.JLabel();
        tfLogin = new javax.swing.JTextField();
        jLabelPassword = new javax.swing.JLabel();
        pfPassword = new javax.swing.JPasswordField();
        btnLogin = new javax.swing.JButton();
        btnCloseLogin = new javax.swing.JButton();
        rbRecordAccess = new javax.swing.ButtonGroup();
        filler4 = new javax.swing.Box.Filler(new java.awt.Dimension(0, 10), new java.awt.Dimension(0, 10), new java.awt.Dimension(32767, 10));
        jTabbedPaneMain = new javax.swing.JTabbedPane();
        tablesOverviewTab = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jListTables = new javax.swing.JList();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTableOverview = new javax.swing.JTable();
        btnTablesPrevPage = new javax.swing.JButton();
        jLabelPages = new javax.swing.JLabel();
        btnTablesNextPage = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jTextFieldFilter = new javax.swing.JTextField();
        btnTablesSearch = new javax.swing.JButton();
        jButton1 = new javax.swing.JButton();
        btnNewEntity = new javax.swing.JButton();
        btnTablesDeleteRec = new javax.swing.JButton();
        btnTablesOverviewCancel = new javax.swing.JButton();
        btnFilterByLetterA = new javax.swing.JButton();
        btnFilterByLetterB = new javax.swing.JButton();
        btnFilterByLetterC = new javax.swing.JButton();
        btnFilterByLetterD = new javax.swing.JButton();
        btnFilterByLetterE = new javax.swing.JButton();
        btnFilterByLetterF = new javax.swing.JButton();
        btnFilterByLetterG = new javax.swing.JButton();
        btnFilterByLetterH = new javax.swing.JButton();
        btnFilterByLetterI = new javax.swing.JButton();
        btnFilterByLetterJ = new javax.swing.JButton();
        btnFilterByLetterK = new javax.swing.JButton();
        btnFilterByLetterL = new javax.swing.JButton();
        btnFilterByLetterM = new javax.swing.JButton();
        btnFilterByLetterN = new javax.swing.JButton();
        btnFilterByLetterO = new javax.swing.JButton();
        btnFilterByLetterP = new javax.swing.JButton();
        btnFilterByLetterQ = new javax.swing.JButton();
        btnFilterByLetterR = new javax.swing.JButton();
        btnFilterByLetterS = new javax.swing.JButton();
        btnFilterByLetterT = new javax.swing.JButton();
        btnFilterByLetterU = new javax.swing.JButton();
        btnFilterByLetterV = new javax.swing.JButton();
        btnFilterByLetterW = new javax.swing.JButton();
        btnFilterByLetterX = new javax.swing.JButton();
        btnFilterByLetterY = new javax.swing.JButton();
        btnFilterByLetterZ = new javax.swing.JButton();
        recordsOverviewTab = new javax.swing.JPanel();
        btnOverviewDeleteRec = new javax.swing.JButton();
        btnOverviewNewHerbRec = new javax.swing.JButton();
        btnOverviewNewLitRec = new javax.swing.JButton();
        btnOverviewCopyRec = new javax.swing.JButton();
        btnOverviewCancel = new javax.swing.JButton();
        jScrollPane10 = new javax.swing.JScrollPane();
        filterRecordsOverview = new sk.sav.bot.dataflos.gui.FilterRecordsOverview();
        recordTab = new javax.swing.JPanel();
        tabbedPaneInsert = new javax.swing.JTabbedPane();
        jScrollPaneLitZdroj = new javax.swing.JScrollPane();
        jPanelLitZdroj = new javax.swing.JPanel();
        jListLitAuthors = new sk.sav.bot.dataflos.gui.JListAddRemove<MenaZberRev>(true, 195);
        jLabel64 = new javax.swing.JLabel();
        jLabel65 = new javax.swing.JLabel();
        jListLitEditors = new sk.sav.bot.dataflos.gui.JListAddRemove<MenaZberRev>(195);
        jLabel66 = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        jLabel70 = new javax.swing.JLabel();
        tfLitPublTitle = new javax.swing.JTextField();
        tfLitPublTitleTransl = new javax.swing.JTextField();
        jLabel71 = new javax.swing.JLabel();
        tfLitSource = new javax.swing.JTextField();
        jLabel72 = new javax.swing.JLabel();
        tfLitPublYear = new javax.swing.JTextField();
        jLabel73 = new javax.swing.JLabel();
        jLabel74 = new javax.swing.JLabel();
        tfLitJournalTitle = new sk.sav.bot.dataflos.gui.TextFieldMoznosti(20, "sk.sav.bot.dataflos.entity.Casopisy", true);
        tfLitYear = new javax.swing.JTextField();
        jLabel75 = new javax.swing.JLabel();
        jLabel76 = new javax.swing.JLabel();
        tfLitIssue = new javax.swing.JTextField();
        tfLitPages = new javax.swing.JTextField();
        jLabel77 = new javax.swing.JLabel();
        tfLitBook = new javax.swing.JTextField();
        jLabel78 = new javax.swing.JLabel();
        tfLitPublisher = new javax.swing.JTextField();
        jLabel79 = new javax.swing.JLabel();
        tfLitChapter = new javax.swing.JTextField();
        jLabel80 = new javax.swing.JLabel();
        jListLitKeywords = new sk.sav.bot.dataflos.gui.JListAddRemove(510);
        jLabel81 = new javax.swing.JLabel();
        tfLitNotes = new javax.swing.JTextField();
        jLabel82 = new javax.swing.JLabel();
        jSeparator2 = new javax.swing.JSeparator();
        tfLitRecPage = new javax.swing.JTextField();
        jLabel83 = new javax.swing.JLabel();
        cbLitPhoto = new javax.swing.JCheckBox();
        cbLitMap = new javax.swing.JCheckBox();
        jSeparator3 = new javax.swing.JSeparator();
        btnLitCopyPrev = new javax.swing.JButton();
        btnAddLitSourceTop = new javax.swing.JButton();
        jLabel62 = new javax.swing.JLabel();
        btnLitTabDataErase = new javax.swing.JButton();
        btnSaveLitSourceToTable = new javax.swing.JButton();
        jLitTablePanel = new javax.swing.JPanel();
        jTableLitSourcesScrollPane = new javax.swing.JScrollPane();
        jTableLitSources = new javax.swing.JTable();
        btnAddLitSourceDown = new javax.swing.JButton();
        btnDeleteLitSource = new javax.swing.JButton();
        btnCancelAdding = new javax.swing.JButton();
        jScrollPaneUrcenie = new javax.swing.JScrollPane();
        jPanelUrcenie = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        tfUrcStdMeno = new sk.sav.bot.dataflos.gui.TextFieldMoznosti(35, "sk.sav.bot.dataflos.entity.ListOfSpecies", true);
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        tfUrcPoznamka = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        tfUrcNazovScheda = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        tfUrcAccName = new javax.swing.JTextField();
        tfDatumUrcSlovom = new javax.swing.JTextField();
        jLabel88 = new javax.swing.JLabel();
        btnIdentCopyPrev = new javax.swing.JButton();
        jPanel8 = new javax.swing.JPanel();
        jListIdentificators = new sk.sav.bot.dataflos.gui.JListAddRemove<MenaZberRev>(true);
        jLabel6 = new javax.swing.JLabel();
        tfDatumUrcD = new javax.swing.JTextField();
        tfDatumUrcM = new javax.swing.JTextField();
        tfDatumUrcR = new javax.swing.JTextField();
        jLabel40 = new javax.swing.JLabel();
        btnIdentTabDataErase = new javax.swing.JButton();
        filler2 = new javax.swing.Box.Filler(new java.awt.Dimension(0, 10), new java.awt.Dimension(0, 10), new java.awt.Dimension(32767, 10));
        tfUrcPochybnost = new javax.swing.JTextField();
        tfUrcOhrozenost = new javax.swing.JTextField();
        cbUrcOchrana = new javax.swing.JCheckBox();
        tfUrcEndemizmus = new javax.swing.JTextField();
        tfUrcPovodnost = new javax.swing.JTextField();
        tfUrcSvkNazov = new javax.swing.JTextField();
        jLabel69 = new javax.swing.JLabel();
        jLabel84 = new javax.swing.JLabel();
        jLabel86 = new javax.swing.JLabel();
        jLabel87 = new javax.swing.JLabel();
        jLabel93 = new javax.swing.JLabel();
        jSeparator4 = new javax.swing.JSeparator();
        jSeparator5 = new javax.swing.JSeparator();
        jScrollPaneLokalita = new javax.swing.JScrollPane();
        jPanelLokalita = new javax.swing.JPanel();
        jLabel9 = new javax.swing.JLabel();
        tfLocBrumFour = new sk.sav.bot.dataflos.gui.TextFieldMoznosti(20, "sk.sav.bot.dataflos.entity.Brumit4");
        jLabel10 = new javax.swing.JLabel();
        tfLocFlora = new sk.sav.bot.dataflos.gui.TextFieldMoznosti(20, "sk.sav.bot.dataflos.entity.Flora", true);
        jLabel11 = new javax.swing.JLabel();
        tfLocVac = new sk.sav.bot.dataflos.gui.TextFieldMoznosti(20, "sk.sav.bot.dataflos.entity.Vac");
        jLabel12 = new javax.swing.JLabel();
        tfLocNac = new sk.sav.bot.dataflos.gui.TextFieldMoznosti(20, "sk.sav.bot.dataflos.entity.Nac");
        jLabel13 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        tfLocVgc = new sk.sav.bot.dataflos.gui.TextFieldMoznosti(20, "sk.sav.bot.dataflos.entity.Vgc");
        jLabel15 = new javax.swing.JLabel();
        tfLocNgc = new sk.sav.bot.dataflos.gui.TextFieldMoznosti(20, "sk.sav.bot.dataflos.entity.Ngc");
        jLabel16 = new javax.swing.JLabel();
        tfLocVill = new sk.sav.bot.dataflos.gui.TextFieldMoznosti(20, "sk.sav.bot.dataflos.entity.Obec", true);
        jLabel18 = new javax.swing.JLabel();
        jScrollPane3 = new javax.swing.JScrollPane();
        taLocDescr = new javax.swing.JTextArea();
        jLabel19 = new javax.swing.JLabel();
        tfLocAltFrom = new javax.swing.JTextField();
        jLabel20 = new javax.swing.JLabel();
        tfLocAltTo = new javax.swing.JTextField();
        jLabel21 = new javax.swing.JLabel();
        chbLocAltCca = new javax.swing.JCheckBox();
        jLabel22 = new javax.swing.JLabel();
        tfLocLatDeg = new javax.swing.JTextField();
        jLabel23 = new javax.swing.JLabel();
        tfLocLatMin = new javax.swing.JTextField();
        jLabel24 = new javax.swing.JLabel();
        tfLocLatSec = new javax.swing.JTextField();
        jLabel25 = new javax.swing.JLabel();
        rbLocNorth = new javax.swing.JRadioButton();
        rbLocSouth = new javax.swing.JRadioButton();
        filler1 = new javax.swing.Box.Filler(new java.awt.Dimension(0, 0), new java.awt.Dimension(0, 0), new java.awt.Dimension(32767, 0));
        jLabel26 = new javax.swing.JLabel();
        tfLocLngDeg = new javax.swing.JTextField();
        jLabel27 = new javax.swing.JLabel();
        tfLocLngMin = new javax.swing.JTextField();
        jLabel28 = new javax.swing.JLabel();
        tfLocLngSec = new javax.swing.JTextField();
        jLabel29 = new javax.swing.JLabel();
        rbLocWest = new javax.swing.JRadioButton();
        rbLocEast = new javax.swing.JRadioButton();
        jLabel30 = new javax.swing.JLabel();
        cbLocCoordType = new javax.swing.JComboBox();
        jLabel31 = new javax.swing.JLabel();
        tfLocSoil = new javax.swing.JTextField();
        jLabel32 = new javax.swing.JLabel();
        jLabel33 = new javax.swing.JLabel();
        tfLocSubstrate = new javax.swing.JTextField();
        jLabel34 = new javax.swing.JLabel();
        tfLocHost = new javax.swing.JTextField();
        jLabel35 = new javax.swing.JLabel();
        tfLocAltEpif = new javax.swing.JTextField();
        jLabel36 = new javax.swing.JLabel();
        jScrollPane5 = new javax.swing.JScrollPane();
        taLocScheda = new javax.swing.JTextArea();
        jLabel37 = new javax.swing.JLabel();
        jScrollPane6 = new javax.swing.JScrollPane();
        taLocNotes = new javax.swing.JTextArea();
        jListCollectors = new sk.sav.bot.dataflos.gui.JListAddRemove<MenaZberRev>(true);
        jLabel38 = new javax.swing.JLabel();
        jLabel39 = new javax.swing.JLabel();
        tfDatumZbD = new javax.swing.JTextField();
        tfDatumZbM = new javax.swing.JTextField();
        tfDatumZbR = new javax.swing.JTextField();
        jLabel41 = new javax.swing.JLabel();
        jLabel42 = new javax.swing.JLabel();
        tfDatumZbSlovom = new javax.swing.JTextField();
        jLabel61 = new javax.swing.JLabel();
        tfLocProtArea = new sk.sav.bot.dataflos.gui.TextFieldMoznosti(20, "sk.sav.bot.dataflos.entity.Chu");
        jListQuadrants = new sk.sav.bot.dataflos.gui.JListAddRemove<Kvadrant>(true);
        btnLocCopyPrev = new javax.swing.JButton();
        jLabel89 = new javax.swing.JLabel();
        jLabel90 = new javax.swing.JLabel();
        jLabel91 = new javax.swing.JLabel();
        jLabel92 = new javax.swing.JLabel();
        btnLocTabDataErase = new javax.swing.JButton();
        jListFyto = new sk.sav.bot.dataflos.gui.JListAddRemove<Ftgokres>(true);
        jScrollPaneRevizie = new javax.swing.JScrollPane();
        jPanelRevizie = new javax.swing.JPanel();
        jLabel43 = new javax.swing.JLabel();
        tfRevNazovListok = new javax.swing.JTextField();
        jLabel44 = new javax.swing.JLabel();
        tfRevStdMeno = new sk.sav.bot.dataflos.gui.TextFieldMoznosti(35, "sk.sav.bot.dataflos.entity.ListOfSpecies");
        jLabel45 = new javax.swing.JLabel();
        tfRevAccName = new javax.swing.JTextField();
        jLabel46 = new javax.swing.JLabel();
        tfRevPoznamka = new javax.swing.JTextField();
        jLabel47 = new javax.swing.JLabel();
        jListRevisors = new sk.sav.bot.dataflos.gui.JListAddRemove();
        jLabel48 = new javax.swing.JLabel();
        jLabel49 = new javax.swing.JLabel();
        tfDatumRevSlovom = new javax.swing.JTextField();
        tfDatumRevD = new javax.swing.JTextField();
        tfDatumRevM = new javax.swing.JTextField();
        tfDatumRevR = new javax.swing.JTextField();
        jLabel50 = new javax.swing.JLabel();
        jButtonAddRev = new javax.swing.JButton();
        jButtonCancelRev = new javax.swing.JButton();
        btnRevCopyPrev = new javax.swing.JButton();
        btnRevTabDataErase = new javax.swing.JButton();
        jPanel9 = new javax.swing.JPanel();
        jScrollPane7 = new javax.swing.JScrollPane();
        jTableRevisions = new javax.swing.JTable();
        btnAddRevDown = new javax.swing.JButton();
        btnDelRev = new javax.swing.JButton();
        btnAddRevTop = new javax.swing.JButton();
        jSeparator6 = new javax.swing.JSeparator();
        tfRevPochybnost = new javax.swing.JTextField();
        jLabel94 = new javax.swing.JLabel();
        jLabel95 = new javax.swing.JLabel();
        tfRevOhrozenost = new javax.swing.JTextField();
        jLabel96 = new javax.swing.JLabel();
        tfRevEndemizmus = new javax.swing.JTextField();
        jLabel97 = new javax.swing.JLabel();
        tfRevPovodnost = new javax.swing.JTextField();
        jLabel98 = new javax.swing.JLabel();
        tfRevSvkNazov = new javax.swing.JTextField();
        cbRevOchrana = new javax.swing.JCheckBox();
        jSeparator7 = new javax.swing.JSeparator();
        jScrollPaneZaradenie = new javax.swing.JScrollPane();
        jPanelZaradenie = new javax.swing.JPanel();
        jLabel51 = new javax.swing.JLabel();
        jLabel52 = new javax.swing.JLabel();
        tfZarHerbar = new sk.sav.bot.dataflos.gui.TextFieldMoznosti(50, "sk.sav.bot.dataflos.entity.Herbar", true);
        tfZarDallaTorre = new sk.sav.bot.dataflos.gui.TextFieldMoznosti(50, "sk.sav.bot.dataflos.entity.DallaTorre");
        tfZarCisloPol = new javax.swing.JTextField();
        jLabel54 = new javax.swing.JLabel();
        tfZarBarcode = new javax.swing.JTextField();
        jLabel55 = new javax.swing.JLabel();
        tfZarExsikat = new sk.sav.bot.dataflos.gui.TextFieldMoznosti(50, "sk.sav.bot.dataflos.entity.Exsikaty");
        jLabel56 = new javax.swing.JLabel();
        tfZarVoucher = new sk.sav.bot.dataflos.gui.TextFieldMoznosti(50, "sk.sav.bot.dataflos.entity.Voucher");
        jLabel57 = new javax.swing.JLabel();
        tfZarCisloZberu = new javax.swing.JTextField();
        jLabel58 = new javax.swing.JLabel();
        tfZarKvantif = new javax.swing.JTextField();
        jLabel59 = new javax.swing.JLabel();
        jScrollPane9 = new javax.swing.JScrollPane();
        taZarHerbPoznamka = new javax.swing.JTextArea();
        cbTypeUnit = new javax.swing.JCheckBox();
        btnHerbCopyPrev = new javax.swing.JButton();
        jLabel85 = new javax.swing.JLabel();
        btnHerbTabDataErase = new javax.swing.JButton();
        filler3 = new javax.swing.Box.Filler(new java.awt.Dimension(0, 10), new java.awt.Dimension(0, 10), new java.awt.Dimension(32767, 10));
        jScrollObrazky = new javax.swing.JScrollPane();
        jPanelObrazky = new javax.swing.JPanel();
        jPanel6 = new javax.swing.JPanel();
        jLabel60 = new javax.swing.JLabel();
        jLabel63 = new javax.swing.JLabel();
        tfImageUrl = new javax.swing.JTextField();
        btnImagesCopyPrev = new javax.swing.JButton();
        btnImagesTabDataErase = new javax.swing.JButton();
        jScrollPane4 = new javax.swing.JScrollPane();
        taImageDescr = new javax.swing.JTextArea();
        jLabel53 = new javax.swing.JLabel();
        tfFindLocalImageName = new javax.swing.JTextField();
        btnFindLocalImage = new javax.swing.JButton();
        jPanel10 = new javax.swing.JPanel();
        jScrollPane8 = new javax.swing.JScrollPane();
        jTableImages = new javax.swing.JTable();
        btnAddImageDown = new javax.swing.JButton();
        btnDelImage = new javax.swing.JButton();
        btnAddImageTop = new javax.swing.JButton();
        lblImageDisplay = new javax.swing.JLabel();
        jLabel67 = new javax.swing.JLabel();
        jButtonAddImage = new javax.swing.JButton();
        jButtonCancelImage = new javax.swing.JButton();
        jLabel68 = new javax.swing.JLabel();
        jScrollPaneStatusUdaja = new javax.swing.JScrollPane();
        jStatusUdaja = new javax.swing.JPanel();
        btnStatusCopyPrev = new javax.swing.JButton();
        rbIhned = new javax.swing.JRadioButton();
        rbOd = new javax.swing.JRadioButton();
        rbNeverejny = new javax.swing.JRadioButton();
        tfNeverDD = new javax.swing.JTextField();
        tfNeverMM = new javax.swing.JTextField();
        tfNeverRRRR = new javax.swing.JTextField();
        jDdMmRrrr = new javax.swing.JLabel();
        jPoznamkaNepub = new javax.swing.JLabel();
        jPoznamkaNepublik = new javax.swing.JTextField();
        jUdajDolozeny = new javax.swing.JLabel();
        fotograficky = new javax.swing.JRadioButton();
        btnStatusTabDataErase = new javax.swing.JButton();
        listMoznosti = new sk.sav.bot.dataflos.gui.FilterJList();
        btnRecordCancel = new javax.swing.JButton();
        btnRecordSave = new javax.swing.JButton();
        btnRecordDelete = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        jLabel17 = new javax.swing.JLabel();
        btnPrevRec = new javax.swing.JButton();
        lblRecPosition = new javax.swing.JTextField();
        lblUdajTyp = new javax.swing.JLabel();
        btnNextRec = new javax.swing.JButton();
        cbListAllRec = new javax.swing.JCheckBox();
        introTab = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jLabelVitajte = new javax.swing.JLabel();
        btnIntroNewHerbRec = new javax.swing.JButton();
        btnIntroNewLitRec = new javax.swing.JButton();
        btnIntroOverviewRecs = new javax.swing.JButton();
        btnIntroImportRecs = new javax.swing.JButton();
        btnIntroExportRecs = new javax.swing.JButton();
        btnIntroShowTables = new javax.swing.JButton();
        jLabelLogo = new javax.swing.JLabel();
        btnIntroNewFieldRec = new javax.swing.JButton();
        jLabelCoUrobit = new javax.swing.JLabel();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenuNew = new javax.swing.JMenu();
        miNewRecord = new javax.swing.JMenuItem();
        jMenuImport = new javax.swing.JMenu();
        jMenuImportItem = new javax.swing.JMenuItem();

        rbLocationNS.add(rbLocNorth);
        rbLocationNS.add(rbLocSouth);

        rbLocationWE.add(rbLocWest);
        rbLocationWE.add(rbLocEast);

        loginDialog.setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        loginDialog.setMinimumSize(new java.awt.Dimension(226, 250));
        loginDialog.setModal(true);
        loginDialog.setResizable(false);

        jLabelDATAflos.setFont(new java.awt.Font("Tahoma", 0, 36)); // NOI18N
        jLabelDATAflos.setText("DATAflos");

        jLabelLogin.setText("Prihlasovacie meno");

        jLabelPassword.setText("Heslo");

        pfPassword.setAction(new DoLoginAction());

        btnLogin.setAction(new DoLoginAction());
        btnLogin.setText("Ok");

        btnCloseLogin.setAction(new CancelLoginAction());
        btnCloseLogin.setText("Zrušiť");

        javax.swing.GroupLayout loginDialogLayout = new javax.swing.GroupLayout(loginDialog.getContentPane());
        loginDialog.getContentPane().setLayout(loginDialogLayout);
        loginDialogLayout.setHorizontalGroup(
            loginDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(loginDialogLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(loginDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(loginDialogLayout.createSequentialGroup()
                        .addComponent(btnLogin)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnCloseLogin))
                    .addGroup(loginDialogLayout.createSequentialGroup()
                        .addGroup(loginDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(loginDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addComponent(jLabelDATAflos, javax.swing.GroupLayout.DEFAULT_SIZE, 206, Short.MAX_VALUE)
                                .addComponent(jLabelLogin, javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(tfLogin, javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jLabelPassword, javax.swing.GroupLayout.Alignment.LEADING))
                            .addComponent(pfPassword, javax.swing.GroupLayout.PREFERRED_SIZE, 206, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        loginDialogLayout.setVerticalGroup(
            loginDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, loginDialogLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabelDATAflos, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabelLogin)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(tfLogin, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabelPassword)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pfPassword, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(6, 6, 6)
                .addGroup(loginDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnLogin)
                    .addComponent(btnCloseLogin))
                .addContainerGap())
        );

        rbRecordAccess.add(rbIhned);
        rbRecordAccess.add(rbOd);
        rbRecordAccess.add(rbNeverejny);

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("DATAflos");
        setIconImage(new javax.swing.ImageIcon(getClass().getResource("/app_logo.png")).getImage());

        jTabbedPaneMain.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jTabbedPaneMain.setName(""); // NOI18N

        jListTables.setModel(new TableListModel(dispEnt));
        jListTables.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        jScrollPane1.setViewportView(jListTables);
        jListTables.addListSelectionListener(new TableListSelectionListener());

        jTableOverview.setAutoCreateRowSorter(true);
        jTableOverview.setModel(new PagingModel(null));
        jTableOverview.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_ALL_COLUMNS);
        jTableOverview.setDoubleBuffered(true);
        jTableOverview.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        jScrollPane2.setViewportView(jTableOverview);

        btnTablesPrevPage.setAction(new PreviousPageAction());
        btnTablesPrevPage.setIcon(new javax.swing.ImageIcon(getClass().getResource("/tab_left.png"))); // NOI18N
        btnTablesPrevPage.setText("Predchádzajúce");
        btnTablesPrevPage.setEnabled(false);
        btnTablesPrevPage.setMaximumSize(new java.awt.Dimension(100, 25));
        btnTablesPrevPage.setMinimumSize(new java.awt.Dimension(100, 25));
        btnTablesPrevPage.setPreferredSize(new java.awt.Dimension(100, 25));

        btnTablesNextPage.setAction(new NextPageAction());
        btnTablesNextPage.setIcon(new javax.swing.ImageIcon(getClass().getResource("/tab_right.png"))); // NOI18N
        btnTablesNextPage.setText("Nasledujúce");
        btnTablesNextPage.setEnabled(false);
        btnTablesNextPage.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);
        btnTablesNextPage.setMaximumSize(new java.awt.Dimension(100, 25));
        btnTablesNextPage.setMinimumSize(new java.awt.Dimension(100, 25));
        btnTablesNextPage.setPreferredSize(new java.awt.Dimension(100, 25));

        jLabel1.setText("Filter");

        btnTablesSearch.setAction(new SearchAction());
        btnTablesSearch.setText("Hľadaj");

        jButton1.setAction(new CancelSearchAction());
        jButton1.setText("Zruš filter");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jTextFieldFilter, javax.swing.GroupLayout.PREFERRED_SIZE, 199, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnTablesSearch)
                        .addGap(18, 18, 18)
                        .addComponent(jButton1)))
                .addContainerGap(1687, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextFieldFilter, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnTablesSearch)
                    .addComponent(jButton1))
                .addContainerGap(46, Short.MAX_VALUE))
        );

        btnNewEntity.setAction(new AddNewObjectAction());
        btnNewEntity.setText("Nový");

        btnTablesDeleteRec.setAction(new DeleteRowAction());
        btnTablesDeleteRec.setText("Zmazať");
        btnTablesDeleteRec.setEnabled(false);

        btnTablesOverviewCancel.setAction(new CancelAction());
        btnTablesOverviewCancel.setText("Zatvoriť");

        btnFilterByLetterA.setAction(new FilterByLetterAction());
        btnFilterByLetterA.setText("A");
        btnFilterByLetterA.setPreferredSize(new java.awt.Dimension(40, 23));

        btnFilterByLetterB.setAction(new FilterByLetterAction());
        btnFilterByLetterB.setText("B");
        btnFilterByLetterB.setPreferredSize(new java.awt.Dimension(40, 23));

        btnFilterByLetterC.setAction(new FilterByLetterAction());
        btnFilterByLetterC.setText("C");
        btnFilterByLetterC.setPreferredSize(new java.awt.Dimension(40, 23));

        btnFilterByLetterD.setAction(new FilterByLetterAction());
        btnFilterByLetterD.setText("D");
        btnFilterByLetterD.setPreferredSize(new java.awt.Dimension(40, 23));

        btnFilterByLetterE.setAction(new FilterByLetterAction());
        btnFilterByLetterE.setText("E");
        btnFilterByLetterE.setPreferredSize(new java.awt.Dimension(40, 23));

        btnFilterByLetterF.setAction(new FilterByLetterAction());
        btnFilterByLetterF.setText("F");
        btnFilterByLetterF.setPreferredSize(new java.awt.Dimension(40, 23));

        btnFilterByLetterG.setAction(new FilterByLetterAction());
        btnFilterByLetterG.setText("G");
        btnFilterByLetterG.setPreferredSize(new java.awt.Dimension(40, 23));

        btnFilterByLetterH.setAction(new FilterByLetterAction());
        btnFilterByLetterH.setText("H");
        btnFilterByLetterH.setPreferredSize(new java.awt.Dimension(40, 23));

        btnFilterByLetterI.setAction(new FilterByLetterAction());
        btnFilterByLetterI.setText("I");
        btnFilterByLetterI.setPreferredSize(new java.awt.Dimension(40, 23));

        btnFilterByLetterJ.setAction(new FilterByLetterAction());
        btnFilterByLetterJ.setText("J");
        btnFilterByLetterJ.setPreferredSize(new java.awt.Dimension(40, 23));

        btnFilterByLetterK.setAction(new FilterByLetterAction());
        btnFilterByLetterK.setText("K");
        btnFilterByLetterK.setPreferredSize(new java.awt.Dimension(40, 23));

        btnFilterByLetterL.setAction(new FilterByLetterAction());
        btnFilterByLetterL.setText("L");
        btnFilterByLetterL.setPreferredSize(new java.awt.Dimension(40, 23));

        btnFilterByLetterM.setAction(new FilterByLetterAction());
        btnFilterByLetterM.setText("M");
        btnFilterByLetterM.setPreferredSize(new java.awt.Dimension(40, 23));

        btnFilterByLetterN.setAction(new FilterByLetterAction());
        btnFilterByLetterN.setText("N");
        btnFilterByLetterN.setPreferredSize(new java.awt.Dimension(40, 23));

        btnFilterByLetterO.setAction(new FilterByLetterAction());
        btnFilterByLetterO.setText("O");
        btnFilterByLetterO.setPreferredSize(new java.awt.Dimension(40, 23));

        btnFilterByLetterP.setAction(new FilterByLetterAction());
        btnFilterByLetterP.setText("P");
        btnFilterByLetterP.setPreferredSize(new java.awt.Dimension(40, 23));

        btnFilterByLetterQ.setAction(new FilterByLetterAction());
        btnFilterByLetterQ.setText("Q");
        btnFilterByLetterQ.setPreferredSize(new java.awt.Dimension(40, 23));

        btnFilterByLetterR.setAction(new FilterByLetterAction());
        btnFilterByLetterR.setText("R");
        btnFilterByLetterR.setPreferredSize(new java.awt.Dimension(40, 23));

        btnFilterByLetterS.setAction(new FilterByLetterAction());
        btnFilterByLetterS.setText("S");
        btnFilterByLetterS.setPreferredSize(new java.awt.Dimension(40, 23));

        btnFilterByLetterT.setAction(new FilterByLetterAction());
        btnFilterByLetterT.setText("T");
        btnFilterByLetterT.setPreferredSize(new java.awt.Dimension(40, 23));

        btnFilterByLetterU.setAction(new FilterByLetterAction());
        btnFilterByLetterU.setText("U");
        btnFilterByLetterU.setPreferredSize(new java.awt.Dimension(40, 23));

        btnFilterByLetterV.setAction(new FilterByLetterAction());
        btnFilterByLetterV.setText("V");
        btnFilterByLetterV.setPreferredSize(new java.awt.Dimension(40, 23));

        btnFilterByLetterW.setAction(new FilterByLetterAction());
        btnFilterByLetterW.setText("W");
        btnFilterByLetterW.setPreferredSize(new java.awt.Dimension(40, 23));

        btnFilterByLetterX.setAction(new FilterByLetterAction());
        btnFilterByLetterX.setText("X");
        btnFilterByLetterX.setPreferredSize(new java.awt.Dimension(40, 23));

        btnFilterByLetterY.setAction(new FilterByLetterAction());
        btnFilterByLetterY.setText("Y");
        btnFilterByLetterY.setPreferredSize(new java.awt.Dimension(40, 23));

        btnFilterByLetterZ.setAction(new FilterByLetterAction());
        btnFilterByLetterZ.setText("Z");
        btnFilterByLetterZ.setPreferredSize(new java.awt.Dimension(40, 23));

        javax.swing.GroupLayout tablesOverviewTabLayout = new javax.swing.GroupLayout(tablesOverviewTab);
        tablesOverviewTab.setLayout(tablesOverviewTabLayout);
        tablesOverviewTabLayout.setHorizontalGroup(
            tablesOverviewTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(tablesOverviewTabLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 142, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(tablesOverviewTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 1997, Short.MAX_VALUE)
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, tablesOverviewTabLayout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(btnTablesOverviewCancel))
                    .addGroup(tablesOverviewTabLayout.createSequentialGroup()
                        .addComponent(btnTablesPrevPage, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabelPages)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnTablesNextPage, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(57, 57, 57)
                        .addGroup(tablesOverviewTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(tablesOverviewTabLayout.createSequentialGroup()
                                .addComponent(btnFilterByLetterA, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnFilterByLetterB, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnFilterByLetterC, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnFilterByLetterD, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnFilterByLetterE, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnFilterByLetterF, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnFilterByLetterG, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnFilterByLetterH, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnFilterByLetterI, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnFilterByLetterJ, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnFilterByLetterK, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnFilterByLetterL, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnFilterByLetterM, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(tablesOverviewTabLayout.createSequentialGroup()
                                .addComponent(btnFilterByLetterN, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnFilterByLetterO, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnFilterByLetterP, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnFilterByLetterQ, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnFilterByLetterR, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnFilterByLetterS, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnFilterByLetterT, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnFilterByLetterU, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnFilterByLetterV, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnFilterByLetterW, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnFilterByLetterX, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnFilterByLetterY, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnFilterByLetterZ, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnNewEntity)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnTablesDeleteRec)))
                .addContainerGap())
        );
        tablesOverviewTabLayout.setVerticalGroup(
            tablesOverviewTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, tablesOverviewTabLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(tablesOverviewTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(tablesOverviewTabLayout.createSequentialGroup()
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(tablesOverviewTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btnTablesPrevPage, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabelPages)
                            .addComponent(btnTablesNextPage, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnNewEntity)
                            .addComponent(btnTablesDeleteRec)
                            .addComponent(btnFilterByLetterA, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnFilterByLetterB, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnFilterByLetterC, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnFilterByLetterD, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnFilterByLetterE, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnFilterByLetterF, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnFilterByLetterG, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnFilterByLetterH, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnFilterByLetterI, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnFilterByLetterJ, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnFilterByLetterK, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnFilterByLetterL, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnFilterByLetterM, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(tablesOverviewTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btnFilterByLetterN, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnFilterByLetterO, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnFilterByLetterP, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnFilterByLetterQ, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnFilterByLetterR, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnFilterByLetterS, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnFilterByLetterT, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnFilterByLetterU, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnFilterByLetterV, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnFilterByLetterW, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnFilterByLetterX, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnFilterByLetterY, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnFilterByLetterZ, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 266, Short.MAX_VALUE)
                        .addComponent(btnTablesOverviewCancel))
                    .addComponent(jScrollPane1))
                .addContainerGap())
        );

        jTabbedPaneMain.addTab("Prehľad tabuliek", tablesOverviewTab);

        btnOverviewDeleteRec.setAction(new DeleteRecordAction());
        btnOverviewDeleteRec.setText("Odstrániť");
        btnOverviewDeleteRec.setEnabled(false);

        btnOverviewNewHerbRec.setAction(new ShowNewHerbariumRecordTabAction());
        btnOverviewNewHerbRec.setText("Nová herbárová položka");

        btnOverviewNewLitRec.setAction(new ShowNewLiteratureRecordTabAction());
        btnOverviewNewLitRec.setText("Nová literárna položka");

        btnOverviewCopyRec.setAction(new CopyRecordAction());
        btnOverviewCopyRec.setText("Kopírovať");
        btnOverviewCopyRec.setEnabled(false);

        btnOverviewCancel.setAction(new CancelAction());
        btnOverviewCancel.setText("Zatvoriť");

        filterRecordsOverview.setMinimumSize(new java.awt.Dimension(932, 400));
        jScrollPane10.setViewportView(filterRecordsOverview);

        javax.swing.GroupLayout recordsOverviewTabLayout = new javax.swing.GroupLayout(recordsOverviewTab);
        recordsOverviewTab.setLayout(recordsOverviewTabLayout);
        recordsOverviewTabLayout.setHorizontalGroup(
            recordsOverviewTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(recordsOverviewTabLayout.createSequentialGroup()
                .addGroup(recordsOverviewTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(recordsOverviewTabLayout.createSequentialGroup()
                        .addGap(328, 328, 328)
                        .addComponent(btnOverviewNewHerbRec)
                        .addGap(18, 18, 18)
                        .addComponent(btnOverviewNewLitRec, javax.swing.GroupLayout.PREFERRED_SIZE, 149, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(btnOverviewCopyRec)
                        .addGap(18, 18, 18)
                        .addComponent(btnOverviewDeleteRec)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnOverviewCancel))
                    .addGroup(recordsOverviewTabLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jScrollPane10, javax.swing.GroupLayout.DEFAULT_SIZE, 2228, Short.MAX_VALUE)))
                .addContainerGap())
        );
        recordsOverviewTabLayout.setVerticalGroup(
            recordsOverviewTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(recordsOverviewTabLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(recordsOverviewTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnOverviewNewHerbRec)
                    .addComponent(btnOverviewCopyRec)
                    .addComponent(btnOverviewDeleteRec)
                    .addComponent(btnOverviewNewLitRec)
                    .addComponent(btnOverviewCancel))
                .addContainerGap(152, Short.MAX_VALUE))
        );

        jTabbedPaneMain.addTab("Prehľad údajov", recordsOverviewTab);

        recordTab.setBackground(new java.awt.Color(211, 240, 211));

        tabbedPaneInsert.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        tabbedPaneInsert.setMinimumSize(new java.awt.Dimension(120, 86));

        jPanelLitZdroj.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jListLitAuthors.setEnabled(false);

        jLabel64.setText("Autor / Autori publikácie:");

        jLabel65.setForeground(new java.awt.Color(102, 102, 102));
        jLabel65.setText("<html>(knihy, kapitoly, <br/>článku, rukopisu, <br/>internetovej stránky, <br/>atď)");

        jListLitEditors.setEnabled(false);

        jLabel66.setText("Editor / Editori publikácie:");

        jLabel70.setText("Názov článku");

        tfLitPublTitle.setBorder(borderGreenWithPadding);

        jLabel71.setText("Názov článku - preklad");

        jLabel72.setText("<html> Prameň<br /> <span style=\"color: #666666\">(= názov rubriky pri chromozómových počtoch<br /> alebo skopírovaný odkaz na internetovú stránku)</span> </html>");

        tfLitPublYear.setBorder(borderGreenWithPadding);

        jLabel73.setText("Rok vydania");

        jLabel74.setText("Názov časopisu");

        tfLitJournalTitle.setType("sk.sav.bot.dataflos.entity.Casopisy");
        tfLitJournalTitle.setMinimumSize(new java.awt.Dimension(73, 20));
        tfLitJournalTitle.setPreferredSize(new java.awt.Dimension(273, 20));
        tfLitJournalTitle.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                tfLitJournalTitleFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                tfLitJournalTitleFocusLost(evt);
            }
        });
        tfLitJournalTitle.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                tfLitJournalTitleKeyReleased(evt);
            }
        });

        jLabel75.setText("Ročník");

        jLabel76.setText("Číslo");

        jLabel77.setText("Strany");

        tfLitBook.setBorder(borderBlueWithPadding);
        tfLitBook.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tfLitBookMouseClicked(evt);
            }
        });
        tfLitBook.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                tfLitBookFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                tfLitBookFocusLost(evt);
            }
        });
        tfLitBook.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                tfLitBookKeyReleased(evt);
            }
        });

        jLabel78.setText("Názov knihy/zborníka/rukopisu/internet. stránky");

        jLabel79.setText("Vydavateľ");

        jLabel80.setText("Názov kapitoly");

        jLabel81.setText("Kľúčové slová");

        jLabel82.setText("Poznámka k literatúre");

        tfLitRecPage.setBorder(borderGreenWithPadding);

        jLabel83.setText("Strana s údajom");

        cbLitPhoto.setText("fotka");

        cbLitMap.setText("mapa rozšírenia");

        btnLitCopyPrev.setIcon(new javax.swing.ImageIcon(getClass().getResource("/copy-16.png"))); // NOI18N
        btnLitCopyPrev.setText("Skopírovať informácie z naposledy zadaného údaja");
        btnLitCopyPrev.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnLitCopyPrevMouseClicked(evt);
            }
        });

        btnAddLitSourceTop.setAction(new NewLitSourceAction());
        btnAddLitSourceTop.setIcon(new javax.swing.ImageIcon(getClass().getResource("/add-row-16.png"))); // NOI18N
        btnAddLitSourceTop.setText("Vybrať literárny zdroj");

        jLabel62.setText("k údaju je prítomná aj");

        btnLitTabDataErase.setIcon(new javax.swing.ImageIcon(getClass().getResource("/erase-16.png"))); // NOI18N
        btnLitTabDataErase.setText("Vymazať informácie zo záložky");
        btnLitTabDataErase.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnLitTabDataEraseMouseClicked(evt);
            }
        });

        btnSaveLitSourceToTable.setAction(new SaveLitSourceToTableAction());
        btnSaveLitSourceToTable.setIcon(new javax.swing.ImageIcon(getClass().getResource("/save-16.png"))); // NOI18N
        btnSaveLitSourceToTable.setText("Uložiť literárny zdroj");
        btnSaveLitSourceToTable.setEnabled(false);

        jLitTablePanel.setBackground(new java.awt.Color(215, 179, 150));
        jLitTablePanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Prehľad literálnych zdrojov"));
        jLitTablePanel.setPreferredSize(new java.awt.Dimension(924, 235));

        jTableLitSources.setModel(new sk.sav.bot.dataflos.models.LiteraturesTableModel());
        jTableLitSources.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        jTableLitSourcesScrollPane.setViewportView(jTableLitSources);

        btnAddLitSourceDown.setAction(new NewLitSourceAction());
        btnAddLitSourceDown.setIcon(new javax.swing.ImageIcon(getClass().getResource("/add-row-16.png"))); // NOI18N
        btnAddLitSourceDown.setText("Pridať literárny zdroj");

        btnDeleteLitSource.setAction(new DeleteLitSourceAction());
        btnDeleteLitSource.setIcon(new javax.swing.ImageIcon(getClass().getResource("/delete-row-16.png"))); // NOI18N
        btnDeleteLitSource.setText("Odstrániť literárny zdroj");
        btnDeleteLitSource.setEnabled(false);

        javax.swing.GroupLayout jLitTablePanelLayout = new javax.swing.GroupLayout(jLitTablePanel);
        jLitTablePanel.setLayout(jLitTablePanelLayout);
        jLitTablePanelLayout.setHorizontalGroup(
            jLitTablePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jLitTablePanelLayout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addGroup(jLitTablePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jTableLitSourcesScrollPane, javax.swing.GroupLayout.PREFERRED_SIZE, 871, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jLitTablePanelLayout.createSequentialGroup()
                        .addComponent(btnAddLitSourceDown)
                        .addGap(18, 18, 18)
                        .addComponent(btnDeleteLitSource)))
                .addGap(21, 21, 21))
        );
        jLitTablePanelLayout.setVerticalGroup(
            jLitTablePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jLitTablePanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jTableLitSourcesScrollPane, javax.swing.GroupLayout.PREFERRED_SIZE, 103, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jLitTablePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnAddLitSourceDown)
                    .addComponent(btnDeleteLitSource))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        btnCancelAdding.setAction(new CancelLitSourceAddingAction());
        btnCancelAdding.setText("Zrušiť pridávanie");
        btnCancelAdding.setEnabled(false);

        javax.swing.GroupLayout jPanelLitZdrojLayout = new javax.swing.GroupLayout(jPanelLitZdroj);
        jPanelLitZdroj.setLayout(jPanelLitZdrojLayout);
        jPanelLitZdrojLayout.setHorizontalGroup(
            jPanelLitZdrojLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelLitZdrojLayout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addGroup(jPanelLitZdrojLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLitTablePanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanelLitZdrojLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(jSeparator2)
                        .addComponent(jLabel65, javax.swing.GroupLayout.PREFERRED_SIZE, 119, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 738, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGroup(jPanelLitZdrojLayout.createSequentialGroup()
                            .addComponent(jLabel83)
                            .addGap(157, 157, 157)
                            .addComponent(tfLitRecPage, javax.swing.GroupLayout.PREFERRED_SIZE, 74, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(99, 99, 99)
                            .addComponent(jLabel62)
                            .addGap(29, 29, 29)
                            .addComponent(cbLitPhoto)
                            .addGap(18, 18, 18)
                            .addComponent(cbLitMap))
                        .addGroup(jPanelLitZdrojLayout.createSequentialGroup()
                            .addGroup(jPanelLitZdrojLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jLabel73)
                                .addComponent(jLabel81)
                                .addComponent(jLabel82)
                                .addComponent(jLabel74))
                            .addGap(70, 70, 70)
                            .addGroup(jPanelLitZdrojLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(jPanelLitZdrojLayout.createSequentialGroup()
                                    .addGap(9, 9, 9)
                                    .addComponent(jLabel75)
                                    .addGap(18, 18, 18)
                                    .addGroup(jPanelLitZdrojLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addGroup(jPanelLitZdrojLayout.createSequentialGroup()
                                            .addComponent(tfLitYear, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addGap(69, 69, 69)
                                            .addComponent(jLabel76)
                                            .addGap(18, 18, 18)
                                            .addComponent(tfLitIssue, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                            .addComponent(jLabel77)
                                            .addGap(18, 18, 18)
                                            .addComponent(tfLitPages, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addComponent(tfLitJournalTitle, javax.swing.GroupLayout.PREFERRED_SIZE, 539, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(tfLitPublTitle, javax.swing.GroupLayout.PREFERRED_SIZE, 539, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(tfLitPublTitleTransl, javax.swing.GroupLayout.PREFERRED_SIZE, 539, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(tfLitSource, javax.swing.GroupLayout.PREFERRED_SIZE, 539, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addGroup(jPanelLitZdrojLayout.createSequentialGroup()
                                    .addGap(61, 61, 61)
                                    .addGroup(jPanelLitZdrojLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(tfLitPublYear, javax.swing.GroupLayout.PREFERRED_SIZE, 66, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jListLitKeywords, javax.swing.GroupLayout.PREFERRED_SIZE, 557, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(tfLitNotes, javax.swing.GroupLayout.PREFERRED_SIZE, 538, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                        .addGroup(jPanelLitZdrojLayout.createSequentialGroup()
                            .addGroup(jPanelLitZdrojLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jLabel79)
                                .addComponent(jLabel80)
                                .addComponent(jLabel78, javax.swing.GroupLayout.Alignment.TRAILING))
                            .addGroup(jPanelLitZdrojLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addComponent(tfLitPublisher)
                                .addComponent(tfLitChapter)
                                .addComponent(tfLitBook, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 539, Short.MAX_VALUE)))
                        .addComponent(jLabel70)
                        .addComponent(jLabel71)
                        .addComponent(jLabel72, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jSeparator3)
                        .addGroup(jPanelLitZdrojLayout.createSequentialGroup()
                            .addComponent(jLabel64)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                            .addComponent(jListLitAuthors, javax.swing.GroupLayout.PREFERRED_SIZE, 240, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(25, 25, 25)
                            .addComponent(jLabel66)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                            .addComponent(jListLitEditors, javax.swing.GroupLayout.PREFERRED_SIZE, 240, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(jPanelLitZdrojLayout.createSequentialGroup()
                            .addComponent(btnLitCopyPrev)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(btnAddLitSourceTop)
                            .addGap(37, 37, 37)
                            .addComponent(btnLitTabDataErase)))
                    .addGroup(jPanelLitZdrojLayout.createSequentialGroup()
                        .addComponent(btnSaveLitSourceToTable)
                        .addGap(18, 18, 18)
                        .addComponent(btnCancelAdding)))
                .addGap(50, 50, 50))
        );
        jPanelLitZdrojLayout.setVerticalGroup(
            jPanelLitZdrojLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelLitZdrojLayout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addGroup(jPanelLitZdrojLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnLitCopyPrev)
                    .addComponent(btnAddLitSourceTop)
                    .addComponent(btnLitTabDataErase, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(12, 12, 12)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanelLitZdrojLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jListLitAuthors, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jListLitEditors, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel66)
                    .addGroup(jPanelLitZdrojLayout.createSequentialGroup()
                        .addComponent(jLabel64)
                        .addGap(3, 3, 3)
                        .addComponent(jLabel65, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(18, 18, 18)
                .addGroup(jPanelLitZdrojLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(tfLitBook, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel78))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanelLitZdrojLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(tfLitPublisher, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel79))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanelLitZdrojLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(tfLitChapter, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel80))
                .addGap(18, 18, 18)
                .addGroup(jPanelLitZdrojLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel74)
                    .addComponent(tfLitJournalTitle, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanelLitZdrojLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(tfLitYear, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel76)
                    .addComponent(tfLitIssue, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(tfLitPages, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel77)
                    .addComponent(jLabel75))
                .addGap(18, 18, 18)
                .addGroup(jPanelLitZdrojLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(tfLitPublTitle, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel70))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanelLitZdrojLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(tfLitPublTitleTransl, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel71))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanelLitZdrojLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(tfLitSource, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel72, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanelLitZdrojLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(tfLitPublYear, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel73))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanelLitZdrojLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jListLitKeywords, javax.swing.GroupLayout.PREFERRED_SIZE, 69, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel81))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanelLitZdrojLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(tfLitNotes, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel82))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanelLitZdrojLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(tfLitRecPage, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel83)
                    .addComponent(cbLitPhoto)
                    .addComponent(cbLitMap)
                    .addComponent(jLabel62))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jSeparator3, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanelLitZdrojLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnSaveLitSourceToTable)
                    .addComponent(btnCancelAdding))
                .addGap(40, 40, 40)
                .addComponent(jLitTablePanel, javax.swing.GroupLayout.PREFERRED_SIZE, 184, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0))
        );

        jListLitAuthors.setVisibleRowCount(8);

        jListLitAuthors.getMainList().addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                jListPeopleFocusGained(jListLitAuthors);
            }
        });
        jListLitEditors.getMainList().addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                jListPeopleFocusGained(jListLitEditors);
            }
        });
        tfLitJournalTitle.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                tfMoznostiFocusGained(tfLitJournalTitle, "Casopisy", "meno");
            }
        });

        jScrollPaneLitZdroj.setViewportView(jPanelLitZdroj);

        tabbedPaneInsert.addTab("Literárny zdroj", jScrollPaneLitZdroj);

        jPanel4.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        tfUrcStdMeno.setMinimumSize(new java.awt.Dimension(73, 20));
        tfUrcStdMeno.setPreferredSize(new java.awt.Dimension(273, 20));

        jLabel2.setText("Neštandardizovaný názov taxónu podľa zberateľa");

        jLabel3.setText("Štandardizované znenie mena taxónu (z akuálneho zoznamu mien)");

        jLabel4.setText("Akceptované meno taxónu");

        jLabel5.setText("Poznámka k názvu taxónu");

        tfUrcPoznamka.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                tfUrcPoznamkaFocusGained(evt);
            }
        });

        jLabel7.setText("Dátum určenia");

        tfUrcNazovScheda.setMaximumSize(new java.awt.Dimension(2147483, 2147483));
        tfUrcNazovScheda.setName(""); // NOI18N
        tfUrcNazovScheda.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                tfUrcNazovSchedaFocusGained(evt);
            }
        });

        jLabel8.setText("Dátum určenia slovom");

        tfUrcAccName.setEditable(false);
        tfUrcAccName.setBackground(new java.awt.Color(250, 250, 250));
        tfUrcAccName.setDisabledTextColor(new java.awt.Color(73, 73, 73));
        tfUrcAccName.setBorder(borderGreenWithPadding);
        tfUrcAccName.setEnabled(false);

        tfDatumUrcSlovom.setColumns(35);

        jLabel88.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel88.setText("(pôvodné znenie)");

        btnIdentCopyPrev.setIcon(new javax.swing.ImageIcon(getClass().getResource("/copy-16.png"))); // NOI18N
        btnIdentCopyPrev.setText("Skopírovať informácie z naposledy zadaného údaja");
        btnIdentCopyPrev.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnIdentCopyPrevMouseClicked(evt);
            }
        });

        jLabel6.setText("Autori určenia");

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel6)
                    .addComponent(jListIdentificators, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 0, Short.MAX_VALUE))
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addGap(1, 1, 1)
                .addComponent(jLabel6)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jListIdentificators, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(1, 1, 1))
        );

        jListIdentificators.getMainList().addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                jListPeopleFocusGained(jListIdentificators);
            }
        });

        tfDatumUrcD.setColumns(2);
        tfDatumUrcD.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        tfDatumUrcD.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(134, 209, 59), 2));
        tfDatumUrcD.setInputVerifier(new DateDayVerifier());
        tfDatumUrcD.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                tfDatumUrcDFocusGained(evt);
            }
        });

        tfDatumUrcM.setColumns(2);
        tfDatumUrcM.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        tfDatumUrcM.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(134, 209, 59), 2));
        tfDatumUrcM.setInputVerifier(new DateMonthVerifier());
        tfDatumUrcM.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                tfDatumUrcMFocusGained(evt);
            }
        });

        tfDatumUrcR.setColumns(4);
        tfDatumUrcR.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        tfDatumUrcR.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(134, 209, 59), 2));
        tfDatumUrcR.setInputVerifier(new DateYearVerifier());

        jLabel40.setText("(dd mm rrrr)");

        btnIdentTabDataErase.setIcon(new javax.swing.ImageIcon(getClass().getResource("/erase-16.png"))); // NOI18N
        btnIdentTabDataErase.setText("Vymazať informácie zo záložky");
        btnIdentTabDataErase.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnIdentTabDataEraseMouseClicked(evt);
            }
        });

        tfUrcPochybnost.setEnabled(false);

        tfUrcOhrozenost.setEnabled(false);

        cbUrcOchrana.setText("Zákonná ochrana taxónu");
        cbUrcOchrana.setEnabled(false);

        tfUrcEndemizmus.setEnabled(false);

        tfUrcPovodnost.setEnabled(false);

        tfUrcSvkNazov.setEnabled(false);

        jLabel69.setText("Pochybnosť taxónu");

        jLabel84.setText("Ohrozenosť taxónu");

        jLabel86.setText("Endemizmus taxónu");

        jLabel87.setText("Pôvodnosť taxónu");

        jLabel93.setText("Slovenský názov taxónu");

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(filler2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGap(16, 16, 16)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addComponent(btnIdentCopyPrev)
                                .addGap(76, 76, 76)
                                .addComponent(btnIdentTabDataErase))
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel8)
                                    .addComponent(jLabel7))
                                .addGap(56, 56, 56)
                                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel4Layout.createSequentialGroup()
                                        .addComponent(tfDatumUrcD, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(18, 18, 18)
                                        .addComponent(tfDatumUrcM, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(18, 18, 18)
                                        .addComponent(tfDatumUrcR, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(18, 18, 18)
                                        .addComponent(jLabel40))
                                    .addComponent(tfDatumUrcSlovom)))
                            .addComponent(jLabel5)
                            .addComponent(jLabel3)
                            .addComponent(jLabel4)
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addComponent(jLabel2)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel88))
                            .addComponent(tfUrcStdMeno, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(tfUrcPoznamka)
                            .addComponent(jPanel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(tfUrcAccName)
                            .addComponent(tfUrcNazovScheda, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jLabel93)
                                    .addGroup(jPanel4Layout.createSequentialGroup()
                                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                            .addComponent(tfUrcPochybnost)
                                            .addComponent(jLabel69, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addGroup(jPanel4Layout.createSequentialGroup()
                                                .addGap(30, 30, 30)
                                                .addComponent(jLabel84))
                                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                                                .addGap(31, 31, 31)
                                                .addComponent(tfUrcOhrozenost, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE))))
                                    .addComponent(tfUrcSvkNazov))
                                .addGap(30, 30, 30)
                                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(cbUrcOchrana)
                                    .addGroup(jPanel4Layout.createSequentialGroup()
                                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                            .addComponent(jLabel86, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                            .addComponent(tfUrcEndemizmus))
                                        .addGap(30, 30, 30)
                                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                            .addComponent(jLabel87, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                            .addComponent(tfUrcPovodnost)))))
                            .addComponent(jSeparator4)
                            .addComponent(jSeparator5))))
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(btnIdentTabDataErase, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnIdentCopyPrev, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(18, 18, 18)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(jLabel88))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(tfUrcNazovScheda, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(tfUrcStdMeno, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jSeparator5, javax.swing.GroupLayout.PREFERRED_SIZE, 5, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(jLabel4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(tfUrcAccName, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel69)
                    .addComponent(jLabel84)
                    .addComponent(jLabel86)
                    .addComponent(jLabel87))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(tfUrcPochybnost, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(tfUrcOhrozenost, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(tfUrcEndemizmus, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(tfUrcPovodnost, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel93)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(tfUrcSvkNazov, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cbUrcOchrana))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator4, javax.swing.GroupLayout.PREFERRED_SIZE, 11, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel5)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(tfUrcPoznamka, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(tfDatumUrcSlovom, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel7)
                            .addComponent(tfDatumUrcD, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(tfDatumUrcM, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(tfDatumUrcR, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel40))
                        .addGap(19, 19, 19)
                        .addComponent(jLabel8)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(filler2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        tfUrcStdMeno.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                tfMoznostiFocusGained(tfUrcStdMeno, "ListOfSpecies", "meno");
            }
        });
        tfDatumUrcSlovom.setDocument(new MaxLengthTextDocument(25));
        tfDatumUrcD.setDocument(new MaxLengthTextDocument(2));
        tfDatumUrcM.setDocument(new MaxLengthTextDocument(2));
        tfDatumUrcR.setDocument(new MaxLengthTextDocument(4));

        javax.swing.GroupLayout jPanelUrcenieLayout = new javax.swing.GroupLayout(jPanelUrcenie);
        jPanelUrcenie.setLayout(jPanelUrcenieLayout);
        jPanelUrcenieLayout.setHorizontalGroup(
            jPanelUrcenieLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanelUrcenieLayout.setVerticalGroup(
            jPanelUrcenieLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        jScrollPaneUrcenie.setViewportView(jPanelUrcenie);

        tabbedPaneInsert.addTab("Určenie", jScrollPaneUrcenie);

        jPanelLokalita.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jLabel9.setText("Základná údajová jednotka (Brummitt 4)");

        jLabel10.setText("Flora");

        tfLocFlora.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                tfLocFloraFocusLost(evt);
            }
        });

        jLabel11.setText("Vyšší administratívny celok");

        jLabel12.setText("Nižší administratívny celok");

        jLabel13.setText("Fytogeografický (pod)okres Slovenska");

        jLabel14.setText("Vyšší geografický celok");

        jLabel15.setText("Nižší geografický celok");

        jLabel16.setText("Najbližšia obec");

        jLabel18.setText("Opis lokality (Pôvodné znenie)");

        jScrollPane3.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(134, 209, 59), 2));
        jScrollPane3.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        taLocDescr.setColumns(20);
        taLocDescr.setLineWrap(true);
        taLocDescr.setRows(5);
        taLocDescr.setWrapStyleWord(true);
        jScrollPane3.setViewportView(taLocDescr);

        jLabel19.setText("Nadmorská výška od");

        tfLocAltFrom.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        tfLocAltFrom.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(134, 209, 59), 2));

        jLabel20.setText("do");

        tfLocAltTo.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        jLabel21.setText("m n.m.");

        chbLocAltCca.setText("cca");

        jLabel22.setText("Zemepisná šírka");

        tfLocLatDeg.setColumns(2);
        tfLocLatDeg.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        tfLocLatDeg.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(134, 209, 59), 2));

        jLabel23.setText("°");

        tfLocLatMin.setColumns(2);
        tfLocLatMin.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        tfLocLatMin.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(134, 209, 59), 2));

        jLabel24.setText("'");

        tfLocLatSec.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        tfLocLatSec.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(134, 209, 59), 2));

        jLabel25.setText("''");

        rbLocNorth.setSelected(true);
        rbLocNorth.setText("S");

        rbLocSouth.setText("J");

        jLabel26.setText("Zemepisná dĺžka");

        tfLocLngDeg.setColumns(2);
        tfLocLngDeg.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        tfLocLngDeg.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(134, 209, 59), 2));

        jLabel27.setText("°");

        tfLocLngMin.setColumns(2);
        tfLocLngMin.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        tfLocLngMin.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(134, 209, 59), 2));

        jLabel28.setText("'");

        tfLocLngSec.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        tfLocLngSec.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(134, 209, 59), 2));

        jLabel29.setText("''");

        rbLocWest.setText("Z");

        rbLocEast.setSelected(true);
        rbLocEast.setText("V");

        jLabel30.setText("Typ súradnicovej sústavy");

        cbLocCoordType.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "GPS", "MAPA", "UTM" }));

        jLabel31.setText("Kvadranty");

        jLabel32.setText("Pôda");

        jLabel33.setText("Substrát");

        jLabel34.setText("Hostiteľ");

        jLabel35.setText("Výška zberu (epifitické org.)");

        jLabel36.setText("<html>Kópia schedy<br\\>(Pôvodné znenie)");

        taLocScheda.setColumns(20);
        taLocScheda.setRows(2);
        jScrollPane5.setViewportView(taLocScheda);

        jLabel37.setText("Poznámka k lokalite");

        taLocNotes.setColumns(20);
        taLocNotes.setRows(3);
        jScrollPane6.setViewportView(taLocNotes);

        jLabel38.setText("Autori zberu");

        jLabel39.setText("Dátum zberu");

        tfDatumZbD.setColumns(2);
        tfDatumZbD.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        tfDatumZbD.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(134, 209, 59), 2));
        tfDatumZbD.setInputVerifier(new DateDayVerifier());

        tfDatumZbM.setColumns(2);
        tfDatumZbM.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        tfDatumZbM.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(134, 209, 59), 2));
        tfDatumZbM.setInputVerifier(new DateMonthVerifier());

        tfDatumZbR.setColumns(4);
        tfDatumZbR.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        tfDatumZbR.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(134, 209, 59), 2));
        tfDatumZbR.setInputVerifier(new DateYearVerifier());

        jLabel41.setText("(dd mm rrrr)");

        jLabel42.setText("Dátum zberu slovom");

        tfDatumZbSlovom.setColumns(25);

        jLabel61.setText("Chránené územie");

        btnLocCopyPrev.setIcon(new javax.swing.ImageIcon(getClass().getResource("/copy-16.png"))); // NOI18N
        btnLocCopyPrev.setText("Skopírovať informácie z naposledy zadaného údaja");
        btnLocCopyPrev.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnLocCopyPrevMouseClicked(evt);
            }
        });

        jLabel89.setText("(Pôvodné znenie)");

        jLabel90.setText("(Pôvodné znenie)");

        jLabel91.setText("(Pôvodné znenie)");

        jLabel92.setText("(Pôvodné znenie)");

        btnLocTabDataErase.setIcon(new javax.swing.ImageIcon(getClass().getResource("/erase-16.png"))); // NOI18N
        btnLocTabDataErase.setText("Vymazať informácie zo záložky");
        btnLocTabDataErase.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnLocTabDataEraseMouseClicked(evt);
            }
        });

        jListFyto.setVisibleRowCount(2);
        jListFyto.setListPrefferedSize(new Dimension(259, 73));

        javax.swing.GroupLayout jPanelLokalitaLayout = new javax.swing.GroupLayout(jPanelLokalita);
        jPanelLokalita.setLayout(jPanelLokalitaLayout);
        jPanelLokalitaLayout.setHorizontalGroup(
            jPanelLokalitaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelLokalitaLayout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addGroup(jPanelLokalitaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanelLokalitaLayout.createSequentialGroup()
                        .addGap(169, 169, 169)
                        .addGroup(jPanelLokalitaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanelLokalitaLayout.createSequentialGroup()
                                .addComponent(tfLocAltFrom, javax.swing.GroupLayout.PREFERRED_SIZE, 57, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jLabel20)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(tfLocAltTo, javax.swing.GroupLayout.PREFERRED_SIZE, 53, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel21)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(chbLocAltCca))
                            .addGroup(jPanelLokalitaLayout.createSequentialGroup()
                                .addGroup(jPanelLokalitaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanelLokalitaLayout.createSequentialGroup()
                                        .addComponent(tfLocLngDeg, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jLabel27, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanelLokalitaLayout.createSequentialGroup()
                                        .addComponent(tfLocLatDeg, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jLabel23)))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanelLokalitaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addGroup(jPanelLokalitaLayout.createSequentialGroup()
                                        .addComponent(tfLocLatMin, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jLabel24))
                                    .addGroup(jPanelLokalitaLayout.createSequentialGroup()
                                        .addComponent(tfLocLngMin, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jLabel28, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanelLokalitaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(tfLocLatSec, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(tfLocLngSec, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanelLokalitaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanelLokalitaLayout.createSequentialGroup()
                                        .addComponent(jLabel25, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(rbLocNorth)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(rbLocSouth))
                                    .addGroup(jPanelLokalitaLayout.createSequentialGroup()
                                        .addComponent(jLabel29, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(rbLocWest)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(rbLocEast)))))
                        .addGap(18, 18, 18)
                        .addGroup(jPanelLokalitaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(jPanelLokalitaLayout.createSequentialGroup()
                                .addComponent(jLabel35)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel92)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(tfLocAltEpif, javax.swing.GroupLayout.PREFERRED_SIZE, 268, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanelLokalitaLayout.createSequentialGroup()
                                .addGroup(jPanelLokalitaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanelLokalitaLayout.createSequentialGroup()
                                        .addComponent(jLabel33)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jLabel90))
                                    .addGroup(jPanelLokalitaLayout.createSequentialGroup()
                                        .addComponent(jLabel34)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(jLabel91))
                                    .addGroup(jPanelLokalitaLayout.createSequentialGroup()
                                        .addComponent(jLabel32)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jLabel89)))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanelLokalitaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(tfLocSoil, javax.swing.GroupLayout.PREFERRED_SIZE, 386, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(tfLocHost, javax.swing.GroupLayout.PREFERRED_SIZE, 386, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(tfLocSubstrate, javax.swing.GroupLayout.PREFERRED_SIZE, 387, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                    .addGroup(jPanelLokalitaLayout.createSequentialGroup()
                        .addGroup(jPanelLokalitaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel36, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel37))
                        .addGap(23, 23, 23)
                        .addGroup(jPanelLokalitaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jScrollPane6, javax.swing.GroupLayout.DEFAULT_SIZE, 832, Short.MAX_VALUE)
                            .addComponent(jScrollPane5)))
                    .addGroup(jPanelLokalitaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(jLabel19)
                        .addComponent(jLabel22)
                        .addComponent(jLabel26)
                        .addGroup(jPanelLokalitaLayout.createSequentialGroup()
                            .addComponent(jLabel30)
                            .addGap(46, 46, 46)
                            .addComponent(cbLocCoordType, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(jPanelLokalitaLayout.createSequentialGroup()
                            .addGap(279, 279, 279)
                            .addComponent(filler1, javax.swing.GroupLayout.PREFERRED_SIZE, 114, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addComponent(jLabel13)
                        .addGroup(jPanelLokalitaLayout.createSequentialGroup()
                            .addGroup(jPanelLokalitaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jLabel38)
                                .addComponent(jListCollectors, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(jPanelLokalitaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jListQuadrants, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel31)))
                        .addGroup(jPanelLokalitaLayout.createSequentialGroup()
                            .addGroup(jPanelLokalitaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jLabel42)
                                .addComponent(jLabel39))
                            .addGap(18, 18, 18)
                            .addGroup(jPanelLokalitaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(jPanelLokalitaLayout.createSequentialGroup()
                                    .addComponent(tfDatumZbD, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(14, 14, 14)
                                    .addComponent(tfDatumZbM, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                    .addComponent(tfDatumZbR, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(jLabel41))
                                .addComponent(tfDatumZbSlovom, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGroup(jPanelLokalitaLayout.createSequentialGroup()
                            .addGroup(jPanelLokalitaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jLabel10)
                                .addComponent(btnLocCopyPrev)
                                .addComponent(jLabel16)
                                .addComponent(tfLocVill, javax.swing.GroupLayout.PREFERRED_SIZE, 259, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(tfLocFlora, javax.swing.GroupLayout.PREFERRED_SIZE, 259, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jListFyto, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 43, Short.MAX_VALUE)
                            .addGroup(jPanelLokalitaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 722, Short.MAX_VALUE)
                                .addGroup(jPanelLokalitaLayout.createSequentialGroup()
                                    .addGroup(jPanelLokalitaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(btnLocTabDataErase)
                                        .addComponent(jLabel18)
                                        .addGroup(jPanelLokalitaLayout.createSequentialGroup()
                                            .addGroup(jPanelLokalitaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addComponent(jLabel14)
                                                .addComponent(tfLocVgc, javax.swing.GroupLayout.PREFERRED_SIZE, 259, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addComponent(jLabel9)
                                                .addComponent(tfLocBrumFour, javax.swing.GroupLayout.PREFERRED_SIZE, 259, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addComponent(jLabel11)
                                                .addComponent(tfLocVac, javax.swing.GroupLayout.PREFERRED_SIZE, 259, javax.swing.GroupLayout.PREFERRED_SIZE))
                                            .addGap(93, 93, 93)
                                            .addGroup(jPanelLokalitaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addComponent(jLabel12)
                                                .addComponent(tfLocNac, javax.swing.GroupLayout.PREFERRED_SIZE, 259, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addComponent(jLabel61)
                                                .addComponent(tfLocProtArea, javax.swing.GroupLayout.PREFERRED_SIZE, 259, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addComponent(tfLocNgc, javax.swing.GroupLayout.PREFERRED_SIZE, 259, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addComponent(jLabel15))))
                                    .addContainerGap()))))))
        );
        jPanelLokalitaLayout.setVerticalGroup(
            jPanelLokalitaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelLokalitaLayout.createSequentialGroup()
                .addComponent(filler1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(10, 10, 10)
                .addGroup(jPanelLokalitaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(btnLocCopyPrev, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnLocTabDataErase, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(18, 18, 18)
                .addGroup(jPanelLokalitaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanelLokalitaLayout.createSequentialGroup()
                        .addGroup(jPanelLokalitaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel10)
                            .addComponent(jLabel9))
                        .addGap(1, 1, 1)
                        .addGroup(jPanelLokalitaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(tfLocFlora, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(tfLocBrumFour, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanelLokalitaLayout.createSequentialGroup()
                        .addComponent(jLabel61)
                        .addGap(1, 1, 1)
                        .addComponent(tfLocProtArea, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanelLokalitaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel13)
                    .addGroup(jPanelLokalitaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel14)
                        .addComponent(jLabel15)))
                .addGap(1, 1, 1)
                .addGroup(jPanelLokalitaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanelLokalitaLayout.createSequentialGroup()
                        .addGroup(jPanelLokalitaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(jPanelLokalitaLayout.createSequentialGroup()
                                .addComponent(tfLocVgc, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel11)
                                .addGap(1, 1, 1)
                                .addComponent(tfLocVac, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jListFyto, javax.swing.GroupLayout.PREFERRED_SIZE, 75, Short.MAX_VALUE))
                        .addGap(10, 10, 10)
                        .addGroup(jPanelLokalitaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel16, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel18))
                        .addGap(1, 1, 1)
                        .addGroup(jPanelLokalitaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(tfLocVill, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 63, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanelLokalitaLayout.createSequentialGroup()
                        .addComponent(tfLocNgc, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel12)
                        .addGap(1, 1, 1)
                        .addComponent(tfLocNac, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 90, Short.MAX_VALUE)
                .addGroup(jPanelLokalitaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(tfLocSoil, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanelLokalitaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel19)
                        .addComponent(tfLocAltFrom, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel20)
                        .addComponent(tfLocAltTo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel21)
                        .addComponent(chbLocAltCca)
                        .addComponent(jLabel32)
                        .addComponent(jLabel89)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanelLokalitaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel22)
                    .addComponent(tfLocLatDeg, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel23)
                    .addComponent(tfLocLatMin, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel24)
                    .addComponent(tfLocLatSec, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel25)
                    .addComponent(rbLocNorth)
                    .addComponent(rbLocSouth)
                    .addComponent(jLabel33)
                    .addComponent(tfLocSubstrate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel90))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanelLokalitaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel26)
                    .addComponent(tfLocLngDeg, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel27)
                    .addComponent(tfLocLngMin, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel28)
                    .addComponent(tfLocLngSec, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel29)
                    .addComponent(rbLocWest)
                    .addComponent(rbLocEast)
                    .addComponent(jLabel34)
                    .addComponent(tfLocHost, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel91))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanelLokalitaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel30)
                    .addComponent(cbLocCoordType, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel35)
                    .addComponent(tfLocAltEpif, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel92, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanelLokalitaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel38)
                    .addComponent(jLabel31))
                .addGap(4, 4, 4)
                .addGroup(jPanelLokalitaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jListCollectors, javax.swing.GroupLayout.PREFERRED_SIZE, 62, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jListQuadrants, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 62, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanelLokalitaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel39)
                    .addComponent(tfDatumZbD, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(tfDatumZbM, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(tfDatumZbR, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel41))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanelLokalitaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel42)
                    .addComponent(tfDatumZbSlovom, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanelLokalitaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 57, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel36, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanelLokalitaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel37))
                .addContainerGap())
        );

        tfLocBrumFour.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                tfMoznostiFocusGained(tfLocBrumFour, "Brumit4", "meno");
            }
        });
        tfLocFlora.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                tfMoznostiFocusGained(tfLocFlora, "Flora", "meno");
            }
        });
        tfLocVac.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                tfMoznostiFocusGained(tfLocVac, "Vac", "meno");
            }
        });
        tfLocNac.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                tfMoznostiFocusGained(tfLocNac, "Nac", "meno");
            }
        });
        tfLocVgc.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                tfMoznostiFocusGained(tfLocVgc, "Vgc", "meno");
            }
        });
        tfLocNgc.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                tfMoznostiFocusGained(tfLocNgc, "Ngc", "meno");
            }
        });
        tfLocVill.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                tfMoznostiFocusGained(tfLocVill, "Obec", "meno");
            }
        });
        jListCollectors.setVisibleRowCount(2);
        jListCollectors.setListPrefferedSize(new Dimension(380, 60));
        jListCollectors.getMainList().addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                jListPeopleFocusGained(jListCollectors);
            }
        });
        tfDatumZbD.setDocument(new MaxLengthTextDocument(2));
        tfDatumZbM.setDocument(new MaxLengthTextDocument(2));
        tfDatumZbR.setDocument(new MaxLengthTextDocument(4));
        tfDatumZbSlovom.setDocument(new MaxLengthTextDocument(25));
        tfLocProtArea.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                tfMoznostiFocusGained(tfLocProtArea, "Chu", "meno");
            }
        });
        jListQuadrants.setVisibleRowCount(2);
        jListQuadrants.setListPrefferedSize(new Dimension(380, 60));
        jListQuadrants.getMainList().addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                jListQuadrantsFocusGained(jListQuadrants);
            }
        });
        jListFyto.getMainList().addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                jListFytoFocusGained(jListFyto);
            }
        });

        jScrollPaneLokalita.setViewportView(jPanelLokalita);

        tabbedPaneInsert.addTab("Lokalita", jScrollPaneLokalita);

        jPanelRevizie.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jLabel43.setText("Revidované znenie mena (z revízneho lístka aj s chybami)");

        jLabel44.setText("Štandardizované znenie revidovaného mena");

        tfRevStdMeno.setMinimumSize(new java.awt.Dimension(73, 20));
        tfRevStdMeno.setPreferredSize(new java.awt.Dimension(273, 20));

        jLabel45.setText("Akceptované meno");

        tfRevAccName.setEditable(false);
        tfRevAccName.setBackground(new java.awt.Color(250, 250, 250));
        tfRevAccName.setEnabled(false);

        jLabel46.setText("Poznámka k revidovanému menu");

        jLabel47.setText("Autori revízie");

        jLabel48.setText("Dátum revízie");

        jLabel49.setText("Dátum revízie slovom");

        tfDatumRevSlovom.setColumns(25);

        tfDatumRevD.setColumns(2);
        tfDatumRevD.setInputVerifier(new DateDayVerifier());

        tfDatumRevM.setColumns(2);
        tfDatumRevM.setInputVerifier(new DateMonthVerifier());

        tfDatumRevR.setColumns(4);
        tfDatumRevR.setInputVerifier(new DateYearVerifier());

        jLabel50.setText("(dd-mm-rrrr)");

        jButtonAddRev.setAction(new AddRevisionAction());
        jButtonAddRev.setIcon(new javax.swing.ImageIcon(getClass().getResource("/save-16.png"))); // NOI18N
        jButtonAddRev.setText("Uložiť revíziu");
        jButtonAddRev.setEnabled(false);

        jButtonCancelRev.setAction(new CancelRevisionAction());
        jButtonCancelRev.setText("Zrušiť pridávanie");
        jButtonCancelRev.setEnabled(false);

        btnRevCopyPrev.setIcon(new javax.swing.ImageIcon(getClass().getResource("/copy-16.png"))); // NOI18N
        btnRevCopyPrev.setText("Skopírovať informácie z naposledy zadaného údaja");
        btnRevCopyPrev.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnRevCopyPrevMouseClicked(evt);
            }
        });

        btnRevTabDataErase.setIcon(new javax.swing.ImageIcon(getClass().getResource("/erase-16.png"))); // NOI18N
        btnRevTabDataErase.setText("Vymazať informácie zo záložky");
        btnRevTabDataErase.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnRevTabDataEraseMouseClicked(evt);
            }
        });

        jPanel9.setBackground(new java.awt.Color(215, 179, 150));
        jPanel9.setBorder(javax.swing.BorderFactory.createTitledBorder("Prehľad revízii"));

        jTableRevisions.setModel(new sk.sav.bot.dataflos.models.RevisionsTableModel());
        jTableRevisions.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        jScrollPane7.setViewportView(jTableRevisions);

        btnAddRevDown.setAction(new NewRevisionAction());
        btnAddRevDown.setIcon(new javax.swing.ImageIcon(getClass().getResource("/add-row-16.png"))); // NOI18N
        btnAddRevDown.setText("Pridať revíziu");

        btnDelRev.setAction(new DeleteRevisionAction());
        btnDelRev.setIcon(new javax.swing.ImageIcon(getClass().getResource("/delete-row-16.png"))); // NOI18N
        btnDelRev.setText("Odstrániť revíziu");
        btnDelRev.setEnabled(false);

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane7, javax.swing.GroupLayout.PREFERRED_SIZE, 870, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel9Layout.createSequentialGroup()
                        .addComponent(btnAddRevDown)
                        .addGap(18, 18, 18)
                        .addComponent(btnDelRev)))
                .addGap(21, 21, 21))
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane7, javax.swing.GroupLayout.PREFERRED_SIZE, 155, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnAddRevDown)
                    .addComponent(btnDelRev))
                .addContainerGap())
        );

        btnAddRevTop.setAction(new NewRevisionAction());
        btnAddRevTop.setIcon(new javax.swing.ImageIcon(getClass().getResource("/add-row-16.png"))); // NOI18N
        btnAddRevTop.setText("Pridať revíziu");

        tfRevPochybnost.setEnabled(false);

        jLabel94.setText("Pochybnosť taxónu");

        jLabel95.setText("Ohrozenosť taxónu");

        tfRevOhrozenost.setEnabled(false);

        jLabel96.setText("Endemizmus taxónu");

        tfRevEndemizmus.setEnabled(false);

        jLabel97.setText("Pôvodnosť taxónu");

        tfRevPovodnost.setEnabled(false);

        jLabel98.setText("Slovenský názov taxónu");

        tfRevSvkNazov.setEnabled(false);

        cbRevOchrana.setText("Zákonná ochrana taxónu");
        cbRevOchrana.setEnabled(false);

        javax.swing.GroupLayout jPanelRevizieLayout = new javax.swing.GroupLayout(jPanelRevizie);
        jPanelRevizie.setLayout(jPanelRevizieLayout);
        jPanelRevizieLayout.setHorizontalGroup(
            jPanelRevizieLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelRevizieLayout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addGroup(jPanelRevizieLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanelRevizieLayout.createSequentialGroup()
                        .addGroup(jPanelRevizieLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel98)
                            .addGroup(jPanelRevizieLayout.createSequentialGroup()
                                .addGroup(jPanelRevizieLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addComponent(tfRevPochybnost)
                                    .addComponent(jLabel94))
                                .addGroup(jPanelRevizieLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanelRevizieLayout.createSequentialGroup()
                                        .addGap(30, 30, 30)
                                        .addComponent(jLabel95))
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanelRevizieLayout.createSequentialGroup()
                                        .addGap(31, 31, 31)
                                        .addComponent(tfRevOhrozenost, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE))))
                            .addComponent(tfRevSvkNazov, javax.swing.GroupLayout.PREFERRED_SIZE, 218, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(30, 30, 30)
                        .addGroup(jPanelRevizieLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(cbRevOchrana)
                            .addGroup(jPanelRevizieLayout.createSequentialGroup()
                                .addGroup(jPanelRevizieLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jLabel96, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(tfRevEndemizmus, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(30, 30, 30)
                                .addGroup(jPanelRevizieLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jLabel97, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(tfRevPovodnost, javax.swing.GroupLayout.PREFERRED_SIZE, 89, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                    .addComponent(jPanel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanelRevizieLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(jSeparator7, javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jSeparator6, javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanelRevizieLayout.createSequentialGroup()
                            .addComponent(btnRevCopyPrev)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(btnAddRevTop)
                            .addGap(37, 37, 37)
                            .addComponent(btnRevTabDataErase))
                        .addComponent(jListRevisors, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanelRevizieLayout.createSequentialGroup()
                            .addGroup(jPanelRevizieLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jLabel49)
                                .addComponent(jLabel48))
                            .addGap(44, 44, 44)
                            .addGroup(jPanelRevizieLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(jPanelRevizieLayout.createSequentialGroup()
                                    .addComponent(tfDatumRevD, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(tfDatumRevM, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(tfDatumRevR, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(jLabel50))
                                .addComponent(tfDatumRevSlovom)))
                        .addComponent(tfRevAccName, javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(tfRevNazovListok, javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(tfRevStdMeno, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(tfRevPoznamka, javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jLabel46, javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jLabel44, javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jLabel45, javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jLabel47, javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jLabel43, javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanelRevizieLayout.createSequentialGroup()
                            .addComponent(jButtonAddRev)
                            .addGap(18, 18, 18)
                            .addComponent(jButtonCancelRev))))
                .addGap(50, 50, 50))
        );
        jPanelRevizieLayout.setVerticalGroup(
            jPanelRevizieLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelRevizieLayout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addGroup(jPanelRevizieLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(btnRevCopyPrev)
                    .addGroup(jPanelRevizieLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(btnRevTabDataErase, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnAddRevTop)))
                .addGap(18, 18, 18)
                .addComponent(jLabel43)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(tfRevNazovListok, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel44)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(tfRevStdMeno, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jSeparator6, javax.swing.GroupLayout.PREFERRED_SIZE, 4, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(jLabel45)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(tfRevAccName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanelRevizieLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel94)
                    .addComponent(jLabel95)
                    .addComponent(jLabel96)
                    .addComponent(jLabel97))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanelRevizieLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(tfRevPochybnost, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(tfRevOhrozenost, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(tfRevEndemizmus, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(tfRevPovodnost, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel98)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanelRevizieLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(tfRevSvkNazov, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cbRevOchrana))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator7, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel46)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(tfRevPoznamka, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel47)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jListRevisors, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanelRevizieLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel48)
                    .addComponent(tfDatumRevD, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(tfDatumRevM, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(tfDatumRevR, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel50))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanelRevizieLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel49)
                    .addComponent(tfDatumRevSlovom, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanelRevizieLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButtonAddRev)
                    .addComponent(jButtonCancelRev))
                .addGap(40, 40, 40)
                .addComponent(jPanel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        tfRevStdMeno.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                tfMoznostiFocusGained(tfRevStdMeno, "ListOfSpecies", "meno");
            }
        });
        jListRevisors.getMainList().addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                jListPeopleFocusGained(jListRevisors);
            }
        });
        tfDatumRevSlovom.setDocument(new MaxLengthTextDocument(25));
        tfDatumRevD.setDocument(new MaxLengthTextDocument(2));
        tfDatumRevM.setDocument(new MaxLengthTextDocument(2));
        tfDatumRevR.setDocument(new MaxLengthTextDocument(4));

        jScrollPaneRevizie.setViewportView(jPanelRevizie);

        tabbedPaneInsert.addTab("Revízie", jScrollPaneRevizie);

        jScrollPaneZaradenie.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        jPanelZaradenie.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jLabel51.setText("Názov herbára, kde je/bude položka uložená");

        jLabel52.setText("Číslo rodu podľa Dalla Torre");

        tfZarHerbar.setMinimumSize(new java.awt.Dimension(73, 20));
        tfZarHerbar.setPreferredSize(new java.awt.Dimension(273, 20));

        tfZarDallaTorre.setMinimumSize(new java.awt.Dimension(73, 20));
        tfZarDallaTorre.setPreferredSize(new java.awt.Dimension(273, 20));

        tfZarCisloPol.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(134, 209, 59), 2));

        jLabel54.setText("Číslo čiarového kódu");

        tfZarBarcode.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(134, 209, 59), 2));

        jLabel55.setText("Názov exsikátovej zbierky");

        tfZarExsikat.setMinimumSize(new java.awt.Dimension(73, 20));
        tfZarExsikat.setPreferredSize(new java.awt.Dimension(273, 20));

        jLabel56.setText("Číslo exsikátovej zbierky");

        tfZarVoucher.setMinimumSize(new java.awt.Dimension(73, 20));
        tfZarVoucher.setPreferredSize(new java.awt.Dimension(273, 20));

        jLabel57.setText("Číslo zberu");

        jLabel58.setText("Počet jedincov na položke");

        jLabel59.setText("Poznámka");

        taZarHerbPoznamka.setColumns(20);
        taZarHerbPoznamka.setRows(5);
        jScrollPane9.setViewportView(taZarHerbPoznamka);

        cbTypeUnit.setText("Typová");

        btnHerbCopyPrev.setIcon(new javax.swing.ImageIcon(getClass().getResource("/copy-16.png"))); // NOI18N
        btnHerbCopyPrev.setText("Skopírovať informácie z naposledy zadaného údaja");
        btnHerbCopyPrev.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnHerbCopyPrevMouseClicked(evt);
            }
        });

        jLabel85.setText("Číslo položky/prírastkové číslo");

        btnHerbTabDataErase.setIcon(new javax.swing.ImageIcon(getClass().getResource("/erase-16.png"))); // NOI18N
        btnHerbTabDataErase.setText("Vymazať informácie zo záložky");
        btnHerbTabDataErase.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnHerbTabDataEraseMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout jPanelZaradenieLayout = new javax.swing.GroupLayout(jPanelZaradenie);
        jPanelZaradenie.setLayout(jPanelZaradenieLayout);
        jPanelZaradenieLayout.setHorizontalGroup(
            jPanelZaradenieLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelZaradenieLayout.createSequentialGroup()
                .addGroup(jPanelZaradenieLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanelZaradenieLayout.createSequentialGroup()
                        .addGap(16, 16, 16)
                        .addGroup(jPanelZaradenieLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(tfZarCisloZberu)
                            .addComponent(jLabel58, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel59, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(cbTypeUnit, javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanelZaradenieLayout.createSequentialGroup()
                                .addGroup(jPanelZaradenieLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel51)
                                    .addComponent(jLabel52)
                                    .addComponent(jLabel55)
                                    .addComponent(jLabel56)
                                    .addComponent(jLabel57)
                                    .addComponent(tfZarCisloPol, javax.swing.GroupLayout.DEFAULT_SIZE, 273, Short.MAX_VALUE)
                                    .addComponent(jLabel85))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 51, Short.MAX_VALUE)
                                .addGroup(jPanelZaradenieLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel54)
                                    .addComponent(tfZarBarcode, javax.swing.GroupLayout.PREFERRED_SIZE, 261, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanelZaradenieLayout.createSequentialGroup()
                                .addComponent(btnHerbCopyPrev)
                                .addGap(76, 76, 76)
                                .addComponent(btnHerbTabDataErase))
                            .addComponent(tfZarDallaTorre, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(tfZarKvantif, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(tfZarExsikat, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(tfZarVoucher, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jScrollPane9, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(tfZarHerbar, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addGroup(jPanelZaradenieLayout.createSequentialGroup()
                        .addGap(51, 51, 51)
                        .addComponent(filler3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(1311, Short.MAX_VALUE))
        );
        jPanelZaradenieLayout.setVerticalGroup(
            jPanelZaradenieLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelZaradenieLayout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addGroup(jPanelZaradenieLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(btnHerbTabDataErase, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnHerbCopyPrev, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(18, 18, 18)
                .addComponent(jLabel51)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(tfZarHerbar, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel52)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(tfZarDallaTorre, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanelZaradenieLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel54)
                    .addComponent(jLabel85))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanelZaradenieLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(tfZarBarcode, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(tfZarCisloPol, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel57)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(tfZarCisloZberu, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel58)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(tfZarKvantif, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel55)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(tfZarExsikat, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel56)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(tfZarVoucher, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel59)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane9, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cbTypeUnit)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(filler3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        tfZarHerbar.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                tfMoznostiFocusGained(tfZarHerbar, "Herbar", "skratkaHerb");
            }
        });
        tfZarDallaTorre.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                tfMoznostiFocusGained(tfZarDallaTorre, "DallaTorre", "nazovRodu");
            }
        });
        tfZarExsikat.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                tfMoznostiFocusGained(tfZarExsikat, "Exsikaty", "meno");
            }
        });
        tfZarVoucher.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                tfMoznostiFocusGained(tfZarVoucher, "Voucher", "menoAutora");
            }
        });

        jScrollPaneZaradenie.setViewportView(jPanelZaradenie);

        tabbedPaneInsert.addTab("Zaradenie položky", jScrollPaneZaradenie);

        jPanel6.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jLabel60.setText("Cesta k obrázku");

        jLabel63.setText("Url adresa obrázku");

        tfImageUrl.setEnabled(false);
        tfImageUrl.setInputVerifier(new UrlVerifier());

        btnImagesCopyPrev.setIcon(new javax.swing.ImageIcon(getClass().getResource("/copy-16.png"))); // NOI18N
        btnImagesCopyPrev.setText("Skopírovať informácie z naposledy zadaného údaja");
        btnImagesCopyPrev.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnImagesCopyPrevMouseClicked(evt);
            }
        });

        btnImagesTabDataErase.setIcon(new javax.swing.ImageIcon(getClass().getResource("/erase-16.png"))); // NOI18N
        btnImagesTabDataErase.setText("Vymazať informácie zo záložky");
        btnImagesTabDataErase.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnImagesTabDataEraseMouseClicked(evt);
            }
        });

        taImageDescr.setColumns(20);
        taImageDescr.setRows(5);
        jScrollPane4.setViewportView(taImageDescr);

        jLabel53.setText("Popis obrázku");

        tfFindLocalImageName.setPreferredSize(new java.awt.Dimension(446, 30));

        btnFindLocalImage.setText("Vyhľadať obrázok");
        btnFindLocalImage.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnFindLocalImageMouseClicked(evt);
            }
        });

        jPanel10.setBackground(new java.awt.Color(215, 179, 150));
        jPanel10.setBorder(javax.swing.BorderFactory.createTitledBorder("Prehľad obrázkov"));

        jTableImages.setModel(new sk.sav.bot.dataflos.models.ImagesTableModel());
        jTableImages.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        jScrollPane8.setViewportView(jTableImages);

        btnAddImageDown.setAction(new NewImageAction());
        btnAddImageDown.setIcon(new javax.swing.ImageIcon(getClass().getResource("/add-row-16.png"))); // NOI18N
        btnAddImageDown.setText("Pridať obrázok");

        btnDelImage.setAction(new DeleteImageAction());
        btnDelImage.setIcon(new javax.swing.ImageIcon(getClass().getResource("/delete-row-16.png"))); // NOI18N
        btnDelImage.setText("Odstrániť obrázok");
        btnDelImage.setEnabled(false);

        javax.swing.GroupLayout jPanel10Layout = new javax.swing.GroupLayout(jPanel10);
        jPanel10.setLayout(jPanel10Layout);
        jPanel10Layout.setHorizontalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane8, javax.swing.GroupLayout.PREFERRED_SIZE, 870, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel10Layout.createSequentialGroup()
                        .addComponent(btnAddImageDown)
                        .addGap(18, 18, 18)
                        .addComponent(btnDelImage)))
                .addGap(21, 21, 21))
        );
        jPanel10Layout.setVerticalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane8, javax.swing.GroupLayout.PREFERRED_SIZE, 155, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnAddImageDown)
                    .addComponent(btnDelImage))
                .addContainerGap())
        );

        btnAddImageTop.setAction(new NewImageAction());
        btnAddImageTop.setIcon(new javax.swing.ImageIcon(getClass().getResource("/add-row-16.png"))); // NOI18N
        btnAddImageTop.setText("Pridať obrázok");

        lblImageDisplay.setBackground(new java.awt.Color(252, 252, 252));
        lblImageDisplay.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblImageDisplay.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        lblImageDisplay.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        lblImageDisplay.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblImageDisplayMouseClicked(evt);
            }
        });

        jLabel67.setText("náhľad obrázku");

        jButtonAddImage.setAction(new AddImageAction());
        jButtonAddImage.setIcon(new javax.swing.ImageIcon(getClass().getResource("/save-16.png"))); // NOI18N
        jButtonAddImage.setText("Uložiť obrázok");
        jButtonAddImage.setEnabled(false);

        jButtonCancelImage.setAction(new CancelImageAction());
        jButtonCancelImage.setText("Zrušiť pridávanie");
        jButtonCancelImage.setEnabled(false);

        jLabel68.setForeground(new java.awt.Color(102, 102, 102));
        jLabel68.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel68.setText("limit na obrázok 6 MB");

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addGap(16, 16, 16)
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addGroup(jPanel6Layout.createSequentialGroup()
                                .addComponent(btnImagesCopyPrev)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnAddImageTop)
                                .addGap(37, 37, 37)
                                .addComponent(btnImagesTabDataErase))
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel6Layout.createSequentialGroup()
                                .addGap(1, 1, 1)
                                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel6Layout.createSequentialGroup()
                                        .addComponent(jButtonAddImage)
                                        .addGap(18, 18, 18)
                                        .addComponent(jButtonCancelImage))
                                    .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel6Layout.createSequentialGroup()
                                            .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                                .addGroup(jPanel6Layout.createSequentialGroup()
                                                    .addComponent(jLabel60)
                                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                    .addComponent(jLabel68, javax.swing.GroupLayout.PREFERRED_SIZE, 165, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                .addComponent(tfFindLocalImageName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                            .addComponent(btnFindLocalImage, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                        .addComponent(jScrollPane4, javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(tfImageUrl, javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel6Layout.createSequentialGroup()
                                            .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                                .addComponent(jLabel63, javax.swing.GroupLayout.Alignment.LEADING)
                                                .addComponent(jLabel53, javax.swing.GroupLayout.Alignment.LEADING))
                                            .addGap(0, 0, Short.MAX_VALUE))))))
                        .addGap(10, 10, 10)
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblImageDisplay, javax.swing.GroupLayout.PREFERRED_SIZE, 209, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel6Layout.createSequentialGroup()
                                .addGap(63, 63, 63)
                                .addComponent(jLabel67))))
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jPanel10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(50, 50, 50))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnImagesCopyPrev, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnImagesTabDataErase, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnAddImageTop))
                .addGap(18, 18, 18)
                .addComponent(jLabel67)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel60)
                            .addComponent(jLabel68))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(tfFindLocalImageName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnFindLocalImage))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel53)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane4)
                        .addGap(16, 16, 16)
                        .addComponent(jLabel63)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(tfImageUrl, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(lblImageDisplay, javax.swing.GroupLayout.PREFERRED_SIZE, 234, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButtonCancelImage)
                    .addComponent(jButtonAddImage))
                .addGap(45, 45, 45)
                .addComponent(jPanel10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(121, 121, 121))
        );

        javax.swing.GroupLayout jPanelObrazkyLayout = new javax.swing.GroupLayout(jPanelObrazky);
        jPanelObrazky.setLayout(jPanelObrazkyLayout);
        jPanelObrazkyLayout.setHorizontalGroup(
            jPanelObrazkyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel6, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanelObrazkyLayout.setVerticalGroup(
            jPanelObrazkyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        jScrollObrazky.setViewportView(jPanelObrazky);

        tabbedPaneInsert.addTab("Obrázky", jScrollObrazky);

        jStatusUdaja.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        btnStatusCopyPrev.setIcon(new javax.swing.ImageIcon(getClass().getResource("/copy-16.png"))); // NOI18N
        btnStatusCopyPrev.setText("Skopírovať informácie z naposledy zadaného údaja");
        btnStatusCopyPrev.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnStatusCopyPrevMouseClicked(evt);
            }
        });

        rbIhned.setAction(new StatusChangeAction());
        rbIhned.setSelected(true);
        rbIhned.setText("údaj verejne prístupný ihneď");

        rbOd.setAction(new StatusChangeAction());
        rbOd.setText("údaj verejne prístupný od");

        rbNeverejny.setAction(new StatusChangeAction());
        rbNeverejny.setText("údaj neverejný");

        jDdMmRrrr.setText("dd mm rrrr");

        jPoznamkaNepub.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jPoznamkaNepub.setText("Poznámka k nepublikovanému údaju");
        jPoznamkaNepub.setEnabled(false);

        jPoznamkaNepublik.setEnabled(false);

        jUdajDolozeny.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jUdajDolozeny.setText("Údaj je doložený:");
        jUdajDolozeny.setEnabled(false);

        fotograficky.setText("fotograficky");
        fotograficky.setEnabled(false);

        btnStatusTabDataErase.setIcon(new javax.swing.ImageIcon(getClass().getResource("/erase-16.png"))); // NOI18N
        btnStatusTabDataErase.setText("Vymazať informácie zo záložky");
        btnStatusTabDataErase.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnStatusTabDataEraseMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout jStatusUdajaLayout = new javax.swing.GroupLayout(jStatusUdaja);
        jStatusUdaja.setLayout(jStatusUdajaLayout);
        jStatusUdajaLayout.setHorizontalGroup(
            jStatusUdajaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jStatusUdajaLayout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addGroup(jStatusUdajaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(rbIhned)
                    .addComponent(rbNeverejny)
                    .addGroup(jStatusUdajaLayout.createSequentialGroup()
                        .addComponent(rbOd)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(tfNeverDD, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(tfNeverMM, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(tfNeverRRRR, javax.swing.GroupLayout.PREFERRED_SIZE, 53, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jDdMmRrrr))
                    .addComponent(jPoznamkaNepub)
                    .addComponent(jPoznamkaNepublik, javax.swing.GroupLayout.PREFERRED_SIZE, 340, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jUdajDolozeny)
                    .addComponent(fotograficky)
                    .addGroup(jStatusUdajaLayout.createSequentialGroup()
                        .addComponent(btnStatusCopyPrev)
                        .addGap(76, 76, 76)
                        .addComponent(btnStatusTabDataErase)))
                .addGap(0, 0, 0))
        );
        jStatusUdajaLayout.setVerticalGroup(
            jStatusUdajaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jStatusUdajaLayout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addGroup(jStatusUdajaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(btnStatusTabDataErase, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnStatusCopyPrev, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(18, 18, 18)
                .addComponent(rbIhned)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jStatusUdajaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(rbOd)
                    .addComponent(tfNeverDD, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(tfNeverMM, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(tfNeverRRRR, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jDdMmRrrr))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(rbNeverejny)
                .addGap(63, 63, 63)
                .addComponent(jPoznamkaNepub)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPoznamkaNepublik, javax.swing.GroupLayout.PREFERRED_SIZE, 74, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jUdajDolozeny)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(fotograficky)
                .addGap(0, 0, 0))
        );

        jScrollPaneStatusUdaja.setViewportView(jStatusUdaja);

        tabbedPaneInsert.addTab("Status údaja", jScrollPaneStatusUdaja);

        listMoznosti.setBackground(new java.awt.Color(211, 240, 211));

        btnRecordCancel.setAction(new CancelAction());
        btnRecordCancel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/close_window.png"))); // NOI18N
        btnRecordCancel.setText("Zatvoriť údaj (bez uloženia)");

        btnRecordSave.setAction(new SaveAction());
        btnRecordSave.setIcon(new javax.swing.ImageIcon(getClass().getResource("/data-add-database-icon.png"))); // NOI18N
        btnRecordSave.setText("Uložiť údaj do databázy");

        btnRecordDelete.setAction(new DeleteRecordAction());
        btnRecordDelete.setIcon(new javax.swing.ImageIcon(getClass().getResource("/data-delete-database-icon.png"))); // NOI18N
        btnRecordDelete.setText("Odstrániť údaj z databázy");

        jPanel1.setBackground(new java.awt.Color(211, 240, 211));

        jLabel17.setText("Údaj:");

        btnPrevRec.setIcon(new javax.swing.ImageIcon(getClass().getResource("/tab_left.png"))); // NOI18N
        btnPrevRec.setText("Predchádzajúci");
        btnPrevRec.setFocusPainted(false);
        btnPrevRec.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        btnPrevRec.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnPrevRecMouseClicked(evt);
            }
        });

        lblRecPosition.setEditable(false);
        lblRecPosition.setBackground(new java.awt.Color(255, 255, 255));
        lblRecPosition.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        lblRecPosition.setPreferredSize(new java.awt.Dimension(71, 20));

        lblUdajTyp.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        lblUdajTyp.setText("-");

        btnNextRec.setAction(null);
        btnNextRec.setIcon(new javax.swing.ImageIcon(getClass().getResource("/tab_right.png"))); // NOI18N
        btnNextRec.setText("Nasledujúci");
        btnNextRec.setFocusPainted(false);
        btnNextRec.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);
        btnNextRec.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnNextRecMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel17)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnPrevRec, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(lblRecPosition, javax.swing.GroupLayout.PREFERRED_SIZE, 159, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblUdajTyp)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnNextRec, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel17)
                    .addComponent(btnPrevRec, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblRecPosition, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(lblUdajTyp, javax.swing.GroupLayout.PREFERRED_SIZE, 21, Short.MAX_VALUE)
                    .addComponent(btnNextRec, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        cbListAllRec.setAction(new AllRecordsChangeListener());
        cbListAllRec.setBackground(new java.awt.Color(211, 240, 211));
        cbListAllRec.setText("zobraziť všetky údaje");
        cbListAllRec.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbListAllRecActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout recordTabLayout = new javax.swing.GroupLayout(recordTab);
        recordTab.setLayout(recordTabLayout);
        recordTabLayout.setHorizontalGroup(
            recordTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(recordTabLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(recordTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(recordTabLayout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(btnRecordSave)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnRecordCancel)
                        .addGap(120, 120, 120)
                        .addComponent(btnRecordDelete))
                    .addComponent(tabbedPaneInsert, javax.swing.GroupLayout.DEFAULT_SIZE, 1925, Short.MAX_VALUE)
                    .addGroup(recordTabLayout.createSequentialGroup()
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(cbListAllRec)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(listMoznosti, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        recordTabLayout.setVerticalGroup(
            recordTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(recordTabLayout.createSequentialGroup()
                .addGap(2, 2, 2)
                .addGroup(recordTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(listMoznosti, javax.swing.GroupLayout.DEFAULT_SIZE, 877, Short.MAX_VALUE)
                    .addGroup(recordTabLayout.createSequentialGroup()
                        .addGroup(recordTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(recordTabLayout.createSequentialGroup()
                                .addGap(13, 13, 13)
                                .addComponent(cbListAllRec)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(tabbedPaneInsert, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(recordTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btnRecordSave)
                            .addComponent(btnRecordDelete)
                            .addComponent(btnRecordCancel)))))
        );

        listMoznosti.getBtnAddNew().setAction(new AddNewObjectAction());
        listMoznosti.getBtnAddNew().setText("Pridať položku do zoznamu");
        listMoznosti.getBtnAddNew().setIcon(new javax.swing.ImageIcon(getClass().getResource("/add_to_list.png")));

        jTabbedPaneMain.addTab("Nový údaj", recordTab);

        introTab.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));

        jPanel2.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));

        jLabelVitajte.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabelVitajte.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabelVitajte.setText("Vitajte v Databáze flóry Slovenska (DATAflos)");

        btnIntroNewHerbRec.setAction(new ShowNewHerbariumRecordTabAction());
        btnIntroNewHerbRec.setText("Vložiť nový herbárový údaj");
        btnIntroNewHerbRec.setMaximumSize(new java.awt.Dimension(139, 23));
        btnIntroNewHerbRec.setMinimumSize(new java.awt.Dimension(139, 23));
        btnIntroNewHerbRec.setPreferredSize(new java.awt.Dimension(139, 23));

        btnIntroNewLitRec.setAction(new ShowNewLiteratureRecordTabAction());
        btnIntroNewLitRec.setText("Vložiť nový literárny alebo rukopisný údaj");

        btnIntroOverviewRecs.setAction(new ShowRecordsOverviewTabAction());
        btnIntroOverviewRecs.setText("Prezerať alebo upravovať existujúce údaje");

        btnIntroImportRecs.setAction(new DataImportAction());
        btnIntroImportRecs.setText("Importovať údaje");

        btnIntroExportRecs.setAction(new ShowExportWindowAction());
        btnIntroExportRecs.setText("Exportovať údaje");

        btnIntroShowTables.setAction(new ShowTablesOverviewTabAction());
        btnIntroShowTables.setText("Spravovať odkazové tabuľky");

        jLabelLogo.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabelLogo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/logo_small_top.jpg"))); // NOI18N

        btnIntroNewFieldRec.setAction(new ShowNewFieldRecordTabAction());
        btnIntroNewFieldRec.setText("Vložiť nový nepublikovaný terénny údaj bez položky");

        jLabelCoUrobit.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabelCoUrobit.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabelCoUrobit.setText("Čo chcete urobiť?");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(74, 74, 74)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(btnIntroNewLitRec, javax.swing.GroupLayout.PREFERRED_SIZE, 301, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnIntroNewHerbRec, javax.swing.GroupLayout.PREFERRED_SIZE, 301, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addComponent(btnIntroShowTables, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(btnIntroNewFieldRec, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 301, Short.MAX_VALUE)
                                .addComponent(btnIntroExportRecs, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(btnIntroImportRecs, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(btnIntroOverviewRecs, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(jLabelCoUrobit, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabelVitajte, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabelLogo, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap(71, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(jLabelLogo, javax.swing.GroupLayout.PREFERRED_SIZE, 153, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabelVitajte, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabelCoUrobit)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnIntroNewHerbRec, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnIntroNewLitRec, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnIntroNewFieldRec, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btnIntroOverviewRecs, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnIntroImportRecs, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnIntroExportRecs, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnIntroShowTables, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout introTabLayout = new javax.swing.GroupLayout(introTab);
        introTab.setLayout(introTabLayout);
        introTabLayout.setHorizontalGroup(
            introTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, introTabLayout.createSequentialGroup()
                .addContainerGap(865, Short.MAX_VALUE)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(866, Short.MAX_VALUE))
        );
        introTabLayout.setVerticalGroup(
            introTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(introTabLayout.createSequentialGroup()
                .addGap(150, 150, 150)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(234, Short.MAX_VALUE))
        );

        jTabbedPaneMain.addTab("Úvod", introTab);
        Box box = new Box(BoxLayout.Y_AXIS);
        box.setAlignmentX(JComponent.CENTER_ALIGNMENT);
        box.add(Box.createVerticalGlue());
        box.add(jPanel2);
        box.add(Box.createVerticalGlue());
        add(box);
        pack();

        jMenuNew.setText("File");

        miNewRecord.setAction(new ShowNewHerbariumRecordTabAction());
        miNewRecord.setText("Nová položka");
        jMenuNew.add(miNewRecord);

        jMenuBar1.add(jMenuNew);

        jMenuImport.setText("Import");

        jMenuImportItem.setAction(new DataImportAction());
        jMenuImportItem.setText("Importovať z excelu");
        jMenuImport.add(jMenuImportItem);

        jMenuBar1.add(jMenuImport);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jTabbedPaneMain))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPaneMain)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    //akcia pri kliknuti na tlacitko prezerania predchadzajuceho udaja
    private void btnPrevRecMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnPrevRecMouseClicked

        //pokial je toto tlacitko povolene, nacitaj udaje o pozicii aktualneho udaja a pocte udajov dokopy
        //povol tlacitko nasledzujuceho udaja a osetri ci je novy udaj prvy(pre zakazanie prezerania predchadzajuceho udaja)
        //nastav text novej pozicie a po ziskani n-teho posledneho udaja z DB zapln prislusne zalozky o udaji        
        if (btnPrevRec.isEnabled()) {
            String recPositionText = this.lblRecPosition.getText();
            String[] recPositions = recPositionText.split("\\ / ");

            int recPosition = Integer.parseInt(recPositions[0]);

            int recCount = Integer.parseInt(recPositions[1]);

            int newRecPosition = recPosition - 1;

            this.btnNextRec.setEnabled(true);

            if (newRecPosition == 1) {
                this.btnPrevRec.setEnabled(false);
            }

            int index = jTabbedPaneMain.getSelectedIndex();

            //ide o to, ze ak sme boli nastaveni na novom udaji, tak po prejdeni na predchadzajuci sa nam znizi pocet udajov v DB na pocitadle (novy udaj nieje oznaceny na zalozke cez kluc. slovo Edit)
            if (jTabbedPaneMain.getTitleAt(index).substring(0, 4).equals("Nový")) {
                recCount = recCount - 1;
            }

            this.lblRecPosition.setText(newRecPosition + " / " + recCount);

            int nthPositionDB = recCount - newRecPosition;

            // TODO: moze by nthPositionDB minusove? pri prechadzani na prvy udaj
            Udaj udaj;
            if (!cbListAllRec.isSelected()) {
                udaj = hq.getMyNthLastUdaj(nthPositionDB);
            } else {
                udaj = hq.getNthLastUdaj(nthPositionDB);
            }

            //zmen titulok zalozky podla pozicie udaja nacitaj jeho data
            jTabbedPaneMain.setTitleAt(index, "Edit " + udaj.getId() + ((udaj.getTyp() == null) ? "" : udaj.getTyp()));
            lblUdajTyp.setText(udaj.getTyp().toString());
            setTabsAndFocus(udaj.getTyp(), false);
            populateRecordTabs(udaj, false);

            //zistime ci udaj je editovatelny uzivatelom (je jeho vlastnik, alebo je admin) a podla toho nastavime ci sa moze neskor pripadne updatovat
            if (udaj.getUzivatel().equals(login) || isAdmin) {
                btnRecordSave.setEnabled(true);
                btnRecordDelete.setEnabled(true);
                SaveAction sa = (SaveAction) btnRecordSave.getAction();
                sa.setUdaj(udaj);
                sa.setUdajType(udaj.getTyp());
                DeleteRecordAction dra = (DeleteRecordAction) btnRecordDelete.getAction();
                dra.setUdaj(udaj);
            } else {
                btnRecordSave.setEnabled(false);
                btnRecordDelete.setEnabled(false);
                // TODO: is needed?
                JOptionPane.showMessageDialog(MainFrame.this, "Nie je možné editovanie iba prehliadanie.");
            }
        }
    }//GEN-LAST:event_btnPrevRecMouseClicked

    //akcia pri kliknuti na tlacitko pre kopirovanie dat o statuse udaja z predchadzajuceho zadaneho udaja
    private void btnStatusCopyPrevMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnStatusCopyPrevMouseClicked
        copyDataFromPrevious("status");
    }//GEN-LAST:event_btnStatusCopyPrevMouseClicked

    //akcia pri kliknuti na tlacitko pre kopirovanie dat o lokalite z predchadzajuceho zadaneho udaja
    private void btnLocCopyPrevMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnLocCopyPrevMouseClicked
        copyDataFromPrevious("loc");
    }//GEN-LAST:event_btnLocCopyPrevMouseClicked

    //akcia pri kliknuti na tlacitko pre kopirovanie dat o revizii z predchadzajuceho zadaneho udaja
    private void btnRevCopyPrevMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnRevCopyPrevMouseClicked
        copyDataFromPrevious("rev");
    }//GEN-LAST:event_btnRevCopyPrevMouseClicked

    //akcia pri kliknuti na tlacitko pre kopirovanie dat o herbari z predchadzajuceho zadaneho udaja
    private void btnHerbCopyPrevMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnHerbCopyPrevMouseClicked
        copyDataFromPrevious("herb");
    }//GEN-LAST:event_btnHerbCopyPrevMouseClicked

    //akcia pri kliknuti na tlacitko prezerania nasledujuceho udaja
    private void btnNextRecMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnNextRecMouseClicked

        //pokial je toto tlacitko povolene, nacitaj udaje o pozicii aktualneho udaja a pocte udajov dokopy
        //povol tlacitko predchadzajuceho udaja a osetri ci je novy udaj posledny(pre zakazanie nasledujuceho prezerania)
        //nastav text novej pozicie a po ziskani n-teho posledneho udaja z DB zapln prislusne zalozky o udaji
        if (btnNextRec.isEnabled()) {
            String recPositionText = this.lblRecPosition.getText();
            String[] recPositions = recPositionText.split("\\ / ");

            int recPosition = Integer.parseInt(recPositions[0]);
            int recCount = Integer.parseInt(recPositions[1]);

            this.btnPrevRec.setEnabled(true);

            int newRecPosition = recPosition + 1;

            if (newRecPosition > recCount) {
                this.btnNextRec.setEnabled(false);
            }

            int nthPositionDB = recCount - newRecPosition;

            int index = jTabbedPaneMain.getSelectedIndex();

            // ak pozicia udaja nie je pozicia noveho udaja, zmen titulok zalozky podla pozicie udaja nacitaj jeho data
            if (newRecPosition <= recCount) {

                Udaj udaj;
                if (!cbListAllRec.isSelected()) {
                    udaj = hq.getMyNthLastUdaj(nthPositionDB);
                } else {
                    udaj = hq.getNthLastUdaj(nthPositionDB);
                }
                jTabbedPaneMain.setTitleAt(index, "Edit " + udaj.getId() + ((udaj.getTyp() == null) ? "" : udaj.getTyp()));
                lblUdajTyp.setText(udaj.getTyp().toString());
                setTabsAndFocus(udaj.getTyp(), false);
                populateRecordTabs(udaj, false);
                this.lblRecPosition.setText(newRecPosition + " / " + recCount);

                //zistime ci udaj je editovatelny uzivatelom (je jeho vlastnik, alebo je admin) a podla toho nastavime ci sa moze neskor pripadne updatovat
                if (udaj.getUzivatel().equals(login) || isAdmin) {
                    btnRecordSave.setEnabled(true);
                    btnRecordDelete.setEnabled(true);
                    SaveAction sa = (SaveAction) btnRecordSave.getAction();
                    sa.setUdaj(udaj);
                    sa.setUdajType(udaj.getTyp());
                    DeleteRecordAction dra = (DeleteRecordAction) btnRecordDelete.getAction();
                    dra.setUdaj(udaj);
                } else {
                    btnRecordSave.setEnabled(false);
                    btnRecordDelete.setEnabled(false);
                    // TODO: is needed?
                    JOptionPane.showMessageDialog(MainFrame.this, "Nie je možné editovanie iba prehliadanie.");
                }

            } else { // v opacnom pripade vycisti policka a nastav podla typu posledneho udaja titulok zalozky na typ noveho udaja (treba nejak urcit)
                recCount = recCount + 1;
                this.lblRecPosition.setText(newRecPosition + " / " + recCount);
                clearAll();
                btnRecordSave.setEnabled(true);
                btnRecordDelete.setEnabled(true);
                SaveAction sa = (SaveAction) btnRecordSave.getAction();
                sa.setUdaj(null);

                Udaj previousUdaj;
                if (!cbListAllRec.isSelected()) {
                    previousUdaj = hq.getMyNthLastUdaj(0);
                } else {
                    previousUdaj = hq.getNthLastUdaj(0);
                }
                setNewRecordTabName(previousUdaj.getTyp());
                sa.setUdajType(previousUdaj.getTyp());
                lblUdajTyp.setText(String.valueOf(sa.getUdajType()));
            }

        }
    }//GEN-LAST:event_btnNextRecMouseClicked

    private void tfLocFloraFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tfLocFloraFocusLost

        if (!this.listMoznosti.isFocusOwner()) {
            this.listMoznosti.clear();

        }
    }//GEN-LAST:event_tfLocFloraFocusLost

    //akcia pri kliknuti na tlacitko pre kopirovanie dat o literature z predchadzajuceho zadaneho udaja
    private void btnLitCopyPrevMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnLitCopyPrevMouseClicked
        copyDataFromPrevious("lit");
    }//GEN-LAST:event_btnLitCopyPrevMouseClicked

    private void tfLitBookKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tfLitBookKeyReleased
        //        if (this.tfLitBook.getText().length()>0){
        //            enabledBookData(true);
        //            enabledJournalTitle(false);
        //            this.tfLitJournalTitle.setEntity(null);
        //        } else {
        //            enabledBookData(false);
        //            enabledJournalTitle(true);
        //        }
    }//GEN-LAST:event_tfLitBookKeyReleased

    private void tfLitBookFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tfLitBookFocusLost
        if (getTfLitBook().getText().equals("")) {
            enabledJournalTitle(true);
            enabledBookData(false);
        }
    }//GEN-LAST:event_tfLitBookFocusLost

    private void tfLitBookFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tfLitBookFocusGained
        enabledJournalTitle(false);
        enabledBookData(true);
    }//GEN-LAST:event_tfLitBookFocusGained

    private void tfLitBookMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tfLitBookMouseClicked
        MainFrame.this.listMoznosti.clear();
    }//GEN-LAST:event_tfLitBookMouseClicked

    private void tfLitJournalTitleKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tfLitJournalTitleKeyReleased
        //        if (this.tfLitJournalTitle.getTextField().getText().length()>0){
        //            enabledJournalData(true);
        //            enabledBookTitle(false);
        //        } else {
        //            enabledJournalData(false);
        //            enabledBookTitle(true);
        //        }
    }//GEN-LAST:event_tfLitJournalTitleKeyReleased

    private void tfLitJournalTitleFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tfLitJournalTitleFocusLost
        if (tfLitJournalTitle.getTextField().getText().equals("")) {
            enabledBookTitle(true);
            enabledJournalData(false);
        }
    }//GEN-LAST:event_tfLitJournalTitleFocusLost

    private void tfLitJournalTitleFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tfLitJournalTitleFocusGained
        enabledBookTitle(false);
        enabledJournalData(true);
    }//GEN-LAST:event_tfLitJournalTitleFocusGained

    private void btnLitTabDataEraseMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnLitTabDataEraseMouseClicked
        clearTabFields("lit");
    }//GEN-LAST:event_btnLitTabDataEraseMouseClicked

    private void btnHerbTabDataEraseMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnHerbTabDataEraseMouseClicked
        clearTabFields("herb");
    }//GEN-LAST:event_btnHerbTabDataEraseMouseClicked

    private void btnStatusTabDataEraseMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnStatusTabDataEraseMouseClicked
        clearTabFields("status");
    }//GEN-LAST:event_btnStatusTabDataEraseMouseClicked

    private void btnLocTabDataEraseMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnLocTabDataEraseMouseClicked
        clearTabFields("loc");
    }//GEN-LAST:event_btnLocTabDataEraseMouseClicked

    private void btnRevTabDataEraseMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnRevTabDataEraseMouseClicked
        clearTabFields("rev");
    }//GEN-LAST:event_btnRevTabDataEraseMouseClicked

    private void btnIdentTabDataEraseMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnIdentTabDataEraseMouseClicked
        clearTabFields("ident");
    }//GEN-LAST:event_btnIdentTabDataEraseMouseClicked

    private void tfDatumUrcMFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tfDatumUrcMFocusGained
        //this.listMoznosti.clear();
    }//GEN-LAST:event_tfDatumUrcMFocusGained

    private void tfDatumUrcDFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tfDatumUrcDFocusGained
        this.listMoznosti.clear();
    }//GEN-LAST:event_tfDatumUrcDFocusGained

    //akcia pri kliknuti na tlacitko pre kopirovanie identifikacnych dat z predchadzajuceho zadaneho udaja
    private void btnIdentCopyPrevMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnIdentCopyPrevMouseClicked
        copyDataFromPrevious("ident");
    }//GEN-LAST:event_btnIdentCopyPrevMouseClicked

    private void tfUrcNazovSchedaFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tfUrcNazovSchedaFocusGained
        this.listMoznosti.clear();
    }//GEN-LAST:event_tfUrcNazovSchedaFocusGained

    private void tfUrcPoznamkaFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tfUrcPoznamkaFocusGained
        this.listMoznosti.clear();
        listMoznosti.getFilterField().setText("");
    }//GEN-LAST:event_tfUrcPoznamkaFocusGained

    private void btnImagesCopyPrevMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnImagesCopyPrevMouseClicked
        copyDataFromPrevious("images");
    }//GEN-LAST:event_btnImagesCopyPrevMouseClicked

    private void btnImagesTabDataEraseMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnImagesTabDataEraseMouseClicked
        clearTabFields("images");
    }//GEN-LAST:event_btnImagesTabDataEraseMouseClicked

    private void btnFindLocalImageMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnFindLocalImageMouseClicked

        JPanel panelImportDialog = new JPanel();
        panelImportDialog.setLayout(new BorderLayout());

        JFileChooser fileopen = new JFileChooser(".");
        FileNameExtensionFilter xlsfilter = new FileNameExtensionFilter("obrázky (*.jpeg), (*.tiff)", "jpg", "jpeg", "tif", "tiff");
        fileopen.setFileFilter(xlsfilter);
        fileopen.setDialogTitle("Vyberte obrázok z počítača");

        int ret = fileopen.showDialog(panelImportDialog, "Zvoliť obrázok");

        if (ret == JFileChooser.APPROVE_OPTION) {
            File imageFile = fileopen.getSelectedFile();

            // limit nastaveny na 6MB
            if (imageFile.length() <= 6291456) {

                String filePath = imageFile.getAbsolutePath();
                tfFindLocalImageName.setText(filePath);

                BufferedImage img;
                try {
                    // nacitaj obrazok zo zadanej cesty
                    img = ImageIO.read(new File(filePath));
                    // naskaluj zadany obrazok
                    BufferedImage scaledImg = scaleImgToLbl(img);
                    //vyobraz naskalovany obrazok
                    lblImageDisplay.setIcon(new ImageIcon(scaledImg));
                    activeImage = img;
                } catch (IOException ex) {
                    log.error("Exception while loading udaj image", ex);
                    JOptionPane.showMessageDialog(MainFrame.this, "Nekorektne zadaný obrázok", "Chyba", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                log.error("Udaj image exceeds 6MB (6291456B) size: " + imageFile.length() + "B");
                JOptionPane.showMessageDialog(MainFrame.this, "Prekročený limit na veľkosť obrázku", "Príliš veľká veľkosť", JOptionPane.ERROR_MESSAGE);
            }
        }
    }//GEN-LAST:event_btnFindLocalImageMouseClicked

    private void lblImageDisplayMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblImageDisplayMouseClicked
        ImageDisplay display = new ImageDisplay(activeImage);
    }//GEN-LAST:event_lblImageDisplayMouseClicked

    private void cbListAllRecActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbListAllRecActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cbListAllRecActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /*
         * Set the Nimbus look and feel
         */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /*
         * If Nimbus (introduced in Java SE 6) is not available, stay with the
         * default look and feel. For details see
         * http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | javax.swing.UnsupportedLookAndFeelException ex) {
            log.error("Nimbus look and feel - error occured", ex);
        }
        //</editor-fold>

        /*
         * Create and display the form
         */
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                JFrame mainFrame = new MainFrame();
                mainFrame.pack();
                mainFrame.setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAddImageDown;
    private javax.swing.JButton btnAddImageTop;
    private javax.swing.JButton btnAddLitSourceDown;
    private javax.swing.JButton btnAddLitSourceTop;
    private javax.swing.JButton btnAddRevDown;
    private javax.swing.JButton btnAddRevTop;
    private javax.swing.JButton btnCancelAdding;
    private javax.swing.JButton btnCloseLogin;
    private javax.swing.JButton btnDelImage;
    private javax.swing.JButton btnDelRev;
    private javax.swing.JButton btnDeleteLitSource;
    private javax.swing.JButton btnFilterByLetterA;
    private javax.swing.JButton btnFilterByLetterB;
    private javax.swing.JButton btnFilterByLetterC;
    private javax.swing.JButton btnFilterByLetterD;
    private javax.swing.JButton btnFilterByLetterE;
    private javax.swing.JButton btnFilterByLetterF;
    private javax.swing.JButton btnFilterByLetterG;
    private javax.swing.JButton btnFilterByLetterH;
    private javax.swing.JButton btnFilterByLetterI;
    private javax.swing.JButton btnFilterByLetterJ;
    private javax.swing.JButton btnFilterByLetterK;
    private javax.swing.JButton btnFilterByLetterL;
    private javax.swing.JButton btnFilterByLetterM;
    private javax.swing.JButton btnFilterByLetterN;
    private javax.swing.JButton btnFilterByLetterO;
    private javax.swing.JButton btnFilterByLetterP;
    private javax.swing.JButton btnFilterByLetterQ;
    private javax.swing.JButton btnFilterByLetterR;
    private javax.swing.JButton btnFilterByLetterS;
    private javax.swing.JButton btnFilterByLetterT;
    private javax.swing.JButton btnFilterByLetterU;
    private javax.swing.JButton btnFilterByLetterV;
    private javax.swing.JButton btnFilterByLetterW;
    private javax.swing.JButton btnFilterByLetterX;
    private javax.swing.JButton btnFilterByLetterY;
    private javax.swing.JButton btnFilterByLetterZ;
    private javax.swing.JButton btnFindLocalImage;
    private javax.swing.JButton btnHerbCopyPrev;
    private javax.swing.JButton btnHerbTabDataErase;
    private javax.swing.JButton btnIdentCopyPrev;
    private javax.swing.JButton btnIdentTabDataErase;
    private javax.swing.JButton btnImagesCopyPrev;
    private javax.swing.JButton btnImagesTabDataErase;
    private javax.swing.JButton btnIntroExportRecs;
    private javax.swing.JButton btnIntroImportRecs;
    private javax.swing.JButton btnIntroNewFieldRec;
    private javax.swing.JButton btnIntroNewHerbRec;
    private javax.swing.JButton btnIntroNewLitRec;
    private javax.swing.JButton btnIntroOverviewRecs;
    private javax.swing.JButton btnIntroShowTables;
    private javax.swing.JButton btnLitCopyPrev;
    private javax.swing.JButton btnLitTabDataErase;
    private javax.swing.JButton btnLocCopyPrev;
    private javax.swing.JButton btnLocTabDataErase;
    private javax.swing.JButton btnLogin;
    private javax.swing.JButton btnNewEntity;
    private javax.swing.JButton btnNextRec;
    private javax.swing.JButton btnOverviewCancel;
    private javax.swing.JButton btnOverviewCopyRec;
    private javax.swing.JButton btnOverviewDeleteRec;
    private javax.swing.JButton btnOverviewNewHerbRec;
    private javax.swing.JButton btnOverviewNewLitRec;
    private javax.swing.JButton btnPrevRec;
    private javax.swing.JButton btnRecordCancel;
    private javax.swing.JButton btnRecordDelete;
    private javax.swing.JButton btnRecordSave;
    private javax.swing.JButton btnRevCopyPrev;
    private javax.swing.JButton btnRevTabDataErase;
    private javax.swing.JButton btnSaveLitSourceToTable;
    private javax.swing.JButton btnStatusCopyPrev;
    private javax.swing.JButton btnStatusTabDataErase;
    private javax.swing.JButton btnTablesDeleteRec;
    private javax.swing.JButton btnTablesNextPage;
    private javax.swing.JButton btnTablesOverviewCancel;
    private javax.swing.JButton btnTablesPrevPage;
    private javax.swing.JButton btnTablesSearch;
    private javax.swing.JCheckBox cbListAllRec;
    private javax.swing.JCheckBox cbLitMap;
    private javax.swing.JCheckBox cbLitPhoto;
    private javax.swing.JComboBox cbLocCoordType;
    private javax.swing.JCheckBox cbRevOchrana;
    private javax.swing.JCheckBox cbTypeUnit;
    private javax.swing.JCheckBox cbUrcOchrana;
    private javax.swing.JCheckBox chbLocAltCca;
    private javax.swing.Box.Filler filler1;
    private javax.swing.Box.Filler filler2;
    private javax.swing.Box.Filler filler3;
    private javax.swing.Box.Filler filler4;
    private sk.sav.bot.dataflos.gui.FilterRecordsOverview filterRecordsOverview;
    private javax.swing.JRadioButton fotograficky;
    private javax.swing.JPanel introTab;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButtonAddImage;
    private javax.swing.JButton jButtonAddRev;
    private javax.swing.JButton jButtonCancelImage;
    private javax.swing.JButton jButtonCancelRev;
    private javax.swing.JLabel jDdMmRrrr;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel29;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel30;
    private javax.swing.JLabel jLabel31;
    private javax.swing.JLabel jLabel32;
    private javax.swing.JLabel jLabel33;
    private javax.swing.JLabel jLabel34;
    private javax.swing.JLabel jLabel35;
    private javax.swing.JLabel jLabel36;
    private javax.swing.JLabel jLabel37;
    private javax.swing.JLabel jLabel38;
    private javax.swing.JLabel jLabel39;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel40;
    private javax.swing.JLabel jLabel41;
    private javax.swing.JLabel jLabel42;
    private javax.swing.JLabel jLabel43;
    private javax.swing.JLabel jLabel44;
    private javax.swing.JLabel jLabel45;
    private javax.swing.JLabel jLabel46;
    private javax.swing.JLabel jLabel47;
    private javax.swing.JLabel jLabel48;
    private javax.swing.JLabel jLabel49;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel50;
    private javax.swing.JLabel jLabel51;
    private javax.swing.JLabel jLabel52;
    private javax.swing.JLabel jLabel53;
    private javax.swing.JLabel jLabel54;
    private javax.swing.JLabel jLabel55;
    private javax.swing.JLabel jLabel56;
    private javax.swing.JLabel jLabel57;
    private javax.swing.JLabel jLabel58;
    private javax.swing.JLabel jLabel59;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel60;
    private javax.swing.JLabel jLabel61;
    private javax.swing.JLabel jLabel62;
    private javax.swing.JLabel jLabel63;
    private javax.swing.JLabel jLabel64;
    private javax.swing.JLabel jLabel65;
    private javax.swing.JLabel jLabel66;
    private javax.swing.JLabel jLabel67;
    private javax.swing.JLabel jLabel68;
    private javax.swing.JLabel jLabel69;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel70;
    private javax.swing.JLabel jLabel71;
    private javax.swing.JLabel jLabel72;
    private javax.swing.JLabel jLabel73;
    private javax.swing.JLabel jLabel74;
    private javax.swing.JLabel jLabel75;
    private javax.swing.JLabel jLabel76;
    private javax.swing.JLabel jLabel77;
    private javax.swing.JLabel jLabel78;
    private javax.swing.JLabel jLabel79;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel80;
    private javax.swing.JLabel jLabel81;
    private javax.swing.JLabel jLabel82;
    private javax.swing.JLabel jLabel83;
    private javax.swing.JLabel jLabel84;
    private javax.swing.JLabel jLabel85;
    private javax.swing.JLabel jLabel86;
    private javax.swing.JLabel jLabel87;
    private javax.swing.JLabel jLabel88;
    private javax.swing.JLabel jLabel89;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JLabel jLabel90;
    private javax.swing.JLabel jLabel91;
    private javax.swing.JLabel jLabel92;
    private javax.swing.JLabel jLabel93;
    private javax.swing.JLabel jLabel94;
    private javax.swing.JLabel jLabel95;
    private javax.swing.JLabel jLabel96;
    private javax.swing.JLabel jLabel97;
    private javax.swing.JLabel jLabel98;
    private javax.swing.JLabel jLabelCoUrobit;
    private javax.swing.JLabel jLabelDATAflos;
    private javax.swing.JLabel jLabelLogin;
    private javax.swing.JLabel jLabelLogo;
    private javax.swing.JLabel jLabelPages;
    private javax.swing.JLabel jLabelPassword;
    private javax.swing.JLabel jLabelVitajte;
    private sk.sav.bot.dataflos.gui.JListAddRemove jListCollectors;
    private sk.sav.bot.dataflos.gui.JListAddRemove jListFyto;
    private sk.sav.bot.dataflos.gui.JListAddRemove jListIdentificators;
    private sk.sav.bot.dataflos.gui.JListAddRemove jListLitAuthors;
    private sk.sav.bot.dataflos.gui.JListAddRemove jListLitEditors;
    private sk.sav.bot.dataflos.gui.JListAddRemove jListLitKeywords;
    private sk.sav.bot.dataflos.gui.JListAddRemove jListQuadrants;
    private sk.sav.bot.dataflos.gui.JListAddRemove jListRevisors;
    private javax.swing.JList jListTables;
    private javax.swing.JPanel jLitTablePanel;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenu jMenuImport;
    private javax.swing.JMenuItem jMenuImportItem;
    private javax.swing.JMenu jMenuNew;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JPanel jPanelLitZdroj;
    private javax.swing.JPanel jPanelLokalita;
    private javax.swing.JPanel jPanelObrazky;
    private javax.swing.JPanel jPanelRevizie;
    private javax.swing.JPanel jPanelUrcenie;
    private javax.swing.JPanel jPanelZaradenie;
    private javax.swing.JLabel jPoznamkaNepub;
    private javax.swing.JTextField jPoznamkaNepublik;
    private javax.swing.JScrollPane jScrollObrazky;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane10;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JScrollPane jScrollPane7;
    private javax.swing.JScrollPane jScrollPane8;
    private javax.swing.JScrollPane jScrollPane9;
    private javax.swing.JScrollPane jScrollPaneLitZdroj;
    private javax.swing.JScrollPane jScrollPaneLokalita;
    private javax.swing.JScrollPane jScrollPaneRevizie;
    private javax.swing.JScrollPane jScrollPaneStatusUdaja;
    private javax.swing.JScrollPane jScrollPaneUrcenie;
    private javax.swing.JScrollPane jScrollPaneZaradenie;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JSeparator jSeparator3;
    private javax.swing.JSeparator jSeparator4;
    private javax.swing.JSeparator jSeparator5;
    private javax.swing.JSeparator jSeparator6;
    private javax.swing.JSeparator jSeparator7;
    private javax.swing.JPanel jStatusUdaja;
    private javax.swing.JTabbedPane jTabbedPaneMain;
    private javax.swing.JTable jTableImages;
    private javax.swing.JTable jTableLitSources;
    private javax.swing.JScrollPane jTableLitSourcesScrollPane;
    private javax.swing.JTable jTableOverview;
    private javax.swing.JTable jTableRevisions;
    private javax.swing.JTextField jTextFieldFilter;
    private javax.swing.JLabel jUdajDolozeny;
    private javax.swing.JLabel lblImageDisplay;
    private javax.swing.JTextField lblRecPosition;
    private javax.swing.JLabel lblUdajTyp;
    private sk.sav.bot.dataflos.gui.FilterJList listMoznosti;
    private javax.swing.JDialog loginDialog;
    private javax.swing.JMenuItem miNewRecord;
    private javax.swing.JPasswordField pfPassword;
    private javax.swing.JRadioButton rbIhned;
    private javax.swing.JRadioButton rbLocEast;
    private javax.swing.JRadioButton rbLocNorth;
    private javax.swing.JRadioButton rbLocSouth;
    private javax.swing.JRadioButton rbLocWest;
    private javax.swing.ButtonGroup rbLocationNS;
    private javax.swing.ButtonGroup rbLocationWE;
    private javax.swing.JRadioButton rbNeverejny;
    private javax.swing.JRadioButton rbOd;
    private javax.swing.ButtonGroup rbRecordAccess;
    private javax.swing.JPanel recordTab;
    private javax.swing.JPanel recordsOverviewTab;
    private javax.swing.JTextArea taImageDescr;
    private javax.swing.JTextArea taLocDescr;
    private javax.swing.JTextArea taLocNotes;
    private javax.swing.JTextArea taLocScheda;
    private javax.swing.JTextArea taZarHerbPoznamka;
    private javax.swing.JTabbedPane tabbedPaneInsert;
    private javax.swing.JPanel tablesOverviewTab;
    private javax.swing.JTextField tfDatumRevD;
    private javax.swing.JTextField tfDatumRevM;
    private javax.swing.JTextField tfDatumRevR;
    private javax.swing.JTextField tfDatumRevSlovom;
    private javax.swing.JTextField tfDatumUrcD;
    private javax.swing.JTextField tfDatumUrcM;
    private javax.swing.JTextField tfDatumUrcR;
    private javax.swing.JTextField tfDatumUrcSlovom;
    private javax.swing.JTextField tfDatumZbD;
    private javax.swing.JTextField tfDatumZbM;
    private javax.swing.JTextField tfDatumZbR;
    private javax.swing.JTextField tfDatumZbSlovom;
    private javax.swing.JTextField tfFindLocalImageName;
    private javax.swing.JTextField tfImageUrl;
    private javax.swing.JTextField tfLitBook;
    private javax.swing.JTextField tfLitChapter;
    private javax.swing.JTextField tfLitIssue;
    private sk.sav.bot.dataflos.gui.TextFieldMoznosti tfLitJournalTitle;
    private javax.swing.JTextField tfLitNotes;
    private javax.swing.JTextField tfLitPages;
    private javax.swing.JTextField tfLitPublTitle;
    private javax.swing.JTextField tfLitPublTitleTransl;
    private javax.swing.JTextField tfLitPublYear;
    private javax.swing.JTextField tfLitPublisher;
    private javax.swing.JTextField tfLitRecPage;
    private javax.swing.JTextField tfLitSource;
    private javax.swing.JTextField tfLitYear;
    private javax.swing.JTextField tfLocAltEpif;
    private javax.swing.JTextField tfLocAltFrom;
    private javax.swing.JTextField tfLocAltTo;
    private sk.sav.bot.dataflos.gui.TextFieldMoznosti tfLocBrumFour;
    private sk.sav.bot.dataflos.gui.TextFieldMoznosti tfLocFlora;
    private javax.swing.JTextField tfLocHost;
    private javax.swing.JTextField tfLocLatDeg;
    private javax.swing.JTextField tfLocLatMin;
    private javax.swing.JTextField tfLocLatSec;
    private javax.swing.JTextField tfLocLngDeg;
    private javax.swing.JTextField tfLocLngMin;
    private javax.swing.JTextField tfLocLngSec;
    private sk.sav.bot.dataflos.gui.TextFieldMoznosti tfLocNac;
    private sk.sav.bot.dataflos.gui.TextFieldMoznosti tfLocNgc;
    private sk.sav.bot.dataflos.gui.TextFieldMoznosti tfLocProtArea;
    private javax.swing.JTextField tfLocSoil;
    private javax.swing.JTextField tfLocSubstrate;
    private sk.sav.bot.dataflos.gui.TextFieldMoznosti tfLocVac;
    private sk.sav.bot.dataflos.gui.TextFieldMoznosti tfLocVgc;
    private sk.sav.bot.dataflos.gui.TextFieldMoznosti tfLocVill;
    private javax.swing.JTextField tfLogin;
    private javax.swing.JTextField tfNeverDD;
    private javax.swing.JTextField tfNeverMM;
    private javax.swing.JTextField tfNeverRRRR;
    private javax.swing.JTextField tfRevAccName;
    private javax.swing.JTextField tfRevEndemizmus;
    private javax.swing.JTextField tfRevNazovListok;
    private javax.swing.JTextField tfRevOhrozenost;
    private javax.swing.JTextField tfRevPochybnost;
    private javax.swing.JTextField tfRevPovodnost;
    private javax.swing.JTextField tfRevPoznamka;
    private sk.sav.bot.dataflos.gui.TextFieldMoznosti tfRevStdMeno;
    private javax.swing.JTextField tfRevSvkNazov;
    private javax.swing.JTextField tfUrcAccName;
    private javax.swing.JTextField tfUrcEndemizmus;
    private javax.swing.JTextField tfUrcNazovScheda;
    private javax.swing.JTextField tfUrcOhrozenost;
    private javax.swing.JTextField tfUrcPochybnost;
    private javax.swing.JTextField tfUrcPovodnost;
    private javax.swing.JTextField tfUrcPoznamka;
    private sk.sav.bot.dataflos.gui.TextFieldMoznosti tfUrcStdMeno;
    private javax.swing.JTextField tfUrcSvkNazov;
    private javax.swing.JTextField tfZarBarcode;
    private javax.swing.JTextField tfZarCisloPol;
    private javax.swing.JTextField tfZarCisloZberu;
    private sk.sav.bot.dataflos.gui.TextFieldMoznosti tfZarDallaTorre;
    private sk.sav.bot.dataflos.gui.TextFieldMoznosti tfZarExsikat;
    private sk.sav.bot.dataflos.gui.TextFieldMoznosti tfZarHerbar;
    private javax.swing.JTextField tfZarKvantif;
    private sk.sav.bot.dataflos.gui.TextFieldMoznosti tfZarVoucher;
    // End of variables declaration//GEN-END:variables

    // vlakno, majuce na starosti odstranenie udaja z databazy popri loadingDialogu
    class DeleteRecordTask extends SwingWorker<Void, Integer> {

        int id, modelRow;
        RecordsInsertedModel model;
        boolean doneOK = false;

        // ak rim != null, odstranujeme udaj z tabulkoveho prehladu udajov, v opacnom pripade sa odstranuje z okna pre vkladanie udajov
        DeleteRecordTask(int recId, RecordsInsertedModel rim, int mr) {
            id = recId;
            model = rim;
            modelRow = mr;
        }

        @Override
        public Void doInBackground() {
            // metoda na odstranenie udaja z DB
            doneOK = deleteRecord(id);
            return null;
        }

        @Override
        public void done() {
            // podla toho ci odstranenie prebehlo v poriadku, vykonaj nutne kroky a vyobraz o tom hlasenie
            if (doneOK) {
                if (model != null) {
                    // ak sa odstranovalo z tabulky, vymaz prislusny riadok a zaktualizuj data
                    model.getData().remove(modelRow);
                    model.fireTableDataChanged();
                } else {
                    // ak sa odstranovalo z "vkladacej" perspektivy, zavri toto okno a nastav sa na uvodnu ponuku
                    jTabbedPaneMain.remove(getRecordTab());
                    jTabbedPaneMain.setSelectedIndex(0);
                }
                loadingDialog.setVisible(false);
                log.info("Deletion of udaj successfully completed.");
                JOptionPane.showMessageDialog(MainFrame.this, "Údaj bol odstránený z databázy");
            } else {
                log.info("Exception while deleting udaj from DB.");
                JOptionPane.showMessageDialog(MainFrame.this, "Nastal problém pri odstraňovaní údaja z databázy", "Problém pri odstraňovaní údaja", JOptionPane.WARNING_MESSAGE);
            }
        }

        @Override
        protected void process(List<Integer> pieces) {
        }
    }
}
