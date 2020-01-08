/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.jmarijano.web.zrna;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import org.foi.nwtis.jmarijano.modeli.Dnevnik;
import org.foi.nwtis.jmarijano.mysql.DnevnikMySQL;
import org.foi.nwtis.jmarijano.sucelja.DnevnikService;
import org.foi.nwtis.jmarijano.web.slusaci.SlusacAplikacije;

/**
 *
 * @author morbi_000
 */
@Named(value = "pregledDnevnikaRada")
@SessionScoped
public class PregledDnevnikaRada implements Serializable {

    private List<Dnevnik> listaDnevnik;
    private int ukupanBrojZapisa;
    private int stranica = 1;
    private int brojRedaka = 0;
    private final DnevnikService dnevnikService;
    private String poruka;
    private String odVremena = "";
    private String doVremena = "";
    private List<String> listaZahtjeva;
    private String odabraniRedak;
    private SimpleDateFormat dateFormat;
    private Date date;
    private int zaOdVremena = 0;
    private int zaDoVremena = 0;

    /**
     * Creates a new instance of PregledDnevnikaRada
     */
    public PregledDnevnikaRada() {
        listaDnevnik = new ArrayList<>();
        dnevnikService = new DnevnikMySQL(SlusacAplikacije.getSc());
        brojRedaka = Integer.parseInt(SlusacAplikacije.getKonfiguracija().dajPostavku("pregledKorisnika.brojRedaka"));
        ukupanBrojZapisa = dnevnikService.brojRedaka();
        listaZahtjeva = new ArrayList<>();
        date = new Date();
        dateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
        try {
            date = dateFormat.parse(odVremena);
            zaOdVremena = (int) (date.getTime() / 1000);
            date = dateFormat.parse(doVremena);
            zaDoVremena = (int) (date.getTime() / 1000);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public List<String> getListaZahtjeva() {
        listaZahtjeva = dnevnikService.dajSveDistinctZahtjeve();
        return listaZahtjeva;
    }

    public void setListaZahtjeva(List<String> listaZahtjeva) {
        this.listaZahtjeva = listaZahtjeva;
    }

    public List<Dnevnik> getListaDnevnik() {
        try {
            listaDnevnik = dnevnikService.dohvatiSve();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listaDnevnik;
    }

    public void setListaDnevnik(List<Dnevnik> listaDnevnik) {
        this.listaDnevnik = listaDnevnik;
    }

    public int getUkupanBrojZapisa() {
        return ukupanBrojZapisa;
    }

    public void setUkupanBrojZapisa(int ukupanBrojZapisa) {
        this.ukupanBrojZapisa = ukupanBrojZapisa;
    }

    public int getStranica() {
        return stranica;
    }

    public void setStranica(int stranica) {
        this.stranica = stranica;
    }

    public int getBrojRedaka() {
        return brojRedaka;
    }

    public void setBrojRedaka(int brojRedaka) {
        this.brojRedaka = brojRedaka;
    }

    public String getPoruka() {
        return poruka;
    }

    public void setPoruka(String poruka) {
        this.poruka = poruka;
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

    public String povecajStranicu() {
        int max = ukupanBrojZapisa / brojRedaka + 1;
        if (stranica < max) {
            try {
                stranica += 1;
                listaDnevnik = dnevnikService.dohvatiUzFiltriranje(stranica, brojRedaka, odabraniRedak, zaOdVremena, zaOdVremena);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return "";
    }

    public String smanjiStranicu() {
        if (stranica > 1) {
            try {
                stranica -= 1;
                listaDnevnik = dnevnikService.dohvatiUzFiltriranje(stranica, brojRedaka, odabraniRedak, zaOdVremena, zaDoVremena);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return "";
    }

    public String getOdabraniRedak() {
        return odabraniRedak;
    }

    public void setOdabraniRedak(String odabraniRedak) {
        this.odabraniRedak = odabraniRedak;
    }

}
