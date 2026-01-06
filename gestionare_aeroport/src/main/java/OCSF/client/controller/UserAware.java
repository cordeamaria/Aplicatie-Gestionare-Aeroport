package OCSF.client.controller;

import model.User;

public interface UserAware {
    void setLoggedUser(User user);
}
