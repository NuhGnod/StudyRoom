package com.example.studyroom;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class CreateAccountActivity extends AppCompatActivity {
    LinearLayout nextLayout;
    TextView nextTextview;
    EditText id_edittext;
    EditText pw_edittext;
    EditText check_pw_edittext;
    EditText nickname_edittext;
    private String TAG = "StudyRoom";

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);
        nextLayout = findViewById(R.id.create_account_next_layout);
        nextTextview = findViewById(R.id.next_textview_on_create_account);
        id_edittext = findViewById(R.id.create_account_id);
        pw_edittext = findViewById(R.id.create_account_pw);
        check_pw_edittext = findViewById(R.id.check_pw);
        nickname_edittext = findViewById(R.id.nickname);

        nextLayout.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Log.d(TAG, "touched");
                int action = event.getAction();

                if (action == MotionEvent.ACTION_DOWN) {
                    nextTextview.setTextColor(Color.RED);
                }
                if (action == MotionEvent.ACTION_UP) {
                    nextTextview.setTextColor(Color.DKGRAY);

                    if (id_edittext.getText().toString().length() == 0) {
                        Toast.makeText(CreateAccountActivity.this, "아이디를 입력해주세요.", Toast.LENGTH_LONG).show();
                        return false;
                    }
                    if (pw_edittext.getText().toString().length() == 0) {
                        Toast.makeText(CreateAccountActivity.this, "비밀번호를 입력해주세요.", Toast.LENGTH_LONG).show();
                        return false;
                    }
                    if (nickname_edittext.getText().toString().length() == 0) {
                        Toast.makeText(CreateAccountActivity.this, "닉네임을 입력해주세요.", Toast.LENGTH_LONG).show();
                        return false;
                    }
                    if (!pw_edittext.getText().toString().equals(check_pw_edittext.getText().toString())) {
                        Toast.makeText(CreateAccountActivity.this, "비밀번호가 맞지 않습니다. 다시 입력해주세요.", Toast.LENGTH_LONG).show();
                        check_pw_edittext.setText("");
                        return false;
                    }
                    Intent intent = new Intent(CreateAccountActivity.this, LoginActivity.class);
                    startActivity(intent);
                    CreateAccountActivity.this.finish();
                }


                return true;
            }

        });


    }
}
