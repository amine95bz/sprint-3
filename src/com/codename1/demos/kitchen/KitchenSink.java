/*
 * Copyright (c) 2012, Codename One and/or its affiliates. All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.  Codename One designates this
 * particular file as subject to the "Classpath" exception as provided
 *  in the LICENSE file that accompanied this code.
 *  
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 2 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 * 
 * You should have received a copy of the GNU General Public License version
 * 2 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 * 
 * Please contact Codename One through http://www.codenameone.com/ if you 
 * need additional information or have any questions.
 */
package com.codename1.demos.kitchen;

import com.codename1.capture.Capture;
import com.codename1.components.ImageViewer;
import com.codename1.components.OnOffSwitch;
import com.codename1.components.ScaleImageLabel;
import com.codename1.components.SpanLabel;
import com.codename1.components.ToastBar;
import com.codename1.io.CharArrayReader;
import com.codename1.io.ConnectionRequest;
import com.codename1.io.JSONParser;
import com.codename1.io.Log;
import com.codename1.io.NetworkEvent;
import com.codename1.io.NetworkManager;
import com.codename1.io.Preferences;
import com.codename1.ui.Button;
import com.codename1.ui.Command;
import com.codename1.ui.Component;
import com.codename1.ui.ComponentGroup;
import com.codename1.ui.Container;
import com.codename1.ui.Dialog;
import com.codename1.ui.Display;
import com.codename1.ui.EncodedImage;
import com.codename1.ui.FontImage;
import com.codename1.ui.Form;
import com.codename1.ui.Graphics;
import com.codename1.ui.Image;
import com.codename1.ui.Label;
import com.codename1.ui.TextField;
import com.codename1.ui.Toolbar;
import com.codename1.ui.animations.CommonTransitions;
import com.codename1.ui.animations.ComponentAnimation;
import com.codename1.ui.events.ActionListener;
import com.codename1.ui.layouts.BorderLayout;
import com.codename1.ui.layouts.BoxLayout;
import com.codename1.ui.layouts.FlowLayout;
import com.codename1.ui.layouts.GridLayout;
import com.codename1.ui.layouts.LayeredLayout;
import com.codename1.ui.plaf.Style;
import com.codename1.ui.plaf.UIManager;
import com.codename1.ui.spinner.Picker;
import com.codename1.ui.table.TableLayout;
import com.codename1.ui.util.Resources;
import entities.Evenement;
import entities.Produit;
import entities.user;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import services.ServiceEvent;
import services.ServiceProduit;

public class KitchenSink {

    //final Resources res;
    private Form c;
    private Resources theme;
    TextField emailTxt;

    public static user connectedUser;
    Container data;
    Integer userId;

    private Resources res;
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

    public void init(Object context) {
        // use 2 network threads for slightly faster networking but not too much to overburden the UI
        NetworkManager.getInstance().updateThreadCount(2);
        res = UIManager.initFirstTheme("/theme");
        Toolbar.setGlobalToolbar(true);
        Dialog.setDefaultBlurBackgroundRadius(10);
        Log.bindCrashProtection(false);
    }

