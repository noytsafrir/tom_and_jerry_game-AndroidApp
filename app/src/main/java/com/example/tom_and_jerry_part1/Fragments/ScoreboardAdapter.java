package com.example.tom_and_jerry_part1.Fragments;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.tom_and_jerry_part1.DB.Record;
import com.example.tom_and_jerry_part1.R;

import java.util.ArrayList;

public class ScoreboardAdapter extends ArrayAdapter<Record> {
    private ArrayList<Record> records;

    public ScoreboardAdapter(Context context, int resource, ArrayList<Record> objects) {
        super(context, resource, objects);
        this.records = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item_scoreboard, parent, false);
        }

        TextView tv_rank = convertView.findViewById(R.id.tv_rank);
        TextView tv_score = convertView.findViewById(R.id.tv_score);
        TextView tv_name = convertView.findViewById(R.id.tv_name);

        Record record = records.get(position);

        tv_rank.setText(String.valueOf(position + 1));
        tv_score.setText(String.valueOf(record.getScore()));
        tv_name.setText(record.getName());

        return convertView;
    }
}
