package com.amigosphire_poc.demo.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Toast;

import com.amigosphire_poc.R;
import com.amigosphire_poc.demo.utils.VolleySingleton;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.amigosphire_poc.demo.utils.ServicePoints.REGISTER;

public class RegistrationActivity extends AppCompatActivity implements View.OnClickListener{

    private String TAG = "RegistrationActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration_consultant);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Button createAccountButton = (Button) findViewById(R.id.create_account);
        createAccountButton.setOnClickListener(this);
    }

    private void userRegistrationRequest() {
        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.POST, REGISTER,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e(TAG,"*** response success" + response);

                        RadioButton userConsultant = (RadioButton) findViewById(R.id.user_consultant);
                        RadioButton userCustomer = (RadioButton) findViewById(R.id.user_customer);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG,"*** response failure " + error.getMessage());

            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String>  params = new HashMap<String, String>();
                params.put("Accept", "application/json");
                params.put("Accept-Language", "en-US, en;q=0.8");
                params.put("Content-Type","application/json");

                return params;
            }

            @Override
            public byte[] getBody() throws AuthFailureError {
                String registrationData = getRegistrationData();
                Log.e(TAG, "Registration Data:" + registrationData);
                byte[] body = new byte[0];
                try {
                    body = registrationData.getBytes("UTF-8");
                } catch (UnsupportedEncodingException e) {
                    Log.e("LoginActivity", "Unable to gets bytes from JSON", e.fillInStackTrace());
                }
                return body;
            }

            @Override
            public String getBodyContentType() {
                return "application/json";
            }
        };

        VolleySingleton.getInstance(this).addToRequestQueue(stringRequest);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.create_account:
                userRegistrationRequest();
                break;
        }
    }

    private String getRegistrationData() {
        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append("{");
        stringBuilder.append("name:");

        EditText editText;

        editText = (EditText) findViewById(R.id.first_name);
        stringBuilder.append(editText.getText().toString());

        editText = (EditText) findViewById(R.id.last_name);
        stringBuilder.append(editText.getText().toString());

        editText = (EditText) findViewById(R.id.phone_number);
        stringBuilder.append(editText.getText().toString());

        editText = (EditText) findViewById(R.id.email_id);
        stringBuilder.append(editText.getText().toString());

        editText = (EditText) findViewById(R.id.confirm_password);
        stringBuilder.append(editText.getText().toString());

        stringBuilder.append("}");
        return stringBuilder.toString();
    }
}