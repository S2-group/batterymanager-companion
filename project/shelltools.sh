base=/data/local/tmp/
export CLASSPATH=$base/shelltools.jar
export ANDROID_DATA=$base
mkdir -p $base/dalvik-cache
exec app_process $base com.example.shelltool.Main "$@"