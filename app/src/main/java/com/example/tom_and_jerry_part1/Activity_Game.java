package com.example.tom_and_jerry_part1;

import android.media.MediaActionSound;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;

import com.bumptech.glide.Glide;
import com.google.android.material.button.MaterialButton;

import java.util.Arrays;
import java.util.Random;

public class Activity_Game extends AppCompatActivity {

    private final int ROWS = 6;
    private final int COLS = 3;
    private final int TIMER_DELAY_MS = 800; //1 sec
    private final int START_PLAYER_POSITION = COLS / 2;

    private AppCompatImageView[]    game_IMG_hearts;
    private AppCompatImageView[]    game_IMG_player;
    private AppCompatImageView[]    game_IMG_player_catch;
    private AppCompatImageView[][] game_IMG_obstacles;
    private MaterialButton          game_BTN_right;
    private MaterialButton          game_BTN_left;
    private AppCompatImageView game_IMG_back;
    private Handler timerHandler;
    private Runnable timerRunnable;
//    private MaterialButton game_BTN_playAgain;
    private int iconsVisibilities[][] = new int[ROWS][COLS];
    private int playerPosition;
    private int lives = 3;
    private int timeCount = 0;
    private Random randomObstacle;
    private int randomCol = 0;

    //todo: delete
//    private MediaActionSound sound = new MediaActionSound();
//            sound.play(MediaActionSound.START_VIDEO_RECORDING);




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        startViews();
        My_Signal.init(this);
        My_Screen_Utils.hideSystemUI(this);
        randomObstacle = new Random();
        initBackground();
        initButtonsListeners();
        startNewGame();
    }

    private void gameOver(){
        stopTimer();
        //TODO: finish logic
        Toast.makeText(this, "Game Over", Toast.LENGTH_SHORT).show();
        game_BTN_right.setEnabled(false);
        game_BTN_left.setEnabled(false);
   //     game_BTN_playAgain.setVisibility(View.VISIBLE);
    }

    //_________________________________IMPORTANT_____________________________________________
    @Override
    protected void onResume() {
        super.onResume();
        startTimer();
    }

    @Override
    protected void onPause() {
        super.onPause();
        stopTimer();
    }
    //_________________________________IMPORTANT_____________________________________________

    //timer
    private void startTimer() {
        timerHandler = new Handler();
        timerRunnable = new Runnable() {
            @Override
            public void run() {
                timerHandler.postDelayed(this, TIMER_DELAY_MS);
                afterSec();
            }
        };
        timerHandler.postDelayed(timerRunnable, TIMER_DELAY_MS);
    }

    private void stopTimer() {
        timerHandler.removeCallbacks(timerRunnable);
    }
    // timer

    private void afterSec() {
        resetLastRow(); //if there was a catch
        timeCount++;
        shiftDownRows();
        addObstacleToFirstRow();
        updateVisibilityOfIcons();
        checkIfCatch();
    }

    private void shiftDownRows() {
        for (int i = ROWS-1; i > 0; i--) {
            for (int j = 0; j < COLS; j++) {
                iconsVisibilities[i][j] = iconsVisibilities[i-1][j];
            }
        }
    }


    private void addObstacleToFirstRow() {
        for (int i=0; i<COLS; i++)
            iconsVisibilities[0][i] = 0;

        if(timeCount % 2 == 0) {
            randomCol = randomObstacle.nextInt(COLS);
            iconsVisibilities[0][randomCol] = 1;
        }
    }

    private void updateVisibilityOfIcons() {
        for (int i=0; i<ROWS; i++){
            for (int j=0; j<COLS; j++){
                if(iconsVisibilities[i][j] == 1)
                    game_IMG_obstacles[i][j].setVisibility(View.VISIBLE);
                else
                    game_IMG_obstacles[i][j].setVisibility(View.INVISIBLE);
            }
        }
    }

    private void resetLastRow() {
        for(int i = 0; i< COLS; i++) {
            game_IMG_player_catch[i].setVisibility(View.INVISIBLE);
            if(playerPosition == i)
                game_IMG_player[i].setVisibility(View.VISIBLE);
        }
    }

    private void checkIfCatch() {
        for (int i = 0; i < COLS; i++) {
            if (iconsVisibilities[ROWS-1][i] == 1 && playerPosition == i) {
                reduceLive();
                updateLivesUI();
                changeIconUIForCatch(i);
                playSoundOfCatching();
                vibrateOnCatch();
                if (lives == 0)
                    gameOver();
            }
        }
    }

    private void vibrateOnCatch() {
        My_Signal.getInstance().vibrate(500);
    }

    private void reduceLive() {
        lives--;
    }
    private void updateLivesUI() {
        for (int i = 0; i < lives; i++) {
            game_IMG_hearts[i].setVisibility(View.VISIBLE);
        }

        for (int i = lives; i < game_IMG_hearts.length; i++) {
            game_IMG_hearts[i].setVisibility(View.INVISIBLE);
        }
    }

    private void changeIconUIForCatch(int i) {
        game_IMG_player[i].setVisibility(View.INVISIBLE);
        game_IMG_obstacles[ROWS-1][i].setVisibility(View.INVISIBLE);
        game_IMG_player_catch[i].setVisibility(View.VISIBLE);
    }

    private void playSoundOfCatching() {
        My_Signal.getInstance().sound(R.raw.msc_tom_catch_jerry);
    }

    private void startNewGame() {
        initLives();
        initPlayerPosition();
        resetMatrixOfIcons(); //reset the matrix to 0, and jerry to the middle
        //TODO: soundOfNewGame
    }

    private void initPlayerPosition() {
        playerPosition = START_PLAYER_POSITION;
        setPlayerVisibility();
    }

    private void resetMatrixOfIcons() {
        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLS; j++) {
                if (iconsVisibilities[i][j] == 1)
                    game_IMG_obstacles[i][j].setVisibility(View.VISIBLE);
            }
        }
        for (int i = 0; i < COLS; i++) {
            if(playerPosition == i)
                game_IMG_player[i].setVisibility(View.VISIBLE);
        }
    }

    private void initLives() {
        lives = 3;
        updateLivesUI();
      //  game_BTN_playAgain.setVisibility(View.GONE);
    }


    private void initBackground() {
        Glide
                .with(Activity_Game.this)
                .load(R.drawable.img_game_background)
                .into(game_IMG_back);
        Glide
                .with(Activity_Game.this)
                .load(R.drawable.img_tom_catch_jerry)
                .into(game_IMG_player_catch[0]);
        Glide
                .with(Activity_Game.this)
                .load(R.drawable.img_tom_catch_jerry)
                .into(game_IMG_player_catch[1]);
        Glide
                .with(Activity_Game.this)
                .load(R.drawable.img_tom_catch_jerry)
                .into(game_IMG_player_catch[2]);
    }


    private void startViews() {
        game_IMG_back = findViewById(R.id.game_IMG_back);
        game_BTN_right = findViewById(R.id.game_BTN_right);
        game_BTN_left  = findViewById(R.id.game_BTN_left);
       // game_BTN_playAgain = findViewById(R.id.game_BTN_playAgain);

        game_IMG_hearts = new AppCompatImageView[]{
                findViewById(R.id.game_IMG_heart1),
                findViewById(R.id.game_IMG_heart2),
                findViewById(R.id.game_IMG_heart3),
        };

        game_IMG_player = new AppCompatImageView[]{
                findViewById(R.id.game_IMG_player_1),
                findViewById(R.id.game_IMG_player_2),
                findViewById(R.id.game_IMG_player_3),
        };

        game_IMG_player_catch = new AppCompatImageView[]{
                findViewById(R.id.game_IMG_player_catch_1),
                findViewById(R.id.game_IMG_player_catch_2),
                findViewById(R.id.game_IMG_player_catch_3),
        };

        game_IMG_obstacles = new AppCompatImageView[][] {
                {findViewById(R.id.game_IMG_1_1), findViewById(R.id.game_IMG_1_2), findViewById(R.id.game_IMG_1_3)},
                {findViewById(R.id.game_IMG_2_1), findViewById(R.id.game_IMG_2_2), findViewById(R.id.game_IMG_2_3)},
                {findViewById(R.id.game_IMG_3_1), findViewById(R.id.game_IMG_3_2), findViewById(R.id.game_IMG_3_3)},
                {findViewById(R.id.game_IMG_4_1), findViewById(R.id.game_IMG_4_2), findViewById(R.id.game_IMG_4_3)},
                {findViewById(R.id.game_IMG_5_1), findViewById(R.id.game_IMG_5_2), findViewById(R.id.game_IMG_5_3)},
                {findViewById(R.id.game_IMG_player_tom_1), findViewById(R.id.game_IMG_player_tom_2), findViewById(R.id.game_IMG_player_tom_3)}
        };

    }

    private void initButtonsListeners() {
        game_BTN_left.setOnClickListener(v -> moveLeft());
        game_BTN_right.setOnClickListener(v -> moveRight());
    }

    private void moveLeft() {
        if(playerPosition == 0)
            return;
        playerPosition--;
        setPlayerVisibility();
    }

    private void moveRight() {
        if(playerPosition == COLS-1)
            return;
        playerPosition++;
        setPlayerVisibility();
    }

    private void setPlayerVisibility() {
        for(int i=0; i<COLS; i++)
            if (i == playerPosition)
                game_IMG_player[i].setVisibility(View.VISIBLE);
            else
                game_IMG_player[i].setVisibility(View.INVISIBLE);
    }
}

