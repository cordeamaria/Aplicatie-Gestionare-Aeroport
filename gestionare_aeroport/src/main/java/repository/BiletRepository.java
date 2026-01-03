package repository;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Bilet;
import model.Zbor;
import repository.ZborRepository;

import java.util.List;
import java.util.stream.Collectors;

public class BiletRepository {

    private final ObservableList<Bilet> bilete = FXCollections.observableArrayList();

    public ObservableList<Bilet> getAll() {
        return bilete;
    }

    public void add(Bilet bilet) {
        bilete.add(bilet);
    }
    public List<Bilet> getBileteByUserId(long userId) {
        return bilete.stream()
                .filter(b -> b.getId_pasager() == userId)
                .collect(Collectors.toList());
    }


}
