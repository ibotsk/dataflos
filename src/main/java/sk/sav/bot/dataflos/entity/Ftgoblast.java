package sk.sav.bot.dataflos.entity;
// Generated May 30, 2012 11:34:04 AM by Hibernate Tools 3.2.1.GA

import sk.sav.bot.dataflos.entity.interf.Entity;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Ftgoblast generated by hbm2java
 */
public class Ftgoblast implements java.io.Serializable, Entity {

    private int id;
    private String meno;
    private String oblast;
    private boolean schvalene;
    private Set ftgobvods = new HashSet(0);

    public Ftgoblast() {
    }

    public Ftgoblast(String oblast, String meno, boolean schvalene, Set ftgobvods) {
        this.oblast = oblast;
        this.meno = meno;
        this.schvalene = schvalene;
        this.ftgobvods = ftgobvods;
    }

    public int getId() {
        return this.id;
    }

    private void setId(int id) {
        this.id = id;
    }

    public String getOblast() {
        return this.oblast;
    }

    public void setOblast(String oblast) {
        this.oblast = oblast;
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

    public Set getFtgobvods() {
        return this.ftgobvods;
    }

    public void setFtgobvods(Set ftgobvods) {
        this.ftgobvods = ftgobvods;
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
        String[] o = {"Oblasť", this.oblast};
        String[] d = {"Schválené", this.schvalene == true ? "ano" : "nie"};
        nAv.add(m);
        nAv.add(o);
        nAv.add(d);
        return nAv;
    }
}