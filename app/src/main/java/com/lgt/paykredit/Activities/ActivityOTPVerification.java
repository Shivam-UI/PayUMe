package com.lgt.paykredit.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.phone.SmsRetriever;
import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.lgt.paykredit.R;
import com.lgt.paykredit.extras.Common;
import com.lgt.paykredit.extras.Language;

public class ActivityOTPVerification extends AppCompatActivity {

    private TextView tvVerifyOTP, tvOtpSentTo, tv_otp_desc;

    String first, second, third, fourth, enteredOTP = "";

    private String mMobile, mUserStatus, mRealOTP, mUserID, mEmail,mAcName,mAcNumber,mAcCode;

    private ProgressBar pb_otpVerification;

    private EditText etLogin1, etLogin2, etLogin3, etLogin4;
    private static final int SMS_CONSENT_REQUEST = 2;

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private TextView tv_otp_heading;
    private Context context;
    private ActivityOTPVerification activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otpverification);

        context = activity = this;
        tvVerifyOTP = findViewById(R.id.tvVerifyOTP);
        tvOtpSentTo = findViewById(R.id.tvOtpSentTo);

        etLogin1 = findViewById(R.id.etLogin1);
        etLogin2 = findViewById(R.id.etLogin2);
        etLogin3 = findViewById(R.id.etLogin3);
        etLogin4 = findViewById(R.id.etLogin4);
        tv_otp_heading = findViewById(R.id.tv_otp_heading);
        tv_otp_desc = findViewById(R.id.tv_otp_desc);

        pb_otpVerification = findViewById(R.id.pb_otpVerification);

        sharedPreferences = ActivityOTPVerification.this.getSharedPreferences("USER_DATA", MODE_PRIVATE);
        editor = sharedPreferences.edit();

        Intent getData = getIntent();
        if (getData != null) {

            Log.e("jlkjkljkl", getData.toString());

            if (getData.hasExtra("KEY_MOBILE")) {
                mMobile = getData.getStringExtra("KEY_MOBILE");
                tvOtpSentTo.setText(mMobile);
            }

            if (getData.hasExtra("KEY_EMAIL")) {
                mEmail = getData.getStringExtra("KEY_EMAIL");
            }

            if (getData.hasExtra("KEY_OTP")) {
                mRealOTP = getData.getStringExtra("KEY_OTP");
            }

            if (getData.hasExtra("KEY_USER_STATUS")) {
                mUserStatus = getData.getStringExtra("KEY_USER_STATUS");
            }

            if (getData.hasExtra("KEY_USER_ID")) {
                mUserID = getData.getStringExtra("KEY_USER_ID");
            }

            if (getData.hasExtra("KEY_AC_NAME")) {
                mAcName = getData.getStringExtra("KEY_AC_NAME");
            }

            if (getData.hasExtra("KEY_AC_NUMBER")) {
                mAcNumber = getData.getStringExtra("KEY_AC_NUMBER");
            }

            if (getData.hasExtra("KEY_AC_CODE")) {
                mAcCode = getData.getStringExtra("KEY_AC_CODE");
            }

        }

        textWatcher();


        tvVerifyOTP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                validation();

                //startActivity(new Intent(ActivityOTPVerification.this,MainActivity.class));
                // startActivity(new Intent(ActivityOTPVerification.this, ActivityRegister.class));
            }
        });

        if (Common.getLanguage(activity) == "HI") {
            // check code for language
            tv_otp_heading.setText("OTP सत्यापन");
            tv_otp_desc.setText("कृपया भेजे गए OTP को दर्ज करें");
            tvVerifyOTP.setText("वेरीफाई करें");
        } else {

            //If not, display "no connection" warning:
            tv_otp_heading.setText(tv_otp_heading.getText().toString());
            tv_otp_desc.setText(tv_otp_desc.getText().toString());
            tvVerifyOTP.setText(tvVerifyOTP.getText().toString());
        }
    }

    private void validation() {

        first = etLogin1.getText().toString().trim();
        second = etLogin2.getText().toString().trim();
        third = etLogin3.getText().toString().trim();
        fourth = etLogin4.getText().toString().trim();

        if (TextUtils.isEmpty(first)) {
            etLogin1.setError("OTP is required");
            etLogin1.requestFocus();
            return;

        }

        if (TextUtils.isEmpty(second)) {
            etLogin2.setError("OTP is required");
            etLogin2.requestFocus();
            return;

        }

        if (TextUtils.isEmpty(third)) {
            etLogin3.setError("OTP is required");
            etLogin3.requestFocus();
            return;

        }

        if (TextUtils.isEmpty(fourth)) {
            etLogin4.setError("OTP is required");
            etLogin4.requestFocus();
            return;

        }


        enteredOTP = first + second + third + fourth;
        Log.e("entertdted", enteredOTP + "");

        if (!enteredOTP.equals(mRealOTP)) {
            Toast.makeText(ActivityOTPVerification.this, "Entered OTP is wrong", Toast.LENGTH_SHORT).show();
        } else {

            editor.putString("KEY_MOBILE", mMobile);
            editor.putString("KEY_EMAIL", mEmail);
            editor.putString("KEY_USER_ID", mUserID);
            editor.putString("KEY_AC_NAME", mAcName);
            editor.putString("KEY_AC_NUMBER", mAcNumber);
            editor.putString("KEY_AC_CODE", mAcCode);
            editor.commit();
            editor.apply();
            Log.e("verificationdonwe", "  dgsahdasjdgj  "+mAcName+mAcNumber+mAcCode);

            if (mUserStatus.equalsIgnoreCase("new_user")) {
                startActivity(new Intent(ActivityOTPVerification.this, ActivityRegister.class));
                finishAffinity();
            } else {
                startActivity(new Intent(ActivityOTPVerification.this, MainActivity.class));
                finishAffinity();               // startActivity(new Intent(ActivityOTPVerification.this,MainActivity.class));
            }

            //OTP Verification (Done)
            //Call login api, and send to Homescreen


        }
    }

    private void textWatcher() {
        etLogin1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (etLogin1.getText().toString().length() == 1)     //size as per your requirement
                {
                    etLogin2.requestFocus();
                    pb_otpVerification.setVisibility(View.GONE);
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        etLogin2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (etLogin2.getText().toString().length() == 1)     //size as per your requirement
                {
                    etLogin3.requestFocus();
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        etLogin3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (etLogin3.getText().toString().length() == 1)     //size as per your requirement
                {
                    etLogin4.requestFocus();
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        etLogin4.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (etLogin4.getText().toString().length() == 1)     //size as per your requirement
                {
                    etLogin4.requestFocus();
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private final BroadcastReceiver smsVerificationReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (SmsRetriever.SMS_RETRIEVED_ACTION.equals(intent.getAction())) {
                Bundle extras = intent.getExtras();
                Status smsRetrieverStatus = (Status) extras.get(SmsRetriever.EXTRA_STATUS);

                switch (smsRetrieverStatus.getStatusCode()) {
                    case CommonStatusCodes.SUCCESS:
                        // Get consent intent
                        Intent consentIntent = extras.getParcelable(SmsRetriever.EXTRA_CONSENT_INTENT);
                        try {
                            // Start activity to show consent dialog to user, activity must be started in
                            // 5 minutes, otherwise you'll receive another TIMEOUT intent

                            startActivityForResult(consentIntent, SMS_CONSENT_REQUEST);

                        } catch (ActivityNotFoundException e) {
                            // Handle the exception ...
                        }
                        break;
                    case CommonStatusCodes.TIMEOUT:
                        // Time out occurred, handle the error.
                        break;
                }
            }
        }
    };


    @Override
    public void onResume() {
        super.onResume();


        Task<Void> task = SmsRetriever.getClient(ActivityOTPVerification.this).startSmsUserConsent(null /* or null */);
        IntentFilter intentFilter = new IntentFilter(SmsRetriever.SMS_RETRIEVED_ACTION);
        ActivityOTPVerification.this.registerReceiver(smsVerificationReceiver, intentFilter);
        task.addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void status) {
                Log.e("dsadsadstatus", status + "");
            }
        });

        task.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
            }
        });

       /* try {
            LocalBroadcastManager.getInstance(getActivity()).
                    registerReceiver(receiver, new IntentFilter("otp"));
        } catch (Exception e) {
            Log.e("fgdgfdg", e.getMessage());
            e.printStackTrace();
        }*/

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            // ...
            case SMS_CONSENT_REQUEST:
                if (resultCode == RESULT_OK) {
                    // Get SMS message content
                    String message = data.getStringExtra(SmsRetriever.EXTRA_SMS_MESSAGE);
                    String oneTimeCode = parseOneTimeCode(message); // define this function
                    if (!message.isEmpty()) {
                        Log.e("dsadsadsad", message + "");
                        pb_otpVerification.setVisibility(View.GONE);
                    } else {
                        pb_otpVerification.setVisibility(View.GONE);

                    }

                    assert oneTimeCode != null;
                    String fullMsg = oneTimeCode.replaceAll("[^0-9]", "");

                    char o1 = fullMsg.charAt(0);
                    char o2 = fullMsg.charAt(1);
                    char o3 = fullMsg.charAt(2);
                    char o4 = fullMsg.charAt(3);

                    etLogin1.setText(o1 + "");
                    etLogin2.setText(o2 + "");
                    etLogin3.setText(o3 + "");
                    etLogin4.setText(o4 + "");

                    Log.e("sadsadsaddsadas", oneTimeCode + "");

                    // send one time code to the server
                } else {
                    pb_otpVerification.setVisibility(View.GONE);
                    Log.e("dsadasdad", "dsasadsad");
                    // Consent canceled, handle the error ...
                }
                break;
        }
    }


    @Override
    public void onPause() {
        super.onPause();
        try {

            LocalBroadcastManager.getInstance(ActivityOTPVerification.this).unregisterReceiver(smsVerificationReceiver);

/*
            LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(receiver);
*/

        } catch (Exception e) {
            Log.e("fgfggggggfgf", e.getMessage());

            e.printStackTrace();
        }
    }


    private String parseOneTimeCode(String message) {
        return message;
    }
}
