package com.example.ilkut.iskibris;


import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.RelativeLayout;


public class MainActivity extends Activity {

    RelativeLayout employerPanel, employeePanel;
    Button employeeButton, employerButton;
    Typeface DINPro;
    Animation BottomScaleAnim, TopScaleAnim, BottomScaleAnimReverse, TopScaleAnimReverse, BottomReverseScaleAnim, TopReverseScaleAnim;
    String loginMessage = "";        //The extra data sent to the login screen which says which button was clicked (also used the other way around, when the activty is launched backwards)

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Definitions
        employeePanel = (RelativeLayout) findViewById(R.id.employeeLayout);
        employerPanel = (RelativeLayout) findViewById(R.id.employerLayout);
        employeeButton = (Button) findViewById(R.id.employeeButton);
        employerButton = (Button) findViewById(R.id.employerButton);


        //Setting things up
        setAnimations();
        setTypefaces();             //Set everything up...


        Bundle bundleReverseScale = getIntent().getExtras();
        if (bundleReverseScale != null) {
            loginMessage = bundleReverseScale.getString("loginMessage");
        }
        if (loginMessage.equals("candidate")) {
            employeeButton.setVisibility(View.GONE);
            employerButton.setVisibility(View.GONE);
            employeePanel.startAnimation(TopScaleAnimReverse);
            employerPanel.startAnimation(TopReverseScaleAnim);
        } else if (loginMessage.equals("employer")) {
            employeeButton.setVisibility(View.GONE);
            employerButton.setVisibility(View.GONE);
            employerPanel.startAnimation(BottomScaleAnimReverse);
        }

    }

    //region initialisers

    public void setTypefaces() {
        DINPro = Typeface.createFromAsset(this.getResources().getAssets(), "fonts/DINPro.otf");
        employeeButton.setTypeface(DINPro);
        employerButton.setTypeface(DINPro);
    }

    public void setAnimations() {           //You can find the events that occur when the animations start and end here

        TopScaleAnim = AnimationUtils.loadAnimation(this, R.anim.top_scale_anim);
        BottomScaleAnim = AnimationUtils.loadAnimation(this, R.anim.bottom_scale_anim);
        TopScaleAnimReverse = AnimationUtils.loadAnimation(this, R.anim.top_scale_anim_reverse);
        TopReverseScaleAnim = AnimationUtils.loadAnimation(this, R.anim.top_reverse_scale_anim);
        BottomScaleAnimReverse = AnimationUtils.loadAnimation(this, R.anim.bottom_scale_anim_reverse);

        /*The Reverse animations (not the ones ending in reverse) are needed for the top panel
        Because top panel is not on top, so will stay under the bottom panel when scaling,
        The Reverse animations shrink the bottom panels at the same time as the top
        panels are growing, so that the top panels don't stay under the bottom panels.*/
        BottomReverseScaleAnim = AnimationUtils.loadAnimation(this, R.anim.bottom_reverse_scale_anim);
        TopReverseScaleAnim = AnimationUtils.loadAnimation(this, R.anim.top_reverse_scale_anim);

        Animation.AnimationListener panelsAnimationListener = new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                employeeButton.setVisibility(View.GONE);
                employerButton.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                launchLoginScreen();
                finish();
            }


            public void onAnimationRepeat(Animation animation) {

            }
        };
        Animation.AnimationListener panelsAnimationListenerReverse = new Animation.AnimationListener() {

            @Override
            public void onAnimationEnd(Animation animation) {
                employeeButton.setVisibility(View.VISIBLE);
                employerButton.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        };

        TopScaleAnim.setAnimationListener(panelsAnimationListener);
        BottomScaleAnim.setAnimationListener(panelsAnimationListener);
        BottomScaleAnimReverse.setAnimationListener(panelsAnimationListenerReverse);
        TopScaleAnimReverse.setAnimationListener(panelsAnimationListenerReverse);
        TopReverseScaleAnim.setAnimationListener(panelsAnimationListenerReverse);
    }

    //endregion


    public void onEmployerButtonClicked(View view) {
        loginMessage = "employer";
        //TODO: Set the TabHost background colors, etc. to reflect the chosen account type
        employerPanel.startAnimation(BottomScaleAnim);
    }

    public void onEmployeeButtonClicked(View view) {
        loginMessage = "candidate";
        //TODO: Set the TabHost background colors, etc. to reflect the chosen account type
        employeePanel.startAnimation(TopScaleAnim);
        employerPanel.startAnimation(BottomReverseScaleAnim);
    }

    public void launchLoginScreen() {
        Intent loginScreenIntent;
        loginScreenIntent = new Intent(MainActivity.this, LoginScreen.class).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        loginScreenIntent.putExtra("loginMessage", loginMessage);
        startActivity(loginScreenIntent);
    }


}