package com.lgt.paykredit.Activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.lgt.paykredit.Adapter.AdapterAddedProducts;
import com.lgt.paykredit.Adapter.ProductAddAdapter;
import com.lgt.paykredit.Fragments.AddNewProduct;
import com.lgt.paykredit.Models.ProductModel;
import com.lgt.paykredit.Models.ProductModelNew;
import com.lgt.paykredit.R;
import com.lgt.paykredit.extras.GenerateCalculation;
import com.lgt.paykredit.extras.LoadInvoiceData;
import com.lgt.paykredit.extras.SingletonRequestQueue;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Locale;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import de.hdodenhof.circleimageview.CircleImageView;
/*
import static com.lgt.paykredit.Adapter.AdapterAddedProducts.ProductAmt;
import static com.lgt.paykredit.Adapter.AdapterAddedProducts.AdvanceAmt;
import static com.lgt.paykredit.Adapter.AdapterAddedProducts.ProductDis;
import static com.lgt.paykredit.Adapter.AdapterAddedProducts.ProductId;
import static com.lgt.paykredit.Adapter.AdapterAddedProducts.ProductName;
import static com.lgt.paykredit.Adapter.AdapterAddedProducts.ProductQua;
import static com.lgt.paykredit.Adapter.AdapterAddedProducts.ProductTax;
import static com.lgt.paykredit.Adapter.AdapterAddedProducts.itemPrice;
import static com.lgt.paykredit.Adapter.AdapterAddedProducts.ItemDue;
import static com.lgt.paykredit.Adapter.AdapterAddedProducts.ItemDiscount;*/
import static com.lgt.paykredit.Adapter.ExistingCustomerAdapter.customer_id;
import static com.lgt.paykredit.Adapter.ExistingCustomerAdapter.customer_name;
import static com.lgt.paykredit.Adapter.ExistingCustomerAdapter.isCustomerAdded;
import static com.lgt.paykredit.Fragments.AddNewProduct.AdvanceAmt;
import static com.lgt.paykredit.Fragments.AddNewProduct.FINALPAYTOME;
import static com.lgt.paykredit.Fragments.AddNewProduct.FINALPRICE;
import static com.lgt.paykredit.Fragments.AddNewProduct.FINALTAXAFTERDISCOUNT;
import static com.lgt.paykredit.Fragments.AddNewProduct.ItemDiscount;
import static com.lgt.paykredit.Fragments.AddNewProduct.ItemDue;
import static com.lgt.paykredit.Fragments.AddNewProduct.PDISCOUNT;
import static com.lgt.paykredit.Fragments.AddNewProduct.PID;
import static com.lgt.paykredit.Fragments.AddNewProduct.PNAME;
import static com.lgt.paykredit.Fragments.AddNewProduct.PQUANTITY;
import static com.lgt.paykredit.Fragments.AddNewProduct.PRATE;
import static com.lgt.paykredit.Fragments.AddNewProduct.PTAX;
import static com.lgt.paykredit.Fragments.AddNewProduct.ProductAmt;
import static com.lgt.paykredit.Fragments.AddNewProduct.ProductDis;
import static com.lgt.paykredit.Fragments.AddNewProduct.ProductId;
import static com.lgt.paykredit.Fragments.AddNewProduct.ProductName;
import static com.lgt.paykredit.Fragments.AddNewProduct.ProductQua;
import static com.lgt.paykredit.Fragments.AddNewProduct.ProductTax;
import static com.lgt.paykredit.Fragments.AddNewProduct.TOTALAMOUNT;
import static com.lgt.paykredit.Fragments.AddNewProduct.TOTALDISCOUNTAMOUNT;
import static com.lgt.paykredit.Fragments.AddNewProduct.itemPrice;
import static com.lgt.paykredit.Fragments.CreateCusFragment.isCustomerNewAdded;
import static com.lgt.paykredit.Fragments.CreateCusFragment.new_customer_id;
import static com.lgt.paykredit.Fragments.CreateCusFragment.new_customer_name;
import static com.lgt.paykredit.extras.PayKreditAPI.CREATE_INVOICE_API;

