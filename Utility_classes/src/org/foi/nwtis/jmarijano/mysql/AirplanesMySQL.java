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
import org.foi.nwtis.jmarijano.sucelja.AirplanesService;
import org.foi.nwtis.rest.podaci.AvionLeti;

public class AirplanesMySQL implements AirplanesService {
    
    private final ServletContext servletContext;

    /**
     * Konstruktor
     *
     * @param servletContext ServletContext objekt.
     */
    public AirplanesMySQL(ServletContext servletContext) {
        this.servletContext = servletContext;
    }

    /**
     * Metoda čija je svrha dohvaćanje svih aviona iz tablice airplanes.
     *
     * @return lista objekta AvionLeti tipa List<AvionLeti>
     */
    @Override
    public List<AvionLeti> dohvatiSve() {
        List<AvionLeti> avioni = new ArrayList<>();
        String upit = "SELECT icao24, firstSeen, estDepartureAirport, lastSeen, estArrivalAirport, callsign,"
                + " estDepartureAirportHorizDistance, estDepartureAirportVertDistance, estArrivalAirportHorizDistance, estArrivalAirportVertDistance,"
                + " departureAirportCandidatesCount, arrivalAirportCandidatesCount "
                + " FROM airplanes";
        try (Connection connection = ConnectionManager.getConnection(servletContext);
                PreparedStatement ps = connection.prepareStatement(upit);) {
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    AvionLeti avion = new AvionLeti();
                    avion.setIcao24(rs.getString("icao24"));
                    avion.setFirstSeen(rs.getInt("firstSeen"));
                    avion.setEstDepartureAirport(rs.getString("estDepartureAirport"));
                    avion.setLastSeen(rs.getInt("lastSeen"));
                    avion.setEstArrivalAirport(rs.getString("estArrivalAirport"));
                    avion.setCallsign(rs.getString("callsign"));
                    avion.setEstDepartureAirportHorizDistance(rs.getInt("estDepartureAirportHorizDistance"));
                    avion.setEstDepartureAirportVertDistance(rs.getInt("estDepartureAirportVertDistance"));
                    avion.setEstArrivalAirportHorizDistance(rs.getInt("estArrivalAirportHorizDistance"));
                    avion.setEstArrivalAirportVertDistance(rs.getInt("estArrivalAirportVertDistance"));
                    avion.setDepartureAirportCandidatesCount(rs.getInt("departureAirportCandidatesCount"));
                    avion.setArrivalAirportCandidatesCount(rs.getInt("arrivalAirportCandidatesCount"));
                    avioni.add(avion);
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return avioni;
    }

    /**
     * Metoda koja dohvaća avione koji su poletjeli s određenog aerodroma u
     * određenom vremenskom razdoblju
     *
     * @param icao IDENT kod polazišnog aerodroma.
     * @param odVremena početno vrijeme. UNIX vrijeme.
     * @param doVremena završno vrijeme. UNIX vrijeme.
     * @return List<AvionLeti>
     */
    @Override
    public List<AvionLeti> dajAvionePoletjeleSAerodroma(String icao, int odVremena, int doVremena) {
        List<AvionLeti> output = new ArrayList<>();
        String upit = "SELECT icao24, firstSeen, estDepartureAirport, lastSeen, estArrivalAirport, callsign,"
                + " estDepartureAirportHorizDistance, estDepartureAirportVertDistance, estArrivalAirportHorizDistance, estArrivalAirportVertDistance,"
                + " departureAirportCandidatesCount, arrivalAirportCandidatesCount "
                + " FROM airplanes "
                + " WHERE estDepartureAirport = ? "
                + " AND lastSeen BETWEEN ? AND ? ORDER BY firstSeen";
        try (Connection connection = ConnectionManager.getConnection(servletContext);
                PreparedStatement ps = connection.prepareStatement(upit);) {
            ps.setString(1, icao.toUpperCase());
            ps.setInt(2, odVremena);
            ps.setInt(3, doVremena);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    AvionLeti avion = new AvionLeti();
                    avion.setIcao24(rs.getString("icao24"));
                    avion.setFirstSeen(rs.getInt("firstSeen"));
                    avion.setEstDepartureAirport(rs.getString("estDepartureAirport"));
                    avion.setLastSeen(rs.getInt("lastSeen"));
                    avion.setEstArrivalAirport(rs.getString("estArrivalAirport"));
                    avion.setCallsign(rs.getString("callsign"));
                    avion.setEstDepartureAirportHorizDistance(rs.getInt("estDepartureAirportHorizDistance"));
                    avion.setEstDepartureAirportVertDistance(rs.getInt("estDepartureAirportVertDistance"));
                    avion.setEstArrivalAirportHorizDistance(rs.getInt("estArrivalAirportHorizDistance"));
                    avion.setEstArrivalAirportVertDistance(rs.getInt("estArrivalAirportVertDistance"));
                    avion.setDepartureAirportCandidatesCount(rs.getInt("departureAirportCandidatesCount"));
                    avion.setArrivalAirportCandidatesCount(rs.getInt("arrivalAirportCandidatesCount"));
                    output.add(avion);
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(AirplanesMySQL.class.getName()).log(Level.SEVERE, null, ex);
        }
        return output;
    }

    /**
     * Metoda koja dohvaća avione prema odredišnom aerodromu.
     *
     * @param icao IDENT kod odredišnog aerodroma.
     * @return List<AvionLeti>
     */
    @Override
    public List<AvionLeti> getByestArrivalAirport(String icao) {
        List<AvionLeti> output = new ArrayList<>();
        String upit = "SELECT icao24, firstSeen, estDepartureAirport, lastSeen, estArrivalAirport, callsign,"
                + " estDepartureAirportHorizDistance, estDepartureAirportVertDistance, estArrivalAirportHorizDistance, estArrivalAirportVertDistance,"
                + " departureAirportCandidatesCount, arrivalAirportCandidatesCount "
                + " FROM airplanes "
                + " WHERE estArrivalAirport = ? ";
        try (Connection connection = ConnectionManager.getConnection(servletContext);
                PreparedStatement ps = connection.prepareStatement(upit);) {
            ps.setString(1, icao.toUpperCase());
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    AvionLeti avion = new AvionLeti();
                    avion.setIcao24(rs.getString("icao24"));
                    avion.setFirstSeen(rs.getInt("firstSeen"));
                    avion.setEstDepartureAirport(rs.getString("estDepartureAirport"));
                    avion.setLastSeen(rs.getInt("lastSeen"));
                    avion.setEstArrivalAirport(rs.getString("estArrivalAirport"));
                    avion.setCallsign(rs.getString("callsign"));
                    avion.setEstDepartureAirportHorizDistance(rs.getInt("estDepartureAirportHorizDistance"));
                    avion.setEstDepartureAirportVertDistance(rs.getInt("estDepartureAirportVertDistance"));
                    avion.setEstArrivalAirportHorizDistance(rs.getInt("estArrivalAirportHorizDistance"));
                    avion.setEstArrivalAirportVertDistance(rs.getInt("estArrivalAirportVertDistance"));
                    avion.setDepartureAirportCandidatesCount(rs.getInt("departureAirportCandidatesCount"));
                    avion.setArrivalAirportCandidatesCount(rs.getInt("arrivalAirportCandidatesCount"));
                    output.add(avion);
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(AirplanesMySQL.class.getName()).log(Level.SEVERE, null, ex);
        }
        return output;
    }

    /**
     * Metoda koja unosi novi avion u tablicu airplanes.
     *
     * @param avion Objekt tipa AvionLeti
     * @return 1 ako je avion uspješno unesen. Inače 0.
     */
    @Override
    public int dodaj(AvionLeti avion) {
        int output = 0;
        String upit = "INSERT INTO airplanes(id, icao24, firstSeen,"
                + " estDepartureAirport, lastSeen, estArrivalAirport,"
                + " callsign, estDepartureAirportHorizDistance,"
                + " estDepartureAirportVertDistance,"
                + " estArrivalAirportHorizDistance,"
                + " estArrivalAirportVertDistance,"
                + " departureAirportCandidatesCount,"
                + " arrivalAirportCandidatesCount, stored)"
                + " VALUES (DEFAULT, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection connection = ConnectionManager.getConnection(servletContext);
                PreparedStatement ps = connection.prepareStatement(upit);) {
            ps.setString(1, avion.getIcao24());
            ps.setInt(2, avion.getFirstSeen());
            ps.setString(3, avion.getEstDepartureAirport());
            ps.setInt(4, avion.getLastSeen());
            ps.setString(5, avion.getEstArrivalAirport());
            ps.setString(6, avion.getCallsign());
            ps.setInt(7, avion.getEstDepartureAirportHorizDistance());
            ps.setInt(8, avion.getEstDepartureAirportVertDistance());
            ps.setInt(9, avion.getEstArrivalAirportHorizDistance());
            ps.setInt(10, avion.getEstArrivalAirportVertDistance());
            ps.setInt(11, avion.getDepartureAirportCandidatesCount());
            ps.setInt(12, avion.getArrivalAirportCandidatesCount());
            Timestamp vrijeme = new Timestamp(System.currentTimeMillis());
            ps.setObject(13, vrijeme);
            output = ps.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(AirplanesMySQL.class.getName()).log(Level.SEVERE, null, ex);
        }
        return output;
    }

