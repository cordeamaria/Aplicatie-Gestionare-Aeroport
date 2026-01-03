package repository;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Aeronava;

public class AeronavaRepository {
    private final ObservableList<Aeronava> aeronave = FXCollections.observableArrayList(
            new Aeronava(101L, "RO332", "Boeing 737", 180, "in_mentenanta", "Hangar 1"),
            new Aeronava(102L, "FR445", "Airbus A320", 150, "activ", "Poarta 5"),
            new Aeronava(103L, "TAR99", "ATR 72", 70, "retras", "Sector Sud")
    );

    public ObservableList<Aeronava> getAllAeronave() {
        return aeronave;
    }
    // CREATE: ADAUGA AERONAVA [cite: 28, 54]
    public void add(Aeronava aeronava) {
        aeronave.add(aeronava);
    }

    // UPDATE: MODIFICA AERONAVA [cite: 28, 58]
    public void update(Aeronava aeronava) {
        for (int i = 0; i < aeronave.size(); i++) {
            if (aeronave.get(i).getId_aeronava().equals(aeronava.getId_aeronava())) {
                aeronave.set(i, aeronava);
                break;
            }
        }
    }

    // DELETE: STERGE AERONAVA [cite: 28, 61]
    public void delete(Aeronava aeronava) {
        aeronave.remove(aeronava);
    }
    public Aeronava findByCod(String cod) {
        return aeronave.stream()
                .filter(a -> a.getCod_aeronava().equalsIgnoreCase(cod))
                .findFirst()
                .orElse(null);
    }
}