package com.example.cddlemptyproject.ui.my_groups.my_groups_detail;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.espertech.esper.client.EPServiceProvider;
import com.espertech.esper.client.EPServiceProviderManager;
import com.espertech.esper.client.EPStatement;
import com.example.cddlemptyproject.R;
import com.example.cddlemptyproject.logic.Events.EventoDistancia;
import com.example.cddlemptyproject.logic.Events.EventoGain;
import com.example.cddlemptyproject.logic.Events.EventoVelocidadeInstantanea;
import com.example.cddlemptyproject.logic.data.model.Coordenadas;
import com.example.cddlemptyproject.logic.data.model.RoutesRegistered;
import com.example.cddlemptyproject.logic.processing.DataProcessor;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

public class GroupMemberFragment extends Fragment implements OnMapReadyCallback{

    private GroupMemberViewModel groupMemberViewModel;
    private GoogleMap mMap;
    private Marker marcador;
    private Marker alertMarker;
    private Handler handler = new Handler();
//    private String resource_uuid = "85acec75-1b9f-4997-a7d4-5bfa91ff9233";
    Bundle extras;
    String group_uuid;
    String group_name;
    String group_leader;
    private boolean alert = false;

    private EditText velInst;
    private EditText velMedia;
    private EditText duration;
    private EditText distance;
    private EditText radius;
    private Button addAlert;
    private Button startTrack;
    EPServiceProvider engine;
    private int counter = 0;
    private LatLng previous;
    private Button changeUser;


    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            groupMemberViewModel.loadRoutesOfGroup(group_uuid);
            handler.postDelayed(this,5 * 1000); // loop a cada 5 segundos

        }
    };

    private void checkAlert(LatLng coordenadas) {
        if(alert){
            Double dist = Double.parseDouble(radius.getText().toString());
            Double instant_distance_m = (Double) (DataProcessor.distance(alertMarker.getPosition().latitude, alertMarker.getPosition().longitude,coordenadas.latitude,coordenadas.longitude,"K")*1000);
            Log.i("bike", dist + " " + instant_distance_m);

            if(instant_distance_m < dist){
                Toast.makeText(requireActivity().getApplicationContext(),"O grupo está dentro do raio escolhido", Toast.LENGTH_SHORT).show();
            }

        }

    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        extras = getArguments();
        group_uuid = extras != null ? extras.getString("group_uuid") : null;
        group_name = extras != null ? extras.getString("group_name") : null;
        group_leader = extras != null ? extras.getString("group_leader") : null;


        startEngine();
        // Iniciando a view Model, as requisições não ficam nessa atividade e sem nessa classe GroupMemberViewModel
        groupMemberViewModel = ViewModelProviders.of(this).get(GroupMemberViewModel.class);
        groupMemberViewModel.setVoleyContext(requireActivity().getApplicationContext());


        View root = inflater.inflate(R.layout.fragment_group_member, container, false);
        velInst = root.findViewById(R.id.velocidade_inst);
        velMedia = root.findViewById(R.id.velocidade_media);
        distance = root.findViewById(R.id.distancia_percorrida);
        duration = root.findViewById(R.id.duracao);
        addAlert = root.findViewById(R.id.addAlert);
        startTrack = root.findViewById(R.id.track);
        radius = root.findViewById(R.id.radius);



        addAlert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(radius.getText().toString().isEmpty()){
                    Toast.makeText(requireActivity().getApplicationContext(),"Defina um valor para o raio", Toast.LENGTH_SHORT).show();
                }else{
                    if(alert){
                        alert = false;
                        groupMemberViewModel.loadTextButton("Adicionar Alerta");
                    }else{
                        alert = true;
                        groupMemberViewModel.loadTextButton("Remover Alerta");


                    }
                }

            }
        });

        startTrack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startLoop();

            }
        });



        // Colocando o mapa
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.member_map);
        mapFragment.getMapAsync(this);

        // Mudando o titulo da atividade
        requireActivity().setTitle("Você é Membro do Grupo");


        // aqui o marcador atualiza cada vez que o valor da variavel coordenadasAtual dentro da view model atualiza
        groupMemberViewModel.getCoordenadaAtual().observe(getViewLifecycleOwner(), new Observer<LatLng>() {
            @Override
            public void onChanged(LatLng coordenadas) {
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(coordenadas,15));
                marcador.setPosition(coordenadas);

                if(counter == 0){
                    previous = coordenadas;
                }else{
                    Double instant_distance_m = (Double) (DataProcessor.distance(previous.latitude,previous.longitude,coordenadas.latitude,coordenadas.longitude,"K")*1000);

                    if(instant_distance_m > 200){
                        Log.i("Outlier", "outlier detected");
                        // tratar o erro com valor medio
                        engine.getEPRuntime().sendEvent(new EventoDistancia(30.0));

                    }else{
                        // enviar a distrancia
                        engine.getEPRuntime().sendEvent(new EventoDistancia(instant_distance_m));


                        previous = coordenadas;
                    }

                }
                checkAlert(coordenadas);
                counter++;

            }
        });

        groupMemberViewModel.getTextButton().observe(getViewLifecycleOwner(), t->{
            addAlert.setText(t);
        });

        groupMemberViewModel.getVi().observe(getViewLifecycleOwner(), vi ->{
            velInst.setText(vi);
        });

        groupMemberViewModel.getTempo().observe(getViewLifecycleOwner(), t ->{
            duration.setText(t);
        });
        groupMemberViewModel.getVel().observe(getViewLifecycleOwner(), t ->{
            velMedia.setText(t);
        });
        groupMemberViewModel.getDistancia().observe(getViewLifecycleOwner(), t ->{
            distance.setText(t);
        });


        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Definindo posição inicial do mapa: essa coordenada é la na litoranea proximo a estatua dos pescadores
        LatLng start = new LatLng(-2.4902906, -44.296496);

        marcador = mMap.addMarker(new MarkerOptions().position(start).title("Grupo"));
        alertMarker = mMap.addMarker(new MarkerOptions().position(start).title("Alerta"));

        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                alertMarker.setPosition(latLng);
            }
        });
    }



    private void startLoop() {
        handler.postDelayed(runnable, 5*1000);
    }

    private void endLoop(){
        handler.removeCallbacks(runnable); //stop handler when activity not visible
    }

    @Override
    public void onDestroy() {
        endLoop();
        super.onDestroy();
    }

    private void startEngine(){
        engine = EPServiceProviderManager.getDefaultProvider();

        engine.getEPAdministrator().getConfiguration().addEventType(EventoDistancia.class);
        engine.getEPAdministrator().getConfiguration().addEventType(EventoVelocidadeInstantanea.class);




        String rule1 = "select (distancia/"+ 5 +") as velocidadeInstantanea, count(*)*" + 5 + "as tempoDecorrido FROM EventoDistancia";
        EPStatement statement = engine.getEPAdministrator().createEPL(rule1);
        statement.addListener((newData, oldData) -> {

            groupMemberViewModel
                    .loadTempoDecorrido(newData[0].get("tempoDecorrido").toString()); // Envia os valores para a INTERFACE

            groupMemberViewModel
                    .loadVelocidadeInstantanea(newData[0].get("velocidadeInstantanea").toString()); // Envia os valores para a INTERFACE

            engine.getEPRuntime()
                    .sendEvent(new EventoVelocidadeInstantanea( (Double) newData[0].get("velocidadeInstantanea") ));

        });

        String rule2 = "select avg(velocidadeInstantanea) as velocidadeMedia,  ((sum(velocidadeInstantanea)/count(*))*count(*)*" +5+ ") as distanciaPercorrida from EventoVelocidadeInstantanea\n";
        EPStatement statement2 = engine.getEPAdministrator().createEPL(rule2);
        statement2.addListener( (newData, oldData) -> {
            groupMemberViewModel.loadDistanciaPercorrida(newData[0].get("distanciaPercorrida").toString());
            groupMemberViewModel.loadVelocidadeMedia(newData[0].get("velocidadeMedia").toString());
        });




    }

}
