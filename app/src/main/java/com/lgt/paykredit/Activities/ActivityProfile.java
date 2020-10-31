package com.lgt.paykredit.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
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
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.lgt.paykredit.R;
import com.lgt.paykredit.bottomsheets.BottomSheetBusinessCardDrawable;
import com.lgt.paykredit.extras.Common;
import com.lgt.paykredit.extras.Language;
import com.lgt.paykredit.extras.PayKreditAPI;
import com.lgt.paykredit.extras.SingletonRequestQueue;
import com.lgt.paykredit.extras.VolleyMultipartRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import de.hdodenhof.circleimageview.CircleImageView;

public class ActivityProfile extends AppCompatActivity {

    private LinearLayout llShareBusinessCard;
    private ImageView ivBackSingleUserTransaction;
    private TextView tvUpdateProfile, tv_Share_card, tv_bussnuss_name, tv_gst_no, tv_mobile_no, tv_emailid, tv_about_us, tv_address, tv_person_name;

    private Uri filePath;
    private Bitmap bitmap, converetdImage;
    int bitmapSize;

    private CircleImageView ivUserProfile;

    private EditText etNameProfile, etGSTINProfile, etMobileNumberProfile, etEmailProfile, etAboutUsProfile, etContactPersonNameProfile, et_address;
    private String mUserName, nameProfile, gstNumber, mobileNumber, emailID, aboutUS, contactPersonName, mNameToSend, mEmailToSend, mMobileToSend, mSendImage, mAddress, GST_NO;
    private Common common;

    private ProgressBar pbProfile;

    private String mUserID;
    private SharedPreferences sharedPreferences;

    private ActivityProfile activity;
    private Context context;

    private Pattern mPattern;
    private Matcher matcher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        context = activity = this;
        sharedPreferences = ActivityProfile.this.getSharedPreferences("USER_DATA", MODE_PRIVATE);
        convertLang();
        llShareBusinessCard = findViewById(R.id.llShareBusinessCard);
        ivBackSingleUserTransaction = findViewById(R.id.ivBackSingleUserTransaction);

        etNameProfile = findViewById(R.id.etNameProfile);
        etGSTINProfile = findViewById(R.id.etGSTINProfile);
        etMobileNumberProfile = findViewById(R.id.etMobileNumberProfile);
        etEmailProfile = findViewById(R.id.etEmailProfile);
        etAboutUsProfile = findViewById(R.id.etAboutUsProfile);
        etContactPersonNameProfile = findViewById(R.id.etContactPersonNameProfile);
        ivUserProfile = findViewById(R.id.ivUserProfile);
        pbProfile = findViewById(R.id.pbProfile);
        et_address = findViewById(R.id.et_address);
        //{2}[A-Z]{5}\d{4}[A-Z]{1}[A-Z\d]{1}[Z]{1}[A-Z\d]{1}
        mPattern = Pattern.compile("^([0][1-9]|[1-2][0-9]|[3][0-7])([a-zA-Z]{5}[0-9]{4}[a-zA-Z]{1}[1-9a-zA-Z]{1}[zZ]{1}[0-9a-zA-Z]{1})+$");
        common = new Common(ActivityProfile.this);

        if (sharedPreferences.contains("KEY_USER_ID")) {
            mUserID = sharedPreferences.getString("KEY_USER_ID", "");
            Log.e("Dasdasddasdsada", mUserID + "");
        }

        ivBackSingleUserTransaction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        llShareBusinessCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              /*  BottomSheetBusinessCard bottomSheetBusinessCard = new BottomSheetBusinessCard();
                bottomSheetBusinessCard.show(getSupportFragmentManager(),"BottomSheetBusinessCard");*/
                BottomSheetBusinessCardDrawable bottomSheetBusinessCardDrawable = new BottomSheetBusinessCardDrawable();

