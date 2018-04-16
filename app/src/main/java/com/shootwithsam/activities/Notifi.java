package com.shootwithsam.activities;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.shootwithsam.APIURLS;
import com.shootwithsam.R;
import com.shootwithsam.adapters.CustomExpandableListAdapter;
import com.shootwithsam.models.Pojo_LeadDetails;
import com.shootwithsam.models.Pojo_Notification;
import com.shootwithsam.utils.NetworkCheckUtility;
import com.shootwithsam.utils.SamSharedPreferences;
import com.shootwithsam.utils.SecuredNetworkUtility;
import com.shootwithsam.utils.ShowToastMessage;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Notifi extends AppCompatActivity {
    private Context mContext;
    ExpandableListView expandableListView;
    ExpandableListAdapter expandableListAdapter;
    List<String> expandableListTitle;
    HashMap<String, List<Pojo_Notification>> expandableListDetail;
    private ArrayList<Pojo_Notification> arrayListNotification;
    private Pojo_LeadDetails leadDetails;
    private ProgressBar mProgressBar;

    private String strFranchiseeId = "";
    private String strUserId = "";
    private String strLeadId = "";
    String[] Notification;
    String strNotification = "";
    private String strIsSeen = "1";
    private String strIsUnseen = "0";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifi);
        try {
            mContext = Notifi.this;

            //Array String
            Notification = getResources().getStringArray(R.array.Notification);
            //set title
            ActionBar toolbar = getSupportActionBar();
            toolbar.setTitle(getString(R.string.strNotificationLabel));
            toolbar.setDisplayHomeAsUpEnabled(true);

            JSONObject user_info = SamSharedPreferences.getUserInfo(mContext);
            if (user_info != null) {
                strFranchiseeId = user_info.getString("franchisee_id");
                strUserId = user_info.getString("user_id");
            }
            expandableListView = (ExpandableListView) findViewById(R.id.expandableListView);
            mProgressBar = (ProgressBar) findViewById(R.id.progressBarLoad);

            expandableListTitle = new ArrayList<String>();
            expandableListTitle.add("Unseen");
            expandableListTitle.add("Seen");

//            expandableListAdapter = new CustomExpandableListAdapter(this, expandableListTitle, expandableListDetail);
//            expandableListView.setAdapter(expandableListAdapter);

            if (NetworkCheckUtility.isNetworkConnectionAvailable(mContext)) {
                //  Notification AsyncTask
                new NotificationAsyncTask().execute();
            } else {
                ShowToastMessage.noInternetToast(mContext);
            }
            expandableListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {

                @Override
                public void onGroupExpand(int groupPosition) {
                    Toast.makeText(getApplicationContext(),
                            expandableListTitle.get(groupPosition) + " List Expanded.",
                            Toast.LENGTH_SHORT).show();
                }
            });

            expandableListView.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {

                @Override
                public void onGroupCollapse(int groupPosition) {
                    Toast.makeText(getApplicationContext(),
                            expandableListTitle.get(groupPosition) + " List Collapsed.",
                            Toast.LENGTH_SHORT).show();

                }
            });

            expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
                @Override
                public boolean onChildClick(ExpandableListView parent, View v,
                                            int groupPosition, int childPosition, long id) {
                    Toast.makeText(
                            getApplicationContext(),
                            expandableListTitle.get(groupPosition)
                                    + " -> "
                                    + expandableListDetail.get(
                                    expandableListTitle.get(groupPosition)).get(
                                    childPosition), Toast.LENGTH_SHORT
                    ).show();
                    return false;
                }
            });

            // strNotification = Notification[pos].toLowerCase();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    private class NotificationAsyncTask extends AsyncTask<String, String, String> {
        String response = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                JSONObject data = new JSONObject();
                data.put("action", APIURLS.str_all_notification);
                data.put("franchisee_id", strFranchiseeId);
                response = SecuredNetworkUtility.sendPostData(APIURLS.str_base_url, data);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return response;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try {
                if (mProgressBar.getVisibility() == View.VISIBLE)
                    mProgressBar.setVisibility(View.GONE);

                if (s != null) {

                    JSONObject result = new JSONObject(s);
                    Log.d("data", String.valueOf(result));
                    if (result.has("success") && result.optBoolean("success")) {

                        arrayListNotification = new ArrayList<>();
                        JSONArray data = result.optJSONArray("details");
                        if (data.length() > 0) {
                            for (int index = 0; index < data.length(); index++) {
                                JSONObject jsonObject = data.optJSONObject(index);
                                if (jsonObject.length() > 0) {

                                    String strNotification_id = jsonObject.optString("notification_id", "").trim();
                                    String strLead_id = jsonObject.optString("lead_id", "").trim();
                                    String strFranchisee_id = jsonObject.optString("franchisee_id", "").trim();
                                    String strNotification = jsonObject.optString("notification", "").trim();
                                    String strDatetime = jsonObject.optString("datetime", "").trim();
                                    String strIs_seen = jsonObject.optString("is_seen", "").trim();

                                    Pojo_Notification allNotification = new Pojo_Notification();

                                    allNotification.setStrLeadId(strLead_id);
                                    allNotification.setStrNotificationId(strNotification_id);
                                    allNotification.setStrFranchiseeId(strFranchisee_id);
                                    allNotification.setStrNotification(strNotification);
                                    allNotification.setStrDateTime(strDatetime);
                                    allNotification.setStrIsSeen(strIs_seen);

                                    arrayListNotification.add(allNotification);
                                }
                            }
                            if (arrayListNotification.size() > 0) {
                                expandableListDetail = ExpandableListData.getData(arrayListNotification);
                                expandableListAdapter = new CustomExpandableListAdapter(mContext, expandableListTitle, expandableListDetail);
                                expandableListView.setAdapter(expandableListAdapter);
                            } else
                                ShowToastMessage.noDataToast(mContext);
                        }

                    } else {
                        ShowToastMessage.noDataToast(mContext);
                    }
                } else
                    ShowToastMessage.showToast(mContext, "Something goes wrong. Try again later");

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }
}
