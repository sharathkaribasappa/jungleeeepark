package com.amigosphire_poc.demo.activities;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.amigosphire_poc.R;
import com.amigosphire_poc.demo.utils.ServicePoints;
import com.amigosphire_poc.demo.utils.VolleySingleton;
import com.android.volley.Request;
import com.android.volley.VolleyError;

public class FeedBackActivity extends AppCompatActivity implements View.OnClickListener{

    public static final String HTTP_FEEDBACK_POST = "APP_FDBK";
    public static final String TAG = "FeedBackActivity";

    public static final String MyPREFERENCES = "MyPrefs" ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed_back);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Button button = (Button) findViewById(R.id.post_feedback);
        button.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        String message = getFeedBackMessage(v.getRootView());
        sendFeedback(message);
    }

    private void sendFeedback(final String message) {
        VolleySingleton.getInstance(this).serverRequest(this, message, HTTP_FEEDBACK_POST,
                new VolleySingleton.VolleyCallBack() {
                    @Override
                    public void userDatarespone(String response) {
                        Log.d(TAG," feedback sent successfully");
                    }

                    @Override
                    public void errorResponse(VolleyError error) {
                        Log.e(TAG," feedback not sent, error response :" + error.networkResponse.statusCode);
                    }
                }, ServicePoints.FEEDBACK_POST, Request.Method.POST);
    }

    private String getFeedBackMessage(View view) {
        StringBuilder sb = new StringBuilder();
        sb.append("{");

        sb.append("\"" + "phone_no" + "\":");

        SharedPreferences sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        String phoneNumber = sharedpreferences.getString("pv_number","");
        sb.append("\"" + phoneNumber + "\",");

        sb.append("\"" + "date_time_raw" + "\":");
        sb.append("\"" + System.currentTimeMillis() + "\",");

        sb.append("\"" + "user_type" + "\":");
        sb.append("\"" + "L" + "\",");

        sb.append("\"" + "first_name" + "\":");
        sb.append("\"" + "Sayeed" + "\",");

        sb.append("\"" + "last_name" + "\":");
        sb.append("\"" + "Shariff" + "\",");

        sb.append("\"" + "app_version" + "\":");
        sb.append("\"" + "1.0" + "\",");

        String model = Build.MODEL;
        int version = Build.VERSION.SDK_INT;
        String versionRelease = Build.VERSION.RELEASE;

        sb.append("\"" + "android_version" + "\":");
        sb.append("\"" + versionRelease + "\",");

        sb.append("\"" + "Android " + "\":");
        sb.append("\"" + version + "\",");

        sb.append("\"" + "phone_model " + "\":");
        sb.append("\"" + model + "\",");

        EditText editText = (EditText) view.findViewById(R.id.enter_feedback);
        String Message = editText.getText().toString();

        sb.append("\"" + "feedback " + "\":");
        sb.append("\"" + Message + "\",");

        sb.append("\"" + "long_info " + "\":");
        sb.append("\"" + "some long info message" + "\"");

        sb.append("}");

        return sb.toString();
    }
}
