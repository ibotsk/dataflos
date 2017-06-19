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
public class LzdrojAutoriAsoc implements Serializable, PeopleAsoc {
    
    private LzdrojAutoriAsocId id;
    private LitZdroj litZdroj;
    private MenaZberRev menoAutora;
    private Integer poradie;

    public LzdrojAutoriAsoc() {
    }

    public LzdrojAutoriAsoc(LitZdroj litZdroj, MenaZberRev menoAutora) {
        this.litZdroj = litZdroj;
        this.menoAutora = menoAutora;
    }

    public LzdrojAutoriAsoc(LitZdroj litZdroj, MenaZberRev menoAutora, Integer poradie) {
        this.litZdroj = litZdroj;
        this.menoAutora = menoAutora;
        this.poradie = poradie;
    }

    public LzdrojAutoriAsocId getId() {
        return id;
    }

    public void setId(LzdrojAutoriAsocId id) {
        this.id = id;
    }

    public LitZdroj getLitZdroj() {
        return litZdroj;
    }

    public void setLitZdroj(LitZdroj litZdroj) {
        this.litZdroj = litZdroj;
    }

    public MenaZberRev getMenoAutora() {
        return menoAutora;
    }

    public void setMenoAutora(MenaZberRev menoAutora) {
        this.menoAutora = menoAutora;
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
        return this.getMenoAutora();
    }
    
    
}
