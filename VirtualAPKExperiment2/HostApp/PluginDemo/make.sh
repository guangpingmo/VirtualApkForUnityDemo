./gradlew clean assemblePlugin
open app/build/outputs/apk/beijing/release/
adb uninstall com.didi.virtualapk.demo
adb install app/build/outputs/apk/beijing/release/app-beijing-release.apk
adb push app/build/outputs/apk/beijing/release/app-beijing-release.apk /sdcard/Test.apk
adb shell am force-stop com.volcano.hostapp
adb shell am start -n com.volcano.hostapp/com.volcano.hostapp.MainActivity
