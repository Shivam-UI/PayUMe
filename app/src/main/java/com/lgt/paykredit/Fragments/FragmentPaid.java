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

import com.lgt.paykredit.Adapter.AdapterPaid;
import com.lgt.paykredit.Models.ModelPaid;
import com.lgt.paykredit.R;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentPaid extends Fragment {

    private RecyclerView rvPaid;
    private List<ModelPaid> listPaid;
    private AdapterPaid adapterPaid;

    public FragmentPaid() {
    }

    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            CharSequence message = intent.getCharSequenceExtra("INPUT_DATA1");
            if (message != null) {
                if (adapterPaid != null) {
                    adapterPaid.getFilter().filter(message.toString());
                }
            }
            Log.e("DASdsadsadttttttt", message + "");
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
        View view = inflater.inflate(R.layout.fragment_paid, container, false);

        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(mMessageReceiver,
                new IntentFilter("FILTER_EVENT1"));

        rvPaid = view.findViewById(R.id.rvPaid);

        loadRecyclerView();
        return view;
    }

    private void loadRecyclerView() {

        listPaid = new ArrayList<>();
        listPaid.clear();

        listPaid.add(new ModelPaid("# Pay_001","12-03-20","Ranjan Singh","100","Done"));
        listPaid.add(new ModelPaid("# Pay_001","12-03-20","Ramesh Kumar","100","Done"));
        listPaid.add(new ModelPaid("# Pay_001","12-03-20","Hari SIngh","100","Done"));
        listPaid.add(new ModelPaid("# Pay_001","12-03-20","Qasim","100","Done"));
        listPaid.add(new ModelPaid("# Pay_001","12-03-20","Ramu Singh","100","Done"));
        listPaid.add(new ModelPaid("# Pay_001","12-03-20","Ranu Singh","100","Done"));

        listPaid.add(new ModelPaid("# Pay_001","12-03-20","Jawala","100","Done"));
        listPaid.add(new ModelPaid("# Pay_001","12-03-20","Batra","100","Done"));
        listPaid.add(new ModelPaid("# Pay_001","12-03-20","Sona","100","Done"));
        listPaid.add(new ModelPaid("# Pay_001","12-03-20","Dalton","100","Done"));
        listPaid.add(new ModelPaid("# Pay_001","12-03-20","Raushan","100","Done"));
        listPaid.add(new ModelPaid("# Pay_001","12-03-20","Hari Om","100","Done"));

        rvPaid.hasFixedSize();
        rvPaid.setNestedScrollingEnabled(false);
        rvPaid.setLayoutManager(new LinearLayoutManager(getActivity(),RecyclerView.VERTICAL,false));
        adapterPaid = new AdapterPaid(listPaid,getActivity());
        rvPaid.setAdapter(adapterPaid);
    }
}
