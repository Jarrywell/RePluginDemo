package com.android.test.host.demo;

import android.content.res.Resources;
import android.content.res.XmlResourceParser;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.qihoo360.replugin.RePlugin;

public class DemoResourceActivity extends AppCompatActivity {

    private static String TAG = "DemoResourceActivity";

    private ImageView mPluginImageView;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_plugin_resource);
        mPluginImageView = findViewById(R.id.id_img_plugin_drawable);

        findViewById(R.id.id_btn_drawable).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Resources resources = RePlugin.fetchResources(PluginManager.PLUGIN2_NAME);

                final int id = resources.getIdentifier("test_plugin2_img", "drawable",
                        "com.test.android.plugin2");
                if (id != 0) {
                    final Drawable drawable = resources.getDrawable(id);
                    if (drawable != null) {
                        mPluginImageView.setImageDrawable(drawable);
                    }
                }
            }
        });

        findViewById(R.id.id_btn_layout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Resources resources = RePlugin.fetchResources(PluginManager.PLUGIN2_NAME);
                final int id = resources.getIdentifier("layout_test_plugin", "layout",
                        "com.test.android.plugin2");
                if (id != 0) {
                    ViewGroup parent = findViewById(R.id.id_layout_plugin);
                    if (parent.getChildCount() > 0) {
                        parent.removeAllViews();
                    }
                    XmlResourceParser parser = resources.getLayout(id);
                    View result = getLayoutInflater().inflate(parser, parent);

                    /**
                     * 不能加载
                     */
                    //View result = getLayoutInflater().inflate(id, parent);

                    Log.i(TAG, "inflate() id: " + id + ", result: " + result);
                }
            }
        });
    }
}
