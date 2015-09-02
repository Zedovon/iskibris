package com.example.ilkut.iskibris;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;

public class FragSearchJobsResults extends android.support.v4.app.Fragment {

    ListView mainResultsListView;
    Context mContext;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_jobs_search_results, container, false);

        mContext = getActivity();

        mainResultsListView = (ListView) view.findViewById(R.id.searchJobsResultsListView);
        ArrayList<JobListing> mListings = SingletonCache.getInstance().getSearchJobListingsCache();
        JobListingsAdapter mAdapter = new JobListingsAdapter(mContext, mListings);
        mainResultsListView.setAdapter(mAdapter);

        return  view;
    }
}
