/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.jmarijano.sucelja;

import java.util.List;
import org.foi.nwtis.jmarijano.modeli.Dnevnik;

/**
 *
 * @author LiterallyCan't
 */
public interface DnevnikService extends CRUDService<Dnevnik> {

    public List<Dnevnik> dohvatiUzFiltriranje(int stranica, int brojRedaka, String vrstaZahtjeva, int odVremena, int doVremena);

    public int brojRedaka();

    public List<String> dajSveDistinctZahtjeve();
}
