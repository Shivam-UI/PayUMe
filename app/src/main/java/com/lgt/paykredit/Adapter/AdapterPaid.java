package com.lgt.paykredit.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.lgt.paykredit.Activities.ActivityInvoiceDescription;
import com.lgt.paykredit.Models.ModelPaid;
import com.lgt.paykredit.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ranjan on 3/20/2020.
 */
public class AdapterPaid extends RecyclerView.Adapter<AdapterPaid.HolderPaid> implements Filterable {

    private List<ModelPaid> listPaid;
    private List<ModelPaid> listPaidFull;
    private Context context;

    public AdapterPaid(List<ModelPaid> listPaid, Context context) {
        this.listPaid = listPaid;
        this.context = context;
        listPaidFull = new ArrayList<>(listPaid);
    }

    @NonNull
    @Override
    public HolderPaid onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_paid,parent,false);

        return new HolderPaid(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HolderPaid holder, int position) {

        holder.tvPaidInvoiceNumberNew.setText(listPaid.get(position).getInvoiceNumber());
        holder.tvPaidInvoiceDateNew.setText(listPaid.get(position).getDate());
        holder.tvPaidInvoiceUserNameNew.setText(listPaid.get(position).getName());
        holder.tvAmountPaidNew.setText(listPaid.get(position).getAmount());

        holder.cardPaidNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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
            List<ModelPaid> filteredList = new ArrayList<>();

            if (constraint == null || constraint.length() == 0) {
                filteredList.addAll(listPaidFull);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();

                for (ModelPaid item : listPaidFull) {
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

        private TextView tvPaidInvoiceNumberNew,tvPaidInvoiceDateNew,tvPaidInvoiceUserNameNew,tvAmountPaidNew;
        private CardView cardPaidNew;

        public HolderPaid(@NonNull View itemView) {
            super(itemView);

            tvPaidInvoiceNumberNew = itemView.findViewById(R.id.tvPaidInvoiceNumberNew);
            tvPaidInvoiceDateNew = itemView.findViewById(R.id.tvPaidInvoiceDateNew);
            tvPaidInvoiceUserNameNew = itemView.findViewById(R.id.tvPaidInvoiceUserNameNew);
            tvAmountPaidNew = itemView.findViewById(R.id.tvAmountPaidNew);

            cardPaidNew = itemView.findViewById(R.id.cardPaidNew);
        }
    }
}
