package com.shootwithsam.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

/**
 * Created by SwatiK on 20-08-2017.
 */
public class NetworkCheckUtility {
    public static boolean isNetworkConnectionAvailable(Context obj_context) {
        boolean isWifiConnected = false;
        boolean isMobileDataConnected = false;

        if (obj_context == null) {
            Log.e("Network", "context is null");
        } else {

            ConnectivityManager cm = (ConnectivityManager) obj_context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo[] netInfo = cm.getAllNetworkInfo();
            for (NetworkInfo ni : netInfo) {
                if (ni.getTypeName().equalsIgnoreCase("WIFI"))
                    if (ni.isConnected())
                        isWifiConnected = true;
                if (ni.getTypeName().equalsIgnoreCase("MOBILE"))
                    if (ni.isConnected())
                        isMobileDataConnected = true;
            }
        }
        return isWifiConnected || isMobileDataConnected;

    }
}
