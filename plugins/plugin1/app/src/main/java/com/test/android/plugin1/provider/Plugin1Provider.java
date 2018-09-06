package com.test.android.plugin1.provider;

import com.android.test.utils.DLog;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.Set;

/**
 * des:
 * author: libingyan
 * Date: 18-9-5 14:14
 */
public class Plugin1Provider extends ContentProvider {

    private final String TAG = "Plugin1Provider";

    @Override
    public boolean onCreate() {
        return false;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection,
        @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        return null;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        Set<String> keys = values.keySet();
        for (String key : keys) {
            DLog.i(TAG, "provider insert key: " + key + ", value: " + values.get(key));
        }
        Uri u = Uri.parse("a://provider ok");
        return u;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection,
        @Nullable String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection,
        @Nullable String[] selectionArgs) {
        return 0;
    }
}
