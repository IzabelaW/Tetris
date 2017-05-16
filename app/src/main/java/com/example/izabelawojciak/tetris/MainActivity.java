package com.example.izabelawojciak.tetris;

import android.content.Intent;
import android.graphics.Point;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.view.Display;
import android.view.View;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

public class MainActivity extends AppCompatActivity {

    private GameView gameView;
    private MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        final boolean isMusicOn = intent.getBooleanExtra("IS_MUSIC_ON",true);

        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);

        mediaPlayer = MediaPlayer.create(this, R.raw.tetris);
        mediaPlayer.setLooping(true);

        int screenWidth = size.x;
        int screenHeight = size.y;

        setContentView(R.layout.activity_main);

        if(savedInstanceState != null) {
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
        EventBus.getDefault().postSticky(gameView);
    }

    @Subscribe(sticky = true)
    public void restoreGameView(GameView gameView){
        this.gameView = gameView;
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        gameView.setGameFocus(hasFocus);
        super.onWindowFocusChanged(hasFocus);
    }

    @Override
    public void onStart() {
        super.onStart();
        if (!EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
        mediaPlayer.stop();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
        mediaPlayer.stop();
    }

}
