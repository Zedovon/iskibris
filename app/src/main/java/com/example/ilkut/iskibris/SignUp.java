package com.example.ilkut.iskibris;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class SignUp extends AppCompatActivity {

    EditText nameEdit, surnameEdit, usernameEdit, passwordEdit, confirmPasswordEdit, emailEdit;
    Button signUpButton;
    AlertDialog generalAlert;
    Typeface DINPro;
    ProgressDialog signupProgress;
    String loginMessage;
    Toolbar signUpToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_up);

        Bundle bundleLoginMessage = getIntent().getExtras();
        loginMessage = bundleLoginMessage.getString("loginMessage");



        nameEdit = (EditText) findViewById(R.id.signupNameEdit);
        surnameEdit = (EditText) findViewById(R.id.signupSurnameEdit);
        usernameEdit = (EditText) findViewById(R.id.signupUsernameEdit);
        passwordEdit = (EditText) findViewById(R.id.signupPasswordEdit);
        confirmPasswordEdit = (EditText) findViewById(R.id.signupConfirmPasswordEdit);
        emailEdit = (EditText) findViewById(R.id.signupEmailEdit);
        signUpButton = (Button) findViewById(R.id.signUpButton);
        signUpToolbar = (Toolbar) findViewById(R.id.signUpToolbar);

        generalAlert = new AlertDialog.Builder(this).create();

        generalAlert.setButton(DialogInterface.BUTTON_NEUTRAL, getResources().getText(R.string.signup_ok_button), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        signupProgress = new ProgressDialog(this);
        signupProgress.setMessage(getResources().getString(R.string.login_progress));
        signupProgress.setProgressStyle(ProgressDialog.STYLE_SPINNER);


        if(loginMessage.equals("employee")){
            signUpToolbar.setTitle(R.string.signup_employee_signup);
        }else if(loginMessage.equals("employer")){
            signUpToolbar.setTitle(R.string.signup_employer_signup);
        }

        setTypefaces();
    }

    public void setTypefaces() {
        DINPro = Typeface.createFromAsset(this.getResources().getAssets(), "fonts/DINPro.otf");

        nameEdit.setTypeface(DINPro);
        surnameEdit.setTypeface(DINPro);
        usernameEdit.setTypeface(DINPro);
        passwordEdit.setTypeface(DINPro);
        confirmPasswordEdit.setTypeface(DINPro);
        emailEdit.setTypeface(DINPro);
        signUpButton.setTypeface(DINPro);
    }


    public void signupOnClick(View v) {

        if(passwordEdit.getText().toString().equals(confirmPasswordEdit.getText().toString())){
            ArrayList<String> userInfo = new ArrayList<>();
            //TODO: Make it so that you get an alert when fields aren't filled
            userInfo.add(nameEdit.getText().toString());
            userInfo.add(surnameEdit.getText().toString());
            userInfo.add(usernameEdit.getText().toString());
            userInfo.add(passwordEdit.getText().toString());
            userInfo.add(emailEdit.getText().toString());

            if(loginMessage.equals("employer")){
                userInfo.add("employer");
            }else if(loginMessage.equals("candidate")){
                userInfo.add("candidate");
            }

            signupProgress.show();
            doSignup(userInfo);

        }else{
            generalAlert.setTitle(getResources().getString(R.string.signup_warning_word));
            generalAlert.setMessage(getResources().getString(R.string.signup_password_mismatch));
            generalAlert.show();
        }
    }


    /**
     *
     * @param userInfo Send the user details in an ArrayList<String>
     *                 0: firstname
     *                 1: lastname
     *                 2: username
     *                 3: password
     *                 4: email
     *                 5: role
     */
    private void doSignup(final ArrayList<String> userInfo) {
        String url = getResources().getString(R.string.URL_ACCOUNT_SIGNUP);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //TODO: Test toast
                        Toast.makeText(SignUp.this, response, Toast.LENGTH_LONG).show();
                        signupProgress.hide();
                        switch (response.trim()){
                            case "SUCCESS":
                                generalAlert.setTitle(getResources().getString(R.string.signup_success_word));
                                generalAlert.setTitle(getResources().getString(R.string.signup_success));
                                generalAlert.show();
                                break;
                            case "ERROR_POST_FAILED":
                                generalAlert.setTitle(getResources().getString(R.string.signup_error_word));
                                generalAlert.setTitle(getResources().getString(R.string.signup_error));
                                generalAlert.show();
                                break;
                            case "ERROR_USERNAME_EXISTS":
                                generalAlert.setTitle(getResources().getString(R.string.signup_warning_word));
                                generalAlert.setTitle(getResources().getString(R.string.signup_username_exists));
                                generalAlert.show();
                                break;
                            case "ERROR_EMAIL_EXISTS":
                                generalAlert.setTitle(getResources().getString(R.string.signup_warning_word));
                                generalAlert.setTitle(getResources().getString(R.string.signup_email_exists));
                                generalAlert.show();
                                break;
                            default:
                                generalAlert.setTitle(getResources().getString(R.string.signup_error_word));
                                generalAlert.setTitle(getResources().getString(R.string.signup_error));
                                generalAlert.show();
                                break;
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(SignUp.this, error.toString(), Toast.LENGTH_LONG).show();
                ResponseOperations.onRequestErrorRespone(SignUp.this, error, new ResponseOperations.TryAgainAction() {
                    @Override
                    public void onTryAgain() {
                        doSignup(userInfo);
                    }
                });
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("fname", userInfo.get(0));
                params.put("lname", userInfo.get(1));
                params.put("username",userInfo.get(2));
                params.put("pass", userInfo.get(3));
                params.put("email", userInfo.get(4));
                params.put("role", userInfo.get(5));
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
