package com.lgt.paykredit.bottomsheets;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.lgt.paykredit.Activities.ActivityAddedProducts;
import com.lgt.paykredit.Fragments.AddNewProduct;
import com.lgt.paykredit.R;
import com.lgt.paykredit.extras.PayKreditAPI;
import com.lgt.paykredit.extras.SingletonRequestQueue;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.lgt.paykredit.extras.PayKreditAPI.TAX_SLAB_API;

/**
 * A simple {@link Fragment} subclass.
 */

public class BottomSheetAddItems extends BottomSheetDialogFragment {

    private EditText etItemName, etHSNCode, etRatePerUnit, etDiscountPercentage, etRemarksProduct, etBtmQuantityItems, etBtmAdavancePrice;
    private TextView tvSaveItem, tvTitleAddItems,tv_edtTaxAmt,tv_edttaxFinalAmt;
    private Spinner etTaxPercentage;
    private String mFinalTaxAmt,mTAX_FIANL_DISCOUNT, mFINAL_TAX_AMOUNT, mItemName, mItemHSNCode, mRatePerUnit, mDiscountPercentage, mTaxPercentage, mItemRemarks, mUserID, mProductID, mQuantity, mAdvanceAmt;

    private ProgressBar pbAddItems;
    private SharedPreferences sharedPreferences;

