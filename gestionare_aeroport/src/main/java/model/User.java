package model;

public class User {
    private Long id_user;
    private String username;
    private String parola;
    private String rol; // Pasager, Muncitor, Dispecer, Administrator, Pilot, Stewardesa, Manager

    public User() {}

    public User(Long id_user, String username, String parola, String rol) {
        this.id_user = id_user;
        this.username = username;
        this.parola = parola;
        this.rol = rol;
    }

    public Long getId_user() {
        return id_user;
    }

    public void setId_user(Long id_user) {
        this.id_user = id_user;
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

