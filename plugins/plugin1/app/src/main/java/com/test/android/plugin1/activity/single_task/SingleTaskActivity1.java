package com.test.android.plugin1.activity.single_task;

import com.test.android.plugin1.activity.BaseActivity;

import android.content.Intent;

/**
 * des:
 * author: libingyan
 * Date: 18-9-4 12:38
 */
public class SingleTaskActivity1 extends BaseActivity {

    @Override
    protected String getActivityTitle() {
        return "这里是SingleTask类型的Acitivity";
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
