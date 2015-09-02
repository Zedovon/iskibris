package com.example.ilkut.iskibris;

import android.app.AlertDialog;
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

public abstract class OperationsBase {

    private Context mContext;   //please assign the application context
    private String URL;
    private Response.Listener<String> mResponse;
    private Response.ErrorListener mErrorResponse;
    private ArrayList<BaseItem> mItems;

    public OperationsBase(Context mContext, String URL, ArrayList<BaseItem> mItemsValue) {
        this.mContext = mContext;
        this.URL = URL;
        this.mItems = mItemsValue;
    }

    public void setResponseListener(Response.Listener<String> mListener) {
        this.mResponse = mListener;
    }
    public void setResponseErrorListener(Response.ErrorListener mErrorListener) {
        mErrorResponse = mErrorListener;
    }

    public void onRequestResponse(String response, boolean fetchImages , ResponseOperations.ImageResponseListener mListener) {
        processItems(response);
        if(fetchImages){
            fetchItemImages(mListener);
        }
    }

    public void fetchItems(final HashMap<String, String> params) {
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

    private void processItems(String text) {
        final AlertDialog mDialog = new AlertDialog.Builder(mContext).create();
        mDialog.setTitle(mContext.getString(R.string.error_word));
        mDialog.setButton(DialogInterface.BUTTON_POSITIVE, mContext.getText(R.string.ok_button_word), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mDialog.cancel();
            }
        });

        try {
            mItems.clear();
            InputStream textStream = new ByteArrayInputStream(text.getBytes());
            XmlPullParserFactory factory;
            factory = XmlPullParserFactory.newInstance();
            factory.setNamespaceAware(true);
            XmlPullParser xpp = factory.newPullParser();
            xpp.setInput(textStream, null);
            int eventType = xpp.getEventType();
            BaseItem tempItem;
            String currentTag;
            while (eventType != XmlPullParser.END_DOCUMENT) {

                if (eventType == XmlPullParser.START_TAG && xpp.getName().equals("item")) {
                    currentTag = xpp.getName();
                    tempItem = new BaseItem(mContext);
                    while (!(eventType == XmlPullParser.END_TAG && xpp.getName().equals("item"))) {         //one single item is being handled
                        if (eventType == XmlPullParser.START_TAG) {
                            String mString = ResponseOperations.isReplacable(xpp, true);
                            if (!(mString == null)) {
                                appendProperty(currentTag, mString, tempItem);
                            } else {
                                currentTag = xpp.getName();
                            }
                        } else if (eventType == XmlPullParser.TEXT) {
                            appendProperty(currentTag.trim(), xpp.getText().trim(), tempItem);
                        } else if (eventType == XmlPullParser.END_TAG) {
                            String mString = ResponseOperations.isReplacable(xpp, false);
                            if (!(mString == null)) {
                                appendProperty(currentTag, mString, tempItem);
                            }
                        }
                        eventType = xpp.next();
                    }                                       //end of item, add it to the jobListings list
                    mItems.add(tempItem);
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

    abstract protected void appendProperty(String tag, String text, BaseItem tempBaseItem);

    abstract protected void isIt();

    private void fetchItemImages(final ResponseOperations.ImageResponseListener mListener){
        for(final BaseItem i: mItems){         //Use the mJobListings here.
            if (i.getImageLink() != null || !(i.getImageLink().trim().equals(""))) {
                ImageRequest request = new ImageRequest(i.getImageLink(),
                        new Response.Listener<Bitmap>() {
                            @Override
                            public void onResponse(Bitmap bitmap) {
                                i.setImage(bitmap);
                                mListener.onImageReceived();
                            }
                        }, 0, 0, ImageView.ScaleType.CENTER, null,
                        new Response.ErrorListener() {
                            public void onErrorResponse(VolleyError error) {
                                i.setImage(BitmapFactory.decodeResource(mContext.getResources(), R.drawable.ic_drawer, null));
                            }
                        });
                SingletonRequest.getInstance(mContext).addToRequestQueue(request);
            } else {
                //TODO: default image
            }
        }
    }
}
