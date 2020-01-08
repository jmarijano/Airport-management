/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.jmarijano.web.rs.klijent;

import javax.ws.rs.ClientErrorException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.WebTarget;

/**
 *
 * @author LiterallyCan't
 */
public class RESTServiceApp1_JerseyClient {

    private WebTarget webTarget;
    private Client client;
    private static final String BASE_URI = "http://localhost:8084/jmarijano_aplikacija_1/webresources";

    public RESTServiceApp1_JerseyClient() {
        client = javax.ws.rs.client.ClientBuilder.newClient();
        webTarget = client.target(BASE_URI).path("aerodromi");
    }

    public String obrisiAvionAerodroma(String ident, String aid, String korisnickoIme, String lozinka) throws ClientErrorException {

        WebTarget resource = webTarget;
        if (lozinka != null) {
            resource = resource.queryParam("lozinka", lozinka);
        }
        if (korisnickoIme != null) {
            resource = resource.queryParam("korisnickoIme", korisnickoIme);
        }
        resource = resource.path(java.text.MessageFormat.format("{0}/avion/{1}", new Object[]{ident}));
        return resource.request(javax.ws.rs.core.MediaType.APPLICATION_JSON).delete(String.class);

    }

    public String dajSveAvioneAerodroma(String ident, String lozinka, String korisnickoIme) throws ClientErrorException {
        WebTarget resource = webTarget;
        if (lozinka != null) {
            resource = resource.queryParam("lozinka", lozinka);
        }
        if (korisnickoIme != null) {
            resource = resource.queryParam("korisnickoIme", korisnickoIme);
        }
        resource = resource.path(java.text.MessageFormat.format("{0}/avion", new Object[]{ident}));
        return resource.request(javax.ws.rs.core.MediaType.APPLICATION_JSON).get(String.class);
    }

    public String dajSveAerodrome(String lozinka, String korisnickoIme) throws ClientErrorException {
        WebTarget resource = webTarget;
        if (lozinka != null) {
            resource = resource.queryParam("lozinka", lozinka);
        }
        if (korisnickoIme != null) {
            resource = resource.queryParam("korisnickoIme", korisnickoIme);
        }
        return resource.request(javax.ws.rs.core.MediaType.APPLICATION_JSON).get(String.class);
    }

    public String azurirajAerodrom(Object requestEntity, String ident, String lozinka, String korisnickoIme) throws ClientErrorException {

        WebTarget resource = webTarget;
        if (lozinka != null) {
            resource = resource.queryParam("lozinka", lozinka);
        }
        if (korisnickoIme != null) {
            resource = resource.queryParam("korisnickoIme", korisnickoIme);
        }
        resource = resource.path(java.text.MessageFormat.format("{0}", new Object[]{ident}));
        return resource.request(javax.ws.rs.core.MediaType.APPLICATION_JSON).put(javax.ws.rs.client.Entity.entity(requestEntity, javax.ws.rs.core.MediaType.APPLICATION_JSON), String.class);

    }

    public String obrisiSveAvioneAerodrome(String ident, String korisnickoIme, String lozinka) throws ClientErrorException {

        WebTarget resource = webTarget;
        if (lozinka != null) {
            resource = resource.queryParam("lozinka", lozinka);
        }
        if (korisnickoIme != null) {
            resource = resource.queryParam("korisnickoIme", korisnickoIme);
        }
        resource = resource.path(java.text.MessageFormat.format("{0}/avion", new Object[]{ident}));

        return resource.request(javax.ws.rs.core.MediaType.APPLICATION_JSON).delete(String.class);

    }

    public String obrisiAerodrom(String ident, String korisnickoIme, String lozinka) throws ClientErrorException {

        WebTarget resource = webTarget;
        if (lozinka != null) {
            resource = resource.queryParam("lozinka", lozinka);
        }
        if (korisnickoIme != null) {
            resource = resource.queryParam("korisnickoIme", korisnickoIme);
        }
        resource = resource.path(java.text.MessageFormat.format("{0}", new Object[]{ident}));

        return resource.request(javax.ws.rs.core.MediaType.APPLICATION_JSON).delete(String.class);

    }

    public String dodajAvioneAerodromu(Object requestEntity, String ident, String korisnickoIme, String lozinka) throws ClientErrorException {

        WebTarget resource = webTarget;
        if (lozinka != null) {
            resource = resource.queryParam("lozinka", lozinka);
        }
        if (korisnickoIme != null) {
            resource = resource.queryParam("korisnickoIme", korisnickoIme);
        }
        resource = resource.path(java.text.MessageFormat.format("{0}/avion", new Object[]{ident}));

        return resource.request(javax.ws.rs.core.MediaType.APPLICATION_JSON).post(javax.ws.rs.client.Entity.entity(requestEntity, javax.ws.rs.core.MediaType.APPLICATION_JSON), String.class);

    }

    public String noviAerodrom(Object requestEntity, String korisnickoIme, String lozinka) throws ClientErrorException {
        WebTarget resource = webTarget;
        if (lozinka != null) {
            resource = resource.queryParam("lozinka", lozinka);
        }
        if (korisnickoIme != null) {
            resource = resource.queryParam("korisnickoIme", korisnickoIme);
        }

        return resource.request(javax.ws.rs.core.MediaType.APPLICATION_JSON).post(javax.ws.rs.client.Entity.entity(requestEntity, javax.ws.rs.core.MediaType.APPLICATION_JSON), String.class);
    }

    public String dajAerodrom(String ident, String lozinka, String korisnickoIme) throws ClientErrorException {
        WebTarget resource = webTarget;
        if (lozinka != null) {
            resource = resource.queryParam("lozinka", lozinka);
        }
        if (korisnickoIme != null) {
            resource = resource.queryParam("korisnickoIme", korisnickoIme);
        }
        resource = resource.path(java.text.MessageFormat.format("{0}", new Object[]{ident}));
        return resource.request(javax.ws.rs.core.MediaType.APPLICATION_JSON).get(String.class);
    }

    public void close() {
        client.close();
    }

}
