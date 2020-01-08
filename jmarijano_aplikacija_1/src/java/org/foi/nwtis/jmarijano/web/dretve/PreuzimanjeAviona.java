package org.foi.nwtis.jmarijano.web.dretve;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import org.foi.nwtis.jmarijano.mysql.AirplanesMySQL;
import org.foi.nwtis.jmarijano.helperi.OpenSkyHelper;
import org.foi.nwtis.jmarijano.mysql.MyAirportsMySQL;
import org.foi.nwtis.jmarijano.modeli.Aerodrom;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import org.foi.nwtis.dkermek.ws.serveri.Avion;
import org.foi.nwtis.jmarijano.konfiguracije.Konfiguracija;
import org.foi.nwtis.rest.klijenti.OSKlijent;
import org.foi.nwtis.rest.podaci.AvionLeti;
import org.foi.nwtis.jmarijano.helperi.DretvaKonfiguracijaHelper;
import org.foi.nwtis.jmarijano.sucelja.AirplanesService;
import org.foi.nwtis.jmarijano.sucelja.MyAirportsService;
import org.foi.nwtis.jmarijano.web.slusaci.SlusacAplikacije;
import org.foi.nwtis.jmarijano.web.ws.klijenti.NWTiS_2019Klijent;

public class PreuzimanjeAviona extends Thread {

    public static AtomicBoolean prekidPreuzimanja;
    boolean kraj = false;
    String username;
    String password;
    private int ciklusDretve = 0;
    private int pocetakIntervala = 0;
    private int trajanje = 0;
    private final DretvaKonfiguracijaHelper konfiguracijaHelper;
    private Konfiguracija konfiguracija;
    private final MyAirportsService myAirportsService;
    private final OpenSkyHelper openSkyHelper;
    private final AirplanesService airplanesService;
    private int krajIntervala;
    private long pocetakDretve;
    private long krajDretve;
    private List<Aerodrom> aerodromi;
    private final DateFormat dateFormat;
    private Date pocetak;
    private int redniBrojCiklusa = 0;
    private List<AvionLeti> departures;
    private int inicijalniPocetakIntervala = 0;
    private String subversionKorime;
    private String subversionLozinka;

    /**
     * Konstruktor klase
     *
     */
    public PreuzimanjeAviona() {
        this.konfiguracija = (Konfiguracija) SlusacAplikacije.getSc().getAttribute("konfiguracija");
        myAirportsService = new MyAirportsMySQL(SlusacAplikacije.getSc());
        openSkyHelper = new OpenSkyHelper(konfiguracija);
        airplanesService = new AirplanesMySQL(SlusacAplikacije.getSc());
        konfiguracijaHelper = new DretvaKonfiguracijaHelper(konfiguracija);
        aerodromi = new ArrayList<>();
        departures = new ArrayList<>();
        dateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
        prekidPreuzimanja = new AtomicBoolean(false);
        subversionKorime = konfiguracija.dajPostavku("subversion.korisnickoIme");
        subversionLozinka = konfiguracija.dajPostavku("subversion.lozinka");
    }

    /**
     * Nadjačavanje metode iz klase Thread
     */
    @Override
    public void interrupt() {
        kraj = true;
        super.interrupt();
    }

