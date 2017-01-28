package com.amigosphire_poc.demo.activities;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.support.v7.app.AlertDialog;
import android.widget.EditText;

import com.amigosphire_poc.R;
import com.amigosphire_poc.demo.adapter.MyWhiteListAdapter;
import com.amigosphire_poc.demo.adapter.WhiteListHolder;
import com.amigosphire_poc.demo.utils.ServicePoints;
import com.amigosphire_poc.demo.utils.SimpleDividerItemDecoration;
import com.amigosphire_poc.demo.utils.VolleySingleton;
import com.android.volley.Request;
import com.android.volley.VolleyError;

import java.util.ArrayList;
import java.util.List;

public class WhiteListActivity extends AppCompatActivity {

    List<WhiteListHolder> mWhiteList = new ArrayList<>();
    private RecyclerView recyclerView;
    private MyWhiteListAdapter mAdapter;

    private static String HTTP_OVERRIDE_WHITELIST_GET = "WHITELIST_GET";
    private static String HTTP_OVERRIDE_WHITELIST_POST = "WHITELIST_POST";
    private static final String TAG = "WhiteListActivity";
    public static final String MyPREFERENCES = "MyPrefs" ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_white_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showUserWhiteListDialog();
            }
        });

        recyclerView = (RecyclerView) findViewById(R.id.whitelist_recycler_view);
        mAdapter = new MyWhiteListAdapter(mWhiteList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.addItemDecoration(new SimpleDividerItemDecoration(getResources()));
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);

        fetchWhiteList();
    }

    private void showUserWhiteListDialog() {
        LayoutInflater layoutInflater = LayoutInflater.from(this);
        final View view = layoutInflater.inflate(R.layout.add_user_to_whitelist, null);

        AlertDialog.Builder dialogbuilder = new AlertDialog.Builder(this);
        dialogbuilder.setTitle("Add user to whitelist");
        dialogbuilder.setView(view);
        dialogbuilder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                EditText userName = (EditText) view.findViewById(R.id.enter_whitelistname);
                EditText userPhoneNumber = (EditText) view.findViewById(R.id.enter_whitelistnumber);

                WhiteListHolder wl = new WhiteListHolder(userName.getText().toString(),userPhoneNumber.getText().toString());
                mWhiteList.add(wl);

                mAdapter.notifyDataSetChanged();

                String whiteListData = getWhiteListData(wl);
                addToWhiteList(whiteListData);
            }
        });
        dialogbuilder.create();

        dialogbuilder.show();
    }

    void fetchWhiteList() {
        VolleySingleton.getInstance(this).serverRequest(this, getProviderNumber(), HTTP_OVERRIDE_WHITELIST_GET,
            new VolleySingleton.VolleyCallBack() {
                @Override
                public void userDatarespone(String response) {
                    Log.d(TAG," user data:" + response);
                    formatString(response);
                }

                @Override
                public void errorResponse(VolleyError error) {
                    Log.e(TAG," user data:" + "*** error response");
                }
            }, ServicePoints.WHITE_LIST_GET, Request.Method.POST);
    }

    void addToWhiteList(String whiteListJson) {
        VolleySingleton.getInstance(this).serverRequest(this, whiteListJson, HTTP_OVERRIDE_WHITELIST_POST,
            new VolleySingleton.VolleyCallBack() {
                @Override
                public void userDatarespone(String response) {
                    Log.d(TAG," user data:" + response);
                    formatString(response);
                }

                @Override
                public void errorResponse(VolleyError error) {
                    Log.e(TAG," user data:" + "*** error response");
                }
            }, ServicePoints.WHITE_LIST_POST, Request.Method.POST);
    }

    private String getWhiteListData(WhiteListHolder whiteListHolder) {

        StringBuilder sb = new StringBuilder();
        sb.append("{");

        sb.append("\"" + "prov_phone_no" + "\":");

        SharedPreferences sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        String phoneNumber = sharedpreferences.getString("pv_number","");
        sb.append("\"" + phoneNumber + "\",");

        sb.append("\"" + "whitelist_phone_no" + "\":");
        sb.append("\"" + whiteListHolder.getPhoneNumber() + "\",");

        sb.append("\"" + "whitelist_name" + "\":");
        sb.append("\"" + whiteListHolder.getName() + "\"");

        sb.append("}");

        return sb.toString();
    }

    private String getProviderNumber() {
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        sb.append("\"" + "prov_phone_no" + "\":");

        SharedPreferences sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        String phoneNumber = sharedpreferences.getString("pv_number","");
        sb.append("\"" + phoneNumber + "\"");

        sb.append("}");

        return sb.toString();
    }

    private void formatString(String response) {
        String[] parts = response.split(",");

        String[] sb;
        for(int i = 1; i < parts.length; i++) {
            sb = parts[i].split(":");
            String userPhoneNumber = sb[1].substring(1, sb[1].length()-1);
            sb = parts[++i].split(":");
            String userName = sb[1].substring(1, sb[1].length()-2);

            WhiteListHolder wl = new WhiteListHolder(userName,userPhoneNumber);
            mWhiteList.add(wl);

            ++i;
        }
        mAdapter.notifyDataSetChanged();
    }
}
