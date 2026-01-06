package OCSF.client.controller;

import OCSF.client.networking.AirportClient;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import model.*;
import util.SceneSwitcher;
import java.time.LocalDateTime;
import java.util.List;

public class FieldWorkerController implements UserAware {
    @FXML private VBox userInfoPane, tasksPane, allFlightsPane, fleetPane, issuesPane;
    @FXML private Label userNameLabel, userRoleLabel;

    @FXML private TextField searchIssueField, searchAirField;

    // Tasks Table
    @FXML private TableView<Task> tasksTable;
    @FXML private TableColumn<Task, String> colTaskDesc, colTaskStatus;
    @FXML private TableColumn<Task, LocalDateTime> colTaskAllocated, colTaskFinished;

    // Flights Table
    @FXML private TableView<Zbor> technicalFlightTable;
    @FXML private TableColumn<Zbor, String> colFlightCode, colDeparture, colDestination, colFlightStatus, colEquipment;
    @FXML private TableColumn<Zbor, LocalDateTime> colDepartureTime, colArrivalTime;
    @FXML private TableColumn<Zbor, Integer> colCrewCount, colPassengerCount;

    // Fleet Table
    @FXML private TableView<Aeronava> fleetTable;
    @FXML private TableColumn<Aeronava, String> colAirCode, colAirModel, colAirOpStatus, colAirLocation;
    @FXML private TableColumn<Aeronava, Integer> colAirCapacity;

    // Issues Table
    @FXML private TableView<Problema> issuesTable;
    @FXML private TableColumn<Problema, String> colIssueDesc, colIssueStatus;
    @FXML private TableColumn<Problema, LocalDateTime> colIssueReported, colIssueResolved;
    @FXML private ComboBox<String> statusUpdateCombo;

    private User loggedUser;

    @FXML
    public void initialize() {
        setupTableColumns();
        statusUpdateCombo.getItems().addAll("noua", "in_analiza", "rezolvata", "inchisa");
        hideAllPanels();
        userInfoPane.setVisible(true);
    }

    private void setupTableColumns() {
        // Task Mappings
        colTaskDesc.setCellValueFactory(new PropertyValueFactory<>("descriere"));
        colTaskStatus.setCellValueFactory(new PropertyValueFactory<>("status"));
        colTaskAllocated.setCellValueFactory(new PropertyValueFactory<>("data_alocare"));
        colTaskFinished.setCellValueFactory(new PropertyValueFactory<>("data_finalizare"));

        // Flight Mappings
        colFlightCode.setCellValueFactory(new PropertyValueFactory<>("cod_zbor"));
        colDeparture.setCellValueFactory(new PropertyValueFactory<>("plecare_din"));
        colDestination.setCellValueFactory(new PropertyValueFactory<>("destinatie"));
        colDepartureTime.setCellValueFactory(new PropertyValueFactory<>("data_plecare"));
        colArrivalTime.setCellValueFactory(new PropertyValueFactory<>("data_sosire"));
        colFlightStatus.setCellValueFactory(new PropertyValueFactory<>("status_zbor"));
        colEquipment.setCellValueFactory(new PropertyValueFactory<>("echipamente_sol"));
        colCrewCount.setCellValueFactory(new PropertyValueFactory<>("nr_echipaj_bord"));
        colPassengerCount.setCellValueFactory(new PropertyValueFactory<>("nr_pasageri_estimat"));

        // Fleet Mappings
        colAirCode.setCellValueFactory(new PropertyValueFactory<>("cod_aeronava"));
        colAirModel.setCellValueFactory(new PropertyValueFactory<>("model"));
        colAirCapacity.setCellValueFactory(new PropertyValueFactory<>("capacitate"));
        colAirOpStatus.setCellValueFactory(new PropertyValueFactory<>("stare_operationala"));
        colAirLocation.setCellValueFactory(new PropertyValueFactory<>("locatie_curenta"));

        // Issue Mappings
        colIssueDesc.setCellValueFactory(new PropertyValueFactory<>("descriere"));
        colIssueStatus.setCellValueFactory(new PropertyValueFactory<>("status"));
        colIssueReported.setCellValueFactory(new PropertyValueFactory<>("data_raportare"));
        colIssueResolved.setCellValueFactory(new PropertyValueFactory<>("data_rezolvare"));
    }

