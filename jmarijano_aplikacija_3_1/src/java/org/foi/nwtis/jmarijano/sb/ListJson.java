/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.jmarijano.sb;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author LiterallyCan't
 */
class ListJson<T> {

    private String status = "OK";

    private List<T> odgovor;

    /**
     * Konstruktor klase.
     */
    public ListJson() {
        odgovor = new ArrayList<>();
    }

    /**
     * Metoda koja dohvaća vrijednost atributa odgovor
     *
     * @return vrijednost atributa odgovor
     */
    public List<T> getOdgovor() {
        return odgovor;
    }

    /**
     * Metoda koja postavlja vrijednost atributa status
     *
     * @param odgovor
     */
    public void setOdgovor(List<T> odgovor) {
        this.odgovor = odgovor;
    }

    /**
     * Metoda koja dohvaća vrijednost atributa status
     *
     * @return Nova vrijednost atributa status
     */
    public String getStatus() {
        return status;
    }
}
