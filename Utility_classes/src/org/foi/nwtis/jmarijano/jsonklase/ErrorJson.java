package org.foi.nwtis.jmarijano.jsonklase;

public class ErrorJson {

    private String status = "ERR";
    private String poruka;

    /**
     * Konstruktor
     */
    public ErrorJson() {
    }

    /**
     * Metoda dohvaća vrijednost atributa status.
     *
     * @return vrijednost atributa status.
     */
    public String getStatus() {
        return status;
    }

    /**
     * Metoda dohvaća vrijednost atributa poruka.
     *
     * @return Nova vrijednost atributa poruka.
     */
    public String getPoruka() {
        return poruka;
    }

    /**
     * Metoda postavlja vrijednost atributa poruka.
     *
     * @param poruka Nova vrijednost atributa poruka.
     */
    public void setPoruka(String poruka) {
        this.poruka = poruka;
    }

}
