package com.example.studyroom;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class FragmentChat extends Fragment {

    private RecyclerView mRecyclerView;
    public RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private List<ChatData> chatDataList;
    private String nickname;
    private String TAG = "FragmentChat_TAG";
    private EditText editText_chat;
    private Button button_send;
    private FirebaseFirestore db;
    private String curTime;

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootview = (ViewGroup) inflater.inflate(R.layout.fragment_chat, container, false);
        button_send = rootview.findViewById(R.id.send_message_button);
        editText_chat = rootview.findViewById(R.id.message_edittext);
        db = FirebaseFirestore.getInstance();
        chatDataList = new ArrayList<>();
        SharedPreferences pref = getActivity().getSharedPreferences("userNickName", Context.MODE_PRIVATE);
        nickname = pref.getString("userNickName", null);
        button_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String msg = editText_chat.getText().toString();
                SimpleDateFormat SDF = new SimpleDateFormat("yyyy-MM-dd aa h:mm:ss");
                curTime = SDF.format(new Date(System.currentTimeMillis()));
                String chat_time;
                Log.d(TAG, "msg : " + msg);
                if (msg != null) {
                    ChatData chatData = new ChatData();
                    chatData.setNickname(nickname);
                    chatData.setContent(msg);
                    chatData.setTime(curTime);
                    chat_time = curTime.substring(11,18);
                    chatData.setChat_time(chat_time);

                    Log.d(TAG, "chat_time : " + chat_time);
                    db.collection("chat").document(nickname).collection("message").document(curTime).set(chatData);
                    ((ChatAdatper) mAdapter).addChat(chatData);
                    mAdapter.notifyDataSetChanged();
                    editText_chat.setText("");
                    Log.d(TAG, "chatDataList is : " + chatDataList.get(0).getTime());

                }
            }
        });
        mRecyclerView = rootview.findViewById(R.id.recyclerview);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        mRecyclerView.setLayoutManager(mLayoutManager);
//        Log.d(TAG, "curTime : "+curTime);

        mAdapter = new ChatAdatper(chatDataList, getActivity().getApplicationContext(), nickname, curTime);
        mRecyclerView.setAdapter(mAdapter);

        return rootview;
    }


}
