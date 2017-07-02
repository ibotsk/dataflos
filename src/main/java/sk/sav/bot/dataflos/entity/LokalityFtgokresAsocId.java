package sk.sav.bot.dataflos.entity;

import javax.persistence.Embeddable;

/**
 * LokalityFtgokresAsocId
 */
@Embeddable
public class LokalityFtgokresAsocId  implements java.io.Serializable {

	private static final long serialVersionUID = 1L;
	
    private int idLokality;
    private int idFtgokres;

    public LokalityFtgokresAsocId() {
    }

    public LokalityFtgokresAsocId(int idLokality, int idFtgokres) {
       this.idLokality = idLokality;
       this.idFtgokres = idFtgokres;
    }
   
    public int getIdLokality() {
        return this.idLokality;
    }
    
    public void setIdLokality(int idLokality) {
        this.idLokality = idLokality;
    }
    public int getIdFtgokres() {
        return this.idFtgokres;
    }
    
    public void setIdFtgokres(int idFtgokres) {
        this.idFtgokres = idFtgokres;
    }

    @Override
    public boolean equals(Object other) {
         if ( (this == other ) ) return true;
		 if ( (other == null ) ) return false;
		 if ( !(other instanceof LokalityFtgokresAsocId) ) return false;
		 LokalityFtgokresAsocId castOther = ( LokalityFtgokresAsocId ) other; 
         
		 return (this.getIdLokality()==castOther.getIdLokality())
				 && (this.getIdFtgokres()==castOther.getIdFtgokres());
   }
   
   @Override
   public int hashCode() {
         int result = 17;
         
         result = 37 * result + this.getIdLokality();
         result = 37 * result + this.getIdFtgokres();
         return result;
   }   


}


