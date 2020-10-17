package com.lgt.paykredit.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lgt.paykredit.R;
import com.lgt.paykredit.bottomsheets.BottomSheetCall;

public class ActivityInvoiceDescription extends AppCompatActivity {

    private TextView tvToolbarTitle;
    private LinearLayout llCallInvoiceDescription;
    private ImageView ivBackSingleUserTransaction;

  private LinearLayout llDateInvoice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invoice_description);

        tvToolbarTitle = findViewById(R.id.tvToolbarTitle);
        llCallInvoiceDescription = findViewById(R.id.llCallInvoiceDescription);
        ivBackSingleUserTransaction = findViewById(R.id.ivBackSingleUserTransaction);
        llDateInvoice = findViewById(R.id.llDateInvoice);

        ivBackSingleUserTransaction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        tvToolbarTitle.setText("Invoice Detail");

        llCallInvoiceDescription.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BottomSheetCall bottomSheetCall = new BottomSheetCall();
                bottomSheetCall.show(getSupportFragmentManager(), "BottomSheetCall");
            }
        });
    }
}
