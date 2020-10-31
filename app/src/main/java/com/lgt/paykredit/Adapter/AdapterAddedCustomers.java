package com.lgt.paykredit.Adapter;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.lgt.paykredit.Models.ModelAddedCustomers;
import com.lgt.paykredit.R;
import com.lgt.paykredit.bottomsheets.BottomSheetAddCustomerDetails;
import com.lgt.paykredit.bottomsheets.BottomSheetDeleteCustomer;

import java.util.List;

public class AdapterAddedCustomers extends RecyclerView.Adapter<AdapterAddedCustomers.HolderCustomers> {

    private List<ModelAddedCustomers> list;
    private Context context;

    public AdapterAddedCustomers(List<ModelAddedCustomers> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public HolderCustomers onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_added_customers,parent,false);
        return new HolderCustomers(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HolderCustomers holder, final int position) {
    holder.tvAddedCustomerName.setText(list.get(position).getName());
    holder.tvAddressLine1.setText(list.get(position).getAddress());
    holder.tvCustomerMobileNumber.setText(list.get(position).getMobile());
    holder.tvCustomerEmail.setText(list.get(position).getEmail());


    holder.ivDeleteCustomer.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            BottomSheetDeleteCustomer bottomSheetDeleteCustomer = new BottomSheetDeleteCustomer();

            FragmentManager fragmentManager = ((AppCompatActivity)context).getSupportFragmentManager();

            Bundle bundleDeleteCustomerData = new Bundle();
            bundleDeleteCustomerData.putString("KEY_DELETE_TYPE","DELETE_ADDED_CUSTOMER");
            bundleDeleteCustomerData.putString("KEY_DELETE_CUSTOMER_NAME",list.get(position).getName());
            bundleDeleteCustomerData.putString("KEY_DELETE_ID_ADDED_CUSTOMERS",list.get(position).getId());

            bottomSheetDeleteCustomer.setArguments(bundleDeleteCustomerData);
            bottomSheetDeleteCustomer.show(fragmentManager,"BottomSheetDeleteCustomer");

        }
    });

    holder.ivEdit.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            BottomSheetAddCustomerDetails bottomSheetAddCustomerDetails = new BottomSheetAddCustomerDetails();
            FragmentManager fragmentManager = ((AppCompatActivity)context).getSupportFragmentManager();

            Bundle bundleEditCustomerDetails = new Bundle();

            bundleEditCustomerDetails.putString("KEY_TYPE","EDIT");

            bundleEditCustomerDetails.putString("KEY_EDIT_NAME",list.get(position).getName());
            bundleEditCustomerDetails.putString("KEY_EDIT_EMAIL",list.get(position).getEmail());
            bundleEditCustomerDetails.putString("KEY_EDIT_MOBILE",list.get(position).getMobile());
            bundleEditCustomerDetails.putString("KEY_EDIT_ADDRESS",list.get(position).getAddress());
            bundleEditCustomerDetails.putString("KEY_EDIT_ID",list.get(position).getId());


            bottomSheetAddCustomerDetails.setArguments(bundleEditCustomerDetails);
            bottomSheetAddCustomerDetails.show(fragmentManager,"BottomSheetAddCustomerDetails");

        }
    });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class HolderCustomers extends RecyclerView.ViewHolder {
        private TextView tvAddedCustomerName,tvAddressLine1,tvAddressLine2,tvCustomerMobileNumber,tvCustomerEmail;
        private ImageView ivDeleteCustomer,ivEdit;
        private CardView cardAddedCustomers;

        public HolderCustomers(@NonNull View itemView) {
            super(itemView);

            tvAddedCustomerName = itemView.findViewById(R.id.tvAddedCustomerName);
            tvAddressLine1 = itemView.findViewById(R.id.tvAddressLine1);
            tvAddressLine2 = itemView.findViewById(R.id.tvAddressLine2);
            tvCustomerMobileNumber = itemView.findViewById(R.id.tvCustomerMobileNumber);
            tvCustomerEmail = itemView.findViewById(R.id.tvCustomerEmail);
            cardAddedCustomers = itemView.findViewById(R.id.cardAddedCustomers);
            ivDeleteCustomer = itemView.findViewById(R.id.ivDeleteCustomer);
            ivEdit = itemView.findViewById(R.id.ivEdit);


        }
    }
}
