package com.example.batterymanager_utility

import android.app.Application
import android.util.Log

class App : Application() {

    private val TAG = "BatteryMgr:App"

    companion object {
        val CHANNEL_ID: String = "serviceChannel"
    }

    override fun onCreate() {
        super.onCreate()
        createNotificationChannel();
    }

    private fun createNotificationChannel() {
        Log.i(TAG, "creating notification channel")

        val serviceChannel = android.app.NotificationChannel(
            CHANNEL_ID,
            "Battery Manager Service Channel",
            android.app.NotificationManager.IMPORTANCE_DEFAULT
        )

        val manager = getSystemService(android.app.NotificationManager::class.java)
        manager.createNotificationChannel(serviceChannel)
    }

}