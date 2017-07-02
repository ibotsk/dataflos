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
 * Herbar
 */
@Entity
@Table(name = "herbar")
public class Herbar implements java.io.Serializable, AssociableEntity {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String skratkaHerb;
    private String institucia;
    private boolean schvalene;
    
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "herbar")
    private Set<HerbarPolozky> herbarPolozkies = new HashSet<>(0);

    public Herbar() {
    }

    public Herbar(String skratkaHerb, String institucia, boolean schvalene, Set<HerbarPolozky> herbarPolozkies) {
        this.skratkaHerb = skratkaHerb;
        this.institucia = institucia;
        this.schvalene = schvalene;
        this.herbarPolozkies = herbarPolozkies;
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSkratkaHerb() {
        return this.skratkaHerb;
    }

    public void setSkratkaHerb(String skratkaHerb) {
        this.skratkaHerb = skratkaHerb;
    }

    public String getInstitucia() {
        return this.institucia;
    }

    public void setInstitucia(String institucia) {
        this.institucia = institucia;
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
        return this.skratkaHerb + ", " + this.institucia;
    }
    
    @Override
    public List<String[]> namesAndValues() {
        //List<Tuple<String, String>> nAv = new ArrayList<>();
        List<String[]> nAv = new ArrayList<>();
        String[] sk = {"Skratka herbára", this.skratkaHerb};
        String[] i = {"Inštitúcia", this.institucia};
        String[] d = {"Schválené", this.schvalene == true ? "ano" : "nie"};
        nAv.add(sk);
        nAv.add(i);
        nAv.add(d);
        return nAv;
    }
}
