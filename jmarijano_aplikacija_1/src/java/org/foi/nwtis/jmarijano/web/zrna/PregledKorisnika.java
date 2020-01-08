/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.jmarijano.web.zrna;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import org.foi.nwtis.jmarijano.modeli.Korisnik;
import org.foi.nwtis.jmarijano.mysql.KorisniciMySQL;
import org.foi.nwtis.jmarijano.sucelja.KorisnikService;
import org.foi.nwtis.jmarijano.web.slusaci.SlusacAplikacije;

/**
 *
 * @author morbi_000
 */
@Named(value = "pregledKorisnika")
@SessionScoped
public class PregledKorisnika implements Serializable {

    private List<Korisnik> listaKorisnika;
    private KorisnikService korisnikService;
    private int stranica;
    private int brojRedaka = 5;
    private int ukupanBrojZapisa = 0;

    /**
     * Creates a new instance of PregledKorisnika
     */
    public PregledKorisnika() {
        listaKorisnika = new ArrayList<>();
        korisnikService = new KorisniciMySQL(SlusacAplikacije.getSc());
        stranica = 1;
        brojRedaka = Integer.parseInt(SlusacAplikacije.getKonfiguracija().dajPostavku("pregledKorisnika.brojRedaka"));
        ukupanBrojZapisa = korisnikService.brojRedaka();

    }

    public List<Korisnik> getListaKorisnika() {
        try {

            listaKorisnika = korisnikService.dovatiSveUzStranicenje(stranica, brojRedaka);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return listaKorisnika;
    }

    public void setListaKorisnika(List<Korisnik> listaKorisnika) {
        this.listaKorisnika = listaKorisnika;
    }

    public String povecajStranicu() {
        int max = ukupanBrojZapisa / brojRedaka + 1;
        if (stranica < max) {
            try {
                stranica += 1;
                listaKorisnika = korisnikService.dovatiSveUzStranicenje(stranica, brojRedaka);
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
                listaKorisnika = korisnikService.dovatiSveUzStranicenje(stranica, brojRedaka);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        return "";
    }

    public int getStranica() {
        return stranica;
    }

    public int getBrojRedaka() {
        return brojRedaka;
    }

    public int getUkupanBrojZapisa() {
        return ukupanBrojZapisa;
    }

    public void setUkupanBrojZapisa(int ukupanBrojZapisa) {
        this.ukupanBrojZapisa = ukupanBrojZapisa;
    }

    public void setStranica(int stranica) {
        this.stranica = stranica;
    }
}
