package com.lgt.paykredit.Activities;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.lgt.paykredit.Adapter.AdapterAllInvoices;
import com.lgt.paykredit.Adapter.DefaultInvoiceAdapter;
import com.lgt.paykredit.Fragments.FragmentAllInvoices;
import com.lgt.paykredit.Models.DefaultModel;
import com.lgt.paykredit.Models.ModelAllInvoices;
import com.lgt.paykredit.R;
import com.lgt.paykredit.extras.SingletonRequestQueue;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.lgt.paykredit.Activities.InvoiceMainPage.tvTotalDueAmt;
import static com.lgt.paykredit.Activities.InvoiceMainPage.tvTotalSale;
import static com.lgt.paykredit.extras.PayKreditAPI.DEFAULT_INVOICE_API;
import static com.lgt.paykredit.extras.PayKreditAPI.INVOICE_LIST_API;

public class ActivityDefaultInvoice extends AppCompatActivity {
    ArrayList<DefaultModel> mListData;
    RecyclerView rv_defaultInvoiceList;
    TextView tv_NoDefaultInvoice,tvToolbarTitle;
    DefaultInvoiceAdapter defaultInvoiceAdapter;
    private SharedPreferences sharedPreferences;
    ImageView ivBackSingleUserTransaction;
    private String mUserID;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.invoice_default_activity);
        initView();
        sharedPreferences = ActivityDefaultInvoice.this.getSharedPreferences("USER_DATA", MODE_PRIVATE);
        if (sharedPreferences.contains("KEY_USER_ID")) {
            mUserID = sharedPreferences.getString("KEY_USER_ID", "");
        }
    }

    private void initView() {
        tvToolbarTitle=findViewById(R.id.tvToolbarTitle);
        rv_defaultInvoiceList=findViewById(R.id.rv_defaultInvoiceList);
        tv_NoDefaultInvoice=findViewById(R.id.tv_NoDefaultInvoice);
        ivBackSingleUserTransaction=findViewById(R.id.ivBackSingleUserTransaction);
        tvToolbarTitle=findViewById(R.id.tvToolbarTitle);
        ivBackSingleUserTransaction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        tvToolbarTitle.setText("Defaulters List");
        mListData=new ArrayList<>();
        mListData.clear();
        showDefaultInvoice();
    }

    public void showDefaultInvoice(){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, DEFAULT_INVOICE_API, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    Log.d("DefaultersList",jsonObject.toString());
                    String message=jsonObject.getString("message");
                    String status=jsonObject.getString("status");
                    if (status.equalsIgnoreCase("1")){
                        JSONArray jsonArray = jsonObject.getJSONArray("data");
                        for (int i=0;i<jsonArray.length();i++){
                            JSONObject data = jsonArray.getJSONObject(i);
                            String tbl_invoice_customer_id =data.getString("tbl_invoice_customer_id");
                            String customer_name =data.getString("customer_name");
                            String customer_mobile =data.getString("customer_mobile");
                            String defaulter_date =data.getString("defaulter_date");
                            mListData.add(new DefaultModel(tbl_invoice_customer_id,customer_name,customer_mobile,defaulter_date));
                        }
                        defaultInvoiceAdapter = new DefaultInvoiceAdapter(ActivityDefaultInvoice.this, mListData);
                        rv_defaultInvoiceList.hasFixedSize();
                        rv_defaultInvoiceList.setNestedScrollingEnabled(false);
                        rv_defaultInvoiceList.setLayoutManager(new LinearLayoutManager(ActivityDefaultInvoice.this, RecyclerView.VERTICAL, false));
                        rv_defaultInvoiceList.setAdapter(defaultInvoiceAdapter);
                    }else {
                        Toast.makeText(ActivityDefaultInvoice.this, message, Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("DefaultersList",error.toString());
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> param = new HashMap<>();
                param.put("user_id",mUserID);
                Log.d("DefaultersList",param.toString());
                return param;
            }
        };
        RequestQueue requestQueue = SingletonRequestQueue.getInstance(ActivityDefaultInvoice.this).getRequestQueue();
        requestQueue.add(stringRequest);
    }
}
