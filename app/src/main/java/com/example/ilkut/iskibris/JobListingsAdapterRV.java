package com.example.ilkut.iskibris;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class JobListingsAdapterRV extends RecyclerView.Adapter<JobListingsAdapterRV.JobListingViewHolder>{

    public static class JobListingViewHolder extends RecyclerView.ViewHolder{
        CardView mCard;
        ImageView mCompanyLogo;
        TextView mTitle;
        TextView mCompanyName;
        TextView mJobType;       //TODO: You might want to use images here
        TextView mLocation;

        JobListingViewHolder(View itemView) {
            super(itemView);
            mCard = (CardView)itemView.findViewById(R.id.cv);
            mCompanyLogo = (ImageView)itemView.findViewById(R.id.logo_photo);
            mTitle = (TextView)itemView.findViewById(R.id.job_title);
            mCompanyName = (TextView)itemView.findViewById(R.id.company_name);
            mJobType = (TextView)itemView.findViewById(R.id.job_type);
            mLocation = (TextView)itemView.findViewById(R.id.job_location);
        }
    }

    List<JobListing> mJobListings;

    JobListingsAdapterRV(List<JobListing> jobListings){
        this.mJobListings = jobListings;
    }

    @Override
    public int getItemCount() {
        return mJobListings.size();
    }

    @Override
    public JobListingViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
     View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_job_listing_rv, parent, false);
    JobListingViewHolder mHolder = new JobListingViewHolder(view);
        return mHolder;
    }

    @Override
    public void onBindViewHolder(JobListingViewHolder holder, int position) {
//        holder.mCompanyLogo.setImageBitmap(mJobListings.get(position).getCompanyLogo());
        holder.mCompanyName.setText("Hello");
        holder.mJobType.setText("Hello");
        holder.mTitle.setText("Hello");
        //TODO: Location field missing from the JobListing class
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }
}
