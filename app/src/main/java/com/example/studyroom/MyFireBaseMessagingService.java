package com.example.studyroom;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.sql.Time;

public class MyFireBaseMessagingService extends FirebaseMessagingService {
    private String TAG = "MyFireBaseMessagingService_TAG";

    @Override
    public void onNewToken(@NonNull String s) {
        Log.d(TAG, "Refreshed token : " + s);
    }

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {

        Log.d(TAG, "time : " + System.currentTimeMillis() + "##4");
        if (remoteMessage.getNotification() != null) {
            Log.d(TAG, "알림 메시지 : " + remoteMessage.getNotification().getBody());
            String messageBody = remoteMessage.getNotification().getBody();
            String messageTitle = remoteMessage.getNotification().getTitle();

            Intent intent = new Intent(this, LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.putExtra("curID", R.id.chat);

            TaskStackBuilder taskStackBuilder = TaskStackBuilder.create(getApplicationContext());
            taskStackBuilder.addParentStack(MainActivity.class);
            taskStackBuilder.addNextIntent(intent);
            PendingIntent pendingIntent = taskStackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

            String channelId = "Channel ID";
            Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            NotificationCompat.Builder notiBuilder = new NotificationCompat.Builder(this, channelId)
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setContentTitle(messageTitle)
                    .setContentText(messageBody)
                    .setAutoCancel(true)
                    .setSound(defaultSoundUri)
                    .setContentIntent(pendingIntent);

            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                String channelName = "Channel Name";
                NotificationChannel channel = new NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_HIGH);
                notificationManager.createNotificationChannel(channel);
            }
            notificationManager.notify(0, notiBuilder.build());

        }
    }
}
