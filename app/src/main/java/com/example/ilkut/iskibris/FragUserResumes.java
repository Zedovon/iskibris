package com.example.ilkut.iskibris;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
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

public class FragUserResumes extends android.support.v4.app.Fragment {

    ListView mainListView;
    AlertDialog mDialog;
    Context mContext;
    SwipeRefreshLayout mRefreshLayout;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_user_resumes, container, false);
        mainListView = (ListView) view.findViewById(R.id.fragUserResumesListView);
        mRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.fragUserResumesRefreshLayout);
        mContext = getActivity();

        SharedPreferences mPrefs = mContext.getSharedPreferences("PREFS", Context.MODE_PRIVATE);
        String URL = getResources().getString(R.string.URL_USER_RESUMES);
        final HashMap<String, String> params = new HashMap<>();
        mDialog = new AlertDialog.Builder(mContext).create();
        mDialog.setTitle(getString(R.string.warning_word));


        params.put("username", mPrefs.getString("uname", null));
        //TODO: What will happen if the username is null? (= You'll get an ERROR_POST_FAILED)

        final UserResumesOperations mOperations = new UserResumesOperations(mContext, URL, SingletonCache.getInstance().getUserResumesCache());
        mOperations.setResponseListener(new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                mOperations.onRequestResponse(response, new ResponseOperations.ImageResponseListener() {
                            @Override
                            public void onImageReceived() {
                                populateListView(SingletonCache.getInstance().getUserResumesCache());
                            }
                        }, true);
            }
        });
        mOperations.setResponseErrorListener(new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                ResponseOperations.onRequestErrorRespone(mContext, error, new ResponseOperations.TryAgainAction() {
                    @Override
                    public void onTryAgain() {
                        mOperations.fetchUserResumes(params, true);
                    }
                });
            }
        });


        mRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
           /*     mOperations.setResponseListener(new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        mOperations.onRequestResponse(response, new ResponseOperations.ImageResponseListener() {
                            @Override
                            public void onImageReceived() {
                                populateListView(SingletonCache.getInstance().getUserResumesCache());
                                mRefreshLayout.setRefreshing(false);
                            }
                        }, false);
                    }
                });
                mOperations.setResponseErrorListener(new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        mRefreshLayout.setRefreshing(false);
                        ResponseOperations.onRequestErrorRespone(mContext, error, new ResponseOperations.TryAgainAction() {
                            @Override
                            public void onTryAgain() {
                                mRefreshLayout.setRefreshing(true);
                                mOperations.fetchUserResumes(params, false);
                            }
                        });
                    }
                });*/
                //mOperations.fetchUserResumes(params, true);
            }
        });


        if (SingletonCache.getInstance().getUserResumesCache().isEmpty()) {
            mOperations.fetchUserResumes(params, true);   //Fetch the user resumes, and put them into the SingletonCache
        } else {
            populateListView(SingletonCache.getInstance().getUserResumesCache());
        }


        return view;
    }


    public void populateListView(ArrayList<UserResume> userResumes) {
        if (userResumes != null && !(userResumes.isEmpty())) {
            UserResumesAdapter mAdapter = new UserResumesAdapter(mContext, userResumes);
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
        String itemID = SingletonCache.getInstance().getUserResumesCache().get(itemPosition).getPostID();

        if (itemID != null) {
            for (UserResume i : SingletonCache.getInstance().getUserResumesCache()) {
                if (i.getPostID().equals(itemID)) {
                    if(i.getCandidatePhoto() != null) {
                        FragDisplayResume mDisplay = new FragDisplayResume();
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
