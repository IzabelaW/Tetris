package com.example.izabelawojciak.tetris;

import android.content.SharedPreferences;
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
    public static final String PREFS_NAME = "MyPrefsFile";


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

        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);

        if (settings != null) {
            String names = settings.getString("Names", "NAME");
            String dates = settings.getString("Dates", "DATE");
            String scoreValues = settings.getString("Scores", "6");

            String[] namesArr = reloadStringToArrayString(names);
            String[] datesArr = reloadStringToArrayString(dates);
            int[] scoresArr = reloadStringToArrayInt(scoreValues);

            Bundle outState = new Bundle();
            outState.putStringArray("Names", namesArr);
            outState.putStringArray("Dates", datesArr);
            outState.putIntArray("Scores", scoresArr);

            populateList(outState);
        }

        if (savedInstanceState != null)
            populateList(savedInstanceState);
    }

    private void populateList(Bundle savedInstanceState) {

        scores = null;
        scores = new ArrayList<>();

        String[] names = savedInstanceState.getStringArray("Names");
        String[] dates = savedInstanceState.getStringArray("Dates");
        int[] scores = savedInstanceState.getIntArray("Scores");
        Score score;

        if (scores != null) {

            for (int i = 0; i < names.length; i++) {
                score = new Score(names[i], scores[i], dates[i]);
                this.scores.add(score);
            }

        }

        sortScores();
        myScoreAdapter = new ScoresAdapter(this.scores);
        recyclerView.setAdapter(myScoreAdapter);
        myScoreAdapter.notifyDataSetChanged();
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

        SharedPreferences settings = getSharedPreferences(PREFS_NAME,0);
        SharedPreferences.Editor editor = settings.edit();

        String[] names = new String[scores.size()];
        String[] dates = new String[scores.size()];
        int[] scoreValues = new int[scores.size()];

        for (int i = 0; i < scores.size(); i++){
            names[i] = scores.get(i).getName();
            dates[i] = scores.get(i).getDate();
            scoreValues[i] = scores.get(i).getScore();
        }

        editor.putString("Names", saveArrayIntoString(names));
        editor.putString("Dates", saveArrayIntoString(dates));
        editor.putString("Scores", saveArrayIntoString(scoreValues));

//         Commit the edits!
        editor.apply();

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

    private String saveArrayIntoString(String[] arrayList){
        String output = "";

        for (int i = 0; i < arrayList.length; i++){
            output += arrayList[i] + ";";
        }

        return output;
    }

    private String saveArrayIntoString(int[] arrayList){
        String output = "";

        for (int i = 0; i < arrayList.length; i++){
            output += arrayList[i] + ";";
        }

        return output;
    }

    private String[] reloadStringToArrayString(String string){
        string = string.replaceAll("NAME","");
        string = string.replaceAll("DATE", "");
        String[] output = string.split(";");

        return output;
    }

    private int[] reloadStringToArrayInt(String string){
        string = string.replaceAll("6","");
        String[] output = string.split(";");
        int[] tab = new int[output.length];

        if (!string.equals("")) {

            for (int i = 0; i < output.length; i++) {
                tab[i] = Integer.parseInt(output[i]);
            }
        }
        return tab;
    }

    private void sortScores(){

        Score key;
        int j;
        Score score;

        for (int i = 1; i < scores.size(); i++){
            key = scores.get(i);
            j = i - 1;

            while (j>=0 && scores.get(j).getScore() <= key.getScore()) {
                //tab[j+1] = tab[j]
                score = scores.get(j);
                scores.remove(j);
                scores.add(j+1,score);
                j--;
            }
            scores.remove(j+1);
            scores.add(j+1,key);
            //tab[j+1] = key

        }
    }
}

