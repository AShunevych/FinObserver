package com.ashunevich.finobserver.dashboard;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;

class DashboardUtilsSharedPref {
    protected Context mContext;
    protected SharedPreferences mSettings;
    protected SharedPreferences.Editor mEditor;

    @SuppressLint("CommitPrefEdits")
    public DashboardUtilsSharedPref(Context ctx, String prefFileName) {
        mContext = ctx;
        mSettings = mContext.getSharedPreferences(prefFileName,
                Context.MODE_PRIVATE);
        mEditor = mSettings.edit();
    }

    public void setValue(String key, String value) {
        mEditor.putString(key, value);
        mEditor.commit();
    }

    public String getValue(String key, String defaultValue) {
        return mSettings.getString(key, defaultValue);
    }

}
