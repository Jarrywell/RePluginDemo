package com.test.android.plugin1.activity.intent_filter;

import com.test.android.plugin1.activity.BaseActivity;

/**
 * des:
 * author: libingyan
 * Date: 18-9-4 17:58
 */
public class IntentFlterActivity1 extends BaseActivity {

    @Override
    protected String getActivityTitle() {
        return "这里是通过Intent-Filter跳转过来的";
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
