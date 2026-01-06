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
import java.util.List;

public class PassengerController implements UserAware {

    @FXML private VBox userInfoPane, searchFlightPane, myFlightsPane, myLuggagePane;
    @FXML private Label userNameLabel, userRoleLabel;

    // Search Flights
    @FXML private TextField departureField, destinationField;
    @FXML private DatePicker datePicker, arrivalDatePicker;
    @FXML private TableView<Zbor> flightTable;
    @FXML private TableColumn<Zbor, String> colCode, colDeparture, colDestination, colStatus;
    @FXML private TableColumn<Zbor, LocalDateTime> colDate, colArrivalDate;

    // Buy Ticket
    @FXML private CheckBox handBagCheck, holdBagCheck;

    // My Flights
    @FXML private TableView<Bilet> myFlightsTable;
    @FXML private TableColumn<Bilet, String> colFlightCode, colSeat, colClass, colPaymentStatus;
    @FXML private TableColumn<Bilet, Double> colPrice;
    @FXML private TableColumn<Bilet, LocalDateTime> colPurchaseDate;

    // My Luggage
    @FXML private TableView<Bagaj> myLuggageTable;
    @FXML private TableColumn<Bagaj, String> colBagType, colFlightCodeBag, colStatusCheck;
    @FXML private TableColumn<Bagaj, Double> colWeight;

    private User loggedUser;

    @FXML
    public void initialize() {
        // Flight Columns
        colCode.setCellValueFactory(new PropertyValueFactory<>("cod_zbor"));
        colDeparture.setCellValueFactory(new PropertyValueFactory<>("plecare_din"));
        colDestination.setCellValueFactory(new PropertyValueFactory<>("destinatie"));
        colDate.setCellValueFactory(new PropertyValueFactory<>("data_plecare"));
        colArrivalDate.setCellValueFactory(new PropertyValueFactory<>("data_sosire"));
        colStatus.setCellValueFactory(new PropertyValueFactory<>("status_zbor"));

        // Ticket Columns
        colFlightCode.setCellValueFactory(new PropertyValueFactory<>("codZbor"));
        colSeat.setCellValueFactory(new PropertyValueFactory<>("loc"));
        colClass.setCellValueFactory(new PropertyValueFactory<>("clasa"));
        colPrice.setCellValueFactory(new PropertyValueFactory<>("pret"));
        colPurchaseDate.setCellValueFactory(new PropertyValueFactory<>("data_cumparare"));
        colPaymentStatus.setCellValueFactory(new PropertyValueFactory<>("status_plata"));

        // Luggage Columns
        colBagType.setCellValueFactory(new PropertyValueFactory<>("tip"));
        colWeight.setCellValueFactory(new PropertyValueFactory<>("greutate"));
        colFlightCodeBag.setCellValueFactory(new PropertyValueFactory<>("id_bilet"));
        colStatusCheck.setCellValueFactory(new PropertyValueFactory<>("status_checkin"));

        hideAllPanels();
        userInfoPane.setVisible(true);
    }

    @Override
    public void setLoggedUser(User user) {
        this.loggedUser = user;
        userNameLabel.setText(user.getUsername());
        userRoleLabel.setText(user.getRol());
    }

    // ================= PANELS =================
    @FXML public void showSearchFlightPane() {
        hideAllPanels();
        searchFlightPane.setVisible(true);
        searchFlightPane.setManaged(true);
    }

    @FXML public void showMyFlightsPane() {
        hideAllPanels();
        myFlightsPane.setVisible(true);
        myFlightsPane.setManaged(true);
        populateMyFlightsTable();
    }

    @FXML public void showMyLuggagePane() {
        hideAllPanels();
        myLuggagePane.setVisible(true);
        myLuggagePane.setManaged(true);
        populateMyLuggageTable();
    }

    // ================= OPERATIONS =================
    @FXML
    public void showAllFlights() {
        List<Zbor> flights = (List<Zbor>) AirportClient.getInstance().sendRequest("GET_ALL_FLIGHTS", null);
        if (flights != null) {
            flightTable.setItems(FXCollections.observableArrayList(flights));
        }
    }

