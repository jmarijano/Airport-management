/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
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
import org.foi.nwtis.jmarijano.ConnectionManager;
import org.foi.nwtis.jmarijano.modeli.Dnevnik;
import org.foi.nwtis.jmarijano.sucelja.DnevnikService;

/**
 *
 * @author LiterallyCan't
 */
public class DnevnikMySQL implements DnevnikService {
    
    private final ServletContext servletContext;
    
    public DnevnikMySQL(ServletContext servletContext) {
        this.servletContext = servletContext;
    }
    
    @Override
    public List<Dnevnik> dohvatiSve() {
        List<Dnevnik> output = new ArrayList<>();
        String upit = "SELECT id, vrijeme, zahtjev, vrstaZahtjeva, dioAplikacije, korisnickoIme, opis"
                + " FROM dnevnik ";
        try (Connection connection = ConnectionManager.getConnection(servletContext);
                PreparedStatement ps = connection.prepareStatement(upit);) {
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Dnevnik dnevnik = new Dnevnik();
                    dnevnik.setId(rs.getInt("id"));
                    dnevnik.setVrijeme((Timestamp) rs.getObject("vrijeme"));
                    dnevnik.setVrstaZahtjeva(rs.getString("vrstaZahtjeva"));
                    dnevnik.setZahtjev(rs.getString("zahtjev"));
                    dnevnik.setDioAplikacije(rs.getInt("dioAplikacije"));
                    output.add(dnevnik);
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(AirplanesMySQL.class.getName()).log(Level.SEVERE, null, ex);
        }
        return output;
    }
    
    @Override
    public int izbrisi(Dnevnik t) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    @Override
    public int dodaj(Dnevnik t) {
        int output = 0;
        String upit = "INSERT INTO dnevnik(id, vrijeme, zahtjev, vrstaZahtjeva, dioAplikacije, korisnickoIme, opis) VALUES"
                + " (DEFAULT, ?, ?, ?, ?, ?, ?)";
        try (Connection connection = ConnectionManager.getConnection(servletContext);
                PreparedStatement ps = connection.prepareStatement(upit);) {
            Timestamp vrijeme = new Timestamp(System.currentTimeMillis());
            ps.setObject(1, vrijeme);
            ps.setString(2, t.getZahtjev());
            ps.setString(3, t.getVrstaZahtjeva());
            ps.setInt(4, t.getDioAplikacije());
            ps.setString(5, t.getKorisnickoIme());
            ps.setString(6, t.getOpis());
            output = ps.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return output;
    }
    
    @Override
    public int azuriraj(Dnevnik t) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    @Override
    public List<Dnevnik> dohvatiUzFiltriranje(int stranica, int brojRedaka, String vrstaZahtjeva, int odVremena, int doVremena) {
        List<Dnevnik> output = new ArrayList<>();
        String upit = "SELECT id, vrijeme, zahtjev, vrstaZahtjeva, dioAplikacije, korisnickoIme, opis"
                + " FROM dnevnik WHERE 1=1 ";
        if (stranica == 0) {
            stranica = 1;
        }
        int pocetak = (stranica - 1) * brojRedaka;
        try {
            if (vrstaZahtjeva != null && !vrstaZahtjeva.isEmpty()) {
                upit += " AND vrstaZahtjeva = ?";
            }
            if (odVremena > 0 && doVremena > 0) {
                upit += " AND vrijeme between ? and ?";
            }
            upit += " LIMIT ?, ?";
        } catch (Exception e) {
            e.printStackTrace();
        }
        try (Connection connection = ConnectionManager.getConnection(servletContext);
                PreparedStatement ps = connection.prepareStatement(upit);) {
            if (vrstaZahtjeva != null && !vrstaZahtjeva.isEmpty()) {
                ps.setString(1, vrstaZahtjeva);
                if (odVremena > 0 && doVremena > 0) {
                    ps.setInt(2, odVremena);
                    ps.setInt(3, doVremena);
                    ps.setInt(4, pocetak);
                    ps.setInt(5, brojRedaka);
                } else {
                    ps.setInt(2, pocetak);
                    ps.setInt(3, brojRedaka);
                }
            } else {
                if (odVremena > 0 && doVremena > 0) {
                    ps.setInt(1, odVremena);
                    ps.setInt(2, doVremena);
                    ps.setInt(3, pocetak);
                    ps.setInt(4, brojRedaka);
                } else {
                    ps.setInt(1, pocetak);
                    ps.setInt(2, brojRedaka);
                }
            }
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Dnevnik dnevnik = new Dnevnik();
                    dnevnik.setId(rs.getInt("id"));
                    dnevnik.setVrijeme((Timestamp) rs.getObject("vrijeme"));
                    dnevnik.setVrstaZahtjeva(rs.getString("vrstaZahtjeva"));
                    dnevnik.setZahtjev(rs.getString("zahtjev"));
                    dnevnik.setDioAplikacije(rs.getInt("dioAplikacije"));
                    output.add(dnevnik);
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(AirplanesMySQL.class.getName()).log(Level.SEVERE, null, ex);
        }
        return output;
    }
    
    @Override
    public int brojRedaka() {
        int output = 0;
        String upit = "SELECT COUNT(*)"
                + " FROM dnevnik";
        try (Connection connection = ConnectionManager.getConnection(servletContext);
                PreparedStatement ps = connection.prepareStatement(upit);) {
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    output = rs.getInt(1);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return output;
    }
    
    @Override
    public List<String> dajSveDistinctZahtjeve() {
        List<String> output = new ArrayList<>();
        String upit = "SELECT DISTINCT zahtjev"
                + " FROM dnevnik ";
        try (Connection connection = ConnectionManager.getConnection(servletContext);
                PreparedStatement ps = connection.prepareStatement(upit);) {
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    String zahtjev = rs.getString("zahtjev");
                    output.add(zahtjev);
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(AirplanesMySQL.class.getName()).log(Level.SEVERE, null, ex);
        }
        return output;
    }
    
}
