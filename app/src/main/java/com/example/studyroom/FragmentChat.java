package com.example.studyroom;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.ArrayList;
import java.util.List;

public class FragmentChat extends Fragment {

    private RecyclerView mRecyclerView;
    public RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private List<ChatData> chatDataList;
    private String nickname = "nick2";
    private String TAG = "chat";
    private EditText editText_chat;
    private Button button_send;
    private FirebaseFirestore db;

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootview = (ViewGroup) inflater.inflate(R.layout.fragment_chat, container, false);
        button_send = rootview.findViewById(R.id.send_message_button);
        editText_chat = rootview.findViewById(R.id.message_edittext);
        db = FirebaseFirestore.getInstance();

        button_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String msg = editText_chat.getText().toString();
                if (msg != null) {
                    ChatData chatData = new ChatData();
                    chatData.setNickname(nickname);
                    chatData.setContent(msg);
                    db.collection("chat").document(nickname).collection("message").document().set(chatData);

                }
            }
        });
        mRecyclerView = rootview.findViewById(R.id.recyclerview);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        mRecyclerView.setLayoutManager(mLayoutManager);

        chatDataList = new ArrayList<>();
        mAdapter = new ChatAdatper(chatDataList, getActivity().getApplicationContext(), nickname);
        mRecyclerView.setAdapter(mAdapter);

        return rootview;
    }
}
