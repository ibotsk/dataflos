/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sk.sav.bot.dataflos.entity;

import java.io.Serializable;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import sk.sav.bot.dataflos.entity.interf.PeopleAsoc;

/**
 *
 * @author Matus
 */
@Entity
@Table(name = "lzdroj_autori_asoc")
public class LzdrojAutoriAsoc implements Serializable, PeopleAsoc {
    
	private static final long serialVersionUID = 1L;
	
	@EmbeddedId
    private LzdrojAutoriAsocId id;
	
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_lit_zdroj")
    private LitZdroj litZdroj;
	
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_meno_autora")
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
