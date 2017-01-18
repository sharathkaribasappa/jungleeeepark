package com.amigosphire_poc.demo.utils;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import static com.amigosphire_poc.demo.utils.ServicePoints.CUSTOMER_REGISTER;

/**
 * Created by sharath on 5/10/2016.
 */
public class VolleySingleton {

    private static final String TAG = "VolleySingleton";

    private static VolleySingleton mInstance;
    private RequestQueue mRequestQueue;
    //private ImageLoader mImageLoader;
    private static Context mContext;

    private VolleySingleton(Context context) {
        mContext = context;

        mRequestQueue = getRequestQueue();
        //mImageLoader = new ImageLoader(mRequestQueue, new MemoryCache());
    }

    public static synchronized VolleySingleton getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new VolleySingleton(context);
        }
        return mInstance;
    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            // getApplicationContext() is key, it keeps you from leaking the
            // Activity or BroadcastReceiver if someone passes one in.
            mRequestQueue = Volley.newRequestQueue(mContext.getApplicationContext());
        }
        return mRequestQueue;
    }

    public <T> void addToRequestQueue(Request<T> req) {
        getRequestQueue().add(req);
    }

    public void serverRequest(final Context context, final String userData, final String httpOverride, final VolleyCallBack callBack, String Url) {
        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e(TAG,"*** response success" + response);
                        callBack.userDatarespone(response);
                        Toast.makeText(context,"user registered as customer",Toast.LENGTH_LONG).show();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //Log.e(TAG,"*** response failure " + error.networkResponse.statusCode + " headers:" + error.networkResponse.headers);
                callBack.errorResponse(error);
            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String>  params = new HashMap<String, String>();
                params.put("Accept", "application/json");
                params.put("Accept-Language", "en-US, en;q=0.8");
                params.put("Content-Type","application/json");
                params.put("X-HTTP-Method-Override",httpOverride);

                return params;
            }

            @Override
            public byte[] getBody() throws AuthFailureError {
                Log.e(TAG, "Customer Registration DaterrorResonsea:" + userData);
                byte[] body = new byte[0];
                try {
                    body = userData.getBytes("UTF-8");
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

        VolleySingleton.getInstance(context).addToRequestQueue(stringRequest);
    }

    public interface VolleyCallBack {
        public void userDatarespone(String response);
        public void errorResponse(VolleyError error);
    }
}