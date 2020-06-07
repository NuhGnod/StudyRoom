package com.example.studyroom.FCM;//package com.example.studyroom.FCM;

import android.util.Log;

import com.firebase.jobdispatcher.JobParameters;
import com.firebase.jobdispatcher.JobService;

public class MyJobService extends JobService {
    private  static  final  String TAG = "MyJobService_TAG";
    @Override
    public boolean onStartJob(JobParameters job) {
        Log.d(TAG, "Performing long running task in scheduled job");

        return false;
    }

    @Override
    public boolean onStopJob(JobParameters job) {
        return false;
    }
}