    /**
     * Metoda koja dohvaća broj aviona koji imaju određeni icao24 kod i lastSeen
     * vrijeme.
     *
     * @param icao Icao24 kod aviona.
     * @param lastSeen vrijeme.
     * @return Broj aviona. 0 ako ne postoje avioni ili u slučaju greške.
     */
    @Override
    public int getCountByIcaoAndLastSeen(String icao, int lastSeen) {
        int output = 0;
        String upit = "SELECT COUNT(*) "
                + " FROM airplanes "
                + " WHERE icao24 = ? "
                + " AND lastSeen = ?";
        try (Connection connection = ConnectionManager.getConnection(servletContext);
                PreparedStatement ps = connection.prepareStatement(upit);) {
            ps.setString(1, icao);
            ps.setInt(2, lastSeen);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    output = rs.getInt(1);
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(AirplanesMySQL.class.getName()).log(Level.SEVERE, null, ex);
        }
        return output;
    }

    /**
     * Metoda koja provjerava da li je avion poletio s aerodroma u određenom
     * vremenskom razdoblju.
     *
     * @param icao24 Icao24 kod aviona.
     * @param icao IDENT kod polazišnog aerodroma.
     * @param odVremena Početno UNIX vrijeme.
     * @param doVremena Završno UNIX vrijeme.
     * @return Broj aviona koji zadovoljavaju kriterije. 0 ako ne postoje ili u
     * slučaju greške.
     */
    @Override
    public int provjeriAvionPoletioSAerodroma(String icao24, String icao, int odVremena, int doVremena) {
        int output = 0;
        String upit = "SELECT COUNT(*) "
                + " FROM airplanes"
                + " WHERE icao24 = ?"
                + " AND estDepartureAirport = ?"
                + " AND lastSeen BETWEEN ? AND ?";
        try (Connection connection = ConnectionManager.getConnection(servletContext);
                PreparedStatement ps = connection.prepareStatement(upit);) {
            ps.setString(1, icao24);
            ps.setString(2, icao);
            ps.setInt(3, odVremena);
            ps.setInt(4, doVremena);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    output = rs.getInt(1);
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(AirplanesMySQL.class.getName()).log(Level.SEVERE, null, ex);
        }
        return output;
    }

