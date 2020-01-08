/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.jmarijano.web.servisi.rs;

import com.google.gson.Gson;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.ServletContext;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PUT;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import org.foi.nwtis.jmarijano.jsonklase.ErrorJson;
import org.foi.nwtis.jmarijano.jsonklase.ListJson;
import org.foi.nwtis.jmarijano.jsonklase.ObjectJson;
import org.foi.nwtis.jmarijano.modeli.Aplikacija_1Klijent;
import org.foi.nwtis.jmarijano.modeli.Korisnik;
import org.foi.nwtis.jmarijano.modeli.KorisnikConverter;
import org.foi.nwtis.jmarijano.mysql.KorisniciMySQL;
import org.foi.nwtis.jmarijano.sucelja.KorisnikService;
import org.foi.nwtis.jmarijano.web.servisi.rs.zahtjevi.GetJson;
import org.foi.nwtis.jmarijano.web.servisi.rs.zahtjevi.PostJson;
import org.foi.nwtis.jmarijano.web.servisi.rs.zahtjevi.Put;
import org.foi.nwtis.jmarijano.web.slusaci.SlusacAplikacije;

/**
 * REST Web Service
 *
 * @author LiterallyCan't
 */
@Path("korisnici")
public class AIRP2REST {

    @Context
    private UriInfo context;
    private Korisnik korisnik;
    private final KorisnikService korisniciService;
    private final ServletContext servletContext;
    private List<Korisnik> listaKorisnika;
    private final ErrorJson errorJson;
    private String odgovor;
    private final Gson gson;
    private final ListJson<Korisnik> listKorisnikJson;
    private final ObjectJson<Object> korisnikJson;
    private PostJson postJson;
    private GetJson getJson;
    private Put put;

    /**
     * Creates a new instance of AIRP2REST
     */
    public AIRP2REST() {
        servletContext = SlusacAplikacije.getSc();
        korisniciService = new KorisniciMySQL(servletContext);
        listaKorisnika = new ArrayList<>();
        errorJson = new ErrorJson();
        odgovor = "";
        gson = new Gson();
        listKorisnikJson = new ListJson<>();
        korisnikJson = new ObjectJson<>();
        korisnik = new Korisnik();
        postJson = new PostJson();
        getJson = new GetJson();
        put = new Put();
    }

    /**
     * Retrieves representation of an instance of
     * org.foi.nwtis.jmarijano.web.servisi.rs.AIRP2REST
     *
     * @return an instance of java.lang.String
     */
    @GET
    @Produces(javax.ws.rs.core.MediaType.APPLICATION_JSON)
    public String getJson(@QueryParam("korisnickoIme") String korisnickoIme, @QueryParam("lozinka") String lozinka) {
        try {
            List<org.foi.nwtis.jmarijano.web.servisi.ws.Korisnik> izServisa = Aplikacija_1Klijent.dajSveKorisnike(korisnickoIme, lozinka);
            listaKorisnika = KorisnikConverter.convert(izServisa);
            if (!listaKorisnika.isEmpty()) {
                listKorisnikJson.setOdgovor(listaKorisnika);
                odgovor = gson.toJson(listKorisnikJson);
                return odgovor;
            } else {
                errorJson.setPoruka("Ne postoje korisnici u bazi podataka");
                odgovor = gson.toJson(errorJson);
                return odgovor;
            }
        } catch (Exception e) {
            errorJson.setPoruka("Dogodila se greška");
            odgovor = gson.toJson(errorJson);
            return odgovor;
        }
    }

    @Path("{korisnickoIme}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String getJsonId(@PathParam("korisnickoIme") String korisnickoIme, @QueryParam("korime") String korime, @QueryParam("lozinka") String lozinka) {
        try {
            List<org.foi.nwtis.jmarijano.web.servisi.ws.Korisnik> izServisa = Aplikacija_1Klijent.dajSveKorisnike(korime, lozinka);
            listaKorisnika = KorisnikConverter.convert(izServisa);
            for (Korisnik korisnik : listaKorisnika) {
                if (korisnik.getKorisnickoIme().equals(korisnickoIme)) {
                    korisnik.setLozinka("");
                    korisnikJson.setOdgovor(korisnik);
                    odgovor = gson.toJson(korisnikJson);
                    return odgovor;
                }
            }
            errorJson.setPoruka("Ne postoji korisnik s dobivenim korisničkim imenom u bazi podataka koji ima dobivenu lozinku");
            odgovor = gson.toJson(errorJson);
            return odgovor;
        } catch (Exception e) {
            e.printStackTrace();
            errorJson.setPoruka("Dogodila se greška");
            odgovor = gson.toJson(errorJson);
            return odgovor;
        }
    }

