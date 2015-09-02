package com.example.ilkut.iskibris;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class JobListingsAdapter extends ArrayAdapter<JobListing> {

    public JobListingsAdapter(Context context, ArrayList<JobListing> jobListings) {
        super(context, 0, jobListings);
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

            mImageView.setImageBitmap(jobListing.getCompanyLogo());         //TODO: Default image -> default value when set in constructor.
            mTitle.setText(jobListing.getJobTitle());
            mPubDate.setText(jobListing.getPubDate());
            mExpDate.setText(jobListing.getJobExpires());
            //TODO: Filled!

        return convertView;
    }
}
