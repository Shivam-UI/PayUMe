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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.lgt.paykredit.Activities.ActivityAddedProducts;
import com.lgt.paykredit.Adapter.AdapterAddedProducts;
import com.lgt.paykredit.Models.ModelAddedProducts;
import com.lgt.paykredit.R;
import com.lgt.paykredit.bottomsheets.BottomSheetAddItems;
import com.lgt.paykredit.extras.LoadInvoiceData;
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

public class ExistingProduct extends Fragment {

    private TextView tvNoProductsFound;
    private RecyclerView rvAddedProducts;
    private List<ModelAddedProducts> list;
    private AdapterAddedProducts adapterAddedProducts;
    private LinearLayout llAddProducts;
    private ImageView ivBackSingleUserTransaction;
    private ProgressBar pbAddedProducts;
    private SearchView searchView;
    private String mUserID;
    private SharedPreferences sharedPreferences; // ProductUpdate

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View mView = inflater.inflate(R.layout.existing_product_fragment, container, false);
        initView(mView);
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(UPDATEPRODUCT, new IntentFilter("ProductUpdate"));
        return mView;
    }

    private void initView(View mView) {
        sharedPreferences = getActivity().getSharedPreferences("USER_DATA", MODE_PRIVATE);
        rvAddedProducts = mView.findViewById(R.id.rvAddedProducts);
        llAddProducts = mView.findViewById(R.id.llAddProducts);
        tvNoProductsFound = mView.findViewById(R.id.tvNoProductsFound);
        pbAddedProducts = mView.findViewById(R.id.pbAddedProducts);
        llAddProducts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BottomSheetAddItems bottomSheetAddItems = new BottomSheetAddItems();
                bottomSheetAddItems.show(getActivity().getSupportFragmentManager(), "BottomSheetAddItems");
            }
        });

        if (sharedPreferences.contains("KEY_USER_ID")) {
            mUserID = sharedPreferences.getString("KEY_USER_ID", "");
        }
    }

    public BroadcastReceiver UPDATEPRODUCT = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            if (intent.getAction() != null) {
                pbAddedProducts.setVisibility(View.VISIBLE);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        //Do something
                        loadAddedProducts();
                    }
                }, 1500);

            }
        }
    };


    public void loadAddedProducts() {
        list = new ArrayList<>();
        list.clear();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, PayKreditAPI.INVOICE_PRODUCT_LIST, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("response", "" + response);
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
                                Log.d("listData", "" + jsonArray.get(i));
                                String tbl_invoice_products_id = object.getString("tbl_invoice_products_id");
                                String products_name = object.getString("products_name");
                                String HSN_code = object.getString("HSN_code");
                                String quantity = object.getString("quantity");
                                String price = object.getString("price");
                                String advance = object.getString("advance");
                                String discount = object.getString("discount");
                                String tax = object.getString("tax");
                                String final_discount = object.getString("final_discount");
                                String final_tax_amount = object.getString("final_tax_amount");
                                String email_id = "";
                                ModelAddedProducts modelAddedProducts = new ModelAddedProducts(tbl_invoice_products_id, products_name, HSN_code, price, discount, tax, quantity);
                                modelAddedProducts.setAdvance(advance);
                                modelAddedProducts.setFinal_discount(final_discount);
                                modelAddedProducts.setFinal_tax_amount(final_tax_amount);
                                list.add(modelAddedProducts);
                            }
                            pbAddedProducts.setVisibility(View.GONE);
                            adapterAddedProducts = new AdapterAddedProducts(list, getContext());
                            rvAddedProducts.hasFixedSize();
                            rvAddedProducts.setNestedScrollingEnabled(false);
                            rvAddedProducts.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));
                            rvAddedProducts.setAdapter(adapterAddedProducts);
                            adapterAddedProducts.notifyDataSetChanged();
                        }
                    } else {
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
                Toast.makeText(getContext(), "Network or server error", Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("user_id", mUserID);
                return params;
            }
        };
        RequestQueue requestQueue = SingletonRequestQueue.getInstance(getContext()).getRequestQueue();
        requestQueue.add(stringRequest);
    }

    @Override
    public void onResume() {
        super.onResume();
        pbAddedProducts.setVisibility(View.VISIBLE);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //Do something
                loadAddedProducts();
            }
        }, 1500);
    }
}
