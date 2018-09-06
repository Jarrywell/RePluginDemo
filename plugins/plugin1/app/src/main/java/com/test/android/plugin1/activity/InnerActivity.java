package com.test.android.plugin1.activity;

import com.android.test.utils.DLog;
import com.test.android.plugin1.R;

import android.os.Bundle;
import android.support.annotation.Keep;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;

/**
 * 插件内部的fragment使用不受影响,但在宿主中使用插件的fragment会受影响
 */
@Keep
public class InnerActivity extends FragmentActivity {

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
