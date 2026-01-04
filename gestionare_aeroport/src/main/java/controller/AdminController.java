package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import model.*;
import repository.*;
import util.SceneSwitcher;

public class AdminController implements UserAware {
    @FXML private VBox userPane, fleetPane, statsPane, userInfoPane;
    @FXML private Label userNameLabel, userRoleLabel;

    // Personal
    @FXML private TableView<User> userTable;
    @FXML private TableColumn<User, Long> colUserId;
    @FXML private TableColumn<User, String> colUser, colPass, colRole;
    @FXML private TextField usernameField, passwordField;
    @FXML private ComboBox<String> roleCombo;

    // Flota
    @FXML private TableView<Aeronava> fleetTable;
    @FXML private TableColumn<Aeronava, String> colAirCode, colAirModel, colAirStatus, colAirLoc;
    @FXML private TableColumn<Aeronava, Integer> colAirCap;
    @FXML private TextField airCodeF, airModelF, airCapF, airLocF, searchAirField;
    @FXML private ComboBox<String> airStatusCombo;

    private final UserRepository userRepo = new UserRepository();
    private final AeronavaRepository airRepo = new AeronavaRepository();
    private User loggedAdmin;

    @FXML
    public void initialize() {
        setupTableColumns();

        // Populare ComboBox-uri (Verificate să existe în FXML)
        if (roleCombo != null) roleCombo.getItems().addAll("Pasager", "Muncitor", "Dispecer", "Administrator", "Pilot", "Stewardesa");
        if (airStatusCombo != null) airStatusCombo.getItems().addAll("activ", "in_mentenanta", "retras");

        userTable.getSelectionModel().selectedItemProperty().addListener((obs, oldV, newV) -> {
            if (newV != null) {
                usernameField.setText(newV.getUsername());
                passwordField.setText(newV.getParola());
                roleCombo.setValue(newV.getRol());
            }
        });

        hideAll();
        userInfoPane.setVisible(true);
    }

    private void setupTableColumns() {
        colUserId.setCellValueFactory(new PropertyValueFactory<>("id_user"));
        colUser.setCellValueFactory(new PropertyValueFactory<>("username"));
        colPass.setCellValueFactory(new PropertyValueFactory<>("parola"));
        colRole.setCellValueFactory(new PropertyValueFactory<>("rol"));

        colAirCode.setCellValueFactory(new PropertyValueFactory<>("cod_aeronava"));
        colAirModel.setCellValueFactory(new PropertyValueFactory<>("model"));
        colAirCap.setCellValueFactory(new PropertyValueFactory<>("capacitate"));
        colAirStatus.setCellValueFactory(new PropertyValueFactory<>("stare_operationala"));
        colAirLoc.setCellValueFactory(new PropertyValueFactory<>("locatie_curenta"));
    }

    @Override
    public void setLoggedUser(User user) {
        this.loggedAdmin = user;
        userNameLabel.setText(user.getUsername());
        userRoleLabel.setText(user.getRol());
    }

    // --- NAVIGARE ---
    @FXML public void showUserManagement() { hideAll(); userPane.setVisible(true); userPane.setManaged(true); userTable.setItems(userRepo.getAllUsers()); }
    @FXML public void showFleetManagement() { hideAll(); fleetPane.setVisible(true); fleetPane.setManaged(true); fleetTable.setItems(airRepo.getAllAeronave()); }
    @FXML public void showStatistics() { hideAll(); statsPane.setVisible(true); statsPane.setManaged(true); }

    // --- CRUD PERSONAL ---
    @FXML public void handleAddUser() {
        User u = new User(0L, usernameField.getText(), passwordField.getText(), roleCombo.getValue());
        userRepo.addUser(u);
    }

    @FXML public void handleUpdateUser() {
        User selected = userTable.getSelectionModel().getSelectedItem();
        if (selected != null) {
            selected.setUsername(usernameField.getText());
            selected.setParola(passwordField.getText());
            selected.setRol(roleCombo.getValue());
            userRepo.updateUser(selected);
            userTable.refresh();
        }
    }

    @FXML public void handleDeleteUser() {
        User selected = userTable.getSelectionModel().getSelectedItem();
        if (selected != null) userRepo.deleteUser(selected);
    }

    // --- CRUD FLOTĂ ---
    @FXML public void handleAddAircraft() {
        try {
            Aeronava a = new Aeronava(0L, airCodeF.getText(), airModelF.getText(), Integer.parseInt(airCapF.getText()), airStatusCombo.getValue(), airLocF.getText());
            airRepo.add(a);
        } catch (Exception e) { System.out.println("Eroare date numerice"); }
    }

    @FXML public void handleUpdateAircraft() {
        Aeronava selected = fleetTable.getSelectionModel().getSelectedItem();
        if (selected != null) {
            selected.setCod_aeronava(airCodeF.getText());
            selected.setModel(airModelF.getText());
            selected.setCapacitate(Integer.parseInt(airCapF.getText()));
            selected.setStare_operationala(airStatusCombo.getValue());
            selected.setLocatie_curenta(airLocF.getText());
            airRepo.update(selected);
            fleetTable.refresh();
        }
    }

    @FXML public void handleDeleteAircraft() {
        Aeronava selected = fleetTable.getSelectionModel().getSelectedItem();
        if (selected != null) airRepo.delete(selected);
    }

    @FXML public void handleSearchAir() {
        String filter = searchAirField.getText().toLowerCase();
        fleetTable.setItems(airRepo.getAllAeronave().filtered(a -> a.getCod_aeronava().toLowerCase().contains(filter)));
    }

    private void hideAll() {
        userPane.setVisible(false); userPane.setManaged(false);
        fleetPane.setVisible(false); fleetPane.setManaged(false);
        statsPane.setVisible(false); statsPane.setManaged(false);
        userInfoPane.setVisible(false);
    }
    private void showAlert(String t, String c) { Alert a = new Alert(Alert.AlertType.INFORMATION); a.setTitle(t); a.setContentText(c); a.show(); }

    public void handleLogout(ActionEvent event) {
        try {
            // Opțional: Aici poți șterge datele utilizatorului logat dacă ai un manager de sesiune
            this.loggedAdmin = null;

            // Redirecționare către pagina de login
            SceneSwitcher.changeScene(event, "LoginView.fxml", "Autentificare Aeroport", null);

        } catch(Exception e){
            e.printStackTrace();
            showAlert("Eroare", "Nu s-a putut efectua deconectarea!");
        }
    }
}