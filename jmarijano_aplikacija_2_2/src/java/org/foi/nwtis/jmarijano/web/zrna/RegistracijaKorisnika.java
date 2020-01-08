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
import javax.json.Json;
import javax.json.JsonObject;
import org.foi.nwtis.jmarijano.web.rs.klijent.AIRP2REST_JerseyClient;
import org.primefaces.json.JSONObject;

/**
 *
 * @author LiterallyCan't
 */
@Named(value = "registracijaKorisnika")
@SessionScoped
public class RegistracijaKorisnika implements Serializable {

    private String korisnickoIme;
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
     * Creates a new instance of RegistracijaKorisnika
     */
    public RegistracijaKorisnika() {
        aIRP2REST_JerseyClient = new AIRP2REST_JerseyClient();
    }

    public String getKorisnickoIme() {
        return korisnickoIme;
    }

    public void setKorisnickoIme(String korisnickoIme) {
        this.korisnickoIme = korisnickoIme;
    }

    public String getLozinka() {
        return lozinka;
    }

    public void setLozinka(String lozinka) {
        this.lozinka = lozinka;
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

    public String getPonovnaLozinka() {
        return ponovnaLozinka;
    }

    public void setPonovnaLozinka(String ponovnaLozinka) {
        this.ponovnaLozinka = ponovnaLozinka;
    }

    public String getPoruka() {
        return poruka;
    }

    public void setPoruka(String poruka) {
        this.poruka = poruka;
    }

    public String registriraj() {
        try {
            if (!ponovnaLozinka.equals(lozinka)
                    || lozinka.length() < 5
                    || korisnickoIme.length() < 5
                    || !validate(email)) {
                poruka = "Pogrešno uneseni podaci";
                return "";
            }
            jsonObject = Json.createObjectBuilder()
                    .add("korisnickoIme", korisnickoIme)
                    .add("lozinka", lozinka)
                    .add("ime", ime)
                    .add("prezime", prezime)
                    .add("email", email)
                    .build();
            String jsonPoruka = aIRP2REST_JerseyClient.postJson(jsonObject.toString());
            JSONObject obj = new JSONObject(jsonPoruka);
            String status = obj.getString("status");
            if (status.equals("OK")) {
                poruka = "Uspješno registriraj korisnik";
            } else if (status.equals("ERR")) {
                poruka = obj.getString("poruka");
            } else {
                poruka = "Nešto";
            }
        } catch (Exception e) {
            e.printStackTrace();
            poruka = "Dogodila se greška prilikom registracije korisnika";
        }
        return "";
    }

    public boolean validate(String emailStr) {
        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX.matcher(emailStr);
        return matcher.find();
    }

}
