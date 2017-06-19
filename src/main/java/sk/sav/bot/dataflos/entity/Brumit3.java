package sk.sav.bot.dataflos.entity;
// Generated May 30, 2012 11:34:04 AM by Hibernate Tools 3.2.1.GA

import sk.sav.bot.dataflos.entity.interf.Entity;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Brumit3 generated by hbm2java
 */
public class Brumit3 implements java.io.Serializable, Entity {

    private int id;
    private String idStr;
    private String meno;
    private String iso;
    private Brumit2 brumit2;
    private Set brumit4sForIdParent = new HashSet(0);
    private Set brumit4sForIdParentid = new HashSet(0);
    private Set lokalities = new HashSet(0);

    public Brumit3() {
    }

    public Brumit3(Brumit2 brumit2, String idStr, String meno) {
        this.brumit2 = brumit2;
        this.idStr = idStr;
        this.meno = meno;
    }

    public Brumit3(Brumit2 brumit2, String idStr, String meno, String iso, Set brumit4sForIdParent, Set brumit4sForIdParentid, Set lokalities) {
        this.brumit2 = brumit2;
        this.idStr = idStr;
        this.meno = meno;
        this.iso = iso;
        this.brumit4sForIdParent = brumit4sForIdParent;
        this.brumit4sForIdParentid = brumit4sForIdParentid;
        this.lokalities = lokalities;
    }

    public int getId() {
        return this.id;
    }

    private void setId(int id) {
        this.id = id;
    }

    public Brumit2 getBrumit2() {
        return this.brumit2;
    }

    public void setBrumit2(Brumit2 brumit2) {
        this.brumit2 = brumit2;
    }

    public String getIdStr() {
        return this.idStr;
    }

    public void setIdStr(String idStr) {
        this.idStr = idStr;
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

    public Set getBrumit4sForIdParent() {
        return this.brumit4sForIdParent;
    }

    public void setBrumit4sForIdParent(Set brumit4sForIdParent) {
        this.brumit4sForIdParent = brumit4sForIdParent;
    }

    public Set getBrumit4sForIdParentid() {
        return this.brumit4sForIdParentid;
    }

    public void setBrumit4sForIdParentid(Set brumit4sForIdParentid) {
        this.brumit4sForIdParentid = brumit4sForIdParentid;
    }

    public Set getLokalities() {
        return this.lokalities;
    }

    public void setLokalities(Set lokalities) {
        this.lokalities = lokalities;
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
        String[] b = {"Brummitt 2", this.brumit2.getMeno(), "F Brumit2"};
        nAv.add(m);
        nAv.add(i);
        nAv.add(b);
        return nAv;
    }
}