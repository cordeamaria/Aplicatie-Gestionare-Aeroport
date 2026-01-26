package OCSF.client.controller;

import OCSF.client.networking.AirportClient;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import model.*;
import util.SceneSwitcher;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

public class DispatcherController implements UserAware {
    @FXML private VBox userInfoPane, flightsPane, usersPane, reportPane, crewAssignmentPane;
    @FXML private Label userNameLabel, userRoleLabel;

    // Search Fields
    @FXML private TextField searchFlightField;

    // Flight Table
    @FXML private TableView<Zbor> flightTable;
    @FXML private TableColumn<Zbor, String> colFlightCode, colDep, colDest, colFlightStatus, colEquipment;
    @FXML private TableColumn<Zbor, Integer> colCrewCount, colPassengerCount;
    @FXML private TableColumn<Zbor, LocalDateTime> colDate, colArrivalDate;

    // Flight Inputs
    @FXML private TextField fCodeF, fDepF, fDestF, fEquipmentF, fCrewF, fPaxF;
    @FXML private ComboBox<String> fStatusCombo;
    @FXML private DatePicker datePicker;
    @FXML private DatePicker arrivalDatePicker;


    // Crew Table
    @FXML private TableView<CrewAssignment> crewTable;
    @FXML private TableColumn<CrewAssignment, Long> colCrewUserId, colCrewFlightId;
    @FXML private TableColumn<CrewAssignment, String> colCrewRole;
    @FXML private TextField crewUserIdField, crewFlightIdField, crewRoleField;

    // Users Table
    @FXML private TableView<User> userTable;
    @FXML private TableColumn<User, Integer> colUserId;
    @FXML private TableColumn<User, String> colUserName, colUserRole, colUserPass;
    @FXML private ComboBox<String> roleFilterCombo;

    // Problems
    @FXML private TextArea problemArea;

    private User loggedUser;

    @FXML
    public void initialize() {
        setupColumns();

        fStatusCombo.getItems().addAll("programat", "imbarcare", "decolat", "in_cursa", "aterizat", "anulat");
        roleFilterCombo.getItems().addAll("Pasager", "Muncitor", "Dispecer", "Administrator", "Pilot", "Stewardesa");

        // Flight Selection Listener
        flightTable.getSelectionModel().selectedItemProperty().addListener((obs, oldV, newV) -> {
            if (newV != null) populateFlightFields(newV);
        });



        hideAll();
        userInfoPane.setVisible(true);
    }

    private void setupColumns() {
        // Flight Mappings
        colFlightCode.setCellValueFactory(new PropertyValueFactory<>("cod_zbor"));
        colDep.setCellValueFactory(new PropertyValueFactory<>("plecare_din"));
        colDest.setCellValueFactory(new PropertyValueFactory<>("destinatie"));
        colDate.setCellValueFactory(new PropertyValueFactory<>("data_plecare"));
        colArrivalDate.setCellValueFactory(new PropertyValueFactory<>("data_sosire"));
        colFlightStatus.setCellValueFactory(new PropertyValueFactory<>("status_zbor"));
        colEquipment.setCellValueFactory(new PropertyValueFactory<>("echipamente_sol"));
        colCrewCount.setCellValueFactory(new PropertyValueFactory<>("nr_echipaj_bord"));
        colPassengerCount.setCellValueFactory(new PropertyValueFactory<>("nr_pasageri_estimat"));



        // User Mappings
        colUserId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colUserName.setCellValueFactory(new PropertyValueFactory<>("username"));
        colUserRole.setCellValueFactory(new PropertyValueFactory<>("rol"));
        colUserPass.setCellValueFactory(new PropertyValueFactory<>("parola"));

        // Crew Mappings
        colCrewUserId.setCellValueFactory(new PropertyValueFactory<>("id_user"));
        colCrewFlightId.setCellValueFactory(new PropertyValueFactory<>("id_zbor"));
        colCrewRole.setCellValueFactory(new PropertyValueFactory<>("rol_in_zbor"));
    }

    @Override
    public void setLoggedUser(User user) {
        this.loggedUser = user;
        userNameLabel.setText(user.getUsername());
        userRoleLabel.setText(user.getRol());
    }

    // NAVIGATION
    @FXML public void showFlightsPane() {
        hideAll(); flightsPane.setVisible(true); flightsPane.setManaged(true);
        refreshFlights();
    }



    @FXML public void showUsersPane() {
        hideAll(); usersPane.setVisible(true); usersPane.setManaged(true);
        refreshUsers();
    }

    @FXML public void showCrewPane() {
        hideAll(); crewAssignmentPane.setVisible(true); crewAssignmentPane.setManaged(true);
        refreshCrew();
    }

    @FXML public void showReportPane() { hideAll(); reportPane.setVisible(true); reportPane.setManaged(true); }

    //  FLIGHT OPERATIONS

    @FXML public void handleAddFlight() {
        try {
            // Logic to handle DatePickers + Time
            LocalDateTime depDT = datePicker.getValue() != null ? datePicker.getValue().atTime(12, 0) : LocalDateTime.now();
            LocalDateTime arrDT = arrivalDatePicker.getValue() != null ? arrivalDatePicker.getValue().atTime(14, 0) : depDT.plusHours(2);

            Zbor z = new Zbor(0, fCodeF.getText(), 1L, fDepF.getText(), fDestF.getText(), depDT,
                    arrDT, fStatusCombo.getValue(), fEquipmentF.getText(),
                    Integer.parseInt(fCrewF.getText()), Integer.parseInt(fPaxF.getText()));

            AirportClient.getInstance().sendRequest("ADD_FLIGHT", z);
            refreshFlights();
            clearFlightFields();
        } catch (Exception e) { showAlert("Eroare", "Date invalide!"); }
    }

