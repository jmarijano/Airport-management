package org.foi.nwtis.jmarijano.web.slusaci;

import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import org.foi.nwtis.jmarijano.konfiguracije.Konfiguracija;
import org.foi.nwtis.jmarijano.konfiguracije.KonfiguracijaApstraktna;
import org.foi.nwtis.jmarijano.konfiguracije.NeispravnaKonfiguracija;
import org.foi.nwtis.jmarijano.konfiguracije.NemaKonfiguracije;
import org.foi.nwtis.jmarijano.konfiguracije.bp.BP_Konfiguracija;
import org.foi.nwtis.jmarijano.web.dretve.PreuzimanjeAviona;
import org.foi.nwtis.jmarijano.web.dretve.ServerAerodroma;

@WebListener
public class SlusacAplikacije implements ServletContextListener {

    private static ServletContext sc;
    private PreuzimanjeAviona preuzimanjeAviona;
    private static Konfiguracija konfig;
    private BP_Konfiguracija bpk;
    private ServerAerodroma serverAerodroma;

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
            String bp_konfig = dohvatiPutanju("BP_Konfig");
            String konfiguracija = dohvatiPutanju("konfiguracija");
            if (!ucitajKonfiguDatoteku(konfiguracija) || !ucitajKonfiguracijskuDatoteku(bp_konfig)) {
                System.out.println("Dogodilo se nešto");
            } else {
                preuzimanjeAviona = new PreuzimanjeAviona();
                preuzimanjeAviona.start();
                serverAerodroma = new ServerAerodroma();
                serverAerodroma.start();
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
        if (preuzimanjeAviona != null) {
            preuzimanjeAviona.interrupt();
        }
        if (serverAerodroma != null) {
            serverAerodroma.interrupt();
        }
        sc = sce.getServletContext();
        sc.removeAttribute("BP_Konfig");
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
     * BP_Konfiguracija i dodavanje u ServletContext objekt.
     *
     * @param putanja Putanja do datoteke
     * @return True ako je učitana. Inače false.
     */
    public boolean ucitajKonfiguracijskuDatoteku(String putanja) {
        boolean output = false;
        try {
            bpk = new BP_Konfiguracija(putanja);
            sc.setAttribute("BP_Konfig", bpk);
            System.out.println("Učitana konfiguracija");
            output = true;
        } catch (NemaKonfiguracije | NeispravnaKonfiguracija ex) {
            Logger.getLogger(SlusacAplikacije.class.getName()).log(Level.SEVERE, null, ex);
        }
        return output;
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
}
