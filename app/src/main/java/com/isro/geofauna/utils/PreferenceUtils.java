package com.isro.geofauna.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.isro.geofauna.GeofaunaApp;
import com.isro.geofauna.data.DatabaseColumns;

import java.util.HashMap;

public class PreferenceUtils  {
    private static SharedPreferences mPrefs;
    private static SharedPreferences.Editor mPrefsEdit;

    public PreferenceUtils(Context context){
        //Empty constructor
        mPrefs = context.getSharedPreferences("Geofauna", Context.MODE_PRIVATE);
    }

    public static String getCollector(Context context){
        mPrefs = context.getSharedPreferences("Geofauna", Context.MODE_PRIVATE);

        return mPrefs.getString("Collector","");
    }

    public static void setCollector(Context context, String collector){
        mPrefs = context.getSharedPreferences("Geofauna", Context.MODE_PRIVATE);

        mPrefsEdit = mPrefs.edit();
        mPrefsEdit.putString(DatabaseColumns.collector,collector);
        mPrefsEdit.apply();
    }

    public static String getPhone(Context context){
        mPrefs = context.getSharedPreferences("Geofauna", Context.MODE_PRIVATE);

        return mPrefs.getString("Phone","");
    }

    public static void setPhone(Context context, String phone){
        mPrefs = context.getSharedPreferences("Geofauna", Context.MODE_PRIVATE);

        mPrefsEdit = mPrefs.edit();
        mPrefsEdit.putString("Phone", phone);
        mPrefsEdit.apply();
    }
}
