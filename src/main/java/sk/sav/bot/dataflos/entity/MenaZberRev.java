package sk.sav.bot.dataflos.entity;
// Generated May 30, 2012 11:34:04 AM by Hibernate Tools 3.2.1.GA

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import sk.sav.bot.dataflos.entity.interf.AssociableEntity;
import sk.sav.bot.dataflos.entity.interf.PeopleAsoc;

/**
 * MenaZberRev
 */
@Entity
@Table(name = "mena_zber_rev")
public class MenaZberRev implements java.io.Serializable, AssociableEntity {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    
    private String meno;
    private String stdMeno;
    //private int uroven;
    private boolean schvalene;
    
    
    private Set<PeopleAsoc> udajZberAsocs = new HashSet<>(0);
    private Set<PeopleAsoc> skupRevDets = new HashSet<>(0);

    private Set<PeopleAsoc> lzdrojAutoriAsocs = new HashSet<>(0);
    private Set<PeopleAsoc> lzdrojEditoriAsocs = new HashSet<>(0);
    
    public MenaZberRev() {
    }

//    public MenaZberRev(int id, int uroven) {
//        this.id = id;
//        this.uroven = uroven;
//    }

    public MenaZberRev(int id, String meno, boolean schvalene,/* int uroven,*/ 
    		String stdMeno, Set<PeopleAsoc> udajZberAsocs, Set<PeopleAsoc> skupRevDets) {
        this.id = id;
        this.meno = meno;
        this.schvalene = schvalene;
        //this.uroven = uroven;
        this.stdMeno = stdMeno;
        this.udajZberAsocs = udajZberAsocs;
        this.skupRevDets = skupRevDets;
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

//    public int getUroven() {
//        return this.uroven;
//    }
//
//    public void setUroven(int uroven) {
//        this.uroven = uroven;
//    }

    public String getStdMeno() {
        return this.stdMeno;
    }

    public void setStdMeno(String stdMeno) {
        this.stdMeno = stdMeno;
    }

    public Set<PeopleAsoc> getUdajZberAsocs() {
        return this.udajZberAsocs;
    }

    public void setUdajZberAsocs(Set<PeopleAsoc> udajZberAsocs) {
        this.udajZberAsocs = udajZberAsocs;
    }

    public Set<PeopleAsoc> getSkupRevDets() {
        return this.skupRevDets;
    }

    public void setSkupRevDets(Set<PeopleAsoc> skupRevDets) {
        this.skupRevDets = skupRevDets;
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
        return this.meno;
    }

    @Override
    public List<String[]> namesAndValues() {
        //List<Tuple<String, String>> nAv = new ArrayList<>();
        List<String[]> nAv = new ArrayList<>();
        String[] m = {"Meno", this.meno};
        String[] st = {"Štandardizované meno", this.stdMeno};
        //String[] u = {"Úroveň", String.valueOf(this.uroven)};
        String[] d = {"Schválené", this.schvalene == true ? "ano" : "nie"};
        nAv.add(m);
        nAv.add(st);
        //nAv.add(u);
        nAv.add(d);
        return nAv;
    }
}
