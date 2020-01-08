/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.jmarijano.sb;

import java.util.ArrayList;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import org.foi.nwtis.jmarijano.eb.MqttPoruke;
import org.foi.nwtis.jmarijano.eb.MqttPoruke_;

/**
 *
 * @author LiterallyCan't
 */
@Stateless
public class MqttPorukeFacade extends AbstractFacade<MqttPoruke> {

    @PersistenceContext(unitName = "jmarijano_aplikacija_2_1PU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public MqttPorukeFacade() {
        super(MqttPoruke.class);
    }

    public List<MqttPoruke> dajSvePorukeKorisnika(String korisnik) {
        List<MqttPoruke> output = new ArrayList<>();
        try {
            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery cq = cb.createQuery();
            Root<MqttPoruke> mqttRoot = cq.from(MqttPoruke.class);
            List<Predicate> uvjeti = new ArrayList<>();
            uvjeti.add(cb.equal(mqttRoot.get(MqttPoruke_.korisnik), korisnik));
            cq.select(mqttRoot);
            cq.where(uvjeti.toArray(new Predicate[]{}));
            Query q = em.createQuery(cq);
            List<Object[]> rezultat = q.getResultList();
            for (Object objects : rezultat) {
                MqttPoruke mqttPoruke = new MqttPoruke();
                mqttPoruke = (MqttPoruke) objects;
                output.add(mqttPoruke);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return output;
    }

    public boolean obrisiSvePorukeKorisnika(String korisnik) {
        boolean output = false;
        int brojObisanih = 0;
        try {
            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery cq = cb.createQuery();
            Root<MqttPoruke> mqttRoot = cq.from(MqttPoruke.class);
            List<Predicate> uvjeti = new ArrayList<>();
            uvjeti.add(cb.equal(mqttRoot.get(MqttPoruke_.korisnik), korisnik));
            cq.select(mqttRoot);
            cq.where(uvjeti.toArray(new Predicate[]{}));
            Query q = em.createQuery(cq);
            List<Object[]> rezultat = q.getResultList();
            for (Object objects : rezultat) {
                MqttPoruke mqttPoruke = new MqttPoruke();
                mqttPoruke = (MqttPoruke) objects;
                this.remove(mqttPoruke);
                brojObisanih++;
            }
            if (brojObisanih > 0) {
                output = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return output;
    }

}
