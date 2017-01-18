package com.amigosphire_poc.demo.services;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.IBinder;
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
import com.amigosphire_poc.demo.utils.VolleySingleton;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.w3c.dom.Text;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import static com.amigosphire_poc.demo.utils.ServicePoints.CALL_POPUP;
import static com.amigosphire_poc.demo.utils.ServicePoints.CUSTOMER_REGISTER;

public class CallManagerService extends Service implements View.OnClickListener {
    private Context mContext;
    private TelephonyManager mTelephonyManager;

    private static final String TAG="CallManagerService";

    @Override
    public void onCreate() {
        mContext = this;
        mTelephonyManager = (TelephonyManager) mContext.getSystemService(Context.TELEPHONY_SERVICE);
        mTelephonyManager.listen(new CallStateListener(), PhoneStateListener.LISTEN_CALL_STATE);

        Log.e("sharath","*** CallManagerService started");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Toast.makeText(this, "service starting", Toast.LENGTH_SHORT).show();

        // If we get killed, after returning from here, restart
        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onClick(View v) {
        final WindowManager wm = (WindowManager) getSystemService(WINDOW_SERVICE);

        switch(v.getId()) {
            case R.id.canceldialog:
                wm.removeView(v.getRootView());
                break;

            case R.id.yesbutton:
                wm.removeView(v.getRootView());
                break;

            case R.id.nobutton:
                wm.removeView(v.getRootView());
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
                    Toast.makeText(mContext,
                            "Incoming: "+incomingNumber,
                            Toast.LENGTH_LONG).show();

                    queryCallerData(incomingNumber);
                    break;

                case TelephonyManager.CALL_STATE_OFFHOOK:
                    Toast.makeText(mContext,
                            "CALL_STATE_OFFHOOK: ",
                            Toast.LENGTH_LONG).show();
                    break;

                case TelephonyManager.CALL_STATE_IDLE:
                    if(mLastState == TelephonyManager.CALL_STATE_OFFHOOK) {
                        //showDialog(R.layout.caller_chargedetails);
                    }
                    break;
            }
            mLastState = state;
        }
    }

    void showDialog(int layoutID, String userData) {
        Toast.makeText(getBaseContext(),"onCreate", Toast.LENGTH_LONG).show();
        WindowManager.LayoutParams params = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_SYSTEM_ALERT,
                0,
                PixelFormat.TRANSLUCENT);
        params.gravity = Gravity.VERTICAL_GRAVITY_MASK | Gravity.TOP;

        final WindowManager wm = (WindowManager) getSystemService(WINDOW_SERVICE);

        switch(layoutID) {
            case R.layout.caller_info:
                final View info_view = View.inflate(this.getApplicationContext(), layoutID, null);
                TextView username = (TextView)  info_view.findViewById(R.id.callername);

                Map<String,String> MapName = convert(userData);

                Log.d(TAG,"mapname:" + MapName + " name:" + MapName.get("\"first_name\"") + MapName.get("\"last_name\"")) ;
                username.setText(MapName.get("\"first_name\"") + MapName.get("\"last_name\""));
                ImageView cancelCall = (ImageView) info_view.findViewById(R.id.canceldialog);
                cancelCall.setOnClickListener(this);

                wm.addView(info_view, params);
                break;

            case R.layout.caller_chargedetails:
                final View charge_view = View.inflate(this.getApplicationContext(), layoutID, null);
                TextView userName = (TextView) charge_view.findViewById(R.id.username);

                Button yesButton = (Button) charge_view.findViewById(R.id.yesbutton);
                yesButton.setOnClickListener(this);

                Button noButton = (Button) charge_view.findViewById(R.id.nobutton);
                noButton.setOnClickListener(this);

                wm.addView(charge_view, params);
                break;
        }
    }

    private void queryCallerData(final String callernumber) {
        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.POST, CALL_POPUP,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e(TAG,"*** response success" + response);

                        showDialog(R.layout.caller_info, response);

                        Toast.makeText(mContext,"user registered as customer",Toast.LENGTH_LONG).show();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG,"*** response failure " + error.networkResponse.statusCode + " headers:" + error.networkResponse.headers);
            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String>  params = new HashMap<String, String>();
                params.put("Accept", "application/json");
                params.put("Accept-Language", "en-US, en;q=0.8");
                params.put("Content-Type","application/json");
                params.put("X-HTTP-Method-Override","RQSTREG");

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

    public static Map<String, String> convert(String data) {
        data = data.substring(1, data.length()-1);           //remove curly brackets
        String[] keyValuePairs = data.split(",");              //split the string to creat key-value pairs
        Map<String,String> map = new HashMap<>();

        for(String pair : keyValuePairs)                        //iterate over the pairs
        {
            String[] entry = pair.split(":");                   //split the pairs to get key and value
            map.put(entry[0].trim(), entry[1].trim());          //add them to the hashmap and trim whitespaces
        }
        return map;
    }
}