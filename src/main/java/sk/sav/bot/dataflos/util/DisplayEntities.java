package sk.sav.bot.dataflos.util;

import java.util.ArrayList;
import java.util.List;

/*
 * To change this template, choose Tools | Templates and open the template in
 * the editor.
 */
/**
 *
 * @author Matus
 */
public class DisplayEntities {

    private final List<String> entToDisplay = new ArrayList<>();
    private static DisplayEntities instance = null;

    private DisplayEntities() {
        this.entToDisplay.add("Brumit1");
        this.entToDisplay.add("Brumit2");
        this.entToDisplay.add("Brumit3");
        this.entToDisplay.add("Brumit4");
        this.entToDisplay.add("Casopisy");
        this.entToDisplay.add("Chu");
        this.entToDisplay.add("DallaTorre");
        this.entToDisplay.add("Exsikaty");
        this.entToDisplay.add("Family");
        this.entToDisplay.add("FamilyApg");
        this.entToDisplay.add("Flora");
        this.entToDisplay.add("Ftgoblast");
        this.entToDisplay.add("Ftgobvod");
        this.entToDisplay.add("Ftgokres");
        this.entToDisplay.add("Genus");
        this.entToDisplay.add("Herbar");
        this.entToDisplay.add("Kvadrant");
        this.entToDisplay.add("ListOfSpecies");
        this.entToDisplay.add("LitZdroj");
        this.entToDisplay.add("MenaZberRev");
        this.entToDisplay.add("Nac");
        this.entToDisplay.add("Ngc");
        this.entToDisplay.add("Obec");
        this.entToDisplay.add("TaxonEndemizmus");
        this.entToDisplay.add("TaxonOhrozenost");
        this.entToDisplay.add("TaxonPochybnost");
        this.entToDisplay.add("TaxonPovodnost");
        this.entToDisplay.add("Vac");
        this.entToDisplay.add("Vgc");
        this.entToDisplay.add("Voucher");
    }
    
    public static DisplayEntities getInstance() {
        if (instance == null) {
            instance = new DisplayEntities();
        }
        return instance;
    }
    
    public final List<String> getEntitiesToDisplay() {
        return this.entToDisplay;
    }
    
}