    /**
     *
     * @param id
     * @param zahtjev
     * @return
     */
    @Path("/aut/{korisnickoIme}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String getJsonIdParam(@PathParam("korisnickoIme") String korisnickoIme, @QueryParam("auth") String zahtjev) {
        try {
            getJson = gson.fromJson(zahtjev, GetJson.class);
            List<org.foi.nwtis.jmarijano.web.servisi.ws.Korisnik> izServisa = Aplikacija_1Klijent.dajSveKorisnike(korisnickoIme, getJson.lozinka);
            listaKorisnika = KorisnikConverter.convert(izServisa);
            for (Korisnik korisnik : listaKorisnika) {
                if (korisnik.getKorisnickoIme().equals(korisnickoIme) && korisnik.getLozinka().equals(getJson.lozinka)) {
                    korisnikJson.setOdgovor(new Object());
                    odgovor = gson.toJson(korisnikJson);
                    return odgovor;
                }
            }
            errorJson.setPoruka("Ne postoji korisnik s dobivenim korisničkim imenom u bazi podataka koji ima dobivenu lozinku");
            odgovor = gson.toJson(errorJson);
            return odgovor;
        } catch (Exception e) {
            e.printStackTrace();
            errorJson.setPoruka("Dogodila se greška");
            odgovor = gson.toJson(errorJson);
            return odgovor;
        }
    }

    /**
     * PUT method for updating or creating an instance of AIRP2REST
     *
     * @param content representation for the resource
     */
    @Path("{korisnickoIme}")
    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public String azurirajKorisnika(@PathParam("korisnickoIme") String korisnickoIme, String zahtjev) {
        try {
            put = gson.fromJson(zahtjev, Put.class);
            org.foi.nwtis.jmarijano.web.servisi.ws.Korisnik noviKorisnik = new org.foi.nwtis.jmarijano.web.servisi.ws.Korisnik();
            noviKorisnik.setEmail(put.email);
            noviKorisnik.setIme(put.ime);
            noviKorisnik.setLozinka(put.lozinka);
            noviKorisnik.setPrezime(put.prezime);
            noviKorisnik.setKorisnickoIme(korisnickoIme);
            String lozinka = put.staraLozinka;
            if (Aplikacija_1Klijent.azurirajKorisnika(korisnickoIme, lozinka, noviKorisnik)) {
                korisnikJson.setOdgovor(new Object());
                odgovor = gson.toJson(korisnikJson);
                return odgovor;
            } else {
                errorJson.setPoruka("Dogodila se greška prilikom ažuriranja postojećeg korisnika");
                odgovor = gson.toJson(errorJson);
                return odgovor;
            }
        } catch (Exception e) {
            errorJson.setPoruka("Dogodila se greška");
            odgovor = gson.toJson(errorJson);
            return odgovor;
        }
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public String postJson(String zahtjev) {
        try {
            postJson = gson.fromJson(zahtjev, PostJson.class);
            org.foi.nwtis.jmarijano.web.servisi.ws.Korisnik noviKorisnik = new org.foi.nwtis.jmarijano.web.servisi.ws.Korisnik();
            noviKorisnik.setEmail(postJson.email);
            noviKorisnik.setIme(postJson.ime);
            noviKorisnik.setKorisnickoIme(postJson.korisnickoIme);
            noviKorisnik.setLozinka(postJson.lozinka);
            noviKorisnik.setPrezime(postJson.prezime);
            if (Aplikacija_1Klijent.dodajKorisnika(noviKorisnik)) {
                korisnikJson.setOdgovor(new Object());
                odgovor = gson.toJson(korisnikJson);
                return odgovor;
            } else {
                errorJson.setPoruka("Dogodila se greška prilikom dodavanja novog korisnika");
                odgovor = gson.toJson(errorJson);
                return odgovor;
            }
        } catch (Exception e) {
            errorJson.setPoruka("Dogodila se greška");
            odgovor = gson.toJson(errorJson);
            return odgovor;
        }
    }
}
