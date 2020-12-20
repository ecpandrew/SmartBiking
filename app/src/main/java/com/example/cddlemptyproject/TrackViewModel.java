package com.example.cddlemptyproject;

import android.content.Context;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.cddlemptyproject.backend.InternalDB;
import com.example.cddlemptyproject.logic.data.model.RoutesPerformed;
import com.example.cddlemptyproject.logic.data.model.RoutesRegistered;
import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.nio.charset.StandardCharsets;
import java.util.List;

public class TrackViewModel extends ViewModel {

    private MutableLiveData<LatLng> coords;
    private MutableLiveData<String> vi;

    private MutableLiveData<String> vel;
    private MutableLiveData<String> distancia;
    private MutableLiveData<String> tempo;

    private MutableLiveData<String> eleGain;


    public TrackViewModel() {
        coords = new MutableLiveData<LatLng>();
        vi = new MutableLiveData<String>();

        vel = new MutableLiveData<String>();
        distancia = new MutableLiveData<String>();
        tempo = new MutableLiveData<String>();
        eleGain = new MutableLiveData<String>();

    }

    public MutableLiveData<String> getEleGain() {
        return eleGain;
    }

    public MutableLiveData<String> getVi() {
        return vi;
    }

    public MutableLiveData<LatLng> getCoords() {
        return coords;
    }

    public MutableLiveData<String> getDistancia() {
        return distancia;
    }

    public MutableLiveData<String> getTempo() {
        return tempo;
    }

    public MutableLiveData<String> getVel() {
        return vel;
    }

    public void loadCoords(LatLng c){
        coords.postValue(c);
    }

    public void loadTempoDecorrido(String tem){
        tempo.postValue(tem);
    }

    public void loadVelocidadeMedia(String vm){
        vel.postValue(vm);

    }

    public void loadVelocidadeInstantanea(String vinst) {
        vi.postValue(vinst);

    }

    public void loadDistanciaPercorrida(String dis){
        distancia.postValue(dis);

    }

    public void loadElevationGain(String ele){
        eleGain.postValue(ele);

    }

}