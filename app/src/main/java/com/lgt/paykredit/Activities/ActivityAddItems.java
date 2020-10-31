package com.lgt.paykredit.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.lgt.paykredit.R;

public class ActivityAddItems extends AppCompatActivity {

    private TextView tvToolbarTitle;
    private ImageView ivBackSingleUserTransaction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_items);

        tvToolbarTitle = findViewById(R.id.tvToolbarTitle);
        ivBackSingleUserTransaction = findViewById(R.id.ivBackSingleUserTransaction);

        tvToolbarTitle.setText("Add Item");

        ivBackSingleUserTransaction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }
}
