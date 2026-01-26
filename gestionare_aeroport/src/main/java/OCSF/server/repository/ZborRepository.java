package OCSF.server.repository;

import OCSF.server.repository.AbstractDAO;
import model.Zbor;

public class ZborRepository extends AbstractDAO<Zbor> {

    public Zbor findByCod(String cod) {
        return findAll().stream()
                .filter(z -> z.getCod_zbor().equalsIgnoreCase(cod))
                .findFirst()
                .orElse(null);
    }
}