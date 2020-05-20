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

import androidx.annotation.LongDef;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.model.value.TimestampValue;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class FragmentChat extends Fragment {

    private RecyclerView mRecyclerView;
    public RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private List<ChatData> chatDataList;
    private String my_nickname;
    private String TAG = "FragmentChat_TAG";
    private EditText editText_chat;
    private Button button_send;
    private FirebaseFirestore db;
    private String curTime;
    private CollectionReference colRef;

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootview = (ViewGroup) inflater.inflate(R.layout.fragment_chat, container, false);
        button_send = rootview.findViewById(R.id.send_message_button);
        editText_chat = rootview.findViewById(R.id.message_edittext);
        db = FirebaseFirestore.getInstance();
        chatDataList = new ArrayList<>();
        SharedPreferences pref = getActivity().getSharedPreferences("userNickName", Context.MODE_PRIVATE);//해당 기기의 로그인된 아이디의 닉네임을 가져온다.
        my_nickname = pref.getString("userNickName", null);
        Log.d(TAG, "nickname : " + my_nickname);
        //        @@@나중에 document 수정 해야함
        colRef = db.collection("chat").document("2").collection("message");
//        @@@나중에 document "2" -> nickname 으로 수정 해야함
        button_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String msg = editText_chat.getText().toString();
                SimpleDateFormat SDF = new SimpleDateFormat("yyyy-MM-dd aa h:mm:ss:SS");
                Date date = new Date(System.currentTimeMillis());
                curTime = SDF.format(new Date(System.currentTimeMillis()));
                String chat_time;
//                Log.d(TAG, "msg : " + msg);
                if (!msg.equals("")) {
                    ChatData chatData = new ChatData();
                    chatData.setNickname(my_nickname);
                    chatData.setContent(msg);
                    chatData.setTime(curTime);
                    chat_time = curTime.substring(11, 18);
                    chatData.setChat_time(chat_time);
                    //채팅 데이터 에 담기
//                    Log.d(TAG, "chat_time : " + chat_time);

//                    나중에 수정!!!!
                    db.collection("chat").document("2").collection("message").document(curTime).set(chatData);//firestore 에 데이터 저장
//                     나중에 document "2" -> nickname 으로 수정!!
                    ((ChatAdatper) mAdapter).addChat(chatData);//어댑터 데이터 전달
                    mAdapter.notifyDataSetChanged();//업데이트


                    Map<String, Object> map = new HashMap<>();
                    Timestamp timestamp = Timestamp.now();
                    map.put("dateExample", timestamp);
                    Timestamp.now().toDate();

                    db.collection("chat").document("2").collection("serverTimestamp").document(timestamp.toString()).set(map);
                    Log.d(TAG, "Server TimeStamp : " + timestamp.getNanoseconds() / 1000000);
                    Log.d(TAG, "Server TimeStamp to Date() : " + Timestamp.now().toDate());
                    db.collection("chat").document("2").collection("serverTimestamp").document(Timestamp.now().toDate().toString()).set(map);
                    db.collection("chat").document("2").collection("serverTimestamp").document(Timestamp.now().toDate().toString()).set(map);

                    editText_chat.setText("");
////                    Log.d(TAG, "chatDataList is : " + chatDataList.get(0).getTime());

                } else {
                    Log.d(TAG, "mag is null !! be careful ");
                }
            }
        });

        colRef.addSnapshotListener(new EventListener<QuerySnapshot>() {//firestore 에 chat 컬렉션에 message 컬렉션의 문서의 변화가 감지되면 실행
            //            상대방의 메시지를 받아옴
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                String nickname_Q;
                String chat_time_Q;
                String content_Q;

                if (e != null) {

                }
                if (queryDocumentSnapshots != null && queryDocumentSnapshots.isEmpty() == false) {

                    nickname_Q = queryDocumentSnapshots.getDocuments().get(queryDocumentSnapshots.size() - 1).get("nickname").toString();
                    chat_time_Q = queryDocumentSnapshots.getDocuments().get(queryDocumentSnapshots.size() - 1).get("chat_time").toString();
                    content_Q = queryDocumentSnapshots.getDocuments().get(queryDocumentSnapshots.size() - 1).get("content").toString();
                    ChatData chatData = new ChatData(nickname_Q, content_Q, chat_time_Q);
                    if (!nickname_Q.equals(my_nickname)) {//상대방의 chat이다.
                        Log.d(TAG, "curNickNmae : " + nickname_Q);//최신 nickname_Q 내용 수신
                        Log.d(TAG, "curChatTime : " + chat_time_Q);//최신 chat_time_Q 내용 수신
                        Log.d(TAG, "curContent : " + content_Q);//최신 content_Q 내용 수신
                        ((ChatAdatper) mAdapter).addChat(chatData);//어댑터 데이터 전달
                        mAdapter.notifyDataSetChanged();//업데이트
                    }


                } else {
                    Log.d(TAG, "Error");
                }
            }
        });

        mRecyclerView = rootview.findViewById(R.id.recyclerview);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        mRecyclerView.setLayoutManager(mLayoutManager);//fragment chat 화면에 들어올 때 초기 1번만 실행됨.
//        Log.d(TAG, "curTime : "+curTime);
        Log.d(TAG, "when this code run : ");
        mAdapter = new ChatAdatper(chatDataList, getActivity().getApplicationContext(), my_nickname, curTime);
        mRecyclerView.setAdapter(mAdapter);

        return rootview;
    }


}
