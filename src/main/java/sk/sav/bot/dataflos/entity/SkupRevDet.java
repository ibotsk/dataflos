package sk.sav.bot.dataflos.entity;
// Generated May 30, 2012 11:34:04 AM by Hibernate Tools 3.2.1.GA

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import sk.sav.bot.dataflos.entity.interf.PeopleAsoc;



/**
 * SkupRevDet
 */
@Entity
@Table(name = "skup_rev_det")
public class SkupRevDet  implements java.io.Serializable, PeopleAsoc {

	private static final long serialVersionUID = 1L;

	@EmbeddedId
    private SkupRevDetId id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_skup_rev")
    private SkupRev skupRev;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_meno_rev")
    private MenaZberRev menaZberRev;
    
    private Integer poradie;

    public SkupRevDet() {
    }

	
    public SkupRevDet(SkupRev skupRev, MenaZberRev menaZberRev) {
        this.skupRev = skupRev;
        this.menaZberRev = menaZberRev;
    }
    public SkupRevDet(SkupRev skupRev, MenaZberRev menaZberRev, Integer poradie) {
       this.skupRev = skupRev;
       this.menaZberRev = menaZberRev;
       this.poradie = poradie;
    }
   
    public SkupRevDetId getId() {
        return this.id;
    }
    
    public void setId(SkupRevDetId id) {
        this.id = id;
    }
    public SkupRev getSkupRev() {
        return this.skupRev;
    }
    
    public void setSkupRev(SkupRev skupRev) {
        this.skupRev = skupRev;
    }
    @Override
    public MenaZberRev getMenaZberRev() {
        return this.menaZberRev;
    }
    
    public void setMenaZberRev(MenaZberRev menaZberRev) {
        this.menaZberRev = menaZberRev;
    }
    public Integer getPoradie() {
        return this.poradie;
    }
    
     @Override
    public void setPoradie(Integer poradie) {
        this.poradie = poradie;
    }

    @Override
    public String toString() {
        return this.menaZberRev.getMeno();
    }


}


