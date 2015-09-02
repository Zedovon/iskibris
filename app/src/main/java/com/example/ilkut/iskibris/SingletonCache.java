package com.example.ilkut.iskibris;

import java.util.ArrayList;

public class SingletonCache {

    private static SingletonCache mInstance;
    private ArrayList<JobListing> jobListingsCache;
    private ArrayList<JobListing> searchJobListingsCache;
    private ArrayList<UserResume> userResumesCache;
    private ArrayList<UserResume> searchUserResumesCache;
    private ArrayList<BlogPost> blogPostsCache;


    private SingletonCache() {
        jobListingsCache = getJobListingsCache();
        userResumesCache = getUserResumesCache();
        blogPostsCache = getBlogPostsCache();
    }

    public static synchronized SingletonCache getInstance(){
        if(mInstance == null){
            mInstance = new SingletonCache();
        }
        return mInstance;
    }


    public ArrayList<JobListing> getJobListingsCache(){
        if(jobListingsCache == null){
            jobListingsCache = new ArrayList<>();
        }
        return jobListingsCache;
    }

    public ArrayList<UserResume> getUserResumesCache(){
        if(userResumesCache == null){
            userResumesCache = new ArrayList<>();
        }
        return userResumesCache;
    }

    public ArrayList<BlogPost> getBlogPostsCache(){
        if(blogPostsCache == null){
            blogPostsCache = new ArrayList<>();
        }
        return blogPostsCache;
    }

    public ArrayList<JobListing> getSearchJobListingsCache(){
        if(searchJobListingsCache == null){
            searchJobListingsCache = new ArrayList<>();
        }
        return searchJobListingsCache;
    }

    public ArrayList<UserResume> getSearchUserResumesCache(){
        if(searchUserResumesCache == null){
            searchUserResumesCache = new ArrayList<>();
        }
        return searchUserResumesCache;
    }

    public void addToSearchJobListingsCache(JobListing jobListingValue){
        getSearchJobListingsCache().add(jobListingValue);
    }

    public void addToJobListingsCache(JobListing jobListingValue){
        getJobListingsCache().add(jobListingValue);
    }

    public void addToUserResumesCache(UserResume userResumeValue){
        getUserResumesCache().add(userResumeValue);
    }

    public void addToBlogPostsCache(BlogPost blogPostValue){
        getBlogPostsCache().add(blogPostValue);
    }

    public void clearJobListingsCache(){
        getJobListingsCache().clear();
    }
    public void clearUserResumesCache(){
        getUserResumesCache().clear();
    }
    public void clearBlogPostsCache(){
        getBlogPostsCache().clear();
    }

    public JobListing getJobListingByPosition(int position){
        return getJobListingsCache().get(position);
    }

    public UserResume getUserResumeByPosition(int position){
        return getUserResumesCache().get(position);
    }

    public BlogPost getBlogPostByPosition(int position){
        return getBlogPostsCache().get(position);
    }


}
