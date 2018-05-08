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
import com.codename1.ui.Button;
//import static com.codename1.ui.CN.BOTTOM;
//import static com.codename1.ui.CN.CENTER;
//import static com.codename1.ui.CN.RIGHT;
//import static com.codename1.ui.CN.convertToPixels;
//import static com.codename1.ui.CN.execute;
//import static com.codename1.ui.CN.getPlatformName;
//import static com.codename1.ui.CN.isNativeShareSupported;
//import static com.codename1.ui.CN.isTablet;
//import static com.codename1.ui.CN.share;
import com.codename1.ui.Component;
import static com.codename1.ui.Component.CENTER;
import com.codename1.ui.ComponentGroup;
import com.codename1.ui.Container;
import com.codename1.ui.Dialog;
import com.codename1.ui.Display;
import com.codename1.ui.EncodedImage;
import com.codename1.ui.Font;
import com.codename1.ui.FontImage;
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
import entities.Evenement;
import entities.Participation;
import java.io.IOException;
import rest.file.uploader.tn.FileUploader;
import services.FacebookShare;
import services.ServiceEvent;
import services.TwilioSms;

/**
 *
 * @author Chaima
 */
public class Bus extends Demobus {

    public Resources res;
    public EncodedImage enc = EncodedImage.createFromImage(Image.createImage(300, 300, 0xffff0000), true);
    public EncodedImage enc1 = EncodedImage.createFromImage(Image.createImage(300, 300, 0xffff0000), true);
    public Image img;
    public ImageViewer imgv;
    public String pathimg;

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

    public Bus(Evenement bp) {
        super(bp);
    }

    @Override
    public String getDisplayName(Evenement bp) {
        return bp.getNom();
    }

    @Override
    public Image getDemoIcon(Evenement bp) {
        String path = "http://localhost/medina/web/EventsImages/" + bp.getImage();

        img = URLImage.createToStorage(enc, "icon" + bp.getImage(),
                path, URLImage.RESIZE_SCALE);

        return img;

    }

    boolean inUpdate;

