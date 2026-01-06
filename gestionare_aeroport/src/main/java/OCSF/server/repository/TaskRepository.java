package OCSF.server.repository;

import OCSF.server.repository.AbstractDAO;
import model.Task;
import java.util.List;
import java.util.stream.Collectors;

public class TaskRepository extends AbstractDAO<Task> {

    public List<Task> getTasksByWorkerId(long workerId) {
        return findAll().stream()
                .filter(t -> t.getId_muncitor() != null && t.getId_muncitor().equals(workerId))
                .collect(Collectors.toList());
    }
}