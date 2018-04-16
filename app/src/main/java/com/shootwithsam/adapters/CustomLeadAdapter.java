package com.shootwithsam.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.shootwithsam.R;
import com.shootwithsam.models.Pojo_LeadDetails;

import java.util.ArrayList;

/**
 * Created by SAI on 8/22/2017.
 */

public class CustomLeadAdapter extends BaseAdapter {

    private Context mContext;
    private int resourceId;
    private ArrayList<Pojo_LeadDetails> arrayListLeadDetails;

    public CustomLeadAdapter(Context context, int resource, ArrayList<Pojo_LeadDetails> arrayList) {
        //     super(context, resource, arrayList);
        mContext = context;
        resourceId = resource;
        arrayListLeadDetails = arrayList;
    }

    @Override
    public int getCount() {
        return arrayListLeadDetails.size();
    }

    @Override
    public Pojo_LeadDetails getItem(int position) {
        return arrayListLeadDetails.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @NonNull
    @Override
    public View getView(int position, View view, ViewGroup parent) {
        try {
            view = LayoutInflater.from(mContext).inflate(resourceId, parent, false);

            final TextView textViewCustomerName = (TextView) view.findViewById(R.id.textViewCustomerName);
            TextView textViewFranchiseeName = (TextView) view.findViewById(R.id.textViewFranchiseeName);
            TextView textViewLeadDescription = (TextView) view.findViewById(R.id.textViewLeadDescription);
            TextView textViewMobileNo = (TextView) view.findViewById(R.id.textViewMobileNo);
            TextView textViewPhotoshootType = (TextView) view.findViewById(R.id.textViewPhotoshootType);
            TextView textViewPhotoshootCity = (TextView) view.findViewById(R.id.textViewPhotoshootCity);
            TextView textViewprice = (TextView) view.findViewById(R.id.textViewPrice);

            Pojo_LeadDetails dueLeadDetails = arrayListLeadDetails.get(position);

            textViewCustomerName.setText(dueLeadDetails.getStrCustomerName());
            textViewFranchiseeName.setText(dueLeadDetails.getStrFranchiseeOwner());
            textViewLeadDescription.setText(dueLeadDetails.getStrLeadDescription());
            textViewMobileNo.setText(dueLeadDetails.getStrMobileNo());
            textViewPhotoshootType.setText(dueLeadDetails.getStrPhotoshootType());
            textViewPhotoshootCity.setText(dueLeadDetails.getStrCity());
            textViewprice.setText(mContext.getResources().getString(R.string.strRupeeLabel) + " " + dueLeadDetails.getStrPaidAmount());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return view;
    }
}
