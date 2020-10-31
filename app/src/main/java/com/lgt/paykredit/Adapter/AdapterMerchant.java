package com.lgt.paykredit.Adapter;

import android.content.Context;
import android.content.Intent;
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

import com.lgt.paykredit.Activities.ActivityPaidUnpaid;
import com.lgt.paykredit.Models.ModelMerchant;
import com.lgt.paykredit.R;
import com.lgt.paykredit.bottomsheets.BottomSheetCall;
import com.lgt.paykredit.bottomsheets.BottomSheetSendReminder;

import java.util.List;

/**
 * Created by Ranjan on 3/19/2020.
 */

public class AdapterMerchant extends RecyclerView.Adapter<AdapterMerchant.HolderUnpaidInvoice> {

    private List<ModelMerchant> listMerchant;
    private Context context;

    public AdapterMerchant(List<ModelMerchant> listMerchant, Context context) {
        this.listMerchant = listMerchant;
        this.context = context;
    }

    @NonNull
    @Override
    public HolderUnpaidInvoice onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_merchant, parent, false);
        return new HolderUnpaidInvoice(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HolderUnpaidInvoice holder, int position) {

        holder.tvInvoiceUserName.setText(listMerchant.get(position).getName());
        holder.tvAmountDue.setText(listMerchant.get(position).getAmount());

        holder.cardUnpaid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //context.startActivity(new Intent(context, ActivityInvoiceDescription.class));
                context.startActivity(new Intent(context, ActivityPaidUnpaid.class));
                //send user to paid or unpaid pager adapter then on click send to ActivityInvoiceDescription
            }
        });

        holder.ivPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BottomSheetCall bottomSheetCall = new BottomSheetCall();
                FragmentManager manager = ((AppCompatActivity) context).getSupportFragmentManager();
                bottomSheetCall.show(manager, "BottomSheetCall");
            }
        });

        holder.ivReminder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BottomSheetSendReminder bottomSheetSendReminder = new BottomSheetSendReminder();
                FragmentManager manager = ((AppCompatActivity) context).getSupportFragmentManager();
                bottomSheetSendReminder.show(manager, "BottomSheetCall");
            }
        });

    }

    @Override
    public int getItemCount() {
        return listMerchant.size();
    }

    public static class HolderUnpaidInvoice extends RecyclerView.ViewHolder {

        private ImageView ivPhone,ivReminder;

        private TextView tvInvoiceUserName, tvAmountDue;
        private CardView cardUnpaid;


        public HolderUnpaidInvoice(@NonNull View itemView) {
            super(itemView);


            ivPhone = itemView.findViewById(R.id.ivPhone);
            ivReminder = itemView.findViewById(R.id.ivReminder);
            tvInvoiceUserName = itemView.findViewById(R.id.tvInvoiceUserName);
            tvAmountDue = itemView.findViewById(R.id.tvAmountDue);
            cardUnpaid = itemView.findViewById(R.id.cardUnpaid);

        }
    }
}
