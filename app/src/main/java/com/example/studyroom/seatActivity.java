package com.example.studyroom;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class seatActivity extends AppCompatActivity implements View.OnClickListener {

    Button[] items;//각 방의 좌석의 객체를 담을 리스트
    int[] id;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.room_1);

        Intent intent = getIntent();
        int room_number = intent.getIntExtra("room_number", 0);
        setContentView(R.layout.room_1 + (room_number));

        Log.d("CNTTT", "room_number : " + (room_number + 1) + "번 방");
        //각 room_? xml의 id값은 1번방 = 2131296293 으로 1 씩 증가한다. 1~4
        RelativeLayout relativeLayout = (RelativeLayout) getLayoutInflater().from(this).inflate(R.layout.room_1 + (room_number), null);
        Log.d("CNTTT", "xml id : " + R.layout.room_1);
        Log.d("CNTTT", "xml id : " + R.layout.room_2);
        Log.d("CNTTT", "xml id : " + R.layout.room_3);
        Log.d("CNTTT", "xml id : " + R.layout.room_4);
        if (R.layout.room_3 == (R.layout.room_1 + (room_number))) {
            Log.d("CNTTT", "same");
        }else{
            Log.d("CNTTT", "not same");
        }


        int cnt = relativeLayout.getChildCount();//room 레이아웃에 있는 뷰(좌석)의 총 갯수 리턴
        items = new Button[cnt];
        id = new int[cnt];
        generateButtonObject(cnt, relativeLayout, items, id);//room 레이아웃에 있는 버튼(뷰)(좌석) 객체를 만들어 ArrayList<Button>리턴
        Log.d("CNTTT", "무슨 xml ? : " + items[0].getText().toString());
        items[0] = findViewById(id[0]);
//        items[0].setOnClickListener(this);
//        items[0].setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Log.d("CNTTT", "items[0]'s test : " + items[0].getText());
//            }
//        });
        items[0].setOnClickListener(this);

    }

    //    }
//    static public RelativeLayout generateLayoutObject(int cnt){
//
//    }
    static public Button[] generateButtonObject(int cnt, RelativeLayout relativeLayout, Button[] items, int[] id) {
        for (int i = 0; i < cnt; i++) {
            int finalI = i;
            items[i] = ((Button) relativeLayout.getChildAt(finalI));
            int getID = items[i].getId();
            id[i] = getID;
        }
        return items;
    }

    @Override
    public void onClick(View v) {
        Log.d("CNTTTT", "클릭 : " + v.getId());
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
//        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP); android 스택에서 해당 액티비티 없애기
// 이 기능 -> 다른 액티비티에서 메인(홈)버튼을 누른경우 그 전까지 쌓인 액티비티를 모두 지운다? 이럴 때에 사용할 듯
        startActivity(intent);
//        finish();
    }

}
