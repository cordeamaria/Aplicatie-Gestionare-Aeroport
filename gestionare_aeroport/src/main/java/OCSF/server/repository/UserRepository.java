package OCSF.server.repository;

import OCSF.server.repository.AbstractDAO;
import model.User;
import java.util.List;

public class UserRepository extends AbstractDAO<User> {

    // AbstractDAO handles findById, insert, update, delete, findAll automatically.

    // Custom method for Login
    public User validateUser(String username, String password) {
        // Retrieve all users and filter (Simplest approach with AbstractDAO)
        List<User> allUsers = findAll();

        if (allUsers == null) return null;

        return allUsers.stream()
                .filter(u -> u.getUsername().equals(username) && u.getParola().equals(password))
                .findFirst()
                .orElse(null);
    }
}