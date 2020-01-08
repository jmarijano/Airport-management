/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.jmarijano.web.zrna;

import com.google.gson.Gson;
import javax.jms.Queue;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import javax.jms.DeliveryMode;
import javax.jms.QueueConnection;
import javax.jms.QueueConnectionFactory;
import javax.jms.QueueSender;
import javax.jms.QueueSession;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.naming.InitialContext;
import org.foi.nwtis.jmarijano.web.zahtjevi.PorukaPosluzitelj;

/**
 *
 * @author LiterallyCan't
 */
@Named(value = "posluziteljskiJMS")
@RequestScoped
public class PosluziteljskiJMS {

    public static int redniBroj = 1;
    private static Gson gson;

    /**
     * Creates a new instance of PosluziteljskiJMS
     */
    public PosluziteljskiJMS() {
    }

    public static void PosaljiPoruku(PorukaPosluzitelj poruka) {
        try {
            gson = new Gson();
            poruka.setId(redniBroj);
            InitialContext initialContext = new InitialContext();
            Queue queue = (Queue) initialContext.lookup("jms/NWTiS_jmarijano_1");
            QueueConnectionFactory connectionFactory = (QueueConnectionFactory) initialContext.lookup("jms/NWTiS_QF_jmarijano_1");
            QueueConnection connection = connectionFactory.createQueueConnection();
            QueueSession session = connection.createQueueSession(false, Session.DUPS_OK_ACKNOWLEDGE);
            QueueSender sender = session.createSender(queue);
            sender.setDeliveryMode(DeliveryMode.NON_PERSISTENT);
            String json = gson.toJson(poruka);
            System.out.println("Poruka.vrijeme" + poruka.getVrijeme());
            System.out.println("Poruka u JSON: " + json);
            TextMessage message = session.createTextMessage(json);
            sender.send(message);
            System.out.println("Poslana poruka: " + message.getText());
            connection.close();
            redniBroj++;
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
