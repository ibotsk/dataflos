package sk.sav.bot.dataflos.entity;
// Generated May 30, 2012 11:34:04 AM by Hibernate Tools 3.2.1.GA

import sk.sav.bot.dataflos.entity.interf.Entity;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * ListOfSpecies generated by hbm2java
 */
public class ListOfSpecies implements java.io.Serializable, Entity {

    private int id;
    private Character typ;
    private String meno;
    private String autori;
    private ListOfSpecies listOfSpecies;
    private Genus genus;
    //private Family family;
    private TaxonPochybnost taxonPochybnost;
    private boolean taxonOchrana;
    private TaxonOhrozenost taxonOhrozenost;
    private TaxonEndemizmus taxonEndemizmus;
    private TaxonPovodnost taxonPovodnost;
    private String taxonSvkNazov;
    private boolean schvalene;
    private Set listOfSpecieses = new HashSet(0);
    private Set menaTaxonovs = new HashSet(0);

    public ListOfSpecies() {
    }

    public ListOfSpecies(boolean schvalene) {
        this.schvalene = schvalene;
    }

    public ListOfSpecies(Genus genus, ListOfSpecies listOfSpecies, Character typ, String meno, String autori, boolean schvalene, Set listOfSpecieses, Set menaTaxonovs, TaxonPochybnost taxonPochybnost, TaxonOhrozenost taxonOhrozenost, TaxonEndemizmus taxonEndemizmus, TaxonPovodnost taxonPovodnost) {
        this.genus = genus;
        this.listOfSpecies = listOfSpecies;
        this.typ = typ;
        this.meno = meno;
        this.autori = autori;
        this.schvalene = schvalene;
        this.listOfSpecieses = listOfSpecieses;
        this.menaTaxonovs = menaTaxonovs;
        this.taxonPochybnost = taxonPochybnost;
        this.taxonOhrozenost = taxonOhrozenost;
        this.taxonEndemizmus = taxonEndemizmus;
        this.taxonPovodnost = taxonPovodnost;         
    }

    public int getId() {
        return this.id;
    }

    private void setId(int id) {
        this.id = id;
    }

    public Genus getGenus() {
        return this.genus;
    }

    public void setGenus(Genus genus) {
        this.genus = genus;
    }

    public ListOfSpecies getListOfSpecies() {
        return this.listOfSpecies;
    }

    public void setListOfSpecies(ListOfSpecies listOfSpecies) {
        this.listOfSpecies = listOfSpecies;
    }

    public Character getTyp() {
        return this.typ;
    }

    public void setTyp(Character typ) {
        this.typ = typ;
    }

    public String getMeno() {
        return this.meno;
    }

    public void setMeno(String meno) {
        this.meno = meno;
    }

    public String getAutori() {
        return this.autori;
    }

    public void setAutori(String autori) {
        this.autori = autori;
    }

    public boolean isSchvalene() {
        return this.schvalene;
    }

    public void setSchvalene(boolean schvalene) {
        this.schvalene = schvalene;
    }

    public Set getListOfSpecieses() {
        return this.listOfSpecieses;
    }

    public void setListOfSpecieses(Set listOfSpecieses) {
        this.listOfSpecieses = listOfSpecieses;
    }

    public Set getMenaTaxonovs() {
        return this.menaTaxonovs;
    }

    public void setMenaTaxonovs(Set menaTaxonovs) {
        this.menaTaxonovs = menaTaxonovs;
    }
    
    /**
     * @return the taxonPochybnost
     */
    public TaxonPochybnost getTaxonPochybnost() {
        return taxonPochybnost;
    }

    /**
     * @param taxonPochybnost the taxonPochybnost to set
     */
    public void setTaxonPochybnost(TaxonPochybnost taxonPochybnost) {
        this.taxonPochybnost = taxonPochybnost;
    }

