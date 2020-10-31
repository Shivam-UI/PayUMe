package com.lgt.paykredit.Fragments;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.lgt.paykredit.R;
import com.lgt.paykredit.extras.PayKreditAPI;
import com.lgt.paykredit.extras.SingletonRequestQueue;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.HashMap;
import java.util.Map;
import static android.content.Context.MODE_PRIVATE;
import static com.lgt.paykredit.Activities.ActivityAddedProducts.vp_add_Product_tab;

public class AddNewProduct extends Fragment {
    TextView tvSaveItem;
    ProgressBar pbAddItems;
    EditText etItemName,etHSNCode,etRatePerUnit,etDiscountPercentage,etTaxPercentage,etRemarksProduct,etQuantityItems,etAdvancePrice;
    private String stItemName,stHSNCode,stRatePerUnit,stDiscountPercent,stTaxPercentage,stRemarkProduct,mUserID,etQuantityItm,etAdvanceAdd;
    private SharedPreferences sharedPreferences;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View mView = inflater.inflate(R.layout.add_new_product,container,false);
        initView(mView);
        return mView;
    }

    private void initView(View mView) {
        sharedPreferences = getActivity().getSharedPreferences("USER_DATA", MODE_PRIVATE);
        if (sharedPreferences.contains("KEY_USER_ID")) {
            mUserID = sharedPreferences.getString("KEY_USER_ID", "");
            Log.e("Sadrrewrew", mUserID);
        }
        pbAddItems=mView.findViewById(R.id.pbAddItems);
        tvSaveItem=mView.findViewById(R.id.tvSaveItem);
        etItemName=mView.findViewById(R.id.etItemName);
        etAdvancePrice=mView.findViewById(R.id.etAdvancePrice);
        etQuantityItems=mView.findViewById(R.id.etQuantityItems);
        etHSNCode=mView.findViewById(R.id.etHSNCode);
        etRatePerUnit=mView.findViewById(R.id.etRatePerUnit);
        etDiscountPercentage=mView.findViewById(R.id.etDiscountPercentage);
        etTaxPercentage=mView.findViewById(R.id.etTaxPercentage);
        etRemarksProduct=mView.findViewById(R.id.etRemarksProduct);

        tvSaveItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validateAddProduct();
            }
        });
    }

    private void validateAddProduct() {
        stItemName=etItemName.getText().toString().trim();
        stHSNCode=etHSNCode.getText().toString().trim();
        etQuantityItm=etQuantityItems.getText().toString().trim();
        etAdvanceAdd=etAdvancePrice.getText().toString().trim();
        stRatePerUnit=etRatePerUnit.getText().toString().trim();
        stDiscountPercent=etDiscountPercentage.getText().toString().trim();
        stTaxPercentage=etTaxPercentage.getText().toString().trim();
        stRemarkProduct=etRemarksProduct.getText().toString().trim();

        if (TextUtils.isEmpty(stItemName)){
            etItemName.setError("Enter Item Name");
            etItemName.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(stHSNCode)){
            etHSNCode.setError("Enter HSN Code");
            etHSNCode.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(etQuantityItm)){
            etQuantityItems.setError("Enter Product Quantity");
            etQuantityItems.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(etAdvanceAdd)){
            etAdvancePrice.setError("Enter Advance Amount");
            etAdvancePrice.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(stRatePerUnit)){
            etRatePerUnit.setError("Enter Rate Per Unit");
            etRatePerUnit.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(stDiscountPercent)){
            etDiscountPercentage.setError("Enter Discount in number");
            etDiscountPercentage.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(stTaxPercentage)){
            etTaxPercentage.setError("Enter Tax in number");
            etTaxPercentage.requestFocus();
            return;
        }
        addNewProductItem();
    }

    private void addNewProductItem() {
        pbAddItems.setVisibility(View.VISIBLE);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, PayKreditAPI.ADD_INVOICE_PRODUCT_API, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("addProduct", response+"");
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String message = jsonObject.getString("message");
                    String status = jsonObject.getString("status");
                    if (status.equalsIgnoreCase("1")) {
                        etItemName.setText("");
                        etHSNCode.setText("");
                        etQuantityItems.setText("");
                        etRatePerUnit.setText("");
                        etAdvancePrice.setText("");
                        etDiscountPercentage.setText("");
                        etTaxPercentage.setText("");
                        etRemarksProduct.setText("");
                        Toast.makeText(getContext(), ""+message, Toast.LENGTH_SHORT).show();
                        Intent intent=new Intent("ProductUpdate");
                        LocalBroadcastManager.getInstance(getActivity()).sendBroadcast(intent);
                        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
                        pbAddItems.setVisibility(View.GONE);
                        vp_add_Product_tab.setCurrentItem(2);
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
                pbAddItems.setVisibility(View.GONE);
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("user_id", mUserID);
                params.put("products_name", stItemName);
                params.put("HSN_code", stHSNCode);
                params.put("quantity", etQuantityItm);
                params.put("price", stRatePerUnit);
                params.put("advance", etAdvanceAdd);
                params.put("discount", stDiscountPercent);
                params.put("tax", stTaxPercentage);
                params.put("remark", stRemarkProduct);
                Log.e("addProduct", "" + params);
                return params;
            }
        };
        RequestQueue requestQueue = SingletonRequestQueue.getInstance(getContext()).getRequestQueue();
        requestQueue.add(stringRequest);
    }
}
