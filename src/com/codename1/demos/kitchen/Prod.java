/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.codename1.demos.kitchen;

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
import com.codename1.ui.events.ActionEvent;
import com.codename1.ui.events.ActionListener;
import com.codename1.ui.geom.Dimension;
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
import com.stripe.exception.APIConnectionException;
import com.stripe.exception.APIException;
import com.stripe.exception.AuthenticationException;
import com.stripe.exception.CardException;
import com.stripe.exception.InvalidRequestException;
import entities.Commande;
import entities.Evenement;
import entities.Participation;
import entities.Produit;
import entities.TwilioSmsAmine;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import rest.file.uploader.tn.FileUploader;
import services.FacebookShare;
import services.ServiceEvent;
import services.ServicePaiement;
import services.ServicePanier;
import services.ServiceProduit;
import services.TwilioSms;

/**
 *
 * @author CHAIMA
 */
public class Prod extends DemoProd {

    public Resources res;
    public EncodedImage enc = EncodedImage.createFromImage(Image.createImage(300, 300, 0xffff0000), true);
    public EncodedImage enc1 = EncodedImage.createFromImage(Image.createImage(300, 300, 0xffff0000), true);
    public Image img;
    public ImageViewer imgv;
    public String pathimg;
    ArrayList<Produit> Panier = new ArrayList<>();
    Form f, h, k, monpanierForm;
    SpanLabel lb;
    Button btnaff, panierbtn, supp, confirm, inscrir;
    int idClient;
    String nomClient;

    private static final String[] DEFAULT_COLUMNS = {
        "Nom bon plan"
    };
    private static final Object[][] DEFAULT_DATA = {
        {"Products", new Double(500000)},
        {"Virtual", new Double(300000)},
        {"Services", new Double(200000)}
    };
    private static final String[] DEFAULT_COLUMNS_LINE = {
        "Year", "Sales"
    };
    private static final Object[][] DEFAULT_DATA_LINE = {
        {new Double(2011), new Double(2500000)},
        {new Double(2012), new Double(2800000)},
        {new Double(2013), new Double(3000000)},
        {new Double(2014), new Double(2900000)},
        {new Double(2015), new Double(3300000)},
        {new Double(2016), new Double(3600000)}
    };
    private static final int[] COLORS = {0x9688, 0x3f51b5, 0x9c27b0};

    public Prod(Produit bp) {
        super(bp);
    }

    boolean inUpdate;
    ///////////////////////////////////////////////////////////////////////////////////////////////

