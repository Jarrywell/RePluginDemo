package com.test.android.plugin1.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Keep;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

/**
 * des: 宿主通过startActivityForResult()启动
 * author: libingyan
 * Date: 18-8-30 20:18
 */
@Keep
public class ForResultActivity extends AppCompatActivity {
    private final static String TAG = "ForResultActivity";

    final int RESULT_CODE = 0x0100;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = new Intent();
        intent.putExtra("extra", "this is plugin1 of ForResultActivity data!!!");
        setResult(RESULT_CODE, intent);

        finish();
    }

}
