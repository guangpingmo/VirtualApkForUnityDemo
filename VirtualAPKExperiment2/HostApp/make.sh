./gradlew clean assembleRelease --stacktrace
adb uninstall com.volcano.hostapp
open app/build/outputs/apk/release/ 
adb install app/build/outputs/apk/release/app-release.apk
