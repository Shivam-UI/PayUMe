package com.lgt.paykredit.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
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
import com.downloader.Error;
import com.downloader.OnCancelListener;
import com.downloader.OnDownloadListener;
import com.downloader.OnPauseListener;
import com.downloader.OnProgressListener;
import com.downloader.OnStartOrResumeListener;
import com.downloader.PRDownloader;
import com.downloader.Progress;
import com.lgt.paykredit.Adapter.AdapterAllInvoices;
import com.lgt.paykredit.Adapter.SingleInvoiceAdapter;
import com.lgt.paykredit.Models.ModelInvoiceDetails;
import com.lgt.paykredit.R;
import com.lgt.paykredit.bottomsheets.BottomSheetCall;
import com.lgt.paykredit.bottomsheets.BottomSheetDeleteInvoice;
import com.lgt.paykredit.bottomsheets.BottomSheetPayment;
import com.lgt.paykredit.extras.Common;
import com.lgt.paykredit.extras.InvoiceDetailsClick;
import com.lgt.paykredit.extras.PayKreditAPI;
import com.lgt.paykredit.extras.SharedData;
import com.lgt.paykredit.extras.SingletonRequestQueue;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import static com.lgt.paykredit.Adapter.AdapterAllInvoices.InvoiceIDShared;
import static com.lgt.paykredit.Adapter.SingleInvoiceAdapter.SelectedDate;
import static com.lgt.paykredit.extras.Common.INVOICE_ID;
import static com.lgt.paykredit.extras.PayKreditAPI.DEFAULT_ADD_REMOVED_CUSTOMER;
import static com.lgt.paykredit.extras.PayKreditAPI.DEFAULT_LIST_INVOICE_API;
import static com.lgt.paykredit.extras.PayKreditAPI.DOWNLOAD_NUMBER;
import static com.lgt.paykredit.extras.PayKreditAPI.EDIT_INVOICE_BY_TYPE_API;
import static com.lgt.paykredit.extras.PayKreditAPI.INVOICE_PRODUCT_LIST_API;
import static com.lgt.paykredit.extras.PayKreditAPI.PREVIEW_INVOICE_API;
import static com.lgt.paykredit.extras.PayKreditAPI.USER_WISE_INVOICE_API;

public class ActivityInvoiceDescription extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, InvoiceDetailsClick {

