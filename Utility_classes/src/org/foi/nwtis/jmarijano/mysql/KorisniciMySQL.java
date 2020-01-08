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
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletContext;
import org.foi.nwtis.jmarijano.ConnectionManager;
import org.foi.nwtis.jmarijano.modeli.Korisnik;
import org.foi.nwtis.jmarijano.sucelja.KorisnikService;

/**
 *
 * @author LiterallyCan't
 */
public class KorisniciMySQL implements KorisnikService {

    private final ServletContext servletContext;

    public KorisniciMySQL(ServletContext servletContext) {
        this.servletContext = servletContext;
    }

    @Override
    public List<Korisnik> dohvatiSve() {
        List<Korisnik> output = new ArrayList<>();
        String upit = "SELECT id, korisnicko_ime, lozinka, ime, prezime, email "
                + " FROM korisnici";
        try (Connection connection = ConnectionManager.getConnection(servletContext);
                PreparedStatement ps = connection.prepareStatement(upit);) {
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Korisnik korisnik = new Korisnik();
                    korisnik.setId(rs.getInt("id"));
                    korisnik.setKorisnickoIme(rs.getString("korisnicko_ime"));
                    korisnik.setLozinka(rs.getString("lozinka"));
                    korisnik.setIme(rs.getString("ime"));
                    korisnik.setPrezime(rs.getString("prezime"));
                    korisnik.setEmail(rs.getString("email"));
                    output.add(korisnik);
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(KorisniciMySQL.class.getName()).log(Level.SEVERE, null, ex);
        }
        return output;
    }

