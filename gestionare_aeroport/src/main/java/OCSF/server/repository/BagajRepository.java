package OCSF.server.repository;

import OCSF.server.repository.AbstractDAO;
import model.Bagaj;
import model.Bilet;
import java.util.List;
import java.util.stream.Collectors;

public class BagajRepository extends AbstractDAO<Bagaj> {

    // Complex query: Find bags belonging to a specific user
    public List<Bagaj> getBagajeByUserId(long userId, BiletRepository biletRepo) {
        // 1. Get all ticket IDs for this user
        List<Long> userTicketIds = biletRepo.getBileteByUserId(userId).stream()
                // REMOVED: .map(Bilet::getId_zbor)
                // We keep the stream as Bilet objects so we can call .getId() on them
                .map(b -> b.getId() != null ? b.getId().longValue() : 0L)
                .collect(Collectors.toList());

        // 2. Filter bags that belong to those tickets
        return findAll().stream()
                .filter(b -> userTicketIds.contains(b.getId_bilet()))
                .collect(Collectors.toList());
    }
}
