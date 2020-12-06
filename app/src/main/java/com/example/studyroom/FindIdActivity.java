package com.example.studyroom;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.nio.file.Files;

public class FindIdActivity extends AppCompatActivity {
    private Toolbar toolbar;

    private Button button;
    private EditText editText;
    private String phoneNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_id);
        toolbar = findViewById(R.id.toolbar_main);
        setSupportActionBar(toolbar);
        button = findViewById(R.id.receive_certifyNumber);
        editText = findViewById(R.id.phoneNumberForCertify);

        phoneNumber = editText.getText().toString();
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SmsManager sms = SmsManager.getDefault();
                String message = "인증번호";
                sms.sendTextMessage(phoneNumber, null, message, null, null);
            }
        });
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle("아이디 찾기");
    }
}
