package sk.sav.bot.dataflos.entity;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

// added July 03, 2014



/**
 * LokalityFtgokresAsoc
 */
@Entity
@Table(name = "lokality_ftgokres_asoc")
public class LokalityFtgokresAsoc  implements java.io.Serializable {

	private static final long serialVersionUID = 1L;
	
	@EmbeddedId
	private LokalityFtgokresAsocId id;
	
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_lokality")
	private Lokality lokality;
	
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_ftgokres")
	private Ftgokres ftgokres;

    public LokalityFtgokresAsoc() {
    }

    public LokalityFtgokresAsoc(Lokality lokality, Ftgokres ftgokres) {
       this.lokality = lokality;
       this.ftgokres = ftgokres;
    }
   
    public LokalityFtgokresAsocId getId() {
        return this.id;
    }
    
    public void setId(LokalityFtgokresAsocId id) {
        this.id = id;
    }
    public Lokality getLokality() {
        return this.lokality;
    }
    
    public void setLokality(Lokality lokality) {
        this.lokality = lokality;
    }
    public Ftgokres getFtgokres() {
        return this.ftgokres;
    }
    
    public void setFtgokres(Ftgokres ftgokres) {
        this.ftgokres = ftgokres;
    }
}


