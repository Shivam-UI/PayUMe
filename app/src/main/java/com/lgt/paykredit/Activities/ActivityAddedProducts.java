package com.lgt.paykredit.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
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
import com.lgt.paykredit.Adapter.AdapterAddedProducts;
import com.lgt.paykredit.Models.ModelAddedProducts;
import com.lgt.paykredit.R;
import com.lgt.paykredit.bottomsheets.BottomSheetAddItems;
import com.lgt.paykredit.extras.PayKreditAPI;
import com.lgt.paykredit.extras.SingletonRequestQueue;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ActivityAddedProducts extends AppCompatActivity {

    private TextView tvToolbarTitle, tvNoProductsFound;
    private RecyclerView rvAddedProducts;
    private List<ModelAddedProducts> list;
    private AdapterAddedProducts adapterAddedProducts;

    private LinearLayout llAddProducts;
    private ImageView ivBackSingleUserTransaction;

    private ProgressBar pbAddedProducts;

    private SearchView searchView;

    private String mUserID;
    private SharedPreferences sharedPreferences;

    public static ActivityAddedProducts activityAddedProducts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_added_products);

        sharedPreferences = ActivityAddedProducts.this.getSharedPreferences("USER_DATA",MODE_PRIVATE);

        tvToolbarTitle = findViewById(R.id.tvToolbarTitle);
        rvAddedProducts = findViewById(R.id.rvAddedProducts);
        llAddProducts = findViewById(R.id.llAddProducts);
        tvNoProductsFound = findViewById(R.id.tvNoProductsFound);
        pbAddedProducts = findViewById(R.id.pbAddedProducts);


        ivBackSingleUserTransaction = findViewById(R.id.ivBackSingleUserTransaction);

        tvToolbarTitle.setText("Added Products");

        ivBackSingleUserTransaction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        llAddProducts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BottomSheetAddItems bottomSheetAddItems = new BottomSheetAddItems();
                bottomSheetAddItems.show(getSupportFragmentManager(), "BottomSheetAddItems");
            }
        });

        if(sharedPreferences.contains("KEY_USER_ID")){
            mUserID = sharedPreferences.getString("KEY_USER_ID","");
        }


        activityAddedProducts = this;
        loadAddedProducts();


    }

    public static ActivityAddedProducts getInstance()
    {
        return activityAddedProducts;
    }

    public void loadAddedProducts() {

        pbAddedProducts.setVisibility(View.VISIBLE);

        list = new ArrayList<>();
        list.clear();


        StringRequest stringRequest = new StringRequest(Request.Method.POST, PayKreditAPI.INVOICE_PRODUCT_LIST, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                pbAddedProducts.setVisibility(View.GONE);

                try {

                    JSONObject jsonObject = new JSONObject(response);

                    String status = jsonObject.getString("status");
                    String message = jsonObject.getString("message");

                    if (status.equalsIgnoreCase("1")) {
                        JSONArray jsonArray = jsonObject.getJSONArray("data");

                        if (jsonArray.length() > 0) {
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject object = jsonArray.getJSONObject(i);

                                String tbl_invoice_products_id = object.getString("tbl_invoice_products_id");
                                String products_name = object.getString("products_name");
                                String HSN_code = object.getString("HSN_code");
                                String quantity = object.getString("quantity");
                                String price = object.getString("price");
                                String discount = object.getString("discount");
                                String tax = object.getString("tax");
                                String email_id = object.getString("email_id");

                                list.add(new ModelAddedProducts(tbl_invoice_products_id, products_name, HSN_code, price, discount, tax, quantity));
                            }

                            adapterAddedProducts = new AdapterAddedProducts(list, ActivityAddedProducts.this);
                            rvAddedProducts.hasFixedSize();
                            rvAddedProducts.setNestedScrollingEnabled(false);
                            rvAddedProducts.setLayoutManager(new LinearLayoutManager(ActivityAddedProducts.this, RecyclerView.VERTICAL, false));
                            rvAddedProducts.setAdapter(adapterAddedProducts);
                            adapterAddedProducts.notifyDataSetChanged();
                        }
                    }

                    else {
                        tvNoProductsFound.setVisibility(View.VISIBLE);
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                pbAddedProducts.setVisibility(View.GONE);
                Toast.makeText(ActivityAddedProducts.this, "Network or server error", Toast.LENGTH_SHORT).show();


            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();
                params.put("user_id",mUserID);
                return params;
            }
        };

        RequestQueue requestQueue = SingletonRequestQueue.getInstance(ActivityAddedProducts.this).getRequestQueue();
        requestQueue.add(stringRequest);


    }
}
