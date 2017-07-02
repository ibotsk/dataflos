package sk.sav.bot.dataflos.entity;
// Generated May 30, 2012 11:34:04 AM by Hibernate Tools 3.2.1.GA

import sk.sav.bot.dataflos.entity.interf.AssociableEntity;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

/**
 * Voucher
 */
@Entity
public class Voucher implements java.io.Serializable, AssociableEntity {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    
    private String menoAutora;
    private Integer rokPubl;
    private String kodLokality;
    private String skrCas;
    private String strany;
    private String poznamka;
    
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "voucher")
    private Set<HerbarPolozky> herbarPolozkies = new HashSet<>(0);

    public Voucher() {
    }

    public Voucher(String menoAutora, Integer rokPubl, String skrCas, String strany, String kodLokality, String poznamka, Set<HerbarPolozky> herbarPolozkies) {
        this.menoAutora = menoAutora;
        this.rokPubl = rokPubl;
        this.skrCas = skrCas;
        this.strany = strany;
        this.kodLokality = kodLokality;
        this.poznamka = poznamka;
        this.herbarPolozkies = herbarPolozkies;
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMenoAutora() {
        return this.menoAutora;
    }

    public void setMenoAutora(String menoAutora) {
        this.menoAutora = menoAutora;
    }

    public Integer getRokPubl() {
        return this.rokPubl;
    }

    public void setRokPubl(Integer rokPubl) {
        this.rokPubl = rokPubl;
    }

    public String getSkrCas() {
        return this.skrCas;
    }

    public void setSkrCas(String skrCas) {
        this.skrCas = skrCas;
    }

    public String getStrany() {
        return this.strany;
    }

    public void setStrany(String strany) {
        this.strany = strany;
    }

    public String getKodLokality() {
        return this.kodLokality;
    }

    public void setKodLokality(String kodLokality) {
        this.kodLokality = kodLokality;
    }

    public String getPoznamka() {
        return this.poznamka;
    }

    public void setPoznamka(String poznamka) {
        this.poznamka = poznamka;
    }

    public Set<HerbarPolozky> getHerbarPolozkies() {
        return this.herbarPolozkies;
    }

    public void setHerbarPolozkies(Set<HerbarPolozky> herbarPolozkies) {
        this.herbarPolozkies = herbarPolozkies;
    }

    @Override
    public String toString() {
        return this.kodLokality + ", " + this.menoAutora;
    }

    @Override
    public List<String[]> namesAndValues() {
        //List<Tuple<String, String>> nAv = new ArrayList<>();
        List<String[]> nAv = new ArrayList<>();
        String[] m = {"Meno autora", this.menoAutora == null ? "" : this.menoAutora};
        String[] r = {"Rok publikácie", this.rokPubl == null ? "" : this.rokPubl.toString()};
        String[] k = {"Kód lokality", this.kodLokality == null ? "" : this.kodLokality};
        String[] sk = {"Skratka časopisu", this.skrCas == null ? "" : this.skrCas};
        String[] st = {"Strany", this.strany == null ? "" : this.strany};
        String[] p = {"Poznámka", this.poznamka == null ? "" : this.poznamka};
        nAv.add(m);
        nAv.add(r);
        nAv.add(k);
        nAv.add(sk);
        nAv.add(st);
        nAv.add(p);
        return nAv;
    }
}
