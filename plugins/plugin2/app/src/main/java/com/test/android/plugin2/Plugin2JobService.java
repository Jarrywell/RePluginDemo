package com.test.android.plugin2;

import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.Intent;
import android.os.AsyncTask;
import android.widget.Toast;

import com.android.test.utils.DLog;

public class Plugin2JobService extends JobService {
    private final String TAG = "Plugin2JobService";

    AsyncTask<JobParameters, Integer, String> mTask;



    @Override
    public boolean onStartJob(JobParameters params) {
        DLog.i(TAG, "onStartJob()");
        mTask = new JobTask(params).execute(params);

        //耗时
        return true;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        DLog.i(TAG, "onStopJob()");
        if (mTask != null && !mTask.isCancelled()) {
            mTask.cancel(false);
        }
        return false;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        DLog.i(TAG, "onCreate()");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        DLog.i(TAG, "onStartCommand()");
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        DLog.i(TAG, "onDestroy()");
    }


    private class JobTask extends AsyncTask<JobParameters, Integer, String> {

        private JobParameters mJobParameters;
        public JobTask(JobParameters parameters) {
            super();
            mJobParameters = parameters;
        }

        @Override
        protected String doInBackground(JobParameters... jobParameters) {
            try {
                Thread.sleep(2000);
            } catch (Exception e) {

            }
            return "this is job servie test!!!";
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            Toast.makeText(Plugin2JobService.this, s, Toast.LENGTH_LONG).show();

            jobFinished(mJobParameters, true);
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();

            jobFinished(mJobParameters, true);
        }


    }
}
