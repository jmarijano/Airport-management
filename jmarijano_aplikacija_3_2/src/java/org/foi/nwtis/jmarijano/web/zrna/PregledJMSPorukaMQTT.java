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
import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;
import org.foi.nwtis.jmarijano.poruke.PorukaMQTT;
import org.foi.nwtis.jmarijano.sb.SingletonSBApp3_1;
import org.foi.nwtis.jmarijano.sb.StatefulSBApp3_1;

/**
 *
 * @author LiterallyCan't
 */
@Named(value = "pregledJMSPorukaMQTT")
@SessionScoped
public class PregledJMSPorukaMQTT implements Serializable {

    @EJB
    private SingletonSBApp3_1 singletonSB;
    @EJB
    private StatefulSBApp3_1 statefulSBApp3_1;
    private List<PorukaMQTT> listaPoruka;
    private String poruka;

    /**
     * Creates a new instance of PregledJMSPorukaRedCekanja
     */
    public PregledJMSPorukaMQTT() {
        listaPoruka = new ArrayList<>();

    }

    public List<PorukaMQTT> getListaPoruka() {
        init();
        return listaPoruka;
    }

    public void setListaPoruka(List<PorukaMQTT> listaPoruka) {
        this.listaPoruka = listaPoruka;
    }

    public String obrisi(PorukaMQTT jmsPoruka) {
        if (poruka == null) {
            poruka = "Dogodila se greška kod slanja objekta za brisanje";
        }
        if (listaPoruka.remove(jmsPoruka)) {
            poruka = "Poruka uspješno obrisana";
        } else {
            poruka = "Dogodila se greška kod brisanja poruke";
        }
        return "";
    }

    public String getPoruka() {
        return poruka;
    }

    public void setPoruka(String poruka) {
        this.poruka = poruka;
    }

    public void init() {
        try {
            HttpSession sesija = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);
            String korisnickoIme = sesija.getAttribute("korisnik").toString();
            String lozinka = sesija.getAttribute("lozinka").toString();
            if (statefulSBApp3_1.autenticiraj(korisnickoIme, lozinka)) {
                listaPoruka = singletonSB.getListaMQTT();
            }
        } catch (NullPointerException e) {
            poruka = "Potrebna prijava";
        } catch (Exception e) {
            poruka = "Dogodila se greška";
        }

    }
}
