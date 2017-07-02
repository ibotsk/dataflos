package sk.sav.bot.dataflos.entity;

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

import sk.sav.bot.dataflos.entity.interf.AssociableEntity;

/**
 * TaxonPochybnost
 */
@Entity
public class TaxonEndemizmus implements java.io.Serializable, AssociableEntity {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String skratka;
	private String popis;
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "taxonEndemizmus")
    private Set<ListOfSpecies> listOfSpecies = new HashSet<>(0);

    public TaxonEndemizmus() {
    }

    public TaxonEndemizmus(String skratka, String popis, Set<ListOfSpecies> listOfSpecies) {
        this.skratka = skratka;
		this.popis = popis;
        this.listOfSpecies = listOfSpecies;
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSkratka() {
        return this.skratka;
    }

    public void setSkratka(String skratka) {
        this.skratka = skratka;
    }
	
	public String getPopis() {
        return this.popis;
    }

    public void setPopis(String popis) {
        this.popis = popis;
    }

    public Set<ListOfSpecies> getListOfSpecies() {
        return this.listOfSpecies;
    }

    public void setListOfSpecies(Set<ListOfSpecies> listOfSpecies) {
        this.listOfSpecies = listOfSpecies;
    }

    @Override
    public String toString() {
        return skratka;
    }

    @Override
    public List<String[]> namesAndValues() {
        //List<Tuple<String, String>> nAv = new ArrayList<>();
        List<String[]> nAv = new ArrayList<>();
        String[] s = {"Skratka", this.skratka};
        String[] p = {"Popis", this.popis};
        nAv.add(s);
        nAv.add(p);
        return nAv;
    }
}
