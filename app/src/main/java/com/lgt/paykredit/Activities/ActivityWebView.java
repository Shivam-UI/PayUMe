package com.lgt.paykredit.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.app.DownloadManager;
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

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.Volley;
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
import com.lgt.paykredit.extras.InputStreamReader;
import com.lgt.paykredit.extras.PayKreditAPI;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Random;

import static com.lgt.paykredit.extras.PayKreditAPI.DOWNLOAD_NUMBER;
import static com.lgt.paykredit.extras.PayKreditAPI.INVOICE_NUMBER;

public class ActivityWebView extends AppCompatActivity {
    private FloatingActionButton fab_downloadStatement;
    private WebView webViewPayKredit;
    private ProgressBar pbWebView;
    private ImageView ivBackSingleUserTransaction,iv_shareInvoice;

    private TextView tvToolbarTitle;
    private String mURL = "",urlToOpen="";
    private String mTypeOfURL = "";
    private String mKeyInvoiceUrl = "";
    // download invoice
    String apiKey = "17486551-d4c3-4151-aa84-f94b9c1e76c8";
    String apiURL = "http://api.html2pdfrocket.com/pdf";
    HashMap<String, String> params = new HashMap<String, String>();
    String fileName;
    File root;
    File gpxfile = null;

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

                // mURL="http://paykredit.in/api/invoice_final.php?number="+mKeyInvoiceUrl;
                mURL=mURL+mKeyInvoiceUrl;
                Log.e("hjkhkjhkjhkj",  mURL);
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
                startDownLoadInvoice(DOWNLOAD_NUMBER+mKeyInvoiceUrl,mKeyInvoiceUrl);
            }
        });
    }

    private void startDownLoadInvoice(String KEY_URL,String KEY_INVOICE_NUMBER) {
        pbWebView.setVisibility(View.VISIBLE);
        String value = INVOICE_NUMBER + KEY_INVOICE_NUMBER;
        params.put("apiKey", apiKey);
        params.put("value", value);
        Log.d("send_param", "" + params);
        InputStreamReader inputStreamReader = new InputStreamReader(Request.Method.POST, apiURL, new Response.Listener<byte[]>() {
            @Override
            public void onResponse(byte[] response) {
                try {
                    if (response != null) {
                        Random r = new Random();
                        int i1 = r.nextInt(80 - 65) + 65;
                        fileName = "PayKreditInvoice" + KEY_INVOICE_NUMBER + ".pdf";
                        root = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "PayKredit" + "/" + "download/Invoice/", "WebPageToPdf");
                        if (!root.exists()) {
                            root.mkdirs();
                        }
                        if (root.exists()) {

                            if (gpxfile == null || !gpxfile.exists()) {
                                gpxfile = new File(root, fileName);
                                OutputStream op = new FileOutputStream(gpxfile);
                                gpxfile.setWritable(true);
                                op.write(response);
                                op.flush();
                                op.close();
                            } else {

                                if (gpxfile.exists()) {
                                    OutputStream op = new FileOutputStream(gpxfile, true);
                                    op.write(response);
                                    op.flush();
                                    op.close();
                                }
                            }
                        }
                        pbWebView.setVisibility(View.GONE);
                        shareDownloadInvoice(fileName);
                        System.out.print("Response ----------------------" + response.toString());
                    }
                } catch (Exception e) {
                    pbWebView.setVisibility(View.GONE);
                    Log.d("KEY_ERROR", "UNABLE TO DOWNLOAD FILE");
                    e.printStackTrace();
                    Toast.makeText(getBaseContext(), ""+e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                pbWebView.setVisibility(View.GONE);
                error.printStackTrace();
                Toast.makeText(getBaseContext(), ""+error.getMessage(), Toast.LENGTH_LONG).show();
            }
        }, params);
        RequestQueue mRequestQueue = Volley.newRequestQueue(getApplicationContext(), new HurlStack());
        mRequestQueue.add(inputStreamReader);
    }

    private void shareDownloadInvoice(String id_invoice) {
        File file = new File(Environment.getExternalStorageDirectory() + "/PayKredit/download/Invoice/WebPageToPdf/" + id_invoice);
        if (file.exists()) {
            Uri Pdf_URI = Build.VERSION.SDK_INT >= Build.VERSION_CODES.N
                    ? FileProvider.getUriForFile(ActivityWebView.this, getPackageName() + ".provider", file)
                    : Uri.fromFile(file);
            Log.e("file_directory_exists", file + "   exists   " + Pdf_URI.toString());
            Intent intentShareFile = new Intent(Intent.ACTION_SEND);
            String share_content = "PayKredit Invoice :" + id_invoice;
            intentShareFile.putExtra(Intent.EXTRA_TEXT, share_content);
            intentShareFile.setType("application/pdf");
            intentShareFile.putExtra(Intent.EXTRA_STREAM, Pdf_URI);
            startActivity(Intent.createChooser(intentShareFile, "Share File"));
            finish();
        } else {
            Log.e("file_directory_exists", file + " not  exists   " + id_invoice);
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
