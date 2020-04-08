package com.example.studyroom;

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

public class seatActivity extends AppCompatActivity {

    Button button;
    static Handler handler = new Handler();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.seat_layout);

        ArrayList<Button> items = new ArrayList<>();//각 방의 좌석의 객체를 담을 리스트
        RelativeLayout relativeLayout = (RelativeLayout) getLayoutInflater().from(this).inflate(R.layout.room_1, null);

        int cnt = relativeLayout.getChildCount();//room 레이아웃에 있는 뷰(좌석)의 총 갯수 리턴
        generateButtonObject(cnt, relativeLayout, items);//room 레이아웃에 있는 버튼(뷰)(좌석) 객체를 만들어 ArrayList<Button>리턴
    }
    static public RelativeLayout generateLayoutObject(int cnt){

    }
    static public ArrayList<Button> generateButtonObject(int cnt, RelativeLayout relativeLayout, ArrayList<Button> items) {
        for (int i = 0; i < cnt; i++) {
            int finalI = i;
            items.add((Button) relativeLayout.getChildAt(finalI));
        }
        return items;
    }

}
