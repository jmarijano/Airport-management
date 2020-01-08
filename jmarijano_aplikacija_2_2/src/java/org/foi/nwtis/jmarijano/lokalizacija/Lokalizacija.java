package org.foi.nwtis.jmarijano.lokalizacija;

import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

@Named(value = "lokalizacija")
@SessionScoped
public class Lokalizacija implements Serializable {

    private String jezik = "hr";
    List<Locale> locales = new ArrayList<>();
    ResourceBundle resourceBundle;
    private String error = "";

    /**
     * Metoda koja dohvaća atribut error
     *
     * @return Vraća atribut error
     */
    public String getError() {
        return error;
    }

    /**
     * Metoda koja postavlja atribut error na primljenu vrijednost
     *
     * @param error Nova vrijednost atributa error
     * @return Vraća atribut error
     */
    public String setError(String error) {
        try {
            this.error = resourceBundle.getString(error);
        } catch (Exception e) {
        }

        return this.error;
    }

    /**
     * Konstruktor klase
     */
    public Lokalizacija() {
        Locale hr = new Locale("hr");
        Locale en = new Locale("en");
        Locale de = new Locale("de");
        locales.add(de);
        locales.add(en);
        locales.add(hr);
        resourceBundle = ResourceBundle.getBundle("org.foi.nwtis.jmarijano.prijevod.prijevod", getLocale());
    }

    /**
     * Metoda koja dohvaća Locale prema prenutnom jeztiku
     *
     * @return null
     */
    public Object odaberiJezik() {
        resourceBundle = ResourceBundle.getBundle("org.foi.nwtis.jmarijano.prijevod.prijevod", getLocale());
        return null;
    }

    /**
     * Metoda koja dohvaća vrijednost atributa jezik
     *
     * @return Vraća atribut jezik kao String
     */
    public String getJezik() {
        return jezik;
    }

    /**
     * Metoda koja postavlja vrijednost atributa jezik
     *
     * @param jezik Nova vrijednost atributa jezik
     */
    public void setJezik(String jezik) {

        this.jezik = jezik;
    }

    /**
     * Metoda koja dohvaća Locale objekt prema trenutnom jeziku
     *
     * @return Locale objekt
     */
    public Locale getLocale() {
        for (Locale l : locales) {
            if (l.getLanguage().equals(jezik)) {
                return l;
            }
        }
        return null;
    }
}
