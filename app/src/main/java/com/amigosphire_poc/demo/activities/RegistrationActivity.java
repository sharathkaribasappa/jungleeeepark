package com.amigosphire_poc.demo.activities;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.amigosphire_poc.R;
import com.amigosphire_poc.demo.fragments.UserProfileDisplayFragment;
import com.amigosphire_poc.demo.fragments.UserProfileEditFragment;

public class RegistrationActivity extends AppCompatActivity implements UserProfileEditFragment.OnFragmentInteractionListener, UserProfileDisplayFragment.OnFragmentInteractionListener {

    private String TAG = "RegistrationActivity";

    private UserProfileEditFragment mUserEditFragment;
    private UserProfileDisplayFragment mUserProfileDisplayFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration_consultant);

        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        mUserEditFragment = new UserProfileEditFragment();
        fragmentTransaction.add(R.id.fragment_container,mUserEditFragment,"editor");
        fragmentTransaction.commit();
    }

    @Override
    public void onFragmentInteraction(Uri uri, String password) {
        Log.d(TAG,"uri.toString():" + uri.toString());

        Bundle data = new Bundle();
        data.putString("userdata",uri.toString());
        data.putString("pwd",password);

        mUserProfileDisplayFragment = new UserProfileDisplayFragment();
        mUserProfileDisplayFragment.setArguments(data);

        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        fragmentTransaction.replace(R.id.fragment_container, mUserProfileDisplayFragment, "display");
        fragmentTransaction.commit();
    }


    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}