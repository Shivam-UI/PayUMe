package com.lgt.paykredit.bottomsheets;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.lgt.paykredit.R;
import com.lgt.paykredit.extras.SingletonRequestQueue;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static android.content.Context.MODE_PRIVATE;
import static com.lgt.paykredit.extras.PayKreditAPI.EDIT_INVOICE_BY_TYPE_API;

public class BottomSheetPayment extends BottomSheetDialogFragment {

    private TextView tvDeleteItemName;
    private LinearLayout llCancelItems, llDeleteItems, llClose;
    private ProgressBar pbDeleteItems;

    private String mItemName, mItemID;
    private SharedPreferences sharedPreferences;
    private String mUserID, invoice_date_picker = "";

    public BottomSheetPayment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_bottom_sheet_success_payment, container, false);
        sharedPreferences = getActivity().getSharedPreferences("USER_DATA", MODE_PRIVATE);
        if (sharedPreferences.contains("KEY_USER_ID")) {
            mUserID = sharedPreferences.getString("KEY_USER_ID", "");
        }
        tvDeleteItemName = view.findViewById(R.id.tvDeleteItemName);
        llCancelItems = view.findViewById(R.id.llCancelItems);
        llDeleteItems = view.findViewById(R.id.llDeleteItems);
        pbDeleteItems = view.findViewById(R.id.pbDeleteItems);

        llClose = view.findViewById(R.id.llClose);

        Bundle getDeleteItems = getArguments();

        Log.e("bundldelteitems", getDeleteItems + "");

        if (getDeleteItems != null) {
            if (getDeleteItems.containsKey("KEY_DELETE_ID")) {
                mItemID = getDeleteItems.getString("KEY_DELETE_ID");
            }

            if (getDeleteItems.containsKey("KEY_DELETE_ITEM")) {
                mItemName = getDeleteItems.getString("KEY_DELETE_ITEM");
                tvDeleteItemName.setText(mItemName);
            }
        }


        llClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideBottomSheet();
            }
        });

        llCancelItems.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideBottomSheet();
            }
        });


        llDeleteItems.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editInvoice("paid");
            }
        });
        return view;
    }

    public void editInvoice(String typeAPI) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, EDIT_INVOICE_BY_TYPE_API, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("editInvoice", response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String message = jsonObject.getString("message");
                    String status = jsonObject.getString("status");
                    if (status.equalsIgnoreCase("1")) {
                        Toast.makeText(getActivity(), "" + message, Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getActivity(), "" + message, Toast.LENGTH_SHORT).show();
                        dismiss();
                    }
                    getActivity().onBackPressed();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("editInvoice", error.getMessage());
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> param = new HashMap<>();
                param.put("user_id", mUserID);
                param.put("tbl_invoice_id", mItemID);
                param.put("type", typeAPI);
                param.put("paid", "1");
                return param;
            }
        };
        RequestQueue requestQueue = SingletonRequestQueue.getInstance(getActivity()).getRequestQueue();
        requestQueue.add(stringRequest);
    }

    private void hideBottomSheet() {
        if (getDialog() != null) {
            if (getDialog().isShowing()) {
                getDialog().dismiss();
            }
        }
    }
}