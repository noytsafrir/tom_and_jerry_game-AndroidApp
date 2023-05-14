package com.example.tom_and_jerry_part1.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Bundle;

import com.bumptech.glide.Glide;
import com.example.tom_and_jerry_part1.R;
import com.example.tom_and_jerry_part1.Utils.My_Screen_Utils;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.switchmaterial.SwitchMaterial;

import android.widget.CompoundButton;

import im.delight.android.location.SimpleLocation;

public class Activity_Home_Page extends AppCompatActivity {
    private static final int MY_PERMISSIONS_REQUEST_LOCATION = 123;
    private AppCompatImageView  game_IMG_back;
     private AppCompatImageView game_IMG_back_top;
     private AppCompatImageView game_IMG_back_title;
    private MaterialButton      menu_BTN_top10;
    private MaterialButton      menu_BTN_playAgain;
    private SwitchMaterial menu_SW_sensor;
    private SwitchMaterial menu_SW_faster;

    private boolean isFasterOn = false; //true = play fast mode // false = play slow mode
    private boolean isSensorOn = false;//true = play with sensor // false = play without sensor- with buttons
    public SimpleLocation location;
    private double lat = 0.0;
    private double lng = 0.0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
        My_Screen_Utils.hideSystemUI(this);
        location = new SimpleLocation(this);
        startView();
        initPictures();
        requestLocationPermission(location);
        changeFastMode();
        changeSensorMode();
        initButtonsListeners();
    }

    private void initButtonsListeners() {
        menu_BTN_top10.setOnClickListener(v -> goToTop10());
        menu_BTN_playAgain.setOnClickListener(v -> goToNewGame());
    }
    private void changeFastMode() {
        menu_SW_faster.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean fasterModeOn) {
                isFasterOn = fasterModeOn;
            }
        });
    }
    private void changeSensorMode(){
        menu_SW_sensor.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean sensorOn) {
                isSensorOn = sensorOn;
            }
        });
    }

    private void goToTop10() {
        Intent intent = new Intent(this,Top10_score.class);
        startActivity(intent);
        finish();
    }

    private void goToNewGame(){
        Intent intent = new Intent(this,Activity_Game.class);
        intent.putExtra(Activity_Game.KEY_DELAY, isFasterOn);
        intent.putExtra(Activity_Game.KEY_SENSOR, isSensorOn);
        intent.putExtra(Activity_Game.KEY_LNG,lng);
        intent.putExtra(Activity_Game.KEY_LAT,lat);
        startActivity(intent);
        finish();
    }

    private void requestLocationPermission(SimpleLocation location){
        // Check if the app has permission to access the device's location
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                !=PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Request permission to access the location
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    MY_PERMISSIONS_REQUEST_LOCATION);
        }
        getLatAndLng(location);
    }

    private void getLatAndLng(SimpleLocation location){
        // Check if the app has permission to access the device's location
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            location.beginUpdates();
            this.lat = location.getLatitude();
            this.lng = location.getLongitude();
        }
    }

    private void startView() {
        game_IMG_back = findViewById(R.id.game_IMG_back);
        game_IMG_back_top   = findViewById(R.id.game_IMG_back_top);
        game_IMG_back_title = findViewById(R.id.game_IMG_back_title);
        menu_BTN_top10 = findViewById(R.id.menu_BTN_top10);
        menu_BTN_playAgain = findViewById(R.id.menu_BTN_playAgain);
        menu_SW_sensor = findViewById(R.id.menu_SW_sensor);
        menu_SW_faster = findViewById(R.id.menu_SW_faster);
    }

    private void initPictures() {
        Glide
                .with(this)
                .load(R.drawable.img_background_red)
                .into(game_IMG_back);

        Glide
                .with(this)
                .load(R.drawable.img_tom_n_jerry)
                .into(game_IMG_back_top);

        Glide
                .with(this)
                .load(R.drawable.img_tom_and_jerry_title)
                .into(game_IMG_back_title);
    }
}