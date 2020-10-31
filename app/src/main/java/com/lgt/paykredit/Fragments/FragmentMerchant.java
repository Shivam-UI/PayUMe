package com.lgt.paykredit.Fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lgt.paykredit.Activities.ActivityAddItems;
import com.lgt.paykredit.Activities.ActivityAddParty;
import com.lgt.paykredit.Activities.ActivityCreateInvoice;
import com.lgt.paykredit.Adapter.AdapterMerchant;
import com.lgt.paykredit.Models.ModelMerchant;
import com.lgt.paykredit.R;

import java.util.ArrayList;
import java.util.List;

public class FragmentMerchant extends Fragment {

    private RecyclerView rvMerchant;
    private AdapterMerchant adapterMerchant;
    private List<ModelMerchant> merchantList;

    private TextView tvAddParty,tvAddItems;

    private LinearLayout llAddInvoice;

    public FragmentMerchant() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_merchants, container, false);

        tvAddParty = view.findViewById(R.id.tvAddParty);
        tvAddItems = view.findViewById(R.id.tvAddItems);

        rvMerchant = view.findViewById(R.id.rvUnPaidInvoice);
        llAddInvoice = view.findViewById(R.id.llAddInvoice);
        llAddInvoice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               //startActivity(new Intent(getActivity(), ActivityInvoice.class));
                startActivity(new Intent(getActivity(), ActivityCreateInvoice.class));

            }
        });

        tvAddParty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), ActivityAddParty.class));
            }
        });

        tvAddItems.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), ActivityAddItems.class));
            }
        });

        loadUnpaidRecyclerView();

        return view;
    }

    private void loadUnpaidRecyclerView() {
        merchantList = new ArrayList<>();
        merchantList.clear();

        merchantList.add(new ModelMerchant("", "# Pay_001", "3", "Ranjan Singh", "12-Mar-20", "12-Mar-20", "150", ""));
        merchantList.add(new ModelMerchant("", "# Pay_002", "4", "Qasim", "13-Mar-20", "12-Mar-20", "100", ""));
        merchantList.add(new ModelMerchant("", "# Pay_003", "2", "Abul Asim", "13-Mar-20", "14-Mar-20", "120", ""));
        merchantList.add(new ModelMerchant("", "# Pay_004", "1", "Logiclump", "14-Mar-20", "14-Mar-20", "130", ""));

        merchantList.add(new ModelMerchant("", "# Pay_001", "3", "Ranjan Singh", "12-Mar-20", "12-Mar-20", "150", ""));
        merchantList.add(new ModelMerchant("", "# Pay_002", "4", "Qasim", "13-Mar-20", "12-Mar-20", "100", ""));
        merchantList.add(new ModelMerchant("", "# Pay_003", "2", "Abul Asim", "13-Mar-20", "14-Mar-20", "120", ""));
        merchantList.add(new ModelMerchant("", "# Pay_004", "1", "Logiclump", "14-Mar-20", "14-Mar-20", "130", ""));


        adapterMerchant = new AdapterMerchant(merchantList, getActivity());
        rvMerchant.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false));
        rvMerchant.hasFixedSize();
        rvMerchant.setNestedScrollingEnabled(false);
        rvMerchant.setAdapter(adapterMerchant);
    }
}
