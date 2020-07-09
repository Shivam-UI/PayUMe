package com.lgt.paykredit.bottomsheets;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.FileProvider;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.lgt.paykredit.R;
import com.lgt.paykredit.extras.Common;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.content.ContentValues.TAG;

public class BottomSheetBusinessCardDrawable extends BottomSheetDialogFragment {

    private String mUserName, mName, mEmail, mMobile, mImage,mAddress;
    private FrameLayout my_container;
    private TextView tvNameBusiness, tvNUmberBusiness, tvEmailBusiness,tvShareCard,tv_address,tv_gst_no,tv_user_name;
    private CircleImageView ivVisitingCard;

    ProgressDialog dialog;
    public BottomSheetBusinessCardDrawable() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.bottom_sheet_business_card_drawable, container, false);

        tv_user_name = view.findViewById(R.id.tv_user_name);
        tvNameBusiness = view.findViewById(R.id.tvNameBusiness);
        tvNUmberBusiness = view.findViewById(R.id.tvNUmberBusiness);
        tvEmailBusiness = view.findViewById(R.id.tvEmailBusiness);
        ivVisitingCard = view.findViewById(R.id.ivVisitingCard);
        tvShareCard = view.findViewById(R.id.tvShareCard);
        my_container = view.findViewById(R.id.my_container);
        tv_address = view.findViewById(R.id.tv_address);
        tv_gst_no = view.findViewById(R.id.tv_gst_no);
        dialog=new ProgressDialog(getActivity());
        Bundle bundle = getArguments();
        if (bundle != null) {
            if (bundle.containsKey("KEY_BUSINESS_NAME")) {
                mUserName = bundle.getString("KEY_USER_NAME");
                mName = bundle.getString("KEY_BUSINESS_NAME");
                mEmail = bundle.getString("KEY_BUSINESS_EMAIL");
                mMobile = bundle.getString("KEY_BUSINESS_MOBILE");
                mImage = bundle.getString("KEY_BUSINESS_IMAGE");
                mAddress = bundle.getString(Common.address_key);
                tv_user_name.setText(mUserName);
                tvNameBusiness.setText(mName);
                tvNUmberBusiness.setText(mMobile);
                tvEmailBusiness.setText(mEmail);
                tv_address.setText(mAddress);

                    if (getActivity() != null) {
                        Glide.with(getActivity()).load(mImage).apply(new RequestOptions().placeholder(R.drawable.user_icon).error(
                                R.drawable.user_icon)).diskCacheStrategy(DiskCacheStrategy.ALL).into(ivVisitingCard);
                    }

                tvShareCard.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        tvShareCard.setVisibility(View.GONE);
                        dialog.setMessage("please wait");
                        dialog.show();

                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                shareMyProfile();
                            }
                        },250);



                    }
                });
            }

        }


        return view;
    }

    private void shareMyProfile() {
        if (getActivity()!=null){

            saveImageExternal(takeScreenShot(my_container));

        }

    }
    private void saveImageExternal(Bitmap image) {
        try {

            File cachePath = new File(getActivity().getCacheDir(), "images");
            cachePath.mkdirs(); // don't forget to make the directory
            FileOutputStream stream = new FileOutputStream(cachePath + "/image.png"); // overwrites this image every time
            image.compress(Bitmap.CompressFormat.PNG, 100, stream);
            stream.close();

            shareImage();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void shareImage() {
        File imagePath = new File(getActivity().getCacheDir(), "images");
        File newFile = new File(imagePath, "image.png");
        Uri contentUri = FileProvider.getUriForFile(getActivity(), "com.lgt.paykredit.provider", newFile);

        if (contentUri != null) {
            if (dialog!=null){
                if (dialog.isShowing()){
                    dialog.dismiss();
                }
            }
            Intent shareIntent = new Intent();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                shareIntent.setAction(Intent.ACTION_SEND);
                shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION); // temp permission for receiving app to read this file
                shareIntent.setDataAndType(contentUri, getActivity().getContentResolver().getType(contentUri));
                shareIntent.putExtra(Intent.EXTRA_STREAM, contentUri);
            } else {

                shareIntent.setAction(Intent.ACTION_SEND);
                shareIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                shareIntent.setDataAndType(contentUri, getActivity().getContentResolver().getType(contentUri));
                shareIntent.putExtra(Intent.EXTRA_STREAM, contentUri);
            }

            startActivity(Intent.createChooser(shareIntent, "Choose an app"));
        }
    }

    public Bitmap takeScreenShot(View view) {
        // configuramos para que la view almacene la cache en una imagen
        view.setDrawingCacheEnabled(true);
        view.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        view.buildDrawingCache();

        if(view.getDrawingCache() == null) return null; // Verificamos antes de que no sea null

        // utilizamos esa cache, para crear el bitmap que tendra la imagen de la view actual
        Bitmap snapshot = Bitmap.createBitmap(view.getDrawingCache());
        view.setDrawingCacheEnabled(false);
        view.destroyDrawingCache();

        return snapshot;
    }
}
