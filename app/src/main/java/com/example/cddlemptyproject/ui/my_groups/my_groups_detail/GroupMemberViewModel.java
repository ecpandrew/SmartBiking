package com.example.cddlemptyproject.ui.my_groups.my_groups_detail;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.cddlemptyproject.backend.InternalDB;
import com.example.cddlemptyproject.logic.data.model.Coordenadas;
import com.example.cddlemptyproject.logic.data.model.RoutesAvailable;
import com.example.cddlemptyproject.logic.data.model.RoutesPerformed;
import com.example.cddlemptyproject.logic.data.model.RoutesRegistered;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class GroupMemberViewModel extends ViewModel {

    private MutableLiveData<Coordenadas> coordenadaAtual;
    private final String baseUri = "http://cidadesinteligentes.lsdi.ufma.br/";
    private RequestQueue queue;
    private Context context;

    public GroupMemberViewModel() {
        coordenadaAtual  = new MutableLiveData<Coordenadas>();
    }

    public void setVoleyContext(Context context) {
        this.context = context;
        this.queue = Volley.newRequestQueue(context);
    }


    public MutableLiveData<Coordenadas> getCoordenadaAtual() {
        return coordenadaAtual;
    }

    public void loadRoutesOfGroup(String resourceUUID){

        String final_uri = baseUri.concat("collector/resources/").concat(resourceUUID).concat("/").concat("data/").concat("last");
        StringRequest postRequest = new StringRequest(Request.Method.POST, final_uri, response -> {
            try {
                JSONObject jsonResponse = new JSONObject(response); // transforma a response em objeto json


                JSONObject dados = jsonResponse.getJSONArray("resources") // acessa cada um dos elementos ate chegar ao dado que ta la no centro do json
                        .getJSONObject(0)
                        .getJSONObject("capabilities")
                        .getJSONArray("sb_group_track").getJSONObject(0);

                String jsonStr = dados.toString();
                Gson gson = new Gson();

                coordenadaAtual.setValue(gson.fromJson(jsonStr, Coordenadas.class)); // esse valor setado notifica automaticamente a interface e lá podemos atualizar o mapa



            } catch (JSONException e) {
                e.printStackTrace();
            }

        }, error -> {
            System.out.println("you loose: "+ error.toString());

        }){
            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }
            @Override
            public byte[] getBody() throws AuthFailureError {

                try {


                    JSONArray capabilities_array = new JSONArray();
                    capabilities_array.put("sb_group_track"); // Aqui vc adiciona as capacidades que deseja consultar o ultimo dado

                    JSONObject data = new JSONObject();
                    data.put("capabilities", capabilities_array);
                    data.put("matchers", new JSONObject());
                    data.put("start_range", "2016-06-25T12:21:29");
                    data.put("end_range", "2022-06-25T16:21:29");


                    return data.toString().getBytes(StandardCharsets.UTF_8);


                } catch (JSONException e) {
                    e.printStackTrace();
                }


                return super.getBody();
            }

        };
        queue.add(postRequest);
    }


}