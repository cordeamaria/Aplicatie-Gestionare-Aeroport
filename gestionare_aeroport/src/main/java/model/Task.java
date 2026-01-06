package model;
import java.io.Serializable;
import java.time.LocalDateTime;

public class Task implements Serializable {
    private static final long serialVersionUID = 1L;

    private Integer id;
    private Long id_muncitor;
    private String descriere;
    private String status; // in_asteptare, in_desfasurare, completat, anulat
    private LocalDateTime data_alocare;
    private LocalDateTime data_finalizare;

    public Task() {}

    public Task(Integer id_task, Long id_muncitor, String descriere, String status, LocalDateTime data_alocare, LocalDateTime data_finalizare) {
        this.id = id_task;
        this.id_muncitor = id_muncitor;
        this.descriere = descriere;
        this.status = status;
        this.data_alocare = data_alocare;
        this.data_finalizare = data_finalizare;
    }

    // Getters și Setters


    public Integer getId() {
        return id;
    }

    public void setId(Integer id_task) {
        this.id = id_task;
    }

    public Long getId_muncitor() {
        return id_muncitor;
    }

    public void setId_muncitor(Long id_muncitor) {
        this.id_muncitor = id_muncitor;
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

    public LocalDateTime getData_alocare() {
        return data_alocare;
    }

    public void setData_alocare(LocalDateTime data_alocare) {
        this.data_alocare = data_alocare;
    }

    public LocalDateTime getData_finalizare() {
        return data_finalizare;
    }

    public void setData_finalizare(LocalDateTime data_finalizare) {
        this.data_finalizare = data_finalizare;
    }
}