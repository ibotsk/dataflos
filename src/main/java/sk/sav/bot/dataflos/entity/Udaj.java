package sk.sav.bot.dataflos.entity;
// Generated May 30, 2012 11:34:04 AM by Hibernate Tools 3.2.1.GA

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
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
import sk.sav.bot.dataflos.entity.interf.PeopleAsoc;
import sk.sav.bot.dataflos.util.ComparatorObr;

/**
 * Udaj
 */
@Entity
@Table(name = "udaj")
public class Udaj implements java.io.Serializable, AssociableEntity {
	
	private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_lit")
    private LitZdroj litZdroj;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_lok")
    private Lokality lokality;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_herb_polozka")
    private HerbarPolozky herbarPolozky;
    private Character typ;
    private String datumZberu;
    private String stranaUdaja;
    private String datumZberuSlovom;
    private boolean verejnePristupny;
    private String verejnePristupnyOd;
    private String uzivatel;
    
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "udaj")
    private Set<SkupRev> skupRevs = new HashSet<>(0);
    
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "udaj")
    private Set<UdajObrazky> udajObrazkies = new HashSet<>(0);
    
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "udaj")
    private Set<PeopleAsoc> udajZberAsocs = new HashSet<>(0);
    
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "udaj")
    private Set<LitZdrojRev> litZdrojRevs = new HashSet<>(0);

    public Udaj() {
    }

    public Udaj(boolean verejnePristupny) {
        this.verejnePristupny = verejnePristupny;
    }

    public Udaj(LitZdroj litZdroj, Lokality lokality, HerbarPolozky herbarPolozky, Character typ, 
    		String datumZberu, String stranaUdaja, String datumZberuSlovom, boolean verejnePristupny, 
    		String uzivatel, Set<SkupRev> skupRevs, Set<UdajObrazky> udajObrazkies, Set<PeopleAsoc> udajZberAsocs) {
        this.litZdroj = litZdroj;
        this.lokality = lokality;
        this.herbarPolozky = herbarPolozky;
        this.typ = typ;
        this.datumZberu = datumZberu;
        this.stranaUdaja = stranaUdaja;
        this.datumZberuSlovom = datumZberuSlovom;
        this.verejnePristupny = verejnePristupny;
        this.uzivatel = uzivatel;
        this.skupRevs = skupRevs;
        this.udajObrazkies = udajObrazkies;
        this.udajZberAsocs = udajZberAsocs;
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public LitZdroj getLitZdroj() {
        return this.litZdroj;
    }

    public void setLitZdroj(LitZdroj litZdroj) {
        this.litZdroj = litZdroj;
    }

    public Lokality getLokality() {
        return this.lokality;
    }

    public void setLokality(Lokality lokality) {
        this.lokality = lokality;
    }

    public HerbarPolozky getHerbarPolozky() {
        return this.herbarPolozky;
    }

    public void setHerbarPolozky(HerbarPolozky herbarPolozky) {
        this.herbarPolozky = herbarPolozky;
    }

    public Character getTyp() {
        return this.typ;
    }

    public void setTyp(Character typ) {
        this.typ = typ;
    }

    public String getDatumZberu() {
        return this.datumZberu;
    }

    public void setDatumZberu(String datumZberu) {
        this.datumZberu = datumZberu;
    }

    public String getStranaUdaja() {
        return this.stranaUdaja;
    }

    public void setStranaUdaja(String stranaUdaja) {
        this.stranaUdaja = stranaUdaja;
    }

    public String getDatumZberuSlovom() {
        return this.datumZberuSlovom;
    }

    public void setDatumZberuSlovom(String datumZberuSlovom) {
        this.datumZberuSlovom = datumZberuSlovom;
    }

    public boolean isVerejnePristupny() {
        return this.verejnePristupny;
    }

    public void setVerejnePristupny(boolean verejnePristupny) {
        this.verejnePristupny = verejnePristupny;
    }
    
    public String getVerejnePristupnyOd() {
        return this.verejnePristupnyOd;
    }

    public void setVerejnePristupnyOd(String verejnePristupnyOd) {
        this.verejnePristupnyOd = verejnePristupnyOd;
    }
    
    public String getUzivatel() {
        return uzivatel;
    }

    public void setUzivatel(String uzivatel) {
        this.uzivatel = uzivatel;
    }

    public Set<SkupRev> getSkupRevs() {
        return this.skupRevs;
    }

    public void setSkupRevs(Set<SkupRev> skupRevs) {
        this.skupRevs = skupRevs;
    }

    public Set<UdajObrazky> getUdajObrazkies() {
        return this.udajObrazkies;
    }

    public void setUdajObrazkies(Set<UdajObrazky> udajObrazkies) {
        this.udajObrazkies = udajObrazkies;
    }

    public Set<PeopleAsoc> getUdajZberAsocs() {
        return this.udajZberAsocs;
    }

    public void setUdajZberAsocs(Set<PeopleAsoc> udajZberAsocs) {
        this.udajZberAsocs = udajZberAsocs;
    }

    public Set<LitZdrojRev> getLitZdrojRevs() {
        return litZdrojRevs;
    }

    public void setLitZdrojRevs(Set<LitZdrojRev> litZdrojRevs) {
        this.litZdrojRevs = litZdrojRevs;
    }
    
    public List<LitZdrojRev> getLitZdrojs() {
        List<LitZdrojRev> litZdrojs = new ArrayList<>(this.litZdrojRevs);
        litZdrojs.removeIf(Objects::isNull);
        return litZdrojs;
    }

    public SkupRev getUrcenie() {
        for (Object object : skupRevs) {
            SkupRev sr = (SkupRev) object;
            if (!sr.isFRevizia()) {
                return sr;
            }
        }
        return null;
    }

    public List<SkupRev> getRevisions() {
        List<SkupRev> revisions = new ArrayList<>();
        for (Object object : skupRevs) {
            SkupRev sr = (SkupRev) object;
            if (sr.isFRevizia()) {
                revisions.add(sr);
            }
        }
        return revisions;
    }
    
    public List<UdajObrazky> getObrazky() {
        List<UdajObrazky> obrazky = new ArrayList<>();
        for (Object object : udajObrazkies) {
            UdajObrazky obr = (UdajObrazky) object;
            obrazky.add(obr);
        }
        Collections.sort(obrazky, new ComparatorObr());
        return obrazky;
    }

    @Override
    public List<String[]> namesAndValues() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
