package com.example.batterymanager_utility

import android.content.Intent
import android.content.IntentFilter
import android.os.BatteryManager
import android.os.Bundle
import android.os.Environment
import android.os.PowerManager
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.example.batterymanager_utility.ui.theme.Batterymanager_utilityTheme
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream
import java.lang.StrictMath.abs
import kotlin.math.floor

class MainActivity : ComponentActivity() {

    private lateinit var batteryManager: BatteryManager
    private lateinit var powerManager: PowerManager
    private lateinit var broadcastReceiver: BatteryManagerBroadcastReceiver
    private lateinit var intentFilter: IntentFilter

    private lateinit var file: File

    @OptIn(DelicateCoroutinesApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Surface(color = MaterialTheme.colors.background) {}
        }

        this.createFile()
        receiverSetup()
        registerReceiver(broadcastReceiver, intentFilter)
        GlobalScope.launch { writeToFile() }

    }

    private fun createFile() {
        // Create a new file and write data to it.
        file = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS), "BatteryManager.csv")
        Log.i("BatteryMgr:createFile", "creating file called $file")

        val cols = "timestamp(ms),currentNow(microA),chargingStatus,currentAverage(microA),lastKnownVoltage(mV),watts,energy(nanoWh),capacity(microAh),capacityPercentage(%),hours,minutes"
        FileOutputStream(file).use {
            it.write("$cols\n".toByteArray())
        }
        Log.i("BatteryMgr:createFile", "created file called $file")
    }

    private fun receiverSetup() {
        batteryManager = this.getSystemService(BATTERY_SERVICE) as BatteryManager
        powerManager = this.getSystemService(POWER_SERVICE)     as PowerManager

        broadcastReceiver = BatteryManagerBroadcastReceiver()
        intentFilter = IntentFilter().apply {
            addAction(Intent.ACTION_BATTERY_CHANGED)
        }
    }

    private fun getStats(): String {
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

    private fun writeToFile() {
//        Toast.makeText(this, "Hello from BatteryManager utility!", Toast.LENGTH_SHORT).show()
        var i = 0
        while (true) {
            Thread.sleep(1000)
            val stats = getStats()

            if (i == 10) {
//                receiverSetup()
                Log.i("BatteryMgr:writeToFile", "wawawawawa")
            }
            Log.i("BatteryMgr:writeToFile", "writing $stats")
            FileOutputStream(file, true).use {
                it.write("$stats\n".toByteArray())
            }
            i++
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(broadcastReceiver)
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