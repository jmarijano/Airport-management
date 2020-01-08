/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.jmarijano.mdb;

import org.foi.nwtis.jmarijano.poruke.PorukaPosluzitelj;
import com.google.gson.Gson;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.ActivationConfigProperty;
import javax.ejb.EJB;
import javax.ejb.MessageDriven;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import org.foi.nwtis.jmarijano.eb.InformatorPoruka;
import org.foi.nwtis.jmarijano.sb.SingletonSBApp3_1;

/**
 *
 * @author LiterallyCan't
 */
@MessageDriven(activationConfig = {
    @ActivationConfigProperty(propertyName = "destinationLookup", propertyValue = "jms/NWTiS_jmarijano_1"),
    @ActivationConfigProperty(propertyName = "destinationType", propertyValue = "javax.jms.Queue"),})
public class PosluziteljMDB implements MessageListener {
    
    @EJB
    private SingletonSBApp3_1 singletonSB;
    private Gson gson;
    
    public PosluziteljMDB() {
        gson = new Gson();
    }
    
    @Override
    public void onMessage(Message message) {
        try {
            PorukaPosluzitelj poruka = gson.fromJson(message.getBody(String.class), PorukaPosluzitelj.class);
            if (singletonSB.dodajUPosluzitelj(poruka)) {
                System.out.println("Spremljena poruka");
                InformatorPoruka.saljiPoruku("PorukaPosluzitelj");
            } else {
                System.out.println("Dogodila se pogreška");
            }
            System.out.println("Primljena poslužiteljska poruka:" + message.getBody(String.class));
            
        } catch (JMSException ex) {
            Logger.getLogger(PosluziteljMDB.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
