package com.example.ilkut.iskibris;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;


public class LoginScreen extends Activity {

    Typeface DINPro;
    EditText usernameEdit, passwordEdit;
    Button loginButton, forgotPasswordText, signupText;
    RelativeLayout loginLayout;
    String loginMessage;        //The message from the other activity, tells the activity which button was clicked ('Employer' or 'Employee') (also used when signing in, only an employee can sign in from the employee section, for example)
    AlertDialog loginAlert;
    ProgressDialog loginProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle bundleLoginMessage = getIntent().getExtras();
        loginMessage = bundleLoginMessage.getString("loginMessage");

        setContentView(R.layout.login_screen);

        //Definitions login screen
        loginButton = (Button) findViewById(R.id.loginButton);
        usernameEdit = (EditText) findViewById(R.id.usernameEdit);
        passwordEdit = (EditText) findViewById(R.id.passwordEdit);
        forgotPasswordText = (Button) findViewById(R.id.forgotPassword);
        loginLayout = (RelativeLayout) findViewById(R.id.loginLayout);
        signupText = (Button) findViewById(R.id.signUp);

        loginAlert = new AlertDialog.Builder(this).create();
        loginAlert.setButton(DialogInterface.BUTTON_NEUTRAL, getResources().getText(R.string.login_ok_button), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                loginAlert.cancel();
            }
        });

        loginProgress = new ProgressDialog(this);
        loginProgress.setMessage(getResources().getString(R.string.login_progress));
        loginProgress.setProgressStyle(ProgressDialog.STYLE_SPINNER);

        setTypefaces();

    }

    public void setTypefaces() {
        DINPro = Typeface.createFromAsset(this.getResources().getAssets(), "fonts/DINPro.otf");
        usernameEdit.setTypeface(DINPro);
        passwordEdit.setTypeface(DINPro);
        loginButton.setTypeface(DINPro);
        forgotPasswordText.setTypeface(DINPro);
        signupText.setTypeface(DINPro);
    }

    @Override
    public void onBackPressed() {

        if (loginMessage.equals("candidate")) {
            loginMessage = "candidate";
            launchMainActivity();

        } else if (loginMessage.equals("employer")) {
            loginMessage = "employer";
            launchMainActivity();
        }

    }

    public void launchMainActivity() {

        Intent MainActivityIntent;
        MainActivityIntent = new Intent(LoginScreen.this, MainActivity.class).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        MainActivityIntent.putExtra("loginMessage", loginMessage);
        startActivity(MainActivityIntent);
        finish();
    }

    //region buttons onClick methods
    public void onClickForgotPassword(View view) {
        Intent forgotPassword;
        forgotPassword = new Intent(this, ForgotPassword.class).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(forgotPassword);
    }

    public void onClickSignup(View view) {
        Intent signUp;
        signUp = new Intent(this, SignUp.class).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        signUp.putExtra("loginMessage", loginMessage);
        startActivity(signUp);
    }

    public void onClickLogin(View view) {

        switch (isAllFieldFilled()) {
            case ALLRIGHT:
                loginProgress.show();
                doLogin(usernameEdit.getText().toString(), passwordEdit.getText().toString());
                break;
            case WRITE_USERNAME:
                loginAlert.setMessage(getResources().getString(R.string.login_no_username));
                loginAlert.show();
                break;
            case WRITE_PASSWORD:
                loginAlert.setMessage(getResources().getString(R.string.login_no_password));
                loginAlert.show();
                break;
        }

    }               //Login step 1/part 1
    //endregion

    //region Login Operations
    enum infoCheckReturn {
        ALLRIGHT,
        WRITE_USERNAME,
        WRITE_PASSWORD,
    }
    private infoCheckReturn isAllFieldFilled() {

        if (usernameEdit.getText().toString().trim().equals("")) {
            return infoCheckReturn.WRITE_USERNAME;
        } else if (passwordEdit.getText().toString().trim().equals("")) {
            return infoCheckReturn.WRITE_PASSWORD;
        } else {
            return infoCheckReturn.ALLRIGHT;
        }


    }                                        //Login step 1/part 2

    private void doLogin(final String userName, final String password) {
        String url = getResources().getString(R.string.URL_ACCOUNT_LOGIN);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        loginProgress.hide();
                        loginAccount(response.trim());
                        //TODO: Test toast
                        Toast.makeText(LoginScreen.this, response.trim(), Toast.LENGTH_LONG).show();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                ResponseOperations.onRequestErrorRespone(LoginScreen.this, error, new ResponseOperations.TryAgainAction() {
                    @Override
                    public void onTryAgain() {
                        doLogin(userName, password);
                    }
                });
                Toast.makeText(LoginScreen.this, error.toString(), Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("username",userName);
                params.put("pass", password);
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

    }        //Login step 2/part 1
    private void loginAccount(String response) {
        /* 1) check whether all fields are filled  (onClick of LoginButton)
           2) check whether the login info is correct (You are here)
           3) save info to shared preferences and send the role of the account to the next screen (stored inside loginMessage)
        */

        if(response.trim().startsWith("SUCCESS")){
            if(response.endsWith("candidate")){
                if(loginMessage.equals("candidate")){
                    doLocalLogin(usernameEdit.getText().toString(), passwordEdit.getText().toString());
                    usernameEdit.setText("");
                    passwordEdit.setText("");
                    finish();
                }else{
                    loginAlert.setTitle(getResources().getString(R.string.login_warning_word));
                    loginAlert.setMessage(getResources().getString(R.string.login_incorrect_panel_employer));
                    loginAlert.show();
                }
            }else if(response.endsWith("employer")){
                if(loginMessage.equals("employer")){
                    doLocalLogin(usernameEdit.getText().toString(), passwordEdit.getText().toString());
                    usernameEdit.setText("");
                    passwordEdit.setText("");
                    finish();
                }else{
                    loginAlert.setTitle(getResources().getString(R.string.login_warning_word));
                    loginAlert.setMessage(getResources().getString(R.string.login_incorrect_panel_candidate));
                    loginAlert.show();
                }
            }

        }else if(response.trim().startsWith("ERROR")){
            loginAlert.setTitle(getResources().getString(R.string.login_error_word));
            loginAlert.setMessage(getResources().getString(R.string.login_error));
            loginAlert.show();
        }else if(response.trim().startsWith("INCORRECT_CREDS")){
            usernameEdit.setText("");
            passwordEdit.setText("");
            loginAlert.setTitle(getResources().getString(R.string.login_warning_word));
            loginAlert.setMessage(getResources().getString(R.string.login_wrong_info));
            loginAlert.show();
        }else if(response.trim().startsWith("ERROR_POST_FAILED")){
            loginAlert.setTitle(getResources().getString(R.string.login_error_word));
            loginAlert.setMessage(getResources().getString(R.string.login_error));
            loginAlert.show();
        }else{
            loginAlert.setTitle(getResources().getString(R.string.login_error_word));
            loginAlert.setMessage(getResources().getString(R.string.login_error));
            loginAlert.show();
        }
    }                               //Login step 2/part 2

    public void doLocalLogin(String username, String password){
        //Save creds to shared preferences, set 'logged in' to true, etc.

        SharedPreferences creds = getSharedPreferences("PREFS", Activity.MODE_PRIVATE);
        SharedPreferences.Editor credsEditor = creds.edit();
        credsEditor.putBoolean("logged", true);
        credsEditor.putString("uname", username);
        credsEditor.putString("paword", password);
        credsEditor.putString("role", loginMessage);        //TODO: CAREFUL! Get the user role here!
        credsEditor.apply();

        Intent loginButton;
        loginButton = new Intent(this, MainArea.class).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(loginButton);

    }                                                     //Login step 3/part 3

    //endregion

}
