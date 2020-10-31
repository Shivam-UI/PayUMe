package com.lgt.paykredit.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.lgt.paykredit.Activities.ActivityInvoiceDescription;
import com.lgt.paykredit.Models.ModelInvoiceDetails;
import com.lgt.paykredit.R;
import com.lgt.paykredit.extras.InvoiceDetailsClick;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.util.ArrayList;
import java.util.Calendar;

public class SingleInvoiceAdapter extends RecyclerView.Adapter<SingleInvoiceAdapter.SingleInvoiceholder>  implements DatePickerDialog.OnDateSetListener {

    Context mContext;
    ArrayList<ModelInvoiceDetails> mList;
    InvoiceDetailsClick mInvoiceDetailsClick;
    public static String SelectedDate = "";

    public SingleInvoiceAdapter(Context mContext, ArrayList<ModelInvoiceDetails> mList,InvoiceDetailsClick DetailsClick) {
        this.mContext = mContext;
        this.mList = mList;
        mInvoiceDetailsClick=DetailsClick;
    }

    @NonNull
    @Override
    public SingleInvoiceholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View mView = LayoutInflater.from(mContext).inflate(R.layout.single_user_invoice,parent,false);
        return new SingleInvoiceholder(mView);
    }

    @Override
    public void onBindViewHolder(@NonNull SingleInvoiceholder holder, int position) {
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
                mInvoiceDetailsClick.payPayment(mList.get(position).getTbl_invoice_id(),mList.get(position).getInvoice_no());
            }
        });

        holder.ll_preview_invoice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mInvoiceDetailsClick.showPreview(mList.get(position).getTbl_invoice_id());
            }
        });

        holder.ll_SetToDefault.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mInvoiceDetailsClick.setDetauld(mList.get(position).getInvoice_customer_id());
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

    public class SingleInvoiceholder extends RecyclerView.ViewHolder{
        TextView tv_InvoiceIdDue,tv_invoiceDueAmt,iv_InvoiceDueDate;
        LinearLayout ll_ChangeDueDate,ll_preview_invoice,ll_PaymentStatusUpdate,ll_SetToDefault;
        public SingleInvoiceholder(@NonNull View itemView) {
            super(itemView);
            tv_InvoiceIdDue=itemView.findViewById(R.id.tv_InvoiceIdDue);
            tv_invoiceDueAmt=itemView.findViewById(R.id.tv_invoiceDueAmt);
            iv_InvoiceDueDate=itemView.findViewById(R.id.iv_InvoiceDueDate);
            ll_ChangeDueDate=itemView.findViewById(R.id.ll_ChangeDueDate);
            ll_preview_invoice=itemView.findViewById(R.id.ll_preview_invoice);
            ll_PaymentStatusUpdate=itemView.findViewById(R.id.ll_PaymentStatusUpdate);
            ll_SetToDefault=itemView.findViewById(R.id.ll_SetToDefault);
        }
    }
}
