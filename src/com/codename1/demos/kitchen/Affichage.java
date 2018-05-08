/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.codename1.demos.kitchen;

import com.codename1.components.Ads;
import com.codename1.components.ImageViewer;
import com.codename1.components.ScaleImageButton;
import com.codename1.components.SpanLabel;
import com.codename1.components.ToastBar;
import com.codename1.io.ConnectionRequest;
import com.codename1.io.JSONParser;
import com.codename1.io.Log;
import com.codename1.io.NetworkManager;
import com.codename1.l10n.ParseException;
import com.codename1.l10n.SimpleDateFormat;
import com.codename1.ui.Button;
import com.codename1.ui.Command;
import com.codename1.ui.Component;
import com.codename1.ui.Container;
import com.codename1.ui.Display;
import com.codename1.ui.EncodedImage;
import com.codename1.ui.FontImage;
import com.codename1.ui.Form;
import com.codename1.ui.Image;
import com.codename1.ui.InfiniteContainer;
import com.codename1.ui.Label;
import com.codename1.ui.TextArea;
import com.codename1.ui.TextField;
import com.codename1.ui.URLImage;
import com.codename1.ui.animations.CommonTransitions;
import com.codename1.ui.layouts.BorderLayout;
import com.codename1.ui.layouts.BoxLayout;
import com.codename1.ui.layouts.GridLayout;
import com.codename1.ui.layouts.LayeredLayout;
import com.codename1.ui.layouts.Layout;
import com.codename1.ui.plaf.Style;
import com.codename1.ui.plaf.UIManager;
import com.codename1.ui.util.Resources;
import services.FacebookShare;
import services.ServiceEvent;
import entities.Evenement;
import entities.Participation;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 *
 * @author sana
 */
public class Affichage extends Demo {

    Resources res;
    private Resources theme;
    Form f, form;
    SpanLabel lb;
    String url = "http://localhost/medina/web/EventsImages";
    EncodedImage enc;
    Label nomD, descD, prixD, dateD, lieuD;
    Button particip, modif, supp;
    private EncodedImage placeholder;

