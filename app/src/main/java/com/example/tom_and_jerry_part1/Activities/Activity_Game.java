package com.example.tom_and_jerry_part1.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.cardview.widget.CardView;
import com.bumptech.glide.Glide;
import com.example.tom_and_jerry_part1.Game_Manager;
import com.example.tom_and_jerry_part1.R;
import com.example.tom_and_jerry_part1.Utils.My_Screen_Utils;
import com.example.tom_and_jerry_part1.Utils.My_Signal;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textview.MaterialTextView;

public class Activity_Game extends AppCompatActivity {

    public static final String KEY_LNG = "KEY_LNG";
    public static final String KEY_LAT = "KEY_LAT";
    public static final String KEY_DELAY = "KEY_DELAY";
    public static final String KEY_SENSOR = "KEY_SENSOR";

    public static final String KEY_NAME = "KEY_NAME";
    private final int TIMER_DELAY_MS_SLOW = 550;
    private final int TIMER_DELAY_MS_FAST = 350;
    private AppCompatImageView[]    game_IMG_hearts;
    private AppCompatImageView[]    game_IMG_player;
    private AppCompatImageView[]    game_IMG_player_catch;
    private AppCompatImageView[][]  game_IMG_obstacles;

    private String[] typeImage= new String[]{"ic_tom","ic_cheese"};
    private String[] typeImagePlayer= new String[]{"ic_jerry", "img_tom_catch_jerry", "img_jerry_catch_cheese"};

    private MaterialButton          game_BTN_right;
    private MaterialButton          game_BTN_left;
    private MaterialButton          game_BTN_move_to_top10;
    private AppCompatEditText       game_IET_name;
    private MaterialTextView        game_LBL_score;
    private MaterialTextView        game_LBL_final_score;
    private CardView                game_CV_gameOverBoard;
    private AppCompatImageView      game_IMG_back;
    private Handler timerHandler;
    private Runnable timerRunnable;
    private int timeCount = 0;
    private int cols;
    private int rows;
    private double lng;
    private double lat;
    private boolean isFirstGame = true;
    public boolean isSensorOn = false;
    public boolean isFasterMode = false;
    private String name = "";


