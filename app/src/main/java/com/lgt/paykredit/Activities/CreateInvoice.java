package com.lgt.paykredit.Activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
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

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.lgt.paykredit.Adapter.AdapterAddedProducts;
import com.lgt.paykredit.Adapter.ProductAddAdapter;
import com.lgt.paykredit.Models.ProductModel;
import com.lgt.paykredit.R;
import com.lgt.paykredit.bottomsheets.BottomSheetAddItems;
import com.lgt.paykredit.extras.LoadInvoiceData;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Random;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.lgt.paykredit.Adapter.AdapterAddedProducts.ProductAmt;
import static com.lgt.paykredit.Adapter.AdapterAddedProducts.ProductDis;
import static com.lgt.paykredit.Adapter.AdapterAddedProducts.ProductId;
import static com.lgt.paykredit.Adapter.AdapterAddedProducts.ProductName;
import static com.lgt.paykredit.Adapter.AdapterAddedProducts.ProductQua;
import static com.lgt.paykredit.Adapter.AdapterAddedProducts.ProductTax;
import static com.lgt.paykredit.Adapter.ExistingCustomerAdapter.customer_id;
import static com.lgt.paykredit.Adapter.ExistingCustomerAdapter.customer_name;
import static com.lgt.paykredit.Adapter.ExistingCustomerAdapter.isCustomerAdded;
import static com.lgt.paykredit.Adapter.ProductAddAdapter.TotalAmt;

public class CreateInvoice extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, LoadInvoiceData {
    LinearLayout ll_invoice_date_picker, ll_due_date_picker;
    CircleImageView civ_add_new_customer, civ_add_product_service;
    ImageView iv_back_press, iv_add_bank_details, iv_deleteCustomer;
    TextView tv_due_date, tv_invoice_date, tv_invoice_id;
    public static TextView tv_DiscountInPrice, tv_subTotalPrice, tv_AdvancePrice, tv_BalanceDue;
    EditText etSelectCustomer;
    Spinner sp_due_terms;
    RecyclerView rvProductItemList;
    private String due_date_picker, invoice_date_picker;
    View myView;
    boolean isUp;
    Intent intent = null;
    ArrayList<String> mDueTermList = new ArrayList<>();
    ArrayList<ProductModel> mList = new ArrayList<>();
    ProductAddAdapter productAddAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_invoice);
        initView();
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
        myView = findViewById(R.id.my_view);
        myView.setVisibility(View.GONE);
        isUp = false;
        clickView();
        loadDueTerms();
        sp_due_terms.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                Log.d("select_term", adapterView.getItemAtPosition(position).toString());
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
        tv_invoice_id.setText("INSTA" + getRandomNumberString());

        iv_deleteCustomer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                etSelectCustomer.setText("");
                iv_deleteCustomer.setVisibility(View.GONE);
                Toast.makeText(CreateInvoice.this, "Cancel Current Customer", Toast.LENGTH_SHORT).show();
            }
        });
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
                Log.d("listData", ProductId + " | " + ProductName + " | " + ProductAmt + " | " + ProductDis + " | " + ProductQua + " | " + ProductTax);
                mList.add(new ProductModel(ProductId, ProductName, ProductAmt, ProductDis, ProductQua, ProductTax));
                setUpAddProductView();
            } else {
                Log.d("listData", ProductId + " | " + ProductName + " | " + ProductAmt + " | " + ProductDis + " | " + ProductQua + " | " + ProductTax);
                mList.add(new ProductModel(ProductId, ProductName, ProductAmt, ProductDis, ProductQua, ProductTax));
                productAddAdapter.notifyDataSetChanged();
            }
        } else if (isCustomerAdded) {
            isCustomerAdded = false;
            String cusName = customer_name;
            String custId = customer_id;
            iv_deleteCustomer.setVisibility(View.VISIBLE);
            etSelectCustomer.setText(cusName);
        }
        startCalculation();
    }

    private void startCalculation() {
        if (mList.size() > 0) {
            int res=0;
            Log.d("listData", "Amt : " + mList.size());
            for (int i = 0; i < mList.size(); i++) {
                int amt = Integer.parseInt(mList.get(i).getProductAmt());
                if (amt>0){
                    res += amt;
                }
            }
            Log.d("listData", "Amt : " + res);
            tv_subTotalPrice.setText(String.valueOf(res)+".00");
        } else {
            Log.d("listData", "Size : " + mList.size());
        }
    }

    private void setUpAddProductView() {
        productAddAdapter = new ProductAddAdapter(mList, getApplicationContext());
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
            }
        });

        civ_add_product_service.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(CreateInvoice.this, ActivityAddedProducts.class));
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
}
