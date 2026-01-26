package OCSF.server.networking;

import OCSF.server.AbstractServer;
import OCSF.server.ConnectionToClient;
import networking.Message;
import model.*;
import OCSF.server.repository.*;

import java.io.IOException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AirportServer extends AbstractServer {

    // Repositories
    private UserRepository userRepo;
    private ZborRepository zborRepo;
    private BiletRepository biletRepo;
    private BagajRepository bagajRepo;
    private AeronavaRepository aeronavaRepo;
    private ProblemaRepository problemaRepo;
    private CrewAssignmentRepository crewRepo;
    private TaskRepository taskRepo;
    // Add other repositories as needed

    public AirportServer(int port) {
        super(port);
        // Initialize the repositories that talk to the Database
        this.userRepo = new UserRepository();
        this.zborRepo = new ZborRepository();
        this.biletRepo = new BiletRepository();
        this.bagajRepo = new BagajRepository();
        this.aeronavaRepo = new AeronavaRepository();
        this.problemaRepo = new ProblemaRepository();
        this.crewRepo = new CrewAssignmentRepository();
        this.taskRepo = new TaskRepository();
    }

    @Override
    protected void handleMessageFromClient(Object msg, ConnectionToClient client) {
        if (msg instanceof Message) {
            Message request = (Message) msg;
            Message response = null;

            System.out.println("Server received command: " + request.getType());

            try {
                switch (request.getType()) {

                    case "LOGIN":
                        User loginRequest = (User) request.getObject();
                        User foundUser = findUser(loginRequest.getUsername(), loginRequest.getParola());

                        if (foundUser != null) {
                            response = new Message("LOGIN_SUCCESS", foundUser);
                            System.out.println("User logged in: " + foundUser.getUsername());
                        } else {
                            response = new Message("LOGIN_FAILED", null);
                            System.out.println("Login failed for: " + loginRequest.getUsername());
                        }
                        break;

                    case "GET_ALL_USERS":
                        response = new Message("USERS_LIST", userRepo.findAll());
                        break;

                    case "ADD_USER":
                        userRepo.insert((User) request.getObject());
                        response = new Message("SUCCESS", "User added");
                        break;

                    case "UPDATE_USER":
                        userRepo.update((User) request.getObject());
                        response = new Message("SUCCESS", "User updated");
                        break;

                    case "DELETE_USER":
                        // The payload is an Integer ID
                        userRepo.delete((Integer) request.getObject());
                        response = new Message("SUCCESS", "User deleted");
                        break;

                    case "GET_ALL_PLANES":
                        response = new Message("PLANES_LIST", aeronavaRepo.findAll());
                        break;

                    case "ADD_PLANE":
                        aeronavaRepo.insert((Aeronava) request.getObject());
                        response = new Message("SUCCESS", "Plane added");
                        break;

                    case "UPDATE_PLANE":
                        Aeronava planeToUpdate = (Aeronava) request.getObject();
                        aeronavaRepo.update(planeToUpdate);
                        response = new Message("SUCCESS", "Plane updated");
                        break;

                    case "DELETE_PLANE":
                        // The client sends the ID (Integer)
                        Integer planeId = (Integer) request.getObject();
                        aeronavaRepo.delete(planeId);
                        response = new Message("SUCCESS", "Plane deleted");
                        break;

                    case "GET_ALL_FLIGHTS":
                        List<Zbor> flights = zborRepo.findAll();
                        response = new Message("FLIGHTS_LIST", flights);
                        break;

                    case "ADD_FLIGHT":
                        // Get the flight object from the message
                        Zbor newFlight = (Zbor) request.getObject();
                        // Insert into database
                        zborRepo.insert(newFlight);
                        // Send success response
                        response = new Message("SUCCESS", "Flight added successfully");
                        break;

                    case "UPDATE_FLIGHT":
                        Zbor flightToUpdate = (Zbor) request.getObject();
                        zborRepo.update(flightToUpdate);
                        response = new Message("SUCCESS", "Flight updated successfully");
                        break;

                    case "DELETE_FLIGHT":
                        // Client sends the ID
                        Integer flightId = (Integer) request.getObject();
                        zborRepo.delete(flightId);
                        response = new Message("SUCCESS", "Flight deleted successfully");
                        break;

                    case "ASSIGN_CREW":
                        CrewAssignment assignment = (CrewAssignment) request.getObject();
                        crewRepo.insert(assignment);
                        response = new Message("SUCCESS", "Crew assigned successfully");
                        break;

                    case "GET_CREW_ASSIGNMENTS":
                        response = new Message("CREW_LIST", crewRepo.findAll());
                        break;

                    case "GET_ALL_CREW":
                        List<CrewAssignment> crewList = crewRepo.findAll();
                        response = new Message("CREW_LIST", crewList);
                        break;

                    case "ADD_CREW":
                        CrewAssignment ca = (CrewAssignment) request.getObject();
                        crewRepo.insert(ca);
                        response = new Message("SUCCESS", "Crew Assigned");
                        break;

                    case "GET_MY_TASKS":
                        Integer workerId = (Integer) request.getObject();

                        List<Task> allTasks = taskRepo.findAll();

                        List<Task> myTasks = allTasks.stream()
                                .filter(t -> t.getId_muncitor() != null && t.getId_muncitor().intValue() == workerId)
                                .toList();

                        response = new Message("TASKS_LIST", myTasks);
                        break;


                    case "GET_ALL_ISSUES":
                        response = new Message("ISSUES_LIST", problemaRepo.findAll());
                        break;

                    case "ADD_TICKET":
                        Bilet ticketToAdd = (Bilet) request.getObject();
                        Bilet savedTicket = biletRepo.insert(ticketToAdd);
                        response = new Message("TICKET_SAVED", savedTicket);
                        break;

                    case "ADD_BAGGAGE":
                        Bagaj bagToAdd = (Bagaj) request.getObject();
                        bagajRepo.insert(bagToAdd);
                        response = new Message("SUCCESS", "Baggage added");
                        break;

                    case "GET_MY_TICKETS":
                        Number userIdNum = (Number) request.getObject();
                        Long userId = userIdNum.longValue();

                        List<Bilet> allTickets = biletRepo.findAll();
                        // Filter: keep only tickets where id_pasager == userId
                        List<Bilet> myTickets = allTickets.stream()
                                .filter(ticket -> ticket.getId_pasager() == userId)
                                .toList();

                        response = new Message("TICKETS_LIST", myTickets);
                        break;

                    case "GET_MY_BAGGAGE":
                        Number uIdNum = (Number) request.getObject();
                        Long uId = uIdNum.longValue();

                        List<Bilet> userTickets = biletRepo.findAll().stream()
                                .filter(ticket -> ticket.getId_pasager() == uId)
                                .toList();

                        List<Long> ticketIds = userTickets.stream()
                                .map(ticket -> ticket.getId().longValue())
                                .toList();

                        List<Bagaj> allBags = bagajRepo.findAll();
                        List<Bagaj> myBags = allBags.stream()
                                .filter(b -> ticketIds.contains(b.getId_bilet())) // ensure Bagaj has getId_bilet()
                                .toList();

                        response = new Message("BAGGAGE_LIST", myBags);
                        break;

                    case "GET_DAILY_STATS":
                        LocalDate reportDate = (LocalDate) request.getObject();

                        List<Zbor> allFlights = zborRepo.findAll();
                        List<Long> flightIdsToday = allFlights.stream()
                                .filter(z -> z.getData_plecare().toLocalDate().equals(reportDate))
                                .map(z -> z.getId().longValue())
                                .toList();

                        List<Bilet> allTicketsStats = biletRepo.findAll();

                        double countPassengers = 0;
                        double sumRevenue = 0;

                        for (Bilet b : allTicketsStats) {
                            if (flightIdsToday.contains(b.getId_zbor())) {
                                countPassengers++;
                                sumRevenue += b.getPret();
                            }
                        }

                        Map<String, Double> statsMap = new HashMap<>();
                        statsMap.put("passengers", countPassengers);
                        statsMap.put("revenue", sumRevenue);

                        response = new Message("STATS_RESULT", statsMap);
                        break;
                }

                client.sendToClient(response);

            } catch (Exception e) {
                e.printStackTrace();
                try {
                    client.sendToClient(new Message("ERROR", e.getMessage()));
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        }
    }

    private User findUser(String username, String password) {
        List<User> allUsers = userRepo.findAll();
        if (allUsers == null) return null;

        return allUsers.stream()
                .filter(u -> u.getUsername().equals(username) && u.getParola().equals(password))
                .findFirst()
                .orElse(null);
    }

    @Override
    protected void serverStarted() {
        System.out.println("Server listening for connections on port " + getPort());
    }

    @Override
    protected void serverStopped() {
        System.out.println("Server has stopped listening for connections.");
    }
}