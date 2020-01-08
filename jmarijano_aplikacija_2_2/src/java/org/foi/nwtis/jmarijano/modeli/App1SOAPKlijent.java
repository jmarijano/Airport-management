/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.jmarijano.modeli;

import org.foi.nwtis.jmarijano.web.servisi.ws.AvionLeti;
import org.foi.nwtis.jmarijano.web.servisi.ws.MeteoPodaci;

/**
 *
 * @author LiterallyCan't
 */
public class App1SOAPKlijent {

    public static MeteoPodaci dajMeteoPodatkeAerodroma(java.lang.String korisnickoIme, java.lang.String lozinka, java.lang.String ident) {
        org.foi.nwtis.jmarijano.web.servisi.ws.WSApp1_Service service = new org.foi.nwtis.jmarijano.web.servisi.ws.WSApp1_Service();
        org.foi.nwtis.jmarijano.web.servisi.ws.WSApp1 port = service.getWSApp1Port();
        return port.dajMeteoPodatkeAerodroma(korisnickoIme, lozinka, ident);
    }

    public static java.util.List<org.foi.nwtis.jmarijano.web.servisi.ws.AvionLeti> dajAvionePoletjeleSAerodroma(java.lang.String korisnickoIme, java.lang.String lozinka, java.lang.String ident, int odVremena, int doVremena) {
        org.foi.nwtis.jmarijano.web.servisi.ws.WSApp1_Service service = new org.foi.nwtis.jmarijano.web.servisi.ws.WSApp1_Service();
        org.foi.nwtis.jmarijano.web.servisi.ws.WSApp1 port = service.getWSApp1Port();
        return port.dajAvionePoletjeleSAerodroma(korisnickoIme, lozinka, ident, odVremena, doVremena);
    }

    public static double udaljenostIzmeduAerodroma(java.lang.String korisnickoIme, java.lang.String lozinka, java.lang.String identPrvi, java.lang.String identDrugi) {
        org.foi.nwtis.jmarijano.web.servisi.ws.WSApp1_Service service = new org.foi.nwtis.jmarijano.web.servisi.ws.WSApp1_Service();
        org.foi.nwtis.jmarijano.web.servisi.ws.WSApp1 port = service.getWSApp1Port();
        return port.udaljenostIzmeduAerodroma(korisnickoIme, lozinka, identPrvi, identDrugi);
    }

    public static AvionLeti dajZadnjiPreuzetiAvionZaOdabraniAerodrom(java.lang.String korisnickoIme, java.lang.String lozinka, java.lang.String ident) {
        org.foi.nwtis.jmarijano.web.servisi.ws.WSApp1_Service service = new org.foi.nwtis.jmarijano.web.servisi.ws.WSApp1_Service();
        org.foi.nwtis.jmarijano.web.servisi.ws.WSApp1 port = service.getWSApp1Port();
        return port.dajZadnjiPreuzetiAvionZaOdabraniAerodrom(korisnickoIme, lozinka, ident);
    }

}
