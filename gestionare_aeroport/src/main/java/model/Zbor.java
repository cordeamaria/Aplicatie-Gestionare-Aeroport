package model;

import java.time.LocalDateTime;

public class Zbor {
    private Long id_zbor;
    private String cod_zbor;
    private Long id_aeronava;
    private String plecare_din;
    private String destinatie;
    private LocalDateTime data_plecare;
    private LocalDateTime data_sosire;
    private String status_zbor;
    private String echipamente_sol;
    private Integer nr_echipaj_bord;
    private Integer nr_pasageri_estimat;

    public Zbor() {}

    public Zbor(Long id_zbor, String cod_zbor, Long id_aeronava, String plecare_din, String destinatie,
                LocalDateTime data_plecare, LocalDateTime data_sosire, String status_zbor,
                String echipamente_sol, Integer nr_echipaj_bord, Integer nr_pasageri_estimat) {
        this.id_zbor = id_zbor;
        this.cod_zbor = cod_zbor;
        this.id_aeronava = id_aeronava;
        this.plecare_din = plecare_din;
        this.destinatie = destinatie;
        this.data_plecare = data_plecare;
        this.data_sosire = data_sosire;
        this.status_zbor = status_zbor;
        this.echipamente_sol = echipamente_sol;
        this.nr_echipaj_bord = nr_echipaj_bord;
        this.nr_pasageri_estimat = nr_pasageri_estimat;
    }

    // Getters și Setters
    public Long getId_zbor() { return id_zbor; }
    public void setId_zbor(Long id_zbor) { this.id_zbor = id_zbor; }
    public String getCod_zbor() { return cod_zbor; }
    public void setCod_zbor(String cod_zbor) { this.cod_zbor = cod_zbor; }
    public Long getId_aeronava() { return id_aeronava; }
    public void setId_aeronava(Long id_aeronava) { this.id_aeronava = id_aeronava; }
    public String getPlecare_din() { return plecare_din; }
    public void setPlecare_din(String plecare_din) { this.plecare_din = plecare_din; }
    public String getDestinatie() { return destinatie; }
    public void setDestinatie(String destinatie) { this.destinatie = destinatie; }
    public LocalDateTime getData_plecare() { return data_plecare; }
    public void setData_plecare(LocalDateTime data_plecare) { this.data_plecare = data_plecare; }
    public LocalDateTime getData_sosire() { return data_sosire; }
    public void setData_sosire(LocalDateTime data_sosire) { this.data_sosire = data_sosire; }
    public String getStatus_zbor() { return status_zbor; }
    public void setStatus_zbor(String status_zbor) { this.status_zbor = status_zbor; }
    public String getEchipamente_sol() { return echipamente_sol; }
    public void setEchipamente_sol(String echipamente_sol) { this.echipamente_sol = echipamente_sol; }
    public Integer getNr_echipaj_bord() { return nr_echipaj_bord; }
    public void setNr_echipaj_bord(Integer nr_echipaj_bord) { this.nr_echipaj_bord = nr_echipaj_bord; }
    public Integer getNr_pasageri_estimat() { return nr_pasageri_estimat; }
    public void setNr_pasageri_estimat(Integer nr_pasageri_estimat) { this.nr_pasageri_estimat = nr_pasageri_estimat; }
}