/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.jmarijano.web.dretve;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.foi.nwtis.jmarijano.modeli.Dnevnik;
import org.foi.nwtis.jmarijano.modeli.Korisnik;
import org.foi.nwtis.jmarijano.mysql.DnevnikMySQL;
import org.foi.nwtis.jmarijano.mysql.KorisniciMySQL;
import org.foi.nwtis.jmarijano.sucelja.DnevnikService;
import org.foi.nwtis.jmarijano.sucelja.KorisnikService;
import org.foi.nwtis.jmarijano.web.slusaci.SlusacAplikacije;
import org.foi.nwtis.jmarijano.web.ws.klijenti.NWTiS_2019Klijent;
import org.foi.nwtis.jmarijano.web.zahtjevi.PorukaPosluzitelj;
import org.foi.nwtis.jmarijano.web.zrna.PosluziteljskiJMS;

/**
 *
 * @author LiterallyCan't
 */
public class DretvaZahtjeva extends Thread {

    public static volatile AtomicBoolean prekidPreuzimanjaPodataka;
    private static volatile AtomicBoolean kraj;
    private final Socket socket;
    private StringBuffer stringBuilder;
    private String komanda;
    private final KorisnikService korisnikService;
    private int vrstaZahtjeva;
    private PorukaPosluzitelj poruka;
    private DateFormat dateFormat;
    private String vrijeme;
    private Dnevnik dnevnik;
    private DnevnikService dnevnikService;

    public DretvaZahtjeva(Socket socket, Timestamp timestamp) {
        this.socket = socket;
        kraj = new AtomicBoolean(false);
        korisnikService = new KorisniciMySQL(SlusacAplikacije.getSc());
        prekidPreuzimanjaPodataka = new AtomicBoolean(false);
        poruka = new PorukaPosluzitelj();
        dateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss.SSS");
        dnevnik = new Dnevnik();
        dnevnikService = new DnevnikMySQL(SlusacAplikacije.getSc());

    }

