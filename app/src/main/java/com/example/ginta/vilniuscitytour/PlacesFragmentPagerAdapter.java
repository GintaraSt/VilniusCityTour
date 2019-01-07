package com.example.ginta.vilniuscitytour;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class PlacesFragmentPagerAdapter extends FragmentPagerAdapter {

    private String titles[] = new String[]{"Live", "Food", "Culture", "Events"};

    public PlacesFragmentPagerAdapter(FragmentManager fm){
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0: return new LiveFragment();
            case 1: return new FoodFragment();
            case 2: return new CultureFragment();
            case 3: return new EventFragment();
            default: return null;
        }
    }

    @Override
    public int getCount() {
        return 4;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return titles[position];
    }
}
