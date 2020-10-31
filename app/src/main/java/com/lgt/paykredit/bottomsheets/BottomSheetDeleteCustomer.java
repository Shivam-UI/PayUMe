package com.lgt.paykredit.bottomsheets;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

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
import com.lgt.paykredit.Activities.ActivityAddedCustomers;
import com.lgt.paykredit.Fragments.FragmentCreditBook;
import com.lgt.paykredit.R;
import com.lgt.paykredit.extras.PayKreditAPI;
import com.lgt.paykredit.extras.SingletonRequestQueue;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static android.content.Context.MODE_PRIVATE;


public class BottomSheetDeleteCustomer extends BottomSheetDialogFragment {

    private TextView tvCustomerNameToDelete;
    private LinearLayout llCancel, llDelete;
    private SharedPreferences sharedPreferences;
    String mUserID;
    private String mId, mName;
    private ProgressBar pbDeletCustomer;

    private boolean deleteAddedCustomer = false;

    public BottomSheetDeleteCustomer() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_bottom_sheet_delete_customer, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        tvCustomerNameToDelete = view.findViewById(R.id.tvCustomerNameToDelete);
        llCancel = view.findViewById(R.id.llCancel);
        llDelete = view.findViewById(R.id.llDelete);
        pbDeletCustomer = view.findViewById(R.id.pbDeletCustomer);
        sharedPreferences = getActivity().getSharedPreferences("USER_DATA", MODE_PRIVATE);
        if (sharedPreferences.contains("KEY_USER_ID")) {
            mUserID = sharedPreferences.getString("KEY_USER_ID", "");
            Log.e("Sadrrewrew", mUserID);
        }
        Bundle getDeleteData = getArguments();
        Log.e("hjkhkjhkjhkj", getDeleteData + "");
        if (getDeleteData != null) {
            if (getDeleteData.containsKey("KEY_DELETE_CUSTOMER_NAME")) {
                mName = getDeleteData.getString("KEY_DELETE_CUSTOMER_NAME");
                tvCustomerNameToDelete.setText(mName);
            }

            if (getDeleteData.containsKey("KEY_DELETE_ID")) {
                mId = getDeleteData.getString("KEY_DELETE_ID");
            }

            if (getDeleteData.containsKey("KEY_DELETE_TYPE")) {
                String mDelete = getDeleteData.getString("KEY_DELETE_TYPE");
                if (mDelete != null && mDelete.equalsIgnoreCase("DELETE_ADDED_CUSTOMER")) {
                    deleteAddedCustomer = true;
                }
            }


            if (getDeleteData.containsKey("KEY_DELETE_ID_ADDED_CUSTOMERS")) {
                mId = getDeleteData.getString("KEY_DELETE_ID_ADDED_CUSTOMERS");
            }
        }

        llCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancelDialog();
            }
        });

        llDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.e("jkljlkjkljlkjlkjl", deleteAddedCustomer + "");
                callDeleteAddedCustomer();
                /* if(deleteAddedCustomer){
                    //Call delete added customer api
                    FragmentCreditBook.getInstance().LoadUserNow("YES");
                    callDeleteAddedCustomer();
                    apiCallDeleteUser();
                }
                else {
                    FragmentCreditBook.getInstance().LoadUserNow("YES");
                    callDeleteAddedCustomer();
                    apiCallDeleteUser();
                } */
            }
        });
    }

    private void callDeleteAddedCustomer() {

        pbDeletCustomer.setVisibility(View.VISIBLE);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, PayKreditAPI.DELETE_INVOICE_ADDED_CUSTOMER, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.e("deletetetetetete", response + "");

                pbDeletCustomer.setVisibility(View.GONE);

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String message = jsonObject.getString("message");
                    String status = jsonObject.getString("status");
                    if (status.equalsIgnoreCase("1")) {
                        Toast.makeText(getActivity(), "" + message, Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent("UPDATE_CUSTOMER");
                        LocalBroadcastManager.getInstance(getActivity()).sendBroadcast(intent);
                        cancelDialog();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) //9923406490
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("tbl_invoice_customer_id", mId);
                params.put("user_id", mUserID);
                Log.e("gjhgjhgjhgj", mId + "  |  " + mUserID);
                return params;
            }
        };
        RequestQueue requestQueue = SingletonRequestQueue.getInstance(getActivity()).getRequestQueue();
        requestQueue.add(stringRequest);

    }

    private void apiCallDeleteUser() {

        pbDeletCustomer.setVisibility(View.VISIBLE);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, PayKreditAPI.DELETE_CUSTOMER, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.e("deleldleldledleld", response + "");

                pbDeletCustomer.setVisibility(View.GONE);

                try {
                    JSONObject jsonObject = new JSONObject(response);

                    String status = jsonObject.getString("status");
                    String message = jsonObject.getString("message");

                    if (status.equalsIgnoreCase("1")) {

                        cancelDialog();

                        FragmentCreditBook fragmentCreditBook = FragmentCreditBook.getInstance();
                        fragmentCreditBook.loadCustomers();

                        Toast.makeText(getActivity(), "" + message, Toast.LENGTH_SHORT).show();


                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                pbDeletCustomer.setVisibility(View.GONE);
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("tbl_add_customer_id", mId);
                Log.e("paramsdelete", params + "");
                return params;
            }
        };

        RequestQueue requestQueue = SingletonRequestQueue.getInstance(getActivity()).getRequestQueue();
        requestQueue.add(stringRequest);


    }

    private void cancelDialog() {

        if (getDialog() != null) {
            if (getDialog().isShowing()) {
                getDialog().dismiss();
            }
        }

    }

    public interface LoadUserAgain {
        void LoadUserNow(String type_key);
    }
}
