package com.lgt.paykredit.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.lgt.paykredit.Fragments.FragmentCreditBook;
import com.lgt.paykredit.Fragments.InvoiceDashboardFragment;
import com.lgt.paykredit.R;
import com.lgt.paykredit.extras.Common;
import com.lgt.paykredit.extras.PayKreditAPI;
import com.lgt.paykredit.extras.SingletonRequestQueue;
import com.lgt.paykredit.extras.VolleyMultipartRequest;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private DrawerLayout drawer;
    private ImageView ivHamburger, ivNotification;
    private TextView tvToolbar, tvHeaderEmail, tvHeaderName, tv_user_profile_name;
    private String urlToOpen, urlType;
    FrameLayout  iv_uploadProfilePicture;
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
    private Context context;
    private MainActivity activity;
    private List<Locale> support_locale = new ArrayList<>();
    public static Toolbar tv_common_toolbar, tv_creditBook_actionBar;
    public static ImageView ivHamburgerS;
    public static MainActivity mainActivity;
    private Uri filePath;
    private Bitmap bitmap, converetdImage;
    int bitmapSize;

    CircleImageView ivHeader, civ_user_profile_logo;

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
        iv_uploadProfilePicture = findViewById(R.id.iv_uploadProfilePicture);
        navigationView = findViewById(R.id.nav_view);
        ivNotification = findViewById(R.id.ivNotification);
        ivHamburgerS = findViewById(R.id.ivHamburgerS);
        tv_common_toolbar = findViewById(R.id.tv_common_toolbar);
        tv_creditBook_actionBar = findViewById(R.id.tv_creditBook_actionBar);
        ivHamburger = findViewById(R.id.ivHamburger);
        tvToolbar = findViewById(R.id.tvToolbar);
        drawer = findViewById(R.id.drawer_layout);
        llHeader = findViewById(R.id.llHeader);
        tvToolbar.setText("Credit Book");


        ivNotification.setVisibility(View.GONE);
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
                    new InvoiceDashboardFragment()).commit();
            tv_common_toolbar.setVisibility(View.VISIBLE);
            tv_creditBook_actionBar.setVisibility(View.GONE);
        }

        iv_uploadProfilePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                askForPermission();
            }
        });

        getProfile();
    }

    private void askForPermission() {
        Log.e("klskdlsakdsal", "askForPermission: ");
        Dexter.withActivity(MainActivity.this).withPermissions(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE).
                withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {

                        if (report.areAllPermissionsGranted()) {
                            selectImage();
                        } else if (report.isAnyPermissionPermanentlyDenied()) {
                            Toast.makeText(MainActivity.this, "Please allow all permissions from setting", Toast.LENGTH_SHORT).show();
                            showSettingsDialog();
                        } else {
                            Toast.makeText(MainActivity.this, "All permissions are required", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                        token.continuePermissionRequest();

                    }
                }).onSameThread().check();
    }

    private void selectImage() {
        final CharSequence[] options = {"Take Photo", "Choose from Gallery", "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("Add Photo!");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (options[item].equals("Take Photo")) {

                    Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(intent, 1);

                } else if (options[item].equals("Choose from Gallery")) {
                    Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(intent, 2);

                } else if (options[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();

    }

    private void showSettingsDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("Need Permissions");
        builder.setMessage("This app needs permission to use this feature. You can grant them in app settings.");
        builder.setPositiveButton("GOTO SETTINGS", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                openSettings();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.show();
    }

    private void openSettings() {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", (getPackageName()), null);
        intent.setData(uri);
        startActivityForResult(intent, 101);
    }


    private void iniNavViews() {
        Menu menu = navigationView.getMenu();

        // find MenuItem you want to change
        MenuItem nav_profile = menu.findItem(R.id.nav_profile);

        civ_user_profile_logo = findViewById(R.id.civ_user_profile_logo);
        tv_user_profile_name = findViewById(R.id.tv_user_profile_name);
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

                        Glide.with(MainActivity.this).load(user_image).apply(new RequestOptions().placeholder(R.drawable.user_icon).error(
                                R.drawable.user_icon)).diskCacheStrategy(DiskCacheStrategy.ALL).into(civ_user_profile_logo);

                        tv_user_profile_name.setText("Hi " + business_name);

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
                    // mSelectedFragment = new FragmentCreditBook();
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
                    replaceFragment(mSelectedFragment, shouldAddToBackStack, mBackStackName, tagName); */
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

        if (id == R.id.nav_dashboard) {
            mSelectedFragment = new InvoiceDashboardFragment();
            mBackStackName = "InvoiceDashboardFragment";
            tagName = "FragmentHomeScreen";
            replaceFragment(mSelectedFragment, shouldAddToBackStack, mBackStackName, tagName);
        }

        if (id == R.id.nav_profile) {
            startActivity(new Intent(MainActivity.this, ActivityProfile.class));
        }

        if (id == R.id.nav_invoice) {
            //Hiding this 6 June 2020
            // startActivity(new Intent(MainActivity.this, ActivityInvoice.class));
            //startActivity(new Intent(MainActivity.this, ActivityCreateInvoice.class));
        }

        if (id == R.id.nav_account_report) {
            Toast.makeText(context, "Account Report", Toast.LENGTH_SHORT).show();
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.e("CLICKEDINBYTES", "Data_Found");
        if (resultCode == RESULT_OK) {
            Log.e("CLICKEDINBYTES", "RESULT_CODE_FOUND");
            if (requestCode == 1) {
                Log.e("CLICKEDINBYTES", "RECIVED_IMAGE_FOUND");
                bitmap = (Bitmap) data.getExtras().get("data");
                // civ_user_profile_logo.setImageBitmap(bitmap);
                Glide.with(MainActivity.this).load(bitmap).apply(new RequestOptions()
                        .override(192, 192)).into(civ_user_profile_logo);
                converetdImage = getResizedBitmap(bitmap, 400);
                Log.e("CLICKEDINBYTES", bitmap.getAllocationByteCount() + "");
                Log.e("CLICKEDINKILLOBYTE", (bitmap.getAllocationByteCount() / 1024) + "");
                sendImage();
            }

        }
        if (requestCode == 2 && resultCode == RESULT_OK && data != null) {

            Log.e("gjhghhhhhhk", "called");

            filePath = data.getData();

            Log.e("filepaththh", filePath + "");
            try {
                bitmap = MediaStore.Images.Media.getBitmap(MainActivity.this.getContentResolver(), filePath);
                bitmapSize = bitmap.getAllocationByteCount();

                converetdImage = getResizedBitmap(bitmap, 400);

                Log.e("oldbitmap", bitmap.getAllocationByteCount() + "");
                Log.e("pickfromgallery", bitmap.getAllocationByteCount() + "");
                Log.e("pickfromgallerydasdas", (bitmap.getAllocationByteCount() / 1024) + "");

                Log.e("dasdasddrerer", converetdImage.getAllocationByteCount() + "");
                Log.e("ytytyyuyutyuytuty", (converetdImage.getAllocationByteCount() / 1024) + "");

                if (converetdImage.getAllocationByteCount() > 512000) {
                    //common.showSnackBar(etEmailProfile, );
                    Toast.makeText(context, "Max. upload size is 1 MB", Toast.LENGTH_SHORT).show();
                } else {
                    Glide.with(MainActivity.this).load(bitmap).apply(new RequestOptions().override(192, 192)).
                            into(civ_user_profile_logo);
                    sendImage();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    private Bitmap getResizedBitmap(Bitmap image, int maxSize) {
        int width = image.getWidth();
        int height = image.getHeight();

        float bitmapRatio = (float) width / (float) height;
        if (bitmapRatio > 1) {
            width = maxSize;
            height = (int) (width / bitmapRatio);
        } else {
            height = maxSize;
            width = (int) (height * bitmapRatio);
        }
        return Bitmap.createScaledBitmap(image, width, height, true);
    }

    private void sendImage() {

        // pbProfile.setVisibility(View.VISIBLE);

        VolleyMultipartRequest volleyMultipartRequest = new VolleyMultipartRequest(Request.Method.POST, PayKreditAPI.PROFILE_IMAGE,
                new com.android.volley.Response.Listener<NetworkResponse>() {
                    @Override
                    public void onResponse(NetworkResponse response) {
                        Log.e("UPLOADDDD", response+ "");

                        try {
                            JSONObject jsonObject = new JSONObject(new String(response.data));
                            String status = jsonObject.getString("status");
                            String message = jsonObject.getString("message");
                            // pbProfile.setVisibility(View.GONE);
                            Log.e("UPLOADDDD", status+ " | " +message);
                            if (status.equals("1")) {
                                Toast.makeText(MainActivity.this, "" + message, Toast.LENGTH_SHORT).show();
                                MainActivity mainActivity = MainActivity.getInstance();
                                mainActivity.getProfile();
                                getProfile();
                            } else {
                                Toast.makeText(MainActivity.this, "" + message, Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            // pbProfile.setVisibility(View.GONE);
                            e.printStackTrace();
                        }

                        Log.e("MULTIPART", response + "");

                    }
                }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // pbProfile.setVisibility(View.GONE);
                Log.e("MULTIPART", error + "");
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("user_id", mUserID);
                Log.e("PPPPPPP", params + "");
                return params;
            }

            @Override
            protected Map<String, DataPart> getByteData() throws AuthFailureError {
                Map<String, DataPart> params = new HashMap<>();
                long imagename = System.currentTimeMillis();
                params.put("image", new DataPart(imagename + ".png", getFileDataFromDrawable(converetdImage)));
                Log.e("PARAMS", params + "");
                return params;
            }
        };
        Volley.newRequestQueue(MainActivity.this).add(volleyMultipartRequest);
    }

    private byte[] getFileDataFromDrawable(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 80, byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
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


