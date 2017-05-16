package com.example.izabelawojciak.tetris;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;

public class MainActivity extends Activity {

    private GameView gameView;
    private MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        final boolean isMusicOn = intent.getBooleanExtra("IS_MUSIC_ON",true);

        mediaPlayer = MediaPlayer.create(this, R.raw.tetris);
        mediaPlayer.setLooping(true);

        setContentView(R.layout.activity_main);

        if(savedInstanceState != null) {
            gameView = (GameView) savedInstanceState.getSerializable("GAME_VIEW");
            gameView.score = savedInstanceState.getInt("score");
            gameView.restoreInstanceState();
        }
        else {
            gameView = (GameView) findViewById(R.id.gameView);
        }

        gameView.post(new Runnable() {
            @Override
            public void run() {
                gameView.init();
            }
        });

        new SwipeDetector(gameView).setOnSwipeListener(new SwipeDetector.onSwipeEvent() {
            @Override
            public void SwipeEventDetected(View v, SwipeDetector.SwipeTypeEnum swipeType) {
                switch (swipeType) {
                    case LEFT_TO_RIGHT:
                        gameView.onRightShift();
                        break;
                    case RIGHT_TO_LEFT:
                        gameView.onLeftShift();
                        break;
                    case BOTTOM_TO_TOP:
                        gameView.onLeftRotate();
                        break;
                    case TOP_TO_BOTTOM:
                        gameView.onRightRotate();
                        break;
                }
            }
        });

        if (isMusicOn)
            mediaPlayer.start();


        final Thread gameThread = new Thread(new Runnable() {
            @Override
            public void run() {
                final Handler handler = new Handler(Looper.getMainLooper());
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (!gameView.gameOver && !isFinishing()) {
                            gameView.update(isMusicOn);
                            gameView.invalidate();
                            handler.postDelayed(this, 500);
                        }
                    }
                },500);
            }
        });

        gameThread.start();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("score", gameView.score);
        outState.putSerializable("GAME_VIEW", gameView);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        gameView.setGameFocus(hasFocus);
        super.onWindowFocusChanged(hasFocus);
    }

    @Override
    public void onStop() {
        super.onStop();
        mediaPlayer.stop();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mediaPlayer.stop();
    }
}
