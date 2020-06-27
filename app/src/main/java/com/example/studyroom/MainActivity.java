package com.example.studyroom;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;

import android.widget.Toast;

import com.example.studyroom.Fragment.FragmentChat;
import com.example.studyroom.Fragment.FragmentHome;
import com.example.studyroom.Fragment.FragmentMyPage;
import com.example.studyroom.Fragment.FragmentSearch;
import com.example.studyroom.Utility.BackPressCloseHandler;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


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
    private String token;
    private DocumentReference docRef;
    private String userID;
    private Map map;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db = FirebaseFirestore.getInstance();
        map = new HashMap();

        docRef = db.collection("chat").document("2");

        layout = findViewById(R.id.constraintLayout_main);
        fragmentManager = getSupportFragmentManager();
        fragmentHome = new FragmentHome();
        toolbar = findViewById(R.id.toolbar_main);
        setSupportActionBar(toolbar);

        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);

        Bundle extras = getIntent().getExtras();
        switchFragmentChat_FCM(extras, transaction);
        detectKeyboardUpDown();
        updateToken();

        SharedPreferences pref = getSharedPreferences("userID", Context.MODE_PRIVATE);
        userID = pref.getString("userID", null);

        bottomNavigationView = findViewById(R.id.navigationView);
        bottomNavigationView.setOnNavigationItemSelectedListener(new ItemSelectedListener());
        bottomNavigationView.getOrCreateBadge(R.id.chat).setNumber(1);

    }

    private void updateToken() {
        FirebaseInstanceId.getInstance().getInstanceId().addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
            @Override
            public void onComplete(@NonNull Task<InstanceIdResult> task) {
                if (!task.isSuccessful()) {
                    Log.w(TAG, "getInstanceId failed", task.getException());
                } else {
                    token = task.getResult().getToken();
                    Intent intent = getIntent();
                    Log.d(TAG, "intent : " + intent.getIntExtra("curID", 0));
                    Toast.makeText(MainActivity.this, token, Toast.LENGTH_SHORT).show();
                    final DocumentReference docRef = db.collection("users").document(userID);
                    docRef.update("token", token);
                }
            }
        });

    }

    private void getReadStatusFromFirestore() {
        final String[] reading_time = {""};
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.w(TAG, "getInstanceId failed", task.getException());
                } else if (task.getResult().get("admin") != null) {
                    SimpleDateFormat SDF = new SimpleDateFormat("yyyy-MM-dd aa h:mm:ss:SS");
                    reading_time[0] = SDF.format(new Date(System.currentTimeMillis()));
                } else {

                }

            }
        });
    }

    private void switchFragmentChat_FCM(Bundle extras, FragmentTransaction transaction) {
        if (extras != null) {
            int curID = extras.getInt("curID");
            fragmentTransition(curID);
        } else {
            transaction.replace(R.id.frameLayout, fragmentHome).commit();

        }
    }

    private void fragmentTransition(int curID) {
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        switch (curID) {
            case R.id.home:
                if (fragmentHome == null) {
                    fragmentHome = new FragmentHome();
                    transaction.add(R.id.frameLayout, fragmentHome, "fragmentHome");
                }

                if (fragmentHome != null) transaction.show(fragmentHome);
                if (fragmentSearch != null) transaction.hide(fragmentSearch);
                if (fragmentChat != null) transaction.hide(fragmentChat);
                if (fragmentMyPage != null) transaction.hide(fragmentMyPage);

                transaction.addToBackStack("fragmentHome");
                getSupportActionBar().setDisplayShowTitleEnabled(true);
                getSupportActionBar().setTitle("Fragment Home");
                transaction.commit();
                map.clear();
                map.put(userID, FieldValue.serverTimestamp());
                docRef.set(map);

                break;
            case R.id.searchItem:

                if (fragmentSearch == null) {
                    fragmentSearch = new FragmentSearch();
                    transaction.add(R.id.frameLayout, fragmentSearch, "fragmentSearch");
                }
                if (fragmentSearch != null) transaction.show(fragmentSearch);
                if (fragmentHome != null) transaction.hide(fragmentHome);
                if (fragmentChat != null) transaction.hide(fragmentChat);
                if (fragmentMyPage != null) transaction.hide(fragmentMyPage);
                transaction.addToBackStack("fragmentSearch");
                transaction.commit();
                map.clear();
                map.put(userID, FieldValue.serverTimestamp());
                docRef.set(map);

                break;
            case R.id.chat:

                if (fragmentChat == null) {
                    fragmentChat = new FragmentChat();
                    transaction.add(R.id.frameLayout, fragmentChat, "fragmentChat");
                }
                if (fragmentChat != null) transaction.show(fragmentChat);
                if (fragmentHome != null) transaction.hide(fragmentHome);
                if (fragmentSearch != null) transaction.hide(fragmentSearch);
                if (fragmentMyPage != null) transaction.hide(fragmentMyPage);
                transaction.addToBackStack("fragmentChat");
                transaction.commit();
                map.clear();
                map.put(userID, FieldValue.serverTimestamp());
                docRef.set(map);
                break;
            case R.id.my_page:
                if (fragmentMyPage == null) {
                    fragmentMyPage = new FragmentMyPage();
                    transaction.add(R.id.frameLayout, fragmentMyPage, "fragmentMyPage");
                }
                if (fragmentMyPage != null) transaction.show(fragmentMyPage);
                if (fragmentHome != null) transaction.hide(fragmentHome);
                if (fragmentSearch != null) transaction.hide(fragmentSearch);
                if (fragmentChat != null) transaction.hide(fragmentChat);
                transaction.addToBackStack("fragmentMyPage");
                transaction.commit();
                map.clear();
                map.put(userID, FieldValue.serverTimestamp());
                docRef.set(map);
                break;
        }
    }

    public Fragment getActiveFragment() {
        if (getSupportFragmentManager().getBackStackEntryCount() == 0) {
            return null;
        }
        String tag = getSupportFragmentManager().getBackStackEntryAt(getSupportFragmentManager().getBackStackEntryCount() - 1).getName();
        return getSupportFragmentManager().findFragmentByTag(tag);
    }

    private float dpTOPx(float i) {
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, i, metrics);
    }

    private void detectKeyboardUpDown() {
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
    }

    @Override
    public void onBackPressed() {//뒤로가기 버튼 클릭시 종료
        backPressCloseHandler.onBackPressed();
    }

    class ItemSelectedListener implements BottomNavigationView.OnNavigationItemSelectedListener {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
            curID = menuItem.getItemId();
            fragmentTransition(curID);
            return true;
        }
    }
}
