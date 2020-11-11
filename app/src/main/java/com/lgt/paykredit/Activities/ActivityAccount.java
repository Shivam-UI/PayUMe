package com.lgt.paykredit.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.lgt.paykredit.R;
import com.lgt.paykredit.extras.Common;
import com.lgt.paykredit.extras.Language;
import com.lgt.paykredit.extras.PayKreditAPI;
import com.lgt.paykredit.extras.SingletonRequestQueue;

public class ActivityAccount extends AppCompatActivity {

    private ImageView ivBackAccount;
    private LinearLayout ll_language, ll_reminder;
    private TextView tv_current_language, tv_account_statement, tv_backup, tv_reminder, tv_language;
    private RelativeLayout rl_account_statement;
    private SharedPreferences sharedPreferences;
    private String mUserID;
    private RelativeLayout rlbackup;
    private Context context;
    private ActivityAccount activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);

        context = activity = this;
        ivBackAccount = findViewById(R.id.ivBackAccount);
        ll_language = findViewById(R.id.ll_language);
        tv_current_language = findViewById(R.id.tv_current_language);
        rl_account_statement = findViewById(R.id.rl_account_statement);
        ll_reminder = findViewById(R.id.ll_reminder);
        rlbackup = findViewById(R.id.rlbackup);
        sharedPreferences = getSharedPreferences("USER_DATA", MODE_PRIVATE);
        ivBackAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        if (sharedPreferences.contains("KEY_USER_ID")) {
            mUserID = sharedPreferences.getString("KEY_USER_ID", "");
            Log.e("Sadrrewrew", mUserID);
        }
        // convert language8
        convertLANG();
        ll_language.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ActivityAccount.this, ActivitySelectLanguage.class);
                intent.putExtra(Common.FROM_MAIN, Common.FROM_MAIN);
                startActivity(intent);
                finish();
            }
        });

        if (Common.getLanguage(getApplicationContext()).equalsIgnoreCase(Common.HINDI)) {
            tv_current_language.setText("Hindi");
           /* new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    //convertLANG();
                }
            }, 1000);*/
        } else {
            tv_current_language.setText("English");
        }

        rl_account_statement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = PayKreditAPI.WEB_URL + "pdf/index.php?user_id=" + mUserID;
                // Intent i = new Intent(Intent.ACTION_VIEW); //
                // i.setData(Uri.parse(url));
                // startActivity(i);
                String ac_stmt = "Account Statement";
                openWebView(url,ac_stmt);
              /*  Intent intent = new Intent(Intent.ACTION_VIEW);

                intent.setDataAndType(Uri.parse( "http://docs.google.com/viewer?url=" + url), "text/html");

                startActivity(intent);*/
               /* Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setDataAndType(Uri.parse(url), "application/pdf");
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);*/
            }
        });
        rlbackup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(ActivityAccount.this, "Auto backup", Toast.LENGTH_SHORT).show();
            }
        });
        ll_reminder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(ActivityAccount.this, "We will auto reminder in 10 days", Toast.LENGTH_LONG).show();
            }
        });


    }

    private void call(String url) {

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.e("jbajks", response.toString());
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        RequestQueue requestQueue = SingletonRequestQueue.getInstance(ActivityAccount.this).getRequestQueue();
        requestQueue.add(stringRequest);
    }

    private void convertLANG() {
        tv_account_statement = findViewById(R.id.tv_account_statement);
        tv_backup = findViewById(R.id.tv_backup);
        tv_reminder = findViewById(R.id.tv_reminder);
        tv_language = findViewById(R.id.tv_language);

        if (Common.getLanguage(activity).equalsIgnoreCase(Common.HINDI)) {

            tv_backup.setText("बैकअप");
            tv_account_statement.setText("अकाउंट रिपोर्ट्स");
            tv_reminder.setText("रिमाइंडर");
            tv_language.setText("भाषा");

        } else {

            //If not, display "no connection" warning:
            tv_account_statement.setText(tv_account_statement.getText().toString());
            tv_backup.setText(tv_backup.getText().toString());
            tv_reminder.setText(tv_reminder.getText().toString());
            tv_current_language.setText(tv_current_language.getText().toString());
            tv_language.setText(tv_language.getText().toString());
        }
    }

    private void openWebView(String urlToOpen, String typeOfURL) {
        Intent webViewIntent = new Intent(ActivityAccount.this, ActivityWebView.class);
        webViewIntent.putExtra("KEY_WEB_URL", urlToOpen);
        webViewIntent.putExtra("KEY_URL_TYPE", typeOfURL);
        startActivity(webViewIntent);
    }
}
