/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.jmarijano.sucelja;

import java.util.List;
import org.foi.nwtis.jmarijano.modeli.Korisnik;

/**
 *
 * @author LiterallyCan't
 */
public interface KorisnikService extends CRUDService<Korisnik> {

    Korisnik getByKorisnickoImeAndLozinka(String korisnickoIme, String lozinka);

    List<Korisnik> dovatiSveUzStranicenje(int stranica, int brojRedaka);

    int brojRedaka();

    Korisnik dohvatiPremaKorisnickomImenu(String korisnickoIme);
}
