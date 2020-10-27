package com.lgt.paykredit.Activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;
import com.lgt.paykredit.Adapter.PagerAdapterInvoiceMain;
import com.lgt.paykredit.R;

public class InvoiceMainPage extends AppCompatActivity {

    private static final String TAG = "InvoiceMainPage";

    private TabLayout tab_layoutInvoiceMainPage;
    private TabItem tab_MerchantsInvoiceAll, tab_InvoicesPaid, tab_InvoicesOutStanding;
    private PagerAdapterInvoiceMain adapterInvoiceMain;

    private ImageView ivInvoiceMainPage,ivFilter;

    private EditText etSearchOrFilter;
    private LinearLayout llTabLayoutParent;

    private TextView rlAddInvoiceMainPage;
    public static TextView tvTotalSale,tvTotalDueAmt;
    ViewPager viewPagerInvoiceMainPage;

    private int currentFragmentPosition = 0;
    private CharSequence mCharSequence;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invoice_main_page);

        tab_layoutInvoiceMainPage = findViewById(R.id.tab_layoutInvoiceMainPage);
        etSearchOrFilter = findViewById(R.id.etSearchOrFilter);

        rlAddInvoiceMainPage = findViewById(R.id.rlAddInvoiceMainPage);
        ivInvoiceMainPage = findViewById(R.id.ivInvoiceMainPage);

        tvTotalSale = findViewById(R.id.tvTotalSale);
        tvTotalDueAmt = findViewById(R.id.tvTotalDueAmt);

        tab_MerchantsInvoiceAll = findViewById(R.id.tab_MerchantsInvoiceAll);
        tab_InvoicesPaid = findViewById(R.id.tab_InvoicesPaid);
        tab_InvoicesOutStanding = findViewById(R.id.tab_InvoicesOutStanding);

        viewPagerInvoiceMainPage = findViewById(R.id.viewPagerInvoiceMainPage);

        llTabLayoutParent = findViewById(R.id.llTabLayoutParent);
        ivFilter = findViewById(R.id.ivFilter);


        ivInvoiceMainPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        ivFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openPopUpMenu();
            }
        });

        adapterInvoiceMain = new PagerAdapterInvoiceMain(getSupportFragmentManager(), tab_layoutInvoiceMainPage.getTabCount());
        viewPagerInvoiceMainPage.setAdapter(adapterInvoiceMain);

        viewPagerInvoiceMainPage.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tab_layoutInvoiceMainPage));

        rlAddInvoiceMainPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(InvoiceMainPage.this, CreateInvoice.class));
            }
        });

        Log.e("dasdasdadasdasd", viewPagerInvoiceMainPage.getCurrentItem() + "");

        etSearchOrFilter.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mCharSequence = s;
                startFilter(s);
                if (s.length() > 0) {
                    llTabLayoutParent.setVisibility(View.GONE);
                    rlAddInvoiceMainPage.setVisibility(View.GONE);

                } else {
                    llTabLayoutParent.setVisibility(View.VISIBLE);
                    rlAddInvoiceMainPage.setVisibility(View.VISIBLE);
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        tab_layoutInvoiceMainPage.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPagerInvoiceMainPage.setCurrentItem(tab.getPosition());
                currentFragmentPosition = viewPagerInvoiceMainPage.getCurrentItem();
                //scrolls
                //whenever scrolls
                //clear editttex -> show tablyout -->show create invice
                etSearchOrFilter.setText("");
                llTabLayoutParent.setVisibility(View.VISIBLE);
                rlAddInvoiceMainPage.setVisibility(View.VISIBLE);

                // hideKeyBoard();


            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });


    }

    private void openPopUpMenu() {
        //
    }

    private void hideKeyBoard() {
        View view = this.getCurrentFocus();
        InputMethodManager imm = (InputMethodManager) this.getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    private void startFilter(CharSequence s) {
        if (currentFragmentPosition == 0) {
            Intent intent = new Intent("FILTER_EVENT");
            intent.putExtra("INPUT_DATA", s);
            Log.e(TAG, "startFilter: " + s + "");
            LocalBroadcastManager.getInstance(InvoiceMainPage.this).sendBroadcast(intent);
        }

        if(currentFragmentPosition == 1){
            Intent intent = new Intent("FILTER_EVENT1");
            intent.putExtra("INPUT_DATA1", s);
            Log.e(TAG, "startFilter1: " + s + "");
            LocalBroadcastManager.getInstance(InvoiceMainPage.this).sendBroadcast(intent);
        }

        if(currentFragmentPosition == 2){
            Intent intent = new Intent("FILTER_EVENT2");
            intent.putExtra("INPUT_DATA2", s);
            Log.e(TAG, "startFilter2: " + s + "");
            LocalBroadcastManager.getInstance(InvoiceMainPage.this).sendBroadcast(intent);
        }
    }

    @Override
    public void onBackPressed() {

        if(mCharSequence != null){
            if (mCharSequence.length() > 0) {
                etSearchOrFilter.setText("");
            } else {
                super.onBackPressed();
            }
        }
        else {
            super.onBackPressed();
        }

    }
}
