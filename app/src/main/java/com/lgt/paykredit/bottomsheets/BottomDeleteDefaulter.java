package com.lgt.paykredit.bottomsheets;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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
import com.lgt.paykredit.Activities.ActivityDefaultInvoice;
import com.lgt.paykredit.R;
import com.lgt.paykredit.extras.PayKreditAPI;
import com.lgt.paykredit.extras.SingletonRequestQueue;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static android.content.Context.MODE_PRIVATE;

public class BottomDeleteDefaulter extends BottomSheetDialogFragment {

    private TextView tvDeleteItemName;
    private LinearLayout llCancelItems, llDeleteItems, llClose;
    private ProgressBar pbDeleteItems;
    private String mItemName, mItemID;
    private SharedPreferences sharedPreferences;
    private String mUserID;

    public BottomDeleteDefaulter() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_bottom_sheet_delete_items, container, false);
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
                deleteItemAPI();
                // hideBottomSheet();
            }
        });

        return view;
    }

    private void deleteItemAPI() {

        pbDeleteItems.setVisibility(View.VISIBLE);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, PayKreditAPI.DEFAULT_ADD_REMOVED_CUSTOMER, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.e("deleteeeresponse", response + "");
                pbDeleteItems.setVisibility(View.GONE);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String status = jsonObject.getString("status");
                    String message = jsonObject.getString("message");
                    hideBottomSheet();
                    if (status.equalsIgnoreCase("1")) {
                        Toast.makeText(getActivity(), "" + message, Toast.LENGTH_SHORT).show();
                        getActivity().onBackPressed();
                    } else {
                        Toast.makeText(getActivity(), "" + message, Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                pbDeleteItems.setVisibility(View.GONE);
                Toast.makeText(getActivity(), "Server or network error", Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("user_id", mUserID);
                params.put("tbl_invoice_customer_id", mItemID);
                params.put("type", "remove");
                Log.e("deleteitemsparams", params + "");
                return params;
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