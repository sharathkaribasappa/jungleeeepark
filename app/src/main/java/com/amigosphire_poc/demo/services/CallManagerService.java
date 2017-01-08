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

import org.w3c.dom.Text;

public class CallManagerService extends Service implements View.OnClickListener {
    private Context mContext;
    private TelephonyManager mTelephonyManager;

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

                    showDialog(R.layout.caller_info);
                    break;

                case TelephonyManager.CALL_STATE_OFFHOOK:
                    Toast.makeText(mContext,
                            "CALL_STATE_OFFHOOK: ",
                            Toast.LENGTH_LONG).show();
                    break;

                case TelephonyManager.CALL_STATE_IDLE:
                    if(mLastState == TelephonyManager.CALL_STATE_OFFHOOK) {
                        showDialog(R.layout.caller_chargedetails);
                    }
                    break;
            }
            mLastState = state;
        }
    }

    void showDialog(int layoutID) {
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

}