    private TextView tvToolbarTitle, tv_UserNameInvoice, tv_BalanceAmtDue, tv_InvoiceIdDue, iv_InvoiceDueDate;
    private LinearLayout llCallInvoiceDescription;
    private ImageView ivBackSingleUserTransaction, iv_defaultIcon,iv_shareInvoice;
    private LinearLayout llDateInvoice, ll_SetToDefault, ll_PaymentStatusUpdate, ll_ChangeDueDate;
    private SharedPreferences sharedPreferences;
    private String mUserID, invoice_date_picker = "", invoiceID = "",type,urlToOpen,urlType;
    private RecyclerView rv_user_single_invoice_details;
    SingleInvoiceAdapter singleInvoiceAdapter;
    ArrayList<ModelInvoiceDetails> list=new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invoice_description);
        sharedPreferences = ActivityInvoiceDescription.this.getSharedPreferences("USER_DATA", MODE_PRIVATE);
        if (sharedPreferences.contains("KEY_USER_ID")) {
            mUserID = sharedPreferences.getString("KEY_USER_ID", "");
        }
        urlToOpen = PayKreditAPI.INVOICE_NUMBER;
        urlType = "PREVIEW";
        tvToolbarTitle = findViewById(R.id.tvToolbarTitle);
        iv_shareInvoice = findViewById(R.id.iv_shareInvoice);
        iv_defaultIcon = findViewById(R.id.iv_defaultIcon);
        rv_user_single_invoice_details = findViewById(R.id.rv_user_single_invoice_details);
        llCallInvoiceDescription = findViewById(R.id.llCallInvoiceDescription);
        ivBackSingleUserTransaction = findViewById(R.id.ivBackSingleUserTransaction);
        llDateInvoice = findViewById(R.id.llDateInvoice);
        tv_UserNameInvoice = findViewById(R.id.tv_UserNameInvoice);
        tv_BalanceAmtDue = findViewById(R.id.tv_BalanceAmtDue);
        tv_InvoiceIdDue = findViewById(R.id.tv_InvoiceIdDue);
        iv_InvoiceDueDate = findViewById(R.id.iv_InvoiceDueDate);
        ll_SetToDefault = findViewById(R.id.ll_SetToDefault);
        ll_PaymentStatusUpdate = findViewById(R.id.ll_PaymentStatusUpdate);
        ll_ChangeDueDate = findViewById(R.id.ll_ChangeDueDate);
        iv_shareInvoice.setVisibility(View.GONE);
        ivBackSingleUserTransaction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        DisplayInvoiceList();
        invoiceID = getIntent().getStringExtra("testID");
        type = getIntent().getStringExtra("type");
        Log.d("invoiceDetails", invoiceID);
        if (!invoiceID.equalsIgnoreCase("")) {
            invoiceDetails();
        } else {
            Log.d("invoiceDetails", "Something wrong");
        }

        tvToolbarTitle.setText("Invoice Detail");
        // not in use
        /*llCallInvoiceDescription.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BottomSheetCall bottomSheetCall = new BottomSheetCall();
                bottomSheetCall.show(getSupportFragmentManager(), "BottomSheetCall");
            }
        });

        ll_SetToDefault.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                iv_defaultIcon.setImageTintList(ColorStateList.valueOf(Color.parseColor("#80000000")));
                Toast.makeText(ActivityInvoiceDescription.this, "Added To Default List", Toast.LENGTH_SHORT).show();

            }
        });

        ll_PaymentStatusUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        ll_ChangeDueDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });*/
        if (type.equalsIgnoreCase("")){

        }else if(type.equalsIgnoreCase("")){

        }
    }

    private void invoiceDetails()    {
        if (!invoiceID.equalsIgnoreCase("")) {
            StringRequest invoiceDetails = new StringRequest(Request.Method.POST, USER_WISE_INVOICE_API, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.d("invoiceDetails", response);
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        String message = jsonObject.getString("message");
                        String status = jsonObject.getString("status");
                        String user_name = jsonObject.getString("customer_name");
                        String total_due = jsonObject.getString("total_due");
                        tv_UserNameInvoice.setText(user_name);
                        tv_BalanceAmtDue.setText(total_due);
                        if (status.equalsIgnoreCase("1")) {
                            JSONArray jsonArray = jsonObject.getJSONArray("data");
                            for (int i=0;i<jsonArray.length();i++){
                                JSONObject invoiceData=jsonArray.getJSONObject(i);
                                String tbl_invoice_id=invoiceData.getString("tbl_invoice_id");
                                String invoice_no=invoiceData.getString("invoice_no");
                                String invoice_date=invoiceData.getString("invoice_date");
                                String due_date=invoiceData.getString("due_date");
                                String total_balance=invoiceData.getString("total_balance");
                                String sub_total=invoiceData.getString("sub_total");
                                String total_advance=invoiceData.getString("total_advance");
                                String customer_name=invoiceData.getString("customer_name");
                                String customer_mobile=invoiceData.getString("customer_mobile");
                                String customer_email=invoiceData.getString("customer_email");
                                String paid=invoiceData.getString("paid");
                                ModelInvoiceDetails modelInvoiceDetails =new ModelInvoiceDetails(tbl_invoice_id,invoice_no,invoice_date,due_date,"r",sub_total,total_advance,total_balance,paid,customer_name,customer_mobile,customer_email);
                                modelInvoiceDetails.setType(type);
                                modelInvoiceDetails.setInvoice_customer_id(invoiceID);
                                list.add(modelInvoiceDetails);
                            }
                            singleInvoiceAdapter.notifyDataSetChanged();
                        } else {
                            Toast.makeText(ActivityInvoiceDescription.this, message, Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.d("invoiceDetails", error.getMessage());
                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> param = new HashMap<>();
                    param.put("user_id", mUserID);
                    param.put("invoice_customer_id", invoiceID);
                    Log.d("invoiceDetails", param.toString());
                    return param;
                }
            };
            RequestQueue requestQueue = SingletonRequestQueue.getInstance(ActivityInvoiceDescription.this).getRequestQueue();
            requestQueue.add(invoiceDetails);
        } else {
            Toast.makeText(this, "No Invoice Id Found!", Toast.LENGTH_SHORT).show();
        }
    }

    private void DisplayInvoiceList() {
        singleInvoiceAdapter = new SingleInvoiceAdapter(this,list,ActivityInvoiceDescription.this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this,RecyclerView.VERTICAL,false);
        rv_user_single_invoice_details.setLayoutManager(linearLayoutManager);
        rv_user_single_invoice_details.setHasFixedSize(true);
        rv_user_single_invoice_details.setAdapter(singleInvoiceAdapter);
    }

    public void editInvoice(String typeAPI,String InId) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, EDIT_INVOICE_BY_TYPE_API, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("editInvoice", response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String message = jsonObject.getString("message");
                    Toast.makeText(ActivityInvoiceDescription.this, message, Toast.LENGTH_SHORT).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("editInvoice", error.getMessage());
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> param = new HashMap<>();
                param.put("user_id", mUserID);
                param.put("tbl_invoice_id", InId);
                param.put("type", typeAPI);
                if (typeAPI.equalsIgnoreCase("due_date")) {
                    invoice_date_picker=SelectedDate;
                    if (!invoice_date_picker.equalsIgnoreCase("")) {
                        param.put("due_date", invoice_date_picker);
                    } else {
                        Toast.makeText(ActivityInvoiceDescription.this, "Please Select Invoice Date", Toast.LENGTH_SHORT).show();
                    }
                } else if (typeAPI.equalsIgnoreCase("paid")) {
                    param.put("paid", "0");
                } else if (typeAPI.equalsIgnoreCase("default_invoice")) {
                    param.put("default_invoice", "1");
                }
                Log.d("editInvoice", param.toString());
                return param;
            }
        };
        RequestQueue requestQueue = SingletonRequestQueue.getInstance(ActivityInvoiceDescription.this).getRequestQueue();
        requestQueue.add(stringRequest);
    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {

    }

    @Override
    public void showPreview(String uid) {
        // Toast.makeText(this, uid, Toast.LENGTH_SHORT).show();
        /*Intent intentPreview = new Intent(this,ActivityPreviewInvoice.class);
        intentPreview.putExtra("tbl_invoice_id",uid);
        startActivity(intentPreview);*/

        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(DOWNLOAD_NUMBER+uid));
        Log.d("download_path",""+DOWNLOAD_NUMBER+uid);
        startActivity(i);
        /*Intent webViewIntent = new Intent(this, ActivityWebView.class);
        webViewIntent.putExtra("KEY_WEB_URL", urlToOpen);
        webViewIntent.putExtra("KEY_URL_TYPE", urlType);
        webViewIntent.putExtra("KEY_INVOICE_NUMBER", uid);
        startActivity(webViewIntent);*/
    }

    @Override
    public void changeDate(String uid) {
        editInvoice("due_date",uid);
        Log.d("due_date",""+uid);
    }

    @Override
    public void payPayment(String uid,String invoiceNo) {
        BottomSheetPayment bottomSheetPayment = new BottomSheetPayment();
        Bundle deleteItems = new Bundle();
        deleteItems.putString("KEY_DELETE_ID", uid);
        deleteItems.putString("KEY_DELETE_ITEM", invoiceNo);
        bottomSheetPayment.setArguments(deleteItems);
        bottomSheetPayment.show(getSupportFragmentManager(), "BottomSheetPayment");
    }

    @Override
    public void setDetauld(String uid) {
        addToDefaulterInvoice(uid);
    }

    @Override
    public void startShareData(String InvoiceID) {
        // startDownLoadInvoice(urlToOpen+InvoiceID,InvoiceID);
    }



    private void startDownLoadInvoice(String KEY_URL,String KEY_INVOICE_NUMBER) {
        Toast.makeText(this, "Downloading....", Toast.LENGTH_SHORT).show();
        //String KEY_URL_DUMMY = "http://paykredit.in/api/invoice_final.php?number=INSTA118709";
        // String KEY_URL_DUMMY = "http://www.africau.edu/images/default/sample.pdf";  +".pdf"
        String fileName = KEY_INVOICE_NUMBER+".pdf";
        // String fileName = "sample.pdf";
        String video_Path = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "PayKredit" + "/" + "download/Invoice/";
        String dirPath = android.os.Environment.getExternalStorageDirectory().getAbsolutePath()+File.separator+"PayKredit/downloads";
        Log.d("dirPath",dirPath+"     |     "+video_Path);
        // Toast.makeText(ActivityInvoiceDescription.this, "KEY_URL"+KEY_URL, Toast.LENGTH_SHORT).show(); DOWNLOAD_NUMBER
        int downloadId = PRDownloader.download(DOWNLOAD_NUMBER, video_Path, fileName)
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
                        Toast.makeText(ActivityInvoiceDescription.this, "Download Error", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void shareDownloadInvoice(String id_invoice) {
        File file = new File(Environment.getExternalStorageDirectory() + "/" + "PayKredit" + "/" + "download/Invoice/"+id_invoice);
        if (file.exists()) {
            Uri Pdf_URI = Build.VERSION.SDK_INT >= Build.VERSION_CODES.N
                    ? FileProvider.getUriForFile(ActivityInvoiceDescription.this, getPackageName() + ".provider", file)
                    : Uri.fromFile(file);
            Log.e("file_directory_exists", "exists" + Pdf_URI.toString());
            Intent intentShareFile = new Intent(Intent.ACTION_SEND);
            String share_content = "PayKredit Invoice :" +id_invoice;
            intentShareFile.putExtra(Intent.EXTRA_TEXT, share_content);
            intentShareFile.setType("application/pdf");
            intentShareFile.putExtra(Intent.EXTRA_STREAM, Pdf_URI);
            startActivity(Intent.createChooser(intentShareFile, "Share File"));
            finish();
        }
    }

    private void addToDefaulterInvoice(String uid) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, DEFAULT_ADD_REMOVED_CUSTOMER, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("DefaulterInvoice", response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String message = jsonObject.getString("message");
                    Toast.makeText(ActivityInvoiceDescription.this, message, Toast.LENGTH_SHORT).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("DefaulterInvoice", error.getMessage());
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> param = new HashMap<>();
                param.put("user_id", mUserID);
                param.put("tbl_invoice_customer_id", uid);
                Log.d("DefaulterInvoice",param.toString());
                return param;
            }
        };
        RequestQueue requestQueue = SingletonRequestQueue.getInstance(ActivityInvoiceDescription.this).getRequestQueue();
        requestQueue.add(stringRequest);
    }
}
