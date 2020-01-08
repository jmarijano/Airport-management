package org.foi.nwtis.jmarijano.mysql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletContext;
import org.foi.nwtis.jmarijano.modeli.Aerodrom;
import org.foi.nwtis.jmarijano.ConnectionManager;
import org.foi.nwtis.jmarijano.sucelja.MyAirportsService;
import org.foi.nwtis.rest.podaci.Lokacija;

public class MyAirportsMySQL implements MyAirportsService {

    private final ServletContext servletContext;

    /**
     * Konstruktor klase.
     *
     * @param servletContext ServletContext objekt.
     */
    public MyAirportsMySQL(ServletContext servletContext) {
        this.servletContext = servletContext;
    }

    /**
     * Metoda koja dohvaća sve aerodroma iz tablice myairports.
     *
     * @return List<Aerodrom>
     * @throws SQLException
     */
    @Override
    public List<Aerodrom> dohvatiSve() {
        List<Aerodrom> aerodromi = new ArrayList<>();
        String upit = "SELECT Ident, Name, iso_country, coordinates FROM myairports ORDER BY ident";
        try (Connection connection = ConnectionManager.getConnection(servletContext);
                PreparedStatement ps = connection.prepareStatement(upit);) {
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Aerodrom ae = new Aerodrom();
                    ae.setIcao(rs.getString("ident"));
                    ae.setNaziv(rs.getString("Name"));
                    ae.setDrzava(rs.getString("iso_country"));
                    String[] polje = rs.getString("coordinates").split(", ");
                    Lokacija lokacija = new Lokacija(polje[0], polje[1]);
                    ae.setLokacija(lokacija);
                    aerodromi.add(ae);
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return aerodromi;
    }

    /**
     * Metoda koja dohvaća aerodrom s određenim IDENT kodom iz tablice
     * myairports.
     *
     * @param icao IDENT kod aerodroma.
     * @return objek tipa Aerodrom-
     */
    @Override
    public Aerodrom dohvatiPremaIdent(String icao) {
        Aerodrom output = new Aerodrom();
        try {
            String upit = "SELECT ident, name, iso_country, coordinates FROM myairports where ident = ?";
            try (Connection connection = ConnectionManager.getConnection(servletContext);
                    PreparedStatement ps = connection.prepareStatement(upit);) {
                ps.setString(1, icao.toUpperCase());
                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        output.setIcao(rs.getString("ident"));
                        output.setNaziv(rs.getString("Name"));
                        output.setDrzava(rs.getString("iso_country"));
                        String[] polje = rs.getString("coordinates").split(", ");
                        Lokacija lokacija = new Lokacija(polje[0], polje[1]);
                        output.setLokacija(lokacija);
                    }
                }
            } catch (SQLException ex) {
                Logger.getLogger(MyAirportsMySQL.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (Exception ex) {
            Logger.getLogger(MyAirportsMySQL.class.getName()).log(Level.SEVERE, null, ex);
        }
        return output;
    }

    /**
     * Metoda koja unosi aerodroma u tablicu myairports.
     *
     * @param aerodrom objekt tipa Aerodrom.
     * @return 1 ukoliko je aerodrom uspješno unesen. Inače 0.
     */
    @Override
    public int dodaj(Aerodrom aerodrom) {
        int output = 0;
        String upit = "INSERT INTO myairports(ident, name, iso_country, coordinates, stored) VALUES"
                + " (?, ?, ?, ?, ?)";
        try (Connection connection = ConnectionManager.getConnection(servletContext);
                PreparedStatement ps = connection.prepareStatement(upit);) {
            ps.setString(1, aerodrom.getIcao());
            ps.setString(2, aerodrom.getNaziv());
            ps.setString(3, aerodrom.getDrzava());
            String koordinate = aerodrom.getLokacija().getLatitude() + ", " + aerodrom.getLokacija().getLongitude();
            ps.setString(4, koordinate);
            Timestamp vrijeme = new Timestamp(System.currentTimeMillis());
            ps.setObject(5, vrijeme);
            output = ps.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return output;
    }

    /**
     * Metoda koja ažurira postojeći aerodrom u tablici myairports.
     *
     * @param aerodrom objekt tipa Aerodrom-
     * @return 1 ako je aerodrom uspješno ažuriran. Inače 0.
     */
    @Override
    public int azuriraj(Aerodrom aerodrom) {
        int output = 0;
        String upit = "UPDATE myairports SET name = ?, iso_country = ?,  coordinates = ?, stored = ? WHERE ident = ?";
        try (Connection connection = ConnectionManager.getConnection(servletContext);
                PreparedStatement ps = connection.prepareStatement(upit)) {
            ps.setString(1, aerodrom.getNaziv());
            ps.setString(2, aerodrom.getDrzava());
            String koordinate = aerodrom.getLokacija().getLatitude() + ", " + aerodrom.getLokacija().getLongitude();
            ps.setString(3, koordinate);
            Timestamp vrijeme = new Timestamp(System.currentTimeMillis());
            ps.setObject(4, vrijeme);
            ps.setString(5, aerodrom.getIcao());
            output = ps.executeUpdate();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return output;
    }

    /**
     * Metoda koja briše određeni aerodrom iz tablice
     *
     * @param aerodrom
     * @param icao IDENT kod aerodroma.
     * @return 1 ako je aerodrom uspješno obrisan. Inače 0.
     */
    @Override
    public int izbrisi(Aerodrom aerodrom) {
        int output = 0;
        String upit = "DELETE FROM myairports WHERE ident = ?";
        try (Connection connection = ConnectionManager.getConnection(servletContext);
                PreparedStatement ps = connection.prepareStatement(upit)) {
            ps.setString(1, aerodrom.getIcao());
            output = ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return output;
    }
}
