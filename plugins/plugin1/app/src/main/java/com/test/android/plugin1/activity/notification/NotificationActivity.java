package com.test.android.plugin1.activity.notification;

import com.test.android.plugin1.activity.BaseActivity;

/**
 * des:
 * author: libingyan
 * Date: 18-9-4 19:27
 */
public class NotificationActivity extends BaseActivity {

    @Override
    protected String getActivityTitle() {
        return "这是通知栏跳转过来的Activity";
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