    private Game_Manager gameManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        startViews();
        My_Screen_Utils.hideSystemUI(this);
        initBackground();
        Intent previousIntent  = getIntent();
        initGameManager(previousIntent);
        initButtonsListeners();
        startNewGame();
    }

    private void startNewGame() {
        My_Signal.getInstance().sound(R.raw.msc_start_game);
        hideGameOverBoard();
        initLives();
        initScore();
        initPlayerPosition();
        gameManager.resetBoardOfObstacles();
        game_BTN_right.setEnabled(true);
        game_BTN_left.setEnabled(true);
        if(!isFirstGame)
            startTimer();
        else
            isFirstGame = false;
    }

    private void initGameManager(Intent previousIntent) {
        gameManager = Game_Manager.getInstance();
        rows = gameManager.ROWS;
        cols = gameManager.COLS;
        isFasterMode = previousIntent.getExtras().getBoolean(KEY_DELAY);
        isSensorOn= previousIntent.getExtras().getBoolean(KEY_SENSOR);
        lng = previousIntent.getExtras().getDouble(KEY_LNG);
        lat = previousIntent.getExtras().getDouble(KEY_LAT);
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
                if(isFasterMode)
                    timerHandler.postDelayed(this, TIMER_DELAY_MS_FAST);
                else
                    timerHandler.postDelayed(this, TIMER_DELAY_MS_SLOW);
                afterSec();
            }
        };
        timerHandler.postDelayed(timerRunnable, TIMER_DELAY_MS_SLOW);
    }

    private void stopTimer() {
        timerHandler.removeCallbacks(timerRunnable);
    }
    // timer

    private void afterSec() {
        resetLastRow(); //if there was a catch
        gameManager.odometer();
        updateScore();
        timeCount++;
        gameManager.shiftDownRows();
        gameManager.addObstacleToFirstRow(timeCount % 2 == 0);
        updateVisibilityOfIcons();
        checkIfCatch();
    }

    private void resetLastRow() {
        for(int i = 0; i< cols; i++) {
            int imageId = getResources().getIdentifier(typeImagePlayer[0], "drawable", getPackageName());
            game_IMG_player[i].setImageResource(imageId);
        }
    }

    private void updateVisibilityOfIcons() {
        for (int i=0; i<rows; i++){
            for (int j=0; j<cols; j++) {
                if (gameManager.getSpecificBoardObstacle(i, j) == 0)
                    game_IMG_obstacles[i][j].setVisibility(View.INVISIBLE);
                else {
                    game_IMG_obstacles[i][j].setVisibility(View.VISIBLE);
                    setImage(gameManager.getSpecificBoardObstacle(i, j), i, j);
                }
            }
        }
    }

    private void setImage(int imgType, int i, int j){
        int imageId = getResources().getIdentifier(typeImage[imgType-1], "drawable", getPackageName());
        game_IMG_obstacles[i][j].setImageResource(imageId);
    }

    private void checkIfCatch() {
        for (int i = 0; i < cols; i++) {
            if(gameManager.getPlayerPosition() == i) {
                if (gameManager.getSpecificBoardObstacle(rows - 1, i) == 1) {
                    gameManager.reduceLive();
                    updateLivesUI();
                    changeIconUIForCatch(i);
                    playSoundOfCatching();
                    vibrateOnCatch();
                    if (gameManager.getLives() == 0)
                        gameOver();
                }
                else if(gameManager.getSpecificBoardObstacle(rows - 1, i) == 2){
                    gameManager.addScore();
                    updateScore();
                    changeIconUIForJerryEatCheese(i);
                    playSoundOfSuccess();
                }
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
        if(gameManager.getLives() < 3 && gameManager.getLives() > 0)
            My_Signal.getInstance().toast("You have "+ gameManager.getLives()+ " lives");
    }
    private void updateScore(){
        game_LBL_score.setText("score: "+ gameManager.getScore());
    }

    private int changeVisibilityOfPlayer(int i, int type){
    game_IMG_obstacles[rows-1][i].setVisibility(View.INVISIBLE);
    game_IMG_player[i].setVisibility(View.VISIBLE);
    return getResources().getIdentifier(typeImagePlayer[type], "drawable", getPackageName());
}

    private void changeIconUIForCatch(int i) {
        int imageId = changeVisibilityOfPlayer(i,1); // 1 --> tom catch jerry
        game_IMG_player[i].setImageResource(imageId);
    }

    private void changeIconUIForJerryEatCheese(int i){
        int imageId = changeVisibilityOfPlayer(i,2); // 2 --> jerry catch cheese
        game_IMG_player[i].setImageResource(imageId);
    }

    private void playSoundOfCatching() {
        My_Signal.getInstance().sound(R.raw.msc_tom_catch_jerry);
    }

    private void playSoundOfSuccess() { My_Signal.getInstance().sound(R.raw.msc_yay); }

    private void vibrateOnCatch() {
        My_Signal.getInstance().vibrate(400);
    }

    private void hideGameOverBoard() {
        game_CV_gameOverBoard.setVisibility(View.INVISIBLE);
    }

    private void initLives() {
        gameManager.resetLives();
        updateLivesUI();
    }

    private void initScore(){
        gameManager.resetScore();
        updateScore();
    }

    private void initPlayerPosition() {
        gameManager.setPlayerPosition();
        setPlayerVisibility();
    }

    private void setPlayerVisibility() {
        for(int i=0; i<cols; i++)
            if (i == gameManager.getPlayerPosition())
                game_IMG_player[i].setVisibility(View.VISIBLE);
            else
                game_IMG_player[i].setVisibility(View.INVISIBLE);
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
        Glide
                .with(Activity_Game.this)
                .load(R.drawable.img_tom_catch_jerry)
                .into(game_IMG_player_catch[3]);
        Glide
                .with(Activity_Game.this)
                .load(R.drawable.img_tom_catch_jerry)
                .into(game_IMG_player_catch[4]);
    }

    private void startViews() {
        game_IMG_back = findViewById(R.id.game_IMG_back);
        game_BTN_right = findViewById(R.id.game_BTN_right);
        game_BTN_left  = findViewById(R.id.game_BTN_left);
        game_BTN_move_to_top10 = findViewById(R.id.game_BTN_confirm);
        game_CV_gameOverBoard = findViewById(R.id.game_CV_gameOverBoard);
        game_IET_name = findViewById(R.id.game_IET_name);
        game_LBL_score = findViewById(R.id.game_LBL_score);
        game_LBL_final_score = findViewById(R.id.game_LBL_final_score);

        game_IMG_hearts = new AppCompatImageView[]{
                findViewById(R.id.game_IMG_heart1),
                findViewById(R.id.game_IMG_heart2),
                findViewById(R.id.game_IMG_heart3),
        };

        game_IMG_player = new AppCompatImageView[]{
                findViewById(R.id.game_IMG_player_1),
                findViewById(R.id.game_IMG_player_2),
                findViewById(R.id.game_IMG_player_3),
                findViewById(R.id.game_IMG_player_4),
                findViewById(R.id.game_IMG_player_5),
        };

        game_IMG_player_catch = new AppCompatImageView[]{
                findViewById(R.id.game_IMG_player_catch_1),
                findViewById(R.id.game_IMG_player_catch_2),
                findViewById(R.id.game_IMG_player_catch_3),
                findViewById(R.id.game_IMG_player_catch_4),
                findViewById(R.id.game_IMG_player_catch_5),
        };

        game_IMG_obstacles = new AppCompatImageView[][] {
                {findViewById(R.id.game_IMG_1_1), findViewById(R.id.game_IMG_1_2), findViewById(R.id.game_IMG_1_3), findViewById(R.id.game_IMG_1_4), findViewById(R.id.game_IMG_1_5)},
                {findViewById(R.id.game_IMG_2_1), findViewById(R.id.game_IMG_2_2), findViewById(R.id.game_IMG_2_3), findViewById(R.id.game_IMG_2_4), findViewById(R.id.game_IMG_2_5)},
                {findViewById(R.id.game_IMG_3_1), findViewById(R.id.game_IMG_3_2), findViewById(R.id.game_IMG_3_3), findViewById(R.id.game_IMG_3_4), findViewById(R.id.game_IMG_3_5)},
                {findViewById(R.id.game_IMG_4_1), findViewById(R.id.game_IMG_4_2), findViewById(R.id.game_IMG_4_3), findViewById(R.id.game_IMG_4_4), findViewById(R.id.game_IMG_4_5)},
                {findViewById(R.id.game_IMG_5_1), findViewById(R.id.game_IMG_5_2), findViewById(R.id.game_IMG_5_3), findViewById(R.id.game_IMG_5_4), findViewById(R.id.game_IMG_5_5)},
                {findViewById(R.id.game_IMG_player_tom_1), findViewById(R.id.game_IMG_player_tom_2), findViewById(R.id.game_IMG_player_tom_3), findViewById(R.id.game_IMG_player_tom_4), findViewById(R.id.game_IMG_player_tom_5)},
        };
    }

    private void initButtonsListeners() {
        game_BTN_left.setOnClickListener(v -> moveLeft());
        game_BTN_right.setOnClickListener(v -> moveRight());
        game_BTN_move_to_top10.setOnClickListener(v -> changeActivityToTop10());
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
        gameManager.resetBoardOfObstacles();
        updateVisibilityOfIcons();
        for(int i = 0; i< cols; i++)
            game_IMG_player_catch[i].setVisibility(View.INVISIBLE);
        game_LBL_final_score.setText("Your Score: " + gameManager.getScore());
        game_CV_gameOverBoard.setVisibility(View.VISIBLE);
        game_BTN_right.setEnabled(false);
        game_BTN_left.setEnabled(false);
    }
    private void changeActivityToTop10() {
        if(game_IET_name.getText().length() != 0) {
            Intent intent = new Intent(this, Top10_score.class);
            name = game_IET_name.getText().toString();
            saveRecord();
            startActivity(intent);
            finish();
        }
        else
            My_Signal.getInstance().toast("You Must Fill Name");
    }

    private void saveRecord() {
        gameManager.saveDetails(lng,lat, name);
    }
}