    public Container AffPanier(Produit P) {
        Container c = new Container(BoxLayout.y());
         // monpanierForm.getToolbar().addCommandToLeftBar("Retour",null,(c)->{
   // h.show();
    //});
    
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
            Dialog.show("Succes","Produit supprimé du panier","ok",null);
            AffPanier(P);
            //h.show();
           
        });

        return c;

    }
    

    @Override
    public Container createDemoProd(Produit bp) {
        Table data = createTable(DEFAULT_COLUMNS, DEFAULT_DATA);
        TableModel tm = data.getModel();
        CategorySeries series = new CategorySeries("Sales");
        updateAxis(tm, 1, series, bp);
        ChartComponent pieChart = new ChartComponent(new PieChart(series, createChartRenderer(tm)));
        pieChart.setZoomEnabled(true);

        int deviceWidth = Display.getInstance().getDisplayWidth();
        System.out.println("\nDevice width: " + deviceWidth);

        EncodedImage enc2 = EncodedImage.createFromImage(Image.createImage(deviceWidth, deviceWidth, 0xffff0000), true);
        String path = "http://localhost/medina/web/EventsImages/" + bp.getImg();

        Image imgbp = URLImage.createToStorage(enc2, "big" + bp.getImg(),
                path, URLImage.RESIZE_SCALE_TO_FILL);
        ImageViewer imgv1 = new ImageViewer(imgbp);

        Table lineTable = createTable(DEFAULT_COLUMNS_LINE, DEFAULT_DATA_LINE);
        TableModel lineTm = lineTable.getModel();
        XYMultipleSeriesDataset multi = new XYMultipleSeriesDataset();
        XYSeries seriesXY = new XYSeries("Sales", 0);
        multi.addSeries(seriesXY);
        updateSeriesFromTable(lineTm, seriesXY);
        XYMultipleSeriesRenderer renderer = createChartMultiRenderer(lineTm);
        ChartComponent lineChart = new ChartComponent(new LineChart(multi, renderer));

        lineTm.addDataChangeListener((i, ii) -> {
            if (inUpdate) {
                return;
            }
            inUpdate = true;
            updateSeriesFromTable(lineTm, seriesXY);
            updateRendererMinMax(lineTm, renderer);
            lineChart.repaint();
            inUpdate = false;
        });

        Label nomuser1 = new Label("Cha");
        EncodedImage enc31 = EncodedImage.createFromImage(Image.createImage(50, 50, 0xffff0000), true);
        String path31 = "http://localhost/medina/web/images/back.png";

        SpanLabel desc = new SpanLabel("Description: " + bp.getDescrip());
        desc.setTextBlockAlign(CENTER);

        SpanLabel lieub = new SpanLabel("Quantité : " + bp.getQuantit());
        lieub.setTextBlockAlign(CENTER);

//        SpanLabel telb = new SpanLabel(bp.getDate());
//        telb.setTextBlockAlign(CENTER);
        SpanLabel prix = new SpanLabel("Prix :" + bp.getPrix());
        prix.setTextBlockAlign(CENTER);

        Button panier = new Button("Ajouter au panier");
        panier.addActionListener(l -> {
            Form test = new Form(BoxLayout.y());
            Panier.add(bp);
            Container c = new Container(BoxLayout.y());

            Button supp = new Button("Supprimer");
            //Label l = new Label("id : "+String.valueOf(P.getIdProduit()));
            Label l1 = new Label("Nom Produit : " + bp.getNomP());
            Label l2 = new Label("Description Produit : " + bp.getDescrip());
            Label l3 = new Label("Prix Produit" + String.valueOf(bp.getPrix()));
            Image placeholder = Image.createImage(380, 300);
            EncodedImage encoded = EncodedImage.createFromImage(placeholder, false);
            System.out.println(bp.getImg());

            URLImage img = URLImage.createToStorage(encoded, bp.getImg(), "http://localhost/medina/web/EventsImages/"
                    + bp.getImg());
            ImageViewer imv = new ImageViewer();
            imv.setImage(img);

            //   c.add(l);
            c.add(l1);
            c.add(l2);
            c.add(l3);
            c.add(imv);
            c.add(supp);
            supp.addActionListener((v) -> {
                c.removeAll();
                Panier.remove(bp);
                System.out.println(Panier);
            });

            System.out.println(Panier);
            //test.add(c);
            //test.show();

        });

        Button afpanier = new Button("Afficher panier");
        afpanier.addActionListener((evt) -> {
            monpanierForm = new Form(BoxLayout.y());
            Button retour = new Button("Retour");
            monpanierForm.add(retour);
          
            confirm = new Button("Valider");
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
                
                 ServicePaiement spr= new ServicePaiement();
                try {
                    spr.payer("4242424242424242", 5, 19, "321", 100 , "payment valide");
                } catch (AuthenticationException ex) {
                } catch (InvalidRequestException ex) {
                } catch (APIConnectionException ex) {
                } catch (CardException ex) {
                } catch (APIException ex) {
                }

            });

            for (Produit p : Panier) {
                monpanierForm.add(AffPanier(p));
            }
            monpanierForm.add(confirm);

            monpanierForm.show();

        });

        Container cnt = BoxLayout.encloseY(ComponentGroup.enclose(desc, lieub, prix));
        cnt.add(panier);
        cnt.add(afpanier);

        cnt.setScrollableY(true);

        Tabs sales = new Tabs();
        sales.addTab("Infos", FontImage.MATERIAL_INFO, 4, encloseInMaximizableGrid(imgv1, cnt));
