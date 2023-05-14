package com.example.tom_and_jerry_part1.DB;

import androidx.annotation.NonNull;

public class Record {
    private int score = 0;
    private String name ="";
    private double lat = 0.0;
    private double lng = 0.0;


    public Record(){ }

    public int getScore() {
        return score;
    }

    public Record setScore(int score) {
        this.score = score;
        return this;
    }

    public String getName() {
        return name;
    }

    public Record setName(String name) {
        this.name = name;
        return this;
    }

    public double getLat() {
        return lat;
    }

    public Record setLat(double lat) {
        this.lat = lat;
        return this;
    }

    public double getLng() {
        return lng;
    }

    public Record setLng(double lon) {
        this.lng = lon;
        return this;
    }

    @NonNull
    @Override
    public String toString() {
        return score + "\t" + name + " \t" + lat + ", " + lng;
    }
}
