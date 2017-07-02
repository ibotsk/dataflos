/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sk.sav.bot.dataflos.excelimport;

import java.util.List;
import java.util.Map;
import javax.swing.JOptionPane;
import javax.swing.SwingWorker;
import org.apache.log4j.Logger;
import sk.sav.bot.dataflos.entity.Udaj;
import sk.sav.bot.dataflos.entity.interf.AssociableEntity;
import sk.sav.bot.dataflos.factory.UdajFactory;
import sk.sav.bot.dataflos.util.HibernateQuery;

/**
 *
 * @author .jakub
 */
public class ExcelDoImport extends SwingWorker<Integer, String>{
    
    private static Logger log = Logger.getLogger(ExcelPrepareImport.class.getName());
    
    ExcelImportMonitor im;
    HibernateQuery hq;
    UdajFactory uf;
    
    Map<String, AssociableEntity> unsavedEntities = null;
    List<Udaj> importUdaje = null;
    
    public ExcelDoImport(final ExcelImportMonitor im, final HibernateQuery hq, Map<String, AssociableEntity> unsavedEntities, List<Udaj> importUdaje) {
        this.im = im;
        this.hq = hq;
        this.uf = UdajFactory.getInstance(hq.getSession());
        this.unsavedEntities = unsavedEntities;
        this.importUdaje = importUdaje;
    }

    @Override
    protected Integer doInBackground() throws Exception {
        
        //najprv sa uskutocni vkladanie novych subentit
        for (AssociableEntity ent : unsavedEntities.values()){
            hq.insertOrUpdateEntity(ent);
        }
                
        int i = 0;
        // nasledne sa vytvori a vlozi impUdaj so vsetkymi jeho priamimi atributmi
        for (Udaj importUdaj : importUdaje){
            
            if (importUdaj != null){
                try {
                    uf.createUdaj(null, importUdaj.getLokality(), importUdaj.getHerbarPolozky(), importUdaj.getLitZdrojs(), importUdaj.getRevisions(), importUdaj.getUrcenie(), null, importUdaj.getUdajZberAsocs(), importUdaj.getTyp(), importUdaj.getDatumZberu(), importUdaj.getDatumZberuSlovom(), importUdaj.isVerejnePristupny(), importUdaj.getVerejnePristupnyOd(), hq.getLogin());
                } catch (Exception e){
                    // i+1 kvoli pocitaniu udajov od 0
                    int failingRow = findOutFailingRow(i+1, importUdaje);
                    String failingTab = "";
                    switch (importUdaje.get(i).getTyp()){
                        case 'H': failingTab = "herbárové položky"; break;
                        case 'L': failingTab = "literárne položky"; break;
                        case 'T': failingTab = "terénne položky"; break;
                    }
                    log.error("Import interrupted: on udaj row "+failingRow+" on tab "+failingTab+" Details: " + e);
                    JOptionPane.showMessageDialog(null, String.format("Počas importu údajov nastala chyba a import dát bol prerušený.\n Údaje od riadku "+failingRow+" od záložky "+failingTab+" neboli naimpotované"), "Chyba", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                log.error("Null udaj pri importe do DB");
            }
            i++;
            log.info(i+". udaj imported");
            int percent = (int)((i * 100) / importUdaje.size());
            setProgress(percent);
        }
        log.info("Import done");
        setProgress(100);
        return 0;
        
    }

    // helpe
    private int findOutFailingRow(int udajNumber, List<Udaj> importUdaje) {
        
        // first, find out herb and lit udaj types counts
        int herbCount = 0;
        int litCount = 0;
    
        for (int i=0; i<importUdaje.size(); i++){
            switch (importUdaje.get(i).getTyp()){
                case 'H': herbCount++; break;
                case 'L': litCount++; break;
            }    
        }
        
        if (herbCount>=udajNumber){
            //it should be inside herbar tab
            // +3 header rows
            return udajNumber+3;
        } else if (herbCount<udajNumber && udajNumber<=(herbCount+litCount)){
            //it should be inside literature tab
            return udajNumber-herbCount+3;
        } else {
            //it should be inside teren tab
            return udajNumber-herbCount-litCount+3;
        }
    }
}
