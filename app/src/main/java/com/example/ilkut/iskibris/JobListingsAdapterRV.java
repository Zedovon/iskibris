package com.example.ilkut.iskibris;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import org.w3c.dom.Text;

/**
 * Created by ilkut on 4.9.2015.
 */
public class JobListingsAdapterRV extends RecyclerView.Adapter<>{

    public static class JobListingViewHolder extends RecyclerView.ViewHolder{
        CardView mCard;
        TextView mTitle;
        TextView mCompanyName;
        TextView JobType            //TODO:
    }

}
