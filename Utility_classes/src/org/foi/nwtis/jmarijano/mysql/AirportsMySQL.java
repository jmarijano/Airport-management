package org.foi.nwtis.jmarijano.mysql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletContext;
import org.foi.nwtis.jmarijano.ConnectionManager;
import org.foi.nwtis.jmarijano.modeli.Aerodrom;
import org.foi.nwtis.jmarijano.sucelja.AirportsService;
import org.foi.nwtis.rest.podaci.Lokacija;

public class AirportsMySQL implements AirportsService {

    private final ServletContext servletContext;

    /**
     * Konstruktor
     *
     * @param servletContext ServletContext objekt
     */
    public AirportsMySQL(ServletContext servletContext) {
        this.servletContext = servletContext;
    }

    /**
     * Metoda dohvaća sve aerodrome iz tablice airports.
     *
     * @return List<Aerodrom>
     */
    @Override
    public List<Aerodrom> dohvatiSve() {
        List<Aerodrom> aerodromi = new ArrayList<>();
        String upit = "SELECT Ident, Name, iso_country, coordinates FROM airports";
        try (Connection connection = ConnectionManager.getConnection(servletContext);
                Statement s = connection.createStatement();
                ResultSet rs = s.executeQuery(upit);) {
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
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return aerodromi;
    }

    /**
     * Metoda dohvaća aerodrom s određenim IDENT kodom iz tablice airports.
     *
     * @param icao IDENT kod aerodroma
     * @return Objekt tipa Aerodrom
     */
    @Override
    public Aerodrom dohvatiPremaIdent(String icao) {
        Aerodrom output = new Aerodrom();
        try {
            String upit = "SELECT ident, name, iso_country FROM airports where ident = ?";
            try (Connection connection = ConnectionManager.getConnection(servletContext);
                    PreparedStatement ps = connection.prepareStatement(upit);) {
                ps.setString(1, icao.toUpperCase());
                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        output.setIcao(rs.getString("ident"));
                        output.setNaziv(rs.getString("Name"));
                        output.setDrzava(rs.getString("iso_country"));
                    }
                }
            } catch (SQLException ex) {
                Logger.getLogger(AirportsMySQL.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (Exception ex) {
            Logger.getLogger(AirportsMySQL.class.getName()).log(Level.SEVERE, null, ex);
        }
        return output;
    }

    @Override
    public int izbrisi(Aerodrom t) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public int dodaj(Aerodrom t) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public int azuriraj(Aerodrom t) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
