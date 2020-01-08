/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.jmarijano.poruke;

import java.util.List;

/**
 *
 * @author LiterallyCan't
 */
public class Pokusaj {

    private List<PorukaMQTT> lista;

    public Pokusaj() {
    }

    public List<PorukaMQTT> getLista() {
        return lista;
    }

    public void setLista(List<PorukaMQTT> lista) {
        this.lista = lista;
    }

}
