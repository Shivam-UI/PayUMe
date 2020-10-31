package com.lgt.paykredit.extras;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.lgt.paykredit.R;

/**
 * Created by Ranjan on 3/17/2020.
 */
public class Common {

    private final Context _context;
    public static String play_store_url="https://play.google.com/store/apps/details?id=";
    public static String address_key="address_key";
    public static String GST_NO_key="GST_NO_key";
    public static String LANGUAGE_SESSION="LANGUAGE_SESSION";
    public static String LANGUAGE="LANGUAGE";
    public static String HINDI="HI";
    public static String ENGLISH="EN";
    public static String FROM_MAIN="MAIN_ACTIIVTY";
    public static String FROM_STARTING="FROM_STARTING";
    public static String USER_NAME="USER_NAME";
    public static String INVOICE_ID="INVOICE_ID";
    public static String CUSTOMER_ADDED="CUSTOMER_ADDED";
    public static String PRODUCT_ADDED="PRODUCT_ADDED";

    // invoice common


    public Common(Context context) {
        this._context = context;
    }

    public boolean isConnectingToInternet() {
        ConnectivityManager connectivity = (ConnectivityManager) _context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null)
                for (NetworkInfo anInfo : info)
                    if (anInfo.getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }

        }
        return false;
    }

    public void showSnackBar(View view, String message) {

        final Snackbar snackbar = Snackbar.make(view, message, Snackbar.LENGTH_INDEFINITE).setDuration(3000);
        View snackbarView = snackbar.getView();
        TextView txtv = snackbarView.findViewById(com.google.android.material.R.id.snackbar_text);
        snackbarView.setBackgroundColor(_context.getResources().getColor(R.color.colorPrimaryDark));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1)
            txtv.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        else
            txtv.setGravity(Gravity.CENTER_HORIZONTAL);
        snackbar.show();

    }

    public void showSnackBarWithTime(View view, String message, int snackTime) {

        final Snackbar snackbar = Snackbar.make(view, message, Snackbar.LENGTH_INDEFINITE).setDuration(snackTime);
        View snackbarView = snackbar.getView();
        TextView txtv = snackbarView.findViewById(com.google.android.material.R.id.snackbar_text);
        snackbarView.setBackgroundColor(_context.getResources().getColor(R.color.colorPrimaryDark));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1)
            txtv.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        else
            txtv.setGravity(Gravity.CENTER_HORIZONTAL);
        snackbar.show();

    }

    public void addMoneySnack(View view, String message, int snackTime) {

        Snackbar snack = Snackbar.make(view, "Your wallet balance is low please add", Snackbar.LENGTH_LONG);
        View aaa = snack.getView();

        TextView txtv = aaa.findViewById(com.google.android.material.R.id.snackbar_text);
        aaa.setBackgroundColor(_context.getResources().getColor(R.color.colorPrimaryDark));
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) view.getLayoutParams();
        params.gravity = Gravity.BOTTOM;
        view.setLayoutParams(params);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1)
            txtv.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        else
            txtv.setGravity(Gravity.CENTER_HORIZONTAL);

        snack.show();

    }
    public static  boolean checkInternetConnection(Context context) {

        //Check internet connection:
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        //Means that we are connected to a network (mobile or wi-fi)
       boolean connected = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED;

        return connected;
    }
    public static void saveLanguage(Context context,String language){
        SharedPreferences pref=context.getSharedPreferences(LANGUAGE_SESSION,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=pref.edit();
        editor.putString(LANGUAGE,language);
        editor.apply();
        editor.commit();
        // session manager
    }
    public static String getLanguage(Context context){
        SharedPreferences pref=context.getSharedPreferences(LANGUAGE_SESSION,Context.MODE_PRIVATE);
        if (!pref.contains(LANGUAGE)){
            return null;
        }
        return pref.getString(LANGUAGE,"");
    }

}

