package com.example.tom_and_jerry_part1.DB;

import java.util.ArrayList;

public class MyDB {//list of result
    private ArrayList<Record> results;
    private final int LIMIT_TOP10 = 10;

    public MyDB() {
        this.results = new ArrayList<>();
    }

    public ArrayList<Record> getResults() {
        results.sort((r1, r2) -> r2.getScore() - r1.getScore());
        if (results.size() == LIMIT_TOP10) {
            results.remove(LIMIT_TOP10 - 1);
        }
        return results;
    }

    public MyDB setResults(ArrayList<Record> results) {
        this.results = results;
        return this;
    }
}