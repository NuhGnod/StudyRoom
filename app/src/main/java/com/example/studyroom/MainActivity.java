package com.example.studyroom;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {
    private BackPressCloseHandler backPressCloseHandler = new BackPressCloseHandler(this);
    static Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
//
//        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
//        if (user != null) {
//            // Name, email address, and profile photo Url
//            String name = user.getDisplayName();
//            String email = user.getEmail();
//            Uri photoUrl = user.getPhotoUrl();
//
//            // Check if user's email is verified
//            boolean emailVerified = user.isEmailVerified();
//
//            // The user's ID, unique to the Firebase project. Do NOT use this value to
//            // authenticate with your backend server, if you have one. Use
//            // FirebaseUser.getIdToken() instead.
//            String uid = user.getUid();
//        }

        Button button = findViewById(R.id.go_room1_button);
        Button button2 = findViewById(R.id.go_room1_button2);
        Button button3 = findViewById(R.id.go_room1_button3);
        Button[] buttons = new Button[3];
        buttons[0] = button;
        buttons[1] = button2;
        buttons[2] = button3;
        for (int i = 0; i < 3; i++) {
            final int finalI = i;
            buttons[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getApplicationContext(), seatActivity.class);
                    intent.putExtra("room_number", finalI);
                    startActivity(intent);
                }
            });

        }
    }


    @Override
    public void onBackPressed() {//뒤로가기 버튼 클릭시 종료
        backPressCloseHandler.onBackPressed();
    }
}
