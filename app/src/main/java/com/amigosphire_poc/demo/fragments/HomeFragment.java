package com.amigosphire_poc.demo.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.amigosphire_poc.R;
import com.amigosphire_poc.demo.utils.ServicePoints;
import com.amigosphire_poc.demo.utils.VolleySingleton;
import com.android.volley.VolleyError;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link HomeFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class HomeFragment extends Fragment {
    private static final String TAG = "HomeFragment";

    private static final String ARG_PARAM1 = "phone_number";

    private static final String HTTP_OVERRIDE_PROV_HOME = "PROV_HOME_GET";

    private OnFragmentInteractionListener mListener;
    private String mProviderNumber;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        serverRequest();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_home, container, false);

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
            mProviderNumber = mListener.onFragmentInteraction();
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        String onFragmentInteraction();
    }

    void serverRequest() {
        VolleySingleton.getInstance(getActivity()).serverRequest(getActivity(), getPhoneNumber(), HTTP_OVERRIDE_PROV_HOME,
        new VolleySingleton.VolleyCallBack() {
            @Override
            public void userDatarespone(String response) {
                Toast.makeText(getActivity(),"logged in successfully, got user data",Toast.LENGTH_LONG).show();
                Log.d(TAG," user data:" + response);
            }

            @Override
            public void errorResponse(VolleyError error) {
                Log.e(TAG," user data:" + "*** error response");
            }
        }, ServicePoints.PROV_HOME);
    }

    String getPhoneNumber() {
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        sb.append("\"" + "prov_phone_no" + "\":");
        sb.append("\"" + mProviderNumber + "\"");
        sb.append("}");

        Log.e(TAG," getPhoneNumber:" + sb.toString());
        return sb.toString();
    }
}
