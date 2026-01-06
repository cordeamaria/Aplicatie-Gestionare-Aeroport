package OCSF.client.controller;

import OCSF.client.networking.AirportClient;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import model.User;
import model.Zbor;
import util.SceneSwitcher;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

public class LoginController {

    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;

    // Flight Table
    @FXML private TableView<Zbor> flightTable;
    @FXML private TableColumn<Zbor, String> colCode;
    @FXML private TableColumn<Zbor, String> colDeparture;
    @FXML private TableColumn<Zbor, String> colDestination;
    @FXML private TableColumn<Zbor, LocalDateTime> colDate;
    @FXML private TableColumn<Zbor, String> colStatus;

    @FXML
    public void initialize() {
        // Setup Columns
        colCode.setCellValueFactory(new PropertyValueFactory<>("cod_zbor"));
        colDeparture.setCellValueFactory(new PropertyValueFactory<>("plecare_din"));
        colDestination.setCellValueFactory(new PropertyValueFactory<>("destinatie"));
        colDate.setCellValueFactory(new PropertyValueFactory<>("data_plecare"));
        colStatus.setCellValueFactory(new PropertyValueFactory<>("status_zbor"));

        flightTable.setItems(null);
    }

    // ================= LOGARE =================
    @FXML
    public void handleLogin(ActionEvent event) throws IOException {
        String user = usernameField.getText().trim();
        String pass = passwordField.getText().trim();

        if(user.isEmpty() || pass.isEmpty()) {
            showAlert("Info", "Completează username și parola pentru logare!");
            return;
        }

        // REQUEST: LOGIN
        // We create a temporary User object just to carry the credentials
        User credentials = new User(0, user, pass, null);

        // SEND TO SERVER
        User validatedUser = (User) AirportClient.getInstance().sendRequest("LOGIN", credentials);

        if (validatedUser != null) {
            String role = validatedUser.getRol().toUpperCase();

            switch (role) {
                case "PASAGER":
                    SceneSwitcher.changeScene(event, "PassengerView.fxml", "Meniu Pasager", validatedUser);
                    break;
                case "MUNCITOR":
                    SceneSwitcher.changeScene(event, "FieldWorkerView.fxml", "Meniu Muncitor", validatedUser);
                    break;
                case "DISPECER":
                    SceneSwitcher.changeScene(event, "DispecerView.fxml", "Meniu Dispecer", validatedUser);
                    break;
                case "ADMINISTRATOR":
                    SceneSwitcher.changeScene(event, "AdminView.fxml", "Meniu Administrator", validatedUser);
                    break;
                // Add Pilot/Stewardess if you have views for them
                default:
                    showAlert("Eroare", "Rol necunoscut sau neimplementat: " + role);
            }
        } else {
            showAlert("Eroare", "Username sau parola incorectă!");
        }
    }

    // ================= VEZI ZBORURI =================
    @FXML
    public void handleViewFlights() {
        // REQUEST: GET_ALL_FLIGHTS
        List<Zbor> flightList = (List<Zbor>) AirportClient.getInstance().sendRequest("GET_ALL_FLIGHTS", null);

        if (flightList != null) {
            flightTable.setItems(FXCollections.observableArrayList(flightList));
            flightTable.setVisible(true);
            flightTable.setManaged(true);

            if (flightTable.getScene() != null) {
                flightTable.getScene().getWindow().sizeToScene();
            }
        } else {
            showAlert("Eroare", "Nu s-a putut conecta la server.");
        }
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
