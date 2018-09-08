package com.test.android.plugin2;

import com.android.test.utils.DLog;

import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        Intent intent = new Intent(MainActivity.this, Plugin2Service1.class);
        startService(intent);

        /**
         * 禁止插件内部调用开放给外部调用的so库
         */
        findViewById(R.id.id_btn_start_jni).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String result = PluginString.getString(0);
                DLog.i(TAG, "test jni string result: " + result);

                Toast.makeText(MainActivity.this, result, Toast.LENGTH_LONG).show();
            }
        });

        findViewById(R.id.id_btn_start_receiver_service).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Plugin2BroadcastReceiver.ACTION);
                intent.putExtra("extra", "this is extra test!!!");
                sendBroadcast(intent);
            }
        });

        /**
         * 初始化参数
         */
        initJob();

        findViewById(R.id.id_btn_start_job_service).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scheduleJob();
            }
        });

        findViewById(R.id.id_btn_cancel_job_service).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancelAllJobs();
            }
        });



    }


    //job service
    public static final String WORK_DURATION_KEY = TAG + ".WORK_DURATION_KEY";
    private int mJobId = 100; //递增
    ComponentName mServieComponent;// 这就是我们的jobservice组件了
    private void initJob() {
        mServieComponent = new ComponentName(this, Plugin2JobService.class);

    }


    private void scheduleJob() {
        //开始配置JobInfo
        JobInfo.Builder builder = new JobInfo.Builder(mJobId++, mServieComponent);

        //设置任务的延迟执行时间(单位是毫秒)
        builder.setMinimumLatency(1 * 1000);

        //设置任务最晚的延迟时间。如果到了规定的时间时其他条件还未满足，你的任务也会被启动。
        builder.setOverrideDeadline(20 * 1000);

        //让你这个任务只有在满足指定的网络条件时才会被执行
        builder.setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY);

        //你的任务只有当用户没有在使用该设备且有一段时间没有使用时才会启动该任务。
        builder.setRequiresDeviceIdle(false);

        //告诉你的应用，只有当设备在充电时这个任务才会被执行。
        builder.setRequiresCharging(false);

        // Extras, work duration.
        PersistableBundle extras = new PersistableBundle();
        extras.putLong(WORK_DURATION_KEY, 2 * 1000);
        builder.setExtras(extras);

        JobScheduler mJobScheduler = (JobScheduler) getSystemService(Context.JOB_SCHEDULER_SERVICE);
        // 这里就将开始在service里边处理我们配置好的job
        final int result = mJobScheduler.schedule(builder.build());
        DLog.i(TAG, "schedule() result: " + result);

        //mJobScheduler.schedule(builder.build())会返回一个int类型的数据
        //如果schedule方法失败了，它会返回一个小于0的错误码。否则它会返回我们在JobInfo.Builder中定义的标识id

    }

    // 当用户点击取消所有时执行
    public void cancelAllJobs() {
        JobScheduler mJobScheduler = (JobScheduler) getSystemService(Context.JOB_SCHEDULER_SERVICE);
        mJobScheduler.cancelAll();
        Toast.makeText(MainActivity.this, "cancel of job scheduler!!!", Toast.LENGTH_SHORT).show();
    }
}
