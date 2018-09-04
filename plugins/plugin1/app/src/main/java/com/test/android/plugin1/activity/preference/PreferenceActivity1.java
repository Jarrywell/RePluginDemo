package com.test.android.plugin1.activity.preference;

import com.test.android.plugin1.R;

import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.support.annotation.Nullable;

/**
 * des:
 * author: libingyan
 * Date: 18-9-4 20:18
 */
public class PreferenceActivity1 extends PreferenceActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.pref_headers);
    }

}
