package sk.sav.bot.dataflos.entity;
// Generated May 30, 2012 11:34:04 AM by Hibernate Tools 3.2.1.GA

import sk.sav.bot.dataflos.entity.interf.Entity;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * FamilyApg generated by hbm2java
 */
public class FamilyApg implements java.io.Serializable, Entity {

    private int id;
    private String meno;
    private boolean schvalene;
    private Set genuses = new HashSet(0);

    public FamilyApg() {
    }

    public FamilyApg(boolean schvalene) {
        this.schvalene = schvalene;
    }

    public FamilyApg(String meno, boolean schvalene, Set genuses) {
        this.meno = meno;
        this.schvalene = schvalene;
        this.genuses = genuses;
    }

    public int getId() {
        return this.id;
    }

    private void setId(int id) {
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

    public Set getGenuses() {
        return this.genuses;
    }

    public void setGenuses(Set genuses) {
        this.genuses = genuses;
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
        String[] d = {"Schválené", this.schvalene == true ? "ano" : "nie"};
        nAv.add(m);
        nAv.add(d);
        return nAv;
    }
}
