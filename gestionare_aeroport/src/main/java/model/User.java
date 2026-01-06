package model;

import java.io.Serializable;

public class User implements Serializable {
    private static final long serialVersionUID = 1L;

    private Integer id;
    private String username;
    private String parola;
    private String rol; // Pasager, Muncitor, Dispecer, Administrator, Pilot, Stewardesa, Manager

    public User() {}

    public User(Integer id_user, String username, String parola, String rol) {
        this.id = id_user;
        this.username = username;
        this.parola = parola;
        this.rol = rol;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id_user) {
        this.id = id_user;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getParola() {
        return parola;
    }

    public void setParola(String parola) {
        this.parola = parola;
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }

}

