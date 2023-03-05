package com.example.shelltool;

import android.content.Context;
// import android.content.Context.BATTERY_SERVICE;
// import android.content.Context.POWER_SERVICE;
import android.os.BatteryManager;

import android.os.IBinder;
import android.os.ServiceManager;
import android.util.DisplayMetrics;
import android.hardware.display.IDisplayManager;



public class Main {

  public static void main(String[] args) {
    
    // The service name comes from the constants defined in android.content.Context     
    // IBinder displayBinder = ServiceManager.getService("battery");
    // BatteryManager batteryManager = BatteryManager.Stub.asInterface(displayBinder);
    
    // try {
    //   int ids[] = displayManager.getDisplayIds();
    //   for (int id : ids) {
    //     int h = displayManager.getDisplayInfo(id).appHeight;
    //     int w = displayManager.getDisplayInfo(id).appWidth;
    //     System.out.println("Screensize[" + id + "]: "+ h + " x "+w);
    //   }
    // }
    // catch(Throwable t) {
    //   t.printStackTrace();
    // }
    Context context = getApplicationContext();
    BatteryManager batteryManager = (BatteryManager) context.getSystemService(BATTERY_SERVICE);

    int currentNow = batteryManager.getIntProperty(BatteryManager.BATTERY_PROPERTY_CURRENT_NOW); //Instantaneous battery current in microamperes
    int status = batteryManager.getIntProperty(BatteryManager.BATTERY_PROPERTY_STATUS); //Battery status
    
    boolean charge = batteryManager.isCharging();

    System.out.println("Charge: "+ charge);
    System.out.println("Status: "+ status);
    System.out.println("CurrentNow: "+ currentNow);

  }
}