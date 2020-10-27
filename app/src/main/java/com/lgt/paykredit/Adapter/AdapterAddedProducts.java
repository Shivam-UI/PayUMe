package com.lgt.paykredit.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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
import com.lgt.paykredit.extras.LoadInvoiceData;

import java.util.List;

public class AdapterAddedProducts extends RecyclerView.Adapter<AdapterAddedProducts.HolderAddedProducts> {

    private List<ModelAddedProducts> list;
    private Context context;
    public LoadInvoiceData mLoadInvoiceData;
    public static boolean IsClickToAdd = false;
    public static String ProductId,ProductName,ProductAmt,ProductDis,ProductQua,ProductTax,AdvanceAmt,itemPrice,ItemDue,ItemDiscount;
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
        holder.tvTaxPercentage.setText(list.get(position).getTax()+" ₹");
        holder.tvAmountProduct.setText(list.get(position).getAmount());
        holder.tvDiscountPercentage.setText(list.get(position).getDiscount()+" ₹");
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
                bundleAddedProducts.putString("KEY_QUANTITY_PER_UNIT",list.get(position).getQuantity());
                bundleAddedProducts.putString("KEY_ADVANCE_PER_UNIT",list.get(position).getAdvance());
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
                Log.d("listData",""+list.get(position).getQuantity());
                IsClickToAdd=true;
                int subTotalPrice = 0, DiscountInPrice = 0, BalanceDue = 0,pprice=0,pqty=0,pdiscunt=0,padvnc=0;
                pprice=Integer.parseInt(list.get(position).getAmount());
                pqty=Integer.parseInt(list.get(position).getQuantity());
                pdiscunt=Integer.parseInt(list.get(position).getDiscount());
                padvnc=Integer.parseInt(list.get(position).getAdvance());
                subTotalPrice = pprice * pqty;
                DiscountInPrice = pdiscunt * pqty;
                BalanceDue = (((pprice - pdiscunt) * pqty) - padvnc);
                setDataToProduct(subTotalPrice,
                        DiscountInPrice,
                        BalanceDue,
                        list.get(position).getTbl_invoice_products_id(),
                        list.get(position).getName(),
                        String.valueOf(pprice),
                        list.get(position).getAdvance(),
                        list.get(position).getDiscount(),
                        list.get(position).getQuantity(),
                        list.get(position).getTax());
                ((Activity)context).onBackPressed();
            }
        });
    }

    private void setDataToProduct(int stp,int dip,int bd,String tbl_invoice_products_id, String name, String amount,String AdvAmt ,String discount, String quantity, String tax) {
        itemPrice=String.valueOf(stp);
        ItemDue=String.valueOf(dip);
        ItemDiscount=String.valueOf(bd);
        ProductId=tbl_invoice_products_id;
        ProductName=name;
        ProductAmt=amount;
        ProductDis=discount;
        ProductQua=quantity;
        ProductTax=tax;
        AdvanceAmt=AdvAmt;
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
