package org.foi.nwtis.jmarijano.slusaci;

import com.google.gson.Gson;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.util.Properties;
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

@WebListener
public class SlusacAplikacije implements ServletContextListener {

    private static ServletContext sc;
    private static Konfiguracija konfig;

    public static String getKorisnik() {
        return korisnik;
    }
    private Gson gson;
    private Properties properties;
    private static String korisnik;

    /**
     * Nadjačanje metode iz klase ServletContextListener. Služi za učitavanje
     * servletcontexta i konfiguracijske datoteke
     *
     * @param sce ServletContextEvent objekt.
     */
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        try {
            gson = new Gson();
            properties = new Properties();
            sc = sce.getServletContext();
            String konfiguracija = dohvatiPutanju("konfiguracija");
            if (!ucitajKonfiguDatoteku(konfiguracija)) {
                System.out.println("Dogodilo se nešto");

            } else {
                File fileDir = new File(konfiguracija);

                BufferedReader in = new BufferedReader(
                        new InputStreamReader(
                                new FileInputStream(fileDir), "UTF8"));
                System.out.println("");
                BufferedReader br = new BufferedReader(new FileReader(fileDir));
                this.properties = gson.fromJson(in, Properties.class);
                korisnik = this.properties.getProperty("korisnik");
                br.close();
                in.close();
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