    // private Resources res;
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
        return "Evenemenets";
    }

    public Image getDemoIcon() {
        return getResources().getImage("6-knocker.jpg");
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
//        InfiniteContainer ic = new InfiniteContainer(10) {
        Demo[] demos = new Demo[]{
            new ModifEvent()
        };

        for (Demo d : demos) {
            d.init(res);
        }
        Container cc = new Container(BoxLayout.y());
        cc.setScrollableY(true);
        ServiceEvent SP = new ServiceEvent();
        ArrayList<Evenement> lis = SP.getList2();
        for (Evenement li : lis) {

            Container c = new Container(BoxLayout.y());
            Container ccc = new Container(BoxLayout.y());
            for (Demo d : demos) {
                //ccc.add(createDemoButton(d));
            }

            Image placeholder = Image.createImage(380, 300);
            EncodedImage enc = EncodedImage.createFromImage(placeholder, false);
            URLImage urlim = URLImage.createToStorage(enc, li.getImage(), url + "/" + li.getImage());
            ImageViewer imgV = new ImageViewer();
            imgV.setImage(urlim);

            TextField a = new TextField(li.getId_event());

            Label aa = new Label("Nom  : " + li.getNom().toString());
            Label desc = new Label("Description : " + li.getDescription().toString());
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            // String dateString = formatter.format(li.getDate());
            Label d = new Label("Date :" + li.getDate());
            //c.add(a);
            c.add(aa);
            c.add(desc);
            c.add(d);
            c.add(imgV);
            //f.add(c);
            cc.add(c);

            System.out.println("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaa");

            aa.addPointerPressedListener((l) -> {

                form = new Form(BoxLayout.x());
                Label lbser = new Label();
                Container F3 = new Container(BoxLayout.y());
                //Container x=new Container(BoxLayout.x());

                F3.add(lbser);
                // form.add(share);

                System.out.println("imaage");
                Image placeholder1 = Image.createImage(55, 50);
                EncodedImage en = EncodedImage.createFromImage(placeholder1, false);
                URLImage urli = URLImage.createToStorage(en, li.getImage(), url + "/" + li.getImage());
                ImageViewer img = new ImageViewer();
                img.setImage(urli);

                ConnectionRequest con = new ConnectionRequest();

                String url = "http://localhost/medina/web/app_dev.php/mobile/findEvent/" + li.getId_event();
                con.setUrl(url);

                con.addResponseListener((le) -> {
                    String reponse = new String(con.getResponseData());
                    System.out.println(reponse);
                    //lbser.setText(reponse);
                    nomD = new Label("Nom :" + li.getNom());
                    descD = new Label("Description :" + li.getDescription());
//                    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                    //String dateS = format.format(li.getDate());
                    dateD = new Label("Date :" + li.getDate());

                });
                NetworkManager.getInstance().addToQueueAndWait(con);
                System.out.println("test");
                particip = new Button("Participer");
                particip.addActionListener((evt) -> {
                    System.out.println("Share");
                    ServiceEvent serv = new ServiceEvent();
                    ServiceEvent part = new ServiceEvent();
                    Participation p = new Participation(
                            4, li.getId_event()
                    );
                    part.participer(li);
                    System.out.println("part");
//                    FacebookShare fb = new FacebookShare("EAACEdEose0cBAKH9SvjldA6ZCP27vLOvrpuQxWfEeAFKHWvJuP80JkuUqdSC79pRV5ZC8RewXdsfv3yMOgbzkIXHuotz9fHHwzbnsKmR23AyE6Eo74nLf8DcOLYe4Oqc5LWW3gG8kHVGEY0pBNMpSE6kocg1IzJko10OCgVsN4xZBDzE3BpCSkawQHkT21pmhcO4hZCiYAZDZD");
//                    try {
//                        fb.share("test");
//                        System.out.println("ba3d share");
//                    } catch (IOException ex) {
//                        System.out.println("erreur partage");
//                    }

                }
                );
                modif = new Button("Modifier");
                modif.addActionListener((evt) -> {

                    ModifEvent m = new ModifEvent(li);
                    //m.createDemo(li);
                    m.getF().show();
                });
                supp = new Button("Supprimer");
                supp.addActionListener((evt) -> {
                    ServiceEvent service = new ServiceEvent();
                    service.supprimerEvent(li);
                    //Ads ad = new Ads();
                    //ad.getAd();
                });
                F3.add(particip);
                F3.add(modif);
                F3.add(supp);
                F3.add(nomD);
                F3.add(descD);
                //F3.add(dateD);
                form.add(img);
                form.add(F3);
                form.show();
            });
            c.setLeadComponent(aa);
//            cc.add(c);
            System.out.println("test ba3d a");

        }
        // };
        return cc;
    }

    public Affichage() {
        //theme = UIManager.initFirstTheme("/theme_1");
        f = new Form(BoxLayout.y());

        Style s = UIManager.getInstance().getComponentStyle("Title");
        TextField searchField = new TextField("", "Toolbar Search");
        searchField.getHintLabel().setUIID("Title");
        searchField.setUIID("Title");
        searchField.getAllStyles().setAlignment(Component.LEFT);
        f.getToolbar().setTitleComponent(searchField);
        FontImage searchIcon = FontImage.createMaterial(FontImage.MATERIAL_SEARCH, s);
        searchField.addDataChangeListener((i1, i2) -> {
            String t = searchField.getText();
            if (t.length() < 1) {
                for (Component cmp : f.getContentPane()) {
                    cmp.setHidden(false);
                    cmp.setVisible(true);
                }
            } else {
                t = t.toLowerCase();
                for (Component cmp : f.getContentPane()) {
                    String val = null;
                    if (cmp instanceof Label) {
                        val = ((Label) cmp).getText();
                    } else {
                        if (cmp instanceof TextArea) {
                            val = ((TextArea) cmp).getText();
                        } else {
                            val = (String) cmp.getPropertyValue("text");
                        }
                    }
                    boolean show = val != null && val.toLowerCase().indexOf(t) > -1;
                    cmp.setHidden(!show); // <3>
                    cmp.setVisible(show);
                }
            }
            f.getContentPane().animateLayout(250);
        });
        f.getToolbar().addCommandToRightBar("", searchIcon, (e) -> {
            searchField.startEditingAsync(); // <4>
        });

        lb = new SpanLabel("");
        f.add(lb);
        ServiceEvent SP = new ServiceEvent();
        ArrayList<Evenement> lis = SP.getList2();
        for (Evenement li : lis) {
            Container cc = new Container(BoxLayout.x());
            Container c = new Container(BoxLayout.y());

            Image placeholder = Image.createImage(380, 300);
            EncodedImage enc = EncodedImage.createFromImage(placeholder, false);
            URLImage urlim = URLImage.createToStorage(enc, li.getImage(), url + "/" + li.getImage());
            ImageViewer imgV = new ImageViewer();
            imgV.setImage(urlim);

            TextField a = new TextField(li.getId_event());

            Label aa = new Label("Nom  : " + li.getNom().toString());
            Label desc = new Label("Description : " + li.getDescription().toString());
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            // String dateString = formatter.format(li.getDate());
            Label d = new Label("Date :" + li.getDate());
            //c.add(a);
            c.add(aa);
            c.add(desc);
            //c.add(d);
            c.add(imgV);
            f.add(c);

            System.out.println("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaa");

            aa.addPointerPressedListener((l) -> {

                form = new Form(BoxLayout.x());
                Label lbser = new Label();
                Container F3 = new Container(BoxLayout.y());
                //Container x=new Container(BoxLayout.x());

                F3.add(lbser);
                // form.add(share);

                System.out.println("imaage");
                Image placeholder1 = Image.createImage(55, 50);
                EncodedImage en = EncodedImage.createFromImage(placeholder1, false);
                URLImage urli = URLImage.createToStorage(en, li.getImage(), url + "/" + li.getImage());
                ImageViewer img = new ImageViewer();
                img.setImage(urli);

                ConnectionRequest con = new ConnectionRequest();

                String url = "http://localhost/medina/web/app_dev.php/mobile/findEvent/" + li.getId_event();
                con.setUrl(url);

                con.addResponseListener((le) -> {
                    String reponse = new String(con.getResponseData());
                    System.out.println(reponse);
                    //lbser.setText(reponse);
                    nomD = new Label("Nom :" + li.getNom());
                    descD = new Label("Description :" + li.getDescription());
//                    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                    //String dateS = format.format(li.getDate());
//                    dateD = new Label("Date :" + li.getDate());

                });
                NetworkManager.getInstance().addToQueueAndWait(con);
                System.out.println("test");
                particip = new Button("Participer");
                particip.addActionListener((evt) -> {
                    System.out.println("Share");
                    ServiceEvent serv = new ServiceEvent();
                    FacebookShare fb = new FacebookShare("EAACEdEose0cBAKH9SvjldA6ZCP27vLOvrpuQxWfEeAFKHWvJuP80JkuUqdSC79pRV5ZC8RewXdsfv3yMOgbzkIXHuotz9fHHwzbnsKmR23AyE6Eo74nLf8DcOLYe4Oqc5LWW3gG8kHVGEY0pBNMpSE6kocg1IzJko10OCgVsN4xZBDzE3BpCSkawQHkT21pmhcO4hZCiYAZDZD");
                    try {
                        fb.share("test");
                        System.out.println("ba3d share");
//                   String u="http://localhost/medina/web/app_dev.php/mobile/particip?id="+5+"&event="+li.getId_event();
//                   con.setUrl(u);
//                   System.out.println(u);
                    } catch (IOException ex) {
                        System.out.println("erreur partage");
                    }

                }
                );
                modif = new Button("Modifier");
                modif.addActionListener((evt) -> {
                    ModifEvent m = new ModifEvent(li);
                    m.getF().show();
                });
                supp = new Button("Supprimer");
                supp.addActionListener((evt) -> {
                    ServiceEvent service = new ServiceEvent();
                    service.supprimerEvent(li);
                    //Ads ad = new Ads();
                    //ad.getAd();
                });
                F3.add(particip);
                F3.add(modif);
                F3.add(supp);
                F3.add(nomD);
                F3.add(descD);
                //F3.add(dateD);
                form.add(img);
                form.add(F3);
                form.show();
            });
            c.setLeadComponent(aa);

            System.out.println("test ba3d a");

        }
        // lb.setText(lis.toString());
        f.getToolbar().addCommandToLeftBar("back", null, (ev) -> {
            AEvenement h;
            try {
                h = new AEvenement();
                h.getF().show();
            } catch (ParseException ex) {
            }

        });
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
