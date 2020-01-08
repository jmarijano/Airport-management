/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.jmarijano.web.servisi.rs;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.servlet.ServletContext;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.Produces;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PUT;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import org.foi.nwtis.jmarijano.helperi.LocationIQHelper;
import org.foi.nwtis.jmarijano.jsonklase.ErrorJson;
import org.foi.nwtis.jmarijano.jsonklase.ListJson;
import org.foi.nwtis.jmarijano.jsonklase.ObjectJson;
import org.foi.nwtis.jmarijano.jsonklase.PostJson;
import org.foi.nwtis.jmarijano.jsonklase.PutJson;
import org.foi.nwtis.jmarijano.konfiguracije.Konfiguracija;
import org.foi.nwtis.jmarijano.modeli.Aerodrom;
import org.foi.nwtis.jmarijano.modeli.Dnevnik;
import org.foi.nwtis.jmarijano.modeli.Korisnik;
import org.foi.nwtis.jmarijano.mysql.AirplanesMySQL;
import org.foi.nwtis.jmarijano.mysql.AirportsMySQL;
import org.foi.nwtis.jmarijano.mysql.DnevnikMySQL;
import org.foi.nwtis.jmarijano.mysql.KorisniciMySQL;
import org.foi.nwtis.jmarijano.mysql.MyAirportsMySQL;
import org.foi.nwtis.jmarijano.sucelja.AirplanesService;
import org.foi.nwtis.jmarijano.sucelja.AirportsService;
import org.foi.nwtis.jmarijano.sucelja.DnevnikService;
import org.foi.nwtis.jmarijano.sucelja.KorisnikService;
import org.foi.nwtis.jmarijano.sucelja.MyAirportsService;
import org.foi.nwtis.jmarijano.web.slusaci.SlusacAplikacije;
import org.foi.nwtis.jmarijano.web.ws.klijenti.NWTiS_2019Klijent;
import org.foi.nwtis.jmarijano.web.zahtjevi.ZaPOSTAvion;
import org.foi.nwtis.rest.podaci.AvionLeti;
import org.primefaces.json.JSONArray;
import org.primefaces.json.JSONObject;

/**
 * REST Web Service
 *
 * @author LiterallyCan't
 */
@Path("aerodromi")
public class RESTServiceApp1 {

    @Context
    private UriInfo context;
    private final KorisnikService korisnikService;
    private final ServletContext servletContext;
    private final Konfiguracija konfiguracija;
    private final ListJson<Aerodrom> listAerodromJson;
    private Korisnik korisnik;
    private final ErrorJson errorJson;
    private String odgovor;
    private final Gson gson;
    private List<Aerodrom> listaAerodroma;
    private final MyAirportsService myAirportsService;
    private Aerodrom aerodrom;
    private final ObjectJson<Aerodrom> aerodromJson;
    private final DnevnikService dnevnikService;
    private final Dnevnik dnevnik;
    private PostJson postJson;
    private final AirportsService airportsService;
    private final LocationIQHelper locationIQHelper;
    private PutJson putJson;
    private List<AvionLeti> listaAviona;
    private final AirplanesService airplanesService;
    private final ListJson<AvionLeti> listAvionLetiJson;
    private String subversionKorisnickoIme;
    private String subversionLozinka;
    private ZaPOSTAvion zaPOSTAvion;

    /**
     * Creates a new instance of RESTServiceApp1
     */
    public RESTServiceApp1() {
        servletContext = SlusacAplikacije.getSc();
        konfiguracija = SlusacAplikacije.getKonfiguracija();
        korisnikService = new KorisniciMySQL(servletContext);
        listAerodromJson = new ListJson<>();
        korisnik = new Korisnik();
        errorJson = new ErrorJson();
        odgovor = "";
        gson = new Gson();
        listaAerodroma = new ArrayList<>();
        myAirportsService = new MyAirportsMySQL(servletContext);
        aerodrom = new Aerodrom();
        aerodromJson = new ObjectJson<>();
        dnevnikService = new DnevnikMySQL(servletContext);
        dnevnik = new Dnevnik();
        postJson = new PostJson();
        airportsService = new AirportsMySQL(servletContext);
        locationIQHelper = new LocationIQHelper(konfiguracija);
        putJson = new PutJson();
        listaAviona = new ArrayList<>();
        airplanesService = new AirplanesMySQL(servletContext);
        listAvionLetiJson = new ListJson<>();
        subversionKorisnickoIme = konfiguracija.dajPostavku("subversion.korisnickoIme");
        subversionLozinka = konfiguracija.dajPostavku("subversion.lozinka");
        zaPOSTAvion = new ZaPOSTAvion();
    }

