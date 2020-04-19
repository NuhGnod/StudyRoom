package com.example.studyroom;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private BackPressCloseHandler backPressCloseHandler = new BackPressCloseHandler(this);
    private static String TAG = "DataBase";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        // Create a new user with a first and last name
        CollectionReference colRef = db.collection("citieas");
        if(colRef != null){
            Log.d(TAG, "not null");
        }else{
            Log.d(TAG, "null");
        }
        Log.d(TAG, colRef.getId());
        Map<String, Object> city = new HashMap<>();
        city.put("name", "Los Angeles");
        city.put("state", "CA");
        city.put("country", "USA");
        Map<String, Object> home = new HashMap<>();

        db.collection("citiess").document("LA")
                .set(city)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "DocumentSnapshot successfully written!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error writing document", e);
                    }
                });
        db.collection("home").document("namwon")
                .set(home)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "DocumentSnapshot successfully written!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error writing document", e);
                    }
                });
        final DocumentReference docRef = db.collection("cities").document("LA");
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.d(TAG, "DocumentSnapshot data name : " + document.get("name"));
                        Log.d(TAG, "DocumentSnapshot data : " + document.getData());
                    } else {
                        Log.d(TAG, "No such document");
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });

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
        Button logout = findViewById(R.id.logout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences login = getSharedPreferences("auto", MODE_PRIVATE);
                SharedPreferences.Editor editor = login.edit();
                editor.clear().commit();
            }
        });
    }


    @Override
    public void onBackPressed() {//뒤로가기 버튼 클릭시 종료
        backPressCloseHandler.onBackPressed();
    }
}
