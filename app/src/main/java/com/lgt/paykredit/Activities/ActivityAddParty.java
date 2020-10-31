package com.lgt.paykredit.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.lgt.paykredit.R;

public class ActivityAddParty extends AppCompatActivity {

    private TextView tvToolbarTitle;
    private ImageView ivBackSingleUserTransaction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_party);
        ivBackSingleUserTransaction = findViewById(R.id.ivBackSingleUserTransaction);

        tvToolbarTitle = findViewById(R.id.tvToolbarTitle);
        tvToolbarTitle.setText("Add Party");

        ivBackSingleUserTransaction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }
}
