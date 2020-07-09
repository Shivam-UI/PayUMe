package com.lgt.paykredit.Fragments;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lgt.paykredit.R;


public class FragmentInvoices extends Fragment {


    public FragmentInvoices() { }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_invoices, container, false);
        return view;
    }

}