    /**
     *
     * @param korisnickoIme
     * @param lozinka
     * @return
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String dajSveAerodrome(@QueryParam("korisnickoIme") String korisnickoIme, @QueryParam("lozinka") String lozinka) {
        try {
            korisnik = korisnikService.getByKorisnickoImeAndLozinka(korisnickoIme, lozinka);
            if (postojiKorisnik(korisnickoIme, lozinka)) {
                listaAerodroma = myAirportsService.dohvatiSve();
                if (listaAerodroma != null) {
                    listAerodromJson.setOdgovor(listaAerodroma);
                    odgovor = gson.toJson(listAerodromJson);
                    unesiUDnevnik("Daj sve aerodrome", "REST", 3, korisnik.getKorisnickoIme(), "Podnesen zahtjev od strane" + korisnik.getKorisnickoIme() + " nad REST servisom, operacija dajSveAerodrome");
                    return odgovor;
                } else {
                    errorJson.setPoruka("Ne postoje aerodromi u bazi podataka");
                    odgovor = gson.toJson(errorJson);
                    return odgovor;
                }
            } else {
                errorJson.setPoruka("Ne postoji korisnik u bazi podataka");
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
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public String noviAerodrom(String zahtjev, @QueryParam("korisnickoIme") String korisnickoIme, @QueryParam("lozinka") String lozinka) {
        try {
            postJson = gson.fromJson(zahtjev, PostJson.class);
            if (postojiKorisnik(korisnickoIme, lozinka)) {
                aerodrom = airportsService.dohvatiPremaIdent(postJson.icao);
                if (aerodrom.getIcao() != null && !aerodrom.getIcao().isEmpty()) {
                    aerodrom.setLokacija(locationIQHelper.preuzmiGPSLokaciju(aerodrom.getNaziv()));
                    if (myAirportsService.dodaj(aerodrom) == 0) {
                        errorJson.setPoruka("Dogodila se greška prilikom dodavanja novog aerodroma u tablicu myairports");
                        odgovor = gson.toJson(errorJson);
                        return odgovor;
                    }
                    listaAerodroma = myAirportsService.dohvatiSve();
                    org.foi.nwtis.dkermek.ws.serveri.Aerodrom profAerodrom = new org.foi.nwtis.dkermek.ws.serveri.Aerodrom();
                    profAerodrom.setDrzava(aerodrom.getDrzava());
                    profAerodrom.setIcao(aerodrom.getIcao());
                    org.foi.nwtis.dkermek.ws.serveri.Lokacija profLok = new org.foi.nwtis.dkermek.ws.serveri.Lokacija();
                    profLok.setLatitude(aerodrom.getLokacija().getLatitude());
                    profLok.setLongitude(aerodrom.getLokacija().getLongitude());
                    profAerodrom.setLokacija(profLok);
                    profAerodrom.setNaziv(aerodrom.getNaziv());
                    NWTiS_2019Klijent.dodajAerodromGrupi(subversionKorisnickoIme, subversionLozinka, profAerodrom);
                    aerodromJson.setOdgovor(new Aerodrom());
                    odgovor = gson.toJson(aerodromJson);
                    unesiUDnevnik("Dodaj aerodrom", "REST", 3, korisnik.getKorisnickoIme(), "Podnesen zahtjev od strane" + korisnik.getKorisnickoIme() + " nad REST servisom, operacija noviAerodrom");
                    return odgovor;
                } else {
                    errorJson.setPoruka("Ne postoji aerodrom u tablici airports s poslanim IDENT");
                    odgovor = gson.toJson(errorJson);
                    return odgovor;
                }
            } else {
                errorJson.setPoruka("Ne postoji korisnik u bazi podataka");
                odgovor = gson.toJson(errorJson);
                return odgovor;
            }
        } catch (Exception e) {
            errorJson.setPoruka("Dogodila se greška");
            odgovor = gson.toJson(errorJson);
            return odgovor;
        }
    }

    @Path("{ident}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String dajAerodrom(@PathParam("ident") String ident, @QueryParam("korisnickoIme") String korisnickoIme, @QueryParam("lozinka") String lozinka) {
        try {
            if (postojiKorisnik(korisnickoIme, lozinka)) {
                aerodrom = myAirportsService.dohvatiPremaIdent(ident);
                if (aerodrom.getIcao() != null) {
                    aerodromJson.setOdgovor(aerodrom);
                    odgovor = gson.toJson(aerodromJson);
                    unesiUDnevnik("Daj aerodrom", "REST", 3, korisnik.getKorisnickoIme(), "Podnesen zahtjev od strane " + korisnik.getKorisnickoIme() + " nad REST servisom, operacija dajAerodrom");
                    return odgovor;
                } else {
                    errorJson.setPoruka("Ne postoji aerodrom u bazi podataka");
                    odgovor = gson.toJson(errorJson);
                    return odgovor;
                }
            } else {
                errorJson.setPoruka("Ne postoji korisnik u bazi podataka");
                odgovor = gson.toJson(errorJson);
                return odgovor;
            }
        } catch (Exception e) {
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
    @Path("{ident}")
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public String azurirajAerodrom(@PathParam("ident") String ident, String zahtjev, @QueryParam("korisnickoIme") String korisnickoIme, @QueryParam("lozinka") String lozinka) {
        try {
            putJson = gson.fromJson(zahtjev, PutJson.class);
            if (postojiKorisnik(korisnickoIme, lozinka)) {
                aerodrom = myAirportsService.dohvatiPremaIdent(ident);
                if (aerodrom.getIcao() != null && !aerodrom.getIcao().isEmpty()) {
                    aerodrom.setNaziv(putJson.naziv);
                    aerodrom.setLokacija(locationIQHelper.preuzmiGPSLokaciju(putJson.adresa));
                    if (myAirportsService.azuriraj(aerodrom) != 0) {
                        aerodromJson.setOdgovor(new Aerodrom());
                        unesiUDnevnik("Ažuriraj aerodrom", "REST", 3, korisnik.getKorisnickoIme(), "Podnesen zahtjev od strane" + korisnik.getKorisnickoIme() + " nad REST servisom, operacija azurirajAerodrom");
                        odgovor = gson.toJson(aerodromJson);
                        return odgovor;
                    } else {
                        errorJson.setPoruka("Dogodila se greška prilikom ažuriranja aerodroma");
                        odgovor = gson.toJson(errorJson);
                        return odgovor;
                    }
                } else {
                    errorJson.setPoruka("Ne postoji aerodrom s unesenim IDENT");
                    odgovor = gson.toJson(errorJson);
                    return odgovor;
                }
            } else {
                errorJson.setPoruka("Ne postoji korisnik u bazi podataka");
                odgovor = gson.toJson(errorJson);
                return odgovor;
            }
        } catch (Exception e) {
            errorJson.setPoruka("Dogodila se greška");
            odgovor = gson.toJson(errorJson);
            return odgovor;
        }
    }

    @DELETE
    @Path("{ident}")
    @Produces(MediaType.APPLICATION_JSON)
    public String obrisiAerodrom(@PathParam("ident") String ident, @QueryParam("korisnickoIme") String korisnickoIme, @QueryParam("lozinka") String lozinka) {
        try {

            if (postojiKorisnik(korisnickoIme, lozinka)) {
                aerodrom.setIcao(ident);
                if (myAirportsService.izbrisi(aerodrom) == 1) {
                    NWTiS_2019Klijent.obrisiAerodromGrupe(subversionKorisnickoIme, subversionLozinka, ident);
                    aerodromJson.setOdgovor(new Aerodrom());
                    odgovor = gson.toJson(aerodromJson);
                    unesiUDnevnik("Obriši aerodrom", "REST", 3, korisnik.getKorisnickoIme(), "Podnesen zahtjev od strane" + korisnik.getKorisnickoIme() + " nad REST servisom, operacija obrisiAerodrom");
                    return odgovor;
                } else {
                    errorJson.setPoruka("Dogodila se pogreška kod brisanja aerodroma");
                    odgovor = gson.toJson(errorJson);
                    return odgovor;
                }
            } else {
                errorJson.setPoruka("Ne postoji korisnik u bazi podataka");
                odgovor = gson.toJson(errorJson);
                return odgovor;
            }
        } catch (Exception e) {
            errorJson.setPoruka("Dogodila se greška");
            odgovor = gson.toJson(errorJson);
            return odgovor;
        }
    }

    @Path("{ident}/avion")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String dajSveAvioneAerodroma(@PathParam("ident") String ident, @QueryParam("korisnickoIme") String korisnickoIme, @QueryParam("lozinka") String lozinka) {
        try {
            if (postojiKorisnik(korisnickoIme, lozinka)) {
                listaAviona = airplanesService.dajSveAvionePoletjeleSAerdroma(ident);
                if (!listaAviona.isEmpty()) {
                    listAvionLetiJson.setOdgovor(listaAviona);
                    odgovor = gson.toJson(listAvionLetiJson);
                    unesiUDnevnik("Daj sve avione aerodroma", "REST", 3, korisnik.getKorisnickoIme(), "Podnesen zahtjev od strane" + korisnik.getKorisnickoIme() + " nad REST servisom, operacija dajSveAvioneAerodroma");
                    return odgovor;
                } else {
                    errorJson.setPoruka("Ne postoje avioni koji su poletjeli s aerodroma koji ima dobiveni IDENT");
                    odgovor = gson.toJson(errorJson);
                    return odgovor;
                }
            } else {
                errorJson.setPoruka("Ne postoji korisnik u bazi podataka");
                odgovor = gson.toJson(errorJson);
                return odgovor;
            }
        } catch (Exception e) {
            errorJson.setPoruka("Dogodila se greška");
            odgovor = gson.toJson(errorJson);
            return odgovor;
        }
    }

    @Path("{ident}/avion")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public String dodajAvioneAerodromu(@PathParam("ident") String ident, @QueryParam("korisnickoIme") String korisnickoIme, @QueryParam("lozinka") String lozinka, String zahtjev) {
        try {
            Type collectionType = new TypeToken<Collection<AvionLeti>>() {
            }.getType();
            JSONObject json = new JSONObject(zahtjev);
            json.getJSONArray("odgovor");
            JSONArray values = json.getJSONArray("odgovor");
            listaAviona = (List<AvionLeti>) gson.fromJson(values.toString(), collectionType);
            int brojac = 0;
            if (postojiKorisnik(korisnickoIme, lozinka)) {
                for (AvionLeti avionLeti : listaAviona) {
                    if ((airplanesService.getCountByIcaoAndLastSeen(avionLeti.getIcao24(), avionLeti.getLastSeen()) == 0)) {
                        if (airplanesService.dodaj(avionLeti) == 1) {
                            unesiUDnevnik("Dodaj avion aerodromu", "REST", 3, korisnik.getKorisnickoIme(), "Podnesen zahtjev od strane" + korisnik.getKorisnickoIme() + " nad REST servisom, operacija dodajAvioneAerodromu");
                            brojac++;
                        }
                    }
                }
                if (brojac > 0) {
                    aerodromJson.setOdgovor(new Aerodrom());
                    odgovor = gson.toJson(aerodromJson);
                    return odgovor;
                } else {
                    errorJson.setPoruka("Dogodila se pogreška kod dodavanja aviona aerodromu");
                    odgovor = gson.toJson(errorJson);
                    return odgovor;
                }
            } else {
                errorJson.setPoruka("Ne postoji korisnik u bazi podataka");
                odgovor = gson.toJson(errorJson);
                return odgovor;
            }
        } catch (Exception e) {
            e.printStackTrace();
            errorJson.setPoruka("Dogodila se greška");
            odgovor = gson.toJson(errorJson);
            return odgovor;
        }
    }

    @Path("{ident}/avion")
    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    public String obrisiSveAvioneAerodrome(@PathParam("ident") String ident, @QueryParam("korisnickoIme") String korisnickoIme, @QueryParam("lozinka") String lozinka) {
        try {
            if (postojiKorisnik(korisnickoIme, lozinka)) {
                if (airplanesService.izbrisiSveAvionePolazisnogAerodroma(ident) > 0) {
                    aerodromJson.setOdgovor(new Aerodrom());
                    odgovor = gson.toJson(aerodromJson);
                    unesiUDnevnik("Obriši sve avione aerodroma", "REST", 3, korisnik.getKorisnickoIme(), "Podnesen zahtjev od strane" + korisnik.getKorisnickoIme() + " nad REST servisom, operacija obrisiSveAvioneAerodroma");
                    return odgovor;
                } else {
                    errorJson.setPoruka("Dogodila se pogreška kod brisanja aviona aerodroma");
                    odgovor = gson.toJson(errorJson);
                    return odgovor;
                }
            } else {
                errorJson.setPoruka("Ne postoji korisnik u bazi podataka");
                odgovor = gson.toJson(errorJson);
                return odgovor;
            }
        } catch (Exception e) {
            errorJson.setPoruka("Dogodila se greška");
            odgovor = gson.toJson(errorJson);
            return odgovor;
        }
    }

    @Path("{ident}/avion/{aid}")
    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    public String obrisiAvionAerodroma(@PathParam("ident") String ident, @PathParam("aid") String aid, String zahtjev, @QueryParam("korisnickoIme") String korisnickoIme, @QueryParam("lozinka") String lozinka) {
        try {
            if (postojiKorisnik(korisnickoIme, lozinka)) {
                if (airplanesService.izbrisiAvionPolazisnogAerodroma(ident, aid) > 0) {
                    aerodromJson.setOdgovor(new Aerodrom());
                    odgovor = gson.toJson(aerodromJson);
                    unesiUDnevnik("Obrisan avion aerodroma", "REST", 3, korisnik.getKorisnickoIme(), "Podnesen zahtjev od strane" + korisnik.getKorisnickoIme() + " nad REST servisom, operacija obrisiAvionAerodroma");
                    return odgovor;
                } else {
                    errorJson.setPoruka("Dogodila se pogreška kod brisanja aviona aerodroma");
                    odgovor = gson.toJson(errorJson);
                    return odgovor;
                }
            } else {
                errorJson.setPoruka("Ne postoji korisnik u bazi podataka");
                odgovor = gson.toJson(errorJson);
                unesiUDnevnik("Obriši avion aerodroma", "REST", 3, korisnik.getKorisnickoIme(), "Podnesen zahtjev od strane" + korisnik.getKorisnickoIme() + " nad REST servisom, operacija obrisiAvionAerodrom");
                return odgovor;
            }
        } catch (Exception e) {
            errorJson.setPoruka("Dogodila se greška");
            odgovor = gson.toJson(errorJson);
            return odgovor;
        }
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

    private boolean postojiKorisnik(String korisnickoIme, String lozinka) {
        boolean output = false;
        korisnik = korisnikService.getByKorisnickoImeAndLozinka(korisnickoIme, lozinka);
        if (korisnik.getKorisnickoIme() != null && korisnik.getLozinka() != null) {
            output = true;
        }
        return output;
    }
}