    /**
     * @return the taxonOhrozenost
     */
    public TaxonOhrozenost getTaxonOhrozenost() {
        return taxonOhrozenost;
    }

    /**
     * @param taxonOhrozenost the taxonOhrozenost to set
     */
    public void setTaxonOhrozenost(TaxonOhrozenost taxonOhrozenost) {
        this.taxonOhrozenost = taxonOhrozenost;
    }

    /**
     * @return the taxonEndemizmus
     */
    public TaxonEndemizmus getTaxonEndemizmus() {
        return taxonEndemizmus;
    }

    /**
     * @param taxonEndemizmus the taxonEndemizmus to set
     */
    public void setTaxonEndemizmus(TaxonEndemizmus taxonEndemizmus) {
        this.taxonEndemizmus = taxonEndemizmus;
    }

    /**
     * @return the taxonPovodnost
     */
    public TaxonPovodnost getTaxonPovodnost() {
        return taxonPovodnost;
    }

    /**
     * @param taxonPovodnost the taxonPovodnost to set
     */
    public void setTaxonPovodnost(TaxonPovodnost taxonPovodnost) {
        this.taxonPovodnost = taxonPovodnost;
    }
    
        /**
     * @return the taxonOchrana
     */
    public boolean isTaxonOchrana() {
        return taxonOchrana;
    }

    /**
     * @param taxonOchrana the taxonOchrana to set
     */
    public void setTaxonOchrana(boolean taxonOchrana) {
        this.taxonOchrana = taxonOchrana;
    }

    /**
     * @return the taxonSvkNazov
     */
    public String getTaxonSvkNazov() {
        return taxonSvkNazov;
    }

    /**
     * @param taxonSvkNazov the taxonSvkNazov to set
     */
    public void setTaxonSvkNazov(String taxonSvkNazov) {
        this.taxonSvkNazov = taxonSvkNazov;
    }

    @Override
    public String toString() {
        if (this.autori != null){
            return this.meno + " " + this.autori;
        } else {
            return this.meno;
        }
    }
    
    @Override
    public List<String[]> namesAndValues() {
        //List<Tuple<String, String>> nAv = new ArrayList<>();
        List<String[]> nAv = new ArrayList<>();
        String[] t = {"Typ", String.valueOf(this.typ), "C D,S"};
        String[] m = {"Meno", this.meno};
        String[] au = {"Autori", this.autori};
        String[] ac = {"Akceptované meno", this.listOfSpecies == null ? "" : this.listOfSpecies.getMeno(), "F ListOfSpecies"};
        String[] r = {"Rod", this.genus == null ? "" : this.genus.getMeno(), "F Genus"};
        String[] poch = {"Pochybnosť", this.taxonPochybnost == null ? "" : this.taxonPochybnost.getSkratka(), "F TaxonPochybnost"};
        String[] ochr = {"Ochrana", this.taxonOchrana == true ? "áno" : "nie"};
        String[] ohr = {"Ohrozenosť", this.taxonOhrozenost == null ? "" : this.taxonOhrozenost.getSkratka(), "F TaxonOhrozenost"};
        String[] end = {"Endemizmus", this.taxonEndemizmus == null ? "" : this.taxonEndemizmus.getSkratka(), "F TaxonEndemizmus"};
        String[] pov = {"Pôvodnosť", this.taxonPovodnost == null ? "" : this.taxonPovodnost.getSkratka(), "F TaxonPovodnost"};
        String[] svk = {"Slovenský názov", this.taxonSvkNazov};
        String[] d = {"Schválené", this.schvalene == true ? "ano" : "nie"};
        nAv.add(t);
        nAv.add(m);
        nAv.add(au);
        nAv.add(ac);
        nAv.add(r);
        nAv.add(poch);
        nAv.add(ochr);
        nAv.add(ohr);
        nAv.add(end);
        nAv.add(pov);
        nAv.add(svk);
        nAv.add(d);
        return nAv;
    }


}
