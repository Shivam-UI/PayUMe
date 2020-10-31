package com.lgt.paykredit.Activities;

import android.app.Application;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
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
import com.lgt.paykredit.Adapter.ProductAddAdapter;
import com.lgt.paykredit.Adapter.ProductViewAdapter;
import com.lgt.paykredit.Models.ProductModel;
import com.lgt.paykredit.Models.ProductViewModel;
import com.lgt.paykredit.R;
import com.lgt.paykredit.extras.SingletonRequestQueue;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.lgt.paykredit.extras.PayKreditAPI.PREVIEW_INVOICE_API;

public class ActivityPreviewInvoice extends AppCompatActivity {
    private SharedPreferences sharedPreferences;
    private String mUserID = "", tbl_invoice_id = "";
    ImageView iv_back_press;
    TextView tv_previewInvoiceId, tv_PreviewDueDate, tv_previewInvoiceDate, tv_PreviewDiscountPrice, tv_PreviewSubTotalPrice, tv_PreviewAdvancePrice, tv_PreviewBalanceDue;
    EditText etSelectCustomer, et_PreviewAccountHolderName, et_PreviewAccountHolderNumber, et_PreviewAccountHolderIFCSCode, et_PreviewNote;
    Spinner sp_DueTerm;
    RecyclerView rvPreViewSelectProduct;
    ArrayList<String> mDueTermList = new ArrayList<>();
    ArrayList<ProductViewModel> mList = new ArrayList<>();
    ProductViewAdapter productViewAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.preview_invoice_activity);
        sharedPreferences = ActivityPreviewInvoice.this.getSharedPreferences("USER_DATA", MODE_PRIVATE);
        if (sharedPreferences.contains("KEY_USER_ID")) {
            mUserID = sharedPreferences.getString("KEY_USER_ID", "");
        }
        initView();
        if (mUserID != null) {
            if (!mUserID.equalsIgnoreCase("")) {
                if (!getIntent().getStringExtra("tbl_invoice_id").equalsIgnoreCase("")) {
                    tbl_invoice_id = getIntent().getStringExtra("tbl_invoice_id");
                    previewInvoive(tbl_invoice_id);
                } else {
                    Toast.makeText(this, "something went wrong!", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    private void initView() {
        iv_back_press = findViewById(R.id.iv_back_press);
        tv_previewInvoiceId = findViewById(R.id.tv_previewInvoiceId);
        tv_PreviewDueDate = findViewById(R.id.tv_PreviewDueDate);
        tv_previewInvoiceDate = findViewById(R.id.tv_previewInvoiceDate);
        tv_PreviewDiscountPrice = findViewById(R.id.tv_PreviewDiscountPrice);
        tv_PreviewSubTotalPrice = findViewById(R.id.tv_PreviewSubTotalPrice);
        tv_PreviewAdvancePrice = findViewById(R.id.tv_PreviewAdvancePrice);
        tv_PreviewBalanceDue = findViewById(R.id.tv_PreviewBalanceDue);
        etSelectCustomer = findViewById(R.id.etSelectCustomer);
        et_PreviewAccountHolderName = findViewById(R.id.et_PreviewAccountHolderName);
        et_PreviewAccountHolderNumber = findViewById(R.id.et_PreviewAccountHolderNumber);
        et_PreviewAccountHolderIFCSCode = findViewById(R.id.et_PreviewAccountHolderIFCSCode);
        et_PreviewNote = findViewById(R.id.et_PreviewNote);
        sp_DueTerm = findViewById(R.id.sp_DueTerm);
        rvPreViewSelectProduct = findViewById(R.id.rvPreViewSelectProduct);
        iv_back_press.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    private void previewInvoive(String tblID) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, PREVIEW_INVOICE_API, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("previewInvoice", response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String message = jsonObject.getString("message");
                    String status = jsonObject.getString("status");
                    if (status.equalsIgnoreCase("1")) {
                        setUpAddProductView();
                        JSONArray jsonArray = jsonObject.getJSONArray("data");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject PreviewData = jsonArray.getJSONObject(i);
                            String tbl_invoice_id = PreviewData.getString("tbl_invoice_id");
                            String invoice_no = PreviewData.getString("invoice_no");
                            String invoice_date = PreviewData.getString("invoice_date");
                            String due_date = PreviewData.getString("due_date");
                            String due_term = PreviewData.getString("due_term");
                            String total_discount = PreviewData.getString("total_discount");
                            String sub_total = PreviewData.getString("sub_total");
                            String total_advance = PreviewData.getString("total_advance");
                            String total_balance = PreviewData.getString("total_balance");
                            String paid = PreviewData.getString("paid");
                            String note = PreviewData.getString("note");
                            String customer_name = PreviewData.getString("customer_name");
                            String customer_mobile = PreviewData.getString("customer_mobile");
                            String customer_email = PreviewData.getString("customer_email");
                            String account_number = PreviewData.getString("account_number");
                            String account_holder_name = PreviewData.getString("account_holder_name");
                            String IFSC_code = PreviewData.getString("IFSC_code");
                            JSONArray product = PreviewData.getJSONArray("products");
                            // set data
                            tv_previewInvoiceId.setText(invoice_no);
                            tv_PreviewDueDate.setText(due_date);
                            tv_previewInvoiceDate.setText(invoice_date);
                            tv_PreviewDiscountPrice.setText(total_discount);
                            tv_PreviewSubTotalPrice.setText(sub_total);
                            tv_PreviewAdvancePrice.setText(total_advance);
                            tv_PreviewBalanceDue.setText(total_balance);
                            etSelectCustomer.setText(customer_name);
                            etSelectCustomer.setEnabled(false);
                            et_PreviewAccountHolderName.setText(account_holder_name);
                            et_PreviewAccountHolderName.setEnabled(false);
                            et_PreviewAccountHolderNumber.setText(account_number);
                            et_PreviewAccountHolderNumber.setEnabled(false);
                            et_PreviewAccountHolderIFCSCode.setText(IFSC_code);
                            et_PreviewAccountHolderIFCSCode.setEnabled(false);
                            loadDueTerms(due_term);
                            et_PreviewNote.setText(note);
                            et_PreviewNote.setEnabled(false);
                            for (int j = 0; j < product.length(); j++) {
                                JSONObject productData = product.getJSONObject(j);
                                String tbl_create_invoice_products_id = productData.getString("tbl_create_invoice_products_id");
                                String products_id = productData.getString("products_id");
                                String pro_qnt = productData.getString("pro_qnt");
                                String pro_name = productData.getString("pro_name");
                                String pro_discount = productData.getString("pro_discount");
                                String pro_price = productData.getString("pro_price");
                                mList.add(new ProductViewModel(tbl_create_invoice_products_id,products_id,pro_qnt,pro_name,pro_discount,pro_price));
                            }
                        }
                        productViewAdapter.notifyDataSetChanged();
                    } else {
                        Toast.makeText(ActivityPreviewInvoice.this, message, Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("previewInvoice", error.getMessage());
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> param = new HashMap<>();
                param.put("user_id", mUserID);
                param.put("tbl_invoice_id", tblID);
                Log.d("previewInvoice", param.toString());
                return param;
            }
        };
        RequestQueue requestQueue = SingletonRequestQueue.getInstance(ActivityPreviewInvoice.this).getRequestQueue();
        requestQueue.add(stringRequest);
    }

    private void setUpAddProductView() {
        productViewAdapter = new ProductViewAdapter(mList, getApplicationContext());
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        rvPreViewSelectProduct.setLayoutManager(linearLayoutManager);
        rvPreViewSelectProduct.setHasFixedSize(true);
        rvPreViewSelectProduct.setAdapter(productViewAdapter);
    }

    private void loadDueTerms(String term) {
        mDueTermList.clear();
        mDueTermList.add(term);
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, mDueTermList);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_DueTerm.setAdapter(dataAdapter);
        sp_DueTerm.setEnabled(false);
    }
}
