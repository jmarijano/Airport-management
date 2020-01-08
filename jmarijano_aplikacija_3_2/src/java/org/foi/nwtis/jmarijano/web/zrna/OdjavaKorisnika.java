/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.jmarijano.web.zrna;

import java.io.Serializable;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

/**
 *
 * @author LiterallyCan't
 */
@Named(value = "odjavaKorisnika")
@SessionScoped
public class OdjavaKorisnika implements Serializable {

    private String poruka;

    /**
     * Creates a new instance of OdjavaKorisnika
     */
    public OdjavaKorisnika() {
    }

    public String odjavi() {
        try {
            HttpSession sesija = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
            if (sesija == null) {
                poruka = "Nema korisnika u sesiji";
            } else {
                sesija.invalidate();
                poruka = "Odjavljen korisnik";

            }
        } catch (Exception e) {
            e.printStackTrace();
            poruka = "Dogodila se greška prilikom odjave";
        }
        return "faces/index.xhtml?faces-redirect=true";
    }

    public String getPoruka() {
        return poruka;
    }

    public void setPoruka(String poruka) {
        this.poruka = poruka;
    }
}
