package com.example.studyroom;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {
    private static final String CHANNEL_ID = "channel_1";
    private BackPressCloseHandler backPressCloseHandler = new BackPressCloseHandler(this);
    private static String TAG = "DataBase";
    private FragmentManager fragmentManager;
    private FragmentSearch fragmentSearch;
    private FragmentChat fragmentChat;
    private FragmentMyPage fragmentMyPage;
    private FragmentHome fragmentHome;
    NotificationManager mNotificationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fragmentManager = getSupportFragmentManager();
        fragmentSearch = new FragmentSearch();
        fragmentChat = new FragmentChat();
        fragmentMyPage = new FragmentMyPage();
        fragmentHome = new FragmentHome();

        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.frameLayout, fragmentHome).commitAllowingStateLoss();

//        chat_data : internal storage cache
//        사용자 입장 : 관리자와 만 채팅
//        채팅 fragment - 채팅방 목록 불필요 - 관리자 와 1ㄷ1 채팅
//        관리자와의 채팅방의 이름으로 내부 저장소에 채팅 기록 파일로 변환해 저장

//        관리자 입장 : 모든 사용자와 채팅
//        채팅 fragement - 채팅방 목록 필요 - 모든 사용자와의 방 생성
//        각 채팅방 별로 내부 저장소에 폴더를 생성 후, 채팅 기록을을 저장
//
        BottomNavigationView bottomNavigationView = findViewById(R.id.navigationView);
        bottomNavigationView.setOnNavigationItemSelectedListener(new ItemSelectedListener());


        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myReference= database.getReference("message");

        myReference.setValue("Hello, World!!");

    }
//        mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//        createNotificationChannel();
//        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), CHANNEL_ID);
//        Intent notificationIntent = new Intent(getApplicationContext(), MainActivity.class);
//        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
//        int requestID = (int) System.currentTimeMillis();
//        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), requestID, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
//        builder.setContentTitle("Title")
//                .setContentText("Content").
//                setDefaults(Notification.DEFAULT_ALL).
//                setAutoCancel(true).
//                setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
//                .setSmallIcon(android.R.drawable.btn_star)
//                .setLargeIcon(BitmapFactory.decodeResource(getResources(),R.drawable.my_page))
//                .setContentIntent(pendingIntent);
//
//    }
//
//    private void createNotificationChannel() {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            CharSequence name = getString(R.string.common_google_play_services_notification_channel_name);
//            int importance = NotificationManager.IMPORTANCE_DEFAULT;
//            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
//            mNotificationManager.createNotificationChannel(channel);
//        }
//    }


    @Override
    public void onBackPressed() {//뒤로가기 버튼 클릭시 종료
        backPressCloseHandler.onBackPressed();
    }

    class ItemSelectedListener implements BottomNavigationView.OnNavigationItemSelectedListener {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
            FragmentTransaction transaction = fragmentManager.beginTransaction();

            switch (menuItem.getItemId()) {
                case R.id.home:
                    transaction.replace(R.id.frameLayout, fragmentHome).commitAllowingStateLoss();
                    break;
                case R.id.searchItem:
                    transaction.replace(R.id.frameLayout, fragmentSearch).commitAllowingStateLoss();
                    break;
                case R.id.chat:
                    transaction.replace(R.id.frameLayout, fragmentChat).commitAllowingStateLoss();
                    break;
                case R.id.my_page:
                    transaction.replace(R.id.frameLayout, fragmentMyPage).commitAllowingStateLoss();
                    break;
            }
            return true;
        }
    }
}
