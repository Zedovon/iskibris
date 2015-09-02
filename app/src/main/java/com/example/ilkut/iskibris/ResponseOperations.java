package com.example.ilkut.iskibris;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

import com.android.volley.NetworkError;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;

import org.xmlpull.v1.XmlPullParser;

import java.io.UnsupportedEncodingException;

public class ResponseOperations {

    public interface TryAgainAction {
        void onTryAgain();
    }
    public interface ImageResponseListener{
        void onImageReceived();
    }

    public static String SanitizeText(String text) {
        text = text.replace("&nbsp;", " ");
        text = text.replace("&#8217;", "'");
        text = text.replace("&amp;", "&");
        text = text.replace("&lt;", "<");
        text = text.replace("&gt;", ">");
        text = text.replace("&#39;", "'");
        text = text.replace("&quot;", "\"");
        text = text.replace("&#64;", "@");
        return text;
    }

    public static String EscapeTags(String text){
        text = text.replaceAll("<(.*?)>", "");
        text = text.replaceAll("</(.*?)>", "");
        return text;
    }
    public static String isReplacable(XmlPullParser xpp, boolean isStartTag) {

            switch (xpp.getName()) {
                case "br":
                    return isStartTag ? addFormattingTags(xpp): addEndTags(xpp.getName());
                case "p":
                    return isStartTag ? addFormattingTags(xpp): addEndTags(xpp.getName());
                case "b":
                    return isStartTag ? addFormattingTags(xpp): addEndTags(xpp.getName());
                case "i":
                    return isStartTag ? addFormattingTags(xpp): addEndTags(xpp.getName());
                case "u":
                    return isStartTag ? addFormattingTags(xpp): addEndTags(xpp.getName());
                case "a":
                    return isStartTag ? addFormattingTags(xpp): addEndTags(xpp.getName());
                case "strong":
                    return isStartTag ? addFormattingTags(xpp): addEndTags(xpp.getName());
                case "big":
                    return isStartTag ? addFormattingTags(xpp): addEndTags(xpp.getName());
                case "small":
                    return isStartTag ? addFormattingTags(xpp): addEndTags(xpp.getName());
                case "strike":
                    return isStartTag ? addFormattingTags(xpp): addEndTags(xpp.getName());
                case "h1":
                    return isStartTag ? addFormattingTags(xpp): addEndTags(xpp.getName());
                case "h2":
                    return isStartTag ? addFormattingTags(xpp): addEndTags(xpp.getName());
                case "h3":
                    return isStartTag ? addFormattingTags(xpp): addEndTags(xpp.getName());
                case "h4":
                    return isStartTag ? addFormattingTags(xpp): addEndTags(xpp.getName());
                case "h5":
                    return isStartTag ? addFormattingTags(xpp): addEndTags(xpp.getName());
                case "h6":
                    return isStartTag ? addFormattingTags(xpp): addEndTags(xpp.getName());
                case "div":
                    return isStartTag ? addFormattingTags(xpp): addEndTags(xpp.getName());
                case "blockquote":
                    return isStartTag ? addFormattingTags(xpp): addEndTags(xpp.getName());
                case "cite":
                    return isStartTag ? addFormattingTags(xpp): addEndTags(xpp.getName());
                case "font":
                    return isStartTag ? addFormattingTags(xpp): addEndTags(xpp.getName());
                case "sub":
                    return isStartTag ? addFormattingTags(xpp): addEndTags(xpp.getName());
                case "sup":
                    return isStartTag ? addFormattingTags(xpp): addEndTags(xpp.getName());
                case "em":
                    return isStartTag ? addFormattingTags(xpp): addEndTags(xpp.getName());
                case "dfn":
                    return isStartTag ? addFormattingTags(xpp): addEndTags(xpp.getName());
                case "tt":
                    return isStartTag ? addFormattingTags(xpp): addEndTags(xpp.getName());
                case "img":     //TODO: So, what will happen here?
                    return null;
                case "del":
                    return isStartTag ? " <strike> ": " </strike> ";
                default:
                    return null;
            }
    }
    private static String addFormattingTags(XmlPullParser xpp) {
        String addTag = "";

        addTag += " <";

        if (!(xpp.getAttributeCount() == 0)) {
            for (int i = 0; i < xpp.getAttributeCount(); i++) {
                addTag += xpp.getName() + " ";
                addTag += xpp.getAttributeName(i);
                addTag += "=";
                addTag += "\"" + xpp.getAttributeValue(i) + "\"";
                if (!(i == xpp.getAttributeCount() - 1)) {
                    addTag += " ";
                }
            }
        } else {
            addTag += xpp.getName();
        }

        addTag += "> ";

        return addTag;

    }
    private static String addEndTags(String tagName){
        return " <" + tagName + "/> ";
    }

    public static boolean parseErrorCode(Context mContext, String errorResponse) {
        final AlertDialog mDialog = new AlertDialog.Builder(mContext).create();
        mDialog.setTitle(mContext.getString(R.string.warning_word));
        mDialog.setButton(DialogInterface.BUTTON_NEUTRAL, mContext.getResources().getText(R.string.ok_button_word), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mDialog.cancel();
            }
        });

        switch (errorResponse) {
            case "ERROR_POST_FAILED":
                mDialog.setMessage(mContext.getString(R.string.response_error_post_failed));
                mDialog.show();
                return false;
            case "NO_USER":
                mDialog.setMessage(mContext.getString(R.string.response_no_user));
                mDialog.show();
                return false;
            case "ERROR":
                mDialog.setMessage(mContext.getString(R.string.response_error));
                mDialog.show();
                return false;
            default:
                return true;
        }
    }
    public static void onRequestErrorRespone(Context mContext, VolleyError error, final ResponseOperations.TryAgainAction onTryAgain) {
        final AlertDialog mDialog = new AlertDialog.Builder(mContext).create();
        mDialog.setTitle(mContext.getString(R.string.warning_word));

        if (error instanceof NetworkError) {
            mDialog.setMessage(mContext.getString(R.string.volley_error));
            mDialog.setButton(DialogInterface.BUTTON_NEUTRAL, mContext.getResources().getText(R.string.ok_button_word), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    mDialog.cancel();
                }
            });
            mDialog.show();
        } else if (error instanceof TimeoutError) {
            mDialog.setMessage(mContext.getString(R.string.volley_time_out_error));
            mDialog.setButton(DialogInterface.BUTTON_NEUTRAL, mContext.getResources().getText(R.string.try_again_word), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    onTryAgain.onTryAgain();
                    mDialog.cancel();
                }
            });
            mDialog.show();
        } else if (error instanceof ServerError) {
            mDialog.setMessage(mContext.getString(R.string.volley_server_error));
            mDialog.setButton(DialogInterface.BUTTON_NEUTRAL, mContext.getResources().getText(R.string.ok_button_word), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    mDialog.cancel();
                    //TODO: Show some error image in the background.
                }
            });
            mDialog.show();

        } else {
            mDialog.setMessage(mContext.getString(R.string.volley_server_error));
            mDialog.setButton(DialogInterface.BUTTON_NEUTRAL, mContext.getResources().getText(R.string.ok_button_word), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    mDialog.cancel();
                }
            });
            mDialog.show();
        }


    }

    @Deprecated
    public static String EncodeString(String text) {
        String mText;
        try {
            mText = new String(text.getBytes("ISO-8859-1"), "UTF-8");
            return mText;
        } catch (UnsupportedEncodingException e) {
            //TODO: Handle error
            return null;
        }
    }

}
