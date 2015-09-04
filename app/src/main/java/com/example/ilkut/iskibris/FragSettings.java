package com.example.ilkut.iskibris;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;


public class FragSettings extends android.support.v4.app.Fragment {

    SwipeRefreshLayout mSwipeLayout;
    ListView mListView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_settings, container, false);

/*
        final ArrayList<String> mTestArray = new ArrayList<>();
        mTestArray.add("This is a test");
        mTestArray.add("The SwipeRefreshLayout is working well!");

        mListView = (ListView) view.findViewById(R.id.mListView);
        mSwipeLayout = (SwipeRefreshLayout) view.findViewById(R.id.mRefreshLayout);
        mSwipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mListView.setAdapter(new ArrayAdapter<>(getActivity(),
                        android.R.layout.simple_list_item_1, mTestArray));
                mSwipeLayout.setRefreshing(false);
            }
        });

        mSwipeLayout.setColorSchemeColors(R.color.bright_foreground_inverse_material_dark,
                R.color.highlighted_text_material_dark, R.color.bright_foreground_material_dark,
                R.color.link_text_material_light);
*/


/*        Button mButton = (Button) view.findViewById(R.id.button);
        final TextView mText = (TextView) view.findViewById(R.id.textView);*/
     /*   final String text = "<del datetime=\"2015-08-31T09:41:47+00:00\">This is a</del> really<strong> nice blog</strong> post <blockquote>abo<span style=\"text-decoration: underline;\">ut many things,</span> really! And, by many things, I mean,</blockquote> re<em>ally, a lo</em>t!";*/


        /*ImageView im1 = (ImageView) view.findViewById(R.id.im1);
        ImageView im2 = (ImageView) view.findViewById(R.id.im2);
        ImageView im3 = (ImageView) view.findViewById(R.id.im3);
        ImageView im4 = (ImageView) view.findViewById(R.id.im4);

        Picasso.with(getActivity()).load("https://upload.wikimedia.org/wikipedia/commons/thumb/b/b8/Willow.jpg/800px-Willow.jpg").resize(300, 300).into(im1);
        Picasso.with(getActivity()).load("https://upload.wikimedia.org/wikipedia/commons/f/fd/Populus-trichocarpa.JPG").resize(300,300).into(im2);
        Picasso.with(getActivity()).load("https://upload.wikimedia.org/wikipedia/commons/a/af/Quercus_robur.jpg").resize(300,300).into(im3);
        Picasso.with(getActivity()).load("https://upload.wikimedia.org/wikipedia/commons/6/68/Sorbus-torminalis.JPG").resize(300,300).into(im4);*/

        RecyclerView rv = (RecyclerView) view.findViewById(R.id.main_RV);
        rv.setHasFixedSize(true);

        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        llm.setReverseLayout(true);
        rv.setLayoutManager(llm);

        JobListingsAdapterRV RVADAPTER = new JobListingsAdapterRV(SingletonCache.getInstance().getJobListingsCache());
        rv.setAdapter(RVADAPTER);
        return view;
    }




}







