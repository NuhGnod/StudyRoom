package com.example.studyroom;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private BackPressCloseHandler backPressCloseHandler = new BackPressCloseHandler(this);
    static Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
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
//        }
//        button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(getApplicationContext(), seatActivity.class);
//                startActivity(intent);
//            }
//        });
        }
    }

    @Override
    public void onBackPressed() {//뒤로가기 버튼 클릭시 종료
        backPressCloseHandler.onBackPressed();
    }
}
