package com.example.batterymanager_utility

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            HomeScreen("BatteryManager Utility", this)
        }
    }

    fun startService() {
//      adb shell am start-foreground-service -n "com.example.batterymanager_utility/com.example.batterymanager_utility.DataCollectionService" --ei sampleRate 800 \ --es "dataFields" "BATTERY_HEALTH_COLD,BATTERY_STATUS_CHARGING,EXTRA_VOLTAGE,EXTRA_TEMPERATURE" \ --ez toCSV True

        val intent = Intent(this, DataCollectionService::class.java)
        intent.putExtra("sampleRate", 1000)
        intent.putExtra("dataFields", "BATTERY_HEALTH_COLD,BATTERY_STATUS_CHARGING,EXTRA_VOLTAGE,EXTRA_TEMPERATURE")
        intent.putExtra("toCSV", true)
        startForegroundService(intent)
    }

    fun stopService() {
//      adb shell am stopservice -n "com.example.batterymanager_utility/com.example.batterymanager_utility.DataCollectionService"
        val intent = Intent(this, DataCollectionService::class.java)
        stopService(intent)
    }
}

@Composable
fun HomeScreen(name: String, mainActivity: MainActivity) {
    Column {
        Text(text = name, color = Color.Red, modifier = Modifier.padding(24.dp))

        // These buttons are mainly for testing purposes, they will be removed later
        Button(onClick = { mainActivity.startService() }) {
            Text(text = "Start Service")
        }

        Button(onClick = { mainActivity.stopService() }) {
            Text(text = "Stop Service")
        }
    }
}