//        sales.addTab("Localisation", FontImage.MATERIAL_MAP, 4, encloseInMaximizableGrid(lineChart, lineTable));
        return sales;
    }

    @Override
    public Container createDemoProdMine(Produit bp) {
        Table data = createTable(DEFAULT_COLUMNS, DEFAULT_DATA);
        TableModel tm = data.getModel();
        CategorySeries series = new CategorySeries("Sales");
        updateAxis(tm, 1, series, bp);
        ChartComponent pieChart = new ChartComponent(new PieChart(series, createChartRenderer(tm)));
        pieChart.setZoomEnabled(true);

        int deviceWidth = Display.getInstance().getDisplayWidth();
        System.out.println("\nDevice width: " + deviceWidth);

        EncodedImage enc2 = EncodedImage.createFromImage(Image.createImage(deviceWidth, deviceWidth, 0xffff0000), true);
        String path = "http://localhost/medina/web/EventsImages/" + bp.getImg();

        Image imgbp = URLImage.createToStorage(enc2, "big" + bp.getImg(),
                path, URLImage.RESIZE_SCALE_TO_FILL);
        ImageViewer imgv1 = new ImageViewer(imgbp);

        Table lineTable = createTable(DEFAULT_COLUMNS_LINE, DEFAULT_DATA_LINE);
        TableModel lineTm = lineTable.getModel();
        XYMultipleSeriesDataset multi = new XYMultipleSeriesDataset();
        XYSeries seriesXY = new XYSeries("Sales", 0);
        multi.addSeries(seriesXY);
        updateSeriesFromTable(lineTm, seriesXY);
        XYMultipleSeriesRenderer renderer = createChartMultiRenderer(lineTm);
        ChartComponent lineChart = new ChartComponent(new LineChart(multi, renderer));

        lineTm.addDataChangeListener((i, ii) -> {
            if (inUpdate) {
                return;
            }
            inUpdate = true;
            updateSeriesFromTable(lineTm, seriesXY);
            updateRendererMinMax(lineTm, renderer);
            lineChart.repaint();
            inUpdate = false;
        });

        Label nomuser1 = new Label("me");
        EncodedImage enc31 = EncodedImage.createFromImage(Image.createImage(50, 50, 0xffff0000), true);
        String path31 = "http://localhost/medina/web/images/back.png";

        SpanLabel descD = new SpanLabel("Description: " + bp.getDescrip());
        descD.setTextBlockAlign(CENTER);

        SpanLabel qttD = new SpanLabel("Quantit: " + bp.getQuantit());
        qttD.setTextBlockAlign(CENTER);

        SpanLabel prixD = new SpanLabel("Prix: " + bp.getPrix());
        prixD.setTextBlockAlign(CENTER);

        Container cnt = BoxLayout.encloseY(ComponentGroup.enclose(descD, qttD, prixD));

        TextField nom;
        TextField qtt;
        TextArea description;
        TextField prix;

        nom = new TextField();
        nom.setHint("Nom du produit");
        TextField image;
        image = new TextField();
        image.setHint("Chemin de l image");

        qtt = new TextField();
        qtt.setHint("Quantité");

        description = new TextArea();
        description.setHint("Description");

        prix = new TextField();
        prix.setHint("Prix");

        Button imgBtn;

        imgBtn = new Button("Parcourir");
        imgBtn.addActionListener(e -> {
            Display.getInstance().openGallery(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent ev) {
                    if (ev != null && ev.getSource() != null) {
                        pathimg = (String) ev.getSource();
                        System.out.println(pathimg.substring(7));
                        Image img = null;
                        image.setText(pathimg.substring(7));//image heya just label nsob feha fel path
                        try {
                            img = Image.createImage(FileSystemStorage.getInstance().openInputStream(pathimg));
                            System.out.println(img);
                        } catch (IOException e) {
                            System.out.println(e.getMessage());
                        }
                    }
                }
            }, Display.GALLERY_IMAGE);
            
            
           
            
            
            
        });

        Button b1 = new Button("Ajouter");

        Button b3 = new Button("Supprimer");
        cnt.add(b3);

        cnt.setScrollableY(true);
        b3.addActionListener(e -> {
            ServiceProduit sb = new ServiceProduit();
            sb.supProduit(bp);
            createDemoProd(bp);
        });
        cnt.refreshTheme(true);

        Container c1 = new Container();
        c1.setScrollableY(true);
        c1.add(nom);
        c1.add(description);
        c1.add(qtt);
        c1.add(prix);
        c1.add(imgBtn);
        c1.add(b1);

        b1.addActionListener(e -> {
            FileUploader fc = new FileUploader("localhost/medina/web/");

            try {
                String f = fc.upload(image.getText());

                ServiceProduit sb = new ServiceProduit();
                Produit b = new Produit(nom.getText(),
                        Float.valueOf(prix.getText()),
                        f,
                        Integer.parseInt(qtt.getText()),
                        description.getText()
                );             
                
                sb.ajouterProduit(b);
                TwilioSmsAmine sms = new TwilioSmsAmine();
                sms.sendSms("Votre produit est ajouter");
                nom.setText("");
                description.setText("");
                qtt.setText("");
                prix.setText("");

            } catch (Exception ex) {

            }
            //cnt.refreshTheme(true);
        });
        //cnt.refreshTheme(true);
        Button b2 = new Button("Modifier");
        Container c2 = new Container();

        TextField nomM = new TextField();
        nomM.setText(bp.getNomP());

        TextArea descriM = new TextArea();
        descriM.setText(bp.getDescrip());

        TextField qttM = new TextField();
        qttM.setText("" + bp.getQuantit());

        TextField prixM = new TextField();
        prixM.setText("" + bp.getPrix());

        c2.add(nomM);
        c2.add(descriM);
        c2.add(qttM);
        c2.add(prixM);
        c2.add(b2);

        b2.addActionListener(e -> {
            ServiceProduit sev = new ServiceProduit();
            Produit b = new Produit(bp.getRef(),
                    nomM.getText(),
                    Float.valueOf(prixM.getText()),
                    Integer.parseInt(qttM.getText()),
                    descriM.getText());
            sev.modierProduit(b);
        });

        Tabs sales = new Tabs();
        sales.addTab("Infos", FontImage.MATERIAL_INFO, 4, encloseInMaximizableGrid(imgv1, cnt));
        sales.addTab("Modifier", FontImage.MATERIAL_UPDATE, 4, encloseInMaximize(cnt, c2));
        sales.addTab("Ajouter", FontImage.MATERIAL_ADD, 4, encloseInMaximize(cnt, c1));

