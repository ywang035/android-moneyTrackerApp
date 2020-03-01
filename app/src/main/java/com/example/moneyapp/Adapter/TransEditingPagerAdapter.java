package com.example.moneyapp.Adapter;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.example.moneyapp.Fragment.TransEditingTabFragment1;
import com.example.moneyapp.Fragment.TransEditingTabFragment2;

public class TransEditingPagerAdapter extends FragmentStatePagerAdapter {

    private int mNumOfTabs;
    private String defaultCategory;

    public TransEditingPagerAdapter(FragmentManager fm, int NumOfTabs, String defaultCategory) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
        this.defaultCategory = defaultCategory;
    }

    public void setter(String defaultCategory){
        this.defaultCategory = defaultCategory;
    }

    /**
     * Return the Fragment associated with a specified position.
     *
     * @param position
     */
    @Override
    public Fragment getItem(int position) {

        Bundle bundle = new Bundle();
        bundle.putString("default_category", defaultCategory);

        switch (position){
            case 0:

                // set Fragmentclass Arguments
                TransEditingTabFragment1 fragobj1 = new TransEditingTabFragment1();
                fragobj1.setArguments(bundle);

                return  fragobj1;
//                return new TransEditingTabFragment1();
            case 1:

                // set Fragmentclass Arguments
                TransEditingTabFragment2 fragobj2 = new TransEditingTabFragment2();
                fragobj2.setArguments(bundle);

                return fragobj2;

//                return new TransEditingTabFragment2();
            default: return null;
        }
    }


    /**
     * Return the number of views available.
     */
    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}
