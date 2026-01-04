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

public class DispatcherController implements UserAware {
    // Panouri
    @FXML private VBox userInfoPane, flightsPane, fleetPane, usersPane, reportPane, crewAssignmentPane;
    @FXML private Label userNameLabel, userRoleLabel;

    // Search Fields
    @FXML private TextField searchFlightField, searchAirField;

    // Flight Table & Fields
    @FXML private TableView<Zbor> flightTable;
    @FXML private TableColumn<Zbor, String> colFlightCode, colDep, colDest, colFlightStatus, colEquipment;
    @FXML private TableColumn<Zbor, Integer> colCrewCount, colPassengerCount;
    @FXML private TextField fCodeF, fDepF, fDestF, fEquipmentF, fCrewF, fPaxF;
    @FXML private ComboBox<String> fStatusCombo;
    @FXML private TableColumn<Zbor, LocalDateTime> colDate;
    @FXML private TableColumn<Zbor, LocalDateTime> colArrivalDate;

    // Fleet Table & Fields
    @FXML private TableView<Aeronava> fleetTable;
    @FXML private TableColumn<Aeronava, String> colAirCode, colAirModel, colAirStatus, colAirLoc;
    @FXML private TableColumn<Aeronava, Integer> colAirCap;
    @FXML private TextField airCodeF, airModelF, airCapF, airLocF;
    @FXML private ComboBox<String> airStatusCombo;

    // Crew Table & Fields
    @FXML private TableView<CrewAssignment> crewTable;
    @FXML private TableColumn<CrewAssignment, Long> colCrewUserId, colCrewFlightId;
    @FXML private TableColumn<CrewAssignment, String> colCrewRole;
    @FXML private TextField crewUserIdField, crewFlightIdField, crewRoleField;

    // Users Table
    @FXML private TableView<User> userTable;
    @FXML private TableColumn<User, Long> colUserId;
    @FXML private TableColumn<User, String> colUserName, colUserRole, colUserPass;
    @FXML private ComboBox<String> roleFilterCombo;

    // Problems
    @FXML private TextArea problemArea;

    private final ZborRepository zborRepo = new ZborRepository();
    private final AeronavaRepository airRepo = new AeronavaRepository();
    private final UserRepository userRepo = new UserRepository();
    private final ProblemaRepository probRepo = new ProblemaRepository();
    private final CrewAssignmentRepository crewRepo = new CrewAssignmentRepository();
    private User loggedUser;

    @FXML
    public void initialize() {
        setupColumns();

        fStatusCombo.getItems().addAll("programat", "imbarcare", "decolat", "in_cursa", "aterizat", "anulat");
        airStatusCombo.getItems().addAll("activ", "in_mentenanta", "retras");
        roleFilterCombo.getItems().addAll("Pasager", "Muncitor", "Dispecer", "Administrator", "Pilot", "Stewardesa");

        flightTable.getSelectionModel().selectedItemProperty().addListener((obs, oldV, newV) -> {
            if (newV != null) populateFlightFields(newV);
        });

        fleetTable.getSelectionModel().selectedItemProperty().addListener((obs, oldV, newV) -> {
            if (newV != null) populateAirFields(newV);
        });

        hideAll();
        userInfoPane.setVisible(true);
    }

    private void setupColumns() {
        colFlightCode.setCellValueFactory(new PropertyValueFactory<>("cod_zbor"));
        colDep.setCellValueFactory(new PropertyValueFactory<>("plecare_din"));
        colDest.setCellValueFactory(new PropertyValueFactory<>("destinatie"));
        colDate.setCellValueFactory(new PropertyValueFactory<>("data_plecare"));
        colArrivalDate.setCellValueFactory(new PropertyValueFactory<>("data_sosire"));
        colFlightStatus.setCellValueFactory(new PropertyValueFactory<>("status_zbor"));
        colEquipment.setCellValueFactory(new PropertyValueFactory<>("echipamente_sol"));
        colCrewCount.setCellValueFactory(new PropertyValueFactory<>("nr_echipaj_bord"));
        colPassengerCount.setCellValueFactory(new PropertyValueFactory<>("nr_pasageri_estimat"));

        colAirCode.setCellValueFactory(new PropertyValueFactory<>("cod_aeronava"));
        colAirModel.setCellValueFactory(new PropertyValueFactory<>("model"));
        colAirCap.setCellValueFactory(new PropertyValueFactory<>("capacitate"));
        colAirStatus.setCellValueFactory(new PropertyValueFactory<>("stare_operationala"));
        colAirLoc.setCellValueFactory(new PropertyValueFactory<>("locatie_curenta"));

        colUserId.setCellValueFactory(new PropertyValueFactory<>("id_user"));
        colUserName.setCellValueFactory(new PropertyValueFactory<>("username"));
        colUserRole.setCellValueFactory(new PropertyValueFactory<>("rol"));
        colUserPass.setCellValueFactory(new PropertyValueFactory<>("parola"));

        colCrewUserId.setCellValueFactory(new PropertyValueFactory<>("id_user"));
        colCrewFlightId.setCellValueFactory(new PropertyValueFactory<>("id_zbor"));
        colCrewRole.setCellValueFactory(new PropertyValueFactory<>("rol_in_zbor"));
    }

    @Override
    public void setLoggedUser(User user) {
        this.loggedUser = user;
        userNameLabel.setText( user.getUsername());
        userRoleLabel.setText(user.getRol());
    }