    private Map<String, String> getText = new HashMap<>();
    List<String> categories = new ArrayList<String>();
    double Discount_after;
    private BottomSheetBehavior mBehavior;
    private boolean shouldEdit = false;
    String value;
    public BottomSheetAddItems() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.DialogStyle);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        BottomSheetDialog dialog = (BottomSheetDialog) super.onCreateDialog(savedInstanceState);
        View view = View.inflate(getContext(), R.layout.bottom_sheet_add_items, null);
        etItemName = view.findViewById(R.id.etItemName);
        etHSNCode = view.findViewById(R.id.etHSNCode);
        tv_edtTaxAmt = view.findViewById(R.id.tv_edtTaxAmt);
        tv_edttaxFinalAmt = view.findViewById(R.id.tv_edttaxFinalAmt);
        // etBtmAdavancePrice = view.findViewById(R.id.etBtmAdavancePrice);
        etRatePerUnit = view.findViewById(R.id.etRatePerUnit);
        etBtmQuantityItems = view.findViewById(R.id.etBtmQuantityItems);
        etDiscountPercentage = view.findViewById(R.id.etDiscountPercentage);
        etTaxPercentage = view.findViewById(R.id.etTaxPercentage);
        etRemarksProduct = view.findViewById(R.id.etRemarksProduct);
        pbAddItems = view.findViewById(R.id.pbAddItems);
        tvSaveItem = view.findViewById(R.id.tvSaveItem);
        tvTitleAddItems = view.findViewById(R.id.tvTitleAddItems);

        dialog.setContentView(view);
        mBehavior = BottomSheetBehavior.from((View) view.getParent());

        Bundle getEditData = getArguments();

        Log.e("editeitemsss", getEditData + "");

        if (getEditData != null) {
            if (getEditData.containsKey("KEY_EDIT_ADDED_PRODUCTS")) {
                String type = getEditData.getString("KEY_EDIT_ADDED_PRODUCTS");

                if (type != null && type.equalsIgnoreCase("EDIT_PRODUCT")) {

                    tvTitleAddItems.setText("Add Or Edit Items");

                    shouldEdit = true;

                    if (getEditData.containsKey("KEY_PRODUCT_NAME")) {
                        mItemName = getEditData.getString("KEY_PRODUCT_NAME");
                        etItemName.setText(mItemName);

                    }

                    if (getEditData.containsKey("KEY_HSN_CODE")) {
                        mItemHSNCode = getEditData.getString("KEY_HSN_CODE");
                        etHSNCode.setText(mItemHSNCode);
                    }

                    if (getEditData.containsKey("KEY_RATE_PER_UNIT")) {
                        mRatePerUnit = getEditData.getString("KEY_RATE_PER_UNIT");
                        etRatePerUnit.setText(mRatePerUnit);
                    }

                    if (getEditData.containsKey("KEY_DISCOUNT_PERCENTAGE")) {
                        mDiscountPercentage = getEditData.getString("KEY_DISCOUNT_PERCENTAGE");
                        etDiscountPercentage.setText(mDiscountPercentage);
                    }

                    if (getEditData.containsKey("KEY_QUANTITY_PER_UNIT")) {
                        mQuantity = getEditData.getString("KEY_QUANTITY_PER_UNIT");
                        etBtmQuantityItems.setText(mQuantity);
                    }

                    if (getEditData.containsKey("KEY_ADVANCE_PER_UNIT")) {
                        //mAdvanceAmt = getEditData.getString("KEY_ADVANCE_PER_UNIT");
                        //etBtmAdavancePrice.setText(mAdvanceAmt);
                    }

                    if (getEditData.containsKey("KEY_TAX_PERCENTAGE")) {
                        mTaxPercentage = getEditData.getString("KEY_TAX_PERCENTAGE");
                        checkPosition(mTaxPercentage);
                    }

                    if (getEditData.containsKey("KEY_PRODUCT_ID")) {
                        //mProductID = getEditData.getString("KEY_PRODUCT_ID");
                    }

                    if (getEditData.containsKey("KEY_TAX_FIANL_DISCOUNT")) {
                        mTAX_FIANL_DISCOUNT = getEditData.getString("KEY_TAX_FIANL_DISCOUNT");
                        double discountAmt=Double.valueOf(mRatePerUnit)-Double.valueOf(mTAX_FIANL_DISCOUNT);
                        Discount_after=discountAmt;
                        Log.d("discountAmt",""+discountAmt);
                        tv_edtTaxAmt.setText(mTAX_FIANL_DISCOUNT);
                    }

                    if (getEditData.containsKey("KEY_TAX_FINAL_TAX_AMOUNT")) {
                        mFINAL_TAX_AMOUNT = getEditData.getString("KEY_TAX_FINAL_TAX_AMOUNT");
                        tv_edttaxFinalAmt.setText(mFINAL_TAX_AMOUNT);
                    }
                }
            }
            setSpinnerTexData();
            startCalculationEdit();
        }

        if (getActivity() != null) {
            sharedPreferences = getActivity().getSharedPreferences("USER_DATA", Context.MODE_PRIVATE);
        }

        if (sharedPreferences.contains("KEY_USER_ID")) {
            mUserID = sharedPreferences.getString("KEY_USER_ID", "");
        }

        mTaxPercentage = "0";
        mDiscountPercentage = "0";

        tvSaveItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fieldValidation();
                // Toast.makeText(getActivity(), "Coming soon", Toast.LENGTH_SHORT).show();
            }
        });


        return dialog;

    }

    private void startCalculationEdit() {
        etDiscountPercentage.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (etRatePerUnit.getText().length() == 0 && TextUtils.isEmpty(etRatePerUnit.getText().toString())) {
                    Toast.makeText(getActivity(), "Please Enter Rate", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                Log.d("DiscountPercentage", "" + charSequence);
                if (charSequence.toString().length()> 0) {
                    startCalculation(charSequence.toString());
                } else {
                    tv_edtTaxAmt.setText("00.00");
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    private void startCalculation(String value) {
        Log.d("discount","discount_value:"+value);
        if (etRatePerUnit.getText().toString().trim().length()>0) {
            double Discount = Integer.parseInt(value);
            double Amt = Double.parseDouble(etRatePerUnit.getText().toString().trim());
            double TexAmt = (Amt * Discount) / 100;
            Discount_after = Float.valueOf(String.valueOf(Amt))-Float.valueOf(String.valueOf(TexAmt));
            Log.d("discount","After Discount: "+Discount_after+"  final_value:"+TexAmt);
            tv_edtTaxAmt.setText(""+String.valueOf(TexAmt));
        } else {
            Toast.makeText(getActivity(), "Enter Rate First", Toast.LENGTH_SHORT).show();
        }
    }

    private void checkPosition(String mTaxPercentage) {
        int position = getIndex(etTaxPercentage,mTaxPercentage);
        Log.d("position",position+"  |  "+mTaxPercentage +""+getText.containsKey(mTaxPercentage));
        etTaxPercentage.setPrompt(mTaxPercentage);
    }

    private int getIndex(Spinner spinner, String myString){
        int index = 0;
        for (int i=0;i<spinner.getCount();i++){
            if (spinner.getItemAtPosition(i).equals(myString)){
                index = i;
            }
        }
        return index;
    }

    private void fieldValidation() {

        mItemName = etItemName.getText().toString().trim();
        mQuantity = etBtmQuantityItems.getText().toString().trim();
        mItemHSNCode = etHSNCode.getText().toString().trim();
        mRatePerUnit = etRatePerUnit.getText().toString().trim();
        mAdvanceAmt = etBtmAdavancePrice.getText().toString().trim();
        mDiscountPercentage = etDiscountPercentage.getText().toString().trim();
        // mTaxPercentage = etTaxPercentage.getText().toString().trim();
        mItemRemarks = etRemarksProduct.getText().toString().trim();

        if (TextUtils.isEmpty(mItemName)) {
            Toast.makeText(getActivity(), "Enter item name", Toast.LENGTH_SHORT).show();
            return;
        }

        if (mItemName.length() < 5) {
            Toast.makeText(getActivity(), "Item name must be at least 5 words", Toast.LENGTH_SHORT).show();
            return;
        }

        if (mQuantity.equalsIgnoreCase("") || mQuantity.equalsIgnoreCase("0")) {
            Toast.makeText(getActivity(), "Item name must be at least 1", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(mAdvanceAmt)) {
            Toast.makeText(getActivity(), "Enter Advance Amount", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(mItemHSNCode)) {
            Toast.makeText(getActivity(), "Enter HSN code", Toast.LENGTH_SHORT).show();
            return;
        }

        if (mItemHSNCode.length() < 4) {
            Toast.makeText(getActivity(), "HSN code must be at least 4 words", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(mRatePerUnit)) {
            Toast.makeText(getActivity(), "Enter per unit price", Toast.LENGTH_SHORT).show();
            return;
        }

        if (shouldEdit) {
            callEditItemsAPI();
        } else {
            callAPIAddItems();
        }
    }

    private void callEditItemsAPI() {

        pbAddItems.setVisibility(View.VISIBLE);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, PayKreditAPI.INVOICE_UPDATE_PRODUCT, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.e("editrespooososod", response + "");

                pbAddItems.setVisibility(View.GONE);

                try {
                    JSONObject jsonObject = new JSONObject(response);

                    String message = jsonObject.getString("message");
                    String status = jsonObject.getString("status");
                    if (status.equalsIgnoreCase("1")) {
                        JSONObject Data = jsonObject.getJSONObject("data");
                        String tbl_invoice_products_id= Data.getString("");
                        String products_name= Data.getString("");
                        String price= Data.getString("");
                        String quantity= Data.getString("");
                        String discount= Data.getString("");
                        String tax= Data.getString("");
                        String final_tax_amount= Data.getString("");
                        String final_discount= Data.getString("");

                        AddNewProduct.PID = tbl_invoice_products_id;
                        AddNewProduct.PNAME = products_name;
                        AddNewProduct.PRATE = price;
                        AddNewProduct.PQUANTITY = quantity;
                        AddNewProduct.PDISCOUNT = discount;
                        AddNewProduct.TOTALAMOUNT = String.valueOf(Float.valueOf(price)*Integer.parseInt(quantity));
                        AddNewProduct.TOTALDISCOUNTAMOUNT = String.valueOf(Float.valueOf(final_discount)*Integer.parseInt(quantity));
                        AddNewProduct.PTAX = tax;
                        AddNewProduct.FINALTAXAFTERDISCOUNT =final_tax_amount;
                        AddNewProduct.FINALPRICE =final_discount;
                        float finalPayToMe = Float.parseFloat(final_tax_amount)*Integer.parseInt(quantity);
                        float ToMe = Float.parseFloat(AddNewProduct.TOTALDISCOUNTAMOUNT) + finalPayToMe;
                        AddNewProduct.FINALPAYTOME = String.valueOf(ToMe);
                        Toast.makeText(getActivity(), "" + message, Toast.LENGTH_SHORT).show();
                        /*ActivityAddedProducts activityAddedProducts = ActivityAddedProducts.getInstance();
                        activityAddedProducts.loadAddedProducts();*/

                        hideBottomSheet();
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

                params.put("products_name", mItemName);
                params.put("HSN_code", mItemHSNCode);
                params.put("quantity", mQuantity);
                params.put("tbl_invoice_products_id", mProductID);
                params.put("price", mRatePerUnit);
                params.put("advance", mAdvanceAmt);
                params.put("discount", mDiscountPercentage);
                params.put("tax", mTaxPercentage);
                params.put("email_id", mItemRemarks);
                Log.e("paprpaprparp", params + "");
                return params;
            }
        };

        RequestQueue requestQueue = SingletonRequestQueue.getInstance(getActivity()).getRequestQueue();
        requestQueue.add(stringRequest);


    }

    private void callAPIAddItems() {

        pbAddItems.setVisibility(View.VISIBLE);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, PayKreditAPI.ADD_INVOICE_PRODUCTS, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("responseadditems", response + "");
                pbAddItems.setVisibility(View.GONE);

                try {
                    JSONObject jsonObject = new JSONObject(response);

                    String message = jsonObject.getString("message");
                    String status = jsonObject.getString("status");

                    if (status.equalsIgnoreCase("1")) {
                        Toast.makeText(getActivity(), "" + message, Toast.LENGTH_SHORT).show();

                        /*ActivityAddedProducts activityAddedProducts = ActivityAddedProducts.getInstance();
                        activityAddedProducts.loadAddedProducts();*/

                        hideBottomSheet();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity(), "Network or server error", Toast.LENGTH_SHORT).show();

                hideBottomSheet();
                pbAddItems.setVisibility(View.GONE);
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("products_name", mItemName);
                params.put("HSN_code", mItemHSNCode);
                params.put("quantity", mQuantity);
                params.put("user_id", mUserID);
                params.put("price", mRatePerUnit);
                params.put("advance", mAdvanceAmt);
                params.put("discount", mDiscountPercentage);
                params.put("tax", mTaxPercentage);
                params.put("email_id", mItemRemarks);

                Log.e("remsmdadmasd", params + "");

                return params;
            }
        };

        RequestQueue requestQueue = SingletonRequestQueue.getInstance(getActivity()).getRequestQueue();
        requestQueue.add(stringRequest);

    }

    private void setSpinnerTexData() {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, TAX_SLAB_API, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                // Log.d("TaxData", "" + response);
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
        if (Discount_after != 0) {
            float Tax = Float.parseFloat(value);
            double FinalTaxAmt = ((Discount_after * Tax) / 100);
            mFinalTaxAmt = String.valueOf(FinalTaxAmt);
            tv_edttaxFinalAmt.setText("" + FinalTaxAmt);
            Log.d("DiscountPercentage", "" + FinalTaxAmt);
        } else {
            Toast.makeText(getActivity(), "Please Enter Amt", Toast.LENGTH_SHORT).show();
        }
    }

    private void hideBottomSheet() {

        if (getDialog() != null) {
            if (getDialog().isShowing()) {
                getDialog().dismiss();
            }
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        mBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
    }
}
