package com.shootwithsam.fragments;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.shootwithsam.APIURLS;
import com.shootwithsam.R;
import com.shootwithsam.activities.LeadInfoActivity;
import com.shootwithsam.activities.MainActivity;
import com.shootwithsam.adapters.CustomExpandableListAdapter;
import com.shootwithsam.adapters.CustomLeadAdapter;
import com.shootwithsam.adapters.CustomNotificationAdapter;
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
import java.util.StringTokenizer;

/**
 * A simple {@link Fragment} subclass.
 */
public class NotificationFragment extends Fragment {
    private Context mContext;
    private String[] Months;

    private ListView mSeenList;
    ExpandableListView expandableListView;
    ExpandableListAdapter expandableListAdapter;
    List<String> expandableListTitle;
    private ArrayList<Pojo_Notification> arrayListNotification;
    HashMap<String, List<Pojo_Notification>> expandableListDetail = new HashMap<String, List<Pojo_Notification>>();
    private Pojo_LeadDetails leadDetails;
    List<Pojo_Notification> Unseen = new ArrayList<Pojo_Notification>();
    List<Pojo_Notification> Seen = new ArrayList<Pojo_Notification>();
    private ProgressBar mProgressBar;
    private int REQUEST_CODE = 1000;

    private String strFranchiseeId = "";
    private String strUserId = "";
    private String strLeadId = "";

    public NotificationFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = null;
        try {
            mContext = getActivity();
            getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            //Months String Array
            Months = getResources().getStringArray(R.array.arrayMonths);
            //  setTitle
            ((MainActivity) mContext).getSupportActionBar().setTitle(mContext.getResources().getString(R.string.strNotificationLabel));


            JSONObject user_info = SamSharedPreferences.getUserInfo(mContext);
            if (user_info != null) {
                strFranchiseeId = user_info.getString("franchisee_id");
                strUserId = user_info.getString("user_id");
            }

            // Inflate the layout for this fragment
            view = inflater.inflate(R.layout.fragment_notification, container, false);

           // expandableListView = (ExpandableListView) view.findViewById(R.id.expandableListView);
            mSeenList=(ListView)view.findViewById(R.id.seenListView);
            mProgressBar = (ProgressBar) view.findViewById(R.id.progressBarLoad);

           /* expandableListTitle = new ArrayList<String>();
            expandableListTitle.add("Unseen");
            expandableListTitle.add("Seen");*/


            if (NetworkCheckUtility.isNetworkConnectionAvailable(mContext)) {
                //  Notification AsyncTask
                new NotificationAsyncTask().execute();
            } else {
                ShowToastMessage.noInternetToast(mContext);
            }

            mSeenList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    strLeadId = arrayListNotification.get(position).getStrLeadId();
                    if (NetworkCheckUtility.isNetworkConnectionAvailable(mContext)) {
                        //  LeadDetails AsyncTask
                        new LeadDetailsAsyncTask().execute();
                    } else {
                        ShowToastMessage.noInternetToast(mContext);
                    }

                }
            });
        /*    expandableListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {

                @Override
                public void onGroupExpand(int groupPosition) {
                   *//* Toast.makeText(getActivity(),
                            expandableListTitle.get(groupPosition) + " List Expanded.",
                            Toast.LENGTH_SHORT).show();*//*

                }
            });
            expandableListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
                @Override
                public boolean onGroupClick(ExpandableListView expandableListView, View view, int i, long l) {
//                    expandableListView.expandGroup(i);
                    return false;
                }
            });

            expandableListView.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {

                @Override
                public void onGroupCollapse(int groupPosition) {
                   *//* Toast.makeText(getActivity(),
                            expandableListTitle.get(groupPosition) + " List Collapsed.",
                            Toast.LENGTH_SHORT).show();*//*

                }
            });

            expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
                @Override
                public boolean onChildClick(ExpandableListView parent, View v,
                                            int groupPosition, int childPosition, long id) {
                   *//* Toast.makeText(
                            getActivity(),
                            expandableListTitle.get(groupPosition)
                                    + " -> "
                                    + expandableListDetail.get(
                                    expandableListTitle.get(groupPosition)).get(
                                    childPosition), Toast.LENGTH_SHORT
                    ).show();*//*
                    if (groupPosition == 0) {
                        strLeadId = Unseen.get(childPosition).getStrLeadId();
                    } else if (groupPosition == 1) {
                        strLeadId = Seen.get(childPosition).getStrLeadId();
                    }

                    new LeadDetailsAsyncTask().execute();
                    return false;
                }
            });
*/
        } catch (Exception e) {
            e.printStackTrace();
        }
        return view;
    }
   // Function for Expandable data
