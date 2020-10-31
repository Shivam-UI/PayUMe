package com.lgt.paykredit.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
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
import com.lgt.paykredit.Fragments.FragmentCreditBook;
import com.lgt.paykredit.R;
import com.lgt.paykredit.extras.Common;
import com.lgt.paykredit.extras.Language;
import com.lgt.paykredit.extras.PayKreditAPI;
import com.lgt.paykredit.extras.SingletonRequestQueue;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class ActivityPayment extends AppCompatActivity {

    private ImageView sendInvoiceSingleUser, ivBackSingleUserTransaction, ivDone;
    private LinearLayout llDatePayment;
    private TextView tvUserNameSingleTransaction, tvDatePayment, tvPaymentReceiveOrGive;
    private EditText etEnterAmount, etMessagePayment;
    private ProgressBar pbPayment;

    private boolean isValid = false;

    private float mEnteredAmount = 0;


    private SharedPreferences sharedPreferences;
    private Calendar myCalendar;
    private String mName, mPaymentType, mMessage, mDateOfTransaction, mCustomerID, mUserID, mPaymentTime;

    private Context context;
    private ActivityPayment activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        context = activity = this;
        tvDatePayment = findViewById(R.id.tvDatePayment);
        sendInvoiceSingleUser = findViewById(R.id.sendInvoiceSingleUser);
        llDatePayment = findViewById(R.id.llDatePayment);
        etEnterAmount = findViewById(R.id.etEnterAmount);
        etEnterAmount.requestFocus();
        etMessagePayment = findViewById(R.id.etMessagePayment);
        tvUserNameSingleTransaction = findViewById(R.id.tvUserNameSingleTransaction);
        ivBackSingleUserTransaction = findViewById(R.id.ivBackSingleUserTransaction);
        ivDone = findViewById(R.id.ivDone);
        tvPaymentReceiveOrGive = findViewById(R.id.tvPaymentReceiveOrGive);

        pbPayment = findViewById(R.id.pbPayment);

        sharedPreferences = ActivityPayment.this.getSharedPreferences("USER_DATA", MODE_PRIVATE);
        if (sharedPreferences.contains("KEY_USER_ID")) {
            mUserID = sharedPreferences.getString("KEY_USER_ID", "");
        }

        myCalendar = Calendar.getInstance();

        Intent getName = getIntent();
        if (getName != null) {
            if (getName.hasExtra("KEY_NAME_SINGLE")) {
                mName = getName.getStringExtra("KEY_NAME_SINGLE");
                tvUserNameSingleTransaction.setText(mName);
                Log.e("sdadasdasas", getName.toString() + "" + mName + "");
            }

            if (getName.hasExtra("KEY_PAYMENT_TYPE")) {
                mPaymentType = getName.getStringExtra("KEY_PAYMENT_TYPE");

                if (mPaymentType != null && mPaymentType.equalsIgnoreCase("recive")) {
                    tvPaymentReceiveOrGive.setTextColor(getResources().getColor(R.color.green));
                    if (Common.getLanguage(context).equalsIgnoreCase(Common.HINDI)) {
                        tvPaymentReceiveOrGive.setText("रिसीव पेमेंट");
                        etEnterAmount.setHint("ऐड पेमेंट");
                    } else {
                        tvPaymentReceiveOrGive.setText("Receiving Payment");
                    }
                } else {
                    tvPaymentReceiveOrGive.setTextColor(getResources().getColor(R.color.red));
                    if (Common.getLanguage(context).equalsIgnoreCase(Common.HINDI)) {
                        tvPaymentReceiveOrGive.setText("उधार दें");
                        etEnterAmount.setHint("ऐड उधार");
                    } else {
                        tvPaymentReceiveOrGive.setText("Giving Credit");
                    }
                }
                Log.e("paymenmmnn", mPaymentType + "");


            }

            if (getName.hasExtra("KEY_CUSTOMER_ID")) {
                mCustomerID = getName.getStringExtra("KEY_CUSTOMER_ID");
                Log.e("keycusto,merid", mCustomerID + "");
            }
        }

        ivDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isValid) {
                    mMessage = etMessagePayment.getText().toString().trim();

                    getTime();

                    callRecordPayment();
                    //  Toast.makeText(ActivityPayment.this, "You are good to go" + mEnteredAmount + "--type" + mPaymentType+"dattte>"+mDateOfTransaction, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(ActivityPayment.this, "Minimum amount is 1Rs.", Toast.LENGTH_LONG).show();

                }
            }
        });

        etEnterAmount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Log.e("enteramountttt", s + "");
                if (s.length() > 0) {
                    mEnteredAmount = Float.parseFloat(etEnterAmount.getText().toString().trim());
                    if (mEnteredAmount > 0) {
                        isValid = true;
                        ivDone.setColorFilter(ContextCompat.getColor(ActivityPayment.this, R.color.green), android.graphics.PorterDuff.Mode.MULTIPLY);

                    } else {
                        isValid = false;
                        ivDone.setColorFilter(ContextCompat.getColor(ActivityPayment.this, R.color.light_grey), android.graphics.PorterDuff.Mode.MULTIPLY);

                    }
                } else {
                    isValid = false;
                    ivDone.setColorFilter(ContextCompat.getColor(ActivityPayment.this, R.color.light_grey), android.graphics.PorterDuff.Mode.MULTIPLY);

                }


            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        llDatePayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openCalendar();

            }
        });


        getCurrentDate();

        ivBackSingleUserTransaction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

