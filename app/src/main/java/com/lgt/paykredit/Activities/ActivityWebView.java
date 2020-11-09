package com.lgt.paykredit.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
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

import com.downloader.Error;
import com.downloader.OnCancelListener;
import com.downloader.OnDownloadListener;
import com.downloader.OnPauseListener;
import com.downloader.OnProgressListener;
import com.downloader.OnStartOrResumeListener;
import com.downloader.PRDownloader;
import com.downloader.Progress;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.lgt.paykredit.R;
import com.lgt.paykredit.extras.FileDownloader;
import com.lgt.paykredit.extras.PayKreditAPI;

import java.io.File;
import java.io.IOException;
import java.net.URLConnection;

public class ActivityWebView extends AppCompatActivity {
    private FloatingActionButton fab_downloadStatement;
    private WebView webViewPayKredit;
    private ProgressBar pbWebView;
    private ImageView ivBackSingleUserTransaction,iv_shareInvoice;

    private TextView tvToolbarTitle;
    private String mURL = "",urlToOpen="";
    private String mTypeOfURL = "";
    private String mKeyInvoiceUrl = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);
        urlToOpen = PayKreditAPI.INVOICE_NUMBER;
        tvToolbarTitle = findViewById(R.id.tvToolbarTitle);
        iv_shareInvoice = findViewById(R.id.iv_shareInvoice);
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
                mKeyInvoiceUrl = getURL.getStringExtra("KEY_INVOICE_NUMBER");

                mURL="http://paykredit.in/api/invoice_final.php?number="+mKeyInvoiceUrl;
                Log.e("hjkhkjhkjhkj",  "http://paykredit.in/api/invoice_final.php?number="+mKeyInvoiceUrl);
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

        iv_shareInvoice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startDownLoadInvoice(urlToOpen+mKeyInvoiceUrl,mKeyInvoiceUrl);
            }
        });
    }

    private void startDownLoadInvoice(String KEY_URL,String KEY_INVOICE_NUMBER) {
        //String KEY_URL_DUMMY = "http://paykredit.in/api/invoice_final.php?number=INSTA118709";
        String fileName = KEY_INVOICE_NUMBER+".pdf";
        //String fileName = "INSTA118709"+".pdf";
        String video_Path = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + getString(R.string.app_name) + "/" + "downloadInvoice";
        Log.d("dirPath",""+video_Path);
        // Toast.makeText(ActivityWebView.this, "KEY_URL"+KEY_URL, Toast.LENGTH_SHORT).show();
        int downloadId = PRDownloader.download(KEY_URL, video_Path, fileName)
                .build()
                .setOnStartOrResumeListener(new OnStartOrResumeListener() {
                    @Override
                    public void onStartOrResume() {
                        Log.d("dirPath","StartOrResume download started:");
                    }
                })
                .setOnPauseListener(new OnPauseListener() {
                    @Override
                    public void onPause() {
                        Log.d("dirPath","onPause download started:");
                    }
                })
                .setOnCancelListener(new OnCancelListener() {
                    @Override
                    public void onCancel() {
                        Log.d("dirPath","onCancel download started:");
                    }
                })
                .setOnProgressListener(new OnProgressListener() {
                    @Override
                    public void onProgress(Progress progress) {
                        Log.d("dirPath","progress : ="+progress);
                    }
                })
                .start(new OnDownloadListener() {
                    @Override
                    public void onDownloadComplete() {
                        shareDownloadInvoice(fileName);
                    }

                    @Override
                    public void onError(Error error) {
                        Toast.makeText(ActivityWebView.this, "Download Error", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void shareDownloadInvoice(String id_invoice) {
        File file = new File(Environment.getExternalStorageDirectory() + "/" + getString(R.string.app_name) + "/" + "downloadInvoice/"+id_invoice);
        if (file.exists()) {
            Log.e("file_directory_exists", "exists");
            Uri videoURI = Build.VERSION.SDK_INT >= Build.VERSION_CODES.N
                    ? FileProvider.getUriForFile(ActivityWebView.this, getPackageName() + ".provider", file)
                    : Uri.fromFile(file);
            Intent intentShareFile = new Intent(Intent.ACTION_SEND);
            String share_content = "PayKredit Invoice :" +id_invoice;
            Log.d("shareee", share_content + "");
            intentShareFile.putExtra(Intent.EXTRA_TEXT, share_content);
            intentShareFile.setType(URLConnection.guessContentTypeFromName(file.getName()));
            intentShareFile.putExtra(Intent.EXTRA_STREAM, videoURI);
            startActivity(Intent.createChooser(intentShareFile, "Share File"));
            finish();
        }
    }

    @Override
    public void onBackPressed() {
        if (webViewPayKredit.canGoBack()) {
            webViewPayKredit.goBack();
        }else if(mTypeOfURL.equalsIgnoreCase("PREVIEW")){
            super.onBackPressed();
        }else if(mTypeOfURL.equalsIgnoreCase("INVOICE")){
            startActivity(new Intent(getApplicationContext(),InvoiceMainPage.class));
            finish();
        }  else {
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
