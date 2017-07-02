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
 * Vac 
 */
@Entity
@Table(name = "vac")
public class Vac implements java.io.Serializable, AssociableEntity {

	private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String meno;
    private boolean schvalene;
    
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "vac")
    private Set<Lokality> lokalities = new HashSet<>(0);

    public Vac() {
    }

    public Vac(String meno, boolean schvalene, Set<Lokality> lokalities) {
        this.meno = meno;
        this.schvalene = schvalene;
        this.lokalities = lokalities;
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
        String[] d = {"Schválené", this.schvalene == true ? "ano" : "nie"};
        nAv.add(m);
        nAv.add(d);
        return nAv;
    }
}
