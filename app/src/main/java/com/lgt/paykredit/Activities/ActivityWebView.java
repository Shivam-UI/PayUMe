package com.lgt.paykredit.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.webkit.DownloadListener;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.lgt.paykredit.R;
import com.lgt.paykredit.extras.FileDownloader;

import java.io.File;
import java.io.IOException;

public class ActivityWebView extends AppCompatActivity {
    private FloatingActionButton fab_downloadStatement;
    private WebView webViewPayKredit;
    private ProgressBar pbWebView;
    private ImageView ivBackSingleUserTransaction;

    private TextView tvToolbarTitle;
    private String mURL = "";
    private String mTypeOfURL = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);

        tvToolbarTitle = findViewById(R.id.tvToolbarTitle);
        fab_downloadStatement = findViewById(R.id.fab_downloadStatement);
        webViewPayKredit = findViewById(R.id.webViewPayKredit);
        ivBackSingleUserTransaction = findViewById(R.id.ivBackSingleUserTransaction);

        pbWebView = findViewById(R.id.pbWebView);
        Log.d("Account","");


        Intent getURL = getIntent();
        if (getURL != null) {
            if (getURL.hasExtra("KEY_WEB_URL")) {
                mURL = getURL.getStringExtra("KEY_WEB_URL");
                mTypeOfURL = getURL.getStringExtra("KEY_URL_TYPE");

                Log.e("hjkhkjhkjhkj", mURL + "");
                tvToolbarTitle.setText(mTypeOfURL);

                if (mURL != null) {
                    if (!mURL.equalsIgnoreCase("")) {
                        webViewPayKredit.loadUrl(mURL);
                        webViewPayKredit.getSettings().setBuiltInZoomControls(true);
                    } else {
                        Toast.makeText(ActivityWebView.this, "URL is not valid", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }

            }
        }

        WebSettings webSettings = webViewPayKredit.getSettings();

        webViewPayKredit.getSettings().setJavaScriptEnabled(true);
     //   webViewPayKredit.getSettings().setLoadWithOverviewMode(true);
        webViewPayKredit.getSettings().setUseWideViewPort(true);
     //   webViewPayKredit.getSettings().setLoadWithOverviewMode(true);
        webViewPayKredit.setInitialScale(1);


        webSettings.setDomStorageEnabled(true);

        ivBackSingleUserTransaction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        webViewPayKredit.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                pbWebView.setVisibility(View.VISIBLE);
                super.onPageStarted(view, url, favicon);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                pbWebView.setVisibility(View.GONE);
                super.onPageFinished(view, url);
            }
        });

        fab_downloadStatement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(ActivityWebView.this, "Downloading...", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (webViewPayKredit.canGoBack()) {
            webViewPayKredit.goBack();
        } else {
            super.onBackPressed();
        }
    }

    private class DownloadFile extends AsyncTask<String, Void, Void> {

        @Override
        protected Void doInBackground(String... strings) {
            String fileUrl = strings[0];   // -> http://maven.apache.org/maven-1.x/maven.pdf
            String fileName = strings[1];  // -> maven.pdf
            String extStorageDirectory = Environment.getExternalStorageDirectory().toString();
            File folder = new File(extStorageDirectory, "testthreepdf");
            folder.mkdir();

            File pdfFile = new File(folder, fileName);

            try {
                pdfFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
            FileDownloader.downloadFile(fileUrl, pdfFile);
            return null;
        }
    }
}
