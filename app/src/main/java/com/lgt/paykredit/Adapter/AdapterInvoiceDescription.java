package com.lgt.paykredit.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.lgt.paykredit.Models.ModelInvoiceItems;
import com.lgt.paykredit.R;

import java.util.List;

/**
 * Created by Ranjan on 3/20/2020.
 */
public class AdapterInvoiceDescription extends RecyclerView.Adapter<AdapterInvoiceDescription.HolderInvoiceDescription> {

    private List<ModelInvoiceItems> list;
    private Context context;

    public clickHandler myClick;


    public interface clickHandler{
        void clickedPosition(int adapterPosition);

    }

    public AdapterInvoiceDescription(List<ModelInvoiceItems> list, Context context,clickHandler myClick) {
        this.list = list;
        this.context = context;
        this.myClick = myClick;
    }

    @NonNull
    @Override
    public HolderInvoiceDescription onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_invoice_items,parent,false);
        return new HolderInvoiceDescription(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final HolderInvoiceDescription holder, int position) {

        holder.tvSNo.setText(String.valueOf(holder.getAdapterPosition()+1+"."));
        holder.tvItemDetails.setText(list.get(position).getDescription());
        holder.tvQty.setText(list.get(position).getQty());

        holder.tvAmountItemInvoice.setText(list.get(position).getAmount()+".00");

        if(holder.getAdapterPosition()%2 ==0){
           holder.rlInvoiceItems.setBackgroundColor(context.getResources().getColor(R.color.test));
        }

        holder.rlInvoiceItems.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myClick.clickedPosition(holder.getAdapterPosition());
            }
        });


    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class HolderInvoiceDescription extends RecyclerView.ViewHolder {

        private TextView tvSNo,tvItemDetails,tvAmountItemInvoice,tvQty;
        private RelativeLayout rlInvoiceItems;
        public HolderInvoiceDescription(@NonNull View itemView) {
            super(itemView);
            tvSNo = itemView.findViewById(R.id.tvSNo);
            tvItemDetails = itemView.findViewById(R.id.tvItemDetails);
            tvAmountItemInvoice = itemView.findViewById(R.id.tvAmountItemInvoice);
            tvQty = itemView.findViewById(R.id.tvQty);

            rlInvoiceItems = itemView.findViewById(R.id.rlInvoiceItems);
        }
    }
}
