package sk.sav.bot.dataflos.entity;
// Generated May 30, 2012 11:34:04 AM by Hibernate Tools 3.2.1.GA

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import sk.sav.bot.dataflos.entity.interf.AssociableEntity;
import sk.sav.bot.dataflos.entity.interf.PeopleAsoc;

/**
 * LitZdroj
 */
@Entity
@Table(name = "lit_zdroj")
public class LitZdroj implements java.io.Serializable, AssociableEntity {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_casopis")
    private Casopisy casopis;
    
    private String kod;
    private Character typ;
    private String nazovClanku;
    private String nazovClankuPreklad;
    private String pramen;
    private String rocnik;
    private String cislo;
    private String vydavatel;
    private String rok;
    private String strany;
    private String poznamka;
    private String pocetZaznamov;
    private boolean komplet;
    private boolean fotka;
    private String nazovKnihy;
    private boolean mapaRozsirenia;
    private String nazovKapitoly;
    private String referencia;
    
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "litZdroj")
    private Set<Udaj> udajs = new HashSet<>(0);
    
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "litZdroj")
    private Set<LitZdrojRev> litZdrojRevs = new HashSet<>(0);
    
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "litZdroj")
    private Set<PeopleAsoc> lzdrojAutoriAsocs = new HashSet<>(0);
    
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "litZdroj")
    private Set<PeopleAsoc> lzdrojEditoriAsocs = new HashSet<>(0);

    public LitZdroj() {
    }

    public LitZdroj(boolean fotka) {
        this.fotka = fotka;
    }

    public LitZdroj(Casopisy casopis, String kod, Character typ, String nazovClanku, String nazovClankuPreklad, 
    		String pramen, String rocnik, String cislo, String vydavatel, String rok, String strany, String poznamka, 
    		String pocetZaznamov, boolean komplet, boolean fotka, String nazovKnihy, boolean mapaRozsirenia, 
    		String nazovKapitoly, String referencia, Set<Udaj> udajs) {
        this.casopis = casopis;
        this.kod = kod;
        this.typ = typ;
        this.nazovClanku = nazovClanku;
        this.nazovClankuPreklad = nazovClankuPreklad;
        this.pramen = pramen;
        this.rocnik = rocnik;
        this.cislo = cislo;
        this.vydavatel = vydavatel;
        this.rok = rok;
        this.strany = strany;
        this.poznamka = poznamka;
        this.pocetZaznamov = pocetZaznamov;
        this.komplet = komplet;
        this.fotka = fotka;
        this.nazovKnihy = nazovKnihy;
        this.mapaRozsirenia = mapaRozsirenia;
        this.nazovKapitoly = nazovKapitoly;
        this.referencia = referencia;
        this.udajs = udajs;
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Casopisy getCasopis() {
        return this.casopis;
    }

    public void setCasopis(Casopisy casopisy) {
        this.casopis = casopisy;
    }

    public String getKod() {
        return this.kod;
    }

    public void setKod(String kod) {
        this.kod = kod;
    }

    public Character getTyp() {
        return this.typ;
    }

    public void setTyp(Character typ) {
        this.typ = typ;
    }

    public String getNazovClanku() {
        return this.nazovClanku;
    }

    public void setNazovClanku(String nazovClanku) {
        this.nazovClanku = nazovClanku;
    }

    public String getNazovClankuPreklad() {
        return this.nazovClankuPreklad;
    }

    public void setNazovClankuPreklad(String nazovClankuPreklad) {
        this.nazovClankuPreklad = nazovClankuPreklad;
    }

    public String getPramen() {
        return this.pramen;
    }

    public void setPramen(String pramen) {
        this.pramen = pramen;
    }

    public String getRocnik() {
        return this.rocnik;
    }

    public void setRocnik(String rocnik) {
        this.rocnik = rocnik;
    }

    public String getCislo() {
        return this.cislo;
    }

    public void setCislo(String cislo) {
        this.cislo = cislo;
    }

    public String getVydavatel() {
        return this.vydavatel;
    }

    public void setVydavatel(String vydavatel) {
        this.vydavatel = vydavatel;
    }

    public String getRok() {
        return this.rok;
    }

    public void setRok(String rok) {
        this.rok = rok;
    }

    public String getStrany() {
        return this.strany;
    }

    public void setStrany(String strany) {
        this.strany = strany;
    }

    public String getPoznamka() {
        return this.poznamka;
    }

    public void setPoznamka(String poznamka) {
        this.poznamka = poznamka;
    }

    public String getPocetZaznamov() {
        return this.pocetZaznamov;
    }

    public void setPocetZaznamov(String pocetZaznamov) {
        this.pocetZaznamov = pocetZaznamov;
    }

    public boolean isKomplet() {
        return this.komplet;
    }

    public void setKomplet(boolean komplet) {
        this.komplet = komplet;
    }

    public boolean isFotka() {
        return this.fotka;
    }

    public void setFotka(boolean fotka) {
        this.fotka = fotka;
    }

    public String getNazovKnihy() {
        return this.nazovKnihy;
    }

    public void setNazovKnihy(String nazovKnihy) {
        this.nazovKnihy = nazovKnihy;
    }

    public boolean isMapaRozsirenia() {
        return this.mapaRozsirenia;
    }

    public void setMapaRozsirenia(boolean mapaRozsirenia) {
        this.mapaRozsirenia = mapaRozsirenia;
    }

    public String getNazovKapitoly() {
        return this.nazovKapitoly;
    }

    public void setNazovKapitoly(String nazovKapitoly) {
        this.nazovKapitoly = nazovKapitoly;
    }

    public String getReferencia() {
        return this.referencia;
    }

    public void setReferencia(String referencia) {
        this.referencia = referencia;
    }

    public Set<Udaj> getUdajs() {
        return this.udajs;
    }

    public void setUdajs(Set<Udaj> udajs) {
        this.udajs = udajs;
    }

    public Set<LitZdrojRev> getLitZdrojRevs() {
        return litZdrojRevs;
    }

    public void setLitZdrojRevs(Set<LitZdrojRev> litZdrojRevs) {
        this.litZdrojRevs = litZdrojRevs;
    }

    public Set<PeopleAsoc> getLzdrojAutoriAsocs() {
        return lzdrojAutoriAsocs;
    }

    public void setLzdrojAutoriAsocs(Set<PeopleAsoc> lzdrojAutoriAsocs) {
        this.lzdrojAutoriAsocs = lzdrojAutoriAsocs;
    }

    public Set<PeopleAsoc> getLzdrojEditoriAsocs() {
        return lzdrojEditoriAsocs;
    }

    public void setLzdrojEditoriAsocs(Set<PeopleAsoc> lzdrojEditoriAsocs) {
        this.lzdrojEditoriAsocs = lzdrojEditoriAsocs;
    }

    @Override
    public String toString() {
        return String.valueOf(this.id);
    }
    
    public LitZdroj replicate() {
        LitZdroj lz = new LitZdroj(casopis, kod, typ, nazovClanku, nazovClankuPreklad, pramen, rocnik, cislo, vydavatel, rok, strany, poznamka, pocetZaznamov, komplet, fotka, nazovKnihy, mapaRozsirenia, nazovKapitoly, referencia, udajs);
        return lz;
    }

    @Override
    public List<String[]> namesAndValues() {
        //List<Tuple<String, String>> nAv = new ArrayList<>();
        List<String[]> nAv = new ArrayList<>();
        //String[] t = {"Typ", String.valueOf(this.typ), "C D,S"};
        String[] ncl = {"Názov článku", this.nazovClanku == null ? "" : this.nazovClanku};
        String[] nkap = {"Názov kaapitoly", this.nazovKapitoly == null ? "" : this.nazovKapitoly};
        String[] nkn = {"Názov knihy", this.nazovKnihy == null ? "" : this.nazovKnihy};
        String[] ncas = {"Názov časopisu", this.getCasopis() == null ? "" : this.getCasopis().getMeno()};
        String[] r = {"Rok vydania", this.rok == null ? "" : this.rok.toString()};
        String[] vyd = {"Vydavateľ", this.vydavatel == null ? "" : this.vydavatel};
        //nAv.add(t);
        nAv.add(ncl);
        nAv.add(nkap);
        nAv.add(nkn);
        nAv.add(ncas);
        nAv.add(r);
        nAv.add(vyd);
        return nAv;
    }
}
