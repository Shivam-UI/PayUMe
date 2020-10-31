package com.lgt.paykredit.Fragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.lgt.paykredit.Adapter.AdapterAllInvoices;
import com.lgt.paykredit.Adapter.AdapterUnpaid;
import com.lgt.paykredit.Models.ModelAllInvoices;
import com.lgt.paykredit.Models.ModelUnPaid;
import com.lgt.paykredit.R;
import com.lgt.paykredit.extras.OpenDetailsInvoice;
import com.lgt.paykredit.extras.SingletonRequestQueue;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.content.Context.MODE_PRIVATE;
import static com.lgt.paykredit.Activities.InvoiceMainPage.tvTotalDueAmt;
import static com.lgt.paykredit.Activities.InvoiceMainPage.tvTotalSale;
import static com.lgt.paykredit.extras.PayKreditAPI.INVOICE_LIST_API;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentUnpaid extends Fragment  implements OpenDetailsInvoice {
    private SharedPreferences sharedPreferences;
    private String mUserID;
    private List<ModelAllInvoices> listAllInvoices;
    private AdapterAllInvoices adapterAllInvoices;
    private RecyclerView rvUnPaid;
    private List<ModelUnPaid> listUnPaid;

    private AdapterUnpaid adapterUnpaid;


    public FragmentUnpaid() {
    }

    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            CharSequence message = intent.getCharSequenceExtra("INPUT_DATA2");
            if (message != null) {
                if (adapterUnpaid != null) {
                    adapterUnpaid.getFilter().filter(message.toString());
                }
            }
            Log.e("DASdsadsad", message + "");
        }
    };

    @Override
    public void onDetach() {
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(mMessageReceiver);
        super.onDetach();
    }

    @Override
    public void onDestroy() {
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(mMessageReceiver);

        super.onDestroy();
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_unpaid, container, false);
        sharedPreferences = getActivity().getSharedPreferences("USER_DATA", MODE_PRIVATE);
        if (sharedPreferences.contains("KEY_USER_ID")) {
            mUserID = sharedPreferences.getString("KEY_USER_ID", "");
        }
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(mMessageReceiver,
                new IntentFilter("FILTER_EVENT2"));

        rvUnPaid = view.findViewById(R.id.rvUnPaid);
        listAllInvoices = new ArrayList<>();
        return view;
    }

    private void loadRecyclerView() {
        listAllInvoices.clear();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, INVOICE_LIST_API, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    Log.d("PaidList",jsonObject.toString());
                    String message=jsonObject.getString("message");
                    String status=jsonObject.getString("status");
                    String total_due=jsonObject.getString("total_due");
                    String total_sale=jsonObject.getString("total_sale");
                    tvTotalSale.setText(total_sale);
                    tvTotalDueAmt.setText(total_due);
                    if (status.equalsIgnoreCase("1")){
                        JSONArray jsonArray = jsonObject.getJSONArray("data");
                        for (int i=0;i<jsonArray.length();i++){
                            JSONObject data = jsonArray.getJSONObject(i);
                            ModelAllInvoices modelAllInvoices = new ModelAllInvoices(data.getString("tbl_invoice_customer_id"), "0", "", data.getString("customer_name"), data.getString("total_balance"), data.getString("total_advance"));
                            modelAllInvoices.setPayAdvance(data.getString("sub_total"));
                            listAllInvoices.add(modelAllInvoices);
                        }
                        adapterAllInvoices = new AdapterAllInvoices(listAllInvoices, getActivity(),FragmentUnpaid.this);
                        rvUnPaid.hasFixedSize();
                        rvUnPaid.setNestedScrollingEnabled(false);
                        rvUnPaid.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false));
                        rvUnPaid.setAdapter(adapterAllInvoices);
                    }else {
                        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("PaidList",error.toString());
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> param = new HashMap<>();
                param.put("user_id",mUserID);
                param.put("type","due");
                Log.d("PaidList",param.toString());
                return param;
            }
        };
        RequestQueue requestQueue = SingletonRequestQueue.getInstance(getContext()).getRequestQueue();
        requestQueue.add(stringRequest);
    }

    @Override
    public void onResume() {
        super.onResume();
        loadRecyclerView();
    }

    @Override
    public void ShowInvoiceDetails(String invoiceID) {

    }
}
