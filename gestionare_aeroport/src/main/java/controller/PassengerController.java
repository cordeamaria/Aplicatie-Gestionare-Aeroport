package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import model.*;
import repository.*;
import util.SceneSwitcher;


import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class PassengerController implements UserAware {

    // ================= INFORMAȚII UTILIZATOR =================
    @FXML private VBox userInfoPane;
    @FXML private Label userNameLabel;
    @FXML private Label userRoleLabel;

    // ================= PANOURI =================
    @FXML private VBox searchFlightPane;
    @FXML private VBox myFlightsPane;
    @FXML private VBox myLuggagePane;

    // ================= CĂUTARE ZBORURI =================
    @FXML private TextField departureField;
    @FXML private TextField destinationField;
    @FXML private DatePicker datePicker;
    @FXML private DatePicker arrivalDatePicker;

    @FXML private TableView<Zbor> flightTable;
    @FXML private TableColumn<Zbor, String> colCode;
    @FXML private TableColumn<Zbor, String> colDeparture;
    @FXML private TableColumn<Zbor, String> colDestination;
    @FXML private TableColumn<Zbor, LocalDateTime> colDate;
    @FXML private TableColumn<Zbor, LocalDateTime> colArrivalDate; // Coloană nouă pentru sosire
    @FXML private TableColumn<Zbor, String> colStatus;

    @FXML private CheckBox handBagCheck;
    @FXML private CheckBox holdBagCheck;
    @FXML private Button buyTicketButton;

    // ================= BILETELE MELE =================
    @FXML private TableView<Bilet> myFlightsTable;
    @FXML private TableColumn<Bilet, String> colFlightCode;
    @FXML private TableColumn<Bilet, String> colSeat;
    @FXML private TableColumn<Bilet, String> colClass;
    @FXML private TableColumn<Bilet, Double> colPrice;
    @FXML private TableColumn<Bilet, LocalDateTime> colPurchaseDate;
    @FXML private TableColumn<Bilet, String> colPaymentStatus;

    // ================= BAGAJELE MELE =================
    @FXML private TableView<Bagaj> myLuggageTable;
    @FXML private TableColumn<Bagaj, String> colBagType;
    @FXML private TableColumn<Bagaj, Double> colWeight;
    @FXML private TableColumn<Bagaj, String> colFlightCodeBag;
    @FXML private TableColumn<Bagaj, String> colStatusCheck;

    // ================= REPOSITORY-URI =================
    private final ZborRepository zborRepository = new ZborRepository();
    private final BiletRepository biletRepository = new BiletRepository();
    private final BagajRepository bagajRepository = new BagajRepository();

    private User loggedUser;

    // ================= INITIALIZE =================
    @FXML
    public void initialize() {
        // Mapare coloane tabel Zboruri
        colCode.setCellValueFactory(new PropertyValueFactory<>("cod_zbor"));
        colDeparture.setCellValueFactory(new PropertyValueFactory<>("plecare_din"));
        colDestination.setCellValueFactory(new PropertyValueFactory<>("destinatie"));
        colDate.setCellValueFactory(new PropertyValueFactory<>("data_plecare"));
        colArrivalDate.setCellValueFactory(new PropertyValueFactory<>("data_sosire")); // Mapare data sosire
        colStatus.setCellValueFactory(new PropertyValueFactory<>("status_zbor"));

        // Mapare coloane tabel Bilete
        colFlightCode.setCellValueFactory(new PropertyValueFactory<>("codZbor"));
        colSeat.setCellValueFactory(new PropertyValueFactory<>("loc"));
        colClass.setCellValueFactory(new PropertyValueFactory<>("clasa"));
        colPrice.setCellValueFactory(new PropertyValueFactory<>("pret"));
        colPurchaseDate.setCellValueFactory(new PropertyValueFactory<>("data_cumparare"));
        colPaymentStatus.setCellValueFactory(new PropertyValueFactory<>("status_plata"));

        // Mapare coloane tabel Bagaje
        colBagType.setCellValueFactory(new PropertyValueFactory<>("tip"));
        colWeight.setCellValueFactory(new PropertyValueFactory<>("greutate"));
        colFlightCodeBag.setCellValueFactory(new PropertyValueFactory<>("id_bilet"));
        colStatusCheck.setCellValueFactory(new PropertyValueFactory<>("status_checkin"));

        // Setare inițială vizibilitate
        userInfoPane.setVisible(true);
        searchFlightPane.setVisible(false);
        myFlightsPane.setVisible(false);
        myLuggagePane.setVisible(false);
    }

    @Override
    public void setLoggedUser(User user) {
        this.loggedUser = user;
        userNameLabel.setText( user.getUsername());
        userRoleLabel.setText( user.getRol());
    }

    // ================= AFIȘARE PANOURI =================
    @FXML
    public void showSearchFlightPane() {
        hideAllPanels();
        searchFlightPane.setVisible(true);
        searchFlightPane.setManaged(true);
    }

    @FXML
    public void showMyFlightsPane() {
        hideAllPanels();
        myFlightsPane.setVisible(true);
        myFlightsPane.setManaged(true);
        populateMyFlightsTable();
    }

    @FXML
    public void showMyLuggagePane() {
        hideAllPanels();
        myLuggagePane.setVisible(true);
        myLuggagePane.setManaged(true);
        populateMyLuggageTable();
    }

    private void hideAllPanels() {
        searchFlightPane.setVisible(false);
        searchFlightPane.setManaged(false);
        myFlightsPane.setVisible(false);
        myFlightsPane.setManaged(false);
        myLuggagePane.setVisible(false);
        myLuggagePane.setManaged(false);
        userInfoPane.setVisible(false);
    }

    // ================= CĂUTARE ȘI FILTRARE =================
    @FXML
    public void showAllFlights() {
        flightTable.setItems(zborRepository.getAllZboruri());
    }

    @FXML
    public void handleFilter() {
        String departure = (departureField.getText() == null) ? "" : departureField.getText().toLowerCase();
        String destination = (destinationField.getText() == null) ? "" : destinationField.getText().toLowerCase();

        // Preluăm valorile din DatePickers
        LocalDate searchDateDep = datePicker.getValue();
        LocalDate searchDateArr = arrivalDatePicker.getValue();

        ObservableList<Zbor> filtered = zborRepository.getAllZboruri().filtered(z -> {
            // Filtrare după text (Plecare și Destinație)
            boolean matchDep = departure.isEmpty() || z.getPlecare_din().toLowerCase().contains(departure);
            boolean matchDest = destination.isEmpty() || z.getDestinatie().toLowerCase().contains(destination);

            // FILTRARE DUPĂ DATĂ (Corectată)
            // Comparăm doar data (an-lună-zi), ignorând ora
            boolean matchDateDep = (searchDateDep == null) ||
                    (z.getData_plecare() != null && z.getData_plecare().toLocalDate().equals(searchDateDep));

            boolean matchDateArr = (searchDateArr == null) ||
                    (z.getData_sosire() != null && z.getData_sosire().toLocalDate().equals(searchDateArr));

            return matchDep && matchDest && matchDateDep && matchDateArr;
        });

        flightTable.setItems(filtered);
    }

    @FXML
    public void handleBuyTicket() {
        Zbor selected = flightTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("Atenție", "Vă rugăm să selectați un zbor!");
            return;
        }

        double price = 100.0;
        if (holdBagCheck.isSelected()) price += 50.0;

        Bilet bilet = new Bilet(0L, selected.getId_zbor(), loggedUser.getId_user(), "A1",
                "Economy", price, LocalDateTime.now(), "PLATIT");

        biletRepository.add(bilet);

        if (handBagCheck.isSelected()) {
            bagajRepository.addBagaj(new Bagaj(bagajRepository.getNextId(), bilet.getId_bilet(), 8.0, "Mana", "predat", "HL" + bilet.getId_bilet()));
        }
        if (holdBagCheck.isSelected()) {
            bagajRepository.addBagaj(new Bagaj(bagajRepository.getNextId(), bilet.getId_bilet(), 23.0, "Cala", "predat", "HL" + bilet.getId_bilet()));
        }

        showAlert("Succes", "Biletul a fost cumpărat cu succes!");
        flightTable.refresh();
    }

    private void populateMyFlightsTable() {
        ObservableList<Bilet> myTickets = FXCollections.observableArrayList(
                biletRepository.getBileteByUserId(loggedUser.getId_user())
        );
        myFlightsTable.setItems(myTickets);
    }

    private void populateMyLuggageTable() {
        ObservableList<Bagaj> myBags = FXCollections.observableArrayList(
                bagajRepository.getBagajeByUserId(loggedUser.getId_user(), biletRepository)
        );
        myLuggageTable.setItems(myBags);
    }

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