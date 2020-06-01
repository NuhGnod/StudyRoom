package com.example.studyroom;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.NotificationCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.os.Build;
import android.os.Bundle;
import android.text.Layout;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;


public class MainActivity extends AppCompatActivity {
    private BackPressCloseHandler backPressCloseHandler = new BackPressCloseHandler(this);
    private static String TAG = "MainActivity_TAG";
    private FragmentManager fragmentManager;
    private FragmentSearch fragmentSearch;
    private FragmentChat fragmentChat;
    private FragmentMyPage fragmentMyPage;
    private FragmentHome fragmentHome;
    private FirebaseFirestore db;
    private BottomNavigationView bottomNavigationView;
    private ConstraintLayout layout;
    private int curID;
    private Toolbar toolbar;
    private CollectionReference colRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db = FirebaseFirestore.getInstance();
        layout = findViewById(R.id.constraintLayout_main);
        fragmentManager = getSupportFragmentManager();
        fragmentHome = new FragmentHome();
        toolbar = findViewById(R.id.toolbar_main);
        setSupportActionBar(toolbar);

        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.frameLayout, fragmentHome).commitAllowingStateLoss();

        bottomNavigationView = findViewById(R.id.navigationView);
        bottomNavigationView.setOnNavigationItemSelectedListener(new ItemSelectedListener());
        bottomNavigationView.getOrCreateBadge(R.id.chat).setNumber(1);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myReference = database.getReference("message");

        myReference.setValue("Hello, World!!");
        layout.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if (curID == R.id.chat) {
                    int mRootViewHeight = layout.getRootView().getHeight();
                    int mRelativeWrapperHeight = layout.getHeight();
                    int mDiff = mRootViewHeight - mRelativeWrapperHeight;

                    if (mDiff > dpTOPx(200)) {
//                        Toast.makeText(getApplicationContext(), "키보드가 위로 올라왔습니다.", Toast.LENGTH_LONG).show();
                        bottomNavigationView.setVisibility(View.GONE);
                    } else {
//                            Toast.makeText(getApplicationContext(), "키보드가 내려갔습니다.", Toast.LENGTH_LONG).show();
                        bottomNavigationView.setVisibility(View.VISIBLE);

                    }
                }
            }
        });

        FirebaseInstanceId.getInstance().getInstanceId().addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
            @Override
            public void onComplete(@NonNull Task<InstanceIdResult> task) {
                if(!task.isSuccessful()){
                    Log.w(TAG, "getInstanceId failed", task.getException());

                }
                String token = task.getResult().getToken();
                Log.d(TAG, "FCM 토큰 : " + token);
                Log.d(TAG, "FCM 토큰 : " + token);

                Toast.makeText(MainActivity.this, token, Toast.LENGTH_SHORT).show();


            }
        });
    }


    private float dpTOPx(float i) {
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, i, metrics);
    }

    @Override
    public void onBackPressed() {//뒤로가기 버튼 클릭시 종료
        backPressCloseHandler.onBackPressed();
    }

    class ItemSelectedListener implements BottomNavigationView.OnNavigationItemSelectedListener {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            curID = menuItem.getItemId();
            switch (menuItem.getItemId()) {
                case R.id.home:
                    if (fragmentHome == null) {


                        fragmentHome = new FragmentHome();
                        transaction.add(R.id.frameLayout, fragmentHome);
                    }

                    if (fragmentHome != null) transaction.show(fragmentHome);
                    if (fragmentSearch != null) transaction.hide(fragmentSearch);
                    if (fragmentChat != null) transaction.hide(fragmentChat);
                    if (fragmentMyPage != null) transaction.hide(fragmentMyPage);
                    getSupportActionBar().setDisplayShowTitleEnabled(true);
                    getSupportActionBar().setTitle("Fragment Home");
                    transaction.commit();

                    break;
                case R.id.searchItem:
                    if (fragmentSearch == null) {
                        fragmentSearch = new FragmentSearch();
                        transaction.add(R.id.frameLayout, fragmentSearch);
                    }
                    if (fragmentSearch != null) transaction.show(fragmentSearch);
                    if (fragmentHome != null) transaction.hide(fragmentHome);
                    if (fragmentChat != null) transaction.hide(fragmentChat);
                    if (fragmentMyPage != null) transaction.hide(fragmentMyPage);
                    transaction.commit();

                    break;
                case R.id.chat:
                    if (fragmentChat == null) {
                        fragmentChat = new FragmentChat();
                        transaction.add(R.id.frameLayout, fragmentChat);
                    }
                    if (fragmentChat != null) transaction.show(fragmentChat);
                    if (fragmentHome != null) transaction.hide(fragmentHome);
                    if (fragmentSearch != null) transaction.hide(fragmentSearch);
                    if (fragmentMyPage != null) transaction.hide(fragmentMyPage);
                    transaction.commit();

                    break;
                case R.id.my_page:
                    if (fragmentMyPage == null) {
                        fragmentMyPage = new FragmentMyPage();
                        transaction.add(R.id.frameLayout, fragmentMyPage);
                    }
                    if (fragmentMyPage != null) transaction.show(fragmentMyPage);
                    if (fragmentHome != null) transaction.hide(fragmentHome);
                    if (fragmentSearch != null) transaction.hide(fragmentSearch);
                    if (fragmentChat != null) transaction.hide(fragmentChat);
                    transaction.commit();

                    break;
            }

            return true;
        }
    }
}