    @Override
    public Korisnik getByKorisnickoImeAndLozinka(String korisnickoIme, String lozinka) {
        Korisnik korisnik = new Korisnik();
        String upit = "SELECT id, korisnicko_ime, lozinka, ime, prezime, email"
                + " FROM korisnici"
                + " WHERE korisnicko_ime = ?"
                + " AND lozinka = ?";
        try (Connection connection = ConnectionManager.getConnection(servletContext);
                PreparedStatement ps = connection.prepareStatement(upit);) {
            ps.setString(1, korisnickoIme.toLowerCase());
            ps.setString(2, lozinka);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    korisnik.setId(rs.getInt("id"));
                    korisnik.setKorisnickoIme(rs.getString("korisnicko_ime"));
                    korisnik.setLozinka(rs.getString("lozinka"));
                    korisnik.setIme(rs.getString("ime"));
                    korisnik.setPrezime(rs.getString("prezime"));
                    korisnik.setEmail(rs.getString("email"));
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return korisnik;
    }

    @Override
    public int dodaj(Korisnik korisnik) {
        int output = 0;
        if (korisnik.getEmail() != null && korisnik.getIme() != null
                && korisnik.getKorisnickoIme() != null && korisnik.getLozinka() != null
                && korisnik.getPrezime() != null && !korisnik.getEmail().isEmpty()
                && !korisnik.getIme().isEmpty()
                && !korisnik.getKorisnickoIme().isEmpty()
                && !korisnik.getLozinka().isEmpty()
                && !korisnik.getPrezime().isEmpty()) {
            if (provjeriPostojanjeKorisnickogImena(korisnik.getKorisnickoIme()) == 0) {
                String upit = "INSERT INTO korisnici(id,korisnicko_ime, lozinka, ime, prezime, email)"
                        + " VALUES (DEFAULT, ?, ?, ?, ?, ?)";
                try (Connection connection = ConnectionManager.getConnection(servletContext);
                        PreparedStatement ps = connection.prepareStatement(upit);) {
                    ps.setString(1, korisnik.getKorisnickoIme());
                    ps.setString(2, korisnik.getLozinka());
                    ps.setString(3, korisnik.getIme());
                    ps.setString(4, korisnik.getPrezime());
                    ps.setString(5, korisnik.getEmail());
                    output = ps.executeUpdate();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        }
        return output;
    }

    @Override
    public int izbrisi(Korisnik t) {
        int output = 0;
        String upit = "DELETE FROM korisnici WHERE korisnicko_ime = ?";
        try (Connection connection = ConnectionManager.getConnection(servletContext);
                PreparedStatement ps = connection.prepareStatement(upit)) {
            ps.setString(1, t.getKorisnickoIme());
            output = ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return output;
    }

    @Override
    public int azuriraj(Korisnik t) {
        int output = 0;
        String upit = "UPDATE korisnici SET lozinka = ?,  ime = ?, prezime = ?, email = ? WHERE korisnicko_ime = ?";
        try (Connection connection = ConnectionManager.getConnection(servletContext);
                PreparedStatement ps = connection.prepareStatement(upit)) {
            ps.setString(1, t.getLozinka());
            ps.setString(2, t.getIme());
            ps.setString(3, t.getPrezime());
            ps.setString(4, t.getEmail());
            ps.setString(5, t.getKorisnickoIme());
            output = ps.executeUpdate();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return output;
    }

    @Override
    public List<Korisnik> dovatiSveUzStranicenje(int stranica, int brojRedaka) {
        List<Korisnik> output = new ArrayList<>();
        if (stranica < 1) {
            stranica = 1;
        }
        int pocetak = (stranica - 1) * brojRedaka;
        String upit = "SELECT id, korisnicko_ime, lozinka, ime, prezime, email "
                + " FROM korisnici limit ?, ?";
        try (Connection connection = ConnectionManager.getConnection(servletContext);
                PreparedStatement ps = connection.prepareStatement(upit);) {
            ps.setInt(1, pocetak);
            ps.setInt(2, brojRedaka);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Korisnik korisnik = new Korisnik();
                    korisnik.setId(rs.getInt("id"));
                    korisnik.setKorisnickoIme(rs.getString("korisnicko_ime"));
                    korisnik.setLozinka(rs.getString("lozinka"));
                    korisnik.setIme(rs.getString("ime"));
                    korisnik.setPrezime(rs.getString("prezime"));
                    korisnik.setEmail(rs.getString("email"));
                    output.add(korisnik);
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(KorisniciMySQL.class.getName()).log(Level.SEVERE, null, ex);
        }
        return output;
    }

    @Override
    public int brojRedaka() {
        int output = 0;
        String upit = "SELECT COUNT(*)"
                + " FROM korisnici";
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
    public Korisnik dohvatiPremaKorisnickomImenu(String korisnickoIme) {
        Korisnik output = new Korisnik();
        String upit = "SELECT id, korisnicko_ime, lozinka, ime, prezime, email "
                + " FROM korisnici"
                + " WHERE korisnicko_ime = ?";
        try (Connection connection = ConnectionManager.getConnection(servletContext);
                PreparedStatement ps = connection.prepareStatement(upit);) {
            ps.setString(1, korisnickoIme);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    output.setId(rs.getInt("id"));
                    output.setKorisnickoIme(rs.getString("korisnicko_ime"));
                    output.setLozinka(rs.getString("lozinka"));
                    output.setIme(rs.getString("ime"));
                    output.setPrezime(rs.getString("prezime"));
                    output.setEmail(rs.getString("email"));
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(KorisniciMySQL.class.getName()).log(Level.SEVERE, null, ex);
        }
        return output;
    }

    public int provjeriPostojanjeKorisnickogImena(String korisnickoIme) {
        int output = 0;
        String upit = "SELECT COUNT(*)"
                + " FROM korisnici"
                + " WHERE korisnicko_ime = ?";
        try (Connection connection = ConnectionManager.getConnection(servletContext);
                PreparedStatement ps = connection.prepareStatement(upit);) {
            ps.setString(1, korisnickoIme);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    output = rs.getInt(1);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            output = -1;
        }
        return output;
    }
}
