/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.jmarijano.poruke;

import java.util.Date;

/**
 *
 * @author LiterallyCan't
 */
public class ZaMqtt {

    private String korisnik;
    private String aerodrom;
    private String avion;
    private String oznaka;
    private String poruka;
    private String vrijeme;

    public ZaMqtt() {
    }

    public ZaMqtt(String korisnik, String aerodrom, String avion, String oznaka, String poruka, String vrijeme) {
        this.korisnik = korisnik;
        this.aerodrom = aerodrom;
        this.avion = avion;
        this.oznaka = oznaka;
        this.poruka = poruka;
        this.vrijeme = vrijeme;
    }

    public String getKorisnik() {
        return korisnik;
    }

    public void setKorisnik(String korisnik) {
        this.korisnik = korisnik;
    }

    public String getAerodrom() {
        return aerodrom;
    }

    public void setAerodrom(String aerodrom) {
        this.aerodrom = aerodrom;
    }

    public String getAvion() {
        return avion;
    }

    public void setAvion(String avion) {
        this.avion = avion;
    }

    public String getOznaka() {
        return oznaka;
    }

    public void setOznaka(String oznaka) {
        this.oznaka = oznaka;
    }

    public String getPoruka() {
        return poruka;
    }

    public void setPoruka(String poruka) {
        this.poruka = poruka;
    }

    public String getVrijeme() {
        return vrijeme;
    }

    public void setVrijeme(String vrijeme) {
        this.vrijeme = vrijeme;
    }

}
