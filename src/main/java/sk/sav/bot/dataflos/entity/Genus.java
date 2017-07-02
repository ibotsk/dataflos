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
 * Genus
 */
@Entity
@Table(name = "genus")
public class Genus implements java.io.Serializable, AssociableEntity {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String meno;
    private String autor;
    private boolean schvalene;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_family")
    private Family family;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_family_apg")
    private FamilyApg familyApg;
    
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "genus")
    private Set<ListOfSpecies> listOfSpecies = new HashSet<>(0);
    
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "genus")
    private Set<DallaTorre> dallaTorres = new HashSet<>(0);

    public Genus() {
    }

    public Genus(FamilyApg familyApg, Family family, String meno, String autor, boolean schvalene, Set<ListOfSpecies> listOfSpecies, Set<DallaTorre> dallaTorres) {
        this.familyApg = familyApg;
        this.family = family;
        this.meno = meno;
        this.autor = autor;
        this.schvalene = schvalene;
        this.listOfSpecies = listOfSpecies;
        this.dallaTorres = dallaTorres;
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public FamilyApg getFamilyApg() {
        return this.familyApg;
    }

    public void setFamilyApg(FamilyApg familyApg) {
        this.familyApg = familyApg;
    }

    public Family getFamily() {
        return this.family;
    }

    public void setFamily(Family family) {
        this.family = family;
    }

    public String getMeno() {
        return this.meno;
    }

    public void setMeno(String meno) {
        this.meno = meno;
    }

    public String getAutor() {
        return this.autor;
    }

    public void setAutor(String autor) {
        this.autor = autor;
    }

    public boolean isSchvalene() {
        return this.schvalene;
    }

    public void setSchvalene(boolean schvalene) {
        this.schvalene = schvalene;
    }

    public Set<ListOfSpecies> getListOfSpecies() {
        return this.listOfSpecies;
    }

    public void setListOfSpecies(Set<ListOfSpecies> listOfSpecies) {
        this.listOfSpecies = listOfSpecies;
    }

    public Set<DallaTorre> getDallaTorres() {
        return this.dallaTorres;
    }

    public void setDallaTorres(Set<DallaTorre> dallaTorres) {
        this.dallaTorres = dallaTorres;
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
        String[] au = {"Autor", this.autor};
        String[] c = {"Čeľaď", this.family == null ? "" : this.family.getMeno(), "F Family"};
        String[] ap = {"Apg čeľaď", this.familyApg == null ? "" : this.familyApg.getMeno(), "F FamilyApg"};
        String[] d = {"Schválené", this.schvalene == true ? "ano" : "nie"};
        nAv.add(m);
        nAv.add(au);
        nAv.add(c);
        nAv.add(ap);
        nAv.add(d);
        return nAv;
    }
}
