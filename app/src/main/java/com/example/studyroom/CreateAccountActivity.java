package com.example.studyroom;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class CreateAccountActivity extends AppCompatActivity {
    LinearLayout nextLayout;
    TextView nextTextview;
    EditText id_edittext;
    EditText pw_edittext;
    EditText check_pw_edittext;
    EditText nickname_edittext;
    Button check_id;
    EditText userName_edittext;
    EditText userNumber_edittext;
    private String TAG = "StudyRoom";
    FirebaseFirestore db;
    Boolean complete = false;

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
        db = FirebaseFirestore.getInstance();
        userName_edittext = findViewById(R.id.userName);
        userNumber_edittext = findViewById(R.id.userNumber);
        check_id = findViewById(R.id.check_id);
        check_id.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userID = id_edittext.getText().toString();
                Log.d(TAG, "userID : " + userID);
                if (!userID.equals("")) {
                    final DocumentReference docRef = db.collection("users").document(userID);//user컬렉션에서 id_edittext.getText().toString() 란 문서를 참조한다.
                    docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()) {
                                DocumentSnapshot document = task.getResult();
                                if (document.exists()) {//이미 있는 아이디
                                    Toast.makeText(getApplicationContext(), "이미 존재하는 아이디입니다.", Toast.LENGTH_SHORT).show();
                                    Log.d(TAG, "이미 존재하는 아이디입니다.");
                                    complete = false;
                                } else {//없는 아이디, 사용 가능
                                    Toast.makeText(getApplicationContext(), "사용 가능한 아이디입니다.", Toast.LENGTH_SHORT).show();
                                    Log.d(TAG, "사용가능한 아이디입니다.");
                                    complete = true;
                                }
                            } else {//실패
                                Log.d(TAG, "get failed with ", task.getException());
                            }
                        }
                    });
                }
                if (userID.equals(""))
                    Toast.makeText(getApplicationContext(), "아이디를 입력해주세요.", Toast.LENGTH_SHORT).show();

            }
        });
        nextLayout.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Log.d(TAG, "touched");
                int action = event.getAction();
                String id = id_edittext.getText().toString();
                String pw = pw_edittext.getText().toString();
                String userNickName = nickname_edittext.getText().toString();
                String userName = userName_edittext.getText().toString();
                String userNumber = userNumber_edittext.getText().toString();
                String token = "";
                if (action == MotionEvent.ACTION_DOWN) {
                    nextTextview.setTextColor(Color.RED);
                }
                if (action == MotionEvent.ACTION_UP) {
                    nextTextview.setTextColor(Color.DKGRAY);

                    if (id.length() == 0) {
                        Toast.makeText(CreateAccountActivity.this, "아이디를 입력해주세요.", Toast.LENGTH_LONG).show();
                        return false;
                    }
                    if (pw.length() == 0) {
                        Toast.makeText(CreateAccountActivity.this, "비밀번호를 입력해주세요.", Toast.LENGTH_LONG).show();
                        return false;
                    }
                    if (userNickName.length() == 0) {
                        Toast.makeText(CreateAccountActivity.this, "닉네임을 입력해주세요.", Toast.LENGTH_LONG).show();
                        return false;
                    }
                    if (!pw.equals(check_pw_edittext.getText().toString())) {
                        Toast.makeText(CreateAccountActivity.this, "비밀번호가 맞지 않습니다. 다시 입력해주세요.", Toast.LENGTH_LONG).show();
                        check_pw_edittext.setText("");
                        return false;
                    }
                    Users users = new Users(id,pw,userName, userNumber, userNickName, token);

                    db.collection("users").document(id).set(users);

                    SharedPreferences preferences = getSharedPreferences("name", MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString("name", userName);

                    Intent intent = new Intent(CreateAccountActivity.this, LoginActivity.class);
                    startActivity(intent);
                    CreateAccountActivity.this.finish();
                }


                return true;
            }

        });


    }
}
