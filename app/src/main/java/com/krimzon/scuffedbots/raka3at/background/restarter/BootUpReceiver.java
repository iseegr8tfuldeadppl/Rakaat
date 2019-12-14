package com.krimzon.scuffedbots.raka3at.background.restarter;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.krimzon.scuffedbots.raka3at.background.Globals;


public class BootUpReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Intent broadcastIntent = new Intent(Globals.RESTART_INTENT);
        context.sendBroadcast(broadcastIntent);
    }



}