/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.jmarijano.sb;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import org.foi.nwtis.jmarijano.poruke.PorukaPosluzitelj;
import org.foi.nwtis.jmarijano.poruke.PorukaMQTT;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.Singleton;
import javax.ejb.LocalBean;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 *
 * @author LiterallyCan't
 */
@Singleton
@LocalBean
public class SingletonSBApp3_1 {

    private List<PorukaPosluzitelj> listaPosluzitelj;
    private String datotekaPosluzitelj;
    private String datotekaMQTT;
    private List<PorukaMQTT> listaMQTT;
    private Gson gson;
    private final ListJson<PorukaMQTT> listPorukaMqtt;

    public SingletonSBApp3_1() {
        listaPosluzitelj = new ArrayList<>();
        listaMQTT = new ArrayList<>();
        gson = new Gson();
        listPorukaMqtt = new ListJson<>();
    }

    /**
     *
     * @param datoteka
     */
    public void dohvatiPutanjuDatotekePosluzitelj(String datoteka) {
        System.out.println("Datoteka posluzitelj: " + datoteka);
        try {
            try (Reader reader = new InputStreamReader(new FileInputStream(new File(datoteka)), StandardCharsets.UTF_8)) {
                if (reader != null && reader.ready()) {
                    Type listType = new TypeToken<ArrayList<PorukaPosluzitelj>>() {
                    }.getType();
                    listaPosluzitelj = gson.fromJson(reader, listType);
                    System.out.println("Ucitane posluzitelj poruke");
                    System.out.println("Lista posluzitelja:" + listaPosluzitelj.size());
                } else {
                    System.out.println("Reader poslužitelj null");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        this.datotekaPosluzitelj = datoteka;

    }

    public void dohvatiPutanjuDatotekeMQTT(String datoteka) {
        System.out.println("Datoteka MQTT:" + datoteka);
        try {
            try (Reader reader = new InputStreamReader(new FileInputStream(new File(datoteka)), StandardCharsets.UTF_8)) {
                System.out.println("Reader read" + reader.read());
                if (reader != null && reader.ready()) {
                    Type collectionType = new TypeToken<Collection<PorukaMQTT>>() {
                    }.getType();

                    BufferedReader buff = new BufferedReader(new InputStreamReader(new FileInputStream(new File(datoteka)), "UTF-8"));
                    StringBuilder sb = new StringBuilder();
                    String line;
                    while ((line = buff.readLine()) != null) {
                        sb.append(line);
                    }
                    JSONObject json = new JSONObject(sb.toString());
                    json.getJSONArray("odgovor");
                    JSONArray values = json.getJSONArray("odgovor");
                    listaMQTT = (List<PorukaMQTT>) gson.fromJson(values.toString(), collectionType);
                    System.out.println("ucitane mqtt poruke");
                    System.out.println("Lista mqtt poruka: " + listaMQTT.size());
                } else {
                    System.out.println("Reader mqtt je null");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        this.datotekaMQTT = datoteka;

    }

    public List<PorukaPosluzitelj> getListaPosluzitelj() {
        return listaPosluzitelj;
    }

    public void setListaPosluzitelj(List<PorukaPosluzitelj> listaPosluzitelj) {
        this.listaPosluzitelj = listaPosluzitelj;
    }

    public List<PorukaMQTT> getListaMQTT() {
        return listaMQTT;
    }

    public void setListaMQTT(List<PorukaMQTT> listaMQTT) {
        this.listaMQTT = listaMQTT;
    }

    @PreDestroy
    public void prijeUnistavanja() {
        try {
            File filePos = new File(datotekaPosluzitelj);
            try (Writer writer = new OutputStreamWriter(
                    new FileOutputStream(filePos), "UTF-8")) {
                if (writer != null) {
                    gson.toJson(listaPosluzitelj, writer);
                    System.out.println("Serijalizirane poslužiteljske poruke");
                } else {
                    System.out.println("Writer je null za poslužitelja");
                }
            }
            File fileMqtt = new File(datotekaMQTT);
            try (Writer writer = new OutputStreamWriter(
                    new FileOutputStream(fileMqtt), "UTF-8")) {
                if (writer != null) {
                    listPorukaMqtt.setOdgovor(listaMQTT);
                    gson.toJson(listPorukaMqtt, writer);
                    System.out.println("Serijalizirane mqtt poruke");
                } else {
                    System.out.println("Writer je null za mqtt");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @PostConstruct
    public void init() {

    }

    public boolean dodajUPosluzitelj(PorukaPosluzitelj poruka) {
        System.out.println("PorukASDASDASDASDASDASDSADASWDderfgdafgadsfgswedfSAEDFa:" + poruka.getKomanda());
        if (listaPosluzitelj == null) {
            System.out.println("KAE OVO SAD");
        }
        if (listaPosluzitelj.add(poruka)) {
            return true;
        }
        return false;
    }

    public boolean dodajUMQTT(PorukaMQTT poruka) {
        if (listaMQTT.add(poruka)) {
            return true;
        }
        return false;
    }

}
