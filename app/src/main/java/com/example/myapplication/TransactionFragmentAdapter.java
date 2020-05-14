package com.example.myapplication;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.View;

import java.sql.Struct;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;


public class TransactionFragmentAdapter extends FragmentStatePagerAdapter {

    private List<String> tabTitles;
    private Context context;
    private HashMap<String, String> months;
    private Boolean isChart;

    public TransactionFragmentAdapter(FragmentManager fm, Context context, List<String> tabTitles, Boolean isChart) {
        super(fm);
        this.context = context;
        this.tabTitles = tabTitles;
        this.months = new HashMap<>();
        this.isChart = isChart;

        String[] mnths = {"Jan", "Feb", "March", "April", "May", "June", "July", "August", "Sept", "Oct", "Nov", "Dec"};
        for (int i = 0; i < 12; i++) {
            if (i < 10) {
                this.months.put('0' + Integer.toString(i), mnths[i]);
            } else {

                this.months.put(Integer.toString(i), mnths[i]);
            }
        }

    }

    @Override
    public int getItemPosition(Object object) {
        // POSITION_NONE makes it possible to reload the PagerAdapter
        return POSITION_NONE;
    }

    @Override
    public Fragment getItem(int position) {


        // get the current month
        String[] temp = this.tabTitles.get(position).split("/");
//        temp[0] = this.months.get(temp[0]);
        List<String> tmp = new ArrayList<>();
        // luna ca numar
        tmp.add(temp[0]);
        // luna ca nume
        tmp.add(this.months.get(temp[0]));
        // anul
        tmp.add(temp[1]);
        if (isChart)
            return PieDataFragment.newInstance(position, tmp);
        else
            return TransactionFragment.newInstance(position + 1, tmp);
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return tabTitles.get(position);
    }

    @Override
    public int getCount() {
        return tabTitles.size();
    }
}
