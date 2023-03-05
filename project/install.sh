#!/bin/sh
# adb shell mkdir -p /data/local/tmp/helloworld
adb push shelltools.jar /data/local/tmp
adb push shelltools.sh /data/local/tmp
adb shell chmod 777 /data/local/tmp/shelltools.sh
