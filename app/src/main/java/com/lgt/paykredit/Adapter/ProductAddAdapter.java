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
import com.lgt.paykredit.Models.ProductModelNew;
import com.lgt.paykredit.R;
import com.lgt.paykredit.extras.GenerateCalculation;

import java.text.DecimalFormat;
import java.util.ArrayList;

import static com.lgt.paykredit.Activities.CreateInvoice.tv_DiscountInPrice;

public class ProductAddAdapter extends RecyclerView.Adapter<ProductAddAdapter.ProductListHolder> {
    ArrayList<ProductModelNew> mList;
    Context mContext;
    public static int TotalAmt = 0, TotalDis = 0, TotalTax = 0, TotalQty = 0;
    GenerateCalculation mGenerateCalculation;
    DecimalFormat df = new DecimalFormat("#,###,##0.00");
    public ProductAddAdapter(ArrayList<ProductModelNew> mList, Context mContext, GenerateCalculation generateCalculation) {
        this.mList = mList;
        this.mContext = mContext;
        this.mGenerateCalculation = generateCalculation;
    }

    @NonNull
    @Override
    public ProductListHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View mView = LayoutInflater.from(mContext).inflate(R.layout.single_product_item, parent, false);
        return new ProductListHolder(mView);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductListHolder holder, int position) {
        holder.tv_AddedProductName.setText(mList.get(position).getName());
        holder.tv_ItemSubTotal.setText("Discount "+mList.get(position).getDiscount()+"%");
        holder.tv_AddedProductPrice.setText(mList.get(position).getQuantity() + " * " + mList.get(position).getRate() + " = " + mList.get(position).getFinalAmount() + "");
        holder.tv_DiscountAmt.setText(mList.get(position).getFinalTax());
        holder.tv_DiscountPrice.setText(df.format(Float.parseFloat(mList.get(position).getFinalDiscont()))+"");
        holder.tv_Discount.setText("Tax GST@" + mList.get(position).getTax() + "% : " + mList.get(position).getTax() + "%");
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

    public void setDataToCalc(int Amt, int Dsc, int Tax, int Qty) {
        TotalAmt = Amt;
        TotalDis = Dsc;
        TotalTax = Tax;
        TotalQty = Qty;
        // tv_DiscountInPrice.setText(TotalAmt);
        Log.d("listData", "data save " + TotalAmt);
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }


    public class ProductListHolder extends RecyclerView.ViewHolder {
        TextView tv_AddedProductPrice, tv_AddedProductName, tv_DiscountAmt, tv_DiscountPrice, tv_ItemSubTotal, tv_Discount;
        ImageView ivDeleteAddedProduct, ivEditAddedProduct;

        public ProductListHolder(@NonNull View itemView) {
            super(itemView);
            tv_AddedProductName = itemView.findViewById(R.id.tv_AddedProductName);
            tv_AddedProductPrice = itemView.findViewById(R.id.tv_AddedProductPrice);
            ivDeleteAddedProduct = itemView.findViewById(R.id.ivDeleteAddedProduct);
            ivEditAddedProduct = itemView.findViewById(R.id.ivEditAddedProduct);
            tv_DiscountAmt = itemView.findViewById(R.id.tv_DiscountAmt);
            tv_Discount = itemView.findViewById(R.id.tv_Discount);
            tv_DiscountPrice = itemView.findViewById(R.id.tv_DiscountPrice);
            tv_ItemSubTotal = itemView.findViewById(R.id.tv_ItemSubTotal);
        }
    }
}