    private void showDemoInformation(Form back, Demo d) {
        Form f = new Form("Information", new BorderLayout());
        Button sourceCode = new Button("View Source");
        FontImage.setMaterialIcon(sourceCode, FontImage.MATERIAL_WEB);
        sourceCode.addActionListener(e -> Display.getInstance().execute(d.getSourceCodeURL()));
        f.add(BorderLayout.CENTER, new SpanLabel(d.getDescription())).
                add(BorderLayout.SOUTH, sourceCode);
        f.getToolbar().setBackCommand("", e -> back.showBack());
        f.show();
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
                    showDemoInformation(f, d);
                });
                f.show();
            }
        });
        return dc;
    }
    
    private DemoComponent createDemoButtonProd(DemoProd d, Produit bp) {
  DemoComponent dc = new DemoComponent(d.getDisplayName(bp), d.getDemoIcon(bp), imageMask,
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
                    tabletSurface.replace(tabletSurface.getComponentAt(0), d.createDemoProd(Display.getInstance().getCurrent(), bp),
                            CommonTransitions.createCover(CommonTransitions.SLIDE_HORIZONTAL, true, 200));
                });
            } else {
                Form previous = Display.getInstance().getCurrent();
                Form f = new Form(d.getDisplayName(bp), new BorderLayout());
                f.add(BorderLayout.CENTER, d.createDemoProd(f, bp));
                f.getToolbar().setBackCommand(" ", ee -> {
                    if (d.onBack()) {
                        previous.showBack();
                    }
                });
                f.getToolbar().addMaterialCommandToRightBar("", FontImage.MATERIAL_INFO, 4, ee -> {
                    // showDemoInformationBus(f, d, bp);
                });
                f.show();
            }
        });
        return dc;
    }

    private DemoComponent createDemoButtonBus(Demobus d, Evenement bp) {
        DemoComponent dc = new DemoComponent(d.getDisplayName(bp), d.getDemoIcon(bp), imageMask,
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
                    tabletSurface.replace(tabletSurface.getComponentAt(0), d.createDemo(Display.getInstance().getCurrent(), bp),
                            CommonTransitions.createCover(CommonTransitions.SLIDE_HORIZONTAL, true, 200));
                });
            } else {
                Form previous = Display.getInstance().getCurrent();
                Form f = new Form(d.getDisplayName(bp), new BorderLayout());
                f.add(BorderLayout.CENTER, d.createDemo(f, bp));
                f.getToolbar().setBackCommand(" ", ee -> {
                    if (d.onBack()) {
                        previous.showBack();
                    }
                });
                f.getToolbar().addMaterialCommandToRightBar("", FontImage.MATERIAL_INFO, 4, ee -> {
                    // showDemoInformationBus(f, d, bp);
                });
                f.show();
            }
        });
        return dc;
    }

    private DemoComponent createDemoButtonMine(Demobus d, Evenement bp) {
        DemoComponent dc = new DemoComponent(d.getDisplayName(bp), d.getDemoIcon(bp), imageMask,
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
                    tabletSurface.replace(tabletSurface.getComponentAt(0), d.createDemoMine(Display.getInstance().getCurrent(), bp),
                            CommonTransitions.createCover(CommonTransitions.SLIDE_HORIZONTAL, true, 200));
                });
            } else {
                Form previous = Display.getInstance().getCurrent();
                Form f = new Form(d.getDisplayName(bp), new BorderLayout());
                f.add(BorderLayout.CENTER, d.createDemoMine(f, bp));
                f.getToolbar().setBackCommand(" ", ee -> {
                    if (d.onBack()) {
                        previous.showBack();
                    }
                });
//                f.getToolbar().addMaterialCommandToRightBar("", FontImage.MATERIAL_INFO, 4, ee -> {
//                    showDemoInformationBus(f, d, bp);
//                });
                f.show();
            }
        });
        return dc;
    }
    
    private DemoComponent createDemoButtonProdMine(DemoProd d, Produit bp) {
        DemoComponent dc = new DemoComponent(d.getDisplayName(bp), d.getDemoIcon(bp), imageMask,
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
                    tabletSurface.replace(tabletSurface.getComponentAt(0), d.createDemoProdMine(Display.getInstance().getCurrent(), bp),
                            CommonTransitions.createCover(CommonTransitions.SLIDE_HORIZONTAL, true, 200));
                });
            } else {
                Form previous = Display.getInstance().getCurrent();
                Form f = new Form(d.getDisplayName(bp), new BorderLayout());
                f.add(BorderLayout.CENTER, d.createDemoProdMine(f, bp));
                f.getToolbar().setBackCommand(" ", ee -> {
                    if (d.onBack()) {
                        previous.showBack();
                    }
                });
//                f.getToolbar().addMaterialCommandToRightBar("", FontImage.MATERIAL_INFO, 4, ee -> {
//                    showDemoInformationBus(f, d, bp);
//                });
                f.show();
            }
        });
        return dc;
    }

    private void showSplashAnimation() {
        theme = UIManager.initFirstTheme("/theme");
        Form splash = new Form(new LayeredLayout());
        splash.setUIID("Splash");
        splash.getContentPane().setUIID("Container");
        splash.getToolbar().setUIID("Container");
        ScaleImageLabel iconBackground = new ScaleImageLabel(res.getImage("codenameone-icon-background.png"));
        iconBackground.setBackgroundType(Style.BACKGROUND_IMAGE_SCALED_FILL);
        Container centerBackground = BorderLayout.center(iconBackground);
        splash.add(centerBackground);
        Label iconForeground = new Label(res.getImage("codenameone-icon-foreground.png"));
        Container centerIcon = BorderLayout.centerAbsolute(iconForeground);
        splash.add(centerIcon);

        splash.show();
        Display.getInstance().callSerially(() -> {
            ((BorderLayout) centerBackground.getLayout()).setCenterBehavior(BorderLayout.CENTER_BEHAVIOR_CENTER_ABSOLUTE);
            centerBackground.setShouldCalcPreferredSize(true);
            centerBackground.animateLayoutAndWait(350);

            iconForeground.remove();
            iconBackground.remove();
            centerIcon.remove();
            Container layers = LayeredLayout.encloseIn(
                    new Label(iconBackground.getIcon(), "CenterIcon"),
                    new Label(iconForeground.getIcon(), "CenterIcon"));
            Container boxy = BoxLayout.encloseY(layers);
            Label placeholder = new Label();
            placeholder.setShowEvenIfBlank(true);
            Label kitchenSink = new Label("SoukLemdina", "SplashTitle");
            Component.setSameHeight(placeholder, kitchenSink);
            Component.setSameWidth(placeholder, kitchenSink, boxy);
            centerBackground.add(BorderLayout.CENTER, boxy);
            splash.revalidate();
            Display.getInstance().callSerially(() -> {
                placeholder.setText(" ");
                boxy.add(placeholder);
                boxy.setShouldCalcPreferredSize(true);
                boxy.getParent().animateLayoutAndWait(400);
                boxy.replaceAndWait(placeholder, kitchenSink, CommonTransitions.createFade(500));

                Label newPlaceholder = new Label(" ");
                Label byCodenameOne = new Label("par Spiritus", "SplashSubTitle");
                Component.setSameHeight(newPlaceholder, byCodenameOne);
                Component.setSameWidth(newPlaceholder, byCodenameOne);
                boxy.add(newPlaceholder);
                boxy.getParent().animateLayoutAndWait(400);
                boxy.replaceAndWait(newPlaceholder, byCodenameOne, CommonTransitions.createFade(500));

                byCodenameOne.setY(splash.getHeight());
                kitchenSink.setY(splash.getHeight());
                layers.setY(splash.getHeight());
                boxy.setHeight(splash.getHeight());

                boxy.animateUnlayoutAndWait(450, 20);
                splash.setTransitionOutAnimator(CommonTransitions.createEmpty());

                // create image masks for card effects
                Image mask = res.getImage("card-full.png");
                maskWidth = mask.getWidth();
                maskHeight = mask.getHeight() / 4 * 3;
                Image top = mask.subImage(0, 0, maskWidth, maskHeight, true);
                Image bottom = mask.subImage(0, maskHeight, maskWidth, mask.getHeight() / 4, true);
                imageMask = top.createMask();

                Image circleMaskImage = res.getImage("circle.png");
                circleMask = circleMaskImage.createMask();
                circleMaskWidth = circleMaskImage.getWidth();
                circleMaskHeight = circleMaskImage.getHeight();

                colorBottoms = new Image[7];
                colors = new int[colorBottoms.length];
                Object bottomMask = bottom.createMask();
                for (int iter = 0; iter < colorBottoms.length; iter++) {
                    colors[iter] = splash.getUIManager().getComponentStyle("Blank" + (iter + 1)).getBgColor();
                    colorBottoms[iter] = Image.createImage(bottom.getWidth(), bottom.getHeight(), 0xff000000 | colors[iter]);
                    colorBottoms[iter] = colorBottoms[iter].applyMask(bottomMask);
                }

                showMainUI();
            });
        });
    }

    public void start() {
        if (getAppstoreURL() != null) {
            RatingWidget.bindRatingListener(180000, getAppstoreURL(), "apps@codenameone.com");
        }
        if (currentForm != null && !(currentForm instanceof Dialog)) {
            currentForm.show();
            return;
        }
        showInput();
    }

    private void addComps(Form parent, Container cnt, Component... cmps) {
        if (Display.getInstance().isTablet() || !Display.getInstance().isPortrait()) {
            TableLayout tl = new TableLayout(cmps.length / 2, 2);
            cnt.setLayout(tl);
            tl.setGrowHorizontally(true);
            for (Component c : cmps) {
                if (c instanceof Container) {
                    cnt.add(tl.createConstraint().horizontalSpan(2), c);
                } else {
                    cnt.add(c);
                }
            }
        } else {
            cnt.setLayout(BoxLayout.y());
            for (Component c : cmps) {
                cnt.add(c);
            }
        }
        if (cnt.getClientProperty("bound") == null) {
            cnt.putClientProperty("bound", "true");
            if (!Display.getInstance().isTablet()) {
                parent.addOrientationListener((e) -> {
                    Display.getInstance().callSerially(() -> {
                        cnt.removeAll();
                        addComps(parent, cnt, cmps);
                        cnt.animateLayout(800);
                    });
                });
            }
        }
    }

    protected Resources getResources() {
        return res;
    }

    public Container createDemoInput(Form parent) {
        TextField name = new TextField("", "Name", 20, TextField.ANY);
        FontImage.setMaterialIcon(name.getHintLabel(), FontImage.MATERIAL_PERSON);
        TextField email = new TextField("", "E-mail", 20, TextField.EMAILADDR);
        FontImage.setMaterialIcon(email.getHintLabel(), FontImage.MATERIAL_EMAIL);
        TextField password = new TextField("", "Password", 20, TextField.PASSWORD);
        FontImage.setMaterialIcon(password.getHintLabel(), FontImage.MATERIAL_LOCK);
        TextField bio = new TextField("", "Bio", 2, 20);
        FontImage.setMaterialIcon(bio.getHintLabel(), FontImage.MATERIAL_LIBRARY_BOOKS);
        Picker birthday = new Picker();
        birthday.setType(Display.PICKER_TYPE_DATE);
        OnOffSwitch joinMailingList = new OnOffSwitch();
        bio.setSingleLineTextArea(false);

        Container comps = new Container();
        addComps(parent, comps,
                new Label("Name", "InputContainerLabel"),
                name,
                new Label("E-Mail", "InputContainerLabel"),
                email,
                new Label("Password", "InputContainerLabel"),
                password,
                BorderLayout.center(new Label("Birthday", "InputContainerLabel")).
                        add(BorderLayout.EAST, birthday),
                new Label("Bio", "InputContainerLabel"),
                bio,
                BorderLayout.center(new Label("Join Mailing List", "InputContainerLabel")).
                        add(BorderLayout.EAST, joinMailingList));

        comps.setScrollableY(true);
        comps.setUIID("PaddedContainer");

        Container content = BorderLayout.center(comps);

        Button save = new Button("Save");
        save.setUIID("InputAvatarImage");
        content.add(BorderLayout.SOUTH, save);
        save.addActionListener(e -> {
            //ToastBar.showMessage("Save pressed...", FontImage.MATERIAL_INFO);
            showMainUI();
        });

        content.setUIID("InputContainerForeground");

        Button avatar = new Button("");
        avatar.setUIID("InputAvatar");
        Image defaultAvatar = FontImage.createMaterial(FontImage.MATERIAL_CAMERA, "InputAvatarImage", 8);
        Image circleMaskImage = getResources().getImage("circle.png");
        defaultAvatar = defaultAvatar.scaled(circleMaskImage.getWidth(), circleMaskImage.getHeight());
        defaultAvatar = ((FontImage) defaultAvatar).toEncodedImage();
        Object circleMask = circleMaskImage.createMask();
        defaultAvatar = defaultAvatar.applyMask(circleMask);
        avatar.setIcon(defaultAvatar);

        avatar.addActionListener(e -> {
            if (Dialog.show("Camera or Gallery", "Would you like to use the camera or the gallery for the picture?", "Camera", "Gallery")) {
                String pic = Capture.capturePhoto();
                if (pic != null) {
                    try {
                        Image img = Image.createImage(pic).fill(circleMaskImage.getWidth(), circleMaskImage.getHeight());
                        avatar.setIcon(img.applyMask(circleMask));
                    } catch (IOException err) {
                        ToastBar.showErrorMessage("An error occured while loading the image: " + err);
                        Log.e(err);
                    }
                }
            } else {
                Display.getInstance().openGallery(ee -> {
                    if (ee.getSource() != null) {
                        try {
                            Image img = Image.createImage((String) ee.getSource()).fill(circleMaskImage.getWidth(), circleMaskImage.getHeight());
                            avatar.setIcon(img.applyMask(circleMask));
                        } catch (IOException err) {
                            ToastBar.showErrorMessage("An error occured while loading the image: " + err);
                            Log.e(err);
                        }
                    }
                }, Display.GALLERY_IMAGE);
            }
        });

        Container actualContent = LayeredLayout.encloseIn(content,
                FlowLayout.encloseCenter(avatar));

        Container input;
        if (!Display.getInstance().isTablet()) {
            Label placeholder = new Label(" ");

            Component.setSameHeight(actualContent, placeholder);
            Component.setSameWidth(actualContent, placeholder);

            input = BorderLayout.center(placeholder);

            parent.addShowListener(e -> {
                if (placeholder.getParent() != null) {
                    input.replace(placeholder, actualContent, CommonTransitions.createFade(1500));
                }
            });
        } else {
            input = BorderLayout.center(actualContent);
        }
        input.setUIID("InputContainerBackground");

        return input;
    }

    private void showInput() {

        this.res = res;
        theme = UIManager.initFirstTheme("/theme_3");
        Form f = new Form("Bienvenue");
        f.setLayout(new BorderLayout());
        Container north = new Container(new FlowLayout(Component.CENTER));
        north.setUIID("header");
        ImageViewer logo = new ImageViewer(res.getImage("logo.png"));

        north.add(logo);
        f.add(BorderLayout.NORTH, north);
        // Container center = new Container(new BoxLayout(BoxLayout.Y_AXIS));
        //  center.setUIID("loginbackground");

        emailTxt = new TextField();
        emailTxt.setHint("Nom d'utilisateur");
        emailTxt.setUIID("loginTextFieald");
        TextField pwdTxt = new TextField();
        pwdTxt.setHint("Mot de Passe");
        pwdTxt.setUIID("loginTextFieald");
        pwdTxt.setConstraint(TextField.PASSWORD);
        Button loginBtn = new Button();
        loginBtn.setUIID("LoginBtn");
        loginBtn.setText("Se Connecter");

        Container center = BoxLayout.encloseY(ComponentGroup.enclose(emailTxt, pwdTxt, loginBtn));

//        center.add(emailTxt);
//        center.add(pwdTxt);
//        center.add(loginBtn);
        center.setScrollableY(true);
        center.setScrollableX(false);

        f.add(BorderLayout.CENTER, center);

        Container south = new Container(new FlowLayout(Component.CENTER));
        south.setUIID("footer");

        Button Exit = new Button("Sortir", res.getImage("power-signal.png"));
        Exit.setGap(Exit.getStyle().getFont().getHeight());
        Exit.setUIID("LoginBtn");
        south.add(Exit);
        Exit.addActionListener((evt) -> {
            Toolbar.setGlobalToolbar(true);
            showSplashAnimation();
            System.out.println("ba3d");
        });
        f.add(BorderLayout.SOUTH, south);
        System.out.println("aazddza");
        //Button loginButton = new Button("LOGIN");
        //loginButton.setUIID("LoginButton");
        loginBtn.addActionListener(e -> {

            ConnectionRequest con = new ConnectionRequest();
            String email = emailTxt.getText();
            String pswd = pwdTxt.getText();
            if (emailTxt.getText() == "") {
                Dialog.show("champ obligatoire", "vous devez taper votre username!!", "OK", "");

            }
            if (pwdTxt.getText() == "") {
                Dialog.show("champ obligatoire", "vous devez taper votre password!!", "OK", "");

            }
            con.setUrl("http://localhost/medina/web/app_dev.php/mobile/login?username=" + email + "&password=" + pswd);

            con.addResponseListener(new ActionListener<NetworkEvent>() {
                @Override
                public void actionPerformed(NetworkEvent evt) {

                    try {
                        String json = new String(con.getResponseData());
                        JSONParser j = new JSONParser();
                        System.out.println(json);
                        Map<String, Object> users = j.parseJSON(new CharArrayReader(json.toCharArray()));

                        System.out.println("+++++++++++++++++++" + users.get("username"));
                        if (users.get("password").equals(0)) {
                            Dialog.show("Erreur d'authentification", "Verifier votre Nom d'utilisateur ou mot de passe!!", "OK", "Annuler");

                        } else {
                            connectedUser = new user();
                            connectedUser.setId(Integer.parseInt(users.get("id").toString().substring(0, 1)));
                            connectedUser.setUsername(users.get("username").toString());
                            connectedUser.setPassword(users.get("password").toString());
                            //connectedUser.setRole(users.get("role").toString());
                            System.out.println(connectedUser.toString() + "+++++++++////+++++++++" + connectedUser.getId());
                            userId = (int) Float.parseFloat(users.get("id").toString());
                            Toolbar.setGlobalToolbar(true);
                            showSplashAnimation();
                            System.out.println("ba3d");

                        }

                    } catch (IOException ex) {
                        System.out.println("erreur");
                    }
                }
            });
            NetworkManager.getInstance().addToQueue(con);

        });

        f.show();
    }

    public void showMainUI() {
        final Form f = new Form("Souk El Medina", new BorderLayout());

        Demo[] demos = new Demo[]{
            new Affichage(),
            new ListPanierUser(),
            // new Layouts(), new WebServices(),
            new ClockDemo(),
            new Themes(),/* new Contacts(),*/
          //  new Input(),
          //  new Video(),/* new SalesDemo(),*/
        };

        for (Demo d : demos) {
            d.init(res);
        }

        Image dukeImage = Image.createImage(circleMaskWidth, circleMaskHeight, 0);
        Graphics g = dukeImage.getGraphics();
        g.drawImage(res.getImage("codenameone-icon-background.png"), 0, 0, circleMaskWidth, circleMaskHeight);
        g.drawImage(res.getImage("6-knocker.jpg"), 0, 0, circleMaskWidth, circleMaskHeight);
        dukeImage = dukeImage.applyMask(circleMask);
        Label duke = new Label(dukeImage);
        Label circle = new Label(res.getImage("circle-line.png"));
        Container dukeImageContainer = LayeredLayout.encloseIn(duke, circle);
        Label name = new Label("Cha");
        name.setUIID("DukeName");
        Container dukeContainer = BorderLayout.west(BoxLayout.encloseY(dukeImageContainer, name));
        dukeContainer.setUIID("ProfileContainer");

        if (Display.getInstance().isTablet()) {
            Toolbar.setPermanentSideMenu(true);
            f.getToolbar().addComponentToSideMenu(dukeContainer);
            for (Demo d : demos) {
                f.getToolbar().addComponentToSideMenu(createDemoButton(d));
            }
            tabletSurface = f.getContentPane();
            f.add(BorderLayout.CENTER, demos[0].createDemo(f));
            f.show();
            return;
        }

        f.getToolbar().addComponentToSideMenu(dukeContainer);

        Container cnt;
        if (Preferences.get("gridLayout", true)) {
            GridLayout gl = new GridLayout(1);
            gl.setAutoFit(true);
            gl.setHideZeroSized(true);
            cnt = new Container(gl);
            for (Demo d : demos) {
                cnt.add(createDemoButton(d));
            }
        } else {
            cnt = new Container(BoxLayout.y());
            for (Demo d : demos) {
                cnt.add(createDemoButton(d));
            }
        }
        cnt.setScrollableY(true);
        f.add(BorderLayout.CENTER, cnt);

        f.getToolbar().addSearchCommand(e -> {
            String t = (String) e.getSource();
            if (t == null) {
                t = "";
            } else {
                t = t.toLowerCase();
            }
            for (Component c : cnt) {
                DemoComponent mb = (DemoComponent) c;
                boolean show = t.length() == 0 || mb.getText().toLowerCase().indexOf(t) > -1;
                mb.setVisible(show);
                mb.setHidden(!show);
            }
            cnt.animateLayout(200);
        }, 3);

        gridCommand = f.getToolbar().addMaterialCommandToRightBar("", FontImage.MATERIAL_VIEW_COMFY, 4, e -> {
            if (cnt.getAnimationManager().isAnimating()) {
                return;
            }
            if (!(cnt.getLayout() instanceof GridLayout)) {
                f.removeCommand(gridCommand);
                f.getToolbar().addCommandToRightBar(listCommand);
                f.getToolbar().layoutContainer();
                Preferences.set("gridLayout", true);
                ComponentAnimation[] arr = new ComponentAnimation[cnt.getComponentCount() + 1];
                int offset = 0;
                for (Component c : cnt) {
                    DemoComponent mb = (DemoComponent) c;
                    arr[offset] = mb.lineToGridStage1();
                    offset++;
                }
                arr[offset] = cnt.createAnimateHierarchy(1000);

                cnt.getAnimationManager().addAnimationAndBlock(ComponentAnimation.compoundAnimation(arr));
                cnt.getParent().revalidate();

                ComponentAnimation[] arr2 = new ComponentAnimation[cnt.getComponentCount()];
                offset = 0;
                for (Component c : cnt) {
                    DemoComponent mb = (DemoComponent) c;
                    arr2[offset] = mb.lineToGridStage2();
                    offset++;
                }

                cnt.getAnimationManager().addAnimationAndBlock(ComponentAnimation.compoundAnimation(arr2));

                GridLayout gl = new GridLayout(1);
                gl.setAutoFit(true);
                gl.setHideZeroSized(true);
                cnt.setLayout(gl);

                cnt.animateLayout(300);

            }
        });

        listCommand = f.getToolbar().addMaterialCommandToRightBar("", FontImage.MATERIAL_FORMAT_LIST_BULLETED, 4, e -> {
            if (!(cnt.getLayout() instanceof BoxLayout)) {
                f.removeCommand(listCommand);
                f.getToolbar().addCommandToRightBar(gridCommand);
                f.getToolbar().layoutContainer();
                Preferences.set("gridLayout", false);
                ComponentAnimation[] arr = new ComponentAnimation[cnt.getComponentCount()];
                int offset = 0;
                for (Component c : cnt) {
                    DemoComponent mb = (DemoComponent) c;
                    arr[offset] = mb.gridToLineStage1();
                    offset++;
                }
                cnt.getAnimationManager().addAnimationAndBlock(ComponentAnimation.compoundAnimation(arr));

                cnt.setLayout(BoxLayout.y());

                arr = new ComponentAnimation[cnt.getComponentCount() + 1];

                offset = 0;
                for (Component c : cnt) {
                    DemoComponent mb = (DemoComponent) c;
                    arr[offset] = mb.gridToLineStage2();
                    offset++;
                }
                arr[offset] = cnt.createAnimateHierarchy(500);
                cnt.getAnimationManager().addAnimationAndBlock(ComponentAnimation.compoundAnimation(arr));

                cnt.getParent().revalidate();
            }
        });

        if (cnt.getLayout() instanceof GridLayout) {
            f.removeCommand(gridCommand);
        } else {
            f.removeCommand(listCommand);
        }

        // f.getToolbar().addMaterialCommandToSideMenu("CodenameOne.com",
        //  FontImage.MATERIAL_WEB, e -> Display.getInstance().execute("https://www.codenameone.com/"));
        f.getToolbar().addMaterialCommandToSideMenu("Acceuil", FontImage.MATERIAL_MAP, e -> showMainUI());
        f.getToolbar().addMaterialCommandToSideMenu("Evenements", FontImage.MATERIAL_BUSINESS, e -> showMainEvent());
        f.getToolbar().addMaterialCommandToSideMenu("Mes evenements", FontImage.MATERIAL_SIM_CARD, e -> showMainEventMine());
        f.getToolbar().addMaterialCommandToSideMenu("Produits", FontImage.MATERIAL_TRENDING_DOWN, e -> showMainProduit());
        f.getToolbar().addMaterialCommandToSideMenu("Mes produits", FontImage.MATERIAL_BUSINESS, e -> showMainProduitMine());
        f.getToolbar().addMaterialCommandToSideMenu("Locaux", FontImage.MATERIAL_BOOK, e -> showMainLocal());
        f.getToolbar().addMaterialCommandToSideMenu("Mes locaux", FontImage.MATERIAL_BUSINESS, e -> showMainLocalMine());
        f.getToolbar().addMaterialCommandToSideMenu("Panier", FontImage.MATERIAL_MONEY_OFF, e -> showMainPanier());
        if (Display.getInstance().isNativeShareSupported() && getAppstoreURL() != null) {
            f.getToolbar().addMaterialCommandToSideMenu("Spread the Word!", FontImage.MATERIAL_SHARE, e -> {
                Display.getInstance().share("Check out the kitchen sink app from Codename One: " + getAppstoreURL(), null, null);
            });
        }
        f.getToolbar().addMaterialCommandToSideMenu("A propos",
                FontImage.MATERIAL_INFO, e -> {
                    Dialog.show("A propos", "Souk El Medina est la premiere application en Tunisie qui concerne l'artisana. "
                            + "Pour plus d'information , visitez notre site web : www.soukLemdina.com!", "OK", null);
                });

        f.getToolbar().setVisible(false);
        cnt.setVisible(false);
        for (Component c : cnt) {
            c.setVisible(false);
        }
        f.addShowListener(e -> {
            f.getToolbar().setHeight(0);
            f.getToolbar().setVisible(true);
            f.animateLayoutFadeAndWait(200, 100);
            for (Component c : cnt) {
                c.setY(f.getHeight());
                c.setVisible(true);
                c.getUnselectedStyle().setOpacity(100);
            }
            cnt.setVisible(true);
            cnt.animateLayoutFadeAndWait(400, 100);
            f.removeAllShowListeners();
        });
        f.setTransitionInAnimator(CommonTransitions.createEmpty());
        f.show();
    }

    private void showMainEvent() {
        final Form f = new Form("Events", new BorderLayout());

        List<Demobus> demos = new ArrayList<>();

        ServiceEvent serviceBus = new ServiceEvent();
        ArrayList<Evenement> listeB = serviceBus.getList2();

        System.out.println(listeB.toString());

        for (Evenement bp : listeB) {

            demos.add(new Bus(bp));
        }

        for (Demobus d : demos) {
            d.init(res);
        }

        Image dukeImage = Image.createImage(circleMaskWidth, circleMaskHeight, 0);
        Graphics g = dukeImage.getGraphics();
        g.drawImage(res.getImage("codenameone-icon-background.png"), 0, 0, circleMaskWidth, circleMaskHeight);
        g.drawImage(res.getImage("codenameone-icon-foreground.png"), 0, 0, circleMaskWidth, circleMaskHeight);
        dukeImage = dukeImage.applyMask(circleMask);
        Label duke = new Label(dukeImage);
        Label circle = new Label(res.getImage("circle-line.png"));
        Container dukeImageContainer = LayeredLayout.encloseIn(duke, circle);
        Label name = new Label("Duke");
        name.setUIID("DukeName");
        Container dukeContainer = BorderLayout.west(BoxLayout.encloseY(dukeImageContainer, name));
        dukeContainer.setUIID("ProfileContainer");

//        if(isTablet()) {
//            Toolbar.setPermanentSideMenu(true);
//            f.getToolbar().addComponentToSideMenu(dukeContainer);
//            //select bonplan
//            
//            for(Demobus d : demos) {
//                f.getToolbar().addComponentToSideMenu(createDemoButtonBus(d,d.getBp()));
//            }
//            tabletSurface = f.getContentPane();
//            f.add(CENTER, demos.get(0).createDemo(f,demos.get(0).getBp()));
//            f.show();
//            return;
//        }
        f.getToolbar().addComponentToSideMenu(dukeContainer);

        Container cnt;
        if (Preferences.get("gridLayout", true)) {
            GridLayout gl = new GridLayout(1);
            gl.setAutoFit(true);
            gl.setHideZeroSized(true);
            cnt = new Container(gl);
            for (Demobus d : demos) {
                cnt.add(createDemoButtonBus(d, d.getBp()));
            }
        } else {
            cnt = new Container(BoxLayout.y());
            for (Demobus d : demos) {
                cnt.add(createDemoButtonBus(d, d.getBp()));
            }
        }
        cnt.setScrollableY(true);
        f.add(BorderLayout.CENTER, cnt);

        f.getToolbar().addSearchCommand(e -> {
            String t = (String) e.getSource();
            if (t == null) {
                t = "";
            } else {
                t = t.toLowerCase();
            }
            for (Component c : cnt) {
                DemoComponent mb = (DemoComponent) c;
                boolean show = t.length() == 0 || mb.getText().toLowerCase().indexOf(t) > -1;
                mb.setVisible(show);
                mb.setHidden(!show);
            }
            cnt.animateLayout(200);
        }, 3);

        gridCommand = f.getToolbar().addMaterialCommandToRightBar("", FontImage.MATERIAL_VIEW_COMFY, 4, e -> {
            if (cnt.getAnimationManager().isAnimating()) {
                return;
            }
            if (!(cnt.getLayout() instanceof GridLayout)) {
                f.removeCommand(gridCommand);
                f.getToolbar().addCommandToRightBar(listCommand);
                f.getToolbar().layoutContainer();
                Preferences.set("gridLayout", true);
                ComponentAnimation[] arr = new ComponentAnimation[cnt.getComponentCount() + 1];
                int offset = 0;
                for (Component c : cnt) {
                    DemoComponent mb = (DemoComponent) c;
                    arr[offset] = mb.lineToGridStage1();
                    offset++;
                }
                arr[offset] = cnt.createAnimateHierarchy(1000);

                cnt.getAnimationManager().addAnimationAndBlock(ComponentAnimation.compoundAnimation(arr));
                cnt.getParent().revalidate();

                ComponentAnimation[] arr2 = new ComponentAnimation[cnt.getComponentCount()];
                offset = 0;
                for (Component c : cnt) {
                    DemoComponent mb = (DemoComponent) c;
                    arr2[offset] = mb.lineToGridStage2();
                    offset++;
                }

                cnt.getAnimationManager().addAnimationAndBlock(ComponentAnimation.compoundAnimation(arr2));

                GridLayout gl = new GridLayout(1);
                gl.setAutoFit(true);
                gl.setHideZeroSized(true);
                cnt.setLayout(gl);

                cnt.animateLayout(300);

            }
        });

        listCommand = f.getToolbar().addMaterialCommandToRightBar("", FontImage.MATERIAL_FORMAT_LIST_BULLETED, 4, e -> {
            if (!(cnt.getLayout() instanceof BoxLayout)) {
                f.removeCommand(listCommand);
                f.getToolbar().addCommandToRightBar(gridCommand);
                f.getToolbar().layoutContainer();
                Preferences.set("gridLayout", false);
                ComponentAnimation[] arr = new ComponentAnimation[cnt.getComponentCount()];
                int offset = 0;
                for (Component c : cnt) {
                    DemoComponent mb = (DemoComponent) c;
                    arr[offset] = mb.gridToLineStage1();
                    offset++;
                }
                cnt.getAnimationManager().addAnimationAndBlock(ComponentAnimation.compoundAnimation(arr));

                cnt.setLayout(BoxLayout.y());

                arr = new ComponentAnimation[cnt.getComponentCount() + 1];

                offset = 0;
                for (Component c : cnt) {
                    DemoComponent mb = (DemoComponent) c;
                    arr[offset] = mb.gridToLineStage2();
                    offset++;
                }
                arr[offset] = cnt.createAnimateHierarchy(500);
                cnt.getAnimationManager().addAnimationAndBlock(ComponentAnimation.compoundAnimation(arr));

                cnt.getParent().revalidate();
            }
        });

        if (cnt.getLayout() instanceof GridLayout) {
            f.removeCommand(gridCommand);
        } else {
            f.removeCommand(listCommand);
        }

        f.getToolbar().addMaterialCommandToSideMenu("CodenameOne",
                FontImage.MATERIAL_WEB, e -> Display.getInstance().execute("https://www.codenameone.com/"));

        f.getToolbar().addMaterialCommandToSideMenu("Acceuil", FontImage.MATERIAL_MAP, e -> showMainUI());
        f.getToolbar().addMaterialCommandToSideMenu("Evenements", FontImage.MATERIAL_BUSINESS, e -> showMainEvent());
        f.getToolbar().addMaterialCommandToSideMenu("Mes evenements", FontImage.MATERIAL_SIM_CARD, e -> showMainEventMine());
        f.getToolbar().addMaterialCommandToSideMenu("Produits", FontImage.MATERIAL_TRENDING_DOWN, e -> showMainProduit());
        f.getToolbar().addMaterialCommandToSideMenu("Mes produits", FontImage.MATERIAL_BUSINESS, e -> showMainProduitMine());
        f.getToolbar().addMaterialCommandToSideMenu("Locaux", FontImage.MATERIAL_BOOK, e -> showMainLocal());
        f.getToolbar().addMaterialCommandToSideMenu("Mes locaux", FontImage.MATERIAL_BUSINESS, e -> showMainLocalMine());
        f.getToolbar().addMaterialCommandToSideMenu("Panier", FontImage.MATERIAL_MONEY_OFF, e -> showMainPanier());
        if (Display.getInstance().isNativeShareSupported() && getAppstoreURL() != null) {
            f.getToolbar().addMaterialCommandToSideMenu("Spread the Word!", FontImage.MATERIAL_SHARE, e -> {
                Display.getInstance().share("Check out the kitchen sink app from Codename One: " + getAppstoreURL(), null, null);
            });
        }
        f.getToolbar().addMaterialCommandToSideMenu("About",
                FontImage.MATERIAL_INFO, e -> {
                    Dialog.show("About", "KitchenSink provides an overview of the core Codename One capaiblities. "
                            + "Codename One allows Java developers to create native mobile applications that work everywhere!", "OK", null);
                });

        f.getToolbar().setVisible(false);
        cnt.setVisible(false);
        for (Component c : cnt) {
            c.setVisible(false);
        }
        f.addShowListener(e -> {
            f.getToolbar().setHeight(0);
            f.getToolbar().setVisible(true);
            f.animateLayoutFadeAndWait(200, 100);
            for (Component c : cnt) {
                c.setY(f.getHeight());
                c.setVisible(true);
                c.getUnselectedStyle().setOpacity(100);
            }
            cnt.setVisible(true);
            cnt.animateLayoutFadeAndWait(400, 100);
            f.removeAllShowListeners();
        });
        f.setTransitionInAnimator(CommonTransitions.createEmpty());
        f.show();
    }

    private void showMainEventMine() {
        final Form f = new Form("Mes Evenements", new BorderLayout());

        List<Demobus> demos = new ArrayList<>();

        ServiceEvent serviceBus = new ServiceEvent();
        ArrayList<Evenement> listeB = serviceBus.getListMine();

        System.out.println(listeB.toString());

        for (Evenement bp : listeB) {

            demos.add(new Bus(bp));
        }

        for (Demobus d : demos) {
            d.init(res);
        }

        Image dukeImage = Image.createImage(circleMaskWidth, circleMaskHeight, 0);
        Graphics g = dukeImage.getGraphics();
        g.drawImage(res.getImage("codenameone-icon-background.png"), 0, 0, circleMaskWidth, circleMaskHeight);
        g.drawImage(res.getImage("codenameone-icon-foreground.png"), 0, 0, circleMaskWidth, circleMaskHeight);
        dukeImage = dukeImage.applyMask(circleMask);
        Label duke = new Label(dukeImage);
        Label circle = new Label(res.getImage("circle-line.png"));
        Container dukeImageContainer = LayeredLayout.encloseIn(duke, circle);
        Label name = new Label("Duke");
        name.setUIID("DukeName");
        Container dukeContainer = BorderLayout.west(BoxLayout.encloseY(dukeImageContainer, name));
        dukeContainer.setUIID("ProfileContainer");

//        if(Display.getInstance().isTablet()) {
//            Toolbar.setPermanentSideMenu(true);
//            f.getToolbar().addComponentToSideMenu(dukeContainer);
//            //select bonplan
//            
//            for(Demobus d : demos) {
//                f.getToolbar().addComponentToSideMenu(createDemoButtonBusMine(d,d.getBp()));
//            }
//            tabletSurface = f.getContentPane();
//            f.add(Display.getInstance().CENTER, demos.get(0).createDemoMine(f,demos.get(0).getBp()));
//            f.show();
//            return;
//        }
        f.getToolbar().addComponentToSideMenu(dukeContainer);

        Container cnt;
        if (Preferences.get("gridLayout", true)) {
            GridLayout gl = new GridLayout(1);
            gl.setAutoFit(true);
            gl.setHideZeroSized(true);
            cnt = new Container(gl);
            for (Demobus d : demos) {
                cnt.add(createDemoButtonMine(d, d.getBp()));
            }
        } else {
            cnt = new Container(BoxLayout.y());
            for (Demobus d : demos) {
                cnt.add(createDemoButtonMine(d, d.getBp()));
            }
        }
        cnt.setScrollableY(true);
        f.add(BorderLayout.CENTER, cnt);

        f.getToolbar().addSearchCommand(e -> {
            String t = (String) e.getSource();
            if (t == null) {
                t = "";
            } else {
                t = t.toLowerCase();
            }
            for (Component c : cnt) {
                DemoComponent mb = (DemoComponent) c;
                boolean show = t.length() == 0 || mb.getText().toLowerCase().indexOf(t) > -1;
                mb.setVisible(show);
                mb.setHidden(!show);
            }
            cnt.animateLayout(200);
        }, 3);

        gridCommand = f.getToolbar().addMaterialCommandToRightBar("", FontImage.MATERIAL_VIEW_COMFY, 4, e -> {
            if (cnt.getAnimationManager().isAnimating()) {
                return;
            }
            if (!(cnt.getLayout() instanceof GridLayout)) {
                f.removeCommand(gridCommand);
                f.getToolbar().addCommandToRightBar(listCommand);
                f.getToolbar().layoutContainer();
                Preferences.set("gridLayout", true);
                ComponentAnimation[] arr = new ComponentAnimation[cnt.getComponentCount() + 1];
                int offset = 0;
                for (Component c : cnt) {
                    DemoComponent mb = (DemoComponent) c;
                    arr[offset] = mb.lineToGridStage1();
                    offset++;
                }
                arr[offset] = cnt.createAnimateHierarchy(1000);

                cnt.getAnimationManager().addAnimationAndBlock(ComponentAnimation.compoundAnimation(arr));
                cnt.getParent().revalidate();

                ComponentAnimation[] arr2 = new ComponentAnimation[cnt.getComponentCount()];
                offset = 0;
                for (Component c : cnt) {
                    DemoComponent mb = (DemoComponent) c;
                    arr2[offset] = mb.lineToGridStage2();
                    offset++;
                }

                cnt.getAnimationManager().addAnimationAndBlock(ComponentAnimation.compoundAnimation(arr2));

                GridLayout gl = new GridLayout(1);
                gl.setAutoFit(true);
                gl.setHideZeroSized(true);
                cnt.setLayout(gl);

                cnt.animateLayout(300);

            }
        });

        listCommand = f.getToolbar().addMaterialCommandToRightBar("", FontImage.MATERIAL_FORMAT_LIST_BULLETED, 4, e -> {
            if (!(cnt.getLayout() instanceof BoxLayout)) {
                f.removeCommand(listCommand);
                f.getToolbar().addCommandToRightBar(gridCommand);
                f.getToolbar().layoutContainer();
                Preferences.set("gridLayout", false);
                ComponentAnimation[] arr = new ComponentAnimation[cnt.getComponentCount()];
                int offset = 0;
                for (Component c : cnt) {
                    DemoComponent mb = (DemoComponent) c;
                    arr[offset] = mb.gridToLineStage1();
                    offset++;
                }
                cnt.getAnimationManager().addAnimationAndBlock(ComponentAnimation.compoundAnimation(arr));

                cnt.setLayout(BoxLayout.y());

                arr = new ComponentAnimation[cnt.getComponentCount() + 1];

                offset = 0;
                for (Component c : cnt) {
                    DemoComponent mb = (DemoComponent) c;
                    arr[offset] = mb.gridToLineStage2();
                    offset++;
                }
                arr[offset] = cnt.createAnimateHierarchy(500);
                cnt.getAnimationManager().addAnimationAndBlock(ComponentAnimation.compoundAnimation(arr));

                cnt.getParent().revalidate();
            }
        });

        if (cnt.getLayout() instanceof GridLayout) {
            f.removeCommand(gridCommand);
        } else {
            f.removeCommand(listCommand);
        }

        f.getToolbar().addMaterialCommandToSideMenu("CodenameOne",
                FontImage.MATERIAL_WEB, e -> Display.getInstance().execute("https://www.codenameone.com/"));

        f.getToolbar().addMaterialCommandToSideMenu("Acceuil", FontImage.MATERIAL_MAP, e -> showMainUI());
        f.getToolbar().addMaterialCommandToSideMenu("Evenements", FontImage.MATERIAL_BUSINESS, e -> showMainEvent());
        f.getToolbar().addMaterialCommandToSideMenu("Mes evenements", FontImage.MATERIAL_SIM_CARD, e -> showMainEventMine());
        f.getToolbar().addMaterialCommandToSideMenu("Produits", FontImage.MATERIAL_TRENDING_DOWN, e -> showMainProduit());
        f.getToolbar().addMaterialCommandToSideMenu("Mes produits", FontImage.MATERIAL_BUSINESS, e -> showMainProduitMine());
        f.getToolbar().addMaterialCommandToSideMenu("Locaux", FontImage.MATERIAL_BOOK, e -> showMainLocal());
        f.getToolbar().addMaterialCommandToSideMenu("Mes locaux", FontImage.MATERIAL_BUSINESS, e -> showMainLocalMine());
        f.getToolbar().addMaterialCommandToSideMenu("Panier", FontImage.MATERIAL_MONEY_OFF, e -> showMainPanier());
        if (Display.getInstance().isNativeShareSupported() && getAppstoreURL() != null) {
            f.getToolbar().addMaterialCommandToSideMenu("Spread the Word!", FontImage.MATERIAL_SHARE, e -> {
                Display.getInstance().share("Check out the kitchen sink app from Codename One: " + getAppstoreURL(), null, null);
            });
        }
        f.getToolbar().addMaterialCommandToSideMenu("About",
                FontImage.MATERIAL_INFO, e -> {
                    Dialog.show("About", "KitchenSink provides an overview of the core Codename One capaiblities. "
                            + "Codename One allows Java developers to create native mobile applications that work everywhere!", "OK", null);
                });

        f.getToolbar().setVisible(false);
        cnt.setVisible(false);
        for (Component c : cnt) {
            c.setVisible(false);
        }
        f.addShowListener(e -> {
            f.getToolbar().setHeight(0);
            f.getToolbar().setVisible(true);
            f.animateLayoutFadeAndWait(200, 100);
            for (Component c : cnt) {
                c.setY(f.getHeight());
                c.setVisible(true);
                c.getUnselectedStyle().setOpacity(100);
            }
            cnt.setVisible(true);
            cnt.animateLayoutFadeAndWait(400, 100);
            f.removeAllShowListeners();
        });
        f.setTransitionInAnimator(CommonTransitions.createEmpty());
        f.show();
    }

    public  void showMainProduit() {
        final Form f = new Form("Produits", new BorderLayout());

        List<DemoProd> demos = new ArrayList<>();

        ServiceProduit serviceBus = new ServiceProduit();
        ArrayList<Produit> listeB = serviceBus.getList2();

        System.out.println(listeB.toString());

        for (Produit bp : listeB) {

            demos.add(new Prod(bp));
        }

        for (DemoProd d : demos) {
            d.init(res);
        }

        Image dukeImage = Image.createImage(circleMaskWidth, circleMaskHeight, 0);
        Graphics g = dukeImage.getGraphics();
        g.drawImage(res.getImage("codenameone-icon-background.png"), 0, 0, circleMaskWidth, circleMaskHeight);
        g.drawImage(res.getImage("codenameone-icon-foreground.png"), 0, 0, circleMaskWidth, circleMaskHeight);
        dukeImage = dukeImage.applyMask(circleMask);
        Label duke = new Label(dukeImage);
        Label circle = new Label(res.getImage("circle-line.png"));
        Container dukeImageContainer = LayeredLayout.encloseIn(duke, circle);
        Label name = new Label("Duke");
        name.setUIID("DukeName");
        Container dukeContainer = BorderLayout.west(BoxLayout.encloseY(dukeImageContainer, name));
        dukeContainer.setUIID("ProfileContainer");

//        if(isTablet()) {
//            Toolbar.setPermanentSideMenu(true);
//            f.getToolbar().addComponentToSideMenu(dukeContainer);
//            //select bonplan
//            
//            for(Demobus d : demos) {
//                f.getToolbar().addComponentToSideMenu(createDemoButtonBus(d,d.getBp()));
//            }
//            tabletSurface = f.getContentPane();
//            f.add(CENTER, demos.get(0).createDemo(f,demos.get(0).getBp()));
//            f.show();
//            return;
//        }
        f.getToolbar().addComponentToSideMenu(dukeContainer);

        Container cnt;
        if (Preferences.get("gridLayout", true)) {
            GridLayout gl = new GridLayout(1);
            gl.setAutoFit(true);
            gl.setHideZeroSized(true);
            cnt = new Container(gl);
            for (DemoProd d : demos) {
                cnt.add(createDemoButtonProd(d, d.getBp()));
            }
        } else {
            cnt = new Container(BoxLayout.y());
            for (DemoProd d : demos) {
                cnt.add(createDemoButtonProd(d, d.getBp()));
            }
        }
        cnt.setScrollableY(true);
        f.add(BorderLayout.CENTER, cnt);

        f.getToolbar().addSearchCommand(e -> {
            String t = (String) e.getSource();
            if (t == null) {
                t = "";
            } else {
                t = t.toLowerCase();
            }
            for (Component c : cnt) {
                DemoComponent mb = (DemoComponent) c;
                boolean show = t.length() == 0 || mb.getText().toLowerCase().indexOf(t) > -1;
                mb.setVisible(show);
                mb.setHidden(!show);
            }
            cnt.animateLayout(200);
        }, 3);

        gridCommand = f.getToolbar().addMaterialCommandToRightBar("", FontImage.MATERIAL_VIEW_COMFY, 4, e -> {
            if (cnt.getAnimationManager().isAnimating()) {
                return;
            }
            if (!(cnt.getLayout() instanceof GridLayout)) {
                f.removeCommand(gridCommand);
                f.getToolbar().addCommandToRightBar(listCommand);
                f.getToolbar().layoutContainer();
                Preferences.set("gridLayout", true);
                ComponentAnimation[] arr = new ComponentAnimation[cnt.getComponentCount() + 1];
                int offset = 0;
                for (Component c : cnt) {
                    DemoComponent mb = (DemoComponent) c;
                    arr[offset] = mb.lineToGridStage1();
                    offset++;
                }
                arr[offset] = cnt.createAnimateHierarchy(1000);

                cnt.getAnimationManager().addAnimationAndBlock(ComponentAnimation.compoundAnimation(arr));
                cnt.getParent().revalidate();

                ComponentAnimation[] arr2 = new ComponentAnimation[cnt.getComponentCount()];
                offset = 0;
                for (Component c : cnt) {
                    DemoComponent mb = (DemoComponent) c;
                    arr2[offset] = mb.lineToGridStage2();
                    offset++;
                }

                cnt.getAnimationManager().addAnimationAndBlock(ComponentAnimation.compoundAnimation(arr2));

                GridLayout gl = new GridLayout(1);
                gl.setAutoFit(true);
                gl.setHideZeroSized(true);
                cnt.setLayout(gl);

                cnt.animateLayout(300);

            }
        });

        listCommand = f.getToolbar().addMaterialCommandToRightBar("", FontImage.MATERIAL_FORMAT_LIST_BULLETED, 4, e -> {
            if (!(cnt.getLayout() instanceof BoxLayout)) {
                f.removeCommand(listCommand);
                f.getToolbar().addCommandToRightBar(gridCommand);
                f.getToolbar().layoutContainer();
                Preferences.set("gridLayout", false);
                ComponentAnimation[] arr = new ComponentAnimation[cnt.getComponentCount()];
                int offset = 0;
                for (Component c : cnt) {
                    DemoComponent mb = (DemoComponent) c;
                    arr[offset] = mb.gridToLineStage1();
                    offset++;
                }
                cnt.getAnimationManager().addAnimationAndBlock(ComponentAnimation.compoundAnimation(arr));

                cnt.setLayout(BoxLayout.y());

                arr = new ComponentAnimation[cnt.getComponentCount() + 1];

                offset = 0;
                for (Component c : cnt) {
                    DemoComponent mb = (DemoComponent) c;
                    arr[offset] = mb.gridToLineStage2();
                    offset++;
                }
                arr[offset] = cnt.createAnimateHierarchy(500);
                cnt.getAnimationManager().addAnimationAndBlock(ComponentAnimation.compoundAnimation(arr));

                cnt.getParent().revalidate();
            }
        });

        if (cnt.getLayout() instanceof GridLayout) {
            f.removeCommand(gridCommand);
        } else {
            f.removeCommand(listCommand);
        }

        f.getToolbar().addMaterialCommandToSideMenu("CodenameOne",
                FontImage.MATERIAL_WEB, e -> Display.getInstance().execute("https://www.codenameone.com/"));

        f.getToolbar().addMaterialCommandToSideMenu("Acceuil", FontImage.MATERIAL_MAP, e -> showMainUI());
        f.getToolbar().addMaterialCommandToSideMenu("Evenements", FontImage.MATERIAL_BUSINESS, e -> showMainEvent());
        f.getToolbar().addMaterialCommandToSideMenu("Mes evenements", FontImage.MATERIAL_SIM_CARD, e -> showMainEventMine());
        f.getToolbar().addMaterialCommandToSideMenu("Produits", FontImage.MATERIAL_TRENDING_DOWN, e -> showMainProduit());
        f.getToolbar().addMaterialCommandToSideMenu("Mes produits", FontImage.MATERIAL_BUSINESS, e -> showMainProduitMine());
        f.getToolbar().addMaterialCommandToSideMenu("Locaux", FontImage.MATERIAL_BOOK, e -> showMainLocal());
        f.getToolbar().addMaterialCommandToSideMenu("Mes locaux", FontImage.MATERIAL_BUSINESS, e -> showMainLocalMine());
        f.getToolbar().addMaterialCommandToSideMenu("Panier", FontImage.MATERIAL_MONEY_OFF, e -> showMainPanier());
        if (Display.getInstance().isNativeShareSupported() && getAppstoreURL() != null) {
            f.getToolbar().addMaterialCommandToSideMenu("Spread the Word!", FontImage.MATERIAL_SHARE, e -> {
                Display.getInstance().share("Check out the kitchen sink app from Codename One: " + getAppstoreURL(), null, null);
            });
        }
        f.getToolbar().addMaterialCommandToSideMenu("About",
                FontImage.MATERIAL_INFO, e -> {
                    Dialog.show("About", "KitchenSink provides an overview of the core Codename One capaiblities. "
                            + "Codename One allows Java developers to create native mobile applications that work everywhere!", "OK", null);
                });

        f.getToolbar().setVisible(false);
        cnt.setVisible(false);
        for (Component c : cnt) {
            c.setVisible(false);
        }
        f.addShowListener(e -> {
            f.getToolbar().setHeight(0);
            f.getToolbar().setVisible(true);
            f.animateLayoutFadeAndWait(200, 100);
            for (Component c : cnt) {
                c.setY(f.getHeight());
                c.setVisible(true);
                c.getUnselectedStyle().setOpacity(100);
            }
            cnt.setVisible(true);
            cnt.animateLayoutFadeAndWait(400, 100);
            f.removeAllShowListeners();
        });
        f.setTransitionInAnimator(CommonTransitions.createEmpty());
        f.show();
    }

    private void showMainProduitMine() {
        final Form f = new Form("Mes produits", new BorderLayout());

        List<DemoProd> demos = new ArrayList<>();

        ServiceProduit serviceBus = new ServiceProduit();
        ArrayList<Produit> listeB = serviceBus.getListMine();

        System.out.println(listeB.toString());

        for (Produit bp : listeB) {

            demos.add(new Prod(bp));
        }

        for (DemoProd d : demos) {
            d.init(res);
        }

        Image dukeImage = Image.createImage(circleMaskWidth, circleMaskHeight, 0);
        Graphics g = dukeImage.getGraphics();
        g.drawImage(res.getImage("codenameone-icon-background.png"), 0, 0, circleMaskWidth, circleMaskHeight);
        g.drawImage(res.getImage("codenameone-icon-foreground.png"), 0, 0, circleMaskWidth, circleMaskHeight);
        dukeImage = dukeImage.applyMask(circleMask);
        Label duke = new Label(dukeImage);
        Label circle = new Label(res.getImage("circle-line.png"));
        Container dukeImageContainer = LayeredLayout.encloseIn(duke, circle);
        Label name = new Label("Duke");
        name.setUIID("DukeName");
        Container dukeContainer = BorderLayout.west(BoxLayout.encloseY(dukeImageContainer, name));
        dukeContainer.setUIID("ProfileContainer");

//        if(Display.getInstance().isTablet()) {
//            Toolbar.setPermanentSideMenu(true);
//            f.getToolbar().addComponentToSideMenu(dukeContainer);
//            //select bonplan
//            
//            for(Demobus d : demos) {
//                f.getToolbar().addComponentToSideMenu(createDemoButtonBusMine(d,d.getBp()));
//            }
//            tabletSurface = f.getContentPane();
//            f.add(Display.getInstance().CENTER, demos.get(0).createDemoMine(f,demos.get(0).getBp()));
//            f.show();
//            return;
//        }
        f.getToolbar().addComponentToSideMenu(dukeContainer);

        Container cnt;
        if (Preferences.get("gridLayout", true)) {
            GridLayout gl = new GridLayout(1);
            gl.setAutoFit(true);
            gl.setHideZeroSized(true);
            cnt = new Container(gl);
            for (DemoProd d : demos) {
                cnt.add(createDemoButtonProdMine(d, d.getBp()));
            }
        } else {
            cnt = new Container(BoxLayout.y());
            for (DemoProd d : demos) {
                cnt.add(createDemoButtonProdMine(d, d.getBp()));
            }
        }
        cnt.setScrollableY(true);
        f.add(BorderLayout.CENTER, cnt);

        f.getToolbar().addSearchCommand(e -> {
            String t = (String) e.getSource();
            if (t == null) {
                t = "";
            } else {
                t = t.toLowerCase();
            }
            for (Component c : cnt) {
                DemoComponent mb = (DemoComponent) c;
                boolean show = t.length() == 0 || mb.getText().toLowerCase().indexOf(t) > -1;
                mb.setVisible(show);
                mb.setHidden(!show);
            }
            cnt.animateLayout(200);
        }, 3);

        gridCommand = f.getToolbar().addMaterialCommandToRightBar("", FontImage.MATERIAL_VIEW_COMFY, 4, e -> {
            if (cnt.getAnimationManager().isAnimating()) {
                return;
            }
            if (!(cnt.getLayout() instanceof GridLayout)) {
                f.removeCommand(gridCommand);
                f.getToolbar().addCommandToRightBar(listCommand);
                f.getToolbar().layoutContainer();
                Preferences.set("gridLayout", true);
                ComponentAnimation[] arr = new ComponentAnimation[cnt.getComponentCount() + 1];
                int offset = 0;
                for (Component c : cnt) {
                    DemoComponent mb = (DemoComponent) c;
                    arr[offset] = mb.lineToGridStage1();
                    offset++;
                }
                arr[offset] = cnt.createAnimateHierarchy(1000);

                cnt.getAnimationManager().addAnimationAndBlock(ComponentAnimation.compoundAnimation(arr));
                cnt.getParent().revalidate();

                ComponentAnimation[] arr2 = new ComponentAnimation[cnt.getComponentCount()];
                offset = 0;
                for (Component c : cnt) {
                    DemoComponent mb = (DemoComponent) c;
                    arr2[offset] = mb.lineToGridStage2();
                    offset++;
                }

                cnt.getAnimationManager().addAnimationAndBlock(ComponentAnimation.compoundAnimation(arr2));

                GridLayout gl = new GridLayout(1);
                gl.setAutoFit(true);
                gl.setHideZeroSized(true);
                cnt.setLayout(gl);

                cnt.animateLayout(300);

            }
        });

        listCommand = f.getToolbar().addMaterialCommandToRightBar("", FontImage.MATERIAL_FORMAT_LIST_BULLETED, 4, e -> {
            if (!(cnt.getLayout() instanceof BoxLayout)) {
                f.removeCommand(listCommand);
                f.getToolbar().addCommandToRightBar(gridCommand);
                f.getToolbar().layoutContainer();
                Preferences.set("gridLayout", false);
                ComponentAnimation[] arr = new ComponentAnimation[cnt.getComponentCount()];
                int offset = 0;
                for (Component c : cnt) {
                    DemoComponent mb = (DemoComponent) c;
                    arr[offset] = mb.gridToLineStage1();
                    offset++;
                }
                cnt.getAnimationManager().addAnimationAndBlock(ComponentAnimation.compoundAnimation(arr));

                cnt.setLayout(BoxLayout.y());

                arr = new ComponentAnimation[cnt.getComponentCount() + 1];

                offset = 0;
                for (Component c : cnt) {
                    DemoComponent mb = (DemoComponent) c;
                    arr[offset] = mb.gridToLineStage2();
                    offset++;
                }
                arr[offset] = cnt.createAnimateHierarchy(500);
                cnt.getAnimationManager().addAnimationAndBlock(ComponentAnimation.compoundAnimation(arr));

                cnt.getParent().revalidate();
            }
        });

        if (cnt.getLayout() instanceof GridLayout) {
            f.removeCommand(gridCommand);
        } else {
            f.removeCommand(listCommand);
        }

        f.getToolbar().addMaterialCommandToSideMenu("CodenameOne",
                FontImage.MATERIAL_WEB, e -> Display.getInstance().execute("https://www.codenameone.com/"));

        f.getToolbar().addMaterialCommandToSideMenu("Acceuil", FontImage.MATERIAL_MAP, e -> showMainUI());
        f.getToolbar().addMaterialCommandToSideMenu("Evenements", FontImage.MATERIAL_BUSINESS, e -> showMainEvent());
        f.getToolbar().addMaterialCommandToSideMenu("Mes evenements", FontImage.MATERIAL_SIM_CARD, e -> showMainEventMine());
        f.getToolbar().addMaterialCommandToSideMenu("Produits", FontImage.MATERIAL_TRENDING_DOWN, e -> showMainProduit());
        f.getToolbar().addMaterialCommandToSideMenu("Mes produits", FontImage.MATERIAL_BUSINESS, e -> showMainProduitMine());
        f.getToolbar().addMaterialCommandToSideMenu("Locaux", FontImage.MATERIAL_BOOK, e -> showMainLocal());
        f.getToolbar().addMaterialCommandToSideMenu("Mes locaux", FontImage.MATERIAL_BUSINESS, e -> showMainLocalMine());
        f.getToolbar().addMaterialCommandToSideMenu("Panier", FontImage.MATERIAL_MONEY_OFF, e -> showMainPanier());
        if (Display.getInstance().isNativeShareSupported() && getAppstoreURL() != null) {
            f.getToolbar().addMaterialCommandToSideMenu("Spread the Word!", FontImage.MATERIAL_SHARE, e -> {
                Display.getInstance().share("Check out the kitchen sink app from Codename One: " + getAppstoreURL(), null, null);
            });
        }
        f.getToolbar().addMaterialCommandToSideMenu("About",
                FontImage.MATERIAL_INFO, e -> {
                    Dialog.show("About", "KitchenSink provides an overview of the core Codename One capaiblities. "
                            + "Codename One allows Java developers to create native mobile applications that work everywhere!", "OK", null);
                });

        f.getToolbar().setVisible(false);
        cnt.setVisible(false);
        for (Component c : cnt) {
            c.setVisible(false);
        }
        f.addShowListener(e -> {
            f.getToolbar().setHeight(0);
            f.getToolbar().setVisible(true);
            f.animateLayoutFadeAndWait(200, 100);
            for (Component c : cnt) {
                c.setY(f.getHeight());
                c.setVisible(true);
                c.getUnselectedStyle().setOpacity(100);
            }
            cnt.setVisible(true);
            cnt.animateLayoutFadeAndWait(400, 100);
            f.removeAllShowListeners();
        });
        f.setTransitionInAnimator(CommonTransitions.createEmpty());
        f.show();
    }

    private void showMainLocal() {
    }

    private void showMainLocalMine() {
    }

    private void showMainPanier() {
    }

    public String getAppstoreURL() {
        if (Display.getInstance().getPlatformName().equals("ios")) {
            return "https://itunes.apple.com/us/app/kitchen-sink-codename-one/id635048865";
        }
        if (Display.getInstance().getPlatformName().equals("and")) {
            return "https://play.google.com/store/apps/details?id=com.codename1.demos.kitchen";
        }
        return null;
    }

    public void stop() {
        currentForm = Display.getInstance().getCurrent();
        RatingWidget.suspendRating();
    }

    public void destroy() {
    }
}