                Bundle bundle = new Bundle();
                bundle.putString("KEY_BUSINESS_NAME", mNameToSend);
                bundle.putString("KEY_USER_NAME", etContactPersonNameProfile.getText().toString());
                bundle.putString("KEY_BUSINESS_MOBILE", mMobileToSend);
                bundle.putString("KEY_BUSINESS_EMAIL", mEmailToSend);
                bundle.putString("KEY_BUSINESS_IMAGE", mSendImage);
                bundle.putString(Common.address_key, mAddress);
                bundle.putString(Common.GST_NO_key, mAddress);

                bottomSheetBusinessCardDrawable.setArguments(bundle);
                bottomSheetBusinessCardDrawable.show(getSupportFragmentManager(), "BottomSheetBusinessCardDrawable");

            }
        });

        if (!common.isConnectingToInternet()) {
            Toast.makeText(ActivityProfile.this, "Check internet connection", Toast.LENGTH_SHORT).show();
        } else {
            //GET PROFILE DATA
            getProfileData();
        }

        ivUserProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                askForPermission();
            }
        });

        tvUpdateProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fieldValidation();
            }
        });

        etMobileNumberProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(ActivityProfile.this, "Mobile number can't be changed", Toast.LENGTH_SHORT).show();
            }
        });

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //convertLang();
            }
        }, 250);


    }

    private void convertLang() {
        tv_Share_card = findViewById(R.id.tv_Share_card);
        tv_bussnuss_name = findViewById(R.id.tv_bussnuss_name);
        tv_gst_no = findViewById(R.id.tv_gst_no);
        tv_mobile_no = findViewById(R.id.tv_mobile_no);
        tv_emailid = findViewById(R.id.tv_emailid);
        tv_about_us = findViewById(R.id.tv_about_us);
        tv_address = findViewById(R.id.tv_address);
        tv_person_name = findViewById(R.id.tv_person_name);
        tvUpdateProfile = findViewById(R.id.tvUpdateProfile);

        if (Common.getLanguage(getApplicationContext()).equalsIgnoreCase(Common.HINDI)) {

            tv_Share_card.setText("शेयर बिज़नेस कार्ड");
            tv_bussnuss_name.setText("बिज़नेस नाम");
            tv_gst_no.setText("GSTIN नंबर");
            tv_mobile_no.setText("मोबाइल नंबर");
            tv_emailid.setText("ईमेल ID");
            tv_about_us.setText("अपने बिज़नेस के बारे मैं");
            tv_address.setText("पता");
            tv_person_name.setText("कांटेक्ट पर्सन");
            tvUpdateProfile.setText("सबमिट");
        } else {

            //If not, display "no connection" warning:
            tv_Share_card.setText(tv_Share_card.getText().toString());
            tv_bussnuss_name.setText(tv_bussnuss_name.getText().toString());
            tv_gst_no.setText(tv_gst_no.getText().toString());
            tv_mobile_no.setText(tv_mobile_no.getText().toString());
            tv_emailid.setText(tv_emailid.getText().toString());
            tv_about_us.setText(tv_about_us.getText().toString());
            tv_address.setText(tv_address.getText().toString());
            tv_person_name.setText(tv_person_name.getText().toString());
        }
    }

    private void askForPermission() {

        Log.e("klskdlsakdsal", "askForPermission: ");
        Dexter.withActivity(ActivityProfile.this).withPermissions(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE).
                withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {

                        if (report.areAllPermissionsGranted()) {
                            selectImage();
                        } else if (report.isAnyPermissionPermanentlyDenied()) {
                            Toast.makeText(ActivityProfile.this, "Please allow all permissions from setting", Toast.LENGTH_SHORT).show();
                            showSettingsDialog();
                        } else {
                            Toast.makeText(ActivityProfile.this, "All permissions are required", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                        token.continuePermissionRequest();

                    }
                }).onSameThread().check();

    }

    private void showSettingsDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(ActivityProfile.this);
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {

            if (requestCode == 1) {
                bitmap = (Bitmap) data.getExtras().get("data");
                ivUserProfile.setImageBitmap(bitmap);
                Glide.with(ActivityProfile.this).load(bitmap).apply(new RequestOptions()
                        .override(192, 192)).into(ivUserProfile);
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
                bitmap = MediaStore.Images.Media.getBitmap(ActivityProfile.this.getContentResolver(), filePath);
                bitmapSize = bitmap.getAllocationByteCount();

                converetdImage = getResizedBitmap(bitmap, 400);

                Log.e("oldbitmap", bitmap.getAllocationByteCount() + "");
                Log.e("pickfromgallery", bitmap.getAllocationByteCount() + "");
                Log.e("pickfromgallerydasdas", (bitmap.getAllocationByteCount() / 1024) + "");

                Log.e("dasdasddrerer", converetdImage.getAllocationByteCount() + "");
                Log.e("ytytyyuyutyuytuty", (converetdImage.getAllocationByteCount() / 1024) + "");

                if (converetdImage.getAllocationByteCount() > 512000) {
                    common.showSnackBar(etEmailProfile, "Max. upload size is 1 MB");

                } else {
                    Glide.with(ActivityProfile.this).load(bitmap).apply(new RequestOptions().override(192, 192)).
                            into(ivUserProfile);

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

        pbProfile.setVisibility(View.VISIBLE);

        VolleyMultipartRequest volleyMultipartRequest = new VolleyMultipartRequest(Request.Method.POST, PayKreditAPI.PROFILE_IMAGE,
                new com.android.volley.Response.Listener<NetworkResponse>() {
                    @Override
                    public void onResponse(NetworkResponse response) {
                        Log.e("UPLOADDDD", response+ "");

                        try {
                            JSONObject jsonObject = new JSONObject(new String(response.data));
                            String status = jsonObject.getString("status");
                            String message = jsonObject.getString("message");
                            pbProfile.setVisibility(View.GONE);
                            Log.e("UPLOADDDD", status+ " | " +message);
                            if (status.equals("1")) {
                                Toast.makeText(ActivityProfile.this, "" + message, Toast.LENGTH_SHORT).show();
                                MainActivity mainActivity = MainActivity.getInstance();
                                mainActivity.getProfile();
                                getProfileData();
                            } else {
                                Toast.makeText(ActivityProfile.this, "" + message, Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            pbProfile.setVisibility(View.GONE);
                            e.printStackTrace();
                        }

                        Log.e("MULTIPART", response + "");

                    }
                }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                pbProfile.setVisibility(View.GONE);
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
        Volley.newRequestQueue(ActivityProfile.this).add(volleyMultipartRequest);

    }

    private byte[] getFileDataFromDrawable(Bitmap bitmap) {

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 80, byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }


    private void selectImage() {

        final CharSequence[] options = {"Take Photo", "Choose from Gallery", "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(ActivityProfile.this);
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


    private void fieldValidation() {

        nameProfile = etNameProfile.getText().toString().trim();
        gstNumber = etGSTINProfile.getText().toString().trim();
        mobileNumber = etMobileNumberProfile.getText().toString().trim();
        emailID = etEmailProfile.getText().toString().trim();
        aboutUS = etAboutUsProfile.getText().toString().trim();
        contactPersonName = etContactPersonNameProfile.getText().toString().trim();
        mUserName = etContactPersonNameProfile.getText().toString().trim();
        mAddress = et_address.getText().toString().trim();

        if (TextUtils.isEmpty(nameProfile)) {
            etNameProfile.setError("Enter business name");
            etNameProfile.requestFocus();
            return;
        }

        if (nameProfile.length() < 4) {
            etNameProfile.setError("Enter valid name");
            etNameProfile.requestFocus();
            return;
        }

//        if (!TextUtils.isEmpty(gstNumber)) {
//            if (gstNumber.length() < 4) {
//                etGSTINProfile.setError("Enter valid GST number");
//                etGSTINProfile.requestFocus();
//                return;
//            }
//        }

        matcher = mPattern.matcher(gstNumber);
        if (!TextUtils.isEmpty(gstNumber)) {
            if (!matcher.find()) {
                etGSTINProfile.setError("Enter Valid GST Number");
                etGSTINProfile.requestFocus();
                return;
            }
        }


        if (TextUtils.isEmpty(emailID)) {
            etEmailProfile.setError("Enter email id");
            etEmailProfile.requestFocus();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(emailID).matches()) {
            etEmailProfile.setError("Please enter valid email id");
            etEmailProfile.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(aboutUS)) {
            etAboutUsProfile.setError("Enter abouts us");
            etAboutUsProfile.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(contactPersonName)) {
            etContactPersonNameProfile.setError("Enter contact person name");
            etContactPersonNameProfile.requestFocus();
            return;

        }
        if (TextUtils.isEmpty(mAddress)) {
            et_address.setError("Enter Address");
            et_address.requestFocus();
            return;

        }

        //API CALL UPDATE PROFILE

        updateProfileAPICall();


    }

    private void updateProfileAPICall() {
        pbProfile.setVisibility(View.VISIBLE);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, PayKreditAPI.EDIT_PROFILE, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                pbProfile.setVisibility(View.GONE);
                Log.e("dasdrewwerwe0", response.toString() + "");

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String status = jsonObject.getString("status");
                    String message = jsonObject.getString("message");

                    if (status.equals("1")) {
                        Toast.makeText(ActivityProfile.this, "" + message, Toast.LENGTH_SHORT).show();
                        getProfileData();
                    } else {
                        Toast.makeText(ActivityProfile.this, "" + message, Toast.LENGTH_SHORT).show();
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

                params.put("business_name", nameProfile);
                params.put("mobile", mobileNumber);
                params.put("email", emailID);
                params.put("about_us", aboutUS);
                params.put("contact_name", contactPersonName);
                params.put("address", mAddress);
                params.put("gst_no", gstNumber);
                //one more parameter is required for GSTIN
                params.put("user_id", mUserID);
                Log.e("dasdrewwerwe0", params + "" +gstNumber);

                return params;
            }
        };

        RequestQueue requestQueue = SingletonRequestQueue.getInstance(ActivityProfile.this).getRequestQueue();
        requestQueue.add(stringRequest);

    }

    //GET PROFILE DATA

    private void getProfileData() {

        pbProfile.setVisibility(View.VISIBLE);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, PayKreditAPI.GET_PROFILE, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                pbProfile.setVisibility(View.GONE);

                try {
                    JSONObject jsonObject = new JSONObject(response);

                    String message = jsonObject.getString("message");
                    String status = jsonObject.getString("status");

                    if (status.equals("1")) {

                        JSONObject data = jsonObject.getJSONObject("data");
                        Log.d("data",""+data.toString());
                        String business_name = data.getString("business_name");
                        String mobile = data.getString("mobile");
                        String email = data.getString("email");
                        String about_us = data.getString("about_us");
                        String contact_person_name = data.getString("contact_person_name");
                        String gst_no = data.getString("gst_no");
                        String address = data.getString("address");
                        String user_image = data.getString("user_image");

                        mNameToSend = business_name;
                        mEmailToSend = email;
                        mMobileToSend = mobile;
                        mSendImage = user_image;
                        mAddress = address;

                        etNameProfile.setText(business_name);
                        etGSTINProfile.setText(gst_no);
                        etMobileNumberProfile.setText(mobile);
                        etEmailProfile.setText(email);
                        etAboutUsProfile.setText(about_us);
                        et_address.setText(mAddress);

                        etContactPersonNameProfile.setText(contact_person_name);

                        Glide.with(getApplicationContext()).load(user_image).apply(new RequestOptions().placeholder(R.drawable.user_icon).error(
                                R.drawable.user_icon)).diskCacheStrategy(DiskCacheStrategy.ALL).into(ivUserProfile);


                        MainActivity mainActivity = MainActivity.getInstance();
                        mainActivity.getProfile();

                    } else {
                        Toast.makeText(ActivityProfile.this, "" + message, Toast.LENGTH_SHORT).show();
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

        RequestQueue requestQueue = SingletonRequestQueue.getInstance(ActivityProfile.this).getRequestQueue();
        requestQueue.add(stringRequest);

    }
}
