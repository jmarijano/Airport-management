package org.foi.nwtis.jmarijano.helperi;

import org.foi.nwtis.jmarijano.konfiguracije.Konfiguracija;
import org.foi.nwtis.rest.klijenti.LIQKlijent;
import org.foi.nwtis.rest.podaci.Lokacija;

public class LocationIQHelper {

    private final Konfiguracija konfiguracija;

    /**
     * Konstruktor klase.
     *
     * @param konfiguracija Konfiguracija objekt.
     */
    public LocationIQHelper(Konfiguracija konfiguracija) {
        this.konfiguracija = konfiguracija;
    }

    /**
     * Metoda koja preuzima GPS lokacju s LocationIQ web servisa.
     *
     * @param nazivAerodroma Naziv aerodroma.
     * @return objekt tipa Lokacija.
     */
    public Lokacija preuzmiGPSLokaciju(String nazivAerodroma) {
        String token = konfiguracija.dajPostavku("LocationIQ.token");
        LIQKlijent lIQKlijent = new LIQKlijent(token);
        Lokacija output = lIQKlijent.getGeoLocation(nazivAerodroma);
        return output;
    }

}
