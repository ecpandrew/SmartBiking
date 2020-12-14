package com.example.cddlemptyproject.logic.services


import android.app.*
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.net.wifi.WifiManager
import android.os.Build
import android.os.IBinder
import android.os.PowerManager
import android.provider.Settings
import android.widget.Toast
import br.ufma.lsdi.cddl.CDDL
import br.ufma.lsdi.cddl.Connection
import br.ufma.lsdi.cddl.ConnectionFactory
import br.ufma.lsdi.cddl.listeners.IConnectionListener
import br.ufma.lsdi.cddl.message.Message
import br.ufma.lsdi.cddl.pubsub.Publisher
import br.ufma.lsdi.cddl.pubsub.PublisherFactory
import br.ufma.lsdi.cddl.pubsub.Subscriber
import com.example.cddlemptyproject.*

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
//import leakcanary.AppWatcher
//import leakcanary.ObjectWatcher

class EndlessService : Service() {

    //    private val scheduler: ScheduledExecutorService = Executors.newScheduledThreadPool(1)


    private var wakeLock: PowerManager.WakeLock? = null
    private var wakeLock2: WifiManager.WifiLock? = null


    private var isServiceStarted = false


    /* IoT middleware responsible to manage bluetooth scanning */
    private  var cddl: CDDL? = null
    private  var eventSub: Subscriber? = null

    var conLocal: Connection? = null
    var conRemota: Connection? = null



    // Memory leak prevention
    var publisher : Publisher? = null
    var rendezvousMessage : Message? = null
//    var mouuid: String? = null
//    var emptyMacAdress : String? = null


