package com.lgt.paykredit.Adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.lgt.paykredit.Fragments.FragmentPaid;
import com.lgt.paykredit.Fragments.FragmentUnpaid;

/**
 * Created by Ranjan on 3/20/2020.
 */
public class PagerAdapterPaidUnpaid extends FragmentPagerAdapter {
    public int numOfTabs;


    public PagerAdapterPaidUnpaid(@NonNull FragmentManager fm, int numOfTabs) {
        super(fm);
        this.numOfTabs = numOfTabs;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new FragmentPaid();
            case 1:
                return new FragmentUnpaid();

        }
        return null;
    }

    @Override
    public int getCount() {
        return numOfTabs;
    }
}


