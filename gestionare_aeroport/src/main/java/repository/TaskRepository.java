package repository;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Task;
import java.time.LocalDateTime;

public class TaskRepository {
    private final ObservableList<Task> toateTaskurile = FXCollections.observableArrayList(
            new Task(1L, 1L, "Verificare presiune pneuri Aeronava RO332", "in_asteptare", LocalDateTime.now().minusHours(2), null),
            new Task(2L, 1L, "Alimentare combustibil Airbus A320", "in_desfasurare", LocalDateTime.now().minusMinutes(30), null),
            new Task(3L, 2L, "Curățenie cabină Boeing 737", "completat", LocalDateTime.now().minusDays(1), LocalDateTime.now().minusDays(1).plusHours(1))
    );

    public ObservableList<Task> getTasksByWorkerId(long workerId) {
        return toateTaskurile.filtered(t -> t.getId_muncitor() == workerId);
    }

    public void addTask(Task t) {
        toateTaskurile.add(t);
    }
}