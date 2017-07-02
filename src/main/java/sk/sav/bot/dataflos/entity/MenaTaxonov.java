package sk.sav.bot.dataflos.entity;
// Generated May 30, 2012 11:34:04 AM by Hibernate Tools 3.2.1.GA

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
 * MenaTaxonov
 */
@Entity
@Table(name = "mena_taxonov")
public class MenaTaxonov implements java.io.Serializable, AssociableEntity {

	private static final long serialVersionUID = 1L;
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
	
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_std_meno")
    private ListOfSpecies species;
	
    private String menoScheda;
    private String poznamka;
    
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "menoTaxonu")
    private Set<SkupRev> skupRevs = new HashSet<>(0);

    public MenaTaxonov() {
    }

    public MenaTaxonov(ListOfSpecies species, String menoScheda, String poznamka, Set<SkupRev> skupRevs) {
        this.species = species;
        this.menoScheda = menoScheda;
        this.poznamka = poznamka;
        this.skupRevs = skupRevs;
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public ListOfSpecies getSpecies() {
        return this.species;
    }

    public void setSpecies(ListOfSpecies species) {
        this.species = species;
    }

    public String getMenoScheda() {
        return this.menoScheda;
    }

    public void setMenoScheda(String menoScheda) {
        this.menoScheda = menoScheda;
    }

    public String getPoznamka() {
        return this.poznamka;
    }

    public void setPoznamka(String poznamka) {
        this.poznamka = poznamka;
    }

    public Set<SkupRev> getSkupRevs() {
        return this.skupRevs;
    }

    public void setSkupRevs(Set<SkupRev> skupRevs) {
        this.skupRevs = skupRevs;
    }

    @Override
    public List<String[]> namesAndValues() {
        return null;
    }
    
    @Override
    public String toString() {
        return this.menoScheda;
    }

    public MenaTaxonov replicate() {
        MenaTaxonov mt = new MenaTaxonov();
        mt.setSpecies(this.species);
        mt.setMenoScheda(this.menoScheda);
        mt.setPoznamka(this.poznamka);
        return mt;
    }
}
