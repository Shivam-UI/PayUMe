package com.lgt.paykredit.Adapter;

import android.content.Context;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.lgt.paykredit.Models.ModelSelectLanguage;
import com.lgt.paykredit.R;
import com.lgt.paykredit.extras.Common;

import java.util.List;

/**
 * Created by Ranjan on 3/16/2020.
 */
public class AdapterSelectLanguage extends RecyclerView.Adapter<AdapterSelectLanguage.HolderLanguage> {

    private List<ModelSelectLanguage> list;
    private Context context;
    private int current_position=0;

    public AdapterSelectLanguage(List<ModelSelectLanguage> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public HolderLanguage onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_select_language,parent,false);
        return new HolderLanguage(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HolderLanguage holder, final int position) {

        holder.tvLanguageName.setText(list.get(position).getLanguageName());
        holder.llSelectLanguageBG.setBackgroundResource(list.get(position).getBgColor());
        holder.tvLanguageStartsWith.setText(String.valueOf(list.get(position).getLanguageName().charAt(0)));

        if (current_position==position){
            holder.iv_select_language.setVisibility(View.VISIBLE);
        }else {
            holder.iv_select_language.setVisibility(View.GONE);

        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                current_position=position;
                notifyDataSetChanged();
                if (list.get(position).getLanguageId().equalsIgnoreCase("1")){
                    Common.saveLanguage(context,Common.ENGLISH);
                }
                if (list.get(position).getLanguageId().equalsIgnoreCase("2")){
                    Common.saveLanguage(context,Common.HINDI);
                }
            }
        });




        if (list.get(current_position).getLanguageId().equalsIgnoreCase("1")){
            Common.saveLanguage(context,Common.ENGLISH);
        }
        if (list.get(current_position).getLanguageId().equalsIgnoreCase("2")){
            Common.saveLanguage(context,Common.HINDI);
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class HolderLanguage extends RecyclerView.ViewHolder {

        private TextView tvLanguageName,tvLanguageStartsWith;
        private LinearLayout llSelectLanguageBG;
        private ImageView iv_select_language;

        public HolderLanguage(@NonNull View itemView) {
            super(itemView);
            tvLanguageName = itemView.findViewById(R.id.tvLanguageName);
            llSelectLanguageBG = itemView.findViewById(R.id.llSelectLanguageBG);
            tvLanguageStartsWith = itemView.findViewById(R.id.tvLanguageStartsWith);
            iv_select_language = itemView.findViewById(R.id.iv_select_language);

        }
    }

}
