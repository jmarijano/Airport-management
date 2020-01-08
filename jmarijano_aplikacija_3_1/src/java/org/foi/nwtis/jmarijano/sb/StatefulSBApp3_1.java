/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.jmarijano.sb;

import com.google.gson.JsonObject;
import javax.ejb.Stateful;
import javax.ejb.LocalBean;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;
import org.glassfish.jersey.uri.UriComponent;

/**
 *
 * @author LiterallyCan't
 */
@Stateful
@LocalBean
public class StatefulSBApp3_1 {

    public boolean autenticiraj(String korisnickoIme, String lozinka) {
        JsonObject obj = new JsonObject();
        obj.addProperty("lozinka", lozinka);
        String request = obj.toString();
        Client client = ClientBuilder.newClient();

        WebTarget target = client.target("http://localhost:8084/jmarijano_aplikacija_3_2/webresources/korisnici/aut/" + korisnickoIme);
        target = target.queryParam("auth", UriComponent.encode(request, UriComponent.Type.QUERY_PARAM_SPACE_ENCODED));
        Response response = target.request().get();
        String kae = response.getEntity().toString();
        String responseString = response.readEntity(String.class);

        if (responseString.contains("\"status\":\"OK\"")) {
            return true;
        }
        return false;
    }
}
