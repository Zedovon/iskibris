package com.example.ilkut.iskibris;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.StringReader;
import java.util.HashMap;
import java.util.Map;

public class BlogPostsOperations {

    private Context mContext;
    private Response.Listener<String> mResponse;
    private Response.ErrorListener mErrorResponse;

    public BlogPostsOperations(Context mContext) {
        this.mContext = mContext;
    }

    /**
     * For the default functions to run, call onRequestResponse
     *
     * @param mListener the listener to be used with the request
     */
    public void setResponseListener(Response.Listener<String> mListener) {
        this.mResponse = mListener;
    }

    /**
     * For the default functions to run, call onRequestResponseError
     *
     * @param mErrorListener the error listener to be used with the request
     */
    public void setResponseErrorListener(Response.ErrorListener mErrorListener) {
        mErrorResponse = mErrorListener;
    }

    public void onRequestResponse(String response) {
        ProcessXMLBlogPosts(mContext, response);
    }


    public void fetchBlogPosts() {
        String url = mContext.getResources().getString(R.string.URL_BLOG_POSTS);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                mResponse, mErrorResponse) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("Content-Type", "application/x-www-form-urlencoded");
                return params;
            }
        };

        SingletonRequest.getInstance(mContext).getRequestQueue().add(stringRequest);

    }

    public static void ProcessXMLBlogPosts(Context mContext, String XMLText) {
        final AlertDialog mDialog = new AlertDialog.Builder(mContext).create();
        mDialog.setTitle(mContext.getString(R.string.error_word));
        mDialog.setButton(DialogInterface.BUTTON_POSITIVE, mContext.getText(R.string.ok_button_word), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mDialog.cancel();
            }
        });

        try {
            SingletonCache.getInstance().clearBlogPostsCache();
            XmlPullParserFactory factory;

            factory = XmlPullParserFactory.newInstance();
            factory.setNamespaceAware(true);
            XmlPullParser xpp = factory.newPullParser();

            xpp.setInput(new StringReader(XMLText));
            int eventType = xpp.getEventType();
            BlogPost tempAddBlogPost;

            String currentTag;
            while (eventType != XmlPullParser.END_DOCUMENT) {

                if (eventType == XmlPullParser.START_TAG && xpp.getName().equals("item")) {
                    currentTag = xpp.getName();
                    tempAddBlogPost = new BlogPost();
                    while (!(eventType == XmlPullParser.END_TAG && xpp.getName().equals("item"))) {         //one single item is being handled


                        if (eventType == XmlPullParser.START_TAG) {
                            if (isIt(xpp.getName())) {
                                currentTag = xpp.getName();
                            } else {
                                String mString = ResponseOperations.isReplacable(xpp, true);
                                if (!(mString == null)) {
                                    appendProperty(currentTag, mString, tempAddBlogPost);
                                } //Else -> the tag is ignored, we do not recognise it!
                            }
                        } else if (eventType == XmlPullParser.TEXT) {
                            appendProperty(currentTag.trim(), xpp.getText().trim(), tempAddBlogPost);
                        } else if (eventType == XmlPullParser.END_TAG) {
                            String mString = ResponseOperations.isReplacable(xpp, false);
                            if (!(mString == null)) {
                                appendProperty(currentTag, mString, tempAddBlogPost);
                            }
                        }


                        eventType = xpp.next();
                    }
                    SingletonCache.getInstance().addToBlogPostsCache(tempAddBlogPost);           //end of item, add it to the jobListings list
                }

                eventType = xpp.next();
            }

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

    private static void appendProperty(String tag, String text, BlogPost tempAddBlogPost) {
        switch (tag) {
            case "title":
                tempAddBlogPost.appendPostTitle(text);
                break;
            case "content":
                tempAddBlogPost.appendPostContent(text);
                break;
            case "pubDate":
                tempAddBlogPost.appendPostPubDate(text);
                break;
            case "guid":
                tempAddBlogPost.appendPostID(text);
                break;
            default:
                break;
        }
    }

    private static boolean isIt(String tag) {
        switch (tag) {
            case "title":
                return true;
            case "content":
                return true;
            case "pubDate":
                return true;
            case "guid":
                return true;
            default:
                return false;
        }
    }


}
