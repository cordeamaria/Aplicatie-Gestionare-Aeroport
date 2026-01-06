package model;
import java.io.Serializable;
import java.time.LocalDateTime;

public class Bilet implements Serializable {
    private static final long serialVersionUID = 1L;

    private Integer id;
    private Long id_zbor;
    private Long id_pasager;
    private String loc;
    private String clasa;
    private Double pret;
    private LocalDateTime data_cumparare;
    private String status_plata;



    // Optional: Add a field to store the code if the server sends it
//    private String codZborCached;
    private String codZborCached;

    public Bilet() {}

    public Bilet(Integer id_bilet, Long id_zbor, Long id_pasager, String loc, String clasa, Double pret, LocalDateTime data_cumparare, String status_plata) {
        this.id = id_bilet;
        this.id_zbor = id_zbor;
        this.id_pasager = id_pasager;
        this.loc = loc;
        this.clasa = clasa;
        this.pret = pret;
        this.data_cumparare = data_cumparare;
        this.status_plata = status_plata;
    }

    // Getters și Setters
    public Integer getId() { return id; }
    public void setId(Integer id_bilet) { this.id = id_bilet; }

    public Long getId_zbor() { return id_zbor; }
    public void setId_zbor(Long id_zbor) { this.id_zbor = id_zbor; }

    public Long getId_pasager() { return id_pasager; }
    public void setId_pasager(Long id_pasager) { this.id_pasager = id_pasager; }

    public String getLoc() { return loc; }
    public void setLoc(String loc) { this.loc = loc; }

    public String getClasa() { return clasa; }
    public void setClasa(String clasa) { this.clasa = clasa; }

    public Double getPret() { return pret; }
    public void setPret(Double pret) { this.pret = pret; }

    public LocalDateTime getData_cumparare() { return data_cumparare; }
    public void setData_cumparare(LocalDateTime data_cumparare) { this.data_cumparare = data_cumparare; }

    public String getStatus_plata() { return status_plata; }
    public void setStatus_plata(String status_plata) { this.status_plata = status_plata; }

    // === THE FIX IS HERE ===
    // We removed the call to 'repository.ZborRepository'
    public String getCodZbor() {
        if (codZborCached != null) {
            return codZborCached;
        }
        return "ID: " + id_zbor; // Return ID as fallback since we can't check DB here
    }

    public void setCodZbor(String code) {
        this.codZborCached = code;
    }
}