/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.jmarijano.web.zahtjevi;

import java.util.List;
import org.foi.nwtis.rest.podaci.AvionLeti;

/**
 *
 * @author LiterallyCan't
 */
public class ZaPOSTAvion {

    private List<AvionLeti> listaAviona;

    public ZaPOSTAvion(List<AvionLeti> listaAviona) {
        this.listaAviona = listaAviona;
    }

    public List<AvionLeti> getListaAviona() {
        return listaAviona;
    }

    public void setListaAviona(List<AvionLeti> listaAviona) {
        this.listaAviona = listaAviona;
    }

    public ZaPOSTAvion() {
    }
    
}
