package com.example.ilkut.iskibris;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.internal.view.menu.ListMenuItemView;
import android.support.v7.widget.LinearLayoutCompat;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;

import java.util.ArrayList;
import java.util.HashMap;

public class FragHomeEmployee extends android.support.v4.app.Fragment {
    ListView mainListView;
    AlertDialog mDialog;
    Context mContext;
    SwipeRefreshLayout mRefreshLayout;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_home_employee, container, false);
        mainListView = (ListView) view.findViewById(R.id.homeEmployeeMainListView);
        mRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.homeEmployeeRefreshLayout);
        mContext = getActivity();

        mDialog = new AlertDialog.Builder(mContext).create();
        mDialog.setTitle(getString(R.string.warning_word));

        String URL = getResources().getString(R.string.URL_JOB_LISTINGS);
        final HashMap<String, String> params = new HashMap<>();
        //TODO: Well, do something about it?
        params.put("username", "iskibris");

        //TODO: What will happen if the username is null?  ->  That's not the way it works, you know that.

        final JobListingsOperations mOperations = new JobListingsOperations(mContext, URL, SingletonCache.getInstance().getJobListingsCache());
        mOperations.setResponseListener(new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                mOperations.onRequestResponse(response,true);
                populateListView(SingletonCache.getInstance().getJobListingsCache());
                mRefreshLayout.setRefreshing(false);
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

        mRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mOperations.fetchJobListings(params, false);
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
        mainListView.setDividerHeight(0);

        if (jobListings != null && !(jobListings.isEmpty())) {
            JobListingsAdapter mAdapter = new JobListingsAdapter(mContext, jobListings);
            mainListView.setAdapter(mAdapter);
            mainListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    onListItemClicked(position);
                }
            });



        }
    }

    public void onListItemClicked(int itemPosition) {
        String itemID = SingletonCache.getInstance().getJobListingByPosition(itemPosition).getPostID();

        if (itemID != null) {
            for (JobListing i : SingletonCache.getInstance().getJobListingsCache()) {
                if (i.getPostID().equals(itemID)) {
                        FragDisplayJobListing mDisplay = new FragDisplayJobListing();
                        Bundle mBundle = new Bundle();
                        mBundle.putString("postID", itemID);
                        mDisplay.setArguments(mBundle);
                        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, mDisplay).addToBackStack(null).commit();
                        break;
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
