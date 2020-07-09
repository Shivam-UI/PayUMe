package com.lgt.paykredit.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.lgt.paykredit.Models.ModelBusiness;
import com.lgt.paykredit.R;

import java.util.List;

public class AdapterBusiness extends BaseAdapter {
    Context context;
    List<ModelBusiness> listBusiness;
    LayoutInflater inflter;

    public AdapterBusiness(Context applicationContext, List<ModelBusiness> listBusiness) {
        this.context = applicationContext;
        this.listBusiness = listBusiness;
        inflter = (LayoutInflater.from(applicationContext));
    }

    @Override
    public int getCount() {
        return listBusiness.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = inflter.inflate(R.layout.custom_spinner_items, null);
        ImageView icon = view.findViewById(R.id.spinner_iv);
        TextView names = view.findViewById(R.id.spinner_tv);
        Glide.with(context).load(listBusiness.get(i).getImage()).diskCacheStrategy(DiskCacheStrategy.ALL).into(icon);
        names.setText(listBusiness.get(i).getName());
        return view;
    }
}
