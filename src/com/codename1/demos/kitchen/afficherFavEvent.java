/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.codename1.demos.kitchen;

import com.codename1.db.Cursor;
import com.codename1.db.Row;
import com.codename1.ui.EncodedImage;
import com.codename1.ui.Form;
import com.codename1.ui.Image;
import com.codename1.ui.URLImage;
import com.codename1.ui.util.Resources;
import java.io.IOException;

/**
 *
 * @author CHAIMA
 */
public class afficherFavEvent extends Form{
    
    public afficherFavEvent(Resources globalResources, Cursor cur) throws IOException{
        
        while (cur.next()) {
                Row row = cur.getRow();
                String url = "http://localhost/medina/web/EventsImages";
                Image placeholder = Image.createImage(120, 90);

                EncodedImage enc = EncodedImage.createFromImage(placeholder, false);
                URLImage urlim = URLImage.createToStorage(enc, row.getString(2), url + "/" + row.getString(2));
                //String username = serviceTopic.getUsername(d.getId());
                //addButton(urlim, row.getString(1), username,d);
                
            }


        
    }

    
}
