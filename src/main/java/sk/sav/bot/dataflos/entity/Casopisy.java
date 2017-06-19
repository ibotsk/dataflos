package sk.sav.bot.dataflos.entity;
// Generated May 30, 2012 11:34:04 AM by Hibernate Tools 3.2.1.GA

import sk.sav.bot.dataflos.entity.interf.Entity;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Casopisy generated by hbm2java
 */
public class Casopisy implements java.io.Serializable, Entity {

    private int id;
    private String meno;
    private String skratka;
    private boolean schvalene;
    private Set litZdrojs = new HashSet(0);

    public Casopisy() {
    }

    public Casopisy(String meno, String skratka, boolean schvalene, Set litZdrojs) {
        this.meno = meno;
        this.skratka = skratka;
        this.schvalene = schvalene;
        this.litZdrojs = litZdrojs;
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

    public String getSkratka() {
        return this.skratka;
    }

    public void setSkratka(String skratka) {
        this.skratka = skratka;
    }
    
    public boolean isSchvalene() {
        return this.schvalene;
    }

    public void setSchvalene(boolean schvalene) {
        this.schvalene = schvalene;
    }

    public Set getLitZdrojs() {
        return this.litZdrojs;
    }

    public void setLitZdrojs(Set litZdrojs) {
        this.litZdrojs = litZdrojs;
    }

    @Override
    public String toString() {
        return meno + (skratka == null ? "" : (", " + skratka));
    }

    @Override
    public List<String[]> namesAndValues() {
        //List<Tuple<String, String>> nAv = new ArrayList<>();
        List<String[]> nAv = new ArrayList<>();
        String[] s = {"Skratka", this.skratka};
        String[] m = {"Meno", this.meno};
        String[] d = {"Schválené", this.schvalene == true ? "ano" : "nie"};
        nAv.add(s);
        nAv.add(m);
        nAv.add(d);
        return nAv;
    }
}
