package com.example.ilkut.iskibris;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.android.volley.Response;
import com.android.volley.VolleyError;

import java.util.ArrayList;
import java.util.HashMap;

public class FragJobListings extends android.support.v4.app.Fragment {
    ListView mainListView;
    AlertDialog mDialog;
    Context mContext;
    SwipeRefreshLayout mRefreshLayout;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_job_listings, container, false);
        mainListView = (ListView) view.findViewById(R.id.fragJobListingsListView);
        mRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.fragJobListingsRefreshLayout);
        mContext = getActivity();

        mDialog = new AlertDialog.Builder(mContext).create();
        mDialog.setTitle(getString(R.string.warning_word));

        SharedPreferences mPrefs = mContext.getSharedPreferences("PREFS", Context.MODE_PRIVATE);
        String URL = getResources().getString(R.string.URL_JOB_LISTINGS);
        final HashMap<String, String> params = new HashMap<>();

        String check = mPrefs.getString("uname", null);
        if (check != null) {
            params.put("username", mPrefs.getString("uname", null));
        } else {
            mDialog.setMessage(mContext.getString(R.string.unexpected_error));
            mDialog.setButton(DialogInterface.BUTTON_NEUTRAL, getResources().getText(R.string.ok_button_word), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    mDialog.cancel();
                }
            });
            mDialog.show();
            //TODO: Display an error image
        }


        final JobListingsOperations mOperations = new JobListingsOperations(mContext, URL, SingletonCache.getInstance().getJobListingsCache());

        mOperations.setResponseListener(new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                mOperations.onRequestResponse(response, true, new ResponseOperations.ImageResponseListener() {
                    @Override
                    public void onImageReceived() {
                        populateListView(SingletonCache.getInstance().getJobListingsCache());
                    }
                });

              /*  if (response.endsWith("SUCCESS")) {                 //TODO: Server doesn't response!
                    mOperations.onRequestResponse(response, new ResponseOperations.ImageResponseListener() {
                        @Override
                        public void onImageReceived() {
                            populateListView(SingletonCache.getInstance().getJobListingsCache());
                        }
                    }, true);
                } else if (response.endsWith("ERROR_POST_FAILED")) {
                    mDialog.setMessage(mContext.getString(R.string.unexpected_error));
                    mDialog.setButton(DialogInterface.BUTTON_NEUTRAL, getResources().getText(R.string.try_again_word), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            mOperations.fetchJobListings(params, true);
                            mDialog.cancel();
                        }
                    });
                }*/
            }

        });


        mRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mOperations.fetchJobListings(params, false);
            }
        });

        mOperations.setResponseErrorListener(new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                ResponseOperations.onRequestErrorRespone(mContext, error, new ResponseOperations.TryAgainAction() {
                    @Override
                    public void onTryAgain() {
                        mOperations.fetchJobListings(params, true);
                    }
                });

            }
        });

        if (SingletonCache.getInstance().getJobListingsCache().isEmpty()) {
            mOperations.fetchJobListings(params, true);   //Fetch the job listings, and put them into the SingletonCache
        } else {
            populateListView(SingletonCache.getInstance().getJobListingsCache());
        }

        return view;

    }


    public void populateListView(ArrayList<JobListing> jobListings) {

        if (jobListings != null && !(jobListings.isEmpty())) {
            JobListingsAdapter mAdapter = new JobListingsAdapter(mContext, jobListings);
            mainListView.setAdapter(mAdapter);
            mainListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    onListViewItemClicked(position);
                }
            });
        }


    }

    public void onListViewItemClicked(int itemPosition) {
        String itemID = SingletonCache.getInstance().getJobListingByPosition(itemPosition).getPostID();

        if (itemID != null) {
            for (JobListing i : SingletonCache.getInstance().getJobListingsCache()) {
                if (i.getPostID().equals(itemID)) {
                    if (i.getCompanyLogo() != null) {
                        FragDisplayJobListing mDisplay = new FragDisplayJobListing();
                        Bundle mBundle = new Bundle();
                        mBundle.putString("postID", itemID);
                        mDisplay.setArguments(mBundle);
                        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, mDisplay).addToBackStack(null).commit();
                        break;
                    }
                }
            }
        } else {
            mDialog.setMessage(mContext.getString(R.string.unexpected_error));
            mDialog.setButton(DialogInterface.BUTTON_NEUTRAL, getResources().getText(R.string.ok_button_word), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    mDialog.cancel();
                }
            });
        }
    }

}
