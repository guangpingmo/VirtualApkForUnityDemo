package com.volcano.hostapp;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import com.didi.virtualapk.PluginManager;

public class HostApplication extends Application{
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        long start = System.currentTimeMillis();
        PluginManager.getInstance(base).init();
        Log.d("HostApplication", "use time:" + (System.currentTimeMillis() - start));
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }
}