    private void updateAxis(TableModel tm, int col, CategorySeries cs, Evenement bp) {

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

    ///////////////////////////////////////////////////////////////////////////////////////////////
    @Override
    public Container createDemoBus(Evenement bp) {
        Table data = createTable(DEFAULT_COLUMNS, DEFAULT_DATA);
        TableModel tm = data.getModel();
        CategorySeries series = new CategorySeries("Sales");
        updateAxis(tm, 1, series, bp);
        ChartComponent pieChart = new ChartComponent(new PieChart(series, createChartRenderer(tm)));
        pieChart.setZoomEnabled(true);

        int deviceWidth = Display.getInstance().getDisplayWidth();
        System.out.println("\nDevice width: " + deviceWidth);

        EncodedImage enc2 = EncodedImage.createFromImage(Image.createImage(deviceWidth, deviceWidth, 0xffff0000), true);
        String path = "http://localhost/medina/web/EventsImages/" + bp.getImage();

        Image imgbp = URLImage.createToStorage(enc2, "big" + bp.getImage(),
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

        Button btnParticip = new Button("Participer");
        //btnParticip.setTextBlockAlign(CENTER);
        btnParticip.addActionListener((evt) -> {
            System.out.println("Share");
            ServiceEvent serv = new ServiceEvent();
            ServiceEvent part = new ServiceEvent();
            Participation p = new Participation(
                    4, bp.getId_event()
            );
            part.participer(bp);
            System.out.println("part");
            TwilioSms sms = new TwilioSms();
            sms.sendSms("Vous venez de participer à l'evenement " + bp.getNom());
        });
        btnParticip.setEnabled(true);

        Button partager = new Button("Partager sur Facebook");
        partager.addActionListener((evt) -> {
            FacebookShare fb = new FacebookShare("EAACEdEose0cBAEX0XgZCd8SVh8QpdE6lVZBBa2E7oTyJxZBiZCcVMPwTcZCnB0ZAtIo0WLIK44pZBqo4ZBasc0n0PCAyMjcgZAIwhFX1ivnRyt0ZAYKGo13R8FfKEXP0f6Yh22eqcJ9tMZBjVPNH5hrlvP5UOpxtKMm9PCJjXfExtnykLfEmy2OH8NWnsc1OdCWgrP6me4BQHoKPgZDZD");
            try {
                fb.share("Je partage avec vous l'evenement " + bp.getNom()
                        + " \n qui aura lieu à " + bp.getLieu()
                        + " le " + bp.getDate()
                        + "\n Seulement à " + bp.getPrix() + " dt"
                        + "\n Ne ratez pas cette occasion !");
                Dialog.show("Succés", "Cet évènement est partagé sur votre mur !", "ok", null);
            } catch (IOException ex) {
            }
        });

        SpanLabel desc = new SpanLabel("Description: " + bp.getDescription());
        desc.setTextBlockAlign(CENTER);

        SpanLabel lieub = new SpanLabel("Lieu: " + bp.getLieu());
        lieub.setTextBlockAlign(CENTER);

        SpanLabel telb = new SpanLabel(bp.getDate());
        telb.setTextBlockAlign(CENTER);

        SpanLabel prix = new SpanLabel("Prix :" + bp.getPrix());
        prix.setTextBlockAlign(CENTER);

        SpanLabel ticket = new SpanLabel("Nombre des tickets" + bp.getTicket());
        ticket.setTextBlockAlign(CENTER);
        Ads ad = new Ads("ca-app-pub-3940256099942544/6300978111");
        System.out.println(ad);
        Button ads = new Button("add");
        ads.addActionListener((evt) -> {
            // Ads ad = new Ads("ca-app-pub-3940256099942544/6300978111"); 
        });
        
         Button fav = new Button("Favoris");
        Database db;
        try {
            db = Database.openOrCreate("revv.db");
            db.execute("create table if not exists Favoris (id INTEGER PRIMARY KEY,nom TEXT,description TEXT,image TEXT);\"");
           
            fav.addActionListener((favv) -> {
                try {
                    db.execute("insert into Favoris (nom,description,image) values ('"
                            + bp.getNom() + "', '"
                            + bp.getDescription() + "', '"
                            + bp.getImage() + "');");
                } catch (IOException ex) {ex.printStackTrace(); }
                System.out.println("element inser");
                Dialog.show("Succés", "Cet évènement est dans votre favoris", "ok", null);
                try {
                    Cursor cur = db.executeQuery("select distinct * from Favoris");
                     afficherFavEvent afc = new afficherFavEvent(Resources.getGlobalResources(), cur);
                     afc.show();
                } catch (IOException ex) { ex.printStackTrace();  }

            });
        } catch (IOException ex) { ex.printStackTrace();}

        Container cnt = BoxLayout.encloseY(ComponentGroup.enclose(desc, lieub, telb, prix, ticket));
        cnt.add(ad);
        cnt.add(fav);
        cnt.add(btnParticip);
        cnt.add(partager);
        cnt.setScrollableY(true);

        Tabs sales = new Tabs();
        sales.addTab("Infos", FontImage.MATERIAL_INFO, 4, encloseInMaximizableGrid(imgv1, cnt));
//        sales.addTab("Localisation", FontImage.MATERIAL_MAP, 4, encloseInMaximizableGrid(lineChart, lineTable));
        return sales;
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////
    @Override
    public Container createDemoMine(Evenement bp) {
        Table data = createTable(DEFAULT_COLUMNS, DEFAULT_DATA);
        TableModel tm = data.getModel();
        CategorySeries series = new CategorySeries("Sales");
        updateAxis(tm, 1, series, bp);
        ChartComponent pieChart = new ChartComponent(new PieChart(series, createChartRenderer(tm)));
        pieChart.setZoomEnabled(true);

        int deviceWidth = Display.getInstance().getDisplayWidth();
        System.out.println("\nDevice width: " + deviceWidth);

        EncodedImage enc2 = EncodedImage.createFromImage(Image.createImage(deviceWidth, deviceWidth, 0xffff0000), true);
        String path = "http://localhost/medina/web/EventsImages/" + bp.getImage();

        Image imgbp = URLImage.createToStorage(enc2, "big" + bp.getImage(),
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

        Label nomuser1 = new Label("Chaa");
        EncodedImage enc31 = EncodedImage.createFromImage(Image.createImage(50, 50, 0xffff0000), true);
        String path31 = "http://localhost/medina/web/images/back.png";

        SpanLabel descD = new SpanLabel("Description: " + bp.getDescription());
        descD.setTextBlockAlign(CENTER);

        SpanLabel lieuD = new SpanLabel("Lieu: " + bp.getLieu());
        lieuD.setTextBlockAlign(CENTER);

        SpanLabel prixD = new SpanLabel("Prix: " + bp.getPrix());
        prixD.setTextBlockAlign(CENTER);

        SpanLabel ticketD = new SpanLabel("Nombre des tickets: " + bp.getTicket());
        ticketD.setTextBlockAlign(CENTER);

        SpanLabel dateD = new SpanLabel(bp.getDate());
        dateD.setTextBlockAlign(CENTER);

        Container cnt = BoxLayout.encloseY(ComponentGroup.enclose(dateD, descD, lieuD, prixD, ticketD));

        TextField nom;
        TextField lieu;
        TextField description;
        TextField prix;
        TextField ticket;

        Picker datePicker = new Picker();

        nom = new TextField();
        nom.setHint("Nom de l'evenement");
        TextField image;
        image = new TextField();
        image.setHint("Chemin de l image");

        lieu = new TextField();
        lieu.setHint("Lieu");

        description = new TextField();
        description.setHint("Description");

        prix = new TextField();
        prix.setHint("Prix");

        ticket = new TextField();
        ticket.setHint("Nombre des tickets");

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

        Button partager = new Button("Partager sur Facebook");
//        partager.setDisabledIcon(res.getImage("fb.png"));
        partager.addActionListener((evt) -> {
            FacebookShare fb = new FacebookShare("EAACEdEose0cBAEX0XgZCd8SVh8QpdE6lVZBBa2E7oTyJxZBiZCcVMPwTcZCnB0ZAtIo0WLIK44pZBqo4ZBasc0n0PCAyMjcgZAIwhFX1ivnRyt0ZAYKGo13R8FfKEXP0f6Yh22eqcJ9tMZBjVPNH5hrlvP5UOpxtKMm9PCJjXfExtnykLfEmy2OH8NWnsc1OdCWgrP6me4BQHoKPgZDZD");
            try {
                fb.share("Je partage avec vous l'evenement " + bp.getNom()
                        + " \n qui aura lieu à " + bp.getLieu()
                        + " le " + bp.getDate()
                        + "\n Seulement à " + bp.getPrix() + " dt"
                        + "\n Ne ratez pas cette occasion !");
                Dialog.show("Succés", "Cet évènement est partagé sur votre mur !", "ok", null);
            } catch (IOException ex) {
            }
        });

        Button b3 = new Button("Supprimer");
        cnt.add(b3);
        cnt.add(partager);
        cnt.setScrollableY(true);
        b3.addActionListener(e -> {
            ServiceEvent sb = new ServiceEvent();
            sb.supprimerEvent(bp);
            createDemoBus(bp);
        });
        cnt.refreshTheme(true);

        Container c1 = new Container();
        c1.setScrollableY(true);
        Ads ad = new Ads("ca-app-pub-3940256099942544/6300978111");
        c1.add(ad);
        c1.add(nom);
        c1.add(datePicker);
        c1.add(description);
        c1.add(lieu);
        c1.add(prix);
        c1.add(ticket);
        c1.add(imgBtn);
        c1.add(b1);

        b1.addActionListener(e -> {
            FileUploader fc = new FileUploader("localhost/medina/web/");

            try {
                String f = fc.upload(image.getText());

                System.out.println(f);
                SimpleDateFormat formater = new SimpleDateFormat("dd-MM-yyyy");
                String date1 = formater.format(datePicker.getDate());
                System.out.println(date1);

                ServiceEvent sb = new ServiceEvent();
                Evenement b = new Evenement(nom.getText(),
                        date1,
                        description.getText(),
                        f,
                        lieu.getText(),
                        prix.getText(),
                        Integer.parseInt(ticket.getText()));
                sb.ajoutEvent(b);
                //TwilioSms sms = new TwilioSms();
                //sms.sendSms("Votre évènement "+bp.getNom()+" est ajouté avec succés");
                nom.setText("");
                description.setText("");
                lieu.setText("");
                prix.setText("");
                ticket.setText("");
                createDemoBus(bp);

            } catch (Exception ex) {

            }
            //cnt.refreshTheme(true);
        });
        //cnt.refreshTheme(true);
        Button b2 = new Button("Modifier");
        Container c2 = new Container();

        TextField nomM = new TextField();
        nomM.setText(bp.getNom());

        TextField dateM = new TextField();
        dateM.setText(bp.getDate());

        TextField descriM = new TextField();
        descriM.setText(bp.getDescription());

        TextField lieuM = new TextField();
        lieuM.setText(bp.getLieu());

        TextField prixM = new TextField();
        lieuM.setText(bp.getPrix());

        TextField ticketM = new TextField();
        ticketM.setText("" + bp.getTicket());

        c2.add(nomM);
        c2.add(dateM);
        c2.add(descriM);
        c2.add(lieuM);
        c2.add(prixM);
        c2.add(ticketM);
        c2.add(b2);

        b2.addActionListener(e -> {
            ServiceEvent sev = new ServiceEvent();
            Evenement b = new Evenement(bp.getId_event(),
                    nomM.getText(),
                    dateM.getText(),
                    descriM.getText(),
                    lieuM.getText(),
                    prixM.getText(),
                    Integer.parseInt(ticketM.getText()));
            sev.modifierEvent(b);
        });

        Tabs sales = new Tabs();
        sales.addTab("Infos", FontImage.MATERIAL_INFO, 4, encloseInMaximizableGrid(imgv1, cnt));
        sales.addTab("Modifier", FontImage.MATERIAL_UPDATE, 4, encloseInMaximize(cnt, c2));
        sales.addTab("Ajouter", FontImage.MATERIAL_ADD, 4, encloseInMaximize(cnt, c1));

//        sales.addTab("Localisation", FontImage.MATERIAL_MAP, 4, encloseInMaximizableGrid(lineChart, lineTable));
        return sales;
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////

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
        Bus bus = new Bus(new Evenement());
        bus.showStarPickingForm();
    }

}
