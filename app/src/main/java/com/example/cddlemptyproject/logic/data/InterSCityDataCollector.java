package com.example.cddlemptyproject.logic.data;

import android.content.Context;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.nio.charset.StandardCharsets;

public class InterSCityDataCollector {

    private final String baseUri = "http://cidadesinteligentes.lsdi.ufma.br/";
    private RequestQueue queue;


    public InterSCityDataCollector(Context context){
        this.queue = Volley.newRequestQueue(context);

    }


    public void getLastCoornidatesOfResourceFromInterSCity(String resourceUUID){
        String final_uri = baseUri.concat("collector/resources/").concat(resourceUUID).concat("/").concat("data/").concat("last");
        StringRequest postRequest = new StringRequest(Request.Method.POST, final_uri, response -> {
            try {
                JSONObject jsonResponse = new JSONObject(response); // transforma a response em objeto json


                JSONObject dados = jsonResponse.getJSONArray("resources") // acessa cada um dos elementos ate chegar ao dado que ta la no centro do json
                        .getJSONObject(0)
                        .getJSONObject("capabilities")
                        .getJSONArray("sb_group_track").getJSONObject(0);

                String identificador = dados.getString("identificador");
                String latitude = dados.getString("latitude");
                String longitude = dados.getString("longitude");
                String altitude = dados.getString("altitude");
                String date = dados.getString("date");

                Log.i("INTERSCITY - ID", identificador);
                Log.i("INTERSCITY - LAT", latitude);
                Log.i("INTERSCITY - LON", longitude);
                Log.i("INTERSCITY - ALT", altitude);
                Log.i("INTERSCITY - DATE", date);

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
