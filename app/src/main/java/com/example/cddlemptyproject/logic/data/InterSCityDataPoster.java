package com.example.cddlemptyproject.logic.data;

import android.content.Context;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.model.Marker;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONStringer;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;
import java.util.UUID;

import br.ufma.lsdi.cddl.message.Message;

public class InterSCityDataPoster {

    private final String baseUri = "http://cidadesinteligentes.lsdi.ufma.br/";
//    private final String baseUri = "http://192.168.15.115:8000/";


    private RequestQueue queue;

    public InterSCityDataPoster(Context context){
        this.queue = Volley.newRequestQueue(context);

    }


    public void postCoordinatesToInterSCity(Message cddlMessage, String resourceUUID, String nome_percurso, String eventName){
        String final_uri = baseUri.concat("adaptor/resources/").concat(resourceUUID).concat("/").concat("data/").concat("sb_group_track");
        StringRequest postRequest = new StringRequest(Request.Method.POST, final_uri, response -> {
            System.out.println("response: " + response);
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


                    JSONObject object = new JSONObject();
                    object.put("identificador", nome_percurso);
                    object.put("evento", eventName);
                    object.put("latitude", cddlMessage.getSourceLocationLatitude().toString());
                    object.put("longitude", cddlMessage.getSourceLocationLongitude().toString());
                    object.put("altitude", cddlMessage.getSourceLocationAltitude().toString());
                    object.put("timestamp", getDateIso8601());

                    JSONArray array = new JSONArray();
                    array.put(object);

                    JSONObject data = new JSONObject();

                    data.put("data", array);

                    return data.toString().getBytes(StandardCharsets.UTF_8);


                } catch (JSONException e) {
                    e.printStackTrace();
                }


                return super.getBody();
            }
        };
        queue.add(postRequest);
    }


    public void postNewRouteToInterSCity(String resourceUUID, String nome, String dificuldade, JSONArray latitude_array, JSONArray longitude_array){
        String final_uri = baseUri.concat("adaptor/resources/").concat(resourceUUID).concat("/").concat("data/").concat("sb_group_routes");
        StringRequest postRequest = new StringRequest(Request.Method.POST, final_uri, response -> {
            System.out.println("response: " + response);
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


                    JSONObject object = new JSONObject();
                    object.put("nome", nome);
                    object.put("identificador", UUID.randomUUID().toString());
                    object.put("dificuldade", dificuldade);

                    Log.d("LALALA", latitude_array.toString());
                    Log.d("LALALA", longitude_array.toString());

                    object.put("latitude_array", latitude_array);
                    object.put("longitude_array", longitude_array);
                    object.put("timestamp", getDateIso8601());



                    JSONArray array = new JSONArray();
                    array.put(object);

                    JSONObject data = new JSONObject();

                    data.put("data", array);

                    return data.toString().getBytes(StandardCharsets.UTF_8);


                } catch (JSONException e) {
                    e.printStackTrace();
                }


                return super.getBody();
            }
        };
        queue.add(postRequest);
    }





    public String getDateIso8601(){
        TimeZone tz = TimeZone.getTimeZone("UTC");

        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm'Z'"); // Quoted "Z" to indicate UTC, no timezone offset

        df.setTimeZone(tz);

        return df.format(new Date());
    }


    public void postResumo(String resourceUUID, String routeName, String event, String velMedia, String distance, String duracao, String gain) {
        String final_uri = baseUri.concat("adaptor/resources/").concat(resourceUUID).concat("/").concat("data/").concat("sb_group_routes_performed");
        StringRequest postRequest = new StringRequest(Request.Method.POST, final_uri, response -> {
            System.out.println("response: " + response);
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


                    JSONObject object = new JSONObject();
                    object.put("percurso", routeName);
                    object.put("evento", event);

                    object.put("velocidade_media", velMedia);
                    object.put("distancia_percorrida", distance);
                    object.put("duracao", duracao);
                    object.put("ganho_elevacao", gain);
                    object.put("timestamp", getDateIso8601());



                    JSONArray array = new JSONArray();
                    array.put(object);

                    JSONObject data = new JSONObject();

                    data.put("data", array);

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
