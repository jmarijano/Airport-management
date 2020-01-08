/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.jmarijano.sb;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.ejb.LocalBean;
import javax.servlet.http.HttpSession;

/**
 *
 * @author LiterallyCan't
 */
@Singleton
@LocalBean
public class SingletonSBApp2_1 {

    private List<HttpSession> listaSesija;
    @EJB
    private StatefulSBApp2_1 statefulSB;

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
    @PreDestroy
    public void prijeUnistavnaja() {
        statefulSB.zaustaviSlusaca();
        listaSesija.clear();
    }

    public List<HttpSession> getListaSesija() {
        return listaSesija;
    }

    public void setListaSesija(List<HttpSession> listaSesija) {
        this.listaSesija = listaSesija;
    }

    public boolean dodajUSesiju(HttpSession sesija) {
        if (listaSesija.add(sesija)) {
            return true;
        } else {
            return false;
        }
    }

    public boolean izbrisiIzSesije(HttpSession sesija) {
        if (listaSesija.remove(sesija)) {
            return true;
        } else {
            return false;
        }
    }

    @PostConstruct
    public void init() {
        listaSesija = new ArrayList<>();
    }
}
