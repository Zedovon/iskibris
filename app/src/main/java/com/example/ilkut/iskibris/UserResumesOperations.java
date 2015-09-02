package com.example.ilkut.iskibris;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.ImageView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.StringRequest;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class UserResumesOperations {

    private Context mContext;   //Please assign the activity context
    private String URL;
    private Response.Listener<String> mResponse;
    private Response.ErrorListener mErrorResponse;
    private ArrayList<UserResume> mUserResumesCache;
    private ProgressDialog mPDialog;

    public UserResumesOperations(Context mContext, String URL, ArrayList<UserResume> userResumesCache) {
        this.mContext = mContext;
        this.URL = URL;
        this.mUserResumesCache = userResumesCache;

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

    public void onRequestResponse(String response, ResponseOperations.ImageResponseListener mListener, Boolean useProgressDialog) {
        ProcessXMLUserResumes(response);
        fetchUserResumeImages(mListener);
        if(useProgressDialog){
            mPDialog.cancel();
        }
    }

    public void fetchUserResumes(final HashMap<String, String> params, Boolean useProgressDialog) {
        if(useProgressDialog){
            mPDialog.show();
        }

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL,
                mResponse,
                mErrorResponse) {
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

        SingletonRequest.getInstance(mContext).addToRequestQueue(stringRequest);
    }

    /**
     * Processes the XML text and adds the items it has found to the UserResumes cache
     *
     * @param text The XML text to be processed
     */
    public void ProcessXMLUserResumes(String text) {
        final AlertDialog mDialog = new AlertDialog.Builder(mContext).create();
        mDialog.setTitle(mContext.getString(R.string.error_word));
        mDialog.setButton(DialogInterface.BUTTON_POSITIVE, mContext.getText(R.string.ok_button_word), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mDialog.cancel();
            }
        });

        try {
            mUserResumesCache.clear();
            InputStream textStream = new ByteArrayInputStream(text.getBytes());
            XmlPullParserFactory factory;

            factory = XmlPullParserFactory.newInstance();
            factory.setNamespaceAware(true);
            XmlPullParser xpp = factory.newPullParser();

            xpp.setInput(textStream, null);
            int eventType = xpp.getEventType();
            UserResume tempAddResume;

            String currentTag;
            while (eventType != XmlPullParser.END_DOCUMENT) {

                if (eventType == XmlPullParser.START_TAG && xpp.getName().equals("item")) {
                    currentTag = xpp.getName();
                    tempAddResume = new UserResume();
                    while (!(eventType == XmlPullParser.END_TAG && xpp.getName().equals("item"))) {         //one single item is being handled
                        if (eventType == XmlPullParser.START_TAG) {
                            String mString = ResponseOperations.isReplacable(xpp, true);
                            if (!(mString == null)) {
                                appendProperty(currentTag, mString, tempAddResume);
                            } else {
                                currentTag = xpp.getName();
                            }
                        } else if (eventType == XmlPullParser.TEXT) {
                            appendProperty(currentTag, xpp.getText().trim(), tempAddResume);
                        } else if (eventType == XmlPullParser.END_TAG) {
                            String mString = ResponseOperations.isReplacable(xpp, false);
                            if (!(mString == null)) {
                                appendProperty(currentTag, mString, tempAddResume);
                            }
                        }
                        eventType = xpp.next();
                    }
                    mUserResumesCache.add(tempAddResume);            //end of item, add it to the userResumes list

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

    public void appendProperty(String tag, String text, UserResume tempAddResume) {
        switch (tag.trim()) {
            case "title":
                tempAddResume.appendTitle(text);
                break;
            case "description":
                tempAddResume.appendDescription(text);
                break;
            //TODO: Fix pubDate
            case "pubDate":
                tempAddResume.appendPubDate(text);
                break;
            case "candittitle":
                tempAddResume.appendCandidateTitle(text);
                break;
            case "canditemail":
                tempAddResume.appendCandidateEmail(text);
                break;
            case "canditlocat":
                tempAddResume.appendLocation(text);
                break;
            case "canditphoto":
                tempAddResume.appendCandidatePhotoLink(text);
                break;
            case "guid":
                tempAddResume.appendPostID(text);
                break;
            case "canditeducation":
                tempAddResume.appendCandidateEducations(parseCandidateEducation(text));
                break;
            case "canditexperience":
                tempAddResume.appendCandidateExperiences(parseCandidateExperience(text));
                break;
            case "canditlink":
                tempAddResume.appendCandidateLinks(parseCandidateLink(text));
                break;
            default:
                break;
        } //End Switch
    }

    //region parsers

    private ArrayList<UserResumeEducation> parseCandidateEducation(String text) {
        ArrayList<UserResumeEducation> tempReturn = new ArrayList<>();
        String[] terms = text.split(",");

        //Order: location, qualification, date, notes
        for (int i = 0; i < terms.length; i++) {
            if (terms[i].equals("[item]")) {
                UserResumeEducation temp = new UserResumeEducation();
                temp.setLocation(terms[i + 1]);
                temp.setQualification(terms[i + 2]);
                temp.setDate(terms[i + 3]);
                temp.setNotes(terms[i + 4]);
                tempReturn.add(temp);
            }

        }
        return tempReturn;
    }

    private ArrayList<UserResumeExperience> parseCandidateExperience(String text) {
        ArrayList<UserResumeExperience> tempReturn = new ArrayList<>();
        String[] terms = text.split(",");

        //Order: employer, job_title, date, notes
        for (int i = 0; i < terms.length; i++) {
            if (terms[i].equals("[item]")) {
                UserResumeExperience temp = new UserResumeExperience();
                temp.setEmployer(terms[i + 1]);
                temp.setJob_title(terms[i + 2]);
                temp.setDate(terms[i + 3]);
                temp.setNotes(terms[i + 4]);
                tempReturn.add(temp);
            }

        }
        return tempReturn;
    }

    private ArrayList<UserResumeLink> parseCandidateLink(String text) {
        ArrayList<UserResumeLink> tempReturn = new ArrayList<>();
        String[] terms = text.split(",");

        //Order: name, url
        for (int i = 0; i < terms.length; i++) {
            if (terms[i].equals("[item]")) {
                UserResumeLink temp = new UserResumeLink();
                temp.setName(terms[i + 1]);
                temp.setUrl(terms[i + 2]);
                tempReturn.add(temp);
            }

        }
        return tempReturn;
    }

    //endregion

    private void fetchUserResumeImages(final ResponseOperations.ImageResponseListener mListener){
        for(final UserResume i: mUserResumesCache){
            if (i.getCandidatePhotoLink() != null && !(i.getCandidatePhotoLink().trim().equals(""))) {
                ImageRequest request = new ImageRequest(i.getCandidatePhotoLink(),
                        new Response.Listener<Bitmap>() {
                            @Override
                            public void onResponse(Bitmap bitmap) {
                                i.setCandidatePhoto(bitmap);
                                mListener.onImageReceived();
                            }
                        }, 0, 0, ImageView.ScaleType.CENTER, null,
                        new Response.ErrorListener() {
                            public void onErrorResponse(VolleyError error) {
                                i.setCandidatePhoto(BitmapFactory.decodeResource(mContext.getResources(), R.drawable.ic_drawer, null));
                            }
                        });
                SingletonRequest.getInstance(mContext).addToRequestQueue(request);
            } else {
                //TODO: default image
            }
        }
    }


}
