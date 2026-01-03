package repository;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Bagaj;
import model.Bilet;

import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

public class BagajRepository {
    private final ObservableList<Bagaj> bagaje = FXCollections.observableArrayList();
    private final AtomicLong idGenerator = new AtomicLong(1);

    public long getNextId() { return idGenerator.getAndIncrement(); }

    public void addBagaj(Bagaj bagaj) { bagaje.add(bagaj); }

    public List<Bagaj> getBagajeByUserId(long userId, BiletRepository biletRepo) {
        List<Long> bileteIds = biletRepo.getBileteByUserId(userId).stream().map(Bilet::getId_bilet).toList();
        return bagaje.stream().filter(b -> bileteIds.contains(b.getId_bilet())).collect(Collectors.toList());
    }
}
