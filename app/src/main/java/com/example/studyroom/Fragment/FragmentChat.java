package com.example.studyroom.Fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.studyroom.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.api.LogDescriptor;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    private DocumentReference docRef;
    private String userID;

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
        docRef = db.collection("chat").document("2");
//        @@@나중에 document "2" -> nickname 으로 수정 해야함
        SharedPreferences pref1 = getActivity().getSharedPreferences("userID", Context.MODE_PRIVATE);
        userID = pref1.getString("userID", null);

        getChatList();

        button_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "ServerTimeStamp : " + FieldValue.serverTimestamp());
                Log.d(TAG, "size == " + chatDataList.size());
                String msg = editText_chat.getText().toString();
                SimpleDateFormat SDF = new SimpleDateFormat("yyyy-MM-dd aa h:mm:ss:SS");
                curTime = SDF.format(new Date(System.currentTimeMillis()));
                final ChatData chatData = new ChatData();
                String chat_time;
//                Log.d(TAG, "msg : " + msg);
                if (!msg.equals("")) {
                    chatData.setNickname(my_nickname);
                    chatData.setContent(msg);
                    chatData.setTime(curTime);
                    docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()) {
                                DocumentSnapshot document = task.getResult();
                                if (document.exists()) {
                                    chatData.setRead_status(document.get(userID).toString());
                                    Log.d(TAG, "read Value : " + chatData.getRead_status());
                                } else {
                                    Log.d(TAG, "No such document");
                                }
                            } else {
                                Log.d(TAG, "get  failed with ", task.getException());
                            }
                        }
                    });

                    chat_time = curTime.substring(11, 18);
                    chatData.setChat_time(chat_time);
                    //채팅 데이터 에 담기
//                    Log.d(TAG, "chat_time : " + chat_time);

//                    나중에 수정!!!!
                    db.collection("chat").document("2").collection("message").document(curTime).set(chatData);//firestore 에 데이터 저장
//                     나중에 document "2" -> nickname 으로 수정!!
                    Map<String, Object> recent_chat = new HashMap<String, Object>();
                    recent_chat.put("from", my_nickname);
                    recent_chat.put("content", msg);
                    recent_chat.put("curTime", curTime);
                    db.collection("chat").document("2").collection("recent_chat").document("recent_chat").set(recent_chat);

                    ((ChatAdatper) mAdapter).addChat(chatData);//어댑터 데이터 전달
                    mAdapter.notifyDataSetChanged();//업데이트
                    mRecyclerView.scrollToPosition(chatDataList.size() - 1);
                    Log.d(TAG, "chatDataList.size() is : " + String.valueOf(chatDataList.size() - 1));
                    Log.d(TAG, "chatDataList.size.get : " + chatDataList.get(chatDataList.size() - 1).getContent());
                    Map<String, Object> map = new HashMap<>();
                    Timestamp timestamp = Timestamp.now();
                    map.put("dateExample", timestamp);
                    Timestamp.now().toDate();

                    Log.d(TAG, "TimeStamp is : " + timestamp);
                    db.collection("chat").document("2").collection("serverTimestamp").document(timestamp.toString()).set(map);
                    Log.d(TAG, "Server TimeStamp : " + timestamp.getNanoseconds() / 1000000);
                    Log.d(TAG, "Server TimeStamp to Date() : " + Timestamp.now().toDate());
                    db.collection("chat").document("2").collection("serverTimestamp").document(Timestamp.now().toDate().toString()).set(map);
                    Map<String, Object> update = new HashMap<>();
                    update.put("timestamp", FieldValue.serverTimestamp());
                    Log.d(TAG, "update : " + update.get("timestamp"));
                    db.collection("chat").document("2").collection("serverTimestamp")
                            .document(FieldValue.serverTimestamp().toString()).set(update);
                    editText_chat.setText("");
