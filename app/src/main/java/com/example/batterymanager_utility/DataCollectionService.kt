package com.example.batterymanager_utility

import android.app.Notification
import android.app.Service
import android.content.Intent
import android.content.IntentFilter
import android.os.BatteryManager
import android.os.Environment
import android.os.IBinder
import android.os.PowerManager
import android.util.Log
import kotlinx.coroutines.*
import java.io.File
import java.io.FileOutputStream
import kotlin.math.abs
import kotlin.math.floor

class DataCollectionService : Service() {

    private val TAG = "BatteryMgr:DataCollectionService"
    private val NOTIFICATION_TITLE = "Battery Manager"
    private val NOTIFICATION_TEXT = "Collecting data..."

    private lateinit var batteryManager: BatteryManager
    private lateinit var powerManager: PowerManager
    private lateinit var broadcastReceiver: BatteryManagerBroadcastReceiver
    private lateinit var intentFilter: IntentFilter
    private var collectorWorker: Job? = null

    private var data: ArrayList<String> = ArrayList<String>()


    override fun onCreate() {
        super.onCreate()
    }

    @OptIn(DelicateCoroutinesApi::class)
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.i(TAG, "onStartCommand: begin")
        val sampleRate: Int? = intent?.getIntExtra("sampleRate", 1000)
        Log.i(TAG, "onStartCommand: sampleRate => $sampleRate")

        val notification: Notification = Notification.Builder(this, App.CHANNEL_ID)
            .setContentTitle(NOTIFICATION_TITLE)
            .setContentText(NOTIFICATION_TEXT)
            .setSmallIcon(R.drawable.ic_notification)
            .build()
        Log.i(TAG, "onStartCommand: notification done")

        startForeground(1, notification)
        Log.i(TAG, "started foreground")

        receiverSetup()
        registerReceiver(broadcastReceiver, intentFilter)
        Log.i(TAG, "registered receiver")

        this.collectorWorker = CoroutineScope(Dispatchers.IO).launch {
            collectData(sampleRate!!)
        }
//        collectData(sampleRate!!)

        return START_NOT_STICKY
    }

    private fun receiverSetup() {
        Log.i(TAG, "receiverSetup: begin")
        batteryManager = this.getSystemService(BATTERY_SERVICE) as BatteryManager
        powerManager = this.getSystemService(POWER_SERVICE)     as PowerManager

        broadcastReceiver = BatteryManagerBroadcastReceiver()
        intentFilter = IntentFilter().apply {
            addAction(Intent.ACTION_BATTERY_CHANGED)
        }
        Log.i(TAG, "receiverSetup: end")
    }

    private suspend fun collectData(sampleRate: Int) {
        Log.i(TAG, "collectData: begin")
        while (true) {
            val stats = getStats()
            data.add(stats)
            delay(sampleRate.toLong())
        }
    }

    private fun getStats(): String {
        Log.i(TAG, "getStats: begin")
        val timestamp = System.currentTimeMillis()

        // code from https://github.com/S2-group/batterydrainer/blob/master/app/src/main/java/nl/vu/cs/s2group/batterydrainer/LiveView.kt
        var currentNow = batteryManager.getIntProperty(BatteryManager.BATTERY_PROPERTY_CURRENT_NOW) //Instantaneous battery current in microamperes
        val status = batteryManager.getIntProperty(BatteryManager.BATTERY_PROPERTY_STATUS)

        if(status == BatteryManager.BATTERY_STATUS_DISCHARGING) {   //some models report with inverted sign
            currentNow = -abs(currentNow)
        }

        val lastKnownVoltage = broadcastReceiver.getVoltage()

        val currentAverage = batteryManager.getIntProperty(BatteryManager.BATTERY_PROPERTY_CURRENT_AVERAGE) //Average battery current in microamperes
        val watts = if(currentNow >= 0)  0.0 else (lastKnownVoltage.toDouble() / 1000) * (abs(currentNow).toDouble()/1000/1000) //Only negative current means discharging

        val energy   = batteryManager.getIntProperty(BatteryManager.BATTERY_PROPERTY_ENERGY_COUNTER) //Remaining energy in nanowatt-hours
        val capacity = batteryManager.getLongProperty(BatteryManager.BATTERY_PROPERTY_CHARGE_COUNTER) //Remaining battery capacity in microampere-hours
        val capacityPercentage = batteryManager.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY) //Remaining battery capacity as an integer percentage of total capacity

        /*
         * currentAverage always reports 0
         * energy         always reports 0
         * capacityPercentage == lastKnownLevel
         * Usable metrics: currentNow, watts, capacity
         */

        val estimatedLifeTime = abs((capacity.toDouble()/1000)/(currentNow.toDouble()/1000))
        val hours = floor(estimatedLifeTime)
        val minutes = ((estimatedLifeTime - hours)*60)

        return "$timestamp,$currentNow,$status,$currentAverage,$lastKnownVoltage,$watts,$energy,$capacity,$capacityPercentage,$hours,$minutes"
    }

    override fun onDestroy() {
        super.onDestroy()
        collectorWorker?.cancel()
        // create the file
        val file = createFile()
        // write to the file
        writeToFile(file)
        unregisterReceiver(broadcastReceiver)
    }

    private fun createFile() : File {
        // Create a new file and write data to it.
        val file = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS), "BatteryManager.csv")
        Log.i("BatteryMgr:createFile", "creating file called $file")

        val cols = "timestamp(ms),currentNow(microA),chargingStatus,currentAverage(microA),lastKnownVoltage(mV),watts,energy(nanoWh),capacity(microAh),capacityPercentage(%),hours,minutes"
        FileOutputStream(file).use {
            it.write("$cols\n".toByteArray())
        }
        Log.i("BatteryMgr:createFile", "created file called $file")

        return file
    }

    private fun writeToFile(file: File) {
        val stats = data.joinToString("\n")
        FileOutputStream(file, true).use {
            it.write("$stats\n".toByteArray())
        }
    }

    override fun onBind(intent: Intent): IBinder {
        null!!
    }
}
