package com.example.tom_and_jerry_part1;

//import com.example.tom_and_jerry_part1.DB.Record;

import java.util.Random;

public class Game_Manager {

    private static Game_Manager instance;
    public final static int COLS = 5;
    public final static int ROWS = 6;
    public final static int IMG_TYPES = 2;

    private final int START_PLAYER_POSITION = COLS / 2;
    private int lives = 3;
    private int playerPosition;
    private Random randomObstacle;
//    private final String RECORD = "records";
    private int score = 0;
//    private Record record;

    private int[][] boardObstacles = new int[ROWS][COLS];

    private Game_Manager() {
        randomObstacle = new Random();
    }

    public static Game_Manager getInstance() {
        if (instance == null)
            instance = new Game_Manager();
        return instance;
    }

        public void resetBoardOfObstacles() {
        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLS; j++) {
                boardObstacles[i][j] = 0;
            }
        }
    }

    public int getLives() {
        return lives;
    }

    public int getScore() { return score; }

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

    public void resetScore() {
        score = 0;
    }

    public void addScore(){ score += 10; }

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
            int randomCol = randomLocationForImg();
            boardObstacles[0][randomCol] = randomTypeOfImg();
        }
    }
    public int randomLocationForImg (){ return randomObstacle.nextInt(COLS); }   //between 0-(cols-1)

    public int randomTypeOfImg(){ return randomObstacle.nextInt(IMG_TYPES) + 1;} //1 --> tom , 2 --> cheese

    public void odometer(){
        score ++;
    }

//    public Record getRecord() {
//        return record;
//    }

//    public void saveDetails(double lng , double lat, String name) {
//        MyDB myDB;
//        String json = MySP.getInstance().getString(RECORD,"");
//        myDB = new Gson().fromJson(json,MyDB.class);
//        if(myDB == null){
//            myDB = new MyDB();
//        }
//        Record rec = createRecord(lng,lat, name);
//        myDB.getTopRecords().add(rec);
//        MySP.getInstance().putString(RECORD,new Gson().toJson(myDB));
//    }
//    private Record createRecord(double lng , double lat, String name) {
//        return new Record().setName(name).setScore(score).setLat(lat).setLng(lng);
//    }
}
