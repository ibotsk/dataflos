/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sk.sav.bot.dataflos.entity;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 *
 * @author Matus
 */
@Entity
@Table(name = "list_zdroj_rev")
public class LitZdrojRev implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String datum;
    private String datumSlovom;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_udaj")
    private Udaj udaj;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_lit_zdroj")
    private LitZdroj litZdroj;

    public LitZdrojRev() {
    }

    public LitZdrojRev(String datum, Udaj udaj, LitZdroj litZdroj, String datumSlovom) {
        this.datum = datum;
        this.udaj = udaj;
        this.litZdroj = litZdroj;
        this.datumSlovom = datumSlovom;
    }

    public String getDatum() {
        return datum;
    }

    public void setDatum(String datum) {
        this.datum = datum;
    }

    public String getDatumSlovom() {
        return datumSlovom;
    }

    public void setDatumSlovom(String datumSlovom) {
        this.datumSlovom = datumSlovom;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public LitZdroj getLitZdroj() {
        return litZdroj;
    }

    public void setLitZdroj(LitZdroj litZdroj) {
        this.litZdroj = litZdroj;
    }

    public Udaj getUdaj() {
        return udaj;
    }

    public void setUdaj(Udaj udaj) {
        this.udaj = udaj;
    }
    
    @Override
    public String toString() {
        return this.litZdroj.toString();
    }
    
    public LitZdrojRev replicate() {
        LitZdrojRev lzr = new LitZdrojRev(datum, null, litZdroj, datumSlovom);
        return lzr;
    }
}
