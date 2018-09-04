package com.test.android.plugin1.activity.single_top;

import com.test.android.plugin1.activity.BaseActivity;

import android.content.Intent;

/**
 * des:
 * author: libingyan
 * Date: 18-9-3 20:44
 */
public class SingleTopActivity1 extends BaseActivity {

    @Override
    protected String getActivityTitle() {
        return "这里是SingleTop类型的Acitivity";
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
