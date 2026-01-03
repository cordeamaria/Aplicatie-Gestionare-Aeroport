package controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import model.*;
import repository.*;
import java.time.LocalDateTime;

public class FieldWorkerController implements UserAware {
    @FXML private VBox userInfoPane, tasksPane, allFlightsPane, fleetPane, issuesPane;
    @FXML private Label userNameLabel, userRoleLabel;

    // Search Fields (Adăugate pentru funcția de Find/Search)
    @FXML private TextField searchFlightField;
    @FXML private TextField searchAirField;

    @FXML private TableView<Task> tasksTable;
    @FXML private TableColumn<Task, String> colTaskDesc, colTaskStatus;
    @FXML private TableColumn<Task, LocalDateTime> colTaskAllocated, colTaskFinished;

    @FXML private TableView<Zbor> technicalFlightTable;
    @FXML private TableColumn<Zbor, String> colFlightCode, colDeparture, colDestination, colFlightStatus, colEquipment;
    @FXML private TableColumn<Zbor, LocalDateTime> colDepartureTime, colArrivalTime;
    @FXML private TableColumn<Zbor, Integer> colCrewCount, colPassengerCount;

    @FXML private TableView<Aeronava> fleetTable;
    @FXML private TableColumn<Aeronava, String> colAirCode, colAirModel, colAirOpStatus, colAirLocation;
    @FXML private TableColumn<Aeronava, Integer> colAirCapacity;

    @FXML private TableView<Problema> issuesTable;
    @FXML private TableColumn<Problema, String> colIssueDesc, colIssueStatus;
    @FXML private TableColumn<Problema, LocalDateTime> colIssueReported, colIssueResolved;
    @FXML private ComboBox<String> statusUpdateCombo;

    private final TaskRepository taskRepository = new TaskRepository();
    private final ZborRepository zborRepository = new ZborRepository();
    private final AeronavaRepository aeronavaRepository = new AeronavaRepository();
    private final ProblemaRepository problemaRepository = new ProblemaRepository();
    private User loggedUser;

    @FXML
    public void initialize() {
        setupTableColumns();
        statusUpdateCombo.getItems().addAll("noua", "in_analiza", "rezolvata", "inchisa");
        hideAllPanels();
        userInfoPane.setVisible(true);
    }

    private void setupTableColumns() {
        // Mapare Task-uri
        colTaskDesc.setCellValueFactory(new PropertyValueFactory<>("descriere"));
        colTaskStatus.setCellValueFactory(new PropertyValueFactory<>("status"));
        colTaskAllocated.setCellValueFactory(new PropertyValueFactory<>("data_alocare"));
        colTaskFinished.setCellValueFactory(new PropertyValueFactory<>("data_finalizare"));

        // Mapare Zboruri
        colFlightCode.setCellValueFactory(new PropertyValueFactory<>("cod_zbor"));
        colDeparture.setCellValueFactory(new PropertyValueFactory<>("plecare_din"));
        colDestination.setCellValueFactory(new PropertyValueFactory<>("destinatie"));
        colDepartureTime.setCellValueFactory(new PropertyValueFactory<>("data_plecare"));
        colArrivalTime.setCellValueFactory(new PropertyValueFactory<>("data_sosire"));
        colFlightStatus.setCellValueFactory(new PropertyValueFactory<>("status_zbor"));
        colEquipment.setCellValueFactory(new PropertyValueFactory<>("echipamente_sol"));
        colCrewCount.setCellValueFactory(new PropertyValueFactory<>("nr_echipaj_bord"));
        colPassengerCount.setCellValueFactory(new PropertyValueFactory<>("nr_pasageri_estimat"));

        // Mapare Aeronave
        colAirCode.setCellValueFactory(new PropertyValueFactory<>("cod_aeronava"));
        colAirModel.setCellValueFactory(new PropertyValueFactory<>("model"));
        colAirCapacity.setCellValueFactory(new PropertyValueFactory<>("capacitate"));
        colAirOpStatus.setCellValueFactory(new PropertyValueFactory<>("stare_operationala"));
        colAirLocation.setCellValueFactory(new PropertyValueFactory<>("locatie_curenta"));

        // Mapare Probleme
        colIssueDesc.setCellValueFactory(new PropertyValueFactory<>("descriere"));
        colIssueStatus.setCellValueFactory(new PropertyValueFactory<>("status"));
        colIssueReported.setCellValueFactory(new PropertyValueFactory<>("data_raportare"));
        colIssueResolved.setCellValueFactory(new PropertyValueFactory<>("data_rezolvare"));
    }

    // ================= FUNCTII DE CAUTARE (Find By) =================

    @FXML
    public void handleSearchFlights() {
        String filter = searchFlightField.getText().toLowerCase();
        // Filtrare în timp real folosind lista din repository
        technicalFlightTable.setItems(zborRepository.getAllZboruri().filtered(z ->
                z.getCod_zbor().toLowerCase().contains(filter)
        ));
    }

    @FXML
    public void handleSearchAir() {
        String filter = searchAirField.getText().toLowerCase();
        // Filtrare în timp real a flotei
        fleetTable.setItems(aeronavaRepository.getAllAeronave().filtered(a ->
                a.getCod_aeronava().toLowerCase().contains(filter)
        ));
    }

    // ================= LOGICA NAVIGARE =================

    @Override
    public void setLoggedUser(User user) {
        this.loggedUser = user;
        userNameLabel.setText("Muncitor: " + user.getUsername());
        userRoleLabel.setText("Rol: " + user.getRol());
    }

    @FXML public void showTasksPane() { hideAllPanels(); tasksPane.setVisible(true); tasksPane.setManaged(true); tasksTable.setItems(taskRepository.getTasksByWorkerId(loggedUser.getId_user())); }
    @FXML public void showAllFlightsPane() { hideAllPanels(); allFlightsPane.setVisible(true); allFlightsPane.setManaged(true); technicalFlightTable.setItems(zborRepository.getAllZboruri()); }
    @FXML public void showFleetPane() { hideAllPanels(); fleetPane.setVisible(true); fleetPane.setManaged(true); fleetTable.setItems(aeronavaRepository.getAllAeronave()); }
    @FXML public void showIssuesPane() { hideAllPanels(); issuesPane.setVisible(true); issuesPane.setManaged(true); issuesTable.setItems(problemaRepository.getAllProbleme()); }

    @FXML
    public void handleUpdateIssueStatus() {
        Problema selected = issuesTable.getSelectionModel().getSelectedItem();
        String newStatus = statusUpdateCombo.getValue();
        if (selected == null || newStatus == null) return;
        selected.setStatus(newStatus);
        if (newStatus.equals("rezolvata") || newStatus.equals("inchisa")) selected.setData_rezolvare(LocalDateTime.now());
        problemaRepository.updateStatus(selected);
        issuesTable.refresh();
    }

    private void hideAllPanels() {
        tasksPane.setVisible(false); tasksPane.setManaged(false);
        allFlightsPane.setVisible(false); allFlightsPane.setManaged(false);
        fleetPane.setVisible(false); fleetPane.setManaged(false);
        issuesPane.setVisible(false); issuesPane.setManaged(false);
        userInfoPane.setVisible(false);
    }
}