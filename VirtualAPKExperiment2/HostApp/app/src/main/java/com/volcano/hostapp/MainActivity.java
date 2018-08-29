package com.volcano.hostapp;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.didi.virtualapk.PluginManager;

import java.io.File;

public class MainActivity extends AppCompatActivity {

    private static final int PERMISSION_REQUEST_CODE_STORAGE = 20171222;

    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (hasPermission()) {
            Log.d(TAG,"loadPlugin");

            this.loadPlugin(this);
        } else {
            requestPermission();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (PERMISSION_REQUEST_CODE_STORAGE == requestCode) {
            if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                requestPermission();
            } else {
                this.loadPlugin(this);
            }
            return;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private boolean hasPermission() {

        Log.d(TAG,"hasPermission");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
        }
        return true;
    }

    private void requestPermission() {

        Log.d(TAG,"requestPermission");

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE_STORAGE);
        }
    }

    private void loadPlugin(Context base) {
        Log.d("VirtualApk", "LoadAndroidPlugin StartLoad");
        if (!Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            Toast.makeText(this, "sdcard was NOT MOUNTED!", Toast.LENGTH_SHORT).show();
        }
        PluginManager pluginManager = PluginManager.getInstance(base);
        File apk = new File(Environment.getExternalStorageDirectory(), "Test.apk");
        if (apk.exists()) {
            try {
                pluginManager.loadPlugin(apk);
                Log.i(TAG, "Loaded plugin from apk: " + apk);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            try {
                File file = new File(base.getFilesDir(), "Test.apk");
                java.io.InputStream inputStream = base.getAssets().open("Test.apk", 2);
                java.io.FileOutputStream outputStream = new java.io.FileOutputStream(file);
                byte[] buf = new byte[1024];
                int len;

                while ((len = inputStream.read(buf)) > 0) {
                    outputStream.write(buf, 0, len);
                }

                outputStream.close();
                inputStream.close();
                pluginManager.loadPlugin(file);
                Log.i(TAG, "Loaded plugin from assets: " + file);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        RunPlugin();
    }

    final String pkg = "com.volcano.unityplugindemo";
    final String pluginActivity = "com.volcano.unityplugindemo.UnityPlayerActivity";
    private void RunPlugin()
    {
        Log.d("VirtualApk", "LoadAndroidPlugin StartFind!");
        if (PluginManager.getInstance(this).getLoadedPlugin(pkg) == null) {
            Log.d("VirtualApk", "LoadAndroidPlugin FindFailed!");
            Toast.makeText(this, String.format("plugin {0} not loaded", pkg), Toast.LENGTH_SHORT).show();
            return;
        }
        Log.d("VirtualApk", "LoadAndroidPlugin FindSucc!");
        // test Activity and Service
        Intent intent = new Intent();
        Log.d("VirtualApk", "LoadAndroidPlugin StartActivity!");
        intent.setClassName(this, pluginActivity);
        startActivity(intent);
    }
}
