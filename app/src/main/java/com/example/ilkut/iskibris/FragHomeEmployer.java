package com.example.ilkut.iskibris;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

/**
 * Created by ilkut on 14.8.2015.
 */
public class FragHomeEmployer extends android.support.v4.app.Fragment {

    ListView mainListView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.frag_home_employer, container, false);
        mainListView = (ListView) view.findViewById(R.id.homeEmployerMainListView);
            return view;
    }



}
