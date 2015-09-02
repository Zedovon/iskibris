package com.example.ilkut.iskibris;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;

import java.util.ArrayList;

public class FragBlog extends android.support.v4.app.Fragment {

    TextView blogTitle;
    TextView blogContent;
    TextView blogPubDate;
    ListView blogPostsListView;
    AlertDialog mDialog;
    Context mContext;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        mContext = getActivity();

        View view = inflater.inflate(R.layout.frag_blog, container, false);
        blogTitle = (TextView) view.findViewById(R.id.blogTitle);
        blogContent = (TextView) view.findViewById(R.id.blogContent);
        blogPubDate = (TextView) view.findViewById(R.id.blogPubDate);
        blogPostsListView = (ListView) view.findViewById(R.id.blogListView);

        mDialog = new AlertDialog.Builder(mContext.getApplicationContext()).create();
        mDialog.setTitle(getString(R.string.warning_word));

        final BlogPostsOperations mOperations = new BlogPostsOperations(getActivity());
        mOperations.setResponseListener(new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                mOperations.onRequestResponse(response);
                populateListView(SingletonCache.getInstance().getBlogPostsCache());
            }
        });
        mOperations.setResponseErrorListener(new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                ResponseOperations.onRequestErrorRespone(mContext, error, new ResponseOperations.TryAgainAction() {
                    @Override
                    public void onTryAgain() {
                        mOperations.fetchBlogPosts();
                    }
                });
            }
        });

//        if (SingletonCache.getInstance().getBlogPostsCache().isEmpty()) {
            mOperations.fetchBlogPosts();   //Fetch the blog posts, and put them into the SingletonCache
//        }
        /*else {
            Toast.makeText(mContext.getApplicationContext(), "Blog Posts Loaded", Toast.LENGTH_LONG).show();
            populateListView(SingletonCache.getInstance().getBlogPostsCache());
        }*/

        return view;
    }


    public void populateListView(ArrayList<BlogPost> blogPosts) {

        BlogPostsAdapter mAdapter = new BlogPostsAdapter(getActivity().getApplicationContext(), blogPosts);
        blogPostsListView.setAdapter(mAdapter);
        blogPostsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                onListViewItemClicked(position);
            }
        });

    }

    public void onListViewItemClicked(int blogPostPosition) {
        String itemID = SingletonCache.getInstance().getBlogPostByPosition(blogPostPosition).getPostID();

        if (itemID != null) {
            for (BlogPost i : SingletonCache.getInstance().getBlogPostsCache()) {
                if (i.getPostID().equals(itemID)) {

                    FragDisplayBlogPost mDisplay = new FragDisplayBlogPost();
                    Bundle mBundle = new Bundle();
                    mBundle.putString("postID", itemID);
                    mDisplay.setArguments(mBundle);
                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, mDisplay).addToBackStack(null).commit();
                    break;
                }
            }
        } else {
            mDialog.setMessage(mContext.getString(R.string.unexpected_error));
            mDialog.setButton(DialogInterface.BUTTON_NEUTRAL, getResources().getText(R.string.ok_button_word), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    mDialog.cancel();
                }
            });
        }
    }


}
