package com.lgt.paykredit.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.tabs.TabLayout;
import com.lgt.paykredit.Adapter.AddCustomerAdapter;
import com.lgt.paykredit.Adapter.ExistingCustomerAdapter;
import com.lgt.paykredit.Fragments.CreateCusFragment;
import com.lgt.paykredit.Fragments.ExistingCusFragment;
import com.lgt.paykredit.R;


public class AddNewCustomer extends AppCompatActivity implements ExistingCusFragment.EditRequestClick{
    private TabLayout tab_layoutInvoiceMainPage;
    ImageView iv_back_press;
    public static ViewPager vp_add_customer_tab;
    AddCustomerAdapter addCustomerAdapter;
    TabLayout tab_layoutAddCustomer;
    public static String Tbl_Customer_id = "";
    public static int Position = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_customer);
        initView();
    }

    private void initView() {
        iv_back_press = findViewById(R.id.iv_back_press);
        vp_add_customer_tab = findViewById(R.id.vp_add_customer_tab);
        tab_layoutAddCustomer = findViewById(R.id.tab_layoutAddCustomer);
        tab_layoutInvoiceMainPage = findViewById(R.id.tab_layoutInvoiceMainPage);
        addCustomerAdapter = new AddCustomerAdapter(getSupportFragmentManager());

        addCustomerAdapter.addFragment(new CreateCusFragment(), "Add Customer");
        addCustomerAdapter.addFragment(new ExistingCusFragment(), "Existing Customer");
        vp_add_customer_tab.setAdapter(addCustomerAdapter);
        tab_layoutAddCustomer.setupWithViewPager(vp_add_customer_tab);
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
    public void editClickFoundRequest(String tbl_id, int pos) {
        Tbl_Customer_id=tbl_id;Position=pos;
        Log.d("onClickkk:", tbl_id + "");
        vp_add_customer_tab.setCurrentItem(pos);
        vp_add_customer_tab.setFocusable(true);
    }
}