package model;

import java.io.Serializable;
import java.time.LocalDateTime;

public class Problema implements Serializable {
    private static final long serialVersionUID = 1L;

    private Integer id;
    private Long id_user_reporter;
    private String descriere;
    private String status;
    private LocalDateTime data_raportare;
    private LocalDateTime data_rezolvare;

    public Problema() {}

    public Problema(Integer id_problema, Long id_user_reporter, String descriere, String status, LocalDateTime data_raportare, LocalDateTime data_rezolvare) {
        this.id = id_problema;
        this.id_user_reporter = id_user_reporter;
        this.descriere = descriere;
        this.status = status;
        this.data_raportare = data_raportare;
        this.data_rezolvare = data_rezolvare;
    }

    // Getters și Setters


    public Integer getId() {
        return id;
    }

    public void setId(Integer id_problema) {
        this.id = id_problema;
    }

    public Long getId_user_reporter() {
        return id_user_reporter;
    }

    public void setId_user_reporter(Long id_user_reporter) {
        this.id_user_reporter = id_user_reporter;
    }

    public String getDescriere() {
        return descriere;
    }

    public void setDescriere(String descriere) {
        this.descriere = descriere;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getData_raportare() {
        return data_raportare;
    }

    public void setData_raportare(LocalDateTime data_raportare) {
        this.data_raportare = data_raportare;
    }

    public LocalDateTime getData_rezolvare() {
        return data_rezolvare;
    }

    public void setData_rezolvare(LocalDateTime data_rezolvare) {
        this.data_rezolvare = data_rezolvare;
    }
}
