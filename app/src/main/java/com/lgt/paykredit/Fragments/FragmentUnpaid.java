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

import com.lgt.paykredit.Adapter.AdapterUnpaid;
import com.lgt.paykredit.Models.ModelUnPaid;
import com.lgt.paykredit.R;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentUnpaid extends Fragment {

    private RecyclerView rvUnPaid;
    private List<ModelUnPaid> listUnPaid;

    private AdapterUnpaid adapterUnpaid;


    public FragmentUnpaid() {
    }

    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            CharSequence message = intent.getCharSequenceExtra("INPUT_DATA2");
            if (message != null) {
                if (adapterUnpaid != null) {
                    adapterUnpaid.getFilter().filter(message.toString());
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
        View view = inflater.inflate(R.layout.fragment_unpaid, container, false);

        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(mMessageReceiver,
                new IntentFilter("FILTER_EVENT2"));

        rvUnPaid = view.findViewById(R.id.rvUnPaid);
        loadRecyclerView();
        return view;
    }

    private void loadRecyclerView() {

        listUnPaid = new ArrayList<>();
        listUnPaid.clear();


        listUnPaid.add(new ModelUnPaid("# Pay_001","12-03-20","Rakesh","120","Done"));
        listUnPaid.add(new ModelUnPaid("# Pay_001","12-03-20","Bhavesh","120","Done"));
        listUnPaid.add(new ModelUnPaid("# Pay_001","12-03-20","Javed","120","Done"));
        listUnPaid.add(new ModelUnPaid("# Pay_001","12-03-20","Savita","120","Done"));

        listUnPaid.add(new ModelUnPaid("# Pay_001","12-03-20","Lalita","120","Done"));
        listUnPaid.add(new ModelUnPaid("# Pay_001","12-03-20","Kavita","120","Done"));
        listUnPaid.add(new ModelUnPaid("# Pay_001","12-03-20","Babita","120","Done"));
        listUnPaid.add(new ModelUnPaid("# Pay_001","12-03-20","Babi","120","Done"));

        listUnPaid.add(new ModelUnPaid("# Pay_001","12-03-20","Ravi","120","Done"));
        listUnPaid.add(new ModelUnPaid("# Pay_001","12-03-20","Sinnu","120","Done"));

        rvUnPaid.hasFixedSize();
        rvUnPaid.setNestedScrollingEnabled(false);
        rvUnPaid.setLayoutManager(new LinearLayoutManager(getActivity(),RecyclerView.VERTICAL,false));
        adapterUnpaid = new AdapterUnpaid(listUnPaid,getActivity());
        rvUnPaid.setAdapter(adapterUnpaid);


    }
}
