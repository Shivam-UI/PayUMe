package com.lgt.paykredit.Fragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.lgt.paykredit.Activities.AddNewCustomer;
import com.lgt.paykredit.Adapter.ExistingCustomerAdapter;
import com.lgt.paykredit.Models.ExistingCustomerModel;
import com.lgt.paykredit.R;
import com.lgt.paykredit.extras.CustomerClick;

import com.lgt.paykredit.extras.PayKreditAPI;
import com.lgt.paykredit.extras.SingletonRequestQueue;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static android.content.Context.MODE_PRIVATE;
import static com.lgt.paykredit.Activities.AddNewCustomer.vp_add_customer_tab;
import static com.lgt.paykredit.Fragments.CreateCusFragment.et_cin_number;
import static com.lgt.paykredit.Fragments.CreateCusFragment.et_company_name;
import static com.lgt.paykredit.Fragments.CreateCusFragment.et_customer_address;
import static com.lgt.paykredit.Fragments.CreateCusFragment.et_customer_name;
import static com.lgt.paykredit.Fragments.CreateCusFragment.et_email_address;
import static com.lgt.paykredit.Fragments.CreateCusFragment.et_gstin_address;
import static com.lgt.paykredit.Fragments.CreateCusFragment.et_gstin_number;
import static com.lgt.paykredit.Fragments.CreateCusFragment.et_mobile_number;
import static com.lgt.paykredit.extras.Common.CUSTOMER_ADDED;

public class ExistingCusFragment extends Fragment implements CustomerClick {
    public static boolean isEditing = false;
    public static String Invoice_Customer_ID = "";
    TextView tv_not_customer_found;
    RecyclerView rv_existing_customer;
    ProgressBar pb_progressLoader;
    ExistingCustomerAdapter existingCustomerAdapter;
    ArrayList<ExistingCustomerModel> mList = new ArrayList<>();
    LinearLayoutManager linearLayoutManager;
    private SharedPreferences sharedPreferences;
    String mUserID;

    public EditRequestClick editClickFoundRequest;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof EditRequestClick) {
            editClickFoundRequest = (EditRequestClick) context;
        }
    }

    public interface EditRequestClick {
        void editClickFoundRequest(String user_id, int pos);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View mView = inflater.inflate(R.layout.existing_customer_list, container, false);
        // init View
        initView(mView);

        sharedPreferences = getActivity().getSharedPreferences("USER_DATA", MODE_PRIVATE);
        if (sharedPreferences.contains("KEY_USER_ID")) {
            mUserID = sharedPreferences.getString("KEY_USER_ID", "");
            Log.e("Sadrrewrew", mUserID);
        }

        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(
                UPDATEDATA,new IntentFilter("UPDATE_CUSTOMER"));

        return mView;
    }

    private void initView(View mView) {
        tv_not_customer_found = mView.findViewById(R.id.tv_not_customer_found);
        pb_progressLoader = mView.findViewById(R.id.pb_progressLoader);
        rv_existing_customer = mView.findViewById(R.id.rv_existing_customer);
        rv_existing_customer.setVisibility(View.VISIBLE);
        tv_not_customer_found.setVisibility(View.GONE);
        linearLayoutManager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
        rv_existing_customer.setLayoutManager(linearLayoutManager);
        rv_existing_customer.hasFixedSize();
        pb_progressLoader.setVisibility(View.VISIBLE);
    }

    public BroadcastReceiver UPDATEDATA=new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction()!=null){
                Log.d("runBroadcast","true");
                pb_progressLoader.setVisibility(View.VISIBLE);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        //Do something
                        getSavedCustomerList();
                    }
                }, 1500);
            }
        }
    };

    private void getSavedCustomerList() {
        mList.clear();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, PayKreditAPI.INVOICE_CUSTOMER_LIST, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("loadUsers", response + "");
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String message = jsonObject.getString("message");
                    String status = jsonObject.getString("status");
                    if (status.equalsIgnoreCase("1")) {
                        JSONArray jsonArray = jsonObject.getJSONArray("data");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject mData = jsonArray.getJSONObject(i);
                            String invoice_id = mData.getString("tbl_invoice_customer_id");
                            String cus_name = mData.getString("customer_name");
                            String cus_mobile = mData.getString("customer_mobile");
                            String cus_email = mData.getString("customer_email");
                            String comp_name = mData.getString("company_name");
                            String gstin_no = mData.getString("GSTIN");
                            String gistin_add = mData.getString("GSTINAddress");
                            String cin_no = mData.getString("CIN");
                            String bill_add = mData.getString("billing_address");
                            mList.add(new ExistingCustomerModel("" + invoice_id
                                    , "" + cus_name
                                    , "" + cus_mobile
                                    , "" + cus_email
                                    , "" + comp_name
                                    , "" + gstin_no
                                    , "" + gistin_add
                                    , "" + cin_no
                                    , "" + bill_add));
                        }
                        setAdapterNotify();
                    } else {
                        Toast.makeText(getContext(), "" + message, Toast.LENGTH_SHORT).show();
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
                Log.e("loadUsers", "" + params);
                return params;
            }
        };
        RequestQueue requestQueue = SingletonRequestQueue.getInstance(getContext()).getRequestQueue();
        requestQueue.add(stringRequest);
    }

    private void setAdapterNotify() {
        pb_progressLoader.setVisibility(View.GONE);
        existingCustomerAdapter = new ExistingCustomerAdapter(getContext(), mList, ExistingCusFragment.this);
        rv_existing_customer.setAdapter(existingCustomerAdapter);
        existingCustomerAdapter.notifyDataSetChanged();
    }


    @Override
    public void deleteCustomer(String u_id, int pos) {
        deleteUser(u_id, pos);
    }

    @Override
    public void editCustomer(String u_id, int pos) {
        isEditing = true;
        Invoice_Customer_ID = mList.get(pos).getTbl_invoice_customer_id();
        editClickFoundRequest.editClickFoundRequest(u_id, pos);
        et_customer_name.setText(mList.get(pos).getCustomer_name());
        et_mobile_number.setText(mList.get(pos).getCustomer_mobile());
        et_company_name.setText(mList.get(pos).getCompany_name());
        et_customer_address.setText(mList.get(pos).getBilling_address());
        et_email_address.setText(mList.get(pos).getCustomer_email());
        et_gstin_number.setText(mList.get(pos).getGSTIN());
        et_gstin_address.setText(mList.get(pos).getGSTINAddress());
        et_cin_number.setText(mList.get(pos).getCIN());
    }

    @Override
    public void refreshList() {

    }

    private void deleteUser(String delete_cus_id, int position) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, PayKreditAPI.DELETE_INVOICE_CUSTOMER, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("DeleteUsers", response + "");
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String message = jsonObject.getString("message");
                    String status = jsonObject.getString("status");
                    if (status.equalsIgnoreCase("1")) {
                        Toast.makeText(getContext(), "Delete Customer", Toast.LENGTH_SHORT).show();
                        adapterRefreshAfterDelete(position);
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
                params.put("tbl_invoice_customer_id", delete_cus_id);
                Log.e("DeleteUsers", "" + params);
                return params;
            }
        };
        RequestQueue requestQueue = SingletonRequestQueue.getInstance(getContext()).getRequestQueue();
        requestQueue.add(stringRequest);
    }

    private void adapterRefreshAfterDelete(int position) {
        existingCustomerAdapter.notifyItemRemoved(position);
    }

    @Override
    public void onResume() {
        super.onResume();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //Do something
                getSavedCustomerList();
            }
        }, 1500);
    }
}
