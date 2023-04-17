package com.example.tom_and_jerry_part1;

public class Game_Manager {

    private static Game_Manager instance;
    private final int COLS = 3;
    private final int START_PLAYER_POSITION = COLS / 2;
    //TODO: decide where to delete the COLS from
    private int lives = 3;
    private int playerPosition;

    private Game_Manager() {}

    public static Game_Manager getInstance() {
        if (instance == null)
            instance = new Game_Manager();
        return instance;
    }

    public int getLives() {
        return lives;
    }

    public int getPlayerPosition() {
        return playerPosition;
    }

    public void reduceLive() {
        lives--;
    }

    public void resetLives() {
        lives = 3;
    }

    public void setPlayerPosition() {
        playerPosition = START_PLAYER_POSITION;
    }

    public void changePlayerLeft() {
        if(playerPosition == 0)
            return;
        playerPosition--;
    }

    public void changePlayerRight() {
        if(playerPosition == COLS-1)
            return;
        playerPosition++;
    }

}
