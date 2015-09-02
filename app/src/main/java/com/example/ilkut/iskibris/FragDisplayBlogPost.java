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
import android.widget.TextView;

public class FragDisplayBlogPost extends android.support.v4.app.Fragment {

    TextView titleText, datePublishedText, contentText;
    BlogPost displayBlogPost;
    Context mContext;
    AlertDialog mDialog;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_display_blog_post, container, false);

        mContext = getActivity();
        mDialog = new AlertDialog.Builder(mContext).create();

        titleText = (TextView) view.findViewById(R.id.dBlogTitleText);
        datePublishedText = (TextView) view.findViewById(R.id.dBlogDatePublishedText);
        contentText = (TextView) view.findViewById(R.id.dBlogContentText);

        String postID = "0";
        if (getArguments() != null) {
            postID = getArguments().getString("postID", "0");
            if (!(postID.isEmpty()) && !(postID.equals("0"))) {
                for (BlogPost i : SingletonCache.getInstance().getBlogPostsCache()) {
                    if (i.getPostID().equals(postID)) {
                        displayBlogPost = i;
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


        titleText.setText(displayBlogPost.getPostTitle());
        datePublishedText.setText(displayBlogPost.getPostPubDate());
        contentText.setText(Html.fromHtml(displayBlogPost.getPostContent()));      //TODO: Come back, from HTML?


        return view;
    }
}
