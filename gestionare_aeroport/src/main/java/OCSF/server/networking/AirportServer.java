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

    // Repositories (Server Side)
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
                    // ================= LOGIN =================
                    case "LOGIN":
                        User loginRequest = (User) request.getObject();
                        // Use the helper method to check credentials
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
                        // Assuming you have: private ZborRepository zborRepo = new ZborRepository();
                        List<Zbor> flights = zborRepo.findAll();
                        response = new Message("FLIGHTS_LIST", flights);
                        break;

                    case "ADD_FLIGHT":
                        // 1. Get the flight object from the message
                        Zbor newFlight = (Zbor) request.getObject();
                        // 2. Insert into database
                        zborRepo.insert(newFlight);
                        // 3. Send success response
                        response = new Message("SUCCESS", "Flight added successfully");
                        break;

                    case "UPDATE_FLIGHT":
                        Zbor flightToUpdate = (Zbor) request.getObject();
                        zborRepo.update(flightToUpdate);
                        response = new Message("SUCCESS", "Flight updated successfully");
                        break;

                    case "DELETE_FLIGHT":
                        // Client sends the ID (Integer)
                        Integer flightId = (Integer) request.getObject();
                        zborRepo.delete(flightId);
                        response = new Message("SUCCESS", "Flight deleted successfully");
                        break;

                    case "ASSIGN_CREW": // <--- Verify this string matches your Client
                        CrewAssignment assignment = (CrewAssignment) request.getObject();
                        crewRepo.insert(assignment);
                        response = new Message("SUCCESS", "Crew assigned successfully");
                        break;

                    case "GET_CREW_ASSIGNMENTS":
                        // Optional: if you need to see existing assignments
                        response = new Message("CREW_LIST", crewRepo.findAll());
                        break;

                    case "GET_ALL_CREW":
                        // 1. Get list from DB
                        List<CrewAssignment> crewList = crewRepo.findAll();
                        // 2. Send back to client
                        response = new Message("CREW_LIST", crewList);
                        break;

                    case "ADD_CREW":
                        CrewAssignment ca = (CrewAssignment) request.getObject();
                        crewRepo.insert(ca);
                        response = new Message("SUCCESS", "Crew Assigned");
                        break;

                    case "GET_MY_TASKS":
                        // The client sends the User ID (Integer)
                        Integer workerId = (Integer) request.getObject();

                        // 1. Get ALL tasks from database
                        List<Task> allTasks = taskRepo.findAll();

                        // 2. Filter them using Java Streams to find only this worker's tasks
                        // (Assumes Task class has getId_muncitor())
                        List<Task> myTasks = allTasks.stream()
                                .filter(t -> t.getId_muncitor() != null && t.getId_muncitor().intValue() == workerId)
                                .toList(); // or .collect(Collectors.toList()) for older Java

                        response = new Message("TASKS_LIST", myTasks);
                        break;

                    case "UPDATE_TASK_STATUS":
                        // Optional: If you want to mark tasks as done later
                        Task t = (Task) request.getObject();
                        taskRepo.update(t);
                        response = new Message("SUCCESS", "Task updated");
                        break;

                    case "GET_ALL_ISSUES":
                        // Assuming: private ProblemaRepository problemaRepo = new ProblemaRepository();
                        response = new Message("ISSUES_LIST", problemaRepo.findAll());
                        break;

                    case "ADD_TICKET":
                        Bilet ticketToAdd = (Bilet) request.getObject();
                        // insert returns the object WITH the generated ID
                        Bilet savedTicket = biletRepo.insert(ticketToAdd);
                        // We must send the object back so the client knows the ID
                        response = new Message("TICKET_SAVED", savedTicket);
                        break;

                    case "ADD_BAGGAGE":
                        Bagaj bagToAdd = (Bagaj) request.getObject();
                        bagajRepo.insert(bagToAdd);
                        response = new Message("SUCCESS", "Baggage added");
                        break;

                    case "GET_MY_TICKETS":
                        // Client sends User ID. We filter tickets for this user.
                        Number userIdNum = (Number) request.getObject();
                        Long userId = userIdNum.longValue();

                        List<Bilet> allTickets = biletRepo.findAll();
                        // Filter: keep only tickets where id_pasager == userId
                        List<Bilet> myTickets = allTickets.stream()
                                .filter(ticket -> ticket.getId_pasager() == userId) // ensure Bilet has getId_pasager()
                                .toList(); // or .collect(Collectors.toList()) for older Java

                        response = new Message("TICKETS_LIST", myTickets);
                        break;

                    case "GET_MY_BAGGAGE":
                        // This is tricky: Bag connects to Ticket, Ticket connects to User.
                        // Client sends User ID.
                        Number uIdNum = (Number) request.getObject();
                        Long uId = uIdNum.longValue();

                        // 1. Find all tickets for this user
                        List<Bilet> userTickets = biletRepo.findAll().stream()
                                .filter(ticket -> ticket.getId_pasager() == uId)
                                .toList();

                        // 2. Get the IDs of those tickets
                        List<Long> ticketIds = userTickets.stream()
                                .map(ticket -> ticket.getId().longValue()) // ensure getId() returns correct type
                                .toList();

                        // 3. Find all bags that belong to those ticket IDs
                        List<Bagaj> allBags = bagajRepo.findAll();
                        List<Bagaj> myBags = allBags.stream()
                                .filter(b -> ticketIds.contains(b.getId_bilet())) // ensure Bagaj has getId_bilet()
                                .toList();

                        response = new Message("BAGGAGE_LIST", myBags);
                        break;

                    case "GET_DAILY_STATS":
                        LocalDate reportDate = (LocalDate) request.getObject();

                        // 1. Find all flights departing on this date
                        List<Zbor> allFlights = zborRepo.findAll();
                        List<Long> flightIdsToday = allFlights.stream()
                                .filter(z -> z.getData_plecare().toLocalDate().equals(reportDate))
                                .map(z -> z.getId().longValue()) // Ensure ID is Long
                                .toList();

                        // 2. Get all tickets
                        List<Bilet> allTicketsStats = biletRepo.findAll();

                        // 3. Calculate Stats
                        double countPassengers = 0;
                        double sumRevenue = 0;

                        for (Bilet b : allTicketsStats) {
                            // Check if this ticket belongs to a flight happening today
                            if (flightIdsToday.contains(b.getId_zbor())) {
                                countPassengers++;
                                sumRevenue += b.getPret();
                            }
                        }

                        // 4. Send back as a Map
                        Map<String, Double> statsMap = new HashMap<>();
                        statsMap.put("passengers", countPassengers);
                        statsMap.put("revenue", sumRevenue);

                        response = new Message("STATS_RESULT", statsMap);
                        break;
                }

                // Send response back to the client
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

    // Helper for Login (since AbstractDAO doesn't have specific findByCreds)
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

    // ================= MAIN METHOD TO START SERVER =================
//    public static void main(String[] args) {
//        int port = 3000; // Make sure this matches your Client's port
//        AirportServer server = new AirportServer(port);
//        try {
//            server.listen(); // Start listening
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
}