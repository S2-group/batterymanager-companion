package com.example.batterymanager_utility

import android.content.Context
import android.content.Context.BATTERY_SERVICE
import android.content.Intent
import android.net.Uri
import android.os.BatteryManager
import android.os.Bundle
import android.provider.DocumentsContract
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.example.batterymanager_utility.ui.theme.Batterymanager_utilityTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Batterymanager_utilityTheme {
                // A surface container using the 'background' color from the theme
                Surface(color = MaterialTheme.colors.background) {
                    Greeting("BatteryManager Utility", this)
//                    writeFile(this)
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, context: Context) {
    Surface(color = Color.Yellow) {
        Text(text = "Hello from $name!", color = Color.Red)
    }
    writeToLog(context)
    createFile(Uri.parse("/sdcard/Download/"), context)

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

fun writeToLog(context: Context) {
// write to new file in /data/local/tmp/

    Toast.makeText(context, "Hello from BatteryManager utility!", Toast.LENGTH_SHORT).show()

    var batteryManager = context.getSystemService(BATTERY_SERVICE) as BatteryManager
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
}

// Request code for creating a PDF document.
const val CREATE_FILE = 1

fun createFile(pickerInitialUri: Uri, context: Context) {
    val intent = Intent(Intent.ACTION_CREATE_DOCUMENT).apply {
        addCategory(Intent.CATEGORY_OPENABLE)
        type = "text/plain"
        putExtra(Intent.EXTRA_TITLE, "bm_log.csv")

        // Optionally, specify a URI for the directory that should be opened in
        // the system file picker before your app creates the document.
        putExtra(DocumentsContract.EXTRA_INITIAL_URI, pickerInitialUri)
    }
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

//@Preview(showBackground = true)
//@Composable
//fun DefaultPreview() {
//    Batterymanager_utilityTheme {
//        Greeting("Android", this)
//    }
//}