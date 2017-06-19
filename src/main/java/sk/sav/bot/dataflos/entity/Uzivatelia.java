/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sk.sav.bot.dataflos.entity;

import sk.sav.bot.dataflos.entity.interf.Entity;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 * @author Matus
 */
public class Uzivatelia implements Serializable, Entity {
    
    private int id;
    private String login;
    private String prefixCk;
    private Integer ckLow;
    private Integer ckHi;
    private Integer ckCounter;
    private String meno;
    private String pracovisko;
    private Date regDate;
    private String note;
    private Integer lastlogin;
    private boolean admin;
    
    //private Set revizie = new HashSet(0);

    public Uzivatelia() {
    }

    public Uzivatelia(String login, String prefixCk, Integer ckLow, Integer ckHi, Integer ckCounter, String meno, String pracovisko, String note, Integer lastlogin, boolean admin) {
        this.login = login;
        this.prefixCk = prefixCk;
        this.ckLow = ckLow;
        this.ckHi = ckHi;
        this.ckCounter = ckCounter;
        this.meno = meno;
        this.pracovisko = pracovisko;
        this.note = note;
        this.lastlogin = lastlogin;
        //this.revizie = revizie;
        this.admin = admin;
    }

    public Integer getCkCounter() {
        return ckCounter;
    }

    public void setCkCounter(Integer ckCounter) {
        this.ckCounter = ckCounter;
    }

    public Integer getCkHi() {
        return ckHi;
    }

    public void setCkHi(Integer ckHi) {
        this.ckHi = ckHi;
    }

    public Integer getCkLow() {
        return ckLow;
    }

    public void setCkLow(Integer ckLow) {
        this.ckLow = ckLow;
    }

    public int getId() {
        return id;
    }

    private void setId(int id) {
        this.id = id;
    }

    public Integer getLastlogin() {
        return lastlogin;
    }

    public void setLastlogin(Integer lastlogin) {
        this.lastlogin = lastlogin;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getMeno() {
        return meno;
    }

    public void setMeno(String meno) {
        this.meno = meno;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getPracovisko() {
        return pracovisko;
    }

    public void setPracovisko(String pracovisko) {
        this.pracovisko = pracovisko;
    }

    public String getPrefixCk() {
        return prefixCk;
    }

    public void setPrefixCk(String prefixCk) {
        this.prefixCk = prefixCk;
    }

    public Date getRegDate() {
        return regDate;
    }

    public void setRegDate(Date regDate) {
        this.regDate = regDate;
    }

//    public Set getRevizie() {
//        return revizie;
//    }
//
//    public void setRevizie(Set revizie) {
//        this.revizie = revizie;
//    }
    
    @Override
    public String toString() {
        return this.login;
    }

    @Override
    public List<String[]> namesAndValues() {
        List<String[]> nAv = new ArrayList<>();
        String[] l = {"Login", this.login};
        String[] m = {"Meno", this.meno};
        String[] p = {"Pracovisko", this.pracovisko};
        String[] n = {"Poznámka", this.note};
        nAv.add(l);
        nAv.add(m);
        nAv.add(p);
        nAv.add(n);
        return nAv;
    }

    /**
     * @return the admin
     */
    public boolean isAdmin() {
        return admin;
    }

    /**
     * @param admin the admin to set
     */
    public void setAdmin(boolean admin) {
        this.admin = admin;
    }
}
