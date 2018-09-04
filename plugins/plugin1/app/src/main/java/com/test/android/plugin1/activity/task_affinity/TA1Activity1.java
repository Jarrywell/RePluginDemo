package com.test.android.plugin1.activity.task_affinity;

import com.test.android.plugin1.activity.BaseActivity;

import android.content.Intent;

/**
 * des:
 * author: libingyan
 * Date: 18-9-4 20:31
 */
public class TA1Activity1 extends BaseActivity {

    @Override
    protected String getActivityTitle() {
        return "这里是single task模式的TA1Activity1, task affinity: ta1";
    }

    @Override
    protected String getButtonTitle() {
        return "跳转到TA1Activity2";
    }

    @Override
    protected void start() {
        startActivity(new Intent(this, TA1Activity2.class));
    }
}
