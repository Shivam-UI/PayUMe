package com.lgt.paykredit.Activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.credentials.Credential;
import com.google.android.gms.auth.api.credentials.CredentialPickerConfig;
import com.google.android.gms.auth.api.credentials.HintRequest;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.material.textfield.TextInputEditText;
import com.lgt.paykredit.R;
import com.lgt.paykredit.extras.Common;
import com.lgt.paykredit.extras.Language;
import com.lgt.paykredit.extras.PayKreditAPI;
import com.lgt.paykredit.extras.SingletonRequestQueue;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class ActivityLogin extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {

    private TextInputEditText etMobileLogin;
    private TextView tv_login_desc;

    private ImageView ivCheckMobileNumber;
    private ProgressBar pbLogin;

    private boolean isValidMoibile = false;
    private String mMobile;

    //Get user number
    GoogleApiClient mCredentialsApiClient;
    int RC_HINT = 1;

    private int PLAY_SERVICES_REQUEST = 007;

    private Context context;
    private ActivityLogin activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        context = activity = this;
        etMobileLogin = findViewById(R.id.etMobileLogin);
        ivCheckMobileNumber = findViewById(R.id.ivCheckMobileNumber);
        tv_login_desc = findViewById(R.id.tv_login_desc);
        pbLogin = findViewById(R.id.pbLogin);

        checkForPlayServices();

        if (!checkForPlayServices()) {
            Toast.makeText(ActivityLogin.this, "No play service found", Toast.LENGTH_LONG).show();
            finish();
        } else {
            getNumber();
        }

        ivCheckMobileNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //send user to OTP verification activity

                mMobile = etMobileLogin.getText().toString().trim();
                if (mMobile.length() == 10) {
                    isValidMoibile = true;
                } else {
                    isValidMoibile = false;
                }

                if (isValidMoibile) {


                    callLoginSignUPAPI();

                } else {
                    Toast.makeText(ActivityLogin.this, "Please enter a valid number", Toast.LENGTH_SHORT).show();
                }

            }
        });

        etMobileLogin.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String mm = etMobileLogin.getText().toString().trim();
                Log.e("lengtyhththht", mm + "" + s.length() + "");

                if (mm.length() == 10) {
                    isValidMoibile = true;
                    ivCheckMobileNumber.setColorFilter(ContextCompat.getColor(ActivityLogin.this, R.color.green), android.graphics.PorterDuff.Mode.MULTIPLY);

                } else {
                    isValidMoibile = false;
                    ivCheckMobileNumber.setColorFilter(ContextCompat.getColor(ActivityLogin.this, R.color.light_grey), android.graphics.PorterDuff.Mode.MULTIPLY);

                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        if (Common.getLanguage(activity) == "HI") {
            // check code for language
            tv_login_desc.setText("अपना मोबाइल नंबर दर्ज़ करे");
            //If there is internet connection, get translate service and start translation:
            //tv_login_desc.setText(Language.convertLanguage(getApplicationContext(),tv_login_desc.getText().toString(),Common.getLanguage(activity)));
        } else {

            //If not, display "no connection" warning:
            tv_login_desc.setText(tv_login_desc.getText().toString());
        }

    }

    private void callLoginSignUPAPI() {

        pbLogin.setVisibility(View.VISIBLE);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, PayKreditAPI.LOGIN_OR_SIGN_UP, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.e("dsadsadsada", response + "");

                pbLogin.setVisibility(View.GONE);

                try {
                    JSONObject jsonObject = new JSONObject(response);

                    String message = jsonObject.getString("message");
                    String status = jsonObject.getString("status");
                    String user_status = jsonObject.getString("user_status");

                    if (status.equals("1")) {
                        JSONObject object = jsonObject.getJSONObject("data");

                        String user_id = object.getString("user_id");
                        String email = object.getString("email");
                        String mobile = object.getString("mobile");
                        String otp = object.getString("otp");

                        openOTPVerification(user_id, email, user_status, mobile, otp);

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("Error","login : "+error.getMessage());
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("mobile", mMobile);
                Log.e("dsadadsadas", params + "");
                return params;
            }
        };

        RequestQueue requestQueue = SingletonRequestQueue.getInstance(ActivityLogin.this).getRequestQueue();
        requestQueue.add(stringRequest);
    }

    private void openOTPVerification(String user_id, String email, String user_status, String mobile, String otp) {
        Intent intentOTPVerification = new Intent(ActivityLogin.this, ActivityOTPVerification.class);

        intentOTPVerification.putExtra("KEY_USER_ID", user_id);
        intentOTPVerification.putExtra("KEY_EMAIL", email);
        intentOTPVerification.putExtra("KEY_MOBILE", mobile);
        intentOTPVerification.putExtra("KEY_OTP", otp);
        intentOTPVerification.putExtra("KEY_USER_STATUS", user_status);

        startActivity(intentOTPVerification);
        finishAffinity();
    }

    private void getNumber() {

        mCredentialsApiClient = new GoogleApiClient.Builder(getBaseContext())
                .addConnectionCallbacks(this)
                .enableAutoManage(ActivityLogin.this, ActivityLogin.this)
                .addApi(Auth.CREDENTIALS_API)
                .build();

        HintRequest hintRequest = new HintRequest.Builder()
                .setHintPickerConfig(new CredentialPickerConfig.Builder()
                        .setShowCancelButton(true)
                        .build())
                .setPhoneNumberIdentifierSupported(true)
                .build();

        PendingIntent intent =
                Auth.CredentialsApi.getHintPickerIntent(mCredentialsApiClient, hintRequest);
        try {
            startIntentSenderForResult(intent.getIntentSender(), RC_HINT, null, 0, 0, 0, new Bundle());
        } catch (IntentSender.SendIntentException e) {
            Log.e("Login", "Could not start hint picker Intent", e);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_HINT) {
            if (resultCode == RESULT_OK) {
                Credential cred = data.getParcelableExtra(Credential.EXTRA_KEY);
                if (cred != null) {
                    etMobileLogin.setText(cred.getId().substring(3));
                    mMobile = etMobileLogin.getText().toString().trim();

                    if (mMobile.length() == 10) {
                        ivCheckMobileNumber.setColorFilter(ContextCompat.getColor(ActivityLogin.this, R.color.green), android.graphics.PorterDuff.Mode.MULTIPLY);
                    } else {
                        ivCheckMobileNumber.setColorFilter(ContextCompat.getColor(ActivityLogin.this, R.color.light_grey), android.graphics.PorterDuff.Mode.MULTIPLY);

                    }
                } else {

                    Toast.makeText(ActivityLogin.this, "Enter Mobile number", Toast.LENGTH_SHORT).show();

                    mMobile = Objects.requireNonNull(etMobileLogin.getText()).toString().trim();
                    if (mMobile.length() == 10) {
                        ivCheckMobileNumber.setColorFilter(ContextCompat.getColor(ActivityLogin.this, R.color.green), android.graphics.PorterDuff.Mode.MULTIPLY);
                    } else {
                        ivCheckMobileNumber.setColorFilter(ContextCompat.getColor(ActivityLogin.this, R.color.light_grey), android.graphics.PorterDuff.Mode.MULTIPLY);
                    }
                }


            }
        }
    }


    private boolean checkForPlayServices() {

        GoogleApiAvailability googleApiAvailability = GoogleApiAvailability.getInstance();
        int resultCode = googleApiAvailability.isGooglePlayServicesAvailable(ActivityLogin.this);

        if (resultCode != ConnectionResult.SUCCESS) {
            if (googleApiAvailability.isUserResolvableError(resultCode)) {
                googleApiAvailability.getErrorDialog(ActivityLogin.this, resultCode,
                        PLAY_SERVICES_REQUEST).show();

                Log.e("jkljkljkljkljkl", "jkljkljkl");


            } else {
                Toast.makeText(ActivityLogin.this,
                        "This device is not supported.", Toast.LENGTH_LONG)
                        .show();

                Log.e("jkljkljkljkljkl", "jkljkljkl");
            }
            return false;
        }
        return true;
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    protected void onPause() {
        super.onPause();

        if (checkForPlayServices()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                mCredentialsApiClient.stopAutoManage(ActivityLogin.this);
                mCredentialsApiClient.disconnect();
            }

        }

    }

}
