package com.lgt.paykredit.Adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.lgt.paykredit.Activities.SingleUserTransaction;
import com.lgt.paykredit.Models.ModelCreditBookCustomers;
import com.lgt.paykredit.R;
import com.lgt.paykredit.bottomsheets.BottomSheetDeleteCustomer;
import com.lgt.paykredit.extras.Common;
import com.lgt.paykredit.extras.Language;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Ranjan on 3/16/2020.
 */

public class AdapterCreditBookCustomers extends RecyclerView.Adapter<AdapterCreditBookCustomers.HolderCreditBookCustomers> implements Filterable {

    private List<ModelCreditBookCustomers> listCredit;
    private List<ModelCreditBookCustomers> listCreditFull;
    private Context context;

    public AdapterCreditBookCustomers(List<ModelCreditBookCustomers> listCredit, Context context) {
        this.listCredit = listCredit;
        this.context = context;
        listCreditFull = new ArrayList<>(listCredit);
    }


    @NonNull
    @Override
    public HolderCreditBookCustomers onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_credit_book_customers, parent, false);
        return new HolderCreditBookCustomers(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HolderCreditBookCustomers holder, final int position) {

        holder.tvUserName.setText(listCredit.get(position).getName());
        holder.tvDate.setText(listCredit.get(position).getDate());


        if (listCredit.get(position).getAdvanceOrDue().equals("Due")) {

            holder.tvAmount.setTextColor(context.getResources().getColor(R.color.red));
            holder.tvAmountOrDue.setTextColor(context.getResources().getColor(R.color.red));

            holder.ivRupees.setColorFilter(ContextCompat.getColor(context, R.color.red));

            holder.tvAmount.setText(listCredit.get(position).getAmount());
            holder.tvAmountOrDue.setText(listCredit.get(position).getAdvanceOrDue());


            if (Common.getLanguage(context).equalsIgnoreCase(Common.HINDI)) {

                holder.tvAmountOrDue.setText("उधार");
                holder.tvDeleteCustomer.setText("(हटाएं)");
                //If there is internet connection, get translate service and start translation:
                //holder.tvDeleteCustomer.setText(Language.convertLanguage(context, holder.tvDeleteCustomer.getText().toString(),Common.getLanguage(context)));
                //holder.tvAmountOrDue.setText(Language.convertLanguage(context, "Borrow",Common.getLanguage(context)));
            } else {

                holder.tvAmount.setText(listCredit.get(position).getAmount());
                holder.tvAmountOrDue.setText(holder.tvAmountOrDue.getText().toString());
            }

        } else {
            holder.tvAmount.setTextColor(context.getResources().getColor(R.color.green));
            holder.tvAmountOrDue.setTextColor(context.getResources().getColor(R.color.green));
            holder.ivRupees.setColorFilter(ContextCompat.getColor(context, R.color.green));
            holder.tvAmount.setText(listCredit.get(position).getAmount());
            holder.tvAmountOrDue.setText(listCredit.get(position).getAdvanceOrDue());

            if (Common.getLanguage(context).equalsIgnoreCase(Common.HINDI)) {
                holder.tvAmountOrDue.setText("एडवांस");
                holder.tvDeleteCustomer.setText("(हटाएं)");
            } else {
                //If not, display "no connection" warning:
                holder.tvAmount.setText(listCredit.get(position).getAmount());
                holder.tvAmountOrDue.setText(holder.tvAmountOrDue.getText().toString());
            }
        }

        holder.llGetFullDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentSingleTransaction = new Intent(context, SingleUserTransaction.class);
                intentSingleTransaction.putExtra("KEY_NAME", listCredit.get(position).getName());
                intentSingleTransaction.putExtra("KEY_TBL_CUSTOMER_ID", listCredit.get(position).getId());
                context.startActivity(intentSingleTransaction);
            }
        });

        holder.tvDeleteCustomer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BottomSheetDeleteCustomer bottomSheetDeleteCustomer = new BottomSheetDeleteCustomer();

                FragmentManager fragmentManager = ((AppCompatActivity) context).getSupportFragmentManager();

                Bundle bundleDeleteCustomerData = new Bundle();
                bundleDeleteCustomerData.putString("KEY_DELETE_CUSTOMER_NAME", listCredit.get(position).getName());
                bundleDeleteCustomerData.putString("KEY_DELETE_ID", listCredit.get(position).getId());

                bottomSheetDeleteCustomer.setArguments(bundleDeleteCustomerData);
                bottomSheetDeleteCustomer.show(fragmentManager, "BottomSheetDeleteCustomer");


            }
        });


    }

    @Override
    public int getItemCount() {
        return listCredit.size();
    }

    @Override
    public Filter getFilter() {
        return exampleFilter;
    }

    private Filter exampleFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<ModelCreditBookCustomers> filteredList = new ArrayList<>();

            if (constraint == null || constraint.length() == 0) {
                filteredList.addAll(listCreditFull);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();

                for (ModelCreditBookCustomers item : listCreditFull) {
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
            listCredit.clear();
            listCredit.addAll((List) results.values);
            notifyDataSetChanged();
        }
    };


    public static class HolderCreditBookCustomers extends RecyclerView.ViewHolder {

        private CircleImageView civUser;
        private TextView tvUserName, tvDate, tvAmount, tvAmountOrDue, tvDeleteCustomer;
        private ImageView ivRupees;

        private LinearLayout llGetFullDetail;


        public HolderCreditBookCustomers(@NonNull View itemView) {
            super(itemView);

            civUser = itemView.findViewById(R.id.civUser);
            tvUserName = itemView.findViewById(R.id.tvUserName);
            tvDate = itemView.findViewById(R.id.tvDate);
            tvAmount = itemView.findViewById(R.id.tvAmount);
            tvAmountOrDue = itemView.findViewById(R.id.tvAmountOrDue);
            llGetFullDetail = itemView.findViewById(R.id.llGetFullDetail);
            ivRupees = itemView.findViewById(R.id.ivRupees);
            tvDeleteCustomer = itemView.findViewById(R.id.tvDeleteCustomer);
        }
    }
}
