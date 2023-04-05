package com.example.batterymanager_utility

import android.content.Context
import android.content.Context.BATTERY_SERVICE
import android.content.Intent
import android.content.IntentFilter
import android.os.BatteryManager

class DataCollector(private val context: Context, private val dataPoints: ArrayList<String>) {

    private val TAG = "BatteryMgr:DataCollector"

    private val batteryManager = context.getSystemService(BATTERY_SERVICE) as BatteryManager

    private var intentFilter: IntentFilter = IntentFilter().apply {
        addAction(Intent.ACTION_BATTERY_CHANGED)
    }

    private val dataPointsMapBATTERY = mapOf(
        "BATTERY_HEALTH_COLD"                   to BatteryManager.BATTERY_HEALTH_COLD,
        "BATTERY_HEALTH_DEAD"                   to BatteryManager.BATTERY_HEALTH_DEAD,
        "BATTERY_HEALTH_GOOD"                   to BatteryManager.BATTERY_HEALTH_GOOD,
        "BATTERY_HEALTH_OVERHEAT"               to BatteryManager.BATTERY_HEALTH_OVERHEAT,
        "BATTERY_HEALTH_OVER_VOLTAGE"           to BatteryManager.BATTERY_HEALTH_OVER_VOLTAGE,
        "BATTERY_HEALTH_UNKNOWN"                to BatteryManager.BATTERY_HEALTH_UNKNOWN,
        "BATTERY_HEALTH_UNSPECIFIED_FAILURE"    to BatteryManager.BATTERY_HEALTH_UNSPECIFIED_FAILURE,
        "BATTERY_PLUGGED_AC"                    to BatteryManager.BATTERY_PLUGGED_AC,
        "BATTERY_PLUGGED_DOCK"                  to BatteryManager.BATTERY_PLUGGED_DOCK, //requires SDK TIRAMISU
        "BATTERY_PLUGGED_USB"                   to BatteryManager.BATTERY_PLUGGED_USB,
        "BATTERY_PLUGGED_WIRELESS"              to BatteryManager.BATTERY_PLUGGED_WIRELESS,
        "BATTERY_PROPERTY_CAPACITY"             to BatteryManager.BATTERY_PROPERTY_CAPACITY,
        "BATTERY_PROPERTY_CHARGE_COUNTER"       to BatteryManager.BATTERY_PROPERTY_CHARGE_COUNTER,
        "BATTERY_PROPERTY_CURRENT_AVERAGE"      to BatteryManager.BATTERY_PROPERTY_CURRENT_AVERAGE,
        "BATTERY_PROPERTY_CURRENT_NOW"          to BatteryManager.BATTERY_PROPERTY_CURRENT_NOW,
        "BATTERY_PROPERTY_ENERGY_COUNTER"       to BatteryManager.BATTERY_PROPERTY_ENERGY_COUNTER,
        "BATTERY_PROPERTY_STATUS"               to BatteryManager.BATTERY_PROPERTY_STATUS,
        "BATTERY_STATUS_CHARGING"               to BatteryManager.BATTERY_STATUS_CHARGING,
        "BATTERY_STATUS_DISCHARGING"            to BatteryManager.BATTERY_STATUS_DISCHARGING,
        "BATTERY_STATUS_FULL"                   to BatteryManager.BATTERY_STATUS_FULL,
        "BATTERY_STATUS_NOT_CHARGING"           to BatteryManager.BATTERY_STATUS_NOT_CHARGING,
        "BATTERY_STATUS_UNKNOWN"                to BatteryManager.BATTERY_STATUS_UNKNOWN,
    )

    // EXTRA_* values are accessed through intents
    private val dataPointsMapEXTRA = mapOf<String, String>(
        "EXTRA_BATTERY_LOW"                     to BatteryManager.EXTRA_BATTERY_LOW, //requires SDK P
        "EXTRA_HEALTH"                          to BatteryManager.EXTRA_HEALTH,
        "EXTRA_ICON_SMALL"                      to BatteryManager.EXTRA_ICON_SMALL,
        "EXTRA_LEVEL"                           to BatteryManager.EXTRA_LEVEL,
        "EXTRA_PLUGGED"                         to BatteryManager.EXTRA_PLUGGED,
        "EXTRA_PRESENT"                         to BatteryManager.EXTRA_PRESENT,
        "EXTRA_SCALE"                           to BatteryManager.EXTRA_SCALE,
        "EXTRA_STATUS"                          to BatteryManager.EXTRA_STATUS,
        "EXTRA_TECHNOLOGY"                      to BatteryManager.EXTRA_TECHNOLOGY,
        "EXTRA_TEMPERATURE"                     to BatteryManager.EXTRA_TEMPERATURE,
        "EXTRA_VOLTAGE"                         to BatteryManager.EXTRA_VOLTAGE
    )

    fun getDataPoints(): ArrayList<String> {
        return this.dataPoints
    }

    fun getData(): String {
        var data = "${System.currentTimeMillis()}"

        for (dataPoint in dataPoints) {
            if (dataPoint.startsWith("EXTRA")) {
                val receiver: Intent? = context.registerReceiver(null, intentFilter)
                data += "," + receiver?.getIntExtra(dataPointsMapEXTRA[dataPoint], -1).toString()
            } else if (dataPoint == "ACTION_CHARGING") {
                data += "," + batteryManager.isCharging.toString()
            } else if (dataPoint == "ACTION_DISCHARGING") {
                data += "," + (!batteryManager.isCharging).toString()
            } else if (dataPoint.startsWith("BATTERY")) {
                data += "," + batteryManager.getIntProperty(dataPointsMapBATTERY[dataPoint]!!).toString()
            }
        }

        return data
    }

}

