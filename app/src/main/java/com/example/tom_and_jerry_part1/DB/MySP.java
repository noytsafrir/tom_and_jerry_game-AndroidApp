package com.example.tom_and_jerry_part1.DB;

import android.content.Context;
import android.content.SharedPreferences;

public class MySP {

    private static final String DB_FILE = "DB_FILE";
    private static MySP mySP_Instance = null;
    private SharedPreferences preferences;

    private MySP(Context context){
        preferences = context.getApplicationContext().getSharedPreferences(DB_FILE, Context.MODE_PRIVATE);
//        preferences = context.getSharedPreferences(DB_FILE,Context.MODE_PRIVATE);
    }

    public static void init(Context context) {
        if (mySP_Instance == null) {
            mySP_Instance = new MySP(context);
        }
    }

    public static MySP getInstance(){
        return mySP_Instance;
    }


    public String getString(String key,String def){
        return preferences.getString(key,def);
    }

    public void putString(String key,String value){
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(key,value);
        editor.apply();
    }
}
