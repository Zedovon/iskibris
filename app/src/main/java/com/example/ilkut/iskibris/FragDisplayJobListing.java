package com.example.ilkut.iskibris;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


public class FragDisplayJobListing extends android.support.v4.app.Fragment {

    ImageView dJobCompanyLogo;
    TextView dJobTitle, dJobDescription, dJobPubDate, dJobApplication, dJobJobExpires, dJobJobType,
            dJobJobCategory, dJobFilled, dJobCompanyName, dJobCompanyWebsite, dJobCompanyTagline,
            dJobCompanyTwitter, dJobCompanyDescription, dJobCompanyFacebook, dJobCompanyGoogle,
            dJobCompanyLinkedin;
    Button dJobApplyButton;
    JobListing displayJobListing;
    AlertDialog mDialog;
    Context mContext;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_display_job, container, false);

        mContext = getActivity();
        mDialog = new AlertDialog.Builder(mContext).create();

        dJobCompanyLogo = (ImageView) view.findViewById(R.id.dJobLogoImage);
        dJobTitle = (TextView) view.findViewById(R.id.dJobTitle);
        dJobDescription = (TextView) view.findViewById(R.id.dJobDescription);
        dJobApplyButton = (Button) view.findViewById(R.id.dJobApplyButton);
        dJobTitle = (TextView) view.findViewById(R.id.dJobTitle);
        dJobDescription = (TextView) view.findViewById(R.id.dJobDescription);
        dJobPubDate = (TextView) view.findViewById(R.id.dJobPubDate);
        dJobApplication = (TextView) view.findViewById(R.id.dJobApplication);
        dJobJobExpires = (TextView) view.findViewById(R.id.dJobJobExpires);
        dJobJobType = (TextView) view.findViewById(R.id.dJobJobType);
        dJobJobCategory = (TextView) view.findViewById(R.id.dJobJobCategory);
        dJobFilled = (TextView) view.findViewById(R.id.dJobFilled);
        dJobCompanyName = (TextView) view.findViewById(R.id.dJobCompanyName);
        dJobCompanyWebsite = (TextView) view.findViewById(R.id.dJobCompanyWebsite);
        dJobCompanyTagline = (TextView) view.findViewById(R.id.dJobCompanyTagline);
        dJobCompanyTwitter = (TextView) view.findViewById(R.id.dJobCompanyTwitter);
        dJobCompanyDescription = (TextView) view.findViewById(R.id.dJobCompanyDescription);
        dJobCompanyFacebook = (TextView) view.findViewById(R.id.dJobCompanyFacebook);
        dJobCompanyGoogle = (TextView) view.findViewById(R.id.dJobCompanyGoogle);
        dJobCompanyLinkedin = (TextView) view.findViewById(R.id.dJobCompanyLinkedin);

        String postID = "0";
        if(getArguments() != null){
            postID = getArguments().getString("postID", "0");
            if (!(postID.isEmpty()) && !(postID.equals("0"))) {
                for (JobListing i : SingletonCache.getInstance().getJobListingsCache()) {
                    if (i.getPostID().equals(postID)) {
                        displayJobListing = i;
                        break;
                    }
                }
            } else {
                //stop the fragment: Display a dialog, when ok is clicked, takes you back to the previous screen
                mDialog.setTitle(mContext.getString(R.string.error_word));
                mDialog.setMessage(mContext.getString(R.string.unexpected_error));
                mDialog.setButton(DialogInterface.BUTTON_POSITIVE, mContext.getString(R.string.ok_button_word), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        getActivity().getSupportFragmentManager().popBackStack();
                    }
                });
                mDialog.show();
            }
        }else{      //This code is the same as above
            mDialog.setTitle(mContext.getString(R.string.error_word));
            mDialog.setMessage(mContext.getString(R.string.unexpected_error));
            mDialog.setButton(DialogInterface.BUTTON_POSITIVE, mContext.getString(R.string.ok_button_word), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    getActivity().getSupportFragmentManager().popBackStack();
                }
            });
            mDialog.show();
        }




        try {
            dJobCompanyLogo.setImageBitmap(displayJobListing.getCompanyLogo());
            dJobTitle.setText(displayJobListing.getJobTitle());

            dJobDescription.setText(Html.fromHtml(displayJobListing.getDescription()));     //TODO: Are you sure that's all the text views that will be Html formatted

            dJobTitle.setText(displayJobListing.getJobTitle());
            dJobPubDate.setText(displayJobListing.getPubDate());
            dJobApplication.setText(displayJobListing.getApplication());
            dJobJobExpires.setText(displayJobListing.getJobExpires());
            dJobJobType.setText(displayJobListing.getJobType());
            dJobJobCategory.setText(displayJobListing.getJobCategory());
            dJobFilled.setText(displayJobListing.getFilled());
            dJobCompanyName.setText(displayJobListing.getCompanyName());
            dJobCompanyWebsite.setText(displayJobListing.getCompanyWebsite());
            dJobCompanyTagline.setText(displayJobListing.getCompanyTagline());
            dJobCompanyTwitter.setText(displayJobListing.getCompanyTwitter());

            dJobCompanyDescription.setText(Html.fromHtml(displayJobListing.getCompanyDescription()));

            dJobCompanyFacebook.setText(displayJobListing.getCompanyFacebook());
            dJobCompanyGoogle.setText(displayJobListing.getCompanyGoogle());
            dJobCompanyLinkedin.setText(displayJobListing.getCompanyLinkedin());

            String role = getActivity().getSharedPreferences("PREFS", Context.MODE_PRIVATE).getString("role", null);
            if (role != null){
                if(role.equals("candidate")){
                    dJobApplyButton.setVisibility(View.VISIBLE);
                    dJobApplyButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            FragDialogApply mDisplay = new FragDialogApply();
                            Bundle mBundle = new Bundle();
                            mBundle.putString("postID", displayJobListing.getPostID());
                            mDisplay.setArguments(mBundle);
                            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, mDisplay).addToBackStack(null).commit();
                        }
                    });
                }else{
                    dJobApplyButton.setVisibility(View.GONE);
                }
            }


        } catch (Exception e) {
            Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_LONG).show();
        }


        return view;
    }
}
