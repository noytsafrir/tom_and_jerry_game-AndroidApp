package com.example.tom_and_jerry_part1;

import android.app.Application;

import com.example.tom_and_jerry_part1.DB.MySP;
import com.example.tom_and_jerry_part1.Utils.My_Signal;

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        My_Signal.init(this);
        MySP.init(this);
    }
}
