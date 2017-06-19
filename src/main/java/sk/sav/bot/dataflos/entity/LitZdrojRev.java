/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sk.sav.bot.dataflos.entity;

import java.io.Serializable;

/**
 *
 * @author Matus
 */
public class LitZdrojRev implements Serializable {
    
    private int id;
    private String datum;
    private Udaj udaj;
    private LitZdroj litZdroj;
    private String datumSlovom;

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

    private void setId(int id) {
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
