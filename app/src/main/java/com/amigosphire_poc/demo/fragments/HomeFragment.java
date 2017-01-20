package com.amigosphire_poc.demo.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Switch;
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
public class HomeFragment extends Fragment implements View.OnClickListener{
    private static final String TAG = "HomeFragment";

    private static final String ARG_PARAM1 = "phone_number";

    private static final String HTTP_OVERRIDE_PROV_HOME = "PROV_HOME_GET";
    private static final String HTTP_OVERRIDE_PROV_HOME_POST = "PROV_HOME_POST";

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

        Switch statusSwitch = (Switch) view.findViewById(R.id.availability_switch);
        statusSwitch.setOnClickListener(this);

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

    @Override
    public void onClick(View v) {

        switch(v.getId()) {
            case R.id.availability_switch:
                VolleySingleton.getInstance(getActivity()).serverRequest(getActivity(), getUserData(2,v.getRootView()), HTTP_OVERRIDE_PROV_HOME_POST,
                        new VolleySingleton.VolleyCallBack() {
                            @Override
                            public void userDatarespone(String response) {
                                Toast.makeText(getActivity(),"user status available",Toast.LENGTH_LONG).show();
                            }

                            @Override
                            public void errorResponse(VolleyError error) {
                                Toast.makeText(getActivity(),"user status available error response",Toast.LENGTH_LONG).show();
                            }
                        }, ServicePoints.PROV_HOME_POST);
                break;
        }

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
        String onFragmentInteraction();
    }

    void serverRequest() {
        VolleySingleton.getInstance(getActivity()).serverRequest(getActivity(), getUserData(1, null), HTTP_OVERRIDE_PROV_HOME,
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

    String getUserData(final int id, final View view) {
        StringBuilder sb = new StringBuilder();
        sb.append("{");

        switch(id) {
            case 1:
                sb.append("\"" + "prov_phone_no" + "\":");
                sb.append("\"" + mProviderNumber + "\"");
                break;

            case 2:
                Switch status = (Switch) view.findViewById(R.id.availability_switch);
                String available = status.isChecked()?"A":"X";
                sb.append("\"" + "prov_phone_no" + "\":");
                sb.append("\"" + mProviderNumber + "\",");
                sb.append("\"" + "status" + "\":");
                sb.append("\"" + available + "\"");
                break;

            case 3:
                break;

            case 4:
                break;

            case 5:
                break;
        }
        sb.append("}");

        Log.e(TAG," getUserData:" + sb.toString());
        return sb.toString();
    }
}