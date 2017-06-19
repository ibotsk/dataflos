package sk.sav.bot.dataflos.entity;
// Generated May 30, 2012 11:34:04 AM by Hibernate Tools 3.2.1.GA

/**
 * SkupRevDetId generated by hbm2java
 */
public class SkupRevDetId implements java.io.Serializable {

    private int idSkupRev;
    private int idMenoRev;

    public SkupRevDetId() {
    }

    public SkupRevDetId(int idSkupRev, int idMenoRev) {
        this.idSkupRev = idSkupRev;
        this.idMenoRev = idMenoRev;
    }

    public int getIdSkupRev() {
        return this.idSkupRev;
    }

    public void setIdSkupRev(int idSkupRev) {
        this.idSkupRev = idSkupRev;
    }

    public int getIdMenoRev() {
        return this.idMenoRev;
    }

    public void setIdMenoRev(int idMenoRev) {
        this.idMenoRev = idMenoRev;
    }

    @Override
    public boolean equals(Object other) {
        if ((this == other)) {
            return true;
        }
        if ((other == null)) {
            return false;
        }
        if (!(other instanceof SkupRevDetId)) {
            return false;
        }
        SkupRevDetId castOther = (SkupRevDetId) other;

        return (this.getIdSkupRev() == castOther.getIdSkupRev())
                && (this.getIdMenoRev() == castOther.getIdMenoRev());
    }

    @Override
    public int hashCode() {
        int result = 17;

        result = 37 * result + this.getIdSkupRev();
        result = 37 * result + this.getIdMenoRev();
        return result;
    }
}
