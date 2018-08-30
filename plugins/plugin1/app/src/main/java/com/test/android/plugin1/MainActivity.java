package com.test.android.plugin1;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    private final static String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        DLog.i(TAG, "onCreate()");

        findViewById(R.id.id_btn_start_inner_activity).setOnClickListener(
            new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(MainActivity.this, InnerActivity.class));
                }
            });
    }

    @Override
    protected void onResume() {
        super.onResume();

        DLog.i(TAG, "onResume()");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        DLog.i(TAG, "onDestroy()");
    }
}