    // ================= ASIGNARE PERSONAL (Crew) =================
    @FXML public void handleAddCrewAssignment() {
        try {
            CrewAssignment ca = new CrewAssignment(0L,
                    Long.parseLong(crewUserIdField.getText()),
                    Long.parseLong(crewFlightIdField.getText()),
                    crewRoleField.getText());
            crewRepo.addAssignment(ca);
            crewTable.setItems(crewRepo.getAllAssignments());
            showAlert("Succes", "Personal asignat cu succes!");
        } catch (Exception e) { showAlert("Eroare", "Date invalide!"); }
    }

    // ================= CRUD ZBOR / AERONAVA / SEARCH râmân neschimbate... =================
    @FXML public void handleSearchFlights() {
        String filter = searchFlightField.getText().toLowerCase();
        flightTable.setItems(zborRepo.getAllZboruri().filtered(z -> z.getCod_zbor().toLowerCase().contains(filter)));
    }

    @FXML public void handleAddFlight() {
        if (zborRepo.findByCod(fCodeF.getText()) != null) {
            showAlert("Eroare", "Există deja un zbor cu acest cod!");
            return;
        }
        try {
            Zbor z = new Zbor(0L, fCodeF.getText(), 1L, fDepF.getText(), fDestF.getText(), LocalDateTime.now(),
                    LocalDateTime.now().plusHours(2), fStatusCombo.getValue(), fEquipmentF.getText(),
                    Integer.parseInt(fCrewF.getText()), Integer.parseInt(fPaxF.getText()));
            zborRepo.add(z);
        } catch (Exception e) { showAlert("Eroare", "Date invalide!"); }
    }

    @FXML public void handleUpdateFlight() {
        Zbor selected = zborRepo.findByCod(fCodeF.getText());
        if (selected != null) {
            selected.setPlecare_din(fDepF.getText());
            selected.setDestinatie(fDestF.getText());
            selected.setStatus_zbor(fStatusCombo.getValue());
            selected.setEchipamente_sol(fEquipmentF.getText());
            selected.setNr_echipaj_bord(Integer.parseInt(fCrewF.getText()));
            selected.setNr_pasageri_estimat(Integer.parseInt(fPaxF.getText()));
            zborRepo.update(selected);
            flightTable.refresh();
        }
    }

    @FXML public void handleFilterUsers() {
        userTable.setItems(userRepo.getUsersByRole(roleFilterCombo.getValue()));
    }

    @FXML public void handleSubmitProblem() {
        if (!problemArea.getText().isEmpty()) {
            probRepo.add(new Problema(0L, loggedUser.getId_user(), problemArea.getText(), "noua", LocalDateTime.now(), null));
            problemArea.clear();
            showAlert("Succes", "Sesizarea a fost trimisă!");
        }
    }

    // ================= NAVIGARE =================
    @FXML public void showFlightsPane() { hideAll(); flightsPane.setVisible(true); flightsPane.setManaged(true); flightTable.setItems(zborRepo.getAllZboruri()); }
    @FXML public void showFleetPane() { hideAll(); fleetPane.setVisible(true); fleetPane.setManaged(true); fleetTable.setItems(airRepo.getAllAeronave()); }
    @FXML public void showUsersPane() { hideAll(); usersPane.setVisible(true); usersPane.setManaged(true); userTable.setItems(userRepo.getAllUsers()); }
    @FXML public void showCrewPane() { hideAll(); crewAssignmentPane.setVisible(true); crewAssignmentPane.setManaged(true); crewTable.setItems(crewRepo.getAllAssignments()); }
    @FXML public void showReportPane() { hideAll(); reportPane.setVisible(true); reportPane.setManaged(true); }

    private void populateFlightFields(Zbor z) {
        fCodeF.setText(z.getCod_zbor()); fDepF.setText(z.getPlecare_din());
        fDestF.setText(z.getDestinatie()); fStatusCombo.setValue(z.getStatus_zbor());
        fEquipmentF.setText(z.getEchipamente_sol()); fCrewF.setText(String.valueOf(z.getNr_echipaj_bord()));
        fPaxF.setText(String.valueOf(z.getNr_pasageri_estimat()));
    }

    private void populateAirFields(Aeronava a) {
        airCodeF.setText(a.getCod_aeronava()); airModelF.setText(a.getModel());
        airCapF.setText(String.valueOf(a.getCapacitate())); airStatusCombo.setValue(a.getStare_operationala());
        airLocF.setText(a.getLocatie_curenta());
    }

    private void hideAll() {
        flightsPane.setVisible(false); flightsPane.setManaged(false);
        fleetPane.setVisible(false); fleetPane.setManaged(false);
        usersPane.setVisible(false); usersPane.setManaged(false);
        reportPane.setVisible(false); reportPane.setManaged(false);
        crewAssignmentPane.setVisible(false); crewAssignmentPane.setManaged(false);
        userInfoPane.setVisible(false);
    }
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

    private void showAlert(String t, String c) { Alert a = new Alert(Alert.AlertType.INFORMATION); a.setTitle(t); a.setContentText(c); a.show(); }
    @FXML public void clearFlightFields() { fCodeF.clear(); fDepF.clear(); fDestF.clear(); fEquipmentF.clear(); fCrewF.clear(); fPaxF.clear(); }
    @FXML public void handleSearchAir() { String filter = searchAirField.getText().toLowerCase(); fleetTable.setItems(airRepo.getAllAeronave().filtered(a -> a.getCod_aeronava().toLowerCase().contains(filter))); }
    @FXML public void handleAddAircraft() { /* implementare similară */ }
    @FXML public void handleUpdateAircraft() { /* implementare similară */ }
    @FXML public void handleDeleteAircraft() { /* implementare similară */ }
    @FXML public void handleDeleteFlight() { Zbor selected = flightTable.getSelectionModel().getSelectedItem(); if (selected != null) zborRepo.delete(selected); }
}