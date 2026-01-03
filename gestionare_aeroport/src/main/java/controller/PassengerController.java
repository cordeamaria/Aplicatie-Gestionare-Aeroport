package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import model.*;
import repository.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class PassengerController implements UserAware{

    // ================= USER INFO =================
    @FXML private VBox userInfoPane;
    @FXML private Label userNameLabel;
    @FXML private Label userRoleLabel;

    // ================= PANELS =================
    @FXML private VBox searchFlightPane;
    @FXML private VBox myFlightsPane;
    @FXML private VBox myLuggagePane;

    // ================= SEARCH FLIGHT =================
    @FXML private TextField departureField;
    @FXML private TextField destinationField;
    @FXML private DatePicker datePicker;
    @FXML private TableView<Zbor> flightTable;
    @FXML private TableColumn<Zbor, String> colCode;
    @FXML private TableColumn<Zbor, String> colDeparture;
    @FXML private TableColumn<Zbor, String> colDestination;
    @FXML private TableColumn<Zbor, LocalDateTime> colDate;
    @FXML private TableColumn<Zbor, String> colStatus;
    @FXML private CheckBox handBagCheck;
    @FXML private CheckBox holdBagCheck;
    @FXML private Button buyTicketButton;

    // ================= MY FLIGHTS =================
    @FXML private TableView<Bilet> myFlightsTable;
    @FXML private TableColumn<Bilet, String> colFlightCode;
    @FXML private TableColumn<Bilet, String> colSeat;
    @FXML private TableColumn<Bilet, String> colClass;
    @FXML private TableColumn<Bilet, Double> colPrice;
    @FXML private TableColumn<Bilet, LocalDateTime> colPurchaseDate;
    @FXML private TableColumn<Bilet, String> colPaymentStatus;


    // ================= MY LUGGAGE =================
    @FXML private TableView<Bagaj> myLuggageTable;
    @FXML private TableColumn<Bagaj, String> colBagType;
    @FXML private TableColumn<Bagaj, Double> colWeight;
    @FXML private TableColumn<Bagaj, String> colFlightCodeBag;
    @FXML private TableColumn<Bagaj, String> colStatusCheck;

    // ================= REPOSITORIES =================
    private final ZborRepository zborRepository = new ZborRepository();
    private final BiletRepository biletRepository = new BiletRepository();
    private final BagajRepository bagajRepository = new BagajRepository();

    private User loggedUser;

    // ================= INITIALIZE =================
    @FXML
    public void initialize() {
        // Table columns binding
        colCode.setCellValueFactory(new PropertyValueFactory<>("cod_zbor"));
        colDeparture.setCellValueFactory(new PropertyValueFactory<>("plecare_din"));
        colDestination.setCellValueFactory(new PropertyValueFactory<>("destinatie"));
        colDate.setCellValueFactory(new PropertyValueFactory<>("data_plecare"));
        colStatus.setCellValueFactory(new PropertyValueFactory<>("status_zbor"));

        colFlightCode.setCellValueFactory(
                new PropertyValueFactory<>("codZbor")
        );
        colSeat.setCellValueFactory(new PropertyValueFactory<>("loc"));
        colClass.setCellValueFactory(new PropertyValueFactory<>("clasa"));
        colPrice.setCellValueFactory(new PropertyValueFactory<>("pret"));
        colPurchaseDate.setCellValueFactory(new PropertyValueFactory<>("data_cumparare"));
        colPaymentStatus.setCellValueFactory(new PropertyValueFactory<>("status_plata"));


        colBagType.setCellValueFactory(new PropertyValueFactory<>("tip"));
        colWeight.setCellValueFactory(new PropertyValueFactory<>("greutate"));
        colFlightCodeBag.setCellValueFactory(new PropertyValueFactory<>("id_bilet")); // poti inlocui cu cod zbor daca ai join
        colStatusCheck.setCellValueFactory(new PropertyValueFactory<>("status_checkin"));

        // Initial user info pane visible
        userInfoPane.setVisible(true);
        searchFlightPane.setVisible(false);
        myFlightsPane.setVisible(false);
        myLuggagePane.setVisible(false);
    }

    // ================= SET LOGGED USER =================
    @Override
    public void setLoggedUser(User user) {
        this.loggedUser = user;
        userNameLabel.setText("User Name: " + user.getUsername());
        userRoleLabel.setText("Rol: " + user.getRol());
    }

    // ================= SHOW PANELS =================
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

    // ================= SEARCH FLIGHT =================
    @FXML
    public void showAllFlights() {
        flightTable.setItems(zborRepository.getAllZboruri());
    }

    @FXML
    public void handleFilter() {
        String departure = departureField.getText().toLowerCase();
        String destination = destinationField.getText().toLowerCase();
        LocalDate date = datePicker.getValue();

        ObservableList<Zbor> filtered = zborRepository.getAllZboruri().filtered(z -> {
            boolean matchDep = departure.isEmpty() || z.getPlecare_din().toLowerCase().contains(departure);
            boolean matchDest = destination.isEmpty() || z.getDestinatie().toLowerCase().contains(destination);
            boolean matchDate = (date == null) || z.getData_plecare().toLocalDate().equals(date);
            return matchDep && matchDest && matchDate;
        });

        flightTable.setItems(filtered);
    }

    @FXML
    public void handleBuyTicket() {
        Zbor selected = flightTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("Warning", "Please select a flight first!");
            return;
        }

        // Create Bilet
        long newBiletId = 0;
        double price = 100.0; // base price
        if (holdBagCheck.isSelected()) price += 50.0;

        Bilet bilet = new Bilet(newBiletId, selected.getId_zbor(), loggedUser.getId_user(), "A1",
                "Economy", price, LocalDateTime.now(), "PAID");

        biletRepository.add(bilet);

        // Create Bagaj
        if (handBagCheck.isSelected()) {
            bagajRepository.addBagaj(new Bagaj(bagajRepository.getNextId(), bilet.getId_bilet(), 8.0, "Hand", "checked-in", "HL" + newBiletId));
        }
        if (holdBagCheck.isSelected()) {
            bagajRepository.addBagaj(new Bagaj(bagajRepository.getNextId(), bilet.getId_bilet(), 23.0, "Hold", "checked-in", "HL" + newBiletId));
        }

        showAlert("Success", "Ticket purchased successfully!");
        flightTable.refresh();
    }

    // ================= POPULATE TABLES =================
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

    // ================= UTILS =================
    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
