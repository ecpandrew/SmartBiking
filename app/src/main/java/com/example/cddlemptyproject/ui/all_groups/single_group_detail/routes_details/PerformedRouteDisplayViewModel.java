package com.example.cddlemptyproject.ui.all_groups.single_group_detail.routes_details;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.cddlemptyproject.backend.InternalDB;
import com.example.cddlemptyproject.logic.data.model.Group;

import java.util.List;

public class PerformedRouteDisplayViewModel extends ViewModel {

    private MutableLiveData<String> percurso;
    private MutableLiveData<String> evento;
    private MutableLiveData<String> distancia_percorrida;
    private MutableLiveData<String> velocidade_media;
    private MutableLiveData<String> duracao;
    private MutableLiveData<String> data;

    public PerformedRouteDisplayViewModel() {
        this.percurso = new MutableLiveData<>();
        this.evento = new MutableLiveData<>();
        this.distancia_percorrida = new MutableLiveData<>();
        this.velocidade_media = new MutableLiveData<>();
        this.duracao = new MutableLiveData<>();
        this.data = new MutableLiveData<>();
    }

    public MutableLiveData<String> getPercurso() {
        return percurso;
    }

    public MutableLiveData<String> getEvento() {
        return evento;
    }

    public MutableLiveData<String> getDistancia_percorrida() {
        return distancia_percorrida;
    }

    public MutableLiveData<String> getVelocidade_media() {
        return velocidade_media;
    }

    public MutableLiveData<String> getDuracao() {
        return duracao;
    }

    public MutableLiveData<String> getData() {
        return data;
    }


    void loadData(
            String percurs,
            String event,
            Double distancia_percorrid,
            Double velocidade_medi,
            Double duraca,
            String dat
    ){
        percurso.postValue(percurs);
        evento.postValue(event);
        distancia_percorrida.postValue(distancia_percorrid.toString());
        velocidade_media.postValue(velocidade_medi.toString());
        duracao.postValue(duraca.toString());
        data.postValue(dat);
    }

}
