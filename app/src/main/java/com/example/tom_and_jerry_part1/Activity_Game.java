package com.example.tom_and_jerry_part1;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.cardview.widget.CardView;
import com.bumptech.glide.Glide;
import com.google.android.material.button.MaterialButton;
import java.util.Random;

public class Activity_Game extends AppCompatActivity {

    private final int ROWS = 6;
    private final int COLS = 3;
    private final int TIMER_DELAY_MS = 550;
    private AppCompatImageView[]    game_IMG_hearts;
    private AppCompatImageView[]    game_IMG_player;
    private AppCompatImageView[]    game_IMG_player_catch;
    private AppCompatImageView[][]  game_IMG_obstacles;
    private MaterialButton          game_BTN_right;
    private MaterialButton          game_BTN_left;
    private MaterialButton          game_BTN_playAgain;
    private CardView                game_CV_gameOverBoard;
    private AppCompatImageView      game_IMG_back;
    private Handler timerHandler;
    private Runnable timerRunnable;
    private int timeCount = 0;
    private int[][] iconsVisibilities = new int[ROWS][COLS];
    private Random randomObstacle;
    private int randomCol = 0;
    private boolean isFirstGame = true;
    private Game_Manager gameManager;


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
        initGameManager();
        startNewGame();
    }

    private void initGameManager() {
        gameManager = Game_Manager.getInstance();
    }

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

    private void resetLastRow() {
        for(int i = 0; i< COLS; i++) {
            game_IMG_player_catch[i].setVisibility(View.INVISIBLE);
            if(gameManager.getPlayerPosition() == i)
                game_IMG_player[i].setVisibility(View.VISIBLE);
        }
    }

    private void shiftDownRows() {
        for (int i = ROWS-1; i > 0; i--) {
            System.arraycopy(iconsVisibilities[i - 1], 0, iconsVisibilities[i], 0, COLS);
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

    private void checkIfCatch() {
        for (int i = 0; i < COLS; i++) {
            if (iconsVisibilities[ROWS-1][i] == 1 && gameManager.getPlayerPosition() == i) {
                gameManager.reduceLive();
                updateLivesUI();
                changeIconUIForCatch(i);
                playSoundOfCatching();
                vibrateOnCatch();
                if (gameManager.getLives() == 0)
                    gameOver();
            }
        }
    }
    private void updateLivesUI() {
        for (int i = 0; i < gameManager.getLives(); i++) {
            game_IMG_hearts[i].setVisibility(View.VISIBLE);
        }

        for (int i = gameManager.getLives(); i < game_IMG_hearts.length; i++) {
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

    private void vibrateOnCatch() {
        My_Signal.getInstance().vibrate(400);
    }

    private void startNewGame() {
        My_Signal.getInstance().sound(R.raw.msc_start_game);
        hideGameOverBoard();
        initLives();
        initPlayerPosition();
        resetMatrixOfIcons();
        game_BTN_right.setEnabled(true);
        game_BTN_left.setEnabled(true);
        if(!isFirstGame)
            startTimer();
        else
            isFirstGame = false;
    }

    private void hideGameOverBoard() {
        game_CV_gameOverBoard.setVisibility(View.INVISIBLE);
    }

    private void initLives() {
        gameManager.resetLives();
        updateLivesUI();
    }

    private void initPlayerPosition() {
        gameManager.setPlayerPosition();
        setPlayerVisibility();
    }

    private void setPlayerVisibility() {
        for(int i=0; i<COLS; i++)
            if (i == gameManager.getPlayerPosition())
                game_IMG_player[i].setVisibility(View.VISIBLE);
            else
                game_IMG_player[i].setVisibility(View.INVISIBLE);
    }

    private void resetMatrixOfIcons() {
        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLS; j++) {
                iconsVisibilities[i][j] = 0;
            }
        }
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
        game_BTN_playAgain = findViewById(R.id.game_BTN_playAgain);
        game_CV_gameOverBoard = findViewById(R.id.game_CV_gameOverBoard);

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
        game_BTN_playAgain.setOnClickListener(v -> startNewGame());
    }

    private void moveLeft() {
        gameManager.changePlayerLeft();
        setPlayerVisibility();
        checkIfCatch();
    }

    private void moveRight() {
        gameManager.changePlayerRight();
        setPlayerVisibility();
        checkIfCatch();
    }

    private void gameOver(){
        stopTimer();
        My_Signal.getInstance().sound(R.raw.msc_game_over);
        resetMatrixOfIcons();
        updateVisibilityOfIcons();
        for(int i = 0; i< COLS; i++)
            game_IMG_player_catch[i].setVisibility(View.INVISIBLE);
        game_CV_gameOverBoard.setVisibility(View.VISIBLE);
        game_BTN_right.setEnabled(false);
        game_BTN_left.setEnabled(false);
    }
}
