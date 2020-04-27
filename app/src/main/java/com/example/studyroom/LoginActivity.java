package com.example.studyroom;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class LoginActivity extends AppCompatActivity {
    private BackPressCloseHandler backPressCloseHandler = new BackPressCloseHandler(this);

    private static String TAG = "DataBase";
    CheckBox checkBox;
    String id;
    String pw;
    Button login_button;
    TextView edittext_id;
    TextView edittext_pw;
    TextView create_account;
    boolean id_count = false;
    boolean pw_count = false;
    FirebaseFirestore db;
    Set<String> set;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        // Access a Cloud Firestore instance from your Activity
        db = FirebaseFirestore.getInstance();
        // Create a new user with a first and last name
        edittext_id = findViewById(R.id.id_edittext);
        edittext_pw = findViewById(R.id.password_edittext);
        id = edittext_id.getText().toString();
        pw = edittext_pw.getText().toString();
        login_button = findViewById(R.id.login_button);
        create_account = findViewById(R.id.create_account_textview);
        login_button.setClickable(false);
        set = new HashSet();

//        final DocumentReference docRef = db.collection("Users").document("A2");
//        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
//            @Override
//            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
//                if (task.isSuccessful()) {
//                    DocumentSnapshot document = task.getResult();
//                    if (document.exists()) {
//                        userID = (String) document.get("userID");
//                        Log.d(TAG, "DocumentSnapshot data name : " + document.get("name"));
//                        Log.d(TAG, "DocumentSnapshot data : " + document.getData());
//                        document.getData().get(userID);
//                        Log.d(TAG, "userID : " + userID);
//                    } else {
//                        Log.d(TAG, "No such document");
//                    }
//                } else {
//                    Log.d(TAG, "get failed with ", task.getException());
//                }
//            }
//        });

        checkBox = findViewById(R.id.checkbox);
        checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkBox.isChecked()) {
                    Log.d(TAG, "check box was clicked");
                    SharedPreferences preferences = getSharedPreferences("auto", MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putBoolean("auto", true).commit();
                } else {
                    Log.d(TAG, "check bax was not clickced");
                }
            }
        });
        edittext_pw.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Log.d(TAG, "pw 변하고있음");
                Log.d(TAG, "CharSequence : " + s);
                if (s.length() > 0)
                    pw_count = true;
                else
                    pw_count = false;
            }
            @Override
            public void afterTextChanged(Editable s) {
                if (id_count && pw_count) {
                    login_button.setClickable(true);
                    Log.d(TAG,"login_button is clickable");
                    login_click();
                } else login_button.setClickable(false);

                pw = edittext_pw.getText().toString();

            }
        });
        edittext_id.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Log.d(TAG, "id 변하고있음");
                Log.d(TAG, "CharSequence : " + s);
                if (s.length() > 0) id_count = true;
                else
                    id_count = false;
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (id_count && pw_count) {
                    login_button.setClickable(true);
                    login_click();
                } else
                    login_button.setClickable(false);
                id = edittext_id.getText().toString();
                Log.d(TAG, "clickable : " + login_button.isClickable());
            }
        });
        create_account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "create_account_button was clicked");
                Intent intent = new Intent(getApplicationContext(), CreateAccountActivity.class);
                startActivity(intent);

            }
        });


    }
    @Override
    public void onBackPressed() {//뒤로가기 버튼 클릭시 종료
        backPressCloseHandler.onBackPressed();
    }
    public void login_click() {
        login_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final DocumentReference docRef = db.collection("Users").document(id);//user컬렉션에서 id_edittext.getText().toString() 란 문서를 참조한다.
                docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {//올바른 아이디
                                if (id.equals(document.get("userID")) && pw.equals(document.get("userPW"))) {
                                    Log.d(TAG, "아이디와 비밀번호가 일치합니다.");
                                    if (checkBox.isChecked()) {
                                        set.add(id);//id
                                        set.add(pw);//pw
                                        SharedPreferences preferences = getSharedPreferences("user", MODE_PRIVATE);
                                        SharedPreferences.Editor editor = preferences.edit();
                                        editor.putStringSet("user", set);
                                    }
                                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                    startActivity(intent);
                                    LoginActivity.this.finish();
                                } else {
                                    Toast.makeText(getApplicationContext(), "아이디와 비밀번호가 일치하지 않습니다.", Toast.LENGTH_SHORT).show();
                                }

                            } else {
                                Toast.makeText(getApplicationContext(), "아이디가 올바르지 않습니다.", Toast.LENGTH_SHORT).show();
                            }
                        } else {//실패
                            Log.d(TAG, "get failed with ", task.getException());
                        }
                    }
                });

            }
        });
    }

}
