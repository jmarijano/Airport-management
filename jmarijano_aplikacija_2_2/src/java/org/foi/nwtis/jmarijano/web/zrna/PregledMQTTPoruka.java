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
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;
import org.foi.nwtis.jmarijano.eb.MqttPoruke;
import org.foi.nwtis.jmarijano.sb.MqttPorukeFacade;
import org.foi.nwtis.jmarijano.sb.StatefulSBApp2_1;
import org.foi.nwtis.jmarijano.slusaci.SlusacAplikacije;

/**
 *
 * @author LiterallyCan't
 */
@Named(value = "pregledMQTTPoruka")
@SessionScoped
public class PregledMQTTPoruka implements Serializable {

    @EJB
    private StatefulSBApp2_1 statefulSB;
    @EJB
    private MqttPorukeFacade mqttPorukeFacade;
    private String poruka;
    private List<MqttPoruke> listaPoruka;
    private int brojLinija;

    public String getPoruka() {
        return poruka;
    }

    public void setPoruka(String poruka) {
        this.poruka = poruka;
    }

    /**
     * Creates a new instance of PregledMQTTPoruka
     */
    public PregledMQTTPoruka() {
        listaPoruka = new ArrayList<>();

    }

    public List<MqttPoruke> getListaPoruka() {
        try {
            HttpSession sesija = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
            String korisnickoIme = sesija.getAttribute("korisnik").toString();
            String lozinka = sesija.getAttribute("lozinka").toString();
            if (statefulSB.autenticiraj(korisnickoIme, lozinka)) {
                listaPoruka = mqttPorukeFacade.dajSvePorukeKorisnika(SlusacAplikacije.getKorisnik());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return listaPoruka;
    }

    public void setListaPoruka(List<MqttPoruke> listaPoruka) {
        this.listaPoruka = listaPoruka;
    }

    public String posalji() {
        try {
            HttpSession sesija = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);
            String korisnickoIme = sesija.getAttribute("korisnik").toString();
            String lozinka = sesija.getAttribute("lozinka").toString();
            if (statefulSB.autenticiraj(korisnickoIme, lozinka)) {
                statefulSB.pokreniMQTTSlusaca(SlusacAplikacije.getKonfiguracija(), mqttPorukeFacade);
                posaljiKomandu("KORISNIK " + korisnickoIme + "; LOZINKA " + lozinka + "; GRUPA DODAJ");

            } else {
                poruka = "Potrebno se prijaviti";
            }
        } catch (Exception e) {
            e.printStackTrace();
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
            System.out.println("Poruka:" + poruka);
        } catch (IOException ex) {
            Logger.getLogger(PregledMQTTPoruka.class.getName()).log(Level.SEVERE, null, ex);
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
            }
        }
    }

    public String obrisiSve() {
        if (mqttPorukeFacade.obrisiSvePorukeKorisnika(SlusacAplikacije.getKorisnik())) {
            poruka = "Obrisani sve poruke korisnika";
        } else {
            poruka = "Dogodila se greška";
        }
        return "";
    }

    public int getBrojLinija() {
        try {
            brojLinija = Integer.parseInt(SlusacAplikacije.getKonfiguracija().dajPostavku("mqtt.brojLinija"));
        } catch (Exception e) {
            e.printStackTrace();
            brojLinija = 5;
        }
        return brojLinija;
    }

    public void setBrojLinija(int brojLinija) {
        this.brojLinija = brojLinija;
    }

    public String posaljiDereg() {
        try {
            HttpSession sesija = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);
            String korisnickoIme = sesija.getAttribute("korisnik").toString();
            String lozinka = sesija.getAttribute("lozinka").toString();
            if (statefulSB.autenticiraj(korisnickoIme, lozinka)) {
                statefulSB.pokreniMQTTSlusaca(SlusacAplikacije.getKonfiguracija(), mqttPorukeFacade);
                posaljiKomandu("KORISNIK " + korisnickoIme + "; LOZINKA " + lozinka + "; GRUPA PREKID");
                statefulSB.zaustaviSlusaca();
            } else {
                poruka = "Potrebno se prijaviti";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }
}
