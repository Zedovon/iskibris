package com.example.ilkut.iskibris;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

public class FragSettings extends android.support.v4.app.Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_settings, container, false);

        Button mButton = (Button) view.findViewById(R.id.button);
        final TextView mText = (TextView) view.findViewById(R.id.textView);
        final String text = "<del datetime=\"2015-08-31T09:41:47+00:00\">This is a</del> really<strong> nice blog</strong> post <blockquote>abo<span style=\"text-decoration: underline;\">ut many things,</span> really! And, by many things, I mean,</blockquote> re<em>ally, a lo</em>t!";

       mButton.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
//               mText.setText(ResponseOperations.SanitizeText(text));
           }
       });

        return view;
    }

}







