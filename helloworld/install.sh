#!/bin/sh
adb shell mkdir -p /data/local/tmp/helloworld
adb push helloworld.jar /data/local/tmp/helloworld
adb push helloworld.sh /data/local/tmp/helloworld
adb shell chmod 777 /data/local/tmp/helloworld/helloworld.sh
