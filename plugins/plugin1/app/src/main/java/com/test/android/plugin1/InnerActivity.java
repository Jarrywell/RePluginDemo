package com.test.android.plugin1;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class InnerActivity extends AppCompatActivity {

    private final static String TAG = "InnerActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inner1);
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
