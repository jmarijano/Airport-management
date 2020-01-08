/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.jmarijano.modeli;

/**
 *
 * @author LiterallyCan't
 */
public class Aplikacija_1Klijent {

    public static boolean azurirajKorisnika(java.lang.String korisnickoIme, java.lang.String lozinka, org.foi.nwtis.jmarijano.web.servisi.ws.Korisnik korisnik) {
        org.foi.nwtis.jmarijano.web.servisi.ws.WSApp1_Service service = new org.foi.nwtis.jmarijano.web.servisi.ws.WSApp1_Service();
        org.foi.nwtis.jmarijano.web.servisi.ws.WSApp1 port = service.getWSApp1Port();
        return port.azurirajKorisnika(korisnickoIme, lozinka, korisnik);
    }

    public static java.util.List<org.foi.nwtis.jmarijano.web.servisi.ws.Korisnik> dajSveKorisnike(java.lang.String korisnickoIme, java.lang.String lozinka) {
        org.foi.nwtis.jmarijano.web.servisi.ws.WSApp1_Service service = new org.foi.nwtis.jmarijano.web.servisi.ws.WSApp1_Service();
        org.foi.nwtis.jmarijano.web.servisi.ws.WSApp1 port = service.getWSApp1Port();
        return port.dajSveKorisnike(korisnickoIme, lozinka);
    }

    public static boolean dodajKorisnika(org.foi.nwtis.jmarijano.web.servisi.ws.Korisnik korisnickoIme) {
        org.foi.nwtis.jmarijano.web.servisi.ws.WSApp1_Service service = new org.foi.nwtis.jmarijano.web.servisi.ws.WSApp1_Service();
        org.foi.nwtis.jmarijano.web.servisi.ws.WSApp1 port = service.getWSApp1Port();
        return port.dodajKorisnika(korisnickoIme);
    }

}