    @FXML public void handleUpdateFlight() {
        Zbor selected = flightTable.getSelectionModel().getSelectedItem();
        if (selected != null) {
            try {
                selected.setCod_zbor(fCodeF.getText());
                selected.setPlecare_din(fDepF.getText());
                selected.setDestinatie(fDestF.getText());
                selected.setStatus_zbor(fStatusCombo.getValue());
                selected.setEchipamente_sol(fEquipmentF.getText());
                selected.setNr_echipaj_bord(Integer.parseInt(fCrewF.getText()));
                selected.setNr_pasageri_estimat(Integer.parseInt(fPaxF.getText()));

                if(datePicker.getValue() != null)
                    selected.setData_plecare(datePicker.getValue().atTime(12,0));

                AirportClient.getInstance().sendRequest("UPDATE_FLIGHT", selected);
                refreshFlights();
                clearFlightFields();
            } catch(Exception e) { showAlert("Eroare", "Eroare la update."); }
        }
    }

    @FXML public void handleDeleteFlight() {
        Zbor selected = flightTable.getSelectionModel().getSelectedItem();
        if (selected != null) {
            AirportClient.getInstance().sendRequest("DELETE_FLIGHT", selected.getId());
            refreshFlights();
            clearFlightFields();
        }
    }

    @FXML public void handleSearchFlights() {
        List<Zbor> list = (List<Zbor>) AirportClient.getInstance().sendRequest("GET_ALL_FLIGHTS", null);
        if (list != null) {
            String filter = searchFlightField.getText().toLowerCase();
            flightTable.setItems(FXCollections.observableArrayList(list).filtered(z -> z.getCod_zbor().toLowerCase().contains(filter)));
        }
    }

    @FXML public void clearFlightFields() {
        fCodeF.clear(); fDepF.clear(); fDestF.clear(); fEquipmentF.clear(); fCrewF.clear(); fPaxF.clear();
        fStatusCombo.getSelectionModel().clearSelection();
        if(datePicker != null) datePicker.setValue(null);
        if(arrivalDatePicker != null) arrivalDatePicker.setValue(null);
    }



    //  USER FILTER

    @FXML public void handleFilterUsers() {
        List<User> list = (List<User>) AirportClient.getInstance().sendRequest("GET_ALL_USERS", null);
        String role = roleFilterCombo.getValue();

        if (list != null) {
            if (role == null || role.isEmpty()) {
                userTable.setItems(FXCollections.observableArrayList(list));
            } else {
                userTable.setItems(FXCollections.observableArrayList(list)
                        .filtered(u -> u.getRol().equalsIgnoreCase(role)));
            }
        }
    }

    @FXML public void handleAddCrewAssignment() {
        try {
            CrewAssignment ca = new CrewAssignment(0,
                    Long.parseLong(crewUserIdField.getText()),
                    Long.parseLong(crewFlightIdField.getText()),
                    crewRoleField.getText());
            AirportClient.getInstance().sendRequest("ADD_CREW", ca);
            refreshCrew();
        } catch (Exception e) { showAlert("Eroare", "Date invalide!"); }
    }

    @FXML public void handleSubmitProblem() {
        if (!problemArea.getText().isEmpty()) {
            Problema p = new Problema(0, loggedUser.getId().longValue(), problemArea.getText(), "noua", LocalDateTime.now(), null);
            AirportClient.getInstance().sendRequest("ADD_ISSUE", p);
            problemArea.clear();
            showAlert("Succes", "Sesizarea a fost trimisă!");
        }
    }


    private void refreshFlights() {
        List<Zbor> list = (List<Zbor>) AirportClient.getInstance().sendRequest("GET_ALL_FLIGHTS", null);
        if(list != null) flightTable.setItems(FXCollections.observableArrayList(list));
    }



    private void refreshUsers() {
        List<User> list = (List<User>) AirportClient.getInstance().sendRequest("GET_ALL_USERS", null);
        if(list != null) userTable.setItems(FXCollections.observableArrayList(list));
    }

    private void refreshCrew() {
        List<CrewAssignment> list = (List<CrewAssignment>) AirportClient.getInstance().sendRequest("GET_ALL_CREW", null);
        if(list != null) crewTable.setItems(FXCollections.observableArrayList(list));
    }

    private void populateFlightFields(Zbor z) {
        fCodeF.setText(z.getCod_zbor()); fDepF.setText(z.getPlecare_din());
        fDestF.setText(z.getDestinatie()); fStatusCombo.setValue(z.getStatus_zbor());
        fEquipmentF.setText(z.getEchipamente_sol()); fCrewF.setText(String.valueOf(z.getNr_echipaj_bord()));
        fPaxF.setText(String.valueOf(z.getNr_pasageri_estimat()));

        if (z.getData_plecare() != null && datePicker != null) datePicker.setValue(z.getData_plecare().toLocalDate());
        if (z.getData_sosire() != null && arrivalDatePicker != null) arrivalDatePicker.setValue(z.getData_sosire().toLocalDate());
    }


    private void hideAll() {
        flightsPane.setVisible(false); flightsPane.setManaged(false);
        usersPane.setVisible(false); usersPane.setManaged(false);
        reportPane.setVisible(false); reportPane.setManaged(false);
        crewAssignmentPane.setVisible(false); crewAssignmentPane.setManaged(false);
        userInfoPane.setVisible(false);
    }

    private void showAlert(String t, String c) { Alert a = new Alert(Alert.AlertType.INFORMATION); a.setTitle(t); a.setContentText(c); a.show(); }
    public void handleLogout(ActionEvent event) { try { this.loggedUser = null; SceneSwitcher.changeScene(event, "LoginView.fxml", "Autentificare Aeroport", null); } catch(Exception e){} }
}