    override fun onBind(intent: Intent): IBinder? {
        log("Some component want to bind with the service")
        // We don't provide binding, so return null
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        log("onStartCommand executed with startId: $startId")


        val intentEndless = Intent(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS, Uri.parse("package:$packageName"))
        intentEndless.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intentEndless)
        if (intent != null) {
//            val action = intent.action
            val bundleAction : String? = intent.getStringExtra("action")
            when (bundleAction) {
                Actions.START.name -> startService()
                Actions.STOP.name -> stopService()
                else -> log("This should never happen. No action in the received intent")
            }

//
        } else {
            log(
                    "with a null intent. It has been probably restarted by the system."
            )
        }
        // by returning this we make sure the service is restarted if the system kills the service
        return START_REDELIVER_INTENT
    }

    override fun onCreate() {
        super.onCreate()

        log("The service has been created".toUpperCase())
        val notification = createNotification()
        startForeground(1, notification)

    }



    private fun startService() {
        if (isServiceStarted) return
        log("Starting the foreground service task")
        Toast.makeText(this, "Service starting its task", Toast.LENGTH_SHORT).show()
        isServiceStarted = true
        setServiceState(this, ServiceState.STARTED)

        wakeLock =
                (getSystemService(Context.POWER_SERVICE) as PowerManager).run {
                    newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "EndlessService::lock").apply {
                        acquire()
                    }
                }
        wakeLock2 =
                (getSystemService(Context.WIFI_SERVICE) as WifiManager).run {
                    createWifiLock(WifiManager.WIFI_MODE_FULL_HIGH_PERF, "EndlessService::lock2").apply {
                        acquire()
                    }
                }
        init()


        val m : Message = Message().also {
            it.serviceName="coordinates"


        }

        GlobalScope.launch(Dispatchers.IO) {
            while (isServiceStarted) {
                Thread.sleep(1200)
                publisher!!.publish(m)

            }
            log("End of the loop for the service")
            stop()
        }
    }

    private fun stop() {
        try {
            cddl!!.stopAllCommunicationTechnologies()
            cddl!!.stopService()
            conRemota!!.disconnect()
            stopService()
        }catch (e: Exception){
            log("Error on stopping cddl and services")
        }
    }

    private fun init() {

        configConRemota()
        initCDDL()
//        initSubscriberLocal()
        initPublisherRemote()
    }
    private fun initPublisherRemote(){
        publisher = PublisherFactory.createPublisher().also {
            it.addConnection(conRemota)
        }

    }

    private fun configConLocal(){
        val host = CDDL.startSecureMicroBroker(applicationContext, true);


//        val host = CDDL.startMicroBroker();
        conLocal = ConnectionFactory.createConnection().also {
            it.clientId = "andre";
            it.host = host;
            it.addConnectionListener(connectionListenerLocal);
//            it.connect();
            it.secureConnect(applicationContext)
        }

    }
    private fun removeConLocal(){
        if (conLocal != null) {
            conLocal!!.disconnect()
        }
        if(conRemota != null) {
            conRemota!!.disconnect()
        }
        CDDL.stopMicroBroker()
    }

    private fun configConRemota(){
//        val host = "192.168.15.56";
        val host = "192.168.15.114";
        conRemota = ConnectionFactory.createConnection().also {
            it.clientId = "mobile"
            it.host = host;
            it.addConnectionListener(connectionListenerRemota);
//            it.connect();
            it.secureConnect(applicationContext);
        }

    }


    private val connectionListenerLocal: IConnectionListener = object : IConnectionListener{
        val con : String = "local"
        override fun onConnectionEstablished() {
            log("Conexão $con estabelecida")
        }

        override fun onConnectionEstablishmentFailed() {
            log("Conexão $con falhou")

        }

        override fun onConnectionLost() {
            log("Conexão $con perdida")

        }

        override fun onDisconnectedNormally() {
            log("Conexão $con desconectada normalmente")

        }
    }
    private val connectionListenerRemota: IConnectionListener = object : IConnectionListener{
        val con : String = "remota"
        override fun onConnectionEstablished() {
            log("Conexão $con estabelecida")
        }

        override fun onConnectionEstablishmentFailed() {
            log("Conexão $con falhou")

        }

        override fun onConnectionLost() {
            log("Conexão $con perdida")

        }

        override fun onDisconnectedNormally() {
            log("Conexão $con desconectada normalmente")

        }
    }













    private fun stopCDDL(){
        cddl = CDDL.getInstance();
        cddl!!.stopLocationSensor()
        cddl!!.stopService()

    }

    private fun initCDDL() {
        try {
            cddl = CDDL.getInstance();
            if(cddl != null) {
                cddl!!.connection = conRemota;
                cddl!!.context = this;
                cddl!!.startService();
                cddl!!.startLocationSensor();
//                cddl!!.setQoS(TimeBasedFilterQoS());
                println("---------------- CDDL iniciado ------------")
            }

        }catch (e : Exception){
            log("initCDDL() Failed")
            e.message?.let { log(it) }
        }


    }




    private fun stopService() {
        log("Stopping the foreground service")
        Toast.makeText(this, "Service stopping", Toast.LENGTH_SHORT).show()
        try {
            wakeLock?.let {
                if (it.isHeld) {
                    it.release()
                }
            }
            wakeLock2?.let {
                if (it.isHeld) {
                    it.release()
                }
            }

//            wifiLock?.let{
//                if(it.isHeld){
//                    it.release()
//                }
//            }

//            scheduler.shutdown()
            stop()
            stopForeground(true)
            stopSelf()
        } catch (e: Exception) {
        }
        isServiceStarted = false
        setServiceState(this, ServiceState.STOPPED)
    }



    private fun createNotification(): Notification {
        val notificationChannelId = "ENDLESS SERVICE CHANNEL"

        // depending on the Android API that we're dealing with we will have
        // to use a specific method to create the notification
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            val channel = NotificationChannel(
                    notificationChannelId,
                    "Endless Service notifications channel",
                    NotificationManager.IMPORTANCE_HIGH
            ).let {
                it.description = "Endless Service channel"
                it.enableLights(true)
                it.lightColor = Color.RED
                it.enableVibration(true)
                it.vibrationPattern = longArrayOf(100, 200, 300, 400, 500, 400, 300, 200, 400)
                it
            }
            notificationManager.createNotificationChannel(channel)
        }

        val pendingIntent: PendingIntent = Intent(this, TrackActivity::class.java).let { notificationIntent ->
            PendingIntent.getActivity(this, 0, notificationIntent, 0)
        }

        val builder: Notification.Builder = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) Notification.Builder(
                this,
                notificationChannelId
        ) else Notification.Builder(this)

        return builder
                .setContentTitle("Endless Service")
                .setContentText("This is your favorite endless service working")
                .setContentIntent(pendingIntent)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setTicker("Ticker text")
                .setPriority(Notification.PRIORITY_HIGH) // for under android 26 compatibility
                .build()
    }



}


