package com.project.nikhil.knock_sense;

import android.app.FragmentManager;
import android.os.Bundle;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by nikhil on 7/10/17.
 */

public class tablayout_restaurant_adapter extends FragmentPagerAdapter {

    restaurant_object obj;
    private String tabTitles[] = new String[] { "Info", "Offers", "Reviews"};

    public tablayout_restaurant_adapter(android.support.v4.app.FragmentManager fm,restaurant_object object) {
        super(fm);
        obj=object;
    }

    @Override
    public android.support.v4.app.Fragment getItem(int position) {
        if (position == 0) {
            restaurant_info fragment=new restaurant_info();
            Bundle bundle = new Bundle();
            bundle.putSerializable("params", obj);
            fragment.setArguments(bundle);
            return fragment;
        } else if (position == 1){
            restaurant_offers fragment=new restaurant_offers();
            Bundle bundle = new Bundle();
            bundle.putSerializable("params", obj);
            fragment.setArguments(bundle);
            return fragment;
        } else{
            restaurant_review fragment=new restaurant_review();
            Bundle bundle = new Bundle();
            bundle.putSerializable("params", obj);
            fragment.setArguments(bundle);
            return fragment;
        }
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        // Generate title based on item position
        return tabTitles[position];
    }

}