package model;

import java.io.Serializable;

public class Aeronava implements Serializable{
    private Integer id;
    private String cod_aeronava;
    private String model;
    private Integer capacitate;
    private String stare_operationala;
    private String locatie_curenta;

    private static final long serialVersionUID = 1L;

    public Aeronava() {}

    public Aeronava(Integer id_aeronava, String cod_aeronava, String model, Integer capacitate, String stare_operationala, String locatie_curenta) {
        this.id = id_aeronava;
        this.cod_aeronava = cod_aeronava;
        this.model = model;
        this.capacitate = capacitate;
        this.stare_operationala = stare_operationala;
        this.locatie_curenta = locatie_curenta;
    }

    // Getters și Setters

    public Integer getId() {
        return id;
    }

    public void setId(Integer id_aeronava) {
        this.id = id_aeronava;
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