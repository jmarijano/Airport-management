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
import org.foi.nwtis.jmarijano.sb.StatefulSBApp3_1;
import org.foi.nwtis.jmarijano.web.slusaci.SlusacAplikacije;

@Named(value = "pregledGrupe")
@SessionScoped
public class PregledGrupe implements Serializable {

    @EJB
    private StatefulSBApp3_1 statefulSB;
    private String poruka;
    private String korisnickoIme;
    private String lozinka;
    private HttpSession sesija;

    /**
     * Creates a new instance of PregledGrupe
     */
    public PregledGrupe() {
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
        } catch (IllegalStateException e) {
            e.printStackTrace();
            poruka = "Potrebno se prijaviti";
        } catch (Exception e) {
            e.printStackTrace();
            poruka = "Dogodila se greška";
        }
        return "";
    }

    public String posaljiDodaj() {
        try {
            sesija = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);
            korisnickoIme = sesija.getAttribute("korisnik").toString();
            lozinka = sesija.getAttribute("lozinka").toString();
            if (statefulSB.autenticiraj(korisnickoIme, lozinka)) {
                posaljiKomandu("KORISNIK " + korisnickoIme + "; LOZINKA " + lozinka + "; GRUPA DODAJ");
            } else {
                poruka = "Potrebno se prijaviti";
            }
        } catch (Exception e) {
            e.printStackTrace();
            poruka = "Dogodila se greška";
        }
        return "";
    }

    public String posaljiPrekid() {
        try {
            sesija = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);
            korisnickoIme = sesija.getAttribute("korisnik").toString();
            lozinka = sesija.getAttribute("lozinka").toString();
            if (statefulSB.autenticiraj(korisnickoIme, lozinka)) {
                posaljiKomandu("KORISNIK " + korisnickoIme + "; LOZINKA " + lozinka + "; GRUPA PREKID");
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
                posaljiKomandu("KORISNIK " + korisnickoIme + "; LOZINKA " + lozinka + "; GRUPA KRENI");
            } else {
                poruka = "Potrebno se prijaviti";
            }
        } catch (Exception e) {
            e.printStackTrace();
            poruka = "Dogodila se greška";
        }
        return "";
    }

    public String posaljiPauza() {
        try {
            sesija = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);
            korisnickoIme = sesija.getAttribute("korisnik").toString();
            lozinka = sesija.getAttribute("lozinka").toString();
            if (statefulSB.autenticiraj(korisnickoIme, lozinka)) {
                posaljiKomandu("KORISNIK " + korisnickoIme + "; LOZINKA " + lozinka + "; GRUPA PAUZA");
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
                posaljiKomandu("KORISNIK " + korisnickoIme + "; LOZINKA " + lozinka + "; GRUPA STANJE");
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
