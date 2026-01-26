package OCSF.server.repository;

import OCSF.server.repository.AbstractDAO;
import model.User;
import java.util.List;

public class UserRepository extends AbstractDAO<User> {

    // Basic CRUD provided by AbstractDAO

    // Login
    public User validateUser(String username, String password) {
        List<User> allUsers = findAll();

        if (allUsers == null) return null;

        return allUsers.stream()
                .filter(u -> u.getUsername().equals(username) && u.getParola().equals(password))
                .findFirst()
                .orElse(null);
    }
}