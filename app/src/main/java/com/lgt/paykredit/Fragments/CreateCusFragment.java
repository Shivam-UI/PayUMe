package com.lgt.paykredit.Fragments;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.lgt.paykredit.Activities.ActivityRegister;
import com.lgt.paykredit.Activities.AddNewCustomer;
import com.lgt.paykredit.Activities.MainActivity;
import com.lgt.paykredit.R;
import com.lgt.paykredit.extras.PayKreditAPI;
import com.lgt.paykredit.extras.SingletonRequestQueue;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static android.content.Context.MODE_PRIVATE;
import static com.lgt.paykredit.Fragments.ExistingCusFragment.Invoice_Customer_ID;
import static com.lgt.paykredit.Fragments.ExistingCusFragment.isEditing;

public class CreateCusFragment extends Fragment {
    private String mCustomerName, mMobileNumber, mCompanyName, mCustomerAddress, mEmailAddress, mGSTINNumber, mGSTINAddress, mCINNumber, mUserID;
    public static EditText et_customer_name, et_mobile_number, et_company_name, et_customer_address, et_email_address, et_gstin_number, et_gstin_address, et_cin_number;
    TextView tv_save_new_customer;
    private SharedPreferences sharedPreferences;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View mView = inflater.inflate(R.layout.new_customer_fragment, container, false);
        init(mView);
        return mView;
    }

    private void init(View mView) {
        sharedPreferences = getActivity().getSharedPreferences("USER_DATA", MODE_PRIVATE);
        if (sharedPreferences.contains("KEY_USER_ID")) {
            mUserID = sharedPreferences.getString("KEY_USER_ID", "");
            Log.e("Sadrrewrew", mUserID);
        }
        tv_save_new_customer = mView.findViewById(R.id.tv_save_new_customer);
        et_mobile_number = mView.findViewById(R.id.et_mobile_number);
        et_customer_name = mView.findViewById(R.id.et_customer_name);
        et_company_name = mView.findViewById(R.id.et_company_name);
        et_customer_address = mView.findViewById(R.id.et_customer_address);
        et_email_address = mView.findViewById(R.id.et_email_address);
        et_gstin_number = mView.findViewById(R.id.et_gstin_number);
        et_gstin_address = mView.findViewById(R.id.et_gstin_address);
        et_cin_number = mView.findViewById(R.id.et_cin_number);

        tv_save_new_customer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validateFields();
            }
        });
    }

    private void validateFields() {
        mCustomerName = "" + et_customer_name.getText().toString().trim();
        mMobileNumber = "" + et_mobile_number.getText().toString().trim();
        mCompanyName = "" + et_company_name.getText().toString().trim();
        mCustomerAddress = "" + et_customer_address.getText().toString().trim();
        mEmailAddress = "" + et_email_address.getText().toString().trim();
        mGSTINNumber = "" + et_gstin_number.getText().toString().trim();
        mGSTINAddress = "" + et_gstin_address.getText().toString().trim();
        mCINNumber = "" + et_cin_number.getText().toString().trim();


        if (TextUtils.isEmpty(mCustomerName)) {
            et_customer_name.setError("Enter Customer Name");
            et_customer_name.requestFocus();
            return;
        }

        if (mMobileNumber.length() < 10) {
            et_mobile_number.setError("Mobile number must be 10 digits");
            et_mobile_number.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(mCompanyName)) {
            et_company_name.setError("Enter Company Name");
            et_company_name.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(mCustomerAddress)) {
            et_customer_address.setError("Enter Customer Address");
            et_customer_address.requestFocus();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(mEmailAddress).matches()) {
            et_email_address.setError("Enter a valid email");
            et_email_address.requestFocus();
            return;
        }

        if (mGSTINNumber.length() < 15) {
            et_gstin_number.setError("Enter Valid GSTIN Number");
            et_gstin_number.requestFocus();
            return;
        }


        if (TextUtils.isEmpty(mGSTINAddress)) {
            et_gstin_address.setError("Enter GSTIN Address");
            et_gstin_address.requestFocus();
            return;
        }


        if (mCINNumber.length() < 6) {
            et_gstin_number.setError("Enter Valid CIN Number");
            et_gstin_number.requestFocus();
            return;
        }

        if (isEditing){
            if (!Invoice_Customer_ID.equalsIgnoreCase("")){
                Toast.makeText(getContext(), "You Are Editor!", Toast.LENGTH_SHORT).show();
                callEditCustomerApi();
            }
        }else{
            callAddCustomerApi();
        }
    }

    private void callAddCustomerApi() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, PayKreditAPI.ADD_INVOICE_CUSTOMER, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("responseregister", response + "");
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String message = jsonObject.getString("message");
                    String status = jsonObject.getString("status");
                    if (status.equalsIgnoreCase("1")) {
                        et_customer_name.setText("");
                        et_mobile_number.setText("");
                        et_company_name.setText("");
                        et_customer_address.setText("");
                        et_email_address.setText("");
                        et_gstin_number.setText("");
                        et_gstin_address.setText("");
                        et_cin_number.setText("");
                        Toast.makeText(getContext(), "New Customer Added!", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getContext(), "Something Went Wrong!", Toast.LENGTH_SHORT).show();
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
                params.put("user_id", mUserID);
                params.put("customer_name", mCustomerName);
                params.put("customer_mobile", mMobileNumber);
                params.put("company_name", mCompanyName);
                params.put("billing_address", mCustomerAddress);
                params.put("customer_email", mEmailAddress);
                params.put("GSTIN", mGSTINNumber);
                params.put("GSTINAddress", mGSTINAddress);
                params.put("CIN", mCINNumber);
                Log.e("paramsregister", "" + params);
                return params;
            }
        };
        RequestQueue requestQueue = SingletonRequestQueue.getInstance(getContext()).getRequestQueue();
        requestQueue.add(stringRequest);
    }

    private void callEditCustomerApi() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, PayKreditAPI.EDIT_INVOICE_CUSTOMER_API, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("responseregister", response + "");
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String message = jsonObject.getString("message");
                    String status = jsonObject.getString("status");
                    if (status.equalsIgnoreCase("1")) {
                        et_customer_name.setText("");
                        et_mobile_number.setText("");
                        et_company_name.setText("");
                        et_customer_address.setText("");
                        et_email_address.setText("");
                        et_gstin_number.setText("");
                        et_gstin_address.setText("");
                        et_cin_number.setText("");
                        Toast.makeText(getContext(), ""+message, Toast.LENGTH_SHORT).show();
                        getActivity().getSupportFragmentManager().popBackStack();
                    } else {
                        Toast.makeText(getContext(), ""+message, Toast.LENGTH_SHORT).show();
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
                params.put("user_id", mUserID);
                params.put("tbl_invoice_customer_id", Invoice_Customer_ID);
                params.put("customer_name", mCustomerName);
                params.put("customer_mobile", mMobileNumber);
                params.put("company_name", mCompanyName);
                params.put("billing_address", mCustomerAddress);
                params.put("customer_email", mEmailAddress);
                params.put("GSTIN", mGSTINNumber);
                params.put("GSTINAddress", mGSTINAddress);
                params.put("CIN", mCINNumber);
                Log.e("editrequest", "" + params);
                return params;
            }
        };
        RequestQueue requestQueue = SingletonRequestQueue.getInstance(getContext()).getRequestQueue();
        requestQueue.add(stringRequest);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (AddNewCustomer.Position != 0 && !AddNewCustomer.Tbl_Customer_id.equalsIgnoreCase("")) {
            Log.d("","Edit Request Found!");
        } else {
            Log.d("","No Edit Request");
        }
    }
}