    /**
     * Metoda koja dohvaća broj aviona koji imaju određeni polazišni aerodrom u
     * određenom vremenskom razdoblju.
     *
     * @param aerodrom IDENT kod polazišnog aerodroma.
     * @param odVremena Početno UNIX vrijeme.
     * @param doVremena Završno UNIX vrijeme.
     * @return Broj aviona koji ispunjavaju te uvjete. 0 ako nema takvog aviona
     * ili u slučaju greške.
     */
    @Override
    public int brojAvionaDepartureAerodrom(String aerodrom, int odVremena, int doVremena) {
        int output = 0;
        String upit = "SELECT COUNT(*)"
                + " FROM airplanes"
                + " WHERE estDepartureAirport = ?"
                + " AND lastSeen BETWEEN ? AND ?";
        try (Connection connection = ConnectionManager.getConnection(servletContext);
                PreparedStatement ps = connection.prepareStatement(upit);) {
            ps.setString(1, aerodrom);
            ps.setInt(2, odVremena);
            ps.setInt(3, doVremena);
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

    /**
     * Metoda koja služi za dohvaćanje aviona s određenim polazišnim aerodromom
     * u određenom vremenskom razdoblju uz sortiranje prema vremenu spremanje te
     * isključivanje prema broju poslane stranice te broju redaka iz
     * konfiguracijske datoteke.
     *
     * @param aerodrom IDENT kod polazišnog aerodroma
     * @param stranica Broj stranice
     * @param brojRedaka Broj redaka
     * @param odVremena Početno UNIX vrijeme
     * @param doVremena Završno UNIX vrijeme
     * @return
     */
    @Override
    public List<AvionLeti> avioniZaTablicu(String aerodrom, int stranica, int brojRedaka, int odVremena, int doVremena) {
        List<AvionLeti> output = new ArrayList<>();
        if (stranica == 0) {
            stranica = 1;
        }
        int pocniOd = (stranica - 1) * brojRedaka;
        String upit = "SELECT *  FROM airplanes"
                + " WHERE estDepartureAirport = ?"
                + " AND lastSeen BETWEEN ? AND ?"
                + " ORDER BY stored DESC"
                + " OFFSET ? ROWS"
                + " FETCH NEXT ? ROWS ONLY";
        try (Connection connection = ConnectionManager.getConnection(servletContext);
                PreparedStatement ps = connection.prepareStatement(upit);) {
            ps.setString(1, aerodrom);
            ps.setInt(2, odVremena);
            ps.setInt(3, doVremena);
            ps.setInt(4, pocniOd);
            ps.setInt(5, brojRedaka);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    AvionLeti avion = new AvionLeti();
                    avion.setIcao24(rs.getString("icao24"));
                    avion.setFirstSeen(rs.getInt("firstSeen"));
                    avion.setEstDepartureAirport(rs.getString("estDepartureAirport"));
                    avion.setLastSeen(rs.getInt("lastSeen"));
                    avion.setEstArrivalAirport(rs.getString("estArrivalAirport"));
                    avion.setCallsign(rs.getString("callsign"));
                    avion.setEstDepartureAirportHorizDistance(rs.getInt("estDepartureAirportHorizDistance"));
                    avion.setEstDepartureAirportVertDistance(rs.getInt("estDepartureAirportVertDistance"));
                    avion.setEstArrivalAirportHorizDistance(rs.getInt("estArrivalAirportHorizDistance"));
                    avion.setEstArrivalAirportVertDistance(rs.getInt("estArrivalAirportVertDistance"));
                    avion.setDepartureAirportCandidatesCount(rs.getInt("departureAirportCandidatesCount"));
                    avion.setArrivalAirportCandidatesCount(rs.getInt("arrivalAirportCandidatesCount"));
                    output.add(avion);
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(AirplanesMySQL.class.getName()).log(Level.SEVERE, null, ex);
        }
        return output;
    }
    
    @Override
    public int izbrisi(AvionLeti t) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    @Override
    public int azuriraj(AvionLeti t) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    @Override
    public List<AvionLeti> dajPosljednjiNAvionaAerodroma(String ident, int brojAviona) {
        List<AvionLeti> output = new ArrayList<>();
        String upit = "SELECT icao24, firstSeen, estDepartureAirport, lastSeen, estArrivalAirport, callsign,"
                + " estDepartureAirportHorizDistance, estDepartureAirportVertDistance, estArrivalAirportHorizDistance, estArrivalAirportVertDistance,"
                + " departureAirportCandidatesCount, arrivalAirportCandidatesCount "
                + " FROM airplanes "
                + " WHERE estDepartureAirport = ?"
                + " ORDER BY id DESC"
                + " LIMIT ?";
        try (Connection connection = ConnectionManager.getConnection(servletContext);
                PreparedStatement ps = connection.prepareStatement(upit);) {
            ps.setString(1, ident);
            ps.setInt(2, brojAviona);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    AvionLeti avion = new AvionLeti();
                    avion.setIcao24(rs.getString("icao24"));
                    avion.setFirstSeen(rs.getInt("firstSeen"));
                    avion.setEstDepartureAirport(rs.getString("estDepartureAirport"));
                    avion.setLastSeen(rs.getInt("lastSeen"));
                    avion.setEstArrivalAirport(rs.getString("estArrivalAirport"));
                    avion.setCallsign(rs.getString("callsign"));
                    avion.setEstDepartureAirportHorizDistance(rs.getInt("estDepartureAirportHorizDistance"));
                    avion.setEstDepartureAirportVertDistance(rs.getInt("estDepartureAirportVertDistance"));
                    avion.setEstArrivalAirportHorizDistance(rs.getInt("estArrivalAirportHorizDistance"));
                    avion.setEstArrivalAirportVertDistance(rs.getInt("estArrivalAirportVertDistance"));
                    avion.setDepartureAirportCandidatesCount(rs.getInt("departureAirportCandidatesCount"));
                    avion.setArrivalAirportCandidatesCount(rs.getInt("arrivalAirportCandidatesCount"));
                    output.add(avion);
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(AirplanesMySQL.class.getName()).log(Level.SEVERE, null, ex);
        }
        return output;
    }
    
    @Override
    public List<String> dajNaziveAerodromaAviona(String icao24, int odVremena, int doVremena) {
        List<String> output = new ArrayList<>();
        String upit = "SELECT myairports.`name`"
                + " FROM airplanes,myairports"
                + " WHERE airplanes.`estDepartureAirport`= myairports.ident "
                + " AND airplanes.icao24 = ?"
                + " AND airplanes.firstSeen BETWEEN ? AND ? "
                + " ORDER BY airplanes.firstSeen";
        try (Connection connection = ConnectionManager.getConnection(servletContext);
                PreparedStatement ps = connection.prepareStatement(upit);) {
            ps.setString(1, icao24);
            ps.setInt(2, odVremena);
            ps.setInt(3, doVremena);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    String naziv = rs.getString("name");
                    output.add(naziv);
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(AirplanesMySQL.class.getName()).log(Level.SEVERE, null, ex);
        }
        return output;
    }
    
    @Override
    public List<AvionLeti> dajAerodromeAviona(String icao24, int odVremena, int doVremena) {
        List<AvionLeti> output = new ArrayList<>();
        String upit = "SELECT icao24, firstSeen, estDepartureAirport, lastSeen, estArrivalAirport, callsign,"
                + " estDepartureAirportHorizDistance, estDepartureAirportVertDistance, estArrivalAirportHorizDistance, estArrivalAirportVertDistance,"
                + " departureAirportCandidatesCount, arrivalAirportCandidatesCount"
                + " FROM airplanes"
                + " WHERE icao24 = ?"
                + " AND airplanes.firstSeen BETWEEN ? AND ?"
                + " ORDER BY `firstSeen`";
        try (Connection connection = ConnectionManager.getConnection(servletContext);
                PreparedStatement ps = connection.prepareStatement(upit);) {
            ps.setString(1, icao24);
            ps.setInt(2, odVremena);
            ps.setInt(3, doVremena);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    AvionLeti avion = new AvionLeti();
                    avion.setIcao24(rs.getString("icao24"));
                    avion.setFirstSeen(rs.getInt("firstSeen"));
                    avion.setEstDepartureAirport(rs.getString("estDepartureAirport"));
                    avion.setLastSeen(rs.getInt("lastSeen"));
                    avion.setEstArrivalAirport(rs.getString("estArrivalAirport"));
                    avion.setCallsign(rs.getString("callsign"));
                    avion.setEstDepartureAirportHorizDistance(rs.getInt("estDepartureAirportHorizDistance"));
                    avion.setEstDepartureAirportVertDistance(rs.getInt("estDepartureAirportVertDistance"));
                    avion.setEstArrivalAirportHorizDistance(rs.getInt("estArrivalAirportHorizDistance"));
                    avion.setEstArrivalAirportVertDistance(rs.getInt("estArrivalAirportVertDistance"));
                    avion.setDepartureAirportCandidatesCount(rs.getInt("departureAirportCandidatesCount"));
                    avion.setArrivalAirportCandidatesCount(rs.getInt("arrivalAirportCandidatesCount"));
                    output.add(avion);
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(AirplanesMySQL.class.getName()).log(Level.SEVERE, null, ex);
        }
        return output;
    }
    
    @Override
    public List<AvionLeti> dajSveAvionePoletjeleSAerdroma(String ident) {
        List<AvionLeti> output = new ArrayList<>();
        String upit = "SELECT icao24, firstSeen, estDepartureAirport, lastSeen, estArrivalAirport, callsign,"
                + " estDepartureAirportHorizDistance, estDepartureAirportVertDistance, estArrivalAirportHorizDistance, estArrivalAirportVertDistance,"
                + " departureAirportCandidatesCount, arrivalAirportCandidatesCount "
                + " FROM airplanes "
                + " WHERE estDepartureAirport = ? "
                + " ORDER BY firstSeen";
        try (Connection connection = ConnectionManager.getConnection(servletContext);
                PreparedStatement ps = connection.prepareStatement(upit);) {
            ps.setString(1, ident.toUpperCase());
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    AvionLeti avion = new AvionLeti();
                    avion.setIcao24(rs.getString("icao24"));
                    avion.setFirstSeen(rs.getInt("firstSeen"));
                    avion.setEstDepartureAirport(rs.getString("estDepartureAirport"));
                    avion.setLastSeen(rs.getInt("lastSeen"));
                    avion.setEstArrivalAirport(rs.getString("estArrivalAirport"));
                    avion.setCallsign(rs.getString("callsign"));
                    avion.setEstDepartureAirportHorizDistance(rs.getInt("estDepartureAirportHorizDistance"));
                    avion.setEstDepartureAirportVertDistance(rs.getInt("estDepartureAirportVertDistance"));
                    avion.setEstArrivalAirportHorizDistance(rs.getInt("estArrivalAirportHorizDistance"));
                    avion.setEstArrivalAirportVertDistance(rs.getInt("estArrivalAirportVertDistance"));
                    avion.setDepartureAirportCandidatesCount(rs.getInt("departureAirportCandidatesCount"));
                    avion.setArrivalAirportCandidatesCount(rs.getInt("arrivalAirportCandidatesCount"));
                    output.add(avion);
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(AirplanesMySQL.class.getName()).log(Level.SEVERE, null, ex);
        }
        return output;
    }
    
    @Override
    public int izbrisiSveAvionePolazisnogAerodroma(String ident) {
        int output = 0;
        String upit = "DELETE FROM airplanes WHERE estDepartureAirport = ?";
        try (Connection connection = ConnectionManager.getConnection(servletContext);
                PreparedStatement ps = connection.prepareStatement(upit)) {
            ps.setString(1, ident);
            System.out.println("Dogodilose ");
            output = ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("Output je tu: " + output);
        return output;
    }
    
    @Override
    public AvionLeti dajZadnjiPreuzetiAvionZaOdabraniAerodrom(String ident) {
        AvionLeti output = new AvionLeti();
        String upit = "SELECT icao24, firstSeen, estDepartureAirport, lastSeen, estArrivalAirport, callsign,"
                + " estDepartureAirportHorizDistance, estDepartureAirportVertDistance, estArrivalAirportHorizDistance, estArrivalAirportVertDistance,"
                + " departureAirportCandidatesCount, arrivalAirportCandidatesCount "
                + " FROM airplanes"
                + " WHERE estDepartureAirport = ?"
                + " ORDER BY id"
                + " LIMIT 1";
        try (Connection connection = ConnectionManager.getConnection(servletContext);
                PreparedStatement ps = connection.prepareStatement(upit);) {
            ps.setString(1, ident);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    output.setIcao24(rs.getString("icao24"));
                    output.setFirstSeen(rs.getInt("firstSeen"));
                    output.setEstDepartureAirport(rs.getString("estDepartureAirport"));
                    output.setLastSeen(rs.getInt("lastSeen"));
                    output.setEstArrivalAirport(rs.getString("estArrivalAirport"));
                    output.setCallsign(rs.getString("callsign"));
                    output.setEstDepartureAirportHorizDistance(rs.getInt("estDepartureAirportHorizDistance"));
                    output.setEstDepartureAirportVertDistance(rs.getInt("estDepartureAirportVertDistance"));
                    output.setEstArrivalAirportHorizDistance(rs.getInt("estArrivalAirportHorizDistance"));
                    output.setEstArrivalAirportVertDistance(rs.getInt("estArrivalAirportVertDistance"));
                    output.setDepartureAirportCandidatesCount(rs.getInt("departureAirportCandidatesCount"));
                    output.setArrivalAirportCandidatesCount(rs.getInt("arrivalAirportCandidatesCount"));
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(AirplanesMySQL.class.getName()).log(Level.SEVERE, null, ex);
        }
        return output;
    }
    
    @Override
    public int izbrisiAvionPolazisnogAerodroma(String ident, String icao24) {
        int output = 0;
        String upit = "DELETE FROM airplanes "
                + " WHERE estDepartureAirport = ?"
                + " AND icao24 = ?";
        try (Connection connection = ConnectionManager.getConnection(servletContext);
                PreparedStatement ps = connection.prepareStatement(upit)) {
            ps.setString(1, ident);
            ps.setString(2, icao24);
            output = ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return output;
    }
}
