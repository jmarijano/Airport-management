/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.jmarijano.sb;

import com.google.gson.JsonObject;
import javax.ejb.LocalBean;
import javax.ejb.Stateful;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;
import org.foi.nwtis.jmarijano.dretve.MQTTSlusac;
import org.foi.nwtis.jmarijano.konfiguracije.Konfiguracija;
import org.glassfish.jersey.uri.UriComponent;

/**
 *
 * @author LiterallyCan't
 */
@Stateful
@LocalBean
public class StatefulSBApp2_1 {

    private MQTTSlusac mQTTSlusac;

    public boolean autenticiraj(String korisnickoIme, String lozinka) {
        try {

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
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    public void pokreniMQTTSlusaca(Konfiguracija konfiguracija, MqttPorukeFacade mqttPorukeFacade) {
        mQTTSlusac = new MQTTSlusac(konfiguracija, mqttPorukeFacade);
        mQTTSlusac.start();
    }

    public void zaustaviSlusaca() {
        if (mQTTSlusac != null) {
            mQTTSlusac.interrupt();
        }
    }
}
