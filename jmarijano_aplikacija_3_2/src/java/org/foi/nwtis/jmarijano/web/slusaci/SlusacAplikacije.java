package org.foi.nwtis.jmarijano.web.slusaci;

import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import org.foi.nwtis.jmarijano.konfiguracije.Konfiguracija;
import org.foi.nwtis.jmarijano.konfiguracije.KonfiguracijaApstraktna;
import org.foi.nwtis.jmarijano.konfiguracije.NeispravnaKonfiguracija;
import org.foi.nwtis.jmarijano.konfiguracije.NemaKonfiguracije;
import org.foi.nwtis.jmarijano.sb.SingletonSBApp3_1;

@WebListener
public class SlusacAplikacije implements ServletContextListener {

    @EJB
    private SingletonSBApp3_1 singletonSB;
    private static ServletContext sc;
    private static Konfiguracija konfig;

    /**
     * Nadjačanje metode iz klase ServletContextListener. Služi za učitavanje
     * servletcontexta i konfiguracijske datoteke
     *
     * @param sce ServletContextEvent objekt.
     */
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        try {
            sc = sce.getServletContext();
            String putanja = dohvatiPutanju("konfiguracija");
            String kae = sc.getRealPath("/web/WEB-INF") + File.separator + "kae";
            System.out.println("Putanja: " + putanja);
            System.out.println("KAEKASEKAK: " + kae);
            if (ucitajKonfiguDatoteku(putanja)) {
                String putanjaMQtt = dohvatiPutanjuDatoteke("datotekaMQTT");
                String putanjaPosluString = dohvatiPutanjuDatoteke("datotekaPosluzitelja");
                singletonSB.dohvatiPutanjuDatotekeMQTT(putanjaMQtt);
                singletonSB.dohvatiPutanjuDatotekePosluzitelj(putanjaPosluString);
                System.out.println("Sve je uspješno učitano");
            } else {
                System.out.println("Dogodila se greška kod učitavanja putanje konfiguracije");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * Nadjačanje metode iz klase ServletContextListener. Uništava atribute iz
     * ServletContexta.
     *
     * @param sce ServletContextEvent objekt.
     */
    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        sc = sce.getServletContext();
        sc.removeAttribute("konfiguracija");
    }

    /**
     * Metoda koja dohvaća ServletContext objekt
     *
     * @return ServletContext objekt.
     */
    public static ServletContext getSc() {
        return sc;
    }

    /**
     * Metoda čija je svrha dohvatiti putanju parametra iz web.xml datoteke.
     *
     * @param nazivParametra Naziv parametra iz web.xml datoteke.
     * @return Putanja do datoteke u obliku String.
     */
    public String dohvatiPutanju(String nazivParametra) {
        String putanja = sc.getRealPath("/WEB-INF") + File.separator + sc.getInitParameter(nazivParametra);

        return putanja;
    }

    /**
     * Metoda čija je svrha učitavanje konfiguracijske datoteke u objekt
     * Konfiguracija i dodavanje u ServletContext objekt.
     *
     * @param putanja Putanja do datoteke.
     * @return True ako je učitana. Inače false.
     */
    public boolean ucitajKonfiguDatoteku(String putanja) {
        boolean output = false;
        try {
            konfig = KonfiguracijaApstraktna.preuzmiKonfiguraciju(putanja);
            sc.setAttribute("konfiguracija", konfig);
            output = true;
        } catch (NemaKonfiguracije | NeispravnaKonfiguracija ex) {
            Logger.getLogger(SlusacAplikacije.class.getName()).log(Level.SEVERE, null, ex);
        }
        return output;
    }

    public static void setKonfiguracija(Konfiguracija konfiguracija) {
        sc.setAttribute("konfiguracija", konfiguracija);
    }

    public static Konfiguracija getKonfiguracija() {
        return konfig;
    }

    public String dohvatiPutanjuDatoteke(String nazivDatoteke) {
        String putanja = sc.getRealPath("/WEB-INF") + File.separator + konfig.dajPostavku(nazivDatoteke);
        String[] polje = putanja.split("jmarijano_aplikacija_3");
        polje[0] += "jmarijano_aplikacija_3_2\\web\\WEB-INF\\" + konfig.dajPostavku(nazivDatoteke);
        System.out.println("PPRAVA PUTANJA: " + polje[0]);
        return polje[0];
    }
}