public class CreateInvoice extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, LoadInvoiceData, GenerateCalculation {
    LinearLayout ll_invoice_date_picker, ll_due_date_picker;
    CircleImageView civ_add_new_customer, civ_add_product_service;
    ImageView iv_back_press, iv_add_bank_details, iv_deleteCustomer;
    TextView tv_due_date, tv_invoice_date, tv_invoice_id, save_invoice_btn, tv_noProductFoundNotify;
    TextView tv_subTotal, tv_duscount_price, tv_gst_amt, tv_totalAmt, tvamount,tv_download_invoice,tv_shareInvoice;
    EditText et_advanceAmount;
    public static TextView tv_DiscountInPrice, tv_subTotalPrice, tv_AdvancePrice, tv_BalanceDue;
    Set<String> mProductID = new HashSet<>();
    Set<String> mProductPriceSingle = new HashSet<>();
    Set<String> mProductDiscount = new HashSet<>();
    Set<String> mProductQuantity = new HashSet<>();
    Set<String> mProductName = new HashSet<>();
    EditText etSelectCustomer, et_extra_note, et_acHolderName, et_acHolderNumber, et_acHolderIFCSCode;
    Spinner sp_due_terms;
    RecyclerView rvProductItemList;
    private String due_date_picker = "", invoice_date_picker = "";
    View myView;
    boolean isUp;
    Intent intent = null;
    ArrayList<String> mDueTermList = new ArrayList<>();
    ArrayList<ProductModelNew> mList = new ArrayList<>();
    ProductAddAdapter productAddAdapter;
    public String termSelect = "", InvoiceUser = "", invoiceID = "", invoiceCustomerId = "", IfcsCode = "", AccountHolderName = "", AccountNumber = "", ExtraNote = "";
    public String FinalDiscount = "", FinalSubTotal = "", FinalAdvance = "", FinalBalance = "", SinglePrice = "";
    private SharedPreferences sharedPreferences;
    private String mUserID, mAcName = "", mAcNumber = "", mAcCode = "";
    private Calendar dueCalender;
    private Calendar myCalendar;
    private Context context;
    private CreateInvoice activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_invoice);
        context = activity = this;
        sharedPreferences = CreateInvoice.this.getSharedPreferences("USER_DATA", MODE_PRIVATE);
        if (sharedPreferences.contains("KEY_USER_ID")) {
            mUserID = sharedPreferences.getString("KEY_USER_ID", "");
            mAcName = sharedPreferences.getString("KEY_AC_NAME", "");
            mAcNumber = sharedPreferences.getString("KEY_AC_NUMBER", "");
            mAcCode = sharedPreferences.getString("KEY_AC_CODE", "");
        }
        if (mUserID != null) {
            if (!mUserID.equalsIgnoreCase("")) {
                initView();
            }
        }
    }

    private void initView() {
        // new items
        et_advanceAmount = findViewById(R.id.et_advanceAmount);
        tv_subTotal = findViewById(R.id.tv_subTotal);
        tv_duscount_price = findViewById(R.id.tv_duscount_price);
        tv_gst_amt = findViewById(R.id.tv_gst_amt);
        tv_totalAmt = findViewById(R.id.tv_totalAmt);
        tvamount = findViewById(R.id.tvamount);
        tv_download_invoice = findViewById(R.id.tv_download_invoice);
        tv_shareInvoice = findViewById(R.id.tv_shareInvoice);
        tv_noProductFoundNotify = findViewById(R.id.tv_noProductFoundNotify);


        tv_DiscountInPrice = findViewById(R.id.tv_DiscountInPrice);
        tv_subTotalPrice = findViewById(R.id.tv_subTotalPrice);
        tv_AdvancePrice = findViewById(R.id.tv_AdvancePrice);
        tv_BalanceDue = findViewById(R.id.tv_BalanceDue);
        iv_back_press = findViewById(R.id.iv_back_press);
        sp_due_terms = findViewById(R.id.sp_due_terms);
        rvProductItemList = findViewById(R.id.rvProductItemList);
        etSelectCustomer = findViewById(R.id.etSelectCustomer);
        iv_deleteCustomer = findViewById(R.id.iv_deleteCustomer);
        ll_invoice_date_picker = findViewById(R.id.ll_invoice_date_picker);
        tv_due_date = findViewById(R.id.tv_due_date);
        tv_invoice_id = findViewById(R.id.tv_invoice_id);
        tv_invoice_date = findViewById(R.id.tv_invoice_date);
        ll_due_date_picker = findViewById(R.id.ll_due_date_picker);
        iv_add_bank_details = findViewById(R.id.iv_add_bank_details);
        civ_add_new_customer = findViewById(R.id.civ_add_new_customer);
        civ_add_product_service = findViewById(R.id.civ_add_product_service);
        // save invoice
        save_invoice_btn = findViewById(R.id.save_invoice_btn);
        et_extra_note = findViewById(R.id.et_extra_note);
        et_acHolderName = findViewById(R.id.et_acHolderName);
        et_acHolderNumber = findViewById(R.id.et_acHolderNumber);
        et_acHolderIFCSCode = findViewById(R.id.et_acHolderIFCSCode);
        myView = findViewById(R.id.my_view);
        myView.setVisibility(View.VISIBLE);
        et_acHolderName.setText(mAcName);
        et_acHolderNumber.setText(mAcNumber);
        et_acHolderIFCSCode.setText(mAcCode);
        isUp = true;
        clickView();
        loadDueTerms();
        tv_shareInvoice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context, "Share Invoice", Toast.LENGTH_SHORT).show();
            }
        });
        tv_download_invoice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context, "Download Invoice", Toast.LENGTH_SHORT).show();
            }
        });
        sp_due_terms.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                if (position == 0) {
                    // Toast.makeText(CreateInvoice.this, "Select Due Term!", Toast.LENGTH_SHORT).show();
                } else {
                    termSelect = adapterView.getItemAtPosition(position).toString();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        ll_invoice_date_picker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                /*                Calendar now = Calendar.getInstance();
                //now.add(Calendar.YEAR,);
                DatePickerDialog dpd = DatePickerDialog.newInstance(
                        CreateInvoice.this,
                        now.get(Calendar.YEAR), // Initial year selection
                        now.get(Calendar.MONTH + 1), // Initial month selection
                        now.get(Calendar.DAY_OF_MONTH) // Init day selection

                );

                dpd.setOnDateSetListener(new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
                        Log.d("invoice_date_picked", dayOfMonth + "-" + String.valueOf(monthOfYear+1) + "-" + year);
                        invoice_date_picker = dayOfMonth + "/" +String.valueOf(monthOfYear+1) + "/" + year;
                        tv_invoice_date.setText(invoice_date_picker);
                    }
                });
                dpd.setOkColor(Color.WHITE);
                dpd.setCancelColor(Color.WHITE);
                dpd.show(getSupportFragmentManager(), "invoice_date_picker");*/
                invoiceDatePicker();

            }
        });

        ll_due_date_picker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                dueDatePicker();
            }
        });
        intent = getIntent();
        // not in use
        /*if (intent.hasExtra("comeFrom")) {
            String comfrom = intent.getStringExtra("comeFrom");
            if (comfrom.equalsIgnoreCase("CustomerDetails")) {
                Toast.makeText(this, "have customer details", Toast.LENGTH_SHORT).show();
                String cusName = intent.getStringExtra("customer_name");
                String custId = intent.getStringExtra("customer_id");
                iv_deleteCustomer.setVisibility(View.VISIBLE);
                etSelectCustomer.setText(cusName);
            } else if (comfrom.equalsIgnoreCase("ProductDetails")) {
                String ProductId = intent.getStringExtra("ProductId");
                String ProductName = intent.getStringExtra("ProductName");
                String ProductAmt = intent.getStringExtra("ProductAmt");
                String ProductDis = intent.getStringExtra("ProductDis");
                String ProductQua = intent.getStringExtra("ProductQua");
                String ProductTax = intent.getStringExtra("ProductTax");
                if (mList.size() == 0){
                    mList.add(new ProductModel(ProductId,ProductName,ProductAmt,ProductDis,ProductQua,ProductTax));
                    setUpAddProductView();
                }else {
                    mList.add(new ProductModel(ProductId,ProductName,ProductAmt,ProductDis,ProductQua,ProductTax));
                    productAddAdapter.notifyDataSetChanged();
                }
            }
        } else {}*/
        invoiceID = "INSTA" + getRandomNumberString();
        tv_invoice_id.setText(invoiceID);

        iv_deleteCustomer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                etSelectCustomer.setText("");
                iv_deleteCustomer.setVisibility(View.GONE);
                Toast.makeText(CreateInvoice.this, "Cancel Current Customer", Toast.LENGTH_SHORT).show();
            }
        });

        save_invoice_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ExtraNote = et_extra_note.getText().toString().trim();
                IfcsCode = et_acHolderIFCSCode.getText().toString().trim();
                AccountNumber = et_acHolderNumber.getText().toString().trim();
                AccountHolderName = et_acHolderName.getText().toString().trim();
                if (validateSaveFields()) {
                    saveInvoice();
                } else {
                    Toast.makeText(CreateInvoice.this, "Required All Field!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        et_advanceAmount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (tv_totalAmt.getText().toString().trim().equalsIgnoreCase("")){
                    Toast.makeText(context, "Please Select Product First", Toast.LENGTH_SHORT).show();
                }else{
                    int finalPaidAmt = Integer.parseInt(tv_totalAmt.getText().toString().trim());
                    if (charSequence.toString().length()>0){
                        int AdvancePayment = Integer.parseInt(charSequence.toString());
                        int PaidAmt = (finalPaidAmt-AdvancePayment);
                        Log.d("PaidAmt",""+PaidAmt);
                        if (!String.valueOf(PaidAmt).contains("-")){
                            if (AdvancePayment !=0 && AdvancePayment < finalPaidAmt){
                                tvamount.setText(""+PaidAmt);
                            }else if(AdvancePayment == finalPaidAmt){
                                tvamount.setText("0");
                            }
                        }else{
                            tvamount.setText("0");
                        }
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        myCalendar = Calendar.getInstance();
        dueCalender = Calendar.getInstance();
        // String date = new SimpleDateFormat("dd-mm-yyyy", Locale.getDefault()).format(new Date());
        String date = new SimpleDateFormat("dd/MM/yyyy").format(new Date());
        invoice_date_picker = date;
        tv_invoice_date.setText(date);
    }

    private void dueDatePicker() {
        final android.app.DatePickerDialog.OnDateSetListener date = (view, year, monthOfYear, dayOfMonth) -> {
            // TODO Auto-generated method stub
            dueCalender.set(Calendar.YEAR, year);
            dueCalender.set(Calendar.MONTH, monthOfYear);
            dueCalender.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateDueLabel();
        };
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            android.app.DatePickerDialog datePickerDialog = new android.app.DatePickerDialog(activity, date
                    , dueCalender.get(Calendar.YEAR), dueCalender.get(Calendar.MONTH), dueCalender.get(Calendar.DAY_OF_MONTH)
            );
            datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis());
            datePickerDialog.show();
        }
    }

    private void updateDueLabel() {
        String myFormat = "dd/MM/yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        due_date_picker = sdf.format(dueCalender.getTime());
        tv_due_date.setText(sdf.format(dueCalender.getTime()));
    }

    private void invoiceDatePicker() {
        // myCalendar.add(Calendar.YEAR, -18);                //Goes 10 Year Back in time ^^

        final android.app.DatePickerDialog.OnDateSetListener date = (view, year, monthOfYear, dayOfMonth) -> {
            // TODO Auto-generated method stub

            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, monthOfYear);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateLabel();
        };
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            android.app.DatePickerDialog datePickerDialog = new android.app.DatePickerDialog(activity, date
                    , myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH)
            );
            long upperLimit = myCalendar.getTimeInMillis();
            datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());

            datePickerDialog.show();

        }
    }

    private void updateLabel() {
        String myFormat = "dd/MM/yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        invoice_date_picker = sdf.format(myCalendar.getTime());
        tv_invoice_date.setText(sdf.format(myCalendar.getTime()));
    }


    private boolean validateSaveFields() {
        if (isValidIFSCode(IfcsCode)) {
            return true;
        }
        if (TextUtils.isEmpty(AccountNumber)) {
            et_acHolderNumber.setError("Enter Account Number");
            et_acHolderNumber.requestFocus();
            return false;
        }
        if (TextUtils.isEmpty(AccountHolderName)) {
            et_acHolderName.setError("Enter Account holder Name");
            et_acHolderName.requestFocus();
            return false;
        }
        return true;
    }

    private void saveInvoice() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, CREATE_INVOICE_API, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("save_invoice", "" + response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String message = jsonObject.getString("message");
                    String status = jsonObject.getString("status");
                    if (status.equalsIgnoreCase("1")) {
                        Toast.makeText(CreateInvoice.this, message, Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(CreateInvoice.this, InvoiceMainPage.class));
                        finish();
                    } else {
                        Toast.makeText(CreateInvoice.this, message, Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("save_invoice", "" + error.getMessage());
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> param = new HashMap<>();
                param.put("user_id", mUserID);
                param.put("invoice_customer_id", invoiceCustomerId);
                param.put("invoice_no", invoiceID);
                param.put("invoice_date", invoice_date_picker);
                param.put("due_date", due_date_picker);
                param.put("due_term", termSelect);
                param.put("total_discount", FinalDiscount);
                param.put("sub_total", FinalSubTotal);
                param.put("total_advance", FinalAdvance);
                param.put("total_balance", FinalBalance);
                param.put("account_number", AccountNumber);
                param.put("account_holder_name", AccountHolderName);
                param.put("IFSC_code", IfcsCode);
                param.put("note", ExtraNote);
                param.put("products_id", getProductId(mProductID.toString()));
                param.put("pro_qnt", getProductId(mProductQuantity.toString()));
                param.put("pro_price", getProductId(mProductPriceSingle.toString()));
                param.put("pro_name", getProductId(mProductName.toString()));
                param.put("pro_discount", getProductId(mProductDiscount.toString()));
                Log.d("save_invoice", ">> " + param);
                return param;
            }
        };
        RequestQueue requestQueue = SingletonRequestQueue.getInstance(CreateInvoice.this).getRequestQueue();
        requestQueue.add(stringRequest);
    }

    private String getProductId(String id) {

        return id.replaceAll("\\[", "").replaceAll("\\]", "");
    }

    private void loadDueTerms() {
        mDueTermList.clear();
        mDueTermList.add("Select Due Terms");
        mDueTermList.add("15 Days ");
        mDueTermList.add("30 Days ");
        mDueTermList.add("60 Days ");
        mDueTermList.add("Due on receipt ");
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, mDueTermList);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_due_terms.setAdapter(dataAdapter);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        if (AddNewProduct.IsClickToAdd) {
            AddNewProduct.IsClickToAdd = false;

            float FinalAmtPay = Float.parseFloat(FINALPRICE) * Integer.parseInt(PQUANTITY);
            float FinalTaxAmt = Float.parseFloat(FINALTAXAFTERDISCOUNT) * Integer.parseInt(PQUANTITY);
            float FinalPayBalance = FinalAmtPay+FinalTaxAmt;

            Log.d("received_data", "\nid : " + PID +
                    "\n name :" + PNAME +
                    "\n Amt : " + PRATE +
                    "\n Quantity : " + PQUANTITY +
                    "\n Discount : " + PDISCOUNT +
                    "\n TotalAmt : " + TOTALAMOUNT +
                    "\n TotalAmt : " + FINALPAYTOME +
                    "\n TOTALDISCOUNT : " + TOTALDISCOUNTAMOUNT +
                    "\n TAX : " + PTAX +
                    "\n FINALTAXAFTERDISCOUNT : " + FINALTAXAFTERDISCOUNT +
                    "\n FINALPRICE : " + FINALPRICE +
                    "\n PayAmt : "+FinalPayBalance);

            if (mList.size() >= 0) {
                tv_noProductFoundNotify.setVisibility(View.GONE);
                /*ProductModel productModel = new ProductModel(ProductId, ProductName, ProductAmt, AdvanceAmt, ProductDis, ProductQua, ProductTax);
                productModel.setTotalAmt(Float.valueOf(itemPrice));
                productModel.setBalanceDue(Float.valueOf(ItemDue));
                productModel.setTotalDiscount(Float.valueOf(ItemDiscount));
                 mList.add(productModel);*/
                ProductModelNew productModelNew = new ProductModelNew();
                productModelNew.setId(PID);
                productModelNew.setName(PNAME);
                productModelNew.setRate(PRATE);
                productModelNew.setTax(PTAX);
                productModelNew.setQuantity(PQUANTITY);
                productModelNew.setDiscount(PDISCOUNT);
                productModelNew.setFinalTax(""+FinalTaxAmt);
                productModelNew.setFinalAmount(TOTALAMOUNT);
                productModelNew.setFinalDiscont(TOTALDISCOUNTAMOUNT);
                productModelNew.setFinalPayToMe(FINALPAYTOME);
                productModelNew.setFinalPayBalance(""+FinalPayBalance);
                mList.add(productModelNew);
                setUpAddProductView();
            } else {
                /*ProductModel productModel = new ProductModel(ProductId, ProductName, ProductAmt, AdvanceAmt, ProductDis, ProductQua, ProductTax);
                productModel.setTotalAmt(Float.valueOf(itemPrice));
                productModel.setBalanceDue(Float.valueOf(ItemDue));
                productModel.setTotalDiscount(Float.valueOf(ItemDiscount));
                mList.add(productModel);*/

                ProductModelNew productModelNew = new ProductModelNew();
                productModelNew.setId(PID);
                productModelNew.setName(PNAME);
                productModelNew.setRate(PRATE);
                productModelNew.setTax(PTAX);
                productModelNew.setQuantity(PQUANTITY);
                productModelNew.setDiscount(PDISCOUNT);
                productModelNew.setFinalTax(""+FinalTaxAmt);
                productModelNew.setFinalAmount(TOTALAMOUNT);
                productModelNew.setFinalDiscont(TOTALDISCOUNTAMOUNT);
                productModelNew.setFinalPayToMe(FINALPAYTOME);
                productModelNew.setFinalPayBalance(""+FinalPayBalance);
                mList.add(productModelNew);
                productAddAdapter.notifyDataSetChanged();
            }
        } else if (isCustomerAdded) {
            isCustomerAdded = false;
            String cusName = customer_name;
            InvoiceUser = customer_name;
            invoiceCustomerId = customer_id;
            iv_deleteCustomer.setVisibility(View.VISIBLE);
            etSelectCustomer.setText(cusName);
        } else if (isCustomerNewAdded) {
            String cusName = new_customer_name;
            InvoiceUser = new_customer_id;
            invoiceCustomerId = new_customer_id;
            iv_deleteCustomer.setVisibility(View.VISIBLE);
            etSelectCustomer.setText(cusName);
        }
    }

    private void startCalculation() {
        int SubTotal = 0, FinalDisc = 0, gstAmt = 0, ProductDiscount = 0, FinalPayMe = 0;

        if (mList.size() > 0) {
            for (int i = 0; i < mList.size(); i++) {

                mProductPriceSingle.add(mList.get(i).getRate());
                mProductID.add(mList.get(i).getId());
                mProductDiscount.add(mList.get(i).getDiscount());
                mProductName.add(mList.get(i).getName());
                mProductQuantity.add(mList.get(i).getQuantity());

                /*Float amtdue = mList.get(i).getTotalAmt();
                Float dsc = mList.get(i).getTotalDiscount();
                Float blncDue = mList.get(i).getBalanceDue();

                if (!mList.get(i).getProductAdvance().equalsIgnoreCase("")) {
                    int adv = Integer.parseInt(mList.get(i).getProductAdvance());
                    if (adv > 0) {
                        ProductAdv += adv;
                    }
                }

                if (amtdue > 0) {
                    productPrice += amtdue;
                }

                if (dsc > 0) {
                    ProductDiscount += dsc;
                }

                if (blncDue > 0) {
                    productblncDue += blncDue;
                }*/

                float finalAmt = Float.valueOf(mList.get(i).getFinalAmount());
                if (finalAmt !=0){
                    SubTotal += finalAmt;
                }

                float FinalDiscount = Float.valueOf(mList.get(i).getFinalDiscont());
                if (FinalDiscount != 0){
                    FinalDisc += FinalDiscount;
                }

                float FinalGST = Float.parseFloat(mList.get(i).getFinalTax());
                if (FinalGST != 0){
                    gstAmt += FinalGST;
                }

                float FinalPaay = Float.parseFloat(mList.get(i).getFinalPayToMe());
                if (FinalPaay != 0 ){
                    FinalPayMe += FinalPaay;
                }
            }
        } else {
            tv_subTotal.setText("0");
            tv_duscount_price.setText("0");
            tv_gst_amt.setText("0");
            tv_totalAmt.setText("0");
            tvamount.setText("0");
        }
        setCalcDataToView(SubTotal, FinalDisc, ProductDiscount, gstAmt, FinalPayMe);
    }

    private void setCalcDataToView(int sproprice, int pprice, int pdiscunt, int pqty, int padvnc) {
        SinglePrice = String.valueOf(sproprice);
        FinalDiscount = String.valueOf(pqty);
        FinalAdvance = String.valueOf(padvnc);
        FinalBalance = String.valueOf(pdiscunt);
        FinalSubTotal = String.valueOf(pprice);

        int FIANLAMMT = (sproprice-pprice)+pqty;
        //Log.d("listData:", "Final Price = | " + pprice + " | " + pdiscunt + " | " + pqty + " | " + padvnc);
        tv_subTotal.setText(sproprice + "");
        tv_duscount_price.setText(pprice + "");
        tv_gst_amt.setText(pqty + "");
        tv_totalAmt.setText(FIANLAMMT + "");
        tvamount.setText(FIANLAMMT + "");
    }

    private void setUpAddProductView() {
        productAddAdapter = new ProductAddAdapter(mList, getApplicationContext(), CreateInvoice.this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        rvProductItemList.setLayoutManager(linearLayoutManager);
        rvProductItemList.setHasFixedSize(true);
        rvProductItemList.setAdapter(productAddAdapter);
    }

    public static String getRandomNumberString() {
        // It will generate 6 digit random Number.
        // from 0 to 999999
        Random rnd = new Random();
        int number = rnd.nextInt(999999);
        // this will convert any number sequence into 6 character.
        return String.format("%06d", number);
    }

    public void slideUp(View view) {
        YoYo.with(Techniques.ZoomIn)
                .duration(400)
                .repeat(0)
                .playOn(view);
        view.setVisibility(view.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);
    }

    public void slideDown(View view) {
        YoYo.with(Techniques.ZoomOut)
                .duration(400)
                .repeat(0)
                .playOn(view);
        view.setVisibility(view.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);
    }

    public void onSlideViewButtonClick() {
        if (isUp) {
            slideDown(myView);
        } else {
            slideUp(myView);
        }
        isUp = !isUp;
    }

    private void clickView() {
        iv_back_press.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        civ_add_new_customer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(CreateInvoice.this, AddNewCustomer.class));
                /*if (invoice_date_picker.equalsIgnoreCase("")) {
                    Toast.makeText(CreateInvoice.this, "Select Invoice Date", Toast.LENGTH_SHORT).show();
                } else {
                    if (due_date_picker.equalsIgnoreCase("")) {
                        Toast.makeText(CreateInvoice.this, "Select Due Date", Toast.LENGTH_SHORT).show();
                    } else {
                        startActivity(new Intent(CreateInvoice.this, AddNewCustomer.class));
                    }
                }*/
            }
        });

        civ_add_product_service.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(CreateInvoice.this, ActivityAddedProducts.class));
                /*if (InvoiceUser.equalsIgnoreCase("")) {
                    Toast.makeText(CreateInvoice.this, "Please Select User", Toast.LENGTH_SHORT).show();
                } else {
                    Log.d("listData", termSelect.equalsIgnoreCase("") + " | " + termSelect.equalsIgnoreCase("Select Due Terms"));
                    if (termSelect.equalsIgnoreCase("")) {
                        Toast.makeText(CreateInvoice.this, "Please Select Due Term", Toast.LENGTH_SHORT).show();
                    } else {
                        if (termSelect.equalsIgnoreCase("Select Due Terms")) {
                            Toast.makeText(CreateInvoice.this, "Please Select Valid Due Term", Toast.LENGTH_SHORT).show();
                        } else {
                            startActivity(new Intent(CreateInvoice.this, ActivityAddedProducts.class));
                        }
                    }
                }*/
            }
        });

        iv_add_bank_details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onSlideViewButtonClick();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        mList.clear();
    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        Toast.makeText(this, dayOfMonth + "-" + monthOfYear + 1 + "-" + year, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void addProduct() {
        Log.d("invoice", "Product Added");
    }

    @Override
    public void calculateProductPrice() {
        startCalculation();
    }

    public static boolean isValidIFSCode(String str) {
        // Regex to check valid IFSC Code.
        String regex
                = "^[A-Z]{4}0[A-Z0-9]{6}$";

        // Compile the ReGex
        Pattern p
                = Pattern.compile(regex);

        // If the string is empty
        // return false
        if (str == null) {
            return false;
        }

        // Pattern class contains matcher()
        // method to find matching between
        // the given string and
        // the regular expression.
        Matcher m = p.matcher(str);

        // Return if the string
        // matched the ReGex
        return m.matches();
    }
}
