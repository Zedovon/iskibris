package com.example.ilkut.iskibris;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class ForgotPassword extends Activity {


    TextView emailText, checkMailText;
    EditText emailEdit;
    Button sendButton;
    Typeface DINPro;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        setContentView(R.layout.forgot_password);

        //Set views
        emailText = (TextView) findViewById(R.id.emailText);
        checkMailText = (TextView) findViewById(R.id.checkMailText);
        emailEdit = (EditText) findViewById(R.id.emailEdit);
        sendButton = (Button) findViewById(R.id.sendButton);

       setTypefaces();

        super.onCreate(savedInstanceState);
    }

    public void onBackPressed() {
        Intent MainActivityIntent;
        MainActivityIntent = new Intent(ForgotPassword.this, LoginScreen.class).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(MainActivityIntent);
        finish();

    }

    public void setTypefaces() {
        DINPro = Typeface.createFromAsset(getResources().getAssets(), "fonts/DINPro.otf");

        //Set typefaces
        emailText.setTypeface(DINPro);
        checkMailText.setTypeface(DINPro);
        emailEdit.setTypeface(DINPro);
        sendButton.setTypeface(DINPro);
    }

}
