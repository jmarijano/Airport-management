/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.jmarijano.web.ws.klijenti;

import org.foi.nwtis.dkermek.ws.serveri.StatusKorisnika;

/**
 *
 * @author LiterallyCan't
 */
public class NWTiS_2019Klijent {

    public static boolean aktivirajAerodromGrupe(java.lang.String korisnickoIme, java.lang.String korisnickaLozinka, java.lang.String idAerodrom) {
        org.foi.nwtis.dkermek.ws.serveri.AerodromiWS_Service service = new org.foi.nwtis.dkermek.ws.serveri.AerodromiWS_Service();
        org.foi.nwtis.dkermek.ws.serveri.AerodromiWS port = service.getAerodromiWSPort();
        return port.aktivirajAerodromGrupe(korisnickoIme, korisnickaLozinka, idAerodrom);
    }

    public static Boolean autenticirajGrupu(java.lang.String korisnickoIme, java.lang.String korisnickaLozinka) {
        org.foi.nwtis.dkermek.ws.serveri.AerodromiWS_Service service = new org.foi.nwtis.dkermek.ws.serveri.AerodromiWS_Service();
        org.foi.nwtis.dkermek.ws.serveri.AerodromiWS port = service.getAerodromiWSPort();
        return port.autenticirajGrupu(korisnickoIme, korisnickaLozinka);
    }

    public static Boolean deregistrirajGrupu(java.lang.String korisnickoIme, java.lang.String korisnickaLozinka) {
        org.foi.nwtis.dkermek.ws.serveri.AerodromiWS_Service service = new org.foi.nwtis.dkermek.ws.serveri.AerodromiWS_Service();
        org.foi.nwtis.dkermek.ws.serveri.AerodromiWS port = service.getAerodromiWSPort();
        return port.deregistrirajGrupu(korisnickoIme, korisnickaLozinka);
    }

    public static Boolean registrirajGrupu(java.lang.String korisnickoIme, java.lang.String korisnickaLozinka) {
        org.foi.nwtis.dkermek.ws.serveri.AerodromiWS_Service service = new org.foi.nwtis.dkermek.ws.serveri.AerodromiWS_Service();
        org.foi.nwtis.dkermek.ws.serveri.AerodromiWS port = service.getAerodromiWSPort();
        return port.registrirajGrupu(korisnickoIme, korisnickaLozinka);
    }

    public static Boolean aktivirajGrupu(java.lang.String korisnickoIme, java.lang.String korisnickaLozinka) {
        org.foi.nwtis.dkermek.ws.serveri.AerodromiWS_Service service = new org.foi.nwtis.dkermek.ws.serveri.AerodromiWS_Service();
        org.foi.nwtis.dkermek.ws.serveri.AerodromiWS port = service.getAerodromiWSPort();
        return port.aktivirajGrupu(korisnickoIme, korisnickaLozinka);
    }

    public static StatusKorisnika dajStatusGrupe(java.lang.String korisnickoIme, java.lang.String korisnickaLozinka) {
        org.foi.nwtis.dkermek.ws.serveri.AerodromiWS_Service service = new org.foi.nwtis.dkermek.ws.serveri.AerodromiWS_Service();
        org.foi.nwtis.dkermek.ws.serveri.AerodromiWS port = service.getAerodromiWSPort();
        return port.dajStatusGrupe(korisnickoIme, korisnickaLozinka);
    }

    public static Boolean blokirajGrupu(java.lang.String korisnickoIme, java.lang.String korisnickaLozinka) {
        org.foi.nwtis.dkermek.ws.serveri.AerodromiWS_Service service = new org.foi.nwtis.dkermek.ws.serveri.AerodromiWS_Service();
        org.foi.nwtis.dkermek.ws.serveri.AerodromiWS port = service.getAerodromiWSPort();
        return port.blokirajGrupu(korisnickoIme, korisnickaLozinka);
    }

    public static Boolean dodajAerodromGrupi(java.lang.String korisnickoIme, java.lang.String korisnickaLozinka, org.foi.nwtis.dkermek.ws.serveri.Aerodrom serodrom) {
        org.foi.nwtis.dkermek.ws.serveri.AerodromiWS_Service service = new org.foi.nwtis.dkermek.ws.serveri.AerodromiWS_Service();
        org.foi.nwtis.dkermek.ws.serveri.AerodromiWS port = service.getAerodromiWSPort();
        return port.dodajAerodromGrupi(korisnickoIme, korisnickaLozinka, serodrom);
    }

    public static java.util.List<org.foi.nwtis.dkermek.ws.serveri.Aerodrom> dajSveAerodromeGrupe(java.lang.String korisnickoIme, java.lang.String korisnickaLozinka) {
        org.foi.nwtis.dkermek.ws.serveri.AerodromiWS_Service service = new org.foi.nwtis.dkermek.ws.serveri.AerodromiWS_Service();
        org.foi.nwtis.dkermek.ws.serveri.AerodromiWS port = service.getAerodromiWSPort();
        return port.dajSveAerodromeGrupe(korisnickoIme, korisnickaLozinka);
    }

    public static boolean obrisiAerodromGrupe(java.lang.String korisnickoIme, java.lang.String korisnickaLozinka, java.lang.String idAerodrom) {
        org.foi.nwtis.dkermek.ws.serveri.AerodromiWS_Service service = new org.foi.nwtis.dkermek.ws.serveri.AerodromiWS_Service();
        org.foi.nwtis.dkermek.ws.serveri.AerodromiWS port = service.getAerodromiWSPort();
        return port.obrisiAerodromGrupe(korisnickoIme, korisnickaLozinka, idAerodrom);
    }

    public static boolean dodajAvionGrupi(java.lang.String korisnickoIme, java.lang.String korisnickaLozinka, org.foi.nwtis.dkermek.ws.serveri.Avion avionNovi) {
        org.foi.nwtis.dkermek.ws.serveri.AerodromiWS_Service service = new org.foi.nwtis.dkermek.ws.serveri.AerodromiWS_Service();
        org.foi.nwtis.dkermek.ws.serveri.AerodromiWS port = service.getAerodromiWSPort();
        return port.dodajAvionGrupi(korisnickoIme, korisnickaLozinka, avionNovi);
    }

    public static java.util.List<org.foi.nwtis.dkermek.ws.serveri.Avion> dajSveAvioneAerodromaGrupe(java.lang.String korisnickoIme, java.lang.String korisnickaLozinka, java.lang.String idAerodrom) {
        org.foi.nwtis.dkermek.ws.serveri.AerodromiWS_Service service = new org.foi.nwtis.dkermek.ws.serveri.AerodromiWS_Service();
        org.foi.nwtis.dkermek.ws.serveri.AerodromiWS port = service.getAerodromiWSPort();
        return port.dajSveAvioneAerodromaGrupe(korisnickoIme, korisnickaLozinka, idAerodrom);
    }

    
}
