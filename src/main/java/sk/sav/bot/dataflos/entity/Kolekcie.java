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
import javax.persistence.OneToMany;
import javax.persistence.Table;

import sk.sav.bot.dataflos.entity.interf.AssociableEntity;

/**
 * Kolekcie
 */
@Entity
@Table(name = "kolekcie")
public class Kolekcie implements java.io.Serializable, AssociableEntity {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private Integer idZbierky;
    private String prirastkoveCislo;
    private Integer evidencneCislo;
    private String nazovZbierkovehoPredmetu;
    private String miestoNalezu;
    private Integer pocetKusov;
    private String datumNadobudnutia;
    private Integer idSposobNadobudnutia;
    private String predchadzajuciVlastnik;
    private String cena;
    private String nadobudajucaHodnota;
    private Integer idDokladONadobudnuti;
    private String predmetPrevzal;
    private String opis;
    private String poznamka;
    
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "kolekcie")
    private Set<HerbarPolozky> herbarPolozkies = new HashSet<>(0);

    public Kolekcie() {
    }

    public Kolekcie(Integer idZbierky, String prirastkoveCislo, Integer evidencneCislo, String nazovZbierkovehoPredmetu, 
    		String miestoNalezu, Integer pocetKusov, String datumNadobudnutia, Integer idSposobNadobudnutia, 
    		String predchadzajuciVlastnik, String cena, String nadobudajucaHodnota, Integer idDokladONadobudnuti, 
    		String predmetPrevzal, String opis, String poznamka, Set<HerbarPolozky> herbarPolozkies) {
        this.idZbierky = idZbierky;
        this.prirastkoveCislo = prirastkoveCislo;
        this.evidencneCislo = evidencneCislo;
        this.nazovZbierkovehoPredmetu = nazovZbierkovehoPredmetu;
        this.miestoNalezu = miestoNalezu;
        this.pocetKusov = pocetKusov;
        this.datumNadobudnutia = datumNadobudnutia;
        this.idSposobNadobudnutia = idSposobNadobudnutia;
        this.predchadzajuciVlastnik = predchadzajuciVlastnik;
        this.cena = cena;
        this.nadobudajucaHodnota = nadobudajucaHodnota;
        this.idDokladONadobudnuti = idDokladONadobudnuti;
        this.predmetPrevzal = predmetPrevzal;
        this.opis = opis;
        this.poznamka = poznamka;
        this.herbarPolozkies = herbarPolozkies;
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Integer getIdZbierky() {
        return this.idZbierky;
    }

    public void setIdZbierky(Integer idZbierky) {
        this.idZbierky = idZbierky;
    }

    public String getPrirastkoveCislo() {
        return this.prirastkoveCislo;
    }

    public void setPrirastkoveCislo(String prirastkoveCislo) {
        this.prirastkoveCislo = prirastkoveCislo;
    }

    public Integer getEvidencneCislo() {
        return this.evidencneCislo;
    }

    public void setEvidencneCislo(Integer evidencneCislo) {
        this.evidencneCislo = evidencneCislo;
    }

    public String getNazovZbierkovehoPredmetu() {
        return this.nazovZbierkovehoPredmetu;
    }

    public void setNazovZbierkovehoPredmetu(String nazovZbierkovehoPredmetu) {
        this.nazovZbierkovehoPredmetu = nazovZbierkovehoPredmetu;
    }

    public String getMiestoNalezu() {
        return this.miestoNalezu;
    }

    public void setMiestoNalezu(String miestoNalezu) {
        this.miestoNalezu = miestoNalezu;
    }

    public Integer getPocetKusov() {
        return this.pocetKusov;
    }

    public void setPocetKusov(Integer pocetKusov) {
        this.pocetKusov = pocetKusov;
    }

    public String getDatumNadobudnutia() {
        return this.datumNadobudnutia;
    }

    public void setDatumNadobudnutia(String datumNadobudnutia) {
        this.datumNadobudnutia = datumNadobudnutia;
    }

    public Integer getIdSposobNadobudnutia() {
        return this.idSposobNadobudnutia;
    }

    public void setIdSposobNadobudnutia(Integer idSposobNadobudnutia) {
        this.idSposobNadobudnutia = idSposobNadobudnutia;
    }

    public String getPredchadzajuciVlastnik() {
        return this.predchadzajuciVlastnik;
    }

    public void setPredchadzajuciVlastnik(String predchadzajuciVlastnik) {
        this.predchadzajuciVlastnik = predchadzajuciVlastnik;
    }

    public String getCena() {
        return this.cena;
    }

    public void setCena(String cena) {
        this.cena = cena;
    }

    public String getNadobudajucaHodnota() {
        return this.nadobudajucaHodnota;
    }

    public void setNadobudajucaHodnota(String nadobudajucaHodnota) {
        this.nadobudajucaHodnota = nadobudajucaHodnota;
    }

    public Integer getIdDokladONadobudnuti() {
        return this.idDokladONadobudnuti;
    }

    public void setIdDokladONadobudnuti(Integer idDokladONadobudnuti) {
        this.idDokladONadobudnuti = idDokladONadobudnuti;
    }

    public String getPredmetPrevzal() {
        return this.predmetPrevzal;
    }

    public void setPredmetPrevzal(String predmetPrevzal) {
        this.predmetPrevzal = predmetPrevzal;
    }

    public String getOpis() {
        return this.opis;
    }

    public void setOpis(String opis) {
        this.opis = opis;
    }

    public String getPoznamka() {
        return this.poznamka;
    }

    public void setPoznamka(String poznamka) {
        this.poznamka = poznamka;
    }

    public Set<HerbarPolozky> getHerbarPolozkies() {
        return this.herbarPolozkies;
    }

    public void setHerbarPolozkies(Set<HerbarPolozky> herbarPolozkies) {
        this.herbarPolozkies = herbarPolozkies;
    }

    @Override
    public List<String[]> namesAndValues() {
        return null;
    }
}