    @Override
    public void interrupt() {
        kraj.set(true);
        try {
            socket.close();
        } catch (IOException ex) {
            Logger.getLogger(DretvaZahtjeva.class.getName()).log(Level.SEVERE, null, ex);
        }
        super.interrupt(); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void run() {
        try {
            stringBuilder = dohvatiZahtjev();
            komanda = stringBuilder.toString();
            System.out.println("Dobivena komanda:" + komanda);
            vrstaZahtjeva = vrstaKomande();
            int duljinaKomande = komanda.split(";").length;
            String[] polje = komanda.split(";");
            String korisnickoime = polje[0].trim().substring(9);
            String lozinka = polje[1].trim().substring(8);
            Korisnik korisnik = new Korisnik();
            korisnik = korisnikService.getByKorisnickoImeAndLozinka(korisnickoime, lozinka);
            System.out.println("Vrsta zahtjeva: " + vrstaZahtjeva);
            System.out.println("PRekid za aerodrome:" + PreuzimanjeAviona.prekidPreuzimanja.get());
            System.out.println("Prekid za grupu:" + ServerAerodroma.prekidGrupa.get());
            switch (vrstaZahtjeva) {
                case 1:
                    switch (duljinaKomande) {
                        case 2:
                            if (korisnik.getLozinka() != null && korisnik.getKorisnickoIme() != null) {
                                vrijeme = dateFormat.format(new Date());
                                poruka.setKomanda(komanda);
                                System.out.println("Komanda: " + komanda);
                                poruka.setVrijeme(vrijeme);
                                System.out.println("Vrijeme" + vrijeme);
                                PosluziteljskiJMS.PosaljiPoruku(poruka);
                                unesiUDnevnik("Korisničko ime i lozinka", "KOMANDE", 1, korisnik.getKorisnickoIme(), "Poslana komanda " + komanda);
                                posaljiOdgovor("OK;");
                            } else {
                                posaljiOdgovor("ERR 11;");
                            }
                            break;
                        case 3:
                            if (korisnik.getLozinka() != null && korisnik.getKorisnickoIme() != null) {
                                String treciDio = polje[2];
                                switch (treciDio) {
                                    case " PAUZA":
                                        if (ServerAerodroma.prekidGrupa.compareAndSet(false, true)) {
                                            vrijeme = dateFormat.format(new Date());
                                            poruka.setKomanda(komanda);
                                            poruka.setVrijeme(vrijeme);
                                            PosluziteljskiJMS.PosaljiPoruku(poruka);
                                            unesiUDnevnik("PAUZA", "KOMANDE", 1, korisnik.getKorisnickoIme(), "Poslana komanda " + komanda);
                                            posaljiOdgovor("OK 10;");
                                        } else {
                                            posaljiOdgovor("ERR 12;");
                                        }
                                        break;
                                    case " KRENI":
                                        if (ServerAerodroma.prekidGrupa.compareAndSet(true, false)) {
                                            vrijeme = dateFormat.format(new Date());
                                            poruka.setKomanda(komanda);
                                            poruka.setVrijeme(vrijeme);
                                            PosluziteljskiJMS.PosaljiPoruku(poruka);
                                            unesiUDnevnik("KRENI", "KOMANDE", 1, korisnik.getKorisnickoIme(), "Poslana komanda " + komanda);
                                            posaljiOdgovor("OK;");
                                        } else {
                                            posaljiOdgovor("ERR 13;");
                                        }
                                        break;
                                    case " PASIVNO":
                                        if (PreuzimanjeAviona.prekidPreuzimanja.compareAndSet(false, true)) {
                                            vrijeme = dateFormat.format(new Date());
                                            poruka.setKomanda(komanda);
                                            poruka.setVrijeme(vrijeme);
                                            PosluziteljskiJMS.PosaljiPoruku(poruka);
                                            unesiUDnevnik("PASIVNO", "KOMANDE", 1, korisnik.getKorisnickoIme(), "Poslana komanda " + komanda);
                                            posaljiOdgovor("OK 10;");
                                        } else {
                                            posaljiOdgovor("ERR 14;");
                                        }
                                        break;
                                    case " AKTIVNO":
                                        if (PreuzimanjeAviona.prekidPreuzimanja.compareAndSet(true, false)) {
                                            vrijeme = dateFormat.format(new Date());
                                            poruka.setKomanda(komanda);
                                            poruka.setVrijeme(vrijeme);
                                            PosluziteljskiJMS.PosaljiPoruku(poruka);
                                            unesiUDnevnik("AKTIVNO", "KOMANDE", 1, korisnik.getKorisnickoIme(), "Poslana komanda " + komanda);
                                            posaljiOdgovor("OK 10;");
                                        } else {
                                            posaljiOdgovor("ERR 15;");
                                        }
                                        break;
                                    case " STANI":
                                        if (PreuzimanjeAviona.prekidPreuzimanja.compareAndSet(false, true)
                                                && ServerAerodroma.kraj.compareAndSet(false, true)) {
                                            vrijeme = dateFormat.format(new Date());
                                            poruka.setKomanda(komanda);
                                            poruka.setVrijeme(vrijeme);
                                            PosluziteljskiJMS.PosaljiPoruku(poruka);
                                            unesiUDnevnik("STANI", "KOMANDE", 1, korisnik.getKorisnickoIme(), "Poslana komanda " + komanda);
                                            posaljiOdgovor("OK 10;");
                                        } else {
                                            posaljiOdgovor("ERR 15;");
                                        }
                                        break;
                                    case " STANJE":
                                        if (PreuzimanjeAviona.prekidPreuzimanja.get() == false && ServerAerodroma.prekidGrupa.get() == false) {
                                            vrijeme = dateFormat.format(new Date());
                                            poruka.setKomanda(komanda);
                                            poruka.setVrijeme(vrijeme);
                                            PosluziteljskiJMS.PosaljiPoruku(poruka);
                                            unesiUDnevnik("STANJE", "KOMANDE", 1, korisnik.getKorisnickoIme(), "Poslana komanda " + komanda);
                                            posaljiOdgovor("OK 11;");
                                        } else if (ServerAerodroma.prekidGrupa.get() == false && PreuzimanjeAviona.prekidPreuzimanja.get() == true) {
                                            vrijeme = dateFormat.format(new Date());
                                            poruka.setKomanda(komanda);
                                            poruka.setVrijeme(vrijeme);
                                            PosluziteljskiJMS.PosaljiPoruku(poruka);
                                            unesiUDnevnik("STANJE", "KOMANDE", 1, korisnik.getKorisnickoIme(), "Poslana komanda " + komanda);
                                            posaljiOdgovor("OK 12;");
                                        } else if (ServerAerodroma.prekidGrupa.get() == true && PreuzimanjeAviona.prekidPreuzimanja.get() == false) {
                                            vrijeme = dateFormat.format(new Date());
                                            poruka.setKomanda(komanda);
                                            poruka.setVrijeme(vrijeme);
                                            PosluziteljskiJMS.PosaljiPoruku(poruka);
                                            unesiUDnevnik("STANJE", "KOMANDE", 1, korisnik.getKorisnickoIme(), "Poslana komanda " + komanda);
                                            posaljiOdgovor("OK 13;");
                                        } else if (ServerAerodroma.prekidGrupa.get() == true && PreuzimanjeAviona.prekidPreuzimanja.get() == true) {
                                            vrijeme = dateFormat.format(new Date());
                                            poruka.setKomanda(komanda);
                                            poruka.setVrijeme(vrijeme);
                                            PosluziteljskiJMS.PosaljiPoruku(poruka);
                                            unesiUDnevnik("STANJE", "KOMANDE", 1, korisnik.getKorisnickoIme(), "Poslana komanda " + komanda);
                                            posaljiOdgovor("OK 14;");
                                        } else {
                                            posaljiOdgovor("ERR 17;");
                                        }
                                        break;
                                    default:
                                        break;
                                }
                            } else {
                                posaljiOdgovor("ERR 11;");
                            }
                            break;
                        default:
                            posaljiOdgovor("ERR 20;");
                            break;
                    }
                    break;
                case 2:
                    if (!ServerAerodroma.prekidGrupa.get()) {
                        String subversionKorisnickoIme = SlusacAplikacije.getKonfiguracija().dajPostavku("subversion.korisnickoIme");
                        String subversionLozinka = SlusacAplikacije.getKonfiguracija().dajPostavku("subversion.lozinka");
                        if (NWTiS_2019Klijent.autenticirajGrupu(subversionKorisnickoIme, subversionLozinka)) {
                            switch (duljinaKomande) {
                                case 2:
                                    if (korisnik.getLozinka() != null && korisnik.getKorisnickoIme() != null) {
                                        unesiUDnevnik("Korisnik i lozinka", "KOMANDE", 1, korisnik.getKorisnickoIme(), "Poslana komanda " + komanda);
                                        posaljiOdgovor("OK;");
                                    } else {
                                        posaljiOdgovor("ERR 11;");
                                    }
                                    break;
                                case 3:
                                    if (korisnik.getLozinka() != null && korisnik.getKorisnickoIme() != null) {
                                        String treciDio = polje[2];
                                        switch (treciDio) {
                                            case " GRUPA DODAJ":
                                                if (NWTiS_2019Klijent.registrirajGrupu(subversionKorisnickoIme, subversionLozinka)) {
                                                    unesiUDnevnik("GRUPA DODAJ", "KOMANDE", 1, korisnik.getKorisnickoIme(), "Poslana komanda " + komanda);
                                                    posaljiOdgovor("OK 20;");
                                                } else {
                                                    posaljiOdgovor("ERR 20;");
                                                }
                                                break;
                                            case " GRUPA PREKID":
                                                if (NWTiS_2019Klijent.deregistrirajGrupu(subversionKorisnickoIme, subversionLozinka)) {
                                                    unesiUDnevnik("GRUPDA PREKID", "KOMANDE", 1, korisnik.getKorisnickoIme(), "Poslana komanda " + komanda);
                                                    posaljiOdgovor("OK 20;");
                                                } else {
                                                    posaljiOdgovor("ERR 21;");
                                                }
                                                break;
                                            case " GRUPA KRENI":
                                                if (NWTiS_2019Klijent.registrirajGrupu(subversionKorisnickoIme, subversionLozinka)) {
                                                    if (NWTiS_2019Klijent.aktivirajGrupu(subversionKorisnickoIme, subversionLozinka)) {
                                                        unesiUDnevnik("GRUPA KRENI", "KOMANDE", 1, korisnik.getKorisnickoIme(), "Poslana komanda " + komanda);
                                                        posaljiOdgovor("OK 20;");
                                                    } else {
                                                        posaljiOdgovor("ERR 21;");
                                                    }
                                                } else {
                                                    posaljiOdgovor("ERR 22;");
                                                }
                                                break;
                                            case " GRUPA PAUZA":
                                                if (NWTiS_2019Klijent.registrirajGrupu(subversionKorisnickoIme, subversionLozinka)) {
                                                    if (NWTiS_2019Klijent.blokirajGrupu(subversionKorisnickoIme, subversionLozinka)) {
                                                        unesiUDnevnik("GRUPA PAUZA", "KOMANDE", 1, korisnik.getKorisnickoIme(), "Poslana komanda " + komanda);
                                                        posaljiOdgovor("OK 20;");
                                                    } else {
                                                        posaljiOdgovor("ERR 22;");
                                                    }
                                                } else {
                                                    posaljiOdgovor("ERR 21;");
                                                }

                                                break;
                                            case " GRUPA STANJE":
                                                switch (NWTiS_2019Klijent.dajStatusGrupe(subversionKorisnickoIme, subversionLozinka)) {
                                                    case AKTIVAN:
                                                        unesiUDnevnik("GRUPA STANJE", "KOMANDE", 1, korisnik.getKorisnickoIme(), "Poslana komanda " + komanda);
                                                        posaljiOdgovor("OK 21;");
                                                        break;
                                                    case BLOKIRAN:
                                                        unesiUDnevnik("GRUPA STANJE", "KOMANDE", 1, korisnik.getKorisnickoIme(), "Poslana komanda " + komanda);
                                                        posaljiOdgovor("OK 22;");
                                                        break;
                                                    default:
                                                        posaljiOdgovor("ERR 21;");
                                                        break;
                                                }
                                                break;
                                            default:
                                                posaljiOdgovor("ERR 404;");
                                                break;
                                        }
                                    }
                                    break;
                                default:
                                    posaljiOdgovor("ERR 20;");
                                    break;
                            }
                        } else {
                            posaljiOdgovor("ERR 11;");
                        }
                    } else {
                        posaljiOdgovor("ERR 12;");
                    }
                    break;
                default:
                    posaljiOdgovor("ERR 404;");
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public synchronized void start() {
        super.start(); //To change body of generated methods, choose Tools | Templates.
    }

    public StringBuffer dohvatiZahtjev() {
        StringBuffer output = null;
        InputStream is;
        try {
            is = socket.getInputStream();
            DataInputStream in = new DataInputStream((is));
            output = new StringBuffer();
            while (true) {
                try {
                    String odgovor = in.readUTF();
                    output.append(odgovor);
                } catch (EOFException ex) {
                    break;
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(DretvaZahtjeva.class.getName()).log(Level.SEVERE, null, ex);
        }
        return output;
    }

    public void posaljiOdgovor(String odgovor) {
        try {
            OutputStream os = socket.getOutputStream();
            DataOutputStream out = new DataOutputStream(os);
            out.writeUTF(odgovor);
            out.flush();
            if (out != null) {
                out.close();
            }
            socket.close();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public boolean ispravnaSintaksaKomandePosluzitelja() {
        boolean output = false;
        String[] polje = komanda.split(";");
        int duljina = komanda.split(";").length;
        switch (duljina) {
            case 2:
                if (polje[0].startsWith("KORISNIK") && polje[1].startsWith(" LOZINKA")) {
                    output = true;
                }
                break;
            case 3:
                if (polje[0].startsWith("KORISNIK") && polje[1].contains(" LOZINKA")
                        && (polje[2].equals(" PAUZA") || polje[2].equals(" KRENI")
                        || polje[2].equals(" PASIVNO")
                        || polje[2].equals(" AKTIVNO")
                        || polje[2].equals(" STANI")
                        || polje[2].equals(" STANJE"))) {
                    output = true;
                }
                break;
            default:
                output = false;
                break;
        }
        return output;
    }

    public int vrstaKomande() {
        int output = 0;
        if (ispravnaSintaksaKomandePosluzitelja()) {
            output = 1;
        } else if (ispravnaSintaksaKomandeGrupe()) {
            output = 2;
        } else {
            output = 0;
        }
        return output;
    }

    private boolean ispravnaSintaksaKomandeGrupe() {
        boolean output = false;
        String[] polje = komanda.split(";");
        int duljina = komanda.split(";").length;
        switch (duljina) {
            case 2:
                if (polje[0].startsWith("KORISNIK") && polje[1].startsWith(" LOZINKA")) {
                    output = true;
                }
                break;
            case 3:
                if (polje[0].startsWith("KORISNIK") && polje[1].startsWith(" LOZINKA")
                        && (polje[2].equals(" GRUPA DODAJ") || polje[2].equals(" GRUPA PREKID")
                        || polje[2].equals(" GRUPA KRENI")
                        || polje[2].equals(" GRUPA PAUZA")
                        || polje[2].equals(" GRUPA STANJE"))) {
                    output = true;
                }
                break;
            default:
                output = false;
                break;
        }
        return output;
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
