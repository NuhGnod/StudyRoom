package com.example.studyroom;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

//import static android.content.Context.MODE_PRIVATE;

public class FragmentHome extends Fragment {
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
    }

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootview = (ViewGroup) inflater.inflate(R.layout.fragment_home, container, false);

        Button button = rootview.findViewById(R.id.go_room1_button);
        Button button2 = rootview.findViewById(R.id.go_room1_button2);
        Button button3 = rootview.findViewById(R.id.go_room1_button3);
        Button[] buttons = new Button[3];
        buttons[0] = button;
        buttons[1] = button2;
        buttons[2] = button3;

        for (int i = 0; i < 3; i++) {
            final int finalI = i;
            buttons[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity().getApplicationContext(), seatActivity.class);

                    intent.putExtra("room_number", finalI);
                    startActivity(intent);
                }
            });

        }
        Button logout = rootview.findViewById(R.id.logout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences login = getActivity().getSharedPreferences("auto", Context.MODE_PRIVATE);

                SharedPreferences.Editor editor = login.edit();
                editor.clear().commit();

                Intent intent = new Intent(getActivity().getApplicationContext(), LoginActivity.class);
                startActivity(intent);
                getActivity().finish();
            }
        });
        return rootview;
    }
}