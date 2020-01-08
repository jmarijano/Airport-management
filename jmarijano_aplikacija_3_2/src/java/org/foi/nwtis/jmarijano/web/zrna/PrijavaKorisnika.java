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
import org.foi.nwtis.jmarijano.sb.StatefulSBApp3_1;

/**
 *
 * @author LiterallyCan't
 */
@Named(value = "prijavaKorisnika")
@SessionScoped
public class PrijavaKorisnika implements Serializable {

    @EJB
    private StatefulSBApp3_1 statefulSB;
    private String korisnickoIme;
    private String lozinka;

    private String poruka;

    /**
     * Creates a new instance of PrijavaKorisnika
     */
    public PrijavaKorisnika() {

    }

    public String getKorisnickoIme() {
        return korisnickoIme;
    }

    public void setKorisnickoIme(String korisnickoIme) {
        this.korisnickoIme = korisnickoIme;
    }

    public String getLozinka() {
        return lozinka;
    }

    public void setLozinka(String lozinka) {
        this.lozinka = lozinka;
    }

    public String getPoruka() {
        return poruka;
    }

    public void setPoruka(String poruka) {
        this.poruka = poruka;
    }

    public String prijava() {
        if (statefulSB.autenticiraj(korisnickoIme, lozinka)) {
            HttpSession sesija = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);
            sesija.setAttribute("korisnik", korisnickoIme);
            sesija.setAttribute("lozinka", lozinka);
            poruka = "Korisnik prijavljen";
        } else {
            poruka = "Ne postoji korisnik s dobivenim korisničkim imenom i lozinkom";
        }
        return "";
    }

}
