package com.example.tom_and_jerry_part1;

import java.util.Random;

public class Game_Manager {

    private static Game_Manager instance;
    public final static int COLS = 3;
    public final static int ROWS = 6;

    private final int START_PLAYER_POSITION = COLS / 2;
    private int lives = 3;
    private int playerPosition;

    private Random randomObstacle;

    private int[][] boardObstacles = new int[ROWS][COLS];

    private Game_Manager() {
        randomObstacle = new Random();
    }

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

    public int getSpecificBoardObstacle(int i, int j) {
        return boardObstacles[i][j];
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

    public void shiftDownRows() {
        for (int i = ROWS-1; i > 0; i--) {
            System.arraycopy(boardObstacles[i - 1], 0, boardObstacles[i], 0, COLS);
        }
    }
    public void addObstacleToFirstRow(boolean addNewRow) {
        for (int i=0; i<COLS; i++)
            boardObstacles[0][i] = 0;

        if(addNewRow) {
            int randomCol = randomObstacle.nextInt(COLS);
            boardObstacles[0][randomCol] = 1;
        }
    }

    public void resetBoardOfObstacles() {
        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLS; j++) {
                boardObstacles[i][j] = 0;
            }
        }
    }

}
