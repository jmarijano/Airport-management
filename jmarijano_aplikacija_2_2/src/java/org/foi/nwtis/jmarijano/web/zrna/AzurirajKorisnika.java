/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.jmarijano.web.zrna;

import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.ejb.EJB;
import javax.faces.context.FacesContext;
import javax.json.Json;
import javax.json.JsonObject;
import javax.servlet.http.HttpSession;
import org.foi.nwtis.jmarijano.sb.StatefulSBApp2_1;
import org.foi.nwtis.jmarijano.web.rs.klijent.AIRP2REST_JerseyClient;

/**
 *
 * @author LiterallyCan't
 */
@Named(value = "azurirajKorisnika")
@SessionScoped
public class AzurirajKorisnika implements Serializable {

    @EJB
    private StatefulSBApp2_1 statefulSB;
    private String lozinka;
    private String ponovnaLozinka;
    private String ime;
    private String prezime;
    private String email;
    private final AIRP2REST_JerseyClient aIRP2REST_JerseyClient;
    private String poruka;
    private JsonObject jsonObject;
    private final Pattern VALID_EMAIL_ADDRESS_REGEX
            = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$",
                    Pattern.CASE_INSENSITIVE);

    /**
     * Creates a new instance of AzurirajKorisnika
     */
    public AzurirajKorisnika() {
        aIRP2REST_JerseyClient = new AIRP2REST_JerseyClient();

    }

    public String getLozinka() {
        return lozinka;
    }

    public void setLozinka(String lozinka) {
        this.lozinka = lozinka;
    }

    public String getPonovnaLozinka() {
        return ponovnaLozinka;
    }

    public void setPonovnaLozinka(String ponovnaLozinka) {
        this.ponovnaLozinka = ponovnaLozinka;
    }

    public String getIme() {
        return ime;
    }

    public void setIme(String ime) {
        this.ime = ime;
    }

    public String getPrezime() {
        return prezime;
    }

    public void setPrezime(String prezime) {
        this.prezime = prezime;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPoruka() {
        return poruka;
    }

    public void setPoruka(String poruka) {
        this.poruka = poruka;
    }

    public String azuriraj() {
        try {
            HttpSession sesija = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
            if (statefulSB.autenticiraj(sesija.getAttribute("korisnik").toString(), sesija.getAttribute("lozinka").toString())) {
                if (lozinka.equals(ponovnaLozinka) && lozinka.length() >= 5 && validate(email)) {
                    jsonObject = Json.createObjectBuilder()
                            .add("lozinka", lozinka)
                            .add("ime", ime)
                            .add("prezime", prezime)
                            .add("email", email)
                            .add("staraLozinka", sesija.getAttribute("lozinka").toString())
                            .build();
                    String odgovor = aIRP2REST_JerseyClient.azurirajKorisnika(jsonObject.toString(), sesija.getAttribute("korisnik").toString());
                    if (odgovor.contains("OK")) {
                        poruka = "Uspješno ažurirani podaci";
                        sesija.setAttribute("lozinka", lozinka);
                    }

                } else {
                    poruka = "Unesene lozinke nisu iste ili lozinka nije 5 ili mail ne zadovoljava regex";
                }
            } else {
                poruka = "Niste se prijavili";
            }
        } catch (Exception e) {
            e.printStackTrace();
            poruka = "Dogodila se pogreška";
        }

        return "";
    }

    public boolean validate(String emailStr) {
        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX.matcher(emailStr);
        return matcher.find();
    }
}
