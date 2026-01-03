package model;

public class Aeronava {
    private Long id_aeronava;
    private String cod_aeronava;
    private String model;
    private Integer capacitate;
    private String stare_operationala; // activ, in_mentenanta, retras
    private String locatie_curenta;

    public Aeronava() {}

    public Aeronava(Long id_aeronava, String cod_aeronava, String model, Integer capacitate, String stare_operationala, String locatie_curenta) {
        this.id_aeronava = id_aeronava;
        this.cod_aeronava = cod_aeronava;
        this.model = model;
        this.capacitate = capacitate;
        this.stare_operationala = stare_operationala;
        this.locatie_curenta = locatie_curenta;
    }

    // Getters și Setters

    public Long getId_aeronava() {
        return id_aeronava;
    }

    public void setId_aeronava(Long id_aeronava) {
        this.id_aeronava = id_aeronava;
    }

    public String getCod_aeronava() {
        return cod_aeronava;
    }

    public void setCod_aeronava(String cod_aeronava) {
        this.cod_aeronava = cod_aeronava;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public Integer getCapacitate() {
        return capacitate;
    }

    public void setCapacitate(Integer capacitate) {
        this.capacitate = capacitate;
    }

    public String getStare_operationala() {
        return stare_operationala;
    }

    public void setStare_operationala(String stare_operationala) {
        this.stare_operationala = stare_operationala;
    }

    public String getLocatie_curenta() {
        return locatie_curenta;
    }

    public void setLocatie_curenta(String locatie_curenta) {
        this.locatie_curenta = locatie_curenta;
    }
}