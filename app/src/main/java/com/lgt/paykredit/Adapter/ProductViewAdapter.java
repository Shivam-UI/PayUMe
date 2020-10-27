package com.lgt.paykredit.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.lgt.paykredit.Models.ProductViewModel;
import com.lgt.paykredit.R;

import java.util.ArrayList;

public class ProductViewAdapter extends RecyclerView.Adapter<ProductViewAdapter.ProductViewHolder>  {
    ArrayList<ProductViewModel> mList;
    Context mContext;

    public ProductViewAdapter(ArrayList<ProductViewModel> mList, Context mContext) {
        this.mList = mList;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View mView = LayoutInflater.from(mContext).inflate(R.layout.single_product_item,parent,false);
        return new ProductViewAdapter.ProductViewHolder(mView);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        holder.ivDeleteAddedProduct.setVisibility(View.GONE);
        holder.ivEditAddedProduct.setVisibility(View.GONE);
        holder.tv_AddedProductName.setText(mList.get(position).getPro_name());
        holder.tv_AddedProductPrice.setText(mList.get(position).getPro_price());
        holder.tv_DiscountAmt.setText(mList.get(position).getPro_discount());
        holder.tv_DiscountPrice.setText(""+(Integer.parseInt(mList.get(position).getPro_qnt())*Integer.parseInt(mList.get(position).getPro_price())));
        holder.tv_ItemSubTotal.setText("Item Sub Total ( "+mList.get(position).getPro_qnt()+" x "+mList.get(position).getPro_price()+ ")");
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class ProductViewHolder extends RecyclerView.ViewHolder{
        TextView tv_AddedProductPrice,tv_AddedProductName,tv_DiscountAmt,tv_DiscountPrice,tv_ItemSubTotal;
        ImageView ivDeleteAddedProduct,ivEditAddedProduct;
        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_AddedProductName=itemView.findViewById(R.id.tv_AddedProductName);
            tv_AddedProductPrice=itemView.findViewById(R.id.tv_AddedProductPrice);
            ivDeleteAddedProduct=itemView.findViewById(R.id.ivDeleteAddedProduct);
            ivEditAddedProduct=itemView.findViewById(R.id.ivEditAddedProduct);
            tv_DiscountAmt=itemView.findViewById(R.id.tv_DiscountAmt);
            tv_DiscountPrice=itemView.findViewById(R.id.tv_DiscountPrice);
            tv_ItemSubTotal=itemView.findViewById(R.id.tv_ItemSubTotal);
        }
    }
}
