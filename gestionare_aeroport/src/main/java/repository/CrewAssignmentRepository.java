package repository;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.CrewAssignment;

public class CrewAssignmentRepository {
    private static final ObservableList<CrewAssignment> assignments = FXCollections.observableArrayList();

    // READ: Vizualizare asignări
    public ObservableList<CrewAssignment> getAllAssignments() {
        return assignments;
    }

    // CREATE: Asignare personal (Conform diagramei Use Case)
    public void addAssignment(CrewAssignment assignment) {
        assignments.add(assignment);
    }

    // DELETE: Eliminare membru din echipaj
    public void removeAssignment(CrewAssignment assignment) {
        assignments.remove(assignment);
    }
}