package com.example.monil.turbotask;

import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by monil on 4/28/2017.
 */

public class PriorityService extends Service {

    private DatabaseReference mTasksReference;
    private FirebaseUser mUser;
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId){
        super.onStartCommand(intent, flags, startId);

        //get a list of user tasks
        //if any are due tommorow make priority 1
        mUser = FirebaseAuth.getInstance().getCurrentUser();
        if(mUser != null){
            mTasksReference = FirebaseDatabase.getInstance().getReference().child("data").child("user-tasks").child(mUser.getUid());
        }
        Date date = new Date();
        String newstring = new SimpleDateFormat("MM/dd/yyyy").format(date);
        Log.d("timer", newstring);
//        Query queryRef = mTasksReference.orderByChild("date").equalTo(date.toString());
        return Service.START_STICKY;
    }
}