/*
    private HashMap<String, List<Pojo_Notification>> UnseenSeenList() {

        Unseen = new ArrayList<>();
        Seen = new ArrayList<>();

        for (int index = 0; index < arrayListNotification.size(); index++) {
            Pojo_Notification data = arrayListNotification.get(index);
            if (data != null) {
                if (data.getStrIsSeen().equalsIgnoreCase("1"))
                    Unseen.add(data);
                else
                    Seen.add(data);
            }
        }

        if (Unseen.size() > 0) {
            Log.d("unseen", String.valueOf(Unseen));
        }
        if (Seen.size() > 0) {
            Log.d("Seen", String.valueOf(Seen));
        }

        expandableListDetail.put("Unseen", Unseen);
        expandableListDetail.put("Seen", Seen);
        return expandableListDetail;
    }
*/
   @Override
   public void onResume() {
       super.onResume();
       GetNotificationList();
   }

    private void GetNotificationList() {
        if (NetworkCheckUtility.isNetworkConnectionAvailable(mContext))
            new NotificationAsyncTask().execute();
        else
            ShowToastMessage.noInternetToast(mContext);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            if (requestCode == REQUEST_CODE && resultCode == Activity.RESULT_OK) {
                GetNotificationList();
            }
        } catch (Exception e) {
            e.printStackTrace();

        }
    }

    private String GetDMYDate(String strDate) {
        try {
            if (strDate.equalsIgnoreCase("")) {
                return "";
            } else {
                StringTokenizer tokenizer = new StringTokenizer(strDate, "-");
                String year = tokenizer.nextToken();
                String month = tokenizer.nextToken();
                String date = tokenizer.nextToken();

                return date + "-" + month + "-" + year;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    // Async Task for Notification data
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
                           /* if (arrayListNotification.size() > 0) {
                                //  expandableListDetail = ExpandableListData.getData(arrayListNotification);
                                expandableListDetail = new HashMap<>();
                                expandableListDetail = UnseenSeenList();
//                                expandableListDetail.put("Unseen", Unseen);
//                                expandableListDetail.put("Seen", Seen);
                                expandableListAdapter = new CustomExpandableListAdapter(mContext, expandableListTitle, expandableListDetail);
                                expandableListView.setAdapter(expandableListAdapter);
                                expandableListView.expandGroup(0);
                            } else
                                ShowToastMessage.noDataToast(mContext);*/

                             //Set Adapter
                                CustomNotificationAdapter adapter = new CustomNotificationAdapter(mContext, R.layout.list_item, arrayListNotification);
                                mSeenList.setAdapter(adapter);
                                adapter.notifyDataSetChanged();
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

    //Async Task for Notification Lead Details
    private class LeadDetailsAsyncTask extends AsyncTask<String, String, String> {
      /*  String response = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressBar.setVisibility(View.VISIBLE);
        }*/

        @Override
        protected String doInBackground(String... params) {
            String response = "";
            try {
                JSONObject data = new JSONObject();
                data.put("action", APIURLS.str_leadDetails);
                data.put("lead_id", strLeadId);
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
               /* if (mProgressBar.getVisibility() == View.VISIBLE)
                    mProgressBar.setVisibility(View.GONE);*/

                if (s != null) {

                    JSONObject result = new JSONObject(s);
                    Log.d("data", String.valueOf(result));
                    if (result.has("success") && result.optBoolean("success")) {

                        JSONArray data = result.optJSONArray("details");
                        if (data.length() > 0) {
                            // for (int index = 0; index < data.length(); index++) {
                            JSONObject jsonObject = data.optJSONObject(0);
                            if (jsonObject.length() > 0) {

                                String strLead_id = jsonObject.optString("lead_id", "").trim();
                                String strLead_no = jsonObject.optString("lead_no", "").trim();
                                String strLead_description = jsonObject.optString("lead_description", "").trim();
                                String strLead_date = jsonObject.optString("lead_date", "").trim();
                                String strLead_source = jsonObject.optString("lead_source", "").trim();
                                String strCustomer_name = jsonObject.optString("customer_name", "").trim();
                                String strPhotoshoot_from_date = jsonObject.optString("photoshoot_from_date", "").trim();
                                String strPhotoshoot_to_date = jsonObject.optString("photoshoot_to_date", "").trim();
                                String strPhotoshoot_type_id = jsonObject.optString("photoshoot_type_id", "").trim();
                                String strMobile_no = jsonObject.optString("mobile_no", "").trim();
                                String strEmail_id = jsonObject.optString("email_id", "").trim();
                                String strAddress_line1 = jsonObject.optString("address_line1", "").trim();
                                String strLandmark = jsonObject.optString("landmark", "").trim();
                                String strPincode = jsonObject.optString("pincode", "").trim();
                                String strState = jsonObject.optString("state", "").trim();
                                String strCountry = jsonObject.optString("country", "").trim();
                                String strPhotoshoot_city_id = jsonObject.optString("photoshoot_city_id", "").trim();
                                String strPaid_amount = jsonObject.optString("paid_amount", "").trim();
                                String strFranchisee_id = jsonObject.optString("franchisee_id", "").trim();
                                String strLead_status = jsonObject.optString("lead_status", "").trim();
                                String strLead_stage = jsonObject.optString("lead_stage", "").trim();
                                String strCustomer_feedback = jsonObject.optString("customer_feedback", "").trim();
                                String strSpecialInstruction= jsonObject.optString("special_instruction","").trim();
                                String strRating = jsonObject.optString("rating", "").trim();
                                String strInsert_date = jsonObject.optString("insert_date", "").trim();
                                String strInsert_by = jsonObject.optString("insert_by", "").trim();
                                String strUpdate_date = jsonObject.optString("update_date", "").trim();
                                String strUpdate_by = jsonObject.optString("update_by", "").trim();
                                String strPhotoshoot_type = jsonObject.optString("photoshoot_type", "").trim();
                                String strCity = jsonObject.optString("city", "").trim();
                                String strFranchisee_owner = jsonObject.optString("franchisee_owner", "").trim();
                                String strAccepted_declined = jsonObject.optString("accepted_declined", "").trim();

                                leadDetails = new Pojo_LeadDetails();

                                leadDetails.setStrLeadId(strLead_id);
                                leadDetails.setStrLeadNo(strLead_no);
                                leadDetails.setStrLeadDescription(strLead_description);
                                leadDetails.setStrLeadDate(strLead_date);
                                leadDetails.setStrLeadSource(strLead_source);
                                leadDetails.setStrCustomerName(strCustomer_name);
                                leadDetails.setStrPhotoshootFromDate(strPhotoshoot_from_date);
                                leadDetails.setStrPhotoshootToDate(strPhotoshoot_to_date);
                                leadDetails.setStrPhotoshootTypeId(strPhotoshoot_type_id);
                                leadDetails.setStrMobileNo(strMobile_no);
                                leadDetails.setStrEmailId(strEmail_id);
                                leadDetails.setStrAddressLine1(strAddress_line1);
                                leadDetails.setStrLandmark(strLandmark);
                                leadDetails.setStrPincode(strPincode);
                                leadDetails.setStrState(strState);
                                leadDetails.setStrCountry(strCountry);
                                leadDetails.setStrPhotoshootCityId(strPhotoshoot_city_id);
                                leadDetails.setStrPaidAmount(strPaid_amount);
                                leadDetails.setStrFranchiseeId(strFranchisee_id);
                                leadDetails.setStrLeadStatus(strLead_status);
                                leadDetails.setStrLeadStage(strLead_stage);
                                leadDetails.setStrSpecialInstruction(strSpecialInstruction);
                                leadDetails.setStrCustomerFeedback(strCustomer_feedback);
                                leadDetails.setStrRating(strRating);
                                leadDetails.setStrInsertDate(strInsert_date);
                                leadDetails.setStrInsert_by(strInsert_by);
                                leadDetails.setStrUpdateDate(strUpdate_date);
                                leadDetails.setStrUpdateBy(strUpdate_by);
                                leadDetails.setStrPhotoshootType(strPhotoshoot_type);
                                leadDetails.setStrCity(strCity);
                                leadDetails.setStrFranchiseeOwner(strFranchisee_owner);
                                leadDetails.setStrAcceptedDeclined(strAccepted_declined);
                            }
                        }
                        if (leadDetails != null) {
                            Intent intent = new Intent(getActivity(), LeadInfoActivity.class);
                            intent.putExtra("data", leadDetails);
                            startActivity(intent);
                        } else {
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
