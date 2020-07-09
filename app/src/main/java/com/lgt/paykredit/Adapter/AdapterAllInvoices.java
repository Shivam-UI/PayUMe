package com.lgt.paykredit.Adapter;

import android.content.Context;
import android.content.Intent;
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
import com.lgt.paykredit.bottomsheets.BottomSheetSendReminder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ranjan on 3/19/2020.
 */
public class AdapterAllInvoices extends RecyclerView.Adapter<AdapterAllInvoices.HolderPaid> implements Filterable {

    private List<ModelAllInvoices> listPaid;
    private List<ModelAllInvoices> listPaidFull;
    private Context context;

    public AdapterAllInvoices(List<ModelAllInvoices> listPaid, Context context) {
        this.listPaid = listPaid;
        this.context = context;
        listPaidFull = new ArrayList<>(listPaid);
    }

    @NonNull
    @Override
    public HolderPaid onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_all_invoices,parent,false);
        return new HolderPaid(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HolderPaid holder, int position) {

        holder.tvPaidInvoiceNumber.setText(listPaid.get(position).getPaidInvoiceNumber());
        holder.tvPaidInvoiceDate.setText(listPaid.get(position).getPaymentDate());
        holder.tvPaidInvoiceUserName.setText(listPaid.get(position).getName());
        holder.tvAmountPaid.setText(listPaid.get(position).getAmount());

        if(listPaid.get(position).getPaymentStatus().equalsIgnoreCase("Done")){
            holder.rlAllInvoicesTop.setBackgroundColor(context.getResources().getColor(R.color.green));
        }
        else {
            holder.rlAllInvoicesTop.setBackgroundColor(context.getResources().getColor(R.color.light_red));
        }

        holder.rlAllInvoices.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                context.startActivity(new Intent(context, ActivityInvoiceDescription.class));
            }
        });

        holder.llCallAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BottomSheetCall bottomSheetCall = new BottomSheetCall();
                FragmentManager manager = ((AppCompatActivity) context).getSupportFragmentManager();
                bottomSheetCall.show(manager, "BottomSheetCall");
            }
        });

        holder.llRemainderAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BottomSheetSendReminder bottomSheetSendReminder = new BottomSheetSendReminder();
                FragmentManager manager = ((AppCompatActivity) context).getSupportFragmentManager();
                bottomSheetSendReminder.show(manager, "BottomSheetCall");
            }
        });

        holder.llRecordPaymentAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.startActivity(new Intent(context, ActivityInvoiceDescription.class));
            }
        });


    }

    @Override
    public int getItemCount() {
        return listPaid.size();
    }

    @Override
    public Filter getFilter() {
        return exampleFilter;
    }

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

        private TextView tvPaidInvoiceNumber,tvPaymentStatus,tvPaidInvoiceDate,tvPaidInvoiceUserName,tvAmountPaid;

        private LinearLayout llRemainderAll,llCallAll,llRecordPaymentAll;
        private RelativeLayout rlAllInvoicesTop,rlAllInvoices;

        public HolderPaid(@NonNull View itemView) {
            super(itemView);

            tvPaidInvoiceNumber = itemView.findViewById(R.id.tvPaidInvoiceNumber);
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
