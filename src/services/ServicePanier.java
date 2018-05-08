/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package services;

import entities.Commande;
import entities.Produit;
import com.codename1.io.CharArrayReader;
import com.codename1.io.ConnectionRequest;
import com.codename1.io.JSONParser;
import com.codename1.io.NetworkEvent;
import com.codename1.io.NetworkManager;
import com.codename1.ui.events.ActionListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Dell
 */
public class ServicePanier {
    
    public void ajoutCommande(Commande C) {
      
        ConnectionRequest con = new ConnectionRequest();
        String Url = "http://localhost/medina/web/app_dev.php/mobile/commande/Add?idClient=" + C.getIdClientCommande() + "&Nom=" + C.getNomUser();
        con.setUrl(Url);

        con.addResponseListener((e) -> {
            String str = new String(con.getResponseData());
            System.out.println(str);
//            if (str.trim().equalsIgnoreCase("OK")) {
//                f2.setTitle(tlogin.getText());
//             f2.show();
//            }
//            else{
//            Dialog.show("error", "login ou pwd invalid", "ok", null);
//            }
        });
        NetworkManager.getInstance().addToQueueAndWait(con);
    }
    
    public void ajoutProduitCommande(Produit P) {
        ConnectionRequest con = new ConnectionRequest();
    
        String Url = "http://localhost/medina1/web/app_dev.php/api/commande/AddProd?idProduit="+P.getRef()+
                "&quantite=1&NomProduit=" + P.getNomP();
        con.setUrl(Url);

        con.addResponseListener((e) -> {
            String str = new String(con.getResponseData());
            System.out.println(str);
//            if (str.trim().equalsIgnoreCase("OK")) {
//                f2.setTitle(tlogin.getText());
//             f2.show();
//            }
//            else{
//            Dialog.show("error", "login ou pwd invalid", "ok", null);
//            }
        });
        NetworkManager.getInstance().addToQueueAndWait(con);
    }
    public ArrayList<Produit> getList2() {
        ArrayList<Produit> listProduit = new ArrayList<>();
        ConnectionRequest con = new ConnectionRequest();
        con.setUrl("http://localhost/medina1/web/app_dev.php/api/produits/all");
        con.addResponseListener((NetworkEvent evt) -> {
            JSONParser jsonp = new JSONParser();
            
            try {
                Map<String, Object> produitss = jsonp.parseJSON(new CharArrayReader(new String(con.getResponseData()).toCharArray()));
                System.out.println(produitss);
                List<Map<String, Object>> list = (List<Map<String, Object>>) produitss.get("root");
                for (Map<String, Object> obj : list) {
                    Produit produits = new Produit();
                    float id = Float.parseFloat(obj.get("ref").toString());
                    float prixProduits = Float.parseFloat(obj.get("prix").toString());
                    produits.setRef((int) id);
                    produits.setNomP(obj.get("nomp").toString());
                    produits.setDescrip(obj.get("descrip").toString());
                    produits.setImg(obj.get("img").toString());
                    
                    produits.setPrix(prixProduits);
                    listProduit.add(produits);
                }
            } catch (IOException ex) {
            }
        });
        NetworkManager.getInstance().addToQueueAndWait(con);
        return listProduit;
    }

}
