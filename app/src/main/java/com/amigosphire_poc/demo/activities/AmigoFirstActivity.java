package com.amigosphire_poc.demo.activities;

import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.amigosphire_poc.R;
import com.amigosphire_poc.demo.services.CallManagerService;

public class AmigoFirstActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_amigo_first);

        Intent intent = new Intent(this, CallManagerService.class);
        startService(intent);

        if(!checkForActiveSession()) {
            Handler handler = new Handler();
            handler.postDelayed((new Runnable() {
                @Override
                public void run() {
                    //showDialog();
                    doAnimation();
                }
            }), 4000);
        } else {

        }
    }

    private boolean checkForActiveSession() {
        return false;
    }

    private void doAnimation() {
        Button signUp = (Button) findViewById(R.id.signupbutton);
        signUp.setVisibility(View.VISIBLE);
        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AmigoFirstActivity.this,RegistrationActivity.class);
                startActivity(intent);
            }
        });

        Button signIn = (Button) findViewById(R.id.signinbutton);
        signIn.setVisibility(View.VISIBLE);
        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AmigoFirstActivity.this,LoginActivity.class);
                startActivity(intent);

                //showDialog();

            }
        });
    }

    void showDialog() {
        Toast.makeText(getBaseContext(),"onCreate", Toast.LENGTH_LONG).show();
        WindowManager.LayoutParams params = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_APPLICATION_PANEL,
                0,
                PixelFormat.TRANSLUCENT);
        params.gravity = Gravity.VERTICAL_GRAVITY_MASK | Gravity.TOP;
        params.setTitle("Load Average");

        final WindowManager wm = (WindowManager) getSystemService(WINDOW_SERVICE);

        final View view = View.inflate(this.getApplicationContext(), R.layout.caller_info, null);
        //ImageView cancelCall = (ImageView) view.findViewById(R.id.canceldialog);
        //cancelCall.setOnClickListener(this);

        wm.addView(view, params);
    }
}
