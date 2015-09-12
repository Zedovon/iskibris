package com.example.ilkut.iskibris;

import android.content.Context;
import android.graphics.drawable.TransitionDrawable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class JobListingsAdapter extends ArrayAdapter<JobListing> {

    Context mContext;

    public JobListingsAdapter(Context context, ArrayList<JobListing> jobListings) {
        super(context, 0, jobListings);
        mContext = context;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final JobListing jobListing = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.row_job_listing, parent, false);
        }

        final TextView mTitle = (TextView) convertView.findViewById(R.id.jobListingTitleText);
        final TextView mPubDate = (TextView) convertView.findViewById(R.id.jobListingPubDate);
        final TextView mExpDate = (TextView) convertView.findViewById(R.id.jobListingExpDate);
        ImageView mFilled = (ImageView) convertView.findViewById(R.id.filledIconImageView);

        //TODO: IMAGE URL!
        final ImageView mImageView = (ImageView) convertView.findViewById(R.id.jobListingImage);

        if (jobListing.getCompanyLogoLink() != null && !(jobListing.getCompanyLogoLink().trim().equals(""))) {
            Picasso.with(mContext).load(jobListing.getCompanyLogoLink()).into(mImageView);
        } else {
            //TODO: default image
        }

        mTitle.setText(jobListing.getJobTitle());
        mPubDate.setText(jobListing.getPubDate());
        mExpDate.setText(jobListing.getJobExpires());
        //TODO: Filled!

        return convertView;
    }

}
