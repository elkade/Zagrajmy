package com.example.lukas.zagrajmy.services;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;

import com.example.lukas.zagrajmy.MapsActivity;
import com.example.lukas.zagrajmy.MatchActivity;
import com.example.lukas.zagrajmy.R;

import java.util.Timer;
import java.util.TimerTask;

public class NotificationService extends Service {
    int mStartMode;       // indicates how to behave if the service is killed
    Timer mTimer;
    MyTimerTask mTimerTask;
    boolean isScheduled = false;
    @Override
    public void onCreate() {
        mTimer = new Timer();
        mTimerTask = new MyTimerTask();
    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        if(!isScheduled) {
            mTimer.schedule(mTimerTask, 0, 15000);
            isScheduled = true;
        }
        return mStartMode;
    }

    private class MyTimerTask extends TimerTask
    {
        public void run()
        {
            NotificationCompat.Builder mBuilder =
                    new NotificationCompat.Builder(NotificationService.this)
                            .setSmallIcon(R.drawable.common_google_signin_btn_icon_dark)
                            .setContentTitle("FULL TEAM!!!")
                            .setContentText("The match is starting in 1 hour!");
            Intent resultIntent = new Intent(NotificationService.this, MapsActivity.class);
            //------------
            resultIntent.putExtra("match_id", 1);
            //------------
            TaskStackBuilder stackBuilder = TaskStackBuilder.create(NotificationService.this);
            stackBuilder.addParentStack(MapsActivity.class);
            stackBuilder.addNextIntent(resultIntent);
            PendingIntent resultPendingIntent =
                    stackBuilder.getPendingIntent(
                            0,
                            PendingIntent.FLAG_UPDATE_CURRENT
                    );
            mBuilder.setContentIntent(resultPendingIntent);
            NotificationManager mNotificationManager =
                    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            mNotificationManager.notify(0, mBuilder.build());
        }
    }

    @Override
    public void onDestroy() {
        isScheduled = false;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}