    @FXML
    public void handleFilter() {
        List<Zbor> allFlights = (List<Zbor>) AirportClient.getInstance().sendRequest("GET_ALL_FLIGHTS", null);
        if (allFlights == null) return;

        String departure = (departureField.getText() == null) ? "" : departureField.getText().toLowerCase();
        String destination = (destinationField.getText() == null) ? "" : destinationField.getText().toLowerCase();
        LocalDate searchDateDep = datePicker.getValue();
        LocalDate searchDateArr = arrivalDatePicker.getValue();

        ObservableList<Zbor> filtered = FXCollections.observableArrayList(allFlights).filtered(z -> {
            boolean matchDep = departure.isEmpty() || z.getPlecare_din().toLowerCase().contains(departure);
            boolean matchDest = destination.isEmpty() || z.getDestinatie().toLowerCase().contains(destination);
            boolean matchDateDep = (searchDateDep == null) || (z.getData_plecare() != null && z.getData_plecare().toLocalDate().equals(searchDateDep));
            boolean matchDateArr = (searchDateArr == null) || (z.getData_sosire() != null && z.getData_sosire().toLocalDate().equals(searchDateArr));
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

        Bilet bilet = new Bilet(0, selected.getId().longValue(), loggedUser.getId().longValue(), "A1", "Economy", price, LocalDateTime.now(), "PLATIT");

        // 1. Send Ticket to Server (Server returns ticket with generated ID)
        Bilet savedTicket = (Bilet) AirportClient.getInstance().sendRequest("ADD_TICKET", bilet);

        if (savedTicket != null) {
            // 2. Add Hand Bag (if selected) using new Ticket ID
            if (handBagCheck.isSelected()) {
                Bagaj bag = new Bagaj(0, savedTicket.getId().longValue(), 8.0, "Mana", "predat", "HL" + savedTicket.getId());
                AirportClient.getInstance().sendRequest("ADD_BAGGAGE", bag);
            }
            // 3. Add Hold Bag (if selected)
            if (holdBagCheck.isSelected()) {
                Bagaj bag = new Bagaj(0, savedTicket.getId().longValue(), 23.0, "Cala", "predat", "HL" + savedTicket.getId());
                AirportClient.getInstance().sendRequest("ADD_BAGGAGE", bag);
            }
            showAlert("Succes", "Biletul a fost cumpărat cu succes!");
            showAllFlights(); // Refresh
        } else {
            showAlert("Eroare", "Eroare la cumpărarea biletului.");
        }
    }

    private void populateMyFlightsTable() {
        // Request tickets by User ID
        List<Bilet> tickets = (List<Bilet>) AirportClient.getInstance().sendRequest("GET_MY_TICKETS", loggedUser.getId());
        if (tickets != null) myFlightsTable.setItems(FXCollections.observableArrayList(tickets));
    }

    private void populateMyLuggageTable() {
        // Request luggage by User ID (Server handles logic)
        List<Bagaj> bags = (List<Bagaj>) AirportClient.getInstance().sendRequest("GET_MY_BAGGAGE", loggedUser.getId());
        if (bags != null) myLuggageTable.setItems(FXCollections.observableArrayList(bags));
    }

    private void hideAllPanels() {
        searchFlightPane.setVisible(false); searchFlightPane.setManaged(false);
        myFlightsPane.setVisible(false); myFlightsPane.setManaged(false);
        myLuggagePane.setVisible(false); myLuggagePane.setManaged(false);
        userInfoPane.setVisible(false);
    }
    private void showAlert(String t, String c) { Alert a = new Alert(Alert.AlertType.INFORMATION); a.setTitle(t); a.setContentText(c); a.show(); }
    public void handleLogout(ActionEvent event) {
        try { this.loggedUser = null; SceneSwitcher.changeScene(event, "LoginView.fxml", "Autentificare Aeroport", null); } catch(Exception e){}
    }
}