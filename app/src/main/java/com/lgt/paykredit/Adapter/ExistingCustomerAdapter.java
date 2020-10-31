package com.lgt.paykredit.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.lgt.paykredit.Activities.CreateInvoice;
import com.lgt.paykredit.Models.ExistingCustomerModel;
import com.lgt.paykredit.R;
import com.lgt.paykredit.bottomsheets.BottomSheetDeleteCustomer;
import com.lgt.paykredit.bottomsheets.BottomSheetDeleteItems;
import com.lgt.paykredit.extras.CustomerClick;

import java.util.ArrayList;


public class ExistingCustomerAdapter extends RecyclerView.Adapter<ExistingCustomerAdapter.CustomerHolder> {

    Context mContext;
    ArrayList<ExistingCustomerModel> mList;
    public CustomerClick mCustomerClick;
    public static boolean isCustomerAdded=false;
    public static String customer_id,customer_name;
    public ExistingCustomerAdapter(Context mContext, ArrayList<ExistingCustomerModel> mList, CustomerClick mClick) {
        this.mContext = mContext;
        this.mList = mList;
        this.mCustomerClick = mClick;
    }

    @NonNull
    @Override
    public CustomerHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View mView = LayoutInflater.from(mContext).inflate(R.layout.show_my_customer, parent, false);
        return new CustomerHolder(mView);
    }

    @Override
    public void onBindViewHolder(@NonNull CustomerHolder holder, final int position) {
        holder.tv_user_name.setText(mList.get(position).getCustomer_name());
        holder.tv_address.setText(mList.get(position).getBilling_address());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Intent mIntent = new Intent(mContext.getApplicationContext(), CreateInvoice.class);
                /*mIntent.putExtra("comeFrom","CustomerDetails");
                mIntent.putExtra("customer_id",mList.get(position).getTbl_invoice_customer_id());
                mIntent.putExtra("customer_name",mList.get(position).getCustomer_name());
                mContext.startActivity(mIntent);*/
                isCustomerAdded=true;
                customer_id=mList.get(position).getTbl_invoice_customer_id();
                customer_name=mList.get(position).getCustomer_name();
                ((Activity)mContext).onBackPressed();
            }
        });

        holder.iv_delete_customer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // mCustomerClick.deleteCustomer(mList.get(position).getTbl_invoice_customer_id(), position);
                BottomSheetDeleteCustomer bottomSheetDeleteCustomer = new BottomSheetDeleteCustomer();
                Bundle deleteItems = new Bundle();
                deleteItems.putString("KEY_DELETE_ID",mList.get(position).getTbl_invoice_customer_id());
                deleteItems.putString("KEY_DELETE_ITEM",mList.get(position).getCustomer_name());
                FragmentManager fragmentManager = ((AppCompatActivity)mContext).getSupportFragmentManager();
                bottomSheetDeleteCustomer.setArguments(deleteItems);
                bottomSheetDeleteCustomer.show(fragmentManager,"BottomSheetDeleteItems");
                mCustomerClick.refreshList();
            }
        });

        holder.iv_edit_customer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCustomerClick.editCustomer(mList.get(position).getTbl_invoice_customer_id(), position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class CustomerHolder extends RecyclerView.ViewHolder {
        TextView tv_user_name, tv_address;
        ImageView iv_delete_customer, iv_edit_customer;

        public CustomerHolder(@NonNull View itemView) {
            super(itemView);
            tv_user_name = itemView.findViewById(R.id.tv_user_name);
            tv_address = itemView.findViewById(R.id.tv_address);
            iv_edit_customer = itemView.findViewById(R.id.iv_edit_customer);
            iv_delete_customer = itemView.findViewById(R.id.iv_delete_customer);
        }
    }
}
