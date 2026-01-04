package controller;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import model.User;
import model.Zbor;
import repository.UserRepository;
import repository.ZborRepository;
import util.SceneSwitcher;

import java.io.IOException;
import java.time.LocalDateTime;

public class LoginController {

    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;

    // TableView și coloane pentru zboruri
    @FXML private TableView<Zbor> flightTable;
    @FXML private TableColumn<Zbor, String> colCode;
    @FXML private TableColumn<Zbor, String> colDeparture;
    @FXML private TableColumn<Zbor, String> colDestination;
    @FXML private TableColumn<Zbor, LocalDateTime> colDate;
    @FXML private TableColumn<Zbor, String> colStatus;

    private final UserRepository userRepository = new UserRepository();
    private final ZborRepository zborRepository = new ZborRepository();

    @FXML
    public void initialize() {
        // Setăm coloanele tabelului pentru zboruri
        colCode.setCellValueFactory(new PropertyValueFactory<>("cod_zbor"));
        colDeparture.setCellValueFactory(new PropertyValueFactory<>("plecare_din"));
        colDestination.setCellValueFactory(new PropertyValueFactory<>("destinatie"));
        colDate.setCellValueFactory(new PropertyValueFactory<>("data_plecare"));
        colStatus.setCellValueFactory(new PropertyValueFactory<>("status_zbor"));

        // Inițial tabela goală
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

        User validatedUser = userRepository.validateUser(user, pass);

        if (validatedUser != null) {
            // Redirecționare după rol
            if (validatedUser.getRol().equals("PASAGER")) {
                SceneSwitcher.changeScene(event, "PassengerView.fxml", "Meniu Pasager", validatedUser);
            } else if (validatedUser.getRol().equals("MUNCITOR")) {
                SceneSwitcher.changeScene(event, "FieldWorkerView.fxml", "Meniu Muncitor", validatedUser);
            }else if(validatedUser.getRol().equals("DISPECER")) {
                SceneSwitcher.changeScene(event, "DispecerView.fxml", "Meniu Dispecer", validatedUser);

            }
            else if(validatedUser.getRol().equals("ADMINISTRATOR")) {
                SceneSwitcher.changeScene(event, "AdminView.fxml", "Meniu Administrator", validatedUser);

            }
            else {
                showAlert("Eroare", "Rol necunoscut!");
            }
        } else {
            showAlert("Eroare", "Username sau parola incorectă!");
        }
    }

    // ================= VEZI ZBORURI =================
    @FXML
    public void handleViewFlights() {
        // Obținem datele
        ObservableList<Zbor> allFlights = zborRepository.getAllZboruri();
        flightTable.setItems(allFlights);

        // FACEM TABELUL VIZIBIL
        // visible(true) îl arată, managed(true) îi spune VBox-ului să îi rezerve spațiu în fereastră
        flightTable.setVisible(true);
        flightTable.setManaged(true);

        // Opțional: Ajustăm dimensiunea ferestrei pentru a cuprinde tabelul
        if (flightTable.getScene() != null) {
            flightTable.getScene().getWindow().sizeToScene();
        }
    }

    // ================= UTILS =================
    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
