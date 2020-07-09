package com.lgt.paykredit.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;

import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;
import com.lgt.paykredit.Adapter.PagerAdapterPaidUnpaid;
import com.lgt.paykredit.R;

public class ActivityPaidUnpaid extends AppCompatActivity {

    private TabLayout tab_layoutPaidUnpaid;
    private TabItem tab_Paid, tab_unPaid;
    private PagerAdapterPaidUnpaid adapterPaidUnpaid;
    ViewPager viewPagerPaidUnpaid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paid_unpaid);

        tab_layoutPaidUnpaid = findViewById(R.id.tab_layoutPaidUnpaid);
        tab_Paid = findViewById(R.id.tab_Paid);
        tab_unPaid = findViewById(R.id.tab_unPaid);
        viewPagerPaidUnpaid = findViewById(R.id.viewPagerPaidUnpaid);

        adapterPaidUnpaid = new PagerAdapterPaidUnpaid(getSupportFragmentManager(), tab_layoutPaidUnpaid.getTabCount());
        viewPagerPaidUnpaid.setAdapter(adapterPaidUnpaid);

        viewPagerPaidUnpaid.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tab_layoutPaidUnpaid));

        tab_layoutPaidUnpaid.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

                viewPagerPaidUnpaid.setCurrentItem(tab.getPosition());

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });


    }
}
