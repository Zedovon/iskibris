package com.example.ilkut.iskibris;

import android.graphics.Bitmap;

import java.util.ArrayList;

public class UserResume{

    private String description;
    private String pubDate;
    private String title;
    private String location;
    private String candidateName;
    private String candidateEmail;
    private String candidateTitle;
    private Bitmap candidatePhoto;
    private String candidatePhotoLink;
    private String postID;

    private ArrayList<UserResumeEducation> candidateEducations;
    private ArrayList<UserResumeExperience> candidateExperiences;
    private ArrayList<UserResumeLink> candidateLinks;


    public UserResume() {
        this.description = "";
        this.pubDate = "";
        this.title = "";
        this.postID = "";

        this.location = "";
        this.candidateName = "";
        this.candidateEmail = "";
        this.candidateTitle = "";
        this.candidatePhotoLink = "";

        candidateEducations = new ArrayList<>();
        candidateExperiences = new ArrayList<>();
        candidateLinks = new ArrayList<>();

    }

    public String getPostID() {
        return postID;
    }

    public void appendPostID(String postID) {
        this.postID += postID;
    }

    public Bitmap getCandidatePhoto() {
        return candidatePhoto;
    }

    public void setCandidatePhoto(Bitmap canditPhoto) {
        candidatePhoto = canditPhoto;
    }

    public String getCandidatePhotoLink() {
        return candidatePhotoLink;
    }

    public void appendCandidatePhotoLink(String canditPhotoLink) {
        candidatePhotoLink += canditPhotoLink;
    }

    public String getLocation() {
        return location;
    }

    public void appendLocation(String location) {
        this.location += location;
    }

    public String getCandidateName() {
        return candidateName;
    }

    public void appendCandidateName(String candidateName) {
        this.candidateName += candidateName;
    }

    public String getCandidateEmail() {
        return candidateEmail;
    }

    public void appendCandidateEmail(String candidateEmail) {
        this.candidateEmail += candidateEmail;
    }

    public String getCandidateTitle() {
        return candidateTitle;
    }

    public void appendCandidateTitle(String candidateTitle) {
        this.candidateTitle += candidateTitle;
    }

    public String getDescription() {
        return description;
    }

    public void appendDescription(String description) {
        this.description += description;
    }

    public String getPubDate() {
        return pubDate;
    }

    public void appendPubDate(String pubDate) {
        this.pubDate += pubDate;
    }

    public String getTitle() {
        return title;
    }

    public void appendTitle(String title) {
        this.title += title;
    }

    public ArrayList<UserResumeEducation> getCandidateEducations() {
        return candidateEducations;
    }

    public void appendCandidateEducations(ArrayList<UserResumeEducation> candidateEducations) {
        if(candidateEducations != null && !(candidateEducations.isEmpty())){
            this.candidateEducations.addAll(candidateEducations);
        }
    }

    public ArrayList<UserResumeExperience> getCandidateExperiences() {
        return candidateExperiences;
    }

    public void appendCandidateExperiences(ArrayList<UserResumeExperience> candidateExperiences) {
        if(candidateExperiences != null && !(candidateExperiences.isEmpty())){
            this.candidateExperiences.addAll(candidateExperiences);
        }
    }

    public ArrayList<UserResumeLink> getCandidateLinks() {
        return candidateLinks;
    }

    public void appendCandidateLinks(ArrayList<UserResumeLink> candidateLinks) {
        if(candidateLinks != null && !(candidateLinks.isEmpty())){
            this.candidateLinks.addAll(candidateLinks);
        }
    }
}