/*
        etEnterAmount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });*/

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                convertLang();
            }
        }, 250);
    }

    private void getTime() {

        mPaymentTime = new SimpleDateFormat("h:mm aaa").format(Calendar.getInstance().getTime());
    }

    private void callRecordPayment() {

        pbPayment.setVisibility(View.VISIBLE);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, PayKreditAPI.ADD_RECORD, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.e("ghjjhgjhgj", response + "");
                pbPayment.setVisibility(View.GONE);

                try {
                    JSONObject jsonObject = new JSONObject(response);

                    String message = jsonObject.getString("message");
                    String status = jsonObject.getString("status");

                    if (status.equalsIgnoreCase("1")) {
                        Toast.makeText(ActivityPayment.this, "" + message, Toast.LENGTH_SHORT).show();
                        SingleUserTransaction singleUserTransaction = SingleUserTransaction.getInstance();
                        singleUserTransaction.loadRecyclerView();

                        FragmentCreditBook fragmentCreditBook = FragmentCreditBook.getInstance();
                        fragmentCreditBook.loadCustomers();

                        onBackPressed();
                    } else {
                        Toast.makeText(ActivityPayment.this, "" + message, Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                pbPayment.setVisibility(View.GONE);
                Toast.makeText(ActivityPayment.this, "" + error.getMessage() + "", Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("tbl_add_customer_id", mCustomerID);
                params.put("user_id", mUserID);
                params.put("payment", String.valueOf(mEnteredAmount));
                params.put("date", mDateOfTransaction);
                params.put("image", "");
                params.put("time", mPaymentTime);
                params.put("msg", mMessage);
                params.put("payment_type", mPaymentType);
                Log.e("paramstblcustomeid", params + "");

                return params;
            }
        };

        RequestQueue requestQueue = SingletonRequestQueue.getInstance(ActivityPayment.this).getRequestQueue();
        requestQueue.add(stringRequest);


    }

    private void getCurrentDate() {
        String date = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
        tvDatePayment.setText(date);
        mDateOfTransaction = date;

    }

    private void openCalendar() {

        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }

        };

        DatePickerDialog datePickerDialog = new DatePickerDialog(ActivityPayment.this, date, myCalendar
                .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH));

        //following line to restrict future date selection
        datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
        datePickerDialog.show();

    }

    private void updateLabel() {

        String myFormat = "dd/MM/yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        tvDatePayment.setText(sdf.format(myCalendar.getTime()));
        mDateOfTransaction = sdf.format(myCalendar.getTime());
    }

    private void convertLang() {

        if (Common.getLanguage(getApplicationContext()).equalsIgnoreCase(Common.HINDI)) {

            tvDatePayment.setText("लेन-देन की तिथि");
            etMessagePayment.setHint("उधार का विवरण लिखें");

        } else {

            //If not, display "no connection" warning:
            tvPaymentReceiveOrGive.setText(tvPaymentReceiveOrGive.getText().toString());
            etEnterAmount.setHint(etEnterAmount.getHint().toString());
            tvDatePayment.setText(tvDatePayment.getText().toString());
            etMessagePayment.setHint(etMessagePayment.getHint().toString());

        }
    }

}
