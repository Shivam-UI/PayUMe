package com.lgt.paykredit.bottomsheets;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
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
import com.lgt.paykredit.R;
import com.lgt.paykredit.extras.PayKreditAPI;
import com.lgt.paykredit.extras.SingletonRequestQueue;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

 /**
 * A simple {@link Fragment} subclass.
 */

public class BottomSheetAddItems extends BottomSheetDialogFragment {

    private EditText etItemName, etHSNCode, etRatePerUnit, etDiscountPercentage, etTaxPercentage, etRemarksProduct,etBtmQuantityItems,etBtmAdavancePrice;
    private TextView tvSaveItem,tvTitleAddItems;

    private String mItemName, mItemHSNCode, mRatePerUnit, mDiscountPercentage, mTaxPercentage, mItemRemarks, mUserID, mProductID,mQuantity,mAdvanceAmt;

    private ProgressBar pbAddItems;
    private SharedPreferences sharedPreferences;

    private BottomSheetBehavior mBehavior;
    private boolean shouldEdit = false;

    public BottomSheetAddItems() {
    }


 /*   @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
      //  View view = inflater.inflate(R.layout.bottom_sheet_add_items, container, false);
       // return view;
    }*/

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        BottomSheetDialog dialog = (BottomSheetDialog) super.onCreateDialog(savedInstanceState);
        View view = View.inflate(getContext(), R.layout.bottom_sheet_add_items, null);
        etItemName = view.findViewById(R.id.etItemName);
        etHSNCode = view.findViewById(R.id.etHSNCode);
        etBtmAdavancePrice = view.findViewById(R.id.etBtmAdavancePrice);
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

                    tvTitleAddItems.setText("Edit Item");

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
                        etDiscountPercentage.setText(mRatePerUnit);
                    }

                    if (getEditData.containsKey("KEY_QUANTITY_PER_UNIT")) {
                        mQuantity = getEditData.getString("KEY_QUANTITY_PER_UNIT");
                        etBtmQuantityItems.setText(mQuantity);
                    }

                    if (getEditData.containsKey("KEY_ADVANCE_PER_UNIT")) {
                        mAdvanceAmt = getEditData.getString("KEY_ADVANCE_PER_UNIT");
                        etBtmAdavancePrice.setText(mAdvanceAmt);
                    }

                    if (getEditData.containsKey("KEY_TAX_PERCENTAGE")) {
                        mTaxPercentage = getEditData.getString("KEY_TAX_PERCENTAGE");
                        etTaxPercentage.setText(mTaxPercentage);
                    }

                    if (getEditData.containsKey("KEY_PRODUCT_ID")) {
                        mProductID = getEditData.getString("KEY_PRODUCT_ID");
                    }
                }
            }
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
            }
        });


        return dialog;

    }

    private void fieldValidation() {

        mItemName = etItemName.getText().toString().trim();
        mQuantity = etBtmQuantityItems.getText().toString().trim();
        mItemHSNCode = etHSNCode.getText().toString().trim();
        mRatePerUnit = etRatePerUnit.getText().toString().trim();
        mAdvanceAmt = etBtmAdavancePrice.getText().toString().trim();
        mDiscountPercentage = etDiscountPercentage.getText().toString().trim();
        mTaxPercentage = etTaxPercentage.getText().toString().trim();
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
            //CAll edit api
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

                Log.e("editrespooososod",response+"");

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
                Log.e("paprpaprparp",params+"");
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
