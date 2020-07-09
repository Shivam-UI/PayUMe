package com.lgt.paykredit.bottomsheets;

import android.content.Intent;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.lgt.paykredit.Activities.ActivityLogin;
import com.lgt.paykredit.Activities.ActivityRegister;
import com.lgt.paykredit.R;

public class BottomSheetLoginOrRegister extends BottomSheetDialogFragment {

    private LinearLayout llLoginBtmSheet, llRegisterBottomSheet;

    public BottomSheetLoginOrRegister() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.bottom_sheet_login_or_register, container, false);

        llLoginBtmSheet = view.findViewById(R.id.llLoginBtmSheet);
        llRegisterBottomSheet = view.findViewById(R.id.llRegisterBottomSheet);

        llLoginBtmSheet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismissDialog();
                startActivity(new Intent(getActivity(), ActivityLogin.class));

            }
        });

        llRegisterBottomSheet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismissDialog();
                startActivity(new Intent(getActivity(), ActivityRegister.class));

            }
        });

        return view;
    }

    private void dismissDialog() {
        if (getActivity() != null) {
            if (getDialog() != null) {
                if (getDialog().isShowing()) {
                    getDialog().dismiss();
                }
            }
        }
    }
}
