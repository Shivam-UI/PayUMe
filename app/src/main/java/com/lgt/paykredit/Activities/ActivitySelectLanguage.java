package com.lgt.paykredit.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lgt.paykredit.Adapter.AdapterSelectLanguage;
import com.lgt.paykredit.Models.ModelSelectLanguage;
import com.lgt.paykredit.R;
import com.lgt.paykredit.extras.Common;

import java.util.ArrayList;
import java.util.List;

public class ActivitySelectLanguage extends AppCompatActivity {

    private RecyclerView rvSelectLanguage;
    private AdapterSelectLanguage adapterSelectLanguage;
    private List<ModelSelectLanguage> listLanguage;

    private LinearLayout llNext;

    private TextView tvNext;
    private String from_come;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_language);

        rvSelectLanguage = findViewById(R.id.rvSelectLanguage);
        llNext = findViewById(R.id.llNext);
        tvNext = findViewById(R.id.tvNext);

        if (getIntent().hasExtra(Common.FROM_MAIN)){
            from_come=Common.FROM_MAIN;
        }else {
            from_come=Common.FROM_STARTING;
        }
        tvNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                sendUserForLoginOrRegister();
            }
        });

        loadRecyclerView();

    }

    private void sendUserForLoginOrRegister() {

       /* BottomSheetLoginOrRegister bottomSheetLoginOrRegister = new BottomSheetLoginOrRegister();
        bottomSheetLoginOrRegister.show(getSupportFragmentManager(),"BottomSheetLoginOrRegister");
*/
       if (from_come.equalsIgnoreCase(Common.FROM_MAIN)){
           startActivity(new Intent(ActivitySelectLanguage.this,MainActivity.class));
           finishAffinity();
       }else {
           startActivity(new Intent(ActivitySelectLanguage.this,ActivityLogin.class));
           finishAffinity();
       }


    }

    private void loadRecyclerView() {

        listLanguage = new ArrayList<>();
        listLanguage.clear();


        listLanguage.add(new ModelSelectLanguage("1","English",R.color.red));
        listLanguage.add(new ModelSelectLanguage("2","Hindi",R.color.blue));


        adapterSelectLanguage = new AdapterSelectLanguage(listLanguage,ActivitySelectLanguage.this);
        rvSelectLanguage.hasFixedSize();
        rvSelectLanguage.setNestedScrollingEnabled(false);
        rvSelectLanguage.setLayoutManager(new GridLayoutManager(ActivitySelectLanguage.this, 2));
        rvSelectLanguage.setAdapter(adapterSelectLanguage);
        adapterSelectLanguage.notifyDataSetChanged();

    }
}
