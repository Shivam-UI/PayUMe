package com.lgt.paykredit.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
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
import com.google.android.material.tabs.TabLayout;
import com.lgt.paykredit.Adapter.AdapterAddedProducts;
import com.lgt.paykredit.Adapter.AddCustomerAdapter;
import com.lgt.paykredit.Fragments.AddNewProduct;
import com.lgt.paykredit.Fragments.CreateCusFragment;
import com.lgt.paykredit.Fragments.ExistingCusFragment;
import com.lgt.paykredit.Fragments.ExistingProduct;
import com.lgt.paykredit.Models.ModelAddedProducts;
import com.lgt.paykredit.R;
import com.lgt.paykredit.bottomsheets.BottomSheetAddItems;
import com.lgt.paykredit.extras.PayKreditAPI;
import com.lgt.paykredit.extras.SingletonRequestQueue;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ActivityAddedProducts extends AppCompatActivity {
    private TabLayout tab_layoutInvoiceMainPage;
    ImageView iv_back_press;
    public static ViewPager vp_add_Product_tab;
    AddCustomerAdapter addCustomerAdapter;
    TabLayout tab_layoutAddCustomer;
    public static String Tbl_Customer_id = "";
    public static int Position = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_added_products);
        initView();
    }

    private void initView() {
        iv_back_press = findViewById(R.id.iv_back_press);
        vp_add_Product_tab = findViewById(R.id.vp_add_customer_tab);
        tab_layoutAddCustomer = findViewById(R.id.tab_layoutAddCustomer);
        tab_layoutInvoiceMainPage = findViewById(R.id.tab_layoutInvoiceMainPage);
        addCustomerAdapter = new AddCustomerAdapter(getSupportFragmentManager());
        addCustomerAdapter.addFragment(new AddNewProduct(), "Add Product");
        addCustomerAdapter.addFragment(new ExistingProduct(), "Existing Product");
        vp_add_Product_tab.setAdapter(addCustomerAdapter);
        tab_layoutAddCustomer.setupWithViewPager(vp_add_Product_tab);
        clickView();
    }

    private void clickView() {
        iv_back_press.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        /*tab_layoutAddCustomer.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab.getPosition() == 0) {
                    Log.d("onSelection", "0");
                }
                if (tab.getPosition() == 1) {
                    Log.d("onSelection", "1");
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });*/
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
