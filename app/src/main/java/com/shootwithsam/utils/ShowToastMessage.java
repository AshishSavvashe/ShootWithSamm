package com.shootwithsam.utils;

import android.content.Context;
import android.view.Gravity;
import android.widget.Toast;

import com.shootwithsam.R;

/**
 * Created by SwatiK on 20-08-2017.
 */
public class ShowToastMessage {
    public static void noInternetToast(Context mContext) {
//        String strNoInternet = mContext.getResources().getString(R.string.strNoInternetMessage);
        Toast.makeText(mContext, mContext.getResources().getString(R.string.strNoInternetMessage), Toast.LENGTH_SHORT).show();
    }

    public static void showToast(Context mContext, String strMessage) {
        Toast toast = Toast.makeText(mContext, strMessage, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }

    public static void noDataToast(Context mContext) {
        Toast.makeText(mContext, mContext.getResources().getString(R.string.strNoDataFound), Toast.LENGTH_SHORT).show();
    }
}
