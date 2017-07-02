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
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import sk.sav.bot.dataflos.entity.interf.AssociableEntity;

/**
 * Brumit3
 */
@Entity
@Table(name = "brumit3")
public class Brumit3 implements java.io.Serializable, AssociableEntity {

	private static final long serialVersionUID = 1L;
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
    private String idStr;
    private String meno;
    private String iso;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_parent")
    private Brumit2 brumit2;
    
    //@OneToMany(fetch = FetchType.LAZY, mappedBy = "")
    //private Set<Brumit4> brumit4sForIdParent = new HashSet<>(0);
    
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "brumit3")
    private Set<Brumit4> brumit4sForIdParentid = new HashSet<>(0);
    
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "brumit3")
    private Set<Lokality> lokalities = new HashSet<>(0);

    public Brumit3() {
    }

    public Brumit3(Brumit2 brumit2, String idStr, String meno) {
        this.brumit2 = brumit2;
        this.idStr = idStr;
        this.meno = meno;
    }

    public Brumit3(Brumit2 brumit2, String idStr, String meno, String iso, /*Set<Brumit4> brumit4sForIdParent,*/ Set<Brumit4> brumit4sForIdParentid, Set<Lokality> lokalities) {
        this.brumit2 = brumit2;
        this.idStr = idStr;
        this.meno = meno;
        this.iso = iso;
        //this.brumit4sForIdParent = brumit4sForIdParent;
        this.brumit4sForIdParentid = brumit4sForIdParentid;
        this.lokalities = lokalities;
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
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

    /*public Set<Brumit4> getBrumit4sForIdParent() {
        return this.brumit4sForIdParent;
    }

    public void setBrumit4sForIdParent(Set<Brumit4> brumit4sForIdParent) {
        this.brumit4sForIdParent = brumit4sForIdParent;
    }*/

    public Set<Brumit4> getBrumit4sForIdParentid() {
        return this.brumit4sForIdParentid;
    }

    public void setBrumit4sForIdParentid(Set<Brumit4> brumit4sForIdParentid) {
        this.brumit4sForIdParentid = brumit4sForIdParentid;
    }

    public Set<Lokality> getLokalities() {
        return this.lokalities;
    }

    public void setLokalities(Set<Lokality> lokalities) {
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
