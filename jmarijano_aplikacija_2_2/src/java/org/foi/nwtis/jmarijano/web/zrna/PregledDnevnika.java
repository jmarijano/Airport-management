/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.jmarijano.web.zrna;

import javax.inject.Named;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;
import org.foi.nwtis.jmarijano.eb.Dnevnik;
import org.foi.nwtis.jmarijano.sb.DnevnikFacade;
import org.foi.nwtis.jmarijano.sb.StatefulSBApp2_1;
import org.foi.nwtis.jmarijano.slusaci.SlusacAplikacije;

/**
 *
 * @author LiterallyCan't
 */
@Named(value = "pregledDnevnika")
@RequestScoped
public class PregledDnevnika implements Serializable {

    @EJB
    private StatefulSBApp2_1 statefulSB;
    @EJB
    private DnevnikFacade dnevnikFacade;
    private List<Dnevnik> listaDnevnika;
    private int brojLinija;

    /**
     * Creates a new instance of PregledDnevnika
     */
    public PregledDnevnika() {
        listaDnevnika = new ArrayList<>();
    }

    public List<Dnevnik> getListaDnevnika() {
        try {
            HttpSession sesija = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
            String korisnickoIme = sesija.getAttribute("korisnik").toString();
            String lozinka = sesija.getAttribute("lozinka").toString();
            if (statefulSB.autenticiraj(korisnickoIme, lozinka)) {
                listaDnevnika = dnevnikFacade.findAll();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listaDnevnika;
    }

    public void setListaDnevnika(List<Dnevnik> listaDnevnika) {
        this.listaDnevnika = listaDnevnika;
    }

    public int getBrojLinija() {
        try {
            brojLinija = Integer.parseInt(SlusacAplikacije.getKonfiguracija().dajPostavku("dnevnikRada.brojLinija"));
        } catch (Exception e) {
            e.printStackTrace();
            brojLinija = 5;
        }
        return brojLinija;
    }

    public void setBrojLinija(int brojLinija) {
        this.brojLinija = brojLinija;
    }

}
