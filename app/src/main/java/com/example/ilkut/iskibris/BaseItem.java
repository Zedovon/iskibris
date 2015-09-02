package com.example.ilkut.iskibris;

import android.content.Context;
import android.graphics.Bitmap;

public class BaseItem {

    Context mContext;
    String ImageLink;
    Bitmap Image;
    String postID;

    public BaseItem(Context mContext) {
        this.mContext = mContext;
    }

    public Bitmap getImage() {
        return Image;
    }

    public void setImage(Bitmap image) {
        Image = image;
    }

    public String getImageLink() {
        return ImageLink;
    }

    public void appendImageLink(String imageLink) {
        ImageLink += imageLink;
    }

    public String getPostID() {
        return postID;
    }

    public void appendPostID(String postID) {
        this.postID += postID;
    }
}
