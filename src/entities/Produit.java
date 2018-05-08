package entities;


/**
 *
 * @author CHAIMA
 */
public class Produit {
     private int ref ;
     public String nomP;
     private float prix;
     public String img;
     private int quantit;
     private String descrip;
     public String nomV;
     
     

    public Produit(String nomP, float prix, String img ,int quantit, String descrip) {
        this.nomP = nomP;
        this.prix = prix;
        this.img = img;
        this.quantit = quantit;
        this.descrip = descrip;
    }

    public Produit(int ref, String nomP, float prix, int quantit, String descrip) {
        this.ref = ref;
        this.nomP = nomP;
        this.prix = prix;
        this.quantit = quantit;
        this.descrip = descrip;
    }
     

     
    public Produit(int ref, String nomP, float prix, String nomV) {
        this.ref = ref;
        this.nomP = nomP;
        this.prix = prix;
        this.nomV = nomV;

    }
     
      public Produit (String nomP,  String descrip /*String img,*/){
        
        this.nomP = nomP;
//        this.prix = prix;         
//        this.quantit = quantit;       
        this.descrip = descrip;
//        this.img = img;

        
    }

    public Produit() {
    }

   

    public int getRef() {
        return ref;
    }

    public String getNomP() {
        return nomP;
    }

    public float getPrix() {
        return prix;
    }

    public String getImg() {
        return img;
    }

    public int getQuantit() {
        return quantit;
    }
    

    public String getDescrip() {
        return descrip;
    }

    public String getNomV() {
        return nomV;
    }



    public void setRef(int ref) {
        this.ref = ref;
    }

    public void setNomP(String nomP) {
        this.nomP = nomP;
    }

    public void setPrix(float prix) {
        this.prix = prix;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public void setQuantit(int quantit) {
        this.quantit = quantit;
    }

    public void setDescrip(String descrip) {
        this.descrip = descrip;
    }

    public void setNomV(String nomV) {
        this.nomV = nomV;
    }   

    @Override
    public String toString() {
        return "Produit{" + "ref=" + ref + ", nomP=" + nomP + ", prix=" + prix + ", img=" + img + ", quantit=" + quantit + ", descrip=" + descrip + ", nomV=" + nomV + '}';
    }
     
    
    
    
    
}
