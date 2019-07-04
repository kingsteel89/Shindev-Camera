package com.shindev.huaweicamera.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.shindev.huaweicamera.fragment.MainFragment;
import com.shindev.huaweicamera.fragment.MenuFragment;
import com.shindev.huaweicamera.fragment.SettingFragment;

public class PaperAdapter extends FragmentPagerAdapter {

    private static int NUM_ITEMS = 3;

    public PaperAdapter(FragmentManager fragmentManager) {
        super(fragmentManager);
    }

    @Override
    public Fragment getItem(int i) {
        switch (i) {
            case 0:
                return new MenuFragment();
            case 1: // Fragment # 0 - This will show FirstFragment different title
                return new MainFragment();
            case 2: // Fragment # 1 - This will show SecondFragment
                return new SettingFragment();
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return NUM_ITEMS;
    }
}
