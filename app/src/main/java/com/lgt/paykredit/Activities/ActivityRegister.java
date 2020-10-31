package com.lgt.paykredit.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.lgt.paykredit.Adapter.AdapterBusiness;
import com.lgt.paykredit.Models.ModelBusiness;
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

public class ActivityRegister extends AppCompatActivity {

    private TextView tvRegister ;

    private EditText etBusinessName,etBusinessUserName,etBusinessAddress,etMobileNumberBusiness,etEmailBusiness,etGSTNumber;
    private Spinner spinnerBusiness;

    private ProgressBar pbRegister;
    private String mBusinessID,mOperatorName,mBusinessName,mBusinessUserName,mBusinessAddress,mMobileNumber,mEmailID,mGSTNumber,mUserID;
    private List<ModelBusiness> listBusiness;

    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        tvRegister = findViewById(R.id.tvRegister);
        
        etBusinessName = findViewById(R.id.etBusinessName);
        etBusinessUserName = findViewById(R.id.etBusinessUserName);
        etBusinessAddress = findViewById(R.id.etBusinessAddress);
        etMobileNumberBusiness = findViewById(R.id.etMobileNumberBusiness);
        etEmailBusiness = findViewById(R.id.etEmailBusiness);
        etGSTNumber = findViewById(R.id.etGSTNumber);
        spinnerBusiness = findViewById(R.id.spinnerBusiness);
        pbRegister = findViewById(R.id.pbRegister);

        sharedPreferences = ActivityRegister.this.getSharedPreferences("USER_DATA",MODE_PRIVATE);

        if(sharedPreferences.contains("KEY_USER_ID")){
            mUserID = sharedPreferences.getString("KEY_USER_ID","");
        }


        tvRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               // startActivity(new Intent(ActivityRegister.this,MainActivity.class));
                
                fieldValidation();
                
            }
        });

        loadBusinessData();
    }

    private void fieldValidation() {

        mBusinessName = etBusinessName.getText().toString().trim();
        mBusinessUserName = etBusinessUserName.getText().toString().trim();
        mBusinessAddress = etBusinessAddress.getText().toString().trim();
        mMobileNumber = etMobileNumberBusiness.getText().toString().trim();
        mEmailID = etEmailBusiness.getText().toString().trim();
        mGSTNumber = etGSTNumber.getText().toString().trim();


        if(TextUtils.isEmpty(mBusinessName)){
            etBusinessName.setError("Please enter business name");
            etBusinessName.requestFocus();
            return;
        }

        if(mBusinessName.length()<5){
            etBusinessName.setError("At least 5 words are required");
            etBusinessName.requestFocus();
            return;
        }

        if(TextUtils.isEmpty(mBusinessUserName)){
            etBusinessUserName.setError("Enter your name");
            etBusinessUserName.requestFocus();
            return;
        }

        if(mBusinessUserName.length()<5){
            etBusinessUserName.setError("At least 5 words are required");
            etBusinessUserName.requestFocus();
            return;
        }

        if(TextUtils.isEmpty(mBusinessAddress)){
            etBusinessAddress.setError("Enter your address");
            etBusinessAddress.requestFocus();
            return;
        }

        if(mBusinessAddress.length()<10){
            etBusinessAddress.setError("Enter your complete address");
            etBusinessAddress.requestFocus();
            return;
        }

        if(TextUtils.isEmpty(mMobileNumber)){
            etMobileNumberBusiness.setError("Enter mobile number");
            etMobileNumberBusiness.requestFocus();
            return;
        }

        if(mMobileNumber.length()<10){
            etMobileNumberBusiness.setError("Mobile number must be 10 digits");
            etMobileNumberBusiness.requestFocus();
            return;
        }

        if(TextUtils.isEmpty(mEmailID)){
            etEmailBusiness.setError("Enter email address");
            etEmailBusiness.requestFocus();
            return;
        }

        if(!Patterns.EMAIL_ADDRESS.matcher(mEmailID).matches()){
            etEmailBusiness.setError("Enter a valid email");
            etEmailBusiness.requestFocus();
            return;
        }

        callRegisterBusinessAPI();


    }

    private void callRegisterBusinessAPI() {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, PayKreditAPI.REGISTER_BUSINESS, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.e("responseregister",response+"");

                try {
                    JSONObject jsonObject = new JSONObject(response);

                    String message = jsonObject.getString("message");
                    String status = jsonObject.getString("status");

                    if(status.equalsIgnoreCase("1")){
                       //data saved successfully
                        //send user to dashboard
                        startActivity(new Intent(ActivityRegister.this,MainActivity.class));
                        finishAffinity();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();
                params.put("mobile",mMobileNumber);
                params.put("business_name",mBusinessName);
                params.put("business_cat_id",mBusinessID);
                params.put("address",mBusinessAddress);
                params.put("about_us",mGSTNumber);
                params.put("user_id",mUserID);
                params.put("email",mEmailID);
                Log.e("paramsregister",params+"");
                return params;
            }
        };
        RequestQueue requestQueue = SingletonRequestQueue.getInstance(ActivityRegister.this).getRequestQueue();
        requestQueue.add(stringRequest);
    }

    private void loadBusinessData(){


        pbRegister.setVisibility(View.VISIBLE);

        listBusiness =new ArrayList<>();
        listBusiness.clear();

        StringRequest stringRequest = new StringRequest(Request.Method.GET, PayKreditAPI.GET_BUSINESS_CATEGORY, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.e("responseoperaty",response+"");
                pbRegister.setVisibility(View.GONE);

                try {
                    JSONObject jsonObject = new JSONObject(response);

                    String message = jsonObject.getString("message");
                    String status = jsonObject.getString("status");

                    if(status.equalsIgnoreCase("1")){

                        JSONArray jsonArray = jsonObject.getJSONArray("data");

                        for(int i=0;i<jsonArray.length();i++){

                            JSONObject object =jsonArray.getJSONObject(i);

                            String business_category_id = object.getString("business_category_id");
                            String name = object.getString("name");
                            String image = object.getString("image");

                            listBusiness.add(new ModelBusiness(business_category_id,name,image));
                        }

                        AdapterBusiness adapterOperators = new AdapterBusiness(ActivityRegister.this, listBusiness);
                        spinnerBusiness.setAdapter(adapterOperators);

                        spinnerBusiness.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                mBusinessID = listBusiness.get(i).getBusiness_category_id();
                                mOperatorName = listBusiness.get(i).getName();
                                Log.e("selectedspinner", mBusinessID +"----"+mOperatorName);
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> adapterView) {
                                

                            }
                        });




                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        RequestQueue requestQueue = SingletonRequestQueue.getInstance(ActivityRegister.this).getRequestQueue();
        requestQueue.add(stringRequest);

    }
}
