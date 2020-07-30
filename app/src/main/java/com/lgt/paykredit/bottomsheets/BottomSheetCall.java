package com.lgt.paykredit.bottomsheets;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;

import androidx.core.app.ActivityCompat;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.lgt.paykredit.R;

public class BottomSheetCall extends BottomSheetDialogFragment {

    private String mMobile, mName;
    private TextView tvBottomSheetCallNUmber, tvBottomSheetCallName;

    private LinearLayout llMakeCall;

    public BottomSheetCall() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.bottom_sheet_call, container, false);

        tvBottomSheetCallName = view.findViewById(R.id.tvBottomSheetCallName);
        tvBottomSheetCallNUmber = view.findViewById(R.id.tvBottomSheetCallNUmber);
        llMakeCall = view.findViewById(R.id.llMakeCall);

        Bundle bundle = getArguments();

        if (bundle != null) {
            if (bundle.containsKey("KEY_CUSTOMER_NUMBER")) {
                mMobile = bundle.getString("KEY_CUSTOMER_NUMBER");
                tvBottomSheetCallNUmber.setText(mMobile);
                Log.d("mobile",""+mMobile);
                llMakeCall.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (getActivity() != null) {
                            makeCall(mMobile);
                        }
                    }
                });
            }

            if (bundle.containsKey("KEY_CUSTOMER_NAME")) {
                mName = bundle.getString("KEY_CUSTOMER_NAME");
                tvBottomSheetCallName.setText("Call " + mName);

            }

        }

        else {
            Toast.makeText(getActivity(), "Some error occurred, contact admin.", Toast.LENGTH_SHORT).show();
        }


        return view;
    }

    private void makeCall(String mobileToCall) {
        Log.e("dsadasdad", "cacaac" + mobileToCall + "");
        Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        callIntent.setData(Uri.parse("tel:" + mobileToCall));
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        getActivity().startActivity(callIntent);
    }
}
