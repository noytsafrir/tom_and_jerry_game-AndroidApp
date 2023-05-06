package com.example.tom_and_jerry_part1;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;

import android.content.Intent;
import android.os.Bundle;

import com.bumptech.glide.Glide;
import com.example.tom_and_jerry_part1.Utils.My_Screen_Utils;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.switchmaterial.SwitchMaterial;

public class Activity_Home_Page extends AppCompatActivity {

    private AppCompatImageView  game_IMG_back;

     private AppCompatImageView game_IMG_back_top;
     private AppCompatImageView game_IMG_back_title;
    private MaterialButton      menu_BTN_top10;
    private MaterialButton      menu_BTN_playAgain;

    private SwitchMaterial menu_SW_sensor;
    private SwitchMaterial menu_SW_faster;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
        My_Screen_Utils.hideSystemUI(this);
        startView();
        initPictures();
        initButtonsListeners();
    }

    private void initButtonsListeners() {
        menu_BTN_top10.setOnClickListener(v -> goToTop10());
        menu_BTN_playAgain.setOnClickListener(v -> goToNewGame());
    }

    private void goToTop10() {
        Intent intent = new Intent(this,Top10_score.class);
        startActivity(intent);
        finish();
    }

    private void goToNewGame(){
        Intent intent = new Intent(this,Activity_Game.class);
        startActivity(intent);
        finish();
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