package com.example.cddlemptyproject;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModelProvider;

import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


import com.espertech.esper.client.EPServiceProvider;
import com.espertech.esper.client.EPServiceProviderManager;
import com.espertech.esper.client.EPStatement;
import com.example.cddlemptyproject.logic.Events.EventoGain;
import com.example.cddlemptyproject.logic.data.InterSCityDataPoster;
import com.example.cddlemptyproject.logic.data.model.RoutesRegistered;
import com.example.cddlemptyproject.logic.models.RouteModel;
import com.example.cddlemptyproject.logic.processing.DataProcessor;
import com.example.cddlemptyproject.logic.Events.EventoDistancia;
import com.example.cddlemptyproject.logic.Events.EventoVelocidadeInstantanea;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;


import br.ufma.lsdi.cddl.CDDL;
import br.ufma.lsdi.cddl.ConnectionFactory;
import br.ufma.lsdi.cddl.listeners.IConnectionListener;
import br.ufma.lsdi.cddl.listeners.ISubscriberListener;
import br.ufma.lsdi.cddl.message.Message;
import br.ufma.lsdi.cddl.message.SensorDataMessage;
import br.ufma.lsdi.cddl.network.ConnectionImpl;
import br.ufma.lsdi.cddl.pubsub.Publisher;
import br.ufma.lsdi.cddl.pubsub.PublisherFactory;
import br.ufma.lsdi.cddl.pubsub.Subscriber;
import br.ufma.lsdi.cddl.pubsub.SubscriberFactory;

public class TrackActivity extends AppCompatActivity implements OnMapReadyCallback {

    private CDDL cddl;
    private View stopButton;
    private TextView messageTextView;
    private ConnectionImpl conLocal;
    private Handler handler = new Handler();
    private RouteModel route = new RouteModel();
    private InterSCityDataPoster poster;
    private GoogleMap mMap;

    private String group_uuid;
    private String event_name;
    private String nome_percurso;

    private EditText velInst;
    private EditText velMedia;
    private EditText duration;
    private EditText distance;
    private EditText gain;

    private Marker marker;
    private LatLng coords;
    EPServiceProvider engine;
    private TrackViewModel trackViewModel;
    private Publisher internalPublisher;
    private int counter = 0;
    private int frequency = 5;

    private MutableLiveData<LatLng> coord = new MutableLiveData<LatLng>();

    private Runnable runnable = new Runnable() {
        @Override
        public void run() {

            publishMessageLocal();

            handler.postDelayed(this,frequency * 1000);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        Bundle extras = getIntent().getExtras();

        group_uuid = extras != null ? extras.getString("group_uuid") : null;
        event_name = extras != null ? extras.getString("event") : null;

        nome_percurso = extras != null ? extras.getString("nome_percurso") : null;
        trackViewModel = new ViewModelProvider(this).get(TrackViewModel.class);

        setContentView(R.layout.track_activity);
        setPermissions();
        setViews();

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.leader_map);
        mapFragment.getMapAsync(this);

        trackViewModel.getCoords().observe(this, c ->{
            if(!c.equals(null)){
                refreshMap(c);
            }
        });

        trackViewModel.getVi().observe(this, vi ->{
            velInst.setText(vi);
        });

        trackViewModel.getTempo().observe(this, t ->{
            duration.setText(t);
        });
        trackViewModel.getVel().observe(this, t ->{
            velMedia.setText(t);
        });
        trackViewModel.getDistancia().observe(this, t ->{
            distance.setText(t);
        });
        trackViewModel.getEleGain().observe(this, t ->{
            gain.setText(t);
        });



        setTitle("Enviando percurso!");


        poster = new InterSCityDataPoster(getApplicationContext());

        configConLocal();
        initCDDL();
        subscribeMessage();

        startEngine();

//        initInternalPublisher();

        startLoop();

//        startMonitor();

        stopButton.setOnClickListener(clickListener);

    }

    private void startEngine(){
        engine = EPServiceProviderManager.getDefaultProvider();

        engine.getEPAdministrator().getConfiguration().addEventType(EventoDistancia.class);
        engine.getEPAdministrator().getConfiguration().addEventType(EventoVelocidadeInstantanea.class);
        engine.getEPAdministrator().getConfiguration().addEventType(EventoGain.class);




        String rule1 = "select (distancia/"+ frequency +") as velocidadeInstantanea, count(*)*" + frequency + "as tempoDecorrido FROM EventoDistancia";
        EPStatement statement = engine.getEPAdministrator().createEPL(rule1);
        statement.addListener((newData, oldData) -> {
            trackViewModel
                    .loadTempoDecorrido(newData[0].get("tempoDecorrido").toString()); // Envia os valores para a INTERFACE

            trackViewModel
                    .loadVelocidadeInstantanea(newData[0].get("velocidadeInstantanea").toString()); // Envia os valores para a INTERFACE

            engine.getEPRuntime()
                    .sendEvent(new EventoVelocidadeInstantanea( (Double) newData[0].get("velocidadeInstantanea") ));

        });

        String rule2 = "select avg(velocidadeInstantanea) as velocidadeMedia,  ((sum(velocidadeInstantanea)/count(*))*count(*)*" +frequency+ ") as distanciaPercorrida from EventoVelocidadeInstantanea\n";
        EPStatement statement2 = engine.getEPAdministrator().createEPL(rule2);
        statement2.addListener( (newData, oldData) -> {
            trackViewModel.loadDistanciaPercorrida(newData[0].get("distanciaPercorrida").toString());
            trackViewModel.loadVelocidadeMedia(newData[0].get("velocidadeMedia").toString());
        });

        String rule3 = "select sum(ganho) as cumulativeGain from EventoGain\n";
        EPStatement statement3 = engine.getEPAdministrator().createEPL(rule3);
        statement3.addListener( (newData, oldData) -> {
            trackViewModel.loadElevationGain(newData[0].get("eventoGain").toString());
        });


    }

