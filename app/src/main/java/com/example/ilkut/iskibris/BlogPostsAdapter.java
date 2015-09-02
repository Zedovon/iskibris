package com.example.ilkut.iskibris;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class BlogPostsAdapter extends ArrayAdapter<BlogPost>{

    public BlogPostsAdapter(Context context, ArrayList<BlogPost> blogPosts) {
        super(context, 0, blogPosts);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        BlogPost blogPost = getItem(position);
        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.row_blog, parent, false);
        }


        TextView mTitle = (TextView) convertView.findViewById(R.id.blogTitle);
        TextView mContent = (TextView) convertView.findViewById(R.id.blogContent);
        TextView mPubDate = (TextView) convertView.findViewById(R.id.blogPubDate);

        mTitle.setText(blogPost.getPostTitle());

        String content = blogPost.getPostContent();
        if(ResponseOperations.EscapeTags(content).length() >= 100){
            mContent.setText(ResponseOperations.EscapeTags(content.substring(0, 100)) + "...");
        }else{

            mContent.setText(ResponseOperations.EscapeTags(content.substring(0, ResponseOperations.EscapeTags(content).length())));
        }

        mPubDate.setText(blogPost.getPostPubDate());

        return convertView;
    }
}
