package com.lgt.paykredit.bottomsheets;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.lgt.paykredit.R;
import com.lgt.paykredit.extras.ClickToAddContact;

public class BottomAddUser extends BottomSheetDialogFragment {
    ClickToAddContact clickToAddContact;
    EditText et_user_mobile, et_user_name;
    TextView tv_save_user_details;
    String userMob = "", userName = "";
    ImageView iv_open_contact;

    public BottomAddUser(ClickToAddContact clickToAdd) {
        clickToAddContact = clickToAdd;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        final BottomSheetDialog dialog = (BottomSheetDialog) super.onCreateDialog(savedInstanceState);

        View view = View.inflate(getContext(), R.layout.add_user_contact, null);

        et_user_mobile = view.findViewById(R.id.et_user_mobile);
        et_user_name = view.findViewById(R.id.et_user_name);
        iv_open_contact = view.findViewById(R.id.iv_open_contact);
        tv_save_user_details = view.findViewById(R.id.tv_save_user_details);


        iv_open_contact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickToAddContact.openContactList();
                dismiss();
            }
        });

        tv_save_user_details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isValidate()) {
                    if (isNameValidate()) {
                        clickToAddContact.addContactUser(userMob, userName);
                        dismiss();
                    }
                }
            }
        });
        dialog.setContentView(view);
        return dialog;
    }

    private Boolean isValidate() {
        if (et_user_mobile.getText().toString().trim().equalsIgnoreCase("")) {
            et_user_mobile.setError("Enter Valid Number");
            return false;
        } else if (et_user_mobile.getText().toString().trim().length() != 10) {
            et_user_mobile.setError("Not A valid Number");
            return false;
        } else if (!et_user_mobile.getText().toString().trim().isEmpty()) {
            userMob = et_user_mobile.getText().toString();
            return true;
        }
        return false;
    }

    private Boolean isNameValidate() {
        if (et_user_name.getText().toString().equalsIgnoreCase("")) {
            et_user_name.setError("Enter Name");
            return false;
        } else if (!et_user_name.getText().toString().trim().isEmpty()) {
            userName = et_user_name.getText().toString();
            return true;
        }
        return false;
    }
}
