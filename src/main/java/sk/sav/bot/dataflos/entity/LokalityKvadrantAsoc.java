package sk.sav.bot.dataflos.entity;
// Generated May 30, 2012 11:34:04 AM by Hibernate Tools 3.2.1.GA



/**
 * LokalityKvadrantAsoc generated by hbm2java
 */
public class LokalityKvadrantAsoc  implements java.io.Serializable {


     private LokalityKvadrantAsocId id;
     private Lokality lokality;
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


