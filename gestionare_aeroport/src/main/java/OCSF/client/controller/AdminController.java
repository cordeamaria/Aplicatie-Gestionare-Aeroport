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
import java.util.List;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;

public class AdminController implements UserAware {
    @FXML private VBox userPane, fleetPane, statsPane, userInfoPane;
    @FXML private Label userNameLabel, userRoleLabel;

    // Personal
    @FXML private TableView<User> userTable;
    @FXML private TableColumn<User, Integer> colUserId; // Changed Long to Integer
    @FXML private TableColumn<User, String> colUser, colPass, colRole;
    @FXML private TextField usernameField, passwordField;
    @FXML private ComboBox<String> roleCombo;

    // Flota
    @FXML private TableView<Aeronava> fleetTable;
    @FXML private TableColumn<Aeronava, String> colAirCode, colAirModel, colAirStatus, colAirLoc;
    @FXML private TableColumn<Aeronava, Integer> colAirCap;
    @FXML private TextField airCodeF, airModelF, airCapF, airLocF, searchAirField;
    @FXML private ComboBox<String> airStatusCombo;

    @FXML private DatePicker reportDatePicker;


    private User loggedAdmin;

    @FXML
    public void initialize() {
        setupTableColumns();
        if (roleCombo != null) roleCombo.getItems().addAll("Pasager", "Muncitor", "Dispecer", "Administrator", "Pilot", "Stewardesa");
        if (airStatusCombo != null) airStatusCombo.getItems().addAll("activ", "in_mentenanta", "retras");

        hideAll();
        userInfoPane.setVisible(true);
    }

