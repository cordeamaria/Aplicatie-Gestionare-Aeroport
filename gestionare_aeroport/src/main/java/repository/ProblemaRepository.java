package repository;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Problema;
import java.time.LocalDateTime;

public class ProblemaRepository {
    private final ObservableList<Problema> mockProbleme = FXCollections.observableArrayList(
            new Problema(1L, 1L, "Scurgere ulei stand 4", "noua", LocalDateTime.now(), null)
    );

    public ObservableList<Problema> getAllProbleme() {
        // Corespunde pasului "Apelare serviciu toate problemele" [cite: 45]
        return mockProbleme;
    }

    public void updateStatus(Problema problema) {
        // Corespunde pasului "Apelare serviciu actualizare status"
        // În implementarea finală, trimite obiectul la server pentru UPDATE în baza de date
        System.out.println("Status actualizat pe server pentru problema: " + problema.getId_problema());
    }
    public void add(Problema problema) {
        // Trimite problema catre server pentru a fi vazuta de muncitori [cite: 63]
        System.out.println("Problema raportata de dispecer: " + problema.getDescriere());
    }
}