package com.example.saleckope.piszkosanyagiak.sevice;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.IBinder;

import com.example.saleckope.piszkosanyagiak.R;
import com.example.saleckope.piszkosanyagiak.ui.main.MainActivity;

import java.util.Calendar;

public class NotifyService extends IntentService {

    public NotifyService() {
        super("NotifyService");
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        //Set notification sound to the default not. sound
        Uri sound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        //Set intent of the notification
        NotificationManager mNM = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        Intent myIntent = new Intent(getBaseContext(), MainActivity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(getBaseContext(), 0, myIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        //Create the notification with the rigth settings
        Resources res = this.getResources();
        Notification notification = new Notification.Builder(getBaseContext())
                //Main tilte of notification
                .setContentTitle("Budget for today")
                //Text of notification
                .setContentText("Check it out!")
                //Icons are the main application icon
                .setSmallIcon(R.mipmap.ic_launcher)
                .setLargeIcon(BitmapFactory.decodeResource(res, R.mipmap.ic_launcher))
                //The intent of notification
                .setContentIntent(contentIntent)
                //Set the sound
                .setSound(sound)
                //By clicking it disappears from notification list
                .setAutoCancel(true)
                //Other settings are the default
                .setDefaults(Notification.DEFAULT_ALL)
                .build();
        //Notifying
        mNM.notify(0, notification);
    }
}
