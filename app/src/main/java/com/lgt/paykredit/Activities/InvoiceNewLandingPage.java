package com.lgt.paykredit.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lgt.paykredit.R;

public class InvoiceNewLandingPage extends AppCompatActivity {

    private LinearLayout llInvoiceNewLandingPage,llProductNewLandingPage,llCustomerNewLandingPage,llProfile;
    private TextView tvToolbarTitle;
    private ImageView ivBackSingleUserTransaction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invoice_new_landing_page);

        llInvoiceNewLandingPage = findViewById(R.id.llInvoiceNewLandingPage);
        llProductNewLandingPage = findViewById(R.id.llProductNewLandingPage);
        llCustomerNewLandingPage = findViewById(R.id.llCustomerNewLandingPage);

        ivBackSingleUserTransaction = findViewById(R.id.ivBackSingleUserTransaction);
        tvToolbarTitle = findViewById(R.id.tvToolbarTitle);


        llProfile = findViewById(R.id.llProfile);

        tvToolbarTitle.setText("Invoice Dashboard");

        ivBackSingleUserTransaction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        llInvoiceNewLandingPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(InvoiceNewLandingPage.this,InvoiceMainPage.class));
            }
        });

        llProductNewLandingPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(InvoiceNewLandingPage.this,ActivityAddedProducts.class));
            }
        });

        llCustomerNewLandingPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(InvoiceNewLandingPage.this,ActivityAddedCustomers.class));
            }
        });

        llProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(InvoiceNewLandingPage.this,ActivityProfile.class));
            }
        });
    }
}
