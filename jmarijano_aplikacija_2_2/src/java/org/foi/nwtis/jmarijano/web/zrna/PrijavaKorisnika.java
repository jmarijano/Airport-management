/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.jmarijano.web.zrna;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;
import org.foi.nwtis.jmarijano.dretve.MQTTSlusac;
import org.foi.nwtis.jmarijano.modeli.Korisnik;
import org.foi.nwtis.jmarijano.sb.StatefulSBApp2_1;
import org.foi.nwtis.jmarijano.slusaci.SlusacAplikacije;
import org.foi.nwtis.jmarijano.web.rs.klijent.AIRP2REST_JerseyClient;
import org.primefaces.json.JSONArray;
import org.primefaces.json.JSONObject;

/**
 *
 * @author LiterallyCan't
 */
@Named(value = "prijavaKorisnika")
@SessionScoped
public class PrijavaKorisnika implements Serializable {

    @EJB
    private StatefulSBApp2_1 statefulSB;
    private String korisnickoIme;
    private String lozinka;
    private String poruka;
    private List<Korisnik> listaKorisnika;
    private final AIRP2REST_JerseyClient aIRP2REST_JerseyClient;
    private MQTTSlusac mQTTSlusac;
    private int brojRedaka;
    private Gson gson;

    /**
     * Creates a new instance of PrijavaKorisnika
     */
    public PrijavaKorisnika() {
        aIRP2REST_JerseyClient = new AIRP2REST_JerseyClient();
        listaKorisnika = new ArrayList<>();
        gson = new Gson();
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

    public List<Korisnik> getListaKorisnika() {
        return listaKorisnika;
    }

    public void setListaKorisnika(List<Korisnik> listaKorisnika) {
        this.listaKorisnika = listaKorisnika;
    }

    public String prijava() {
        if (statefulSB.autenticiraj(korisnickoIme, lozinka)) {
            poruka = "Uspješno prijavljen korisnik";
            HttpSession sesija = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);
            sesija.setAttribute("korisnik", korisnickoIme);
            sesija.setAttribute("lozinka", lozinka);
            try {
                String jsonPoruka = aIRP2REST_JerseyClient.getJson(sesija.getAttribute("lozinka").toString(), sesija.getAttribute("korisnik").toString());
                JSONObject jSONObject = new JSONObject(jsonPoruka);
                JSONArray values = jSONObject.getJSONArray("odgovor");
                Type listType = new TypeToken<ArrayList<Korisnik>>() {
                }.getType();
                listaKorisnika = gson.fromJson(values.toString(), listType);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {

            poruka = "Ne postoji korisnik s dobivenim korisničkim imenom i lozinkom";
        }

        return "";
    }

    public int getBrojRedaka() {
        try {
            brojRedaka = Integer.parseInt(SlusacAplikacije.getKonfiguracija().dajPostavku("korisnici.brojLinija"));
        } catch (Exception e) {
            e.printStackTrace();
            brojRedaka = 10;
        }

        return brojRedaka;
    }

    public void setBrojRedaka(int brojRedaka) {
        this.brojRedaka = brojRedaka;
    }

}
