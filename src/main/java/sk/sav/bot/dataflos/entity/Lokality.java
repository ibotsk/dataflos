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
 * Lokality
 */
@Entity
@Table(name = "lokality")
public class Lokality implements java.io.Serializable, AssociableEntity {

	private static final long serialVersionUID = 1L;
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_brumit3")
    private Brumit3 brumit3;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_brumit4")
    private Brumit4 brumit4;
    
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_ngc")
    private Ngc ngc;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_nac")
    private Nac nac;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_flora")
    private Flora flora;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_vgc")
    private Vgc vgc;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_vac")
    private Vac vac;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_chu")
    private Chu chu;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_obec")
    private Obec obec;
	
    private String typss;
    private Double latitude;
    private Double longitude;
    private boolean altOdCca;
    private Integer altOd;
    private Integer altDo;
    private String opisLokality;
    private String poznamkaLok;
    private String poda;
    private String substrat;
    private String hostitel;
    private Integer vyskaZberu;
    private String kopiaSchedy;
    private String kod;
    
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "lokality")
    private Set<Udaj> udajs = new HashSet<>(0);
    
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "lokality")
    private Set<LokalityFtgokresAsoc> lokalityFtgokresAsocs = new HashSet<>(0);

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "lokality")
    private Set<LokalityKvadrantAsoc> lokalityKvadrantAsocs = new HashSet<>(0);

    public Lokality() {
    }
    
    public Lokality(Brumit3 brumit3, Ngc ngc, Nac nac, Flora flora, Brumit4 brumit4, Vgc vgc, Vac vac, Chu chu, Obec obec, 
    		String typss, Double latitude, Double longitude, boolean altOdCca, Integer altOd, Integer altDo, String expo, 
    		String opisLokality, String poznamkaLok, String poda, String substrat, String hostitel, Integer vyskaZberu, 
    		String kopiaSchedy, String kod, Set<Udaj> udajs, Set<LokalityFtgokresAsoc> lokalityFtgokresAsocs, Set<LokalityKvadrantAsoc> lokalityKvadrantAsocs) {
        this.brumit3 = brumit3;
        this.ngc = ngc;
        this.nac = nac;
        this.flora = flora;
        this.brumit4 = brumit4;
        this.vgc = vgc;
        this.vac = vac;
        this.chu = chu;
        this.obec = obec;
        this.typss = typss;
        this.latitude = latitude;
        this.longitude = longitude;
        this.altOdCca = altOdCca;
        this.altOd = altOd;
        this.altDo = altDo;
        this.opisLokality = opisLokality;
        this.poznamkaLok = poznamkaLok;
        this.poda = poda;
        this.substrat = substrat;
        this.hostitel = hostitel;
        this.vyskaZberu = vyskaZberu;
        this.kopiaSchedy = kopiaSchedy;
        this.kod = kod;
        this.udajs = udajs;
        this.lokalityFtgokresAsocs = lokalityFtgokresAsocs;
        this.lokalityKvadrantAsocs = lokalityKvadrantAsocs;
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Brumit3 getBrumit3() {
        return this.brumit3;
    }

    public void setBrumit3(Brumit3 brumit3) {
        this.brumit3 = brumit3;
    }

    public Ngc getNgc() {
        return this.ngc;
    }

    public void setNgc(Ngc ngc) {
        this.ngc = ngc;
    }

    public Nac getNac() {
        return this.nac;
    }

    public void setNac(Nac nac) {
        this.nac = nac;
    }

    public Flora getFlora() {
        return this.flora;
    }

    public void setFlora(Flora flora) {
        this.flora = flora;
    }

    public Brumit4 getBrumit4() {
        return this.brumit4;
    }

    public void setBrumit4(Brumit4 brumit4) {
        this.brumit4 = brumit4;
    }

    public Vgc getVgc() {
        return this.vgc;
    }

    public void setVgc(Vgc vgc) {
        this.vgc = vgc;
    }

    public Vac getVac() {
        return this.vac;
    }

    public void setVac(Vac vac) {
        this.vac = vac;
    }

    public Chu getChu() {
        return this.chu;
    }

    public void setChu(Chu chu) {
        this.chu = chu;
    }

    public Obec getObec() {
        return this.obec;
    }

    public void setObec(Obec obec) {
        this.obec = obec;
    }

    public String getTypss() {
        return this.typss;
    }

    public void setTypss(String typss) {
        this.typss = typss;
    }

    public Double getLatitude() {
        return this.latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return this.longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public boolean isAltOdCca() {
        return this.altOdCca;
    }

    public void setAltOdCca(boolean altOdCca) {
        this.altOdCca = altOdCca;
    }

    public Integer getAltOd() {
        return this.altOd;
    }

    public void setAltOd(Integer altOd) {
        this.altOd = altOd;
    }

    public Integer getAltDo() {
        return this.altDo;
    }

    public void setAltDo(Integer altDo) {
        this.altDo = altDo;
    }

    public String getOpisLokality() {
        return this.opisLokality;
    }

    public void setOpisLokality(String opisLokality) {
        this.opisLokality = opisLokality;
    }

    public String getPoznamkaLok() {
        return this.poznamkaLok;
    }

    public void setPoznamkaLok(String poznamkaLok) {
        this.poznamkaLok = poznamkaLok;
    }

    public String getPoda() {
        return this.poda;
    }

    public void setPoda(String poda) {
        this.poda = poda;
    }

    public String getSubstrat() {
        return this.substrat;
    }

    public void setSubstrat(String substrat) {
        this.substrat = substrat;
    }

    public String getHostitel() {
        return this.hostitel;
    }

    public void setHostitel(String hostitel) {
        this.hostitel = hostitel;
    }

    public Integer getVyskaZberu() {
        return this.vyskaZberu;
    }

    public void setVyskaZberu(Integer vyskaZberu) {
        this.vyskaZberu = vyskaZberu;
    }

    public String getKopiaSchedy() {
        return this.kopiaSchedy;
    }

    public void setKopiaSchedy(String kopiaSchedy) {
        this.kopiaSchedy = kopiaSchedy;
    }

    public String getKod() {
        return this.kod;
    }

    public void setKod(String kod) {
        this.kod = kod;
    }

    public Set<Udaj> getUdajs() {
        return this.udajs;
    }

    public void setUdajs(Set<Udaj> udajs) {
        this.udajs = udajs;
    }
    
    public Set<LokalityFtgokresAsoc> getLokalityFtgokresAsocs() {
        return this.lokalityFtgokresAsocs;
    }
    
    public void setLokalityFtgokresAsocs(Set<LokalityFtgokresAsoc> lokalityFtgokresAsocs) {
        this.lokalityFtgokresAsocs = lokalityFtgokresAsocs;
    }

    public Set<LokalityKvadrantAsoc> getLokalityKvadrantAsocs() {
        return this.lokalityKvadrantAsocs;
    }

    public void setLokalityKvadrantAsocs(Set<LokalityKvadrantAsoc> lokalityKvadrantAsocs) {
        this.lokalityKvadrantAsocs = lokalityKvadrantAsocs;
    }

    @Override
    public List<String[]> namesAndValues() {
        return null;
    }
}
