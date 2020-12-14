package com.example.cddlemptyproject.logic.data.model;

public class RoutesRegistered {

    private String nome;
    private String identificador;
    private String dificuldade;
    private Double[] latitude_array;
    private Double[] longitude_array;
    public RoutesRegistered(String nome, String identificador, String dificuldade, Double[] latitude_array, Double[] longitude_array){
        this.nome = nome;
        this.identificador = identificador;
        this.dificuldade = dificuldade;
        this.latitude_array = latitude_array;
        this.longitude_array = longitude_array;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getIdentificador() {
        return identificador;
    }

    public void setIdentificador(String identificador) {
        this.identificador = identificador;
    }

    public String getDificuldade() {
        return dificuldade;
    }

    public void setDificuldade(String dificuldade) {
        this.dificuldade = dificuldade;
    }

    public Double[] getLatitude_array() {
        return latitude_array;
    }

    public void setLatitude_array(Double[] latitude_array) {
        this.latitude_array = latitude_array;
    }

    public Double[] getLongitude_array() {
        return longitude_array;
    }

    public void setLongitude_array(Double[] longitude_array) {
        this.longitude_array = longitude_array;
    }
}
