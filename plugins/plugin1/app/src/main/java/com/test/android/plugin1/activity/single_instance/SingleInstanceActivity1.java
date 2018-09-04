package com.test.android.plugin1.activity.single_instance;

import com.test.android.plugin1.activity.BaseActivity;

import android.content.Intent;

/**
 * des:
 * author: libingyan
 * Date: 18-9-4 12:42
 */
public class SingleInstanceActivity1 extends BaseActivity {

    @Override
    protected String getActivityTitle() {
        return "这里是SingleInstance类型的Acitivity";
    }

    @Override
    protected String getButtonTitle() {
        return "back";
    }

    @Override
    protected void start() {
        finish();
    }
}
