/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.jmarijano.dretve;

import com.google.gson.Gson;
import java.io.StringReader;
import java.net.URISyntaxException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import org.foi.nwtis.jmarijano.eb.MqttPoruke;
import org.foi.nwtis.jmarijano.jms.MQTTJMS;
import org.foi.nwtis.jmarijano.konfiguracije.Konfiguracija;
import org.foi.nwtis.jmarijano.poruke.PorukaMQTT;
import org.foi.nwtis.jmarijano.poruke.ZaMqtt;
import org.foi.nwtis.jmarijano.sb.MqttPorukeFacade;
import org.fusesource.hawtbuf.Buffer;
import org.fusesource.hawtbuf.UTF8Buffer;
import org.fusesource.mqtt.client.Callback;
import org.fusesource.mqtt.client.CallbackConnection;
import org.fusesource.mqtt.client.Listener;
import org.fusesource.mqtt.client.MQTT;
import org.fusesource.mqtt.client.QoS;
import org.fusesource.mqtt.client.Topic;

/**
 *
 * @author LiterallyCan't
 */
public class MQTTSlusac extends Thread {

    private String user;
    private String password;
    private String host;
    private int port;
    private String destination;
    private Konfiguracija konfiguracija;
    private MQTT mqtt;
    private CallbackConnection connection;
    private boolean kraj = false;
    private Listener glavniSlusac;
    private Callback<Void> konektor;
    private MqttPorukeFacade mqttPorukeFacade;
    private MqttPoruke mqttPoruke;
    private PorukaMQTT porukaMQTT;
    private String vrijeme;
    private DateFormat dateFormat;
    private Gson gson;
    private ZaMqtt zaMqtt;

    public MQTTSlusac(Konfiguracija konfiguracija, MqttPorukeFacade mqttPorukeFacade) {
        this.konfiguracija = konfiguracija;
        mqttPoruke = new MqttPoruke();
        porukaMQTT = new PorukaMQTT();
        zaMqtt = new ZaMqtt();
        dateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss.SSS");
        this.mqttPorukeFacade = mqttPorukeFacade;
        gson = new Gson();
    }

    @Override
    public void interrupt() {
        super.interrupt(); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void run() {
        connection = mqtt.callbackConnection();
        glavniSlusac = new org.fusesource.mqtt.client.Listener() {
            long count = 0;
            ArrayList<String> poruke = new ArrayList<>();
            Timestamp pocetak = new Timestamp(System.currentTimeMillis());

            @Override
            public void onConnected() {
                System.out.println("Otvorena veza na MQTT");
            }

            @Override
            public void onDisconnected() {
                System.out.println("Prekinuta veza na MQTT");
            }

            @Override
            public void onFailure(Throwable value) {
                System.out.println("Problem u vezi na MQTT");
            }

            @Override
            public void onPublish(UTF8Buffer topic, Buffer msg, Runnable ack) {
                try {
                    String body = msg.utf8().toString();
                    vrijeme = dateFormat.format(new Date());
                    porukaMQTT.setVrijeme(vrijeme);
                    JsonObject jsonObject;
                    JsonReader reader = Json.createReader(new StringReader(body));
                    jsonObject = reader.readObject();
                    mqttPoruke.setAerodrom(jsonObject.getString("aerodrom"));
                    mqttPoruke.setAvion(jsonObject.getString("avion"));
                    mqttPoruke.setKorisnik(jsonObject.getString("korisnik"));
                    mqttPoruke.setOznaka(jsonObject.getString("oznaka"));
                    mqttPoruke.setPoruka(jsonObject.getString("poruka"));
                    String vrijeme = jsonObject.getString("vrijeme");
                    Date parsedDate = dateFormat.parse(vrijeme);
                    Timestamp timestamp = new java.sql.Timestamp(parsedDate.getTime());
                    mqttPoruke.setVrijeme(timestamp);
                    mqttPoruke.setId(1);
                    mqttPorukeFacade.create(mqttPoruke);
                    zaMqtt.setAerodrom(jsonObject.getString("aerodrom"));
                    zaMqtt.setAvion(jsonObject.getString("avion"));
                    zaMqtt.setKorisnik(jsonObject.getString("korisnik"));
                    zaMqtt.setOznaka(jsonObject.getString("oznaka"));
                    zaMqtt.setPoruka(jsonObject.getString("poruka"));
                    zaMqtt.setVrijeme(timestamp);
                    porukaMQTT.setPoruka(zaMqtt);
                    MQTTJMS.PosaljiPoruku(porukaMQTT);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        konektor = new Callback<Void>() {
            @Override
            public void onSuccess(Void value) {
                Topic[] topics = {new Topic(destination, QoS.AT_LEAST_ONCE)};
                connection.subscribe(topics, new Callback<byte[]>() {
                    @Override
                    public void onSuccess(byte[] qoses) {
                        System.out.println("Pretplata na: " + destination);
                    }

                    @Override
                    public void onFailure(Throwable value) {
                        System.out.println("Problem kod pretplate na: " + destination);
                    }
                });
            }

            @Override
            public void onFailure(Throwable value) {
                System.out.println("Neuspjela pretplata na: " + destination);
            }
        };
        connection.listener(glavniSlusac);
        connection.connect(konektor);
        synchronized (MQTTSlusac.class) {
            while (!kraj && !this.isInterrupted()) {
                try {
                    MQTTSlusac.class.wait();
                } catch (InterruptedException ex) {
                    Logger.getLogger(MQTTSlusac.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }

    @Override
    public synchronized void start() {
        user = konfiguracija.dajPostavku("mqtt.user");
        password = konfiguracija.dajPostavku("mqtt.password");
        host = konfiguracija.dajPostavku("mqtt.host");
        port = Integer.parseInt(konfiguracija.dajPostavku("mqtt.port"));
        destination = konfiguracija.dajPostavku("mqtt.destination");
        mqtt = new MQTT();
        try {
            mqtt.setHost(host, port);
        } catch (URISyntaxException ex) {
            Logger.getLogger(MQTTSlusac.class.getName()).log(Level.SEVERE, null, ex);
        }
        mqtt.setUserName(user);
        mqtt.setPassword(password);
        super.start(); //To change body of generated methods, choose Tools | Templates.
    }

}