    @Override
    public void setLoggedUser(User user) {
        this.loggedUser = user;
        userNameLabel.setText(user.getUsername());
        userRoleLabel.setText(user.getRol());
    }

    // --- NAVIGATION ---
    @FXML public void showTasksPane() {
        hideAllPanels(); tasksPane.setVisible(true); tasksPane.setManaged(true);
        // Request tasks by User ID
        List<Task> list = (List<Task>) AirportClient.getInstance().sendRequest("GET_MY_TASKS", loggedUser.getId());
        if(list != null) tasksTable.setItems(FXCollections.observableArrayList(list));
    }

    @FXML public void showAllFlightsPane() {
        hideAllPanels(); allFlightsPane.setVisible(true); allFlightsPane.setManaged(true);
        List<Zbor> list = (List<Zbor>) AirportClient.getInstance().sendRequest("GET_ALL_FLIGHTS", null);
        if(list != null) technicalFlightTable.setItems(FXCollections.observableArrayList(list));
    }

    @FXML public void showFleetPane() {
        hideAllPanels(); fleetPane.setVisible(true); fleetPane.setManaged(true);
        List<Aeronava> list = (List<Aeronava>) AirportClient.getInstance().sendRequest("GET_ALL_PLANES", null);
        if(list != null) fleetTable.setItems(FXCollections.observableArrayList(list));
    }

    @FXML public void showIssuesPane() {
        hideAllPanels(); issuesPane.setVisible(true); issuesPane.setManaged(true);
        List<Problema> list = (List<Problema>) AirportClient.getInstance().sendRequest("GET_ALL_ISSUES", null);
        if(list != null) issuesTable.setItems(FXCollections.observableArrayList(list));
    }

    // --- ACTIONS ---
    @FXML
    public void handleUpdateIssueStatus() {
        Problema selected = issuesTable.getSelectionModel().getSelectedItem();
        String newStatus = statusUpdateCombo.getValue();
        if (selected == null || newStatus == null) return;

        selected.setStatus(newStatus);
        if (newStatus.equals("rezolvata") || newStatus.equals("inchisa")) {
            selected.setData_rezolvare(LocalDateTime.now());
        }

        AirportClient.getInstance().sendRequest("UPDATE_ISSUE", selected);
        issuesTable.refresh();
    }

    // Search Handlers
    @FXML public void handleSearchIssues() {
        List<Problema> list = (List<Problema>) AirportClient.getInstance().sendRequest("GET_ALL_ISSUES", null);
        if (list == null) return;
        String filter = searchIssueField.getText().toLowerCase();
        issuesTable.setItems(FXCollections.observableArrayList(list).filtered(p -> p.getDescriere().toLowerCase().contains(filter)));
    }

    @FXML public void handleSearchAir() {
        List<Aeronava> list = (List<Aeronava>) AirportClient.getInstance().sendRequest("GET_ALL_PLANES", null);
        if (list == null) return;
        String filter = searchAirField.getText().toLowerCase();
        fleetTable.setItems(FXCollections.observableArrayList(list).filtered(a -> a.getCod_aeronava().toLowerCase().contains(filter)));
    }

    private void hideAllPanels() {
        tasksPane.setVisible(false); tasksPane.setManaged(false);
        allFlightsPane.setVisible(false); allFlightsPane.setManaged(false);
        fleetPane.setVisible(false); fleetPane.setManaged(false);
        issuesPane.setVisible(false); issuesPane.setManaged(false);
        userInfoPane.setVisible(false);
    }

    public void handleLogout(ActionEvent event) { try { this.loggedUser = null; SceneSwitcher.changeScene(event, "LoginView.fxml", "Autentificare Aeroport", null); } catch(Exception e){} }
}