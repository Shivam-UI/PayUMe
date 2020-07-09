package com.lgt.paykredit.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lgt.paykredit.Adapter.AdapterInvoiceDescription;
import com.lgt.paykredit.Models.ModelInvoiceItems;
import com.lgt.paykredit.R;
import com.lgt.paykredit.bottomsheets.BottomSheetAddCustomerDetails;
import com.lgt.paykredit.bottomsheets.BottomSheetAddItems;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ActivityCreateInvoice extends AppCompatActivity implements AdapterInvoiceDescription.clickHandler{

    private RecyclerView rvInvoiceItems;
    private List<ModelInvoiceItems> list;
    private AdapterInvoiceDescription adapterInvoiceDescription;

    private TextView tvDueDateCreateInvoice,tvInvoiceDateCreateInvoice;

    private LinearLayout llAddCustomerBox;

    private Calendar myCalendar;

    // Animation
    Animation animBlink;

    private boolean blinkDueDate,blinkDateOfInvoice,blinkAddDescription = false;

    private LinearLayout llBillTo,llDueDateCreateInvoice,llDateInvoice;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_invoice);

        tvDueDateCreateInvoice = findViewById(R.id.tvDueDateCreateInvoice);
        tvInvoiceDateCreateInvoice = findViewById(R.id.tvInvoiceDateCreateInvoice);

        llAddCustomerBox = findViewById(R.id.llAddCustomerBox);

        llDateInvoice = findViewById(R.id.llDateInvoice);

        myCalendar = Calendar.getInstance();

        rvInvoiceItems = findViewById(R.id.rvInvoiceItems);
        llBillTo = findViewById(R.id.llBillTo);
        llDueDateCreateInvoice = findViewById(R.id.llDueDateCreateInvoice);

        animBlink = AnimationUtils.loadAnimation(ActivityCreateInvoice.this,
                R.anim.blink_animation);

        llAddCustomerBox.startAnimation(animBlink);

        getCurrentDate();



        llDueDateCreateInvoice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                llDueDateCreateInvoice.clearAnimation();
                rvInvoiceItems.startAnimation(animBlink);
                openCalendarDueDate("Due_Date");

            }
        });

        llDateInvoice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openCalendarDueDate("Invoice_Date");
            }
        });

        llBillTo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BottomSheetAddCustomerDetails bottomSheetAddCustomerDetails = new BottomSheetAddCustomerDetails();
                bottomSheetAddCustomerDetails.show(getSupportFragmentManager(), "BottomSheetAddCustomerDetails");

                blinkDueDate = true;
                llAddCustomerBox.clearAnimation();

                blinkDueDate();
            }
        });

        loadRecyclerView();
    }

    private void blinkDueDate() {
        if(blinkDueDate){
            llDueDateCreateInvoice.startAnimation(animBlink);
        }
    }

    private void getCurrentDate() {
        String date = new SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault()).format(new Date());
        tvInvoiceDateCreateInvoice.setText(date);

    }

    private void openCalendarDueDate(final String type) {

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

        new DatePickerDialog(ActivityCreateInvoice.this, date, myCalendar
                .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH)).show();
    }

    private void updateLabel(String type) {
        String myFormat = "dd/MMM/yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        if (type.equalsIgnoreCase("Invoice_Date")) {
            tvInvoiceDateCreateInvoice.setText(sdf.format(myCalendar.getTime()));
        } else {
            tvDueDateCreateInvoice.setText(sdf.format(myCalendar.getTime()));
            tvDueDateCreateInvoice.setTextColor(getResources().getColor(R.color.red));
        }

    }


    private void loadRecyclerView() {
        list = new ArrayList<>();
        list.clear();


        list.add(new ModelInvoiceItems("Test item one","100","5"));
        list.add(new ModelInvoiceItems("Test item two","200","6"));
        list.add(new ModelInvoiceItems("Test item three","300","8"));
        list.add(new ModelInvoiceItems("Test item four","500","7"));
        list.add(new ModelInvoiceItems("Test item five","600","2"));



        rvInvoiceItems.setLayoutManager(new LinearLayoutManager(ActivityCreateInvoice.this,RecyclerView.VERTICAL,false));
        rvInvoiceItems.hasFixedSize();
        rvInvoiceItems.setNestedScrollingEnabled(false);
        adapterInvoiceDescription = new AdapterInvoiceDescription(list,ActivityCreateInvoice.this,this);
        rvInvoiceItems.setAdapter(adapterInvoiceDescription);
    }

    @Override
    public void clickedPosition(int adapterPosition) {
        if(adapterPosition >= 0){
            BottomSheetAddItems bottomSheetAddItems = new BottomSheetAddItems();
            bottomSheetAddItems.show(getSupportFragmentManager(), "BottomSheetAddItems");
        }
    }
}
