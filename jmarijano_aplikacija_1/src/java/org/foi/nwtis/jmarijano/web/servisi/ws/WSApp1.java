/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.jmarijano.web.servisi.ws;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.jws.WebService;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.servlet.ServletContext;
import org.foi.nwtis.jmarijano.helperi.OWMHelper;
import org.foi.nwtis.jmarijano.konfiguracije.Konfiguracija;
import org.foi.nwtis.jmarijano.modeli.Aerodrom;
import org.foi.nwtis.jmarijano.modeli.Dnevnik;
import org.foi.nwtis.jmarijano.modeli.Korisnik;
import org.foi.nwtis.jmarijano.mysql.AirplanesMySQL;
import org.foi.nwtis.jmarijano.mysql.DnevnikMySQL;
import org.foi.nwtis.jmarijano.mysql.KorisniciMySQL;
import org.foi.nwtis.jmarijano.mysql.MyAirportsMySQL;
import org.foi.nwtis.jmarijano.sucelja.AirplanesService;
import org.foi.nwtis.jmarijano.sucelja.DnevnikService;
import org.foi.nwtis.jmarijano.sucelja.KorisnikService;
import org.foi.nwtis.jmarijano.sucelja.MyAirportsService;
import org.foi.nwtis.jmarijano.web.slusaci.SlusacAplikacije;
import org.foi.nwtis.rest.klijenti.OSKlijent;
import org.foi.nwtis.rest.podaci.AvionLeti;
import org.foi.nwtis.rest.podaci.MeteoPodaci;

/**
 *
 * @author LiterallyCan't
 */
@WebService(serviceName = "WSApp1")
public class WSApp1 {

    private final KorisnikService korisnikService;
    private final ServletContext servletContext;
    private final Konfiguracija konfiguracija;
    private final OWMHelper oWMHelper;
    private final MyAirportsService myAirportsService;
    private Korisnik korisnik;
    private Aerodrom aerodrom;
    private final OSKlijent oSKlijent;
    private final DateFormat dateFormat;
    private Date pocetak;
    private final AirplanesService airplanesService;
    private final DnevnikService dnevnikService;
    private final Dnevnik dnevnik;

    public WSApp1() {
        this.servletContext = SlusacAplikacije.getSc();
        konfiguracija = SlusacAplikacije.getKonfiguracija();
        korisnikService = new KorisniciMySQL(servletContext);
        oWMHelper = new OWMHelper(konfiguracija);
        myAirportsService = new MyAirportsMySQL(servletContext);
        //TODO treba preuzeti iz nečega
        oSKlijent = new OSKlijent("jmarijano", "96954559");
        dateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
        airplanesService = new AirplanesMySQL(servletContext);
        dnevnik = new Dnevnik();
        dnevnikService = new DnevnikMySQL(servletContext);
    }

