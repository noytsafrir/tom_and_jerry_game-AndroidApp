package com.example.tom_and_jerry_part1.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;

import com.bumptech.glide.Glide;
import com.example.tom_and_jerry_part1.Fragments.ListFragment;
import com.example.tom_and_jerry_part1.Fragments.MapFragment;
import com.example.tom_and_jerry_part1.Interfaces.CallBack_List;
import com.example.tom_and_jerry_part1.R;
import com.example.tom_and_jerry_part1.Utils.My_Screen_Utils;
import com.google.android.material.button.MaterialButton;

public class Top10_score extends AppCompatActivity {

    private AppCompatImageView game_IMG_back;
    private MapFragment     mapFragment;
    private ListFragment    listFragment;
    private FrameLayout     top10_FLO_map;
    private FrameLayout        top10_FLO_scores;
    private MaterialButton  top10_BTN_menu;

    CallBack_List callBack_list = new CallBack_List() {

        @Override
        public void setMapLocation(double lat, double lng, String playerName) {
            mapFragment.setMapLocation(lat, lng, playerName);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_top10_score);
        My_Screen_Utils.hideSystemUI(this);
        mapFragment = new MapFragment();
        listFragment = new ListFragment();
        listFragment.setCallBack_list(callBack_list);
        startView();
        initPictures();

        getSupportFragmentManager().beginTransaction().add(R.id.top10_FLO_map,mapFragment).commit();
        getSupportFragmentManager().beginTransaction().add(R.id.top10_FLO_scores,listFragment).commit();

        top10_BTN_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Top10_score.this , Activity_Home_Page.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void startView() {
        top10_FLO_map = findViewById(R.id.top10_FLO_map);
        top10_FLO_scores = findViewById(R.id.top10_FLO_scores);
        top10_BTN_menu = findViewById(R.id.top10_BTN_menu);
        game_IMG_back = findViewById(R.id.game_IMG_back);
    }

    private void initPictures() {
        Glide
                .with(this)
                .load(R.drawable.img_game_background)
                .into(game_IMG_back);
    }
}