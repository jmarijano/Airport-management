/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.jmarijano.modeli;

import java.util.ArrayList;
import java.util.List;
import org.foi.nwtis.jmarijano.web.servisi.ws.AvionLeti;

/**
 *
 * @author LiterallyCan't
 */
public class AvionLetiConverter {

    public static List<org.foi.nwtis.rest.podaci.AvionLeti> convert(List<org.foi.nwtis.jmarijano.web.servisi.ws.AvionLeti> zaConvertati) {
        List<org.foi.nwtis.rest.podaci.AvionLeti> output = new ArrayList<>();
        for (AvionLeti avionLeti : zaConvertati) {
            org.foi.nwtis.rest.podaci.AvionLeti noviAvionLeti = new org.foi.nwtis.rest.podaci.AvionLeti();
            noviAvionLeti.setArrivalAirportCandidatesCount(avionLeti.getArrivalAirportCandidatesCount());
            noviAvionLeti.setCallsign(avionLeti.getCallsign());
            noviAvionLeti.setDepartureAirportCandidatesCount(avionLeti.getDepartureAirportCandidatesCount());
            noviAvionLeti.setEstArrivalAirport(avionLeti.getEstArrivalAirport());
            noviAvionLeti.setEstArrivalAirportHorizDistance(avionLeti.getEstArrivalAirportHorizDistance());
            noviAvionLeti.setEstArrivalAirportVertDistance(avionLeti.getEstArrivalAirportVertDistance());
            noviAvionLeti.setEstDepartureAirport(avionLeti.getEstDepartureAirport());
            noviAvionLeti.setEstDepartureAirportHorizDistance(avionLeti.getEstDepartureAirportHorizDistance());
            noviAvionLeti.setEstDepartureAirportVertDistance(avionLeti.getEstDepartureAirportVertDistance());
            noviAvionLeti.setFirstSeen(avionLeti.getFirstSeen());
            noviAvionLeti.setIcao24(avionLeti.getIcao24());
            noviAvionLeti.setLastSeen(avionLeti.getLastSeen());
            output.add(noviAvionLeti);
        }
        return output;
    }
}
