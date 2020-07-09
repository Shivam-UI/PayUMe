package com.lgt.paykredit.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.lgt.paykredit.Models.ModelSingleUserTransaction;
import com.lgt.paykredit.R;

import java.util.List;

import static com.lgt.paykredit.Models.ModelSingleUserTransaction.PAID;
import static com.lgt.paykredit.Models.ModelSingleUserTransaction.RECEIVED;

/**
 * Created by Ranjan on 3/17/2020.
 */
public class AdapterSingleUserTransaction extends RecyclerView.Adapter {

    private List<ModelSingleUserTransaction> listSingleTransaction;
    private Context context;

    public AdapterSingleUserTransaction(List<ModelSingleUserTransaction> listSingleTransaction, Context context) {
        this.listSingleTransaction = listSingleTransaction;
        this.context = context;
    }

    @Override
    public int getItemViewType(int position) {

        switch (listSingleTransaction.get(position).getContent_type()) {
            case 0:
                return PAID;

            case 1:
                return RECEIVED;

          /*  case 2:
                return TIME;*/

        }

        return -1;

    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (viewType) {
            case RECEIVED: {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.items_amount_received, parent, false);
                return new HolderAmountReceived(view);
            }

            case PAID: {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_amount_paid, parent, false);
                return new HolderAmountPaid(view);
            }

          /*  case TIME: {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_time_transaction, parent, false);
                return new HolderTime(view);
            }*/

        }
        return null;
    }


    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        switch (listSingleTransaction.get(position).getContent_type()) {
            case PAID:

                ((HolderAmountPaid) holder).tvAmountSent.setText(listSingleTransaction.get(position).getAmount());
                ((HolderAmountPaid) holder).tvAmountSentTime.setText(listSingleTransaction.get(position).getTime());


                if (position<listSingleTransaction.size() && (position+1)<listSingleTransaction.size()) {
                    if (listSingleTransaction.size() > 1) {
                        if (listSingleTransaction.get(position).getDate().equals(listSingleTransaction.get(position + 1).getDate())) {
                            ((HolderAmountPaid) holder).tvDatePaid.setVisibility(View.GONE);
                        } else {
                            ((HolderAmountPaid) holder).tvDatePaid.setVisibility(View.VISIBLE);
                            ((HolderAmountPaid) holder).tvDatePaid.setText(listSingleTransaction.get(position).getDate());
                        }
                    } else {
                        ((HolderAmountPaid) holder).tvDatePaid.setVisibility(View.VISIBLE);
                        ((HolderAmountPaid) holder).tvDatePaid.setText(listSingleTransaction.get(position).getDate());

                    }
                } else {
                    ((HolderAmountPaid) holder).tvDatePaid.setVisibility(View.VISIBLE);
                    ((HolderAmountPaid) holder).tvDatePaid.setText(listSingleTransaction.get(position).getDate());

                }


                if (listSingleTransaction.get(position).getMsg().equals("")) {
                    ((HolderAmountPaid) holder).tvRemarksPaid.setVisibility(View.GONE);
                } else {

                    String messageWithoutCamelCase = listSingleTransaction.get(position).getMsg();
                    String messageCamelCase = messageWithoutCamelCase.substring(0,1).toUpperCase()+messageWithoutCamelCase.substring(1);

                    ((HolderAmountPaid) holder).tvRemarksPaid.setText(messageCamelCase);
                }


                break;

            case RECEIVED: {
                ((HolderAmountReceived) holder).tvAmountReceived.setText(listSingleTransaction.get(position).getAmount());
                ((HolderAmountReceived) holder).tvAmountReceivedTime.setText(listSingleTransaction.get(position).getTime());

                if (position<listSingleTransaction.size() && (position+1)<listSingleTransaction.size()) {
                    if (listSingleTransaction.size() > 1) {
                        if (listSingleTransaction.get(position).getDate().equals(listSingleTransaction.get(position + 1).getDate())) {
                            ((HolderAmountReceived) holder).tvDateReceived.setVisibility(View.GONE);
                        } else {
                            ((HolderAmountReceived) holder).tvDateReceived.setVisibility(View.VISIBLE);
                            ((HolderAmountReceived) holder).tvDateReceived.setText(listSingleTransaction.get(position).getDate());
                        }
                    } else {
                        ((HolderAmountReceived) holder).tvDateReceived.setVisibility(View.VISIBLE);
                        ((HolderAmountReceived) holder).tvDateReceived.setText(listSingleTransaction.get(position).getDate());

                    }
                } else {
                    ((HolderAmountReceived) holder).tvDateReceived.setVisibility(View.VISIBLE);
                    ((HolderAmountReceived) holder).tvDateReceived.setText(listSingleTransaction.get(position).getDate());

                }

                if (listSingleTransaction.get(position).getMsg().equals("")) {
                    ((HolderAmountReceived) holder).tvRemarks.setVisibility(View.GONE);
                } else {
                    String messageWithoutCamelCase = listSingleTransaction.get(position).getMsg();
                    String messageCamelCase = messageWithoutCamelCase.substring(0,1).toUpperCase()+messageWithoutCamelCase.substring(1);
                    ((HolderAmountReceived) holder).tvRemarks.setText(messageCamelCase);
                }
                //  ((HolderAmountReceived)holder).tvDateOfTxn.setText(listSingleTransaction.get(position).getDate());
                break;
            }

          /*  case TIME: {
                ((HolderTime) holder).tvDatePaid.setText(listSingleTransaction.get(position).getTime());

            }*/


        }

    }


    @Override
    public int getItemCount() {
        return listSingleTransaction.size();
    }

    public static class HolderAmountReceived extends RecyclerView.ViewHolder {

        private TextView tvAmountReceived, tvRemarksReceived, tvAmountReceivedTime, tvDateOfTxn, tvRemarks,tvDateReceived;

        public HolderAmountReceived(@NonNull View itemView) {
            super(itemView);
            tvAmountReceived = itemView.findViewById(R.id.tvAmountReceived);
            tvAmountReceivedTime = itemView.findViewById(R.id.tvAmountReceivedTime);
            tvRemarks = itemView.findViewById(R.id.tvRemarks);
            tvDateReceived = itemView.findViewById(R.id.tvDateReceived);
            tvDateReceived = itemView.findViewById(R.id.tvDateReceived);
            //  tvDateOfTxn = itemView.findViewById(R.id.tvDateOfTxn);
        }
    }

    public static class HolderAmountPaid extends RecyclerView.ViewHolder {

        private TextView tvAmountSent, tvAmountSentTime, tvRemarksPaid,tvDatePaid;


        public HolderAmountPaid(@NonNull View itemView) {
            super(itemView);

            tvAmountSent = itemView.findViewById(R.id.tvAmountSent);
            tvAmountSentTime = itemView.findViewById(R.id.tvAmountSentTime);
            tvRemarksPaid = itemView.findViewById(R.id.tvRemarksPaid);
            tvDatePaid = itemView.findViewById(R.id.tvDatePaid);
        }
    }


    public static class HolderTime extends RecyclerView.ViewHolder {

        private TextView tvDatePaid;


        public HolderTime(@NonNull View itemView) {
            super(itemView);
            tvDatePaid = itemView.findViewById(R.id.tvDatePaid);
        }
    }


}
