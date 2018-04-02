package com.demo_chat_app.pulkit.services;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.widget.Toast;

/**
 * Created by pulkit on 19/2/18.
 */

public class FriendsChatService extends Service {

    @Override
    public void onCreate() {
        super.onCreate();

        Toast.makeText(this, "Service Created", Toast.LENGTH_SHORT).show();


    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        Toast.makeText(getApplicationContext(), "Service Started", Toast.LENGTH_SHORT).show();

        return super.onStartCommand(intent, flags, startId);

    }


    @Override
    public void onDestroy() {
        super.onDestroy();

        Toast.makeText(getApplicationContext(), "Service Stopped", Toast.LENGTH_SHORT).show();

    }


}
