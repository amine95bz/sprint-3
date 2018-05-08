/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.codename1.demos.kitchen;

import entities.Commande;
import entities.Produit;
import entities.User1;
import services.ServicePanier;
import services.ServiceUser;
import com.codename1.charts.ChartComponent;
import com.codename1.charts.models.CategorySeries;
import com.codename1.charts.models.XYMultipleSeriesDataset;
import com.codename1.charts.models.XYSeries;
import com.codename1.charts.renderers.DefaultRenderer;
import com.codename1.charts.renderers.SimpleSeriesRenderer;
import com.codename1.charts.renderers.XYMultipleSeriesRenderer;
import com.codename1.charts.renderers.XYSeriesRenderer;
import com.codename1.charts.views.LineChart;
import com.codename1.charts.views.PieChart;
import com.codename1.charts.views.PointStyle;
import com.codename1.components.Ads;
import com.codename1.components.ImageViewer;
import com.codename1.components.MultiButton;
import com.codename1.components.SpanLabel;
import com.codename1.db.Cursor;
import com.codename1.db.Database;
import com.codename1.io.FileSystemStorage;
import com.codename1.l10n.L10NManager;
import com.codename1.l10n.SimpleDateFormat;
import com.codename1.messaging.Message;
import com.codename1.ui.Button;
import com.codename1.ui.Command;
import com.codename1.ui.Component;
import static com.codename1.ui.Component.CENTER;
import com.codename1.ui.ComponentGroup;
import com.codename1.ui.Container;
import com.codename1.ui.Dialog;
import com.codename1.ui.Display;
import com.codename1.ui.EncodedImage;
import com.codename1.ui.Font;
import com.codename1.ui.FontImage;
import com.codename1.ui.Form;
import com.codename1.ui.Image;
import com.codename1.ui.Label;
import com.codename1.ui.Slider;
import com.codename1.ui.SwipeableContainer;
import com.codename1.ui.Tabs;
import com.codename1.ui.TextArea;
import com.codename1.ui.TextField;
import com.codename1.ui.URLImage;
import com.codename1.ui.animations.CommonTransitions;
import com.codename1.ui.events.ActionEvent;
import com.codename1.ui.events.ActionListener;
import com.codename1.ui.geom.Dimension;
import com.codename1.ui.layouts.BorderLayout;
import com.codename1.ui.layouts.BoxLayout;
import com.codename1.ui.layouts.FlowLayout;
import com.codename1.ui.layouts.GridLayout;
import com.codename1.ui.layouts.LayeredLayout;
import com.codename1.ui.layouts.Layout;
import com.codename1.ui.plaf.Border;
import com.codename1.ui.plaf.Style;
import com.codename1.ui.spinner.Picker;
import com.codename1.ui.table.DefaultTableModel;
import com.codename1.ui.table.Table;
import com.codename1.ui.table.TableLayout;
import com.codename1.ui.table.TableModel;
import com.codename1.ui.util.Resources;
import entities.Commande;
import entities.Evenement;
import entities.Participation;
import entities.Produit;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import rest.file.uploader.tn.FileUploader;
import services.FacebookShare;
import services.ServiceEvent;
import services.ServicePanier;
import services.ServiceProduit;
import services.TwilioSms;


/**
 *
 * @author Dell
 */
public class ListPanierUser extends Demo {

    Form f, h, k, monpanierForm;
    SpanLabel lb;
    Button btnaff, panierbtn, supp, confirm, inscrir;
    int idClient;
    String nomClient;
    ArrayList<Produit> Panier = new ArrayList<>();
    //////////////////////////////////////////
    Resources res;
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
        return "Panier";
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
        
            FileUploader fc = new FileUploader("localhost/medina/web/");
    
            monpanierForm = new Form("Votre Panier", BoxLayout.y());
        Container cc = new Container(BoxLayout.y());
        Label l1=new Label("Non d Utilisateur");
 TextField t1=new TextField("","username");
 
  Label l2=new Label("Adresse Mail");
   TextField t2=new TextField("", "Email");

  
 Label l3=new Label("Mot de Passe");
  TextField t3=new TextField("", "Mot de passe");
  TextField m=new TextField("", "image");
  
Button imgBtn = new Button("Parcourir");
     imgBtn.addActionListener(e->{
                 Display.getInstance().openGallery(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent ev) {
                    if (ev != null && ev.getSource() != null) {
                      String  pathimg = (String) ev.getSource();
                        System.out.println(pathimg.substring(7));
                        m.setText(pathimg.substring(7));//image heya just label nsob feha fel path
                        try {
                        Image img = Image.createImage(FileSystemStorage.getInstance().openInputStream(pathimg));
                            System.out.println(img);
                        } catch (IOException e) {
                            System.out.println(e.getMessage());
                        }
                    }
                }
            }, Display.GALLERY_IMAGE);
     
     
     
     });
     
     
     
     
     
     
     
     
Button b=new Button("Inscrire");

k.add(l1);
k.add(t1);
k.add(l2);
k.add(t2);
k.add(l3);
k.add(t3);
k.add(imgBtn);
k.add(m);
k.add(b);
k.getToolbar().addCommandToLeftBar("retour", null, n->{f.show();});
 
