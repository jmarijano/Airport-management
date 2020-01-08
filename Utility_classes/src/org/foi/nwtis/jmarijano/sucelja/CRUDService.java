/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.jmarijano.sucelja;

import java.util.List;

/**
 *
 * @author LiterallyCan't
 * @param <T>
 */
public interface CRUDService<T> {

    List<T> dohvatiSve();

    int izbrisi(T t);

    int dodaj(T t);

    int azuriraj(T t);
}
