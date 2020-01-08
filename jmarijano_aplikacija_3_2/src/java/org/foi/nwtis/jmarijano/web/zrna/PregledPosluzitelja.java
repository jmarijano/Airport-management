/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.jmarijano.web.zrna;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;
import org.foi.nwtis.jmarijano.Komande;
import org.foi.nwtis.jmarijano.sb.StatefulSBApp3_1;
import org.foi.nwtis.jmarijano.web.slusaci.SlusacAplikacije;

/**
 *
 * @author LiterallyCan't
 */
@Named(value = "pregledPosluzitelja")
@SessionScoped
public class PregledPosluzitelja implements Serializable {

    @EJB
    private StatefulSBApp3_1 statefulSB;
    private String komanda;
    private String poruka;
    private Komande komande;
    private final int port;
    private String korisnickoIme;
    private String lozinka;
    private HttpSession sesija;

    /**
     * Creates a new instance of PregledPosluzitelja
     */
    public PregledPosluzitelja() {
        port = Integer.parseInt(SlusacAplikacije.getKonfiguracija().dajPostavku("port"));

    }

    public String getPoruka() {
        return poruka;
    }

    public void setPoruka(String poruka) {
        this.poruka = poruka;
    }

    public String posaljiKorisnika() {
        try {
            sesija = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);
            korisnickoIme = sesija.getAttribute("korisnik").toString();
            lozinka = sesija.getAttribute("lozinka").toString();
            if (statefulSB.autenticiraj(korisnickoIme, lozinka)) {
                posaljiKomandu("KORISNIK " + korisnickoIme + "; LOZINKA " + lozinka);
            } else {
                poruka = "Potrebno se prijaviti";
            }
        } catch (Exception e) {
            e.printStackTrace();
            poruka = "Dogodila se greška";
        }
        return "";
    }

    public String posaljiPauzu() {
        try {
            sesija = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);
            korisnickoIme = sesija.getAttribute("korisnik").toString();
            lozinka = sesija.getAttribute("lozinka").toString();
            if (statefulSB.autenticiraj(korisnickoIme, lozinka)) {
                posaljiKomandu("KORISNIK " + korisnickoIme + "; LOZINKA " + lozinka + "; PAUZA");
            } else {
                poruka = "Potrebno se prijaviti";
            }
        } catch (Exception e) {
            e.printStackTrace();
            poruka = "Dogodila se greška";
        }
        return "";
    }

    public String posaljiKreni() {
        try {
            sesija = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);
            korisnickoIme = sesija.getAttribute("korisnik").toString();
            lozinka = sesija.getAttribute("lozinka").toString();
            if (statefulSB.autenticiraj(korisnickoIme, lozinka)) {
                posaljiKomandu("KORISNIK " + korisnickoIme + "; LOZINKA " + lozinka + "; KRENI");
            } else {
                poruka = "Potrebno se prijaviti";
            }
        } catch (Exception e) {
            e.printStackTrace();
            poruka = "Dogodila se greška";
        }
        return "";
    }

    public String posaljiPasivno() {
        try {
            sesija = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);
            korisnickoIme = sesija.getAttribute("korisnik").toString();
            lozinka = sesija.getAttribute("lozinka").toString();
            if (statefulSB.autenticiraj(korisnickoIme, lozinka)) {
                posaljiKomandu("KORISNIK " + korisnickoIme + "; LOZINKA " + lozinka + "; PASIVNO");
            } else {
                poruka = "Potrebno se prijaviti";
            }
        } catch (Exception e) {
            e.printStackTrace();
            poruka = "Dogodila se greška";
        }
        return "";
    }

    public String posaljiAktivno() {
        try {
            sesija = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);
            korisnickoIme = sesija.getAttribute("korisnik").toString();
            lozinka = sesija.getAttribute("lozinka").toString();
            if (statefulSB.autenticiraj(korisnickoIme, lozinka)) {
                posaljiKomandu("KORISNIK " + korisnickoIme + "; LOZINKA " + lozinka + "; AKTIVNO");
            } else {
                poruka = "Potrebno se prijaviti";
            }
        } catch (Exception e) {
            e.printStackTrace();
            poruka = "Dogodila se greška";
        }
        return "";
    }

    public String posaljiStani() {
        try {
            sesija = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);
            korisnickoIme = sesija.getAttribute("korisnik").toString();
            lozinka = sesija.getAttribute("lozinka").toString();
            if (statefulSB.autenticiraj(korisnickoIme, lozinka)) {
                posaljiKomandu("KORISNIK " + korisnickoIme + "; LOZINKA " + lozinka + "; STANI");
            } else {
                poruka = "Potrebno se prijaviti";
            }
        } catch (Exception e) {
            e.printStackTrace();
            poruka = "Dogodila se greška";
        }
        return "";
    }

    public String posaljiStanje() {
        try {
            sesija = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);
            korisnickoIme = sesija.getAttribute("korisnik").toString();
            lozinka = sesija.getAttribute("lozinka").toString();
            if (statefulSB.autenticiraj(korisnickoIme, lozinka)) {
                posaljiKomandu("KORISNIK " + korisnickoIme + "; LOZINKA " + lozinka + "; STANJE");
            } else {
                poruka = "Potrebno se prijaviti";
            }
        } catch (Exception e) {
            e.printStackTrace();
            poruka = "Dogodila se greška";
        }
        return "";
    }

    public void posaljiKomandu(String komanda) {
        Socket socket = null;
        InputStream is = null;
        DataInputStream in = null;
        OutputStream os = null;
        DataOutputStream out = null;
        try {
            String adresa = SlusacAplikacije.getKonfiguracija().dajPostavku("adresa");
            int port = Integer.parseInt(SlusacAplikacije.getKonfiguracija().dajPostavku("port"));
            socket = new Socket(adresa, port);
            is = socket.getInputStream();
            in = new DataInputStream(is);
            os = socket.getOutputStream();
            out = new DataOutputStream(os);
            out.writeUTF(komanda);
            out.flush();
            socket.shutdownOutput();
            StringBuffer sb = new StringBuffer();
            while (true) {
                try {
                    String odgovor = in.readUTF();
                    sb.append(odgovor);
                } catch (EOFException ex) {
                    break;
                }
            }
            poruka = sb.toString();
        } catch (IOException ex) {
            Logger.getLogger(PregledGrupe.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (is != null) {
                    is.close();
                }
                if (os != null) {
                    os.close();
                }
                if (in != null) {
                    in.close();
                }
                if (out != null) {
                    out.close();
                }
                socket.close();
            } catch (IOException ex) {
                System.out.println("ERROR; Problemi kod zatvaranja socketa/streamova");
            }
        }
    }
}
