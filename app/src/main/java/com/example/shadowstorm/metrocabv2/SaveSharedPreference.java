package com.example.shadowstorm.metrocabv2;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

/**
 * Created by Shadow Storm on 7/15/2016.
 */

public class SaveSharedPreference
{
    static final String PREF_USER_NAME= "username";
    static final String PREF_PASS = "password";

    static SharedPreferences getSharedPreferences(Context ctx) {
        return PreferenceManager.getDefaultSharedPreferences(ctx);
    }

    public static void setUserName(Context ctx, String userName)
    {
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.putString(PREF_USER_NAME, userName);
        Log.e("LOGIN1113","1111");
        editor.commit();
    }
    public static void setPassword(Context ctx, String password)
    {
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.putString(PREF_PASS, password);
        Log.e("LOGIN1113","1111");
        editor.commit();
    }

    public static String getUserName(Context ctx)
    {Log.e("LOGIN1112","1111");
        return getSharedPreferences(ctx).getString(PREF_USER_NAME, "");


    }
    public static String getPassword(Context ctx)
    {Log.e("LOGIN1112","1111");
        return getSharedPreferences(ctx).getString(PREF_PASS, "");


    }


    public static void clearUserName(Context ctx)
    {
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.clear(); //clear all stored data
        Log.e("QQQQ","cleared data");
        editor.commit();
    }
    public static void clearPassword(Context ctx)
    {
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.clear(); //clear all stored data
        editor.commit();
    }
}