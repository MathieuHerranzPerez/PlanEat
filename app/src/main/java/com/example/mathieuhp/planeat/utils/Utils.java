package com.example.mathieuhp.planeat.utils;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AlertDialog;

import com.example.mathieuhp.planeat.R;

public class Utils {

    public static boolean isInteger(String str) {
        if(str == null || str.trim().isEmpty()) {
            return false;
        }
        Boolean res = true;
        for (int i = 0; i < str.length(); ++i) {
            if(!Character.isDigit(str.charAt(i))) {
                res = false;
            }
        }
        return res;
    }

    /**
     * Check if the user is connected to Internet
     * @param context
     * @return boolean
     */
    public static boolean isConnected(Context context) {

        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netinfo = cm.getActiveNetworkInfo();

        if (netinfo != null && netinfo.isConnectedOrConnecting()) {
            android.net.NetworkInfo wifi = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            android.net.NetworkInfo mobile = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

            if((mobile != null && mobile.isConnectedOrConnecting()) || (wifi != null && wifi.isConnectedOrConnecting()))
                return true;
            else
                return false;
        }
        else
            return false;
    }

    public static AlertDialog.Builder buildDialog(Context c, final Activity a) {

        AlertDialog.Builder builder = new AlertDialog.Builder(c);
        builder.setTitle(c.getResources().getString(R.string.no_internet));
        builder.setMessage(c.getResources().getString(R.string.message_no_internet));

        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                a.finish();
            }
        });

        return builder;
    }
}
