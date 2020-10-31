package com.lgt.paykredit.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.lgt.paykredit.Models.ModelPurchaseOrder;
import com.lgt.paykredit.R;

import java.util.List;

public class AdapterPurchaseOrder extends RecyclerView.Adapter<AdapterPurchaseOrder.HolderPurchase> {

    private List<ModelPurchaseOrder> list;
    private Context context;

    public AdapterPurchaseOrder(List<ModelPurchaseOrder> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public HolderPurchase onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_purchase_order,parent,false);
        return new HolderPurchase(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HolderPurchase holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public static class HolderPurchase extends RecyclerView.ViewHolder {
        public HolderPurchase(@NonNull View itemView) {
            super(itemView);
        }
    }
}
