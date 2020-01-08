/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.jmarijano.web.zrna;

import com.google.gson.Gson;
import javax.inject.Named;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;
import org.foi.nwtis.jmarijano.modeli.Aerodrom;
import org.foi.nwtis.jmarijano.sb.StatefulSBApp2_1;
import org.foi.nwtis.jmarijano.web.rs.klijent.RESTServiceApp1_JerseyClient;
import org.primefaces.json.JSONArray;
import org.primefaces.json.JSONException;
import org.primefaces.json.JSONObject;
import java.lang.reflect.Type;
import com.google.gson.reflect.TypeToken;
import org.foi.nwtis.jmarijano.slusaci.SlusacAplikacije;

/**
 *
 * @author LiterallyCan't
 */
@Named(value = "pregledAerodroma")
@RequestScoped
public class PregledAerodroma implements Serializable {

    @EJB
    private StatefulSBApp2_1 statefulSB;
    private List<Aerodrom> listaAerodroma;
    private String poruka;
    private final RESTServiceApp1_JerseyClient rESTServiceApp1_JerseyClient;
    private final Gson gson;
    private int brojRedaka;

    public int getBrojRedaka() {
        try {
            brojRedaka = Integer.parseInt(SlusacAplikacije.getKonfiguracija().dajPostavku("aerodromi.brojLinija"));
        } catch (Exception e) {
            e.printStackTrace();
            brojRedaka = 4;
        }
        return brojRedaka;
    }

    public void setBrojRedaka(int brojRedaka) {
        this.brojRedaka = brojRedaka;
    }

    /**
     * Creates a new instance of PregledAerodroma
     */
    public PregledAerodroma() {
        listaAerodroma = new ArrayList<>();
        rESTServiceApp1_JerseyClient = new RESTServiceApp1_JerseyClient();
        gson = new Gson();
    }

    public List<Aerodrom> getListaAerodroma() {
        return listaAerodroma;
    }

    public void setListaAerodroma(List<Aerodrom> listaAerodroma) {
        this.listaAerodroma = listaAerodroma;
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
            if (statefulSB.autenticiraj(korisnickoIme, lozinka)) {
                String jsonPoruka = rESTServiceApp1_JerseyClient.dajSveAerodrome(lozinka, korisnickoIme);
                JSONObject jSONObject = new JSONObject(jsonPoruka);
                JSONArray values = jSONObject.getJSONArray("odgovor");
                Type listType = new TypeToken<ArrayList<Aerodrom>>() {
                }.getType();
                listaAerodroma = gson.fromJson(values.toString(), listType);
            }
        } catch (NullPointerException e) {
            poruka = "Potrebna prijava";
        } catch (JSONException ex) {
            poruka = "Dodogila se pogreška kod parsiranja JSON objekta";
            Logger.getLogger(PregledAerodroma.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception e) {
            poruka = "Dogodila se greška";
        }

    }

    public String obrisi(String ident) {
        return "";
    }

    public String azuriraj() {
        return "";
    }
}
