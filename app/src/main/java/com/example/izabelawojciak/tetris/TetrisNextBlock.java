package com.example.izabelawojciak.tetris;

import android.content.Context;

import java.util.Random;

/**
 * Created by izabelawojciak on 11.05.2017.
 */

public class TetrisNextBlock {

    private final TetrisBlockI tetrisBlockI;
    private final TetrisBlockT tetrisBlockT;
    private final TetrisBlockZ tetrisBlockZ;
    private final TetrisBlockJ tetrisBlockJ;
    private final TetrisBlockL tetrisBlockL;
    private final TetrisBlockO tetrisBlockO;
    private final TetrisBlockS tetrisBlockS;

    private int currentBlockIndex = 0;
    private TetrisBlock nextBlock;

    private Random random;

    public TetrisNextBlock(Context context){

        tetrisBlockI = new TetrisBlockI(context);
        tetrisBlockT = new TetrisBlockT(context);
        tetrisBlockZ = new TetrisBlockZ(context);
        tetrisBlockJ = new TetrisBlockJ(context);
        tetrisBlockL = new TetrisBlockL(context);
        tetrisBlockO = new TetrisBlockO(context);
        tetrisBlockS = new TetrisBlockS(context);

        nextBlock = tetrisBlockI;

        random = new Random();
    }

    public TetrisBlock getNextBlock(){
        nextBlock.resetPosition();
        return nextBlock;
    }

    public void generateNextBlock() {

        int nextBlockIndex = random.nextInt(7);

        while (nextBlockIndex == currentBlockIndex) {
            nextBlockIndex = random.nextInt(7);
        }

        currentBlockIndex = nextBlockIndex;

        switch (currentBlockIndex) {
            case 0:
                nextBlock = tetrisBlockI;
                break;
            case 1:
                nextBlock = tetrisBlockJ;
                break;
            case 2:
                nextBlock = tetrisBlockL;
                break;
            case 3:
                nextBlock = tetrisBlockO;
                break;
            case 4:
                nextBlock = tetrisBlockS;
                break;
            case 5:
                nextBlock = tetrisBlockT;
                break;
            case 6:
                nextBlock = tetrisBlockZ;
                break;
            default:
                return;
        }
    }
}
