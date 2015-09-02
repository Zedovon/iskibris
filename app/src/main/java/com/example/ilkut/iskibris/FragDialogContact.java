package com.example.ilkut.iskibris;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class FragDialogContact extends android.support.v4.app.Fragment {

    UserResume contactUserResume;
    Context mContext;
    AlertDialog mDialog;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_dialog_contact, container, false);

        String postID = getArguments().getString("postID");
        mContext = getActivity();
        mDialog = new AlertDialog.Builder(mContext).create();

        if(getArguments() != null) {
            if (!(postID.isEmpty()) && !(postID.equals("0"))) {
                for (UserResume i : SingletonCache.getInstance().getUserResumesCache()) {               //TODO: CAREFUL! If it fails to find it in the UserResumesCache, it will have to search the SearchUserResumesCache
                    if (i.getPostID().equals(postID)) {
                        contactUserResume = i;
                        break;
                    }
                }
            } else {
                mDialog.setTitle(mContext.getString(R.string.error_word));
                mDialog.setMessage(mContext.getString(R.string.unexpected_error));
                mDialog.setButton(DialogInterface.BUTTON_POSITIVE, mContext.getString(R.string.ok_button_word), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        getActivity().getSupportFragmentManager().popBackStack();
                    }
                });
                mDialog.show();
            }
        }
        else{
            mDialog.setTitle(mContext.getString(R.string.error_word));
            mDialog.setMessage(mContext.getString(R.string.unexpected_error));
            mDialog.setButton(DialogInterface.BUTTON_POSITIVE, mContext.getString(R.string.ok_button_word), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    getActivity().getSupportFragmentManager().popBackStack();
                }
            });
            mDialog.show();
        }

        return view;
    }
}
