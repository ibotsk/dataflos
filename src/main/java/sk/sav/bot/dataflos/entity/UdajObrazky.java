package sk.sav.bot.dataflos.entity;
// Generated May 30, 2012 11:34:04 AM by Hibernate Tools 3.2.1.GA

import sk.sav.bot.dataflos.entity.interf.AssociableEntity;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

/**
 * UdajObrazky
 */
@Entity
public class UdajObrazky implements java.io.Serializable, AssociableEntity {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_udaj")
    private Udaj udaj;
    
    private String popis;
    private byte[] obrazok;
    private String url;

    public UdajObrazky() {
    }

    public UdajObrazky(Udaj udaj) {
        this.udaj = udaj;
    }

    public UdajObrazky(Udaj udaj, String popis, byte[] obrazok, String url) {
        this.udaj = udaj;
        this.popis = popis;
        this.obrazok = obrazok;
        this.url = url;
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Udaj getUdaj() {
        return this.udaj;
    }

    public void setUdaj(Udaj udaj) {
        this.udaj = udaj;
    }

    public String getPopis() {
        return this.popis;
    }

    public void setPopis(String popis) {
        this.popis = popis;
    }

    public byte[] getObrazok() {
        return this.obrazok;
    }

    public void setObrazok(byte[] obrazok) {
        this.obrazok = obrazok;
    }

    public String getUrl() {
        return this.url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
    
    @Override
    public String toString() {
        return String.valueOf(this.id);
    }
    
    public UdajObrazky replicate() {
        UdajObrazky uo = new UdajObrazky(udaj, popis, obrazok, url);
        return uo;
    }

    @Override
    public List<String[]> namesAndValues() {
        List<String[]> nAv = new ArrayList<>();
        String[] p = {"Popis obrázku", this.popis == null ? "" : this.popis};
        String[] r = {"Obrázok", this.obrazok == null ? "" : this.obrazok.toString()};
        String[] u = {"URL", this.url == null ? "" : this.url};
        nAv.add(p);
        nAv.add(r);
        nAv.add(u);
        return nAv;
    }
}
