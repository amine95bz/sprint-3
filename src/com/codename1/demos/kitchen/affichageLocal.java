/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.codename1.demos.kitchen;



import com.codename1.components.SpanLabel;
import com.codename1.ui.Command;
import com.codename1.ui.Container;
import com.codename1.ui.Display;
import com.codename1.ui.EncodedImage;
import com.codename1.ui.FontImage;
import com.codename1.ui.Form;
import com.codename1.ui.Image;
import com.codename1.ui.Label;
import com.codename1.ui.animations.CommonTransitions;
import com.codename1.ui.layouts.BorderLayout;
import com.codename1.ui.layouts.BoxLayout;
import com.codename1.ui.plaf.UIManager;
import com.codename1.ui.util.Resources;

import entities.Local;
import java.util.ArrayList;
import services.ServiceLocal;

/**
 *
 * @author sana
 */
public class affichageLocal extends Demo {
Resources res;
    private Resources theme;

   // Form f;
   // SpanLabel lb;
    Form f,F3;
    SpanLabel lb;
 // Database db;
      String url="http://localhost/medina1/web/EventsImages";

    EncodedImage enc;
   // URLImage uRLImage;
    ////////////////////////////////////////////
    private Form currentForm;
    private Container tabletSurface;
    private Command gridCommand;
    private Command listCommand;

    private Object imageMask;
    private int maskWidth;
    private int maskHeight;
    private Object circleMask;
    private int circleMaskWidth;
    private int circleMaskHeight;

    private int[] colors;
    private Image[] colorBottoms;
    private int currentColor;

    public String getDisplayName() {
        return "Local";
    }

    public Image getDemoIcon() {
        return getResources().getImage("background.jpg");
    }

    private DemoComponent createDemoButton(Demo d) {
        DemoComponent dc = new DemoComponent(d.getDisplayName(), d.getDemoIcon(), imageMask,
                maskWidth, maskHeight, colorBottoms[currentColor],
                circleMask, res.getImage("circle-line.png"), circleMaskWidth, circleMaskHeight,
                "Blank" + (currentColor + 1));
        currentColor++;
        if (currentColor == colorBottoms.length) {
            currentColor = 0;
        }
        dc.addActionListener(e -> {
            if (Display.getInstance().isTablet()) {
                tabletSurface.getAnimationManager().flushAnimation(() -> {
                    tabletSurface.replace(tabletSurface.getComponentAt(0), d.createDemo(Display.getInstance().getCurrent()),
                            CommonTransitions.createCover(CommonTransitions.SLIDE_HORIZONTAL, true, 200));
                });
            } else {
                Form previous = Display.getInstance().getCurrent();
                Form f = new Form(d.getDisplayName(), new BorderLayout());
                f.add(BorderLayout.CENTER, d.createDemo(f));
                f.getToolbar().setBackCommand(" ", ee -> {
                    if (d.onBack()) {
                        previous.showBack();
                    }
                });
                f.getToolbar().addMaterialCommandToRightBar("", FontImage.MATERIAL_INFO, 4, ee -> {
                    //showDemoInformation(f, d);
                });
                f.show();
            }
        });
        return dc;
    }
    
    public Container createDemo() {
        
        Container cc = new Container(BoxLayout.y());
         ServiceLocal SP=new ServiceLocal();
        ArrayList<Local> lis=SP.getList2();
//         lb.setText(lis.toString());
          

         for (Local li : lis) {
//            Image placeholder = Image.createImage(380, 300);
//            
//            EncodedImage enc = EncodedImage.createFromImage(placeholder, false);
//            URLImage urlim = URLImage.createToStorage(enc,li.getImgl1(), url+"/"+li.getImgl1());
//            ImageViewer imgV=new ImageViewer();
//            imgV.setImage(urlim);
            Label aa= new Label(li.getNom_local().toString());
            Label desc = new Label(li.getDescription().toString());
            Label localisation = new Label(li.getLocalisation().toString());
//             Label prop = new Label((li.getProp());
//             Label lat = new Label( Float.valueOf(li.getLat()));
//             Label lang = new Label(li.getLang());
             cc.add(aa);
            cc.add(desc);
//             f.add(imgV);
             cc.add(localisation);
//             f.add(prop);
//             f.add(lat);
//             f.add(lang);

             //f.add(F3);
             
            
           
       
        }
        return cc;
    }

    
    
  //////////////////////////////////////////////////////
    public affichageLocal() {
         theme = UIManager.initFirstTheme("/theme_1");
        f = new Form(BoxLayout.y());
        F3 = new Form(BoxLayout.x());
        lb = new SpanLabel("");
        f.add(lb);
        ServiceLocal SP=new ServiceLocal();
        ArrayList<Local> lis=SP.getList2();
//         lb.setText(lis.toString());
          

         for (Local li : lis) {
//            Image placeholder = Image.createImage(380, 300);
//            
//            EncodedImage enc = EncodedImage.createFromImage(placeholder, false);
//            URLImage urlim = URLImage.createToStorage(enc,li.getImgl1(), url+"/"+li.getImgl1());
//            ImageViewer imgV=new ImageViewer();
//            imgV.setImage(urlim);
            Label aa= new Label(li.getNom_local().toString());
            Label desc = new Label(li.getDescription().toString());
            Label localisation = new Label(li.getLocalisation().toString());
//             Label prop = new Label((li.getProp());
//             Label lat = new Label( Float.valueOf(li.getLat()));
//             Label lang = new Label(li.getLang());
             f.add(aa);
             f.add(desc);
//             f.add(imgV);
             f.add(localisation);
//             f.add(prop);
//             f.add(lat);
//             f.add(lang);

             f.add(F3);
             
            
           
       
        }
        lb.setText(lis.toString());
         // f.getToolbar().addCommandToLeftBar("back", null, (ev)->{AddLocal h=new AddLocal();
          //h.getF().show();
          
          //});
    }     
//        f = new Form();
//        lb = new SpanLabel("");
//        f.add(lb);
//        ServiceEvent serviceTask=new ServiceEvent();
//        ArrayList<Evenement> lis=serviceTask.getList2();
//        lb.setText(lis.toString());
////////          f.getToolbar().addCommandToRightBar("back", null, (ev)->{AEvenement h=new AEvenement(int idEvent ,
////////                  string nom ,
////////                  string description);
////////          h.getF().show();
////////          });
//    }
//
    public Form getF() {
        return f;
    }

    public void setF(Form f) {
        this.f = f;
    }

}
