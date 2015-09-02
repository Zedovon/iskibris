package com.example.ilkut.iskibris;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class MainDrawerCustomAdapter extends ArrayAdapter<MainDrawerItem> {

    public MainDrawerCustomAdapter(Context context, ArrayList<MainDrawerItem> items) {
        super(context, 0, items);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        MainDrawerItem customItem = getItem(position);

        if (convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.drawer_item, parent,false);
        }

        ImageView myImage = (ImageView) convertView.findViewById(R.id.drawerItemImageView);
        TextView myText = (TextView) convertView.findViewById(R.id.drawerItemTextView);

        myImage.setImageDrawable(customItem.getItemIcon());
        myText.setText(customItem.getItemTitle());

        return convertView;

    }
}
