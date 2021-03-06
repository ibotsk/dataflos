package sk.sav.bot.dataflos.entity;
// Generated May 30, 2012 11:34:04 AM by Hibernate Tools 3.2.1.GA

import sk.sav.bot.dataflos.entity.interf.Entity;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Brumit2 generated by hbm2java
 */
public class Brumit2 implements java.io.Serializable, Entity {

    private int id;
    private String meno;
    private String iso;
    private Brumit1 brumit1;
    private Set brumit3s = new HashSet(0);

    public Brumit2() {
    }

    public Brumit2(Brumit1 brumit1, String meno) {
        this.brumit1 = brumit1;
        this.meno = meno;
    }

    public Brumit2(Brumit1 brumit1, String meno, String iso, Set brumit3s) {
        this.brumit1 = brumit1;
        this.meno = meno;
        this.iso = iso;
        this.brumit3s = brumit3s;
    }

    public int getId() {
        return this.id;
    }

    private void setId(int id) {
        this.id = id;
    }

    public Brumit1 getBrumit1() {
        return this.brumit1;
    }

    public void setBrumit1(Brumit1 brumit1) {
        this.brumit1 = brumit1;
    }

    public String getMeno() {
        return this.meno;
    }

    public void setMeno(String meno) {
        this.meno = meno;
    }

    public String getIso() {
        return this.iso;
    }

    public void setIso(String iso) {
        this.iso = iso;
    }

    public Set getBrumit3s() {
        return this.brumit3s;
    }

    public void setBrumit3s(Set brumit3s) {
        this.brumit3s = brumit3s;
    }

    @Override
    public String toString() {
        return this.iso + ", " + this.meno;
    }

    @Override
    public List<String[]> namesAndValues() {
        //List<Tuple<String, String>> nAv = new ArrayList<>();
        List<String[]> nAv = new ArrayList<>();
        String[] m = {"Meno", this.meno};
        String[] i = {"Iso", this.iso};
        String[] b = {"Brummitt 1", this.brumit1.getMeno(), "F Brumit1"};
        nAv.add(m);
        nAv.add(i);
        nAv.add(b);
        return nAv;
    }
}
