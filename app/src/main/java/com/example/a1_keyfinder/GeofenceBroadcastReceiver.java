package com.example.a1_keyfinder;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by Diana B on 5/31/2018.
 */

public class GeofenceBroadcastReceiver extends BroadcastReceiver{
    @Override
    public void onReceive(Context context, Intent intent) {

        GeofenceTransitionsJobIntentService.enqueueWork(context, intent);
    }
}
