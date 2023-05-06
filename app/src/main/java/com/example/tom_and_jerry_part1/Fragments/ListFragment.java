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


import com.example.tom_and_jerry_part1.Interfaces.CallBack_List;
import com.example.tom_and_jerry_part1.R;
//import com.google.gson.Gson;


public class ListFragment extends Fragment {

    private ListView fraglist_LV_list;
    private CallBack_List callBack_list;
//    private MyDB allRecords;
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
//        listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
//
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
//                double lat = allRecords.getResults().get(position).getLat();
//                double lon = allRecords.getResults().get(position).getLon();
//                String namePlayer = allRecords.getResults().get(position).getName();
//                callBack_list.setMapLocation(lat,lon,namePlayer);
//            }
//        });
        return view;
    }
    private void initViews() {
//        allRecords = new Gson().fromJson(MySPV.getInstance().getStrSP(RECORD,""),MyDB.class);
//        if(allRecords != null){
//            ArrayAdapter<Record> adapter = new ArrayAdapter<Record>(getActivity(), android.R.layout.simple_expandable_list_item_1,allRecords.getResults());
//            listView.setAdapter(adapter);
//        }

    }

    private void startViews(View view) {
        fraglist_LV_list = view.findViewById(R.id.fraglist_LV_list);
    }


}
