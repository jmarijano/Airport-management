package org.foi.nwtis.jmarijano.modeli;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import org.foi.nwtis.rest.podaci.Lokacija;

@XmlRootElement
public class Aerodrom {

    private String icao;

    private String naziv;

    private String drzava;

    private Lokacija lokacija;

    /**
     * Konstruktor
     *
     * @param icao Icao kod
     * @param naziv Naziv aerodroma
     * @param drzava Država u kojoj se nalazi aerodrom
     * @param lokacija Lokacija aerodroma
     */
    public Aerodrom(String icao, String naziv, String drzava, Lokacija lokacija) {
        this.icao = icao;
        this.naziv = naziv;
        this.drzava = drzava;
        this.lokacija = lokacija;
    }

    /**
     * Konstruktor
     */
    public Aerodrom() {
    }

    /**
     * Metoda dohvaća vrijednost atributa icao.
     *
     * @return vrijednost atributa icao.
     */
    @XmlElement
    public String getIcao() {
        return icao;
    }

    /**
     * Metoda postavlja novu vrijednost atributa icao.
     *
     * @param icao Nova vrijednost atributa icao.
     */
    public void setIcao(String icao) {
        this.icao = icao;
    }

    /**
     * Metoda dohvaća vrijednost atributa naziv.
     *
     * @return vrijednost atributa naziv.
     */
    @XmlElement
    public String getNaziv() {
        return naziv;
    }

    /**
     * Metoda postavlja vrijednost atributa naziv.
     *
     * @param naziv Nova vrijednost atributa naziv.
     */
    public void setNaziv(String naziv) {
        this.naziv = naziv;
    }

    /**
     * Metoda dohvaća vrijednost atributa država.
     *
     * @return vrijednost atributa država.
     */
    @XmlElement
    public String getDrzava() {
        return drzava;
    }

    /**
     * Metoda postavlja vrijednost atributa država.
     *
     * @param drzava Nova vrijednost atributa država.
     */
    public void setDrzava(String drzava) {
        this.drzava = drzava;
    }

    /**
     * Metoda dohvaća vrijednost atributa kaeboktimater.
     *
     * @return vrijednost atributa kaeboktimater.
     */
    @XmlElement
    public Lokacija getLokacija() {
        return lokacija;
    }

    /**
     * Metoda postavlja vrijednost atributa kaeboktimater.
     *
     * @param lokacija Nova vrijednost atributa kaeboktimater.
     */
    public void setLokacija(Lokacija lokacija) {
        this.lokacija = lokacija;
    }

}
