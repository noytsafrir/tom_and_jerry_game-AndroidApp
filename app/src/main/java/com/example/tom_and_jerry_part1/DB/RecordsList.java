package com.example.tom_and_jerry_part1.DB;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class RecordsList {
    private final String SP_RECORDS = "SP_RECORDS";
    private final int MAX_RECORDS = 10;
    private static RecordsList instance = null;
    private ArrayList<Record> topRecords;

    public RecordsList() {
        this.topRecords = loadData();
        sortRecords();

        if (topRecords == null)
            this.topRecords = new ArrayList<>();
    }

    public static RecordsList getInstance() {
        if (instance == null)
            instance = new RecordsList();

        return instance;
    }

    public ArrayList<Record> getTopRecords() {
        return topRecords;
    }

    public void addRecord(Record newRecord){
        if(topRecords.size() < MAX_RECORDS) {
            topRecords.add(newRecord);
            saveData();
        }
        Record lastRecord = topRecords.get(topRecords.size()-1);
        if (lastRecord.getScore() < newRecord.getScore()){
            topRecords.remove(lastRecord);
            topRecords.add(newRecord);
            saveData();
        }
        sortRecords();
    }

    public ArrayList<Record> loadData() {
        Type typeMyType = new TypeToken<ArrayList<Record>>(){}.getType();
        String recordsJson = MySP.getInstance().getString(SP_RECORDS, "");
        return new Gson().fromJson(recordsJson, typeMyType);
    }

    private void saveData() {
        sortRecords();
        String recordsJson = new Gson().toJson(topRecords);
        MySP.getInstance().putString(SP_RECORDS, recordsJson);
    }

    private void sortRecords() {
        topRecords.sort((r1, r2) -> r2.getScore() - r1.getScore());
    }
}
