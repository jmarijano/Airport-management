/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.jmarijano;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author LiterallyCan't
 */
public class Komande {

    private Socket socket;
    private DataOutputStream dataOutputStream;
    private DataInputStream dataInputStream;
    private InputStream inputStream;
    private OutputStream outputStream;

    public Komande(Socket socket) {
        this.socket = socket;
    }

    public StringBuffer dohvatiZahtjev() {
        StringBuffer output = null;
        try {
            inputStream = socket.getInputStream();
            dataInputStream = new DataInputStream(inputStream);
            output = new StringBuffer();
            while (true) {
                try {
                    String odgovor = dataInputStream.readUTF();
                    output.append(odgovor);
                } catch (EOFException e) {
                    e.printStackTrace();
                }

            }

        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        return output;
    }

    public void posaljiOdgovor(String odgovor) {
        try {
            outputStream = socket.getOutputStream();
            dataOutputStream = new DataOutputStream(outputStream);
            dataOutputStream.writeUTF(odgovor);
            dataOutputStream.flush();
            dataOutputStream.close();
            socket.shutdownOutput();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public void zatvoriSocket() {
        try {
            socket.close();
        } catch (IOException ex) {
            Logger.getLogger(Komande.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
