/*
 * created by Andrew K. <rembozebest@gmail.com> on 18.05.20 19:13
 * copyright (c) 2020
 * last modified 18.05.20 19:13 with ❤
 */

package com.add.finance;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class pagerAdapter extends FragmentPagerAdapter {
    private static int NUM_ITEMS = 2;

    public pagerAdapter(FragmentManager fragmentManager) {
        super(fragmentManager);
    }

    // Returns total number of pages
    @Override
    public int getCount() {
        return NUM_ITEMS;
    }

    // Returns the fragment to display for that page
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0: // Fragment # 0 - This will show FirstFragment
                return new fragment_spend();
            case 1: // Fragment # 0 - This will show FirstFragment different title
                return  new adventFragment();

            default:
                return null;
        }
    }

    // Returns the page title for the top indicator
    @Override
    public CharSequence getPageTitle(int position) {
        switch(position) {
            case 0:
                return "Расходы";

            case 1:
                return "Доходы";

            default:
                return null;
        }
    }

}




