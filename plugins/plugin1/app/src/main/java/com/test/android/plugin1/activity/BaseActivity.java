package com.test.android.plugin1.activity;

import com.test.android.plugin1.R;
import com.test.android.plugin1.activity.single_instance.SingleInstanceActivity1;
import com.test.android.plugin1.activity.single_task.SingleTaskActivity1;
import com.test.android.plugin1.activity.single_top.SingleTopActivity1;
import com.test.android.plugin1.activity.standard.StandardActivity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/**
 * des:
 * author: libingyan
 * Date: 18-9-3 20:22
 */
public abstract class BaseActivity extends AppCompatActivity {


    protected abstract String getActivityTitle();

    protected abstract String getButtonTitle();

    protected abstract void start();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_simple);

        TextView title = findViewById(R.id.id_title);
        title.setText(getActivityTitle());

        Button button = findViewById(R.id.id_btn_start);
        button.setText(getButtonTitle());

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                start();
            }
        });

        findViewById(R.id.id_btn_base_start_standard).setOnClickListener(
            new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(BaseActivity.this, StandardActivity.class));
                }
            });

        findViewById(R.id.id_btn_base_start_single_top).setOnClickListener(
            new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(BaseActivity.this, SingleTopActivity1.class));
                }
            });

        findViewById(R.id.id_btn_base_start_single_task).setOnClickListener(
            new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(BaseActivity.this, SingleTaskActivity1.class));
                }
            });

        findViewById(R.id.id_btn_base_start_single_instance).setOnClickListener(
            new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(BaseActivity.this, SingleInstanceActivity1.class));
                }
            });
    }
}
