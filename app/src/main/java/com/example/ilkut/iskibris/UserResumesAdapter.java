package com.example.ilkut.iskibris;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

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

        if (userResume.getCandidatePhotoLink() != null && !(userResume.getCandidatePhotoLink().trim().equals(""))) {
            Picasso.with(getContext()).load(userResume.getCandidatePhotoLink()).into(new Target() {                 //TODO: Context problem here!
                @Override
                public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom loadedFrom) {
                    userResume.setCandidatePhoto(bitmap);
                    mImage.setImageBitmap(bitmap);
                }

                @Override
                public void onBitmapFailed(Drawable drawable) {
                    //TODO: Error Image
                }

                @Override
                public void onPrepareLoad(Drawable drawable) {
                    //TODO: Loading Image
                }
            });
        } else {
            //TODO: default image
        }

            mTitle.setText(userResume.getTitle());
            mName.setText(userResume.getCandidateName());
            mLocation.setText(userResume.getLocation());

        return convertView;
    }

}
