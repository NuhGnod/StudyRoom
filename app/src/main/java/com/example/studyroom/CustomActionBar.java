package com.example.studyroom;

import android.app.Activity;


import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.LayoutInflater;
import android.view.View;

public class CustomActionBar  extends AppCompatActivity {
    private Activity activity;
    private androidx.appcompat.app.ActionBar actionBar;
    private Toolbar toolbar;
    public CustomActionBar(Activity _activity, ActionBar _actionBar) {
        this.activity = _activity;
        this.actionBar = _actionBar;
        this.toolbar = toolbar;
    }


    public void setActionBar() {

        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(false);
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayShowHomeEnabled(false);

        View mCustomView = LayoutInflater.from(activity).inflate(R.layout.activity_custom_action_bar, null);
        actionBar.setCustomView(mCustomView);
    }

}
