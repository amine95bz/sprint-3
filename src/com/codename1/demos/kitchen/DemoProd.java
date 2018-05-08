/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.codename1.demos.kitchen;

import com.codename1.ui.Container;
import com.codename1.ui.Form;
import com.codename1.ui.Image;
import com.codename1.ui.util.Resources;
import entities.Produit;

/**
 *
 * @author CHAIMA
 */
public abstract class DemoProd {
    
     public DemoProd(Produit bp) {
        this.bp = bp;
    }

    public Produit getBp() {
        return bp;
    }
    
    
    
    private Produit bp;
    private Resources res;
    public void init(Resources res) {
        this.res = res;
    }
    
    protected Resources getResources() {
        return res;
    }
    
    public abstract String getDisplayName(Produit bp);
    public abstract Image getDemoIcon(Produit bp);

    /**
     *
     * @param bp
     * @return
     */
    public Container createDemoProdMine(Produit bp){
    throw new RuntimeException("Must override createDemo");
    }
    /**
     *
     * @param bp
     * @return
     */
    public Container createDemoProd(Produit bp) {
        throw new RuntimeException("Must override createDemo");
    }
    
    public String getDescription(Produit bp) {
        return "";
    }
    
    public String getSourceCodeURL(Produit bp) {
        return "https://www.codenameone.com/";
    }
    
    public Container createDemoProd(Form parentForm, Produit bp) {
        return createDemoProd(bp);
    }
    public Container createDemoProdMine(Form parentForm, Produit bp) {
        return createDemoProdMine(bp);
    }

    boolean onBack() {
        return true;
    }
    
}
