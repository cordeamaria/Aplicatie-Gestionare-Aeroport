package repository;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Zbor;
import java.time.LocalDateTime;

public class ZborRepository {
    private static final ObservableList<Zbor> zboruri = FXCollections.observableArrayList(
            new Zbor(1L, "RO332", 101L, "Bucuresti", "Londra", LocalDateTime.now().plusDays(1), LocalDateTime.now().plusDays(1).plusHours(3), "programat", "Scari, Cisterna", 5, 120),
            new Zbor(2L, "RO445", 102L, "Cluj", "Paris", LocalDateTime.now().plusDays(2), LocalDateTime.now().plusDays(2).plusHours(2), "imbarcare", "Platforma bagaje", 4, 150),
            new Zbor(3L, "RO991", 103L, "Bucuresti", "Viena", LocalDateTime.now().plusDays(1), LocalDateTime.now().plusDays(1).plusHours(1), "anulat", "N/A", 0, 0)
    );

    public ObservableList<Zbor> getAllZboruri() { return zboruri; }

    public static Zbor getZbor(long id) {
        return zboruri.stream().filter(z -> z.getId_zbor() == id).findFirst().orElse(null);
    }
    // CREATE: ADAUGA ZBOR [cite: 22, 52]
    public void add(Zbor zbor) {
        zboruri.add(zbor);
        // Aici se va face apelul catre Server: client.sendToServer(zbor)
    }

    // UPDATE: MODIFICA ZBOR [cite: 22, 56]
    public void update(Zbor zbor) {
        for (int i = 0; i < zboruri.size(); i++) {
            if (zboruri.get(i).getId_zbor().equals(zbor.getId_zbor())) {
                zboruri.set(i, zbor);
                break;
            }
        }
    }

    // DELETE: STERGE ZBOR [cite: 22, 59]
    public void delete(Zbor zbor) {
        zboruri.remove(zbor);
    }
    public Zbor findByCod(String cod) {
        return zboruri.stream()
                .filter(z -> z.getCod_zbor().equalsIgnoreCase(cod))
                .findFirst()
                .orElse(null);
    }
}