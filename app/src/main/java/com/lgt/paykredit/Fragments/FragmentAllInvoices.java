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

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.lgt.paykredit.Activities.ActivityCreateInvoice;
import com.lgt.paykredit.Activities.ActivityInvoiceDescription;
import com.lgt.paykredit.Activities.ActivityWebView;
import com.lgt.paykredit.Activities.CreateInvoice;
import com.lgt.paykredit.Adapter.AdapterAllInvoices;
import com.lgt.paykredit.Adapter.ExistingCustomerAdapter;
import com.lgt.paykredit.Models.ExistingCustomerModel;
import com.lgt.paykredit.Models.ModelAllInvoices;
import com.lgt.paykredit.R;
import com.lgt.paykredit.extras.OpenDetailsInvoice;
import com.lgt.paykredit.extras.PayKreditAPI;
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


public class FragmentAllInvoices extends Fragment implements OpenDetailsInvoice {

    private static final String TAG = "FragmentAllInvoices";
    private ProgressBar pb_progressBarListAll;
    private RecyclerView rvAllInvoices;
    private List<ModelAllInvoices> listAllInvoices;
    private AdapterAllInvoices adapterAllInvoices;
    private SharedPreferences sharedPreferences;
    private String mUserID,urlToOpen,urlType;
    private LinearLayout llAddInvoice_Paid;
  //  SharedData sharedData;
    private CharSequence aaa;

    public FragmentAllInvoices() {
    }

    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            CharSequence message = intent.getCharSequenceExtra("INPUT_DATA");
            if (message != null) {
                if (adapterAllInvoices != null) {
                    if (message.toString().length() > 0) {
                        startSearch(message.toString());
                    }else {
                        setNotifyAdapter();
                    }
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
        View view = inflater.inflate(R.layout.fragment_all_invoices, container, false);
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(mMessageReceiver,
                new IntentFilter("FILTER_EVENT"));

        sharedPreferences = getActivity().getSharedPreferences("USER_DATA", MODE_PRIVATE);
        if (sharedPreferences.contains("KEY_USER_ID")) {
            mUserID = sharedPreferences.getString("KEY_USER_ID", "");
        }
     //   sharedData = (SharedData) getActivity().getApplicationContext();+

        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(
                UPDATEDATA,new IntentFilter("UPDATE_CUSTOMER"));

        pb_progressBarListAll = view.findViewById(R.id.pb_progressBarListAll);
        rvAllInvoices = view.findViewById(R.id.rvPaidInvoice);
        llAddInvoice_Paid = view.findViewById(R.id.llAddInvoice_Paid);

        llAddInvoice_Paid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), ActivityCreateInvoice.class));
            }
        });
        listAllInvoices = new ArrayList<>();
        return view;
    }

    public BroadcastReceiver UPDATEDATA=new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction()!=null){
                Log.d("runBroadcast","true");
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        //Do something
                        pb_progressBarListAll.setVisibility(View.VISIBLE);
                        loadPaidRecyclerView();
                    }
                }, 1500);
            }
        }
    };



    private void startSearch(String key) {
        ArrayList<ModelAllInvoices> tempLits = new ArrayList<>();
        for (ModelAllInvoices temp : listAllInvoices) {
            if (temp.getCustomer_name().toLowerCase().contains(key.toLowerCase())) {
                tempLits.add(temp);
            }
        }
        adapterAllInvoices = new AdapterAllInvoices(tempLits, getActivity(),FragmentAllInvoices.this);
        rvAllInvoices.setAdapter(adapterAllInvoices);
        adapterAllInvoices.notifyDataSetChanged();
    }


    private void loadPaidRecyclerView() {
        listAllInvoices.clear();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, INVOICE_LIST_API, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                pb_progressBarListAll.setVisibility(View.GONE);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    Log.d("invoiceListttttt",jsonObject.toString());
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
                            modelAllInvoices.setTbl_invoice_customer_id(data.getString("tbl_invoice_customer_id"));
                            modelAllInvoices.setCustomer_name(data.getString("customer_name"));
                            listAllInvoices.add(modelAllInvoices);
                        }
                        setNotifyAdapter();
                    }else {
                        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    pb_progressBarListAll.setVisibility(View.GONE);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("invoiceList",error.toString());
                pb_progressBarListAll.setVisibility(View.GONE);
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> param = new HashMap<>();
                param.put("user_id",mUserID);
                param.put("type","All");
                Log.d("invoiceList",param.toString());
                return param;
            }
        };
        RequestQueue requestQueue = SingletonRequestQueue.getInstance(getContext()).getRequestQueue();
        requestQueue.add(stringRequest);
    }

    private void setNotifyAdapter() {
        adapterAllInvoices = new AdapterAllInvoices(listAllInvoices, getActivity(),FragmentAllInvoices.this);
        rvAllInvoices.hasFixedSize();
        rvAllInvoices.setNestedScrollingEnabled(false);
        rvAllInvoices.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false));
        rvAllInvoices.setAdapter(adapterAllInvoices);
    }

    @Override
    public void onResume() {
        super.onResume();
        pb_progressBarListAll.setVisibility(View.VISIBLE);
        loadPaidRecyclerView();
    }

    @Override
    public void ShowInvoiceDetails(String invoiceID) {
        Intent detailsIntent = new Intent(getContext(), ActivityInvoiceDescription.class);
        detailsIntent.putExtra("testID",invoiceID);
        detailsIntent.putExtra("type","paid");
        startActivity(detailsIntent);
    }
}
