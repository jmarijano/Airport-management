package org.foi.nwtis.jmarijano.helperi;

import java.io.IOException;
import java.net.Socket;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.foi.nwtis.jmarijano.konfiguracije.Konfiguracija;
import org.foi.nwtis.jmarijano.konfiguracije.NeispravnaKonfiguracija;
import org.foi.nwtis.jmarijano.konfiguracije.NemaKonfiguracije;

public class DretvaKonfiguracijaHelper {

    private final Konfiguracija konfiguracija;
    private final SimpleDateFormat dateFormat;
    private Date date;

    /**
     * Konstruktor
     *
     * @param konfiguracija Konfiguracija objekt.
     */
    public DretvaKonfiguracijaHelper(Konfiguracija konfiguracija) {
        this.konfiguracija = konfiguracija;
        dateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
        date = new Date();
    }

    /**
     * Metoda koja provjerava postojanje konfiguracijskih postavki-
     *
     * @return True ako postoje sve. Inače false.
     */
    public synchronized boolean postojePostavke() {
        if (!konfiguracija.postojiPostavka("OpenSkyNetwork.korisnik")
                || !konfiguracija.postojiPostavka("OpenSkyNetwork.lozinka")
                || !konfiguracija.postojiPostavka("preuzimanje.ciklus")
                || !konfiguracija.postojiPostavka("preuzimanje.pocetakIntervala")
                || !konfiguracija.postojiPostavka("preuzimanje.trajanje")) {
            return false;
        }
        return true;
    }

    /**
     * Metoda koja provjerava postojanje praznih postavki.
     *
     * @return True ako postoje. Inače false.
     */
    public synchronized boolean praznePostavke() {
        if (konfiguracija.dajPostavku("OpenSkyNetwork.korisnik").equals("")
                || konfiguracija.dajPostavku("OpenSkyNetwork.lozinka").equals("")
                || konfiguracija.dajPostavku("preuzimanje.ciklus").equals("")
                || konfiguracija.dajPostavku("preuzimanje.pocetakIntervala").equals("")
                || konfiguracija.dajPostavku("preuzimanje.trajanje").equals("")) {
            return true;
        }
        return false;
    }

    /**
     * Metoda koja provjerava postojanje nepozitivnih brojeva u konfiguracijskoj
     * datoteki.
     *
     * @return True ako postoje. Inače false.
     */
    public synchronized boolean postojeNepozitivniBrojevi() {
        try {
            if (Integer.parseInt(konfiguracija.dajPostavku("preuzimanje.ciklus")) <= 0
                    || Integer.parseInt(konfiguracija.dajPostavku("preuzimanje.trajanje")) <= 0) {
                return true;
            }
            return false;
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return true;
        }
    }

    /**
     * Metoda koja provjerava pravilnost postavke preuzimanje.trajanje.
     *
     * @return True ako je ispravna vrijednost. Inače false.
     */
    public synchronized boolean pravilnoTrajanje() {
        if (Integer.parseInt(konfiguracija.dajPostavku("preuzimanje.trajanje")) > 168) {
            return false;
        }
        return true;
    }

    public synchronized boolean pravilanDatum() {
        boolean output = false;
        try {
            date = dateFormat.parse(konfiguracija.dajPostavku("preuzimanje.pocetakIntervala"));
            int datum = (int) (date.getTime() / 1000);
            output = true;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return output;
    }

    public synchronized int dohvatiRedniBrojCiklusa() {
        int output = 0;
        try {
            output = Integer.parseInt(konfiguracija.dajPostavku("preuzimanje.redniBrojCiklusa"));
            if (output <= 0) {
                output = 0;
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return output;
    }

    public synchronized boolean zapisiUDatoteku(String nazivPostavke, String vrijednostPostavke) {
        boolean output = false;
        try {
            if (konfiguracija.postojiPostavka(nazivPostavke)) {
                konfiguracija.azurirajPostavku(nazivPostavke, vrijednostPostavke);
            } else {
                konfiguracija.spremiPostavku(nazivPostavke, vrijednostPostavke);
            }
            konfiguracija.spremiKonfiguraciju();
            output = true;
        } catch (NeispravnaKonfiguracija | NemaKonfiguracije e) {
            e.printStackTrace();
        }
        return output;
    }

    public synchronized int dohvatiPort() {
        int output = 0;
        try {
            int provjera = Integer.parseInt(konfiguracija.dajPostavku("port"));
            if (provjeriPort(provjera) && provjeriDostupnostPorta(provjera)) {
                output = provjera;
            }

        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return output;
    }

    public synchronized boolean provjeriPort(int port) {
        boolean output = false;
        try {
            if (port >= 8000 && port <= 9999) {
                output = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return output;
    }

    public synchronized boolean provjeriDostupnostPorta(int port) {
        try (Socket ignored = new Socket("localhost", port)) {
            return false;
        } catch (IOException ex) {
            return true;
        }
    }
}
