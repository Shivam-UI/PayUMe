package com.lgt.paykredit.Activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.math.MathUtils;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.lgt.paykredit.Adapter.AdapterAddedProducts;
import com.lgt.paykredit.Adapter.ProductAddAdapter;
import com.lgt.paykredit.Models.ProductModel;
import com.lgt.paykredit.R;
import com.lgt.paykredit.bottomsheets.BottomSheetAddItems;
import com.lgt.paykredit.extras.GenerateCalculation;
import com.lgt.paykredit.extras.LoadInvoiceData;
import com.lgt.paykredit.extras.SingletonRequestQueue;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.lgt.paykredit.Adapter.AdapterAddedProducts.ProductAmt;
import static com.lgt.paykredit.Adapter.AdapterAddedProducts.AdvanceAmt;
import static com.lgt.paykredit.Adapter.AdapterAddedProducts.ProductDis;
import static com.lgt.paykredit.Adapter.AdapterAddedProducts.ProductId;
import static com.lgt.paykredit.Adapter.AdapterAddedProducts.ProductName;
import static com.lgt.paykredit.Adapter.AdapterAddedProducts.ProductQua;
import static com.lgt.paykredit.Adapter.AdapterAddedProducts.ProductTax;
import static com.lgt.paykredit.Adapter.AdapterAddedProducts.itemPrice;
import static com.lgt.paykredit.Adapter.AdapterAddedProducts.ItemDue;
import static com.lgt.paykredit.Adapter.AdapterAddedProducts.ItemDiscount;
import static com.lgt.paykredit.Adapter.ExistingCustomerAdapter.customer_id;
import static com.lgt.paykredit.Adapter.ExistingCustomerAdapter.customer_name;
import static com.lgt.paykredit.Adapter.ExistingCustomerAdapter.isCustomerAdded;
import static com.lgt.paykredit.extras.PayKreditAPI.CREATE_INVOICE_API;

