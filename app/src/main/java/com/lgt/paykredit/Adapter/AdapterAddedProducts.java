package com.lgt.paykredit.Adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.lgt.paykredit.Activities.CreateInvoice;
import com.lgt.paykredit.Models.ModelAddedProducts;
import com.lgt.paykredit.R;
import com.lgt.paykredit.bottomsheets.BottomSheetAddItems;
import com.lgt.paykredit.bottomsheets.BottomSheetDeleteItems;

import java.util.List;

public class AdapterAddedProducts extends RecyclerView.Adapter<AdapterAddedProducts.HolderAddedProducts> {

    private List<ModelAddedProducts> list;
    private Context context;

    public AdapterAddedProducts(List<ModelAddedProducts> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public HolderAddedProducts onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_added_products,parent,false);
        return new HolderAddedProducts(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HolderAddedProducts holder, final int position) {
        holder.tvTaxPercentage.setText(list.get(position).getTax()+" %");
        holder.tvAmountProduct.setText(list.get(position).getAmount());
        holder.tvDiscountPercentage.setText(list.get(position).getDiscount()+" %");
        holder.tvProductCode.setText(list.get(position).getHsnCode());
        holder.tvProductName.setText(list.get(position).getName());
        holder.ivEditProducts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BottomSheetAddItems bottomSheetAddItems = new BottomSheetAddItems();
                FragmentManager fragmentManager = ((AppCompatActivity)context).getSupportFragmentManager();
                Bundle bundleAddedProducts = new Bundle();
                bundleAddedProducts.putString("KEY_EDIT_ADDED_PRODUCTS","EDIT_PRODUCT");
                bundleAddedProducts.putString("KEY_PRODUCT_ID",list.get(position).getTbl_invoice_products_id());
                bundleAddedProducts.putString("KEY_PRODUCT_NAME",list.get(position).getName());
                bundleAddedProducts.putString("KEY_HSN_CODE",list.get(position).getHsnCode());
                bundleAddedProducts.putString("KEY_RATE_PER_UNIT",list.get(position).getAmount());
                bundleAddedProducts.putString("KEY_DISCOUNT_PERCENTAGE",list.get(position).getDiscount());
                bundleAddedProducts.putString("KEY_TAX_PERCENTAGE",list.get(position).getTax());
                bottomSheetAddItems.setArguments(bundleAddedProducts);
                bottomSheetAddItems.show(fragmentManager,"BottomSheetAddItems"); //13300295533
            }
        });


        holder.ivDeleteProducts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BottomSheetDeleteItems bottomSheetDeleteItems = new BottomSheetDeleteItems();
                Bundle deleteItems = new Bundle();
                deleteItems.putString("KEY_DELETE_ID",list.get(position).getTbl_invoice_products_id());
                deleteItems.putString("KEY_DELETE_ITEM",list.get(position).getName());
                FragmentManager fragmentManager = ((AppCompatActivity)context).getSupportFragmentManager();
                bottomSheetDeleteItems.setArguments(deleteItems);
                bottomSheetDeleteItems.show(fragmentManager,"BottomSheetDeleteItems");
            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent mIntent = new Intent(context.getApplicationContext(), CreateInvoice.class);
                mIntent.putExtra("comeFrom","ProductDetails");
                mIntent.putExtra("ProductId",list.get(position).getTbl_invoice_products_id());
                mIntent.putExtra("ProductName",list.get(position).getName());
                mIntent.putExtra("ProductAmt",list.get(position).getAmount());
                mIntent.putExtra("ProductDis",list.get(position).getDiscount());
                mIntent.putExtra("ProductQua",list.get(position).getQuantity());
                mIntent.putExtra("ProductTax",list.get(position).getTax());
                context.startActivity(mIntent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class HolderAddedProducts extends RecyclerView.ViewHolder {

        private TextView tvProductName,tvProductCode,tvDiscountPercentage,tvAmountProduct,tvTaxPercentage;
        private ImageView ivEditProducts,ivDeleteProducts;

        public HolderAddedProducts(@NonNull View itemView) {
            super(itemView);
            tvProductName = itemView.findViewById(R.id.tvProductName);
            tvProductCode = itemView.findViewById(R.id.tvProductCode);
            tvDiscountPercentage = itemView.findViewById(R.id.tvDiscountPercentage);
            tvAmountProduct = itemView.findViewById(R.id.tvAmountProduct);
            tvTaxPercentage = itemView.findViewById(R.id.tvTaxPercentage);
            ivEditProducts = itemView.findViewById(R.id.ivEditProducts);
            ivDeleteProducts = itemView.findViewById(R.id.ivDeleteProducts);
        }
    }
}
