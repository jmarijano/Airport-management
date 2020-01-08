/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.jmarijano.web.dretve;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Timestamp;
import java.util.Date;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.foi.nwtis.jmarijano.helperi.DretvaKonfiguracijaHelper;
import org.foi.nwtis.jmarijano.konfiguracije.Konfiguracija;
import org.foi.nwtis.jmarijano.web.slusaci.SlusacAplikacije;

/**
 *
 * @author LiterallyCan't
 */
public class ServerAerodroma extends Thread {

    public static volatile AtomicBoolean kraj;
    public static volatile AtomicBoolean prekidGrupa;
    ServerSocket serverSocket;
    private int port;
    private Socket klijentSocket;
    Konfiguracija konfiguracija;
    DretvaKonfiguracijaHelper konfiguracijaHelper;
    private Date date;

    @Override
    @SuppressWarnings("empty-statement")
    public void interrupt() {
        kraj.set(true);
        try {
            serverSocket.close();
            klijentSocket.close();
        } catch (IOException ex) {
            Logger.getLogger(ServerAerodroma.class.getName()).log(Level.SEVERE, null, ex);
        }
        super.interrupt(); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void run() {
        try {
            serverSocket = new ServerSocket(port);
            while (!kraj.get()) {

                System.out.println("Cekam");
                klijentSocket = serverSocket.accept();
                date = new Date();
                System.out.println("Primljen zahtjev");
                DretvaZahtjeva dretvaZahtjeva = new DretvaZahtjeva(klijentSocket, new Timestamp(date.getTime()));
                dretvaZahtjeva.start();
            }
        } catch (IOException ex) {
            Logger.getLogger(ServerAerodroma.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public synchronized void start() {
        kraj = new AtomicBoolean(false);
        konfiguracija = SlusacAplikacije.getKonfiguracija();
        konfiguracijaHelper = new DretvaKonfiguracijaHelper(konfiguracija);
        port = konfiguracijaHelper.dohvatiPort();
        prekidGrupa = new AtomicBoolean(false);
        System.out.println("Port:" + port);
        super.start();
    }

}
