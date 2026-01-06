package model;

import java.io.Serializable;

public class CrewAssignment implements Serializable {
    private static final long serialVersionUID = 1L;

    private Integer id;
    private Long id_user;
    private Long id_zbor;
    private String rol_in_zbor; // ex: Pilot, Copilot, Stewardesa

    public CrewAssignment() {}

    public CrewAssignment(Integer id_assignment, Long id_user, Long id_zbor, String rol_in_zbor) {
        this.id = id_assignment;
        this.id_user = id_user;
        this.id_zbor = id_zbor;
        this.rol_in_zbor = rol_in_zbor;
    }

    // Getters și Setters


    public Integer getId() {
        return id;
    }

    public void setId(Integer id_assignment) {
        this.id = id_assignment;
    }

    public Long getId_user() {
        return id_user;
    }

    public void setId_user(Long id_user) {
        this.id_user = id_user;
    }

    public Long getId_zbor() {
        return id_zbor;
    }

    public void setId_zbor(Long id_zbor) {
        this.id_zbor = id_zbor;
    }

    public String getRol_in_zbor() {
        return rol_in_zbor;
    }

    public void setRol_in_zbor(String rol_in_zbor) {
        this.rol_in_zbor = rol_in_zbor;
    }
}