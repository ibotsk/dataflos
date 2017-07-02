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
 * Exsikaty
 */
@Entity
@Table(name = "exsikaty")
public class Exsikaty implements java.io.Serializable, AssociableEntity {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String meno;
    private String cislo;
    
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "exsikaty")
    private Set<HerbarPolozky> herbarPolozkies = new HashSet<>(0);

    public Exsikaty() {
    }

    public Exsikaty(String meno, String cislo, Set<HerbarPolozky> herbarPolozkies) {
        this.meno = meno;
        this.cislo = cislo;
        this.herbarPolozkies = herbarPolozkies;
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

    public String getCislo() {
        return this.cislo;
    }

    public void setCislo(String cislo) {
        this.cislo = cislo;
    }

    public Set<HerbarPolozky> getHerbarPolozkies() {
        return this.herbarPolozkies;
    }

    public void setHerbarPolozkies(Set<HerbarPolozky> herbarPolozkies) {
        this.herbarPolozkies = herbarPolozkies;
    }
    
    @Override
    public String toString() {
        return this.cislo + ", " + this.meno;
    }

    @Override
    public List<String[]> namesAndValues() {
        //List<Tuple<String, String>> nAv = new ArrayList<>();
        List<String[]> nAv = new ArrayList<>();
        String[] m = {"Meno", this.meno};
        String[] c = {"Cislo", this.cislo};
        nAv.add(m);
        nAv.add(c);
        return nAv;
    }
}
