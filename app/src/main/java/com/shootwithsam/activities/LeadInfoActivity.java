package com.shootwithsam.activities;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.shootwithsam.APIURLS;
import com.shootwithsam.R;
import com.shootwithsam.models.Pojo_LeadDetails;
import com.shootwithsam.models.Pojo_Notification;
import com.shootwithsam.utils.NetworkCheckUtility;
import com.shootwithsam.utils.SamSharedPreferences;
import com.shootwithsam.utils.SecuredNetworkUtility;
import com.shootwithsam.utils.ShowToastMessage;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.StringTokenizer;

public class LeadInfoActivity extends AppCompatActivity {

    private final String TAG = "LeadInfoActivity";

    private String[] Months;
    private Context mContext;

    private Pojo_LeadDetails leadDetails;

    private ProgressBar mProgressBar;
    private LinearLayout mLayoutBtns;
    private TextView mTextViewCustomerName;
    private TextView mTextViewFranchiseName;
    private TextView mTextViewLeadDescription;
    private TextView mTextViewLeadDate;
    private TextView mTextViewMobileNo;
    private TextView mTextViewEmailId;
    private TextView mTextViewPhotoshootType;
    private TextView mTextViewPhotoshootCity;
    private TextView mTextViewAddress;
    private TextView mTextViewPhotoshootFromDate;
    private TextView mTextViewPhotoshootFromTo;
    private TextView mTextViewSpecialInstruction;
    private TextView mTextViewPrice;
    private Button mBtnAccept;
    private Button mBtnReject;
    private String strFranchisee_id = "";
    private String strUser_Id = "";
    private String strLead_id = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lead_info);

        try {
            mContext = LeadInfoActivity.this;

            //Months String Array
            Months = getResources().getStringArray(R.array.arrayMonths);

            //set title
            ActionBar toolbar = getSupportActionBar();
            toolbar.setTitle(getString(R.string.strLeadDetailsLabel));
            toolbar.setDisplayHomeAsUpEnabled(true);

            leadDetails = (Pojo_LeadDetails) getIntent().getSerializableExtra("data");

            mProgressBar = (ProgressBar) findViewById(R.id.progressBarLoad);
            mLayoutBtns = (LinearLayout) findViewById(R.id.layoutLeadInfoBtns);
            mTextViewCustomerName = (TextView) findViewById(R.id.textViewCustomerName);
            mTextViewFranchiseName = (TextView) findViewById(R.id.textViewFranchiseeName);
            mTextViewLeadDescription = (TextView) findViewById(R.id.textViewLeadDescription);
            mTextViewLeadDate = (TextView) findViewById(R.id.textViewLeadDate);
            mTextViewMobileNo = (TextView) findViewById(R.id.textViewMobileNo);
            mTextViewEmailId = (TextView) findViewById(R.id.textViewEmailId);
            mTextViewPhotoshootType = (TextView) findViewById(R.id.textViewPhotoshootType);
            mTextViewPhotoshootCity = (TextView) findViewById(R.id.textViewPhotoshootCity);
            mTextViewAddress = (TextView) findViewById(R.id.textViewAddress);
            mTextViewPhotoshootFromDate = (TextView) findViewById(R.id.textViewPhotoshootFromDate);
            mTextViewPhotoshootFromTo = (TextView) findViewById(R.id.textViewPhotoshootFromTo);
            mTextViewSpecialInstruction=(TextView)findViewById(R.id.textViewSpecialInstruction);
            mTextViewPrice = (TextView) findViewById(R.id.textViewPrice);
            mBtnAccept = (Button) findViewById(R.id.btnAccept);
            mBtnReject = (Button) findViewById(R.id.btnReject);

            JSONObject user_info = SamSharedPreferences.getUserInfo(mContext);
            if (user_info != null) {
                try {
                    strFranchisee_id = user_info.getString("franchisee_id");
                    strUser_Id = user_info.getString("user_id");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            if (leadDetails == null) {
                Toast.makeText(mContext, "No Data Found", Toast.LENGTH_LONG).show();
            } else {

                // strAction = getIntent().getStringExtra("lead_details");
                strLead_id = leadDetails.getStrLeadId();

                mTextViewCustomerName.setText(leadDetails.getStrCustomerName());
                mTextViewFranchiseName.setText(leadDetails.getStrFranchiseeOwner());
                mTextViewLeadDescription.setText(leadDetails.getStrLeadDescription());
                mTextViewLeadDate.setText(GetLeadDate(leadDetails.getStrLeadDate()));
                mTextViewMobileNo.setText(leadDetails.getStrMobileNo());
                mTextViewEmailId.setText(leadDetails.getStrEmailId());
                mTextViewPhotoshootType.setText(leadDetails.getStrPhotoshootType());
                mTextViewPhotoshootCity.setText(leadDetails.getStrCity());
                mTextViewAddress.setText(leadDetails.getStrAddressLine1());
                mTextViewPhotoshootFromDate.setText(GetFromDate(leadDetails.getStrPhotoshootFromDate()));
                mTextViewPhotoshootFromTo.setText(GetEndDate(leadDetails.getStrPhotoshootToDate()));
                mTextViewSpecialInstruction.setText(leadDetails.getStrSpecialInstruction());
                mTextViewPrice.setText(mContext.getResources().getString(R.string.strRupeeLabel) + " "+leadDetails.getStrPaidAmount());

                if(leadDetails.getStrSpecialInstruction().equalsIgnoreCase("null")
                    || leadDetails.getStrSpecialInstruction().equalsIgnoreCase(""))
                    mTextViewSpecialInstruction.setText("-");
                   else{
                    mTextViewSpecialInstruction.setText(leadDetails.getStrSpecialInstruction());
                }

                if (leadDetails.getStrAcceptedDeclined().equalsIgnoreCase("null")
                        || leadDetails.getStrAcceptedDeclined().equalsIgnoreCase("")
                        || leadDetails.getStrAcceptedDeclined() == null ) {
                    mLayoutBtns.setVisibility(View.GONE);
                } else {
                    if(strFranchisee_id.equalsIgnoreCase(leadDetails.getStrFranchiseeId())){
                        mLayoutBtns.setVisibility(View.VISIBLE);
                    }else
                        mLayoutBtns.setVisibility(View.GONE);
                }

                mBtnAccept.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (NetworkCheckUtility.isNetworkConnectionAvailable(mContext))
                            new AcceptAsyncTask().execute();
                        else
                            ShowToastMessage.noInternetToast(mContext);

                    }
                });
                mBtnReject.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        new RejectAsyncTask().execute();
                    }
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
    //Change Date format
    private String GetLeadDate(String strMonths) {
        String strLeadDate = leadDetails.getStrLeadDate();

        String strNewDay = "";
        String strNewMonth = "";
        String strNewYear = "";

        StringTokenizer tokenizer = new StringTokenizer(strLeadDate, "-");
        strNewYear = tokenizer.nextToken();
        strNewMonth = Months[Integer.parseInt(tokenizer.nextToken()) - 1].substring(0, 3);
        strNewDay = tokenizer.nextToken();
        strLeadDate = strNewDay + "-" + strNewMonth + "-" + strNewYear;

        return strLeadDate;
    }
    private String GetFromDate(String strMonths) {

        String strFromDate = leadDetails.getStrPhotoshootFromDate();

        String strNewDay = "";
        String strNewMonth = "";
        String strNewYear = "";

        StringTokenizer tokenizer = new StringTokenizer(strFromDate, "-");
        strNewYear = tokenizer.nextToken();
        strNewMonth = Months[Integer.parseInt(tokenizer.nextToken()) - 1].substring(0, 3);
        strNewDay = tokenizer.nextToken();
        strFromDate = strNewDay + "-" + strNewMonth + "-" + strNewYear;

        return strFromDate;
    }
    private String GetEndDate(String strMonths) {

        String strEndDate = leadDetails.getStrPhotoshootToDate();

        String strNewDay = "";
        String strNewMonth = "";
        String strNewYear = "";

        StringTokenizer tokenizer = new StringTokenizer(strEndDate, "-");
        strNewYear = tokenizer.nextToken();
        strNewMonth = Months[Integer.parseInt(tokenizer.nextToken()) - 1].substring(0, 3);
        strNewDay = tokenizer.nextToken();
        strEndDate = strNewDay + "-" + strNewMonth + "-" + strNewYear;

        return strEndDate;
    }

    //Async Task for Accept Lead
    private class AcceptAsyncTask extends AsyncTask<String, String, String> {
        String response = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressBar.setVisibility(View.VISIBLE);
            mLayoutBtns.setVisibility(View.GONE);
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                JSONObject data = new JSONObject();
                data.put("action", APIURLS.str_LeadAccetance);
                data.put("franchisee_id", strFranchisee_id);
                data.put("lead_id", strLead_id);
                data.put("user_id", strUser_Id);
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
                mProgressBar.setVisibility(View.GONE);
                mLayoutBtns.setVisibility(View.VISIBLE);

                if (s != null) {
                    JSONObject result = new JSONObject(s);
//                    if (result.has("success") && result.optBoolean("success")) {
//                    } else
//                        ShowToastMessage.showToast(mContext, "Lead Accepted");
                    if (result.has("success") && result.optBoolean("success")) {
                        ShowToastMessage.showToast(mContext, "Lead Accepted");
                        finish();
                    } else
                        ShowToastMessage.showToast(mContext, "Failed to accept lead. Try again later");
                } else
                    ShowToastMessage.showToast(mContext, "Something goes wrong. Try again later");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    //Async Task for Reject Lead
    private class RejectAsyncTask extends AsyncTask<String, String, String> {
        String response = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressBar.setVisibility(View.VISIBLE);
            mLayoutBtns.setVisibility(View.GONE);
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                JSONObject data = new JSONObject();
                data.put("action", APIURLS.str_LeadDecline);
                data.put("franchisee_id", strFranchisee_id);
                data.put("lead_id", strLead_id);
                data.put("user_id", strUser_Id);
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
                mProgressBar.setVisibility(View.GONE);
                mLayoutBtns.setVisibility(View.VISIBLE);

                if (s != null) {
                    JSONObject result = new JSONObject(s);
//                    if (result.has("success") && result.optBoolean("success")) {
//                    } else
//                        ShowToastMessage.showToast(mContext, "Lead Rejected");
                    if (result.has("success") && result.optBoolean("success")) {
                        ShowToastMessage.showToast(mContext, "Lead Rejected");
                        finish();
                    } else
                        ShowToastMessage.showToast(mContext, "Failed to reject lead. Try again later");
                } else
                    ShowToastMessage.showToast(mContext, "Something goes wrong. Try again later");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }
}
