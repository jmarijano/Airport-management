package org.foi.nwtis.jmarijano;

import java.sql.DriverManager;
import java.sql.Connection;
import javax.servlet.ServletContext;
import org.foi.nwtis.jmarijano.konfiguracije.bp.BP_Konfiguracija;

public class ConnectionManager {

    private static String url = "";
    private static String korisnik = "";
    private static String lozinka = "";
    private static Connection con;

    /**
     * Metoda kreira konekciju prema bazi podataka.
     *
     * @param servletContext ServletContext objekt
     * @return objekt tipa Connection
     */
    public static Connection getConnection(ServletContext servletContext) {
        try {
            BP_Konfiguracija bpk = (BP_Konfiguracija) servletContext.getAttribute("BP_Konfig");
            url = bpk.getServerDatabase() + bpk.getUserDatabase();
            korisnik = bpk.getUserUsername();
            lozinka = bpk.getUserPassword();
            Class.forName(bpk.getDriverDatabase());
            con = DriverManager.getConnection(url, korisnik, lozinka);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return con;
    }

}
