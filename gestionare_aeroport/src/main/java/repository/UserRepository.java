package repository;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.User;

import static java.lang.Math.random;

public class UserRepository {
    private static final ObservableList<User> users = FXCollections.observableArrayList(
            new User(1L, "admin", "123", "ADMINISTRATOR"),
            new User(2L, "pilot1", "123", "PILOT"),
            new User(3L, "muncitor1", "123", "MUNCITOR"),
            new User(4L,"dispecer","123","DISPECER"),
            new User(5L,"pasager","123","PASAGER")
    );

    public ObservableList<User> getAllUsers() {
        return users;
    }

    public ObservableList<User> getUsersByRole(String role) {
        return users.filtered(u -> u.getRol().equalsIgnoreCase(role));
    }

    // CREATE PERSONAL
    public void addUser(User user) {
        users.add(user);
    }

    // UPDATE PERSONAL (Extends Read)
    public void updateUser(User updatedUser) {
        for (int i = 0; i < users.size(); i++) {
            if (users.get(i).getId_user().equals(updatedUser.getId_user())) {
                users.set(i, updatedUser);
                break;
            }
        }
    }

    // DELETE PERSONAL
    public void deleteUser(User user) {
        users.remove(user);
    }

    public User validateUser(String username, String password) {
        return users.stream()
                .filter(u -> u.getUsername().equals(username) && u.getParola().equals(password))
                .findFirst()
                .orElse(null);
    }
    // Aici se va face legătura cu OCSF Client în viitor
}