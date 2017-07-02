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
 * Brumit1
 */
@Entity
@Table(name = "brumit1")
public class Brumit1 implements java.io.Serializable, AssociableEntity {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String meno;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "brumit1")
    private Set<Brumit2> brumit2s = new HashSet<>(0);

    public Brumit1() {
    }

    public Brumit1(String meno) {
        this.meno = meno;
    }

    public Brumit1(String meno, Set<Brumit2> brumit2s) {
        this.meno = meno;
        this.brumit2s = brumit2s;
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

    public Set<Brumit2> getBrumit2s() {
        return this.brumit2s;
    }

    public void setBrumit2s(Set<Brumit2> brumit2s) {
        this.brumit2s = brumit2s;
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
        nAv.add(m);
        return nAv;
    }
}
