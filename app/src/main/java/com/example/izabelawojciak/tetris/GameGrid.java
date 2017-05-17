package com.example.izabelawojciak.tetris;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.media.AudioManager;
import android.media.SoundPool;

import java.io.Serializable;

/**
 * Created by izabelawojciak on 06.05.2017.
 */

public class GameGrid implements Serializable{

    //game actions
    public static final int ACTION_SHIFT_LEFT = 1;
    public static final int ACTION_SHIFT_RIGHT = 2;
    public static final int ACTION_ROTATE_L = 3;
    public static final int ACTION_ROTATE_R = 4;
    public static final int ACTION_MAKE_FALL = 5;

    public static final int PLAYFIELD_COLS = 10;
    public static final int PLAYFIELD_ROWS = 20;

    private Color[] gameGrid;
    private TetrisBlock currentBlock;
    private TetrisNextBlock nextBlock;

    private int mTileW;
    private int mTileH;
    private int mLeft;
    private int mTop = 100;
    private int mRight;
    private int mBottom;

    private Context context;
    private SoundPool soundPool;
    private int pieceFall;
    private int pieceMove;
    private int pieceRotate;
    private int pieceLockDown;


    public boolean gameOver = false;

    public GameGrid(Context context){
        this.context = context;
        gameGrid = new Color[PLAYFIELD_ROWS * PLAYFIELD_COLS];
        nextBlock = new TetrisNextBlock(context);
        soundPool = new SoundPool(2, AudioManager.STREAM_MUSIC, 0);

        pieceFall = soundPool.load(context, R.raw.piecefall, 0);
        pieceMove = soundPool.load(context, R.raw.piecemove, 0);
        pieceRotate = soundPool.load(context, R.raw.piecerotate, 0);
        pieceLockDown = soundPool.load(context, R.raw.piecelockdown,0);
    }

    public void initBackground(int w, int h){

        h -= mTop;

        mTileW = w / PLAYFIELD_COLS;
        mTileH = h / PLAYFIELD_ROWS;

        mRight = mLeft + (mTileW * PLAYFIELD_COLS);
        mBottom = mTop + (mTileH * PLAYFIELD_ROWS);

    }

    public void init(){
        for (int i = 0; i < gameGrid.length; i++) {
            gameGrid[i] = Color.EMPTY;
        }

        currentBlock = nextBlock.getNextBlock();
        nextBlock.generateNextBlock();
    }

    public int update(boolean isMusicOn){

        int score = 0;

        removeCurrentBlock();

        if (canBlockFallLower()) {
            currentBlock.setYPosition(currentBlock.getYPosition() + 1);
            putCurrentBlockOnTheGrid();
            if(isMusicOn)
                soundPool.play(pieceFall, 1, 1, 1, 0, 1);

        }
        else {
            putCurrentBlockOnTheGrid();
            if (isMusicOn)
                soundPool.play(pieceLockDown, 1, 1, 1, 0, 1);
            score = removeFullLines();

            if (isGameOver())
                gameOver = true;

            currentBlock = nextBlock.getNextBlock();
            nextBlock.generateNextBlock();
        }
        return score;
    }

    public void moveBlock(int move, boolean isMusicOn){
        switch (move){
            case ACTION_SHIFT_LEFT:
                if (isMusicOn)
                    soundPool.play(pieceMove, 1, 1, 1, 0, 1);
                tryMoveLeft();
                break;
            case ACTION_SHIFT_RIGHT:
                if (isMusicOn)
                    soundPool.play(pieceMove, 1, 1, 1, 0, 1);
                tryMoveRight();
                break;
            case ACTION_ROTATE_L:
                if (isMusicOn)
                    soundPool.play(pieceRotate, 1, 1, 1, 0, 1);
                tryRotateLeft();
                break;
            case ACTION_ROTATE_R:
                if (isMusicOn)
                    soundPool.play(pieceRotate, 1, 1, 1, 0, 1);
                tryRotateRight();
                break;
            case ACTION_MAKE_FALL:
                tryAddGravity(3);
                break;
            default:
                break;
        }
    }

