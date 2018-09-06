package com.test.android.plugin2;

import com.android.test.utils.DLog;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = new Intent(MainActivity.this, Plugin2Service1.class);
        startService(intent);

        findViewById(R.id.id_btn_start_jni).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String result = PluginString.getString(0);
                DLog.i(TAG, "test jni string result: " + result);

                Toast.makeText(MainActivity.this, result, Toast.LENGTH_LONG).show();
            }
        });
    }
}
