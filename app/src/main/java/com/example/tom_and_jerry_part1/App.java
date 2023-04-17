package com.example.tom_and_jerry_part1;

import android.app.Application;

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        My_Signal.init(this);
    }
}
