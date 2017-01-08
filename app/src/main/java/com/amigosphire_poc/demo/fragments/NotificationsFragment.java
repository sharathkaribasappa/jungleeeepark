package com.amigosphire_poc.demo.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.amigosphire_poc.R;
import com.amigosphire_poc.demo.utils.NotificationAdapter;
import com.amigosphire_poc.demo.utils.Notifications;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link NotificationsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link NotificationsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NotificationsFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    private List<Notifications> notificationsList = new ArrayList<>();
    private RecyclerView recyclerView;
    private NotificationAdapter mNotificationsAdapter;

    public NotificationsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment NotificationsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static NotificationsFragment newInstance(String param1, String param2) {
        NotificationsFragment fragment = new NotificationsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_notifications, container, false);

        recyclerView = (RecyclerView) view.findViewById(R.id.notifications_listview);
        mNotificationsAdapter = new NotificationAdapter(notificationsList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this.getActivity().getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mNotificationsAdapter);

        prepareNotificationsList();

        return view;
    }

    private void prepareNotificationsList() {
        Notifications notification1 = new Notifications("health check camp","14/01/2017");
        notificationsList.add(notification1);

        Notifications notification2 = new Notifications("eye checkup camp","24/03/2017");
        notificationsList.add(notification2);

        Notifications notification3 = new Notifications("blood donation drive","25/04/2017");
        notificationsList.add(notification3);

        Notifications notification4 = new Notifications("blood donation drive","28/07/2017");
        notificationsList.add(notification4);

        Notifications notification12 = new Notifications("blood donation drive","28/07/2017");
        notificationsList.add(notification12);

        Notifications notification5 = new Notifications("blood donation drive","28/07/2017");
        notificationsList.add(notification5);

        Notifications notification6 = new Notifications("blood donation drive","28/07/2017");
        notificationsList.add(notification6);

        Notifications notification7 = new Notifications("blood donation drive","28/07/2017");
        notificationsList.add(notification7);

        Notifications notification8 = new Notifications("blood donation drive","28/07/2017");
        notificationsList.add(notification8);

        Notifications notification9 = new Notifications("blood donation drive","28/07/2017");
        notificationsList.add(notification9);

        Notifications notification10 = new Notifications("blood donation drive","28/07/2017");
        notificationsList.add(notification10);

        Notifications notification11 = new Notifications("blood donation drive","28/07/2017");
        notificationsList.add(notification11);

        mNotificationsAdapter.notifyDataSetChanged();
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
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
        void onFragmentInteraction(Uri uri);
    }
}
