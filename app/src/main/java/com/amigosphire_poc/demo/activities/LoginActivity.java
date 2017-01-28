package com.amigosphire_poc.demo.activities;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.amigosphire_poc.R;
import com.amigosphire_poc.demo.utils.VolleySingleton;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import static com.amigosphire_poc.demo.utils.ServicePoints.LOGIN;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity {

    // UI references.
    private View mProgressView;
    private View mLoginFormView;

    private String mPhoneNumber;
    public static final String MyPREFERENCES = "MyPrefs" ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Button mEmailSignInButton = (Button) findViewById(R.id.email_sign_in_button);
        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                showProgress(true);
                userLogin();
            }
        });

        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);
    }

    private boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        return email.contains("@");
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 4;
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
    public void userLogin() {
        // Request a string response from the provided URL.

        StringRequest stringRequest = new StringRequest(Request.Method.POST, LOGIN,
            new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.e("UserLoginTask", "*** response:" + response);

                    showProgress(false);
                    Intent intent = new Intent(LoginActivity.this, AmigoLandingActivity.class);
                    intent.putExtra("phone_number",mPhoneNumber);
                    startActivity(intent);
                    finish();
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    if(error != null) {
                        Log.e("UserLoginTask", "*** response failure " + error.networkResponse.statusCode + error.networkResponse.headers);
                        showProgress(false);
                        if (error.networkResponse.statusCode == 403 && error.networkResponse.headers.get("Status").equals("FAIL")) {
                            Toast.makeText(getApplicationContext(), "invalid phonenumber or password", Toast.LENGTH_LONG).show();
                        }
                    }
                }
            })
            {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String>  params = new HashMap<String, String>();
                params.put("Accept", "application/json");
                params.put("Content-Type","application/json");
                params.put("X-HTTP-Method-Override","LOGIN");
                return params;
            }

            @Override
            public byte[] getBody() throws AuthFailureError {
                String loginData = getLoginData();
                Log.e("LoginActivity", "loginData:" + loginData);
                byte[] body = new byte[0];
                try {
                    body = loginData.getBytes("UTF-8");
                } catch (UnsupportedEncodingException e) {
                    Log.e("LoginActivity", "Unable to gets bytes from JSON", e.fillInStackTrace());
                }
                return body;
            }
        };
        VolleySingleton.getInstance(LoginActivity.this).addToRequestQueue(stringRequest);
    }

    public String getLoginData() {
        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append("{");
        stringBuilder.append("\"phone_no\":");

        EditText editText;
        editText = (EditText) this.findViewById(R.id.user_phonenumber);
        mPhoneNumber = editText.getText().toString();
        stringBuilder.append("\"" + mPhoneNumber + "\"");

        setProviderNumberinSP(mPhoneNumber);

        stringBuilder.append(",");

        stringBuilder.append("\"pass\":");

        editText = (EditText) findViewById(R.id.password);
        stringBuilder.append("\"" + editText.getText().toString() + "\"");

        stringBuilder.append("}");
        return stringBuilder.toString();
    }

    private void setProviderNumberinSP(String phoneNumber) {
        SharedPreferences sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();

        editor.putString("pv_number", phoneNumber);
        editor.commit();
    }
}