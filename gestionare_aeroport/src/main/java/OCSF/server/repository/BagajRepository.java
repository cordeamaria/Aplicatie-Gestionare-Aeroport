package OCSF.server.repository;

import OCSF.server.repository.AbstractDAO;
import model.Bagaj;
import model.Bilet;
import java.util.List;
import java.util.stream.Collectors;

public class BagajRepository extends AbstractDAO<Bagaj> {

    public List<Bagaj> getBagajeByUserId(long userId, BiletRepository biletRepo) {
        List<Long> userTicketIds = biletRepo.getBileteByUserId(userId).stream()
                .map(b -> b.getId() != null ? b.getId().longValue() : 0L)
                .collect(Collectors.toList());

        return findAll().stream()
                .filter(b -> userTicketIds.contains(b.getId_bilet()))
                .collect(Collectors.toList());
    }
}
