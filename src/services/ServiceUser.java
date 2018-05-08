/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package services;

import entities.Produit;
import entities.User1;
import com.codename1.io.ConnectionRequest;
import com.codename1.io.NetworkManager;

/**
 *
 * @author fares
 */
public class ServiceUser {
     public void ajoutUser(User1 u) {
        ConnectionRequest con = new ConnectionRequest();
    
        String Url = "http://localhost/medina1/web/app_dev.php/api/user/Add?"
                +"username="+u.getUsername()
                +"&mail=" +u.getEmail()
                +"&pass=" +u.getPasswaord()
                +"&photo="+u.getPhoto();
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
    
}
