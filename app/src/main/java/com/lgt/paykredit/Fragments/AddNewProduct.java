package com.lgt.paykredit.Fragments;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
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
import com.lgt.paykredit.Models.CustomArrayAdapter;
import com.lgt.paykredit.R;
import com.lgt.paykredit.extras.Common;
import com.lgt.paykredit.extras.PayKreditAPI;
import com.lgt.paykredit.extras.SingletonRequestQueue;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.content.Context.MODE_PRIVATE;
import static com.lgt.paykredit.Activities.ActivityAddedProducts.vp_add_Product_tab;
import static com.lgt.paykredit.extras.PayKreditAPI.TAX_SLAB_API;

public class AddNewProduct extends Fragment {
    TextView tvSaveItem, tv_taxAmt, tv_taxFinalAmt;
    ProgressBar pbAddItems;
    public static boolean IsClickToAdd = false;
    public static String ProductId, ProductName, ProductAmt, ProductDis, ProductQua, ProductTax, AdvanceAmt, itemPrice, ItemDue, ItemDiscount,ItemTotalTaxAmt,ItemAfterDiscount;
    public static String PID,PNAME,PDISCOUNT,PQUANTITY,PRATE,PTAX,FINALPRICE,FINALTAXAFTERDISCOUNT,TOTALAMOUNT,TOTALDISCOUNTAMOUNT,FINALPAYTOME;
    EditText etItemName, etHSNCode, etRatePerUnit, etDiscountPercentage, etRemarksProduct, etQuantityItems;
    Spinner etTaxPercentage;
    private String stItemName, stHSNCode, stRatePerUnit, stDiscountPercent, mFinalDiscount, mFinalTaxAmt, mUserID, etQuantityItm, etAdvanceAdd;
    private SharedPreferences sharedPreferences;
    CustomArrayAdapter customArrayAdapter;
    List<String> categories = new ArrayList<String>();
    String value;
    float Discount_after;
    private Map<String, String> getText = new HashMap<>();

