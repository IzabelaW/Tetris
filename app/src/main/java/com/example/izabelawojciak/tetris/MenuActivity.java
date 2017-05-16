package com.example.izabelawojciak.tetris;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

public class MenuActivity extends AppCompatActivity {

    private boolean playMusic = true;
    private int counter = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
    }

    public void onPlayMusicClick(View view){
        ImageView button = (ImageView) findViewById(R.id.playMusicButton);
        counter++;

        if (counter % 2 == 0) {
            button.setImageResource(R.drawable.soundon);
            playMusic = true;
        } else {
            button.setImageResource(R.drawable.soundoff);
            playMusic = false;
        }

    }

    public void onPlayGameClick(View view){
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("IS_MUSIC_ON",playMusic);
        startActivity(intent);
    }

    public void onScoresClick(View view){
        Intent intent = new Intent(this, ScoresList.class);
        startActivity(intent);
    }

}
