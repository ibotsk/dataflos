package sk.sav.bot.dataflos.entity;
// Generated May 30, 2012 11:34:04 AM by Hibernate Tools 3.2.1.GA

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * LokalityKvadrantAsoc
 */
@Entity
@Table(name = "lokality_kvadrant_asoc")
public class LokalityKvadrantAsoc  implements java.io.Serializable {

	private static final long serialVersionUID = 1L;
	
	@EmbeddedId
    private LokalityKvadrantAsocId id;
	
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_lokality")
    private Lokality lokality;
	
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_kvadrant")
    private Kvadrant kvadrant;

    public LokalityKvadrantAsoc() {
    }

    public LokalityKvadrantAsoc(Lokality lokality, Kvadrant kvadrant) {
       this.lokality = lokality;
       this.kvadrant = kvadrant;
    }
   
    public LokalityKvadrantAsocId getId() {
        return this.id;
    }
    
    public void setId(LokalityKvadrantAsocId id) {
        this.id = id;
    }
    public Lokality getLokality() {
        return this.lokality;
    }
    
    public void setLokality(Lokality lokality) {
        this.lokality = lokality;
    }
    public Kvadrant getKvadrant() {
        return this.kvadrant;
    }
    
    public void setKvadrant(Kvadrant kvadrant) {
        this.kvadrant = kvadrant;
    }
}


