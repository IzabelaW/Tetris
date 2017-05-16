package com.example.izabelawojciak.tetris;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.v7.app.AlertDialog;
import android.util.AttributeSet;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by izabelawojciak on 06.05.2017.
 */

public class GameView extends View implements Serializable {

    //game actions
    public static final int ACTION_NONE = 0;
    public static final int ACTION_SHIFT_LEFT = 1;
    public static final int ACTION_SHIFT_RIGHT = 2;
    public static final int ACTION_ROTATE_L = 3;
    public static final int ACTION_ROTATE_R = 4;
    public static final int ACTION_MAKE_FALL = 5;

    private boolean restored = false;
    private boolean hasFocus;
    private GameGrid gameGrid;
    private int currentAction = ACTION_NONE;
    private Paint paint;

    public int score = 0;
    public boolean gameOver = false;


    public GameView(Context context) {
        super(context);
    }

    public GameView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public GameView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void init (){

        setBackgroundColor(android.graphics.Color.BLACK);
        setFocusable(true);
        setFocusableInTouchMode(true);
        requestFocus();
        paint = new Paint();

        if (restored){

            restored = false;

        } else {

            gameGrid = new GameGrid(getContext());
            gameGrid.init(getWidth(), getHeight());

        }

    }

    @Override
    public void onDraw(Canvas canvas){
        super.onDraw(canvas);

        paint.setColor(Color.WHITE);
        paint.setTextSize(60);
        paint.setFakeBoldText(true);
        canvas.drawText("Score: " + score, getWidth() - 300, getHeight() / 25, paint);

        gameGrid.paintGrid(canvas,paint);
    }

    public void update(boolean isMusicOn){

        if (!gameGrid.gameOver && hasFocus) {

            if (currentAction != ACTION_NONE) {
                gameGrid.moveBlock(currentAction,isMusicOn);
                currentAction = ACTION_NONE;
            }
            score += gameGrid.update(isMusicOn);
        }
        else {
            gameOver = true;

            LinearLayout layout = new LinearLayout(getContext());
            layout.setOrientation(LinearLayout.VERTICAL);

            final String scoreNumber = String.valueOf(score);

            final TextView scoreTv = new TextView(getContext());
            scoreTv.setText("Congrats! You reached " + scoreNumber + " score.");
            layout.addView(scoreTv);

            final TextView save = new TextView(getContext());
            save.setText("Do you want to save it?");
            layout.addView(save);

            final EditText nameBox = new EditText(getContext());
            nameBox.setHint("Nick");
            layout.addView(nameBox);

            final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setTitle("GAME OVER!")
                    .setView(layout)
                    .setPositiveButton("Save score", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            DateFormat df = new SimpleDateFormat("EEE, d MMM yyyy, HH:mm");
                            String date = df.format(Calendar.getInstance().getTime());

                            EventNewScore eventNewScore = new EventNewScore(String.valueOf(nameBox.getText()),score,date);
                            EventBus.getDefault().postSticky(eventNewScore);
                        }
                    })
                    .setNegativeButton("Cancel", null);
            final AlertDialog alert = builder.create();
            alert.show();
        }
    }

    public void restoreInstanceState(){
        restored = true;
    }

    public void setGameFocus(boolean hasFocus){
        this.hasFocus = hasFocus;
    }

    public void onLeftRotate(){
        currentAction = ACTION_ROTATE_L;
    }

    public void onRightRotate(){
        currentAction = ACTION_ROTATE_R;
    }

    public void onLeftShift(){
        currentAction = ACTION_SHIFT_LEFT;
    }

    public void onRightShift(){
        currentAction = ACTION_SHIFT_RIGHT;
    }

}
