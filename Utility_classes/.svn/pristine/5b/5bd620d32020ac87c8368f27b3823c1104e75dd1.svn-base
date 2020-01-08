package org.foi.nwtis.jmarijano.helperi;

import org.foi.nwtis.jmarijano.konfiguracije.Konfiguracija;
import org.foi.nwtis.rest.klijenti.OWMKlijent;
import org.foi.nwtis.rest.podaci.Lokacija;
import org.foi.nwtis.rest.podaci.MeteoPodaci;

public class OWMHelper {

    private final Konfiguracija konfiguracija;

    /**
     * Konstruktor
     *
     * @param konfiguracija Konfiguracija objekt.
     */
    public OWMHelper(Konfiguracija konfiguracija) {
        this.konfiguracija = konfiguracija;
    }

    /**
     * Metoda koja preuzima meteo podatke o lokaciji s OpenWeatherMap web
     * servisa.
     *
     * @param lokacija objekt tipa Lokacija.
     * @return objekt tipa MeteoPodaci.
     */
    public MeteoPodaci preuzmiMeteoPodatke(Lokacija lokacija) {
        String apikey = konfiguracija.dajPostavku("OpenWeatherMap.apikey");
        OWMKlijent oWMKlijent = new OWMKlijent(apikey);
        return oWMKlijent.getRealTimeWeather(
                lokacija.getLatitude(),
                lokacija.getLongitude());
    }
    
}
