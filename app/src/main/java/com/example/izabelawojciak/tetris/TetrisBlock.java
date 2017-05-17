package com.example.izabelawojciak.tetris;

import android.content.Context;
import android.graphics.Bitmap;

import java.io.Serializable;

/**
 * Created by izabelawojciak on 11.05.2017.
 */

public abstract class TetrisBlock implements Serializable {

    public static final int BLOCKGRIDWIDTH = 4;
    public static final int BLOCKGRIDHEIGHT = 4;
    public static final int BLOCKGRIDSIZE = BLOCKGRIDWIDTH * BLOCKGRIDHEIGHT;

    public enum TetrisBlockTypes {
        TETRISBLOCKI,
        TETRISBLOCKJ,
        TETRISBLOCKL,
        TETRISBLOCKO,
        TETRISBLOCKS,
        TETRISBLOCKT,
        TETRISBLOCKZ
    };

    protected Color color;
    protected Bitmap bitmap;
    protected Context context;
    protected short rotation = 0;
    protected short blockGrid0[];
    protected short blockGrid1[];
    protected short blockGrid2[];
    protected short blockGrid3[];

    private int screenXPosition;
    private int screenYPosition;

//    private final int RENDERPOSX = 5;  //Leave 2 * 16x16 pixels blocks at left
//    private final int RENDERPOSY = 27; //Leave 5 * 16x16 pixels blocks at bottom

    public TetrisBlock(Context context){
        this.context = context;

        screenXPosition = 3;
        screenYPosition = 0;
    }

    public void resetPosition(){
        screenXPosition = 3;
        screenYPosition = 0 - getHighestRowFilled();
    }

    public Color getColor(){
        return color;
    }

    public Bitmap getBitmap(){
        return bitmap;
    }

    public void rotateLeft() {
        rotation--;
        if(rotation < 0) {
            rotation = 3;
        }
    }

    public void rotateRight() {
        rotation++;
        if(rotation > 3) {
            rotation = 0;
        }
    }

    public short[] getBlockGrid() {
        switch(rotation) {
            case 0:
                return blockGrid0;
            case 1:
                return blockGrid1;
            case 2:
                return blockGrid2;
            case 3:
                return blockGrid3;
            default:
                return null;
        }
    }

    public short getBlockGridCellValue(int row, int column){
        return getBlockGrid()[row * BLOCKGRIDWIDTH + column];
    }

    public int getXPosition() {
        return screenXPosition;
    }

    public int getYPosition() {
        return screenYPosition;
    }

    public void setXPosition(int xPos) {
        screenXPosition = xPos;
    }

    public void setYPosition(int yPos) {
        screenYPosition = yPos;
    }

    public int getHighestRowFilled(){

        short[] blockGrid = getBlockGrid();

        int highestRowFilled = -1;
        for(int i = 0; i < BLOCKGRIDHEIGHT; ++i) {
            for(int j = 0; j < BLOCKGRIDWIDTH; ++j) {
                if(blockGrid[i * BLOCKGRIDWIDTH + j] == 1) {
                    highestRowFilled = i;
                    break;
                }
            }
            if(highestRowFilled != -1) break;
        }

        return highestRowFilled;
    }

    public int getLowestRowFilled(){
        short[] blockGrid = getBlockGrid();
        int lowestRowFilled = 0;

        for (int i = BLOCKGRIDSIZE - 1; i >= 0; i--){
            if (blockGrid[i] == 1){
                lowestRowFilled = i/BLOCKGRIDWIDTH;
                break;
            }
        }

        return lowestRowFilled;
    }

    public int getMostLeftColumnFilled(){
        short[] blockGrid = getBlockGrid();
        int mostLeftColumnFilled = -1;

        for (int i = 0; i < BLOCKGRIDWIDTH; i++){
            for (int j = 0; j < BLOCKGRIDHEIGHT; j++){
                if (blockGrid[j * BLOCKGRIDWIDTH + i] == 1){
                    mostLeftColumnFilled = i;
                    break;
                }
            }
            if (mostLeftColumnFilled != -1) break;
        }
        return mostLeftColumnFilled;

    }

    public int getMostRightColumnFilled(){
        short[] blockGrid = getBlockGrid();
        int mostRightColumnFilled = -1;

        for (int i = BLOCKGRIDWIDTH - 1; i >= 0; i--){
            for (int j = 0; j < BLOCKGRIDHEIGHT; j ++){
                if (blockGrid[j * BLOCKGRIDWIDTH + i] == 1){
                    mostRightColumnFilled = i;
                    break;
                }
            }
            if (mostRightColumnFilled != -1) break;
        }
        return mostRightColumnFilled;
    }
}
