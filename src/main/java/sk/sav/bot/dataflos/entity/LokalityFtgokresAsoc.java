package sk.sav.bot.dataflos.entity;
// added July 03, 2014



/**
 * LokalityFtgokresAsoc
 */
public class LokalityFtgokresAsoc  implements java.io.Serializable {


     private LokalityFtgokresAsocId id;
     private Lokality lokality;
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


