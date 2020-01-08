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
import org.foi.nwtis.jmarijano.sb.StatefulSBApp2_1;
import org.foi.nwtis.jmarijano.web.rs.klijent.RESTServiceApp1_JerseyClient;
import org.primefaces.json.JSONObject;

/**
 *
 * @author LiterallyCan't
 */
@Named(value = "dodajAerodrom")
@SessionScoped
public class DodajAerodrom implements Serializable {

    @EJB
    private StatefulSBApp2_1 statefulSB;
    private String ident;
    private RESTServiceApp1_JerseyClient rESTServiceApp1_JerseyClient;
    private String poruka;

    /**
     * Creates a new instance of DodajAerodrom
     */
    public DodajAerodrom() {
        rESTServiceApp1_JerseyClient = new RESTServiceApp1_JerseyClient();
    }

    public String getIdent() {
        return ident;
    }

    public void setIdent(String ident) {
        this.ident = ident;
    }

    public String dodaj() {
        try {
            HttpSession sesija = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
            String korisnickoIme = sesija.getAttribute("korisnik").toString();
            String lozinka = sesija.getAttribute("lozinka").toString();
            if (statefulSB.autenticiraj(korisnickoIme, lozinka)) {
                JSONObject jSONObject = new JSONObject();
                jSONObject.put("icao", ident);
                poruka = rESTServiceApp1_JerseyClient.noviAerodrom(jSONObject.toString(), korisnickoIme, lozinka);
            } else {
                poruka = "Potrebno se prijaviti";
            }
        } catch (Exception e) {
            poruka = "Dogodila se pogreška";
        }
        return "";
    }

    public String getPoruka() {
        return poruka;
    }

    public void setPoruka(String poruka) {
        this.poruka = poruka;
    }

}
