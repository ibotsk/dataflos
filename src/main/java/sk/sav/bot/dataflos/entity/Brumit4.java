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
 * Brumit4
 */
@Entity
@Table(name = "brumit4")
public class Brumit4 implements java.io.Serializable, AssociableEntity {

	private static final long serialVersionUID = 1L;
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String idStr;
    private String meno;
    private String iso;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_parentid")
    private Brumit3 brumit3;
    
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "brumit4")
    private Set<Lokality> lokalities = new HashSet<>(0);

    public Brumit4() {
    }

    public Brumit4(Brumit3 brumit3, String idStr, String meno) {
        this.brumit3 = brumit3;
        this.idStr = idStr;
        this.meno = meno;
    }

    public Brumit4(Brumit3 brumit3ByIdParentid, String idStr, String meno, String iso, Set<Lokality> lokalities) {
        this.brumit3 = brumit3ByIdParentid;
        this.idStr = idStr;
        this.meno = meno;
        this.iso = iso;
        this.lokalities = lokalities;
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Brumit3 getBrumit3() {
        return this.brumit3;
    }

    public void setBrumit3(Brumit3 brumit3) {
        this.brumit3 = brumit3;
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

    public Set<Lokality> getLokalities() {
        return this.lokalities;
    }

    public void setLokalities(Set<Lokality> lokalities) {
        this.lokalities = lokalities;
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
        String[] i = {"Iso", this.iso};
        String[] b = {"Brummitt 3", this.brumit3.getMeno(), "F Brumit3"};
        nAv.add(m);
        nAv.add(i);
        nAv.add(b);
        return nAv;
    }
}
