package com.example.ilkut.iskibris;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;


public class UserResumesAdapter extends ArrayAdapter<UserResume> {

    public UserResumesAdapter(Context context, ArrayList<UserResume> userResumes) {
        super(context, 0, userResumes);
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final UserResume userResume = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.row_user_resume, parent, false);
        }

        final TextView mName = (TextView) convertView.findViewById(R.id.userResumeNameText);
        final TextView mTitle = (TextView) convertView.findViewById(R.id.userResumeTitleText);
        final TextView mLocation = (TextView) convertView.findViewById(R.id.userResumeLocationText);
        final TextView mStatus = (TextView) convertView.findViewById(R.id.userResumeStatusText);
        //TODO: Get the status of the resume, somehow (whether it is hidden or published)
        //TODO: IMAGE URL!
        final ImageView mImage = (ImageView) convertView.findViewById(R.id.userResumeImage);


            mImage.setImageBitmap(userResume.getCandidatePhoto());      //Photo is already downloaded, set the photo and at the same time, the texts, so that there is no delay
            mTitle.setText(userResume.getTitle());
            mName.setText(userResume.getCandidateName());
            mLocation.setText(userResume.getLocation());

        return convertView;
    }

}
