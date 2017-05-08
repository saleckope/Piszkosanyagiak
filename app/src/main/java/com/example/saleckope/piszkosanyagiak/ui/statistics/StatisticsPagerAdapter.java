package com.example.saleckope.piszkosanyagiak.ui.statistics;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.saleckope.piszkosanyagiak.R;

/**
 * Created by saleckope on 2016. 11. 17..
 */

public class StatisticsPagerAdapter extends FragmentPagerAdapter {
    private Context context;

    public StatisticsPagerAdapter(FragmentManager fm, Context context) {
        super(fm);
        this.context = context;
    }

    //Method for set current fragment
    @Override
    public Fragment getItem(int position) {
        Fragment ret = null;
        switch (position) {
            //DetailsFragment is the default
            case 0:
                ret = new StatisticsDetailsFragment();
                break;
            case 1:
                ret = new StatisticsGraphicsFragment();
                break;
        }
        return ret;
    }

    //Method for set page title
    @Override
    public CharSequence getPageTitle(int position) {
        String title;
        switch (position) {
            case 0:
                title = context.getString(R.string.details);
                break;
            case 1:
                title = context.getString(R.string.garphics);
                break;
            default:
                title = "";
        }
        return title;
    }

    @Override
    public int getCount() {
        return 2;
    }
}