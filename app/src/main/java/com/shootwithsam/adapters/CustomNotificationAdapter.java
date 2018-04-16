package com.shootwithsam.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.shootwithsam.R;
import com.shootwithsam.models.Pojo_LeadDetails;
import com.shootwithsam.models.Pojo_Notification;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.StringTokenizer;

/**
 * Created by SAI on 9/11/2017.
 */

public class CustomNotificationAdapter extends BaseAdapter{

    private Context mContext;
    private int resourceId;
    private ArrayList<Pojo_Notification> arrayListNotification;
    private String[] months;

    public CustomNotificationAdapter(Context context, int resource, ArrayList<Pojo_Notification> arrayList) {
        //     super(context, resource, arrayList);
        mContext = context;
        resourceId = resource;
        arrayListNotification = arrayList;
        months = mContext.getResources().getStringArray(R.array.arrayMonths);
    }
    @Override
    public int getCount() {
        return arrayListNotification.size();
    }

    @Override
    public Pojo_Notification getItem(int position) {
        return arrayListNotification.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        try {
            view = LayoutInflater.from(mContext).inflate(resourceId, parent, false);

            final TextView textViewNotification = (TextView) view.findViewById(R.id.textViewNotification);
            TextView textViewDateTime= (TextView) view.findViewById(R.id.textViewDate);

            Pojo_Notification dueNotificationDetails = arrayListNotification.get(position);

            String strNewDay = "";
            String strNewMonth = "";
            String strNewYear = "";

            StringTokenizer tokenizer = new StringTokenizer(dueNotificationDetails.getStrDateTime(), " ");
            String strFromDate = tokenizer.nextToken();
         //   String strTime = tokenizer.nextToken();

            tokenizer = new StringTokenizer(strFromDate, "-");
            strNewYear = tokenizer.nextToken();
            strNewMonth = months[Integer.parseInt(tokenizer.nextToken()) - 1].substring(0, 3);
            strNewDay = tokenizer.nextToken();
            strFromDate = strNewDay + "-" + strNewMonth + "-" + strNewYear;

            textViewNotification.setText(dueNotificationDetails.getStrNotification());
           // textViewDateTime.setText(getDateTime(dueNotificationDetails.getStrDateTime()));
            textViewDateTime.setText(strFromDate);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return view;
    }
    public String getDateTime(String strDateTime){
        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH);
        Date date = new Date();
        return format.format(date)+"" ;
    }
}
