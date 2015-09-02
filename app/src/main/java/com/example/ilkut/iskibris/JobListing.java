package com.example.ilkut.iskibris;

import android.content.Context;
import android.graphics.Bitmap;

public class JobListing extends BaseItem {
    private String jobTitle;
    private String description;
    private String pubDate;
    private String postID;

    private String companyName;
    private String companyWebsite;
    private String companyTagline;
    private String companyVideo;
    private String companyTwitter;
    private String companyLogoLink;
    private Bitmap companyLogo;
    private String companyDescription;
    private String companyFacebook;
    private String companyGoogle;
    private String companyLinkedin;

    private String application;
    private String jobExpires;
    private String jobType;
    private String jobCategory;
    private String filled;


    public JobListing(Context context) {
        super(context);
        this.description = "";
        this.postID = "";
        this.jobTitle = "";
        this.pubDate = "";
        this.companyName = "";
        this.companyWebsite = "";
        this.companyTagline = "";
        this.companyVideo = "";
        this.companyTwitter = "";

        super.ImageLink = "";

        this.companyDescription = "";
        this.companyFacebook = "";
        this.companyGoogle = "";
        this.companyLinkedin = "";

        this.application = "";
        this.jobExpires = "";
        this.jobType = "";
        this.jobCategory = "";
        this.filled = "";
    }

    public String getFilled() {
        return filled;
    }

    public void appendFilled(String filled) {
        this.filled += filled;
    }

    public String getCompanyWebsite() {
        return companyWebsite;
    }

    public void appendCompanyWebsite(String comapanyWebsite) {
        this.companyWebsite += comapanyWebsite;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void appendCompanyName(String companyName) {
        this.companyName += companyName;
    }

    public String getApplication() {
        return application;
    }

    public void appendApplication(String companyApplication) {
        this.application += companyApplication;
    }

    public String getCompanyDescription() {
        return companyDescription;
    }

    public void appendCompanyDescription(String companyDescription) {
        this.companyDescription += companyDescription;
    }

    public String getCompanyFacebook() {
        return companyFacebook;
    }

    public void appendCompanyFacebook(String companyFacebook) {
        this.companyFacebook += companyFacebook;
    }

    public String getCompanyGoogle() {
        return companyGoogle;
    }

    public void appendCompanyGoogle(String companyGoogle) {
        this.companyGoogle += companyGoogle;
    }

    public String getCompanyLinkedin() {
        return companyLinkedin;
    }

    public void appendCompanyLinkedin(String companyLinkedin) {
        this.companyLinkedin += companyLinkedin;
    }

    /**
     * Set the company logo manually, does not download the image from the companyLogoLink
     * Use setCompanyLogoFromURL with  the companyLogoLink as the parameter to download and set the
     * company logo from the companyLogoLink
     *
     * @param setCompanyLogo Company logo to be set as Bitmap
     */
    public void setCompanyLogo(Bitmap setCompanyLogo) {
        companyLogo = setCompanyLogo;
    }

    public Bitmap getCompanyLogo() {
        return companyLogo;
    }

    public String getCompanyTagline() {
        return companyTagline;
    }

    public void appendCompanyTagline(String companyTagline) {
        this.companyTagline += companyTagline;
    }

    public String getCompanyTwitter() {
        return companyTwitter;
    }

    public void appendCompanyTwitter(String companyTwitter) {
        this.companyTwitter += companyTwitter;
    }

    public String getCompanyVideo() {
        return companyVideo;
    }

    public void appendCompanyVideo(String companyVideo) {
        this.companyVideo += companyVideo;
    }

    public String getJobCategory() {
        return jobCategory;
    }

    public void appendJobCategory(String jobCategory) {
        this.jobCategory += jobCategory;
    }

    public String getJobExpires() {
        return jobExpires;
    }

    public void appendJobExpires(String jobExpires) {
        this.jobExpires += jobExpires;
    }

    public String getJobType() {
        return jobType;
    }

    public void appendJobType(String jobType) {
        this.jobType += jobType;
    }

    public String getDescription() {
        return description;
    }

    public void appendDescription(String description) {
        this.description += description;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public void appendJobTitle(String jobTitle) {
        this.jobTitle += jobTitle;
    }

    public String getPubDate() {
        return pubDate;
    }

    public void appendPubDate(String pubDate) {
        this.pubDate += pubDate;
    }
}
