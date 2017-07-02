/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sk.sav.bot.dataflos.entity;

import java.io.Serializable;

import javax.persistence.Embeddable;

/**
 *
 * @author Matus
 */
@Embeddable
public class LzdrojAutoriAsocId implements Serializable {
    
	private static final long serialVersionUID = 1L;
	
	private int idLitZdroj;
    private int idMenoAutora;

    public LzdrojAutoriAsocId() {
    }

    public LzdrojAutoriAsocId(int idLitZdroj, int idMenoAutora) {
        this.idLitZdroj = idLitZdroj;
        this.idMenoAutora = idMenoAutora;
    }

    public int getIdLitZdroj() {
        return idLitZdroj;
    }

    public void setIdLitZdroj(int idLitZdroj) {
        this.idLitZdroj = idLitZdroj;
    }

    public int getIdMenoAutora() {
        return idMenoAutora;
    }

    public void setIdMenoAutora(int idMenoAutora) {
        this.idMenoAutora = idMenoAutora;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 37 * hash + this.idLitZdroj;
        hash = 37 * hash + this.idMenoAutora;
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final LzdrojAutoriAsocId other = (LzdrojAutoriAsocId) obj;
        if (this.idLitZdroj != other.idLitZdroj) {
            return false;
        }
        if (this.idMenoAutora != other.idMenoAutora) {
            return false;
        }
        return true;
    }
    
    
}
