/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.jmarijano.web.zrna;

import javax.inject.Named;
import java.io.Serializable;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;
import org.foi.nwtis.jmarijano.modeli.App1SOAPKlijent;
import org.foi.nwtis.jmarijano.sb.StatefulSBApp2_1;
import org.foi.nwtis.jmarijano.web.servisi.ws.MeteoPodaci;

/**
 *
 * @author LiterallyCan't
 */
@Named(value = "pregledMeteoroloskihPodataka")
@SessionScoped
public class PregledMeteoroloskihPodataka implements Serializable {

    @EJB
    private StatefulSBApp2_1 statefulSB;

    private String odabraniAerodrom;
    private String poruka;
    private MeteoPodaci meteoPodaci;
    private HttpSession sesija;

    /**
     * Creates a new instance of PregledMeteoroloskihPodataka
     */
    public PregledMeteoroloskihPodataka() {
        meteoPodaci = new MeteoPodaci();
    }

    public String getOdabraniAerodrom() {
        return odabraniAerodrom;
    }

    public void setOdabraniAerodrom(String odabraniAerodrom) {
        this.odabraniAerodrom = odabraniAerodrom;
    }

    public String prikaziMeteoroloskePodatke() {
        try {
            sesija = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
            if (statefulSB.autenticiraj(sesija.getAttribute("korisnik").toString(), sesija.getAttribute("lozinka").toString())) {
                meteoPodaci = App1SOAPKlijent.dajMeteoPodatkeAerodroma(sesija.getAttribute("korisnik").toString(), sesija.getAttribute("lozinka").toString(), odabraniAerodrom);
                poruka = "Uspješno preuzeti podaci";
            } else {
                poruka = "Potrebna je prijava";
            }
        } catch (Exception e) {
            e.printStackTrace();
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

    public MeteoPodaci getMeteoPodaci() {
        return meteoPodaci;
    }

    public void setMeteoPodaci(MeteoPodaci meteoPodaci) {
        this.meteoPodaci = meteoPodaci;
    }

}
