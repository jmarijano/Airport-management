package org.foi.nwtis.jmarijano.eb;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.websocket.EndpointConfig;
import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

@ServerEndpoint("/infoPoruka")
public class InformatorPoruka {

    static List<Session> sjednice = new ArrayList<>();

    /**
     * Metoda koja se poziva prilikom dobivanja poruke.
     *
     * @param message Dobivena poruka
     */
    @OnMessage
    public void onMessage(String message) {
        for (Session s : sjednice) {
            if (s.isOpen()) {
                try {
                    s.getBasicRemote().sendText(message);
                } catch (IOException ex) {
                    Logger.getLogger(InformatorPoruka.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }

    /**
     * Metoda koja se poziva nakon što je inicijalizirana nova WebSocket
     * konekcija.
     *
     * @param session Sesija
     * @param conf EndPointConfig
     */
    @OnOpen
    public void onOpen(Session session, EndpointConfig conf) {
        sjednice.add(session);
        System.out.println("WS open: " + session.getId());
    }

    /**
     * Metoda koja se poziva kad se zatvori WebSocket konekcija
     *
     * @param session Sesija
     * @param conf EndPointConfig
     */
    @OnClose
    public void onClose(Session session, EndpointConfig conf) {
        System.out.println("WS close: " + session.getId());
        sjednice.remove(session);
    }

    /**
     * Metoda koja šalje poruku klijentu.
     *
     * @param poruka Poruka koja se šalje klijentu.
     */
    public static void saljiPoruku(String poruka) {
        for (Session s : sjednice) {
            if (s.isOpen()) {
                try {
                    s.getBasicRemote().sendText(poruka);
                } catch (IOException ex) {
                    Logger.getLogger(InformatorPoruka.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }

    }

}
