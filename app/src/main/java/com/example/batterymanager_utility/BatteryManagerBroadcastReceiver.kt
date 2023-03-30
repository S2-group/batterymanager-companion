package com.example.batterymanager_utility

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.BatteryManager
import android.util.Log

class BatteryManagerBroadcastReceiver : BroadcastReceiver() {

    var lastKnownVoltage : Int = 0 // mili volts     -- still not working

    companion object {
        private const val TAG = "BatteryManagerBroadcastReceiver"
    }

    fun getVoltage(): Int {
        return lastKnownVoltage
    }

    override fun onReceive(context: Context, intent: Intent) {

        Log.i(TAG, "onReceive action => " + intent.action)

        if (intent.action.equals(Intent.ACTION_BATTERY_CHANGED)) {
            val voltage = intent.getIntExtra(BatteryManager.EXTRA_VOLTAGE, 0)
            val level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1)
            val scale = intent.getIntExtra(BatteryManager.EXTRA_SCALE, -1)
            val lastLevel = (level * 100).toDouble() / scale

            lastKnownVoltage = voltage

            Log.i(TAG, "voltage => $voltage")
            Log.i(TAG, "lastLevel => $lastLevel")
        }
//        onReceiveIntent(intent)
    }
}