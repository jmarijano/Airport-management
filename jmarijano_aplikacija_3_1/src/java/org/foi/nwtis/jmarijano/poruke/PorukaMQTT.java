/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.jmarijano.poruke;

import java.io.Serializable;

/**
 *
 * @author LiterallyCan't
 */
public class PorukaMQTT implements Serializable {

    private ZaMqtt poruka;
    private String vrijeme;
    private int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public PorukaMQTT() {
    }

    public String getVrijeme() {
        return vrijeme;
    }

    public void setVrijeme(String vrijeme) {
        this.vrijeme = vrijeme;
    }

    public ZaMqtt getPoruka() {
        return poruka;
    }

    public void setPoruka(ZaMqtt poruka) {
        this.poruka = poruka;
    }
}
