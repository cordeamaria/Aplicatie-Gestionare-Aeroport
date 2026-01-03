package controller;

import model.User;

public interface UserAware {
    void setLoggedUser(User user);
}