//        sales.addTab("Localisation", FontImage.MATERIAL_MAP, 4, encloseInMaximizableGrid(lineChart, lineTable));
        return sales;
    }

    private void updateAxis(TableModel tm, int col, CategorySeries cs, Produit bp) {

        cs.clear();
        for (int iter = 0; iter < tm.getRowCount(); iter++) {
            Object tt = tm.getValueAt(iter, 1);
            double v;
            if (tt instanceof Double) {
                v = ((Double) tt).doubleValue();
            } else {
                v = L10NManager.getInstance().parseCurrency((String) tt);
                tm.setValueAt(iter, col, new Double(v));
            }
            cs.add((String) tm.getValueAt(iter, 0), v);
        }
    }

    private void updateSeriesFromTable(TableModel tm, XYSeries cs) {
        cs.clear();
        for (int iter = 0; iter < tm.getRowCount(); iter++) {
            Object tt = tm.getValueAt(iter, 1);
            double v;
            if (tt instanceof Double) {
                v = ((Double) tt).doubleValue();
            } else {
                v = L10NManager.getInstance().parseCurrency((String) tt);
                tm.setValueAt(iter, 1, new Double(v));
            }
            double x = asDouble(tm, iter, 0);
            cs.add(x, v);
        }
    }

    private Container encloseInMaximize(Container parent, Component cmp) {
        Button max = new Button();
        max.setUIID("Label");
        FontImage.setMaterialIcon(max, FontImage.MATERIAL_FULLSCREEN);
        Container maximize = LayeredLayout.encloseIn(cmp, FlowLayout.encloseRight(max));
        Image originalIcon = max.getIcon();
        Layout original = parent.getLayout();

        max.addActionListener(e -> {
            // should maximize
            if (max.getIcon() == originalIcon) {
                parent.setLayout(BoxLayout.y());
                FontImage.setMaterialIcon(max, FontImage.MATERIAL_FULLSCREEN_EXIT);
                for (Component c : parent) {
                    if (c != maximize) {
                        c.setHidden(true);
                    }
                }
                parent.setShouldCalcPreferredSize(true);
                parent.animateLayoutAndWait(400);
            } else {
                parent.setLayout(original);
                max.setIcon(originalIcon);
                for (Component c : parent) {
                    if (c != maximize) {
                        c.setHidden(false);
                    }
                }
                parent.animateLayoutAndWait(400);
            }
        });
        return maximize;
    }

    private Container encloseInMaximizableGrid(Component cmp1, Component cmp2) {
        GridLayout gl = new GridLayout(2, 1);
        Container grid = new Container(gl);
        gl.setHideZeroSized(true);

        grid.add(encloseInMaximize(grid, cmp1)).
                add(encloseInMaximize(grid, cmp2));
        return grid;
    }

    private Table createTable(String[] columns, Object[][] tableData) {
        TableModel tm = new DefaultTableModel(columns, tableData) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return true;
            }
        };
        Table data = new Table(tm) {
            @Override
            protected Component createCell(Object value, int row, int column, boolean editable) {
                Component cmp = super.createCell(value, row, column, editable);;
                if (row > -1) {
                    if (row % 2 != 0) {
                        cmp.setUIID("TableCellOdd");
                    }
                    TextArea t = (TextArea) cmp;
                    if (column == 1) {
                        t.setConstraint(TextArea.DECIMAL);
                        t.setText(L10NManager.getInstance().formatCurrency(((Double) value).doubleValue()));
                        t.addActionListener(e -> {
                            tm.setValueAt(row, column, L10NManager.getInstance().parseCurrency(t.getText()));
                        });
                    } else {
                        t.addActionListener(e -> tm.setValueAt(row, column, t.getText()));
                    }
                    Container centeredContent = FlowLayout.encloseCenter(cmp);
                    centeredContent.setUIID(cmp.getUIID());
                    return centeredContent;
                }
                return cmp;
            }

            @Override
            protected TableLayout.Constraint createCellConstraint(Object value, int row, int column) {
                TableLayout.Constraint c = super.createCellConstraint(value, row, column);
                return c.widthPercentage(50);
            }
        };
        data.setDrawBorder(false);
        data.setScrollableY(true);
        return data;
    }

    private static int darkerColor(int color, int factor) {
        int r = color >> 16 & 0xff;
        int g = color >> 8 & 0xff;
        int b = color & 0xff;
        r = Math.max(0, r - factor);
        g = Math.max(0, g - factor);
        b = Math.max(0, b - factor);
        return ((r << 16) & 0xff0000) | ((g << 8) & 0xff00) | (b & 0xff);
    }

    private DefaultRenderer createChartRenderer(TableModel tm) {
        DefaultRenderer renderer = new DefaultRenderer();
        for (int color : COLORS) {
            SimpleSeriesRenderer r = new SimpleSeriesRenderer();
            r.setGradientEnabled(true);
            r.setGradientStart(0, color);
            int cc = darkerColor(color, -50);
            r.setGradientStop(0, cc);
            renderer.addSeriesRenderer(r);
        }
        renderer.setLabelsColor(0);
        renderer.setBackgroundColor(0xffffffff);
        renderer.setApplyBackgroundColor(true);
        renderer.setLabelsTextFont(Font.createTrueTypeFont("native:MainThin", "native:MainThin"));
//        renderer.setLabelsTextSize(convertToPixels(5));
        return renderer;
    }

    double asDouble(TableModel tm, int row, int col) {
        Object v = tm.getValueAt(row, col);
        if (v instanceof Double) {
            return ((Double) v).doubleValue();
        }
        if (col == 1) {
            return L10NManager.getInstance().parseCurrency(v.toString());
        }
        return Double.parseDouble(v.toString());
    }

    void updateRendererMinMax(TableModel tm, XYMultipleSeriesRenderer renderer) {
        double xmin = Double.MAX_VALUE;
        double xmax = Double.MIN_VALUE;
        double ymin = Double.MAX_VALUE;
        double ymax = Double.MIN_VALUE;
        for (int iter = 0; iter < tm.getRowCount(); iter++) {
            double currentX = asDouble(tm, iter, 0);
            double currentY = asDouble(tm, iter, 1);
            xmin = Math.min(xmin, currentX);
            xmax = Math.max(xmax, currentX);
            ymin = Math.min(ymin, currentY);
            ymax = Math.max(ymax, currentY);
        }
        renderer.setXAxisMin(xmin);
        renderer.setXAxisMax(xmax);
        renderer.setYAxisMin(ymin);
        renderer.setYAxisMax(ymax);
    }

    private XYMultipleSeriesRenderer createChartMultiRenderer(TableModel tm) {
        XYMultipleSeriesRenderer renderer = new XYMultipleSeriesRenderer();
        for (int color : COLORS) {
            XYSeriesRenderer r = new XYSeriesRenderer();
            r.setColor(color);
            renderer.addSeriesRenderer(r);
            r.setFillPoints(true);
            r.setLineWidth(5);
            r.setPointStyle(PointStyle.CIRCLE);
        }
        renderer.setPointSize(5f);
        renderer.setLabelsColor(0);
        renderer.setBackgroundColor(0xffffffff);
        renderer.setApplyBackgroundColor(true);
        renderer.setAxesColor(COLORS[0]);
        renderer.setLabelsTextFont(Font.createTrueTypeFont("native:MainThin", "native:MainThin"));
        renderer.setLabelsTextSize(Display.getInstance().convertToPixels(2));

        renderer.setXTitle(DEFAULT_COLUMNS_LINE[0]);
        renderer.setYTitle(DEFAULT_COLUMNS_LINE[1]);
        updateRendererMinMax(tm, renderer);
        renderer.setAxesColor(0xcccccc);
        renderer.setLabelsColor(0);
        renderer.setXLabels(5);
        renderer.setYLabels(5);
        renderer.setShowGrid(true);
        renderer.setXLabelsAlign(Component.RIGHT);
        renderer.setYLabelsAlign(Component.RIGHT);

        renderer.setMargins(new int[]{0, 0, 0, 0});
        renderer.setMarginsColor(0xffffff);
        return renderer;
    }

    @Override
    public String getDisplayName(Produit bp) {
        return bp.getNomP();

    }

    @Override
    public Image getDemoIcon(Produit bp) {
        String path = "http://localhost/medina/web/EventsImages/" + bp.getImg();

        img = URLImage.createToStorage(enc, "icon" + bp.getImg(),
                path, URLImage.RESIZE_SCALE);

        return img;

    }

    public SwipeableContainer createRankWidget(String title, String year) {
        MultiButton button = new MultiButton(title);
        button.setTextLine2(year);
        return new SwipeableContainer(FlowLayout.encloseCenterMiddle(createStarRankSlider()),
                button);
    }

    private void initStarRankStyle(Style s, Image star) {
        s.setBackgroundType(Style.BACKGROUND_IMAGE_TILE_BOTH);
        s.setBorder(Border.createEmpty());
        s.setBgImage(star);
        s.setBgTransparency(0);
    }

    private Slider createStarRankSlider() {
        Slider starRank = new Slider();
        starRank.setEditable(true);
        starRank.setMinValue(0);
        starRank.setMaxValue(5);
        Font fnt = Font.createTrueTypeFont("native:MainLight", "native:MainLight").
                derive(Display.getInstance().convertToPixels(8, true), Font.STYLE_PLAIN);
        Style s = new Style(0x663399, 0, fnt, (byte) 0);
        Image fullStar = FontImage.createMaterial(FontImage.MATERIAL_STAR, s).toImage();
        s.setOpacity(100);
        s.setFgColor(0);
        Image emptyStar = FontImage.createMaterial(FontImage.MATERIAL_STAR, s).toImage();
        initStarRankStyle(starRank.getSliderEmptySelectedStyle(), emptyStar);
        initStarRankStyle(starRank.getSliderEmptyUnselectedStyle(), emptyStar);
        initStarRankStyle(starRank.getSliderFullSelectedStyle(), fullStar);
        initStarRankStyle(starRank.getSliderFullUnselectedStyle(), fullStar);
        starRank.setPreferredSize(new Dimension(fullStar.getWidth(), fullStar.getHeight()));
        return starRank;
    }

    public void showStarPickingForm() {
        Prod bus = new Prod(new Produit());
        bus.showStarPickingForm();
    }

}
