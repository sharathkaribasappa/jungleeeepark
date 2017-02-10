package com.amigosphire_poc.demo.activities;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.EditText;

import com.amigosphire_poc.R;
import com.amigosphire_poc.demo.utils.ServicePoints;
import com.amigosphire_poc.demo.utils.VolleySingleton;
import com.android.volley.Request;
import com.android.volley.VolleyError;

import java.util.HashMap;
import java.util.Map;

public class ProfileScreenActivity extends AppCompatActivity {

    private static final String HTTP_OVERRIDE_PROFILE_GET = "PROV_PRFL_GET";

    private static final String TAG = "ProfileScreenActivity";
    public static final String MyPREFERENCES = "MyPrefs";

    private Map<String,String> userData = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_screen);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        serverRequest();
    }


    void serverRequest() {
        VolleySingleton.getInstance(this).serverRequest(this, getUserData(), HTTP_OVERRIDE_PROFILE_GET,
        new VolleySingleton.VolleyCallBack() {
            @Override
            public void userDatarespone(String response) {
                formatResponse(response);
                updateUI();
                Log.d(TAG," user data:" + response);
            }

            @Override
            public void errorResponse(VolleyError error) {
                Log.e(TAG," user data:" + "*** error response");
            }
        }, ServicePoints.PROV_PROFILE_GET, Request.Method.POST);
    }

    private String getUserData() {
        StringBuilder sb = new StringBuilder();
        sb.append("{");

        sb.append("\"" + "prov_phone_no" + "\":");

        SharedPreferences sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        String phoneNumber = sharedpreferences.getString("pv_number","");
        sb.append("\"" + phoneNumber + "\"");
        sb.append("}");

        return sb.toString();
    }

    private void formatResponse(String response) {
        String[] parts = response.split(",");
        String[] sb;

        for(int i = 0; i < parts.length; i++) {
            sb = parts[i].split(":");
            removeQuotes(sb[0]);
            removeQuotes(sb[1]);
            userData.put(removeQuotes(sb[0]), removeQuotes(sb[1]));
        }

        for(String key : userData.keySet()) {
            Log.d(TAG,"key:" + key + " value:" + userData.get(key));
        }

    }

    private String removeQuotes(String response) {
        int a = 0;
        int b = 0;

        char[] str = response.toCharArray();
        for(int i = 0; i < str.length; i++) {
            if(str[i] == '"') {
                a = i;
                break;
            }
        }

        for(int i = a+1; i < str.length; i++) {
            if(str[i] == '"') {
                b = i;
                break;
            }
        }

        //System.out.println(response.substring(a+1, b));

        return response.substring(a+1, b);
    }

    private void updateUI() {
        EditText Profession = (EditText) findViewById(R.id.user_profession);
        Profession.setText(userData.get("profession"));
        Profession.setFocusable(false);

        EditText Location = (EditText) findViewById(R.id.user_location);
        Location.setText(userData.get("city"));
        Location.setFocusable(false);

        EditText Area = (EditText) findViewById(R.id.user_area);
        Area.setText(userData.get("area"));
        Location.setFocusable(false);

        EditText Specialization = (EditText) findViewById(R.id.user_specialization);
        Specialization.setText(userData.get("specialization"));
        Location.setFocusable(false);

        EditText highest_certification = (EditText) findViewById(R.id.user_highestcertificate);
        highest_certification.setText(userData.get("highest_certification"));
        Location.setFocusable(false);

        EditText office = (EditText) findViewById(R.id.user_office);
        office.setText(userData.get("office"));
        Location.setFocusable(false);
    }
}
