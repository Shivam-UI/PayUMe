package com.lgt.paykredit.bottomsheets;

import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.lgt.paykredit.R;

import java.util.Objects;


public class BottomSheetBusinessCard extends BottomSheetDialogFragment {

    private TextView tvShareCard;

    public BottomSheetBusinessCard() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.bottom_sheet_business_card, container, false);
        tvShareCard = view.findViewById(R.id.tvShareCard);

        tvShareCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity(), "Coming soon...", Toast.LENGTH_SHORT).show();
                if (getActivity() != null) {
                    if (Objects.requireNonNull(getDialog()).isShowing()) {
                        dismiss();
                    }
                }

            }
        });
        return view;
    }
}
