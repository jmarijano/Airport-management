package org.foi.nwtis.jmarijano.helperi;

import org.foi.nwtis.jmarijano.konfiguracije.Konfiguracija;

public class OpenSkyHelper {

    private final Konfiguracija konfiguracija;

    /**
     * Konstruktor
     *
     * @param konfiguracija Konfiguracija objekt.
     */
    public OpenSkyHelper(Konfiguracija konfiguracija) {
        this.konfiguracija = konfiguracija;
    }

    /**
     * Metoda koja preuzima postavku OpenSkyNetwork.korisnik iz konfiguracijske
     * datoteke
     *
     * @return korisničko ime OpenSkyNetwork web servisa.
     */
    public String preuzmiUsername() {
        return konfiguracija.dajPostavku("OpenSkyNetwork.korisnik");
    }

    /**
     * Metoda koja preuzima postavku OpenSkyNetwork.lozinka iz konfiguracijske
     * datoteke
     *
     * @return lozinka korisnika s OpenSkyNetwork web servisa.
     */
    public String preuzmiPassword() {
        return konfiguracija.dajPostavku("OpenSkyNetwork.lozinka");
    }
}
