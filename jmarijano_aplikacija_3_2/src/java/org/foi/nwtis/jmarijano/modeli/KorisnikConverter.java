/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.jmarijano.modeli;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author LiterallyCan't
 */
public class KorisnikConverter {

    public static List<org.foi.nwtis.jmarijano.modeli.Korisnik> convert(List<org.foi.nwtis.jmarijano.web.servisi.ws.Korisnik> zaConvertati) {
        List<org.foi.nwtis.jmarijano.modeli.Korisnik> output = new ArrayList<>();
        for (org.foi.nwtis.jmarijano.web.servisi.ws.Korisnik korisnik : zaConvertati) {
            org.foi.nwtis.jmarijano.modeli.Korisnik noviKorisnik = new org.foi.nwtis.jmarijano.modeli.Korisnik();
            noviKorisnik.setId(korisnik.getId());
            noviKorisnik.setEmail(korisnik.getEmail());
            noviKorisnik.setIme(korisnik.getIme());
            noviKorisnik.setKorisnickoIme(korisnik.getKorisnickoIme());
            noviKorisnik.setLozinka(korisnik.getLozinka());
            noviKorisnik.setPrezime(korisnik.getPrezime());
            output.add(noviKorisnik);

        }
        return output;
    }
}
