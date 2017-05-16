package com.example.izabelawojciak.tetris;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * Created by izabelawojciak on 23.04.2017.
 */

public class ScoresAdapter extends RecyclerView.Adapter<ScoresAdapter.ViewHolder>{

    private List<Score> scores;

    public ScoresAdapter(List<Score> scores){
        this.scores = scores;
    }

    @Override
    public ScoresAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.item_score_list, parent, false);

        ViewHolder viewHolder = new ViewHolder(itemView);
        viewHolder.score = (TextView) itemView.findViewById(R.id.score);
        viewHolder.name = (TextView) itemView.findViewById(R.id.name);
        viewHolder.date = (TextView) itemView.findViewById(R.id.date);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ScoresAdapter.ViewHolder holder, int position) {

        Score score = getItem(position);

        holder.score.setText(String.valueOf(score.getScore()));
        holder.name.setText(score.getName());
        holder.date.setText(score.getDate());

    }

    @Override
    public int getItemCount() {
        Log.println(Log.INFO, "SIZE", String.valueOf(scores.size()));
        return scores.size();
    }

    private Score getItem(int position) {
        return scores.get(position);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        public TextView name;
        public TextView score;
        public TextView date;

        public ViewHolder(View itemView) {
            super(itemView);
        }
    }
}

