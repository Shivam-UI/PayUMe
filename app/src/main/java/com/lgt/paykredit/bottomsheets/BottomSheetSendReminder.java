package com.lgt.paykredit.bottomsheets;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.lgt.paykredit.R;
import com.lgt.paykredit.extras.Common;
import com.lgt.paykredit.extras.Language;

import java.net.URLEncoder;
import java.util.Objects;


public class BottomSheetSendReminder extends BottomSheetDialogFragment {

    private LinearLayout sendReminderWhatsApp, llSendReminderUsingMessage;
    private String mMobile, mName, message;

    private TextView tvREminderName, tvReminderNumber, tv_send_reminder;
    private float mDueAmount = 0;

    public BottomSheetSendReminder() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.bottom_sheet_send_reminder, container, false);

        tvReminderNumber = view.findViewById(R.id.tvReminderNumber);
        tvREminderName = view.findViewById(R.id.tvREminderName);
        tv_send_reminder = view.findViewById(R.id.tv_send_reminder);
        llSendReminderUsingMessage = view.findViewById(R.id.llSendReminderUsingMessage);
        sendReminderWhatsApp = view.findViewById(R.id.sendReminderWhatsApp);

        Bundle bundle = getArguments();
        if (bundle != null) {
            if (bundle.containsKey("KEY_CUSTOMER_NUMBER")) {
                mMobile = bundle.getString("KEY_CUSTOMER_NUMBER");
                tvReminderNumber.setText(mMobile);
            }

            if (bundle.containsKey("KEY_CUSTOMER_NAME")) {
                mName = bundle.getString("KEY_CUSTOMER_NAME");
                tvREminderName.setText("Call " + mName);
            }

            if (bundle.containsKey("KEY_DUE_AMOUNT")) {
                mDueAmount = bundle.getFloat("KEY_DUE_AMOUNT");
            }

        }

        //message = "Hi, "+mName+" this is a reminder that you have "+mDueAmount+" Rs. due. Kindly pay as soon as possible to avoid any surcharges on it.";

        message = "Dear customer your due balance is Rs. " + mDueAmount + ". Please ignore it , if you have already paid. Install " +
                getString(R.string.app_name) + " app for free.\n" + Common.play_store_url + Objects.requireNonNull(getActivity()).getPackageName();

        sendReminderWhatsApp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideBottomSheet();
                boolean installed = appInstalledOrNot("com.whatsapp");
                if (installed) {
                    if (getActivity() != null) {
                        PackageManager packageManager = getActivity().getPackageManager();
                        Intent i = new Intent(Intent.ACTION_VIEW);
                        try {
                            //  Uri imageuri2=Uri.parse("android.resource://"+getActivity().getPackageName()+"/"+R.drawable.logo);
                            String url = "https://api.whatsapp.com/send?phone=" + "+91" + mMobile + "&text=" + URLEncoder.encode(message, "UTF-8");
                            i.setPackage("com.whatsapp");
                           /* Intent share = new Intent(Intent.ACTION_SEND);
                            share.setType("image/*");
                            share.putExtra(Intent.EXTRA_STREAM, imageuri2);*/


                            i.setData(Uri.parse(url));
                            if (i.resolveActivity(packageManager) != null) {
                                getActivity().startActivity(i);
                                // getActivity().startActivity(Intent.createChooser(share, "Share Image using"));

                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            Toast.makeText(getActivity(), "Please check some error occurred", Toast.LENGTH_SHORT).show();
                        }
                    }
                } else {
                    Toast.makeText(getActivity(), "No WhatsApp Installed on your phone.", Toast.LENGTH_SHORT).show();
                }


            }
        });


        llSendReminderUsingMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendReminderUsingMessage();
            }
        });

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (getActivity() != null) {
                    convertLang();
                }

            }
        }, 200);
        return view;
    }

    private void sendReminderUsingMessage() {
        Intent i = new Intent(Intent.ACTION_VIEW, Uri.fromParts("sms", mMobile, null));
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        i.putExtra("sms_body", message);

        if (getActivity() != null) {
            startActivity(i);
        }
    }

    private void hideBottomSheet() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (getActivity() != null) {
                    if (getDialog() != null) {
                        if (getDialog().isShowing()) {
                            getDialog().dismiss();
                        }
                    }
                }
            }
        }, 2000);
    }

    private boolean appInstalledOrNot(String uri) {
        if (getActivity() != null) {
            PackageManager pm = getActivity().getPackageManager();
            boolean app_installed;
            try {
                pm.getPackageInfo(uri, PackageManager.GET_ACTIVITIES);
                app_installed = true;
            } catch (PackageManager.NameNotFoundException e) {
                app_installed = false;
            }
            return app_installed;
        }

        return false;
    }

    private void convertLang() {

        if (Common.getLanguage(Objects.requireNonNull(getActivity())).equalsIgnoreCase(Common.HINDI)) {
            tv_send_reminder.setText("सेंड रिमाइंडर");
        } else {

            //If not, display "no connection" warning:
            tv_send_reminder.setText(tv_send_reminder.getText().toString());


        }
    }

}