////                    Log.d(TAG, "chatDataList is : " + chatDataList.get(0).getTime());

                } else {
                    Log.d(TAG, "msg is null !! be careful ");
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
                Log.d(TAG, "Error : " + queryDocumentSnapshots.getDocuments());
                if (e != null) {
                }
                if (queryDocumentSnapshots != null && queryDocumentSnapshots.isEmpty() == false) {

                    nickname_Q = queryDocumentSnapshots.getDocuments().get(queryDocumentSnapshots.size() - 1).get("nickname").toString();
                    chat_time_Q = queryDocumentSnapshots.getDocuments().get(queryDocumentSnapshots.size() - 1).get("chat_time").toString();
                    content_Q = queryDocumentSnapshots.getDocuments().get(queryDocumentSnapshots.size() - 1).get("content").toString();
                    final String[] read_status_Q = {""};
                    docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()) {
                                DocumentSnapshot document = task.getResult();
                                if (document.exists()) {
                                    read_status_Q[0] = String.valueOf(document.get(userID));
                                } else {
                                    Log.d(TAG, "No such document");
                                }
                            } else {
                                Log.d(TAG, "get  failed with ", task.getException());
                            }
                        }
                    });
                    ChatData chatData = new ChatData(nickname_Q, content_Q, chat_time_Q, read_status_Q[0]);
                    if (!nickname_Q.equals(my_nickname)) {//상대방의 chat이다.
                        Log.d(TAG, "curNickNmae : " + nickname_Q);//최신 nickname_Q 내용 수신
                        Log.d(TAG, "curChatTime : " + chat_time_Q);//최신 chat_time_Q 내용 수신
                        Log.d(TAG, "curContent : " + content_Q);//최신 content_Q 내용 수신
                        Log.d(TAG, "read_status : " + read_status_Q[0]);
                        ((ChatAdatper) mAdapter).addChat(chatData);//어댑터 데이터 전달
                        mAdapter.notifyDataSetChanged();//업데이트
                    }


                } else {
                    Log.d(TAG, "Error");
                }
                mRecyclerView.scrollToPosition(chatDataList.size());
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
        mRecyclerView.scrollToPosition(chatDataList.size() - 1);

//        mRecyclerView.scrollToPosition(20);
        Log.d(TAG, "chatDataList.size() : " + (chatDataList.size()));
        return rootview;
    }

    private void getChatList() {
        final String[] recentTime = new String[1];
        DocumentReference documentReference = docRef.collection("recent_chat").document("recent_chat");
        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.d(TAG, "getInstanceId failed", task.getException());
                } else if (task.getResult().get("curTime") != null) {
                    recentTime[0] = String.valueOf(task.getResult().get("curTime"));
                    Log.d(TAG, "recentTIme : " + recentTime[0]);
                } else {
                    Log.d(TAG, "null");
                }
            }
        });
        Log.d(TAG, "??");
        colRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
//                    Log.d(TAG, recentTime[0]);
                    for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                        ChatData chatData = new ChatData();

                        if (documentSnapshot.getId().equals(recentTime[0])) {
                            Log.d(TAG, "curTIme is ; " + recentTime[0]);
                            Log.d(TAG, documentSnapshot.getId() + " => " + documentSnapshot.getData());
                            break;

                        }
                        chatData.setNickname(String.valueOf(documentSnapshot.getData().get("nickname")));
                        chatData.setChat_time(String.valueOf(documentSnapshot.getData().get("chat_time")));
                        chatData.setContent(String.valueOf(documentSnapshot.getData().get("content")));
                        ((ChatAdatper) mAdapter).addChat(chatData);//어댑터 데이터 전달
                        mAdapter.notifyDataSetChanged();//업데이트
                        Log.d(TAG, "size" + (chatDataList.size()));

//                        Log.d(TAG, documentSnapshot.getId() + " => " + documentSnapshot.getData());
                    }
                    mRecyclerView.scrollToPosition(chatDataList.size() - 1);


                }
            }
        });


    }

}



