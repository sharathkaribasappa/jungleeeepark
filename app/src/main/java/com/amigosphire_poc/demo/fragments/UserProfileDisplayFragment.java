package com.amigosphire_poc.demo.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.amigosphire_poc.R;
import com.amigosphire_poc.demo.utils.ServicePoints;
import com.amigosphire_poc.demo.utils.VolleySingleton;
import com.android.volley.VolleyError;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link UserProfileDisplayFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class UserProfileDisplayFragment extends Fragment implements View.OnClickListener {
    private static final String TAG = "UPDF";
    private static final String HTTP_OVERRIDE_CONSULTANT = "PROVREG";

    private static final String ARG_PARAM1 = "userdata";
    private static final String ARG_PARAM2 = "pwd";

    private String mUserData;
    private String mPwd;

    private OnFragmentInteractionListener mListener;

    public UserProfileDisplayFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mUserData = getArguments().getString(ARG_PARAM1);
            mPwd = getArguments().getString(ARG_PARAM2);
            Log.d(TAG, "userdata:" + mUserData);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_user_profile_display, container, false);

        setupCitySpinner(rootView);
        setupAreaSpinner(rootView);
        setupProfessionSpinner(rootView);
        setupSpecializationSpinner(rootView);

        Button createAccount = (Button) rootView.findViewById(R.id.consultant_create_account);
        createAccount.setOnClickListener(this);

        return rootView;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
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
        VolleySingleton.getInstance(getActivity()).serverRequest(getActivity(), getRegistrationData(v.getRootView()), HTTP_OVERRIDE_CONSULTANT,
            new VolleySingleton.VolleyCallBack() {
                @Override
                public void userDatarespone(String response) {
                    Toast.makeText(getActivity(),"user registered as consultant",Toast.LENGTH_LONG).show();
                }

                @Override
                public void errorResponse(VolleyError error) {
                    Toast.makeText(getActivity(),"user registration failed",Toast.LENGTH_LONG).show();
                }
            }, ServicePoints.CONSULTANT_REGISTER);
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
        void onFragmentInteraction(Uri uri);
    }

    void setupCitySpinner(View view) {
        // Spinner element
        Spinner spinner = (Spinner) view.findViewById(R.id.city);

        // Spinner click listener
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        // Spinner Drop down elements
        List<String> categories = new ArrayList<String>();
        categories.add("Bangalore");
        categories.add("Mysore");
        categories.add("Chennai");
        categories.add("Mumbai");
        categories.add("Hyderabad");

        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getActivity(), R.layout.spinner_item, categories);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        spinner.setAdapter(dataAdapter);
        spinner.setSelection(0);
    }

    void setupAreaSpinner(View view) {
        // Spinner element
        Spinner spinner = (Spinner) view.findViewById(R.id.area);

        // Spinner click listener
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        // Spinner Drop down elements
        List<String> categories = new ArrayList<String>();
        categories.add("JP Nagar");
        categories.add("RT Nagar");
        categories.add("RajajiNagar");
        categories.add("Hebbal");
        categories.add("Mosque Road");

        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getActivity(), R.layout.spinner_item, categories);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        spinner.setAdapter(dataAdapter);
        spinner.setSelection(0);
    }

    void setupProfessionSpinner(View view) {
        // Spinner element
        Spinner spinner = (Spinner) view.findViewById(R.id.profession);

        // Spinner click listener
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        // Spinner Drop down elements
        List<String> categories = new ArrayList<String>();
        categories.add("Doctor");
        categories.add("Lawyer");

        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getActivity(), R.layout.spinner_item, categories);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        spinner.setAdapter(dataAdapter);
        spinner.setSelection(0);
    }

    void setupSpecializationSpinner(View view) {
        // Spinner element
        Spinner spinner = (Spinner) view.findViewById(R.id.specialization);

        // Spinner click listener
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        // Spinner Drop down elements
        List<String> categories = new ArrayList<String>();
        categories.add("Dentist");
        categories.add("Pediatrician");
        categories.add("General Physician");

        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getActivity(), R.layout.spinner_item, categories);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        spinner.setAdapter(dataAdapter);
        spinner.setSelection(0);
    }

    private String getRegistrationData(View view) {
        EditText editText;
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(mUserData.replace("rqstr_phone_no","prov_phone_no"));

        stringBuilder.append("\"profession\":");
        Spinner professionSpinner = (Spinner) view.findViewById(R.id.profession);
        stringBuilder.append("\""+ professionSpinner.getSelectedItem() + "\",");

        stringBuilder.append("\"city\":");
        Spinner citySpinner = (Spinner) view.findViewById(R.id.city);
        stringBuilder.append("\""+ citySpinner.getSelectedItem() + "\",");

        stringBuilder.append("\"area\":");
        Spinner areaSpinner = (Spinner) view.findViewById(R.id.area);
        stringBuilder.append("\""+ areaSpinner.getSelectedItem() + "\",");

        stringBuilder.append("\"specialization\":");
        Spinner specializationSpinner = (Spinner) view.findViewById(R.id.specialization);
        stringBuilder.append("\""+ specializationSpinner.getSelectedItem() + "\",");

        stringBuilder.append("\"highest_certification\":");
        editText = (EditText) view.findViewById(R.id.qualification);
        stringBuilder.append("\""+ editText.getText().toString() + "\",");

        stringBuilder.append("\"office\":");
        editText = (EditText) view.findViewById(R.id.workplace_name);
        stringBuilder.append("\""+ editText.getText().toString() + "\",");

        stringBuilder.append("\"photo1\":" + null + ",");
        stringBuilder.append("\"description\":" + null + ",");

        stringBuilder.append("\"pass\":" + "\"" + mPwd + "\"");

        stringBuilder.append("}");

        Log.d(TAG,"sb:" + stringBuilder);

        return stringBuilder.toString();
    }
}