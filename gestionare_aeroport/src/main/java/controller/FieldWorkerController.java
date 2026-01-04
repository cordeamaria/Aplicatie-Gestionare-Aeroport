package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import model.*;
import repository.*;
import util.SceneSwitcher;

import java.time.LocalDateTime;

public class FieldWorkerController implements UserAware {
    @FXML private VBox userInfoPane, tasksPane, allFlightsPane, fleetPane, issuesPane;
    @FXML private Label userNameLabel, userRoleLabel;

    // Search Fields conform diagramei Use Case
    @FXML private TextField searchIssueField;
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
        // Mapare Task-uri (Vizualizare taskurile personale)
        colTaskDesc.setCellValueFactory(new PropertyValueFactory<>("descriere"));
        colTaskStatus.setCellValueFactory(new PropertyValueFactory<>("status"));
        colTaskAllocated.setCellValueFactory(new PropertyValueFactory<>("data_alocare"));
        colTaskFinished.setCellValueFactory(new PropertyValueFactory<>("data_finalizare"));

        // Mapare Zboruri (Vizualizare tehnica)
        colFlightCode.setCellValueFactory(new PropertyValueFactory<>("cod_zbor"));
        colDeparture.setCellValueFactory(new PropertyValueFactory<>("plecare_din"));
        colDestination.setCellValueFactory(new PropertyValueFactory<>("destinatie"));
        colDepartureTime.setCellValueFactory(new PropertyValueFactory<>("data_plecare"));
        colArrivalTime.setCellValueFactory(new PropertyValueFactory<>("data_sosire"));
        colFlightStatus.setCellValueFactory(new PropertyValueFactory<>("status_zbor"));
        colEquipment.setCellValueFactory(new PropertyValueFactory<>("echipamente_sol"));
        colCrewCount.setCellValueFactory(new PropertyValueFactory<>("nr_echipaj_bord"));
        colPassengerCount.setCellValueFactory(new PropertyValueFactory<>("nr_pasageri_estimat"));

        // Mapare Aeronave (Vizualizare aeronave)
        colAirCode.setCellValueFactory(new PropertyValueFactory<>("cod_aeronava"));
        colAirModel.setCellValueFactory(new PropertyValueFactory<>("model"));
        colAirCapacity.setCellValueFactory(new PropertyValueFactory<>("capacitate"));
        colAirOpStatus.setCellValueFactory(new PropertyValueFactory<>("stare_operationala"));
        colAirLocation.setCellValueFactory(new PropertyValueFactory<>("locatie_curenta"));

        // Mapare Probleme (Vizualizare probleme)
        colIssueDesc.setCellValueFactory(new PropertyValueFactory<>("descriere"));
        colIssueStatus.setCellValueFactory(new PropertyValueFactory<>("status"));
        colIssueReported.setCellValueFactory(new PropertyValueFactory<>("data_raportare"));
        colIssueResolved.setCellValueFactory(new PropertyValueFactory<>("data_rezolvare"));
    }

    // ================= FUNCTII DE CAUTARE =================

    @FXML
    public void handleSearchIssues() {
        String filter = searchIssueField.getText().toLowerCase();
        // Căutare probleme conform diagramei
        issuesTable.setItems(problemaRepository.getAllProbleme().filtered(p ->
                p.getDescriere().toLowerCase().contains(filter)
        ));
    }

    @FXML
    public void handleSearchAir() {
        String filter = searchAirField.getText().toLowerCase();
        // Căutare aeronave conform diagramei
        fleetTable.setItems(aeronavaRepository.getAllAeronave().filtered(a ->
                a.getCod_aeronava().toLowerCase().contains(filter)
        ));
    }

    // ================= LOGICA NAVIGARE =================

    @Override
    public void setLoggedUser(User user) {
        this.loggedUser = user;
        userNameLabel.setText(user.getUsername());
        userRoleLabel.setText( user.getRol());
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
        // Modificare status problema conform diagramei
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
    private void showAlert(String t, String c) { Alert a = new Alert(Alert.AlertType.INFORMATION); a.setTitle(t); a.setContentText(c); a.show(); }

    public void handleLogout(ActionEvent event) {
        try {
            // Opțional: Aici poți șterge datele utilizatorului logat dacă ai un manager de sesiune
            this.loggedUser = null;

            // Redirecționare către pagina de login
            SceneSwitcher.changeScene(event, "LoginView.fxml", "Autentificare Aeroport", null);

        } catch(Exception e){
            e.printStackTrace();
            showAlert("Eroare", "Nu s-a putut efectua deconectarea!");
        }
    }
}