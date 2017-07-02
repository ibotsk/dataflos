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
public class LzdrojEditoriAsocId implements Serializable {
    
	private static final long serialVersionUID = 1L;
	
    private int idLitZdroj;
    private int idMenoEditora;

    public LzdrojEditoriAsocId() {
    }

    public LzdrojEditoriAsocId(int idLitZdroj, int idMenoEditora) {
        this.idLitZdroj = idLitZdroj;
        this.idMenoEditora = idMenoEditora;
    }

    public int getIdLitZdroj() {
        return idLitZdroj;
    }

    public void setIdLitZdroj(int idLitZdroj) {
        this.idLitZdroj = idLitZdroj;
    }

    public int getIdMenoEditora() {
        return idMenoEditora;
    }

    public void setIdMenoEditora(int idMenoEditora) {
        this.idMenoEditora = idMenoEditora;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 83 * hash + this.idLitZdroj;
        hash = 83 * hash + this.idMenoEditora;
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
        final LzdrojEditoriAsocId other = (LzdrojEditoriAsocId) obj;
        if (this.idLitZdroj != other.idLitZdroj) {
            return false;
        }
        if (this.idMenoEditora != other.idMenoEditora) {
            return false;
        }
        return true;
    }
    
}
