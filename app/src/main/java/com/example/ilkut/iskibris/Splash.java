package com.example.ilkut.iskibris;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;


public class Splash extends Activity {

    Animation FadeOut;
    ImageView startLogo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);

        //TODO: When checking the info, display the logo (no fade out, maybe a progress view) OR skip this, do authentification in the main area
        //Check everytime whether the login info is correct, directs to the MainActivity if incorrect, directs to the MainArea without asking for login if correct.
        SharedPreferences checkLogged = getSharedPreferences("PREFS", Activity.MODE_PRIVATE);
        Boolean isLogged = checkLogged.getBoolean("logged", false);
        if (isLogged) {
            checkCreds();
        } else {
            startAnimations();
        }

    }

    public void startAnimations() {

        startLogo = (ImageView) findViewById(R.id.startLogo);
        FadeOut = AnimationUtils.loadAnimation(this, R.anim.fade_out);

        FadeOut.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                Intent startIntent;
                startIntent = new Intent(Splash.this, MainActivity.class).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(startIntent);
                finish();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        CountDownTimer fadeOutTimer;
        fadeOutTimer = new CountDownTimer(1500, 100) {
            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {
                startLogo.startAnimation(FadeOut);
            }

        };
        fadeOutTimer.start();
    }

    public void checkCreds() {
        final SharedPreferences checkLogged = getSharedPreferences("PREFS", Activity.MODE_PRIVATE);
        final AlertDialog mDialog = new AlertDialog.Builder(this).create();

        String url = getResources().getString(R.string.URL_ACCOUNT_LOGIN);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response.trim().startsWith("SUCCESS")) {
                            Intent splashRedirect;
                            splashRedirect = new Intent(Splash.this, MainArea.class).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                            startActivity(splashRedirect);
                            finish();
                        } else if (response.trim().startsWith("ERROR_POST_FAILED")) {

                            mDialog.setMessage(getResources().getString(R.string.response_error_post_failed));
                            mDialog.setButton(DialogInterface.BUTTON_NEUTRAL, getResources().getText(R.string.try_again_word), new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    checkCreds();
                                    mDialog.cancel();
                                }
                            });
                            mDialog.show();

                        } else if (response.trim().startsWith("ERROR")) {
                            mDialog.setMessage(getResources().getString(R.string.response_error_post_failed));
                            mDialog.setButton(DialogInterface.BUTTON_NEUTRAL, getResources().getText(R.string.try_again_word), new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    checkCreds();
                                    mDialog.cancel();
                                }
                            });
                            mDialog.show();
                        } else if (response.trim().startsWith("INCORRECT_CREDS")) {

                            mDialog.setMessage(getResources().getString(R.string.splash_details_changed));
                            mDialog.setButton(DialogInterface.BUTTON_NEUTRAL, getResources().getText(R.string.ok_button_word), new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    mDialog.cancel();
                                }
                            });
                            mDialog.show();
                            startAnimations();
                        }
                        else {
                            mDialog.setMessage(getResources().getString(R.string.response_error_post_failed));
                            mDialog.setButton(DialogInterface.BUTTON_NEUTRAL, getResources().getText(R.string.try_again_word), new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    checkCreds();
                                    mDialog.cancel();
                                }
                            });
                            mDialog.show();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                ResponseOperations.onRequestErrorRespone(Splash.this, error, new ResponseOperations.TryAgainAction() {
                    @Override
                    public void onTryAgain() {
                        checkCreds();
                    }
                });
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("username", checkLogged.getString("uname", null));
                params.put("pass", checkLogged.getString("paword", null));
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("Content-Type", "application/x-www-form-urlencoded");
                return params;
            }
        };

        SingletonRequest.getInstance(this).getRequestQueue().add(stringRequest);
    }

}
