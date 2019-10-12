package com.krimzon.scuffedbots.raka3at.Dates;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.krimzon.scuffedbots.raka3at.force;

public class DatesCollectionAdapter extends FragmentStatePagerAdapter {
    public DatesCollectionAdapter(FragmentManager fm) { super(fm); }

    @Override
    public Fragment getItem(int i) {
        Dates date = new Dates();
        Bundle bundle = new Bundle();
        if(i==0)
            bundle.putString("date", force.datin);
        else if(i==1)
            bundle.putString("date", force.hijri);
        date.setArguments(bundle);
        return date;
    }

    @Override
    public int getCount() { return 2; }
}
