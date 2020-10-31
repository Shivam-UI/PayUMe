package com.lgt.paykredit.Adapter;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.lgt.paykredit.Models.DefaultModel;
import com.lgt.paykredit.R;
import com.lgt.paykredit.bottomsheets.BottomDeleteDefaulter;
import com.lgt.paykredit.bottomsheets.BottomSheetDeleteInvoice;

import java.util.ArrayList;

public class DefaultInvoiceAdapter extends RecyclerView.Adapter<DefaultInvoiceAdapter.DefaultHolder> {

    Context mContext;
    ArrayList<DefaultModel> mList;

    public DefaultInvoiceAdapter(Context mContext, ArrayList<DefaultModel> mList) {
        this.mContext = mContext;
        this.mList = mList;
    }

    @NonNull
    @Override
    public DefaultHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View mView = LayoutInflater.from(mContext).inflate(R.layout.defaulter_invoice_list_items,parent,false);
        return new DefaultHolder(mView);
    }

    @Override
    public void onBindViewHolder(@NonNull DefaultHolder holder, int position) {
        holder.tvDefaulterDate.setText(mList.get(position).getDefaulter_date());
        holder.tvDefaulterUserName.setText(mList.get(position).getCustomer_name());
        holder.tvDeleteCustomerDefaulter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BottomDeleteDefaulter DefaulterDeleteInvoice = new BottomDeleteDefaulter();
                Bundle deleteItems = new Bundle();
                deleteItems.putString("KEY_DELETE_ID", mList.get(position).getTbl_invoice_customer_id());
                deleteItems.putString("KEY_DELETE_ITEM", mList.get(position).getCustomer_name());
                FragmentManager fragmentManager = ((AppCompatActivity) mContext).getSupportFragmentManager();
                DefaulterDeleteInvoice.setArguments(deleteItems);
                DefaulterDeleteInvoice.show(fragmentManager, "BottomSheetDeleteItems");
            }
        });
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class DefaultHolder extends RecyclerView.ViewHolder{
        TextView tvDefaulterUserName,tvDeleteCustomerDefaulter,tvDefaulterDate;
        public DefaultHolder(@NonNull View itemView) {
            super(itemView);
            tvDefaulterDate=itemView.findViewById(R.id.tvDefaulterDate);
            tvDefaulterUserName=itemView.findViewById(R.id.tvDefaulterUserName);
            tvDeleteCustomerDefaulter=itemView.findViewById(R.id.tvDeleteCustomerDefaulter);
        }
    }
}
