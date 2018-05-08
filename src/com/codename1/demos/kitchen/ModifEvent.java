/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.codename1.demos.kitchen;

import com.codename1.components.ImageViewer;
import com.codename1.io.ConnectionRequest;
import com.codename1.io.NetworkManager;
import com.codename1.l10n.SimpleDateFormat;
import com.codename1.ui.Button;
import com.codename1.ui.Container;
import com.codename1.ui.EncodedImage;
import com.codename1.ui.Form;
import com.codename1.ui.Image;
import com.codename1.ui.Label;
import com.codename1.ui.TextField;
import com.codename1.ui.URLImage;
import com.codename1.ui.layouts.BoxLayout;
import com.codename1.ui.util.Resources;
import entities.Evenement;
import java.io.IOException;
import java.util.ArrayList;
import services.FacebookShare;
import services.ServiceEvent;

/**
 *
 * @author CHAIMA
 */
public class ModifEvent extends Demo{
    Form f;
    Container c;
    TextField ttitre;
    TextField tdescription;
    TextField tlieu;
    TextField tprix;
    TextField tnbreticket;
    Button btnMod,btnAnnuler;
    Resources res;
    private Resources theme;
    
    @Override
    public String getDisplayName() {
        return "Modification de l'evenement";
    }

    @Override
    public Image getDemoIcon() {
        return getResources().getImage("dog.jpg");
    }
    
    public Container createDemo(Evenement ta) {
        //Container cc = new Container(BoxLayout.y());
        //cc.setScrollableY(true);
        Affichage a = new Affichage();
        
         c = new Container(BoxLayout.y());
        c.setScrollableY(true); 
        ttitre = new TextField(ta.getNom());
        tdescription = new TextField(ta.getDescription());
        tprix = new TextField(ta.getPrix());

        tlieu = new TextField(ta.getLieu());
        tnbreticket = new TextField(ta.getTicket());
        
        btnMod = new Button("Modifier");
        btnAnnuler=new Button("Annuler");
        c.add(ttitre);
        c.add(tdescription);
        c.add(tprix);

        c.add(tlieu);
        c.add(tnbreticket);
        
        c.add(btnMod);
        c.add(btnAnnuler);
        btnMod.addActionListener((e) -> {
            ServiceEvent ser = new ServiceEvent();
            ta.setNom(ttitre.getText());
           
            ta.setDescription(tdescription.getText());
            ta.setPrix(tprix.getText());
             ta.setLieu(tlieu.getText());
            ta.setTicket(Integer.parseInt(tnbreticket.getText()));
//            Event t = new Event( ttitre.getText(),
//                    tlieu.getText() ,
//                    Double.valueOf(tprix.getText()), 
//                   // Double.parseDouble(tprix.getText()),
//                    tdescription.getText(),     
//                    Integer.parseInt(tnbreticket.getText()));
//            //Event t = new Event( ttitre.getText(), tdescription.getText(), Double.parseDouble(tprix.getText()), tlieu.getText() ,Integer.parseInt(tnbreticket.getText()));
//
            ser.modifierEvent(ta);
//                        Dialog.show("Succés", "Evenement modifié", "ok", null);

            

        }); 
        //f.add(c);
        return c;
    }

    public ModifEvent(){}
    public ModifEvent( Evenement ta) {
        f = new Form("Modification");
        c = new Container(BoxLayout.y());
        c.setScrollableY(true); 
        ttitre = new TextField(ta.getNom());
        tdescription = new TextField(ta.getDescription());
        tprix = new TextField(ta.getPrix());

        tlieu = new TextField(ta.getLieu());
        tnbreticket = new TextField(ta.getTicket());
        
        btnMod = new Button("Modifier");
        btnAnnuler=new Button("Annuler");
        c.add(ttitre);
        c.add(tdescription);
        c.add(tprix);

        c.add(tlieu);
        c.add(tnbreticket);
        
        c.add(btnMod);
        c.add(btnAnnuler);
        btnMod.addActionListener((e) -> {
            ServiceEvent ser = new ServiceEvent();
            ta.setNom(ttitre.getText());
           
            ta.setDescription(tdescription.getText());
            ta.setPrix(tprix.getText());
             ta.setLieu(tlieu.getText());
            ta.setTicket(Integer.parseInt(tnbreticket.getText()));
//            Event t = new Event( ttitre.getText(),
//                    tlieu.getText() ,
//                    Double.valueOf(tprix.getText()), 
//                   // Double.parseDouble(tprix.getText()),
//                    tdescription.getText(),     
//                    Integer.parseInt(tnbreticket.getText()));
//            //Event t = new Event( ttitre.getText(), tdescription.getText(), Double.parseDouble(tprix.getText()), tlieu.getText() ,Integer.parseInt(tnbreticket.getText()));
//
            ser.modifierEvent(ta);
//                        Dialog.show("Succés", "Evenement modifié", "ok", null);

            

        }); 
        
        btnAnnuler.addActionListener((e)->{
            Affichage a = new Affichage();
            a.createDemo();

        });
        f.add(c);
    }

    public Container getC(){
        return c;
    }

    public Form getF() {
        return f;
    }

    public void setF(Form f) {
        this.f = f;
    }

    public TextField getTnom() {
        return ttitre;
    }

    public void setTnom(TextField ttitre) {
        this.ttitre = ttitre;
    }
    
}