    private void startLoop() {
        handler.postDelayed(runnable, frequency*1000);
    }

    private void endLoop(){
        handler.removeCallbacks(runnable); //stop handler when activity not visible
    }



    private void configConLocal() {
//        String host = CDDL.startSecureMicroBroker(this, true);
        String host = CDDL.startMicroBroker();
        conLocal = ConnectionFactory.createConnection();
        conLocal.setClientId("mobile");
        conLocal.setHost(host);
//        conLocal.addConnectionListener(connectionListener);
        conLocal.connect();
//        conLocal.secureConnect(this);
    }


    private void initCDDL() {
        cddl = CDDL.getInstance();
        cddl.setConnection(conLocal);
        cddl.setContext(this);
        cddl.startService();
        cddl.startLocationSensor();
    }

    @Override
    protected void onDestroy() {
        cddl.stopLocationSensor();
        cddl.stopAllCommunicationTechnologies();
        cddl.stopService();
        conLocal.disconnect();
        CDDL.stopMicroBroker();
        super.onDestroy();
    }

    private void subscribeMessage() {
        Subscriber sub = SubscriberFactory.createSubscriber();
        sub.addConnection(conLocal);
        sub.subscribeServiceByName("coordinates");

        sub.setSubscriberListener(new ISubscriberListener() {
            @Override
            public void onMessageArrived(Message message) {

                if (message.getServiceName().equals("coordinates")) {

                    route.addTimestamp(message.getSourceLocationTimestamp());
                    route.addLatitude(message.getSourceLocationLatitude());
                    route.addLongitude(message.getSourceLocationLongitude());
                    route.addAltitude(message.getSourceLocationAltitude());

                    poster.postCoordinatesToInterSCity(message, group_uuid, nome_percurso, event_name);
                    trackViewModel.loadCoords(new LatLng(message.getSourceLocationLatitude(), message.getSourceLocationLongitude()));


                    int len = route.getSize();

                    if(len>1){
                        Double lat1 = route.getLatitudeAtIndex(len-2);
                        Double lon1 = route.getLongitudeAtIndex(len-2);
                        Double alt1 = route.getAltitudeAtIndex(len-2);

                        Double lat2 = route.getLatitudeAtIndex(len-1);
                        Double lon2 = route.getLongitudeAtIndex(len-1);
                        Double alt2 = route.getAltitudeAtIndex(len-1);

                        Double instant_distance_m = (Double) (DataProcessor.distance(lat1,lon1,lat2,lon2,"K")*1000);
                        if(instant_distance_m < 200){ // Por algum motivo essa conta eventualmente estoura e o valor da distância dá por volta  de 9105288 metros kkkk. Então é necessário descartar esse outlier absurdo.
                            publishDistanceToCEP(instant_distance_m); // os valores vem na faixa de 50 a 60 metros, por segurança coloquei 200 como limite.
                            Log.d("gain", alt2-alt1 + " de ganho");

                            if(alt1 < alt2){
                                publishElevationGainToCEP(alt2-alt1);
                            }

                            Log.d("bike", String.valueOf(instant_distance_m) + " essa distancia foi percorrida em 5 segundos");

                        }else{
                            route.clearAll(); // apagando os registros por garantia.
                            counter= 0;
                            publishDistanceToCEP(60.0); //para não perder o calculo total por conta do descarte do outlier, eu computo o valor médio quando tenho que descartar algum dado
                            Log.d("Outlier", String.valueOf(instant_distance_m) + " essa distancia percorrida em 5 segundos foi descartada pois é absurda");
                        }
//                        int instant_timestamp_sec = (int) ((route.getTimestampAtIndex(counter-1) - route.getTimestampAtIndex(counter-2))/1000);

//                        float instant_vel_ms = instant_distance_m/instant_timestamp_sec;

//                        publishInstantSpeed(instant_distance_m);

                    }
                    counter++;




                }else{
                    Log.d("_MAIN other", message.toString());
                }
            }
        });

    }


    private void publishMessageLocal() {
        Publisher publisher = PublisherFactory.createPublisher();
        publisher.addConnection(conLocal);
        Message message = new Message();
        message.setServiceName("coordinates");
        message.setServiceByteArray("no value");
        publisher.publish(message);
    }





    private void publishDistanceToCEP(Double d){
        engine.getEPRuntime().sendEvent(new EventoDistancia(d));
    }

    private void publishElevationGainToCEP(Double d) {
        engine.getEPRuntime().sendEvent(new EventoGain(d));
    }



    private void setViews() {
        stopButton = findViewById(R.id.stopButton);
        velInst = findViewById(R.id.velocidade_inst);
        velMedia = findViewById(R.id.velocidade_media);
        distance = findViewById(R.id.distancia_percorrida);
        duration = findViewById(R.id.duracao);
        gain = findViewById(R.id.cumulative_gain);
    }

    private View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            endLoop();

            String vel_media = velMedia.getText().toString();
            String distancia = distance.getText().toString();
            String duracao = duration.getText().toString();
            String ganho = gain.getText().toString();
            InterSCityDataPoster p = new InterSCityDataPoster(getApplicationContext());
            p.postResumo(group_uuid, nome_percurso, event_name, vel_media,distancia,duracao, ganho);
            finish();
        }

    };


    private void setPermissions() {

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        }

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera

        marker = mMap.addMarker(new MarkerOptions().position(new LatLng(-2.4902906, -44.296496)).title("Vc esta aqui!"));


    }

    private void refreshMap(LatLng coord){
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(coord, 15));
        marker.setPosition(coord);
    }
}

