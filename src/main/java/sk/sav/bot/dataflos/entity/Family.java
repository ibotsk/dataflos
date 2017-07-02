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
 * Family
 */
@Entity
@Table(name = "family")
public class Family implements java.io.Serializable, AssociableEntity {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String meno;
    private boolean schvalene;
    
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "family")
    private Set<Genus> genera = new HashSet<>(0);
    
    //@OneToMany(fetch = FetchType.LAZY, mappedBy = "family")
    //private Set<ListOfSpecies> listOfSpecieses = new HashSet<>(0);

    public Family() {
    }

    public Family(String meno, boolean schvalene, Set<Genus> genera/*, Set listOfSpecieses*/) {
        this.meno = meno;
        this.schvalene = schvalene;
        this.genera = genera;
        //this.listOfSpecieses = listOfSpecieses;
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMeno() {
        return this.meno;
    }

    public void setMeno(String meno) {
        this.meno = meno;
    }

    public boolean isSchvalene() {
        return this.schvalene;
    }

    public void setSchvalene(boolean schvalene) {
        this.schvalene = schvalene;
    }

    public Set<Genus> getGenera() {
        return this.genera;
    }

    public void setGenera(Set<Genus> genera) {
        this.genera = genera;
    }

    /*public Set<ListOfSpecies> getListOfSpecieses() {
        return this.listOfSpecieses;
    }

    public void setListOfSpecieses(Set<ListOfSpecies> listOfSpecieses) {
        this.listOfSpecieses = listOfSpecieses;
    }*/
    
    @Override
    public String toString() {
        return this.meno;
    }

    @Override
    public List<String[]> namesAndValues() {
        //List<Tuple<String, String>> nAv = new ArrayList<>();
        List<String[]> nAv = new ArrayList<>();
        String[] m = {"Meno", this.meno};
        String[] d = {"Schválené", this.schvalene == true ? "ano" : "nie"};
        nAv.add(m);
        nAv.add(d);
        return nAv;
    }
}
