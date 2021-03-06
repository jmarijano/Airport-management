package org.foi.nwtis.jmarijano.jsonklase;

/**
 * Generička klasa koja služi za spremanje stringa u JSON format te se tada
 * prosljeđuje Gson objektu.
 *
 * @param <T> Konkretna klasa
 */
public class ObjectJson<T> {

    private final String status = "OK";

    private T odgovor;

    /**
     * Konstruktor
     */
    public ObjectJson() {
    }

    /**
     * Metoda postavlja vrijednost odgovora s konkretnom klasom.
     *
     * @param objekt objekt konkretne klase.
     */
    public void setOdgovor(T objekt) {

        this.odgovor = objekt;
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
     * Metoda dohvaća vrijednost atributa odgovor
     *
     * @return Vrijednost atributa odgovor.
     */
    public T getOdgovor() {
        return odgovor;
    }

}
