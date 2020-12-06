package com.example.studyroom.Fragment;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.example.studyroom.LoginActivity;
import com.example.studyroom.R;
import com.example.studyroom.seatActivity;
import com.readystatesoftware.viewbadger.BadgeView;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;

//import static android.content.Context.MODE_PRIVATE;

public class FragmentHome extends Fragment {
    private String TAG = "FragmentChat_TAG";

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
    }

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootview = (ViewGroup) inflater.inflate(R.layout.fragment_home, container, false);
        SharedPreferences pref = getActivity().getSharedPreferences("userNickName", Context.MODE_PRIVATE);
        String name = pref.getString("userNickName",null);
        TextView tv = rootview.findViewById(R.id.home);
        tv.setText(name + "!#@#");
        Button button = rootview.findViewById(R.id.go_room1_button);
        Button button2 = rootview.findViewById(R.id.go_room1_button2);
        Button button3 = rootview.findViewById(R.id.go_room1_button3);
        Button[] buttons = new Button[3];
        buttons[0] = button;
        buttons[1] = button2;
        buttons[2] = button3;

        String fileString = "hello StudyRoom";
        String FILENAME = "test.txt";
        File saveFile = new File(getActivity().getFilesDir() + "/chatdata");
        Log.i(TAG, "cur dir : " + getActivity().getFilesDir());
        if (!saveFile.exists()) {
            saveFile.mkdir();
        }
        try {
            Log.d(TAG, "cur dir:" + getActivity().getFilesDir().toString());
            FileOutputStream fos = getActivity().openFileOutput(FILENAME, Context.MODE_PRIVATE);
            fos.write(fileString.getBytes());
            fos.close();

            FileInputStream fis = getActivity().openFileInput(FILENAME);
            String line = new BufferedReader(new InputStreamReader(fis)).readLine();
            fis.close();
            Log.i(TAG, "file contents : " + line);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
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
        View target = rootview.findViewById(R.id.button3);
        BadgeView badgeView = new BadgeView(getActivity().getApplicationContext(), target);
        badgeView.setText("1");
        badgeView.show();
        return rootview;
    }
}