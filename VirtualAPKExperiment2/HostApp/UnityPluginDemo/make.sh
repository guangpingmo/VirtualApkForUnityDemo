./gradlew clean assemblePlugin --stacktrace
open build/outputs/apk/release/
adb uninstall com.volcano.unityplugindemo
pluginApkPath=build/outputs/apk/release/UnityPluginDemo-release.apk
adb install $pluginApkPath
adb push $pluginApkPath /sdcard/Test.apk
adb shell am force-stop com.volcano.hostapp
adb shell am start -n com.volcano.hostapp/com.volcano.hostapp.MainActivity
