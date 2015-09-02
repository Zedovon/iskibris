package com.example.ilkut.iskibris;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by ilkut on 27.8.2015.
 */
public class FragDialogApply extends android.support.v4.app.Fragment{

    EditText diaApplyMessage;
    Spinner diaApplyResumeSpinner;
    Button diaApplySendButton;
    LinearLayout diaApplyMainLayout;
    AlertDialog mDialog;
    Context mContext;
    JobListing applyJobListing;
    SharedPreferences mPrefs;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_dialog_apply, container, false);
        diaApplyMessage = (EditText) view.findViewById(R.id.diaApplyMessage);
        diaApplyResumeSpinner = (Spinner) view.findViewById(R.id.diaApplyResumeSpinner);
        diaApplySendButton = (Button) view.findViewById(R.id.diaSendButton);
        diaApplyMainLayout = (LinearLayout) view.findViewById(R.id.diaApplyMainLayout);
        mContext = getActivity();
        mPrefs = mContext.getSharedPreferences("PREFS", Context.MODE_PRIVATE);
        String postID = getArguments().getString("postID");

        for (JobListing i: SingletonCache.getInstance().getJobListingsCache()){
            if(i.getPostID().equals(postID)){
                applyJobListing = i;
            }
        }

        mDialog = new AlertDialog.Builder(mContext).create();



        diaApplySendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendApplication();
            }
        });

        if(SingletonCache.getInstance().getUserResumesCache().isEmpty()){
            fetchUserResumes();     //Fetches the user resumes, and then populates the spinner
        }else{
            populateSpinner();
        }

        return view;

    }

    private void fetchUserResumes(){
        final UserResumesOperations mOperations = new UserResumesOperations(mContext,
                getResources().getString(R.string.URL_USER_RESUMES),
                SingletonCache.getInstance().getUserResumesCache());
        mOperations.setResponseListener(new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                mOperations.onRequestResponse(response, null, true);
                populateSpinner();

            }
        });
        mOperations.setResponseErrorListener(new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                ResponseOperations.onRequestErrorRespone(mContext, error, new ResponseOperations.TryAgainAction() {
                    @Override
                    public void onTryAgain() {
                        fetchUserResumes();         //TODO: CAUTION: infinite loop might occur
                    }
                });
            }
        });

        HashMap<String, String> params = new HashMap<>();
        String mString = mPrefs.getString("uname", null);
        if(mPrefs != null){
            params.put("username" ,mString);
        }else{
            mDialog.setTitle(mContext.getString(R.string.error_word));
            mDialog.setMessage(mContext.getString(R.string.null_pointer_exception));
            mDialog.setButton(DialogInterface.BUTTON_POSITIVE, mContext.getString(R.string.ok_button_word), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    mDialog.cancel();
                    getActivity().getSupportFragmentManager().popBackStack();
                }
            });
        }

        mOperations.fetchUserResumes(params, true);

    }

    private void populateSpinner(){
        ArrayList<String> resumeTitles = new ArrayList<>();
        for (UserResume i: SingletonCache.getInstance().getUserResumesCache()){
            resumeTitles.add(i.getTitle());
        }

        ArrayAdapter<String> mAdapter = new ArrayAdapter<>(mContext, android.R.layout.simple_spinner_item ,resumeTitles);
        diaApplyResumeSpinner.setAdapter(mAdapter);

    }

    private void sendApplication() {

        String url = getResources().getString(R.string.URL_APPLY_JOB);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        handleResponse(response);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                ResponseOperations.onRequestErrorRespone(mContext, error, new ResponseOperations.TryAgainAction() {
                    @Override
                    public void onTryAgain() {
                        sendApplication();
                    }
                });

            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                String mString = mPrefs.getString("uname", null);
                if(mString != null){
                    params.put("username", mString);
                }else{
                    mDialog.setTitle(mContext.getString(R.string.error_word));
                    mDialog.setMessage(mContext.getString(R.string.null_pointer_exception));
                    mDialog.setButton(DialogInterface.BUTTON_POSITIVE, mContext.getString(R.string.ok_button_word), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            mDialog.cancel();           //Maybe also, try again?
                        }
                    });
                }

                params.put("message", diaApplyMessage.getText().toString());

                UserResume chosenResume = SingletonCache.getInstance().
                        getUserResumeByPosition(diaApplyResumeSpinner.getSelectedItemPosition());
                //TODO: Check whether it really sends the correct resume
                //TODO: (You might even want to include some kind of preview feature)

                params.put("resume", chosenResume.getDescription());
                params.put("email", applyJobListing.getApplication());
                params.put("lang", getResources().getString(R.string.language));
                params.put("jobTitle", applyJobListing.getJobTitle());

                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("Content-Type", "application/x-www-form-urlencoded");
                return params;
            }
        };

        SingletonRequest.getInstance(mContext).getRequestQueue().add(stringRequest);
    }

    //TODO: Come back to here, response-handler already exists!
    public void handleResponse(String response) {
        if (response.endsWith("SUCCESS")) {
            mDialog.setMessage(getResources().getString(R.string.frag_apply_job_success_message));
            mDialog.setButton(DialogInterface.BUTTON_NEUTRAL, getResources().getText(R.string.ok_button_word), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    mDialog.cancel();
                }
            });
            mDialog.show();
        } else if (response.trim().endsWith("ERROR_POST_FAILED")) {

            mDialog.setMessage(getResources().getString(R.string.response_error_post_failed));
            mDialog.setButton(DialogInterface.BUTTON_NEUTRAL, getResources().getText(R.string.try_again_word), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    sendApplication();
                    mDialog.cancel();
                }
            });
            mDialog.show();

            //TODO: Are you sure that with this error, try again will work?
        } else if (response.trim().endsWith("ERROR")) {
            mDialog.setMessage(getResources().getString(R.string.response_error_post_failed));
            mDialog.setButton(DialogInterface.BUTTON_NEUTRAL, getResources().getText(R.string.try_again_word), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    sendApplication();
                    mDialog.cancel();
                }
            });
            mDialog.show();

        }

    }

}
