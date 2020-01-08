/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.jmarijano.slusaci;

import javax.ejb.EJB;
import javax.servlet.http.HttpSessionAttributeListener;
import javax.servlet.http.HttpSessionBindingEvent;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;
import org.foi.nwtis.jmarijano.sb.SingletonSBApp2_1;

/**
 * Web application lifecycle listener.
 *
 * @author LiterallyCan't
 */
public class SlusacSesije implements HttpSessionListener, HttpSessionAttributeListener {

    @EJB
    private SingletonSBApp2_1 singletonSBApp2_1;
    private static int brojAktivnihSesija = 0;

    @Override
    public void sessionCreated(HttpSessionEvent se) {
        brojAktivnihSesija++;
        if (singletonSBApp2_1.dodajUSesiju(se.getSession())) {
            System.out.println("Dodana sesija");
        } else {
            System.out.println("Greška kod dodavanje sesije");
        }
    }

    @Override
    public void sessionDestroyed(HttpSessionEvent se) {
        try {
            if (brojAktivnihSesija > 0) {
                brojAktivnihSesija--;
                if (singletonSBApp2_1.izbrisiIzSesije(se.getSession())) {
                    System.out.println("Obrisana sesija");
                } else {
                    System.out.println("Greška kod brisanja sesije");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void attributeAdded(HttpSessionBindingEvent event) {
    }

    @Override
    public void attributeRemoved(HttpSessionBindingEvent event) {
    }

    @Override
    public void attributeReplaced(HttpSessionBindingEvent event) {
    }
}
