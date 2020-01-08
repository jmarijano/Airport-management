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
public class AIRP2REST_JerseyClient {

    private WebTarget webTarget;
    private Client client;
    private static final String BASE_URI = "http://localhost:8084/jmarijano_aplikacija_3_2/webresources";

    public AIRP2REST_JerseyClient() {
        client = javax.ws.rs.client.ClientBuilder.newClient();
        webTarget = client.target(BASE_URI).path("korisnici");
    }

    public String getJsonIdParam(String korisnickoIme, String auth) throws ClientErrorException {
        WebTarget resource = webTarget;
        if (auth != null) {
            resource = resource.queryParam("auth", auth);
        }
        resource = resource.path(java.text.MessageFormat.format("aut/{0}", new Object[]{korisnickoIme}));
        return resource.request(javax.ws.rs.core.MediaType.APPLICATION_JSON).get(String.class);
    }

    public String azurirajKorisnika(Object requestEntity, String korisnickoIme) throws ClientErrorException {
        return webTarget.path(java.text.MessageFormat.format("{0}", new Object[]{korisnickoIme})).request(javax.ws.rs.core.MediaType.APPLICATION_JSON).put(javax.ws.rs.client.Entity.entity(requestEntity, javax.ws.rs.core.MediaType.APPLICATION_JSON), String.class);
    }

    public String postJson(Object requestEntity) throws ClientErrorException {
        return webTarget.request(javax.ws.rs.core.MediaType.APPLICATION_JSON).post(javax.ws.rs.client.Entity.entity(requestEntity, javax.ws.rs.core.MediaType.APPLICATION_JSON), String.class);
    }

    public String getJsonId(String korisnickoIme) throws ClientErrorException {
        WebTarget resource = webTarget;
        resource = resource.path(java.text.MessageFormat.format("{0}", new Object[]{korisnickoIme}));
        return resource.request(javax.ws.rs.core.MediaType.APPLICATION_JSON).get(String.class);
    }

    public String getJson(String lozinka, String korisnickoIme) throws ClientErrorException {
        WebTarget resource = webTarget;
        if (lozinka != null) {
            resource = resource.queryParam("lozinka", lozinka);
        }
        if (korisnickoIme != null) {
            resource = resource.queryParam("korisnickoIme", korisnickoIme);
        }
        return resource.request(javax.ws.rs.core.MediaType.APPLICATION_JSON).get(String.class);
    }

    public void close() {
        client.close();
    }

}
