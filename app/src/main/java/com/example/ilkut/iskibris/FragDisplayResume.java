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

public class FragDisplayResume extends android.support.v4.app.Fragment {

    ImageView dResCandidatePhoto;
    TextView dResTitle, dResDescription, dResPubDate, dResLocation, dResCandidateName,
            dResCandidateEmail, dResCandidateTitle, dResCandidateLink, dResCandidateEducation,
            dResCandidateExperience;
    Button dResContactButton;
    UserResume displayUserResume;
    Context mContext;
    AlertDialog mDialog;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_display_resume, container, false);

        mContext = getActivity();
        mDialog = new AlertDialog.Builder(mContext).create();

        String postID = getArguments().getString("postID", "0");
        if (getArguments() != null) {
            if (!(postID.isEmpty()) && !(postID.equals("0"))) {
                for (UserResume i : SingletonCache.getInstance().getUserResumesCache()) {
                    if (i.getPostID().equals(postID)) {
                        displayUserResume = i;
                        break;
                    }
                }
            } else {
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
        } else {
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

        dResCandidatePhoto = (ImageView) view.findViewById(R.id.dResumeCandidPhoto);
        dResTitle = (TextView) view.findViewById(R.id.dResumeTitle);
        dResDescription = (TextView) view.findViewById(R.id.dResumeDescription);
        dResContactButton = (Button) view.findViewById(R.id.dResumeContactButton);
        dResPubDate = (TextView) view.findViewById(R.id.dResumePubDate);
        dResLocation = (TextView) view.findViewById(R.id.dResumeLocation);
        dResCandidateName = (TextView) view.findViewById(R.id.dResumeCandidateName);
        dResCandidateEmail = (TextView) view.findViewById(R.id.dResumeCandidateEmail);
        dResCandidateTitle = (TextView) view.findViewById(R.id.dResumeCandidateTitle);
        dResCandidateLink = (TextView) view.findViewById(R.id.dResumeCandidateLink);
        dResCandidateEducation = (TextView) view.findViewById(R.id.dResumeCandidateEducation);
        dResCandidateExperience = (TextView) view.findViewById(R.id.dResumeCandidateExperience);


        dResContactButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        String role = getActivity().getSharedPreferences("PREFS", Context.MODE_PRIVATE).getString("role", null);
        if (role != null) {
            if (role.equals("employer")) {
                dResContactButton.setVisibility(View.VISIBLE);
                dResContactButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        FragDialogContact mDisplay = new FragDialogContact();
                        Bundle mBundle = new Bundle();
                        mBundle.putString("postID", displayUserResume.getPostID());
                        mDisplay.setArguments(mBundle);
                        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, mDisplay).addToBackStack(null).commit();
                    }
                });
            } else {
                dResContactButton.setVisibility(View.GONE);
            }
        }

        try {
            dResCandidatePhoto.setImageBitmap(displayUserResume.getCandidatePhoto());
            dResPubDate.setText(displayUserResume.getPubDate());
            dResLocation.setText(displayUserResume.getLocation());
            dResCandidateName.setText(displayUserResume.getCandidateName());
            dResCandidateEmail.setText(displayUserResume.getCandidateEmail());
            dResCandidateTitle.setText(displayUserResume.getCandidateTitle());

            dResDescription.setText(Html.fromHtml(displayUserResume.getDescription()));

            for (UserResumeEducation i : displayUserResume.getCandidateEducations()) {
                dResCandidateEducation.append(i.getLocation() + "\n");
                dResCandidateEducation.append(i.getQualification() + "\n");
                dResCandidateEducation.append(i.getDate() + "\n");
                dResCandidateEducation.append(Html.fromHtml(i.getNotes()) + "\n\n");
            }

            for (UserResumeExperience i : displayUserResume.getCandidateExperiences()) {
                dResCandidateExperience.append(i.getEmployer() + "\n");
                dResCandidateExperience.append(i.getJob_title() + "\n");
                dResCandidateExperience.append(i.getDate() + "\n");
                dResCandidateExperience.append(Html.fromHtml(i.getNotes()) + "\n\n");
            }

            for (UserResumeLink i : displayUserResume.getCandidateLinks()) {
                dResCandidateLink.append(i.getName() + "\n");
                dResCandidateLink.append(i.getUrl() + "\n\n");
            }

        } catch (Exception e) {
            Toast.makeText(getActivity().getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
        }


        return view;
    }
}