    private boolean isGameOver(){
        for (int i = 0; i < PLAYFIELD_COLS; i++){
            if (gameGrid[i] != Color.EMPTY){
                return true;
            }
        }
        return false;
    }

    private boolean canBlockFallLower(){
        if (isOutsideGrid(1)) {
            return false;
        }
        else if (isOccupied(1)){
            return false;
        }
        else
            return true;
    }

    private boolean isOutsideGrid(int x){
        for (int i = currentBlock.getHighestRowFilled(); i <= currentBlock.getLowestRowFilled(); i++){
            for (int j = currentBlock.getMostLeftColumnFilled(); j <= currentBlock.getMostRightColumnFilled(); j++){

                int checkPositionX = currentBlock.getXPosition() + j;
                int checkPositionY = currentBlock.getYPosition() + i + x;

                if (checkPositionX < 0 || checkPositionX >= PLAYFIELD_COLS || checkPositionY < 0 || checkPositionY >= PLAYFIELD_ROWS){
                    return true;
                }
            }
        }
        return false;
    }

    private boolean isOccupied(int x){
        for (int i = currentBlock.getHighestRowFilled(); i <= currentBlock.getLowestRowFilled(); i++){
            for (int j = currentBlock.getMostLeftColumnFilled(); j <= currentBlock.getMostRightColumnFilled(); j++){

                int checkPositionX = currentBlock.getXPosition() + j;
                int checkPositionY = currentBlock.getYPosition() + i + x;

                if (currentBlock.getBlockGridCellValue(i,j) == 1 && isCellOccupied(checkPositionY,checkPositionX)){
                    return true;
                }
            }
        }
        return false;
    }

    private boolean isCellOccupied(int row, int column){
        if (row <= 19 && column <= 9)
            if (gameGrid[row * PLAYFIELD_COLS + column] != Color.EMPTY)
                return true;
            else
                return false;
        else
            return false;
    }

    private void setGridCellValue(int row, int column, Color color){
        gameGrid[row * PLAYFIELD_COLS + column] = color;
    }

    private Color getGridCellValue(int row, int column){
        return gameGrid[row * PLAYFIELD_COLS + column];
    }

    private void tryMoveLeft(){
        removeCurrentBlock();
        currentBlock.setXPosition(currentBlock.getXPosition() - 1);

        if (isOccupied(0) || isOutsideGrid(0))
            currentBlock.setXPosition(currentBlock.getXPosition() + 1);
        putCurrentBlockOnTheGrid();
    }

    private void tryMoveRight(){
        removeCurrentBlock();
        currentBlock.setXPosition(currentBlock.getXPosition() + 1);

        if (isOccupied(0) || isOutsideGrid(0))
            currentBlock.setXPosition(currentBlock.getXPosition() - 1);
        putCurrentBlockOnTheGrid();
    }

    private void tryRotateLeft(){
        removeCurrentBlock();
        currentBlock.rotateLeft();

        if (isOccupied(0) || isOutsideGrid(0))
            currentBlock.rotateRight();
        putCurrentBlockOnTheGrid();
    }

    private void tryRotateRight(){
        removeCurrentBlock();
        currentBlock.rotateRight();

        if (isOccupied(0) || isOutsideGrid(0))
            currentBlock.rotateLeft();
        putCurrentBlockOnTheGrid();
    }

    private void tryAddGravity(int x){
        if (x != 0) {
            removeCurrentBlock();
            currentBlock.setYPosition(currentBlock.getYPosition() + x);

            if (isOutsideGrid(0))
                currentBlock.setYPosition(19);
            else if (isOccupied(0)) {
                currentBlock.setYPosition(currentBlock.getYPosition());
                tryAddGravity(x - 1);
            }
        }
        putCurrentBlockOnTheGrid();
    }

