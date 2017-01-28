package com.amigosphire_poc.demo.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.amigosphire_poc.R;
import com.amigosphire_poc.demo.utils.ServicePoints;
import com.amigosphire_poc.demo.utils.VolleySingleton;
import com.android.volley.Request;
import com.android.volley.VolleyError;

/**
 *
 */
public class UserProfileEditFragment extends Fragment implements View.OnClickListener{
    private static final String TAG = "UserProfileEditFragment";

    private static final String HTTP_OVERRIDE_CUSTOMER = "RQSTREG";
    public static final String MyPREFERENCES = "MyPrefs" ;

    Button mCreateAccountButton;
    Button mNext;
    private String mGender = "";

    private OnFragmentInteractionListener mListener;

    public UserProfileEditFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.content_registration_consultant, container, false);

        mCreateAccountButton = (Button) view.findViewById(R.id.create_account);
        mCreateAccountButton.setOnClickListener(this);

        RadioButton consultant = (RadioButton) view.findViewById(R.id.user_consultant);
        consultant.setOnClickListener(this);
        RadioButton customer = (RadioButton) view.findViewById(R.id.user_customer);
        customer.setOnClickListener(this);

        RadioButton user_male = (RadioButton) view.findViewById(R.id.user_male);
        user_male.setOnClickListener(this);

        RadioButton user_female = (RadioButton) view.findViewById(R.id.user_female);
        user_female.setOnClickListener(this);

        mNext = (Button) view.findViewById(R.id.next);
        mNext.setOnClickListener(this);

        return view;
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
        void onFragmentInteraction(Uri uri, String password);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.user_consultant:
                mNext.setVisibility(View.VISIBLE);
                mCreateAccountButton.setVisibility(View.GONE);
                break;

            case R.id.user_customer:
                mNext.setVisibility(View.GONE);
                mCreateAccountButton.setVisibility(View.VISIBLE);
                break;

            case R.id.user_male:
                mGender = "m";
                break;

            case R.id.user_female:
                mGender = "f";
                break;

            case R.id.create_account:
                VolleySingleton.getInstance(getActivity()).serverRequest(getActivity(), getRegistrationData(v.getRootView()), HTTP_OVERRIDE_CUSTOMER,
                    new VolleySingleton.VolleyCallBack() {
                        @Override
                        public void userDatarespone(String response) {
                            Toast.makeText(getActivity(),"user registered as customer",Toast.LENGTH_LONG).show();
                        }

                        @Override
                        public void errorResponse(VolleyError error) {

                        }
                    }, ServicePoints.CUSTOMER_REGISTER, Request.Method.POST);
                break;

            case R.id.next:
                mListener.onFragmentInteraction(Uri.parse(getUserData(v.getRootView())), getPassword(v.getRootView()));
                break;
        }
    }

    private String getRegistrationData(View view) {
        EditText editText;
        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append("{");

        stringBuilder.append("\"rqstr_phone_no\":");
        editText = (EditText) view.findViewById(R.id.phone_number);
        stringBuilder.append("\"+91"+ editText.getText().toString() + "\",");

        stringBuilder.append("\"email\":");
        editText = (EditText) view.findViewById(R.id.email_id);
        stringBuilder.append("\""+ editText.getText().toString() + "\",");

        stringBuilder.append("\"first_name\":");
        editText = (EditText) view.findViewById(R.id.first_name);
        stringBuilder.append("\""+ editText.getText().toString() + "\",");

        stringBuilder.append("\"last_name\":");
        editText = (EditText) view.findViewById(R.id.last_name);
        stringBuilder.append("\""+ editText.getText().toString() + "\",");

        stringBuilder.append("\"dob\":");
        editText = (EditText) view.findViewById(R.id.dob);
        stringBuilder.append("\""+ editText.getText().toString() + "\",");

        stringBuilder.append("\"gender\":" + "\"" + mGender + "\",");

        stringBuilder.append("\"city\":" + null + ",");
        stringBuilder.append("\"area\":" + null + ",");
        stringBuilder.append("\"location\":" + null + ",");
        stringBuilder.append("\"photo1\":" + null + ",");

        stringBuilder.append("\"pass\":");
        editText = (EditText) view.findViewById(R.id.confirm_password);
        stringBuilder.append("\""+ editText.getText().toString() + "\"");

        stringBuilder.append("}");

        return stringBuilder.toString();
    }

    private String getUserData(View view) {
        EditText editText;
        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append("{");

        stringBuilder.append("\"rqstr_phone_no\":");
        editText = (EditText) view.findViewById(R.id.phone_number);
        stringBuilder.append("\"+91"+ editText.getText().toString() + "\",");

        setProviderNumberinSP("+91" + editText.getText().toString());

        stringBuilder.append("\"email\":");
        editText = (EditText) view.findViewById(R.id.email_id);
        stringBuilder.append("\""+ editText.getText().toString() + "\",");

        stringBuilder.append("\"first_name\":");
        editText = (EditText) view.findViewById(R.id.first_name);
        stringBuilder.append("\""+ editText.getText().toString() + "\",");

        stringBuilder.append("\"last_name\":");
        editText = (EditText) view.findViewById(R.id.last_name);
        stringBuilder.append("\""+ editText.getText().toString() + "\",");

        stringBuilder.append("\"dob\":");
        editText = (EditText) view.findViewById(R.id.dob);
        stringBuilder.append("\""+ editText.getText().toString() + "\",");

        stringBuilder.append("\"gender\":" + "\"" + mGender + "\",");

        return stringBuilder.toString();
    }

    private String getPassword(View view) {
        EditText editText;
        editText = (EditText) view.findViewById(R.id.confirm_password);
        return editText.getText().toString();
    }

    private void setProviderNumberinSP(String phoneNumber) {
        SharedPreferences sharedpreferences = getActivity().getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();

        editor.putString("pv_number", phoneNumber);
        editor.commit();
    }
}
