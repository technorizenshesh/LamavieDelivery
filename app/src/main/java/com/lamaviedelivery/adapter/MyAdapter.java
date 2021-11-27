package com.lamaviedelivery.adapter;

import android.content.Context;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.lamaviedelivery.fragment.AllFragment;
import com.lamaviedelivery.fragment.CompleteFragment;
import com.lamaviedelivery.fragment.InProgressFragment;
import com.lamaviedelivery.fragment.NewFragment;


public class MyAdapter extends FragmentPagerAdapter {
    Context context;
    int totalTabs;
    public MyAdapter(Context c, FragmentManager fm, int totalTabs) {
        super(fm);
        context = c;
        this.totalTabs = totalTabs;
    }
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                AllFragment allFragment = new AllFragment();
                return allFragment;
            case 1:
                NewFragment newFragment = new NewFragment();
                return newFragment;

            case 2:
                InProgressFragment progressFragment = new InProgressFragment();
                return progressFragment;
            case 3:
                CompleteFragment completedFragment = new CompleteFragment();
                return completedFragment;

            default:
                return null;
        }
    }
    @Override
    public int getCount() {
        return totalTabs;
    }
}
