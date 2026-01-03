package model;

public class Bagaj {
    private Long id_bagaj;
    private Long id_bilet;
    private Double greutate;
    private String tip; // mana, cala
    private String status_checkin; // nepredat, predat, incarcat, livrat, pierdut
    private String eticheta;

    public Bagaj() {}

    public Bagaj(Long id_bagaj, Long id_bilet, Double greutate, String tip, String status_checkin, String eticheta) {
        this.id_bagaj = id_bagaj;
        this.id_bilet = id_bilet;
        this.greutate = greutate;
        this.tip = tip;
        this.status_checkin = status_checkin;
        this.eticheta = eticheta;
    }

    // Getters și Setters


    public Long getId_bagaj() {
        return id_bagaj;
    }

    public void setId_bagaj(Long id_bagaj) {
        this.id_bagaj = id_bagaj;
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
}
