package com.lgt.paykredit.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.translate.Translate;
import com.google.cloud.translate.TranslateOptions;
import com.google.cloud.translate.Translation;
import com.lgt.paykredit.Fragments.FragmentCreditBook;
import com.lgt.paykredit.R;
import com.lgt.paykredit.extras.Common;
import com.lgt.paykredit.extras.Language;
import com.lgt.paykredit.extras.PayKreditAPI;
import com.lgt.paykredit.extras.SingletonRequestQueue;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {


    private DrawerLayout drawer;
    private ImageView ivHamburger, ivNotification;
    private TextView tvToolbar, tvHeaderEmail, tvHeaderName;
    private String urlToOpen, urlType;
    public static BottomNavigationView btmNavigation;
    private Fragment mSelectedFragment = null;
    private FragmentManager mFragmentManager;
    private boolean shouldAddToBackStack;
    private String mBackStackName, tagName, mUserID;
        private SharedPreferences sharedPreferences;
        private SharedPreferences.Editor editor;
    NavigationView navigationView;
    private Common common;
    private LinearLayout llHeader;
    Translate translate;
    private Context context;
    private MainActivity activity;
    private List<Locale> support_locale = new ArrayList<>();

    public static MainActivity mainActivity;

    CircleImageView ivHeader;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        context = activity = this;
        sharedPreferences = MainActivity.this.getSharedPreferences("USER_DATA", MODE_PRIVATE);
        editor = sharedPreferences.edit();

        mainActivity = this;
        common = new Common(MainActivity.this);
        btmNavigation = findViewById(R.id.btmNavHomeScreen);
        btmNavigation.setOnNavigationItemSelectedListener(btmListener);
        mFragmentManager = getSupportFragmentManager();
        navigationView = findViewById(R.id.nav_view);
        ivNotification = findViewById(R.id.ivNotification);
        ivHamburger = findViewById(R.id.ivHamburger);
        tvToolbar = findViewById(R.id.tvToolbar);
        drawer = findViewById(R.id.drawer_layout);
        llHeader = findViewById(R.id.llHeader);
        tvToolbar.setText("Credit Book");


        ivNotification.setVisibility(View.VISIBLE);
        navigationView.setNavigationItemSelectedListener(this);


        if (sharedPreferences.contains("KEY_USER_ID")) {
            mUserID = sharedPreferences.getString("KEY_USER_ID", "");
            Log.e("Sadrrewrew", mUserID);
        }


        View headerView = navigationView.getHeaderView(0);

        ivHeader = headerView.findViewById(R.id.ivHeader);
        tvHeaderName = headerView.findViewById(R.id.tvHeaderName);
        tvHeaderEmail = headerView.findViewById(R.id.tvHeaderEmail);


        iniNavViews();

        llHeader = headerView.findViewById(R.id.llHeader);


        llHeader.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, ActivityProfile.class));
                if (drawer.isDrawerOpen(GravityCompat.START)) {
                    drawer.closeDrawer(GravityCompat.START);
                }
            }
        });

        ivHamburger.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawer.openDrawer(Gravity.LEFT);
            }
        });

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.frameHomeScreen,
                    new FragmentCreditBook()).commit();
        }

        getProfile();


    }


    private void iniNavViews() {
        Menu menu = navigationView.getMenu();

        // find MenuItem you want to change
        MenuItem nav_profile = menu.findItem(R.id.nav_profile);

        MenuItem nav_account = menu.findItem(R.id.nav_account);
        MenuItem Help = menu.findItem(R.id.nav_help);
        MenuItem nav_share = menu.findItem(R.id.nav_share);
        MenuItem nav_about_us = menu.findItem(R.id.nav_about_us);
        MenuItem nav_privacy_policy = menu.findItem(R.id.nav_privacy_policy);
        MenuItem nav_signout = menu.findItem(R.id.nav_signout);


        if (Common.getLanguage(getApplicationContext()).equalsIgnoreCase(Common.HINDI)) {

            //If there is internet connection, get translate service and start translation:
            tvToolbar.setText("क्रेडिट बुक");
            nav_profile.setTitle("प्रोफाइल");
            nav_account.setTitle("अकाउंट");
            Help.setTitle("हेल्प");
            nav_share.setTitle("शेयर");
            nav_about_us.setTitle("हमारे बारे मैं");
            nav_privacy_policy.setTitle("प्राइवेसी पालिसी");
            nav_signout.setTitle("sign आउट");
        } else {

            tvToolbar.setText(tvToolbar.getText().toString());
            nav_profile.setTitle(nav_profile.getTitle().toString());
            nav_account.setTitle(nav_account.getTitle().toString());
            Help.setTitle(Help.getTitle().toString());
            nav_share.setTitle(nav_share.getTitle().toString());
            nav_about_us.setTitle(nav_about_us.getTitle().toString());
            nav_privacy_policy.setTitle(nav_privacy_policy.getTitle().toString());
            nav_signout.setTitle(nav_signout.getTitle().toString());

        }


    }


    public static MainActivity getInstance() {
        return mainActivity;
    }

    public void getProfile() {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, PayKreditAPI.GET_PROFILE, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.e("sadsadrewrewr", response + "");

                try {
                    JSONObject jsonObject = new JSONObject(response);

                    String message = jsonObject.getString("message");
                    String status = jsonObject.getString("status");

                    if (status.equals("1")) {

                        JSONObject data = jsonObject.getJSONObject("data");

                        String business_name = data.getString("business_name");
                        String mobile = data.getString("mobile");
                        String email = data.getString("email");
                        String about_us = data.getString("about_us");
                        String contact_person_name = data.getString("contact_person_name");
                        String user_image = data.getString("user_image");

                        tvHeaderName.setText(business_name);
                        tvHeaderEmail.setText(email);

                        // Inside about_us -> GST number is being received from backend. Change if required.

                        editor.putString("KEY_BUSINESS_NAME", business_name);
                        editor.putString("KEY_GST", about_us);
                        editor.commit();
                        editor.apply();

                        Glide.with(MainActivity.this).load(user_image).apply(new RequestOptions().placeholder(R.drawable.user_icon).error(
                                R.drawable.user_icon)).diskCacheStrategy(DiskCacheStrategy.ALL).into(ivHeader);

                    } else {
                        Toast.makeText(MainActivity.this, "" + message, Toast.LENGTH_SHORT).show();
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
                params.put("user_id", mUserID);
                Log.e("fghfhhhhhhhhhhhh", params + "");
                return params;
            }
        };

        RequestQueue requestQueue = SingletonRequestQueue.getInstance(MainActivity.this).getRequestQueue();
        requestQueue.add(stringRequest);

    }


    BottomNavigationView.OnNavigationItemSelectedListener btmListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
            switch (menuItem.getItemId()) {

                case R.id.navigation_credit_book:
                    mSelectedFragment = new FragmentCreditBook();
                    mBackStackName = "FragmentCreditBook";
                    tagName = "FragmentHomeScreen";
                    replaceFragment(mSelectedFragment, shouldAddToBackStack, mBackStackName, tagName);
                    return true;

                case R.id.navigation_invoices:
                    //startActivity(new Intent(MainActivity.this, ActivityInvoice.class));

                    //startActivity(new Intent(MainActivity.this, InvoiceMainPage.class));
                    //Changine invoice landling page as per ppt, 23-03-20
                    btmNavigation.getMenu().getItem(0).setChecked(true);
                    startActivity(new Intent(MainActivity.this, InvoiceNewLandingPage.class));

                   /* mSelectedFragment = new FragmentInvoices();
                    mBackStackName = "FragmentInvoices";
                    tagName = "FragmentInvoices";
                    replaceFragment(mSelectedFragment, shouldAddToBackStack, mBackStackName, tagName);*/
                    return true;
            }

            return false;
        }
    };


    private void replaceFragment(Fragment mSelectedFragment, boolean shouldAddToBackStack, String mBackStackName, String tagName) {
        getSupportFragmentManager().beginTransaction().replace(R.id.frameHomeScreen, mSelectedFragment, tagName).addToBackStack(mBackStackName).commit();
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
            {
                if (mFragmentManager.getBackStackEntryCount() > 0) {
                    for (int i = 0; i < mFragmentManager.getBackStackEntryCount(); i++) {
                        mFragmentManager.popBackStack();
                        btmNavigation.getMenu().getItem(0).setChecked(true);
                    }
                } else {
                    finish();
                    btmNavigation.getMenu().getItem(0).setChecked(true);
                }
            }


        } else {

            if (mFragmentManager.getBackStackEntryCount() > 0) {
                for (int i = 0; i < mFragmentManager.getBackStackEntryCount(); i++) {
                    mFragmentManager.popBackStack();
                    btmNavigation.getMenu().getItem(0).setChecked(true);
                }
            } else {
                super.onBackPressed();
            }


        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }

        int id = item.getItemId();

        if (id == R.id.nav_profile) {
            startActivity(new Intent(MainActivity.this, ActivityProfile.class));
        }

        if (id == R.id.nav_invoice) {
            //HIding this 6 June 2020
            // startActivity(new Intent(MainActivity.this, ActivityInvoice.class));
            //startActivity(new Intent(MainActivity.this, ActivityCreateInvoice.class));
        }

        if (id == R.id.nav_account) {
            startActivity(new Intent(MainActivity.this, ActivityAccount.class));
        }
        if (id == R.id.nav_share) {
            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_TEXT, "Now replace your traditional Udhar bahi khata by new " + getString(R.string.app_name) + " " +
                    "digital mobile app. It is 100% Free, Safe and Secure. Download it: \n" + Common.play_store_url + getPackageName());
            sendIntent.setType("text/plain");
            Intent shareIntent = Intent.createChooser(sendIntent, "Share Paykredit");
            startActivity(shareIntent);
        }


        if (id == R.id.nav_help) {

            urlToOpen = PayKreditAPI.HELP;
            urlType = "Help";
            openWebView(urlToOpen, urlType);
        }

        if (id == R.id.nav_about_us) {
            urlToOpen = PayKreditAPI.ABOUT_US;
            urlType = "About us";
            openWebView(urlToOpen, urlType);

        }

        if (id == R.id.nav_privacy_policy) {
            urlToOpen = PayKreditAPI.PRIVACY;
            urlType = "Privacy Policy";
            openWebView(urlToOpen, urlType);
        }

        if (id == R.id.nav_signout) {
            logOutUser();
        }


        return false;
    }

    private void openWebView(String urlToOpen, String typeOfURL) {
        Intent webViewIntent = new Intent(MainActivity.this, ActivityWebView.class);
        webViewIntent.putExtra("KEY_WEB_URL", urlToOpen);
        webViewIntent.putExtra("KEY_URL_TYPE", typeOfURL);
        startActivity(webViewIntent);
    }


    private void logOutUser() {
        new AlertDialog.Builder(MainActivity.this)
                .setIcon(R.drawable.alert)
                .setTitle("Logout")
                .setMessage("Are you sure want to log out?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        editor.clear();
                        editor.apply();

                     /*   assert getFragmentManager() != null;
                        getFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);*/
                        Intent intent = new Intent(MainActivity.this, ActivitySplash.class);
                        //    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK );
                        startActivity(intent);
                        finishAffinity();
                    }
                })

                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                }).show();
    }

}


