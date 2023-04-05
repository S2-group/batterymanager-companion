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

class DataCollectionService : Service() {

    private val TAG = "BatteryMgr:DataCollectionService"
    private val NOTIFICATION_TITLE = "Battery Manager"
    private val NOTIFICATION_TEXT = "Collecting data..."
    private var collectorWorker: Job? = null

    private lateinit var collector: DataCollector
    private lateinit var dataFields: ArrayList<String>
    private var data: ArrayList<String> = ArrayList()


    override fun onCreate() {
        super.onCreate()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.i(TAG, "onStartCommand: begin")
        val sampleRate: Int? = intent?.getIntExtra("sampleRate", 1000)
        Log.i(TAG, "onStartCommand: sampleRate => $sampleRate")
        val rawFields: String? = intent?.getStringExtra("dataFields")
        Log.i(TAG, "onStartCommand: rawFields => $rawFields")
        dataFields = rawFields?.split(",") as ArrayList<String>
        dataFields.add(0,"Timestamp")

        val notification: Notification = Notification.Builder(this, App.CHANNEL_ID)
            .setContentTitle(NOTIFICATION_TITLE)
            .setContentText(NOTIFICATION_TEXT)
            .setSmallIcon(R.drawable.ic_notification)
            .build()
        Log.i(TAG, "onStartCommand: notification done")

        startForeground(1, notification)
        Log.i(TAG, "started foreground")

        collector = DataCollector(this, dataFields)

        this.collectorWorker = CoroutineScope(Dispatchers.IO).launch {
            collectData(sampleRate!!)
        }
        return START_NOT_STICKY
    }

    private suspend fun collectData(sampleRate: Int) {
        Log.i(TAG, "collectData: begin")
        while (true) {
            val stats = collector.getData()
            Log.i(TAG, "stats => $stats")
            data.add(stats)
            delay(sampleRate.toLong())
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        collectorWorker?.cancel()
        // create the file
        val file = createFile()
        // write to the file
        writeToFile(file)
    }

    private fun createFile() : File {
        // Create a new file and write data to it.
        val file = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS), "BatteryManager.csv")
        Log.i("BatteryMgr:createFile", "creating file called $file")

        val cols = collector.getDataPoints().joinToString(",")
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