public class CreateInvoice extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, LoadInvoiceData, GenerateCalculation {
    LinearLayout ll_invoice_date_picker, ll_due_date_picker;
    CircleImageView civ_add_new_customer, civ_add_product_service;
    ImageView iv_back_press, iv_add_bank_details, iv_deleteCustomer;
    TextView tv_due_date, tv_invoice_date, tv_invoice_id, save_invoice_btn;
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
    ArrayList<ProductModel> mList = new ArrayList<>();
    ProductAddAdapter productAddAdapter;
    public String termSelect = "", InvoiceUser = "", invoiceID = "", invoiceCustomerId = "", IfcsCode = "", AccountHolderName = "", AccountNumber = "", ExtraNote = "";
    public String FinalDiscount = "", FinalSubTotal = "", FinalAdvance = "", FinalBalance = "", SinglePrice = "";
    private SharedPreferences sharedPreferences;
    private String mUserID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_invoice);
        sharedPreferences = CreateInvoice.this.getSharedPreferences("USER_DATA", MODE_PRIVATE);
        if (sharedPreferences.contains("KEY_USER_ID")) {
            mUserID = sharedPreferences.getString("KEY_USER_ID", "");
        }
        if (mUserID != null) {
            if (!mUserID.equalsIgnoreCase("")) {
                initView();
            }
        }
    }

    private void initView() {
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
        myView.setVisibility(View.GONE);
        isUp = false;
        clickView();
        loadDueTerms();
        sp_due_terms.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                if (position == 0) {
                    Toast.makeText(CreateInvoice.this, "Select Due Term!", Toast.LENGTH_SHORT).show();
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
                Calendar now = Calendar.getInstance();
                DatePickerDialog dpd = DatePickerDialog.newInstance(
                        CreateInvoice.this,
                        now.get(Calendar.YEAR), // Initial year selection
                        now.get(Calendar.MONTH), // Initial month selection
                        now.get(Calendar.DAY_OF_MONTH) // Init day selection
                );
                dpd.setOnDateSetListener(new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
                        Log.d("invoice_date_picked", dayOfMonth + "-" + monthOfYear + "-" + year);
                        invoice_date_picker = dayOfMonth + "/" + monthOfYear + "/" + year;
                        tv_invoice_date.setText(invoice_date_picker);
                    }
                });
                dpd.setOkColor(Color.WHITE);
                dpd.setCancelColor(Color.WHITE);
                dpd.show(getSupportFragmentManager(), "invoice_date_picker");
            }
        });

        ll_due_date_picker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (invoice_date_picker.equalsIgnoreCase("")) {
                    Toast.makeText(CreateInvoice.this, "Select Invoice Date", Toast.LENGTH_SHORT).show();
                } else {
                    Calendar now = Calendar.getInstance();
                    DatePickerDialog dpd = DatePickerDialog.newInstance(
                            CreateInvoice.this,
                            now.get(Calendar.YEAR), // Initial year selection
                            now.get(Calendar.MONTH), // Initial month selection
                            now.get(Calendar.DAY_OF_MONTH) // Inital day selection
                    );
                    dpd.setOnDateSetListener(new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
                            Log.d("due_date_picked", dayOfMonth + "/" + monthOfYear + "/" + year);
                            due_date_picker = dayOfMonth + "/" + monthOfYear + "/" + year;
                            tv_due_date.setText(due_date_picker);
                        }
                    });
                    dpd.setOkColor(Color.WHITE);
                    dpd.setCancelColor(Color.WHITE);
                    dpd.show(getSupportFragmentManager(), "due_date_picker");
                }
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
                        startActivity(new Intent(CreateInvoice.this,InvoiceMainPage.class));
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
        if (AdapterAddedProducts.IsClickToAdd) {
            AdapterAddedProducts.IsClickToAdd = false;
            if (mList.size() >= 0) {
                ProductModel productModel = new ProductModel(ProductId, ProductName, ProductAmt, AdvanceAmt, ProductDis, ProductQua, ProductTax);
                productModel.setTotalAmt(Integer.parseInt(itemPrice));
                productModel.setBalanceDue(Integer.parseInt(ItemDue));
                productModel.setTotalDiscount(Integer.parseInt(ItemDiscount));
                mList.add(productModel);
                setUpAddProductView();
            } else {
                ProductModel productModel = new ProductModel(ProductId, ProductName, ProductAmt, AdvanceAmt, ProductDis, ProductQua, ProductTax);
                productModel.setTotalAmt(Integer.parseInt(itemPrice));
                productModel.setBalanceDue(Integer.parseInt(ItemDue));
                productModel.setTotalDiscount(Integer.parseInt(ItemDiscount));
                mList.add(productModel);
                productAddAdapter.notifyDataSetChanged();
            }
        } else if (isCustomerAdded) {
            isCustomerAdded = false;
            String cusName = customer_name;
            InvoiceUser = customer_name;
            invoiceCustomerId = customer_id;
            iv_deleteCustomer.setVisibility(View.VISIBLE);
            etSelectCustomer.setText(cusName);
        }
    }

    private void startCalculation() {
        int SingleProductPrice=0,productPrice = 0, productblncDue = 0, ProductDiscount = 0, ProductAdv = 0;
        if (mList.size() > 0) {
            for (int i = 0; i < mList.size(); i++) {
                mProductPriceSingle.add(mList.get(i).getProductAmt());
                mProductID.add(mList.get(i).getProductId());
                mProductDiscount.add(mList.get(i).getProductDis());
                mProductName.add(mList.get(i).getProductName());
                mProductQuantity.add(mList.get(i).getProductQua());
                if (!mList.get(i).getProductAdvance().equalsIgnoreCase("")) {
                    int adv = Integer.parseInt(mList.get(i).getProductAdvance());
                    if (adv > 0) {
                        ProductAdv += adv;
                    }
                }

                int amtdue = mList.get(i).getTotalAmt();
                int dsc = mList.get(i).getTotalDiscount();
                int blncDue = mList.get(i).getBalanceDue();
                if (amtdue > 0) {
                    productPrice += amtdue;
                }
                if (dsc > 0) {
                    ProductDiscount += dsc;
                }
                if (blncDue > 0) {
                    productblncDue += blncDue;
                }
            }
        } else {
            tv_subTotalPrice.setText("0");
            tv_DiscountInPrice.setText("0");
            tv_BalanceDue.setText("0");
            tv_AdvancePrice.setText("0");
        }
        setCalcDataToView(SingleProductPrice,productPrice, ProductDiscount, productblncDue, ProductAdv);
    }

    private void setCalcDataToView(int sproprice,int pprice, int pdiscunt, int pqty, int padvnc) {
        SinglePrice = String.valueOf(sproprice);
        FinalDiscount = String.valueOf(pqty);
        FinalAdvance = String.valueOf(padvnc);
        FinalBalance = String.valueOf(pdiscunt);
        FinalSubTotal = String.valueOf(pprice);
        //Log.d("listData:", "Final Price = | " + pprice + " | " + pdiscunt + " | " + pqty + " | " + padvnc);
        tv_subTotalPrice.setText(pprice + ".00");
        tv_DiscountInPrice.setText(pqty + ".00");
        tv_BalanceDue.setText(pdiscunt + ".00");
        tv_AdvancePrice.setText(padvnc + ".00");
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
                if (invoice_date_picker.equalsIgnoreCase("")) {
                    Toast.makeText(CreateInvoice.this, "Select Invoice Date", Toast.LENGTH_SHORT).show();
                } else {
                    if (due_date_picker.equalsIgnoreCase("")) {
                        Toast.makeText(CreateInvoice.this, "Select Due Date", Toast.LENGTH_SHORT).show();
                    } else {
                        startActivity(new Intent(CreateInvoice.this, AddNewCustomer.class));
                    }
                }
            }
        });

        civ_add_product_service.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (InvoiceUser.equalsIgnoreCase("")) {
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
                }
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
        Toast.makeText(this, dayOfMonth + "-" + monthOfYear + "-" + year, Toast.LENGTH_SHORT).show();
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