b.addActionListener(s->{  
    
        ServiceUser serviceUser = new ServiceUser();
        User1 u=new User1();
                
        u.setUsername(t1.getText());
        u.setEmail(t2.getText());
        u.setPasswaord(t3.getText());
        u.setPhoto(m.getText());
            serviceUser.ajoutUser(u);
           
            });
                
        return cc;

    }

    ///////////////////////////////////////////
    public ListPanierUser() {
        f = new Form("SOUK EL MADINA", BoxLayout.y());
        k = new Form("Inscrir", BoxLayout.y());

        h = new Form("Nos Produit", BoxLayout.y());
        monpanierForm = new Form("Votre Panier", BoxLayout.y());
        monpanierForm.getToolbar().addCommandToLeftBar("Retour", null, (c) -> {
            h.show();
        });
        lb = new SpanLabel("zzz");

        btnaff = new Button("Aficher Produits");
        Button btnuser = new Button("inscire");

        btnuser.addActionListener(e -> {
            Label l1 = new Label("Non d Utilisateur");
            TextField t1 = new TextField("", "username");

            Label l2 = new Label("Adresse Mail");
            TextField t2 = new TextField("", "Email");

            Label l3 = new Label("Mot de Passe");
            TextField t3 = new TextField("", "Mot de passe");

            TextField m = new TextField("image");
            Button b = new Button("Inscrire");

            k.add(l1);
            k.add(t1);
            k.add(l2);
            k.add(t2);
            k.add(l3);
            k.add(t3);
            k.add(m);
            k.add(b);
            //     k.getToolbar().addCommandToLeftBar("retour", null, n -> {
            //       f.show();
            // });

            b.addActionListener(s -> {

                ServiceUser serviceUser = new ServiceUser();
                User1 u = new User1();
                u.setUsername(t1.getText());
                u.setEmail(t2.getText());
                u.setPasswaord(t3.getText());
                u.setPhoto("aaaaaaaaaa");
                serviceUser.ajoutUser(u);

            });
            k.show();
        });

        confirm = new Button("Valider");
        f.add(btnuser);
        f.add(btnaff);
        f.show();

        panierbtn = new Button("Afficher Mon Panier");

        btnaff.addActionListener((e) -> {
            ServicePanier SP = new ServicePanier();
            ArrayList<Produit> lis = SP.getList2();
            for (Produit s : lis) {
                h.add(addItem(s));
            }
            h.add(panierbtn);
            h.show();
        });

        panierbtn.addActionListener((h) -> {
            monpanierForm.removeAll();

            for (Produit p : Panier) {
                monpanierForm.add(AffPanier(p));
            }
            monpanierForm.add(confirm);

            monpanierForm.show();
        });

        confirm.addActionListener((u) -> {
            ServicePanier servicePanier = new ServicePanier();
            Commande commande = new Commande(1, "Fares Maghzaoui");

            servicePanier.ajoutCommande(commande);

            for (Produit p : Panier) {
                servicePanier.ajoutProduitCommande(p);

            }
            Panier.removeAll(Panier);

            monpanierForm.removeAll();
            Message m = new Message("Body of message");
            m.getAttachments().put("textAttachmentUri", "text/plain");
            m.getAttachments().put("imageAttachmentUri", "image/png");
            Display.getInstance().sendMessage(new String[]{"fares.maghzaoui@esprit.tn"}, "APi", m);

        });
    }

    public Container AffPanier(Produit P) {
        Container c = new Container(BoxLayout.y());

        supp = new Button("Supprimer");
        //Label l = new Label("id : "+String.valueOf(P.getIdProduit()));
        Label l1 = new Label("Nom Produit : " + P.getNomP());
        Label l2 = new Label("Description Produit : " + P.getDescrip());
        Label l3 = new Label("Prix Produit" + String.valueOf(P.getPrix()));
        Image placeholder = Image.createImage(380, 300);
        EncodedImage encoded = EncodedImage.createFromImage(placeholder, false);
        System.out.println(P.getImg());

        URLImage img = URLImage.createToStorage(encoded, P.getImg(), "http://localhost/medina/web/EventsImages/"
                + P.getImg());
        ImageViewer imv = new ImageViewer();
        imv.setImage(img);

        //   c.add(l);
        c.add(l1);
        c.add(l2);
        c.add(l3);
        c.add(imv);
        c.add(supp);
        supp.addActionListener((v) -> {
            monpanierForm.removeAll();
            Panier.remove(P);
            System.out.println(Panier);
            h.show();
        });

        return c;

    }

    public Container addItem(Produit P) {
        // Form f = new Form("Hi World");

        Container c1 = new Container(BoxLayout.y());
        //Label l = new Label("id : "+String.valueOf(P.getIdProduit()));
        Label l1 = new Label("Nom Produit : " + P.getNomP());
        Label l2 = new Label("Type : " + P.getDescrip());
        Label l3 = new Label("Prix : " + String.valueOf(P.getPrix()));
        Button AjPanier = new Button("Ajouter au Panier");
        Image placeholder = Image.createImage(380, 300);
        EncodedImage encoded = EncodedImage.createFromImage(placeholder, false);
        System.out.println(P.getImg());

        URLImage img = URLImage.createToStorage(encoded, P.getImg(), "http://localhost/medina/web/EventsImages/"
                + P.getImg());
        ImageViewer imv = new ImageViewer();
        imv.setImage(img);

        AjPanier.addActionListener((e) -> {
            Panier.add(P);
            System.out.println(Panier);
        });
        c1.add(l1);
        c1.add(l2);
        c1.add(l3);
        c1.add(imv);
        c1.add(AjPanier);

        Container c2 = new Container(BoxLayout.x());

        c2.add(c1);

        return c2;
    }

    public Container AddUser() {

        Container c = new Container();
        c.add(k);

        return c;
    }

    public Form getK() {
        return k;
    }

    public void setK(Form k) {
        this.k = k;
    }

    public Form getF() {
        return f;
    }

    public void setF(Form f) {
        this.f = f;
    }

    public SpanLabel getLb() {
        return lb;
    }

    public void setLb(SpanLabel lb) {
        this.lb = lb;
    }

}
