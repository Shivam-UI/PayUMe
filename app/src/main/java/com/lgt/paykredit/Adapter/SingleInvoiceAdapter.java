package com.lgt.paykredit.Adapter;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.lgt.paykredit.Activities.ActivityInvoiceDescription;
import com.lgt.paykredit.Models.ModelInvoiceDetails;
import com.lgt.paykredit.R;
import com.lgt.paykredit.bottomsheets.BottomSheetCall;
import com.lgt.paykredit.bottomsheets.BottomSheetSendReminder;
import com.lgt.paykredit.extras.InvoiceDetailsClick;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.util.ArrayList;
import java.util.Calendar;

public class SingleInvoiceAdapter extends RecyclerView.Adapter<SingleInvoiceAdapter.SingleInvoiceholder> implements DatePickerDialog.OnDateSetListener {

    Context mContext;
    ArrayList<ModelInvoiceDetails> mList;
    InvoiceDetailsClick mInvoiceDetailsClick;
    public static String SelectedDate = "";

    public SingleInvoiceAdapter(Context mContext, ArrayList<ModelInvoiceDetails> mList, InvoiceDetailsClick DetailsClick) {
        this.mContext = mContext;
        this.mList = mList;
        mInvoiceDetailsClick = DetailsClick;
    }

    @NonNull
    @Override
    public SingleInvoiceholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View mView = LayoutInflater.from(mContext).inflate(R.layout.single_user_invoice, parent, false);
        return new SingleInvoiceholder(mView);
    }

    @Override
    public void onBindViewHolder(@NonNull SingleInvoiceholder holder, int position) {
        if (mList.get(position).getType().equalsIgnoreCase("unpaid")) {
            holder.ll_SetToDefault.setVisibility(View.VISIBLE);
        } else if (mList.get(position).getType().equalsIgnoreCase("paid")) {
            holder.ll_SetToDefault.setVisibility(View.GONE);
        }
        holder.tv_callNotification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BottomSheetCall bottomSheetCall = new BottomSheetCall();
                Bundle bundle = new Bundle();
                bundle.putString("KEY_CUSTOMER_NUMBER", mList.get(position).getCustomer_mobile());
                bundle.putString("KEY_CUSTOMER_NAME", mList.get(position).getCustomer_name());
                bottomSheetCall.setArguments(bundle);
                bottomSheetCall.show(((AppCompatActivity) mContext).getSupportFragmentManager(), "BottomSheetCall");
               /* Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:" + mList.get(position).getCustomer_mobile()));
                if (ActivityCompat.checkSelfPermission(mContext,
                        Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                mContext.startActivity(callIntent);*/
            }
        });
        holder.tv_ReminderNotification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BottomSheetSendReminder bottomSheetSendReminder = new BottomSheetSendReminder();
                Bundle bundle = new Bundle();
                bundle.putString("KEY_CUSTOMER_NUMBER", mList.get(position).getCustomer_mobile());
                bundle.putString("KEY_CUSTOMER_NAME", mList.get(position).getCustomer_name());
                bundle.putFloat("KEY_DUE_AMOUNT", Float.parseFloat(mList.get(position).getTotal_balance()));
                bottomSheetSendReminder.setArguments(bundle);
                bottomSheetSendReminder.show(((AppCompatActivity) mContext).getSupportFragmentManager(), "BottomSheetSendReminder");
            }
        });
        holder.tv_InvoiceIdDue.setText(mList.get(position).getInvoice_no());
        holder.tv_invoiceDueAmt.setText(mList.get(position).getTotal_balance());
        holder.iv_InvoiceDueDate.setText(mList.get(position).getDue_date());
        holder.ll_ChangeDueDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar now = Calendar.getInstance();
                DatePickerDialog dpd = DatePickerDialog.newInstance(
                        SingleInvoiceAdapter.this,
                        now.get(Calendar.YEAR), // Initial year selection
                        now.get(Calendar.MONTH), // Initial month selection
                        now.get(Calendar.DAY_OF_MONTH) // Init day selection
                );
                dpd.setOnDateSetListener(new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
                        Log.d("invoice_date_picked", dayOfMonth + "-" + monthOfYear + "-" + year);
                        SelectedDate = dayOfMonth + "/" + monthOfYear + "/" + year;
                        holder.iv_InvoiceDueDate.setText(SelectedDate);
                        mInvoiceDetailsClick.changeDate(mList.get(position).getTbl_invoice_id());
                    }
                });
                dpd.setOkColor(Color.WHITE);
                dpd.setCancelColor(Color.WHITE);
                dpd.show(((AppCompatActivity) mContext).getSupportFragmentManager(), "invoice_date_picker");
            }
        });

        holder.ll_PaymentStatusUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mInvoiceDetailsClick.payPayment(mList.get(position).getTbl_invoice_id(), mList.get(position).getInvoice_no());
            }
        });

        holder.ll_preview_invoice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mInvoiceDetailsClick.showPreview(mList.get(position).getInvoice_no());
            }
        });

        holder.ll_SetToDefault.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mInvoiceDetailsClick.setDetauld(mList.get(position).getInvoice_customer_id());
            }
        });

        holder.tv_ShareNotification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mInvoiceDetailsClick.startShareData(mList.get(position).getInvoice_no());
            }
        });
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {

    }

    public class SingleInvoiceholder extends RecyclerView.ViewHolder {
        TextView tv_InvoiceIdDue, tv_invoiceDueAmt, iv_InvoiceDueDate, tv_ReminderNotification, tv_callNotification,tv_ShareNotification;
        LinearLayout ll_ChangeDueDate, ll_preview_invoice, ll_PaymentStatusUpdate, ll_SetToDefault;

        public SingleInvoiceholder(@NonNull View itemView) {
            super(itemView);
            tv_InvoiceIdDue = itemView.findViewById(R.id.tv_InvoiceIdDue);
            tv_invoiceDueAmt = itemView.findViewById(R.id.tv_invoiceDueAmt);
            iv_InvoiceDueDate = itemView.findViewById(R.id.iv_InvoiceDueDate);
            ll_ChangeDueDate = itemView.findViewById(R.id.ll_ChangeDueDate);
            ll_preview_invoice = itemView.findViewById(R.id.ll_preview_invoice);
            ll_PaymentStatusUpdate = itemView.findViewById(R.id.ll_PaymentStatusUpdate);
            tv_ReminderNotification = itemView.findViewById(R.id.tv_ReminderNotification);
            tv_callNotification = itemView.findViewById(R.id.tv_callNotification);
            ll_SetToDefault = itemView.findViewById(R.id.ll_SetToDefault);
            tv_ShareNotification = itemView.findViewById(R.id.tv_ShareNotification);
        }
    }
}
