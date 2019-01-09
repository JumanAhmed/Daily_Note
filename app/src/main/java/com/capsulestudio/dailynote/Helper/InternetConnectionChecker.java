package com.capsulestudio.dailynote.Helper;

import android.content.Context;
import android.net.ConnectivityManager;

/**
 * Created by Juman on 10/23/2017.
 */

public class InternetConnectionChecker {
    /** CHECK WHETHER INTERNET CONNECTION IS AVAILABLE OR NOT */
    public static boolean checkConnection(Context context){
        return  ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo() != null;
    }

//    protected boolean isOnline(Context context) {
//        ConnectivityManager cm = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
//        NetworkInfo netInfo = cm.getActiveNetworkInfo();
//        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
//            return true;
//        } else {
//            return false;
//        }
//    }
}
