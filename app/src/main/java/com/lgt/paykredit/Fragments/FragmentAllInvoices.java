package com.lgt.paykredit.Fragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.lgt.paykredit.Activities.ActivityCreateInvoice;
import com.lgt.paykredit.Adapter.AdapterAllInvoices;
import com.lgt.paykredit.Models.ModelAllInvoices;
import com.lgt.paykredit.R;

import java.util.ArrayList;
import java.util.List;


public class FragmentAllInvoices extends Fragment {

    private static final String TAG = "FragmentAllInvoices";
    private RecyclerView rvAllInvoices;
    private List<ModelAllInvoices> listAllInvoices;
    private AdapterAllInvoices adapterAllInvoices;

    private LinearLayout llAddInvoice_Paid;

    private CharSequence aaa;

    public FragmentAllInvoices() {
    }

    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            CharSequence message = intent.getCharSequenceExtra("INPUT_DATA");
            if (message != null) {
                if (adapterAllInvoices != null) {
                    adapterAllInvoices.getFilter().filter(message.toString());
                }
            }
            Log.e("DASdsadsad", message + "");
        }
    };

    @Override
    public void onDetach() {
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(mMessageReceiver);
        super.onDetach();
    }

    @Override
    public void onDestroy() {
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(mMessageReceiver);

        super.onDestroy();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_all_invoices, container, false);

        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(mMessageReceiver,
                new IntentFilter("FILTER_EVENT"));


        rvAllInvoices = view.findViewById(R.id.rvPaidInvoice);
        llAddInvoice_Paid = view.findViewById(R.id.llAddInvoice_Paid);

        llAddInvoice_Paid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), ActivityCreateInvoice.class));
            }
        });
        loadPaidRecyclerView();


        return view;
    }


    private void loadPaidRecyclerView() {

        listAllInvoices = new ArrayList<>();
        listAllInvoices.clear();

        listAllInvoices.add(new ModelAllInvoices("Pay_001", "Done", "", "Ranjan Singh", "100", "18-03-20"));
        listAllInvoices.add(new ModelAllInvoices("Pay_002", "Done", "", "Raushan Singh", "120", "16-03-20"));
        listAllInvoices.add(new ModelAllInvoices("Pay_003", "Partially Done", "", "Amar Singh", "10", "12-03-20"));
        listAllInvoices.add(new ModelAllInvoices("Pay_004", "Done", "", "Samar Singh", "140", "19-03-20"));
        listAllInvoices.add(new ModelAllInvoices("Pay_005", "Done", "", "Dummer Singh", "150", "14-03-20"));
        listAllInvoices.add(new ModelAllInvoices("Pay_006", "Done", "", "Kajal Singh", "180", "12-03-20"));

        adapterAllInvoices = new AdapterAllInvoices(listAllInvoices, getActivity());
        rvAllInvoices.hasFixedSize();
        rvAllInvoices.setNestedScrollingEnabled(false);
        rvAllInvoices.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false));
        rvAllInvoices.setAdapter(adapterAllInvoices);

    }


}
