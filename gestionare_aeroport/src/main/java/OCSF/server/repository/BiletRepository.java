package OCSF.server.repository;

import OCSF.server.repository.AbstractDAO;
import model.Bilet;
import model.Zbor;

import java.util.List;
import java.util.stream.Collectors;


public class BiletRepository extends AbstractDAO<Bilet> {

    // Helper repository to look up Flight Codes
    private ZborRepository zborRepo = new ZborRepository();

    @Override
    public List<Bilet> findAll() {
        List<Bilet> bilete = super.findAll();
        populateFlightCodes(bilete);
        return bilete;
    }

    public List<Bilet> getBileteByUserId(long userId) {
        // 1. Get raw tickets from DB
        List<Bilet> userTickets = super.findAll().stream()
                .filter(b -> b.getId_pasager() != null && b.getId_pasager().equals(userId))
                .collect(Collectors.toList());

        // 2. Fill in the Flight Codes (e.g., "RO332")
        populateFlightCodes(userTickets);

        return userTickets;
    }

    /**
     * This method goes through every ticket, finds the matching Flight,
     * and updates the 'codZborCached' field so the Client can see it.
     */
    private void populateFlightCodes(List<Bilet> bilete) {
        if (bilete == null) return;

        // Optimize: Get all flights once implies caching, but for now we look up individually
        // (Simpler for your current phase)
        List<Zbor> allFlights = zborRepo.findAll();

        for (Bilet b : bilete) {
            if (b.getId_zbor() != null) {
                // Find the matching flight
                Zbor match = allFlights.stream()
                        .filter(z -> z.getId().equals(b.getId_zbor().intValue())) // Match IDs
                        .findFirst()
                        .orElse(null);

                // If found, "set" the dynamic value
                if (match != null) {
                    b.setCodZbor(match.getCod_zbor());
                }
            }
        }
    }
}