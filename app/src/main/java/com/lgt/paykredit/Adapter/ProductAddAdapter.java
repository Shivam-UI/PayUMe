package com.lgt.paykredit.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.lgt.paykredit.Models.ProductModel;
import com.lgt.paykredit.R;

import java.util.ArrayList;

public class ProductAddAdapter extends RecyclerView.Adapter<ProductAddAdapter.ProductListHolder> {
    ArrayList<ProductModel> mList;
    Context mContext;

    public ProductAddAdapter(ArrayList<ProductModel> mList, Context mContext) {
        this.mList = mList;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public ProductListHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View mView = LayoutInflater.from(mContext).inflate(R.layout.single_product_item,parent,false);
        return new ProductListHolder(mView);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductListHolder holder, int position) {
        holder.tv_AddedProductName.setText(mList.get(position).getProductName());
        holder.tv_AddedProductPrice.setText("₹ "+mList.get(position).getProductAmt()+".00");
        holder.ivDeleteAddedProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(mContext, "Removed Item", Toast.LENGTH_SHORT).show();
            }
        });

        holder.ivEditAddedProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(mContext, "Edit Item", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class ProductListHolder extends RecyclerView.ViewHolder{
        TextView tv_AddedProductPrice,tv_AddedProductName;
        ImageView ivDeleteAddedProduct,ivEditAddedProduct;
        public ProductListHolder(@NonNull View itemView) {
            super(itemView);
            tv_AddedProductName=itemView.findViewById(R.id.tv_AddedProductName);
            tv_AddedProductPrice=itemView.findViewById(R.id.tv_AddedProductPrice);
            ivDeleteAddedProduct=itemView.findViewById(R.id.ivDeleteAddedProduct);
            ivEditAddedProduct=itemView.findViewById(R.id.ivEditAddedProduct);
        }
    }
}
