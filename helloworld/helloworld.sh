base=/data/local/tmp/helloworld
export CLASSPATH=$base/helloworld.jar
export ANDROID_DATA=$base
mkdir -p $base/dalvik-cache
exec app_process $base com.example.HelloWorld "$@"
