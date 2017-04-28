package com.example.monil.turbotask;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Created by monil on 3/31/2017.
 */

public class BootReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

        Log.d("boot", intent.getAction());
        Intent serviceIntent = new Intent(context, PriorityService.class);
        context.startService(serviceIntent);
    }
}
