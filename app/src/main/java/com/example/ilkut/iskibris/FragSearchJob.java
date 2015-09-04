package com.example.ilkut.iskibris;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.android.volley.Response;

import java.util.HashMap;

public class FragSearchJob extends android.support.v4.app.Fragment {

    Button searchButton;
    EditText searchEdit;
    Spinner regionSpinner;
    Spinner jobTypeSpinner;
    Spinner jobCategoriesSpinner;
    Context mContext;
    FragListener mListener;

    public interface FragListener {

        enum ExternalFragment {
            SEARCH_JOBS_RESULTS,
            SEARCH_JOBS_SCREEN,
            SEARCH_RESUMES_RESULTS,
            SEARCH_RESUMES_SCREEN
        }

        void setExternalFragment(ExternalFragment frag);
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mListener = (FragListener) activity;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_jobs_search_screen, container, false);

        mContext = getActivity();

        searchEdit = (EditText) view.findViewById(R.id.searchJobMainEdit);
        searchButton = (Button) view.findViewById(R.id.searchJobSearchButton);
        regionSpinner = (Spinner) view.findViewById(R.id.searchJobRegionsSpinner);
        jobTypeSpinner = (Spinner) view.findViewById(R.id.searchJobJobTypeSpinner);
        jobCategoriesSpinner = (Spinner) view.findViewById(R.id.searchJobJobCategorySpinner);

        ArrayAdapter<String> regionAdapter = new ArrayAdapter<>(mContext, android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.job_regions));
        ArrayAdapter<String> jobCategoriesAdapter = new ArrayAdapter<>(mContext, android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.job_categories));
        ArrayAdapter<String> jobTypesAdapter = new ArrayAdapter<>(mContext, android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.job_types));

        regionSpinner.setAdapter(regionAdapter);
        jobCategoriesSpinner.setAdapter(jobCategoriesAdapter);
        jobTypeSpinner.setAdapter(jobTypesAdapter);


        final String URL = getResources().getString(R.string.URL_SEARCH_JOBS);
        final JobListingsOperations mOperations = new JobListingsOperations(mContext, URL, SingletonCache.getInstance().getSearchJobListingsCache());


        mOperations.setResponseListener(new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                mOperations.onRequestResponse(response, true);
            }
        });

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HashMap<String, String> params = new HashMap<>();
                params.put("keywords", searchEdit.getText().toString().trim());

                params.put("region", regionSpinner.getSelectedItem().toString());
                params.put("jobCat", jobCategoriesSpinner.getSelectedItem().toString());
                params.put("jobType", getSlug());       //TODO: Slug problem!
                mOperations.fetchJobListings(params, true);
                //TODO: Do some check to see whether everything is fine

            }
        });

        return view;
    }

    private String getSlug() {
        return getResources().getStringArray(R.array.job_types_slug)[jobTypeSpinner.getSelectedItemPosition()];
    }
}
