package com.example.tom_and_jerry_part1.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;


import com.example.tom_and_jerry_part1.DB.RecordsList;
import com.example.tom_and_jerry_part1.Interfaces.CallBack_List;
import com.example.tom_and_jerry_part1.R;


public class ListFragment extends Fragment {

    private ListView fraglist_LV_list;
    private CallBack_List callBack_list;

    public void setCallBack_list(CallBack_List callBack_list){
        this.callBack_list = callBack_list;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list, container, false);
        startViews(view);
        initViews();

        fraglist_LV_list.setOnItemClickListener((adapterView, view1, position, l) -> {
            double lat = RecordsList.getInstance().getTopRecords().get(position).getLat();
            double lng = RecordsList.getInstance().getTopRecords().get(position).getLng();
            String playerName = RecordsList.getInstance().getTopRecords().get(position).getName();
            callBack_list.setMapLocation(lat,lng,playerName);
        });
        return view;
    }
    private void initViews() {
        if (RecordsList.getInstance().getTopRecords() != null) {
            ScoreboardAdapter adapter = new ScoreboardAdapter(getActivity(), R.layout.list_item_scoreboard, RecordsList.getInstance().getTopRecords());
            fraglist_LV_list.setAdapter(adapter);
        }
    }

    private void startViews(View view) {
        fraglist_LV_list = view.findViewById(R.id.fraglist_LV_list);
    }
}
