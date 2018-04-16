package com.shootwithsam.activities;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.shootwithsam.APIURLS;
import com.shootwithsam.R;
import com.shootwithsam.adapters.CustomExpandableListAdapter;
import com.shootwithsam.fragments.AllLeadsFragment;
import com.shootwithsam.models.Pojo_LeadDetails;
import com.shootwithsam.models.Pojo_Notification;
import com.shootwithsam.utils.NetworkCheckUtility;
import com.shootwithsam.utils.SamSharedPreferences;
import com.shootwithsam.utils.SecuredNetworkUtility;
import com.shootwithsam.utils.ShowToastMessage;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.StringTokenizer;

import static com.shootwithsam.R.id.expandableListView;

public class LeadDetailsActivity extends AppCompatActivity {
    private final String TAG = "LeadDetailsActivity";

    private String[] Months;
    private Context mContext;

    private Pojo_Notification notificationDetails;

    private ProgressBar mProgressBar;
    private TextView mTextViewCustomerName;
    private TextView mTextViewLeadId;
    private TextView mTextViewLeadNo;
    private TextView mTextViewLeadDescription;
    private TextView mTextViewLeadDate;
    private TextView mTextViewMobileNo;
    private TextView mTextViewPhotoshootId;
    private TextView mTextViewLeadSource;
    private TextView mTextViewPhotoshootFromDate;
    private TextView mTextViewPhotoshootFromTo;

    private String strFranchisee_id = "";
    private String strUser_Id = "";
    private String strLead_id = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lead_details);
        try {
            mContext = LeadDetailsActivity.this;

            //Months String Array
            Months = getResources().getStringArray(R.array.arrayMonths);

            //set title
            ActionBar toolbar = getSupportActionBar();
            toolbar.setTitle(getString(R.string.strLeadDetailsLabel));
            toolbar.setDisplayHomeAsUpEnabled(true);

           notificationDetails = (Pojo_Notification) getIntent().getSerializableExtra("data");

            mProgressBar = (ProgressBar) findViewById(R.id.progressBarLoad);
            mTextViewCustomerName = (TextView) findViewById(R.id.textViewCustomerName);
            mTextViewLeadId = (TextView) findViewById(R.id.textViewLeadId);
            mTextViewLeadNo = (TextView) findViewById(R.id.textViewLeadNo);
            mTextViewLeadDescription = (TextView) findViewById(R.id.textViewLeadDescription);
            mTextViewLeadDate = (TextView) findViewById(R.id.textViewLeadDate);
            mTextViewMobileNo = (TextView) findViewById(R.id.textViewMobileNo);
            mTextViewPhotoshootId = (TextView) findViewById(R.id.textViewPhotoshootId);
            mTextViewLeadSource = (TextView) findViewById(R.id.textViewLeadSource);
            mTextViewPhotoshootFromDate = (TextView) findViewById(R.id.textViewPhotoshootFromDate);
            mTextViewPhotoshootFromTo = (TextView) findViewById(R.id.textViewPhotoshootFromTo);

            JSONObject user_info = SamSharedPreferences.getUserInfo(mContext);
            if (user_info != null) {
                try {
                    strFranchisee_id = user_info.getString("franchisee_id");
                    strUser_Id = user_info.getString("user_id");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            if (NetworkCheckUtility.isNetworkConnectionAvailable(mContext))
                new LeadDetailsAsyncTask().execute();
            else
                ShowToastMessage.noInternetToast(mContext);

            strLead_id=notificationDetails.getStrLeadId();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
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
  /*  private String GetDMYDate(String strMonths) {

        String strLeadDate = leadDetails.getStrLeadDate();
        String strFromDate = leadDetails.getStrPhotoshootFromDate();
        String strEndDate = leadDetails.getStrPhotoshootToDate();

        String strNewDay = "";
        String strNewMonth = "";
        String strNewYear = "";

        StringTokenizer tokenizer = new StringTokenizer(strFromDate, "-");
        strNewYear = tokenizer.nextToken();
        strNewMonth = Months[Integer.parseInt(tokenizer.nextToken()) - 1].substring(0, 3);
        strNewDay = tokenizer.nextToken();
        strFromDate = strNewDay + "-" + strNewMonth + "-" + strNewYear;

        tokenizer = new StringTokenizer(strEndDate, "-");
        strNewYear = tokenizer.nextToken();
        strNewMonth = Months[Integer.parseInt(tokenizer.nextToken()) - 1].substring(0, 3);
        strNewDay = tokenizer.nextToken();
        strEndDate = strNewDay + "-" + strNewMonth + "-" + strNewYear;

        tokenizer = new StringTokenizer(strLeadDate, "-");
        strNewYear = tokenizer.nextToken();
        strNewMonth = Months[Integer.parseInt(tokenizer.nextToken()) - 1].substring(0, 3);
        strNewDay = tokenizer.nextToken();
        strLeadDate = strNewDay + "-" + strNewMonth + "-" + strNewYear;

        return strFromDate;
    }
    */
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
                data.put("lead_id", strLead_id);
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
                            for (int index = 0; index < data.length(); index++) {
                                JSONObject jsonObject = data.optJSONObject(index);
                                if (jsonObject.length() > 0) {

                                    String strLeadId = jsonObject.optString("lead_id", "").trim();
                                    String strLeadNo = jsonObject.optString("lead_no", "").trim();
                                    String strCustomerName = jsonObject.optString("customer_name", "").trim();
                                    String strLeadDescription = jsonObject.optString("lead_description", "").trim();
                                    String strLeadDate = jsonObject.optString("lead_date", "").trim();
                                    String strLeadSource = jsonObject.optString("lead_source", "").trim();
                                    String strPhotoshootFromDate = jsonObject.optString("photoshoot_from_date", "").trim();
                                    String strPhotoshootToDate = jsonObject.optString("photoshoot_to_date", "").trim();
                                    String strPhotoshootId = jsonObject.optString("photoshoot_type_id", "").trim();
                                    String strMobile = jsonObject.optString("mobile_no", "").trim();

                                    mTextViewLeadId.setText(strLeadId);
                                    mTextViewLeadNo.setText(strLeadNo);
                                    mTextViewCustomerName.setText(strCustomerName);
                                    mTextViewLeadDescription.setText(strLeadDescription);
                                    mTextViewLeadDate.setText(GetDMYDate(strLeadDate));
                                    mTextViewLeadSource.setText(strLeadSource);
                                    mTextViewPhotoshootFromDate.setText(GetDMYDate(strPhotoshootFromDate));
                                    mTextViewPhotoshootFromTo.setText(GetDMYDate(strPhotoshootToDate));
                                    mTextViewPhotoshootId.setText(strPhotoshootId);
                                    mTextViewMobileNo.setText(strMobile);

                                }
                            }
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
