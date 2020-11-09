package com.lgt.paykredit.Adapter;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.lgt.paykredit.Activities.ActivityInvoiceDescription;
import com.lgt.paykredit.Models.ModelAllInvoices;
import com.lgt.paykredit.R;
import com.lgt.paykredit.bottomsheets.BottomSheetCall;
import com.lgt.paykredit.bottomsheets.BottomSheetDeleteCustomer;
import com.lgt.paykredit.bottomsheets.BottomSheetDeleteInvoice;
import com.lgt.paykredit.bottomsheets.BottomSheetDeleteItems;
import com.lgt.paykredit.bottomsheets.BottomSheetSendReminder;
import com.lgt.paykredit.extras.Common;
import com.lgt.paykredit.extras.OpenDetailsInvoice;
import com.lgt.paykredit.extras.SharedData;

import java.util.ArrayList;
import java.util.List;

import static com.lgt.paykredit.extras.Common.INVOICE_ID;

/**
 * Created by Ranjan on 3/19/2020.
 *  implements Filterable
 */

public class AdapterAllInvoices extends RecyclerView.Adapter<AdapterAllInvoices.HolderPaid> {
    OpenDetailsInvoice openDetailsInvoice;
    private List<ModelAllInvoices> listPaid;
    private List<ModelAllInvoices> listPaidFull;
    private Context mContext;
    public static String InvoiceIDShared = "";

    public AdapterAllInvoices(List<ModelAllInvoices> listPaid, Context context, OpenDetailsInvoice detailsInvoice) {
        this.listPaid = listPaid;
        this.mContext = context;
        listPaidFull = new ArrayList<>(listPaid);
        openDetailsInvoice = detailsInvoice;
    }

    @NonNull
    @Override
    public HolderPaid onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_all_invoices, parent, false);
        return new HolderPaid(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HolderPaid holder, int position) {

        holder.tvPaidInvoiceNumber.setText(listPaid.get(position).getPaidInvoiceNumber());
        holder.tvPaidInvoiceDate.setText(listPaid.get(position).getPaymentDate());
        holder.tvPaidInvoiceUserName.setText(listPaid.get(position).getName());
        holder.tvAmountPaid.setText(listPaid.get(position).getAmount());

        if (listPaid.get(position).getPaymentStatus().equalsIgnoreCase("Done")) {
            holder.rlAllInvoicesTop.setBackgroundColor(mContext.getResources().getColor(R.color.green));
        } else {
            holder.rlAllInvoicesTop.setBackgroundColor(mContext.getResources().getColor(R.color.light_red));
        }

        if (listPaid.get(position).getPaymentStatus().equalsIgnoreCase("0")) {
            holder.tvAmountOrDue.setText("Due Amount");
        } else {
            holder.tvAmountOrDue.setText("Paid Amount");
        }

        holder.tvDeleteInvoice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("bund",listPaid.get(position).getPaidInvoiceNumber()+"  |   "+listPaid.get(position).getName());
                /*BottomSheetDeleteInvoice bottomSheetDeleteInvoice = new BottomSheetDeleteInvoice();
                Bundle deleteItems = new Bundle();
                deleteItems.putString("KEY_DELETE_ID", listPaidFull.get(position).getPaidInvoiceNumber());
                deleteItems.putString("KEY_DELETE_ITEM", listPaid.get(position).getName());
                FragmentManager fragmentManager = ((AppCompatActivity) mContext).getSupportFragmentManager();
                bottomSheetDeleteInvoice.setArguments(deleteItems);
                bottomSheetDeleteInvoice.show(fragmentManager, "BottomSheetDeleteItems");*/
                BottomSheetDeleteCustomer bottomSheetDeleteCustomer = new BottomSheetDeleteCustomer();
                Bundle deleteItems = new Bundle();
                deleteItems.putString("KEY_DELETE_ID",listPaidFull.get(position).getPaidInvoiceNumber());
                deleteItems.putString("KEY_DELETE_CUSTOMER_NAME",listPaid.get(position).getName());
                deleteItems.putString("KEY_MY_BILLS","MYBILLS");
                FragmentManager fragmentManager = ((AppCompatActivity)mContext).getSupportFragmentManager();
                bottomSheetDeleteCustomer.setArguments(deleteItems);
                bottomSheetDeleteCustomer.show(fragmentManager,"BottomSheetDeleteItems");
            }
        });


        holder.rlAllInvoices.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDetailsInvoice.ShowInvoiceDetails(listPaid.get(position).getPaidInvoiceNumber());
            }
        });
    }

    @Override
    public int getItemCount() {
        return listPaid.size();
    }

   /* @Override
    public Filter getFilter() {
        return exampleFilter;
    }*/

    private Filter exampleFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<ModelAllInvoices> filteredList = new ArrayList<>();

            if (constraint == null || constraint.length() == 0) {
                filteredList.addAll(listPaidFull);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();

                for (ModelAllInvoices item : listPaidFull) {
                    if (item.getName().toLowerCase().contains(filterPattern)) {
                        filteredList.add(item);
                    }
                }
            }

            FilterResults results = new FilterResults();
            results.values = filteredList;

            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            listPaid.clear();
            listPaid.addAll((List) results.values);
            notifyDataSetChanged();
        }
    };


    public static class HolderPaid extends RecyclerView.ViewHolder {

        private TextView tvPaidInvoiceNumber, tvAmountOrDue, tvPaidInvoiceDate, tvPaidInvoiceUserName, tvAmountPaid, tvDeleteInvoice;

        private LinearLayout llRemainderAll, llCallAll, llRecordPaymentAll;
        private RelativeLayout rlAllInvoicesTop, rlAllInvoices;

        public HolderPaid(@NonNull View itemView) {
            super(itemView);

            tvPaidInvoiceNumber = itemView.findViewById(R.id.tvPaidInvoiceNumber);
            tvAmountOrDue = itemView.findViewById(R.id.tvAmountOrDue);
            tvDeleteInvoice = itemView.findViewById(R.id.tvDeleteInvoice);
            tvPaidInvoiceDate = itemView.findViewById(R.id.tvPaidInvoiceDate);
            tvPaidInvoiceUserName = itemView.findViewById(R.id.tvPaidInvoiceUserName);
            tvAmountPaid = itemView.findViewById(R.id.tvAmountPaid);
            rlAllInvoicesTop = itemView.findViewById(R.id.rlAllInvoicesTop);
            rlAllInvoices = itemView.findViewById(R.id.rlAllInvoices);
            llRemainderAll = itemView.findViewById(R.id.llRemainderAll);
            llCallAll = itemView.findViewById(R.id.llCallAll);
            llRecordPaymentAll = itemView.findViewById(R.id.llRecordPaymentAll);
        }
    }
}
