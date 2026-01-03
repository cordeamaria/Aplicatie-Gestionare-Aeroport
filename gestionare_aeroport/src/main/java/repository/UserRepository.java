package repository;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.User;

import static java.lang.Math.random;

public class UserRepository {
    private static final ObservableList<User> users = FXCollections.observableArrayList();

    // Vede lista angajati/pasageri/piloti etc
    public ObservableList<User> getAllUsers() {
        return users;
    }
    public ObservableList<User> getUsersByRole(String role) {
        // În varianta finală, aici vei face: SELECT * FROM User WHERE rol = ?
        return users.filtered(u -> u.getRol().equalsIgnoreCase(role));
    }
    // Aici se va face legătura cu OCSF Client în viitor
    public User validateUser(String username, String password) {
        // Mock pentru testarea interfeței momentan [cite: 14]
        if (username.equals("pasager") && password.equals("123")) {
            long id=1;
            return new User(id,"pasager", "123", "PASAGER");
        } else if (username.equals("muncitor") && password.equals("123")) {
            long id=1;
            return new User(id,"muncitor", "123", "MUNCITOR");
        } else if(username.equals("dispecer") && password.equals("123")) {
            long id=1;
            return new User(id,"dispecer", "123", "DISPECER");
        }
        return null;
    }
}