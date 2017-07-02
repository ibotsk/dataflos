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
 * HerbarPolozky
 */
@Entity
@Table(name = "herbar_polozky")
public class HerbarPolozky implements java.io.Serializable, AssociableEntity {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_herbar")
    private Herbar herbar;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_dalla_torre")
    private DallaTorre dallaTorre;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_voucher")
    private Voucher voucher;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_exsikat")
    private Exsikaty exsikaty;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_kolekcie")
    private Kolekcie kolekcie;
    
    private String cisloPol;
    private Integer cisloCk;
    private String cisloCkFull;
    private String cisloZberu;
    private String kvantifikacia;
    private String poznamka;
    private boolean typ; //je typova polozka?
    
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "herbarPolozky")
    private Set<Udaj> udajs = new HashSet<>(0);

    public HerbarPolozky() {
    }

    public HerbarPolozky(Herbar herbar, DallaTorre dallaTorre, Voucher voucher, Exsikaty exsikaty, 
    		Kolekcie kolekcie, String cisloPol, Integer cisloCk, String cisloCkFull, String cisloZberu, 
    		String kvantifikacia, String poznamka, boolean typ, Set<Udaj> udajs) {
        this.herbar = herbar;
        this.dallaTorre = dallaTorre;
        this.voucher = voucher;
        this.exsikaty = exsikaty;
        this.kolekcie = kolekcie;
        this.cisloPol = cisloPol;
        this.cisloCk = cisloCk;
        this.cisloZberu = cisloZberu;
        this.kvantifikacia = kvantifikacia;
        this.poznamka = poznamka;
        this.typ = typ;
        this.udajs = udajs;
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Herbar getHerbar() {
        return this.herbar;
    }

    public void setHerbar(Herbar herbar) {
        this.herbar = herbar;
    }

    public DallaTorre getDallaTorre() {
        return this.dallaTorre;
    }

    public void setDallaTorre(DallaTorre dallaTorre) {
        this.dallaTorre = dallaTorre;
    }

    public Voucher getVoucher() {
        return this.voucher;
    }

    public void setVoucher(Voucher voucher) {
        this.voucher = voucher;
    }

    public Exsikaty getExsikaty() {
        return this.exsikaty;
    }

    public void setExsikaty(Exsikaty exsikaty) {
        this.exsikaty = exsikaty;
    }

    public Kolekcie getKolekcie() {
        return this.kolekcie;
    }

    public void setKolekcie(Kolekcie kolekcie) {
        this.kolekcie = kolekcie;
    }

    public String getCisloPol() {
        return this.cisloPol;
    }

    public void setCisloPol(String cisloPol) {
        this.cisloPol = cisloPol;
    }

    public Integer getCisloCk() {
        return this.cisloCk;
    }

    public void setCisloCk(Integer cisloCk) {
        this.cisloCk = cisloCk;
    }

    public String getCisloCkFull() {
        return cisloCkFull;
    }

    public void setCisloCkFull(String cisloCkFull) {
        this.cisloCkFull = cisloCkFull;
    }

    public String getCisloZberu() {
        return this.cisloZberu;
    }

    public void setCisloZberu(String cisloZberu) {
        this.cisloZberu = cisloZberu;
    }

    public String getKvantifikacia() {
        return this.kvantifikacia;
    }

    public void setKvantifikacia(String kvantifikacia) {
        this.kvantifikacia = kvantifikacia;
    }

    public String getPoznamka() {
        return this.poznamka;
    }

    public void setPoznamka(String poznamka) {
        this.poznamka = poznamka;
    }

    public boolean isTyp() {
        return typ;
    }

    public void setTyp(boolean typ) {
        this.typ = typ;
    }

    public Set<Udaj> getUdajs() {
        return this.udajs;
    }

    public void setUdajs(Set<Udaj> udajs) {
        this.udajs = udajs;
    }

    @Override
    public List<String[]> namesAndValues() {
        return null;
    }
}
