/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package services;

import com.codename1.io.CharArrayReader;
import com.codename1.io.ConnectionRequest;
import com.codename1.io.JSONParser;
import com.codename1.io.NetworkEvent;
import com.codename1.io.NetworkManager;
import com.codename1.ui.Dialog;
import com.codename1.ui.events.ActionListener;
//import com.mycompany.gui.AffichageP;
import entities.Produit;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Amine
 */
public class ServiceProduit {

    public ArrayList<Produit> getListMine() {
        ArrayList<Produit> listEv = new ArrayList<>();
        ConnectionRequest con = new ConnectionRequest();
        con.setUrl("http://localhost/medina/web/app_dev.php/mobile/findProdMine?prop=" + 4);
        con.addResponseListener(e -> {
            JSONParser jsonp = new JSONParser();
            try {
                Map<String, Object> buss = jsonp.parseJSON(new CharArrayReader(new String(con.getResponseData()).toCharArray()));

                List<Map<String, Object>> list = (List<Map<String, Object>>) buss.get("root");
                for (Map<String, Object> obj : list) {

                    Produit task = new Produit();

                    task.setRef((int) Float.parseFloat(obj.get("ref").toString()));

                    task.setNomP(obj.get("nomp").toString());

                    task.setImg(obj.get("img").toString());

                    task.setPrix((Float) Float.parseFloat(obj.get("prix").toString()));

                    task.setQuantit((int) Float.parseFloat(obj.get("quantit").toString()));

                    task.setDescrip(obj.get("descrip").toString());

                    listEv.add(task);

                }
            } catch (IOException ex) {
            }

        });
        NetworkManager.getInstance().addToQueueAndWait(con);
        return listEv;
    }

    public void ajouterProduit(Produit ta) {
        ConnectionRequest con = new ConnectionRequest();
        String Url = "http://localhost/medina/web/app_dev.php/mobile/ajouterProduit?nom=" + ta.getNomP()
                + "&image=" + ta.getImg()
                + "&prix=" + ta.getPrix()
                + "&quantit=" + ta.getQuantit()
                + "&description=" + ta.getDescrip();

        con.setUrl(Url);

        con.addResponseListener((e) -> {
            String str = new String(con.getResponseData());
            System.out.println(str);

        });
        NetworkManager.getInstance().addToQueueAndWait(con);
        Dialog.show("Succés", "Produit ajouté", "Mriguel", null);
    }

    public void modierProduit(Produit p) {
        ConnectionRequest con = new ConnectionRequest();
        String Url = "http://localhost/medina/web/app_dev.php/mobile/modifProd/"
                + p.getRef()+"/"
                 + p.getNomP()+"/"
                 + p.getDescrip()+"/"           
                 + p.getPrix()+"/"
                + p.getQuantit()
                ;

        con.setUrl(Url);

        con.addResponseListener((e) -> {
            String str = new String(con.getResponseData());
            System.out.println(str);
        });
        NetworkManager.getInstance().addToQueueAndWait(con);
    }

    public void supProduit(Produit p) {
        ConnectionRequest con = new ConnectionRequest();
        String Url = "http://localhost/medina/web/app_dev.php/mobile/suppProd/" + p.getRef();
        con.setUrl(Url);
        con.addResponseListener((e) -> {
            String str = new String(con.getResponseData());
            System.out.println(str);
            Dialog.show("Mriguel", "Produit supprimer", "waaaw", null);
            // AffichageP k = new AffichageP();
            // k.getF().show();
        });
        NetworkManager.getInstance().addToQueueAndWait(con);
    }

    public ArrayList<Produit> getList2() {
        ArrayList<Produit> listTasks = new ArrayList<>();
        ConnectionRequest con = new ConnectionRequest();
        con.setUrl("http://localhost/medina/web/app_dev.php/mobile/allProd");
        con.addResponseListener(new ActionListener<NetworkEvent>() {
            @Override
            public void actionPerformed(NetworkEvent evt) {
                //listTasks = getListTask(new String(con.getResponseData()));
                JSONParser jsonp = new JSONParser();

                try {
                    Map<String, Object> tasks = jsonp.parseJSON(new CharArrayReader(new String(con.getResponseData()).toCharArray()));
                    System.out.println(tasks);
                    //System.out.println(tasks);
                    List<Map<String, Object>> list = (List<Map<String, Object>>) tasks.get("root");
                    for (Map<String, Object> obj : list) {
                        Produit task = new Produit();

                        task.setRef((int) Float.parseFloat(obj.get("ref").toString()));

                        task.setNomP(obj.get("nomp").toString());

                        task.setImg(obj.get("img").toString());

                        task.setPrix((Float) Float.parseFloat(obj.get("prix").toString()));

                        task.setQuantit((int) Float.parseFloat(obj.get("quantit").toString()));

                        task.setDescrip(obj.get("descrip").toString());

                        listTasks.add(task);

                    }
                } catch (IOException ex) {
                }

            }
        });
        NetworkManager.getInstance().addToQueueAndWait(con);
        return listTasks;
    }

}
