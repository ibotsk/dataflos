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
 * Ftgokres
 */
@Entity
@Table(name = "ftgokres")
public class Ftgokres implements java.io.Serializable, AssociableEntity {

	private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String meno;
    private String cislo;
    private boolean schvalene;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_obvod")
    private Ftgobvod ftgobvod;
    
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "ftgokres")
    private Set<LokalityFtgokresAsoc> lokalityFtgokresAsocs = new HashSet<>(0);

    public Ftgokres() {
    }

    public Ftgokres(Ftgobvod ftgobvod, String meno, String cislo, boolean schvalene, Set<LokalityFtgokresAsoc> lokalityFtgokresAsocs) {
        this.ftgobvod = ftgobvod;
        this.meno = meno;
        this.cislo = cislo;
        this.schvalene = schvalene;
        this.lokalityFtgokresAsocs = lokalityFtgokresAsocs;
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Ftgobvod getFtgobvod() {
        return this.ftgobvod;
    }

    public void setFtgobvod(Ftgobvod ftgobvod) {
        this.ftgobvod = ftgobvod;
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

    public boolean isSchvalene() {
        return this.schvalene;
    }

    public void setSchvalene(boolean schvalene) {
        this.schvalene = schvalene;
    }

    public Set<LokalityFtgokresAsoc> getLokalityFtgokresAsocs() {
        return this.lokalityFtgokresAsocs;
    }

    public void setLokalityFtgokresAsocs(Set<LokalityFtgokresAsoc> lokalityFtgokresAsocs) {
        this.lokalityFtgokresAsocs = lokalityFtgokresAsocs;
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
        String[] o = {"Číslo", this.cislo};
        String[] f = {"Ftg obvod", this.ftgobvod == null ? "" : this.ftgobvod.getMeno(), "F Ftgobvod"};
        String[] d = {"Schválené", this.schvalene == true ? "ano" : "nie"};
        nAv.add(m);
        nAv.add(o);
        nAv.add(f);
        nAv.add(d);
        return nAv;
    }
}
