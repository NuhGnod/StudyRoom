package com.example.studyroom;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ChatAdatper extends BaseAdapter {
    private Context context;
    private int layout;
    private ArrayList<ChatData> chatData;
    private LayoutInflater inflater;
    private String id;


    public ChatAdatper(Context context, int list, ArrayList<ChatData> arr, String id) {
        this.context = context;
        this.layout = list;
        this.chatData = arr;
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.id = id;

    }

    @Override
    public int getCount() {
        return chatData.size();
    }

    @Override
    public Object getItem(int position) {
        return chatData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            holder.name = convertView.findViewById()
        }
    }
}
