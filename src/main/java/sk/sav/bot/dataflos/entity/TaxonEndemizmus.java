package sk.sav.bot.dataflos.entity;

import sk.sav.bot.dataflos.entity.interf.Entity;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * TaxonPochybnost
 */
public class TaxonEndemizmus implements java.io.Serializable, Entity {

    private int id;
    private String skratka;
	private String popis;
    private Set listOfSpecies = new HashSet(0);

    public TaxonEndemizmus() {
    }

    public TaxonEndemizmus(String skratka, String popis, Set listOfSpecies) {
        this.skratka = skratka;
		this.popis = popis;
        this.listOfSpecies = listOfSpecies;
    }

    public int getId() {
        return this.id;
    }

    private void setId(int id) {
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

    public Set getListOfSpecies() {
        return this.listOfSpecies;
    }

    public void setListOfSpecies(Set listOfSpecies) {
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
