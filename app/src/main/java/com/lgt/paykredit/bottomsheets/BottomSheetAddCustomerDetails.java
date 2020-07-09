package com.lgt.paykredit.bottomsheets;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.lgt.paykredit.Activities.ActivityAddedCustomers;
import com.lgt.paykredit.R;
import com.lgt.paykredit.extras.PayKreditAPI;
import com.lgt.paykredit.extras.SingletonRequestQueue;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class BottomSheetAddCustomerDetails extends BottomSheetDialogFragment {

    private EditText etCustomerName, etCustomerMobile, etCustomerBillingAddress, etCustomerEmailAddress;
    private ProgressBar pbAddCustomer;
    private TextView tvSaveCustomer, tvTitleCustomer;

    private String mCustomerName, mMobileNumber, mBillingAddress, mEmailAddress, mUserID, mTblCustomerID;

    private SharedPreferences sharedPreferences;

    private boolean update = false;

    private BottomSheetBehavior mBehavior;

    // EDIT sheet


    public BottomSheetAddCustomerDetails() {
    }
    /*@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.bottom_sheet_add_customer_details, container, false);
        return view;
    }*/

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        if (getActivity() != null) {
            sharedPreferences = getActivity().getSharedPreferences("USER_DATA", Context.MODE_PRIVATE);
            mUserID = sharedPreferences.getString("KEY_USER_ID", "");
        }


        BottomSheetDialog dialog = (BottomSheetDialog) super.onCreateDialog(savedInstanceState);

        View view = View.inflate(getContext(), R.layout.bottom_sheet_add_customer_details, null);

        etCustomerName = view.findViewById(R.id.etCustomerName);
        etCustomerMobile = view.findViewById(R.id.etCustomerMobile);
        etCustomerBillingAddress = view.findViewById(R.id.etCustomerBillingAddress);
        etCustomerEmailAddress = view.findViewById(R.id.etCustomerEmailAddress);
        pbAddCustomer = view.findViewById(R.id.pbAddCustomer);
        tvSaveCustomer = view.findViewById(R.id.tvSaveCustomer);
        tvTitleCustomer = view.findViewById(R.id.tvTitleCustomer);

        // if true means - > Add customer
        // if false -> Edit customer

        Bundle getEditData = getArguments();


        Log.e("hjkhkjhkjhkjk",getEditData+"");
        if (getEditData != null) {
            if (getEditData.containsKey("KEY_TYPE")) {
                String mType = getEditData.getString("KEY_TYPE");

                if (mType != null && mType.equalsIgnoreCase("EDIT")) {
                    update = true;
                    tvTitleCustomer.setText("Edit Customer Details");
                }

                if (getEditData.containsKey("KEY_EDIT_NAME")) {
                    etCustomerName.setText(getEditData.getString("KEY_EDIT_NAME"));
                }


                if (getEditData.containsKey("KEY_EDIT_EMAIL")) {
                    etCustomerEmailAddress.setText(getEditData.getString("KEY_EDIT_EMAIL"));
                }


                if (getEditData.containsKey("KEY_EDIT_ID")) {
                    mTblCustomerID = getEditData.getString("KEY_EDIT_ID");
                }

                if (getEditData.containsKey("KEY_EDIT_MOBILE")) {
                    etCustomerMobile.setText(getEditData.getString("KEY_EDIT_MOBILE"));
                }

                if (getEditData.containsKey("KEY_EDIT_ADDRESS")) {
                    etCustomerBillingAddress.setText(getEditData.getString("KEY_EDIT_ADDRESS"));
                }


            }
        }

        tvSaveCustomer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fieldValidation();
            }
        });


        dialog.setContentView(view);
        mBehavior = BottomSheetBehavior.from((View) view.getParent());
        return dialog;

    }


    private void fieldValidation() {

        mCustomerName = etCustomerName.getText().toString().trim();
        mMobileNumber = etCustomerMobile.getText().toString().trim();
        mBillingAddress = etCustomerBillingAddress.getText().toString().trim();
        mEmailAddress = etCustomerEmailAddress.getText().toString().trim();

        if (TextUtils.isEmpty(mCustomerName)) {
            Toast.makeText(getActivity(), "Customer name is empty", Toast.LENGTH_SHORT).show();
            return;
        }

        if (mCustomerName.length() < 5) {
            Toast.makeText(getActivity(), "Address must be at least 10 words", Toast.LENGTH_SHORT).show();
            return;
        }


        if (TextUtils.isEmpty(mMobileNumber)) {
            Toast.makeText(getActivity(), "Enter mobile number", Toast.LENGTH_SHORT).show();
            return;
        }

        if (mMobileNumber.length() < 10) {
            Toast.makeText(getActivity(), "Mobile number must be 10 digits", Toast.LENGTH_SHORT).show();
            return;
        }


        if (TextUtils.isEmpty(mBillingAddress)) {
            Toast.makeText(getActivity(), "Enter billing address", Toast.LENGTH_SHORT).show();
            return;
        }

        if (mBillingAddress.length() < 10) {
            Toast.makeText(getActivity(), "At least 10 words are required", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(mEmailAddress)) {
            Toast.makeText(getActivity(), "Enter email address", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(mEmailAddress).matches()) {
            Toast.makeText(getActivity(), "Enter a valid email address", Toast.LENGTH_SHORT).show();
            return;
        }


        if (update) {
            Log.e("dasdasdas","Caaascasc");
            //call update api
            callUpdateAPI();
        } else {
            Log.e("fdgfgdfgdfgdfgdfg","adasdadasdasdas");
            apiCallAddCustomerDetails();
        }


    }

    private void callUpdateAPI() {

        pbAddCustomer.setVisibility(View.VISIBLE);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, PayKreditAPI.EDIT_INVOICE_CUSTOMER, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("updateresponse", response + "");
                pbAddCustomer.setVisibility(View.GONE);

                try {
                    JSONObject jsonObject = new JSONObject(response);

                    String message = jsonObject.getString("message");
                    String status = jsonObject.getString("status");

                    if (status.equalsIgnoreCase("1")) {
                        JSONObject data = jsonObject.getJSONObject("data");

                        ActivityAddedCustomers activityAddedCustomers = ActivityAddedCustomers.getInstance();
                        activityAddedCustomers.loadCustomers();

                        Toast.makeText(getActivity(), "" + message, Toast.LENGTH_SHORT).show();
                        hideBottomSheet();

                        String tbl_invoice_customer_id = data.getString("tbl_invoice_customer_id");
                        String customer_mobile = data.getString("customer_mobile");
                        String customer_email = data.getString("customer_email");
                        String customer_name = data.getString("customer_name");
                        String billing_address = data.getString("billing_address");


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
                pbAddCustomer.setVisibility(View.GONE);
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("tbl_invoice_customer_id", mTblCustomerID);
                params.put("customer_name", mCustomerName);
                params.put("customer_mobile", mMobileNumber);
                params.put("customer_email", mEmailAddress);
                params.put("billing_address", mBillingAddress);
                Log.e("paramsedit", params + "");
                return params;
            }
        };

        RequestQueue requestQueue = SingletonRequestQueue.getInstance(getActivity()).getRequestQueue();
        requestQueue.add(stringRequest);

    }

    private void apiCallAddCustomerDetails() {

        pbAddCustomer.setVisibility(View.VISIBLE);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, PayKreditAPI.ADD_CUSTOMER_DETAILS, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.e("ddasdreqrewrewr", response + "");

                pbAddCustomer.setVisibility(View.GONE);

                try {
                    JSONObject jsonObject = new JSONObject(response);

                    String message = jsonObject.getString("message");
                    String status = jsonObject.getString("status");
                    String invoice_customer_id = jsonObject.getString("invoice_customer_id");

                    if (status.equalsIgnoreCase("1")) {

                        ActivityAddedCustomers activityAddedCustomers = ActivityAddedCustomers.getInstance();
                        activityAddedCustomers.loadCustomers();

                        Toast.makeText(getActivity(), "" + message, Toast.LENGTH_SHORT).show();
                        hideBottomSheet();
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

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("customer_name", mCustomerName);
                params.put("customer_mobile", mMobileNumber);
                params.put("customer_email", mEmailAddress);
                params.put("user_id", mUserID);
                params.put("billing_address", mBillingAddress);
                Log.e("addcustomerparams", params + "");
                return params;
            }
        };


        RequestQueue requestQueue = SingletonRequestQueue.getInstance(getActivity()).getRequestQueue();
        requestQueue.add(stringRequest);

    }

    private void hideBottomSheet() {

        if (getActivity() != null) {
            if (getDialog() != null) {
                if (getDialog().isShowing()) {
                    getDialog().dismiss();
                }
            }
        }

    }

    @Override
    public void onStart() {
        super.onStart();
        mBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
    }


}
