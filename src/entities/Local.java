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
public class Local {
    private int id_Local;
    private String description ;
    private String nom_local;
  
    private float superficie ;
    private String localisation;
    private String imgl1;
  
    public int prop;
    public double lat;
     public double lang;

    

    public Local() {
    }

    public Local(String nom_local) {
        this.nom_local = nom_local;
    }

    public Local(int id_Local, String description, String nom_local, float superficie, String localisation, String imgl1, int prop) {
        this.id_Local = id_Local;
        this.description = description;
        this.nom_local = nom_local;
        this.superficie = superficie;
        this.localisation = localisation;
        this.imgl1 = imgl1;
        this.prop = prop;
    }

    public Local(double lat, double lang) {
        this.lat = lat;
        this.lang = lang;
    }
    
      

    public Local(String description, String nom_local, float superficie, String localisation, String imgl1, int prop, double lat, double lang) {
        this.description = description;
        this.nom_local = nom_local;
        this.superficie = superficie;
        this.localisation = localisation;
        this.imgl1 = imgl1;
        this.prop = prop;
        this.lat = lat;
        this.lang = lang;
    }
        
        
        
        
        

    public Local(String description, String nom_local, float superficie, String localisation, int prop,double lat,double lang) {
        this.description = description;
        this.nom_local = nom_local;
        this.superficie = superficie;
        this.localisation = localisation;
        this.prop = prop;
        this.lat = lat;
        this.lang = lang;
    }

    public Local(String description, String nom_local, float superficie, String localisation, String imgl1, int prop) {
        this.description = description;
        this.nom_local = nom_local;
        this.superficie = superficie;
        this.localisation = localisation;
        this.imgl1 = imgl1;
        this.prop = prop;
    }

    public Local(int id_Local, String description, String nom_local, float superficie, String localisation) {
        this.id_Local = id_Local;
        this.description = description;
        this.nom_local = nom_local;
        this.superficie = superficie;
        this.localisation = localisation;
    }
    

    
    

   
    public Local(String description, String nom_local, float superficie, String localisation) {
        this.description = description;
        this.nom_local = nom_local;
        this.superficie = superficie;
        this.localisation = localisation;
    }

    public int getId_Local() {
        return id_Local;
    }

    public void setId_Local(int id_Local) {
        this.id_Local = id_Local;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getNom_local() {
        return nom_local;
    }

    public void setNom_local(String nom_local) {
        this.nom_local = nom_local;
    }

  

    public float getSuperficie() {
        return superficie;
    }

    public void setSuperficie(float superficie) {
        this.superficie = superficie;
    }

    public String getLocalisation() {
        return localisation;
    }

    public void setLocalisation(String localisation) {
        this.localisation = localisation;
    }

    public String getImgl1() {
        return imgl1;
    }

    public void setImgl1(String imgl1) {
        this.imgl1 = imgl1;
    }

  

    public int getProp() {
        return prop;
    }

    public void setProp(int prop) {
        this.prop = prop;
    }

    public double getLat() {
        return lat;
    }

    public double getLang() {
        return lang;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public void setLang(double lang) {
        this.lang = lang;
    }
    
    
    
}