    private void setupTableColumns() {
        colUserId.setCellValueFactory(new PropertyValueFactory<>("id"));
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


    @FXML public void showUserManagement() {
        hideAll(); userPane.setVisible(true); userPane.setManaged(true);
        List<User> users = (List<User>) AirportClient.getInstance().sendRequest("GET_ALL_USERS", null);
        if (users != null) userTable.setItems(FXCollections.observableArrayList(users));
    }

    @FXML public void showFleetManagement() {
        hideAll(); fleetPane.setVisible(true); fleetPane.setManaged(true);
        // FIX: Request data from Server
        List<Aeronava> planes = (List<Aeronava>) AirportClient.getInstance().sendRequest("GET_ALL_PLANES", null);
        if (planes != null) fleetTable.setItems(FXCollections.observableArrayList(planes));
    }


    // CRUD PERSONAL
    @FXML public void handleAddUser() {
        User u = new User(0, usernameField.getText(), passwordField.getText(), roleCombo.getValue());
        AirportClient.getInstance().sendRequest("ADD_USER", u);
        showUserManagement(); // Refresh table
    }


    @FXML
    public void handleUpdateUser() {
        User selected = userTable.getSelectionModel().getSelectedItem();

        if (selected != null) {
            // 1. UPDATE USERNAME (Only if field is not empty)
            if (!usernameField.getText().isEmpty()) {
                selected.setUsername(usernameField.getText());
            }

            // 2. UPDATE PASSWORD (Only if field is not empty)
            if (!passwordField.getText().isEmpty()) {
                selected.setParola(passwordField.getText());
            }

            // 3. UPDATE ROLE (Only if a new value is selected)
            if (roleCombo.getValue() != null) {
                selected.setRol(roleCombo.getValue());
            }

            // Send the request
            AirportClient.getInstance().sendRequest("UPDATE_USER", selected);

            // Refresh table and clear inputs
            showUserManagement();
            usernameField.clear();
            passwordField.clear();
            roleCombo.setValue(null);
        } else {
            showAlert("Eroare", "Selectați un utilizator din tabel!");
        }
    }

    @FXML public void handleDeleteUser() {
        User selected = userTable.getSelectionModel().getSelectedItem();
        if (selected != null) {
            AirportClient.getInstance().sendRequest("DELETE_USER", selected.getId());
            showUserManagement();
        }
    }

    // CRUD FLOTĂ
    @FXML public void handleAddAircraft() {
        try {
            Aeronava a = new Aeronava(0, airCodeF.getText(), airModelF.getText(), Integer.parseInt(airCapF.getText()), airStatusCombo.getValue(), airLocF.getText());
            AirportClient.getInstance().sendRequest("ADD_PLANE", a);
            showFleetManagement();
        } catch (Exception e) { System.out.println("Eroare date numerice"); }
    }


    private void hideAll() {
        userPane.setVisible(false); userPane.setManaged(false);
        fleetPane.setVisible(false); fleetPane.setManaged(false);
        statsPane.setVisible(false); statsPane.setManaged(false);
        userInfoPane.setVisible(false);
    }


    @FXML
    public void handleUpdateAircraft() {
        Aeronava selected = fleetTable.getSelectionModel().getSelectedItem();

        if (selected != null) {
            try {
                // Update the object with data from text fields
                if(!airCodeF.getText().isEmpty()){
                selected.setCod_aeronava(airCodeF.getText());
                }
                if(!airModelF.getText().isEmpty()){
                selected.setModel(airModelF.getText());
                }
                // Parse integer safely
                if(!airCapF.getText().isEmpty()){
                selected.setCapacitate(Integer.parseInt(airCapF.getText()));
                }
                if(airStatusCombo.getValue() != null){
                selected.setStare_operationala(airStatusCombo.getValue());
                }
                if(!airLocF.getText().isEmpty()){
                selected.setLocatie_curenta(airLocF.getText());
                }

                // SEND REQUEST TO SERVER
                AirportClient.getInstance().sendRequest("UPDATE_PLANE", selected);

                // Refresh table
                showFleetManagement();

            } catch (NumberFormatException e) {
                showAlert("Eroare", "Capacitatea trebuie să fie un număr valid!");
            } catch (Exception e) {
                e.printStackTrace();
                showAlert("Eroare", "Nu s-a putut actualiza aeronava.");
            }
        } else {
            showAlert("Atenție", "Selectați o aeronavă din tabel pentru a o modifica.");
        }
    }

    @FXML
    public void handleDeleteAircraft() {
        Aeronava selected = fleetTable.getSelectionModel().getSelectedItem();

        if (selected != null) {
            // SEND REQUEST TO SERVER (Send ID)
            AirportClient.getInstance().sendRequest("DELETE_PLANE", selected.getId());

            // Refresh table
            showFleetManagement();
        } else {
            showAlert("Atenție", "Selectați o aeronavă din tabel pentru a o șterge.");
        }
    }

    // --- UTILS & LOGOUT ---

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    @FXML
    public void handleLogout(ActionEvent event) {
        try {
            this.loggedAdmin = null;
            SceneSwitcher.changeScene(event, "LoginView.fxml", "Autentificare Aeroport", null);
        } catch(Exception e) {
            e.printStackTrace();
            showAlert("Eroare", "Nu s-a putut efectua deconectarea!");
        }
    }

    @FXML
    public void handleSearchAir() {
        String filter = searchAirField.getText().toLowerCase();


        List<Aeronava> allPlanes = (List<Aeronava>) AirportClient.getInstance().sendRequest("GET_ALL_PLANES", null);

        if (allPlanes != null) {
            ObservableList<Aeronava> filteredList = FXCollections.observableArrayList(allPlanes)
                    .filtered(a -> a.getCod_aeronava().toLowerCase().contains(filter));

            fleetTable.setItems(filteredList);
        }
    }

    @FXML
    public void showStatistics() {
        hideAll();
        statsPane.setVisible(true);
        statsPane.setManaged(true);

        // === ADD THIS ===
        if (reportDatePicker != null && reportDatePicker.getValue() == null) {
            reportDatePicker.setValue(java.time.LocalDate.now());
        }
    }

    // STATISTICS & PDF GENERATION

    @FXML
    public void handleGeneratePDF() {
        if (reportDatePicker == null || reportDatePicker.getValue() == null) {
            showAlert("Eroare", "Vă rugăm să selectați o dată pentru raport!");
            return;
        }

        java.time.LocalDate date = reportDatePicker.getValue();

        // 1. Request statistics from Server (Returns a Map with "passengers" and "revenue")
        java.util.Map<String, Double> stats = (java.util.Map<String, Double>) AirportClient.getInstance().sendRequest("GET_DAILY_STATS", date);

        if (stats != null) {
            int passengers = stats.get("passengers").intValue();
            double revenue = stats.get("revenue");

            // 2. Generate the PDF
            createPdfReport(date, passengers, revenue);
        } else {
            showAlert("Eroare", "Nu s-au putut prelua datele de la server.");
        }
    }

    private void createPdfReport(java.time.LocalDate date, int passengers, double revenue) {
        String dest = "Raport_Aeroport_" + date.toString() + ".pdf";

        try {
            // Initialize PDF writer
            PdfWriter writer = new PdfWriter(dest);
            PdfDocument pdf = new PdfDocument(writer);
            Document document = new Document(pdf);

            // Add Content
            document.add(new Paragraph("RAPORT ZILNIC DE ACTIVITATE").setBold().setFontSize(18));
            document.add(new Paragraph("Generat de: ADMINISTRATOR"));
            document.add(new Paragraph("Data Raportului: " + date.toString()));
            document.add(new Paragraph("\n")); // Empty line

            // Create Table
            float[] columnWidths = {200F, 200F};
            Table table = new Table(columnWidths);

            // Header Row
            table.addCell(new Paragraph("INDICATOR").setBold());
            table.addCell(new Paragraph("VALOARE").setBold());

            // Data Rows
            table.addCell("Total Pasageri (Zboruri Plecare)");
            table.addCell(String.valueOf(passengers));

            table.addCell("Venituri Totale (Bilete Vandute)");
            table.addCell(String.format("%.2f EUR", revenue));

            document.add(table);
            document.close();

            showAlert("Succes", "Raport PDF generat cu succes:\n" + dest);

        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Eroare", "Nu s-a putut genera PDF-ul. Verificați dacă fișierul este deja deschis.");
        }
    }


}