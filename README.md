# BatteryManager Companion
## Description
This app provides and interface between the [BatteryManager](https://developer.android.com/reference/android/os/BatteryManager) class in the Android API and the user, allowing for an easy way to view the battery-related information of any Android device. The application was developed to integrate with [AndroidRunner](https://github.com/s2-group/android-runner), but it can be used as a standalone app as well via [ADB](https://developer.android.com/studio/command-line/adb) commands.


## Validation
A series of validation experiments of the application can be found in the [battery-manager-ar-plugin-evaluation](https://github.com/S2-group/battery-manager-ar-plugin-evaluation) repository.
The experiments were run over the course of a week on the following devices:
* Samsung Galaxy J7
* Google Pixel 3
* Nexus 5X
* Nokia 6.1
* Nokia G10

All the devices went through a series of [stress test applications](https://github.com/S2-group/android-apps-benchmark), and the data was collected using [AndroidRunner](https://github.com/S2-group/android-runner). The device battery level was not allowed to drop below 75% during the experiments. All the devices were connected to a Raspberry PI 4B via WiFi. The Raspberry PI was used to run the experiments and collect the data. 

The scope of the validation was to ensure that there are no discrepancies in the energy usage between the different devices when the BatteryManager app is running at different sampling rates. The sampling rates that were used are:
* 1000 ms (1 second)
* 100 ms (0.1 seconds)
* 10 ms (0.01 seconds)
* 1 ms (0.001 seconds)
* 0 ms (as fast as possible)


## Installation
### via ADB
1. Download the [latest release](https://github.com/S2-group/batterymanager-companion/releases) of the app.
2. Connect your Android device to your computer via USB or WiFi.
3. Open a terminal and navigate to the folder where you downloaded the app.
4. Run the following command: `adb install -g com.example.batterymanager_utility.apk`.
5. The app should now be installed on your device.


## Usage
### via ADB
1. Connect your Android device to your computer via USB or WiFi.
2. Open a terminal and run the following command to connect to your device shell. All of the following commands will be run from the device shell.
``` bash
adb shell
```
3. Connected to the device shell, we can now start the app. Run the following command:
``` bash
am start -n "com.example.batterymanager_utility/com.example.batterymanager_utility.MainActivity" -a android.intent.action.MAIN -c android.intent.category.LAUNCHER
```
4. The app should now be open and running on your device (i.e., you should see the app's main screen). 
5. Now that the application is running, we can use ADB to interact with it. It's a good moment to decide how often the collection should happen (`sampleRate`), which metrics should be collected (`dataFields`), and whether we want them into a `.csv` file on the Android device or not (`toCSV`):
    * sampleRate `(Type: Integer, Default: 1000)`
        * The value is in milliseconds, so if you want to collect every 5 seconds, you should set the interval to 5000. The default value is 1000 (1 second). The range of values is from 0 (i.e., collect as fast as possible) to INT_MAX (i.e., collect every 24.8 days). 
        * NOTE: there is no checking for the validity of the value, so if you set it to a negative value, let us know what happened, because we didn't try it yet.
    * dataFields `(Type: String, Default: None)`
        * A list of all the metrics that BatteryManager-companion can collect can be found in the [BatteryManager documentation page under Constants](https://developer.android.com/reference/android/os/BatteryManager#constants_1).
        * The value names are the same as in the documentation, so you can use them directly. If you want to collect more than one value, you can separate them with a comma (e.g., `BATTERY_HEALTH_COLD,BATTERY_PROPERTY_CURRENT_NOW,EXTRA_LEVEL`). The Timestamp of the collection is recorded by default, so you don't need to specify it.
    * toCSV `(Type: Boolean, Default: True)`
        * Whether the `toCSV` feature is enabled or not. The values recorded by the app are also sent to the Android logs. These values can be accessed via `adb logcat`.
        * If you want to save the collected data into a `.csv` file on the Android device, set this value to `True`. Otherwise, set it to `False`. Unfortunately the this feature is very flaky, so we recommend setting it to `False` and using the `adb logcat` command to collect the data from the logcat.

6. With the values from the previous step, we can now start the collection. Run the following command replacing our values with the values you chose:
``` bash
am start-foreground-service -n "com.example.batterymanager_utility/com.example.batterymanager_utility.DataCollectionService" --ei sampleRate 100 --es "dataFields" "BATTERY_HEALTH_COLD,BATTERY_PROPERTY_CURRENT_NOW,EXTRA_LEVEL" --ez toCSV False
```

7. The collection should now be running. If you are using a phone or tablet, you will get a notification saying that the app is running in the foreground.

8. To stop the collection, run the following command:
``` bash
am stopservice com.example.batterymanager_utility/com.example.batterymanager_utility.DataCollectionService
```

9. To stop the application completely, run the following command:
``` bash
am force-stop com.example.batterymanager_utility
```

10. To get the collected data from the Android device logcat to your main device in the `batterymanager-companion.log` file, exit the adb shell by running the `exit` command and run the following command:
``` bash
adb shell logcat -d | grep "BatteryMgr:DataCollectionService" > batterymanager-companion.log
```

11. The `batterymanager-companion.log` file should now be in the same folder where you ran the command. You can open it with any text editor.

## Limitations and Known Issues
* The companion app keeps everything in memory and then dumps it to a csv file. This means that if the user wants to use memory as a dependent variable, they should not use the `csv` persistency strategy.
* Extremely low `sampleRate` values causes the number of observations from the companion app to be inconsistent between runs. (i.e., one run might have 1000 rows, next run could have 800, or 1200 rows).
* Running the collection step using the `toCSV` set to `True` can crash on older devices. We recommend setting it to `False`.