    private void removeCurrentBlock(){
        for (int i = currentBlock.getHighestRowFilled(); i <= currentBlock.getLowestRowFilled(); i++){
            for (int j = currentBlock.getMostLeftColumnFilled(); j <= currentBlock.getMostRightColumnFilled(); j++){

                int checkPositionX = currentBlock.getXPosition() + j;
                int checkPositionY = currentBlock.getYPosition() + i;

                if (currentBlock.getBlockGridCellValue(i,j) == 1)
                    gameGrid[checkPositionY * PLAYFIELD_COLS + checkPositionX] = Color.EMPTY;
            }
        }
    }

    private void putCurrentBlockOnTheGrid(){
        for (int i = currentBlock.getHighestRowFilled(); i <= currentBlock.getLowestRowFilled(); i++){
            for (int j = currentBlock.getMostLeftColumnFilled(); j <= currentBlock.getMostRightColumnFilled(); j++){

                int checkPositionX = currentBlock.getXPosition() + j;
                int checkPositionY = currentBlock.getYPosition() + i;

                if (currentBlock.getBlockGridCellValue(i,j) == 1)
                    gameGrid[checkPositionY * PLAYFIELD_COLS + checkPositionX] = currentBlock.getColor();
            }
        }
    }

    private void removeOneLine(int row) {
        for(int i = row; i > 0; i--) {
            for(int j = 0; j < PLAYFIELD_COLS; j++) {
                setGridCellValue(i, j, getGridCellValue(i - 1, j));
            }
        }

        for(int i = 0; i < PLAYFIELD_COLS; i++) {
            setGridCellValue(0, i, Color.EMPTY);
        }
    }

    private int removeFullLines() {
        int score = 0;

        for(int i = 0; i < PLAYFIELD_ROWS; i++) {
            for (int j = 0; j < PLAYFIELD_COLS; j++) {
                if (getGridCellValue(i, j) == Color.EMPTY)
                    break;
                if (j == PLAYFIELD_COLS - 1) {
                    removeOneLine(i);
                    score += 10;
                }
            }
        }
        return score;
    }

    public void paintGrid(Canvas canvas, Paint paint){

        paint.setColor(android.graphics.Color.GRAY);
        paint.setStyle(Paint.Style.FILL);
        canvas.drawRect(mLeft, mTop, mRight, mBottom, paint);

        Color color;
        RectF block;
        Bitmap bitmap;
        int x;
        int y;

        for (int i = 0; i < gameGrid.length; i++){

                x = mLeft+(i%PLAYFIELD_COLS)*mTileW;
                y = mTop+(i/PLAYFIELD_COLS)*mTileH;

                color = gameGrid[i];
                block = new RectF(x,y,x+mTileW,y+mTileH);

                switch (color) {
                    case EMPTY:
                        paint.setColor(android.graphics.Color.BLACK);
                        paint.setStyle(Paint.Style.STROKE);
                        canvas.drawRect(x, y, x + mTileW, y + mTileH, paint);
                        break;

                    case CYAN:
                        bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.cyan);
                        canvas.drawBitmap(bitmap, null, block, paint);
                        break;

                    case YELLOW:
                        bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.yellow);
                        canvas.drawBitmap(bitmap, null, block, paint);
                        break;
                    case RED:
                        bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.red);
                        canvas.drawBitmap(bitmap, null, block, paint);
                        break;

                    case GREEN:
                        bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.green);
                        canvas.drawBitmap(bitmap, null, block, paint);
                        break;

                    case BLUE:
                        bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.blue);
                        canvas.drawBitmap(bitmap, null, block, paint);
                        break;

                    case ORANGE:
                        bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.orange);
                        canvas.drawBitmap(bitmap, null, block, paint);
                        break;

                    case PURPLE:
                        bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.purple);
                        canvas.drawBitmap(bitmap, null, block, paint);
                        break;
                }
        }
    }
}
