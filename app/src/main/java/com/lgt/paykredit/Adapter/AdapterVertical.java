package com.lgt.paykredit.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.lgt.paykredit.Models.ModelSingleUserTransaction;
import com.lgt.paykredit.Models.ModelVertical;
import com.lgt.paykredit.R;

import java.util.List;

public class AdapterVertical extends RecyclerView.Adapter<AdapterVertical.HolderVertical> {

    private Context context;
    private List<ModelVertical> list;

    public AdapterVertical(Context context, List<ModelVertical> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public HolderVertical onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_vertical, parent, false);
        return new HolderVertical(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HolderVertical holder, int position) {

        final String sectionName = list.get(position).getDate();
        List<ModelSingleUserTransaction> singleSectionItems = list.get(position).getList();

        holder.tvDateVertical.setText(sectionName);

        AdapterSingleUserTransaction itemListDataAdapter = new AdapterSingleUserTransaction(singleSectionItems, context);
        holder.rvVertical.setHasFixedSize(true);
        holder.rvVertical.setNestedScrollingEnabled(false);
        holder.rvVertical.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
        holder.rvVertical.setAdapter(itemListDataAdapter);
        Log.e("abcdefg",singleSectionItems.size()+"");

        itemListDataAdapter.notifyDataSetChanged();

      /*  LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        linearLayoutManager.setStackFromEnd(true);
        linearLayoutManager.setReverseLayout(true);
        holder.rvVertical.setLayoutManager(linearLayoutManager);
        holder.rvVertical.hasFixedSize();
        holder.rvVertical.setNestedScrollingEnabled(false);
        holder.rvVertical.setAdapter(itemListDataAdapter);*/

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class HolderVertical extends RecyclerView.ViewHolder {
        private TextView tvDateVertical;
        private RecyclerView rvVertical;

        public HolderVertical(@NonNull View itemView) {
            super(itemView);
            rvVertical = itemView.findViewById(R.id.rvVertical);
            tvDateVertical = itemView.findViewById(R.id.tvDateVertical);
        }
    }

}
