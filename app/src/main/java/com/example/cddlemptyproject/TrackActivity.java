package com.example.cddlemptyproject;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Bundle;
import androidx.core.app.ActivityCompat;

import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.TextView;


import com.espertech.esper.client.EPServiceProvider;
import com.espertech.esper.client.EPServiceProviderManager;
import com.espertech.esper.client.EPStatement;
import com.example.cddlemptyproject.logic.data.InterSCityDataPoster;
import com.example.cddlemptyproject.logic.models.RouteModel;
import com.example.cddlemptyproject.logic.processing.DataProcessor;
import com.example.cddlemptyproject.logic.Events.EventoDistancia;
import com.example.cddlemptyproject.logic.Events.EventoVelocidadeInstantanea;


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

public class TrackActivity extends Activity {

    private CDDL cddl;
    private View stopButton;
    private TextView messageTextView;
    private ConnectionImpl conLocal;
    private Handler handler = new Handler();
    private RouteModel route = new RouteModel("andré", "percurso X");
    private InterSCityDataPoster poster;

    EPServiceProvider engine;

    private Publisher internalPublisher;
    private int counter = 0;


    private Runnable runnable = new Runnable() {
        @Override
        public void run() {

            publishMessageLocal();

            handler.postDelayed(this,5 * 1000);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.track_activity);
        setPermissions();
        setViews();
        poster = new InterSCityDataPoster(getApplicationContext());

        configConLocal();
        initCDDL();
        subscribeMessage();

        startEngine();

//        initInternalPublisher();

        startLoop();

        startMonitor();

        stopButton.setOnClickListener(clickListener);

    }

    private void startEngine(){
        engine = EPServiceProviderManager.getDefaultProvider();
        engine.getEPAdministrator().getConfiguration().addEventType(EventoDistancia.class);
        engine.getEPAdministrator().getConfiguration().addEventType(EventoVelocidadeInstantanea.class);

        String rule1 = "select (distancia/10) as velocidadeInstantanea FROM EventoDistancia";
        EPStatement statement = engine.getEPAdministrator().createEPL(rule1);
        statement.addListener((newData, oldData) -> {
            engine.getEPRuntime().sendEvent(new EventoVelocidadeInstantanea( (Double) newData[0].get("velocidadeInstantanea")));

        });

        String rule2 = "select (sum(velocidadeInstantanea)/count(*)) as velocidadeMedia, count(*)*10 as tempoDecorrido, ((sum(velocidadeInstantanea)/count(*))*count(*)*10) as distanciaPercorrida from EventoVelocidadeInstantanea\n";
        EPStatement statement2 = engine.getEPAdministrator().createEPL(rule2);
        statement2.addListener( (newData, oldData) -> {
            Log.d("monitor", "rule2: "+ newData[0].get("velocidadeMedia") +" tempo decorrido (s): "+ newData[0].get("tempoDecorrido") +" distancia percorrida: "+ newData[0].get("distanciaPercorrida"));

        });


    }
    private void startMonitor() {
//        Subscriber sub = SubscriberFactory.createSubscriber();
//        sub.addConnection(conLocal);
//        sub.subscribeServiceByName("distance_meters");
//        sub.setSubscriberListener(new ISubscriberListener() {
//            @Override
//            public void onMessageArrived(Message message) {
//            }
//        });
//
//        Monitor monitor = sub.getMonitor();
////        cast(serviceValue[0],double)/10 as velocidade
////        String rule = "INSERT INTO FluxoVelocidadeInstantanea SELECT *, cast(serviceValue[0],double)/10 as velocidade FROM SensorDataMessage WHERE serviceName  =\"distance_meters\"";
////
////        String rule = "INSERT INTO FluxoVelocidadeInstantanea SELECT * FROM SensorDataMessage WHERE serviceName  =\"distance_meters\"";
//        String rule = "INSERT INTO FluxoVelocidadeInstantanea SELECT cast(serviceValue[0],double)/10 as velocidade FROM SensorDataMessage WHERE serviceName  =\"distance_meters\"";
//
//
//        monitor.addRule(rule, new IMonitorListener() {
//            @Override
//            public void onEvent(Message message) {
//
//                Log.d("monitor", "onEvent: ."+message.toString());
//            }
//        });
//
////        monitor.addRule("update istream FluxoVelocidadeInstantanea set accyu=(cast(serviceValue[0],double)/10)", new IMonitorListener() {
////            @Override
////            public void onEvent(Message message) {
////
////                Log.d("monitor2", "onEvent: "+((BikeMessage) message).toString());
////            }
////        });
//
////
////        monitor.addRule(rule, new IMonitorListener() {
////            @Override
////            public void onEvent(Message message) {
////
////
//////                Log.d("monitor", "onEvent: ."+message.toString());
////            }
////        });
////
////
////
//        monitor.addRule("SELECT velocidade FROM FluxoVelocidadeInstantanea", new IMonitorListener() {
//            @Override
//            public void onEvent(Message message) {
//                Log.d("monitor2", "onEvent: ."+message.toString());
//            }
//        });
//
//
//
//
//
////        monitor.addRule("SELECT*FROM SensorDataMessage WHERE serviceName  =\"instant_speed\" AND cast(serviceValue[0],double)/10>0", new IMonitorListener() {
////            @Override
////            public void onEvent(Message message) {
////                Log.d("monitor", "onEvent: ."+message.toString());
////            }
////        });

    }

