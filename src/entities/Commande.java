/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entities;

/**
 *
 * @author Dell
 */
public class Commande {
    private int idClientCommande;
    private String nomUser;

    public int getIdClientCommande() {
        return idClientCommande;
    }

    public void setIdClientCommande(int idClientCommande) {
        this.idClientCommande = idClientCommande;
    }

    public String getNomUser() {
        return nomUser;
    }

    public void setNomUser(String nomUser) {
        this.nomUser = nomUser;
    }

    public Commande(int idClientCommande, String nomUser) {
        this.idClientCommande = idClientCommande;
        this.nomUser = nomUser;
    }

    public Commande() {
    }
    
}
