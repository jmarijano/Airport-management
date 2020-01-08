/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.jmarijano.web.zrna;

import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import javax.ejb.EJB;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;
import org.foi.nwtis.jmarijano.modeli.App1SOAPKlijent;
import org.foi.nwtis.jmarijano.sb.StatefulSBApp2_1;
import org.foi.nwtis.jmarijano.slusaci.SlusacAplikacije;

/**
 *
 * @author LiterallyCan't
 */
@Named(value = "pregledUdaljenostiAerodroma")
@SessionScoped
public class PregledUdaljenostiAerodroma implements Serializable {

    @EJB
    private StatefulSBApp2_1 statefulSB;
    private String poruka;
    private String identPrvi;
    private String identDrugi;
    private double udaljenost;
    private int brojRedakaPrvi;

    public int getBrojRedakaPrvi() {
        try {
            brojRedakaPrvi = Integer.parseInt(SlusacAplikacije.getKonfiguracija().dajPostavku("udaljenost.prviComboBox"));
        } catch (Exception e) {
            e.printStackTrace();
            brojRedakaPrvi = 7;
        }
        return brojRedakaPrvi;
    }

    public void setBrojRedakaPrvi(int brojRedakaPrvi) {
        this.brojRedakaPrvi = brojRedakaPrvi;
    }

    public int getBrojRedakaDrugi() {
        try {

            brojRedakaDrugi = Integer.parseInt(SlusacAplikacije.getKonfiguracija().dajPostavku("udaljenost.drugiComboBox"));
        } catch (Exception e) {
            e.printStackTrace();
            brojRedakaDrugi = 7;
        }
        return brojRedakaDrugi;
    }

    public void setBrojRedakaDrugi(int brojRedakaDrugi) {
        this.brojRedakaDrugi = brojRedakaDrugi;
    }
    private int brojRedakaDrugi;

    /**
     * Creates a new instance of PregledUdaljenostiAerodroma
     */
    public PregledUdaljenostiAerodroma() {
    }

    public String getPoruka() {
        return poruka;
    }

    public void setPoruka(String poruka) {
        this.poruka = poruka;
    }

    public String izracunaj(String ident1, String ident2) {
        try {
            HttpSession sesija = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
            String korisnickoIme = sesija.getAttribute("korisnik").toString();
            String lozinka = sesija.getAttribute("lozinka").toString();
            if (statefulSB.autenticiraj(korisnickoIme, lozinka)) {
                udaljenost = App1SOAPKlijent.udaljenostIzmeduAerodroma(korisnickoIme, lozinka, identPrvi, identDrugi);
            }
        } catch (NullPointerException e) {
            poruka = "Potrebna prijava";
        } catch (Exception e) {
            poruka = "Dogodila se greška";
        }
        return "";
    }

    public String getIdentPrvi() {
        return identPrvi;
    }

    public void setIdentPrvi(String identPrvi) {
        this.identPrvi = identPrvi;
    }

    public String getIdentDrugi() {
        return identDrugi;
    }

    public void setIdentDrugi(String identDrugi) {
        this.identDrugi = identDrugi;
    }

    public double getUdaljenost() {
        return udaljenost;
    }

    public void setUdaljenost(double udaljenost) {
        this.udaljenost = udaljenost;
    }
}
