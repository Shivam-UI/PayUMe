package com.lgt.paykredit.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lgt.paykredit.R;
import com.lgt.paykredit.bottomsheets.BottomSheetAddCustomerDetails;
import com.lgt.paykredit.bottomsheets.BottomSheetAddItems;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class ActivityInvoice extends AppCompatActivity {

    private TextView tvAddMoreDetails, tvInvoiceDate, tvDueDate;
    private LinearLayout llAddItems, llInvoiceDate, llDueDate;
    private Calendar myCalendar;

    private ImageView ivBackSingleUserTransaction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invoice);

        tvAddMoreDetails = findViewById(R.id.tvAddMoreDetails);
        llAddItems = findViewById(R.id.llAddItems);
        ivBackSingleUserTransaction = findViewById(R.id.ivBackSingleUserTransaction);

        llInvoiceDate = findViewById(R.id.llInvoiceDate);
        llDueDate = findViewById(R.id.llDueDate);
        llDueDate = findViewById(R.id.llDueDate);
        tvInvoiceDate = findViewById(R.id.tvInvoiceDate);
        tvDueDate = findViewById(R.id.tvDueDate);

        myCalendar = Calendar.getInstance();

        ivBackSingleUserTransaction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
                hideKeyBoard();
            }
        });

        llAddItems.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BottomSheetAddItems bottomSheetAddItems = new BottomSheetAddItems();
                bottomSheetAddItems.show(getSupportFragmentManager(), "BottomSheetAddItems");
            }
        });

        tvAddMoreDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                BottomSheetAddCustomerDetails bottomSheetAddCustomerDetails = new BottomSheetAddCustomerDetails();
                bottomSheetAddCustomerDetails.show(getSupportFragmentManager(), "BottomSheetAddCustomerDetails");

            }
        });

        llInvoiceDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openCalendarInvoiceDate("Invoice_Date");
            }
        });

        llDueDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openCalendarInvoiceDate("Due_Date");
            }
        });

        hideKeyBoard();
    }

    private void hideKeyBoard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }


    private void openCalendarInvoiceDate(final String type) {

        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel(type);
            }

        };

        new DatePickerDialog(ActivityInvoice.this, date, myCalendar
                .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH)).show();
    }

    private void updateLabel(String type) {
        String myFormat = "dd/MMM/yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        if (type.equalsIgnoreCase("Invoice_Date")) {
            tvInvoiceDate.setText(sdf.format(myCalendar.getTime()));
        } else {
            tvDueDate.setText(sdf.format(myCalendar.getTime()));
            tvDueDate.setTextColor(getResources().getColor(R.color.red));
        }

    }
}
