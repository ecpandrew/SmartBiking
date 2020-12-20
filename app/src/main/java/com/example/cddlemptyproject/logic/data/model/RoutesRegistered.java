package com.example.cddlemptyproject.logic.data.model;

import android.os.Parcel;
import android.os.Parcelable;

public class RoutesRegistered implements Parcelable {

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

    protected RoutesRegistered(Parcel in) {
        nome = in.readString();
        identificador = in.readString();
        dificuldade = in.readString();
    }

    public static final Creator<RoutesRegistered> CREATOR = new Creator<RoutesRegistered>() {
        @Override
        public RoutesRegistered createFromParcel(Parcel in) {
            return new RoutesRegistered(in);
        }

        @Override
        public RoutesRegistered[] newArray(int size) {
            return new RoutesRegistered[size];
        }
    };

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(nome);
        dest.writeString(identificador);
        dest.writeString(dificuldade);
    }
}
