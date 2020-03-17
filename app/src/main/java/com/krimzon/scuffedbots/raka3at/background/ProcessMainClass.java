package com.krimzon.scuffedbots.raka3at.background;


import android.content.Context;
import android.content.Intent;
import android.os.Build;

import com.krimzon.scuffedbots.raka3at.background.restarter.RestartServiceBroadcastReceiver;

public class ProcessMainClass {
    public static final String TAG = ProcessMainClass.class.getSimpleName();
    private static Intent serviceIntent = null;

    public ProcessMainClass() {
    }


    private void setServiceIntent(Context context) {
        if (serviceIntent == null) {
            serviceIntent = new Intent(context, Service.class);
        }
    }
    /**
     * launching the service
     */
    public void launchService(Context context) {
        if (context == null) {
            return;
        }
        setServiceIntent(context);
        // depending on the version of Android we eitehr launch the simple service (version<O)
        // or we start a foreground service
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            //RestartServiceBroadcastReceiver.scheduleJob(context);
            context.startForegroundService(serviceIntent);
        } else {
            context.startService(serviceIntent);
        }
    }
}

