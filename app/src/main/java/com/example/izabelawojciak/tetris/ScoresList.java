package com.example.izabelawojciak.tetris;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;

/**
 * Created by izabelawojciak on 23.04.2017.
 */

public class ScoresList extends AppCompatActivity {

    private ArrayList<Score> scores;
    private RecyclerView recyclerView;
    private ScoresAdapter myScoreAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.score_list);

        scores = new ArrayList<>();

        recyclerView = (RecyclerView) findViewById(R.id.score_recyclerView);
        recyclerView.setHasFixedSize(true);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);

        myScoreAdapter = new ScoresAdapter(scores);

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(myScoreAdapter);

        if (savedInstanceState != null)
            populateList(savedInstanceState);

        myScoreAdapter.notifyDataSetChanged();
    }

    private void populateList(Bundle savedInstanceState) {
        String[] names = savedInstanceState.getStringArray("Names");
        String[] dates = savedInstanceState.getStringArray("Dates");
        int[] scores = savedInstanceState.getIntArray("Scores");
        Score score;

        for (int i = 0; i < names.length; i++) {
            score = new Score(names[i], scores[i], dates[i]);
            this.scores.add(score);
        }
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
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        String[] names = new String[scores.size()];
        String[] dates = new String[scores.size()];
        int[] scoreValues = new int[scores.size()];

        for (int i = 0; i < scores.size(); i++) {
            names[i] = scores.get(i).getName();
            dates[i] = scores.get(i).getDate();
            scoreValues[i] = scores.get(i).getScore();
        }
        outState.putStringArray("Names", names);
        outState.putStringArray("Dates", dates);
        outState.putIntArray("Scores", scoreValues);

        super.onSaveInstanceState(outState);
    }

    @Subscribe(sticky = true)
    public void onNewScore(EventNewScore eventNewScore) {

        Score score = new Score(eventNewScore.getName(), eventNewScore.getScore(), eventNewScore.getDate());
        scores.add(score);
//        myScoreAdapter.notifyItemChanged(scores.indexOf(score));

        myScoreAdapter.notifyItemInserted(scores.size() - 1);
        Log.println(Log.INFO, "NEW SCORE", String.valueOf(scores.size()));

        EventBus.getDefault().removeStickyEvent(eventNewScore);
    }
}

