/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sk.sav.bot.dataflos.entity;

import java.util.Date;

/**
 * Unused
 * @author Matus
 */
public class Revizie {

    private Integer id;
    //private Uzivatelia uzivatel;
    private String uzivatelLogin;
    private String tabulka;
    private Integer idRec;
    private Date datetime;
    private String op;

    public Revizie() {
    }

    public Revizie(/*
             * Uzivatelia uzivatel,
             */String uzivatelLogin, String tabulka, Integer idRec, String op) {
        //this.uzivatel = uzivatel;
        this.uzivatelLogin = uzivatelLogin;
        this.tabulka = tabulka;
        this.idRec = idRec;
        this.op = op;
    }

    public Date getDatetime() {
        return datetime;
    }

    public void setDatetime(Date datetime) {
        this.datetime = datetime;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getIdRec() {
        return idRec;
    }

    public void setIdRec(Integer idRec) {
        this.idRec = idRec;
    }

    public String getOp() {
        return op;
    }

    public void setOp(String op) {
        this.op = op;
    }

    public String getTabulka() {
        return tabulka;
    }

    public void setTabulka(String tabulka) {
        this.tabulka = tabulka;
    }

    /*
     * public Uzivatelia getUzivatel() { return uzivatel; }
     *
     * public void setUzivatel(Uzivatelia uzivatel) { this.uzivatel = uzivatel;
    }
     */
    public String getUzivatelLogin() {
        return uzivatelLogin;
    }

    public void setUzivatelLogin(String uzivatelLogin) {
        this.uzivatelLogin = uzivatelLogin;
    }
}
