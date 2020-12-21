package com.example.cddlemptyproject.logic.data.model;


import android.os.Parcel;
import android.os.Parcelable;

public class RoutesPerformed implements Parcelable {


    private String percurso;
    private String evento;
    private Double velocidade_media;
    private Double distancia_percorrida;
    private Double duracao;
    private String ganho_elevacao;
    private String date;

    public RoutesPerformed(String percurso, String evento, Double velocidade_media, Double distancia_percorrida, Double duracao, String ganho_elevacao, String date) {
        this.percurso = percurso;
        this.evento = evento;
        this.velocidade_media = velocidade_media;
        this.distancia_percorrida = distancia_percorrida;
        this.duracao = duracao;
        this.ganho_elevacao = ganho_elevacao;
        this.date = date;
    }

    protected RoutesPerformed(Parcel in) {
        percurso = in.readString();
        evento = in.readString();
        if (in.readByte() == 0) {
            velocidade_media = null;
        } else {
            velocidade_media = in.readDouble();
        }
        if (in.readByte() == 0) {
            distancia_percorrida = null;
        } else {
            distancia_percorrida = in.readDouble();
        }
        if (in.readByte() == 0) {
            duracao = null;
        } else {
            duracao = in.readDouble();
        }
        ganho_elevacao = in.readString();
        date = in.readString();
    }

    public static final Creator<RoutesPerformed> CREATOR = new Creator<RoutesPerformed>() {
        @Override
        public RoutesPerformed createFromParcel(Parcel in) {
            return new RoutesPerformed(in);
        }

        @Override
        public RoutesPerformed[] newArray(int size) {
            return new RoutesPerformed[size];
        }
    };

    @Override
    public String toString() {
        return "RoutesPerformed{" +
                "percurso='" + percurso + '\'' +
                ", evento='" + evento + '\'' +
                ", velocidade_media=" + velocidade_media +
                ", distancia_percorrida=" + distancia_percorrida +
                ", duracao=" + duracao +
                ", ganho_elevacao='" + ganho_elevacao + '\'' +
                ", date='" + date + '\'' +
                '}';
    }

    public String getPercurso() {
        return percurso;
    }

    public void setPercurso(String percurso) {
        this.percurso = percurso;
    }

    public String getEvento() {
        return evento;
    }

    public void setEvento(String evento) {
        this.evento = evento;
    }

    public Double getVelocidade_media() {
        return velocidade_media;
    }

    public void setVelocidade_media(Double velocidade_media) {
        this.velocidade_media = velocidade_media;
    }

    public Double getDistancia_percorrida() {
        return distancia_percorrida;
    }

    public void setDistancia_percorrida(Double distancia_percorrida) {
        this.distancia_percorrida = distancia_percorrida;
    }

    public Double getDuracao() {
        return duracao;
    }

    public void setDuracao(Double duracao) {
        this.duracao = duracao;
    }

    public String getGanho_elevacao() {
        return ganho_elevacao;
    }

    public void setGanho_elevacao(String ganho_elevacao) {
        this.ganho_elevacao = ganho_elevacao;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(percurso);
        dest.writeString(evento);
        if (velocidade_media == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeDouble(velocidade_media);
        }
        if (distancia_percorrida == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeDouble(distancia_percorrida);
        }
        if (duracao == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeDouble(duracao);
        }
        dest.writeString(ganho_elevacao);
        dest.writeString(date);
    }
}
