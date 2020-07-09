package com.lgt.paykredit.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.lgt.paykredit.Activities.ActivityInvoiceDescription;
import com.lgt.paykredit.Models.ModelUnPaid;
import com.lgt.paykredit.R;
import com.lgt.paykredit.bottomsheets.BottomSheetCall;
import com.lgt.paykredit.bottomsheets.BottomSheetSendReminder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ranjan on 3/20/2020.
 */
public class AdapterUnpaid extends RecyclerView.Adapter<AdapterUnpaid.HolderUnpaid> implements Filterable {

    private List<ModelUnPaid> listUnpaid;
    private List<ModelUnPaid> listUnpaidFull;
    private Context context;

    public AdapterUnpaid(List<ModelUnPaid> listUnpaid, Context context) {
        this.listUnpaid = listUnpaid;
        this.context = context;
        listUnpaidFull = new ArrayList<>(listUnpaid);
    }

    @NonNull
    @Override
    public HolderUnpaid onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_unpaid, parent, false);
        return new HolderUnpaid(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HolderUnpaid holder, int position) {

        holder.tvUnPaidInvoiceNumberNew.setText(listUnpaid.get(position).getInvoiceNumber());
        holder.tvUnPaidInvoiceDateNew.setText(listUnpaid.get(position).getDate());
        holder.tvUnPaidInvoiceUserNameNew.setText(listUnpaid.get(position).getName());
        holder.tvAmountUnPaidNew.setText(listUnpaid.get(position).getAmount());

        holder.llUnPaidDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                context.startActivity(new Intent(context, ActivityInvoiceDescription.class));
            }
        });

        holder.llUnpaidCallNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BottomSheetCall bottomSheetCall = new BottomSheetCall();
                FragmentManager manager = ((AppCompatActivity) context).getSupportFragmentManager();
                bottomSheetCall.show(manager, "BottomSheetCall");
            }
        });

        holder.llUnpaidReminderNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BottomSheetSendReminder bottomSheetSendReminder = new BottomSheetSendReminder();
                FragmentManager manager = ((AppCompatActivity) context).getSupportFragmentManager();
                bottomSheetSendReminder.show(manager, "BottomSheetCall");
            }
        });

        holder.llRecordPaymentUnPaid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.startActivity(new Intent(context, ActivityInvoiceDescription.class));
            }
        });


    }

    @Override
    public int getItemCount() {
        return listUnpaid.size();
    }

    @Override
    public Filter getFilter() {
        return exampleFilter;
    }

    private Filter exampleFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<ModelUnPaid> filteredList = new ArrayList<>();

            if (constraint == null || constraint.length() == 0) {
                filteredList.addAll(listUnpaidFull);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();

                for (ModelUnPaid item : listUnpaidFull) {
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
            listUnpaid.clear();
            listUnpaid.addAll((List) results.values);
            notifyDataSetChanged();
        }
    };



    public static class HolderUnpaid extends RecyclerView.ViewHolder {

        private TextView tvUnPaidInvoiceNumberNew, tvUnPaidInvoiceDateNew, tvUnPaidInvoiceUserNameNew, tvAmountUnPaidNew;


        private LinearLayout llUnpaidCallNew, llUnpaidReminderNew,llUnPaidDetails,llRecordPaymentUnPaid;

        public HolderUnpaid(@NonNull View itemView) {
            super(itemView);

            tvUnPaidInvoiceNumberNew = itemView.findViewById(R.id.tvUnPaidInvoiceNumberNew);
            tvUnPaidInvoiceDateNew = itemView.findViewById(R.id.tvUnPaidInvoiceDateNew);
            tvUnPaidInvoiceUserNameNew = itemView.findViewById(R.id.tvUnPaidInvoiceUserNameNew);
            tvAmountUnPaidNew = itemView.findViewById(R.id.tvAmountUnPaidNew);

            llUnPaidDetails = itemView.findViewById(R.id.llUnPaidDetails);
            llUnpaidCallNew = itemView.findViewById(R.id.llUnpaidCallNew);
            llUnpaidReminderNew = itemView.findViewById(R.id.llUnpaidReminderNew);
            llRecordPaymentUnPaid = itemView.findViewById(R.id.llRecordPaymentUnPaid);
        }
    }
}