    @Nullable
    @Override

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View mView = inflater.inflate(R.layout.add_new_product, container, false);
        initView(mView);
        return mView;
    }

    private void initView(View mView) {
        sharedPreferences = getActivity().getSharedPreferences("USER_DATA", MODE_PRIVATE);
        if (sharedPreferences.contains("KEY_USER_ID")) {
            mUserID = sharedPreferences.getString("KEY_USER_ID", "");
            Log.e("Sadrrewrew", mUserID);
        }
        setSpinnerTexData();
        tv_taxAmt = mView.findViewById(R.id.tv_taxAmt);
        tv_taxFinalAmt = mView.findViewById(R.id.tv_taxFinalAmt);
        pbAddItems = mView.findViewById(R.id.pbAddItems);
        tvSaveItem = mView.findViewById(R.id.tvSaveItem);
        etItemName = mView.findViewById(R.id.etItemName);
        etQuantityItems = mView.findViewById(R.id.etQuantityItems);
        etHSNCode = mView.findViewById(R.id.etHSNCode);
        etRatePerUnit = mView.findViewById(R.id.etRatePerUnit);
        etDiscountPercentage = mView.findViewById(R.id.etDiscountPercentage);
        etTaxPercentage = mView.findViewById(R.id.etTaxPercentage);
        etRemarksProduct = mView.findViewById(R.id.etRemarksProduct);
        tvSaveItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validateAddProduct();
            }
        });
        etDiscountPercentage.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {
                if (etRatePerUnit.getText().length() == 0 && TextUtils.isEmpty(etRatePerUnit.getText().toString())) {
                    Toast.makeText(getActivity(), "Please Enter Rate", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int count, int after) {
                Log.d("DiscountPercentage", "" + charSequence);
                if (charSequence.toString().length()> 0) {
                    startCalculation(charSequence.toString());
                } else {
                    tv_taxAmt.setText("00.00");
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        etItemName.requestFocus();
    }

    private void setSpinnerTexData() {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, TAX_SLAB_API, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("TaxData", "" + response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String message = jsonObject.getString("message");
                    String status = jsonObject.getString("status");
                    if (status.equalsIgnoreCase("1")) {
                        JSONArray jsonArray = jsonObject.getJSONArray("data");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                            // String tbl_tax_slab_id = jsonObject1.getString("tbl_tax_slab_id");
                            String tax_slab = jsonObject1.getString("tax_slab");
                            String value = jsonObject1.getString("value");
                            getText.put(tax_slab, value);

                            categories.add(tax_slab);
                            Log.d("TaxData", "\n :" + tax_slab + ",get HASH" + getText.toString() + "");
                        }
                        setData();
                    } else {
                        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("TaxData", "TaxData : " + error.getMessage());
            }
        });
        RequestQueue requestQueue = SingletonRequestQueue.getInstance(getContext()).getRequestQueue();
        requestQueue.add(stringRequest);
    }

    private void setData() {
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, categories);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        etTaxPercentage.setAdapter(dataAdapter);
        etTaxPercentage.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (!TextUtils.isEmpty(etRatePerUnit.getText())) {
                    // Toast.makeText(getActivity(), "You Select : " +categories.get(i), Toast.LENGTH_SHORT).show();
                    if (getText.containsKey(categories.get(i))) {
                        value = getText.get(categories.get(i));
                        Log.d("TaxData", value + "");
                        startTaxCalculation(value);
                    }
                } else {
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
    }

    private void startTaxCalculation(String value) {
        if (etQuantityItems.getText().toString().trim().length()>0){
            if (Discount_after != 0) {
                float Tax = Float.parseFloat(value);
                float FinalTaxAmt = ((Discount_after * Tax) / 100);
                mFinalTaxAmt = String.valueOf(FinalTaxAmt);
                double afterQuantityTaxCalc = FinalTaxAmt*Integer.parseInt(etQuantityItems.getText().toString().trim());
                tv_taxFinalAmt.setText("" + afterQuantityTaxCalc);
                Log.d("DiscountPercentage", "" + FinalTaxAmt + "        :----- "+ afterQuantityTaxCalc);
            } else {
                Toast.makeText(getActivity(), "Please Enter Amt", Toast.LENGTH_SHORT).show();
            }
        }else{
            Toast.makeText(getActivity(), "Please Select Quantity!", Toast.LENGTH_SHORT).show();
        }
    }


    private void startCalculation(String value) {
        if (etQuantityItems.getText().toString().trim().length()>0){
            Log.d("discount","discount_value:"+value);
            if (etRatePerUnit.getText().toString().trim().length()>0) {
                double Discount = Integer.parseInt(value);
                double Amt = Double.parseDouble(etRatePerUnit.getText().toString().trim());
                double TexAmt = (Amt * Discount) / 100;
                Discount_after = Float.valueOf(String.valueOf(Amt))-Float.valueOf(String.valueOf(TexAmt));
                double afterQuantityCalc = TexAmt*Integer.parseInt(etQuantityItems.getText().toString().trim());
                Log.d("discount","After Discount: "+Discount_after+"  final_value:"+TexAmt+"  :--- "+afterQuantityCalc);
                tv_taxAmt.setText(""+String.valueOf(afterQuantityCalc));
            } else {
                Toast.makeText(getActivity(), "Enter Rate First", Toast.LENGTH_SHORT).show();
            }
        }else{
            Toast.makeText(getActivity(), "Please Select Quantity!", Toast.LENGTH_SHORT).show();
        }
    }

    private void validateAddProduct() {
        stItemName = etItemName.getText().toString().trim();
        stHSNCode = etHSNCode.getText().toString().trim();
        etQuantityItm = etQuantityItems.getText().toString().trim();
        stRatePerUnit = etRatePerUnit.getText().toString().trim();
        stDiscountPercent = etDiscountPercentage.getText().toString().trim();
        mFinalDiscount = tv_taxAmt.getText().toString().trim();
        mFinalTaxAmt = tv_taxFinalAmt.getText().toString().trim();

        if (TextUtils.isEmpty(stRatePerUnit)) {
            etRatePerUnit.setError("Enter Rate Per Unit");
            etRatePerUnit.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(stDiscountPercent)) {
            etDiscountPercentage.setError("Enter Discount in number");
            etDiscountPercentage.requestFocus();
            return;
        }
        addNewProductItem();
    }

    private void addNewProductItem() {
        pbAddItems.setVisibility(View.VISIBLE);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, PayKreditAPI.ADD_INVOICE_PRODUCT_API, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("addProduct", response + "");
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String message = jsonObject.getString("message");
                    String status = jsonObject.getString("status");
                    if (status.equalsIgnoreCase("1")) {
                        IsClickToAdd = true;
                        JSONObject data = jsonObject.getJSONObject("data");
                        String tbl_invoice_products_id = data.getString("tbl_invoice_products_id");
                        String products_name = data.getString("products_name");
                        String HSN_code = data.getString("HSN_code");
                        String quantity = data.getString("quantity");
                        String price = data.getString("price");
                        String discount = data.getString("discount");
                        String tax = data.getString("tax");
                        String rate = data.getString("rate");
                        String final_discount = data.getString("final_discount");
                        String final_tax_amount = data.getString("final_tax_amount");

                        PID = tbl_invoice_products_id;
                        PNAME = products_name;
                        PRATE = price;
                        PQUANTITY = quantity;
                        PDISCOUNT = discount;
                        TOTALAMOUNT = String.valueOf(Float.valueOf(price)*Integer.parseInt(quantity));
                        TOTALDISCOUNTAMOUNT = String.valueOf(Float.valueOf(final_discount));
                        PTAX = tax;
                        FINALTAXAFTERDISCOUNT =final_tax_amount;
                        FINALPRICE =final_discount;
                        float finalPayToMe = Float.parseFloat(final_tax_amount);
                        float ToMe = Float.parseFloat(TOTALDISCOUNTAMOUNT) + finalPayToMe;
                        FINALPAYTOME = String.valueOf(ToMe);
                        etItemName.setText("");
                        etHSNCode.setText("");
                        etQuantityItems.setText("");
                        etRatePerUnit.setText("");
                        etDiscountPercentage.setText("");
                        etRemarksProduct.setText("");
                        pbAddItems.setVisibility(View.GONE);
                        Toast.makeText(getContext(), "" + message, Toast.LENGTH_SHORT).show();
                        getActivity().onBackPressed();

                        /*Intent intent = new Intent("ProductUpdate");
                        LocalBroadcastManager.getInstance(getActivity()).sendBroadcast(intent);
                        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
                        vp_add_Product_tab.setCurrentItem(2);*/

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
                params.put("discount", stDiscountPercent);
                params.put("rate", stRatePerUnit);
                params.put("final_discount", mFinalDiscount);
                params.put("final_tax_amount", mFinalTaxAmt);
                params.put("tax", value);
                Log.e("addProduct", "" + params);
                return params;
            }
        };
        RequestQueue requestQueue = SingletonRequestQueue.getInstance(getContext()).getRequestQueue();
        requestQueue.add(stringRequest);
    }
}
