package com.example.moneyapp.Adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.example.moneyapp.Fragment.TransAddingTabFragment1;
import com.example.moneyapp.Fragment.TransAddingTabFragment2;

public class TransAddingPagerAdapter extends FragmentStatePagerAdapter {

    private int mNumOfTabs;

    public TransAddingPagerAdapter(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
    }

    /**
     * Return the Fragment associated with a specified position.
     *
     * @param position
     */
    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return new TransAddingTabFragment1();
            case 1:
                return new TransAddingTabFragment2();
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
