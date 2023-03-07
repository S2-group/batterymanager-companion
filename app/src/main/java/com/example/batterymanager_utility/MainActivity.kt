package com.example.batterymanager_utility

import android.content.Context
import android.content.Context.BATTERY_SERVICE
import android.os.BatteryManager
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import com.example.batterymanager_utility.ui.theme.Batterymanager_utilityTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Batterymanager_utilityTheme {
                // A surface container using the 'background' color from the theme
                Surface(color = MaterialTheme.colors.background) {
                    Greeting("Android", this)
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, mcoContext: Context) {
    Text(text = "Hello $name!")
    writeFile(mcoContext)

}

//private fun writeToFile(data: String, context: Context) {
//    try {
//        val outputStreamWriter =
//            OutputStreamWriter(context.openFileOutput("config.txt", Context.MODE_PRIVATE))
//        outputStreamWriter.write(data)
//        outputStreamWriter.close()
//    } catch (e: IOException) {
//        Log.e("Exception", "File write failed: " + e.toString())
//    }
//}

fun writeFile(mcoContext: Context) {
// write to new file in /data/local/tmp/
    var batteryManager = mcoContext.getSystemService(BATTERY_SERVICE) as BatteryManager
    var chargingStatus: Boolean
    var currentNow: Int

    while (true) {
        chargingStatus = batteryManager.isCharging
        currentNow = batteryManager.getIntProperty(BatteryManager.BATTERY_PROPERTY_CURRENT_NOW)

        //TODO: write logs of relevant data
        Log.e("BatteryMgr", "chargeStat $chargingStatus")
        Log.e("BatteryMgr", "currentNow $currentNow")
        
        Thread.sleep(5000)
    }

    

// write to new file in /data/data/com.example.batterymanager_utility/files/mydir



//    val dir = File(mcoContext.filesDir, "mydir")
//    if (!dir.exists()) {
//        dir.mkdir()
//    }
//    try {
//        val gpxfile = File(dir, sFileName)
//        val writer = FileWriter(gpxfile)
//        writer.append(sBody)
//        writer.flush()
//        writer.close()
//    } catch (e: Exception) {
//        e.printStackTrace()
//    }
}

//@Preview(showBackground = true)
//@Composable
//fun DefaultPreview() {
//    Batterymanager_utilityTheme {
//        Greeting("Android", this)
//    }
//}