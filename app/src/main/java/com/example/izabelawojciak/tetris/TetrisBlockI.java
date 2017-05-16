package com.example.izabelawojciak.tetris;

import android.content.Context;
import android.graphics.BitmapFactory;

/**
 * Created by izabelawojciak on 11.05.2017.
 */

public class TetrisBlockI extends TetrisBlock {

    public TetrisBlockI(Context context) {
        super(context);
        this.color = Color.BLUE;
        this.bitmap =  BitmapFactory.decodeResource(context.getResources(), R.drawable.blue);
        this.blockGrid0 = new short[] {
                0, 0, 0, 0,
                1, 1, 1, 1,
                0, 0, 0, 0,
                0, 0, 0, 0
        };
        this.blockGrid1 = new short[] {
                0, 0, 1, 0,
                0, 0, 1, 0,
                0, 0, 1, 0,
                0, 0, 1, 0
        };
        this.blockGrid2 = new short[] {
                0, 0, 0, 0,
                0, 0, 0, 0,
                1, 1, 1, 1,
                0, 0, 0, 0
        };
        this.blockGrid3 = new short[] {
                0, 1, 0, 0,
                0, 1, 0, 0,
                0, 1, 0, 0,
                0, 1, 0, 0
        };
    }

}
