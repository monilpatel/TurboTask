package com.example.monil.turbotask;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Created by monil on 4/28/2017.
 */

public class AlarmReceiver extends BroadcastReceiver
{
    public static final int REQUEST_CODE = 12345;

    @Override
    public void onReceive(Context context, Intent intent) {
        //Auto Prioritize
        Log.d("timer", "Time caused background service");
        Intent myService = new Intent(context, PriorityService.class);
        context.startService(myService);
    }
}

