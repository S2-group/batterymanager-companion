package com.example.batterymanager_utility

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.batterymanager_utility.ui.theme.Batterymanager_utilityTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Greeting("BatteryManager Utility", this)
        }
    }

    fun startService() {
        val intent = Intent(this, DataCollectionService::class.java)
        intent.putExtra("sampleRate", 1000)
        intent.putExtra("dataFields", "BATTERY_HEALTH_COLD,BATTERY_STATUS_CHARGING,EXTRA_VOLTAGE,EXTRA_TEMPERATURE")
        startForegroundService(intent)
    }

    fun stopService() {
        val intent = Intent(this, DataCollectionService::class.java)
        stopService(intent)
    }
}

@Composable
fun Greeting(name: String, mainActivity: MainActivity) {
    Column() {
        Text(text = name, color = Color.Red, modifier = Modifier.padding(24.dp))

        Button(onClick = { mainActivity.startService() }) {
            Text(text = "Start Service")
        }

        Button(onClick = { mainActivity.stopService() }) {
            Text(text = "Stop Service")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    Batterymanager_utilityTheme {
        Greeting("BatteryManager Utility", MainActivity())
    }
}