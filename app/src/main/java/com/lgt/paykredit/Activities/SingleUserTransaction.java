package com.lgt.paykredit.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.lgt.paykredit.Adapter.AdapterSingleUserTransaction;
import com.lgt.paykredit.Adapter.AdapterVertical;
import com.lgt.paykredit.Models.ModelSingleUserTransaction;
import com.lgt.paykredit.Models.ModelVertical;
import com.lgt.paykredit.R;
import com.lgt.paykredit.bottomsheets.BottomSheetCall;
import com.lgt.paykredit.bottomsheets.BottomSheetSendReminder;
import com.lgt.paykredit.bottomsheets.BottomSheetShareStatement;
import com.lgt.paykredit.extras.Common;
import com.lgt.paykredit.extras.Language;
import com.lgt.paykredit.extras.PayKreditAPI;
import com.lgt.paykredit.extras.SingletonRequestQueue;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SingleUserTransaction extends AppCompatActivity {

    private static final String TAG = "SingleUserTransaction";

    private RecyclerView rvSingleUserTransaction;

    private List<ModelVertical> listVertical;
    private AdapterVertical adapterVertical;

    private List<ModelSingleUserTransaction> listSingleUserTransaction;
    LinearLayoutManager linearLayoutManager;
    private AdapterSingleUserTransaction adapterSingleUserTransaction;

    private ImageView ivCall, ivReminder;
    private TextView tvUserNameSingleTransaction, tvCreditSingleTransaction, tvReceivedSingleTransaction, tvAdvanceOrDue, tvCreditOrAdvanced,
            tvAmountCreditOrDue, tvNoTransactionFound, tv_recieved, tv_credit, tv_credit_payment, tv_recived_payment;
    private SharedPreferences sharedPreferences;
    private LinearLayout llPayment, llCredit, llBgBalanceOrDue, llbtm, Status;
    private ProgressDialog progressDialog;
    private ProgressBar pbSingleUserTransaction;
    private String mName, mUserID, mAddCustomerID, mReceivedValue, mCreditValue, mCustomerMobile, timeStamp, mCustomerName, mBusinessName, mGSTNumber, mBusinessNumber;
    private ImageView ivBackSingleUserTransaction, ivShareSingleTransaction, sendInvoiceSingleUser;
    private boolean shouldCallOrRemind = false;
    private File pdfFile;
    private List<ModelSingleUserTransaction> listPDF= new ArrayList<>();;
    public static SingleUserTransaction singleUserTransaction;
    private Float mDueAmount;
    private String mMessageToDisplay;
    private SingleUserTransaction activity;
    private Context context;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_user_transaction);

        context = activity = this;
        singleUserTransaction = this;

        sharedPreferences = SingleUserTransaction.this.getSharedPreferences("USER_DATA", MODE_PRIVATE);
        if (sharedPreferences.contains("KEY_USER_ID")) {
            mUserID = sharedPreferences.getString("KEY_USER_ID", "");
        }
        if (sharedPreferences.contains("KEY_BUSINESS_NAME")) {
            mBusinessName = sharedPreferences.getString("KEY_BUSINESS_NAME", "");
        }
        if (sharedPreferences.contains("KEY_GST")) {
            mGSTNumber = sharedPreferences.getString("KEY_GST", "");
        }
        if (sharedPreferences.contains("KEY_MOBILE")) {
            mBusinessNumber = sharedPreferences.getString("KEY_MOBILE", "");
        }

        Toolbar toolbarSingleTransaction = findViewById(R.id.toolbarSingleTransaction);
        rvSingleUserTransaction = findViewById(R.id.rvSingleUserTransaction);
        ivBackSingleUserTransaction = findViewById(R.id.ivBackSingleUserTransaction);
        tvUserNameSingleTransaction = findViewById(R.id.tvUserNameSingleTransaction);
        ivShareSingleTransaction = findViewById(R.id.ivShareSingleTransaction);
        tvAmountCreditOrDue = findViewById(R.id.tvAmountCreditOrDue);
        tvAdvanceOrDue = findViewById(R.id.tvAdvanceOrDue);
        tvCreditOrAdvanced = findViewById(R.id.tvCreditOrAdvanced);
        tvCreditSingleTransaction = findViewById(R.id.tvCreditSingleTransaction);
        tvReceivedSingleTransaction = findViewById(R.id.tvReceivedSingleTransaction);
        Status = findViewById(R.id.Status);
        pbSingleUserTransaction = findViewById(R.id.pbSingleUserTransaction);
        llBgBalanceOrDue = findViewById(R.id.llBgBalanceOrDue);
        tvNoTransactionFound = findViewById(R.id.tvNoTransactionFound);
        llbtm = findViewById(R.id.llbtm);
        ivReminder = findViewById(R.id.ivReminder);
        ivCall = findViewById(R.id.ivCall);
        llPayment = findViewById(R.id.llPayment);
        llCredit = findViewById(R.id.llCredit);
        tv_recieved = findViewById(R.id.tv_recieved);
        tv_credit = findViewById(R.id.tv_credit);
        tv_credit_payment = findViewById(R.id.tv_credit_payment);
        tv_recived_payment = findViewById(R.id.tv_recived_payment);
        progressDialog = new ProgressDialog(SingleUserTransaction.this);
        sendInvoiceSingleUser = findViewById(R.id.sendInvoiceSingleUser);

        loadLanguage();

        setSupportActionBar(toolbarSingleTransaction);

        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());

        Intent getData = getIntent();
        if (getData != null) {
            if (getData.hasExtra("KEY_NAME")) {
                mName = getData.getStringExtra("KEY_NAME");
                tvUserNameSingleTransaction.setText(mName);
            }
        }

        Intent getTblId = getIntent();
        if (getTblId != null) {
            if (getTblId.hasExtra("KEY_TBL_CUSTOMER_ID")) {
                mAddCustomerID = getTblId.getStringExtra("KEY_TBL_CUSTOMER_ID");
                Log.e("tblusomtreid", mAddCustomerID + "");
                loadRecyclerView();
            }
        }

        //button click for pdf

        sendInvoiceSingleUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    timeStamp = new SimpleDateFormat("ddMMMyyyy_HHmmss").format(Calendar.getInstance().getTime());
                    progressDialog.setTitle("Please wait");
                    progressDialog.setMessage("Generating report");
                    progressDialog.show();
                    progressDialog.setCancelable(false);
                    createPDF();
                } catch (DocumentException e) {
                    e.printStackTrace();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        });

        //loadRecyclerView();

        ivBackSingleUserTransaction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        llPayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentPayment = new Intent(SingleUserTransaction.this, ActivityPayment.class);
                intentPayment.putExtra("KEY_NAME_SINGLE", mName);
                intentPayment.putExtra("KEY_CUSTOMER_ID", mAddCustomerID);
                intentPayment.putExtra("KEY_PAYMENT_TYPE", "recive");
                startActivity(intentPayment);
            }
        });

        llCredit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentPayment = new Intent(SingleUserTransaction.this, ActivityPayment.class);
                intentPayment.putExtra("KEY_NAME_SINGLE", mName);
                intentPayment.putExtra("KEY_CUSTOMER_ID", mAddCustomerID);
                intentPayment.putExtra("KEY_PAYMENT_TYPE", "send");
                startActivity(intentPayment);
            }
        });

        ivShareSingleTransaction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BottomSheetShareStatement bottomSheetShareStatement = new BottomSheetShareStatement();
                bottomSheetShareStatement.show(getSupportFragmentManager(), "BottomSheetShareStatement");

            }
        });

        ivCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (shouldCallOrRemind) {
                    BottomSheetCall bottomSheetCall = new BottomSheetCall();
                    Bundle bundle = new Bundle();
                    bundle.putString("KEY_CUSTOMER_NUMBER", mCustomerMobile);
                    bundle.putString("KEY_CUSTOMER_NAME", mName);
                    bottomSheetCall.setArguments(bundle);
                    bottomSheetCall.show(getSupportFragmentManager(), "BottomSheetCall");
                } else {
                    Toast.makeText(SingleUserTransaction.this, mMessageToDisplay, Toast.LENGTH_SHORT).show();
                }
            }
        });

        ivReminder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (shouldCallOrRemind) {
                    BottomSheetSendReminder bottomSheetSendReminder = new BottomSheetSendReminder();
                    Bundle bundle = new Bundle();
                    bundle.putString("KEY_CUSTOMER_NUMBER", mCustomerMobile);
                    bundle.putString("KEY_CUSTOMER_NAME", mName);
                    bundle.putFloat("KEY_DUE_AMOUNT", mDueAmount);
                    bottomSheetSendReminder.setArguments(bundle);
                    bottomSheetSendReminder.show(getSupportFragmentManager(), "BottomSheetSendReminder");
                } else {
                    Toast.makeText(SingleUserTransaction.this, mMessageToDisplay, Toast.LENGTH_SHORT).show();
                }
            }
        });

