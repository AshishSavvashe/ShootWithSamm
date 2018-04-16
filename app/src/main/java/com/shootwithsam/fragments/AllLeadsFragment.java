package com.shootwithsam.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.shootwithsam.APIURLS;
import com.shootwithsam.R;
import com.shootwithsam.activities.LeadInfoActivity;
import com.shootwithsam.activities.MainActivity;
import com.shootwithsam.adapters.CustomLeadAdapter;
import com.shootwithsam.models.Pojo_LeadDetails;
import com.shootwithsam.models.Pojo_Notification;
import com.shootwithsam.utils.NetworkCheckUtility;
import com.shootwithsam.utils.SamSharedPreferences;
import com.shootwithsam.utils.SecuredNetworkUtility;
import com.shootwithsam.utils.ShowToastMessage;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class AllLeadsFragment extends Fragment {

    private static final String TAG = "AllLeadsFragment";
    private Context mContext;
    private ProgressBar mProgressBar;
    private EditText mEditTextLeadSearch;
    private ListView mListViewAllLeads;
    private View convertView;
    private String strFranchisee_id = "";
    private String strlead_id = "";
    private int REQUEST_CODE = 1000;
    private ArrayList<Pojo_LeadDetails> arrayListLeadDetails = new ArrayList<>();
    private ArrayList<Pojo_LeadDetails> filterListLead = new ArrayList<>();

    public AllLeadsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = null;
        try {
            mContext = getActivity();
            getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            //  setTitle
            ((MainActivity) mContext).getSupportActionBar().setTitle(mContext.getResources().getString(R.string.strAllLeadLabel));

            JSONObject user_info = SamSharedPreferences.getUserInfo(mContext);
            if (user_info != null) {
                strFranchisee_id = user_info.getString("franchisee_id");
            }
            // Inflate the layout for this fragment
            view = inflater.inflate(R.layout.fragment_all_leads, container, false);

            mListViewAllLeads = (ListView) view.findViewById(R.id.listViewAllLeads);
            mEditTextLeadSearch = (EditText) view.findViewById(R.id.editTextLeadSearch);
            mProgressBar = (ProgressBar) view.findViewById(R.id.progressBarLoad);

            GetLeadList();

            if (NetworkCheckUtility.isNetworkConnectionAvailable(mContext)) {
                //  Get All Lead details
                new GetLeadListAsyncTask().execute();
            } else {
                ShowToastMessage.noInternetToast(mContext);
            }

            mEditTextLeadSearch.setText("");
            mEditTextLeadSearch.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                }
                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    String strSearch = mEditTextLeadSearch.getText().toString().trim();
                    UpdateLeadList();
                }

                @Override
                public void afterTextChanged(Editable editable) {
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
        // return convertView;
        return view;
    }

    private void UpdateLeadList() {
        try {
            //Search details
            String strSearch = mEditTextLeadSearch.getText().toString().trim().toLowerCase();
            if (!strSearch.equalsIgnoreCase("")) {
                arrayListLeadDetails = new ArrayList<>();

                for (Pojo_LeadDetails leadDetails : filterListLead) {
                    if (leadDetails.getStrCustomerName().toLowerCase().contains(strSearch)
                            || leadDetails.getStrFranchiseeOwner().toLowerCase().contains(strSearch)
                            || leadDetails.getStrLeadDescription().toLowerCase().contains(strSearch)
                            || leadDetails.getStrCity().toLowerCase().contains(strSearch)
                            || leadDetails.getStrMobileNo().toLowerCase().contains(strSearch)
                            || leadDetails.getStrPhotoshootType().toLowerCase().contains(strSearch)) {
                            arrayListLeadDetails.add(leadDetails);
                    }
                }

            } else {
                arrayListLeadDetails = new ArrayList<>();
                arrayListLeadDetails.addAll(filterListLead);
            }
            //Search Result Not found Toast
            if(arrayListLeadDetails.size()==0) {
                ShowToastMessage.showToast(mContext, "No Record Found");
            }

            //  setAdapter
            CustomLeadAdapter adapter = new CustomLeadAdapter(mContext, R.layout.layout_all_lead_row, arrayListLeadDetails);
            mListViewAllLeads.setAdapter(adapter);
            adapter.notifyDataSetChanged();

            mListViewAllLeads.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Pojo_LeadDetails leadDetails = arrayListLeadDetails.get(position);
                    //  Start Lead Details Fragment and see other details
                    Bundle data = new Bundle();
                    data.putSerializable("lead_details", leadDetails);

                    Intent intent = new Intent(getActivity(), LeadInfoActivity.class);
                    intent.putExtra("data", leadDetails);
                    startActivity(intent);
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        GetLeadList();
    }

    private void GetLeadList() {
        if (NetworkCheckUtility.isNetworkConnectionAvailable(mContext))
            new GetLeadListAsyncTask().execute();
        else
            ShowToastMessage.noInternetToast(mContext);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            if (requestCode == REQUEST_CODE && resultCode == Activity.RESULT_OK) {
                GetLeadList();
            }
        } catch (Exception e) {
            e.printStackTrace();

        }
    }

    //Async Task for get Lead list
    private class GetLeadListAsyncTask extends AsyncTask<String, String, String> {
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
                data.put("action", APIURLS.str_allLeads);
                data.put("franchisee_id", strFranchisee_id);
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
                    if (result.has("success") && result.optBoolean("success")) {

                        arrayListLeadDetails = new ArrayList<>();
                        JSONArray data = result.optJSONArray("details");
                        if (data.length() > 0) {
                            for (int index = 0; index < data.length(); index++) {
                                JSONObject jsonObject = data.optJSONObject(index);
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


                                    Pojo_LeadDetails leadDetails = new Pojo_LeadDetails();

                                    leadDetails.setStrLeadId(strLead_id);
                                    leadDetails.setStrLeadNo(strLead_no);
//                                    leadDetails.setStrLeadDescription(strLead_description + " " + "...");
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
//                                    leadDetails.setStrPaidAmount(mContext.getResources().getString(R.string.strRupeeLabel) + " " + strPaid_amount);
                                    leadDetails.setStrPaidAmount(strPaid_amount);
                                    leadDetails.setStrFranchiseeId(strFranchisee_id);
                                    leadDetails.setStrLeadStatus(strLead_status);
                                    leadDetails.setStrLeadStage(strLead_stage);
                                    leadDetails.setStrCustomerFeedback(strCustomer_feedback);
                                    leadDetails.setStrSpecialInstruction(strSpecialInstruction);
                                    leadDetails.setStrRating(strRating);
                                    leadDetails.setStrInsertDate(strInsert_date);
                                    leadDetails.setStrInsert_by(strInsert_by);
                                    leadDetails.setStrUpdateDate(strUpdate_date);
                                    leadDetails.setStrUpdateBy(strUpdate_by);
                                    leadDetails.setStrPhotoshootType(strPhotoshoot_type);
                                    leadDetails.setStrCity(strCity);
                                    leadDetails.setStrFranchiseeOwner(strFranchisee_owner);
                                    leadDetails.setStrAcceptedDeclined(strAccepted_declined);

                                    arrayListLeadDetails.add(leadDetails);
                                }
                            }


                        }

                    } else {
                        ShowToastMessage.noDataToast(mContext);
                    }
                    filterListLead = new ArrayList<>();
                    filterListLead.addAll(arrayListLeadDetails);
                    UpdateLeadList();
                } else
                    ShowToastMessage.showToast(mContext, "Something goes wrong. Try again later");

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }
}
