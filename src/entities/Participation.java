/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entities;

/**
 *
 * @author CHAIMA
 */
public class Participation {
    
    private int idParticipation;
    private int idUSer;
    private int idEvent;

    public Participation(int idUSer, int idEvent) {
        this.idUSer = idUSer;
        this.idEvent = idEvent;
    }

    public int getIdParticipation() {
        return idParticipation;
    }

    public void setIdParticipation(int idParticipation) {
        this.idParticipation = idParticipation;
    }

    public int getIdUSer() {
        return idUSer;
    }

    public void setIdUSer(int idUSer) {
        this.idUSer = idUSer;
    }

    public int getIdEvent() {
        return idEvent;
    }

    public void setIdEvent(int idEvent) {
        this.idEvent = idEvent;
    }

    @Override
    public String toString() {
        return "Participation{" + "idParticipation=" + idParticipation + ", idUSer=" + idUSer + ", idEvent=" + idEvent + '}';
    }
    
    
    
}
