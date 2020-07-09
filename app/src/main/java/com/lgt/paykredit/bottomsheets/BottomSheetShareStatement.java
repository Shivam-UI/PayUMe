package com.lgt.paykredit.bottomsheets;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.itextpdf.text.DocumentException;
import com.lgt.paykredit.Activities.SingleUserTransaction;
import com.lgt.paykredit.R;
import com.lgt.paykredit.extras.Common;
import com.lgt.paykredit.extras.Language;

import java.io.IOException;
import java.util.Objects;


public class BottomSheetShareStatement extends BottomSheetDialogFragment {

    private RelativeLayout rlFullStatement;
    private TextView tv_share_title,tv_account_statement;
    private String pathFile;

    private ProgressBar pbShareStatement;


    public BottomSheetShareStatement() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.bottom_sheet_share_statement, container, false);

        rlFullStatement = view.findViewById(R.id.rlFullStatement);
        pbShareStatement = view.findViewById(R.id.pbShareStatement);
        tv_share_title = view.findViewById(R.id.tv_share_title);
        tv_account_statement = view.findViewById(R.id.tv_account_statement);

        Bundle bundle = getArguments();
        if (bundle != null) {
            if (bundle.containsKey("KEY_PATH")) {
                pathFile = bundle.getString("KEY_PATH");
                if (pathFile != null) {
                    try {
                        sendImage();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (DocumentException e) {
                        e.printStackTrace();
                    }
                }
                Log.e("dsadrerer", pathFile + "");
            }
        }

        rlFullStatement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {

                    pbShareStatement.setVisibility(View.VISIBLE);

                    SingleUserTransaction singleUserTransaction = SingleUserTransaction.getInstance();
                    singleUserTransaction.generateOnlyPDF();

                    hideBottomSheet();

                } catch (IOException e) {
                    e.printStackTrace();
                } catch (DocumentException e) {
                    e.printStackTrace();
                }
            }
        });


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (getActivity()!=null){
                    convertLang();
                }

            }
        },200);
        return view;
    }

    private void convertLang() {
        if (Common.getLanguage(getActivity()).equalsIgnoreCase(Common.HINDI)) {

            tv_account_statement.setText("शेयर रिपोर्ट");
            tv_share_title.setText("अकाउंट रिपोर्ट्स");

        } else {

            //If not, display "no connection" warning:
            tv_share_title.setText(tv_share_title.getText().toString());
            tv_account_statement.setText(tv_account_statement.getText().toString());

        }
    }

    private void hideBottomSheet() {
        {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (getActivity() != null) {
                        if (getDialog() != null) {
                            if (getDialog().isShowing()) {
                                getDialog().dismiss();
                                pbShareStatement.setVisibility(View.GONE);
                            }
                        }
                    }
                }
            }, 1000);
        }
    }

    private void sendImage() throws IOException, DocumentException {

        String mMobile = "8826025250";
        boolean installed = appInstalledOrNot("com.whatsapp");
        if (installed) {
            if (getActivity() != null) {
                PackageManager packageManager = getActivity().getPackageManager();

                try {
                    String url = "https://api.whatsapp.com/send?phone=" + "+91" + mMobile;
                    Intent share = new Intent(Intent.ACTION_SEND);
                    share.setType("application/pdf");
                    share.putExtra(Intent.EXTRA_STREAM, Uri.parse(pathFile));
                    share.setPackage("com.whatsapp");//package name of the app
                    startActivity(Intent.createChooser(share, "Share Image"));
                    share.setData(Uri.parse(url));
                    hideBottomSheet();
                    if (share.resolveActivity(packageManager) != null) {
                        getActivity().startActivity(share);
                        hideBottomSheet();
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

}
