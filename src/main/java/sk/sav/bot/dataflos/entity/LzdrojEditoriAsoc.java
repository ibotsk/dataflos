/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sk.sav.bot.dataflos.entity;

import sk.sav.bot.dataflos.entity.interf.PeopleAsoc;
import java.io.Serializable;

/**
 *
 * @author Matus
 */
public class LzdrojEditoriAsoc implements Serializable, PeopleAsoc {
    
    private LzdrojEditoriAsocId id;
    private LitZdroj litZdroj;
    private MenaZberRev menoEditora;
    private Integer poradie;

    public LzdrojEditoriAsoc() {
    }

    public LzdrojEditoriAsoc(LitZdroj litZdroj, MenaZberRev menoEditora) {
        this.litZdroj = litZdroj;
        this.menoEditora = menoEditora;
    }

    public LzdrojEditoriAsoc(LitZdroj litZdroj, MenaZberRev menoEditora, Integer poradie) {
        this.litZdroj = litZdroj;
        this.menoEditora = menoEditora;
        this.poradie = poradie;
    }

    public LzdrojEditoriAsocId getId() {
        return id;
    }

    public void setId(LzdrojEditoriAsocId id) {
        this.id = id;
    }

    public LitZdroj getLitZdroj() {
        return litZdroj;
    }

    public void setLitZdroj(LitZdroj litZdroj) {
        this.litZdroj = litZdroj;
    }

    public MenaZberRev getMenoEditora() {
        return menoEditora;
    }

    public void setMenoEditora(MenaZberRev menoEditora) {
        this.menoEditora = menoEditora;
    }

    public Integer getPoradie() {
        return poradie;
    }

    @Override
    public void setPoradie(Integer poradie) {
        this.poradie = poradie;
    }

    @Override
    public MenaZberRev getMenaZberRev() {
        return this.getMenoEditora();
    }
    
}
