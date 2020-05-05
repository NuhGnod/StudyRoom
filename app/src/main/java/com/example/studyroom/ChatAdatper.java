package com.example.studyroom;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class ChatAdatper extends RecyclerView.Adapter<ChatAdatper.MyViewHolder> {
    private List<ChatData> mDataset;
    private String myNickName;

    public ChatAdatper(List<ChatData> mDataset, Context context, String myNickName) {
        this.mDataset = mDataset;
        this.myNickName = myNickName;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LinearLayout v = (LinearLayout) LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_item, parent, false);
        MyViewHolder vh = new MyViewHolder(v);
        return vh;

    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        ChatData chatData = this.mDataset.get(position);
        holder.name.setText(chatData.getNickname());
        holder.msg.setText(chatData.getContent());

        if (chatData.getNickname().equals(this.myNickName)) {
            holder.name.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_END);
            holder.msg.setTextAlignment((View.TEXT_ALIGNMENT_TEXT_END));
        } else {
            holder.msg.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_START);
            holder.name.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_START);
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
