package com.lgt.paykredit.Activities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.DexterError;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.PermissionRequestErrorListener;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.lgt.paykredit.R;

import java.util.List;

public class ActivitySplash extends AppCompatActivity {

    private SharedPreferences sharedPreferences;

    private String mUserID;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        sharedPreferences = ActivitySplash.this.getSharedPreferences("USER_DATA", MODE_PRIVATE);
        if (sharedPreferences.contains("KEY_USER_ID")) {
            mUserID = sharedPreferences.getString("KEY_USER_ID", "");
        }

        Log.e("userererer", mUserID + "");

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

               // goToApp();

                checkPermissionForApp();

            }
        }, 2000);
    }

    private void checkPermissionForApp() {
        
            try {
                Dexter.withActivity(ActivitySplash.this).withPermissions(Manifest.permission.CAMERA,Manifest.permission.CALL_PHONE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_CONTACTS)
                        .withListener(new MultiplePermissionsListener() {
                            @Override
                            public void onPermissionsChecked(MultiplePermissionsReport report) {

                                if (report.areAllPermissionsGranted()) {

                                    //Check intrnet connection
                                    //if connected , check for sharedpreferences
                                    //if sharedpref == null send to register else Dashboard

                                    //Connection found, good to go

                                    if (sharedPreferences.contains("KEY_USER_ID")) {
                                        mUserID = sharedPreferences.getString("KEY_USER_ID", "");
                                        Log.e("Dsadsadsadsa", mUserID + "");

                                        if (mUserID != null) {
                                            if(!mUserID.equalsIgnoreCase("")){
                                                startActivity(new Intent(ActivitySplash.this, MainActivity.class));
                                                finishAffinity();
                                            }
                                        }
                                    } else {
                                        startActivity(new Intent(ActivitySplash.this, ActivitySelectLanguage.class));
                                        finishAffinity();
                                    }

                                    //No connection, close app

                               /* else {
                                    new Handler().postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            finish();
                                        }
                                    }, 3000);
                                    common.showSnackBar(tvSplash, "You are not connected to internet");
                                }*/

                                } else if (!report.areAllPermissionsGranted()) {
                                    Toast.makeText(ActivitySplash.this, "All permission are required to use this app", Toast.LENGTH_LONG).show();
                                    finish();
                                } else {
                                    if (report.isAnyPermissionPermanentlyDenied()) {
                                        //Some permission are permanently denied, send user to app setting to allow them manually
                                        showSettingDialog();
                                    }
                                }

                            }

                            @Override
                            public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                                token.continuePermissionRequest();
                            }
                        })

                        .withErrorListener(new PermissionRequestErrorListener() {
                            @Override
                            public void onError(DexterError error) {
                                Toast.makeText(ActivitySplash.this, "Permission Error occurred...", Toast.LENGTH_SHORT).show();
                            }
                        }).onSameThread().check();

            } catch (Exception e) {
                e.printStackTrace();
            }

    }

    private void showSettingDialog() {
        {
            AlertDialog.Builder builder = new AlertDialog.Builder(ActivitySplash.this);
            builder.setTitle("Need Permission");
            builder.setMessage("This app needs permissions to use it's features");
            builder.setPositiveButton("Goto Settings", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.cancel();
                    openSetting();

                }
            });

            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.cancel();
                }
            });
        }
    }

    private void openSetting() {
        {
            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
            Uri uri = Uri.fromParts("package", getPackageName(), null);
            intent.setData(uri);
            startActivity(intent);
        }
    }

    private void goToApp() {
        if (mUserID != null) {
            if (!mUserID.equalsIgnoreCase("")) {
                startActivity(new Intent(ActivitySplash.this, MainActivity.class));
                finishAffinity();
            }

        } else {

            startActivity(new Intent(ActivitySplash.this, ActivitySelectLanguage.class));
            finishAffinity();
        }
    }
}
