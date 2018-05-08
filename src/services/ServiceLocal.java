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
import com.codename1.ui.TextArea;
import com.codename1.ui.events.ActionListener;
import com.codename1.ui.layouts.BorderLayout;
import entities.Local;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONObject;


/**
 *
 * @author CHAIMA
 */
public class ServiceLocal {
    private ConnectionRequest connectionRequest;
    ConnectionRequest con = new ConnectionRequest();
    String url = "http://localhost/medina1/web/app_dev.php/mobile/";
    ArrayList<Local> listAnnonces = new ArrayList<>();
    
    public void ajoutEvent(Local ta) {
        ConnectionRequest con = new ConnectionRequest();
        String Url = "http://localhost/medina1/web/app_dev.php/mobile/local/new?description="
                + ta.getDescription()
                + "&nomLocal=" + ta.getNom_local()
                + "&superficie=" + ta.getSuperficie()
                + "&localisation=" + ta.getLocalisation()
                + "&imgl1=" + ta.getImgl1()
                + "&prop=" + ta.getProp()
                + "&lat=" + ta.getLat()
                + "&lang=" + ta.getLang();

                
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
        Dialog.show("Succés", "Evenement ajouté", "ok", null);
    }
    
    
    
    public ArrayList<Local> getList2() {
        ArrayList<Local> listTasks = new ArrayList<>();
        ConnectionRequest con = new ConnectionRequest();
        con.setUrl("http://localhost/medina1/web/app_dev.php/mobile/local/all");
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
                        Local task = new Local();
                        task.setNom_local(obj.get("description").toString());
                        task.setDescription(obj.get("description").toString());
                        task.setSuperficie(Float.parseFloat(obj.get("superficie").toString()));
//                        
//                         float superficie = Float.parseFloat(obj.get("superficie").toString());
//                          task.setSuperficie(superficie);
                        task.setLocalisation(obj.get("localisation").toString());
                        task.setImgl1(obj.get("imgl1").toString());

//                        float prop = Float.parseFloat(obj.get("prop").toString());
//
//                        task.setProp((int) prop);

                        float lat = Float.parseFloat(obj.get("lat").toString());

                        task.setLat((double) lat);

                         float lang = Float.parseFloat(obj.get("lang").toString());
                        
                        task.setLang((double)lang);
                      
                       

                        listTasks.add(task);

                    }
                } catch (IOException ex) {
                }

            }
        });
        NetworkManager.getInstance().addToQueueAndWait(con);
        return listTasks;
    }
   
     public ArrayList<Local> getJSONListAnnonce(String json) {
        ArrayList<Local> listAnnonces = new ArrayList<>();
        try {
            System.out.println(json);
            JSONParser j = new JSONParser();
            Map<String, Object> annonces = j.parseJSON(new CharArrayReader(json.toCharArray()));
            System.out.println(annonces);
            List<Map<String, Object>> list = (List<Map<String, Object>>) annonces.get("root");
            for (Map<String, Object> obj : list) {
                Local task = new Local();
           
                task.setNom_local(obj.get("nom").toString());
                task.setDescription(obj.get("description").toString());
                task.setSuperficie(Float.parseFloat(obj.get("superficie").toString()));
                task.setLocalisation(obj.get("localisation").toString());
                task.setImgl1(obj.get("localisation").toString());

                task.setProp((int) (double) obj.get("prop"));

                task.setProp((int) (double) obj.get("lat"));

                task.setProp((int) (double) obj.get("lang"));

                      
                       

               
                listAnnonces.add(task);
            }
        } catch (IOException ex) {
        }
        System.out.println(listAnnonces);
        return listAnnonces;

    }

   
    
     public ArrayList<Local> getListAnnonce() {
        con = new ConnectionRequest();
        con.setUrl("http://localhost/medina/web/app_dev.php/api/local/all");

        con.addResponseListener(new ActionListener<NetworkEvent>() {
            @Override
            public void actionPerformed(NetworkEvent evt) {
                listAnnonces = getJSONListAnnonce(new String(con.getResponseData()));
            }
        });
        NetworkManager.getInstance().addToQueueAndWait(con);
        return listAnnonces;
    }
  

    
}
