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
import javax.persistence.OneToMany;
import javax.persistence.Table;

import sk.sav.bot.dataflos.entity.interf.AssociableEntity;

/**
 * DallaTorre
 */
@Entity
@Table(name = "dalla_torre")
public class DallaTorre implements java.io.Serializable, AssociableEntity {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String nazovRodu;
    private String autor;
    private Genus genus;
    private Integer idEvid;
    private boolean schvalene;
    
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "dallaTorre")
    private Set<HerbarPolozky> herbarPolozkies = new HashSet<>(0);

    public DallaTorre() {
    }

    public DallaTorre(Genus genus, Integer idEvid, String nazovRodu, String autor, boolean schvalene, Set<HerbarPolozky> herbarPolozkies) {
        this.genus = genus;
        this.idEvid = idEvid;
        this.nazovRodu = nazovRodu;
        this.autor = autor;
        this.schvalene = schvalene;
        this.herbarPolozkies = herbarPolozkies;
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Genus getGenus() {
        return this.genus;
    }

    public void setGenus(Genus genus) {
        this.genus = genus;
    }

    public Integer getIdEvid() {
        return this.idEvid;
    }

    public void setIdEvid(Integer idEvid) {
        this.idEvid = idEvid;
    }

    public String getNazovRodu() {
        return this.nazovRodu;
    }

    public void setNazovRodu(String nazovRodu) {
        this.nazovRodu = nazovRodu;
    }

    public String getAutor() {
        return this.autor;
    }

    public void setAutor(String autor) {
        this.autor = autor;
    }

    public boolean isSchvalene() {
        return this.schvalene;
    }

    public void setSchvalene(boolean schvalene) {
        this.schvalene = schvalene;
    }

    public Set<HerbarPolozky> getHerbarPolozkies() {
        return this.herbarPolozkies;
    }

    public void setHerbarPolozkies(Set<HerbarPolozky> herbarPolozkies) {
        this.herbarPolozkies = herbarPolozkies;
    }

    @Override
    public String toString() {
        return this.nazovRodu + ", " + this.autor;
    }

    @Override
    public List<String[]> namesAndValues() {
        //List<Tuple<String, String>> nAv = new ArrayList<>();
        List<String[]> nAv = new ArrayList<>();
        String[] evid = {"Číslo rodu podľa autora Dalla Torre", this.idEvid.toString()};
        String[] nd = {"Rod", this.nazovRodu};
        String[] au = {"Autor", this.autor};
        String[] d = {"Schválené", this.schvalene == true ? "ano" : "nie"};
        nAv.add(evid);
        nAv.add(nd);
        nAv.add(au);
        nAv.add(d);
        return nAv;
    }
}
