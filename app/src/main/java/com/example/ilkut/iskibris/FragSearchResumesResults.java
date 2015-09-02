package com.example.ilkut.iskibris;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;

public class FragSearchResumesResults extends android.support.v4.app.Fragment {

    ListView mainListView;
    Context mContext;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_resumes_search_results, container, false);
        mContext = getActivity();

        mainListView = (ListView) view.findViewById(R.id.searchResumesResultsListView);

        ArrayList<UserResume> displayUserResumes = SingletonCache.getInstance().getSearchUserResumesCache();
        UserResumesAdapter mAdapter = new UserResumesAdapter(mContext, displayUserResumes);
        mainListView.setAdapter(mAdapter);

        return view;
    }
}
