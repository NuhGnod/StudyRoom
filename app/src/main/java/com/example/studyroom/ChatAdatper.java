package com.example.studyroom;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.zip.Inflater;

public class ChatAdatper extends RecyclerView.Adapter<ChatAdatper.MyViewHolder> {
    private List<ChatData> mDataset;
    private String myNickName;
    private String TAG = "ChatAdapterDebug";

    public ChatAdatper(List<ChatData> mDataset, Context context, String myNickName) {
        this.mDataset = mDataset;
        this.myNickName = myNickName;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        ConstraintLayout v = (ConstraintLayout) LayoutInflater.from(parent.getContext()).inflate(viewType, parent, false);
        MyViewHolder vh = new MyViewHolder(v);
        return vh;

    }

    @Override
    public int getItemViewType(int position) {
        ChatData chatData = this.mDataset.get(position);
        if (chatData.getNickname().equals(this.myNickName)) {
             return R.layout.chat_item_my;//내가 보내는 메시지
        } else {
            return R.layout.chat_item;//상대방이 보내는 메시지
        }    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        ChatData chatData = this.mDataset.get(position);
        Log.d(TAG,"mDataset .size()" + mDataset.size());
        Log.d(TAG, "position : " + position);
        holder.name.setText(chatData.getNickname());
        holder.msg.setText(chatData.getContent());
        Log.d(TAG, "chatData.getNickname() ; " + chatData.getNickname());
        Log.d(TAG, "chatAdapter.getNickname() ; " + myNickName);

        if (chatData.getNickname().equals(this.myNickName)) {
            holder.name.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_END);
            holder.msg.setTextAlignment((View.TEXT_ALIGNMENT_TEXT_END));
            Log.d(TAG, "where : " + "if is true");
        } else {
            holder.msg.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_START);
            holder.name.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_START);
            Log.d(TAG, "where : " + "if is false");

        }
    }

    @Override
    public int getItemCount() {
        return mDataset == null ? 0 : mDataset.size();

    }

    public ChatData getChat(int position) {
        return mDataset != null ? mDataset.get(position) : null;
    }

    public void addChat(ChatData chatData) {
        mDataset.add(chatData);
        notifyItemChanged(mDataset.size() - 1);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView msg;
        public TextView time;
        public TextView name;

        public MyViewHolder(@NonNull View itemView) {

            super(itemView);
            name = itemView.findViewById(R.id.nickname_chat_iem);
            msg = itemView.findViewById(R.id.chat_content_chat_item);
            time = itemView.findViewById(R.id.chat_cur_time_chat_item);
        }
    }
}
