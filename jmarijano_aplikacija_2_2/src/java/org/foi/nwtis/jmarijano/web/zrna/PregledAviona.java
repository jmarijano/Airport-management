/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.jmarijano.web.zrna;

import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;
import org.foi.nwtis.jmarijano.modeli.App1SOAPKlijent;
import org.foi.nwtis.jmarijano.modeli.AvionLetiConverter;
import org.foi.nwtis.jmarijano.sb.StatefulSBApp2_1;
import org.foi.nwtis.jmarijano.slusaci.SlusacAplikacije;
import org.foi.nwtis.rest.podaci.AvionLeti;

/**
 *
 * @author LiterallyCan't
 */
@Named(value = "pregledAviona")
@SessionScoped
public class PregledAviona implements Serializable {

    @EJB
    private StatefulSBApp2_1 statefulSB;
    private String odabraniAerodrom;
    private String odVremena;
    private String doVremena;
    private List<AvionLeti> listaAviona;
    private String poruka;
    private int zaOdVremena = 0;
    private int zaDoVremena = 0;
    private Date date;
    private SimpleDateFormat dateFormat;
    private int brojRedakaComboBox;
    private int brojRedakaTablica;

    /**
     * Creates a new instance of PregledAviona
     */
    public PregledAviona() {
        listaAviona = new ArrayList<>();
        date = new Date();
        dateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
    }

    public String getOdabraniAerodrom() {
        return odabraniAerodrom;
    }

    public void setOdabraniAerodrom(String odabraniAerodrom) {
        this.odabraniAerodrom = odabraniAerodrom;
    }

    public String getOdVremena() {
        return odVremena;
    }

    public void setOdVremena(String odVremena) {
        this.odVremena = odVremena;
    }

    public String getDoVremena() {
        return doVremena;
    }

    public void setDoVremena(String doVremena) {
        this.doVremena = doVremena;
    }

    public List<AvionLeti> getListaAviona() {
        return listaAviona;
    }

    public void setListaAviona(List<AvionLeti> listaAviona) {
        this.listaAviona = listaAviona;
    }

    public String getPoruka() {
        return poruka;
    }

    public void setPoruka(String poruka) {
        this.poruka = poruka;
    }

    public String preuzmiAvione() {
        try {
            HttpSession sesija = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
            String korisnickoIme = sesija.getAttribute("korisnik").toString();
            String lozinka = sesija.getAttribute("lozinka").toString();
            if (statefulSB.autenticiraj(korisnickoIme, lozinka)) {
                date = dateFormat.parse(odVremena);
                zaOdVremena = (int) (date.getTime() / 1000);
                date = dateFormat.parse(doVremena);
                zaDoVremena = (int) (date.getTime() / 1000);
                List<org.foi.nwtis.jmarijano.web.servisi.ws.AvionLeti> zaConvertati = App1SOAPKlijent.dajAvionePoletjeleSAerodroma(korisnickoIme, lozinka, odabraniAerodrom, zaOdVremena, zaDoVremena);
                listaAviona = AvionLetiConverter.convert(zaConvertati);
            } else {
                poruka = "Potrebna je prijava";
            }
        } catch (Exception e) {
            e.printStackTrace();
            poruka = "Dogodila se greška";
        }
        return "";
    }

    public String pretvoriVrijeme(int vrijeme) {
        Date date = new Date(vrijeme * 1000L);
        Format format = new SimpleDateFormat("dd-MM-YYYY hh:mm:ss");
        return format.format(date);
    }

    public int getBrojRedakaComboBox() {
        try {
            brojRedakaComboBox = Integer.parseInt(SlusacAplikacije.getKonfiguracija().dajPostavku("aerodromi.comboBox"));
        } catch (Exception e) {
            e.printStackTrace();
            brojRedakaComboBox = 5;
        }
        return brojRedakaComboBox;
    }

    public void setBrojRedakaComboBox(int brojRedakaComboBox) {
        this.brojRedakaComboBox = brojRedakaComboBox;
    }

    public int getBrojRedakaTablica() {
        try {
            brojRedakaTablica = Integer.parseInt(SlusacAplikacije.getKonfiguracija().dajPostavku("avioni.brojLinija"));
        } catch (Exception e) {
            e.printStackTrace();
            brojRedakaTablica = 5;
        }
        return brojRedakaTablica;
    }

    public void setBrojRedakaTablica(int brojRedakaTablica) {
        this.brojRedakaTablica = brojRedakaTablica;
    }
}
