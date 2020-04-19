package com.example.studyroom;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class SplashActivity extends AppCompatActivity {
    Handler handler = new Handler();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                SharedPreferences login = getSharedPreferences("auto", MODE_PRIVATE);
                boolean isAuto = false;
                isAuto = login.getBoolean("auto", false);
                if (isAuto) {
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);
                    SplashActivity.this.finish();
                } else {
                    Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                    startActivity(intent);
                    SplashActivity.this.finish();
                }

            }
        }, 1000);
    }

    @Override
    public void onBackPressed() {

    }
}