    private void startLoop() {
        handler.postDelayed(runnable, 10*1000);
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
        conLocal.addConnectionListener(connectionListener);
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
                    counter++;

                    if(route.getSize()>=2){

                        Double lat1 = route.getLatitudeAtIndex(counter-2);
                        Double lon1 = route.getLongitudeAtIndex(counter-2);
                        Double alt1 = route.getAltitudeAtIndex(counter-2);

                        Double lat2 = route.getLatitudeAtIndex(counter-1);
                        Double lon2 = route.getLongitudeAtIndex(counter-1);
                        Double alt2 = route.getAltitudeAtIndex(counter-1);

                        Double instant_distance_m = (Double) (DataProcessor.distance(lat1,lon1,lat2,lon2,"K")*1000);

//                        int instant_timestamp_sec = (int) ((route.getTimestampAtIndex(counter-1) - route.getTimestampAtIndex(counter-2))/1000);

//                        float instant_vel_ms = instant_distance_m/instant_timestamp_sec;

//                        publishInstantSpeed(instant_distance_m);
                          publishToCEP(instant_distance_m);
                        Log.d("bike", String.valueOf(instant_distance_m));

                    }


//                    poster.postCoordinatesToInterSCity(message, "f0d3126e-edea-4bf2-b79b-834f9afae7fe");

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

    private void initInternalPublisher() {
        internalPublisher = PublisherFactory.createPublisher();
        internalPublisher.addConnection(conLocal);


    }

    private void publishInstantSpeed(Float d){
        SensorDataMessage message = new SensorDataMessage();
        message.setServiceName("distance_meters");
        message.setServiceByteArray(d);
        message.setServiceValue(d);
        internalPublisher.publish(message);

    }

    private void publishToCEP(Double d){
        engine.getEPRuntime().sendEvent(new EventoDistancia(d));
    }



    private void setViews() {
        stopButton = findViewById(R.id.stopButton);
        messageTextView = (TextView) findViewById(R.id.messageTexView);
    }

    private View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            endLoop();
            Log.d("bike", route.toString());

        }

    };

    private IConnectionListener connectionListener = new IConnectionListener() {
        @Override
        public void onConnectionEstablished() {
            messageTextView.setText("Conexão estabelecida.");
        }

        @Override
        public void onConnectionEstablishmentFailed() {
            messageTextView.setText("Falha na conexão.");
        }

        @Override
        public void onConnectionLost() {
            messageTextView.setText("Conexão perdida.");
        }

        @Override
        public void onDisconnectedNormally() {
            messageTextView.setText("Uma disconexão normal ocorreu.");
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

}

