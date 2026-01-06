package OCSF.server.repository;

import OCSF.server.repository.AbstractDAO;
import model.Aeronava;

public class AeronavaRepository extends AbstractDAO<Aeronava> {

    public Aeronava findByCod(String cod) {
        return findAll().stream()
                .filter(a -> a.getCod_aeronava().equalsIgnoreCase(cod))
                .findFirst()
                .orElse(null);
    }
}