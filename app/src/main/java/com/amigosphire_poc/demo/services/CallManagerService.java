package com.amigosphire_poc.demo.services;

import android.Manifest;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.PixelFormat;
import android.os.AsyncTask;
import android.os.IBinder;
import android.provider.CallLog;
import android.support.v4.app.ActivityCompat;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.amigosphire_poc.R;
import com.amigosphire_poc.demo.utils.ServicePoints;
import com.amigosphire_poc.demo.utils.VolleySingleton;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

import static com.amigosphire_poc.demo.utils.ServicePoints.CALL_POPUP;

public class CallManagerService extends Service implements View.OnClickListener {
    private Context mContext;
    private TelephonyManager mTelephonyManager;

    private static final String TAG = "CallManagerService";

    private Map<String, String> mUserData = new HashMap<>();

    private String mPhoneNumber = "";
    private String mCharge = "";
    private static final String HTTP_OVERRIDE_CALL_CHARGES = "CALL_CHARGES";

    private String[] Projection = {CallLog.Calls._ID, CallLog.Calls.NUMBER, CallLog.Calls.DURATION, CallLog.Calls.TYPE, CallLog.Calls.DATE};

    @Override
    public void onCreate() {
        mContext = this;
        mTelephonyManager = (TelephonyManager) mContext.getSystemService(Context.TELEPHONY_SERVICE);
        mTelephonyManager.listen(new CallStateListener(), PhoneStateListener.LISTEN_CALL_STATE);

        Log.e("sharath", "*** CallManagerService started");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // If we get killed, after returning from here, restart
        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onClick(View v) {
        final WindowManager wm = (WindowManager) getSystemService(WINDOW_SERVICE);

        switch (v.getId()) {
            case R.id.canceldialog:
                wm.removeView(v.getRootView());
                break;

            case R.id.yesbutton:
                wm.removeView(v.getRootView());
                mCharge = "Y";
                sendCallLogData();
                break;

            case R.id.nobutton:
                wm.removeView(v.getRootView());
                mCharge = "N";
                sendCallLogData();
                break;
        }
    }

    /**
     * Listener to detect phone state changes
     */
    private class CallStateListener extends PhoneStateListener {
        private int mLastState = TelephonyManager.CALL_STATE_IDLE;

        @Override
        public void onCallStateChanged(int state, String incomingNumber) {
            switch (state) {
                case TelephonyManager.CALL_STATE_RINGING:
                    queryCallerData(incomingNumber);
                    break;

                case TelephonyManager.CALL_STATE_OFFHOOK:
                    break;

                case TelephonyManager.CALL_STATE_IDLE:
                    if (mLastState == TelephonyManager.CALL_STATE_OFFHOOK) {
                        showDialog(R.layout.caller_chargedetails, "charge the customer");
                    }
                    break;
            }
            mLastState = state;
        }
    }

    void showDialog(int layoutID, String userData) {
        Toast.makeText(getBaseContext(), "onCreate", Toast.LENGTH_LONG).show();
        WindowManager.LayoutParams params = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_SYSTEM_ALERT,
                0,
                PixelFormat.TRANSLUCENT);
        params.gravity = Gravity.VERTICAL_GRAVITY_MASK | Gravity.TOP;

        final WindowManager wm = (WindowManager) getSystemService(WINDOW_SERVICE);

        switch (layoutID) {
            case R.layout.caller_info:
                final View info_view = View.inflate(this.getApplicationContext(), layoutID, null);
                TextView userName1 = (TextView) info_view.findViewById(R.id.callername);

                mUserData = convert(userData);

                Log.d(TAG, "mapname:" + mUserData + " name:" + mUserData.get("\"first_name\"") + mUserData.get("\"last_name\""));
                userName1.setText(mUserData.get("\"first_name\"") + mUserData.get("\"last_name\""));
                ImageView cancelCall = (ImageView) info_view.findViewById(R.id.canceldialog);
                cancelCall.setOnClickListener(this);

                wm.addView(info_view, params);
                break;

            case R.layout.caller_chargedetails:
                final View charge_view = View.inflate(this.getApplicationContext(), layoutID, null);
                TextView userName2 = (TextView) charge_view.findViewById(R.id.username);

                userName2.setText(mUserData.get("\"first_name\"") + mUserData.get("\"last_name\""));

                Button yesButton = (Button) charge_view.findViewById(R.id.yesbutton);
                yesButton.setOnClickListener(this);

                Button noButton = (Button) charge_view.findViewById(R.id.nobutton);
                noButton.setOnClickListener(this);

                wm.addView(charge_view, params);
                break;
        }
    }

    private void queryCallerData(final String callernumber) {
        mPhoneNumber = callernumber;
        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.POST, CALL_POPUP,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e(TAG, "*** response success" + response);
                        showDialog(R.layout.caller_info, response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e(TAG, "*** response failure " + error.networkResponse.statusCode + " headers:" + error.networkResponse.headers);
                }
            }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Accept", "application/json");
                params.put("Accept-Language", "en-US, en;q=0.8");
                params.put("Content-Type", "application/json");
                params.put("X-HTTP-Method-Override", "RQSTREG");

