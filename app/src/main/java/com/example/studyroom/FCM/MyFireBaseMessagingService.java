package com.example.studyroom.FCM;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.example.studyroom.LoginActivity;
import com.example.studyroom.MainActivity;
import com.example.studyroom.R;

import com.firebase.jobdispatcher.FirebaseJobDispatcher;
import com.firebase.jobdispatcher.GooglePlayDriver;
import com.firebase.jobdispatcher.Job;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONObject;

import java.sql.Time;

public class MyFireBaseMessagingService extends FirebaseMessagingService {
    private String TAG = "MyFireBaseMessagingService_TAG";
    private String title = "";
    private String body = "";


    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        Log.d(TAG, " From : " + remoteMessage.getFrom());
        Log.d(TAG, "time : " + System.currentTimeMillis() + "##4");
//        푸시 알림 메시지 분기
//        putDate를 사용했을 때 data가져오기

        if (remoteMessage.getData().size() > 0) {
            Log.d(TAG, "Message data payload: " + remoteMessage.getData());
            title = remoteMessage.getData().get("title");
            body = remoteMessage.getData().get("body");

            if (/* Check if data needs to be processed by long running job */ true) {
//                For long- running tasks (10 seconds or more) use Firebase Job Dispatcher.
                scheduleJob();

            }
            else{
//                Handle message within 10 seconds
                handleNow();
            }
        }
        Log.d(TAG, "??  ");
//        Notification 사용 했을 때 data 가져오기
        if (remoteMessage.getNotification() != null) {
            Log.d(TAG, "Message Notification Body Color : " + remoteMessage.getNotification().getColor());
            Log.d(TAG, "Message Notification Body Icon : " + remoteMessage.getNotification().getIcon());
            Log.d(TAG, "Message Notification Body Title : " + remoteMessage.getNotification().getTitle());
            Log.d(TAG, "Message Notification Body Body : " + remoteMessage.getNotification().getBody());
            title = remoteMessage.getNotification().getTitle();
            body = remoteMessage.getNotification().getBody();
        }
        sendNotification();
//        Also if you intend on generating your own notifications as a result of a received FCM
//        Message, here is where that should be initiated. See sendNotification method below.
    }
    private void handleNow(){
        Log.d(TAG, "Short lived task is done.");
    }
    private void sendRegistrationToServer(String token){
    }
    @Override
    public void onNewToken(@NonNull String s) {
        Log.d(TAG, "Refreshed token : " + s);
//        If you want to send dmessages to this application instance or
//        manage thi apps subscriptions on the server side, send the
//        Instance ID token to your app server.
        sendRegistrationToServer(s);
    }

    // [END on_new_token]
    private void scheduleJob() {
//        [START dispatch_job]
        FirebaseJobDispatcher dispatcher = new FirebaseJobDispatcher(new GooglePlayDriver(this));
        Job myJob = dispatcher.newJobBuilder()
                .setService(MyJobService.class)
                .setTag("my-job-tag")
                .build();
        dispatcher.schedule(myJob);

    }

    private void sendNotification() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("curID", R.id.chat);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);
        String channelId = "채널 ID";
        
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, channelId)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(title)
                .setContentText(body)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);
        Log.d(TAG, "pendingIntent extra : " + intent.getIntExtra("curID", 0));
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//        Since android Oreo notification channel is needed.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channelId, "channel human readable title", NotificationManager.IMPORTANCE_HIGH);
            notificationManager.createNotificationChannel(channel);
        }
        notificationManager.notify(0, notificationBuilder.build());
    }

//    @Override
//    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
//        if(remoteMessage.getNotification()!=null)
//
//        {
//            Log.d(TAG, "알림 메시지 : " + remoteMessage.getNotification().getBody());
//            String messageBody = remoteMessage.getNotification().getBody();
//            String messageTitle = remoteMessage.getNotification().getTitle();
//
//            Intent intent = new Intent(this, LoginActivity.class);
//            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//            intent.putExtra("curID", R.id.chat);
//
//            TaskStackBuilder taskStackBuilder = TaskStackBuilder.create(getApplicationContext());
//            taskStackBuilder.addParentStack(MainActivity.class);
//            taskStackBuilder.addNextIntent(intent);
//            PendingIntent pendingIntent = taskStackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
//
//            String channelId = "Channel ID";
//            Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
//            NotificationCompat.Builder notiBuilder = new NotificationCompat.Builder(this, channelId)
//                    .setSmallIcon(R.mipmap.ic_launcher)
//                    .setContentTitle(messageTitle)
//                    .setContentText(messageBody)
//                    .setAutoCancel(true)
//                    .setSound(defaultSoundUri)
//                    .setContentIntent(pendingIntent);
//
//            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//                String channelName = "Channel Name";
//                NotificationChannel channel = new NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_HIGH);
//                notificationManager.createNotificationChannel(channel);
//            }
//            notificationManager.notify(0, notiBuilder.build());
//
//        }

    }


//        fcm _ 1


