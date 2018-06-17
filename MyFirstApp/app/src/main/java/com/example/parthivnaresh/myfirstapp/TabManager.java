package com.example.parthivnaresh.myfirstapp;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.example.parthivnaresh.myfirstapp.MyList.Tab_MyList;
import com.example.parthivnaresh.myfirstapp.SearchFragment.Tab_Search;

public class TabManager extends FragmentStatePagerAdapter {

    int mNumOfTabs;
    public TabManager(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new Tab_MyList();
            case 1:
                return new Tab_Alerts();
            case 2:
                return new Tab_Search();
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }


}
