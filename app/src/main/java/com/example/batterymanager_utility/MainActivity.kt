package com.example.batterymanager_utility

import android.Manifest
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.os.BatteryManager
import android.os.Bundle
import android.os.Environment
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.content.ContextCompat
import com.example.batterymanager_utility.ui.theme.Batterymanager_utilityTheme
import java.io.File
import java.io.FileOutputStream
import java.lang.StrictMath.abs
import kotlin.math.floor

class MainActivity : ComponentActivity() {

    private lateinit var broadcastReceiver: BroadcastReceiver
    private var lastKnownVoltage : Int = 0 // milivolts
    private var lastKnownLevel : Double = 0.0 // percentage


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Surface(color = MaterialTheme.colors.background) {}
        }

        when { ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
            == PackageManager.PERMISSION_GRANTED -> {

                Log.i("BatteryMgr:onCreate", "permission granted")
                val file = this.createFile()
//                receiverSetup()
                writeToFile(file)

            }
            shouldShowRequestPermissionRationale() -> {
                Toast.makeText(this, "Pls accept", Toast.LENGTH_SHORT).show()
            }
            else -> {
                    // Directly ask for the permission.
                    // The registered ActivityResultCallback gets the result of this request.
                    Log.i("BatteryMgr:onCreate", "requesting permission")
                    requestPermissionLauncher.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE)
            }
        }
    }

    override fun onCreateView(
        parent: View?,
        name: String,
        context: Context,
        attrs: AttributeSet
    ): View? {
        return super.onCreateView(parent, name, context, attrs)
    }


    override fun onDestroy() {
        super.onDestroy()
        this.unregisterReceiver(broadcastReceiver)
    }

    private fun shouldShowRequestPermissionRationale(): Boolean {
        return true
    }

    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) {
            isGranted: Boolean ->
            if (isGranted) {
                Toast.makeText(this, "Permission granted", Toast.LENGTH_SHORT).show()
                val file = this.createFile()
                writeToFile(file)
            } else {
                Toast.makeText(this, "Features unavailable", Toast.LENGTH_SHORT).show()
            }
        }

    private fun createFile(): File {
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
        Toast.makeText(this, "Hello from BatteryManager utility!", Toast.LENGTH_SHORT).show()
        val batteryManager = this.getSystemService(BATTERY_SERVICE) as BatteryManager
        while (true) {
            receiverSetup()

            Thread.sleep(1000)
            val stats = getStats(batteryManager)

            Log.i("BatteryMgr:writeToFile", "writing $stats")
            FileOutputStream(file, true).use {
                it.write("$stats\n".toByteArray())
            }
        }
    }


    private fun receiverSetup() {
        broadcastReceiver = BatteryManagerBroadcastReceiver { intent ->
            this.lastKnownVoltage = intent.getIntExtra(BatteryManager.EXTRA_VOLTAGE, 0)

            val level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1)
            val scale = intent.getIntExtra(BatteryManager.EXTRA_SCALE, -1)
            lastKnownLevel = (level * 100).toDouble() / scale
        }
        val filter = IntentFilter().apply {
            addAction(Intent.ACTION_BATTERY_CHANGED)
        }
        this.registerReceiver(broadcastReceiver, filter)
    }


    private fun getStats(batteryManager: BatteryManager): String {
        val timestamp = System.currentTimeMillis()

        // code from https://github.com/S2-group/batterydrainer/blob/master/app/src/main/java/nl/vu/cs/s2group/batterydrainer/LiveView.kt
        var currentNow = batteryManager.getIntProperty(BatteryManager.BATTERY_PROPERTY_CURRENT_NOW) //Instantaneous battery current in microamperes
        val status = batteryManager.getIntProperty(BatteryManager.BATTERY_PROPERTY_STATUS)

        if(status == BatteryManager.BATTERY_STATUS_DISCHARGING) {   //some models report with inverted sign
            currentNow = -abs(currentNow)
        }

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

        return "$timestamp,$currentNow,$status,$currentAverage,$lastKnownVoltage,$watts,$capacity,$capacityPercentage,$hours,$minutes"
    }


//    fun writeToLog() {
//        Toast.makeText(this, "Hello from BatteryManager utility!", Toast.LENGTH_SHORT).show()
//
//        var batteryManager = this.getSystemService(BATTERY_SERVICE) as BatteryManager
//        var chargingStatus: Boolean
//        var currentNow: Int
//
//        while (true) {
//            chargingStatus = batteryManager.isCharging
//            currentNow = batteryManager.getIntProperty(BatteryManager.BATTERY_PROPERTY_CURRENT_NOW)
//
//            Log.e("BatteryMgr", "chargeStat $chargingStatus")
//            Log.e("BatteryMgr", "currentNow $currentNow")
//
//            Thread.sleep(5000)
//        }
//    }
}

private class BatteryManagerBroadcastReceiver(
    private val onReceiveIntent: (Intent) -> Unit,
) : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        onReceiveIntent(intent)
    }
}


@Composable
fun Greeting(name: String) {
    Text(text = "$name\n :)", color = Color.White,
        style = MaterialTheme.typography.h3)
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    Batterymanager_utilityTheme {
        Greeting("BatteryManager Utility")
    }
}