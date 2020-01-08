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
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import org.foi.nwtis.jmarijano.poruke.PorukaPosluzitelj;
import org.foi.nwtis.jmarijano.sb.SingletonSBApp3_1;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;
import org.foi.nwtis.jmarijano.sb.StatefulSBApp3_1;

/**
 *
 * @author LiterallyCan't
 */
@Named(value = "pregledJMSPorukaPosluzitelj")
@SessionScoped
public class PregledJMSPorukaPosluzitelj implements Serializable {

    @EJB
    private StatefulSBApp3_1 statefulSBApp3_1;
    @EJB
    private SingletonSBApp3_1 singletonSB;
    private List<PorukaPosluzitelj> listaPoruka;
    private String poruka;

    /**
     * Creates a new instance of PregledJMSPorukaRedCekanja
     */
    public PregledJMSPorukaPosluzitelj() {
        listaPoruka = new ArrayList<>();
    }

    public List<PorukaPosluzitelj> getListaPoruka() {
        return listaPoruka;
    }

    public void setListaPoruka(List<PorukaPosluzitelj> listaPoruka) {
        this.listaPoruka = listaPoruka;
    }

    public String obrisi(PorukaPosluzitelj jmsPoruka) {
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

    @PostConstruct
    public void init() {
        try {
            HttpSession sesija = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);
            String korisnickoIme = sesija.getAttribute("korisnik").toString();
            String lozinka = sesija.getAttribute("lozinka").toString();
            if (statefulSBApp3_1.autenticiraj(korisnickoIme, lozinka)) {
                listaPoruka = singletonSB.getListaPosluzitelj();
            }
        } catch (NullPointerException e) {
            poruka = "Potrebna prijava";
        } catch (Exception e) {
            poruka = "Dogodila se greška";
        }

    }
}
