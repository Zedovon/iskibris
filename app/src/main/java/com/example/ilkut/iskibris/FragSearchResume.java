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

/**
 * Created by ilkut on 28.8.2015.
 */
public class FragSearchResume extends android.support.v4.app.Fragment {

    private Context mContext;
    EditText resumeMainEdit;
    Spinner resumeJobCatSpinner, resumeLocationSpinner;
    Button resumeSearchButton;
    FragSearchJob.FragListener mListener;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_resumes_search_screen, container, false);
        mContext = getActivity();

        resumeMainEdit = (EditText) view.findViewById(R.id.searchResumesMainEdit);
        resumeJobCatSpinner = (Spinner) view.findViewById(R.id.searchResumesJobCategorySpinner);
        resumeLocationSpinner = (Spinner) view.findViewById(R.id.searchResumesRegionsSpinner);
        resumeSearchButton = (Button) view.findViewById(R.id.searchResumesSearchButton);


        ArrayAdapter<String> mJobCatAdapter = new ArrayAdapter<>(mContext, android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.job_categories));
        ArrayAdapter<String> mLocationAdapter = new ArrayAdapter<>(mContext, android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.job_regions));
        resumeJobCatSpinner.setAdapter(mJobCatAdapter);
        resumeLocationSpinner.setAdapter(mLocationAdapter);

        final String URL = getResources().getString(R.string.URL_SEARCH_USER_RESUMES);
        final UserResumesOperations mOperations = new UserResumesOperations(mContext, URL, SingletonCache.getInstance().getSearchUserResumesCache());
        mOperations.setResponseListener(new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                mOperations.onRequestResponse(response, new ResponseOperations.ImageResponseListener() {
                    @Override
                    public void onImageReceived() {
                        mListener.setExternalFragment(FragSearchJob.FragListener.ExternalFragment.SEARCH_RESUMES_RESULTS);
                    }
                });
            }
        });


        resumeSearchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HashMap<String, String> params = new HashMap<>();
                params.put("keywords", resumeMainEdit.getText().toString().trim());

                params.put("region", resumeLocationSpinner.getSelectedItem().toString());       //TODO: Slugs!
                params.put("jobCat", resumeJobCatSpinner.getSelectedItem().toString());
                mOperations.fetchUserResumes(params);
                //TODO: Do some check to see whether everything is fine
            }
        });

        return view;
    }



}