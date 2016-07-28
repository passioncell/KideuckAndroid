package com.kidueck.Common;

import android.content.Context;
import android.content.SharedPreferences;

import com.kidueck.Activity.MainActivity;

/**
 * Created by system777 on 2016-07-14.
 */
public class UserInfo {
    static public int getUserId(){
        SharedPreferences pref = MainActivity.getInstace().getSharedPreferences("userId", Context.MODE_PRIVATE);
        return Integer.parseInt(pref.getString("userId", ""));
    }
}