/*        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

            }
        }, 500);*/

    }

    private void loadLanguage() {
        if (Common.getLanguage(getApplicationContext()).equalsIgnoreCase(Common.HINDI)) {
            tv_credit.setText("कुल उधार");
            tv_recieved.setText("कुल पेमेंट");
            tv_recived_payment.setText("रिसीव पेमेंट");
            tv_credit_payment.setText("उधार दें");
            tvAdvanceOrDue.setText("शेष राशि");
            tvCreditOrAdvanced.setText("एडवांस");

        } else {
            //If not, display "no connection" warning:
            tv_credit.setText(tv_credit.getText().toString());
            tv_recieved.setText(tv_recieved.getText().toString());
            tv_recived_payment.setText(tv_recived_payment.getText().toString());
            tv_credit_payment.setText(tv_credit_payment.getText().toString());
            tvAdvanceOrDue.setText(tvAdvanceOrDue.getText().toString());
            tvCreditOrAdvanced.setText(tvCreditOrAdvanced.getText().toString());

        }
    }


    //Create PDF
    private void createPDF() throws DocumentException, IOException {

        Date currentTime = Calendar.getInstance().getTime();
        Log.e(TAG, "createPDF: " + currentTime);
        Log.d(TAG, "createPDFcacascac:");

        File docsFolder = new File(Environment.getExternalStorageDirectory() + "/PayKredit");
        if (!docsFolder.exists()) {
            docsFolder.mkdir();
            Log.d(TAG, "createPDFFolder: ");
        }

        //String pdfName = "Statement.pdf";
        String pdfName = "Statement" + timeStamp + ".pdf";
        pdfFile = new File(docsFolder.getAbsoluteFile(), pdfName);
        Log.e(TAG, "createPDF: " + pdfFile);

        try {
            OutputStream outputStream = new FileOutputStream(pdfFile);
            Document document = new Document(PageSize.A4);
            PdfPTable table = new PdfPTable(new float[]{2, 3, 3, 3, 3});
            table.setSpacingBefore(20);
            table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
            table.getDefaultCell().setFixedHeight(50);
            table.setTotalWidth(PageSize.A4.getWidth());
            table.setWidthPercentage(100);
            table.getDefaultCell().setVerticalAlignment(Element.ALIGN_MIDDLE);
            table.addCell("S.No");
            table.addCell("Date");
            table.addCell("Description");
            table.addCell("Credit(+)");
            table.addCell("Debit(-)");
            table.setHeaderRows(1);

            PdfPCell[] cells = table.getRow(0).getCells();
            for (int j = 0; j < cells.length; j++) {
                cells[j].setBackgroundColor(BaseColor.GRAY);
            }
            Log.e(TAG, "createPDFlistsize: " + listPDF.size() + "");
            for (int i = 0; i < listPDF.size(); i++) {
                table.addCell(String.valueOf(i + 1));
                table.addCell(listPDF.get(i).getDate());
                table.addCell("Description");
                table.addCell(listPDF.get(i).getAmount());
                table.addCell(listPDF.get(i).getPaidOrReceived());
            }

            PdfWriter.getInstance(document, outputStream);
            document.open();
            Font f = new Font(Font.FontFamily.TIMES_ROMAN, 30.0f, Font.UNDERLINE, BaseColor.BLUE);
            Font fontBold = new Font(Font.FontFamily.TIMES_ROMAN, 16.0f, Font.BOLD, BaseColor.BLACK);
            Font fontTitle = new Font(Font.FontFamily.TIMES_ROMAN, 20.0f, Font.BOLD, BaseColor.BLACK);
            Font fontDue = new Font(Font.FontFamily.TIMES_ROMAN, 16.0f, Font.BOLD, BaseColor.RED);

            BaseColor myBaseColorGreen = new BaseColor(61, 158, 53);
            Font fontAdvanced = new Font(Font.FontFamily.TIMES_ROMAN, 16.0f, Font.BOLD, myBaseColorGreen);

            Drawable d = getResources().getDrawable(R.drawable.logo);
            BitmapDrawable bitDw = ((BitmapDrawable) d);
            Bitmap bmp = bitDw.getBitmap();
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bmp.compress(Bitmap.CompressFormat.PNG, 100, stream);
            Image image = Image.getInstance(stream.toByteArray());
            image.scaleAbsolute(96, 48);
            document.add(image);

            document.add(new Paragraph("Paykredit", fontBold));

            Paragraph shopName = new Paragraph(mBusinessName, fontBold);
            shopName.setAlignment(Element.ALIGN_RIGHT);
            document.add(shopName);

            Paragraph mobileNumber = new Paragraph(mBusinessNumber + "\n", fontBold);
            mobileNumber.setAlignment(Element.ALIGN_RIGHT);
            document.add(mobileNumber);

            Paragraph paragraphTitle = new Paragraph("Transaction History \n\n", fontTitle);
            paragraphTitle.setAlignment(Element.ALIGN_CENTER);
            document.add(paragraphTitle);
            Paragraph customerName = new Paragraph("Customer Name : " + mCustomerName, fontBold);
            document.add(customerName);

            Paragraph balance = new Paragraph("Final Balance :", fontBold);
            balance.setSpacingBefore(8);
            document.add(balance);

            int received = Integer.parseInt(mReceivedValue);
            int sent = Integer.parseInt(mCreditValue);

            int advance = received - sent;
            int due = sent - received;

            if (received > sent) {
                Paragraph advanced = new Paragraph("Advanced Rs: " + advance, fontAdvanced);
                document.add(advanced);
            } else {
                Paragraph advanced = new Paragraph("Due Amount Rs: " + due, fontDue);
                document.add(advanced);
            }

            document.add(table);
            document.close();

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    progressDialog.dismiss();
                    test2();
                }
            }, 500);


        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        } catch (DocumentException ex) {
            ex.printStackTrace();
        }
    }

    public void generateOnlyPDF() throws DocumentException, IOException {
        listPDF.clear();
        progressDialog.setTitle("Please wait");
        progressDialog.setMessage("Generating PDF");
        progressDialog.setCancelable(false);
        progressDialog.show();

        timeStamp = new SimpleDateFormat("ddMMMyyyy_HHmmss").format(Calendar.getInstance().getTime());

        Date currentTime = Calendar.getInstance().getTime();
        Log.e(TAG, "createPDF: " + currentTime);
        Log.d(TAG, "createPDFcacascac:");

        File docsFolder = new File(Environment.getExternalStorageDirectory() + "/PayKredit");
        if (!docsFolder.exists()) {
            docsFolder.mkdir();
            Log.d(TAG, "createPDFFolder: ");
        }

        //String pdfName = "Statement.pdf";
        String pdfName = "Statement" + timeStamp + ".pdf";
        pdfFile = new File(docsFolder.getAbsoluteFile(), pdfName);
        Log.e(TAG, "createPDF: " + pdfFile);

        try {
            OutputStream outputStream = new FileOutputStream(pdfFile);
            Document document = new Document(PageSize.A4);
            PdfPTable table = new PdfPTable(new float[]{2, 3, 3, 3, 3});
            table.setSpacingBefore(20);
            table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
            table.getDefaultCell().setFixedHeight(50);
            table.setTotalWidth(PageSize.A4.getWidth());
            table.setWidthPercentage(100);
            table.getDefaultCell().setVerticalAlignment(Element.ALIGN_MIDDLE);
            table.addCell("S.No");
            table.addCell("Date");
            table.addCell("Description");
            table.addCell("Credit(+)");
            table.addCell("Debit(-)");
            table.setHeaderRows(1);

            PdfPCell[] cells = table.getRow(0).getCells();
            for (int j = 0; j < cells.length; j++) {
                cells[j].setBackgroundColor(BaseColor.GRAY);
            }
            Log.e(TAG, "createPDFlistsize: " + listPDF.size() + "");
            for (int i = 0; i < listPDF.size(); i++) {
                table.addCell(String.valueOf(i + 1));
                table.addCell(listPDF.get(i).getDate());
                table.addCell("Description");
                table.addCell(listPDF.get(i).getAmount());
                table.addCell(listPDF.get(i).getPaidOrReceived());
            }

            PdfWriter.getInstance(document, outputStream);
            document.open();
            Font f = new Font(Font.FontFamily.TIMES_ROMAN, 30.0f, Font.UNDERLINE, BaseColor.BLUE);
            Font fontBold = new Font(Font.FontFamily.TIMES_ROMAN, 16.0f, Font.BOLD, BaseColor.BLACK);
            Font fontTitle = new Font(Font.FontFamily.TIMES_ROMAN, 20.0f, Font.BOLD, BaseColor.BLACK);
            Font fontDue = new Font(Font.FontFamily.TIMES_ROMAN, 16.0f, Font.BOLD, BaseColor.RED);

            BaseColor myBaseColorGreen = new BaseColor(61, 158, 53);
            Font fontAdvanced = new Font(Font.FontFamily.TIMES_ROMAN, 16.0f, Font.BOLD, myBaseColorGreen);

            Drawable d = getResources().getDrawable(R.drawable.logo);
            BitmapDrawable bitDw = ((BitmapDrawable) d);
            Bitmap bmp = bitDw.getBitmap();
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bmp.compress(Bitmap.CompressFormat.PNG, 100, stream);
            Image image = Image.getInstance(stream.toByteArray());
            image.scaleAbsolute(96, 48);
            document.add(image);

            document.add(new Paragraph("Paykredit", fontBold));
            Paragraph shopName = new Paragraph(mBusinessName, fontBold);
            shopName.setAlignment(Element.ALIGN_RIGHT);
            document.add(shopName);

            Paragraph mobileNumber = new Paragraph(mBusinessNumber + "\n", fontBold);
            mobileNumber.setAlignment(Element.ALIGN_RIGHT);
            document.add(mobileNumber);

            Paragraph paragraphTitle = new Paragraph("Transaction History \n\n", fontTitle);
            paragraphTitle.setAlignment(Element.ALIGN_CENTER);
            document.add(paragraphTitle);
            Paragraph customerName = new Paragraph("Customer Name : " + mCustomerName, fontBold);
            document.add(customerName);

            Paragraph balance = new Paragraph("Final Balance :", fontBold);
            balance.setSpacingBefore(8);
            document.add(balance);

            int received = Integer.parseInt(mReceivedValue);
            int sent = Integer.parseInt(mCreditValue);

            int advance = received - sent;
            int due = sent - received;

            if (received > sent) {
                Paragraph advanced = new Paragraph("Advanced Rs: " + advance, fontAdvanced);
                document.add(advanced);
            } else {
                Paragraph advanced = new Paragraph("Due Amount Rs: " + due, fontDue);
                document.add(advanced);
            }

            document.add(table);
            document.close();

            sendPathToShareStatement();

        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        } catch (DocumentException ex) {
            ex.printStackTrace();
        }
    }

    private void sendPathToShareStatement() {

        BottomSheetShareStatement bottomSheetShareStatement = new BottomSheetShareStatement();
        Bundle bundle = new Bundle();
        bundle.putString("KEY_PATH", String.valueOf(pdfFile));
        bottomSheetShareStatement.setArguments(bundle);
        bottomSheetShareStatement.show(getSupportFragmentManager(), "AAAA");

        progressDialog.dismiss();
    }

    private void test2() {
        {
            PackageManager packageManager = SingleUserTransaction.this.getPackageManager();
            Intent testIntent = new Intent(Intent.ACTION_VIEW);
            testIntent.setType("application/pdf");
            List list = packageManager.queryIntentActivities(testIntent, PackageManager.MATCH_DEFAULT_ONLY);
            if (list.size() > 0) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                Uri uri = Uri.fromFile(pdfFile);
                intent.setDataAndType(uri, "application/pdf");
                startActivity(intent);
            } else {
                Toast.makeText(SingleUserTransaction.this, "Download a PDF Viewer to see the generated PDF", Toast.LENGTH_SHORT).show();
            }
        }

    }

    public static SingleUserTransaction getInstance() {
        return singleUserTransaction;
    }

   /* @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_single_user_transaction, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int menuID = item.getItemId();
        switch (menuID){
            case R.id.menu_call:
                BottomSheetCall bottomSheetCall = new BottomSheetCall();
                bottomSheetCall.show(getSupportFragmentManager(),"BottomSheetCall");
                return true;
            case R.id.menu_reminder:
                BottomSheetSendReminder bottomSheetSendReminder = new BottomSheetSendReminder();
                bottomSheetSendReminder.show(getSupportFragmentManager(),"BottomSheetSendReminder");
                return true;
            case R.id.option3:
                Toast.makeText(getApplicationContext(),"Option 3 clicked",Toast.LENGTH_LONG).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }*/

    public void loadRecyclerView() {

        listVertical = new ArrayList<>();
        listVertical.clear();

        listSingleUserTransaction = new ArrayList<>();

        listSingleUserTransaction.clear();


        listPDF.clear();

        pbSingleUserTransaction.setVisibility(View.VISIBLE);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, PayKreditAPI.CUSTOMER_RECORD_LIST, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.e("responsesingluseron", response + "");
                listSingleUserTransaction = new ArrayList<>();
                listSingleUserTransaction.clear();
                pbSingleUserTransaction.setVisibility(View.GONE);

                try {
                    JSONObject jsonObject = new JSONObject(response);

                    String message = jsonObject.getString("message");
                    mCreditValue = jsonObject.getString("send_value");
                    mReceivedValue = jsonObject.getString("recived_value");
                    String status = jsonObject.getString("status");

                    mCustomerName = jsonObject.getString("customer_name");
                    mCustomerMobile = jsonObject.getString("customer_mobile");

                    if (status.equalsIgnoreCase("1")) {

                        llBgBalanceOrDue.setVisibility(View.VISIBLE);
                        tvReceivedSingleTransaction.setText(mReceivedValue);
                        tvCreditSingleTransaction.setText(mCreditValue);
                        rvSingleUserTransaction.setVisibility(View.VISIBLE);

                        float receivedValue = 0;
                        float creditValue = 0;

                        receivedValue = Float.parseFloat(mReceivedValue);
                        creditValue = Float.parseFloat(mCreditValue);

                        if (creditValue > receivedValue) {
                            llBgBalanceOrDue.setBackground(getResources().getDrawable(R.drawable.bg_due_red));
                            tvAmountCreditOrDue.setText(String.valueOf(creditValue - receivedValue));
                            if (Common.getLanguage(context).equalsIgnoreCase(Common.HINDI)) {
                                tvCreditOrAdvanced.setText("उधार");
                            } else {
                                tvCreditOrAdvanced.setText("Credit");
                            }
                            mDueAmount = creditValue - receivedValue;

                            shouldCallOrRemind = true;

                            Log.e("huhuhuhu", mDueAmount + "");


                        }

                        if (receivedValue > creditValue) {
                            shouldCallOrRemind = false;
                            mMessageToDisplay = "Advance paid, don't need to send reminder";
                            llBgBalanceOrDue.setBackground(getResources().getDrawable(R.drawable.btn_bg));
                            tvAmountCreditOrDue.setText(String.valueOf(receivedValue - creditValue));
                            if (Common.getLanguage(context).equalsIgnoreCase(Common.HINDI)) {
                                tvCreditOrAdvanced.setText("एडवांस");
                            } else {
                                tvCreditOrAdvanced.setText("Advance");
                            }
                        }

                        if (creditValue == receivedValue) {
                            llBgBalanceOrDue.setVisibility(View.GONE);

                        }

                        JSONArray jsonArray = jsonObject.getJSONArray("data");

                        if (jsonArray.length() > 0) {
                            tvNoTransactionFound.setVisibility(View.GONE);
                        }


                        for (int i = 0; i < jsonArray.length(); i++) {
                            List<ModelSingleUserTransaction> listSingle = new ArrayList<>();
                            JSONObject data = jsonArray.getJSONObject(i);

                            String title = data.getString("title");

                            JSONArray data_date_wise = data.getJSONArray("data_date_wise");

                            if (data_date_wise != null) {
                                if (data_date_wise.length() > 0) {

                                    //int k = data_date_wise.length() - 1; k >= 0; k--

                                    for (int k = 0; k < data_date_wise.length(); k++) {
                                        JSONObject internalData = data_date_wise.getJSONObject(k);

                                        String tbl_record_id = internalData.getString("tbl_record_id");

                                        String payment = internalData.getString("payment");
                                        String date = internalData.getString("date");
                                        String payment_type = internalData.getString("payment_type");
                                        String time = internalData.getString("time");
                                        String msg = internalData.getString("msg");

                                        int type;
                                        if (payment_type.equalsIgnoreCase("send")) {
                                            type = 0;
                                        } else {
                                            type = 1;
                                        }


                                        // listSingle.add(new ModelSingleUserTransaction(type, tbl_record_id, payment, date, time, "", 2,msg));
                                        listSingleUserTransaction.add(new ModelSingleUserTransaction(type, tbl_record_id, payment, date, time, "", 2, msg));
                                        listPDF.add(new ModelSingleUserTransaction(type, tbl_record_id, payment, date, time, payment_type, 2, msg));
                                        Log.e("hjkhkjhkj", listSingle.size() + "");
                                    }


                                }
                            } else {
                                Toast.makeText(SingleUserTransaction.this, "Some error occurred.", Toast.LENGTH_SHORT).show();
                            }
                            //    listVertical.add(new ModelVertical(title, listSingle));
                            // adapterVertical = new AdapterVertical(SingleUserTransaction.this, listVertical);

                            Log.e("dddddddddddddddddd", listVertical.size() + "----" + listSingle.size() + "");
                        }

                        adapterSingleUserTransaction = new AdapterSingleUserTransaction(listSingleUserTransaction, SingleUserTransaction.this);
                        linearLayoutManager = new LinearLayoutManager(SingleUserTransaction.this);
                        linearLayoutManager.setReverseLayout(true);
                        linearLayoutManager.setStackFromEnd(true);
                        rvSingleUserTransaction.setLayoutManager(linearLayoutManager);
                        rvSingleUserTransaction.setNestedScrollingEnabled(false);
                        rvSingleUserTransaction.setAdapter(adapterSingleUserTransaction);
                        rvSingleUserTransaction.scrollToPosition(0);
                    } else {
                        tvNoTransactionFound.setVisibility(View.VISIBLE);
                        rvSingleUserTransaction.setVisibility(View.GONE);
                        mMessageToDisplay = "No transactions, can't send reminder";
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                pbSingleUserTransaction.setVisibility(View.GONE);
                Toast.makeText(SingleUserTransaction.this, "" + error.getMessage() + "", Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("tbl_add_customer_id", mAddCustomerID);
                params.put("user_id", mUserID);
                Log.e("paramssingleuser", params + "");
                return params;
            }
        };

        RequestQueue requestQueue = SingletonRequestQueue.getInstance(SingleUserTransaction.this).getRequestQueue();
        requestQueue.add(stringRequest);

    }

    private void scrolltolast() {
        adapterSingleUserTransaction.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                super.onItemRangeInserted(positionStart, itemCount);

                int friendlyMessageCount = adapterSingleUserTransaction.getItemCount();
                int lastVisiblePosition = linearLayoutManager.findLastCompletelyVisibleItemPosition();
                // If the recycler view is initially being loaded or the
                // user is at the bottom of the list, scroll to the bottom
                // of the list to show the newly added message.

                Log.e("countttmess", friendlyMessageCount + "");
                if (lastVisiblePosition == -1 ||
                        (positionStart >= (friendlyMessageCount - 1) &&
                                lastVisiblePosition == (positionStart - 1))) {
                    rvSingleUserTransaction.scrollToPosition(positionStart);

                }
            }
        });


    }

    @Override
    protected void onPostResume() {
        super.onPostResume();

    }

    @Override
    protected void onPause() {
        super.onPause();
        loadRecyclerView();
    }
}
