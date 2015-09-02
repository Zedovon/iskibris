package com.example.ilkut.iskibris;

public class BlogPost {
    String postTitle;
    String postContent;
    String postPubDate;
    String postID;

    public BlogPost() {
        this.postTitle = "";
        this.postContent = "";
        this.postPubDate = "";
        this.postID = "";
    }

    public void setPostContent(String postContent) {
        this.postContent = postContent;
    }

    public void setPostID(String postID) {
        this.postID = postID;
    }

    public void setPostPubDate(String postPubDate) {
        this.postPubDate = postPubDate;
    }

    public void setPostTitle(String postTitle) {
        this.postTitle = postTitle;
    }

    public String getPostContent() {
        return postContent;
    }

    public void appendPostContent(String postContent) {
        this.postContent+= postContent;
    }

    public String getPostPubDate() {
        return postPubDate;
    }

    public void appendPostPubDate(String postPubDate) {
        this.postPubDate+= postPubDate;
    }

    public String getPostTitle() {
        return postTitle;
    }

    public void appendPostTitle(String postTitle) {
        this.postTitle+= postTitle;
    }

    public String getPostID() {
        return postID;
    }

    public void appendPostID(String postID) {
        this.postID += postID;
    }

}
