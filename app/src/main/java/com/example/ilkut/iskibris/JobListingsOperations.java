package com.example.ilkut.iskibris;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class JobListingsOperations {

    private Context mContext;   //please assign the activity context
    private String URL;
    private Response.Listener<String> mResponse;
    private Response.ErrorListener mErrorResponse;
    private ArrayList<JobListing> mJobListings;
    private ProgressDialog mPDialog;


    public JobListingsOperations(Context mContext, String URL, ArrayList<JobListing> jobListingsCache) {
        this.mContext = mContext;
        this.URL = URL;
        this.mJobListings = jobListingsCache;

            mPDialog = new ProgressDialog(mContext);
            mPDialog.setMessage(mContext.getString(R.string.loading_word));
            mPDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);

    }

    /**
     * For the default functions to run, call onRequestResponse
     * @param mListener the listener to be used with the request
     */
    public void setResponseListener(Response.Listener<String> mListener) {
        this.mResponse = mListener;
    }

    /**
     * For the default functions to run, call onRequestResponseError
     * @param mErrorListener the error listener to be used with the request
     */
    public void setResponseErrorListener(Response.ErrorListener mErrorListener) {
        mErrorResponse = mErrorListener;
    }

    public void onRequestResponse(String response,  Boolean useProgressDialog, ResponseOperations.ImageResponseListener mListener) {
        ProcessXMLJobListings(response);
        fetchJobListingImages(mListener);
        if(useProgressDialog){
            mPDialog.cancel();
        }
    }

    public void fetchJobListings(final HashMap<String, String> params, Boolean useProgressDialog) {
        if(useProgressDialog){
            mPDialog.show();
        }

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL,
                mResponse
                , mErrorResponse) {
            @Override
            protected Map<String, String> getParams() {
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("Content-Type", "application/x-www-form-urlencoded");
                return params;
            }
        };

        SingletonRequest.getInstance(mContext).getRequestQueue().add(stringRequest);
    }

    /**
     * Processes the XML text and adds the items it has found to the JobListings cache
     * @param text The XML text to be processed
     */
    private void ProcessXMLJobListings(String text) {
        final AlertDialog mDialog = new AlertDialog.Builder(mContext).create();
        mDialog.setTitle(mContext.getString(R.string.error_word));
        mDialog.setButton(DialogInterface.BUTTON_POSITIVE, mContext.getText(R.string.ok_button_word), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mDialog.cancel();
            }
        });

        try {
            mJobListings.clear();
            InputStream textStream = new ByteArrayInputStream(text.getBytes());
            XmlPullParserFactory factory;
            factory = XmlPullParserFactory.newInstance();
            factory.setNamespaceAware(true);
            XmlPullParser xpp = factory.newPullParser();
            xpp.setInput(textStream, null);
            int eventType = xpp.getEventType();
            JobListing tempAddListing;
            String currentTag;
            while (eventType != XmlPullParser.END_DOCUMENT) {

                if (eventType == XmlPullParser.START_TAG && xpp.getName().equals("item")) {
                    currentTag = xpp.getName();
                    tempAddListing = new JobListing(mContext);
                    while (!(eventType == XmlPullParser.END_TAG && xpp.getName().equals("item"))) {         //one single item is being handled
                        if (eventType == XmlPullParser.START_TAG) {
                            String mString = ResponseOperations.isReplacable(xpp, true);
                            if (!(mString == null)) {
                                appendProperty(currentTag, mString, tempAddListing);
                            } else {
                                currentTag = xpp.getName();
                            }
                        } else if (eventType == XmlPullParser.TEXT) {
                            appendProperty(currentTag.trim(), xpp.getText().trim(), tempAddListing);
                        } else if (eventType == XmlPullParser.END_TAG) {
                            String mString = ResponseOperations.isReplacable(xpp, false);
                            if (!(mString == null)) {
                                appendProperty(currentTag, mString, tempAddListing);
                            }
                        }
                        eventType = xpp.next();
                    }                                       //end of item, add it to the jobListings list
                    mJobListings.add(tempAddListing);
                }
                eventType = xpp.next();
            }

            textStream.close();

        } catch (NullPointerException Ne) {
            mDialog.setMessage(mContext.getString(R.string.null_pointer_exception));
            mDialog.show();
        } catch (XmlPullParserException Xe) {
            mDialog.setMessage(mContext.getString(R.string.xml_pull_parser_exception));
            mDialog.show();
        } catch (IOException Ie) {
            mDialog.setMessage(mContext.getString(R.string.io_exception));
            mDialog.show();
        } catch (Exception e) {
            mDialog.setMessage(mContext.getString(R.string.exception));
            mDialog.show();
        }

    }
    private void appendProperty(String tag, String text, JobListing tempJobListing) {
        switch (tag) {
            case "title":
                tempJobListing.appendJobTitle(text);
                break;
            case "description":
                tempJobListing.appendDescription(text);
                break;
            case "pubDate":
                tempJobListing.appendPubDate(text);
                break;
            case "guid":
                tempJobListing.appendPostID(text);
                break;
            case "application":
                tempJobListing.appendApplication(text);
                break;
            case "expdate":
                tempJobListing.appendJobExpires(text);
                break;
            case "comnam":
                tempJobListing.appendCompanyName(text);
                break;
            case "comweb":
                tempJobListing.appendCompanyWebsite(text);
                break;
            case "comtag":
                tempJobListing.appendCompanyTagline(text);
                break;
            case "comvid":
                tempJobListing.appendCompanyVideo(text);
                break;
            case "comtwi":
                tempJobListing.appendCompanyTwitter(text);
                break;
            case "comlog":
                tempJobListing.appendCompanyLogoLink(text);
                break;
            case "comdes":
                tempJobListing.appendCompanyDescription(text);
                break;
            case "comfac":
                tempJobListing.appendCompanyFacebook(text);
                break;
            case "comgoo":
                tempJobListing.appendCompanyGoogle(text);
                break;
            case "comlin":
                tempJobListing.appendCompanyLinkedin(text);
                break;
            case "filled":
                tempJobListing.appendFilled(text);
                break;
            case "jobcat":
                tempJobListing.appendJobCategory(text);
                break;
            case "jobtype":
                tempJobListing.appendJobType(text);
                break;
            default:
                break;
        }
    }

    private void fetchJobListingImages(final ResponseOperations.ImageResponseListener mListener){
        for(final JobListing i: mJobListings){         //Use the mJobListings here.
            if (i.getCompanyLogoLink() != null && !(i.getCompanyLogoLink().trim().equals(""))) {
                Picasso.with(mContext).load(i.getCompanyLogoLink()).into(new Target() {
                    @Override
                    public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom loadedFrom) {
                        i.setCompanyLogo(bitmap);
                        mListener.onImageReceived();
                    }

                    @Override
                    public void onBitmapFailed(Drawable drawable) {
                        //TODO: Error Image
                    }

                    @Override
                    public void onPrepareLoad(Drawable drawable) {
                        //TODO: Loading Image
                    }
                });
            } else {
                //TODO: default image
            }
        }
    }

}
