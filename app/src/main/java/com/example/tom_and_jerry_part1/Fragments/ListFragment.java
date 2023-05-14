package com.example.tom_and_jerry_part1.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;


import com.example.tom_and_jerry_part1.DB.MyDB;
import com.example.tom_and_jerry_part1.DB.MySP;
import com.example.tom_and_jerry_part1.DB.Record;
import com.example.tom_and_jerry_part1.Interfaces.CallBack_List;
import com.example.tom_and_jerry_part1.R;
import com.google.gson.Gson;


public class ListFragment extends Fragment {

    private ListView fraglist_LV_list;
    private CallBack_List callBack_list;
    private MyDB allRecords;
    private final String RECORD = "records";


    public void setCallBack_list(CallBack_List callBack_list){
        this.callBack_list = callBack_list;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list, container, false);
        startViews(view);
        initViews();
        fraglist_LV_list.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                double lat = allRecords.getResults().get(position).getLat();
                double lng = allRecords.getResults().get(position).getLng();
                String playerName = allRecords.getResults().get(position).getName();
//                double lat = Data_Manager.getInstance().getTop10Records().get(position).getLat();
//                double lng = Data_Manager.getInstance().getTop10Records().get(position).getLng();
//                String playerName = Data_Manager.getInstance().getTop10Records().get(position).getName();
                callBack_list.setMapLocation(lat,lng,playerName);
            }
        });
        return view;
    }
    private void initViews() {
        allRecords = new Gson().fromJson(MySP.getInstance().getString(RECORD,""),MyDB.class);
        if(allRecords != null){
            ArrayAdapter<Record> adapter = new ArrayAdapter<Record>(getActivity(), android.R.layout.simple_expandable_list_item_1,allRecords.getResults());
            fraglist_LV_list.setAdapter(adapter);
        }
    }

    private void startViews(View view) {
        fraglist_LV_list = view.findViewById(R.id.fraglist_LV_list);
    }
}
