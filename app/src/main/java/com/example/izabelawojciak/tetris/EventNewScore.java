package com.example.izabelawojciak.tetris;

/**
 * Created by izabelawojciak on 16.05.2017.
 */

public class EventNewScore {

    private int score;
    private String name;
    private String date;

    public EventNewScore(String name, int score, String date){
        this.name = name;
        this.score = score;
        this.date = date;
    }

    public int getScore() {
        return score;
    }

    public String getName() {
        return name;
    }

    public String getDate() {
        return date;
    }
}
