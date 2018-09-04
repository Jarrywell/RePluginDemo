package com.test.android.plugin1.activity.standard;

import com.test.android.plugin1.activity.BaseActivity;
import com.test.android.plugin1.activity.single_top.SingleTopActivity1;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

/**
 * des:
 * author: libingyan
 * Date: 18-9-3 20:22
 */
public class StandardActivity extends BaseActivity {

    @Override
    protected String getActivityTitle() {
        return "这里是standard模式的activity";
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