    /**
     * Web service operation
     *
     * @param korisnickoIme
     * @param lozinka
     * @param ident
     * @return
     */
    @WebMethod(operationName = "dajZadnjiPreuzetiAvionZaOdabraniAerodrom")
    public AvionLeti dajZadnjiPreuzetiAvionZaOdabraniAerodrom(
            @WebParam(name = "korisnickoIme") String korisnickoIme,
            @WebParam(name = "lozinka") String lozinka,
            @WebParam(name = "ident") String ident) {
        AvionLeti output = new AvionLeti();
        try {
            korisnik = korisnikService.getByKorisnickoImeAndLozinka(korisnickoIme, lozinka);
            if (korisnik.getKorisnickoIme() != null && korisnik.getLozinka() != null) {
                pocetak = dateFormat.parse(konfiguracija.dajPostavku("preuzimanje.pocetakIntervala"));
                output = airplanesService.dajZadnjiPreuzetiAvionZaOdabraniAerodrom(ident);
                unesiUDnevnik("Daj zadnji preuzet avion aerodroma " + ident, "SOAP", 2, korisnickoIme, "Preuzet zadnji avion od strane korisnika");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return output;
    }

    /**
     *
     * @param korisnickoIme
     * @param lozinka
     * @param ident
     * @param brojAviona
     * @return
     */
    @WebMethod(operationName = "dajPosljednjihNAvionaZaOdredeniAerodrom")
    @SuppressWarnings("CallToPrintStackTrace")
    public List<AvionLeti> dajPosljednjihNAvionaZaOdredeniAerodrom(
            @WebParam(name = "korisnickoIme") String korisnickoIme,
            @WebParam(name = "lozinka") String lozinka,
            @WebParam(name = "ident") String ident,
            @WebParam(name = "brojAviona") int brojAviona) {        //TODO write your implementation code here:
        List<AvionLeti> output = new ArrayList<>();
        try {
            korisnik = korisnikService.getByKorisnickoImeAndLozinka(korisnickoIme, lozinka);
            if (korisnik.getKorisnickoIme() != null && korisnik.getLozinka() != null) {
                output = airplanesService.dajPosljednjiNAvionaAerodroma(ident, brojAviona);
                if (output.size() > 0) {
                    unesiUDnevnik("Daj posljednjih " + brojAviona + " aviona aerodroma" + ident, "SOAP", 2, korisnickoIme, "Preuzeto posljednjih n aviona");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return output;
    }

    /**
     *
     * @param korisnickoIme
     * @param lozinka
     * @param ident
     * @param odVremena
     * @param doVremena
     * @return
     */
    @WebMethod(operationName = "dajAvionePoletjeleSAerodroma")
    public List<AvionLeti> dajAvionePoletjeleSAerodroma(@WebParam(name = "korisnickoIme") String korisnickoIme,
            @WebParam(name = "lozinka") String lozinka,
            @WebParam(name = "ident") String ident,
            @WebParam(name = "odVremena") int odVremena,
            @WebParam(name = "doVremena") int doVremena) {
        //TODO write your implementation code here:
        List<AvionLeti> output = new ArrayList<>();
        try {
            korisnik = korisnikService.getByKorisnickoImeAndLozinka(korisnickoIme, lozinka);
            if (korisnik.getKorisnickoIme() != null && korisnik.getLozinka() != null) {
                output = airplanesService.dajAvionePoletjeleSAerodroma(ident, odVremena, doVremena);
                if (output.size() > 0) {
                    unesiUDnevnik("Daj avione poletjele s aerodroma " + ident, "SOAP", 2, korisnickoIme, "Preuzeti avioni aerodroma");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return output;
    }

    /**
     *
     * @param korisnickoIme
     * @param lozinka
     * @param icao24
     * @param odVremena
     * @param doVremena
     * @return
     */
    @WebMethod(operationName = "dajAerodromeAviona")
    public List<AvionLeti> dajAerodromeAviona(@WebParam(name = "korisnickoIme") String korisnickoIme,
            @WebParam(name = "lozinka") String lozinka,
            @WebParam(name = "icao24") String icao24,
            @WebParam(name = "odVremena") int odVremena,
            @WebParam(name = "doVremena") int doVremena
    ) {
        //TODO write your implementation code here:
        List<AvionLeti> output = new ArrayList<>();
        try {
            korisnik = korisnikService.getByKorisnickoImeAndLozinka(korisnickoIme, lozinka);
            if (korisnik.getKorisnickoIme() != null && korisnik.getLozinka() != null) {
                output = airplanesService.dajAerodromeAviona(icao24, odVremena, doVremena);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return output;
    }

    /**
     *
     * @param korisnickoIme
     * @param lozinka
     * @param icao24
     * @param odVremena
     * @param doVremena
     * @return
     */
    @WebMethod(operationName = "dajNaziveAerodromaAviona")
    public List<String> dajNaziveAerodromaAviona(@WebParam(name = "korisnickoIme") String korisnickoIme,
            @WebParam(name = "lozinka") String lozinka,
            @WebParam(name = "icao24") String icao24,
            @WebParam(name = "odVremena") int odVremena,
            @WebParam(name = "doVremena") int doVremena
    ) {
        //TODO write your implementation code here:
        List<String> output = new ArrayList<>();
        try {
            korisnik = korisnikService.getByKorisnickoImeAndLozinka(korisnickoIme, lozinka);
            if (korisnik.getKorisnickoIme() != null && korisnik.getLozinka() != null) {
                output = airplanesService.dajNaziveAerodromaAviona(icao24, odVremena, doVremena);
                if (output.size() > 0) {
                    //TODO dnevnik
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return output;
    }

    /**
     * Web service operation
     *
     * @param korisnickoIme
     * @param lozinka
     * @param ident
     * @return
     */
    @WebMethod(operationName = "dajMeteoPodatkeAerodroma")
    public MeteoPodaci dajMeteoPodatkeAerodroma(@WebParam(name = "korisnickoIme") String korisnickoIme,
            @WebParam(name = "lozinka") String lozinka,
            @WebParam(name = "ident") String ident
    ) {
        //TODO write your implementation code here:
        MeteoPodaci output = new MeteoPodaci();
        try {
            korisnik = korisnikService.getByKorisnickoImeAndLozinka(korisnickoIme, lozinka);
            if (korisnik.getKorisnickoIme() != null && korisnik.getLozinka() != null) {
                aerodrom = myAirportsService.dohvatiPremaIdent(ident);
                output = oWMHelper.preuzmiMeteoPodatke(aerodrom.getLokacija());
                unesiUDnevnik("Preuzi meteo podatke aerodroma " + ident, "SOAP", 2, korisnickoIme, "Preuzeti meteo podaci aerodroma");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return output;
    }

    /**
     *
     * @param korisnickoIme
     * @param lozinka
     * @param korisnik
     * @return
     */
    @WebMethod(operationName = "dodajKorisnika")
    public boolean dodajKorisnika(@WebParam(name = "korisnickoIme") Korisnik primljeniKorisnik) {
        //TODO write your implementation code here:
        boolean output = false;
        try {
            if (korisnikService.dodaj(primljeniKorisnik) == 1) {
                output = true;
                unesiUDnevnik("Dodaj korisnika", "SOAP", 2, primljeniKorisnik.getKorisnickoIme(), "Dodan novi korisnik");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return output;
    }

    /**
     *
     * @param korisnickoIme
     * @param lozinka
     * @param korisnik
     * @return
     */
    @WebMethod(operationName = "azurirajKorisnika")
    public boolean azurirajKorisnika(@WebParam(name = "korisnickoIme") String korisnickoIme,
            @WebParam(name = "lozinka") String lozinka,
            @WebParam(name = "korisnik") Korisnik primljeniKorisnik
    ) {
        //TODO write your implementation code here:
        boolean output = false;
        try {
            korisnik = korisnikService.getByKorisnickoImeAndLozinka(korisnickoIme, lozinka);
            if (korisnik.getKorisnickoIme() != null && korisnik.getLozinka() != null) {
                if (korisnikService.azuriraj(primljeniKorisnik) == 1) {
                    output = true;
                    unesiUDnevnik("Ažuriraj korisnik " + korisnickoIme, "SOAP", 2, korisnickoIme, "Ažuriraj korisnik");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return output;
    }

    /**
     *
     * @param korisnickoIme
     * @param lozinka
     * @return
     */
    @WebMethod(operationName = "dajSveKorisnike")
    public List<Korisnik> dajSveKorisnike(@WebParam(name = "korisnickoIme") String korisnickoIme,
            @WebParam(name = "lozinka") String lozinka
    ) {
        //TODO write your implementation code here:

        List<Korisnik> output = new ArrayList<>();
        try {
            korisnik = korisnikService.getByKorisnickoImeAndLozinka(korisnickoIme, lozinka);
            if (korisnik.getKorisnickoIme() != null && korisnik.getLozinka() != null) {
                output = korisnikService.dohvatiSve();
                if (output.size() > 0) {
                    unesiUDnevnik("Daj sve korisnike", "SOAP", 2, korisnickoIme, "Preuzeti svi korisnici");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return output;
    }

    /**
     *
     * @param korisnickoIme
     * @param lozinka
     * @param identPrvi
     * @param identDrugi
     * @return
     */
    @WebMethod(operationName = "udaljenostIzmeduAerodroma")
    public double udaljenostIzmeduAerodroma(@WebParam(name = "korisnickoIme") String korisnickoIme,
            @WebParam(name = "lozinka") String lozinka,
            @WebParam(name = "identPrvi") String identPrvi,
            @WebParam(name = "identDrugi") String identDrugi
    ) {
        //TODO write your implementation code here:
        double output = 0;
        try {
            korisnik = korisnikService.getByKorisnickoImeAndLozinka(korisnickoIme, lozinka);
            if (korisnik.getKorisnickoIme() != null && korisnik.getLozinka() != null) {
                Aerodrom prvi = myAirportsService.dohvatiPremaIdent(identPrvi);
                Aerodrom drugi = myAirportsService.dohvatiPremaIdent(identDrugi);
                double latPrvi = Double.parseDouble(prvi.getLokacija().getLatitude());
                double lonPrvi = Double.parseDouble(prvi.getLokacija().getLongitude());
                double latDrugi = Double.parseDouble(drugi.getLokacija().getLatitude());
                double lonDrugi = Double.parseDouble(drugi.getLokacija().getLongitude());
                double earthRadius = 6371; //meters
                double dLat = Math.toRadians(latDrugi - latPrvi);
                double dLng = Math.toRadians(lonDrugi - lonPrvi);
                double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                        + Math.cos(Math.toRadians(latPrvi)) * Math.cos(Math.toRadians(latDrugi))
                        * Math.sin(dLng / 2) * Math.sin(dLng / 2);
                double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
                double dist = (double) (earthRadius * c);
                output = dist;
                unesiUDnevnik("Preuzeta udaljenost između aerodroma " + identPrvi + " i aerodroma" + identPrvi, "SOAP", 2, korisnickoIme, "Preuzeta udaljenost između dva aerodroma");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return output;
    }

    /**
     *
     * @param korisnickoIme
     * @param lozinka
     * @param ident
     * @param odKm
     * @param doKm
     * @return
     */
    @WebMethod(operationName = "dajAerodromeAerodromaUdaljeneURasponu")
    public List<Aerodrom> dajAerodromeAerodromaUdaljeneURasponu(@WebParam(name = "korisnickoIme") String korisnickoIme,
            @WebParam(name = "lozinka") String lozinka,
            @WebParam(name = "ident") String ident,
            @WebParam(name = "odKm") int odKm,
            @WebParam(name = "doKm") int doKm
    ) {
        //TODO write your implementation code here:
        List<Aerodrom> output = new ArrayList<>();
        List<Aerodrom> sviAerodromi = new ArrayList<>();
        try {
            korisnik = korisnikService.getByKorisnickoImeAndLozinka(korisnickoIme, lozinka);
            if (korisnik.getKorisnickoIme() != null && korisnik.getLozinka() != null) {
                Aerodrom prvi = myAirportsService.dohvatiPremaIdent(ident);
                sviAerodromi = myAirportsService.dohvatiSve();
                double latPrvi = Double.parseDouble(prvi.getLokacija().getLatitude());
                double lonPrvi = Double.parseDouble(prvi.getLokacija().getLongitude());
                for (Aerodrom drugi : sviAerodromi) {
                    double latDrugi = Double.parseDouble(drugi.getLokacija().getLatitude());
                    double lonDrugi = Double.parseDouble(drugi.getLokacija().getLongitude());
                    double earthRadius = 6371; //meters
                    double dLat = Math.toRadians(latDrugi - latPrvi);
                    double dLng = Math.toRadians(lonDrugi - lonPrvi);
                    double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                            + Math.cos(Math.toRadians(latPrvi)) * Math.cos(Math.toRadians(latDrugi))
                            * Math.sin(dLng / 2) * Math.sin(dLng / 2);
                    double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
                    double dist = (double) (earthRadius * c);
                    if (dist >= odKm && dist <= doKm) {
                        output.add(drugi);
                    }
                }
                if (output.size() > 0) {
                    unesiUDnevnik("Prezeti svi aerodromi koji su udaljeni od aerodroma " + ident + " u rasponu od " + odKm + " do " + doKm + " kilometara", "SOAP", 2, korisnickoIme, "Preuzeti svi aerodromi koji su udaljeni od odabranog aerodroma u odabranom rasponu kilometara");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return output;
    }

    /**
     *
     * @param korisnickoIme
     * @param lozinka
     * @param identPolazisni
     * @param identOdredisni
     * @param odVremena
     * @param doVremena
     * @return
     */
    @WebMethod(operationName = "operation")
    public List<AvionLeti> operation(@WebParam(name = "korisnickoIme") String korisnickoIme,
            @WebParam(name = "lozinka") String lozinka,
            @WebParam(name = "identPolazisni") String identPolazisni,
            @WebParam(name = "identOdredisni") String identOdredisni,
            @WebParam(name = "odVremena") String odVremena,
            @WebParam(name = "doVremena") String doVremena
    ) {
        //TODO write your implementation code here:
        List<AvionLeti> output = new ArrayList<>();

        return output;
    }

    /* The function to convert decimal into radians */
    private double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    /* The function to convert radians into decimal */
    private double rad2deg(double rad) {
        return (rad * 180.0 / Math.PI);
    }

    private void unesiUDnevnik(String zahtjev, String vrstaZahtjeva, int dioApp, String korime, String opis) {
        dnevnik.setKorisnickoIme(korime);
        dnevnik.setDioAplikacije(dioApp);
        dnevnik.setOpis(opis);
        Timestamp sad = new Timestamp(System.currentTimeMillis());
        dnevnik.setVrijeme(sad);
        dnevnik.setVrstaZahtjeva(vrstaZahtjeva);
        dnevnik.setZahtjev(zahtjev);
        dnevnikService.dodaj(dnevnik);
    }
}
