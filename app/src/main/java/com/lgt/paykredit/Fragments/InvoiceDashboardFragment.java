package com.lgt.paykredit.Fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.lgt.paykredit.Activities.ActivityDefaultInvoice;
import com.lgt.paykredit.Activities.CreateInvoice;
import com.lgt.paykredit.Activities.InvoiceMainPage;
import com.lgt.paykredit.R;

import static com.lgt.paykredit.Activities.MainActivity.tv_common_toolbar;
import static com.lgt.paykredit.Activities.MainActivity.tv_creditBook_actionBar;

public class InvoiceDashboardFragment extends Fragment {
    LinearLayout ll_create_invoice,ll_my_invoice,ll_kyc,ll_my_defaulter,ll_udhar_khata,ll_manage_employee;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View mView = inflater.inflate(R.layout.new_invoice_dashboard,container,false);
        // init
        initView(mView);
        return mView;
    }

    private void initView(View mView) {
        ll_create_invoice = mView.findViewById(R.id.ll_create_invoice);
        ll_my_invoice = mView.findViewById(R.id.ll_my_invoice);
        ll_kyc = mView.findViewById(R.id.ll_kyc);
        ll_my_defaulter = mView.findViewById(R.id.ll_my_defaulter);
        ll_udhar_khata = mView.findViewById(R.id.ll_udhar_khata);
        ll_manage_employee = mView.findViewById(R.id.ll_manage_employee);
        ll_create_invoice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent =new Intent(getActivity(), CreateInvoice.class);
                startActivity(intent);
            }
        });
        ll_my_invoice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), InvoiceMainPage.class));
            }
        });
        ll_kyc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getContext(), "Coming Soon", Toast.LENGTH_SHORT).show();
            }
        });

        ll_my_defaulter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), ActivityDefaultInvoice.class));
            }
        });

        ll_udhar_khata.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frameHomeScreen,
                        new FragmentCreditBook()).addToBackStack("invoice").commit();
                tv_common_toolbar.setVisibility(View.GONE);
                tv_creditBook_actionBar.setVisibility(View.VISIBLE);
            }
        });
        ll_manage_employee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getContext(), "Coming Soon", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void replaceFragment(Fragment mSelectedFragment, boolean shouldAddToBackStack, String mBackStackName, String tagName) {
        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frameHomeScreen, mSelectedFragment, tagName).addToBackStack(mBackStackName).commit();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        Log.d("added","onAttach");
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d("added","onResume");
    }
}