    /**
     * Nadjačavanje metode run iz klase Thread.
     */
    @Override
    public void run() {
        while (!kraj) {
            try {
                pocetakDretve = System.currentTimeMillis();
                if (!prekidPreuzimanja.get()) {
                    System.out.println("Preuzimam aerodrome");
                    this.konfiguracija = (Konfiguracija) SlusacAplikacije.getSc().getAttribute("konfiguracija");
                    krajIntervala = pocetakIntervala + trajanje;
                    OSKlijent oSKlijent = new OSKlijent(username, password);
                    aerodromi = myAirportsService.dohvatiSve();
                    departures = new ArrayList<>();
                    for (Aerodrom a : aerodromi) {
                        departures.addAll(oSKlijent.getDepartures(a.getIcao(), pocetakIntervala, krajIntervala));
                    }
                    for (AvionLeti aleti : departures) {
                        if (!postojiNullAtributi(aleti)) {
                            try {
                                if (airplanesService.getCountByIcaoAndLastSeen(aleti.getIcao24(), aleti.getLastSeen()) == 0) {
                                    airplanesService.dodaj(aleti);
                                    Avion avionNovi = new Avion();
                                    avionNovi.setCallsign(aleti.getCallsign());
                                    avionNovi.setEstarrivalairport(aleti.getEstArrivalAirport());
                                    avionNovi.setEstdepartureairport(aleti.getEstDepartureAirport());
                                    avionNovi.setIcao24(aleti.getIcao24());
                                    avionNovi.setId(1);
                                    NWTiS_2019Klijent.dodajAvionGrupi(subversionKorime, subversionLozinka, avionNovi);
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                    int trenutnoVrijeme = (int) Instant.now().getEpochSecond();
                    if (trenutnoVrijeme > pocetakIntervala) {
                        pocetakIntervala = krajIntervala;
                        krajIntervala = pocetakIntervala + trajanje;
                        redniBrojCiklusa++;
                        System.out.println("Redni broj ciklusa: " + redniBrojCiklusa);
                        pocetak.setTime((long) pocetakIntervala * 1000);
                        String pocetniDatumZaZapisati = dateFormat.format(pocetak);
                        System.out.println("Početni datum za zapisati: " + pocetniDatumZaZapisati);
                        konfiguracijaHelper.zapisiUDatoteku("preuzimanje.redniBrojCiklusa", Integer.toString(redniBrojCiklusa));
                        konfiguracijaHelper.zapisiUDatoteku("preuzimanje.pocetakIntervala", pocetniDatumZaZapisati);
                    } else {
                        redniBrojCiklusa++;
                        konfiguracijaHelper.zapisiUDatoteku("preuzimanje.redniBrojCiklusa", Integer.toString(redniBrojCiklusa));
                        System.out.println("Redni broj ciklusa: " + redniBrojCiklusa);
                        pocetak.setTime((long) inicijalniPocetakIntervala * 1000);
                        String pocetniDatumZaZapisati = dateFormat.format(pocetak);
                        System.out.println("Početni datum za zapisati: " + pocetniDatumZaZapisati);
                        konfiguracijaHelper.zapisiUDatoteku("preuzimanje.pocetakIntervala", pocetniDatumZaZapisati);
                        pocetakIntervala = (int) (pocetak.getTime() / 1000);
                    }

                }
                krajDretve = System.currentTimeMillis();
                Thread.sleep(ciklusDretve - (krajDretve - pocetakDretve));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Nadjačanje metode start iz klase Thread.
     */
    @Override
    public synchronized void start() {
        if (konfiguracijaHelper.postojePostavke() && !konfiguracijaHelper.praznePostavke() && !konfiguracijaHelper.postojeNepozitivniBrojevi() && konfiguracijaHelper.pravilnoTrajanje() && konfiguracijaHelper.pravilanDatum()) {
            try {
                username = openSkyHelper.preuzmiUsername();
                password = openSkyHelper.preuzmiPassword();
                trajanje = Integer.parseInt(konfiguracija.dajPostavku("preuzimanje.trajanje")) * 3600;
                ciklusDretve = Integer.parseInt(konfiguracija.dajPostavku("preuzimanje.ciklus")) * 1000 * 60;
                redniBrojCiklusa = konfiguracijaHelper.dohvatiRedniBrojCiklusa();
                pocetak = dateFormat.parse(konfiguracija.dajPostavku("preuzimanje.pocetakIntervala"));
                pocetakIntervala = (int) (pocetak.getTime() / 1000);
                inicijalniPocetakIntervala = (int) (pocetak.getTime() / 1000);
                System.out.println("Inicijalni početak intervala: " + inicijalniPocetakIntervala);
            } catch (NumberFormatException | ParseException e) {
                e.printStackTrace();
            }
            super.start();
        } else {
            System.out.println("Krivo unesene postavke vremena");
        }
    }

    public boolean postojiNullAtributi(AvionLeti avionLeti) {
        boolean output = false;
        if (avionLeti.getIcao24() == null
                || avionLeti.getEstArrivalAirport() == null
                || avionLeti.getEstDepartureAirport() == null
                || avionLeti.getCallsign() == null) {
            output = true;
        }
        return output;
    }
}
