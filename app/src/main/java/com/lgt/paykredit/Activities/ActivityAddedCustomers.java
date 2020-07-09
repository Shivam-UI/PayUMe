package com.lgt.paykredit.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
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
import com.lgt.paykredit.Adapter.AdapterAddedCustomers;
import com.lgt.paykredit.Models.ModelAddedCustomers;
import com.lgt.paykredit.R;
import com.lgt.paykredit.bottomsheets.BottomSheetAddCustomerDetails;
import com.lgt.paykredit.extras.PayKreditAPI;
import com.lgt.paykredit.extras.SingletonRequestQueue;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ActivityAddedCustomers extends AppCompatActivity {

    private List<ModelAddedCustomers> list;
    private RecyclerView rvAddedCustomers;
    private AdapterAddedCustomers addedCustomers;

    private TextView tvToolbarTitle,tvNoCustomersFound;
    private ImageView ivBackSingleUserTransaction;

    private LinearLayout llAddCustomers;

    private ProgressBar pbAddedCustomers;

    private String mUserID;

    private SharedPreferences sharedPreferences;

    public static ActivityAddedCustomers activityAddedCustomers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_added_customers);

        activityAddedCustomers = this;

        sharedPreferences = ActivityAddedCustomers.this.getSharedPreferences("USER_DATA", MODE_PRIVATE);
        if (sharedPreferences.contains("KEY_USER_ID")) {
            mUserID = sharedPreferences.getString("KEY_USER_ID", "");
        }

        rvAddedCustomers = findViewById(R.id.rvAddedCustomers);
        tvToolbarTitle = findViewById(R.id.tvToolbarTitle);
        ivBackSingleUserTransaction = findViewById(R.id.ivBackSingleUserTransaction);
        pbAddedCustomers = findViewById(R.id.pbAddedCustomers);
        llAddCustomers = findViewById(R.id.llAddCustomers);
        tvNoCustomersFound = findViewById(R.id.tvNoCustomersFound);

        tvToolbarTitle.setText("Customers");

        ivBackSingleUserTransaction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        llAddCustomers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BottomSheetAddCustomerDetails addCustomerDetails = new BottomSheetAddCustomerDetails();
                addCustomerDetails.show(getSupportFragmentManager(), "BottomSheetAddCustomerDetails");
            }
        });
        loadCustomers();
    }

    public static ActivityAddedCustomers getInstance(){
        return activityAddedCustomers;
    }

    public void loadCustomers() {

        pbAddedCustomers.setVisibility(View.VISIBLE);

        list = new ArrayList<>();
        list.clear();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, PayKreditAPI.INVOICE_CUSTOMER_LIST, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                pbAddedCustomers.setVisibility(View.GONE);

                try {
                    JSONObject jsonObject = new JSONObject(response);

                    String message = jsonObject.getString("message");
                    String status = jsonObject.getString("status");

                    if (status.equalsIgnoreCase("1")) {

                        JSONArray jsonArray = jsonObject.getJSONArray("data");

                        if (jsonArray.length() > 0) {

                            for (int i = 0; i < jsonArray.length(); i++) {

                                JSONObject object = jsonArray.getJSONObject(i);

                                String tbl_invoice_customer_id = object.getString("tbl_invoice_customer_id");
                                String customer_name = object.getString("customer_name");
                                String customer_mobile = object.getString("customer_mobile");
                                String customer_email = object.getString("customer_email");
                                String billing_address = object.getString("billing_address");

                                list.add(new ModelAddedCustomers(tbl_invoice_customer_id, customer_name, "", customer_mobile, customer_email, billing_address, ""));
                                // list.add(new ModelAddedCustomers("Customer 1","","01234567890","abc@gmail.com","G 25, Shaheen Bagh, Delhi","GST123"));
                            }

                            addedCustomers = new AdapterAddedCustomers(list, ActivityAddedCustomers.this);
                            rvAddedCustomers.setNestedScrollingEnabled(false);
                            rvAddedCustomers.hasFixedSize();
                            rvAddedCustomers.setLayoutManager(new LinearLayoutManager(ActivityAddedCustomers.this, RecyclerView.VERTICAL, false));
                            rvAddedCustomers.setAdapter(addedCustomers);
                            addedCustomers.notifyDataSetChanged();
                        }

                        else {
                            Toast.makeText(ActivityAddedCustomers.this, ""+message, Toast.LENGTH_SHORT).show();
                            tvNoCustomersFound.setVisibility(View.VISIBLE);
                        }

                    }

                    else {
                        tvNoCustomersFound.setVisibility(View.VISIBLE);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                pbAddedCustomers.setVisibility(View.GONE);

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("user_id", mUserID);
                return params;
            }
        };

        RequestQueue requestQueue = SingletonRequestQueue.getInstance(ActivityAddedCustomers.this).getRequestQueue();
        requestQueue.add(stringRequest);


    }
}
