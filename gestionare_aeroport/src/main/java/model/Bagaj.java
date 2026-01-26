package model;

import java.io.Serializable;

public class Bagaj implements Serializable{
    private Integer id;
    private Long id_bilet;
    private Double greutate;
    private String tip;
    private String status_checkin;
    private String eticheta;
    private String flightCodeForBagCached;

    private static final long serialVersionUID = 1L;

    public Bagaj() {}

    public Bagaj(Integer id_bagaj, Long id_bilet, Double greutate, String tip, String status_checkin, String eticheta) {
        this.id = id_bagaj;
        this.id_bilet = id_bilet;
        this.greutate = greutate;
        this.tip = tip;
        this.status_checkin = status_checkin;
        this.eticheta = eticheta;
    }

    // Getters și Setters


    public Integer getId() {
        return id;
    }

    public void setId(Integer id_bagaj) {
        this.id = id_bagaj;
    }

    public Long getId_bilet() {
        return id_bilet;
    }

    public void setId_bilet(Long id_bilet) {
        this.id_bilet = id_bilet;
    }

    public Double getGreutate() {
        return greutate;
    }

    public void setGreutate(Double greutate) {
        this.greutate = greutate;
    }

    public String getTip() {
        return tip;
    }

    public void setTip(String tip) {
        this.tip = tip;
    }

    public String getStatus_checkin() {
        return status_checkin;
    }

    public void setStatus_checkin(String status_checkin) {
        this.status_checkin = status_checkin;
    }

    public String getEticheta() {
        return eticheta;
    }

    public void setEticheta(String eticheta) {
        this.eticheta = eticheta;
    }

    public String getFlightCodeBag() {
        if (flightCodeForBagCached != null) return flightCodeForBagCached;
        return "Ticket: " + id_bilet; // Fallback
    }

    public void setFlightCodeForBag(String s) {
        this.flightCodeForBagCached = s;
    }
}
