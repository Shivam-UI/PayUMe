package com.lgt.paykredit.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.lgt.paykredit.Activities.CreateInvoice;
import com.lgt.paykredit.Models.ProductModel;
import com.lgt.paykredit.R;
import com.lgt.paykredit.extras.GenerateCalculation;

import java.util.ArrayList;

import static com.lgt.paykredit.Activities.CreateInvoice.tv_DiscountInPrice;

public class ProductAddAdapter extends RecyclerView.Adapter<ProductAddAdapter.ProductListHolder> {
    ArrayList<ProductModel> mList;
    Context mContext;
    public static int TotalAmt=0,TotalDis=0,TotalTax=0,TotalQty=0;
    GenerateCalculation mGenerateCalculation;

    public ProductAddAdapter(ArrayList<ProductModel> mList, Context mContext,GenerateCalculation generateCalculation) {
        this.mList = mList;
        this.mContext = mContext;
        this.mGenerateCalculation = generateCalculation;
    }

    @NonNull
    @Override
    public ProductListHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View mView = LayoutInflater.from(mContext).inflate(R.layout.single_product_item,parent,false);
        return new ProductListHolder(mView);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductListHolder holder, int position) {
        Log.d("listData", mList.get(position).getProductName() + " | " + mList.get(position).getProductDis() +" | "+mList.get(position).getProductId()+" | "+mList.get(position).getProductAmt()+" | "+mList.get(position).getProductQua()+" | "+mList.get(position).getProductTax());
        setDataToCalc(Integer.parseInt(mList.get(position).getProductAmt()),Integer.parseInt(mList.get(position).getProductDis()),Integer.parseInt(mList.get(position).getProductTax()),Integer.parseInt(mList.get(position).getProductQua()));
        holder.tv_AddedProductName.setText(mList.get(position).getProductName());
        holder.tv_AddedProductPrice.setText("₹ "+mList.get(position).getProductAmt()+".00");
        holder.tv_ItemSubTotal.setText("Item Sub Total ("+mList.get(position).getProductQua()+" x "+mList.get(position).getProductAmt()+" )");
        holder.tv_DiscountAmt.setText(mList.get(position).getProductDis());
        holder.tv_DiscountPrice.setText(Integer.parseInt(mList.get(position).getProductAmt())*Integer.parseInt(mList.get(position).getProductQua())+"");

       holder.ivDeleteAddedProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(mContext, "Removed Item", Toast.LENGTH_SHORT).show();
                mList.remove(position);
                mGenerateCalculation.calculateProductPrice();
                notifyDataSetChanged();
            }
        });

        holder.ivEditAddedProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(mContext, "Edit Item", Toast.LENGTH_SHORT).show();
            }
        });
        mGenerateCalculation.calculateProductPrice();
    }

    public void setDataToCalc(int Amt,int Dsc,int Tax,int Qty){
        TotalAmt=Amt;TotalDis=Dsc;TotalTax=Tax;TotalQty=Qty;
        // tv_DiscountInPrice.setText(TotalAmt);
        Log.d("listData","data save "+TotalAmt);
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }


    public class ProductListHolder extends RecyclerView.ViewHolder{
        TextView tv_AddedProductPrice,tv_AddedProductName,tv_DiscountAmt,tv_DiscountPrice,tv_ItemSubTotal;
        ImageView ivDeleteAddedProduct,ivEditAddedProduct;
        public ProductListHolder(@NonNull View itemView) {
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