                return params;
            }

            @Override
            public byte[] getBody() throws AuthFailureError {
                String registrationData = getCallerNumber(callernumber);
                Log.e(TAG, "Customer Registration Data:" + registrationData);
                byte[] body = new byte[0];
                try {
                    body = registrationData.getBytes("UTF-8");
                } catch (UnsupportedEncodingException e) {
                    Log.e("UserProfileEditFragment", "Unable to gets bytes from JSON", e.fillInStackTrace());
                }
                return body;
            }

            @Override
            public String getBodyContentType() {
                return "application/json";
            }
        };

        VolleySingleton.getInstance(mContext).addToRequestQueue(stringRequest);
    }

    String getCallerNumber(final String number) {
        return "{\"rqstr_phone_no\":\"" + number + "\"}";
    }

    public Map<String, String> convert(String data) {
        data = data.substring(1, data.length() - 1);           //remove curly brackets
        String[] keyValuePairs = data.split(",");              //split the string to creat key-value pairs
        Map<String, String> map = new HashMap<>();

        for (String pair : keyValuePairs)                        //iterate over the pairs
        {
            String[] entry = pair.split(":");                   //split the pairs to get key and value
            map.put(entry[0].trim(), entry[1].trim());          //add them to the hashmap and trim whitespaces
        }
        return map;
    }

    private void sendCallLogData() {
        new CallLogQuery().execute(mPhoneNumber);
    }

    private class CallLogQuery extends AsyncTask<String, Void, Cursor> {
        @Override
        protected Cursor doInBackground(String... params) {
            if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.READ_CALL_LOG) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return null;
            }

            /*Cursor cursor = mContext.getContentResolver().query(CallLog.Calls.CONTENT_URI,
                    null, CallLog.Calls.NUMBER + " = " + "+918088217497", null, CallLog.Calls.DATE + " DESC"); */

            Cursor cursor = mContext.getContentResolver().query(CallLog.Calls.CONTENT_URI,
                    Projection, null, null, CallLog.Calls.DATE + " DESC");

            return cursor;
        }

        @Override
        protected void onPostExecute(Cursor cursor) {
            int number = cursor.getColumnIndex(CallLog.Calls.NUMBER);
            int duration = cursor.getColumnIndex(CallLog.Calls.DURATION);
            int type = cursor.getColumnIndex(CallLog.Calls.TYPE);
            int date = cursor.getColumnIndex(CallLog.Calls.DATE);

            while (cursor.moveToNext()) {
                String phNumber = cursor.getString(number);
                String callType = cursor.getString(type);
                String callDate = cursor.getString(date);
                Date callDayTime = new Date(Long.valueOf(callDate));
                String callDuration = cursor.getString(duration);

                if (phNumber.equals(mPhoneNumber)) {
                    mUserData.put("Date",callDate);
                    mUserData.put("Duration",callDuration);

                    TimeZone tz = TimeZone.getDefault();

                    Log.d(TAG, " calllog:" + "phNumber:" + phNumber + " callType:" + callType + " callDate:" + callDate + " callDayTime:" + callDayTime
                            + " callDuration:" + callDuration);

                    Log.d(TAG, " timezone:" + tz.getDisplayName(false, TimeZone.SHORT) + " tz.id:" + tz.getID());

                    mUserData.put("timeZone",tz.getDisplayName(false, TimeZone.SHORT));

                    sendUserData();
                    break;
                }
            }
        }
    }

    private String getUserData() {
        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append("{");

        stringBuilder.append("\"prov_phone_no\":");
        stringBuilder.append("\""+ mPhoneNumber + "\",");

        stringBuilder.append("\"rqstr_phone_no\":");
        stringBuilder.append("\""+ mPhoneNumber + "\",");

        stringBuilder.append("\"case_id\":");
        stringBuilder.append("\""+ "0004" + "\",");

        stringBuilder.append("\"call_date_time_raw\":");
        stringBuilder.append("\""+ mUserData.get("Date") + "\",");

        stringBuilder.append("\"call_duration_raw\":");
        stringBuilder.append("\""+ mUserData.get("Duration") + "\",");

        stringBuilder.append("\"prov_timezone\":");
        stringBuilder.append("\""+ mUserData.get("timeZone") + "\",");

        stringBuilder.append("\"chargeability\":");
        stringBuilder.append("\""+ mCharge + "\"");

        stringBuilder.append("}");

        Log.d(TAG,"sb:" + stringBuilder);

        mPhoneNumber = "";
        return stringBuilder.toString();
    }

    void sendUserData() {
        VolleySingleton.getInstance(mContext).serverRequest(mContext, getUserData(), HTTP_OVERRIDE_CALL_CHARGES,
        new VolleySingleton.VolleyCallBack() {
            @Override
            public void userDatarespone(String response) {
                Toast.makeText(mContext,"charge status sent successfully",Toast.LENGTH_LONG).show();
            }

            @Override
            public void errorResponse(VolleyError error) {
                Toast.makeText(mContext,"charge status not sent",Toast.LENGTH_LONG).show();
                Log.d(TAG,"sendUserData, error:" + error.networkResponse.statusCode + " errormessage:" + error.getMessage());
            }
        }, ServicePoints.CALL_CHARGE, Request.Method.POST);
    }
}